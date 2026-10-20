// What the C# SDK does beyond the conformance suite, against the same mock server (scripted answers and WebSocket
// connections): retries by the API's own retryable, the error's fields and printed form, the base URL, the raw call,
// a page's other fields, the timeouts, the User-Agent, the environment, the rate-limit budgets, 304 on a list; the
// streams' error frames, heartbeat, subscription bookkeeping, list and ping, account ping, unsubscribe, risk frames and
// seq, the reconnect log line, and a second dispose; the helpers' refusals.
using System.Diagnostics;
using System.Globalization;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using Xunit;

namespace PhoenixPerps.Tests;

public sealed class SdkTests(MockServer mock) : IClassFixture<MockServer>
{
    private static readonly TimeSpan Wait = TimeSpan.FromSeconds(10);
    private static readonly CreateOrder Limit = new() { Market = "BTC-USD", Side = OrderSide.Buy, Type = CreateOrderType.Limit, Size = 0.01m, LimitPrice = 62000m };
    private static readonly Dictionary<string, object?> Every250 = new() { ["interval_ms"] = 250 };
    private const string AccountHello = """{"send":{"type":"hello","stream_id":"str_1","environment":"sandbox","heartbeat_ms":15000}}""";
    private const string MarketHello = """{"send":{"type":"hello"}}""";
    private const string Ticker = """{"send":{"type":"ticker","symbol":"BTC-USD","data":{"last":"62000.1"}}}""";
    private const string Linger = """{"wait_ms":3000}""";

    private PhoenixPerpsClient Client(PhoenixPerpsClientOptions? o = null)
    {
        var ws = "ws" + mock.Url[4..];
        return new PhoenixPerpsClient((o ?? new PhoenixPerpsClientOptions()) with
        {
            ApiKey = mock.Key,
            BaseUrl = o?.BaseUrl ?? mock.Url,
            AccountStreamUrl = ws + "/v1/stream",
            MarketStreamUrl = ws + "/v1/market-data",
        });
    }

    private async Task<List<JsonElement>> Requests() => (await mock.Requests()).EnumerateArray().ToList();

    private static string Exchanges(params string[] exchanges) => "{\"exchanges\":[" + string.Join(",", exchanges) + "]}";

    private static string Connections(params string[][] connections) => "{\"connections\":[" + string.Join(",", connections.Select(c => "[" + string.Join(",", c) + "]")) + "]}";

    private static string Ok(string data) => "{\"respond\":{\"status\":200,\"body\":{\"data\":" + data + "}}}";

    // one exchange answering the API's error envelope
    private static string Error(int status, string code, bool retryable, string? retryAfter = null) =>
        "{\"respond\":{\"status\":" + status + ",\"headers\":" + (retryAfter is null ? "{}" : "{\"retry-after\":\"" + retryAfter + "\"}")
        + ",\"body\":{\"error\":{\"type\":\"x\",\"code\":\"" + code + "\",\"message\":\"x\",\"request_id\":\"req_1\",\"retryable\":" + (retryable ? "true" : "false")
        + ",\"doc_url\":\"https://docs.phoenixperpsfunding.com/reference/errors#" + code + "\"}}}}";

    private static string Expect(string op) => "{\"expect\":\"" + op + "\"}";

    private static string Subscribed(string channel, params string[] symbols) =>
        "{\"reply\":{\"type\":\"subscribed\",\"channel\":\"" + channel + "\",\"symbols\":[" + string.Join(",", symbols.Select(s => "\"" + s + "\"")) + "],\"rejected\":[]}}";

    private static async Task<MarketFrame> Next(MarketStream m, string type)
    {
        using var timeout = new CancellationTokenSource(Wait);
        while (true)
        {
            var f = await m.ReadAsync(timeout.Token);
            if (f.Type == type) return f;
        }
    }

