# Starter bot (Go)

An EMA cross on one-minute candles, each entry sized so its stop loses `PP_RISK_USD`, previewed first and placed with
its stop loss on the API's servers. What it does, exactly, and every setting: [../README.md](../README.md).

1. Go 1.23 or later.
2. `go mod tidy`
3. Copy `.env.example` to `.env` and put a sandbox key in it (dashboard, Developers, Create API key, Sandbox).
4. `go run .`
5. Read `main.go`, change it, run it again. Stop it with Ctrl+C: its stops stay on the API's servers.

It refuses a live key unless `PHOENIXPERPS_ALLOW_LIVE=yes`, and then prints LIVE on every line. Bots follow the
platform's rules like any trader: https://docs.phoenixperpsfunding.com/capabilities
