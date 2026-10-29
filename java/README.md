# phoenixperps-sdk (Java)

The Phoenix Perpetuals API for Java: every REST call, the account stream and market data, with retries, idempotency
keys, paging and typed errors. Java 17 or later; one dependency (Jackson). Docs: https://docs.phoenixperpsfunding.com

```kotlin
repositories {
    mavenCentral()
    maven("https://phoenixperps.github.io/sdk/maven")
}

dependencies {
    implementation("com.phoenixperpsfunding:phoenixperps-sdk:1.1.0")
}
```

```java
import com.phoenixperpsfunding.sdk.PhoenixPerpsClient;
import com.phoenixperpsfunding.sdk.gen.*;
import java.math.BigDecimal;

PhoenixPerpsClient client = PhoenixPerpsClient.fromEnv(); // PHOENIXPERPS_API_KEY
Account account = client.accounts().list().data().get(0);
Order order = client.orders().create(account.id(), new CreateOrder()
        .market("BTC-USD")
        .side(OrderSide.BUY)
        .type(CreateOrderType.MARKET)
        .size(new BigDecimal("0.01")));
```

## The client

`PhoenixPerpsClient.builder().apiKey(..).environment(..).baseUrl(..).timeout(..).maxRetries(..).appInfo(..).httpClient(..).logger(..).build()`

- The key comes from `apiKey`, else `PHOENIXPERPS_API_KEY`. The environment comes from the key itself (`ppk_live_`,
  `ppk_test_`; `PHOENIXPERPS_ENVIRONMENT` only for a key without one; `live` or `sandbox` in any case); a key of the
  other environment fails at `build()`, before any request.
- Without a key the client is made all the same: the public calls (`markets()`, `platform()`) work, sent without
  Authorization; any other answers the API's `missing_api_key`.
- `baseUrl` (else `PHOENIXPERPS_BASE_URL`) is the API host; the REST address as the docs write it (`.../v1`) works too.
  It moves the REST calls only: the streams take `streamUrls(account, market)`.
- Every call has an overload taking `RequestOptions` (`withIdempotencyKey`, `withTimeout`, `withMaxRetries`,
  `withHeaders`, `withOnResponse`).
- The key never appears in `toString()`, the client's JSON or an exception (`client.maskedKey()` shows its end).
- `logger` receives a line per retry (a call's, or a stream's reconnect with its cause) and one per deprecated API
  version.

## Calls

Resources follow the API: `platform()`, `me()`, `markets()`, `accounts()`, `history()`, `margin()`, `positions()`,
`orders()`, `fills()`, `exits()`, `strategies()`, `events()`, `webhookEndpoints()`, `tradingView()`. Prices and sizes
are `BigDecimal`, exact, sent as strings. Enums are open: a value from a newer API is kept as is, and fields this
version does not know land in `extra()`.

- A call is tried again only when that is safe (a read, a route the API marks retry-safe, or a call carrying an
  Idempotency-Key: an order change given one with `withIdempotencyKey` too), after a network failure, a timeout (the
  answer's body included) or an answer the API marks `retryable` (so a 409 `operation_in_progress` is asked again, a
  503 `sandbox_disabled` is not). Writes get one Idempotency-Key, the same on every attempt.
- `Retry-After` is honoured, fractions included; one over 60 s is not slept: the call throws `RateLimitException` with
  `retryAfter()`.
- With a rate-limit budget used up, the next call of that budget waits for its reset instead of drawing a 429.
- An `If-None-Match` (`withHeaders`) that still matches answers `null` (a list call too, never an empty page): the
  API's empty 304, nothing changed; `withOnResponse` sees the 304 and its ETag.