    // I18: a 409 operation_in_progress (retryable) is asked again with the same key; a 503 the API marks not retryable is not
    [Fact]
    public async Task RetriesWhatTheApiMarksRetryable()
    {
        using var c = Client();
        await mock.Load(Exchanges(Error(409, "operation_in_progress", true, "0"), """{"respond":{"status":201,"body":{"data":{"id":"ord_1","status":"working"}}}}"""));
        Assert.Equal("ord_1", (await c.Orders.CreateAsync("SANDBOX-00042-001", Limit)).Id);
        var got = await Requests();
        Assert.Equal(2, got.Count);
        Assert.Single(got.Select(r => r.GetProperty("headers").GetProperty("idempotency-key").GetString()).Distinct());

        await mock.Load(Exchanges(Error(503, "sandbox_disabled", false, "0"), Ok("""{"market":"BTC-USD"}""")));
        Assert.Equal("sandbox_disabled", (await Assert.ThrowsAsync<ServerException>(() => c.Markets.GetAsync("BTC-USD"))).Code);
        Assert.Single(await Requests());
    }

    // I78: the envelope's retryable and doc_url reach the exception (null when there is no envelope)
    [Fact]
    public async Task ErrorsKeepRetryableAndDocUrl()
    {
        using var c = Client();
        await mock.Load(Exchanges(Error(409, "account_not_ready", true)));
        var e = await Assert.ThrowsAsync<ConflictException>(() => c.Orders.ModifyAsync("SANDBOX-00042-001", "ord_1", new ModifyOrder { Size = 0.02m }));
        Assert.Equal((true, "https://docs.phoenixperpsfunding.com/reference/errors#account_not_ready"), (e.Retryable, e.DocUrl));
        Assert.StartsWith("application/json", e.Headers!["content-type"]);
        // without the envelope, by the status
        await mock.Load(Exchanges("""{"respond":{"status":502,"body":"<html>Bad Gateway</html>"}}"""));
        Assert.True((await Assert.ThrowsAsync<ServerException>(() => c.Orders.ModifyAsync("SANDBOX-00042-001", "ord_1", new ModifyOrder { Size = 0.02m }))).Retryable);
        await mock.Load(Exchanges("""{"respond":{"status":400,"body":"<html>Bad Request</html>"}}"""));
        Assert.False((await Assert.ThrowsAsync<ValidationException>(() => c.Orders.ModifyAsync("SANDBOX-00042-001", "ord_1", new ModifyOrder { Size = 0.02m }))).Retryable);
    }

    // a change sent with an Idempotency-Key is safe to send again: it is, with the same key
    [Fact]
    public async Task ChangeWithAKeyIsTriedAgain()
    {
        using var c = Client();
        await mock.Load(Exchanges(Error(503, "engine_busy", true, "0"), Ok("""{"id":"ord_1","status":"working"}""")));
        await c.Orders.ModifyAsync("SANDBOX-00042-001", "ord_1", new ModifyOrder { Size = 0.02m }, new RequestOptions { IdempotencyKey = "change-1" });
        Assert.Equal(new[] { "change-1", "change-1" }, (await Requests()).Select(r => r.GetProperty("headers").GetProperty("idempotency-key").GetString()));
    }

    // live or sandbox in any case; the key's own environment before PHOENIXPERPS_ENVIRONMENT
    [Fact]
    public void EnvironmentInAnyCaseAndTheKeysOwnFirst()
    {
        using var upper = new PhoenixPerpsClient(new PhoenixPerpsClientOptions { ApiKey = mock.Key, Environment = "SANDBOX" });
        Assert.Equal("sandbox", upper.Environment);
        var before = Environment.GetEnvironmentVariable("PHOENIXPERPS_ENVIRONMENT");
        Environment.SetEnvironmentVariable("PHOENIXPERPS_ENVIRONMENT", "sandbox");
        try
        {
            using var live = new PhoenixPerpsClient(new PhoenixPerpsClientOptions { ApiKey = "ppk_live_" + mock.Key[9..] });
            Assert.Equal("live", live.Environment);
        }
        finally
        {
            Environment.SetEnvironmentVariable("PHOENIXPERPS_ENVIRONMENT", before);
        }
    }

    // a window used up on one budget (trade) does not hold a call of another (read)
    [Fact]
    public async Task RateLimitWindowsArePerBudget()
    {
        using var c = Client();
        await mock.Load(Exchanges("""{"respond":{"status":200,"headers":{"x-ratelimit-limit":"10","x-ratelimit-remaining":"0","x-ratelimit-reset":"3"},"body":{"data":{}}}}""", Ok("{}")));
        var trade = new Operation("trade", "GET", "/v1/me", false, true, null, "trade");
        await c.SendAsync(trade, null, null, null, null, default);
        var watch = Stopwatch.StartNew();
        await c.SendAsync(trade with { Scope = "read" }, null, null, null, null, default);
        Assert.True(watch.ElapsedMilliseconds < 1500, $"{watch.ElapsedMilliseconds} ms");
    }

