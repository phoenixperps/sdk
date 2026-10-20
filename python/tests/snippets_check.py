# Generated from the API spec by generator/printers/snippets.js. Do not edit: run `node generator`.
# Every docs snippet, compiled with the suite; never run.
from phoenixperps import PhoenixPerps


def get_api(client: PhoenixPerps) -> None:
    platform = client.platform.info()
    print(platform)


def get_spec(client: PhoenixPerps) -> None:
    result = client.platform.spec()
    print(result)


def get_status(client: PhoenixPerps) -> None:
    status = client.platform.status()
    print(status)


def get_limits(client: PhoenixPerps) -> None:
    limits = client.platform.limits()
    print(limits)


def get_me(client: PhoenixPerps) -> None:
    me = client.me.get()
    print(me)


def list_markets(client: PhoenixPerps) -> None:
    for market in client.markets.list().data:
        print(market["market"])


def list_market_stats(client: PhoenixPerps) -> None:
    for market_stats in client.markets.stats().data:
        print(market_stats["market"])


def get_market(client: PhoenixPerps) -> None:
    market = client.markets.get("BTC-USD")
    print(market)


def list_accounts(client: PhoenixPerps) -> None:
    for account in client.accounts.list().data:
        print(account["id"])


def create_sandbox_account(client: PhoenixPerps) -> None:
    account = client.accounts.create_sandbox()
    print(account)


def get_account(client: PhoenixPerps) -> None:
    account_detail = client.accounts.get("SANDBOX-00042-001")
    print(account_detail)


def get_risk(client: PhoenixPerps) -> None:
    account_risk = client.accounts.risk("SANDBOX-00042-001")
    print(account_risk)


def get_trading_policy(client: PhoenixPerps) -> None:
    trading_policy = client.accounts.trading_policy("SANDBOX-00042-001")
    print(trading_policy)


def get_progress(client: PhoenixPerps) -> None:
    progress = client.accounts.progress("SANDBOX-00042-001")
    print(progress)


def get_equity_history(client: PhoenixPerps) -> None:
    equity_history = client.history.equity("SANDBOX-00042-001")
    print(equity_history)


def list_daily_results(client: PhoenixPerps) -> None:
    for daily_result in client.history.daily("SANDBOX-00042-001").data:
        print(daily_result)


def list_margin_settings(client: PhoenixPerps) -> None:
    for margin_setting in client.margin.list("SANDBOX-00042-001").data:
        print(margin_setting["market"])


def list_positions(client: PhoenixPerps) -> None:
    for position in client.positions.list("SANDBOX-00042-001").data:
        print(position["id"])


def get_position(client: PhoenixPerps) -> None:
    position = client.positions.get("SANDBOX-00042-001", "BTC-USD")
    print(position)


def list_closed_positions(client: PhoenixPerps) -> None:
    for closed_position in client.positions.list_closed("SANDBOX-00042-001").auto_paging_iter():
        print(closed_position["market"])


def list_orders(client: PhoenixPerps) -> None:
    for order in client.orders.list("SANDBOX-00042-001").auto_paging_iter():
        print(order["id"])


def place_order(client: PhoenixPerps) -> None:
    order = client.orders.create("SANDBOX-00042-001", {
        "market": "BTC-USD",
        "side": "buy",
        "type": "market",
        "size": "0.01",
        "max_slippage_pct": "0.5",
        "client_order_id": "momentum-0142",
        "exits": {
            "take_profit": [
                {
                    "distance": "600",
                    "size_pct": "100",
                },
            ],
            "stop_loss": {
                "distance": "300",
            },
        },
    })
    print(order)


