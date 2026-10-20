using System.Globalization;
using System.Net;
using System.Net.Http.Headers;
using System.Reflection;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Text;
using System.Text.Json;
using System.Text.Json.Serialization.Metadata;
using System.Text.RegularExpressions;

namespace PhoenixPerps;

/// <summary>The options of a client.</summary>
public sealed record PhoenixPerpsClientOptions
{
    /// <summary>The key (else PHOENIXPERPS_API_KEY; none: the public calls only, markets and status).</summary>
    public string? ApiKey { get; init; }

    /// <summary>live or sandbox, in any case (else the key's own, else PHOENIXPERPS_ENVIRONMENT).</summary>
    public string? Environment { get; init; }

    /// <summary>The API host (else PHOENIXPERPS_BASE_URL, else the environment's); the REST address with /v1 works too.</summary>
    public string? BaseUrl { get; init; }

    /// <summary>The time one attempt may take (30 s).</summary>
    public TimeSpan Timeout { get; init; } = TimeSpan.FromSeconds(30);

    /// <summary>How often a safe call is tried again (2).</summary>
    public int MaxRetries { get; init; } = 2;

    /// <summary>Your app's name and version, added to the User-Agent.</summary>
    public string? AppInfo { get; init; }

    /// <summary>The HttpClient the calls go through; its own Timeout caps every attempt too, so keep it infinite or above
    /// Timeout.</summary>
    public HttpClient? HttpClient { get; init; }

    /// <summary>Receives a line per retry and one per deprecated API version.</summary>
    public Action<string, string>? Logger { get; init; }

    /// <summary>The account stream URL (else the environment's).</summary>
    public string? AccountStreamUrl { get; init; }

    /// <summary>The market stream URL.</summary>
    public string? MarketStreamUrl { get; init; }
}

// Scope: the operation's x-pp-scope (read, trade, public), the rate-limit budget it draws from (null: one of its own)
internal sealed record Operation(string Id, string Method, string Path, bool Idempotent, bool RetrySafe, string? Cursor, string? Scope = null);

/// <summary>
/// The Phoenix Perpetuals API (docs/BEHAVIOUR.md): every REST call, the account stream and market data, with retries,
/// idempotency keys, paging and typed errors. Make one and share it: it is safe for concurrent use.
/// </summary>
public sealed partial class PhoenixPerpsClient : IDisposable
{
    private static readonly Regex Prefix = new("^(ppk|ppo)_(live|test)_", RegexOptions.CultureInvariant);
    private static readonly TimeSpan RetryAfterMax = TimeSpan.FromSeconds(60);
    private static Operation[]? _known;

    // the OS as the other SDKs name it (OSDescription starts with a vendor: "Microsoft Windows 10.0.26200")
    private static readonly string Os = OperatingSystem.IsWindows() ? "windows" : OperatingSystem.IsMacOS() ? "darwin" : OperatingSystem.IsLinux() ? "linux"
        : OperatingSystem.IsFreeBSD() ? "freebsd" : RuntimeInformation.OSDescription.Split(' ')[0].ToLowerInvariant();
    private readonly string? _key;
    private readonly HttpClient _http;
    private readonly bool _ownsHttp;
    private readonly Action<string, string>? _logger;
    private readonly object _lock = new();
    private readonly HashSet<string> _warned = new();
    private readonly Dictionary<string, RateLimit> _budgets = new();
    private RateLimit? _rateLimit;

