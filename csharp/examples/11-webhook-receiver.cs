// 11: receive webhooks: every delivery is checked (its signature and its time) before it is trusted.
//   PHOENIXPERPS_WEBHOOK_SECRET=ppwhsec_... dotnet run examples/11-webhook-receiver.cs
// Then point a webhook endpoint (dashboard, Developers, Webhooks) at https://<this machine>/webhooks (port 8080 here).
// It listens on localhost (no administrator rights needed on Windows): put your HTTPS proxy or tunnel in front of it.
#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj
using System.Net;
using PhoenixPerps;

var secret = Environment.GetEnvironmentVariable("PHOENIXPERPS_WEBHOOK_SECRET")
    ?? throw new InvalidOperationException("Set PHOENIXPERPS_WEBHOOK_SECRET to the endpoint's signing secret.");

using var listener = new HttpListener();
listener.Prefixes.Add("http://localhost:8080/webhooks/");
listener.Start();
Console.WriteLine("listening on localhost:8080/webhooks");
while (true)
{
    var ctx = await listener.GetContextAsync();
    if (ctx.Request.HttpMethod != "POST")
    {
        ctx.Response.StatusCode = 404;
        ctx.Response.Close();
        continue;
    }
    using var reader = new StreamReader(ctx.Request.InputStream, ctx.Request.ContentEncoding);
    var raw = await reader.ReadToEndAsync(); // the body exactly as received
    var headers = ctx.Request.Headers.AllKeys.Where(k => k is not null).ToDictionary(k => k!.ToLowerInvariant(), k => ctx.Request.Headers[k]!);
    try
    {
        var e = Webhooks.Verify(raw, headers, secret); // the event, with its environment and test flag (webhook.ping is one)
        Console.WriteLine($"{e.Type}{(e.Test == true ? " (test)" : "")} on {e.AccountId ?? "-"} ({e.Environment}): {string.Join(" ", e.Data.Select(kv => $"{kv.Key}={kv.Value.GetRawText()}"))}");
        ctx.Response.StatusCode = 200;
    }
    catch (WebhookVerificationException err)
    {
        Console.WriteLine($"refused a delivery: {err.Code}");
        ctx.Response.StatusCode = 400;
    }
    ctx.Response.Close();
}
