// Generated from the API spec by generator/printers/snippets.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.phoenixperpsfunding.sdk.gen.*;
import java.math.BigDecimal;
import java.util.List;

/** Every docs snippet, compiled with the tests; never run. */
@SuppressWarnings("unused")
final class Snippets {
    private Snippets() {
    }

    static void getApi(PhoenixPerpsClient client) {
        Platform platform = client.platform().info();
        System.out.println(platform);
    }

    static void getSpec(PhoenixPerpsClient client) {
        JsonNode result = client.platform().spec();
        System.out.println(result);
    }

    static void getStatus(PhoenixPerpsClient client) {
        Status status = client.platform().status();
        System.out.println(status);
    }

    static void getLimits(PhoenixPerpsClient client) {
        Limits limits = client.platform().limits();
        System.out.println(limits);
    }

    static void getMe(PhoenixPerpsClient client) {
        Me me = client.me().get();
        System.out.println(me);
    }

    static void listMarkets(PhoenixPerpsClient client) {
        for (Market market : client.markets().list().data()) {
            System.out.println(market.market());
        }
    }

    static void listMarketStats(PhoenixPerpsClient client) {
        for (MarketStats marketStats : client.markets().stats().data()) {
            System.out.println(marketStats.market());
        }
    }

    static void getMarket(PhoenixPerpsClient client) {
        Market market = client.markets().get("BTC-USD");
        System.out.println(market);
    }

    static void listAccounts(PhoenixPerpsClient client) {
        for (Account account : client.accounts().list().data()) {
            System.out.println(account.id());
        }
    }

    static void createSandboxAccount(PhoenixPerpsClient client) {
        Account account = client.accounts().createSandbox();
        System.out.println(account);
    }

    static void getAccount(PhoenixPerpsClient client) {
        AccountDetail accountDetail = client.accounts().get("SANDBOX-00042-001");
        System.out.println(accountDetail);
    }

    static void getRisk(PhoenixPerpsClient client) {
        AccountRisk accountRisk = client.accounts().risk("SANDBOX-00042-001");
        System.out.println(accountRisk);
    }

    static void getTradingPolicy(PhoenixPerpsClient client) {
        TradingPolicy tradingPolicy = client.accounts().tradingPolicy("SANDBOX-00042-001");
        System.out.println(tradingPolicy);
    }

    static void getProgress(PhoenixPerpsClient client) {
        Progress progress = client.accounts().progress("SANDBOX-00042-001");
        System.out.println(progress);
    }

    static void getEquityHistory(PhoenixPerpsClient client) {
        EquityHistory equityHistory = client.history().equity("SANDBOX-00042-001", null);
        System.out.println(equityHistory);
    }

    static void listDailyResults(PhoenixPerpsClient client) {
        for (DailyResult dailyResult : client.history().daily("SANDBOX-00042-001").data()) {
            System.out.println(dailyResult);
        }
    }

    static void listMarginSettings(PhoenixPerpsClient client) {
        for (MarginSetting marginSetting : client.margin().list("SANDBOX-00042-001").data()) {
            System.out.println(marginSetting.market());
        }
    }

    static void listPositions(PhoenixPerpsClient client) {
        for (Position position : client.positions().list("SANDBOX-00042-001", null).data()) {
            System.out.println(position.id());
        }
    }

    static void getPosition(PhoenixPerpsClient client) {
        Position position = client.positions().get("SANDBOX-00042-001", "BTC-USD");
        System.out.println(position);
    }

    static void listClosedPositions(PhoenixPerpsClient client) {
        for (ClosedPosition closedPosition : client.positions().listClosedAutoPaging("SANDBOX-00042-001", null)) {
            System.out.println(closedPosition.market());
        }
    }

    static void listOrders(PhoenixPerpsClient client) {
        for (Order order : client.orders().listAutoPaging("SANDBOX-00042-001", null)) {
            System.out.println(order.id());
        }
    }

    static void placeOrder(PhoenixPerpsClient client) {
        Order order = client.orders().create("SANDBOX-00042-001", new CreateOrder()
                .market("BTC-USD")
                .side(OrderSide.BUY)
                .type(CreateOrderType.MARKET)
                .size(new BigDecimal("0.01"))
                .maxSlippagePct(new BigDecimal("0.5"))
                .clientOrderId("momentum-0142")
                .exits(new OrderExits()
                        .takeProfit(List.of(new OrderExitsTakeProfit()
                                .distance(new BigDecimal("600"))
                                .sizePct(new BigDecimal("100"))))
                        .stopLoss(new OrderExitsStopLoss()
                                .distance(new BigDecimal("300")))));
        System.out.println(order);
    }

