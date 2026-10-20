// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.phoenixperpsfunding.sdk.Page;
import com.phoenixperpsfunding.sdk.RequestOptions;
import com.phoenixperpsfunding.sdk.Transport;
import com.phoenixperpsfunding.sdk.Transport.Operation;
import com.fasterxml.jackson.databind.JsonNode;

/** The resources of a client, one per resource of the API. */
public final class Resources {
    private Resources() {
    }

    /** The operations of the API. */
    public static final class Ops {
        private Ops() {
        }

        static final Operation GET_API = new Operation("getApi", "GET", "/v1", false, true, null, "public");
        static final Operation GET_SPEC = new Operation("getSpec", "GET", "/v1/openapi.json", false, true, null, "public");
        static final Operation GET_STATUS = new Operation("getStatus", "GET", "/v1/status", false, true, null, "public");
        static final Operation GET_LIMITS = new Operation("getLimits", "GET", "/v1/limits", false, true, null, "public");
        static final Operation GET_ME = new Operation("getMe", "GET", "/v1/me", false, true, null, "read");
        static final Operation LIST_MARKETS = new Operation("listMarkets", "GET", "/v1/markets", false, true, null, "public");
        static final Operation LIST_MARKET_STATS = new Operation("listMarketStats", "GET", "/v1/markets/stats", false, true, null, "public");
        static final Operation GET_MARKET = new Operation("getMarket", "GET", "/v1/markets/{market}", false, true, null, "public");
        static final Operation LIST_ACCOUNTS = new Operation("listAccounts", "GET", "/v1/accounts", false, true, null, "read");
        static final Operation CREATE_SANDBOX_ACCOUNT = new Operation("createSandboxAccount", "POST", "/v1/accounts", true, false, null, "trade");
        static final Operation GET_ACCOUNT = new Operation("getAccount", "GET", "/v1/accounts/{account_id}", false, true, null, "read");
        static final Operation GET_RISK = new Operation("getRisk", "GET", "/v1/accounts/{account_id}/risk", false, true, null, "read");
        static final Operation GET_TRADING_POLICY = new Operation("getTradingPolicy", "GET", "/v1/accounts/{account_id}/trading-policy", false, true, null, "read");
        static final Operation GET_PROGRESS = new Operation("getProgress", "GET", "/v1/accounts/{account_id}/progress", false, true, null, "read");
        static final Operation GET_EQUITY_HISTORY = new Operation("getEquityHistory", "GET", "/v1/accounts/{account_id}/history/equity", false, true, null, "read");
        static final Operation LIST_DAILY_RESULTS = new Operation("listDailyResults", "GET", "/v1/accounts/{account_id}/history/daily", false, true, null, "read");
        static final Operation LIST_MARGIN_SETTINGS = new Operation("listMarginSettings", "GET", "/v1/accounts/{account_id}/margin", false, true, null, "read");
        static final Operation LIST_POSITIONS = new Operation("listPositions", "GET", "/v1/accounts/{account_id}/positions", false, true, null, "read");
        static final Operation GET_POSITION = new Operation("getPosition", "GET", "/v1/accounts/{account_id}/positions/{market}", false, true, null, "read");
        static final Operation LIST_CLOSED_POSITIONS = new Operation("listClosedPositions", "GET", "/v1/accounts/{account_id}/closed-positions", false, true, "cursor", "read");
        static final Operation LIST_ORDERS = new Operation("listOrders", "GET", "/v1/accounts/{account_id}/orders", false, true, "cursor", "read");
        static final Operation PLACE_ORDER = new Operation("placeOrder", "POST", "/v1/accounts/{account_id}/orders", true, false, null, "trade");
        static final Operation GET_ORDER = new Operation("getOrder", "GET", "/v1/accounts/{account_id}/orders/{order_id}", false, true, null, "read");
        static final Operation MODIFY_ORDER = new Operation("modifyOrder", "PATCH", "/v1/accounts/{account_id}/orders/{order_id}", false, false, null, "trade");
        static final Operation CANCEL_ORDER = new Operation("cancelOrder", "DELETE", "/v1/accounts/{account_id}/orders/{order_id}", false, true, null, "trade");
        static final Operation TEST_ORDER = new Operation("testOrder", "POST", "/v1/accounts/{account_id}/orders/test", false, true, null, "read");
        static final Operation CANCEL_ALL_ORDERS = new Operation("cancelAllOrders", "POST", "/v1/accounts/{account_id}/orders/cancel-all", true, false, null, "trade");
        static final Operation CLOSE_POSITION = new Operation("closePosition", "POST", "/v1/accounts/{account_id}/positions/{market}/close", true, false, null, "trade");
        static final Operation FLATTEN = new Operation("flatten", "POST", "/v1/accounts/{account_id}/flatten", true, false, null, "trade");
        static final Operation SET_MARGIN = new Operation("setMargin", "PUT", "/v1/accounts/{account_id}/margin/{market}", false, true, null, "trade");
        static final Operation LIST_EVENTS = new Operation("listEvents", "GET", "/v1/events", false, true, "after", "read");
        static final Operation GET_EXITS = new Operation("getExits", "GET", "/v1/accounts/{account_id}/positions/{market}/exits", false, true, null, "read");
        static final Operation SET_EXITS = new Operation("setExits", "PUT", "/v1/accounts/{account_id}/positions/{market}/exits", false, true, null, "trade");
        static final Operation SET_ORDER_EXITS = new Operation("setOrderExits", "PUT", "/v1/accounts/{account_id}/orders/{order_id}/exits", false, true, null, "trade");
        static final Operation CREATE_STRATEGY = new Operation("createStrategy", "POST", "/v1/accounts/{account_id}/strategies", true, false, null, "trade");
        static final Operation LIST_STRATEGIES = new Operation("listStrategies", "GET", "/v1/accounts/{account_id}/strategies", false, true, "cursor", "read");
        static final Operation GET_STRATEGY = new Operation("getStrategy", "GET", "/v1/accounts/{account_id}/strategies/{strategy_id}", false, true, null, "read");
        static final Operation CANCEL_STRATEGY = new Operation("cancelStrategy", "DELETE", "/v1/accounts/{account_id}/strategies/{strategy_id}", false, true, null, "trade");
        static final Operation LIST_WEBHOOK_ENDPOINTS = new Operation("listWebhookEndpoints", "GET", "/v1/webhook-endpoints", false, true, null, "read");
        static final Operation GET_WEBHOOK_ENDPOINT = new Operation("getWebhookEndpoint", "GET", "/v1/webhook-endpoints/{endpoint_id}", false, true, null, "read");
        static final Operation DELETE_WEBHOOK_ENDPOINT = new Operation("deleteWebhookEndpoint", "DELETE", "/v1/webhook-endpoints/{endpoint_id}", false, true, null, "trade");
        static final Operation TEST_WEBHOOK_ENDPOINT = new Operation("testWebhookEndpoint", "POST", "/v1/webhook-endpoints/{endpoint_id}/test", false, false, null, "trade");
        static final Operation LIST_WEBHOOK_DELIVERIES = new Operation("listWebhookDeliveries", "GET", "/v1/webhook-endpoints/{endpoint_id}/deliveries", false, true, "cursor", "read");
        static final Operation GET_WEBHOOK_DELIVERY = new Operation("getWebhookDelivery", "GET", "/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}", false, true, null, "read");
        static final Operation RESEND_WEBHOOK_DELIVERY = new Operation("resendWebhookDelivery", "POST", "/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}/resend", false, false, null, "trade");
        static final Operation DISABLE_WEBHOOK_ENDPOINT = new Operation("disableWebhookEndpoint", "POST", "/v1/webhook-endpoints/{endpoint_id}/disable", false, true, null, "trade");
        static final Operation ENABLE_WEBHOOK_ENDPOINT = new Operation("enableWebhookEndpoint", "POST", "/v1/webhook-endpoints/{endpoint_id}/enable", false, true, null, "trade");
        static final Operation LIST_TRADING_VIEW_HOOKS = new Operation("listTradingViewHooks", "GET", "/v1/tradingview/hooks", false, true, null, "read");
        static final Operation GET_TRADING_VIEW_HOOK = new Operation("getTradingViewHook", "GET", "/v1/tradingview/hooks/{hook_id}", false, true, null, "read");
        static final Operation DELETE_TRADING_VIEW_HOOK = new Operation("deleteTradingViewHook", "DELETE", "/v1/tradingview/hooks/{hook_id}", false, true, null, "trade");
        static final Operation LIST_TRADING_VIEW_ALERTS = new Operation("listTradingViewAlerts", "GET", "/v1/tradingview/hooks/{hook_id}/alerts", false, true, "cursor", "read");
        static final Operation GET_TRADING_VIEW_ALERT = new Operation("getTradingViewAlert", "GET", "/v1/tradingview/hooks/{hook_id}/alerts/{alert_id}", false, true, null, "read");
        static final Operation DISABLE_TRADING_VIEW_HOOK = new Operation("disableTradingViewHook", "POST", "/v1/tradingview/hooks/{hook_id}/disable", false, true, null, "trade");
        static final Operation ENABLE_TRADING_VIEW_HOOK = new Operation("enableTradingViewHook", "POST", "/v1/tradingview/hooks/{hook_id}/enable", false, true, null, "trade");
        static final Operation PARSE_TRADING_VIEW_MESSAGE = new Operation("parseTradingViewMessage", "POST", "/v1/tradingview/parse", false, true, null, "read");
        static final Operation LIST_WEBHOOK_EVENT_TYPES = new Operation("listWebhookEventTypes", "GET", "/v1/webhook-event-types", false, true, null, "read");
        static final Operation GET_EVENT = new Operation("getEvent", "GET", "/v1/events/{event_id}", false, true, null, "read");
        static final Operation LIST_FILLS = new Operation("listFills", "GET", "/v1/accounts/{account_id}/fills", false, true, "cursor", "read");

