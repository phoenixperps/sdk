# Changelog

One version for every SDK (`VERSION`). Inside v1 the API only adds; a release says what each SDK gained or fixed.

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
