// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk;

import com.phoenixperpsfunding.sdk.gen.*;

/** Makes the call a conformance scenario names (resource + method) through the typed client. */
final class Dispatch {
    private Dispatch() {
    }

    static Result run(PhoenixPerpsClient c, Call call, RequestOptions options) {
        switch (call.resource() + "." + call.method()) {
            case "platform.info": {
                return Result.of(c.platform().info(options));
            }
            case "platform.spec": {
                return Result.of(c.platform().spec(options));
            }
            case "platform.status": {
                return Result.of(c.platform().status(options));
            }
            case "platform.limits": {
                return Result.of(c.platform().limits(options));
            }
            case "me.get": {
                return Result.of(c.me().get(options));
            }
            case "markets.list": {
                return Result.of(c.markets().list(options));
            }
            case "markets.stats": {
                return Result.of(c.markets().stats(options));
            }
            case "markets.get": {
                return Result.of(c.markets().get(call.path("market"), options));
            }
            case "accounts.list": {
                return Result.of(c.accounts().list(options));
            }
            case "accounts.createSandbox": {
                return Result.of(c.accounts().createSandbox(options));
            }
            case "accounts.get": {
                return Result.of(c.accounts().get(call.path("account_id"), options));
            }
            case "accounts.risk": {
                return Result.of(c.accounts().risk(call.path("account_id"), options));
            }
            case "accounts.tradingPolicy": {
                return Result.of(c.accounts().tradingPolicy(call.path("account_id"), options));
            }
            case "accounts.progress": {
                return Result.of(c.accounts().progress(call.path("account_id"), options));
            }
            case "history.equity": {
                return Result.of(c.history().equity(call.path("account_id"), call.query(HistoryEquityParams.class), options));
            }
            case "history.daily": {
                return Result.of(c.history().daily(call.path("account_id"), options));
            }
            case "margin.list": {
                return Result.of(c.margin().list(call.path("account_id"), options));
            }
            case "positions.list": {
                return Result.of(c.positions().list(call.path("account_id"), call.query(PositionsListParams.class), options));
            }
            case "positions.get": {
                return Result.of(c.positions().get(call.path("account_id"), call.path("market"), options));
            }
            case "positions.listClosed": {
                if (call.iterate()) {
                    return Result.items(ids(c.positions().listClosedAutoPaging(call.path("account_id"), call.query(PositionsListClosedParams.class), options.withMaxItems(call.maxItems()))));
                }
                return Result.of(c.positions().listClosed(call.path("account_id"), call.query(PositionsListClosedParams.class), options));
            }
            case "orders.list": {
                if (call.iterate()) {
                    return Result.items(ids(c.orders().listAutoPaging(call.path("account_id"), call.query(OrdersListParams.class), options.withMaxItems(call.maxItems()))));
                }
                return Result.of(c.orders().list(call.path("account_id"), call.query(OrdersListParams.class), options));
            }
            case "orders.create": {
                return Result.of(c.orders().create(call.path("account_id"), call.body(CreateOrder.class), options));
            }
            case "orders.get": {
                return Result.of(c.orders().get(call.path("account_id"), call.path("order_id"), options));
            }
            case "orders.modify": {
                return Result.of(c.orders().modify(call.path("account_id"), call.path("order_id"), call.body(ModifyOrder.class), options));
            }
            case "orders.cancel": {
                return Result.of(c.orders().cancel(call.path("account_id"), call.path("order_id"), options));
            }
            case "orders.preview": {
                return Result.of(c.orders().preview(call.path("account_id"), call.body(CreateOrder.class), options));
            }
            case "orders.cancelAll": {
                return Result.of(c.orders().cancelAll(call.path("account_id"), call.body(CancelAll.class), options));
            }
            case "positions.close": {
                return Result.of(c.positions().close(call.path("account_id"), call.path("market"), call.body(ClosePosition.class), options));
            }
            case "positions.flatten": {
                return Result.of(c.positions().flatten(call.path("account_id"), options));
            }
            case "margin.set": {
                return Result.of(c.margin().set(call.path("account_id"), call.path("market"), call.body(MarginSettingInput.class), options));
            }
            case "events.list": {
                if (call.iterate()) {
                    return Result.items(ids(c.events().listAutoPaging(call.query(EventsListParams.class), options.withMaxItems(call.maxItems()))));
                }
                return Result.of(c.events().list(call.query(EventsListParams.class), options));
            }
            case "exits.get": {
                return Result.of(c.exits().get(call.path("account_id"), call.path("market"), options));
            }
            case "exits.set": {
                return Result.of(c.exits().set(call.path("account_id"), call.path("market"), call.body(ExitsBatch.class), options));
            }
            case "orders.setExits": {
                return Result.of(c.orders().setExits(call.path("account_id"), call.path("order_id"), call.body(OrderExits.class), options));
            }
            case "strategies.create": {
                return Result.of(c.strategies().create(call.path("account_id"), call.body(CreateStrategy.class), options));
            }
            case "strategies.list": {
                if (call.iterate()) {
                    return Result.items(ids(c.strategies().listAutoPaging(call.path("account_id"), call.query(StrategiesListParams.class), options.withMaxItems(call.maxItems()))));
                }
                return Result.of(c.strategies().list(call.path("account_id"), call.query(StrategiesListParams.class), options));
            }
            case "strategies.get": {
                return Result.of(c.strategies().get(call.path("account_id"), call.path("strategy_id"), options));
            }
            case "strategies.cancel": {
                return Result.of(c.strategies().cancel(call.path("account_id"), call.path("strategy_id"), options));
            }
            case "webhookEndpoints.list": {
                return Result.of(c.webhookEndpoints().list(options));
            }
            case "webhookEndpoints.get": {
                return Result.of(c.webhookEndpoints().get(call.path("endpoint_id"), options));
            }
            case "webhookEndpoints.delete": {
                return Result.of(c.webhookEndpoints().delete(call.path("endpoint_id"), options));
            }
            case "webhookEndpoints.test": {
                return Result.of(c.webhookEndpoints().test(call.path("endpoint_id"), options));
            }
            case "webhookEndpoints.listDeliveries": {
                if (call.iterate()) {
                    return Result.items(ids(c.webhookEndpoints().listDeliveriesAutoPaging(call.path("endpoint_id"), call.query(WebhookEndpointsListDeliveriesParams.class), options.withMaxItems(call.maxItems()))));
                }
                return Result.of(c.webhookEndpoints().listDeliveries(call.path("endpoint_id"), call.query(WebhookEndpointsListDeliveriesParams.class), options));
            }
            case "webhookEndpoints.getDelivery": {
                return Result.of(c.webhookEndpoints().getDelivery(call.path("endpoint_id"), call.path("delivery_id"), options));
            }
            case "webhookEndpoints.resendDelivery": {
                return Result.of(c.webhookEndpoints().resendDelivery(call.path("endpoint_id"), call.path("delivery_id"), options));
            }
            case "webhookEndpoints.disable": {
                return Result.of(c.webhookEndpoints().disable(call.path("endpoint_id"), options));
            }
            case "webhookEndpoints.enable": {
                return Result.of(c.webhookEndpoints().enable(call.path("endpoint_id"), options));
            }
            case "tradingview.listHooks": {
                return Result.of(c.tradingView().listHooks(options));
            }
            case "tradingview.getHook": {
                return Result.of(c.tradingView().getHook(call.path("hook_id"), options));
            }
            case "tradingview.deleteHook": {
                return Result.of(c.tradingView().deleteHook(call.path("hook_id"), options));
            }
            case "tradingview.listAlerts": {
                if (call.iterate()) {
                    return Result.items(ids(c.tradingView().listAlertsAutoPaging(call.path("hook_id"), call.query(TradingviewListAlertsParams.class), options.withMaxItems(call.maxItems()))));
                }
                return Result.of(c.tradingView().listAlerts(call.path("hook_id"), call.query(TradingviewListAlertsParams.class), options));
            }
            case "tradingview.getAlert": {
                return Result.of(c.tradingView().getAlert(call.path("hook_id"), call.path("alert_id"), options));
            }
            case "tradingview.disableHook": {
                return Result.of(c.tradingView().disableHook(call.path("hook_id"), options));
            }
            case "tradingview.enableHook": {
                return Result.of(c.tradingView().enableHook(call.path("hook_id"), options));
            }
            case "tradingview.parse": {
                return Result.of(c.tradingView().parse(call.body(TradingViewParseInput.class), options));
            }
            case "webhookEndpoints.eventTypes": {
                return Result.of(c.webhookEndpoints().eventTypes(options));
            }
            case "events.get": {
                return Result.of(c.events().get(call.path("event_id"), options));
            }
            case "fills.list": {
                if (call.iterate()) {
                    return Result.items(ids(c.fills().listAutoPaging(call.path("account_id"), call.query(FillsListParams.class), options.withMaxItems(call.maxItems()))));
                }
                return Result.of(c.fills().list(call.path("account_id"), call.query(FillsListParams.class), options));
            }
            default:
                throw new IllegalArgumentException("no operation " + call.resource() + "." + call.method());
        }
    }

    static java.util.List<String> ids(Iterable<?> items) {
        java.util.List<String> ids = new java.util.ArrayList<>();
        for (Object item : items) {
            ids.add(Json.MAPPER.valueToTree(item).path("id").asText());
        }
        return ids;
    }
}