        /** Every operation, for the raw request (its route's retry-safety and budget). */
        public static final java.util.List<Operation> ALL = java.util.List.of(GET_API, GET_SPEC, GET_STATUS, GET_LIMITS, GET_ME, LIST_MARKETS, LIST_MARKET_STATS, GET_MARKET, LIST_ACCOUNTS, CREATE_SANDBOX_ACCOUNT, GET_ACCOUNT, GET_RISK, GET_TRADING_POLICY, GET_PROGRESS, GET_EQUITY_HISTORY, LIST_DAILY_RESULTS, LIST_MARGIN_SETTINGS, LIST_POSITIONS, GET_POSITION, LIST_CLOSED_POSITIONS, LIST_ORDERS, PLACE_ORDER, GET_ORDER, MODIFY_ORDER, CANCEL_ORDER, TEST_ORDER, CANCEL_ALL_ORDERS, CLOSE_POSITION, FLATTEN, SET_MARGIN, LIST_EVENTS, GET_EXITS, SET_EXITS, SET_ORDER_EXITS, CREATE_STRATEGY, LIST_STRATEGIES, GET_STRATEGY, CANCEL_STRATEGY, LIST_WEBHOOK_ENDPOINTS, GET_WEBHOOK_ENDPOINT, DELETE_WEBHOOK_ENDPOINT, TEST_WEBHOOK_ENDPOINT, LIST_WEBHOOK_DELIVERIES, GET_WEBHOOK_DELIVERY, RESEND_WEBHOOK_DELIVERY, DISABLE_WEBHOOK_ENDPOINT, ENABLE_WEBHOOK_ENDPOINT, LIST_TRADING_VIEW_HOOKS, GET_TRADING_VIEW_HOOK, DELETE_TRADING_VIEW_HOOK, LIST_TRADING_VIEW_ALERTS, GET_TRADING_VIEW_ALERT, DISABLE_TRADING_VIEW_HOOK, ENABLE_TRADING_VIEW_HOOK, PARSE_TRADING_VIEW_MESSAGE, LIST_WEBHOOK_EVENT_TYPES, GET_EVENT, LIST_FILLS);
    }