    /// <summary>A client; a key of the other environment fails here, before any request. Without a key the public calls
    /// (markets, status) work, any other answers the API's missing_api_key.</summary>
    public PhoenixPerpsClient(PhoenixPerpsClientOptions? options = null)
    {
        var o = options ?? new PhoenixPerpsClientOptions();
        var key = o.ApiKey ?? System.Environment.GetEnvironmentVariable("PHOENIXPERPS_API_KEY");
        if (string.IsNullOrEmpty(key)) key = null;
        var m = key is null ? Match.Empty : Prefix.Match(key);
        string? inferred = m.Success ? (m.Groups[2].Value == "live" ? "live" : "sandbox") : null;
        var env = (o.Environment ?? inferred ?? System.Environment.GetEnvironmentVariable("PHOENIXPERPS_ENVIRONMENT") ?? "sandbox").ToLowerInvariant();
        if (env != "live" && env != "sandbox") throw new ValidationException("invalid_environment", "The environment is live or sandbox.");
        if (inferred is not null && inferred != env) throw new AuthenticationException("wrong_environment", $"This key is a {inferred} key, not a {env} one.");
        _key = key;
        Environment = env;
        // the host: every path already starts with /v1, so the REST address the docs show (host/v1) loses it here
        var baseUrl = (o.BaseUrl ?? System.Environment.GetEnvironmentVariable("PHOENIXPERPS_BASE_URL") ?? SdkInfo.Servers[env]).TrimEnd('/');
        BaseUrl = baseUrl.EndsWith("/v1", StringComparison.OrdinalIgnoreCase) ? baseUrl[..^3] : baseUrl;
        Timeout = o.Timeout;
        MaxRetries = o.MaxRetries;
        AppInfo = o.AppInfo;
        _ownsHttp = o.HttpClient is null;
        _http = o.HttpClient ?? new HttpClient { Timeout = System.Threading.Timeout.InfiniteTimeSpan };
        _logger = o.Logger;
        AccountStreamUrl = o.AccountStreamUrl ?? SdkInfo.AccountStreams[env];
        MarketStreamUrl = o.MarketStreamUrl ?? SdkInfo.MarketStream;
        InitResources();
    }

    /// <summary>live or sandbox.</summary>
    public string Environment { get; }

    /// <summary>The API host the calls go to.</summary>
    public string BaseUrl { get; }

    /// <summary>The time one attempt may take.</summary>
    public TimeSpan Timeout { get; }

    /// <summary>How often a safe call is tried again.</summary>
    public int MaxRetries { get; }

    /// <summary>Added to the User-Agent.</summary>
    public string? AppInfo { get; }

    internal string AccountStreamUrl { get; }

    internal string MarketStreamUrl { get; }

    internal string? Key => _key;

    /// <summary>The key with its secret hidden (ppk_test_****abcd; empty without a key).</summary>
    public string MaskedKey => _key is null ? "" : Mask(_key);

    /// <summary>What the last answer said of its rate-limit window (null before the first answer).</summary>
    public RateLimit? RateLimit
    {
        get { lock (_lock) return _rateLimit; }
    }

    /// <summary>What every request says of the SDK.</summary>
    public string UserAgent =>
        $"phoenixperps-csharp/{SdkInfo.Version} (.NET {System.Environment.Version}; {Os}) spec/{SdkInfo.SpecVersion}{(AppInfo is null ? "" : " " + AppInfo)}";

    /// <summary>Never shows the key.</summary>
    public override string ToString() => $"PhoenixPerpsClient {{ Environment = {Environment}, BaseUrl = {BaseUrl}, ApiKey = {(_key is null ? "none" : MaskedKey)} }}";

    /// <inheritdoc />
    public void Dispose()
    {
        if (_ownsHttp) _http.Dispose();
    }

    internal static string Mask(string key)
    {
        var i = key.IndexOf('_', Math.Min(4, key.Length));
        var head = i >= 0 ? key[..(i + 1)] : "";
        return head + "****" + (key.Length > 4 ? key[^4..] : key);
    }

    internal void Log(string level, string message)
    {
        _logger?.Invoke(level, message);
    }

    // X-RateLimit-* are kept per budget: an operation's x-pp-scope (read, trade, public); a route this SDK does not know
    // keeps a window of its own
    private async Task PreWaitAsync(string budget, CancellationToken ct)
    {
        RateLimit? r;
        lock (_lock) _budgets.TryGetValue(budget, out r);
        if (r is null || r.Remaining > 0 || r.ResetAt is null) return;
        var wait = r.ResetAt.Value - DateTimeOffset.UtcNow;
        if (wait > TimeSpan.Zero && wait < TimeSpan.FromMinutes(1)) await Task.Delay(wait, ct).ConfigureAwait(false);
    }

