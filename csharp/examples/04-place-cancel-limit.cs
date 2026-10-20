// 04: a limit order far from the market, found again by your own id, then canceled. The Idempotency-Key makes a
// retried request return the first order instead of placing a second one.
//   dotnet run examples/04-place-cancel-limit.cs
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
var rules = new MarketRules(await client.Markets.GetAsync("BTC-USD"));

decimal bid;
await using (var market = client.OpenMarketStream())
{
    await market.SubscribeAsync("ticker", new[] { "BTC-USD" });
    while (true)
    {
        var f = await market.ReadAsync();
        if (f.Type == "ticker" && f.Data is { } q)
        {
            bid = decimal.Parse(q.GetProperty("bid").GetString()!, CultureInfo.InvariantCulture);
            break;
        }
    }
}

var clientOrderId = $"example-04-{DateTimeOffset.UtcNow.ToUnixTimeMilliseconds()}";
var order = await client.Orders.CreateAsync(account, new CreateOrder
{
    Market = "BTC-USD",
    Side = OrderSide.Buy,
    Type = CreateOrderType.Limit,
    Size = rules.SizeForNotional(100m, bid), // about 100 USD
    LimitPrice = rules.RoundPrice(bid * 0.9m, RoundMode.Down), // 10% below: it rests
    ClientOrderId = clientOrderId,
}, new RequestOptions { IdempotencyKey = Guid.NewGuid().ToString() });
Console.WriteLine($"placed {order.Id}: {order.Status}, {order.Size} at {order.LimitPrice}");

var found = (await client.Orders.ListAsync(account, new OrdersListParams { ClientOrderId = clientOrderId })).Data[0];
Console.WriteLine($"found by client_order_id: {found.Id}");

var canceled = await client.Orders.CancelAsync(account, order.Id);
Console.WriteLine($"canceled: {canceled.Status} ({canceled.CancelReason})");
return 0;
