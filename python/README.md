# phoenixperps

The Phoenix Perpetuals API for Python: every REST call (sync and async), the account stream and market data, with
retries, idempotency keys, paging and typed errors. Python 3.10 or later. Docs: https://docs.phoenixperpsfunding.com

```
pip install https://github.com/phoenixperps/sdk/releases/download/v1.0.0/phoenixperps-1.0.0-py3-none-any.whl
```

```python
from phoenixperps import PhoenixPerps

client = PhoenixPerps()                       # PHOENIXPERPS_API_KEY from the environment
account = client.accounts.list().data[0]

order = client.orders.create(account["id"], {
    "market": "BTC-USD", "side": "buy", "type": "market", "size": "0.01",
    "exits": {"take_profit": [{"distance": "600", "size_pct": 100}], "stop_loss": {"distance": "300"}},
})
```

Async: `AsyncPhoenixPerps`, the same calls awaited (`async with AsyncPhoenixPerps() as client: ...`).

## The client

`PhoenixPerps(api_key=None, *, environment=None, base_url=None, timeout=30.0, max_retries=2, app_info=None, http_client=None, account_stream_url=None, market_stream_url=None, logger=None)`

- `api_key`: else `PHOENIXPERPS_API_KEY`. Without one the client is still made: the public calls (markets, status) work,
  any other answers the API's `missing_api_key` (`AuthenticationError`), and no Authorization header goes out.
- The environment comes from the key itself (`ppk_live_`, `ppk_test_`; `environment=` or `PHOENIXPERPS_ENVIRONMENT`,
  `live` or `sandbox` in any case, only for a key without either); a key of the other environment fails before any
  request.
- `base_url`: else `PHOENIXPERPS_BASE_URL`, else the environment's host; the REST address as the docs write it
  (`https://api.phoenixperpsfunding.com/v1`) works too. It moves the REST calls only: the streams take
  `account_stream_url` and `market_stream_url`.
- Every call also takes `idempotency_key=`, `timeout=`, `max_retries=`, `headers=` and `raw_response=`.
- The key never appears in `repr(client)`, an error or a log line (`client.masked_key` shows its end).

## Calls

Resources follow the API: `platform`, `me`, `markets`, `accounts`, `history`, `margin`, `positions`, `orders`, `fills`,
`exits`, `strategies`, `events`, `webhook_endpoints`, `tradingview`. Prices and sizes are decimal strings; a `Decimal`
or a float goes out in plain decimal form, never an exponent.

- Answers are plain dicts, typed for your type checker with the TypedDicts of `phoenixperps.types` (`orders.create`
  answers an `Order`, `fills.list` a `Page[Fill]`): `from phoenixperps.types import Order`. A body is any mapping in the
  API's words, one of those TypedDicts too (`CreateOrder`).
