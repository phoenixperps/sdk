// 01: who you are (the key, its access and limits) and your accounts.
//   PHOENIXPERPS_API_KEY=ppk_test_... dotnet run examples/01-accounts.cs
#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj
using System.Globalization;
using PhoenixPerps;

CultureInfo.CurrentCulture = CultureInfo.InvariantCulture; // decimals print as 0.0001 on every machine

using var client = new PhoenixPerpsClient(); // the key: PHOENIXPERPS_API_KEY (a sandbox key: the sandbox)

var me = await client.Me.GetAsync();
var c = me.Credential;
Console.WriteLine($"{c.Kind} \"{c.Name}\" on {c.Environment}: {c.Access} access");
Console.WriteLine($"limits: {me.Budgets.ReadPerMin} reads and {me.Budgets.TradePerMin} trading calls a minute");

var accounts = await client.Accounts.ListAsync();
foreach (var a in accounts.Data)
    Console.WriteLine($"{a.Id}  {a.Stage}  {a.Status}  balance {a.Balance?.ToString() ?? "-"}");
