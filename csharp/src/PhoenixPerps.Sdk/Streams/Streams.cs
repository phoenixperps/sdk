using System.Collections.Concurrent;
using System.Net;
using System.Net.WebSockets;
using System.Runtime.CompilerServices;
using System.Text;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Threading.Channels;

namespace PhoenixPerps;

/*
The two read-only streams (docs/BEHAVIOUR.md "Streams"; orders always go through REST).

AccountStream: the key on the upgrade; hello, one snapshot per account, snapshot_end (Ready), then events, each once
(replays are told apart by their event id; each keeps its frame's Seq and Replayed). It reconnects by itself with
?resume=<last event id>, waiting min(30 s, 0.5 s x 2^n) with jitter (0 to 3 s after 1012, the server restarting). A
resume the server cannot cover (a resume_gap notice) and a 4008 (too far behind, reconnected without resume) are gaps:
told to OnGap and the log with the last event told, to read GET /v1/events?after= from. 4001 (the key ended) ends it
with an AuthenticationException; an error frame (too_many_accounts, account_not_found: its one subscribe refused, which
every reconnect would send again) with a ValidationException; either fails Ready too. 4013 waits longer. Risk frames
(asked with Risk) go to OnRisk as they come. UnsubscribeAsync keeps accounts out: the subscribe sent again after a
reconnect names what is left.

Both streams: an upgrade refused with 429 or 5xx, or no answer, is tried again after its Retry-After (else the backoff);
any other refusal ends the stream with the API's own error, as a REST call would. One log line per reconnect, with its
cause. Three heartbeats without a frame mean a dead connection. Nothing escapes: a failure ends the reads with it.

MarketStream: SubscribeAsync("ticker", new[] { "BTC-USD" }, new() { ["interval_ms"] = 250 }) answers once the server
took it; the markets it took (not those it rejected) are made again after a reconnect, until unsubscribed: the server
knows a subscription by its channel and market (and interval, for candles), whatever its other options (the last
subscribe sets them). An unsubscribe without markets ends every one of the channel, a candles one without interval
every interval. An ask still unanswered goes again on the next connection. Made again, a subscription sends its
history again (a trade's id, a candle's time tell the repeats apart). A slow reader loses the oldest frames, never the
connection.
*/

/// <summary>What an account stream follows.</summary>
public sealed record AccountStreamOptions
{
    /// <summary>The accounts to follow (null: every account of the key).</summary>
    public IReadOnlyList<string>? Accounts { get; init; }

    /// <summary>Live risk frames too (the server sends them only when asked).</summary>
    public bool Risk { get; init; }

    /// <summary>Start after this event id.</summary>
    public string? Resume { get; init; }

    /// <summary>Receives each risk frame as it comes (with Risk set): the account and its risk, as GET .../risk answers it.
    /// It runs on the stream's reader: return quickly.</summary>
    public Action<string, AccountRisk>? OnRisk { get; init; }

    /// <summary>Told when events were missed (a resume the server could not cover, or a 4008), with the id of the last
    /// event told: read GET /v1/events?after= from it.</summary>
    public Action<string?>? OnGap { get; init; }
}

public sealed partial class Event
{
    /// <summary>The account stream frame's seq (its connection's count of events, gapless); null outside the stream.</summary>
    [JsonIgnore]
    public long? Seq { get; internal set; }

    /// <summary>The account stream sent it again on a resume (one of the events of the time it was away).</summary>
    [JsonIgnore]
    public bool Replayed { get; internal set; }
}

/// <summary>The upgrade goes through this handler: ClientWebSocket keeps only a refused upgrade's status, this keeps its
/// answer too (the API's error envelope and Retry-After).</summary>
internal sealed class Upgrade : DelegatingHandler
{
    internal Upgrade() : base(new SocketsHttpHandler { UseCookies = false, PooledConnectionLifetime = TimeSpan.Zero }) =>
        Invoker = new HttpMessageInvoker(this, disposeHandler: false);

    internal HttpMessageInvoker Invoker { get; }

    /// <summary>The last upgrade's refusal (null: it was taken, or never answered).</summary>
    internal PhoenixPerpsException? Refusal { get; set; }

    protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
    {
        var res = await base.SendAsync(request, cancellationToken).ConfigureAwait(false);
        if (res.StatusCode != HttpStatusCode.SwitchingProtocols)
            Refusal = ErrorReader.From(res, await res.Content.ReadAsByteArrayAsync(cancellationToken).ConfigureAwait(false), null);
        return res;
    }
}

