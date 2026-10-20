using System.Globalization;
using System.Net.Http.Headers;
using System.Text.Json;

namespace PhoenixPerps;

/// <summary>
/// Every failure of a call (docs/BEHAVIOUR.md "Answers"): the API's code and message, the HTTP status, the request id,
/// the details, the Retry-After, whether it is retryable, the order it is about, the call's Idempotency-Key and the
/// answer's headers. Its class tells the family.
/// </summary>
public class PhoenixPerpsException : Exception
{
    /// <summary>A failure with its code and message.</summary>
    public PhoenixPerpsException(string code, string message, int? status = null, Exception? inner = null) : base(message, inner)
    {
        Code = code;
        Status = status;
    }

    /// <summary>The API's error code (ErrorCodes), or http_&lt;status&gt; for an answer that is not the API's.</summary>
    public string Code { get; }

    /// <summary>The HTTP status, when there was an answer.</summary>
    public int? Status { get; }

    /// <summary>The error type of the API's envelope.</summary>
    public string? ErrorType { get; internal set; }

    /// <summary>The request id, to quote to support.</summary>
    public string? RequestId { get; internal set; }

    /// <summary>The details the API gave, as they came.</summary>
    public JsonElement? Details { get; internal set; }

    /// <summary>When to try again, when the API said.</summary>
    public TimeSpan? RetryAfter { get; internal set; }

    /// <summary>Whether the same call may work if sent again later: as the API said; by the status for an answer without
    /// its envelope (429, 500, 502, 503, 504); true for a timeout or a network failure (null: no call was made).</summary>
    public bool? Retryable { get; internal set; }

    /// <summary>Where the API explains this code.</summary>
    public string? DocUrl { get; internal set; }

    /// <summary>The order this is about (a refused order keeps its id).</summary>
    public string? OrderId { get; internal set; }

    /// <summary>The call's Idempotency-Key: send the call again with it to be sure it happens once.</summary>
    public string? IdempotencyKey { get; internal set; }

    /// <summary>The answer's headers (names in lower case), when there was an answer.</summary>
    public IReadOnlyDictionary<string, string>? Headers { get; internal set; }

    /// <summary>As every exception's, with the code, the HTTP status and the request id after the message: a crash log
    /// carries what support asks for.</summary>
    public override string ToString()
    {
        var s = base.ToString();
        var head = GetType().FullName + ": " + Message;
        var facts = $" ({Code}{(Status is { } status ? $", HTTP {status}" : "")}{(RequestId is null ? "" : $", {RequestId}")})";
        return s.StartsWith(head, StringComparison.Ordinal) ? head + facts + s[head.Length..] : s;
    }
}

/// <summary>401: the key is missing, wrong, revoked, expired or of the other environment.</summary>
public sealed class AuthenticationException(string code, string message, int? status = null) : PhoenixPerpsException(code, message, status);

/// <summary>403: the key may not do this.</summary>
public sealed class PermissionException(string code, string message, int? status = null) : PhoenixPerpsException(code, message, status);

/// <summary>404.</summary>
public sealed class NotFoundException(string code, string message, int? status = null) : PhoenixPerpsException(code, message, status);

/// <summary>409.</summary>
public sealed class ConflictException(string code, string message, int? status = null) : PhoenixPerpsException(code, message, status);

/// <summary>Another 4xx: the request is not right.</summary>
public sealed class ValidationException(string code, string message, int? status = null) : PhoenixPerpsException(code, message, status);

/// <summary>422: the account's rules or the key's controls said no.</summary>
public sealed class RuleRejectedException(string code, string message, int? status = null) : PhoenixPerpsException(code, message, status);

/// <summary>429.</summary>
public sealed class RateLimitException(string code, string message, int? status = null) : PhoenixPerpsException(code, message, status);

/// <summary>5xx.</summary>
public sealed class ServerException(string code, string message, int? status = null, Exception? inner = null) : PhoenixPerpsException(code, message, status, inner);

/// <summary>No answer: the network failed.</summary>
public sealed class ApiConnectionException(string code, string message, Exception? inner = null) : PhoenixPerpsException(code, message, null, inner);

/// <summary>No answer in time.</summary>
public sealed class ApiTimeoutException(string code, string message, Exception? inner = null) : PhoenixPerpsException(code, message, null, inner);

/// <summary>A stream ended for good.</summary>
public sealed class StreamClosedException(string code, string message) : PhoenixPerpsException(code, message);

