"""06: a position whose stop loss trails the price by 0.5% and moves to break-even once the price has gone 0.5% your
way, all on the API's servers. Follows the account stream for 30 s to watch the stop move, then closes.

    python examples/06_trailing_breakeven.py
"""
import asyncio
import json
import os

from phoenixperps import PhoenixPerps

client = PhoenixPerps()
if client.environment == "live" and os.environ.get("PHOENIXPERPS_ALLOW_LIVE") != "yes":
    raise SystemExit("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).")
account = client.accounts.list().data[0]
btc = client.markets.get("BTC-USD")

async def main() -> None:
    # the stream first, so every event of the stop is told, from its placing on
    async with client.account_stream(accounts=[account["id"]]) as stream:
        await stream.ready.wait()
        order = client.orders.create(account["id"], {
            "market": "BTC-USD",
            "side": "buy",
            "type": "market",
            "size": btc["min_size"],
            "exits": {
                "stop_loss": {
                    "percent": "1",
                    "trail": {"percent": "0.5"},
                    "break_even": {"trigger_percent": "0.5", "offset_ticks": 2},
                },
            },
        })
        print(f'bought {order["size"]} BTC at {order.get("average_fill_price") or "-"}; watching the stop for 30 s')

        async def watch() -> None:
            async for event in stream:
                if event["type"].startswith("strategy."):
                    print(f'{event["time"]} {event["type"]} {json.dumps(event["data"])}')

        try:
            await asyncio.wait_for(watch(), timeout=30)
        except asyncio.TimeoutError:
            pass


asyncio.run(main())

close = client.positions.close(account["id"], "BTC-USD", {})
print(f'closed: {close["status"]}')
