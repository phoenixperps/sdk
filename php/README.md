# phoenixperps/sdk (PHP)

The Phoenix Perpetuals API for PHP: every REST call, the account stream and market data, with retries, idempotency
keys, paging and typed errors. PHP 8.2 or later (Guzzle 7, phrity/websocket 3). Docs: https://docs.phoenixperpsfunding.com

```
composer config repositories.phoenixperps composer https://phoenixperps.github.io/sdk/composer
composer require phoenixperps/sdk
```

```php
use PhoenixPerps\Client;
use PhoenixPerps\Enum\CreateOrderType;
use PhoenixPerps\Enum\OrderSide;
use PhoenixPerps\Model\CreateOrder;

$client = new Client(); // PHOENIXPERPS_API_KEY from the environment
$account = $client->accounts->list()->data[0];
$order = $client->orders->create($account->id, new CreateOrder(
    market: 'BTC-USD',
    side: OrderSide::BUY,
    type: CreateOrderType::MARKET,
    size: '0.01',
));
```

A body can also be an array in the API's own words: `['market' => 'BTC-USD', 'side' => 'buy', 'type' => 'market', ...]`.

## The client

`new Client(apiKey: ..., environment: ..., baseUrl: ..., timeout: 30.0, maxRetries: 2, appInfo: ..., httpClient: ..., logger: ..., accountStreamUrl: ..., marketStreamUrl: ...)`

- The key comes from `apiKey`, else `PHOENIXPERPS_API_KEY`. The environment (`live` or `sandbox`, in any case) comes
  from the key itself (`ppk_live_`, `ppk_test_`; `PHOENIXPERPS_ENVIRONMENT` only for a key without one); a key of the
  other environment fails in the constructor, before any request.
- Without a key the client is made all the same: the public calls (`markets`, `platform`) work, any other answers the
  API's `missing_api_key` (an `AuthenticationException`). No `Authorization` goes out without a key.
- `baseUrl` (else `PHOENIXPERPS_BASE_URL`) is the API host; the REST address as the docs write it (`.../v1`) works too.
  It moves the REST calls only: the streams take `accountStreamUrl` and `marketStreamUrl`.
- Every call takes a last `RequestOptions` (`idempotencyKey`, `timeout`, `maxRetries`, `maxItems`, `headers`,
  `onResponse`).
- `logger` receives `(level, message)`: a line per retry, per stream reconnect (with its cause) and per stream gap, and
  one per deprecated API version (`Deprecation`).
- The key never appears when the client is printed, dumped, logged or in an exception (`$client->maskedKey()` shows
  its end); the client cannot be serialized.
- Any route, also one newer than this SDK: `$client->request('GET', '/v1/...', $query, $body, $options)` sends it with
  the key, retries and errors, and answers the `RawResponse` as it came (`status`, `headers`, `body`). A route this SDK
  knows keeps its own rules (tried again when retry-safe, its own Idempotency-Key); another is tried again when a GET,
  or sent with an `idempotencyKey`.
- An `If-None-Match` (in `headers`) that still matches is no error: the API's empty 304 answers `null` (a read's
  types are nullable: `?Market`, `?Page`); `onResponse`, or `request()`, shows the 304 and its `etag`.

## Calls

Resources follow the API: `platform`, `me`, `markets`, `accounts`, `history`, `margin`, `positions`, `orders`, `fills`,
`exits`, `strategies`, `events`, `webhookEndpoints`, `tradingView`. Answers are readonly objects (camelCase properties);
prices and sizes are strings, exact. Enums are open: the fields are strings and `Enum\*` classes name the known values;
fields this version does not know land in `$extra`.

- A call is tried again only when that is safe (a read, a retry-safe route, or a call carrying an Idempotency-Key: an
  order change takes yours), after a network failure, a timeout, or an answer the API marks `retryable` (without its
  envelope: 429 and 5xx). So `operation_in_progress` is asked again with the same key, `sandbox_disabled` is not.
  Writes get one Idempotency-Key, the same on every attempt.
- `Retry-After` is honoured (fractions too); one over 60 s is not slept: the call throws `RateLimitException` with
  `retryAfter`.
- With a budget used up, the next call of that budget waits for its reset instead of drawing a 429
  (`$client->rateLimit()`: what the last answer said).