    // a 304 on a list is no page at all, and the answer's meta carries it and its ETag
    [Fact]
    public async Task NotModifiedListAnswersNothing()
    {
        using var c = Client();
        await mock.Load(Exchanges("""{"respond":{"status":304,"headers":{"etag":"\"v1-abc\""}}}"""));
        RawResponse? raw = null;
        var options = new RequestOptions { Headers = new Dictionary<string, string> { ["If-None-Match"] = "\"v1-abc\"" }, OnResponse = r => raw = r };
        Assert.Null(await c.Orders.ListAsync("SANDBOX-00042-001", null, options));
        Assert.Equal((304, "\"v1-abc\""), (raw!.StatusCode, raw.Headers["etag"]));
        Assert.Equal("\"v1-abc\"", (await Requests())[0].GetProperty("headers").GetProperty("if-none-match").GetString());
    }

    // I79: an unhandled exception prints the code, the status and the request id
    [Fact]
    public void PrintedErrorCarriesCodeAndRequestId()
    {
        var e = new ServerException("state_unavailable", "The account's figures cannot be read right now.", 503) { RequestId = "req_5f5b" };
        Assert.StartsWith("PhoenixPerps.ServerException: The account's figures cannot be read right now. (state_unavailable, HTTP 503, req_5f5b)", e.ToString());
    }

    // I75: the REST address the docs show (host/v1) works as the base URL
    [Fact]
    public async Task BaseUrlMayEndInV1()
    {
        using var c = Client(new PhoenixPerpsClientOptions { BaseUrl = mock.Url + "/v1/" });
        Assert.Equal(mock.Url, c.BaseUrl);
        await mock.Load(Exchanges(Ok("{}")));
        await c.Me.GetAsync();
        Assert.Equal("/v1/me", (await Requests())[0].GetProperty("path").GetString());
    }

    // I81: any route, with the client's key, retries and errors
    [Fact]
    public async Task SendsAnyRequest()
    {
        using var c = Client();
        await mock.Load(Exchanges(Error(503, "engine_busy", true, "0"), Ok("""{"a":1}""")));
        var raw = await c.SendAsync("get", "/v1/new-thing", new[] { new KeyValuePair<string, string>("x", "1 2") });
        Assert.Equal(1, JsonDocument.Parse(raw.Body).RootElement.GetProperty("data").GetProperty("a").GetInt32());
        var got = await Requests();
        Assert.Equal(2, got.Count);
        Assert.Equal(("/v1/new-thing", "1 2", "Bearer " + mock.Key), (got[1].GetProperty("path").GetString(), got[1].GetProperty("query").GetProperty("x").GetString(), got[1].GetProperty("headers").GetProperty("authorization").GetString()));

        await mock.Load(Exchanges(Error(503, "engine_busy", true, "0")));
        await Assert.ThrowsAsync<ServerException>(() => c.SendAsync("POST", "/v1/new-thing", body: """{"a":1}"""));
        got = await Requests();
        Assert.Single(got); // a write without a key is never sent twice
        Assert.Equal(1, got[0].GetProperty("body").GetProperty("a").GetInt32());
        await Assert.ThrowsAsync<ValidationException>(() => c.SendAsync("GET", "v1/me"));

        // a route the spec marks retry-safe (PUT margin) is tried again as its typed call would be
        await mock.Load(Exchanges(Error(503, "engine_busy", true, "0"), Ok("{}")));
        await c.SendAsync("PUT", "/v1/accounts/SANDBOX-00042-001/margin/BTC-USD", body: """{"mode":"isolated"}""");
        Assert.Equal(2, (await Requests()).Count);
    }

