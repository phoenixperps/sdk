"""The starter bot: an EMA cross on one-minute candles, each entry sized so its stop loses PP_RISK_USD, previewed first,
placed with its stop loss on the API's servers. What it does, exactly: ../README.md. Run it on the sandbox.

    pip install -r requirements.txt && python bot.py
"""
import asyncio
import math
import os
import re
import sys
from datetime import datetime, timezone
from decimal import Decimal
from typing import Any, Dict, List, Optional

from phoenixperps import AsyncPhoenixPerps, MarketRules

# .env beside it: only what the environment does not set already
if os.path.exists(".env"):
    for line in open(".env", encoding="utf-8").read().splitlines():
        m = re.match(r"^\s*([A-Z0-9_]+)\s*=\s*(.*?)\s*$", line)
        if m and not line.lstrip().startswith("#") and m.group(1) not in os.environ:
            os.environ[m.group(1)] = re.sub(r"^(['\"])(.*)\1$", r"\2", m.group(2))


def env(name: str, fallback: str = "") -> str:
    return os.environ.get(name) or fallback


MARKET = env("PP_MARKET", "BTC-USD").upper()
RISK_USD = env("PP_RISK_USD", "50")
FAST, SLOW = int(env("PP_FAST", "9")), int(env("PP_SLOW", "21"))
ATR_N, ATR_MULT = int(env("PP_ATR", "14")), float(env("PP_ATR_MULT", "2"))
TRAIL_PERCENT, BREAK_EVEN_PERCENT = env("PP_TRAIL_PERCENT"), env("PP_BREAK_EVEN_PERCENT")
MAX_TRADES_PER_DAY = int(env("PP_MAX_TRADES_PER_DAY", "10"))
RUN_CANDLES = int(env("PP_RUN_CANDLES", "0"))


class Ema:
    """EMA(n): the mean of the first n values, then (x - ema) * 2 / (n + 1) + ema."""

    def __init__(self, n: int) -> None:
        self.n = n
        self.value: Optional[float] = None
        self.seed: List[float] = []

    def add(self, x: float) -> None:
        if self.value is None:
            self.seed.append(x)
            if len(self.seed) == self.n:
                self.value = sum(self.seed) / self.n
        else:
            self.value = (x - self.value) * (2 / (self.n + 1)) + self.value


class Atr:
    """Wilder's ATR(n): the mean of the first n true ranges, then (atr * (n - 1) + tr) / n."""

    def __init__(self, n: int) -> None:
        self.n = n
        self.value: Optional[float] = None
        self.seed: List[float] = []
        self.prev_close: Optional[float] = None

    def add(self, c: Dict[str, Any]) -> None:
        h, low, close = float(c["high"]), float(c["low"]), float(c["close"])
        if self.prev_close is None:
            tr = h - low
        else:
            tr = max(h - low, abs(h - self.prev_close), abs(low - self.prev_close))
        self.prev_close = close
        if self.value is None:
            self.seed.append(tr)
            if len(self.seed) == self.n:
                self.value = sum(self.seed) / self.n
        else:
            self.value = (self.value * (self.n - 1) + tr) / self.n


