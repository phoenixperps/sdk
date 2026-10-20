# Changelog

One version for every SDK (`VERSION`). Inside v1 the API only adds; a release says what each SDK gained or fixed.

## 1.0.0

The first release, for the API's v1.

- TypeScript, Python, Go, C#, Java, Rust and PHP: every operation of the API, the account stream with resume, the
  market data stream, retries only when safe, an Idempotency-Key per write, paging, typed errors.
- `MarketRules` (exact rounding to the tick and the step, sizes for a notional or a risk, worst prices) and
  `Webhooks.verify`.
- Twelve examples and a starter bot in every language.