    // I84: a list's own fields (history_complete) are kept
    [Fact]
    public async Task PageKeepsTheListsOtherFields()
    {
        using var c = Client();
        await mock.Load(Exchanges("""{"respond":{"status":200,"body":{"data":[],"has_more":false,"next_cursor":null,"as_of":1,"history_complete":false}}}"""));
        var page = await c.Orders.ListAsync("SANDBOX-00042-001");
        Assert.False(page.Extra!["history_complete"].GetBoolean());
    }

    // I131: the HttpClient's own Timeout still caps an attempt, and the error says it was that one
    [Fact]
    public async Task TimeoutNamesTheOneThatEnded()
    {
        using var http = new HttpClient { Timeout = TimeSpan.FromMilliseconds(300) };
        using var c = Client(new PhoenixPerpsClientOptions { HttpClient = http, Timeout = TimeSpan.FromSeconds(10), MaxRetries = 0 });
        await mock.Load(Exchanges("""{"respond":{"status":200,"delay_ms":2000,"body":{"data":{}}}}"""));
        Assert.Equal("No answer within 0.3 s.", (await Assert.ThrowsAsync<ApiTimeoutException>(() => c.Me.GetAsync())).Message);
    }

    // I137: the OS as windows, linux or darwin
    [Fact]
    public void UserAgentNamesTheOs()
    {
        using var c = Client();
        var os = System.Text.RegularExpressions.Regex.Match(c.UserAgent, @"\(\.NET [^;]+; ([^)]+)\)").Groups[1].Value;
        Assert.Contains(os, new[] { "windows", "linux", "darwin", "freebsd" });
    }

    // I86: the side in any casing, or as OrderSide; anything else, or a slippage outside the market's bounds, is the
    // SDK's own ValidationException
    [Fact]
    public void WorstPriceTakesTheSideWhateverItsCasing()
    {
        var r = new MarketRules(new Market { Value = "BTC-USD", TickSize = 0.1m, SizeStep = 0.001m, MinSize = 0.001m, MaxSlippagePct = new MarketMaxSlippagePct { Default = 1m, Min = 0.05m, Max = 5m } });
        // a decimal at the tick's decimals, as an order takes it
        Assert.Equal("62620.0", r.WorstPrice("BUY", 62000m).ToString(CultureInfo.InvariantCulture));
        Assert.Equal("62620.0", r.WorstPrice(OrderSide.Buy, 62000m).ToString(CultureInfo.InvariantCulture));
        Assert.Equal("61380.0", r.WorstPrice("Sell", 62000m).ToString(CultureInfo.InvariantCulture));
        var order = new CreateOrder { Market = "BTC-USD", Side = OrderSide.Buy, Type = CreateOrderType.Limit, Size = r.SizeForNotional(100m, 62000m), LimitPrice = r.RoundPrice(61999.96m) };
        Assert.Equal((0.001m, 62000.0m), (order.Size!.Value, order.LimitPrice!.Value));
        Assert.Throws<ValidationException>(() => r.WorstPrice("long", 62000m));
        Assert.Throws<ValidationException>(() => r.WorstPrice(OrderSide.Buy, 62000m, 0.01m));
    }

    // I134: a zero stop distance or price is a typed refusal, not a DivideByZeroException
    [Fact]
    public void SizeHelpersRefuseZero()
    {
        var r = new MarketRules(new Market { Value = "BTC-USD", TickSize = 0.1m, SizeStep = 0.001m, MinSize = 0.001m });
        Assert.Throws<ValidationException>(() => r.SizeForRisk(50m, 0m));
        Assert.Throws<ValidationException>(() => r.SizeForNotional(10m, 0m));
    }

    private static Dictionary<string, string> Signed(string timestamp, string body) => new()
    {
        ["X-Phoenix-Timestamp"] = timestamp,
        ["X-Phoenix-Signature"] = "sha256=" + Convert.ToHexString(HMACSHA256.HashData(Encoding.UTF8.GetBytes("whsec_1"), Encoding.UTF8.GetBytes(timestamp + "." + body))).ToLowerInvariant(),
    };

