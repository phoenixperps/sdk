// 08: a live copy of your accounts' positions and working orders: the snapshot first, then every event applied once.
// After a reconnect the SDK resumes from the last event; a gap it cannot cover is told, and the copy says so (read
// GET /v1/events?after= from there, or the accounts again). Ctrl+C to stop.
//   dotnet run examples/08-account-stream-mirror.cs
#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj
using System.Globalization;
using System.Text.Json;
using PhoenixPerps;

CultureInfo.CurrentCulture = CultureInfo.InvariantCulture; // decimals print as 0.0001 on every machine

var final = new HashSet<string> { "filled", "canceled", "rejected", "expired" };
var mirror = new Dictionary<string, (Dictionary<string, JsonElement> Positions, Dictionary<string, JsonElement> Orders)>();
using var stop = new CancellationTokenSource();
Console.CancelKeyPress += (_, e) => { e.Cancel = true; stop.Cancel(); };

using var client = new PhoenixPerpsClient();
var accounts = await client.Accounts.ListAsync();
await using var stream = client.OpenAccountStream(new AccountStreamOptions
{
    OnGap = after => Console.WriteLine($"[gap] events after {after ?? "the start"} were missed: the copy may be stale"),
});
await stream.Ready.WaitAsync(stop.Token);
foreach (var a in accounts.Data)
{
    if (stream.Snapshot(a.Id) is not { } snap) continue;
    if (snap.TryGetProperty("error", out var error))
    {
        // the account could not be read just now (its lists are empty, not flat): read it again by REST
        Console.WriteLine($"[snapshot] {a.Id}: not readable now ({error})");
        continue;
    }
    mirror[a.Id] = (
        snap.GetProperty("positions").EnumerateArray().ToDictionary(p => p.GetProperty("market").GetString()!),
        snap.GetProperty("orders").EnumerateArray().ToDictionary(o => o.GetProperty("id").GetString()!));
}
Show("snapshot");

try
{
    await foreach (var e in stream.ReadAllAsync(stop.Token))
    {
        if (!mirror.TryGetValue(e.AccountId, out var m)) continue;
        if (e.Data.TryGetValue("order", out var order))
        {
            var id = order.GetProperty("id").GetString()!;
            if (final.Contains(order.GetProperty("status").GetString()!)) m.Orders.Remove(id);
            else m.Orders[id] = order;
        }
        if (e.Data.TryGetValue("position", out var position))
        {
            var market = position.GetProperty("market").GetString()!;
            if (e.Type.ToString() == "position.closed") m.Positions.Remove(market);
            else m.Positions[market] = position;
        }
        Show(e.Type.ToString());
    }
}
catch (OperationCanceledException)
{
}

void Show(string why)
{
    foreach (var (id, m) in mirror)
    {
        var positions = string.Join(", ", m.Positions.Values.Select(p => $"{p.GetProperty("market")} {p.GetProperty("side")} {p.GetProperty("size")}"));
        Console.WriteLine($"[{why}] {id}: positions {(positions.Length > 0 ? positions : "none")}; {m.Orders.Count} working orders");
    }
}
