// 03: live market data: prices for two markets and the trades of one, until 20 frames came.
//   dotnet run examples/03-market-stream.cs
#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj
using PhoenixPerps;

using var client = new PhoenixPerpsClient();
await using var market = client.OpenMarketStream(); // subscriptions are made again after a reconnect
await market.SubscribeAsync("ticker", new[] { "BTC-USD", "ETH-USD" }, new Dictionary<string, object?> { ["interval_ms"] = 500 });
await market.SubscribeAsync("trades", new[] { "BTC-USD" }, new Dictionary<string, object?> { ["history"] = 5 });

var n = 0;
await foreach (var f in market.ReadAllAsync())
{
    if (f.Data is not { } data) continue;
    if (f.Type == "ticker")
        Console.WriteLine($"ticker {f.Symbol}: bid {data.GetProperty("bid")} ask {data.GetProperty("ask")}");
    else if (f.Type == "trades")
        foreach (var t in data.EnumerateArray())
            Console.WriteLine($"trade  {f.Symbol}: {t.GetProperty("side")} {t.GetProperty("size")} @ {t.GetProperty("price")}");
    else
        continue;
    if (++n >= 20) break;
}
