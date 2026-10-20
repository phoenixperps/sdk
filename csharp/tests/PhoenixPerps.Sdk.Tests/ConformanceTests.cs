// The conformance suite (conformance/scenarios.json) through the C# SDK against the mock server
// (node conformance/mock-server.js): retries, idempotency keys, Retry-After, errors, 304, paging, open enums, decimals,
// the rate-limit wait, the streams (resume, gaps, refused upgrades, error frames, the market watchdog, unsubscribes);
// and the local checks (wrong environment, the key never printed, the keyless client, webhook signatures, exact
// rounding). Every scenario and vector is played; a key the runner does not know fails it.
using System.Diagnostics;
using System.Globalization;
using System.Text;
using System.Text.Json;
using System.Text.Json.Nodes;
using System.Text.RegularExpressions;
using Xunit;

namespace PhoenixPerps.Tests;

internal sealed class Call
{
    private readonly JsonElement _e;

    internal Call(JsonElement e) => _e = e;

    internal string Resource => _e.GetProperty("resource").GetString()!;

    internal string Method => _e.GetProperty("method").GetString()!;

    internal bool Iterate => _e.TryGetProperty("iterate", out var v) && v.GetBoolean();

    internal int? MaxItems => _e.TryGetProperty("max_items", out var v) ? v.GetInt32() : null;

    internal string Path(string name) => _e.GetProperty("path").GetProperty(name).GetString()!;

    internal T? Body<T>(System.Text.Json.Serialization.Metadata.JsonTypeInfo<T> type) where T : class =>
        _e.TryGetProperty("body", out var b) ? b.Deserialize(type) : null;

    internal T? Query<T>(System.Text.Json.Serialization.Metadata.JsonTypeInfo<T> type) where T : class =>
        _e.TryGetProperty("query", out var q) ? q.Deserialize(type) : null;
}

public sealed class MockServer : IDisposable
{
    private static readonly HttpClient Http = new();
    private readonly Process _p;

    public MockServer()
    {
        Root = FindRoot();
        _p = Process.Start(new ProcessStartInfo("node", $"\"{System.IO.Path.Combine(Root, "conformance", "mock-server.js")}\" 0") { RedirectStandardOutput = true, UseShellExecute = false })!;
        Url = _p.StandardOutput.ReadLine()!.Trim();
        Suite = JsonDocument.Parse(File.ReadAllText(System.IO.Path.Combine(Root, "conformance", "scenarios.json"))).RootElement;
    }

    public string Root { get; }

    public string Url { get; }

    public JsonElement Suite { get; }

    public string Key => Suite.GetProperty("key").GetString()!;

    /// <summary>Plays one scenario ({ exchanges, connections }) next, forgetting the requests.</summary>
    public async Task Load(string scenario) =>
        (await Http.PostAsync(Url + "/__scenario", new StringContent(scenario, Encoding.UTF8, "application/json"))).Dispose();

    /// <summary>Every request (and WebSocket upgrade, with its messages) since the scenario was loaded.</summary>
    public async Task<JsonElement> Requests() => JsonDocument.Parse(await Http.GetStringAsync(Url + "/__requests")).RootElement;

    internal static string FindRoot() =>
        typeof(MockServer).Assembly.GetCustomAttributes(typeof(System.Reflection.AssemblyMetadataAttribute), false).Cast<System.Reflection.AssemblyMetadataAttribute>().First(m => m.Key == "SdkRoot").Value!;

    public void Dispose()
    {
        try
        {
            _p.Kill();
        }
        catch (InvalidOperationException)
        {
        }
        _p.Dispose();
    }
}

internal static partial class Conformance
{
    internal static async Task<List<string>> Ids<T>(IAsyncEnumerable<T> items)
    {
        var ids = new List<string>();
        await foreach (var item in items)
        {
            var el = JsonSerializer.SerializeToElement(item, (System.Text.Json.Serialization.Metadata.JsonTypeInfo<T>)Json.Default.GetTypeInfo(typeof(T))!);
            ids.Add(el.GetProperty("id").GetString()!);
        }
        return ids;
    }
}