    // I133: a signed body that is not an event (not JSON, or no id and type) is a WebhookVerificationException bad_body,
    // one of the SDK's own errors
    [Fact]
    public void WebhookBodyThatIsNotAnEvent()
    {
        var now = DateTimeOffset.FromUnixTimeSeconds(1791460800);
        PhoenixPerpsException e = Assert.Throws<WebhookVerificationException>(() => Webhooks.Verify("not json", Signed("1791460800", "not json"), "whsec_1", null, now));
        Assert.Equal("bad_body", e.Code);
        Assert.Equal("bad_body", Assert.Throws<WebhookVerificationException>(() => Webhooks.Verify("{}", Signed("1791460800", "{}"), "whsec_1", null, now)).Code);
    }

    // a timestamp that is a number, but beyond any clock, is stale (never the runtime's own out-of-range exception)
    [Fact]
    public void WebhookTimestampBeyondAnyClockIsStale()
    {
        var body = """{"id":"evt_1","type":"order.filled"}""";
        Assert.Equal("stale", Assert.Throws<WebhookVerificationException>(() => Webhooks.Verify(body, Signed("9999999999999", body), "whsec_1")).Code);
    }

    // I48: a second DisposeAsync (an await using after an explicit one) does nothing. Both streams are connected first:
    // the mock refuses an upgrade it has no script for on a socket that an aborting client can crash it through.
    [Fact]
    public async Task StreamsDisposeTwice()
    {
        await mock.Load(Connections(
            new[] { AccountHello, Expect("subscribe"), """{"send":{"type":"snapshot_end","accounts":[]}}""", Linger },
            new[] { MarketHello, Expect("subscribe"), Subscribed("ticker", "BTC-USD"), Linger }));
        using var c = Client();
        var a = c.OpenAccountStream();
        await a.Ready.WaitAsync(Wait);
        await a.DisposeAsync();
        await a.DisposeAsync();
        var m = c.OpenMarketStream();
        await m.SubscribeAsync("ticker", new[] { "BTC-USD" }).WaitAsync(Wait);
        await m.DisposeAsync();
        await m.DisposeAsync();
    }

    // a stream disposed before its first snapshots fails Ready (StreamClosedException), never leaves it pending
    [Fact]
    public async Task DisposeBeforeTheSnapshotsFailsReady()
    {
        await mock.Load(Connections(new[] { AccountHello, Expect("subscribe"), Linger }));
        using var c = Client();
        var s = c.OpenAccountStream(new AccountStreamOptions { Accounts = new[] { "SANDBOX-00042-001" } });
        await Task.Delay(300); // connected and subscribed: the mock is not left with an aborted upgrade
        await s.DisposeAsync();
        Assert.Equal("stream_closed", (await Assert.ThrowsAsync<StreamClosedException>(() => s.Ready.WaitAsync(Wait))).Code);
    }

    // a decimal the answer leaves null (or a word) is an answer that cannot be read: invalid_response, never a .NET exception
    [Fact]
    public async Task NullDecimalIsInvalidResponse()
    {
        await mock.Load(Exchanges(Ok("""{"market":"BTC-USD","tick_size":null}"""), Ok("""{"market":"BTC-USD","tick_size":"abc"}""")));
        using var c = Client();
        Assert.Equal("invalid_response", (await Assert.ThrowsAsync<ServerException>(() => c.Markets.GetAsync("BTC-USD"))).Code);
        Assert.Equal("invalid_response", (await Assert.ThrowsAsync<ServerException>(() => c.Markets.GetAsync("BTC-USD"))).Code);
    }

    // I21: an error frame refusing the subscribe ends the stream (no snapshot_end comes: Ready fails), never reconnecting
    [Fact]
    public async Task AccountErrorFrameEndsTheStream()
    {
        await mock.Load(Connections(new[] { AccountHello, Expect("subscribe"), """{"send":{"type":"error","code":"too_many_accounts","details":{"max":50}}}""", Linger }));
        using var c = Client();
        await using var s = c.OpenAccountStream();
        var e = await Assert.ThrowsAsync<ValidationException>(() => s.Ready.WaitAsync(Wait));
        Assert.Equal(("too_many_accounts", 50), (e.Code, e.Details!.Value.GetProperty("max").GetInt32()));
        Assert.Equal("too_many_accounts", (await Assert.ThrowsAsync<ValidationException>(async () => await s.ReadAsync())).Code);
        await Task.Delay(500); // time enough for a wrong reconnect to show
        Assert.Single(await Requests());
    }

