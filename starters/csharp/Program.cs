// The starter bot: an EMA cross on one-minute candles, each entry sized so its stop loses PP_RISK_USD, previewed first,
// placed with its stop loss on the API's servers. What it does, exactly: ../README.md. Run it on the sandbox.
//   dotnet run
using System.Globalization;
using System.Text.Json;
using System.Text.RegularExpressions;
using PhoenixPerps;

var inv = CultureInfo.InvariantCulture;
CultureInfo.CurrentCulture = inv; // decimals print as the API writes them (6.250)

// .env beside it: only what the environment does not set already
if (File.Exists(".env"))
{
    foreach (var line in File.ReadAllLines(".env"))
    {
        var m = Regex.Match(line, @"^\s*([A-Z0-9_]+)\s*=\s*(.*?)\s*$");
        if (m.Success && !line.TrimStart().StartsWith('#') && Environment.GetEnvironmentVariable(m.Groups[1].Value) is null)
            Environment.SetEnvironmentVariable(m.Groups[1].Value, m.Groups[2].Value.Trim('"', '\''));
    }
}
static string Env(string name, string fallback = "") => Environment.GetEnvironmentVariable(name) is { Length: > 0 } v ? v : fallback;

var marketId = Env("PP_MARKET", "BTC-USD").ToUpperInvariant();
var riskUsd = decimal.Parse(Env("PP_RISK_USD", "50"), inv);
int fastN = int.Parse(Env("PP_FAST", "9"), inv), slowN = int.Parse(Env("PP_SLOW", "21"), inv), atrN = int.Parse(Env("PP_ATR", "14"), inv);
var atrMult = double.Parse(Env("PP_ATR_MULT", "2"), inv);
string trailPercent = Env("PP_TRAIL_PERCENT"), breakEvenPercent = Env("PP_BREAK_EVEN_PERCENT");
int maxTrades = int.Parse(Env("PP_MAX_TRADES_PER_DAY", "10"), inv), runCandles = int.Parse(Env("PP_RUN_CANDLES", "0"), inv);

using var client = new PhoenixPerpsClient(new PhoenixPerpsClientOptions { MarketStreamUrl = Env("PP_STREAM_URL") is { Length: > 0 } url ? url : null });
var live = client.Environment == "live";
if (live && Env("PHOENIXPERPS_ALLOW_LIVE") != "yes")
{
    Console.Error.WriteLine("This is a live key: the starter trades real money only with PHOENIXPERPS_ALLOW_LIVE=yes. Use a sandbox key to try it.");
    return 1;
}
void Log(string msg) => Console.WriteLine($"{DateTime.UtcNow:yyyy-MM-ddTHH:mm:ss.fffZ} {(live ? "LIVE " : "")}{msg}");

// the account and the market's rules
var accounts = (await client.Accounts.ListAsync()).Data;
var wanted = Env("PP_ACCOUNT").ToUpperInvariant();
var account = accounts.FirstOrDefault(a => wanted.Length > 0 ? a.Id == wanted : a.Status.ToString() == "active")?.Id
    ?? throw new InvalidOperationException(wanted.Length > 0 ? $"No account {wanted} for this key." : "No active account for this key.");
var market = await client.Markets.GetAsync(marketId);
var rules = new MarketRules(market);
var tick = (double)market.TickSize;
Log($"{account} on {marketId}: EMA {fastN}/{slowN}, stop {Env("PP_ATR_MULT", "2")} x ATR({atrN}), {riskUsd} USD a trade");

var fast = new Ema(fastN);
var slow = new Ema(slowN);
var atr = new Atr(atrN);
var (tradesDay, tradesN) = ("", 0);

