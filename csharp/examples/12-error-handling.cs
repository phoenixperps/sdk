// 12: what failures look like: each has a class by family, the API's code, the request id to quote to support, and
// details. A write whose outcome is unknown (a timeout) is repeated with the same Idempotency-Key, never a new one.
//   dotnet run examples/12-error-handling.cs
#:project ../src/PhoenixPerps.Sdk/PhoenixPerps.Sdk.csproj
using PhoenixPerps;

using var client = new PhoenixPerpsClient();
var account = (await client.Accounts.ListAsync()).Data[0].Id;

try
{
    await client.Orders.GetAsync(account, "ord_does_not_exist");
}
catch (NotFoundException err)
{
    Show(err);
}

try
{
    await client.Orders.PreviewAsync(account, new CreateOrder { Market = "BTC-USD", Side = OrderSide.Buy, Type = CreateOrderType.Market, Size = 0.0000001m });
}
catch (Exception err) when (err is ValidationException or RuleRejectedException)
{
    Show((PhoenixPerpsException)err);
}

static void Show(PhoenixPerpsException err)
{
    Console.WriteLine($"{err.GetType().Name}: {err.Code} (HTTP {err.Status}, {err.RequestId}) {err.Message}");
    if (err.Details is { } details) Console.WriteLine($"  details: {details}");
}

static class Orders
{
    // a write that may or may not have happened: the same key again is safe, a new key could place it twice
    public static async Task<Order> PlaceOnce(PhoenixPerpsClient client, string account, CreateOrder body)
    {
        var options = new RequestOptions { IdempotencyKey = Guid.NewGuid().ToString() };
        for (var attempt = 1; ; attempt++)
        {
            try
            {
                return await client.Orders.CreateAsync(account, body, options);
            }
            catch (ApiTimeoutException) when (attempt < 3)
            {
                // still unknown after the SDK's own retries: the same key
            }
            catch (RateLimitException err) when (attempt < 3 && err.RetryAfter is { } wait)
            {
                await Task.Delay(wait);
            }
        }
    }
}
