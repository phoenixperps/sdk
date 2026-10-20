# Starter bots

One trading bot, the same in seven languages: an EMA cross on one-minute candles, each entry sized so the stop loses
`PP_RISK_USD`, previewed first, and placed with its stop loss on the API's servers. It is a starting point to read and
change, not a strategy to trust: run it on the sandbox.

Each folder is a standalone project that installs the published SDK. `conformance/starter.js` runs every one of them
against the same scripted market and holds them to the same requests, in the same order, with the same bodies.

## Settings (environment variables, or a `.env` file beside it)

| Name | Default | What it is |
|---|---|---|
| `PHOENIXPERPS_API_KEY` | | Your key. A sandbox key (`ppk_test_`) trades the sandbox |
| `PHOENIXPERPS_ALLOW_LIVE` | | `yes` to let it trade with a live key; without it a live key is refused |
| `PP_ACCOUNT` | the first active account | The account it trades |
| `PP_MARKET` | `BTC-USD` | The market |
| `PP_RISK_USD` | `50` | What one trade loses if its stop is hit, in USD |
| `PP_FAST`, `PP_SLOW` | `9`, `21` | The two EMAs, in candles |
| `PP_ATR`, `PP_ATR_MULT` | `14`, `2` | The stop: this many ATRs from the entry |
| `PP_TRAIL_PERCENT` | | The stop trails the best price by this percent |
| `PP_BREAK_EVEN_PERCENT` | | The stop moves to the entry once the price has gone this percent your way |
| `PP_MAX_TRADES_PER_DAY` | `10` | The local brake: entries per UTC day |
| `PP_RUN_CANDLES` | `0` | Stop after this many live candles (0: never) |
| `PP_STREAM_URL` | the SDK's | The market data stream (a local test server) |

## What it does, exactly

1. `GET /v1/accounts`: the account (`PP_ACCOUNT`, else the first whose status is `active`). `GET /v1/markets/{market}`:
   its rules.
2. The market stream: `candles`, interval `1m`, history `max(3 x PP_SLOW, 50)`. Only final candles count, each time
   once, in time order. The candles before the subscription's `snapshot_end` warm the indicators up; the bot trades on
   the candles after it.
3. The indicators, in IEEE doubles, in this order:
   - EMA(n): its first value is the mean of the first n closes; then `ema = (close - ema) * (2 / (n + 1)) + ema`.
   - ATR(n), Wilder's: `tr = max(high - low, |high - prev close|, |low - prev close|)` (the first candle: `high - low`);
     the first ATR is the mean of the first n TRs; then `atr = (atr * (n - 1) + tr) / n`.
   - A cross needs the previous candle's EMAs too: up when `prev fast <= prev slow` and `fast > slow`; down when
     `prev fast >= prev slow` and `fast < slow`.
4. On each live candle, once the indicators are ready: `GET /v1/accounts/{account}/positions?market={market}`, then
   the first that applies:
   - a position without its protection (`protected` false): `POST .../positions/{market}/close`, nothing else;
   - no cross: nothing;
   - a position on the other side of the cross: `POST .../positions/{market}/close`;
   - a position on the same side: nothing;
   - `PP_MAX_TRADES_PER_DAY` entries already today: nothing;
   - else an entry. The stop distance is `ticks x tick_size` with `ticks = floor(atr x PP_ATR_MULT / tick_size + 0.5)`
     (none below 1 tick); the size is the market rules' `sizeForRisk(PP_RISK_USD, distance)` (none below `min_size`).
     `POST .../orders/test` with `{market, side, type: market, size}`; when it `would_reject`, nothing. Else
     `POST .../orders` with `{market, side, type: market, size, client_order_id: "starter-<candle time>", exits:
     {stop_loss: {distance, trail: {percent}?, break_even: {trigger_percent, offset_ticks: 1}?}}}` and the
     Idempotency-Key `starter-<market>-<candle time>` (a restart on the same candle never places it twice).
5. A live key needs `PHOENIXPERPS_ALLOW_LIVE=yes`, and then the bot says LIVE in every line it prints.