internal static class StreamCommon
{
    internal static TimeSpan Backoff(int n) => TimeSpan.FromMilliseconds(Random.Shared.NextDouble() * Math.Min(30000, 500 * Math.Pow(2, n)));

    internal static ClientWebSocket Socket(PhoenixPerpsClient c)
    {
        var ws = new ClientWebSocket();
        if (c.Key is { } key) ws.Options.SetRequestHeader("Authorization", "Bearer " + key);
        ws.Options.SetRequestHeader("User-Agent", c.UserAgent);
        return ws;
    }

    /// <summary>Opens ws: null once connected, else why not (a refused upgrade as the API's own error, with its status,
    /// code and Retry-After; no answer as an ApiConnectionException).</summary>
    internal static async Task<PhoenixPerpsException?> ConnectAsync(ClientWebSocket ws, string url, Upgrade upgrade, CancellationToken ct)
    {
        upgrade.Refusal = null;
        try
        {
            await ws.ConnectAsync(new Uri(url), upgrade.Invoker, ct).ConfigureAwait(false);
            return null;
        }
        catch (Exception e) when (!ct.IsCancellationRequested)
        {
            return upgrade.Refusal ?? new ApiConnectionException("connection_failed", ErrorReader.Clip(e.Message), e) { Retryable = true };
        }
    }

    /// <summary>A refusal worth connecting again after: 429, 5xx, or no answer.</summary>
    internal static bool Transient(PhoenixPerpsException e) => e.Status is null or 429 or >= 500;

    internal static string Cause(PhoenixPerpsException e) => e.Status is { } s ? $"refused ({s} {e.Code})" : $"failed ({e.Message})";

    /// <summary>One whole text message, or null when the server closed.</summary>
    internal static async Task<byte[]?> ReceiveAsync(ClientWebSocket ws, CancellationToken ct)
    {
        var buffer = new byte[16384];
        using var ms = new MemoryStream();
        while (true)
        {
            var r = await ws.ReceiveAsync(buffer, ct).ConfigureAwait(false);
            if (r.MessageType == WebSocketMessageType.Close) return null;
            ms.Write(buffer, 0, r.Count);
            if (ms.Length > 4 << 20) throw new WebSocketException("frame too large");
            if (r.EndOfMessage) return ms.ToArray();
        }
    }

    internal static Task SendAsync(ClientWebSocket ws, object message, CancellationToken ct) =>
        ws.SendAsync(Encoding.UTF8.GetBytes(Serialize(message)), WebSocketMessageType.Text, true, ct);

    internal static string Serialize(object message)
    {
        // the stream messages are small maps of strings, numbers, booleans and string lists
        var sb = new StringBuilder();
        Write(sb, message);
        return sb.ToString();
    }

    private static void Write(StringBuilder sb, object? v)
    {
        switch (v)
        {
            case null: sb.Append("null"); break;
            case string s: sb.Append('"').Append(JsonEncodedText.Encode(s).ToString()).Append('"'); break;
            case bool b: sb.Append(b ? "true" : "false"); break;
            case JsonElement el: sb.Append(el.GetRawText()); break;
            case IDictionary<string, object?> d:
                sb.Append('{');
                var first = true;
                foreach (var (k, x) in d)
                {
                    if (!first) sb.Append(',');
                    first = false;
                    Write(sb, k);
                    sb.Append(':');
                    Write(sb, x);
                }
                sb.Append('}');
                break;
            case System.Collections.IEnumerable list:
                sb.Append('[');
                var f = true;
                foreach (var x in list)
                {
                    if (!f) sb.Append(',');
                    f = false;
                    Write(sb, x);
                }
                sb.Append(']');
                break;
            case IFormattable n: sb.Append(n.ToString(null, System.Globalization.CultureInfo.InvariantCulture)); break;
            default: Write(sb, v.ToString()); break;
        }
    }

    internal static int? CloseCode(ClientWebSocket ws) => ws.CloseStatus is { } s ? (int)s : null;

    /// <summary>An error frame as a ValidationException: its code, its details (with the account it names), and a
    /// message that shows them (the server sends none).</summary>
    internal static ValidationException Refusal(JsonElement f)
    {
        string? S(string name) => f.TryGetProperty(name, out var v) && v.ValueKind == JsonValueKind.String ? v.GetString() : null;
        var code = S("code") ?? "stream_refused";
        JsonElement? details = f.TryGetProperty("details", out var d) && d.ValueKind != JsonValueKind.Null ? d.Clone() : null;
        if (S("account_id") is { } account)
        {
            var buffer = new System.Buffers.ArrayBufferWriter<byte>();
            using (var w = new Utf8JsonWriter(buffer))
            {
                w.WriteStartObject();
                if (details is { ValueKind: JsonValueKind.Object } o)
                    foreach (var p in o.EnumerateObject()) p.WriteTo(w);
                w.WriteString("account_id", account);
                w.WriteEndObject();
            }
            using var doc = JsonDocument.Parse(buffer.WrittenMemory);
            details = doc.RootElement.Clone();
        }
        return new ValidationException(code, S("message") ?? (details is { } x ? $"{code} {x.GetRawText()}" : code)) { Details = details };
    }
}

