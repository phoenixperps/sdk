"""09: an account's history: the equity curve by day, the result of each trading day, closed positions and fills.

    python examples/09_history.py
"""
from phoenixperps import PhoenixPerps

client = PhoenixPerps()
account = client.accounts.list().data[0]

equity = client.history.equity(account["id"], {"resolution": "day"})
print(f'equity: {len(equity["points"])} points', equity["points"][-3:])

for d in client.history.daily(account["id"]).data:
    print(f'{d["date"]}: {d["trades"]} trades, P&L {d["pnl"]}, fees {d["fees"]}')
for p in client.positions.list_closed(account["id"], {"limit": 10}).data:
    print(f'closed {p["market"]} {p["side"]} {p["size"]}: {p["entry_price"]} -> {p["exit_price"]}, P&L {p["pnl"]}')

n = 0
for f in client.fills.list(account["id"]).auto_paging_iter(max_items=100):  # every page, at most 100
    n += 1
    if n <= 5:
        print(f'fill {f["market"]} {f["side"]} {f["size"]} @ {f["price"]}')
print(f"{n} fills read")