- A 2xx that is not JSON (a proxy's page) throws a `ServerException` (`http_<status>`).

## Lists

```php
$page = $client->orders->list($accountId, ['status' => 'working']);
$next = $page->nextPage(); // null after the last

foreach ($client->fills->listAutoPaging($accountId, null, new RequestOptions(maxItems: 500)) as $fill) {
}
```

A page's `data` is a plain array (`end($page->data)`); the list's other fields land in its `extra` (the orders list's
`history_complete`: false when the history reaches further back than the list covers).

## Errors

Every failure is a `PhoenixPerpsException` with `errorCode`, `status`, `getMessage()`, `requestId`, `details`,
`retryAfter`, `orderId`, `idempotencyKey`, `retryable` (whether the same call may work if sent again later, as the API
said), `docUrl` and the answer's `headers`. Its class tells the family: `AuthenticationException` (401),
`PermissionException` (403), `NotFoundException`, `ConflictException` (409), `ValidationException` (400),
`RuleRejectedException` (422: the account's rules said no), `RateLimitException`, `ServerException`,
`ApiConnectionException`, `ApiTimeoutException`. Every code the API answers is a constant of `ErrorCodes`.

## Streams (read only; orders go through REST)

```php
$stream = $client->accountStream(
    risk: true,
    onRisk: fn (array $frame) => print("risk of {$frame['account_id']}\n"),  // each risk frame as it comes
    onGap: fn (?string $after) => print("events after {$after} may be missing\n"), // read GET /v1/events?after=
);
foreach ($stream as $event) { // blocks; each event once, resumed after reconnects
}

$market = $client->marketStream();
$market->subscribe('ticker', ['BTC-USD'], ['interval_ms' => 250]);
foreach ($market as $frame) {
}

$stream = $client->accountStream();
while ($running) {                 // a bot that does other things: poll() gives control back
    $event = $stream->poll(1.0);   // the next event, or null after a second without one
}
```

- Both reconnect by themselves, also after three heartbeats without a frame (a connection a proxy or a sleeping
  laptop left half open), an upgrade refused with 429 or 5xx after its `Retry-After`; each reconnect is a `logger`
  line with its cause. Both have `poll($seconds)` (the next item, else null) and `close()`.
- What a reconnect cannot change ends the stream with an exception: any other refused upgrade with the API's error
  (`errorCode`, `requestId`, `details`), 4001 with an `AuthenticationException`, the account stream's subscribe refused
  (`too_many_accounts`, `account_not_found` with its `accountId`) with a `ValidationException`.
- Each event keeps its frame's `seq` and `replayed` in `$event->extra`. A resume the server could not cover, or a
  4008 (too far behind), is a gap: `onGap` receives the last event id told (and the `logger` a line).
- A snapshot (`$stream->snapshot($accountId)`) with an `error` (`state_unavailable`) is an account that could not be
  read just now (read it by REST), not a flat one.
- `$stream->ping()` and `$stream->unsubscribe($accounts)` on the account stream (an account unsubscribed stays out
  after a reconnect); `$market->list()` and `$market->ping()` on the market stream, and `$market->limits` (the hello's
  `subscriptions`, `symbols_per_subscribe`, `messages_per_s`). A subscribe the market stream refuses throws a
  `ValidationException` with the server's `details` (`field`, `issue`).
- A market `subscribe()` or `unsubscribe()` waits for its answer (sent again on each new connection) until the stream
  ends, or the `$wait` you give. A market subscription is a channel, a market and (candles) an `interval`; its other
  params are settings the last subscribe sets. `$market->unsubscribe('candles', ['BTC-USD'])` ends every interval of that market, with
  `['interval' => '1m']` that one only. Made again after a reconnect, a subscription sends its history again (a trade's
  id, a candle's time tell one already seen).

## Helpers

- `new MarketRules($client->markets->get('BTC-USD'))` (bcmath): `roundPrice`, `roundSize`, `sizeForNotional`,
  `sizeForRisk($riskUsd, $stopDistance)`, `worstPrice($side, $price, $slippagePct)` (side `buy` or `sell` in any case,
  the slippage within the market's bounds), in exact decimals. What the API would refuse (a side, a slippage out of
  bounds, a value that is no plain decimal, a price or stop distance of 0) throws its `ValidationException`
  (`invalid_request`).
- `Webhooks::verify($rawBody, $headers, $secret)`: the `WebhookPayload` (the event with `apiVersion`, `environment`
  and `test`: a `webhook.ping`, or a delivery resent as a test), or a `WebhookVerificationException` (a
  `PhoenixPerpsException`: `missing_headers`, `stale`, `bad_signature`, `bad_body`). Pass the body exactly as received.
  No secret (an unset variable) is an `InvalidArgumentException`.

## Examples

`examples/01-accounts.php` to `12-error-handling.php`, the same twelve programs as in every SDK, each one
in every language in the docs' cookbook (https://docs.phoenixperpsfunding.com/cookbook). From the SDK's source: `php examples/01-accounts.php`.