    private void Remember(HttpResponseHeaders h, string budget)
    {
        if (!h.TryGetValues("X-RateLimit-Limit", out var lv)) return;
        int.TryParse(lv.FirstOrDefault(), NumberStyles.Integer, CultureInfo.InvariantCulture, out var limit);
        var remaining = h.TryGetValues("X-RateLimit-Remaining", out var rv) && int.TryParse(rv.FirstOrDefault(), NumberStyles.Integer, CultureInfo.InvariantCulture, out var rem) ? rem : 0;
        DateTimeOffset? resetAt = null;
        if (h.TryGetValues("X-RateLimit-Reset", out var sv) && double.TryParse(sv.FirstOrDefault(), NumberStyles.Float, CultureInfo.InvariantCulture, out var reset))
        {
            // Reset as epoch seconds (above a year in seconds) or as seconds from now
            resetAt = reset > 31536000 ? DateTimeOffset.FromUnixTimeMilliseconds((long)(reset * 1000)) : DateTimeOffset.UtcNow.AddSeconds(reset);
        }
        string? dep = h.TryGetValues("Deprecation", out var dv) ? dv.FirstOrDefault() : null;
        bool first;
        lock (_lock)
        {
            _rateLimit = _budgets[budget] = new RateLimit(limit, remaining, resetAt);
            first = dep is not null && _warned.Add(dep);
        }
        if (first) Log("warn", $"phoenixperps: this API version is deprecated (Deprecation: {dep})");
    }

    private string Url(Operation op, string[]? path, List<KeyValuePair<string, string>>? query)
    {
        var sb = new StringBuilder();
        var p = op.Path;
        var n = 0;
        for (var i = 0; i < p.Length; i++)
        {
            if (p[i] != '{')
            {
                sb.Append(p[i]);
                continue;
            }
            var end = p.IndexOf('}', i);
            var v = path is not null && n < path.Length ? path[n++] : null;
            if (string.IsNullOrEmpty(v)) throw new ValidationException("invalid_request", $"{p.Substring(i + 1, end - i - 1)} is required.");
            sb.Append(Uri.EscapeDataString(v));
            i = end;
        }
        var url = BaseUrl + sb;
        if (query is { Count: > 0 }) url += "?" + string.Join("&", query.Select(kv => Uri.EscapeDataString(kv.Key) + "=" + Uri.EscapeDataString(kv.Value)));
        return url;
    }

    /// <summary>
    /// Any request to the API (a route newer than this SDK) with the client's key, retries and errors. path is the API
    /// path (/v1/...), body the JSON to send. A GET, a route the spec marks retry-safe, or a call with
    /// options.IdempotencyKey is tried again when that is safe. A 2xx (or 304) answer comes as it came (read its Body);
    /// any other is a PhoenixPerpsException.
    /// </summary>
    public async Task<RawResponse> SendAsync(string method, string path, IEnumerable<KeyValuePair<string, string>>? query = null, string? body = null, RequestOptions? options = null, CancellationToken cancellationToken = default)
    {
        if (!path.StartsWith('/')) throw new ValidationException("invalid_request", "The path starts with /.");
        var m = method.ToUpperInvariant();
        var known = Route(m, path);
        var op = new Operation("send", m, path, false, m == "GET" || known?.RetrySafe == true, null, known?.Scope);
        return await SendAsync(op, null, query?.ToList(), body is null ? null : Encoding.UTF8.GetBytes(body), options, cancellationToken).ConfigureAwait(false);
    }

    // the spec's operation a path is one of (its {parameters} any segment), for its retry safety and its budget
    private static Operation? Route(string method, string path)
    {
        _known ??= typeof(Ops).GetFields(BindingFlags.Static | BindingFlags.NonPublic).Select(f => f.GetValue(null)).OfType<Operation>().ToArray();
        var parts = path.Split('/');
        return _known.FirstOrDefault(o => o.Method == method && o.Path.Split('/') is var t && t.Length == parts.Length
            && t.Zip(parts).All(s => s.First.StartsWith('{') ? s.Second.Length > 0 : s.First == s.Second));
    }

