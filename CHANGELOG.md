# Changelog

One version for every SDK (`VERSION`). Inside v1 the API only adds; a release says what each SDK gained or fixed.

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
