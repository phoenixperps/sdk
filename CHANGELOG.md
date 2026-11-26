# Changelog

One version for every SDK (`VERSION`). Inside v1 the API only adds; a release says what each SDK gained or fixed.

## 1.3.1

What the second QA run found, fixed; spec 1.1.2.

- Every SDK: a raw cancel (the raw request) of an order already canceled or expired answers that order, read at the
  path it was sent to; it read the route's template before (Go answered 404 account_not_found, TypeScript, Python, C#,
  Java and PHP the same way).
- Every SDK: a key of one environment with the other one's API host as its base URL fails before any request
  (`wrong_environment`), as a key of the other environment does.
- Every SDK: the error code `account_closed` (a closed sandbox account's figures) and the spec 1.1.2.
- Go, C#, Java: the exits batch's `trail` and `break_even` take an explicit null (`TrailNull`, `BreakEvenNull`;
  `ClearTrail()`, `ClearBreakEven()`), as every field the API takes as null.
- Go: `Page` has `Stale` (a list read from figures older than usual); examples 04 (the lookup checked before the
  cancel) and 09 (the equity points' values) as the cookbook has them.
- Python: an async list call awaited, then iterated, stops at its `max_items`; a subscription made again after a
  reconnect that the server takes only in part is told to the reader, and the refused markets are not asked again;
  `account_stream()` and `market_stream()` answer their stream types and frames are dicts (mypy --strict clean on the
  package and every example).
- READMEs: webhook verification without a secret (`missing_secret`), in every SDK.

## 1.3.0

Rate limits that never stall other calls, safer retries, stricter webhook checks, and the same rules in every SDK.

- Rate limits, every SDK: the state is kept per budget, the operation's own and the bucket the answer names
  (`X-RateLimit-Scope`: key, bulk, account_orders...), so a bucket bulk calls emptied holds bulk calls only. With
  nothing left, the next call waits for the request the budget gives back (60 s / `X-RateLimit-Limit`, or a 429's
  `Retry-After`), never for the whole window. The rate-limit state names its scope.
- Retries: a write that takes an `Idempotency-Key` gets one, required or not (`orders.modify`, `exits.set`), so it is
  sent again safely after a lost answer. A cancel answered `order_not_working` with the order already canceled or
  expired answers that order (the order no longer works, as asked); filled still fails. The raw request keeps a
  known route's Idempotency-Key, retries and budget (Python, Go, C#, Java, Rust), and Java sends a String body as the
  JSON it is.
- Webhooks: a missing or empty secret is a validation error (`missing_secret`), never a check against an empty key;
  empty secrets beside a real one are dropped.
- A `Deprecation` header is told once on any answer, with or without rate-limit headers.
- Market data streams: a subscription of more symbols than one message holds (the hello's `symbols_per_subscribe`) is
  sent in parts, answered as one, and made again so after a reconnect; the replay is paced at `messages_per_s`.
  TypeScript: a refused change leaves the live subscription as the server holds it. Python: an error no ask waits for
  reaches the reader as a frame. Go: `Frames` yields market data only.
- Decimals, every SDK: a decimal string is a sign, digits with a fraction and an optional exponent (`6.2E+4`, read
  exactly); spaces, `+`, `1/3`, `0x10` and `1_000` are validation errors. A zero step or price in the helpers is a
  validation error too.
- A request field the API takes as null ("null removes it", a price alert's note) can be sent as null: Go
  `NoteNull`, C# `ClearNote()`, Java `clearNote()`.
- TypeScript: types that need nothing else installed (no `@types/node`); `ws` ^8.23.0; `ping()` and listeners start
  the account stream; a raw path's own query is kept; a header the runtime refuses is a ValidationError, not retried.
- Python: the spec's required fields are `Required` in `phoenixperps.types` and `mypy --strict` passes on the package;
  `async for` over an `AsyncPage` walks every page.
- C# and Java: auto-paging answered 304 throws `NotModifiedException`. Java: `rateLimit()` (limit, remaining,
  resetAt, scope; the older accessors are deprecated), exceptions print their code, status and request id, an
  interrupt is `cancelled` and not retryable, and a connect timeout is named as the HttpClient's.
- PHP: the key never shows in `var_export`; `orders->cancelAll($accountId)` and `positions->close($accountId,
  $market)` take no body.
- The spec 1.1.0: the status page's `engine.writes` and `account_data.state`, the risk's `as_of`, `stale` and
  `paused`, the limits' opening spacing.

## 1.2.0

Price alerts, conditional orders and the restricted countries.

- Every SDK: `alerts` (create, list, get, modify, rearm, delete): a level on a market, told once (`alert.triggered`)
  when the price reaches it, never trading; the events group `alerts` for webhooks.
- The `conditional` strategy: an order (market or limit) sent once a price is reached, on its own market (a buy when
  the price falls to `trigger_price`, a sell when it rises) or on another one (`trigger_market`, `trigger_direction`),
  with `expires_at` and `exits`; the event `strategy.triggered`.
- `platform.restrictedCountries`: the countries whose residents cannot sign up or buy.
- The error codes `trigger_already_met`, `alert_not_found`, `alert_limit`, `alert_at_price`, `alert_not_armed`; the
  limits' `alerts`.

## 1.1.0

For a market's options: the same asset on another venue, or on Standard Market Hours.

- Every SDK: `Market` gains `venue`, `trading_hours` (`24/7`, `standard`, `fx`), `option_of`, and `options` (each
  `{ market, venue, trading_hours }`, the market first); the error codes `market_option_held` (with
  `details.held_market`) and `weekend_close`.
- A margin setting's `max_leverage` is documented as the account's own (its trading rule's), null when the market is
  not available on the account.

## 1.0.0

The first release, for the API's v1.

- TypeScript, Python, Go, C#, Java, Rust and PHP: every operation of the API, the account stream with resume, the
  market data stream, retries only when safe, an Idempotency-Key per write, paging, typed errors.
- `MarketRules` (exact rounding to the tick and the step, sizes for a notional or a risk, worst prices) and
  `Webhooks.verify`.
- Twelve examples and a starter bot in every language.