- A call is tried again only when that is safe (a read, a call carrying an Idempotency-Key, an order change given one
  too, or a route the API marks retry-safe), after a network failure, a timeout, or an answer the API marks `retryable`
  (429 or 5xx when the answer is not the API's own). Writes get one Idempotency-Key, the same on every attempt.
- `Retry-After` is honoured (fractions too); one over 60 s is not slept: the call raises `RateLimitError` with
  `retry_after`.
- The rate limit is kept per budget (`client.rate_limits`: read, trade, public); with one used up, the next call of that
  budget waits for its reset instead of drawing a 429.
- `raw_response=RawResponse()` keeps the answer as it came: `status_code`, `headers` (`Idempotent-Replayed`, `ETag`),
  `body`, `request_id`. With `headers={"if-none-match": etag}`, an unchanged answer is a 304: the call returns `None`
  (a list call too), never an error. A 2xx whose body is not JSON (a proxy's page) is an error (`http_200`).
- A route this version does not know: `client.request("GET", "/v1/status", query=None, body=None)` answers a
  `RawResponse` (`.json()`, its body as it came), with the same key, retries and errors.

```python
from phoenixperps import RawResponse

raw = RawResponse()
order = client.orders.create(account["id"], body, idempotency_key=key, raw_response=raw)
print(raw.request_id, raw.headers.get("idempotent-replayed") == "true")   # the first order, sent again
```

## Lists

```python
page = client.orders.list(account["id"], {"status": "working"})     # page.data, page.has_more, page.next_cursor
for fill in client.fills.list(account["id"]).auto_paging_iter(500):  # every page, at most 500 items
    ...
async for fill in aclient.fills.list(account_id, max_items=500):    # async (await it: the first page, iterable too)
    ...
```

A page also has every other field of the list as the API names it (`page.as_of`, `page.history_complete`), and each
next page is asked with the first call's options (`headers=`, `timeout=`, ...).

## Errors

Every failure is a `PhoenixPerpsError` with `code`, `status`, `message`, `request_id`, `details`, `retry_after`,
`retryable` (the same call may work later: the API's own flag), `doc_url`, `order_id`, `idempotency_key` and the
answer's `headers`. Its class tells the family: `AuthenticationError` (401), `PermissionDeniedError` (403), `NotFoundError`, `ConflictError` (409),
`ValidationError` (400), `RuleRejectedError` (422: the account's rules said no), `RateLimitError`, `ServerError`,
`APIConnectionError`, `APITimeoutError`.

## Streams (async, read only; orders go through REST)

```python
async with aclient.account_stream(risk=True) as stream:
    async for event in stream:                 # each event once, resumed after reconnects
        if event["type"] == "risk":            # an account's risk, as it changes (risk=True only)
            print(event["account_id"], event["data"]["equity"])
        elif event["type"] == "notice":        # a gap: what came after event["after"] may be missing here
            async for missed in aclient.events.list({"after": event["after"]}):
                print(missed["type"], missed["data"])
        else:
            print(event["type"], event["seq"], event["replayed"], event["data"])

async with aclient.market_stream() as market:
    await market.subscribe("ticker", ["BTC-USD", "ETH-USD"], interval_ms=250)
    async for frame in market:
        print(frame)
```

- The account stream tells each event once, with its frame's `seq` and `replayed`; with `risk=True`, risk frames too
  (also kept in `stream.risks`). A resume the server cannot cover, or a 4008 (too far behind), is a gap: a
  `{"type": "notice", "code": "resume_gap", "after": <the last event told>}`, to read `GET /v1/events?after=` from.
- `await stream.ping()`; `await stream.unsubscribe(["SANDBOX-00042-002"])`: an account unsubscribed stays out after a
  reconnect.
- A subscribe the account stream refuses (`account_not_found`, `too_many_accounts`) ends it with `ValidationError`
  (`account_id` and `details` name the account); `stream.ready` is set either way. A snapshot with `error` set could
  not be read now (its orders and positions are then empty): read that account by REST.
- A refused upgrade (429 or 5xx) or a dropped connection is tried again (after its Retry-After), one log line each
  (`logging.getLogger("phoenixperps")` at INFO, or the client's `logger`); any other refusal ends the stream with the
  API's own error, as a REST call would.
- The market stream reconnects after three heartbeats of silence and makes again the subscriptions the server still
  holds: unsubscribe needs only the channel and symbols (and `interval` for candles; without it, every interval;
  without symbols, the whole channel). `await market.list()`, `await market.ping()`; `market.limits` are the hello's
  (subscriptions, symbols_per_subscribe, messages_per_s).

## Helpers

- `MarketRules(client.markets.get("BTC-USD"))`: `round_price`, `round_size`, `size_for_notional`,
  `size_for_risk(risk_usd, stop_distance)`, `worst_price(side, price, slippage_pct)`, in exact decimals with the
  market's own steps and slippage bounds (a side other than buy or sell, a slippage out of bounds or a value that is no
  decimal: `ValidationError`, a `ValueError` too). A size is rounded down to the step, even below `min_size` (it may be
  `0`): check it against `min_size` before you order.
- `Webhooks.verify(raw_body, headers, secret_or_secrets, tolerance_seconds=300)`: the WebhookPayload (the event, with
  `api_version`, `environment` and `test`; `webhook.ping` has no `account_id`), or `WebhookVerificationError` (a
  `PhoenixPerpsError`: `missing_headers`, `stale`, `bad_signature`, `bad_body`). Pass the body exactly as received.

## Examples

`examples/01_accounts.py` to `12_error_handling.py`, the same twelve programs as in every SDK, each one
in every language in the docs' cookbook (https://docs.phoenixperpsfunding.com/cookbook). From the SDK's source: `python examples/01_accounts.py`.
