// 10: the last 100 closed one-minute candles of a market and the one forming (final false), written to candles.csv.
//   dotnet run examples/10-candles-to-csv.cs [BTC-USD]
#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj
using System.Text.Json;
using PhoenixPerps;

using var client = new PhoenixPerpsClient();
var symbol = (args.Length > 0 ? args[0] : "BTC-USD").ToUpperInvariant();
var candles = new SortedDictionary<long, JsonElement>();

await using (var market = client.OpenMarketStream())
{
    await market.SubscribeAsync("candles", new[] { symbol }, new Dictionary<string, object?> { ["interval"] = "1m", ["history"] = 100 });
    await foreach (var f in market.ReadAllAsync())
    {
        if (f.Type == "snapshot_end") break;
        if (f.Type != "candles" || f.Data is not { } data) continue;
        foreach (var c in data.EnumerateArray()) candles[c.GetProperty("time").GetInt64()] = c;
    }
}

using (var csv = new StreamWriter("candles.csv"))
{
    csv.WriteLine("time,open,high,low,close,volume,final");
    foreach (var (t, c) in candles)
        csv.WriteLine(string.Join(",", DateTimeOffset.FromUnixTimeMilliseconds(t).ToString("O"),
            c.GetProperty("open"), c.GetProperty("high"), c.GetProperty("low"), c.GetProperty("close"), c.GetProperty("volume"),
            c.GetProperty("final").GetBoolean() ? "true" : "false"));
}
Console.WriteLine($"{candles.Count} candles of {symbol} in candles.csv");