/// <summary>The account stream: snapshots, then every event of the accounts, each once.</summary>
public sealed class AccountStream : IAsyncDisposable
{
    private const int Lru = 2048;
    private readonly PhoenixPerpsClient _c;
    private readonly AccountStreamOptions _o;
    private readonly CancellationTokenSource _cts = new();
    private readonly Channel<Event> _events = Channel.CreateBounded<Event>(1024);
    private readonly TaskCompletionSource _ready = new(TaskCreationOptions.RunContinuationsAsynchronously);
    private readonly ConcurrentDictionary<string, JsonElement> _snapshots = new();
    private readonly ConcurrentDictionary<string, JsonElement> _risks = new();
    private readonly HashSet<string> _seen = new();
    private readonly Queue<string> _order = new();
    private readonly Upgrade _upgrade = new();
    private readonly object _lock = new();
    private readonly HashSet<string> _unsubscribed = new(StringComparer.OrdinalIgnoreCase);
    // asks waiting for their answer (the stream's replies carry no id: each answers the oldest ask of its kind)
    private readonly List<(string Reply, TaskCompletionSource<JsonElement> Done, Dictionary<string, object?> Message)> _pending = new();
    private readonly Task _run;
    private ClientWebSocket? _ws;
    private Exception? _error;
    private int _attempt;
    private int _disposed;

    internal AccountStream(PhoenixPerpsClient c, AccountStreamOptions? o)
    {
        _c = c;
        _o = o ?? new AccountStreamOptions();
        LastEventId = _o.Resume;
        _run = Task.Run(RunAsync);
    }

    /// <summary>Done once the first snapshots are in; failed if the stream ended for good before.</summary>
    public Task Ready => _ready.Task;

    /// <summary>The id of the last event told (the resume point).</summary>
    public string? LastEventId { get; private set; }

    /// <summary>The last snapshot frame of an account.</summary>
    public JsonElement? Snapshot(string accountId) => _snapshots.TryGetValue(accountId, out var s) ? s : null;

    /// <summary>The last risk frame of an account (with Risk set; OnRisk is told each as it comes).</summary>
    public JsonElement? Risk(string accountId) => _risks.TryGetValue(accountId, out var s) ? s : null;

    /// <summary>A round trip to the server (its pong).</summary>
    public Task<JsonElement> PingAsync(CancellationToken cancellationToken = default) =>
        AskAsync("pong", new Dictionary<string, object?> { ["op"] = "ping" }, cancellationToken);

    /// <summary>Stops following accounts (the server's unsubscribed reply). They stay out after a reconnect: the subscribe
    /// sent again names what is left.</summary>
    public Task<JsonElement> UnsubscribeAsync(IEnumerable<string> accounts, CancellationToken cancellationToken = default)
    {
        var list = accounts.ToList();
        lock (_lock) _unsubscribed.UnionWith(list);
        return AskAsync("unsubscribed", new Dictionary<string, object?> { ["op"] = "unsubscribe", ["accounts"] = list }, cancellationToken);
    }

    /// <summary>The next event. After Dispose it throws StreamClosedException; after 4001 an AuthenticationException;
    /// after an error frame a ValidationException; after a refused upgrade the API's own error.</summary>
    public async ValueTask<Event> ReadAsync(CancellationToken cancellationToken = default)
    {
        while (await _events.Reader.WaitToReadAsync(cancellationToken).ConfigureAwait(false))
            if (_events.Reader.TryRead(out var e)) return e;
        throw new StreamClosedException("stream_closed", "The stream is closed.");
    }

    /// <summary>Every event until the stream ends (an error that ended it is thrown).</summary>
    public IAsyncEnumerable<Event> ReadAllAsync(CancellationToken cancellationToken = default) => _events.Reader.ReadAllAsync(cancellationToken);

