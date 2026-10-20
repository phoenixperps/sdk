// Generated from the API spec by generator/printers/snippets.js. Do not edit: run `node generator`.
// Every docs snippet, compiled with the tests; never run.
#![allow(dead_code, unused_imports)]

use futures_util::StreamExt;
use phoenixperps::*;

pub async fn get_api(client: &Client) -> Result<()> {
    let platform = client.platform().info().await?;
    println!("{platform:?}");
    Ok(())
}

pub async fn get_spec(client: &Client) -> Result<()> {
    let result = client.platform().spec().await?;
    println!("{result:?}");
    Ok(())
}

pub async fn get_status(client: &Client) -> Result<()> {
    let status = client.platform().status().await?;
    println!("{status:?}");
    Ok(())
}

pub async fn get_limits(client: &Client) -> Result<()> {
    let limits = client.platform().limits().await?;
    println!("{limits:?}");
    Ok(())
}

pub async fn get_me(client: &Client) -> Result<()> {
    let me = client.me().get().await?;
    println!("{me:?}");
    Ok(())
}

pub async fn list_markets(client: &Client) -> Result<()> {
    let page = client.markets().list().await?;
    for market in &page.data {
        println!("{}", market.market);
    }
    Ok(())
}

pub async fn list_market_stats(client: &Client) -> Result<()> {
    let page = client.markets().stats().await?;
    for market_stats in &page.data {
        println!("{}", market_stats.market);
    }
    Ok(())
}

pub async fn get_market(client: &Client) -> Result<()> {
    let market = client.markets().get("BTC-USD").await?;
    println!("{market:?}");
    Ok(())
}

pub async fn list_accounts(client: &Client) -> Result<()> {
    let page = client.accounts().list().await?;
    for account in &page.data {
        println!("{}", account.id);
    }
    Ok(())
}

pub async fn create_sandbox_account(client: &Client) -> Result<()> {
    let account = client.accounts().create_sandbox().await?;
    println!("{account:?}");
    Ok(())
}

pub async fn get_account(client: &Client) -> Result<()> {
    let account_detail = client.accounts().get("SANDBOX-00042-001").await?;
    println!("{account_detail:?}");
    Ok(())
}

pub async fn get_risk(client: &Client) -> Result<()> {
    let account_risk = client.accounts().risk("SANDBOX-00042-001").await?;
    println!("{account_risk:?}");
    Ok(())
}

pub async fn get_trading_policy(client: &Client) -> Result<()> {
    let trading_policy = client.accounts().trading_policy("SANDBOX-00042-001").await?;
    println!("{trading_policy:?}");
    Ok(())
}

pub async fn get_progress(client: &Client) -> Result<()> {
    let progress = client.accounts().progress("SANDBOX-00042-001").await?;
    println!("{progress:?}");
    Ok(())
}

pub async fn get_equity_history(client: &Client) -> Result<()> {
    let equity_history = client.history().equity("SANDBOX-00042-001", None).await?;
    println!("{equity_history:?}");
    Ok(())
}

pub async fn list_daily_results(client: &Client) -> Result<()> {
    let page = client.history().daily("SANDBOX-00042-001").await?;
    for daily_result in &page.data {
        println!("{daily_result:?}");
    }
    Ok(())
}

pub async fn list_margin_settings(client: &Client) -> Result<()> {
    let page = client.margin().list("SANDBOX-00042-001").await?;
    for margin_setting in &page.data {
        println!("{}", margin_setting.market);
    }
    Ok(())
}

pub async fn list_positions(client: &Client) -> Result<()> {
    let page = client.positions().list("SANDBOX-00042-001", None).await?;
    for position in &page.data {
        println!("{}", position.id);
    }
    Ok(())
}

pub async fn get_position(client: &Client) -> Result<()> {
    let position = client.positions().get("SANDBOX-00042-001", "BTC-USD").await?;
    println!("{position:?}");
    Ok(())
}