// one live candle: the position first, then at most one order (../README.md step 4)
async Task OnLive(long time, double? prevFast, double? prevSlow)
{
    if (fast.Value is not { } f || slow.Value is not { } s || atr.Value is not { } a || prevFast is not { } pf || prevSlow is not { } ps) return;
    var up = pf <= ps && f > s;
    var down = pf >= ps && f < s;
    var positions = await client.Positions.ListAsync(account, new PositionsListParams { Market = marketId });
    var position = positions.Data.FirstOrDefault(p => p.Size != 0);
    if (position is not null && !position.Protected)
    {
        Log($"the {position.Side} position has no stop: closing it");
        await client.Positions.CloseAsync(account, marketId, new ClosePosition());
        return;
    }
    if (!up && !down) return;
    var cross = up ? "up" : "down";
    if (position is not null)
    {
        if ((position.Side.ToString() == "long") != up)
        {
            Log($"cross {cross}: closing the {position.Side} position");
            await client.Positions.CloseAsync(account, marketId, new ClosePosition());
        }
        return;
    }
    var day = DateTimeOffset.FromUnixTimeMilliseconds(time).UtcDateTime.ToString("yyyy-MM-dd", inv);
    if (tradesDay != day) (tradesDay, tradesN) = (day, 0);
    if (tradesN >= maxTrades)
    {
        Log($"cross {cross}: {tradesN} entries today already (PP_MAX_TRADES_PER_DAY)");
        return;
    }
    var ticks = Math.Floor(a * atrMult / tick + 0.5);
    if (ticks < 1) return;
    var distance = rules.RoundPrice((decimal)(ticks * tick));
    var size = rules.SizeForRisk(riskUsd, distance);
    if (size < market.MinSize || size == 0)
    {
        Log($"cross {cross}: {riskUsd} USD over a {distance} stop is below the market's smallest size");
        return;
    }
    var side = up ? OrderSide.Buy : OrderSide.Sell;
    var preview = await client.Orders.PreviewAsync(account, new CreateOrder { Market = marketId, Side = side, Type = CreateOrderType.Market, Size = size });
    if (preview.Result.ToString() == "would_reject")
    {
        Log($"cross {cross}: the API would refuse {side} {size} ({preview.Reject?.Code})");
        return;
    }
    var stopLoss = new OrderExitsStopLoss { Distance = distance };
    if (trailPercent.Length > 0) stopLoss.Trail = new() { Percent = decimal.Parse(trailPercent, inv) };
    if (breakEvenPercent.Length > 0) stopLoss.BreakEven = new() { TriggerPercent = decimal.Parse(breakEvenPercent, inv), OffsetTicks = 1 };
    var order = await client.Orders.CreateAsync(account, new CreateOrder
    {
        Market = marketId, Side = side, Type = CreateOrderType.Market, Size = size,
        ClientOrderId = $"starter-{time}", Exits = new OrderExits { StopLoss = stopLoss },
    }, new RequestOptions { IdempotencyKey = $"starter-{marketId}-{time}" });
    tradesN++;
    Log($"cross {cross}: {side} {size} {marketId} ({order.Status}), stop {distance} away");
}

// candles in, one at a time: history warms the indicators up, live ones trade
await using var stream = client.OpenMarketStream();
await stream.SubscribeAsync("candles", new[] { marketId }, new Dictionary<string, object?> { ["interval"] = "1m", ["history"] = Math.Max(3 * slowN, 50) });
long last = 0;
var (warm, liveCandles) = (false, 0);
await foreach (var frame in stream.ReadAllAsync())
{
    if (frame.Type == "snapshot_end")
    {
        if (!warm) Log("warmed up: trading on the next candles");
        warm = true;
        continue;
    }
    if (frame.Type != "candles" || frame.Data is not { } data) continue;
    foreach (var c in data.EnumerateArray())
    {
        var time = c.GetProperty("time").GetInt64();
        if (!c.GetProperty("final").GetBoolean() || time <= last) continue;
        last = time;
        var (prevFast, prevSlow) = (fast.Value, slow.Value);
        var close = double.Parse(c.GetProperty("close").GetString()!, inv);
        fast.Add(close);
        slow.Add(close);
        atr.Add(double.Parse(c.GetProperty("high").GetString()!, inv), double.Parse(c.GetProperty("low").GetString()!, inv), close);
        if (!warm) continue;
        await OnLive(time, prevFast, prevSlow);
        if (runCandles > 0 && ++liveCandles >= runCandles) return 0;
    }
}
return 0;

/// <summary>EMA(n): the mean of the first n values, then (x - ema) * 2 / (n + 1) + ema.</summary>
sealed class Ema(int n)
{
    private readonly List<double> _seed = new();
    public double? Value { get; private set; }

    public void Add(double x)
    {
        if (Value is not { } v)
        {
            _seed.Add(x);
            if (_seed.Count == n) Value = _seed.Sum() / n;
        }
        else Value = (x - v) * (2.0 / (n + 1)) + v;
    }
}

/// <summary>Wilder's ATR(n): the mean of the first n true ranges, then (atr * (n - 1) + tr) / n.</summary>
sealed class Atr(int n)
{
    private readonly List<double> _seed = new();
    private double? _prevClose;
    public double? Value { get; private set; }

    public void Add(double high, double low, double close)
    {
        var tr = _prevClose is not { } pc ? high - low : Math.Max(high - low, Math.Max(Math.Abs(high - pc), Math.Abs(low - pc)));
        _prevClose = close;
        if (Value is not { } v)
        {
            _seed.Add(tr);
            if (_seed.Count == n) Value = _seed.Sum() / n;
        }
        else Value = (v * (n - 1) + tr) / n;
    }
}