    /// <inheritdoc />
    public async ValueTask DisposeAsync()
    {
        if (Interlocked.Exchange(ref _disposed, 1) != 0) return; // once: an await using after an explicit dispose
        _cts.Cancel();
        try
        {
            await _run.ConfigureAwait(false);
        }
        catch (OperationCanceledException)
        {
        }
        End(new StreamClosedException("stream_closed", "The stream is closed."), complete: true);
        _upgrade.Dispose();
        _cts.Dispose();
    }

    // the end: the asks still waiting fail with e, and Ready if the first snapshots never came (a Dispose too); for good
    // (not a Dispose), the reads throw e once drained
    private void End(Exception e, bool complete)
    {
        List<TaskCompletionSource<JsonElement>> waiting;
        lock (_lock)
        {
            _error ??= e;
            waiting = _pending.Select(p => p.Done).ToList();
            _pending.Clear();
        }
        foreach (var w in waiting) w.TrySetException(e);
        _ready.TrySetException(e);
        _events.Writer.TryComplete(complete ? null : e);
    }

    private async Task<JsonElement> AskAsync(string reply, Dictionary<string, object?> message, CancellationToken ct)
    {
        TaskCompletionSource<JsonElement> done = new(TaskCreationOptions.RunContinuationsAsynchronously);
        ClientWebSocket? ws;
        lock (_lock)
        {
            if (_error is not null) throw _error;
            _pending.Add((reply, done, message));
            ws = _ws;
        }
        if (ws is not null)
        {
            try
            {
                await StreamCommon.SendAsync(ws, message, ct).ConfigureAwait(false);
            }
            catch (Exception) when (!ct.IsCancellationRequested)
            {
                // the connection dropped: the message goes again on the next one
            }
        }
        using (ct.Register(() => done.TrySetCanceled(ct)))
            return await done.Task.ConfigureAwait(false);
    }

    // the subscribe: the accounts asked (none: every one of the key); once some are unsubscribed, what is left, named one
    // by one (from the hello's list when every one was asked)
    private Dictionary<string, object?> Subscribe(JsonElement hello)
    {
        var msg = new Dictionary<string, object?> { ["op"] = "subscribe" };
        IEnumerable<string>? accounts = _o.Accounts is { Count: > 0 } a ? a : null;
        if (_unsubscribed.Count > 0)
        {
            accounts ??= hello.TryGetProperty("accounts", out var h) && h.ValueKind == JsonValueKind.Array ? h.EnumerateArray().Select(x => x.GetString() ?? "") : Array.Empty<string>();
            accounts = accounts.Where(x => !_unsubscribed.Contains(x)).ToList();
        }
        if (accounts is not null) msg["accounts"] = accounts;
        if (_o.Risk) msg["risk"] = true;
        return msg;
    }

    // events missed (a resume the server could not cover, or a 4008): told, never hidden
    private void Gap(string? after)
    {
        _c.Log("warn", $"phoenixperps: account stream: events after {after ?? "the start"} were missed: read GET /v1/events?after={after}");
        _o.OnGap?.Invoke(after);
    }

    private async Task RunAsync()
    {
        var resume = true;
        var ct = _cts.Token;
        try
        {
            while (!ct.IsCancellationRequested)
            {
                var url = _c.AccountStreamUrl + (resume && LastEventId is not null ? "?resume=" + Uri.EscapeDataString(LastEventId) : "");
                resume = true;
                TimeSpan wait;
                string cause;
                using (var ws = StreamCommon.Socket(_c))
                {
                    if (await StreamCommon.ConnectAsync(ws, url, _upgrade, ct).ConfigureAwait(false) is { } refused)
                    {
                        if (!StreamCommon.Transient(refused))
                        {
                            End(refused, complete: false); // the API's answer, as a REST call would have it
                            return;
                        }
                        (wait, cause) = (refused.RetryAfter ?? StreamCommon.Backoff(_attempt), StreamCommon.Cause(refused));
                    }
                    else
                    {
                        (wait, resume, var stop, cause) = await ReadAsync(ws, ct).ConfigureAwait(false);
                        lock (_lock) _ws = null;
                        if (stop) return;
                    }
                }
                _attempt++;
                _c.Log("info", $"phoenixperps: account stream {cause}: reconnecting in {wait.TotalMilliseconds:0} ms");
                await Task.Delay(wait, ct).ConfigureAwait(false);
            }
        }
        catch (Exception e) when (!ct.IsCancellationRequested)
        {
            End(e, complete: false); // nothing escapes: the reads end with it
        }
    }

