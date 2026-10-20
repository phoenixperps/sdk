# phoenixperps (Rust)

The Phoenix Perpetuals API for Rust: every REST call, the account stream and market data, with retries, idempotency
keys, paging and typed errors. Async on tokio, TLS by rustls. Rust 1.85 or later.
Docs: https://docs.phoenixperpsfunding.com

```toml
[dependencies]
phoenixperps = { git = "https://github.com/phoenixperps/sdk", tag = "v1.0.0" }
tokio = { version = "1", features = ["macros", "rt-multi-thread"] }
rust_decimal = "1"
```

```rust
use phoenixperps::{Client, CreateOrder, CreateOrderType, OrderSide};
use rust_decimal::Decimal;

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?; // PHOENIXPERPS_API_KEY
    let accounts = client.accounts().list().await?;
    let order = client
        .orders()
        .create(&accounts.data[0].id, &CreateOrder {
            market: "BTC-USD".into(),
            side: OrderSide::Buy,
            r#type: CreateOrderType::Market,
            size: Some(Decimal::new(1, 2)), // 0.01
            ..Default::default()
        })
        .await?;
    println!("{} {}", order.id, order.status);
    Ok(())
}
```

## The client

`Client::builder().api_key(..).environment(..).base_url(..).timeout(..).max_retries(..).app_info(..).http_client(..).logger(..).build()?`

- The key comes from `api_key`, else `PHOENIXPERPS_API_KEY`. The environment (`live` or `sandbox`, in any case) comes
  from the key itself (`ppk_live_`, `ppk_test_`), before `PHOENIXPERPS_ENVIRONMENT`; a key of the other environment
  fails at `build()`, before any request.
- Without a key the client is made all the same: the public calls (`markets()`, `platform().status()`) work, any other
  answers the API's `missing_api_key`; no Authorization is sent.
- `base_url` is the API host (else `PHOENIXPERPS_BASE_URL`); the REST address as the docs write it, with `/v1`, works
  too.
- Every call has a `_with` twin taking `RequestOptions` (`idempotency_key`, `timeout`, `max_retries`, `max_items`,
  `headers`, `on_response`). A read's twin answers `Result<Option<T>>`: `None` is a 304 Not Modified to the
  `If-None-Match` you put in `headers` (the plain read, which sends none, answers `Result<T>`).
- The key never appears in `Display` or `Debug` (`client.masked_key()` shows its end).

## Calls

Resources follow the API: `platform()`, `me()`, `markets()`, `accounts()`, `history()`, `margin()`, `positions()`,
`orders()`, `fills()`, `exits()`, `strategies()`, `events()`, `webhook_endpoints()`, `tradingview()`. Field names are
the API's own (snake_case; `r#type`). Prices and sizes are `rust_decimal::Decimal`, exact, sent as strings. Enums are
open: a value from a newer API lands in `Other(String)`, and fields this version does not know land in `extra`.

- A call is tried again only when that is safe (a read, or a write carrying an Idempotency-Key: its own, or one given
  in `RequestOptions`), after a network failure, a timeout, or an answer the API marks `retryable` (without its
  envelope: 429, 500, 502, 503, 504). So a 409 `operation_in_progress` is asked again with the same key, and a 503
  `sandbox_disabled` is not. Writes get one Idempotency-Key, the same on every attempt.
- `Retry-After` is honoured (fractions of a second too); one over 60 s is not slept: the call fails with
  `ErrorKind::RateLimit` and `retry_after`.
- With the rate limit used up, the next call waits for its reset instead of drawing a 429.
- `client.request("GET", "/v1/...", &[("market", "BTC-USD")], None, &options)` calls a route this version does not know
  yet, with the key, the retries and the errors; it answers the `RawResponse`, a 304 (sent with `If-None-Match` in
  `options.headers`) as it came, with its ETag.
- `RequestOptions::on_response` receives every call's answer as it came: status, headers, request id, Idempotency-Key.

## Lists

```rust
use futures_util::StreamExt;

let page = client.orders().list(&account_id, None).await?;
let next = page.next_page().await?; // None after the last
let complete = page.extra.get("history_complete"); // the list's other fields

let options = phoenixperps::RequestOptions { max_items: Some(500), ..Default::default() };
let mut fills = client.fills().list_auto_paging(&account_id, None, &options);
while let Some(fill) = fills.next().await {
    let fill = fill?;
}
```