    /** Every resource of a client. */
    public static final class All {
        /** The platform calls. */
        public final PlatformResource platform;

        /** The me calls. */
        public final MeResource me;

        /** The markets calls. */
        public final MarketsResource markets;

        /** The accounts calls. */
        public final AccountsResource accounts;

        /** The history calls. */
        public final HistoryResource history;

        /** The margin calls. */
        public final MarginResource margin;

        /** The positions calls. */
        public final PositionsResource positions;

        /** The orders calls. */
        public final OrdersResource orders;

        /** The events calls. */
        public final EventsResource events;

        /** The exits calls. */
        public final ExitsResource exits;

        /** The strategies calls. */
        public final StrategiesResource strategies;

        /** The webhookEndpoints calls. */
        public final WebhookEndpointsResource webhookEndpoints;

        /** The tradingview calls. */
        public final TradingViewResource tradingView;

        /** The fills calls. */
        public final FillsResource fills;

        /** The resources over a transport. */
        public All(Transport c) {
            platform = new PlatformResource(c);
            me = new MeResource(c);
            markets = new MarketsResource(c);
            accounts = new AccountsResource(c);
            history = new HistoryResource(c);
            margin = new MarginResource(c);
            positions = new PositionsResource(c);
            orders = new OrdersResource(c);
            events = new EventsResource(c);
            exits = new ExitsResource(c);
            strategies = new StrategiesResource(c);
            webhookEndpoints = new WebhookEndpointsResource(c);
            tradingView = new TradingViewResource(c);
            fills = new FillsResource(c);
        }
    }

    /** The resources as methods of the client (client.orders()). */
    public interface Access {
        /** Every resource. */
        All resources();

        /** The platform calls. */
        default PlatformResource platform() {
            return resources().platform;
        }

        /** The me calls. */
        default MeResource me() {
            return resources().me;
        }

        /** The markets calls. */
        default MarketsResource markets() {
            return resources().markets;
        }

        /** The accounts calls. */
        default AccountsResource accounts() {
            return resources().accounts;
        }

        /** The history calls. */
        default HistoryResource history() {
            return resources().history;
        }

        /** The margin calls. */
        default MarginResource margin() {
            return resources().margin;
        }

        /** The positions calls. */
        default PositionsResource positions() {
            return resources().positions;
        }

        /** The orders calls. */
        default OrdersResource orders() {
            return resources().orders;
        }

        /** The events calls. */
        default EventsResource events() {
            return resources().events;
        }

        /** The exits calls. */
        default ExitsResource exits() {
            return resources().exits;
        }

        /** The strategies calls. */
        default StrategiesResource strategies() {
            return resources().strategies;
        }

        /** The webhookEndpoints calls. */
        default WebhookEndpointsResource webhookEndpoints() {
            return resources().webhookEndpoints;
        }

        /** The tradingview calls. */
        default TradingViewResource tradingView() {
            return resources().tradingView;
        }

        /** The fills calls. */
        default FillsResource fills() {
            return resources().fills;
        }
    }

    /** The platform calls (client.platform()). */
    public static final class PlatformResource {
        private final Transport c;

        PlatformResource(Transport c) {
            this.c = c;
        }

        /** Get the API. */
        public Platform info(RequestOptions options) {
            return c.one(Ops.GET_API, null, null, null, Platform.class, options);
        }

        /** Get the API. */
        public Platform info() {
            return info(null);
        }

        /** Get the OpenAPI document. */
        public JsonNode spec(RequestOptions options) {
            return c.one(Ops.GET_SPEC, null, null, null, JsonNode.class, options);
        }