    private async Task<(TimeSpan Wait, bool Resume, bool Stop, string Cause)> ReadAsync(ClientWebSocket ws, CancellationToken ct)
    {
        var heartbeat = TimeSpan.FromSeconds(15);
        while (true)
        {
            byte[]? data;
            using (var beat = CancellationTokenSource.CreateLinkedTokenSource(ct))
            {
                beat.CancelAfter(heartbeat * 3);
                try
                {
                    data = await StreamCommon.ReceiveAsync(ws, beat.Token).ConfigureAwait(false);
                }
                catch (Exception) when (!ct.IsCancellationRequested)
                {
                    return (StreamCommon.Backoff(_attempt), true, false, beat.IsCancellationRequested ? "silent for three heartbeats" : "dropped");
                }
            }
            if (data is null)
            {
                var code = StreamCommon.CloseCode(ws);
                switch (code)
                {
                    case 4001:
                        End(new AuthenticationException("stream_credential_ended", "The key or session of this stream ended."), complete: false);
                        return (TimeSpan.Zero, false, true, "");
                    case 4008:
                        Gap(LastEventId); // too far behind: what the server dropped is lost to this stream
                        return (StreamCommon.Backoff(_attempt), false, false, "too far behind (4008)");
                    case 4013: return (TimeSpan.FromSeconds(30) + StreamCommon.Backoff(_attempt), true, false, "closed (4013)");
                    case 1012: return (TimeSpan.FromMilliseconds(Random.Shared.NextDouble() * 3000), true, false, "closed (1012)");
                    default: return (StreamCommon.Backoff(_attempt), true, false, $"closed ({code})");
                }
            }
            using var doc = JsonDocument.Parse(data);
            var f = doc.RootElement;
            var type = f.TryGetProperty("type", out var t) ? t.GetString() : null;
            switch (type)
            {
                case "hello":
                    _attempt = 0;
                    if (f.TryGetProperty("heartbeat_ms", out var hb) && hb.TryGetInt64(out var ms) && ms > 0) heartbeat = TimeSpan.FromMilliseconds(ms);
                    List<Dictionary<string, object?>> again;
                    lock (_lock)
                    {
                        // the subscribe, then the asks still unanswered
                        again = new() { Subscribe(f) };
                        again.AddRange(_pending.Select(p => p.Message));
                        _ws = ws;
                    }
                    foreach (var m in again) await StreamCommon.SendAsync(ws, m, ct).ConfigureAwait(false);
                    break;
                case "snapshot":
                    _snapshots[f.GetProperty("account_id").GetString()!] = f.Clone();
                    break;
                case "snapshot_end":
                    _ready.TrySetResult();
                    break;
                case "risk":
                    var account = f.GetProperty("account_id").GetString()!;
                    _risks[account] = f.Clone();
                    if (_o.OnRisk is { } onRisk && f.TryGetProperty("data", out var r)) onRisk(account, r.Deserialize(Json.Default.AccountRisk)!);
                    break;
                case "notice":
                    // a resume the server cannot cover: the events after it are lost to this stream
                    if (f.TryGetProperty("code", out var nc) && nc.ValueKind == JsonValueKind.String && nc.ValueEquals("resume_gap"))
                        Gap(f.TryGetProperty("after", out var after) && after.ValueKind == JsonValueKind.String ? after.GetString() : LastEventId);
                    break;
                case "pong" or "unsubscribed":
                    TaskCompletionSource<JsonElement>? done = null;
                    lock (_lock)
                    {
                        var i = _pending.FindIndex(p => p.Reply == type);
                        if (i >= 0)
                        {
                            done = _pending[i].Done;
                            _pending.RemoveAt(i);
                        }
                    }
                    done?.TrySetResult(f.Clone());
                    break;
                case "error":
                    // our one subscribe refused (too_many_accounts, account_not_found, invalid_json): every reconnect
                    // would send it again as it is, so it ends the stream, as 4001 does
                    End(StreamCommon.Refusal(f), complete: false);
                    return (TimeSpan.Zero, false, true, "");
                case "event":
                    var e = f.GetProperty("event").Deserialize(Json.Default.Event)!;
                    if (!_seen.Add(e.Id)) break;
                    _order.Enqueue(e.Id);
                    if (_order.Count > Lru) _seen.Remove(_order.Dequeue());
                    LastEventId = e.Id;
                    e.Seq = f.TryGetProperty("seq", out var seq) && seq.TryGetInt64(out var n) ? n : null;
                    e.Replayed = f.TryGetProperty("replayed", out var replayed) && replayed.ValueKind == JsonValueKind.True;
                    await _events.Writer.WriteAsync(e, ct).ConfigureAwait(false);
                    break;
            }
        }
    }
}