def get_order(client: PhoenixPerps) -> None:
    order = client.orders.get("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(order)


def modify_order(client: PhoenixPerps) -> None:
    order = client.orders.modify("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W", {
        "limit_price": "61900",
    })
    print(order)


def cancel_order(client: PhoenixPerps) -> None:
    order = client.orders.cancel("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(order)


def test_order(client: PhoenixPerps) -> None:
    what_if = client.orders.preview("SANDBOX-00042-001", {
        "market": "BTC-USD",
        "side": "buy",
        "type": "market",
        "size": "0.01",
        "max_slippage_pct": "0.5",
    })
    print(what_if)


def cancel_all_orders(client: PhoenixPerps) -> None:
    cancel_all_result = client.orders.cancel_all("SANDBOX-00042-001", {
        "market": "BTC-USD",
    })
    print(cancel_all_result)


def close_position(client: PhoenixPerps) -> None:
    order = client.positions.close("SANDBOX-00042-001", "BTC-USD", {
        "size": "0.005",
    })
    print(order)


def flatten(client: PhoenixPerps) -> None:
    flatten_result = client.positions.flatten("SANDBOX-00042-001")
    print(flatten_result)


def set_margin(client: PhoenixPerps) -> None:
    margin_setting_result = client.margin.set("SANDBOX-00042-001", "BTC-USD", {
        "margin_mode": "isolated",
        "leverage": 5,
    })
    print(margin_setting_result)


def list_events(client: PhoenixPerps) -> None:
    for event in client.events.list().auto_paging_iter():
        print(event["id"])


def get_exits(client: PhoenixPerps) -> None:
    exits = client.exits.get("SANDBOX-00042-001", "BTC-USD")
    print(exits)


def set_exits(client: PhoenixPerps) -> None:
    exits = client.exits.set("SANDBOX-00042-001", "BTC-USD", {
        "expected_position": {
            "side": "long",
            "size": "0.01",
        },
        "expected_exits": [
            {
                "id": "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W",
                "size": "0.01",
                "price": "61700",
            },
        ],
        "operations": [
            {
                "op": "modify",
                "id": "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W",
                "price": "61900",
            },
            {
                "op": "place",
                "group": "take_profit",
                "type": "limit",
                "price": "63000",
                "size": "0.01",
            },
        ],
    })
    print(exits)


def set_order_exits(client: PhoenixPerps) -> None:
    order = client.orders.set_exits("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W", {
        "take_profit": [
            {
                "distance": "600",
                "size_pct": "50",
            },
            {
                "distance": "1200",
                "size_pct": "50",
            },
        ],
        "stop_loss": {
            "distance": "300",
            "type": "stop_market",
        },
    })
    print(order)


def create_strategy(client: PhoenixPerps) -> None:
    strategy = client.strategies.create("SANDBOX-00042-001", {
        "type": "twap",
        "market": "BTC-USD",
        "side": "buy",
        "size": "0.05",
        "duration_minutes": 30,
        "interval_seconds": 60,
        "client_strategy_id": "twap-07",
    })
    print(strategy)


def list_strategies(client: PhoenixPerps) -> None:
    for strategy in client.strategies.list("SANDBOX-00042-001").auto_paging_iter():
        print(strategy["id"])


def get_strategy(client: PhoenixPerps) -> None:
    strategy = client.strategies.get("SANDBOX-00042-001", "stg_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(strategy)


def cancel_strategy(client: PhoenixPerps) -> None:
    strategy = client.strategies.cancel("SANDBOX-00042-001", "stg_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(strategy)


def list_webhook_endpoints(client: PhoenixPerps) -> None:
    for webhook_endpoint in client.webhook_endpoints.list().data:
        print(webhook_endpoint["id"])


def get_webhook_endpoint(client: PhoenixPerps) -> None:
    webhook_endpoint = client.webhook_endpoints.get("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(webhook_endpoint)


def delete_webhook_endpoint(client: PhoenixPerps) -> None:
    deleted_endpoint = client.webhook_endpoints.delete("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(deleted_endpoint)


def test_webhook_endpoint(client: PhoenixPerps) -> None:
    webhook_delivery = client.webhook_endpoints.test("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(webhook_delivery)


def list_webhook_deliveries(client: PhoenixPerps) -> None:
    for webhook_delivery in client.webhook_endpoints.list_deliveries("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W").auto_paging_iter():
        print(webhook_delivery["id"])


def get_webhook_delivery(client: PhoenixPerps) -> None:
    webhook_delivery = client.webhook_endpoints.get_delivery("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", "dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(webhook_delivery)


def resend_webhook_delivery(client: PhoenixPerps) -> None:
    webhook_delivery = client.webhook_endpoints.resend_delivery("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", "dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(webhook_delivery)


def disable_webhook_endpoint(client: PhoenixPerps) -> None:
    webhook_endpoint = client.webhook_endpoints.disable("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(webhook_endpoint)


def enable_webhook_endpoint(client: PhoenixPerps) -> None:
    webhook_endpoint = client.webhook_endpoints.enable("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(webhook_endpoint)


def list_trading_view_hooks(client: PhoenixPerps) -> None:
    for trading_view_hook in client.tradingview.list_hooks().data:
        print(trading_view_hook["id"])


def get_trading_view_hook(client: PhoenixPerps) -> None:
    trading_view_hook = client.tradingview.get_hook("K7x2mQ9pLr4A")
    print(trading_view_hook)


def delete_trading_view_hook(client: PhoenixPerps) -> None:
    deleted_hook = client.tradingview.delete_hook("K7x2mQ9pLr4A")
    print(deleted_hook)


def list_trading_view_alerts(client: PhoenixPerps) -> None:
    for trading_view_alert in client.tradingview.list_alerts("K7x2mQ9pLr4A").auto_paging_iter():
        print(trading_view_alert["id"])


def get_trading_view_alert(client: PhoenixPerps) -> None:
    trading_view_alert = client.tradingview.get_alert("K7x2mQ9pLr4A", "tva_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(trading_view_alert)


def disable_trading_view_hook(client: PhoenixPerps) -> None:
    trading_view_hook = client.tradingview.disable_hook("K7x2mQ9pLr4A")
    print(trading_view_hook)


def enable_trading_view_hook(client: PhoenixPerps) -> None:
    trading_view_hook = client.tradingview.enable_hook("K7x2mQ9pLr4A")
    print(trading_view_hook)


def parse_trading_view_message(client: PhoenixPerps) -> None:
    trading_view_parse = client.tradingview.parse({
        "message": "buy BTCUSD qty 0.01 tp 1% sl 61500",
    })
    print(trading_view_parse)


def list_webhook_event_types(client: PhoenixPerps) -> None:
    for webhook_event_type in client.webhook_endpoints.event_types().data:
        print(webhook_event_type)


def get_event(client: PhoenixPerps) -> None:
    event = client.events.get("evt_01JAB7Q8XK4M2N5P7R9S3T5V7W")
    print(event)


def list_fills(client: PhoenixPerps) -> None:
    for fill in client.fills.list("SANDBOX-00042-001").auto_paging_iter():
        print(fill["id"])