pub async fn list_closed_positions(client: &Client) -> Result<()> {
    let options = RequestOptions::default();
    let mut items = client.positions().list_closed_auto_paging("SANDBOX-00042-001", None, &options);
    while let Some(item) = items.next().await {
        let closed_position = item?;
        println!("{}", closed_position.market);
    }
    Ok(())
}

pub async fn list_orders(client: &Client) -> Result<()> {
    let options = RequestOptions::default();
    let mut items = client.orders().list_auto_paging("SANDBOX-00042-001", None, &options);
    while let Some(item) = items.next().await {
        let order = item?;
        println!("{}", order.id);
    }
    Ok(())
}

pub async fn place_order(client: &Client) -> Result<()> {
    let order = client.orders().create("SANDBOX-00042-001", &CreateOrder {
        market: "BTC-USD".into(),
        side: OrderSide::Buy,
        r#type: CreateOrderType::Market,
        size: Some(dec("0.01")),
        max_slippage_pct: Some(dec("0.5")),
        client_order_id: Some("momentum-0142".into()),
        exits: Some(OrderExits {
            take_profit: Some(vec![OrderExitsTakeProfit {
                distance: Some(dec("600")),
                size_pct: Some(dec("100")),
                ..Default::default()
            }]),
            stop_loss: Some(OrderExitsStopLoss {
                distance: Some(dec("300")),
                ..Default::default()
            }),
            ..Default::default()
        }),
        ..Default::default()
    }).await?;
    println!("{order:?}");
    Ok(())
}

pub async fn get_order(client: &Client) -> Result<()> {
    let order = client.orders().get("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{order:?}");
    Ok(())
}

pub async fn modify_order(client: &Client) -> Result<()> {
    let order = client.orders().modify("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W", &ModifyOrder {
        limit_price: Some(dec("61900")),
        ..Default::default()
    }).await?;
    println!("{order:?}");
    Ok(())
}

pub async fn cancel_order(client: &Client) -> Result<()> {
    let order = client.orders().cancel("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{order:?}");
    Ok(())
}

pub async fn test_order(client: &Client) -> Result<()> {
    let what_if = client.orders().preview("SANDBOX-00042-001", &CreateOrder {
        market: "BTC-USD".into(),
        side: OrderSide::Buy,
        r#type: CreateOrderType::Market,
        size: Some(dec("0.01")),
        max_slippage_pct: Some(dec("0.5")),
        ..Default::default()
    }).await?;
    println!("{what_if:?}");
    Ok(())
}

pub async fn cancel_all_orders(client: &Client) -> Result<()> {
    let cancel_all_result = client.orders().cancel_all("SANDBOX-00042-001", &CancelAll {
        market: Some("BTC-USD".into()),
        ..Default::default()
    }).await?;
    println!("{cancel_all_result:?}");
    Ok(())
}

pub async fn close_position(client: &Client) -> Result<()> {
    let order = client.positions().close("SANDBOX-00042-001", "BTC-USD", &ClosePosition {
        size: Some(dec("0.005")),
        ..Default::default()
    }).await?;
    println!("{order:?}");
    Ok(())
}

pub async fn flatten(client: &Client) -> Result<()> {
    let flatten_result = client.positions().flatten("SANDBOX-00042-001").await?;
    println!("{flatten_result:?}");
    Ok(())
}

pub async fn set_margin(client: &Client) -> Result<()> {
    let margin_setting_result = client.margin().set("SANDBOX-00042-001", "BTC-USD", &MarginSettingInput {
        margin_mode: MarginMode::Isolated,
        leverage: 5,
        ..Default::default()
    }).await?;
    println!("{margin_setting_result:?}");
    Ok(())
}

pub async fn list_events(client: &Client) -> Result<()> {
    let options = RequestOptions::default();
    let mut items = client.events().list_auto_paging(None, &options);
    while let Some(item) = items.next().await {
        let event = item?;
        println!("{}", event.id);
    }
    Ok(())
}

pub async fn get_exits(client: &Client) -> Result<()> {
    let exits = client.exits().get("SANDBOX-00042-001", "BTC-USD").await?;
    println!("{exits:?}");
    Ok(())
}