/// <summary>One frame of the market stream: its type (the channel, or snapshot_end), its symbol and data.</summary>
public sealed record MarketFrame(string Type, string? Symbol, JsonElement? Data, JsonElement Raw)
{
    /// <summary>The candle interval of a candles frame, which tells two intervals of one market apart.</summary>
    public string? Interval => Raw.ValueKind == JsonValueKind.Object && Raw.TryGetProperty("interval", out var i) && i.ValueKind == JsonValueKind.String ? i.GetString() : null;
}

/// <summary>The market data stream.</summary>
public sealed class MarketStream : IAsyncDisposable
{
    // the markets the server took on a channel with these options (one entry per options, a market in one entry only)
    private sealed class Sub
    {
        public required string Channel { get; init; }
        public HashSet<string> Symbols { get; } = new();
        public required IReadOnlyDictionary<string, object?> Params { get; init; }
    }

    private readonly PhoenixPerpsClient _c;
    private readonly CancellationTokenSource _cts = new();
    private readonly Channel<MarketFrame> _frames = Channel.CreateBounded<MarketFrame>(new BoundedChannelOptions(1024) { FullMode = BoundedChannelFullMode.DropOldest });
    private readonly object _lock = new();
    private readonly Dictionary<string, Sub> _subs = new();
    private readonly Dictionary<long, (TaskCompletionSource<JsonElement> Reply, Dictionary<string, object?> Message)> _pending = new();
    private readonly Upgrade _upgrade = new();
    private readonly Task _run;
    private ClientWebSocket? _ws;
    private long _id;
    private int _attempt;
    private int _disposed;
    private Exception? _error;

    internal MarketStream(PhoenixPerpsClient c)
    {
        _c = c;
        _run = Task.Run(RunAsync);
    }

    /// <summary>The stream's limits, as its hello announced them: subscriptions, symbols_per_subscribe, messages_per_s.</summary>
    public JsonElement? Limits { get; private set; }

    /// <summary>Follows symbols on a channel (ticker, book, trades, candles, stats, status) with its params; the server's reply.</summary>
    public async Task<JsonElement> SubscribeAsync(string channel, IEnumerable<string>? symbols = null, IReadOnlyDictionary<string, object?>? parameters = null, CancellationToken cancellationToken = default)
    {
        var p = parameters ?? new Dictionary<string, object?>();
        var syms = symbols?.ToList() ?? new List<string>();
        var reply = await AskAsync(Message("subscribe", channel, syms, p), cancellationToken).ConfigureAwait(false);
        // what the server took (never the markets it rejected), now under these options only
        var taken = channel == "status" ? new List<string> { "*" }
            : reply.TryGetProperty("symbols", out var t) && t.ValueKind == JsonValueKind.Array ? t.EnumerateArray().Select(x => x.GetString() ?? "").ToList() : syms;
        lock (_lock)
        {
            Drop(channel, taken, p, unsubscribe: false);
            var k = Key(channel, p);
            if (!_subs.TryGetValue(k, out var s)) s = new Sub { Channel = channel, Params = p };
            foreach (var x in taken) s.Symbols.Add(x.ToUpperInvariant());
            if (s.Symbols.Count > 0) _subs[k] = s;
        }
        return reply;
    }

    /// <summary>Stops following symbols on a channel (none: every one; candles: of the interval in parameters, else every
    /// interval; other options do not matter). The reply names the markets ended and, rejected, those not followed.</summary>
    public Task<JsonElement> UnsubscribeAsync(string channel, IEnumerable<string>? symbols = null, IReadOnlyDictionary<string, object?>? parameters = null, CancellationToken cancellationToken = default)
    {
        var p = parameters ?? new Dictionary<string, object?>();
        var syms = symbols?.ToList() ?? new List<string>();
        lock (_lock) Drop(channel, syms, p, unsubscribe: true);
        var wire = new Dictionary<string, object?>();
        if (channel == "candles" && p.TryGetValue("interval", out var interval)) wire["interval"] = interval;
        return AskAsync(Message("unsubscribe", channel, syms, wire), cancellationToken);
    }

    /// <summary>The subscriptions the server holds for this connection (its list reply).</summary>
    public Task<JsonElement> ListAsync(CancellationToken cancellationToken = default) =>
        AskAsync(new Dictionary<string, object?> { ["op"] = "list" }, cancellationToken);

    /// <summary>A round trip to the server (its pong).</summary>
    public Task<JsonElement> PingAsync(CancellationToken cancellationToken = default) =>
        AskAsync(new Dictionary<string, object?> { ["op"] = "ping" }, cancellationToken);

