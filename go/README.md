# phoenixperps (Go)

The Phoenix Perpetuals API for Go: every REST call, the account stream and market data, with retries, idempotency
keys, paging and typed errors. Go 1.23 or later. Docs: https://docs.phoenixperpsfunding.com

```
go get github.com/phoenixperps/sdk/go
```

```go
import phoenixperps "github.com/phoenixperps/sdk/go"

client, err := phoenixperps.NewClient() // PHOENIXPERPS_API_KEY from the environment
accounts, err := client.Accounts.List(ctx)
order, err := client.Orders.Create(ctx, accounts.Data[0].ID, phoenixperps.CreateOrder{
	Market: "BTC-USD",
	Side:   phoenixperps.OrderSideBuy,
	Type:   phoenixperps.CreateOrderTypeMarket,
	Size:   phoenixperps.Dec("0.01"),
})
```

## The client

`NewClient(WithAPIKey, WithEnvironment, WithBaseURL, WithStreamURLs, WithTimeout, WithMaxRetries, WithAppInfo,
WithHTTPClient, WithLogger)`

- The key comes from `WithAPIKey`, else `PHOENIXPERPS_API_KEY`. Without one the client is made all the same: the
  public calls (`Markets` and `Platform`) work, and any other answers the API's `missing_api_key`.
- The environment (`live` or `sandbox`, in any case) comes from `WithEnvironment`, else the key itself (`ppk_live_`,
  `ppk_test_`), else `PHOENIXPERPS_ENVIRONMENT`; a key of the other environment fails before any request.
- `WithBaseURL` (else `PHOENIXPERPS_BASE_URL`) is the REST host; the REST address as the docs write it, with `/v1`,
  works too. The streams keep their own addresses (`WithStreamURLs`).
- Every call takes a `context.Context` first and `CallOption`s last: `WithIdempotencyKey`, `WithRequestTimeout`,
  `WithRequestMaxRetries`, `WithHeader`, `WithRawResponse(&raw)` (the answer's status, headers, request id and
  Idempotency-Key).
- The key never appears when the client is printed or marshalled (`client.MaskedKey()` shows its end).

## Calls

Services follow the API: `Platform`, `Me`, `Markets`, `Accounts`, `History`, `Margin`, `Positions`, `Orders`, `Fills`,
`Exits`, `Strategies`, `Events`, `WebhookEndpoints`, `TradingView`. Prices and sizes are `Decimal`, a string type:
exact, never a float (`DecimalOf(f)` writes a float in plain form). Optional fields are pointers: `Dec("1")`,
`Ptr(true)`. Enums are open: a value from a newer API is kept as is.

- A call is tried again only when that is safe (a read, a call the API marks retry-safe, or one carrying its
  Idempotency-Key, yours included), after a network failure, a timeout, or an answer the API marks `retryable` (409
  `operation_in_progress`: the same key while the first call still runs; an answer without the API's envelope: 429 or
  5xx). Writes get one Idempotency-Key, the same on every attempt.
- `Retry-After` (fractions of a second included) is honoured; one over 60 s is not slept: the call fails with
  `ErrRateLimit` and `RetryAfter` set.
- With a rate-limit budget used up (read, trade or public), the next call of that budget waits for its reset instead
  of drawing a 429.
- A 304 Not Modified (your own `If-None-Match` header) is no error: nothing is read (a list answers a nil page), and
  `WithRawResponse` shows the 304 and its ETag.
- `client.Do(ctx, method, path, query, body, opts...)` sends any route with the same key, retries and errors, and
  answers the answer as it came.

## Lists

```go
page, err := client.Orders.List(ctx, accountID, &phoenixperps.OrdersListParams{Status: phoenixperps.Ptr(phoenixperps.OrdersListParamsStatusWorking)})
next, err := page.GetNextPage(ctx) // nil after the last
// page.HistoryComplete (orders): false when the history reaches further back than the list covers

for fill, err := range client.Fills.ListAutoPaging(ctx, accountID, nil, phoenixperps.WithMaxItems(500)) {
	if err != nil { ... }
}
```

## Errors