public sealed class ConformanceTests(MockServer mock) : IClassFixture<MockServer>
{
    private static readonly Dictionary<string, Type> Classes = new()
    {
        ["AuthenticationError"] = typeof(AuthenticationException),
        ["PermissionError"] = typeof(PermissionException),
        ["NotFoundError"] = typeof(NotFoundException),
        ["ConflictError"] = typeof(ConflictException),
        ["ValidationError"] = typeof(ValidationException),
        ["RuleRejectedError"] = typeof(RuleRejectedException),
        ["RateLimitError"] = typeof(RateLimitException),
        ["ServerError"] = typeof(ServerException),
    };

    // what this runner knows of the vocabulary (scenarios.json "vocabulary"): anything else fails the scenario
    private static readonly HashSet<string> RestKeys = new() { "name", "client", "call", "calls", "exchanges", "expect" };
    private static readonly HashSet<string> RestExpect = new() { "ok", "requests", "result", "error", "items", "cursors", "first_body", "min_elapsed_ms", "checks", "page_fields", "paths" };
    private static readonly HashSet<string> StreamKeys = new() { "name", "stream", "connections", "subscribe", "unsubscribe", "expect" };
    private static readonly HashSet<string> StreamExpect = new() { "events", "connections", "resume", "first_message", "error", "frames", "resubscribe", "unsubscribe_sent", "limits", "gap" };
    private static readonly HashSet<string> ErrorKeys = new() { "class", "code", "status", "retry_after_ms", "order_id", "request_id", "retryable", "doc_url", "account_id" };

    public static IEnumerable<object[]> Scenarios() => Names("scenarios");

    public static IEnumerable<object[]> Streams() => Names("streams");

    public static IEnumerable<object[]> Locals() => Names("local");

    private static void Known(JsonElement o, HashSet<string> keys, string what)
    {
        foreach (var p in o.EnumerateObject()) Assert.True(keys.Contains(p.Name), $"{what}: the runner does not know {p.Name}");
    }

    // the error a scenario expects: its class (when named), code, and whichever other field it names
    private static void SameError(Exception? error, JsonElement xe)
    {
        Known(xe, ErrorKeys, "error");
        var e = Assert.IsAssignableFrom<PhoenixPerpsException>(error);
        if (xe.TryGetProperty("class", out var cls)) Assert.Equal(Classes[cls.GetString()!], e.GetType());
        Assert.Equal(xe.GetProperty("code").GetString(), e.Code);
        if (xe.TryGetProperty("status", out var st)) Assert.Equal(st.GetInt32(), e.Status);
        if (xe.TryGetProperty("retry_after_ms", out var ra)) Assert.Equal(ra.GetInt64(), (long)e.RetryAfter!.Value.TotalMilliseconds);
        if (xe.TryGetProperty("order_id", out var oid)) Assert.Equal(oid.GetString(), e.OrderId);
        if (xe.TryGetProperty("request_id", out var rid)) Assert.Equal(rid.GetString(), e.RequestId);
        if (xe.TryGetProperty("retryable", out var rt)) Assert.Equal(rt.GetBoolean(), e.Retryable);
        if (xe.TryGetProperty("doc_url", out var du)) Assert.Equal(du.GetString(), e.DocUrl);
        if (xe.TryGetProperty("account_id", out var aid)) Assert.Equal(aid.GetString(), e.Details?.GetProperty("account_id").GetString());
    }

    // a message as sent: the fields expected and no other (its id aside)
    private static void SameMessage(JsonElement got, JsonElement want)
    {
        Same(JsonNode.Parse(got.GetRawText()), want);
        foreach (var p in got.EnumerateObject()) Assert.True(p.Name == "id" || want.TryGetProperty(p.Name, out _), $"{got.GetRawText()} also sends {p.Name}");
    }

    private static IEnumerable<object[]> Names(string kind)
    {
        var s = JsonDocument.Parse(File.ReadAllText(Path.Combine(MockServer.FindRoot(), "conformance", "scenarios.json"))).RootElement;
        return s.GetProperty(kind).EnumerateArray().Select(x => new object[] { x.GetProperty("name").GetString()! });
    }

    private JsonElement Find(string kind, string name) => mock.Suite.GetProperty(kind).EnumerateArray().First(x => x.GetProperty("name").GetString() == name);

    private Task Load(JsonElement scenario) => mock.Load(JsonSerializer.Serialize(scenario, JsonCtx.Default.JsonElement));

    private Task<JsonElement> Requests() => mock.Requests();