        /** Get the OpenAPI document. */
        public JsonNode spec() {
            return spec(null);
        }

        /** Get the status. */
        public Status status(RequestOptions options) {
            return c.one(Ops.GET_STATUS, null, null, null, Status.class, options);
        }

        /** Get the status. */
        public Status status() {
            return status(null);
        }

        /** Get the limits. */
        public Limits limits(RequestOptions options) {
            return c.one(Ops.GET_LIMITS, null, null, null, Limits.class, options);
        }

        /** Get the limits. */
        public Limits limits() {
            return limits(null);
        }
    }

    /** The me calls (client.me()). */
    public static final class MeResource {
        private final Transport c;

        MeResource(Transport c) {
            this.c = c;
        }

        /** Get your key. */
        public Me get(RequestOptions options) {
            return c.one(Ops.GET_ME, null, null, null, Me.class, options);
        }

        /** Get your key. */
        public Me get() {
            return get(null);
        }
    }

    /** The markets calls (client.markets()). */
    public static final class MarketsResource {
        private final Transport c;

        MarketsResource(Transport c) {
            this.c = c;
        }

        /** List markets. */
        public Page<Market> list(RequestOptions options) {
            return c.list(Ops.LIST_MARKETS, null, null, Market.class, options);
        }

        /** List markets. */
        public Page<Market> list() {
            return list(null);
        }

        /** List market stats. */
        public Page<MarketStats> stats(RequestOptions options) {
            return c.list(Ops.LIST_MARKET_STATS, null, null, MarketStats.class, options);
        }

        /** List market stats. */
        public Page<MarketStats> stats() {
            return stats(null);
        }

        /** Get a market. */
        public Market get(String market, RequestOptions options) {
            return c.one(Ops.GET_MARKET, new String[] {market}, null, null, Market.class, options);
        }

        /** Get a market. */
        public Market get(String market) {
            return get(market, null);
        }
    }

    /** The accounts calls (client.accounts()). */
    public static final class AccountsResource {
        private final Transport c;

        AccountsResource(Transport c) {
            this.c = c;
        }

        /** List accounts. */
        public Page<Account> list(RequestOptions options) {
            return c.list(Ops.LIST_ACCOUNTS, null, null, Account.class, options);
        }

        /** List accounts. */
        public Page<Account> list() {
            return list(null);
        }

        /** Open a sandbox account. */
        public Account createSandbox(RequestOptions options) {
            return c.one(Ops.CREATE_SANDBOX_ACCOUNT, null, null, null, Account.class, options);
        }

        /** Open a sandbox account. */
        public Account createSandbox() {
            return createSandbox(null);
        }

        /** Get an account. */
        public AccountDetail get(String accountId, RequestOptions options) {
            return c.one(Ops.GET_ACCOUNT, new String[] {accountId}, null, null, AccountDetail.class, options);
        }

        /** Get an account. */
        public AccountDetail get(String accountId) {
            return get(accountId, null);
        }

        /** Get live risk. */
        public AccountRisk risk(String accountId, RequestOptions options) {
            return c.one(Ops.GET_RISK, new String[] {accountId}, null, null, AccountRisk.class, options);
        }

        /** Get live risk. */
        public AccountRisk risk(String accountId) {
            return risk(accountId, null);
        }

        /** Get the trading policy. */
        public TradingPolicy tradingPolicy(String accountId, RequestOptions options) {
            return c.one(Ops.GET_TRADING_POLICY, new String[] {accountId}, null, null, TradingPolicy.class, options);
        }

        /** Get the trading policy. */
        public TradingPolicy tradingPolicy(String accountId) {
            return tradingPolicy(accountId, null);
        }

        /** Get progress. */
        public Progress progress(String accountId, RequestOptions options) {
            return c.one(Ops.GET_PROGRESS, new String[] {accountId}, null, null, Progress.class, options);
        }

        /** Get progress. */
        public Progress progress(String accountId) {
            return progress(accountId, null);
        }
    }

    /** The history calls (client.history()). */
    public static final class HistoryResource {
        private final Transport c;

        HistoryResource(Transport c) {
            this.c = c;
        }

        /** Get the equity curve. */
        public EquityHistory equity(String accountId, HistoryEquityParams params, RequestOptions options) {
            return c.one(Ops.GET_EQUITY_HISTORY, new String[] {accountId}, (params == null ? null : params.toQuery()), null, EquityHistory.class, options);
        }

        /** Get the equity curve. */
        public EquityHistory equity(String accountId, HistoryEquityParams params) {
            return equity(accountId, params, null);
        }

        /** List daily results. */
        public Page<DailyResult> daily(String accountId, RequestOptions options) {
            return c.list(Ops.LIST_DAILY_RESULTS, new String[] {accountId}, null, DailyResult.class, options);
        }

        /** List daily results. */
        public Page<DailyResult> daily(String accountId) {
            return daily(accountId, null);
        }
    }

    /** The margin calls (client.margin()). */
    public static final class MarginResource {
        private final Transport c;

        MarginResource(Transport c) {
            this.c = c;
        }

