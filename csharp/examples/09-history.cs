// 09: an account's history: the equity curve by day, the result of each trading day, closed positions and fills.
//   dotnet run examples/09-history.cs
#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj
using System.Globalization;
using PhoenixPerps;

CultureInfo.CurrentCulture = CultureInfo.InvariantCulture; // decimals print as 0.0001 on every machine

using var client = new PhoenixPerpsClient();
var account = (await client.Accounts.ListAsync()).Data[0].Id;

var equity = await client.History.EquityAsync(account, new HistoryEquityParams { Resolution = Resolution.Day });
Console.WriteLine($"equity: {equity.Points.Count} points");

foreach (var d in (await client.History.DailyAsync(account)).Data)
    Console.WriteLine($"{d.Date}: {d.Trades} trades, P&L {d.Pnl}, fees {d.Fees}");
foreach (var p in (await client.Positions.ListClosedAsync(account, new PositionsListClosedParams { Limit = 10 })).Data)
    Console.WriteLine($"closed {p.Market} {p.Side} {p.Size}: {p.EntryPrice} -> {p.ExitPrice}, P&L {p.Pnl}");

var n = 0;
await foreach (var f in client.Fills.ListAutoPagingAsync(account, options: new RequestOptions { MaxItems = 100 })) // every page, at most 100
{
    if (++n <= 5) Console.WriteLine($"fill {f.Market} {f.Side} {f.Size} @ {f.Price}");
}
Console.WriteLine($"{n} fills read");
