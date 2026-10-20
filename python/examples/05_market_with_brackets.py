"""05: a market order with its exits: a take profit 1% away and a stop loss 0.5% away, placed by the API the moment it
fills and kept on its servers. The position is closed at the end.

    python examples/05_market_with_brackets.py
"""
import json
import os

from phoenixperps import PhoenixPerps

client = PhoenixPerps()
if client.environment == "live" and os.environ.get("PHOENIXPERPS_ALLOW_LIVE") != "yes":
    raise SystemExit("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).")
account = client.accounts.list().data[0]
btc = client.markets.get("BTC-USD")

order = client.orders.create(account["id"], {
    "market": "BTC-USD",
    "side": "buy",
    "type": "market",
    "size": btc["min_size"],
    "exits": {
        "take_profit": [{"percent": "1"}],
        "stop_loss": {"percent": "0.5"},
    },
})
print(f'market order {order["id"]}: {order["status"]} at {order.get("average_fill_price") or "-"}')

exits = client.exits.get(account["id"], "BTC-USD")
for tp in exits["take_profit"]:
    print(f"take profit: {json.dumps(tp)}")
for sl in exits["stop_loss"]:
    print(f"stop loss:   {json.dumps(sl)}")

close = client.positions.close(account["id"], "BTC-USD", {})
print(f'closed: {close["id"]} {close["status"]} (the exits went with the position)')