    // I21: account_not_found names the account (a snapshot_end follows it)
    [Fact]
    public async Task AccountNotFoundNamesTheAccount()
    {
        await mock.Load(Connections(new[] { AccountHello, Expect("subscribe"), """{"send":{"type":"error","code":"account_not_found","account_id":"SANDBOX-00042-009"}}""", """{"send":{"type":"snapshot_end","accounts":[]}}""", Linger }));
        using var c = Client();
        await using var s = c.OpenAccountStream(new AccountStreamOptions { Accounts = new[] { "SANDBOX-00042-009" } });
        var e = await Assert.ThrowsAsync<ValidationException>(() => s.Ready.WaitAsync(Wait));
        Assert.Equal(("account_not_found", "SANDBOX-00042-009"), (e.Code, e.Details!.Value.GetProperty("account_id").GetString()));
    }

    // I21: 4001 before the first snapshots fails Ready too
    [Fact]
    public async Task AccountKeyEndedFailsReady()
    {
        await mock.Load(Connections(new[] { AccountHello, Expect("subscribe"), """{"close":4001}""" }));
        using var c = Client();
        await using var s = c.OpenAccountStream();
        Assert.Equal("stream_credential_ended", (await Assert.ThrowsAsync<AuthenticationException>(() => s.Ready.WaitAsync(Wait))).Code);
    }

    // I23: three heartbeats of silence on a market connection: it reconnects and subscribes again
    [Fact]
    public async Task MarketReconnectsAfterThreeSilentHeartbeats()
    {
        await mock.Load(Connections(
            new[] { """{"send":{"type":"hello","heartbeat_ms":100}}""", Expect("subscribe"), Subscribed("ticker", "BTC-USD") },
            new[] { """{"send":{"type":"hello","heartbeat_ms":15000}}""", Expect("subscribe"), Subscribed("ticker", "BTC-USD"), Ticker, Linger }));
        using var c = Client();
        await using var m = c.OpenMarketStream();
        await m.SubscribeAsync("ticker", new[] { "BTC-USD" }, Every250);
        await Next(m, "ticker");
        var got = await Requests();
        Assert.Equal(2, got.Count);
        Assert.Equal("""["BTC-USD"]""", got[1].GetProperty("messages")[0].GetProperty("symbols").GetRawText());
    }

    // I25: an unsubscribe without the subscribe's options still stops the market for good; a candles one carries its interval
    [Fact]
    public async Task MarketUnsubscribedStaysUnsubscribedAfterAReconnect()
    {
        const string unsubscribed = """{"reply":{"type":"unsubscribed"}}""";
        await mock.Load(Connections(
            new[] { MarketHello, Expect("subscribe"), Subscribed("ticker", "BTC-USD", "ETH-USD"), Expect("subscribe"), Subscribed("candles", "BTC-USD"), Expect("unsubscribe"), unsubscribed, Expect("unsubscribe"), unsubscribed, """{"close":1012}""" },
            new[] { MarketHello, Expect("subscribe"), Subscribed("ticker", "BTC-USD"), Ticker, Linger }));
        using var c = Client();
        await using var m = c.OpenMarketStream();
        await m.SubscribeAsync("ticker", new[] { "BTC-USD", "ETH-USD" }, Every250);
        await m.SubscribeAsync("candles", new[] { "BTC-USD" }, new Dictionary<string, object?> { ["interval"] = "1m", ["history"] = 0 });
        await m.UnsubscribeAsync("ticker", new[] { "ETH-USD" });
        await m.UnsubscribeAsync("candles", new[] { "btc-usd" }, new Dictionary<string, object?> { ["interval"] = "1m" });
        await Next(m, "ticker");
        await Task.Delay(300); // time enough for a wrong second resubscribe to arrive
        var got = await Requests();
        Assert.Equal("1m", got[0].GetProperty("messages")[3].GetProperty("interval").GetString());
        var again = got[1].GetProperty("messages");
        Assert.Equal(1, again.GetArrayLength());
        Assert.Equal(("""["BTC-USD"]""", 250), (again[0].GetProperty("symbols").GetRawText(), again[0].GetProperty("interval_ms").GetInt32()));
    }

