// 02: the markets and their rules, then a quote (bid, ask, last) from the market stream.
//   dotnet run examples/02-markets.cs [BTC-USD]
#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj
using System.Globalization;
using PhoenixPerps;

CultureInfo.CurrentCulture = CultureInfo.InvariantCulture; // decimals print as 0.0001 on every machine

using var client = new PhoenixPerpsClient();
var symbol = (args.Length > 0 ? args[0] : "BTC-USD").ToUpperInvariant();

foreach (var m in (await client.Markets.ListAsync()).Data)
    Console.WriteLine($"{m.Value,-10} tick {m.TickSize}  step {m.SizeStep}  min {m.MinSize}  up to {m.MaxLeverage}x  {m.Status}");

await using var market = client.OpenMarketStream();
var reply = await market.SubscribeAsync("ticker", new[] { symbol }, new Dictionary<string, object?> { ["interval_ms"] = 250 });
if (reply.GetProperty("rejected").GetArrayLength() > 0)
    throw new InvalidOperationException($"{symbol}: {reply.GetProperty("rejected")[0].GetProperty("code")}");
await foreach (var frame in market.ReadAllAsync())
{
    if (frame.Type != "ticker" || frame.Data is not { } q) continue;
    Console.WriteLine($"{symbol}: bid {q.GetProperty("bid")}  ask {q.GetProperty("ask")}  last {q.GetProperty("last")}");
    break;
}