    private static JsonNode? Pick(JsonNode? got, JsonNode? want)
    {
        if (want is not JsonObject w) return got?.DeepClone();
        var o = new JsonObject();
        foreach (var (k, v) in w) o[k] = Pick(got is JsonObject g ? g[k] : null, v);
        return o;
    }

    private static void Same(JsonNode? got, JsonElement want) =>
        Assert.True(JsonNode.DeepEquals(Pick(got, JsonNode.Parse(want.GetRawText())), JsonNode.Parse(want.GetRawText())), $"{got?.ToJsonString()} vs {want.GetRawText()}");

    [Theory]
    [MemberData(nameof(Scenarios))]
    public async Task Rest(string name)
    {
        var sc = Find("scenarios", name);
        Known(sc, RestKeys, name);
        await Load(JsonSerializer.SerializeToElement(new Dictionary<string, JsonElement> { ["exchanges"] = sc.GetProperty("exchanges") }, JsonCtx.Default.DictionaryStringJsonElement));
        var suffix = sc.TryGetProperty("client", out var cl) && cl.TryGetProperty("base_url_suffix", out var bs) ? bs.GetString() : "";
        using var c = new PhoenixPerpsClient(new PhoenixPerpsClientOptions { ApiKey = mock.Key, BaseUrl = mock.Url + suffix, MaxRetries = 2 });
        var calls = sc.TryGetProperty("calls", out var cs) ? cs.EnumerateArray().ToList() : new List<JsonElement> { sc.GetProperty("call") };
        JsonElement? result = null;
        List<string>? items = null;
        Exception? error = null;
        RawResponse? last = null;
        try
        {
            foreach (var call in calls) (result, items) = await Conformance.Dispatch(c, new Call(call), new RequestOptions { OnResponse = r => last = r });
        }
        catch (PhoenixPerpsException e)
        {
            error = e;
        }
        var got = (await Requests()).EnumerateArray().ToList();
        var x = sc.GetProperty("expect");
        Known(x, RestExpect, name);
        Assert.Equal(x.GetProperty("requests").GetInt32(), got.Count);
        if (x.TryGetProperty("ok", out var ok)) Assert.True(ok.GetBoolean() == error is null, $"ok {ok.GetBoolean()}, error {error}");
        if (x.TryGetProperty("error", out var xe)) SameError(error, xe);
        else
        {
            Assert.Null(error);
            if (x.TryGetProperty("result", out var want))
            {
                // null: the call answered nothing (a 304)
                if (want.ValueKind == JsonValueKind.Null) Assert.True(result is null or { ValueKind: JsonValueKind.Null }, $"{result} vs null");
                else Same(result is { } r ? JsonNode.Parse(r.GetRawText()) : null, want);
            }
            if (x.TryGetProperty("items", out var wi)) Assert.Equal(wi.EnumerateArray().Select(i => i.GetString()!).ToList(), items);
        }
        if (x.TryGetProperty("page_fields", out var pf))
        {
            // the dispatch table answers a page's data only: the page is read here again as the SDK's own Page reads it
            var page = JsonSerializer.Deserialize(last!.Body, JsonCtx.Default.PageJsonElement)!;
            foreach (var f in pf.EnumerateObject()) Assert.Equal(f.Value.GetRawText(), page.Extra![f.Name].GetRawText());
        }
        if (x.TryGetProperty("paths", out var paths)) Assert.Equal(paths.EnumerateArray().Select(p => p.GetString()), got.Select(r => r.GetProperty("path").GetString()));
        if (x.TryGetProperty("cursors", out var cur))
        {
            var wants = cur.EnumerateArray().ToList();
            for (var i = 0; i < wants.Count; i++)
            {
                var has = got[i].GetProperty("query").TryGetProperty("cursor", out var cv);
                Assert.Equal(wants[i].ValueKind == JsonValueKind.Null ? null : wants[i].GetString(), has ? cv.GetString() : null);
            }
        }
        if (x.TryGetProperty("first_body", out var fb)) Same(JsonNode.Parse(got[0].GetProperty("body").GetRawText()), fb);
        if (x.TryGetProperty("min_elapsed_ms", out var me)) Assert.True(got[^1].GetProperty("at").GetInt64() - got[0].GetProperty("at").GetInt64() >= me.GetInt64());
        if (x.TryGetProperty("checks", out var checks))
        {
            foreach (var check in checks.EnumerateArray().Select(v => v.GetString()))
            {
                Assert.Contains(check, new[] { "bearer", "user_agent", "same_idempotency_key" });
                var h = got[0].GetProperty("headers");
                if (check == "bearer") Assert.Equal("Bearer " + mock.Key, h.GetProperty("authorization").GetString());
                if (check == "user_agent") Assert.Matches(new Regex(@"^phoenixperps-csharp/\d+\.\d+\.\d+ \(.+\) spec/"), h.GetProperty("user-agent").GetString()!);
                if (check == "same_idempotency_key")
                {
                    var keys = got.Select(r => r.GetProperty("headers").GetProperty("idempotency-key").GetString()).Distinct().ToList();
                    Assert.Single(keys);
                    Assert.Matches(new Regex("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"), keys[0]!);
                }
            }
        }
    }