- A 2xx whose body is not JSON (a proxy's page) is an error (`http_<status>`), never an empty answer.
- A route newer than this SDK: `client.request("GET", "/v1/...", query, body, options)` sends it with the key, retries
  and errors, and answers the `RawResponse` as it came (a write is tried again only with `withIdempotencyKey`).

## Lists

```java
Page<Order> page = client.orders().list(accountId, new OrdersListParams().status(OrdersListParamsStatus.WORKING));
Page<Order> next = page.nextPage(); // null after the last
Object complete = page.extra().get("history_complete"); // the list's other fields

for (Fill fill : client.fills().listAutoPaging(accountId, null, RequestOptions.none().withMaxItems(500))) {
}
```

## Errors

Every failure is a `PhoenixPerpsException` with `code()`, `status()`, `getMessage()`, `requestId()`, `details()`,
`retryAfter()`, `retryable()`, `docUrl()`, `orderId()`, `idempotencyKey()` and `headers()`. Its class tells the
family: `AuthenticationException` (401), `PermissionException` (403), `NotFoundException`, `ConflictException` (409),
`ValidationException` (400), `RuleRejectedException` (422: the account's rules said no), `RateLimitException`,
`ServerException`, `ApiConnectionException`, `ApiTimeoutException`, `Webhooks.VerificationException`. Every code the
API answers is a constant of `ErrorCodes`.

## Streams (read only; orders go through REST)

```java
AccountStream.Options options = AccountStream.options()
        .risk(true).onRisk(frame -> System.out.println(frame)) // risk frames as they come
        .onGap(after -> System.out.println("read GET /v1/events?after=" + after)); // what a reconnect could not resume
try (AccountStream stream = client.openAccountStream(options)) {
    for (Event e : stream) { // each event once, resumed after reconnects; e.extra() keeps its frame's seq and replayed
    }
}

try (MarketStream market = client.openMarketStream()) {
    market.subscribe("ticker", List.of("BTC-USD"), Map.of("interval_ms", 250), Duration.ofSeconds(10));
    for (MarketStream.Frame f : market) {
    }
}
```

- Both reconnect by themselves (a line to the `logger` each time). What a reconnect cannot change ends the stream,
  thrown by `next()`/`poll()` and by `ready()`: an upgrade refused for good (401, 403, 404...: not 429 or 5xx) with the
  server's error, the account stream's subscribe refused (`too_many_accounts`, `account_not_found`) with a
  `ValidationException` (its `details()` name the account), 4001 with an `AuthenticationException`.
- A resume the server cannot cover (its `resume_gap` notice) and a 4008 (too far behind) are gaps: told to `onGap` and
  the `logger` with the last event id told, to read `GET /v1/events?after=` from.
- A snapshot with an `error` (`state_unavailable`) is an account that could not be read just now, not a flat one.
- `stream.ping(wait)` and `stream.unsubscribe(accounts, wait)` on the account stream (an account unsubscribed stays
  out after a reconnect); `market.list(wait)` and `market.ping(wait)` on the market stream.
- A market subscription is one channel, one market and (candles) one interval; its other params are settings the last
  subscribe sets. `market.unsubscribe(channel, symbols, params, wait)` names the markets and (candles, in params) the
  interval, nothing else; without an interval it ends every interval of those markets. Made again after a reconnect, a
  subscription asks its history again. `market.limits()` is what the hello allows (`subscriptions`,
  `symbols_per_subscribe`, `messages_per_s`); an `error` frame keeps its `details()`.

## Helpers

- `new MarketRules(client.markets().get("BTC-USD"))`: `roundPrice`, `roundSize`, `sizeForNotional`,
  `sizeForRisk(riskUsd, stopDistance)`, `worstPrice(side, price, slippagePct)` (an `OrderSide`, or `"buy"`/`"sell"`
  in any case), in exact decimals, from `BigDecimal`s or decimal strings. Each answers the `BigDecimal` an order takes,
  at the step's decimals (`.size(rules.sizeForNotional(usd, bid))`). Another side, a slippage outside the market's
  `max_slippage_pct` bounds, a price or stop distance of 0 or a string that is no decimal throws a
  `ValidationException`. A size is answered as it rounds, 0 included: check it against `minSize()` before you order.
- `Webhooks.verify(rawBody, headers, secret)`: the `WebhookPayload` (the event, with `apiVersion()`, `environment()`
  and `test()`; `webhook.ping` included), or a `Webhooks.VerificationException` (`missing_headers`, `stale`,
  `bad_signature`, `bad_body`). Pass the body exactly as received.

## Examples

`examples/01-accounts.java` to `12-error-handling.java` (compiled with the SDK), the same twelve programs as in every SDK, each one
in every language in the docs' cookbook (https://docs.phoenixperpsfunding.com/cookbook). From the SDK's source: `gradle -q example -Pn=01`.