    /// <summary>The next frame. After Dispose it throws StreamClosedException.</summary>
    public async ValueTask<MarketFrame> ReadAsync(CancellationToken cancellationToken = default)
    {
        while (await _frames.Reader.WaitToReadAsync(cancellationToken).ConfigureAwait(false))
            if (_frames.Reader.TryRead(out var f)) return f;
        throw new StreamClosedException("stream_closed", "The stream is closed.");
    }

    /// <summary>Every frame until the stream ends (an error that ended it is thrown).</summary>
    public IAsyncEnumerable<MarketFrame> ReadAllAsync(CancellationToken cancellationToken = default) => _frames.Reader.ReadAllAsync(cancellationToken);

    /// <inheritdoc />
    public async ValueTask DisposeAsync()
    {
        if (Interlocked.Exchange(ref _disposed, 1) != 0) return; // once: an await using after an explicit dispose
        _cts.Cancel();
        try
        {
            await _run.ConfigureAwait(false);
        }
        catch (OperationCanceledException)
        {
        }
        End(new StreamClosedException("stream_closed", "The stream is closed."), complete: true);
        _upgrade.Dispose();
        _cts.Dispose();
    }

    private static string Key(string channel, IReadOnlyDictionary<string, object?> p) =>
        channel + "|" + string.Join("|", p.OrderBy(kv => kv.Key, StringComparer.Ordinal).Select(kv => kv.Key + "=" + Value(kv.Value)));

    private static string Value(object? v) => StreamCommon.Serialize(new Dictionary<string, object?> { ["v"] = v });

    // the server knows a subscription by its channel and market (and interval, for candles): those markets leave every
    // entry of the channel (of that interval), whatever options it was made with. An unsubscribe naming no market ends
    // every one, a candles one naming no interval every interval (status has no markets: all of it goes).
    private void Drop(string channel, IEnumerable<string> symbols, IReadOnlyDictionary<string, object?> p, bool unsubscribe)
    {
        string? Interval(IReadOnlyDictionary<string, object?> x) => x.TryGetValue("interval", out var v) ? Value(v) : null;
        var interval = Interval(p);
        var gone = symbols.Select(x => x.ToUpperInvariant()).ToHashSet();
        var all = channel == "status" || (unsubscribe && gone.Count == 0);
        var everyInterval = unsubscribe && interval is null;
        foreach (var (k, s) in _subs.Where(kv => kv.Value.Channel == channel && (channel != "candles" || everyInterval || Interval(kv.Value.Params) == interval)).ToList())
        {
            if (all) s.Symbols.Clear();
            else s.Symbols.ExceptWith(gone);
            if (s.Symbols.Count == 0) _subs.Remove(k);
        }
    }

    private static Dictionary<string, object?> Message(string op, string channel, List<string> symbols, IReadOnlyDictionary<string, object?> p)
    {
        var m = new Dictionary<string, object?>();
        foreach (var (k, v) in p) m[k] = v;
        m["op"] = op;
        m["channel"] = channel;
        if (channel != "status") m["symbols"] = symbols;
        return m;
    }

    private async Task<JsonElement> AskAsync(Dictionary<string, object?> message, CancellationToken ct)
    {
        TaskCompletionSource<JsonElement> reply = new(TaskCreationOptions.RunContinuationsAsynchronously);
        ClientWebSocket? ws;
        lock (_lock)
        {
            if (_error is not null) throw _error;
            var id = ++_id;
            message["id"] = id;
            _pending[id] = (reply, message);
            ws = _ws;
        }
        if (ws is not null)
        {
            try
            {
                await StreamCommon.SendAsync(ws, message, ct).ConfigureAwait(false);
            }
            catch (Exception) when (!ct.IsCancellationRequested)
            {
                // the connection dropped: the message goes again on the next one
            }
        }
        using (ct.Register(() => reply.TrySetCanceled(ct)))
            return await reply.Task.ConfigureAwait(false);
    }

    private void End(Exception e, bool complete)
    {
        List<TaskCompletionSource<JsonElement>> waiting;
        lock (_lock)
        {
            _error ??= e;
            waiting = _pending.Values.Select(p => p.Reply).ToList();
            _pending.Clear();
        }
        foreach (var w in waiting) w.TrySetException(e);
        if (complete) _frames.Writer.TryComplete();
        else _frames.Writer.TryComplete(e);
    }

