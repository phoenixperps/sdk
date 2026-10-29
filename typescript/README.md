# @phoenixperps/sdk

The Phoenix Perpetuals API for Node.js and TypeScript: every REST call, the account stream and market data, with
retries, idempotency keys, paging and typed errors. Node 22 or later. Docs: https://docs.phoenixperpsfunding.com

```
npm install https://github.com/phoenixperps/sdk/releases/download/v1.1.0/phoenixperps-sdk-1.1.0.tgz
```

```js
const { PhoenixPerps } = require('@phoenixperps/sdk');   // or: import PhoenixPerps from '@phoenixperps/sdk'

const client = new PhoenixPerps();                          // PHOENIXPERPS_API_KEY from the environment
const [account] = (await client.accounts.list()).data;

const order = await client.orders.create(account.id, {
  market: 'BTC-USD', side: 'buy', type: 'market', size: '0.01',
  exits: { take_profit: [{ distance: '600', size_pct: 100 }], stop_loss: { distance: '300' } },
});
```

## The client

`new PhoenixPerps({ apiKey, environment, baseUrl, timeout, maxRetries, appInfo, logger, fetch, accountStreamUrl, marketStreamUrl })`

- `apiKey`: else `PHOENIXPERPS_API_KEY`. `environment` (`live` or `sandbox`, in any case): else the key's own
  (`ppk_live_`, `ppk_test_`), else `PHOENIXPERPS_ENVIRONMENT`; a key of the other environment fails before any request.
- Without a key the client still works for the public calls (`markets`, `platform.status`): no Authorization is sent,
  and any other call answers the API's `missing_api_key`.
- `baseUrl` (else `PHOENIXPERPS_BASE_URL`) is the REST host (`https://.../v1` as the docs write it works too) and moves
  the REST calls only; the streams have their own: `accountStreamUrl` and `marketStreamUrl`.
- `timeout` (ms, 30000) and `maxRetries` (2) per client; every call also takes them, plus `idempotencyKey` and `signal`:
  `client.orders.create(id, body, { idempotencyKey, timeout: 5000 })`.
- The key never appears in a printed client, its JSON, an error or a log line (`client.maskedKey` shows its end).

## Calls

Resources follow the API: `platform`, `me`, `markets`, `accounts`, `history`, `margin`, `positions`, `orders`, `fills`,
`exits`, `strategies`, `events`, `webhookEndpoints`, `tradingview`. Prices and sizes are decimal strings, in and out
(a number goes out in its decimal form, never with an exponent).

- A call is tried again only when that is safe (a read, or a write carrying its Idempotency-Key), after a network
  failure, a timeout, or an answer the API marks `retryable` (409 `operation_in_progress` among them; an answer
  without the API's envelope: 429 or 5xx). Writes get one Idempotency-Key, the same on every attempt; a change of an
  order (`orders.modify`) is sent once, or as often as needed when you give it an `idempotencyKey`.
- `Retry-After` is honoured (fractions too); one over 60 s is not slept: the call throws `RateLimitError` with
  `retryAfter`.
- With the rate limit used up, the next call waits for its reset instead of drawing a 429.
- A result carries `.meta` (not enumerable): `status`, `headers` (`Idempotent-Replayed`, ...), `requestId`,
  `idempotencyKey`. Any call also takes `onMeta(meta)`, told every answer's meta.
- Sent with `If-None-Match` (in `headers`), a call whose answer did not change (304) answers `null`, a list call too;
  `onMeta` gets the 304 and its `ETag`.

Any route, raw, through the same key, retries and errors: the answer as it came.

```js
const { status, headers, body } = await client.request('GET', '/v1/events', { after: 'evt_...' });
```

## Lists

```js
const page = await client.orders.list(account.id, { status: 'all' });   // { data, hasMore, nextCursor, asOf, historyComplete }
for await (const fill of client.fills.list(account.id, {}, { maxItems: 500 })) { ... }   // every page
```

A page keeps every field of the list, camelCased. Every page is asked with the call's options (`headers`, `timeout`,
`signal`, ...).

## Errors

