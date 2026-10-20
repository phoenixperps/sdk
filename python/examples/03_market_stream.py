"""03: live market data: prices for two markets and the trades of one, until 20 frames came.

    python examples/03_market_stream.py
"""
import asyncio

from phoenixperps import PhoenixPerps


async def main() -> None:
    client = PhoenixPerps()
    async with client.market_stream() as market:
        await market.subscribe("ticker", ["BTC-USD", "ETH-USD"], interval_ms=500)
        await market.subscribe("trades", ["BTC-USD"], history=5)
        n = 0
        async for frame in market:  # subscriptions are made again after a reconnect
            if frame["type"] == "ticker":
                print(f'ticker {frame["symbol"]}: bid {frame["data"]["bid"]} ask {frame["data"]["ask"]}')
            elif frame["type"] == "trades":
                for t in frame["data"]:
                    print(f'trade  {frame["symbol"]}: {t["side"]} {t["size"]} @ {t["price"]}')
            else:
                continue
            n += 1
            if n >= 20:
                return


asyncio.run(main())