    private async Task RunAsync()
    {
        var ct = _cts.Token;
        try
        {
            while (!ct.IsCancellationRequested)
            {
                TimeSpan wait;
                string cause;
                using (var ws = StreamCommon.Socket(_c))
                {
                    if (await StreamCommon.ConnectAsync(ws, _c.MarketStreamUrl, _upgrade, ct).ConfigureAwait(false) is { } refused)
                    {
                        if (!StreamCommon.Transient(refused))
                        {
                            End(refused, complete: false); // the API's answer, as a REST call would have it
                            return;
                        }
                        (wait, cause) = (refused.RetryAfter ?? StreamCommon.Backoff(_attempt), StreamCommon.Cause(refused));
                    }
                    else
                    {
                        (wait, var stop, cause) = await ReadAsync(ws, ct).ConfigureAwait(false);
                        lock (_lock) _ws = null;
                        if (stop) return;
                    }
                }
                _attempt++;
                _c.Log("info", $"phoenixperps: market stream {cause}: reconnecting in {wait.TotalMilliseconds:0} ms");
                await Task.Delay(wait, ct).ConfigureAwait(false);
            }
        }
        catch (Exception e) when (!ct.IsCancellationRequested)
        {
            End(e, complete: false); // nothing escapes: the reads and the asks end with it
        }
    }

    private async Task<(TimeSpan Wait, bool Stop, string Cause)> ReadAsync(ClientWebSocket ws, CancellationToken ct)
    {
        var heartbeat = TimeSpan.FromSeconds(15);
        while (true)
        {
            byte[]? data;
            using (var beat = CancellationTokenSource.CreateLinkedTokenSource(ct))
            {
                beat.CancelAfter(heartbeat * 3);
                try
                {
                    data = await StreamCommon.ReceiveAsync(ws, beat.Token).ConfigureAwait(false);
                }
                catch (Exception) when (!ct.IsCancellationRequested)
                {
                    return (StreamCommon.Backoff(_attempt), false, beat.IsCancellationRequested ? "silent for three heartbeats" : "dropped");
                }
            }
            if (data is null)
            {
                var code = StreamCommon.CloseCode(ws);
                switch (code)
                {
                    case 4001:
                        End(new AuthenticationException("stream_credential_ended", "The key of this stream ended."), complete: false);
                        return (TimeSpan.Zero, true, "");
                    case 1012: return (TimeSpan.FromMilliseconds(Random.Shared.NextDouble() * 3000), false, "closed (1012)");
                    default: return (StreamCommon.Backoff(_attempt), false, $"closed ({code})");
                }
            }
            var f = JsonDocument.Parse(data).RootElement;
            var type = f.TryGetProperty("type", out var t) ? t.GetString() ?? "" : "";
            switch (type)
            {
                case "hello":
                    _attempt = 0;
                    Limits = f.TryGetProperty("limits", out var l) ? l.Clone() : null;
                    if (f.TryGetProperty("heartbeat_ms", out var hb) && hb.TryGetInt64(out var ms) && ms > 0) heartbeat = TimeSpan.FromMilliseconds(ms);
                    List<Dictionary<string, object?>> again;
                    lock (_lock)
                    {
                        // every subscription the server took, again, as it was; then the asks still unanswered
                        again = _subs.Values.Select(s =>
                        {
                            var m = Message("subscribe", s.Channel, s.Symbols.OrderBy(x => x, StringComparer.Ordinal).ToList(), s.Params);
                            m["id"] = ++_id;
                            return m;
                        }).ToList();
                        again.AddRange(_pending.Values.Select(p => p.Message));
                        _ws = ws;
                    }
                    foreach (var m in again) await StreamCommon.SendAsync(ws, m, ct).ConfigureAwait(false);
                    break;
                case "subscribed" or "unsubscribed" or "error" or "list" or "pong":
                    TaskCompletionSource<JsonElement>? reply = null;
                    if (f.TryGetProperty("id", out var idEl) && idEl.TryGetInt64(out var id))
                        lock (_lock)
                            if (_pending.Remove(id, out var entry)) reply = entry.Reply;
                    if (reply is null) break;
                    if (type == "error") reply.TrySetException(StreamCommon.Refusal(f));
                    else reply.TrySetResult(f);
                    break;
                case "heartbeat":
                    break;
                default:
                    _frames.Writer.TryWrite(new MarketFrame(type, f.TryGetProperty("symbol", out var sy) ? sy.GetString() : null, f.TryGetProperty("data", out var d) ? d : null, f));
                    break;
            }
        }
    }
}

public sealed partial class PhoenixPerpsClient
{
    /// <summary>Connects to the account stream; it runs until disposed.</summary>
    public AccountStream OpenAccountStream(AccountStreamOptions? options = null) => new(this, options);

    /// <summary>Connects to the market data stream; it runs until disposed.</summary>
    public MarketStream OpenMarketStream() => new(this);
}
