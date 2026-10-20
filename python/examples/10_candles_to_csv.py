"""10: the last 100 closed one-minute candles of a market and the one forming (final false), written to candles.csv.

    python examples/10_candles_to_csv.py [BTC-USD]
"""
import asyncio
import csv
import sys
from datetime import datetime, timezone
from typing import Any, Dict

from phoenixperps import PhoenixPerps

symbol = (sys.argv[1] if len(sys.argv) > 1 else "BTC-USD").upper()


async def main() -> None:
    client = PhoenixPerps()
    candles: Dict[int, Dict[str, Any]] = {}
    async with client.market_stream() as market:
        await market.subscribe("candles", [symbol], interval="1m", history=100)
        async for frame in market:
            if frame["type"] == "candles":
                for c in frame["data"]:
                    candles[c["time"]] = c
            elif frame["type"] == "snapshot_end":
                break
    with open("candles.csv", "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["time", "open", "high", "low", "close", "volume", "final"])
        for t in sorted(candles):
            c = candles[t]
            w.writerow([datetime.fromtimestamp(t / 1000, timezone.utc).isoformat(), c["open"], c["high"], c["low"], c["close"], c["volume"], c["final"]])
    print(f"{len(candles)} candles of {symbol} in candles.csv")


asyncio.run(main())