    // I130: a market the server rejected is not asked for again on the next connection
    [Fact]
    public async Task MarketRejectedIsNotAskedAgain()
    {
        await mock.Load(Connections(
            new[] { MarketHello, Expect("subscribe"), """{"reply":{"type":"subscribed","channel":"ticker","symbols":["BTC-USD"],"rejected":[{"symbol":"NOPE-USD","code":"market_not_found"}]}}""", """{"close":1012}""" },
            new[] { MarketHello, Expect("subscribe"), Subscribed("ticker", "BTC-USD"), Ticker, Linger }));
        using var c = Client();
        await using var m = c.OpenMarketStream();
        await m.SubscribeAsync("ticker", new[] { "BTC-USD", "NOPE-USD" }, Every250);
        await Next(m, "ticker");
        Assert.Equal("""["BTC-USD"]""", (await Requests())[1].GetProperty("messages")[0].GetProperty("symbols").GetRawText());
    }

    // I90: a market error frame keeps its details
    [Fact]
    public async Task MarketErrorKeepsItsDetails()
    {
        await mock.Load(Connections(new[] { MarketHello, Expect("subscribe"), """{"reply":{"type":"error","code":"invalid_parameter","details":{"field":"interval","issue":"1m 3m 5m"}}}""", Linger }));
        using var c = Client();
        await using var m = c.OpenMarketStream();
        var e = await Assert.ThrowsAsync<ValidationException>(() => m.SubscribeAsync("candles", new[] { "BTC-USD" }, new Dictionary<string, object?> { ["interval"] = "2m" }).WaitAsync(Wait));
        Assert.Equal(("invalid_parameter", "interval"), (e.Code, e.Details!.Value.GetProperty("field").GetString()));
        Assert.Contains("interval", e.Message);
    }

    // I82: list and ping, and the interval of a candles frame
    [Fact]
    public async Task MarketListPingAndCandleInterval()
    {
        await mock.Load(Connections(new[]
        {
            MarketHello, Expect("list"), """{"reply":{"type":"list","subscriptions":[{"channel":"candles","symbol":"BTC-USD","interval":"1m"}]}}""",
            Expect("ping"), """{"reply":{"type":"pong","time":1}}""", """{"send":{"type":"candles","symbol":"BTC-USD","interval":"5m","data":[]}}""", Linger,
        }));
        using var c = Client();
        await using var m = c.OpenMarketStream();
        Assert.Equal("1m", (await m.ListAsync().WaitAsync(Wait)).GetProperty("subscriptions")[0].GetProperty("interval").GetString());
        Assert.Equal("pong", (await m.PingAsync().WaitAsync(Wait)).GetProperty("type").GetString());
        Assert.Equal("5m", (await Next(m, "candles")).Interval);
    }

    private static string EventFrame(string id, int seq, bool replayed) =>
        "{\"send\":{\"type\":\"event\",\"seq\":" + seq + (replayed ? ",\"replayed\":true" : "")
        + ",\"event\":{\"id\":\"" + id + "\",\"type\":\"order.created\",\"account_id\":\"SANDBOX-00042-001\",\"time\":1791460800000,\"source\":\"api\",\"data\":{}}}}";