        /** List margin settings. */
        public Page<MarginSetting> list(String accountId, RequestOptions options) {
            return c.list(Ops.LIST_MARGIN_SETTINGS, new String[] {accountId}, null, MarginSetting.class, options);
        }

        /** List margin settings. */
        public Page<MarginSetting> list(String accountId) {
            return list(accountId, null);
        }

        /** Set leverage and margin mode. */
        public MarginSettingResult set(String accountId, String market, MarginSettingInput body, RequestOptions options) {
            return c.one(Ops.SET_MARGIN, new String[] {accountId, market}, null, body, MarginSettingResult.class, options);
        }

        /** Set leverage and margin mode. */
        public MarginSettingResult set(String accountId, String market, MarginSettingInput body) {
            return set(accountId, market, body, null);
        }
    }

    /** The positions calls (client.positions()). */
    public static final class PositionsResource {
        private final Transport c;

        PositionsResource(Transport c) {
            this.c = c;
        }

        /** List open positions. */
        public Page<Position> list(String accountId, PositionsListParams params, RequestOptions options) {
            return c.list(Ops.LIST_POSITIONS, new String[] {accountId}, (params == null ? null : params.toQuery()), Position.class, options);
        }

        /** List open positions. */
        public Page<Position> list(String accountId, PositionsListParams params) {
            return list(accountId, params, null);
        }

        /** Get an open position. */
        public Position get(String accountId, String market, RequestOptions options) {
            return c.one(Ops.GET_POSITION, new String[] {accountId, market}, null, null, Position.class, options);
        }

        /** Get an open position. */
        public Position get(String accountId, String market) {
            return get(accountId, market, null);
        }

        /** List closed positions. */
        public Page<ClosedPosition> listClosed(String accountId, PositionsListClosedParams params, RequestOptions options) {
            return c.list(Ops.LIST_CLOSED_POSITIONS, new String[] {accountId}, (params == null ? null : params.toQuery()), ClosedPosition.class, options);
        }

        /** List closed positions. */
        public Page<ClosedPosition> listClosed(String accountId, PositionsListClosedParams params) {
            return listClosed(accountId, params, null);
        }

        /** Every item of listClosed, page after page (RequestOptions.maxItems caps it). */
        public Iterable<ClosedPosition> listClosedAutoPaging(String accountId, PositionsListClosedParams params, RequestOptions options) {
            return c.autoPaging(() -> listClosed(accountId, params, options), options);
        }

        /** Every item of listClosed, page after page. */
        public Iterable<ClosedPosition> listClosedAutoPaging(String accountId, PositionsListClosedParams params) {
            return listClosedAutoPaging(accountId, params, null);
        }

        /** Close a position. */
        public Order close(String accountId, String market, ClosePosition body, RequestOptions options) {
            return c.one(Ops.CLOSE_POSITION, new String[] {accountId, market}, null, body, Order.class, options);
        }

        /** Close a position. */
        public Order close(String accountId, String market, ClosePosition body) {
            return close(accountId, market, body, null);
        }

        /** Flatten the account. */
        public FlattenResult flatten(String accountId, RequestOptions options) {
            return c.one(Ops.FLATTEN, new String[] {accountId}, null, null, FlattenResult.class, options);
        }

        /** Flatten the account. */
        public FlattenResult flatten(String accountId) {
            return flatten(accountId, null);
        }
    }

    /** The orders calls (client.orders()). */
    public static final class OrdersResource {
        private final Transport c;

        OrdersResource(Transport c) {
            this.c = c;
        }

        /** List orders. */
        public Page<Order> list(String accountId, OrdersListParams params, RequestOptions options) {
            return c.list(Ops.LIST_ORDERS, new String[] {accountId}, (params == null ? null : params.toQuery()), Order.class, options);
        }

        /** List orders. */
        public Page<Order> list(String accountId, OrdersListParams params) {
            return list(accountId, params, null);
        }

        /** Every item of list, page after page (RequestOptions.maxItems caps it). */
        public Iterable<Order> listAutoPaging(String accountId, OrdersListParams params, RequestOptions options) {
            return c.autoPaging(() -> list(accountId, params, options), options);
        }

        /** Every item of list, page after page. */
        public Iterable<Order> listAutoPaging(String accountId, OrdersListParams params) {
            return listAutoPaging(accountId, params, null);
        }

        /** Place an order. */
        public Order create(String accountId, CreateOrder body, RequestOptions options) {
            return c.one(Ops.PLACE_ORDER, new String[] {accountId}, null, body, Order.class, options);
        }

        /** Place an order. */
        public Order create(String accountId, CreateOrder body) {
            return create(accountId, body, null);
        }

        /** Get an order. */
        public Order get(String accountId, String orderId, RequestOptions options) {
            return c.one(Ops.GET_ORDER, new String[] {accountId, orderId}, null, null, Order.class, options);
        }

        /** Get an order. */
        public Order get(String accountId, String orderId) {
            return get(accountId, orderId, null);
        }

        /** Change an order. */
        public Order modify(String accountId, String orderId, ModifyOrder body, RequestOptions options) {
            return c.one(Ops.MODIFY_ORDER, new String[] {accountId, orderId}, null, body, Order.class, options);
        }

        /** Change an order. */
        public Order modify(String accountId, String orderId, ModifyOrder body) {
            return modify(accountId, orderId, body, null);
        }