    static void getOrder(PhoenixPerpsClient client) {
        Order order = client.orders().get("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(order);
    }

    static void modifyOrder(PhoenixPerpsClient client) {
        Order order = client.orders().modify("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W", new ModifyOrder()
                .limitPrice(new BigDecimal("61900")));
        System.out.println(order);
    }

    static void cancelOrder(PhoenixPerpsClient client) {
        Order order = client.orders().cancel("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(order);
    }

    static void testOrder(PhoenixPerpsClient client) {
        WhatIf whatIf = client.orders().preview("SANDBOX-00042-001", new CreateOrder()
                .market("BTC-USD")
                .side(OrderSide.BUY)
                .type(CreateOrderType.MARKET)
                .size(new BigDecimal("0.01"))
                .maxSlippagePct(new BigDecimal("0.5")));
        System.out.println(whatIf);
    }

    static void cancelAllOrders(PhoenixPerpsClient client) {
        CancelAllResult cancelAllResult = client.orders().cancelAll("SANDBOX-00042-001", new CancelAll()
                .market("BTC-USD"));
        System.out.println(cancelAllResult);
    }

    static void closePosition(PhoenixPerpsClient client) {
        Order order = client.positions().close("SANDBOX-00042-001", "BTC-USD", new ClosePosition()
                .size(new BigDecimal("0.005")));
        System.out.println(order);
    }

    static void flatten(PhoenixPerpsClient client) {
        FlattenResult flattenResult = client.positions().flatten("SANDBOX-00042-001");
        System.out.println(flattenResult);
    }

    static void setMargin(PhoenixPerpsClient client) {
        MarginSettingResult marginSettingResult = client.margin().set("SANDBOX-00042-001", "BTC-USD", new MarginSettingInput()
                .marginMode(MarginMode.ISOLATED)
                .leverage(5L));
        System.out.println(marginSettingResult);
    }

    static void listEvents(PhoenixPerpsClient client) {
        for (Event event : client.events().listAutoPaging(null)) {
            System.out.println(event.id());
        }
    }

    static void getExits(PhoenixPerpsClient client) {
        Exits exits = client.exits().get("SANDBOX-00042-001", "BTC-USD");
        System.out.println(exits);
    }

    static void setExits(PhoenixPerpsClient client) {
        Exits exits = client.exits().set("SANDBOX-00042-001", "BTC-USD", new ExitsBatch()
                .expectedPosition(new ExitsBatchExpectedPosition()
                        .side(PositionSide.LONG)
                        .size(new BigDecimal("0.01")))
                .expectedExits(List.of(new ExitsBatchExpectedExit()
                        .id("ord_01JAB7Q8XK4M2N5P7R9S3T5V7W")
                        .size(new BigDecimal("0.01"))
                        .price(new BigDecimal("61700"))))
                .operations(List.of(new ExitsBatchOperation()
                        .op(ExitsBatchOperationOp.MODIFY)
                        .id("ord_01JAB7Q8XK4M2N5P7R9S3T5V7W")
                        .price(new BigDecimal("61900")), new ExitsBatchOperation()
                        .op(ExitsBatchOperationOp.PLACE)
                        .group(ExitsLegGroup.TAKE_PROFIT)
                        .type(CreateStrategyOrderType.LIMIT)
                        .price(new BigDecimal("63000"))
                        .size(new BigDecimal("0.01")))));
        System.out.println(exits);
    }

    static void setOrderExits(PhoenixPerpsClient client) {
        Order order = client.orders().setExits("SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W", new OrderExits()
                .takeProfit(List.of(new OrderExitsTakeProfit()
                        .distance(new BigDecimal("600"))
                        .sizePct(new BigDecimal("50")), new OrderExitsTakeProfit()
                        .distance(new BigDecimal("1200"))
                        .sizePct(new BigDecimal("50"))))
                .stopLoss(new OrderExitsStopLoss()
                        .distance(new BigDecimal("300"))
                        .type(OrderExitsStopLossType.STOP_MARKET)));
        System.out.println(order);
    }

    static void createStrategy(PhoenixPerpsClient client) {
        Strategy strategy = client.strategies().create("SANDBOX-00042-001", new CreateStrategy()
                .type(CreateStrategyType.TWAP)
                .market("BTC-USD")
                .side(OrderSide.BUY)
                .size(new BigDecimal("0.05"))
                .durationMinutes(30L)
                .intervalSeconds(60L)
                .clientStrategyId("twap-07"));
        System.out.println(strategy);
    }

    static void listStrategies(PhoenixPerpsClient client) {
        for (Strategy strategy : client.strategies().listAutoPaging("SANDBOX-00042-001", null)) {
            System.out.println(strategy.id());
        }
    }

    static void getStrategy(PhoenixPerpsClient client) {
        Strategy strategy = client.strategies().get("SANDBOX-00042-001", "stg_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(strategy);
    }

    static void cancelStrategy(PhoenixPerpsClient client) {
        Strategy strategy = client.strategies().cancel("SANDBOX-00042-001", "stg_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(strategy);
    }

    static void listWebhookEndpoints(PhoenixPerpsClient client) {
        for (WebhookEndpoint webhookEndpoint : client.webhookEndpoints().list().data()) {
            System.out.println(webhookEndpoint.id());
        }
    }

    static void getWebhookEndpoint(PhoenixPerpsClient client) {
        WebhookEndpoint webhookEndpoint = client.webhookEndpoints().get("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(webhookEndpoint);
    }

    static void deleteWebhookEndpoint(PhoenixPerpsClient client) {
        DeletedEndpoint deletedEndpoint = client.webhookEndpoints().delete("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(deletedEndpoint);
    }

    static void testWebhookEndpoint(PhoenixPerpsClient client) {
        WebhookDelivery webhookDelivery = client.webhookEndpoints().test("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(webhookDelivery);
    }

    static void listWebhookDeliveries(PhoenixPerpsClient client) {
        for (WebhookDelivery webhookDelivery : client.webhookEndpoints().listDeliveriesAutoPaging("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", null)) {
            System.out.println(webhookDelivery.id());
        }
    }

    static void getWebhookDelivery(PhoenixPerpsClient client) {
        WebhookDelivery webhookDelivery = client.webhookEndpoints().getDelivery("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", "dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(webhookDelivery);
    }

    static void resendWebhookDelivery(PhoenixPerpsClient client) {
        WebhookDelivery webhookDelivery = client.webhookEndpoints().resendDelivery("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", "dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(webhookDelivery);
    }

    static void disableWebhookEndpoint(PhoenixPerpsClient client) {
        WebhookEndpoint webhookEndpoint = client.webhookEndpoints().disable("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(webhookEndpoint);
    }

    static void enableWebhookEndpoint(PhoenixPerpsClient client) {
        WebhookEndpoint webhookEndpoint = client.webhookEndpoints().enable("whk_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(webhookEndpoint);
    }

    static void listTradingViewHooks(PhoenixPerpsClient client) {
        for (TradingViewHook tradingViewHook : client.tradingView().listHooks().data()) {
            System.out.println(tradingViewHook.id());
        }
    }

    static void getTradingViewHook(PhoenixPerpsClient client) {
        TradingViewHook tradingViewHook = client.tradingView().getHook("K7x2mQ9pLr4A");
        System.out.println(tradingViewHook);
    }

    static void deleteTradingViewHook(PhoenixPerpsClient client) {
        DeletedHook deletedHook = client.tradingView().deleteHook("K7x2mQ9pLr4A");
        System.out.println(deletedHook);
    }

    static void listTradingViewAlerts(PhoenixPerpsClient client) {
        for (TradingViewAlert tradingViewAlert : client.tradingView().listAlertsAutoPaging("K7x2mQ9pLr4A", null)) {
            System.out.println(tradingViewAlert.id());
        }
    }

    static void getTradingViewAlert(PhoenixPerpsClient client) {
        TradingViewAlert tradingViewAlert = client.tradingView().getAlert("K7x2mQ9pLr4A", "tva_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(tradingViewAlert);
    }

    static void disableTradingViewHook(PhoenixPerpsClient client) {
        TradingViewHook tradingViewHook = client.tradingView().disableHook("K7x2mQ9pLr4A");
        System.out.println(tradingViewHook);
    }

    static void enableTradingViewHook(PhoenixPerpsClient client) {
        TradingViewHook tradingViewHook = client.tradingView().enableHook("K7x2mQ9pLr4A");
        System.out.println(tradingViewHook);
    }

    static void parseTradingViewMessage(PhoenixPerpsClient client) {
        TradingViewParse tradingViewParse = client.tradingView().parse(new TradingViewParseInput()
                .message("buy BTCUSD qty 0.01 tp 1% sl 61500"));
        System.out.println(tradingViewParse);
    }

    static void listWebhookEventTypes(PhoenixPerpsClient client) {
        for (WebhookEventType webhookEventType : client.webhookEndpoints().eventTypes().data()) {
            System.out.println(webhookEventType);
        }
    }

    static void getEvent(PhoenixPerpsClient client) {
        Event event = client.events().get("evt_01JAB7Q8XK4M2N5P7R9S3T5V7W");
        System.out.println(event);
    }

    static void listFills(PhoenixPerpsClient client) {
        for (Fill fill : client.fills().listAutoPaging("SANDBOX-00042-001", null)) {
            System.out.println(fill.id());
        }
    }
}