## Errors

Every failure is a `phoenixperps::Error` with `kind`, `status`, `code`, `message`, `request_id`, `details`,
`retryable` (send the same call again later only when it is `Some(true)`), `doc_url`, `retry_after`, `order_id`,
`idempotency_key` and the answer's `headers`. `kind` tells the family: `Authentication` (401), `Permission` (403),
`NotFound`, `Conflict` (409), `Validation` (400), `RuleRejected` (422: the account's rules said no), `RateLimit`,
`Server`, `Connection`, `Timeout`, `StreamClosed`, `WebhookVerification`. Any answer that is not the API's (HTML, a 2xx
that is not JSON) is an error too, `http_<status>`. Every code the API answers is a constant of `codes`.

## Streams (read only; orders go through REST)

```rust
use phoenixperps::AccountMessage;

let mut stream = client.account_stream(phoenixperps::AccountStreamOptions { risk: true, ..Default::default() });
while let Some(message) = stream.next_message().await { // each event once, resumed after reconnects
    match message? {
        AccountMessage::Event(e) => println!("{} (seq {:?}, replayed {})", e.event.r#type, e.seq, e.replayed),
        AccountMessage::Risk(frame) => println!("risk of {}", frame["account_id"]),
        AccountMessage::Gap { after } => println!("events may be missing: read GET /v1/events?after={after:?}"),
        _ => {}
    }
}

let mut market = client.market_stream();
let mut params = serde_json::Map::new();
params.insert("interval_ms".into(), 250.into());
market.subscribe("ticker", &["BTC-USD"], params).await?;
while let Some(frame) = market.next().await {
    let frame = frame?;
}
```

Both reconnect by themselves, also when three heartbeats pass without a frame or when the upgrade is refused with 429
or 5xx (after its Retry-After), with one `logger` line per reconnect; any other refusal ends the stream with the API's
error. The account stream resumes from the last event; a resume the server cannot cover, or a 4008 (too far behind),
is told as a `Gap` (`next()`, which tells events only, tells it as an error with code `resume_gap` and goes on). A
subscribe its server refuses (`account_not_found`, `too_many_accounts`) ends it with an `ErrorKind::Validation` error;
a snapshot carrying `error` is an account that could not be read now, not a flat one. `stream.ping()` and
`stream.unsubscribe(&[account])` answer the server's pong and unsubscribed; an account unsubscribed stays out after a
reconnect.

A market subscription is a channel, a market and (candles) an interval; its other params are settings the last
subscribe sets. `market.unsubscribe(channel, symbols, params)` names the markets and (candles) the interval: without
one, every interval of those markets. Made again after a reconnect, a subscription asks its history again (a trade's
id, a candle's time tell repeats apart). `market.list()` and `market.ping()` answer the server's list of your
subscriptions and its pong; `market.limits()` are the limits its hello announced.

## Helpers

- `MarketRules::new(&client.markets().get("BTC-USD").await?)`: `round_price`, `round_size`, `size_for_notional`,
  `size_for_risk(risk_usd, stop_distance)`, `worst_price(side, price, slippage_pct)`, in exact decimals, from a
  `Decimal` or its string. Each answers the `Decimal` an order takes, at the step's decimals
  (`size: Some(rules.size_for_notional(usd, bid)?)`). A size for a notional or a risk is answered as it rounds, even
  below `min_size`: check it before you order. A string that is no decimal, a side other than `buy` or `sell` (any case) or a slippage outside the
  market's bounds is an `ErrorKind::Validation` error.
- `verify_webhook(raw_body, headers, &[secret], None, None)`: the `WebhookPayload` (the event, with `api_version`,
  `environment` and `test`; `webhook.ping` too), or an `ErrorKind::WebhookVerification` error (`missing_headers`,
  `stale`, `bad_signature`, `bad_body`). Pass the body exactly as received.

## Examples

`examples/01-accounts.rs` to `12-error-handling.rs`, the same twelve programs as in every SDK, each one
in every language in the docs' cookbook (https://docs.phoenixperpsfunding.com/cookbook). From the SDK's source: `cargo run --example 01-accounts`.