internal static class ErrorReader
{
    // the statuses worth trying again when the answer is not the API's envelope (a proxy's page)
    private static readonly HashSet<int> RetryStatus = new() { 429, 500, 502, 503, 504 };

    internal static PhoenixPerpsException For(int status, string code, string message) => status switch
    {
        401 => new AuthenticationException(code, message, status),
        403 => new PermissionException(code, message, status),
        404 => new NotFoundException(code, message, status),
        409 => new ConflictException(code, message, status),
        422 => new RuleRejectedException(code, message, status),
        429 => new RateLimitException(code, message, status),
        >= 500 => new ServerException(code, message, status),
        _ => new ValidationException(code, message, status),
    };

    internal static TimeSpan? RetryAfter(HttpResponseHeaders headers, DateTimeOffset now)
    {
        if (!headers.TryGetValues("Retry-After", out var values)) return null;
        var v = values.FirstOrDefault()?.Trim();
        if (string.IsNullOrEmpty(v)) return null;
        // seconds, fractions included (1.5 is not 0); one beyond any clock is the longest wait
        if (decimal.TryParse(v, NumberStyles.AllowDecimalPoint, CultureInfo.InvariantCulture, out var s)) return s < 1e11m ? TimeSpan.FromTicks((long)(s * TimeSpan.TicksPerSecond)) : TimeSpan.MaxValue;
        if (DateTimeOffset.TryParse(v, CultureInfo.InvariantCulture, DateTimeStyles.AssumeUniversal, out var at)) return at > now ? at - now : TimeSpan.Zero;
        return null;
    }

    /// <summary>An answer's headers, its content's too, the names in lower case.</summary>
    internal static IReadOnlyDictionary<string, string> Headers(HttpResponseMessage res) =>
        res.Headers.Concat(res.Content.Headers).GroupBy(h => h.Key, StringComparer.OrdinalIgnoreCase).ToDictionary(g => g.Key.ToLowerInvariant(), g => string.Join(",", g.First().Value));

    /// <summary>Any answer as an error: the API's envelope, a bare {"error": "text"}, or HTML or text.</summary>
    internal static PhoenixPerpsException From(HttpResponseMessage res, byte[] body, string? idempotencyKey)
    {
        var status = (int)res.StatusCode;
        var headers = res.Headers;
        string err = $"http_{status}", message = "", type = "", orderId = "", docUrl = "";
        var requestId = headers.TryGetValues("X-Request-Id", out var ids) ? ids.FirstOrDefault() : null;
        JsonElement? details = null;
        // the API's flag; an answer without its envelope by its status
        bool? retryable = null;
        try
        {
            using var doc = JsonDocument.Parse(body);
            if (doc.RootElement.ValueKind == JsonValueKind.Object && doc.RootElement.TryGetProperty("error", out var e))
            {
                if (e.ValueKind == JsonValueKind.Object)
                {
                    if (Str(e, "code") is { Length: > 0 } c) err = c;
                    message = Str(e, "message") ?? "";
                    type = Str(e, "type") ?? "";
                    orderId = Str(e, "order_id") ?? "";
                    docUrl = Str(e, "doc_url") ?? "";
                    requestId = Str(e, "request_id") ?? requestId;
                    if (e.TryGetProperty("details", out var d) && d.ValueKind != JsonValueKind.Null) details = d.Clone();
                    if (e.TryGetProperty("retryable", out var r) && r.ValueKind is JsonValueKind.True or JsonValueKind.False) retryable = r.GetBoolean();
                }
                else if (e.ValueKind == JsonValueKind.String) message = Clip(e.GetString() ?? "");
            }
            else message = Clip(System.Text.Encoding.UTF8.GetString(body));
        }
        catch (JsonException)
        {
            message = Clip(System.Text.Encoding.UTF8.GetString(body));
        }
        if (message.Length == 0) message = err;
        var x = For(status, err, message);
        x.ErrorType = N(type);
        x.RequestId = requestId;
        x.Details = details;
        x.RetryAfter = RetryAfter(headers, DateTimeOffset.UtcNow);
        x.Retryable = retryable ?? RetryStatus.Contains(status);
        x.DocUrl = N(docUrl);
        x.OrderId = N(orderId);
        x.IdempotencyKey = idempotencyKey;
        x.Headers = Headers(res);
        return x;
    }

    private static string? N(string s) => s.Length == 0 ? null : s;

    private static string? Str(JsonElement e, string name) => e.TryGetProperty(name, out var v) && v.ValueKind == JsonValueKind.String ? v.GetString() : null;

    internal static string Clip(string s) => s.Length > 200 ? s[..200] : s;
}
