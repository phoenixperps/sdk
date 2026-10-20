// 05: a market order with its exits: a take profit 1% away and a stop loss 0.5% away, placed by the API the moment
// it fills and kept on its servers. The position is closed at the end.
//   dotnet run examples/05-market-with-brackets.cs
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

var order = await client.Orders.CreateAsync(account, new CreateOrder
{
    Market = "BTC-USD",
    Side = OrderSide.Buy,
    Type = CreateOrderType.Market,
    Size = btc.MinSize,
    Exits = new OrderExits
    {
        TakeProfit = new() { new OrderExitsTakeProfit { Percent = 1m } },
        StopLoss = new OrderExitsStopLoss { Percent = 0.5m },
    },
});
Console.WriteLine($"market order {order.Id}: {order.Status} at {order.AverageFillPrice?.ToString() ?? "-"}");

var exits = await client.Exits.GetAsync(account, "BTC-USD");
foreach (var tp in exits.TakeProfit) Console.WriteLine($"take profit: {tp.Size} at {tp.Price} ({tp.Status})");
foreach (var sl in exits.StopLoss) Console.WriteLine($"stop loss:   {sl.Size} at {sl.Price} ({sl.Status})");

var closed = await client.Positions.CloseAsync(account, "BTC-USD", new ClosePosition());
Console.WriteLine($"closed: {closed.Id} {closed.Status} (the exits went with the position)");
return 0;