    [Theory]
    [MemberData(nameof(Streams))]
    public async Task Stream(string name)
    {
        var sc = Find("streams", name);
        Known(sc, StreamKeys, name);
        await Load(JsonSerializer.SerializeToElement(new Dictionary<string, JsonElement> { ["connections"] = sc.GetProperty("connections") }, JsonCtx.Default.DictionaryStringJsonElement));
        var ws = "ws" + mock.Url[4..];
        using var c = new PhoenixPerpsClient(new PhoenixPerpsClientOptions { ApiKey = mock.Key, BaseUrl = mock.Url, AccountStreamUrl = ws + "/v1/stream", MarketStreamUrl = ws + "/v1/market-data" });
        using var timeout = new CancellationTokenSource(TimeSpan.FromSeconds(15));
        var x = sc.GetProperty("expect");
        Known(x, StreamExpect, name);
        if (sc.GetProperty("stream").GetString() == "account")
        {
            var want = x.TryGetProperty("events", out var we) ? we.EnumerateArray().Select(e => e.GetString()!).ToList() : new List<string>();
            var ids = new List<string>();
            var gaps = new List<string?>();
            Exception? error = null;
            await using (var s = c.OpenAccountStream(new AccountStreamOptions { OnGap = after => gaps.Add(after) }))
            {
                try
                {
                    while (ids.Count < want.Count) ids.Add((await s.ReadAsync(timeout.Token)).Id);
                    if (x.TryGetProperty("error", out _)) await s.ReadAsync(timeout.Token);
                }
                catch (PhoenixPerpsException e)
                {
                    error = e;
                }
                if (x.TryGetProperty("error", out _)) await Task.Delay(500); // time enough for a wrong reconnect to show
            }
            Assert.Equal(want, ids);
            if (x.TryGetProperty("error", out var xe)) SameError(error, xe);
            if (x.TryGetProperty("gap", out var gap)) Assert.Equal(new[] { gap.GetString() }, gaps);
        }
        else
        {
            var sub = sc.GetProperty("subscribe");
            var want = x.GetProperty("frames").EnumerateArray().Select(e => e.GetString()!).ToList();
            var frames = new List<string>();
            JsonElement? limits;
            static Dictionary<string, object?> Params(JsonElement call) =>
                call.TryGetProperty("params", out var p) ? p.EnumerateObject().ToDictionary(kv => kv.Name, kv => (object?)kv.Value.Clone()) : new Dictionary<string, object?>();
            static IEnumerable<string> Symbols(JsonElement call) => call.GetProperty("symbols").EnumerateArray().Select(v => v.GetString()!);
            await using (var m = c.OpenMarketStream())
            {
                await m.SubscribeAsync(sub.GetProperty("channel").GetString()!, Symbols(sub), Params(sub), timeout.Token);
                if (sc.TryGetProperty("unsubscribe", out var unsub)) await m.UnsubscribeAsync(unsub.GetProperty("channel").GetString()!, Symbols(unsub), Params(unsub), timeout.Token);
                while (frames.Count < want.Count)
                {
                    var f = await m.ReadAsync(timeout.Token);
                    if (f.Type == sub.GetProperty("channel").GetString()) frames.Add(f.Data!.Value.GetProperty("last").GetString()!);
                }
                limits = m.Limits;
            }
            Assert.Equal(want, frames);
            if (x.TryGetProperty("limits", out var xl)) Same(JsonNode.Parse(limits!.Value.GetRawText()), xl);
        }
        var reqs = (await Requests()).EnumerateArray().ToList();
        Assert.Equal(x.GetProperty("connections").GetInt32(), reqs.Count);
        if (x.TryGetProperty("resume", out var resume))
        {
            var i = 0;
            foreach (var r in resume.EnumerateArray())
            {
                var has = reqs[i++].GetProperty("query").TryGetProperty("resume", out var v);
                Assert.Equal(r.ValueKind == JsonValueKind.Null ? null : r.GetString(), has ? v.GetString() : null);
            }
        }
        Assert.Equal("Bearer " + mock.Key, reqs[0].GetProperty("headers").GetProperty("authorization").GetString());
        if (x.TryGetProperty("first_message", out var fm)) Same(JsonNode.Parse(reqs[0].GetProperty("messages")[0].GetRawText()), fm);
        if (x.TryGetProperty("resubscribe", out var rs)) SameMessage(reqs[^1].GetProperty("messages")[0], rs);
        if (x.TryGetProperty("unsubscribe_sent", out var us))
            SameMessage(reqs.SelectMany(r => r.GetProperty("messages").EnumerateArray()).First(m => m.GetProperty("op").GetString() == "unsubscribe"), us);
    }

