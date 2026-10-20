# PhoenixPerps.Sdk

The Phoenix Perpetuals API for .NET: every REST call, the account stream and market data, with retries, idempotency
keys, paging and typed errors. .NET 8 or later, no dependencies, trimming and AOT safe.
Docs: https://docs.phoenixperpsfunding.com

```
dotnet nuget add source https://phoenixperps.github.io/sdk/nuget/index.json --name phoenixperps
dotnet add package PhoenixPerps.Sdk
```

```csharp
using PhoenixPerps;

using var client = new PhoenixPerpsClient(); // PHOENIXPERPS_API_KEY from the environment
var accounts = await client.Accounts.ListAsync();
var order = await client.Orders.CreateAsync(accounts.Data[0].Id, new CreateOrder
{
    Market = "BTC-USD",
    Side = OrderSide.Buy,
    Type = CreateOrderType.Market,
    Size = 0.01m,
});
```

## The client

`new PhoenixPerpsClient(new PhoenixPerpsClientOptions { ApiKey, Environment, BaseUrl, Timeout, MaxRetries, AppInfo, HttpClient, Logger, AccountStreamUrl, MarketStreamUrl })`

- The key comes from `ApiKey`, else `PHOENIXPERPS_API_KEY`. The environment (`live` or `sandbox`, in any case) comes
  from `Environment`, else the key itself (`ppk_live_`, `ppk_test_`), else `PHOENIXPERPS_ENVIRONMENT`; a key of the other
  environment fails before any request.
- Without a key the client still works for the public calls (`Markets`, `Platform.StatusAsync`); any other answers the
  API's `missing_api_key`. No Authorization header is sent without a key.
- `BaseUrl` is the REST host; the REST address with `/v1` works too. It moves the REST calls only: the streams take
  `AccountStreamUrl` and `MarketStreamUrl`. A `HttpClient` of yours keeps its own `Timeout`, which caps every attempt as
  well: leave it infinite or above `Timeout`.
- Every call takes `RequestOptions` (`IdempotencyKey`, `Timeout`, `MaxRetries`, `Headers`, `OnResponse`) and a
  `CancellationToken`. `OnResponse` gets each answer's meta: status, headers, request id and Idempotency-Key.
- The key never appears in `ToString()` or an exception (`client.MaskedKey` shows its end).

## Calls

Resources follow the API: `Platform`, `Me`, `Markets`, `Accounts`, `History`, `Margin`, `Positions`, `Orders`, `Fills`,
`Exits`, `Strategies`, `Events`, `WebhookEndpoints`, `TradingView`. Prices and sizes are `decimal`, exact, sent as
strings. Enums are open: a value from a newer API is kept as is, and fields this version does not know land in `Extra`.

- A call is tried again only when that is safe (a read, a route the API marks retry-safe, or a call carrying an
  Idempotency-Key: an order change sent with `RequestOptions.IdempotencyKey` too), after a network failure, a timeout,
  or an answer the API marks `retryable` (409 `operation_in_progress`: the first call with that key is still running;
  a 503 `sandbox_disabled` is not); an answer without the API's envelope (a proxy's page) when it is 429 or 5xx. Writes
  get one Idempotency-Key, the same on every attempt.
- `Retry-After` is honoured (fractions of a second too); one over 60 s is not slept: the call throws
  `RateLimitException` with `RetryAfter` set.
- With a rate-limit budget used up (read, trade or public, as the API counts them), the next call of that budget waits
  for its reset instead of drawing a 429.
- A 304 Not Modified (you sent `If-None-Match` in `RequestOptions.Headers`, nothing changed) is no error: the call
  answers `null` (a list call too, never an empty page), and `OnResponse` gets the 304 with its `etag`.
- A route newer than this SDK: `await client.SendAsync("GET", "/v1/new-route", query, body, options)` answers the
  `RawResponse` (status, headers, `Body`) as it came, with the same key, retries and errors (a write is tried again
  only on a route the API marks retry-safe, or with `RequestOptions.IdempotencyKey`).

## Lists

```csharp
var page = await client.Orders.ListAsync(accountId, new OrdersListParams { Status = OrdersListParamsStatus.Working });
var next = await page.GetNextPageAsync(); // null after the last

await foreach (var fill in client.Fills.ListAutoPagingAsync(accountId, options: new RequestOptions { MaxItems = 500 }))
{
}
```

A page's other fields land in its `Extra` (the orders list's `history_complete`: false when the history reaches further
back than the list covers).

## Errors