    // I82, I91: the account stream's ping and unsubscribe (the subscribe sent again names what is left), each event's seq
    // and replayed, risk frames told as they come; one log line per reconnect, with its cause
    [Fact]
    public async Task AccountPingUnsubscribeRiskAndSeq()
    {
        const string hello = """{"send":{"type":"hello","stream_id":"str_1","environment":"sandbox","heartbeat_ms":15000,"accounts":["SANDBOX-00042-001","SANDBOX-00042-002"]}}""";
        const string end = """{"send":{"type":"snapshot_end","accounts":[]}}""";
        await mock.Load(Connections(
            new[]
            {
                hello, Expect("subscribe"), end, EventFrame("evt_1", 1, false), """{"send":{"type":"risk","account_id":"SANDBOX-00042-001","data":{"equity":"10000"}}}""",
                Expect("ping"), """{"send":{"type":"pong","time":1}}""", Expect("unsubscribe"), """{"send":{"type":"unsubscribed","accounts":["SANDBOX-00042-002"]}}""", """{"close":1012}""",
            },
            new[] { hello, Expect("subscribe"), end, EventFrame("evt_2", 1, true), Linger }));
        var lines = new List<string>();
        var risks = new List<(string, decimal)>();
        using var c = Client(new PhoenixPerpsClientOptions { Logger = (_, line) => { lock (lines) lines.Add(line); } });
        await using var s = c.OpenAccountStream(new AccountStreamOptions { Risk = true, OnRisk = (account, risk) => risks.Add((account, risk.Equity)) });
        using var timeout = new CancellationTokenSource(Wait);
        var first = await s.ReadAsync(timeout.Token);
        Assert.Equal(("evt_1", (long?)1, false), (first.Id, first.Seq, first.Replayed));
        Assert.Equal("pong", (await s.PingAsync(timeout.Token)).GetProperty("type").GetString());
        Assert.Equal(("SANDBOX-00042-001", 10000m), Assert.Single(risks));
        await s.UnsubscribeAsync(new[] { "SANDBOX-00042-002" }, timeout.Token);
        var second = await s.ReadAsync(timeout.Token);
        Assert.Equal(("evt_2", (long?)1, true), (second.Id, second.Seq, second.Replayed));
        var got = await Requests();
        Assert.Equal("""{"op":"subscribe","risk":true}""", got[0].GetProperty("messages")[0].GetRawText());
        Assert.Equal("""{"op":"subscribe","accounts":["SANDBOX-00042-001"],"risk":true}""", got[1].GetProperty("messages")[0].GetRawText());
        lock (lines) Assert.Contains(lines, l => l.Contains("account stream closed (1012): reconnecting"));
    }

    // a candles unsubscribe naming no interval ends every interval; one naming no market every market of the channel
    [Fact]
    public async Task MarketUnsubscribeWithoutIntervalOrMarketsEndsThemAll()
    {
        const string unsubscribed = """{"reply":{"type":"unsubscribed"}}""";
        await mock.Load(Connections(
            new[]
            {
                MarketHello, Expect("subscribe"), Subscribed("candles", "BTC-USD"), Expect("subscribe"), Subscribed("candles", "BTC-USD"), Expect("subscribe"), Subscribed("ticker", "BTC-USD", "ETH-USD"),
                Expect("subscribe"), Subscribed("trades", "BTC-USD"), Expect("unsubscribe"), unsubscribed, Expect("unsubscribe"), unsubscribed, """{"close":1012}""",
            },
            new[] { MarketHello, Expect("subscribe"), Subscribed("trades", "BTC-USD"), """{"send":{"type":"trades","symbol":"BTC-USD","data":[]}}""", Linger }));
        using var c = Client();
        await using var m = c.OpenMarketStream();
        await m.SubscribeAsync("candles", new[] { "BTC-USD" }, new Dictionary<string, object?> { ["interval"] = "1m" });
        await m.SubscribeAsync("candles", new[] { "BTC-USD" }, new Dictionary<string, object?> { ["interval"] = "5m" });
        await m.SubscribeAsync("ticker", new[] { "BTC-USD", "ETH-USD" }, Every250);
        await m.SubscribeAsync("trades", new[] { "BTC-USD" });
        await m.UnsubscribeAsync("candles", new[] { "BTC-USD" });
        await m.UnsubscribeAsync("ticker");
        await Next(m, "trades");
        await Task.Delay(300); // time enough for a wrong resubscribe to arrive
        var again = (await Requests())[1].GetProperty("messages");
        Assert.Equal(1, again.GetArrayLength());
        Assert.Equal("trades", again[0].GetProperty("channel").GetString());
    }

    // a client without a key opens a stream without an Authorization header too
    [Fact]
    public async Task KeylessStreamSendsNoAuthorization()
    {
        await mock.Load(Connections(new[] { MarketHello, Expect("ping"), """{"reply":{"type":"pong","time":1}}""", Linger }));
        using var c = new PhoenixPerpsClient(new PhoenixPerpsClientOptions { Environment = "sandbox", BaseUrl = mock.Url, MarketStreamUrl = "ws" + mock.Url[4..] + "/v1/market-data" });
        await using var m = c.OpenMarketStream();
        await m.PingAsync().WaitAsync(Wait);
        Assert.False((await Requests())[0].GetProperty("headers").TryGetProperty("authorization", out _));
    }
}