async def main() -> int:
    stream_url = env("PP_STREAM_URL") or None
    async with AsyncPhoenixPerps(market_stream_url=stream_url) as client:
        live = client.environment == "live"
        if live and env("PHOENIXPERPS_ALLOW_LIVE") != "yes":
            print("This is a live key: the starter trades real money only with PHOENIXPERPS_ALLOW_LIVE=yes. Use a sandbox key to try it.", file=sys.stderr)
            return 1

        def log(msg: str) -> None:
            print(f'{datetime.now(timezone.utc).isoformat(timespec="milliseconds")} {"LIVE " if live else ""}{msg}', flush=True)

        # the account and the market's rules
        accounts = (await client.accounts.list()).data
        wanted = env("PP_ACCOUNT").upper()
        account = next((a for a in accounts if (a["id"] == wanted if wanted else a["status"] == "active")), None)
        if account is None:
            raise SystemExit(f"No account {wanted} for this key." if wanted else "No active account for this key.")
        market = await client.markets.get(MARKET)
        rules = MarketRules(market)
        tick = float(market["tick_size"])
        log(f'{account["id"]} on {MARKET}: EMA {FAST}/{SLOW}, stop {env("PP_ATR_MULT", "2")} x ATR({ATR_N}), {RISK_USD} USD a trade')

        fast, slow, atr = Ema(FAST), Ema(SLOW), Atr(ATR_N)
        trades = {"day": "", "n": 0}

        async def on_live(c: Dict[str, Any], prev_fast: Optional[float], prev_slow: Optional[float]) -> None:
            """One live candle: the position first, then at most one order (../README.md step 4)."""
            if None in (fast.value, slow.value, atr.value, prev_fast, prev_slow):
                return
            assert fast.value is not None and slow.value is not None and atr.value is not None
            assert prev_fast is not None and prev_slow is not None
            up = prev_fast <= prev_slow and fast.value > slow.value
            down = prev_fast >= prev_slow and fast.value < slow.value
            positions = (await client.positions.list(account["id"], {"market": MARKET})).data
            position = next((p for p in positions if Decimal(p["size"]) != 0), None)
            if position and not position["protected"]:
                log(f'the {position["side"]} position has no stop: closing it')
                await client.positions.close(account["id"], MARKET, {})
                return
            if not up and not down:
                return
            side, cross = ("buy", "up") if up else ("sell", "down")
            if position:
                if (position["side"] == "long") != up:
                    log(f'cross {cross}: closing the {position["side"]} position')
                    await client.positions.close(account["id"], MARKET, {})
                return
            day = datetime.fromtimestamp(c["time"] / 1000, timezone.utc).strftime("%Y-%m-%d")
            if trades["day"] != day:
                trades["day"], trades["n"] = day, 0
            if trades["n"] >= MAX_TRADES_PER_DAY:
                log(f'cross {cross}: {trades["n"]} entries today already (PP_MAX_TRADES_PER_DAY)')
                return
            ticks = math.floor(atr.value * ATR_MULT / tick + 0.5)
            if ticks < 1:
                return
            distance = rules.round_price(ticks * tick)
            size = rules.size_for_risk(RISK_USD, distance)
            if Decimal(size) < Decimal(market["min_size"]) or Decimal(size) == 0:
                log(f"cross {cross}: {RISK_USD} USD over a {distance} stop is below the market's smallest size")
                return
            preview = await client.orders.preview(account["id"], {"market": MARKET, "side": side, "type": "market", "size": size})
            if preview["result"] == "would_reject":
                log(f'cross {cross}: the API would refuse {side} {size} ({(preview.get("reject") or {}).get("code")})')
                return
            stop_loss: Dict[str, Any] = {"distance": distance}
            if TRAIL_PERCENT:
                stop_loss["trail"] = {"percent": TRAIL_PERCENT}
            if BREAK_EVEN_PERCENT:
                stop_loss["break_even"] = {"trigger_percent": BREAK_EVEN_PERCENT, "offset_ticks": 1}
            order = await client.orders.create(account["id"], {
                "market": MARKET, "side": side, "type": "market", "size": size,
                "client_order_id": f'starter-{c["time"]}', "exits": {"stop_loss": stop_loss},
            }, idempotency_key=f'starter-{MARKET}-{c["time"]}')
            trades["n"] += 1
            log(f'cross {cross}: {side} {size} {MARKET} ({order["status"]}), stop {distance} away')

        # candles in, one at a time: history warms the indicators up, live ones trade
        last, warm, live_candles = 0, False, 0
        async with client.market_stream() as stream:
            await stream.subscribe("candles", [MARKET], interval="1m", history=max(3 * SLOW, 50))
            async for frame in stream:
                if frame["type"] == "snapshot_end":
                    if not warm:
                        log("warmed up: trading on the next candles")
                    warm = True
                    continue
                if frame["type"] != "candles":
                    continue
                for c in frame["data"]:
                    if not c["final"] or c["time"] <= last:
                        continue
                    last = c["time"]
                    prev_fast, prev_slow = fast.value, slow.value
                    fast.add(float(c["close"]))
                    slow.add(float(c["close"]))
                    atr.add(c)
                    if not warm:
                        continue
                    await on_live(c, prev_fast, prev_slow)
                    live_candles += 1
                    if RUN_CANDLES and live_candles >= RUN_CANDLES:
                        return 0
    return 0


if __name__ == "__main__":
    sys.exit(asyncio.run(main()))