Every failure is a `PhoenixPerpsException` with `Code`, `Status`, `Message`, `RequestId`, `Details`, `RetryAfter`,
`Retryable` (whether the same call may work later: as the API said, by the status for an answer without its envelope,
true for a timeout or a network failure), `DocUrl`, `OrderId`, `IdempotencyKey` and the answer's `Headers`; printed,
it shows its code and request id. Its class tells the family: `AuthenticationException` (401), `PermissionException`
(403), `NotFoundException`, `ConflictException` (409), `ValidationException` (400), `RuleRejectedException` (422: the
account's rules said no), `RateLimitException`, `ServerException`, `ApiConnectionException`, `ApiTimeoutException`.
Any answer that is not the API's (an HTML page, a 2xx that is no JSON) is one too, coded `http_<status>`. Every code
the API answers is a constant of `ErrorCodes`.

## Streams (read only; orders go through REST)

```csharp
await using var stream = client.OpenAccountStream(new AccountStreamOptions
{
    Risk = true,
    OnRisk = (account, risk) => Console.WriteLine($"{account}: equity {risk.Equity}"),
    OnGap = after => Console.WriteLine($"missed events: read GET /v1/events?after={after}"),
});
await foreach (var e in stream.ReadAllAsync()) // each event once, resumed after reconnects
{
}

await using var market = client.OpenMarketStream();
await market.SubscribeAsync("ticker", new[] { "BTC-USD" }, new Dictionary<string, object?> { ["interval_ms"] = 250 });
await foreach (var frame in market.ReadAllAsync())
{
}
```

- Both reconnect by themselves, also after three heartbeats of silence, with one `Logger` line per reconnect and its
  cause. An upgrade refused with 429 or 5xx (or no answer) is tried again after its `Retry-After`; any other refusal
  ends the stream with the API's own error (an `AuthenticationException` `invalid_api_key`, say), as a REST call would.
- The account stream's `Ready` completes once the first snapshots are in. A snapshot carrying `error` is an account that
  could not be read then (read it by REST), not a flat one. A refused subscribe (an unknown account,
  `too_many_accounts`) ends the stream and fails `Ready` with a `ValidationException` (`Details` names the account).
- Each event keeps its frame's `Seq` and `Replayed`. A resume the server cannot cover, or a 4008 (too far behind), is a
  gap: `OnGap` (and the log) get the last event told, to read `GET /v1/events?after=` from. Risk frames come only with
  `Risk = true`, to `OnRisk` as they come (and `stream.Risk(accountId)`). `PingAsync()` asks the server;
  `UnsubscribeAsync(accounts)` keeps them out after a reconnect too.
- The market stream subscribes again, after a reconnect, to what the server took and was not unsubscribed; its other
  options are the last subscribe's. An unsubscribe without markets ends every one of the channel, a candles one without
  `interval` every interval. Made again, a subscription sends its history again: a trade's id and a candle's time tell
  the repeats apart. `ListAsync()` and `PingAsync()` ask the server, `market.Limits` holds the hello's limits,
  `frame.Interval` tells candle intervals apart, and a refused subscribe throws a `ValidationException` with the
  server's `Details`.

## Helpers

- `new MarketRules(await client.Markets.GetAsync("BTC-USD"))`: `RoundPrice`, `RoundSize`, `SizeForNotional`,
  `SizeForRisk(riskUsd, stopDistance)`, `WorstPrice(side, price, slippagePct)` (side an `OrderSide` or "buy"/"sell" in
  any casing; the slippage within the market's bounds), from decimals or decimal strings, with the market's own steps.
  Each answers the `decimal` an order takes, at the step's decimals (`Size = rules.SizeForNotional(100m, bid)`). A size
  rounds down even below `MinSize` (to `0`): check it before ordering. What they refuse is a `ValidationException`.
- `Webhooks.Verify(rawBody, headers, secrets)`: the `WebhookPayload` (the event with its `ApiVersion`, `Environment`
  and `Test`; `webhook.ping` is a type like any other), or a `WebhookVerificationException` (a `PhoenixPerpsException`:
  `missing_headers`, `stale`, `bad_signature`, `bad_body`). Pass the body exactly as received.

## Examples

`examples/01-accounts.cs` to `12-error-handling.cs`, .NET 10 file-based apps, the same twelve programs as in every SDK, each one
in every language in the docs' cookbook (https://docs.phoenixperpsfunding.com/cookbook). From the SDK's source: `dotnet run examples/01-accounts.cs`.