Every failure is a `PhoenixPerpsError` with `code`, `status`, `message`, `requestId`, `details`, `retryAfter`,
`retryable` (the API's flag; true for a timeout or a network failure), `docUrl`, `orderId` and `idempotencyKey`, and the
answer's `headers` when one came. Its class tells the family: `AuthenticationError` (401),
`PermissionError` (403), `NotFoundError`, `ConflictError` (409), `ValidationError` (400), `RuleRejectedError` (422: the
account's rules said no), `RateLimitError`, `ServerError`, `ConnectionError`, `TimeoutError` (the body's time counts),
all exported by the package. A call you abort with `signal` rejects with the signal's own reason, as `fetch` does
(give a write its own `idempotencyKey` to send it again safely).

```js
try { await client.orders.create(id, body); }
catch (err) { if (err instanceof RuleRejectedError) console.log(err.code, err.details); else throw err; }
```

## Streams (read only; orders go through REST)

```js
const stream = client.accountStream({ risk: true });
stream.on('snapshot', (s) => console.log(s.account_id, s.positions, s.orders));   // one per account, then 'ready'
stream.on('risk', (r) => console.log(r.account_id, r.data));                       // as it comes (risk: true only)
stream.on('gap', ({ after }) => console.log(`missed events: GET /v1/events?after=${after}`));
for await (const event of stream) console.log(event.seq, event.type, event.data);  // each event once, resumed after reconnects

const market = client.marketStream();
market.on('ticker', (f) => console.log(f.symbol, f.data.bid, f.data.ask));   // an event per channel
await market.subscribe('ticker', ['BTC-USD', 'ETH-USD'], { interval_ms: 250 });
await market.unsubscribe('ticker', ['ETH-USD']);
```

- A stream reconnects by itself after a close, three heartbeats of silence, a network failure or an upgrade refused
  with 429 or 5xx (after its Retry-After); each reconnect is a `'reconnect'` event `{ in_ms, resume, cause, error? }`
  (`error`: the refusal it waits out) and one `logger.warn` line. Any other refusal ends it with the API's error
  (`AuthenticationError` for a bad key): `'error'` is only what ends a stream, so `events.once(stream, 'ready')` waits
  through what is tried again.
- The account stream resumes from the last event. Events it could not tell (a resume the server no longer covers, or
  a 4008 when it fell too far behind) are a `'gap'` `{ code, after }`: read them with `client.events.list({ after })`;
  the snapshot that follows is the state as it stands. Each event keeps its frame's `seq` and `replayed`.
- `stream.ping()`, `stream.unsubscribe(accounts)` (they stay out after a reconnect). The stream ends with a
  `ValidationError` when the server refuses its subscribe (`too_many_accounts`, `account_not_found` with `accountId`),
  as it ends with an `AuthenticationError` when the key ends; `events.once(stream, 'ready')` rejects with it.
- A snapshot with `error` set is an account that could not be read now (read it by REST), not a flat one.
- A market subscription is one channel, one market and (candles) one interval; a subscribe again changes its
  settings. `unsubscribe(channel, symbols, { interval })` ends them (no interval: every interval). Each is made again
  after a reconnect with its history, so trades and candles already told can come again: a trade's `id` and a
  candle's `time` tell them apart. `market.list()`, `market.ping()`, and `market.limits` (the server's, per connection).

## Examples

`examples/01-accounts.mts` to `12-error-handling.mts`, the same twelve programs as in every SDK, each one
in every language in the docs' cookbook (https://docs.phoenixperpsfunding.com/cookbook). From the SDK's source: `node examples/01-accounts.mts`.

## Helpers

- `new MarketRules(await client.markets.get('BTC-USD'))`: `roundPrice`, `roundSize`, `sizeForNotional`,
  `sizeForRisk(riskUsd, stopDistance)`, `worstPrice(side, price, slippagePct)`, all with the market's own steps and
  slippage bounds. A size is rounded down and may be `'0.000'`: check it against `min_size` before you order. A side
  that is not buy or sell, a slippage outside the market's, or a value that is no decimal is a `ValidationError`.
- `Webhooks.verify(rawBody, headers, secretOrSecrets, toleranceSeconds = 300)`: the `WebhookPayload` (the event, with
  `api_version`, `environment` and `test`; `webhook.ping` too), or `WebhookVerificationError`, a `PhoenixPerpsError`
  (`missing_headers`, `stale`, `bad_signature`, `bad_body`). Pass the body exactly as received.
