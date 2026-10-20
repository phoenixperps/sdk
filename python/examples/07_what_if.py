"""07: what an order would do now, without placing it: fill or rest, the price, the fee, the margin, the position
after. Nothing is sent to the market.

    python examples/07_what_if.py
"""
from phoenixperps import PhoenixPerps

client = PhoenixPerps()
account = client.accounts.list().data[0]

w = client.orders.preview(account["id"], {"market": "BTC-USD", "side": "buy", "type": "market", "notional_usd": "1000"})
execution = w.get("execution") or {}
margin = w.get("margin") or {}
after = w.get("position_after") or {}
print(f'result:         {w["result"]}{" (" + str(w["reject"]) + ")" if w.get("reject") else ""}')
print(f'size:           {w.get("size") or "-"}')
print(f'price:          {execution.get("expected_price", "-")} (worst {execution.get("worst_price") or "-"})')
print(f'notional, fee:  {w.get("notional") or "-"}, {w.get("fee") or "-"}')
print(f'margin:         {margin.get("required", "-")} needed, {margin.get("available_after") or "-"} left after')
print(f'position after: {after.get("side") or "flat"} {after.get("size", "")}')