    /// <summary>One logical call: its attempts, and its answer.</summary>
    internal async Task<RawResponse> SendAsync(Operation op, string[]? path, List<KeyValuePair<string, string>>? query, byte[]? body, RequestOptions? options, CancellationToken ct)
    {
        var key = options?.IdempotencyKey ?? (op.Idempotent ? Guid.NewGuid().ToString() : null);
        // safe to send again: a read, a route the spec marks retry-safe, or a call carrying its Idempotency-Key
        var retryable = op.RetrySafe || key is not null;
        var max = options?.MaxRetries ?? MaxRetries;
        var timeout = options?.Timeout ?? Timeout;
        var budget = op.Scope ?? "";
        var url = Url(op, path, query);
        for (var attempt = 0; ; attempt++)
        {
            await PreWaitAsync(budget, ct).ConfigureAwait(false);
            HttpResponseMessage? res = null;
            byte[] bytes = Array.Empty<byte>();
            PhoenixPerpsException? failure = null;
            using (var cts = CancellationTokenSource.CreateLinkedTokenSource(ct))
            {
                cts.CancelAfter(timeout);
                using var req = new HttpRequestMessage(new HttpMethod(op.Method), url);
                if (_key is not null) req.Headers.TryAddWithoutValidation("Authorization", "Bearer " + _key);
                req.Headers.TryAddWithoutValidation("Accept", "application/json");
                req.Headers.TryAddWithoutValidation("User-Agent", UserAgent);
                if (key is not null) req.Headers.TryAddWithoutValidation("Idempotency-Key", key);
                if (options?.Headers is { } extra)
                    foreach (var (k, v) in extra) req.Headers.TryAddWithoutValidation(k, v);
                if (body is not null) req.Content = new ByteArrayContent(body) { Headers = { ContentType = new MediaTypeHeaderValue("application/json") } };
                try
                {
                    res = await _http.SendAsync(req, HttpCompletionOption.ResponseContentRead, cts.Token).ConfigureAwait(false);
                    bytes = await res.Content.ReadAsByteArrayAsync(cts.Token).ConfigureAwait(false);
                }
                catch (OperationCanceledException e) when (!ct.IsCancellationRequested)
                {
                    // this client's Timeout, or the HttpClient's own (it caps every attempt too)
                    var within = !cts.IsCancellationRequested && e.InnerException is TimeoutException ? _http.Timeout : timeout;
                    failure = new ApiTimeoutException("timeout", string.Create(CultureInfo.InvariantCulture, $"No answer within {within.TotalSeconds:0.###} s."), e) { IdempotencyKey = key, Retryable = true };
                }
                catch (HttpRequestException e)
                {
                    var message = _key is null ? e.Message : e.Message.Replace(_key, MaskedKey);
                    failure = new ApiConnectionException("connection_failed", ErrorReader.Clip(message), e) { IdempotencyKey = key, Retryable = true };
                }
            }
            if (res is not null)
            {
                using (res)
                {
                    Remember(res.Headers, budget);
                    var raw = new RawResponse
                    {
                        StatusCode = (int)res.StatusCode,
                        Headers = ErrorReader.Headers(res),
                        Body = bytes,
                        RequestId = res.Headers.TryGetValues("X-Request-Id", out var ids) ? ids.FirstOrDefault() : null,
                        IdempotencyKey = key,
                    };
                    options?.OnResponse?.Invoke(raw);
                    // a 304 (If-None-Match, nothing changed) is no error: the call answers nothing
                    if (res.IsSuccessStatusCode || res.StatusCode == HttpStatusCode.NotModified) return raw;
                    var error = ErrorReader.From(res, bytes, key);
                    // the API's own retryable (a 409 operation_in_progress is, a 503 sandbox_disabled is not); an answer
                    // without its envelope (a proxy's 502 page) by its status
                    var again = retryable && attempt < max && error.Retryable == true;
                    if (!again || error.RetryAfter > RetryAfterMax) throw error;
                    var wait = error.RetryAfter ?? Jitter(attempt);
                    Log("debug", $"phoenixperps: retrying {op.Id} after {(int)res.StatusCode} in {wait.TotalMilliseconds:0} ms (attempt {attempt + 1})");
                    await Task.Delay(wait, ct).ConfigureAwait(false);
                    continue;
                }
            }
            if (!(retryable && attempt < max)) throw failure!;
            var w = Jitter(attempt);
            Log("debug", $"phoenixperps: retrying {op.Id} after {failure!.Code} in {w.TotalMilliseconds:0} ms (attempt {attempt + 1})");
            await Task.Delay(w, ct).ConfigureAwait(false);
        }
    }

    private static TimeSpan Jitter(int attempt) => TimeSpan.FromMilliseconds(Random.Shared.NextDouble() * Math.Min(8000, 500 * Math.Pow(2, attempt)));