        /** Cancel an order. */
        public Order cancel(String accountId, String orderId, RequestOptions options) {
            return c.one(Ops.CANCEL_ORDER, new String[] {accountId, orderId}, null, null, Order.class, options);
        }

        /** Cancel an order. */
        public Order cancel(String accountId, String orderId) {
            return cancel(accountId, orderId, null);
        }

        /** Preview an order. */
        public WhatIf preview(String accountId, CreateOrder body, RequestOptions options) {
            return c.one(Ops.TEST_ORDER, new String[] {accountId}, null, body, WhatIf.class, options);
        }

        /** Preview an order. */
        public WhatIf preview(String accountId, CreateOrder body) {
            return preview(accountId, body, null);
        }

        /** Cancel all orders. */
        public CancelAllResult cancelAll(String accountId, CancelAll body, RequestOptions options) {
            return c.one(Ops.CANCEL_ALL_ORDERS, new String[] {accountId}, null, body, CancelAllResult.class, options);
        }

        /** Cancel all orders. */
        public CancelAllResult cancelAll(String accountId, CancelAll body) {
            return cancelAll(accountId, body, null);
        }

        /** Attach exits to an order. */
        public Order setExits(String accountId, String orderId, OrderExits body, RequestOptions options) {
            return c.one(Ops.SET_ORDER_EXITS, new String[] {accountId, orderId}, null, body, Order.class, options);
        }

        /** Attach exits to an order. */
        public Order setExits(String accountId, String orderId, OrderExits body) {
            return setExits(accountId, orderId, body, null);
        }
    }

    /** The events calls (client.events()). */
    public static final class EventsResource {
        private final Transport c;

        EventsResource(Transport c) {
            this.c = c;
        }

        /** List events. */
        public Page<Event> list(EventsListParams params, RequestOptions options) {
            return c.list(Ops.LIST_EVENTS, null, (params == null ? null : params.toQuery()), Event.class, options);
        }

        /** List events. */
        public Page<Event> list(EventsListParams params) {
            return list(params, null);
        }

        /** Every item of list, page after page (RequestOptions.maxItems caps it). */
        public Iterable<Event> listAutoPaging(EventsListParams params, RequestOptions options) {
            return c.autoPaging(() -> list(params, options), options);
        }

        /** Every item of list, page after page. */
        public Iterable<Event> listAutoPaging(EventsListParams params) {
            return listAutoPaging(params, null);
        }

        /** Get an event. */
        public Event get(String eventId, RequestOptions options) {
            return c.one(Ops.GET_EVENT, new String[] {eventId}, null, null, Event.class, options);
        }

        /** Get an event. */
        public Event get(String eventId) {
            return get(eventId, null);
        }
    }

    /** The exits calls (client.exits()). */
    public static final class ExitsResource {
        private final Transport c;

        ExitsResource(Transport c) {
            this.c = c;
        }

        /** Get a position's exits. */
        public Exits get(String accountId, String market, RequestOptions options) {
            return c.one(Ops.GET_EXITS, new String[] {accountId, market}, null, null, Exits.class, options);
        }

        /** Get a position's exits. */
        public Exits get(String accountId, String market) {
            return get(accountId, market, null);
        }

        /** Change a position's exits. */
        public Exits set(String accountId, String market, ExitsBatch body, RequestOptions options) {
            return c.one(Ops.SET_EXITS, new String[] {accountId, market}, null, body, Exits.class, options);
        }

        /** Change a position's exits. */
        public Exits set(String accountId, String market, ExitsBatch body) {
            return set(accountId, market, body, null);
        }
    }

    /** The strategies calls (client.strategies()). */
    public static final class StrategiesResource {
        private final Transport c;

        StrategiesResource(Transport c) {
            this.c = c;
        }

        /** Create a strategy. */
        public Strategy create(String accountId, CreateStrategy body, RequestOptions options) {
            return c.one(Ops.CREATE_STRATEGY, new String[] {accountId}, null, body, Strategy.class, options);
        }

        /** Create a strategy. */
        public Strategy create(String accountId, CreateStrategy body) {
            return create(accountId, body, null);
        }

        /** List strategies. */
        public Page<Strategy> list(String accountId, StrategiesListParams params, RequestOptions options) {
            return c.list(Ops.LIST_STRATEGIES, new String[] {accountId}, (params == null ? null : params.toQuery()), Strategy.class, options);
        }

        /** List strategies. */
        public Page<Strategy> list(String accountId, StrategiesListParams params) {
            return list(accountId, params, null);
        }

        /** Every item of list, page after page (RequestOptions.maxItems caps it). */
        public Iterable<Strategy> listAutoPaging(String accountId, StrategiesListParams params, RequestOptions options) {
            return c.autoPaging(() -> list(accountId, params, options), options);
        }

        /** Every item of list, page after page. */
        public Iterable<Strategy> listAutoPaging(String accountId, StrategiesListParams params) {
            return listAutoPaging(accountId, params, null);
        }

        /** Get a strategy. */
        public Strategy get(String accountId, String strategyId, RequestOptions options) {
            return c.one(Ops.GET_STRATEGY, new String[] {accountId, strategyId}, null, null, Strategy.class, options);
        }

