"""01: who you are (the key, its access and limits) and your accounts.

    PHOENIXPERPS_API_KEY=ppk_test_... python examples/01_accounts.py
"""
from phoenixperps import PhoenixPerps

client = PhoenixPerps()  # the key: PHOENIXPERPS_API_KEY (a sandbox key: the sandbox)

me = client.me.get()
c = me["credential"]
print(f'{c["kind"]} "{c["name"]}" on {c["environment"]}: {c["access"]} access')
print(f'limits: {me["budgets"]["read_per_min"]} reads and {me["budgets"]["trade_per_min"]} trading calls a minute')

for account in client.accounts.list().data:
    print(f'{account["id"]}  {account["stage"]}  {account["status"]}  balance {account.get("balance") or "-"}')
