"""02: the markets and their rules, then a quote (bid, ask, last) from the market stream.

    python examples/02_markets.py [BTC-USD]
"""
import asyncio
import sys

from phoenixperps import PhoenixPerps

client = PhoenixPerps()
symbol = (sys.argv[1] if len(sys.argv) > 1 else "BTC-USD").upper()

for m in client.markets.list().data:
    print(f'{m["market"]:<10} tick {m["tick_size"]}  step {m["size_step"]}  min {m["min_size"]}  up to {m["max_leverage"]}x  {m["status"]}')


async def quote() -> None:
    async with client.market_stream() as market:
        reply = await market.subscribe("ticker", [symbol], interval_ms=250)
        if reply["rejected"]:
            raise SystemExit(f'{symbol}: {reply["rejected"][0]["code"]}')
        async for frame in market:
            if frame["type"] == "ticker":
                q = frame["data"]
                print(f'{symbol}: bid {q["bid"]}  ask {q["ask"]}  last {q["last"]}')
                return


asyncio.run(quote())