        /** Get a strategy. */
        public Strategy get(String accountId, String strategyId) {
            return get(accountId, strategyId, null);
        }

        /** Cancel a strategy. */
        public Strategy cancel(String accountId, String strategyId, RequestOptions options) {
            return c.one(Ops.CANCEL_STRATEGY, new String[] {accountId, strategyId}, null, null, Strategy.class, options);
        }

        /** Cancel a strategy. */
        public Strategy cancel(String accountId, String strategyId) {
            return cancel(accountId, strategyId, null);
        }
    }

    /** The webhookEndpoints calls (client.webhookEndpoints()). */
    public static final class WebhookEndpointsResource {
        private final Transport c;

        WebhookEndpointsResource(Transport c) {
            this.c = c;
        }

        /** List webhook endpoints. */
        public Page<WebhookEndpoint> list(RequestOptions options) {
            return c.list(Ops.LIST_WEBHOOK_ENDPOINTS, null, null, WebhookEndpoint.class, options);
        }

        /** List webhook endpoints. */
        public Page<WebhookEndpoint> list() {
            return list(null);
        }

        /** Get a webhook endpoint. */
        public WebhookEndpoint get(String endpointId, RequestOptions options) {
            return c.one(Ops.GET_WEBHOOK_ENDPOINT, new String[] {endpointId}, null, null, WebhookEndpoint.class, options);
        }

        /** Get a webhook endpoint. */
        public WebhookEndpoint get(String endpointId) {
            return get(endpointId, null);
        }

        /** Delete an endpoint. */
        public DeletedEndpoint delete(String endpointId, RequestOptions options) {
            return c.one(Ops.DELETE_WEBHOOK_ENDPOINT, new String[] {endpointId}, null, null, DeletedEndpoint.class, options);
        }

        /** Delete an endpoint. */
        public DeletedEndpoint delete(String endpointId) {
            return delete(endpointId, null);
        }

        /** Send a test. */
        public WebhookDelivery test(String endpointId, RequestOptions options) {
            return c.one(Ops.TEST_WEBHOOK_ENDPOINT, new String[] {endpointId}, null, null, WebhookDelivery.class, options);
        }

        /** Send a test. */
        public WebhookDelivery test(String endpointId) {
            return test(endpointId, null);
        }

        /** List deliveries. */
        public Page<WebhookDelivery> listDeliveries(String endpointId, WebhookEndpointsListDeliveriesParams params, RequestOptions options) {
            return c.list(Ops.LIST_WEBHOOK_DELIVERIES, new String[] {endpointId}, (params == null ? null : params.toQuery()), WebhookDelivery.class, options);
        }

        /** List deliveries. */
        public Page<WebhookDelivery> listDeliveries(String endpointId, WebhookEndpointsListDeliveriesParams params) {
            return listDeliveries(endpointId, params, null);
        }

        /** Every item of listDeliveries, page after page (RequestOptions.maxItems caps it). */
        public Iterable<WebhookDelivery> listDeliveriesAutoPaging(String endpointId, WebhookEndpointsListDeliveriesParams params, RequestOptions options) {
            return c.autoPaging(() -> listDeliveries(endpointId, params, options), options);
        }

        /** Every item of listDeliveries, page after page. */
        public Iterable<WebhookDelivery> listDeliveriesAutoPaging(String endpointId, WebhookEndpointsListDeliveriesParams params) {
            return listDeliveriesAutoPaging(endpointId, params, null);
        }

        /** Get a delivery. */
        public WebhookDelivery getDelivery(String endpointId, String deliveryId, RequestOptions options) {
            return c.one(Ops.GET_WEBHOOK_DELIVERY, new String[] {endpointId, deliveryId}, null, null, WebhookDelivery.class, options);
        }

        /** Get a delivery. */
        public WebhookDelivery getDelivery(String endpointId, String deliveryId) {
            return getDelivery(endpointId, deliveryId, null);
        }

        /** Send a delivery again. */
        public WebhookDelivery resendDelivery(String endpointId, String deliveryId, RequestOptions options) {
            return c.one(Ops.RESEND_WEBHOOK_DELIVERY, new String[] {endpointId, deliveryId}, null, null, WebhookDelivery.class, options);
        }

        /** Send a delivery again. */
        public WebhookDelivery resendDelivery(String endpointId, String deliveryId) {
            return resendDelivery(endpointId, deliveryId, null);
        }

        /** Pause an endpoint. */
        public WebhookEndpoint disable(String endpointId, RequestOptions options) {
            return c.one(Ops.DISABLE_WEBHOOK_ENDPOINT, new String[] {endpointId}, null, null, WebhookEndpoint.class, options);
        }

        /** Pause an endpoint. */
        public WebhookEndpoint disable(String endpointId) {
            return disable(endpointId, null);
        }

        /** Resume an endpoint. */
        public WebhookEndpoint enable(String endpointId, RequestOptions options) {
            return c.one(Ops.ENABLE_WEBHOOK_ENDPOINT, new String[] {endpointId}, null, null, WebhookEndpoint.class, options);
        }

        /** Resume an endpoint. */
        public WebhookEndpoint enable(String endpointId) {
            return enable(endpointId, null);
        }