pub async fn set_exits(client: &Client) -> Result<()> {
    let exits = client.exits().set("SANDBOX-00042-001", "BTC-USD", &ExitsBatch {
        expected_position: Some(ExitsBatchExpectedPosition {
            side: PositionSide::Long,
            size: dec("0.01"),
            ..Default::default()
        }),
        expected_exits: vec![ExitsBatchExpectedExit {
            id: "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W".into(),
            size: dec("0.01"),
            price: Some(dec("61700")),
            ..Default::default()
        }],
        operations: vec![ExitsBatchOperation {
            op: ExitsBatchOperationOp::Modify,
            id: Some("ord_01JAB7Q8XK4M2N5P7R9S3T5V7W".into()),
            price: Some(dec("61900")),
            ..Default::default()
        }, ExitsBatchOperation {
            op: ExitsBatchOperationOp::Place,
            group: Some(ExitsLegGroup::TakeProfit),
            r#type: Some(CreateStrategyOrderType::Limit),
            price: Some(dec("63000")),
            size: Some(dec("0.01")),
            ..Default::default()
        }],
        ..Default::default()
    }).await?;
    println!("{exits:?}");
    Ok(())
}

pub async fn set_order_exits(client: &Client) -> Result<()> {
    let order = client.orders().set_exits("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W", &OrderExits {
        take_profit: Some(vec![OrderExitsTakeProfit {
            distance: Some(dec("600")),
            size_pct: Some(dec("50")),
            ..Default::default()
        }, OrderExitsTakeProfit {
            distance: Some(dec("1200")),
            size_pct: Some(dec("50")),
            ..Default::default()
        }]),
        stop_loss: Some(OrderExitsStopLoss {
            distance: Some(dec("300")),
            r#type: Some(OrderExitsStopLossType::StopMarket),
            ..Default::default()
        }),
        ..Default::default()
    }).await?;
    println!("{order:?}");
    Ok(())
}

pub async fn create_strategy(client: &Client) -> Result<()> {
    let strategy = client.strategies().create("SANDBOX-00042-001", &CreateStrategy {
        r#type: CreateStrategyType::Twap,
        market: "BTC-USD".into(),
        side: Some(OrderSide::Buy),
        size: Some(dec("0.05")),
        duration_minutes: Some(30),
        interval_seconds: Some(60),
        client_strategy_id: Some("twap-07".into()),
        ..Default::default()
    }).await?;
    println!("{strategy:?}");
    Ok(())
}

pub async fn list_strategies(client: &Client) -> Result<()> {
    let options = RequestOptions::default();
    let mut items = client.strategies().list_auto_paging("SANDBOX-00042-001", None, &options);
    while let Some(item) = items.next().await {
        let strategy = item?;
        println!("{}", strategy.id);
    }
    Ok(())
}

pub async fn get_strategy(client: &Client) -> Result<()> {
    let strategy = client.strategies().get("SANDBOX-00042-001", "stg_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{strategy:?}");
    Ok(())
}

pub async fn cancel_strategy(client: &Client) -> Result<()> {
    let strategy = client.strategies().cancel("SANDBOX-00042-001", "stg_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{strategy:?}");
    Ok(())
}

pub async fn list_webhook_endpoints(client: &Client) -> Result<()> {
    let page = client.webhook_endpoints().list().await?;
    for webhook_endpoint in &page.data {
        println!("{}", webhook_endpoint.id);
    }
    Ok(())
}

pub async fn get_webhook_endpoint(client: &Client) -> Result<()> {
    let webhook_endpoint = client.webhook_endpoints().get("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{webhook_endpoint:?}");
    Ok(())
}

pub async fn delete_webhook_endpoint(client: &Client) -> Result<()> {
    let deleted_endpoint = client.webhook_endpoints().delete("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{deleted_endpoint:?}");
    Ok(())
}

pub async fn test_webhook_endpoint(client: &Client) -> Result<()> {
    let webhook_delivery = client.webhook_endpoints().test("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{webhook_delivery:?}");
    Ok(())
}