    [Theory]
    [MemberData(nameof(Locals))]
    public async Task Local(string name)
    {
        var l = Find("local", name);
        switch (l.TryGetProperty("check", out var check) ? check.GetString() : null)
        {
            case null:
                var cl = l.GetProperty("client");
                var e = Assert.ThrowsAny<PhoenixPerpsException>(() => new PhoenixPerpsClient(new PhoenixPerpsClientOptions { ApiKey = cl.GetProperty("api_key").GetString(), Environment = cl.GetProperty("environment").GetString(), BaseUrl = "http://127.0.0.1:9" }));
                Assert.Equal(l.GetProperty("expect").GetProperty("error_code").GetString(), e.Code);
                break;
            case "redaction":
                await Redaction();
                break;
            case "webhook_vectors":
                WebhookVectors();
                break;
            case "keyless":
                await Keyless(l.GetProperty("client"));
                break;
            default:
                Assert.Fail($"{name}: the runner does not know the check {check}");
                break;
        }
    }

    private async Task Redaction()
    {
        using var c = new PhoenixPerpsClient(new PhoenixPerpsClientOptions { ApiKey = mock.Key, BaseUrl = "http://127.0.0.1:9", MaxRetries = 0, Timeout = TimeSpan.FromSeconds(2) });
        Assert.DoesNotContain(mock.Key, c.ToString());
        Assert.DoesNotContain(mock.Key, $"{c}");
        Assert.Equal("ppk_test_****" + mock.Key[^4..], c.MaskedKey);
        var e = await Assert.ThrowsAnyAsync<PhoenixPerpsException>(() => c.Me.GetAsync());
        Assert.DoesNotContain(mock.Key, e.ToString());
    }

    // a client with api_key null is made, and a public call sends no Authorization
    private async Task Keyless(JsonElement client)
    {
        Assert.Equal(JsonValueKind.Null, client.GetProperty("api_key").ValueKind);
        await Load(JsonDocument.Parse("""{"exchanges":[{"respond":{"status":200,"body":{"data":[{"market":"BTC-USD"}],"has_more":false,"next_cursor":null}}}]}""").RootElement);
        using var c = new PhoenixPerpsClient(new PhoenixPerpsClientOptions { ApiKey = null, Environment = client.GetProperty("environment").GetString(), BaseUrl = mock.Url });
        Assert.Equal("BTC-USD", (await c.Markets.ListAsync()).Data[0].Value);
        var got = Assert.Single((await Requests()).EnumerateArray());
        Assert.False(got.GetProperty("headers").TryGetProperty("authorization", out _));
    }