        /** List event types. */
        public Page<WebhookEventType> eventTypes(RequestOptions options) {
            return c.list(Ops.LIST_WEBHOOK_EVENT_TYPES, null, null, WebhookEventType.class, options);
        }

        /** List event types. */
        public Page<WebhookEventType> eventTypes() {
            return eventTypes(null);
        }
    }

    /** The tradingview calls (client.tradingView()). */
    public static final class TradingViewResource {
        private final Transport c;

        TradingViewResource(Transport c) {
            this.c = c;
        }

        /** List TradingView links. */
        public Page<TradingViewHook> listHooks(RequestOptions options) {
            return c.list(Ops.LIST_TRADING_VIEW_HOOKS, null, null, TradingViewHook.class, options);
        }

        /** List TradingView links. */
        public Page<TradingViewHook> listHooks() {
            return listHooks(null);
        }

        /** Get a TradingView link. */
        public TradingViewHook getHook(String hookId, RequestOptions options) {
            return c.one(Ops.GET_TRADING_VIEW_HOOK, new String[] {hookId}, null, null, TradingViewHook.class, options);
        }

        /** Get a TradingView link. */
        public TradingViewHook getHook(String hookId) {
            return getHook(hookId, null);
        }

        /** Revoke a TradingView link. */
        public DeletedHook deleteHook(String hookId, RequestOptions options) {
            return c.one(Ops.DELETE_TRADING_VIEW_HOOK, new String[] {hookId}, null, null, DeletedHook.class, options);
        }

        /** Revoke a TradingView link. */
        public DeletedHook deleteHook(String hookId) {
            return deleteHook(hookId, null);
        }

        /** List a link's alerts. */
        public Page<TradingViewAlert> listAlerts(String hookId, TradingviewListAlertsParams params, RequestOptions options) {
            return c.list(Ops.LIST_TRADING_VIEW_ALERTS, new String[] {hookId}, (params == null ? null : params.toQuery()), TradingViewAlert.class, options);
        }

        /** List a link's alerts. */
        public Page<TradingViewAlert> listAlerts(String hookId, TradingviewListAlertsParams params) {
            return listAlerts(hookId, params, null);
        }

        /** Every item of listAlerts, page after page (RequestOptions.maxItems caps it). */
        public Iterable<TradingViewAlert> listAlertsAutoPaging(String hookId, TradingviewListAlertsParams params, RequestOptions options) {
            return c.autoPaging(() -> listAlerts(hookId, params, options), options);
        }

        /** Every item of listAlerts, page after page. */
        public Iterable<TradingViewAlert> listAlertsAutoPaging(String hookId, TradingviewListAlertsParams params) {
            return listAlertsAutoPaging(hookId, params, null);
        }

        /** Get an alert. */
        public TradingViewAlert getAlert(String hookId, String alertId, RequestOptions options) {
            return c.one(Ops.GET_TRADING_VIEW_ALERT, new String[] {hookId, alertId}, null, null, TradingViewAlert.class, options);
        }

        /** Get an alert. */
        public TradingViewAlert getAlert(String hookId, String alertId) {
            return getAlert(hookId, alertId, null);
        }

        /** Pause a TradingView link. */
        public TradingViewHook disableHook(String hookId, RequestOptions options) {
            return c.one(Ops.DISABLE_TRADING_VIEW_HOOK, new String[] {hookId}, null, null, TradingViewHook.class, options);
        }

        /** Pause a TradingView link. */
        public TradingViewHook disableHook(String hookId) {
            return disableHook(hookId, null);
        }

        /** Resume a TradingView link. */
        public TradingViewHook enableHook(String hookId, RequestOptions options) {
            return c.one(Ops.ENABLE_TRADING_VIEW_HOOK, new String[] {hookId}, null, null, TradingViewHook.class, options);
        }

        /** Resume a TradingView link. */
        public TradingViewHook enableHook(String hookId) {
            return enableHook(hookId, null);
        }

        /** Read an alert message. */
        public TradingViewParse parse(TradingViewParseInput body, RequestOptions options) {
            return c.one(Ops.PARSE_TRADING_VIEW_MESSAGE, null, null, body, TradingViewParse.class, options);
        }

        /** Read an alert message. */
        public TradingViewParse parse(TradingViewParseInput body) {
            return parse(body, null);
        }
    }

    /** The fills calls (client.fills()). */
    public static final class FillsResource {
        private final Transport c;

        FillsResource(Transport c) {
            this.c = c;
        }

        /** List fills. */
        public Page<Fill> list(String accountId, FillsListParams params, RequestOptions options) {
            return c.list(Ops.LIST_FILLS, new String[] {accountId}, (params == null ? null : params.toQuery()), Fill.class, options);
        }

        /** List fills. */
        public Page<Fill> list(String accountId, FillsListParams params) {
            return list(accountId, params, null);
        }

        /** Every item of list, page after page (RequestOptions.maxItems caps it). */
        public Iterable<Fill> listAutoPaging(String accountId, FillsListParams params, RequestOptions options) {
            return c.autoPaging(() -> list(accountId, params, options), options);
        }

        /** Every item of list, page after page. */
        public Iterable<Fill> listAutoPaging(String accountId, FillsListParams params) {
            return listAutoPaging(accountId, params, null);
        }
    }
}
