// Generated from the API spec by generator/printers/rust.js. Do not edit: run `node generator`.

use super::{ids, Answer, Call};
use phoenixperps::*;

/// Makes the call a conformance scenario names (resource + method) through the typed client.
pub async fn dispatch(c: &Client, call: &Call) -> Result<Answer> {
    match format!("{}.{}", call.resource, call.method).as_str() {
        "platform.info" => {
            let r = c.platform().info_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "platform.spec" => {
            let r = c.platform().spec_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "platform.status" => {
            let r = c.platform().status_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "platform.limits" => {
            let r = c.platform().limits_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "me.get" => {
            let r = c.me().get_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "markets.list" => {
            let r = c.markets().list_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "markets.stats" => {
            let r = c.markets().stats_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "markets.get" => {
            let r = c.markets().get_with(&call.path("market"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "accounts.list" => {
            let r = c.accounts().list_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "accounts.createSandbox" => {
            let r = c.accounts().create_sandbox().await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "accounts.get" => {
            let r = c.accounts().get_with(&call.path("account_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "accounts.risk" => {
            let r = c.accounts().risk_with(&call.path("account_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "accounts.tradingPolicy" => {
            let r = c.accounts().trading_policy_with(&call.path("account_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "accounts.progress" => {
            let r = c.accounts().progress_with(&call.path("account_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "history.equity" => {
            let params: Option<HistoryEquityParams> = call.query();
            let r = c.history().equity_with(&call.path("account_id"), params.as_ref(), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "history.daily" => {
            let r = c.history().daily_with(&call.path("account_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "margin.list" => {
            let r = c.margin().list_with(&call.path("account_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "positions.list" => {
            let params: Option<PositionsListParams> = call.query();
            let r = c.positions().list_with(&call.path("account_id"), params.as_ref(), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "positions.get" => {
            let r = c.positions().get_with(&call.path("account_id"), &call.path("market"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "positions.listClosed" => {
            let params: Option<PositionsListClosedParams> = call.query();
            if call.iterate {
                let opts = RequestOptions { max_items: call.max_items, ..Default::default() };
                return Ok(Answer::Items(ids(c.positions().list_closed_auto_paging(&call.path("account_id"), params.as_ref(), &opts)).await?));
            }
            let r = c.positions().list_closed_with(&call.path("account_id"), params.as_ref(), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "orders.list" => {
            let params: Option<OrdersListParams> = call.query();
            if call.iterate {
                let opts = RequestOptions { max_items: call.max_items, ..Default::default() };
                return Ok(Answer::Items(ids(c.orders().list_auto_paging(&call.path("account_id"), params.as_ref(), &opts)).await?));
            }
            let r = c.orders().list_with(&call.path("account_id"), params.as_ref(), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "orders.create" => {
            let body: CreateOrder = call.body();
            let r = c.orders().create(&call.path("account_id"), &body).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "orders.get" => {
            let r = c.orders().get_with(&call.path("account_id"), &call.path("order_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "orders.modify" => {
            let body: ModifyOrder = call.body();
            let r = c.orders().modify(&call.path("account_id"), &call.path("order_id"), &body).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "orders.cancel" => {
            let r = c.orders().cancel(&call.path("account_id"), &call.path("order_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "orders.preview" => {
            let body: CreateOrder = call.body();
            let r = c.orders().preview(&call.path("account_id"), &body).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "orders.cancelAll" => {
            let body: CancelAll = call.body();
            let r = c.orders().cancel_all(&call.path("account_id"), &body).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "positions.close" => {
            let body: ClosePosition = call.body();
            let r = c.positions().close(&call.path("account_id"), &call.path("market"), &body).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "positions.flatten" => {
            let r = c.positions().flatten(&call.path("account_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "margin.set" => {
            let body: MarginSettingInput = call.body();
            let r = c.margin().set(&call.path("account_id"), &call.path("market"), &body).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "events.list" => {
            let params: Option<EventsListParams> = call.query();
            if call.iterate {
                let opts = RequestOptions { max_items: call.max_items, ..Default::default() };
                return Ok(Answer::Items(ids(c.events().list_auto_paging(params.as_ref(), &opts)).await?));
            }
            let r = c.events().list_with(params.as_ref(), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "exits.get" => {
            let r = c.exits().get_with(&call.path("account_id"), &call.path("market"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "exits.set" => {
            let body: ExitsBatch = call.body();
            let r = c.exits().set(&call.path("account_id"), &call.path("market"), &body).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "orders.setExits" => {
            let body: OrderExits = call.body();
            let r = c.orders().set_exits(&call.path("account_id"), &call.path("order_id"), &body).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "strategies.create" => {
            let body: CreateStrategy = call.body();
            let r = c.strategies().create(&call.path("account_id"), &body).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "strategies.list" => {
            let params: Option<StrategiesListParams> = call.query();
            if call.iterate {
                let opts = RequestOptions { max_items: call.max_items, ..Default::default() };
                return Ok(Answer::Items(ids(c.strategies().list_auto_paging(&call.path("account_id"), params.as_ref(), &opts)).await?));
            }
            let r = c.strategies().list_with(&call.path("account_id"), params.as_ref(), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "strategies.get" => {
            let r = c.strategies().get_with(&call.path("account_id"), &call.path("strategy_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "strategies.cancel" => {
            let r = c.strategies().cancel(&call.path("account_id"), &call.path("strategy_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "webhookEndpoints.list" => {
            let r = c.webhook_endpoints().list_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "webhookEndpoints.get" => {
            let r = c.webhook_endpoints().get_with(&call.path("endpoint_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "webhookEndpoints.delete" => {
            let r = c.webhook_endpoints().delete(&call.path("endpoint_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "webhookEndpoints.test" => {
            let r = c.webhook_endpoints().test(&call.path("endpoint_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "webhookEndpoints.listDeliveries" => {
            let params: Option<WebhookEndpointsListDeliveriesParams> = call.query();
            if call.iterate {
                let opts = RequestOptions { max_items: call.max_items, ..Default::default() };
                return Ok(Answer::Items(ids(c.webhook_endpoints().list_deliveries_auto_paging(&call.path("endpoint_id"), params.as_ref(), &opts)).await?));
            }
            let r = c.webhook_endpoints().list_deliveries_with(&call.path("endpoint_id"), params.as_ref(), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "webhookEndpoints.getDelivery" => {
            let r = c.webhook_endpoints().get_delivery_with(&call.path("endpoint_id"), &call.path("delivery_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "webhookEndpoints.resendDelivery" => {
            let r = c.webhook_endpoints().resend_delivery(&call.path("endpoint_id"), &call.path("delivery_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "webhookEndpoints.disable" => {
            let r = c.webhook_endpoints().disable(&call.path("endpoint_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "webhookEndpoints.enable" => {
            let r = c.webhook_endpoints().enable(&call.path("endpoint_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "tradingview.listHooks" => {
            let r = c.tradingview().list_hooks_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "tradingview.getHook" => {
            let r = c.tradingview().get_hook_with(&call.path("hook_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "tradingview.deleteHook" => {
            let r = c.tradingview().delete_hook(&call.path("hook_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "tradingview.listAlerts" => {
            let params: Option<TradingviewListAlertsParams> = call.query();
            if call.iterate {
                let opts = RequestOptions { max_items: call.max_items, ..Default::default() };
                return Ok(Answer::Items(ids(c.tradingview().list_alerts_auto_paging(&call.path("hook_id"), params.as_ref(), &opts)).await?));
            }
            let r = c.tradingview().list_alerts_with(&call.path("hook_id"), params.as_ref(), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "tradingview.getAlert" => {
            let r = c.tradingview().get_alert_with(&call.path("hook_id"), &call.path("alert_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "tradingview.disableHook" => {
            let r = c.tradingview().disable_hook(&call.path("hook_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "tradingview.enableHook" => {
            let r = c.tradingview().enable_hook(&call.path("hook_id")).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "tradingview.parse" => {
            let body: TradingViewParseInput = call.body();
            let r = c.tradingview().parse(&body).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "webhookEndpoints.eventTypes" => {
            let r = c.webhook_endpoints().event_types_with(&RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        "events.get" => {
            let r = c.events().get_with(&call.path("event_id"), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(&r).unwrap()))
        }
        "fills.list" => {
            let params: Option<FillsListParams> = call.query();
            if call.iterate {
                let opts = RequestOptions { max_items: call.max_items, ..Default::default() };
                return Ok(Answer::Items(ids(c.fills().list_auto_paging(&call.path("account_id"), params.as_ref(), &opts)).await?));
            }
            let r = c.fills().list_with(&call.path("account_id"), params.as_ref(), &RequestOptions::default()).await?;
            Ok(Answer::Value(serde_json::to_value(r.map(|p| p.data)).unwrap()))
        }
        other => panic!("no operation {other}"),
    }
}