    // the base vector (its signature, a rotation, another secret, a late clock, a changed body), then each extra one
    private void WebhookVectors()
    {
        var v = mock.Suite.GetProperty("webhook_vectors");
        static string S(JsonElement e, string n) => e.GetProperty(n).GetString()!;
        static Dictionary<string, string> At(string timestamp, string sig) => new() { ["X-Phoenix-Timestamp"] = timestamp, ["X-Phoenix-Signature"] = sig };
        static void Want(WebhookPayload got, JsonElement want) => Same(JsonNode.Parse(JsonSerializer.Serialize(got, Json.Default.WebhookPayload)), want);
        var now = DateTimeOffset.FromUnixTimeMilliseconds(v.GetProperty("now_ms").GetInt64());
        var (secret, ts, body) = (S(v, "secret"), S(v, "timestamp"), S(v, "body"));
        Want(Webhooks.Verify(body, At(ts, S(v, "signature")), secret, null, now), v.GetProperty("want"));
        Want(Webhooks.Verify(body, At(ts, S(v, "rotation")), new[] { secret }, null, now), v.GetProperty("want"));
        Assert.Equal("bad_signature", Assert.Throws<WebhookVerificationException>(() => Webhooks.Verify(body, At(ts, S(v, "signature")), S(v, "other"), null, now)).Code);
        Assert.Equal("stale", Assert.Throws<WebhookVerificationException>(() => Webhooks.Verify(body, At(ts, S(v, "signature")), secret, null, now.AddSeconds(1000))).Code);
        Assert.Equal("bad_signature", Assert.Throws<WebhookVerificationException>(() => Webhooks.Verify(body + " ", At(ts, S(v, "signature")), secret, null, now)).Code);
        Assert.Equal("missing_headers", Assert.Throws<WebhookVerificationException>(() => Webhooks.Verify(body, new Dictionary<string, string>(), secret, null, now)).Code);
        foreach (var x in v.GetProperty("extra").EnumerateArray())
        {
            var headers = At(S(x, "timestamp"), S(x, "signature"));
            if (x.TryGetProperty("error", out var error))
                Assert.Equal(error.GetString(), Assert.Throws<WebhookVerificationException>(() => Webhooks.Verify(S(x, "body"), headers, secret, null, now)).Code);
            else Want(Webhooks.Verify(S(x, "body"), headers, secret, null, now), x.GetProperty("want"));
        }
    }

    // the strings as given, through the SDK's own string overloads; error: true is the SDK's ValidationException
    [Fact]
    public void MarketRulesVectors()
    {
        var rv = mock.Suite.GetProperty("rules_vectors");
        var r = new MarketRules(rv.GetProperty("market").Deserialize(Json.Default.Market)!);
        foreach (var c in rv.GetProperty("cases").EnumerateArray())
        {
            var a = c.GetProperty("args").EnumerateArray().Select(x => x.GetString()!).ToList();
            var mode = (c.TryGetProperty("mode", out var m) ? m.GetString() : "nearest") switch { "down" => RoundMode.Down, "up" => RoundMode.Up, _ => RoundMode.Nearest };
            // the decimal an order takes, at the step's decimals: its invariant text is the vector's want
            Func<decimal> run = c.GetProperty("fn").GetString() switch
            {
                "round_price" => () => r.RoundPrice(a[0], mode),
                "round_size" => () => r.RoundSize(a[0]),
                "size_for_notional" => () => r.SizeForNotional(a[0], a[1]),
                "size_for_risk" => () => r.SizeForRisk(a[0], a[1]),
                "worst_price" => () => r.WorstPrice(a[0], a[1], a.Count > 2 ? a[2] : null),
                var fn => throw new InvalidOperationException($"the runner does not know {fn}"),
            };
            if (c.TryGetProperty("error", out var error) && error.GetBoolean()) Assert.Throws<ValidationException>(() => run());
            else Assert.Equal(c.GetProperty("want").GetString(), run().ToString(CultureInfo.InvariantCulture));
        }
    }

    [Fact]
    public async Task OpenEnumAndUnknownField()
    {
        await Load(JsonDocument.Parse("""{"exchanges":[{"respond":{"status":200,"body":{"data":{"id":"ord_1","status":"a_status_from_the_future","a_new_field":1}}}}]}""").RootElement);
        using var c = new PhoenixPerpsClient(new PhoenixPerpsClientOptions { ApiKey = mock.Key, BaseUrl = mock.Url });
        var o = await c.Orders.GetAsync("SANDBOX-00042-001", "ord_1");
        Assert.Equal("a_status_from_the_future", o.Status.Value);
        Assert.Equal(1, o.Extra!["a_new_field"].GetInt32());
    }
}

[System.Text.Json.Serialization.JsonSerializable(typeof(JsonElement))]
[System.Text.Json.Serialization.JsonSerializable(typeof(Dictionary<string, JsonElement>))]
[System.Text.Json.Serialization.JsonSerializable(typeof(Page<JsonElement>))]
internal sealed partial class JsonCtx : System.Text.Json.Serialization.JsonSerializerContext
{
}