    internal async Task<T> OneAsync<T, TBody>(Operation op, string[]? path, List<KeyValuePair<string, string>>? query, TBody? body, JsonTypeInfo<TBody> bodyType, JsonTypeInfo<T> type, RequestOptions? options, CancellationToken ct)
    {
        var payload = body is null ? null : JsonSerializer.SerializeToUtf8Bytes(body, bodyType);
        var raw = await SendAsync(op, path, query, payload, options, ct).ConfigureAwait(false);
        return Read(raw, type);
    }

    internal async Task<T> OneAsync<T>(Operation op, string[]? path, List<KeyValuePair<string, string>>? query, JsonTypeInfo<T> type, RequestOptions? options, CancellationToken ct)
    {
        var raw = await SendAsync(op, path, query, null, options, ct).ConfigureAwait(false);
        return Read(raw, type);
    }

    // the answer's data (null for a 304 Not Modified or a 204, which have no body; any other empty answer is no JSON:
    // http_<status>)
    private static T Read<T>(RawResponse raw, JsonTypeInfo<T> type)
    {
        if (raw.StatusCode == (int)HttpStatusCode.NotModified || (raw.StatusCode == (int)HttpStatusCode.NoContent && raw.Body.Length == 0)) return default!;
        using var doc = Parse(raw);
        var el = doc.RootElement.ValueKind == JsonValueKind.Object && doc.RootElement.TryGetProperty("data", out var d) ? d : doc.RootElement;
        try
        {
            return el.Deserialize(type)!;
        }
        catch (JsonException e)
        {
            throw Unreadable(raw, e);
        }
    }

    // a 2xx whose body is not JSON (a proxy's page) is an error, as any other answer is: http_<status>
    private static JsonDocument Parse(RawResponse raw)
    {
        try
        {
            return JsonDocument.Parse(raw.Body);
        }
        catch (JsonException e)
        {
            throw new ServerException($"http_{raw.StatusCode}", ErrorReader.Clip(Encoding.UTF8.GetString(raw.Body)), raw.StatusCode, e)
            {
                RequestId = raw.RequestId, IdempotencyKey = raw.IdempotencyKey, Headers = raw.Headers, Retryable = false,
            };
        }
    }

    private static ServerException Unreadable(RawResponse raw, JsonException e) =>
        new("invalid_response", "The answer could not be read: " + e.Message, raw.StatusCode, e) { RequestId = raw.RequestId, IdempotencyKey = raw.IdempotencyKey, Headers = raw.Headers, Retryable = false };

    internal async Task<Page<T>> ListAsync<T>(Operation op, string[]? path, List<KeyValuePair<string, string>>? query, JsonTypeInfo<Page<T>> type, RequestOptions? options, CancellationToken ct)
    {
        var raw = await SendAsync(op, path, query, null, options, ct).ConfigureAwait(false);
        if (raw.StatusCode == (int)HttpStatusCode.NotModified) return null!; // nothing, never an empty page
        using var doc = Parse(raw);
        Page<T> page;
        try
        {
            page = doc.RootElement.Deserialize(type) ?? new Page<T>();
        }
        catch (JsonException e)
        {
            throw Unreadable(raw, e);
        }
        page.Raw = raw;
        if (op.Cursor is not null)
        {
            page.Next = (cursor, token) =>
            {
                var q = (query ?? new()).Where(kv => kv.Key != op.Cursor).ToList();
                q.Add(new(op.Cursor, cursor));
                return ListAsync(op, path, q, type, options, token);
            };
        }
        return page;
    }

    internal async IAsyncEnumerable<T> AutoPagingAsync<T>(Func<Task<Page<T>>> first, RequestOptions? options, [EnumeratorCancellation] CancellationToken ct)
    {
        var max = options?.MaxItems ?? 0;
        var n = 0;
        Page<T>? page = await first().ConfigureAwait(false);
        while (page is not null)
        {
            foreach (var item in page.Data)
            {
                if (max > 0 && n >= max) yield break;
                n++;
                yield return item;
            }
            if (max > 0 && n >= max) yield break;
            page = await page.GetNextPageAsync(ct).ConfigureAwait(false);
        }
    }
}