pub async fn list_webhook_deliveries(client: &Client) -> Result<()> {
    let options = RequestOptions::default();
    let mut items = client.webhook_endpoints().list_deliveries_auto_paging("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", None, &options);
    while let Some(item) = items.next().await {
        let webhook_delivery = item?;
        println!("{}", webhook_delivery.id);
    }
    Ok(())
}

pub async fn get_webhook_delivery(client: &Client) -> Result<()> {
    let webhook_delivery = client.webhook_endpoints().get_delivery("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", "dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{webhook_delivery:?}");
    Ok(())
}

pub async fn resend_webhook_delivery(client: &Client) -> Result<()> {
    let webhook_delivery = client.webhook_endpoints().resend_delivery("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", "dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{webhook_delivery:?}");
    Ok(())
}

pub async fn disable_webhook_endpoint(client: &Client) -> Result<()> {
    let webhook_endpoint = client.webhook_endpoints().disable("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{webhook_endpoint:?}");
    Ok(())
}

pub async fn enable_webhook_endpoint(client: &Client) -> Result<()> {
    let webhook_endpoint = client.webhook_endpoints().enable("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{webhook_endpoint:?}");
    Ok(())
}

pub async fn list_trading_view_hooks(client: &Client) -> Result<()> {
    let page = client.tradingview().list_hooks().await?;
    for trading_view_hook in &page.data {
        println!("{}", trading_view_hook.id);
    }
    Ok(())
}

pub async fn get_trading_view_hook(client: &Client) -> Result<()> {
    let trading_view_hook = client.tradingview().get_hook("K7x2mQ9pLr4A").await?;
    println!("{trading_view_hook:?}");
    Ok(())
}

pub async fn delete_trading_view_hook(client: &Client) -> Result<()> {
    let deleted_hook = client.tradingview().delete_hook("K7x2mQ9pLr4A").await?;
    println!("{deleted_hook:?}");
    Ok(())
}

pub async fn list_trading_view_alerts(client: &Client) -> Result<()> {
    let options = RequestOptions::default();
    let mut items = client.tradingview().list_alerts_auto_paging("K7x2mQ9pLr4A", None, &options);
    while let Some(item) = items.next().await {
        let trading_view_alert = item?;
        println!("{}", trading_view_alert.id);
    }
    Ok(())
}

pub async fn get_trading_view_alert(client: &Client) -> Result<()> {
    let trading_view_alert = client.tradingview().get_alert("K7x2mQ9pLr4A", "tva_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{trading_view_alert:?}");
    Ok(())
}

pub async fn disable_trading_view_hook(client: &Client) -> Result<()> {
    let trading_view_hook = client.tradingview().disable_hook("K7x2mQ9pLr4A").await?;
    println!("{trading_view_hook:?}");
    Ok(())
}

pub async fn enable_trading_view_hook(client: &Client) -> Result<()> {
    let trading_view_hook = client.tradingview().enable_hook("K7x2mQ9pLr4A").await?;
    println!("{trading_view_hook:?}");
    Ok(())
}

pub async fn parse_trading_view_message(client: &Client) -> Result<()> {
    let trading_view_parse = client.tradingview().parse(&TradingViewParseInput {
        message: "buy BTCUSD qty 0.01 tp 1% sl 61500".into(),
        ..Default::default()
    }).await?;
    println!("{trading_view_parse:?}");
    Ok(())
}

pub async fn list_webhook_event_types(client: &Client) -> Result<()> {
    let page = client.webhook_endpoints().event_types().await?;
    for webhook_event_type in &page.data {
        println!("{webhook_event_type:?}");
    }
    Ok(())
}

pub async fn get_event(client: &Client) -> Result<()> {
    let event = client.events().get("evt_01JAB7Q8XK4M2N5P7R9S3T5V7W").await?;
    println!("{event:?}");
    Ok(())
}

pub async fn list_fills(client: &Client) -> Result<()> {
    let options = RequestOptions::default();
    let mut items = client.fills().list_auto_paging("SANDBOX-00042-001", None, &options);
    while let Some(item) = items.next().await {
        let fill = item?;
        println!("{}", fill.id);
    }
    Ok(())
}
