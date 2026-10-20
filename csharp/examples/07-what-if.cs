// 07: what an order would do now, without placing it: fill or rest, the price, the fee, the margin, the position
// after. Nothing is sent to the market.
//   dotnet run examples/07-what-if.cs
#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj
using System.Globalization;
using PhoenixPerps;

CultureInfo.CurrentCulture = CultureInfo.InvariantCulture; // decimals print as 0.0001 on every machine

using var client = new PhoenixPerpsClient();
var account = (await client.Accounts.ListAsync()).Data[0].Id;

var w = await client.Orders.PreviewAsync(account, new CreateOrder
{
    Market = "BTC-USD",
    Side = OrderSide.Buy,
    Type = CreateOrderType.Market,
    NotionalUsd = 1000m,
});
Console.WriteLine($"result:         {w.Result}{(w.Reject is null ? "" : $" ({w.Reject.Code})")}");
Console.WriteLine($"size:           {w.Size?.ToString() ?? "-"}");
Console.WriteLine($"price:          {w.Execution?.ExpectedPrice.ToString() ?? "-"} (worst {w.Execution?.WorstPrice?.ToString() ?? "-"})");
Console.WriteLine($"notional, fee:  {w.Notional?.ToString() ?? "-"}, {w.Fee?.ToString() ?? "-"}");
Console.WriteLine($"margin:         {w.Margin?.Required.ToString() ?? "-"} needed, {w.Margin?.AvailableAfter?.ToString() ?? "-"} left after");
Console.WriteLine($"position after: {w.PositionAfter?.Side?.ToString() ?? "flat"} {w.PositionAfter?.Size}");
