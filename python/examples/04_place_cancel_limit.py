"""04: a limit order far from the market, found again by your own id, then canceled. The Idempotency-Key makes a
retried request return the first order instead of placing a second one.

    python examples/04_place_cancel_limit.py
"""
import asyncio
import os
import time
import uuid
from decimal import Decimal

from phoenixperps import MarketRules, PhoenixPerps

client = PhoenixPerps()
if client.environment == "live" and os.environ.get("PHOENIXPERPS_ALLOW_LIVE") != "yes":
    raise SystemExit("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).")
account = client.accounts.list().data[0]
rules = MarketRules(client.markets.get("BTC-USD"))


async def best_bid() -> str:
    async with client.market_stream() as market:
        await market.subscribe("ticker", ["BTC-USD"])
        async for frame in market:
            if frame["type"] == "ticker":
                return frame["data"]["bid"]
    raise RuntimeError("the market stream ended")


bid = asyncio.run(best_bid())
client_order_id = f"example-04-{int(time.time() * 1000)}"
order = client.orders.create(account["id"], {
    "market": "BTC-USD",
    "side": "buy",
    "type": "limit",
    "size": rules.size_for_notional("100", bid),  # about 100 USD
    "limit_price": rules.round_price(Decimal(bid) * Decimal("0.9"), "down"),  # 10% below: it rests
    "client_order_id": client_order_id,
}, idempotency_key=str(uuid.uuid4()))
print(f'placed {order["id"]}: {order["status"]}, {order["size"]} at {order["limit_price"]}')

found = client.orders.list(account["id"], {"client_order_id": client_order_id}).data[0]
print(f'found by client_order_id: {found["id"]}')

canceled = client.orders.cancel(account["id"], order["id"])
print(f'canceled: {canceled["status"]} ({canceled["cancel_reason"]})')