Every failure is a `*phoenixperps.Error` with `Kind`, `Status`, `Code`, `Message`, `RequestID`, `Details`,
`RetryAfter`, `Retryable` (the API's own flag; true for a timeout or a network failure), `DocURL`, `OrderID`,
`IdempotencyKey` and the answer's `Header`. `errors.Is` tells the family: `ErrAuthentication` (401), `ErrPermission`
(403), `ErrNotFound`, `ErrConflict` (409), `ErrValidation` (400), `ErrRuleRejected` (422: the account's rules said no),
`ErrRateLimit`, `ErrServer` (a 2xx that is no JSON, a proxy's page, too), `ErrConnection`, `ErrTimeout`,
`ErrWebhookVerification`. Every code the API answers is an `ErrCode*` constant.

`ErrTimeout` is the SDK's own per-attempt timeout (`WithTimeout`, `WithRequestTimeout`). A call ended by your own
context answers `ctx.Err()` as is (`context.Canceled`, `context.DeadlineExceeded`): its outcome is unknown, so bound a
write with your own `WithIdempotencyKey` (or a `client_order_id`) and call again with a fresh context.

```go
var e *phoenixperps.Error
if errors.As(err, &e) && e.Code == phoenixperps.ErrCodeInsufficientMargin { ... }
```

## Streams (read only; orders go through REST)

```go
s := client.AccountStream(ctx, phoenixperps.AccountStreamOptions{
	Risk:   true,
	OnRisk: func(accountID string, risk *phoenixperps.AccountRisk) { ... }, // each risk frame as it comes
	OnGap:  func(after string) { ... },                                     // events after this one may be missing
})
defer s.Close()
for event, err := range s.Events() { // each event once (event.Seq, event.Replayed), resumed after reconnects
	...
}

m := client.MarketStream(ctx)
_, err = m.Subscribe(ctx, "ticker", []string{"BTC-USD"}, map[string]any{"interval_ms": 250})
for frame, err := range m.Frames() { ... }
```

- Both reconnect by themselves, also after three heartbeats without a frame (a connection a proxy or a sleeping
  laptop left half open), and after an upgrade refused with 429 or 5xx (its `Retry-After`); each reconnect is one line
  in the client's log, with its cause. Any other refusal ends the stream with the API's own error.
- A resume the server cannot cover, and a 4008 (too far behind), are gaps: `OnGap` hears the last event told; read
  `client.Events.List` after it for what was missed.
- An account the server refuses (`account_not_found`, `too_many_accounts`) ends the account stream: `Next` answers an
  `ErrValidation` with its code and `Details` (`account_id`, `max`), and `Ready()` is closed then too. A snapshot whose
  `error` is set (`state_unavailable`) is an account not read now, not a flat one: read it again by REST.
- `s.Ping` asks for a pong; `s.Unsubscribe(ctx, accounts)` stops following accounts, after a reconnect too.
- The market stream makes again what the server still holds: `Unsubscribe` needs no params (without a candle's
  `interval` it ends every interval; naming no market, every market of the channel). Made again, a subscription asks
  its history again: trades and candles already told can come again (a trade's id, a candle's time tell them apart).
  `List` and `Ping` ask the server for its list of subscriptions and a pong; `Limits()` are the hello's
  (subscriptions, symbols per subscribe, messages a second).

## Helpers

- `NewMarketRules(market)`: `RoundPrice`, `RoundSize`, `SizeForNotional`, `SizeForRisk(riskUSD, stopDistance)`,
  `WorstPrice(side, price, slippagePct)` (side `buy` or `sell`, any case; a slippage within the market's own min..max),
  in exact decimals with the market's own steps. What they cannot take (a string that is no decimal, another side) is
  an `ErrValidation`. A size for a notional or a risk is rounded down, even below `MinSize` (it may be 0): check it
  before you order.
- `VerifyWebhook(rawBody, header, secrets, tolerance)`: the `WebhookPayload` (the event with `APIVersion`,
  `Environment` and `Test`; a `webhook.ping` is a type like any other), or an `ErrWebhookVerification` whose `Code` is
  `missing_headers`, `stale`, `bad_signature` or `bad_body`. Pass the body exactly as received.

## Examples

`examples/01-accounts` to `12-error-handling`, the same twelve programs as in every SDK, each one
in every language in the docs' cookbook (https://docs.phoenixperpsfunding.com/cookbook). From the SDK's source: `go run ./examples/01-accounts`.
