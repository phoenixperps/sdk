# Phoenix Perpetuals SDKs

The Phoenix Perpetuals API in your language: REST, the account stream and market data, with retries, idempotency keys,
paging and typed errors. One spec, one behaviour: every SDK passes the same conformance suite.

| Language | Package | Folder |
|---|---|---|
| TypeScript / JavaScript | `@phoenixperps/sdk` | [typescript](typescript) |
| Python | `phoenixperps` | [python](python) |
| Go | `github.com/phoenixperps/sdk/go` | [go](go) |
| C# / .NET | `PhoenixPerps.Sdk` | [csharp](csharp) |
| Java | `com.phoenixperpsfunding:phoenixperps-sdk` | [java](java) |
| Rust | `phoenixperps` | [rust](rust) |
| PHP | `phoenixperps/sdk` | [php](php) |

Postman: [postman](postman) (a collection and the live and sandbox environments).

## Examples

The same twelve programs in every language, in each SDK's `examples/` folder, each one file run with one command. They
read the key from `PHOENIXPERPS_API_KEY`; the ones that trade stay on the sandbox unless `PHOENIXPERPS_ALLOW_LIVE=yes`.

| | What it shows |
|---|---|
| 01 accounts | The key's access and limits (`/v1/me`), your accounts |
| 02 markets | The markets and their rules, a quote from the market stream |
| 03 market-stream | Prices and trades live, subscriptions kept across reconnects |
| 04 place-cancel-limit | A limit order with your own id and an Idempotency-Key, found again, canceled |
| 05 market-with-brackets | A market order with its take profit and stop loss, kept on the API's servers |
| 06 trailing-breakeven | A stop loss that trails and moves to break-even by itself |
| 07 what-if | What an order would do now, without placing it |
| 08 account-stream-mirror | A live copy of positions and working orders: the snapshot, then every event once |
| 09 history | Equity by day, daily results, closed positions, fills over every page |
| 10 candles-to-csv | The last 100 one-minute candles into a CSV file |
| 11 webhook-receiver | Webhooks received with their signature and time checked |
| 12 error-handling | Error families, codes and request ids; repeating a write safely |

| Language | Run one |
|---|---|
| TypeScript | `node examples/01-accounts.mts` |
| Python | `python examples/01_accounts.py` |
| Go | `go run ./examples/01-accounts` |
| C# | `dotnet run examples/01-accounts.cs` (.NET 10) |
| Java | `gradle -q example -Pn=01` |
| Rust | `cargo run --example 01-accounts` |
| PHP | `php examples/01-accounts.php` |

## Starter bots

[starters](starters): one bot in seven languages, each a standalone project on the published SDK. An EMA cross on
one-minute candles, sized so the stop loses a fixed amount, previewed first, placed with its stop loss on the API's
servers, sandbox unless told otherwise. `conformance/starter.js` holds all seven to the same requests on the same
scripted market.

## How it is built

- `spec/openapi.json` is the API's own spec, pinned in `spec/SPEC_SOURCE` (`node scripts/sync-spec.js` updates it).
- `node generator` writes each SDK's generated surface (resources, types, the Postman collection) from it;
  `node generator/check.js` fails when what is committed differs (the drift gate).
- `conformance/` holds the scenarios every SDK runs against the same mock server (`docs/BEHAVIOUR.md` says why).

## Tests

```
cd typescript && npm install && npm test          # conformance + types (npm run typecheck)
cd python && pip install -e .[test] && pytest     # sync and async
cd go && go test ./...                            # conformance, streams, helpers
cd csharp && dotnet test tests/PhoenixPerps.Sdk.Tests
cd java && gradle test
cd rust && cargo test
cd php && composer install && vendor/bin/phpunit
```
