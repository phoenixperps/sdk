// 06: a position whose stop loss trails the price by 0.5% and moves to break-even once the price has gone 0.5% your
// way, all on the API's servers. Follows the account stream for 30 s to watch the stop move, then closes.
//   dotnet run examples/06-trailing-breakeven.cs
#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj
using System.Globalization;
using PhoenixPerps;

CultureInfo.CurrentCulture = CultureInfo.InvariantCulture; // decimals print as 0.0001 on every machine

using var client = new PhoenixPerpsClient();
if (client.Environment == "live" && Environment.GetEnvironmentVariable("PHOENIXPERPS_ALLOW_LIVE") != "yes")
{
    Console.Error.WriteLine("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).");
    return 1;
}
var account = (await client.Accounts.ListAsync()).Data[0].Id;
var btc = await client.Markets.GetAsync("BTC-USD");

using var watch = new CancellationTokenSource(TimeSpan.FromSeconds(30));
// the stream first, so every event of the stop is told, from its placing on
await using (var stream = client.OpenAccountStream(new AccountStreamOptions { Accounts = new[] { account } }))
{
    await stream.Ready;
    var order = await client.Orders.CreateAsync(account, new CreateOrder
    {
        Market = "BTC-USD",
        Side = OrderSide.Buy,
        Type = CreateOrderType.Market,
        Size = btc.MinSize,
        Exits = new OrderExits
        {
            StopLoss = new OrderExitsStopLoss
            {
                Percent = 1m,
                Trail = new() { Percent = 0.5m },
                BreakEven = new() { TriggerPercent = 0.5m, OffsetTicks = 2 },
            },
        },
    });
    Console.WriteLine($"bought {order.Size} BTC ({order.Status}); watching the stop for 30 s");
    try
    {
        await foreach (var e in stream.ReadAllAsync(watch.Token))
            if (e.Type.ToString().StartsWith("strategy.", StringComparison.Ordinal))
                Console.WriteLine($"{DateTimeOffset.FromUnixTimeMilliseconds(e.Time):O} {e.Type} {string.Join(" ", e.Data.Select(kv => $"{kv.Key}={kv.Value.GetRawText()}"))}");
    }
    catch (OperationCanceledException)
    {
    }
}

var closed = await client.Positions.CloseAsync(account, "BTC-USD", new ClosePosition());
Console.WriteLine($"closed: {closed.Status}");
return 0;
