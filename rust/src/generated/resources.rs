// Generated from the API spec by generator/printers/rust.js. Do not edit: run `node generator`.

use super::types::*;
use crate::client::{auto_paging, Client};
use crate::{Page, RequestOptions, Result};
use futures_util::Stream;
use serde_json::Value;

pub(crate) mod ops {
    use crate::client::Operation;

    pub(crate) const GET_API: Operation = Operation { id: "getApi", method: "GET", path: "/v1", idempotent: false, retry_safe: true, cursor: None, scope: "public" };
    pub(crate) const GET_SPEC: Operation = Operation { id: "getSpec", method: "GET", path: "/v1/openapi.json", idempotent: false, retry_safe: true, cursor: None, scope: "public" };
    pub(crate) const GET_STATUS: Operation = Operation { id: "getStatus", method: "GET", path: "/v1/status", idempotent: false, retry_safe: true, cursor: None, scope: "public" };
    pub(crate) const GET_LIMITS: Operation = Operation { id: "getLimits", method: "GET", path: "/v1/limits", idempotent: false, retry_safe: true, cursor: None, scope: "public" };
    pub(crate) const GET_ME: Operation = Operation { id: "getMe", method: "GET", path: "/v1/me", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const LIST_MARKETS: Operation = Operation { id: "listMarkets", method: "GET", path: "/v1/markets", idempotent: false, retry_safe: true, cursor: None, scope: "public" };
    pub(crate) const LIST_MARKET_STATS: Operation = Operation { id: "listMarketStats", method: "GET", path: "/v1/markets/stats", idempotent: false, retry_safe: true, cursor: None, scope: "public" };
    pub(crate) const GET_MARKET: Operation = Operation { id: "getMarket", method: "GET", path: "/v1/markets/{market}", idempotent: false, retry_safe: true, cursor: None, scope: "public" };
    pub(crate) const LIST_ACCOUNTS: Operation = Operation { id: "listAccounts", method: "GET", path: "/v1/accounts", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const CREATE_SANDBOX_ACCOUNT: Operation = Operation { id: "createSandboxAccount", method: "POST", path: "/v1/accounts", idempotent: true, retry_safe: false, cursor: None, scope: "trade" };
    pub(crate) const GET_ACCOUNT: Operation = Operation { id: "getAccount", method: "GET", path: "/v1/accounts/{account_id}", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const GET_RISK: Operation = Operation { id: "getRisk", method: "GET", path: "/v1/accounts/{account_id}/risk", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const GET_TRADING_POLICY: Operation = Operation { id: "getTradingPolicy", method: "GET", path: "/v1/accounts/{account_id}/trading-policy", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const GET_PROGRESS: Operation = Operation { id: "getProgress", method: "GET", path: "/v1/accounts/{account_id}/progress", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const GET_EQUITY_HISTORY: Operation = Operation { id: "getEquityHistory", method: "GET", path: "/v1/accounts/{account_id}/history/equity", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const LIST_DAILY_RESULTS: Operation = Operation { id: "listDailyResults", method: "GET", path: "/v1/accounts/{account_id}/history/daily", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const LIST_MARGIN_SETTINGS: Operation = Operation { id: "listMarginSettings", method: "GET", path: "/v1/accounts/{account_id}/margin", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const LIST_POSITIONS: Operation = Operation { id: "listPositions", method: "GET", path: "/v1/accounts/{account_id}/positions", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const GET_POSITION: Operation = Operation { id: "getPosition", method: "GET", path: "/v1/accounts/{account_id}/positions/{market}", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const LIST_CLOSED_POSITIONS: Operation = Operation { id: "listClosedPositions", method: "GET", path: "/v1/accounts/{account_id}/closed-positions", idempotent: false, retry_safe: true, cursor: Some("cursor"), scope: "read" };
    pub(crate) const LIST_ORDERS: Operation = Operation { id: "listOrders", method: "GET", path: "/v1/accounts/{account_id}/orders", idempotent: false, retry_safe: true, cursor: Some("cursor"), scope: "read" };
    pub(crate) const PLACE_ORDER: Operation = Operation { id: "placeOrder", method: "POST", path: "/v1/accounts/{account_id}/orders", idempotent: true, retry_safe: false, cursor: None, scope: "trade" };
    pub(crate) const GET_ORDER: Operation = Operation { id: "getOrder", method: "GET", path: "/v1/accounts/{account_id}/orders/{order_id}", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const MODIFY_ORDER: Operation = Operation { id: "modifyOrder", method: "PATCH", path: "/v1/accounts/{account_id}/orders/{order_id}", idempotent: false, retry_safe: false, cursor: None, scope: "trade" };
    pub(crate) const CANCEL_ORDER: Operation = Operation { id: "cancelOrder", method: "DELETE", path: "/v1/accounts/{account_id}/orders/{order_id}", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const TEST_ORDER: Operation = Operation { id: "testOrder", method: "POST", path: "/v1/accounts/{account_id}/orders/test", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const CANCEL_ALL_ORDERS: Operation = Operation { id: "cancelAllOrders", method: "POST", path: "/v1/accounts/{account_id}/orders/cancel-all", idempotent: true, retry_safe: false, cursor: None, scope: "trade" };
    pub(crate) const CLOSE_POSITION: Operation = Operation { id: "closePosition", method: "POST", path: "/v1/accounts/{account_id}/positions/{market}/close", idempotent: true, retry_safe: false, cursor: None, scope: "trade" };
    pub(crate) const FLATTEN: Operation = Operation { id: "flatten", method: "POST", path: "/v1/accounts/{account_id}/flatten", idempotent: true, retry_safe: false, cursor: None, scope: "trade" };
    pub(crate) const SET_MARGIN: Operation = Operation { id: "setMargin", method: "PUT", path: "/v1/accounts/{account_id}/margin/{market}", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const LIST_EVENTS: Operation = Operation { id: "listEvents", method: "GET", path: "/v1/events", idempotent: false, retry_safe: true, cursor: Some("after"), scope: "read" };
    pub(crate) const GET_EXITS: Operation = Operation { id: "getExits", method: "GET", path: "/v1/accounts/{account_id}/positions/{market}/exits", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const SET_EXITS: Operation = Operation { id: "setExits", method: "PUT", path: "/v1/accounts/{account_id}/positions/{market}/exits", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const SET_ORDER_EXITS: Operation = Operation { id: "setOrderExits", method: "PUT", path: "/v1/accounts/{account_id}/orders/{order_id}/exits", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const CREATE_STRATEGY: Operation = Operation { id: "createStrategy", method: "POST", path: "/v1/accounts/{account_id}/strategies", idempotent: true, retry_safe: false, cursor: None, scope: "trade" };
    pub(crate) const LIST_STRATEGIES: Operation = Operation { id: "listStrategies", method: "GET", path: "/v1/accounts/{account_id}/strategies", idempotent: false, retry_safe: true, cursor: Some("cursor"), scope: "read" };
    pub(crate) const GET_STRATEGY: Operation = Operation { id: "getStrategy", method: "GET", path: "/v1/accounts/{account_id}/strategies/{strategy_id}", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const CANCEL_STRATEGY: Operation = Operation { id: "cancelStrategy", method: "DELETE", path: "/v1/accounts/{account_id}/strategies/{strategy_id}", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const LIST_WEBHOOK_ENDPOINTS: Operation = Operation { id: "listWebhookEndpoints", method: "GET", path: "/v1/webhook-endpoints", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const GET_WEBHOOK_ENDPOINT: Operation = Operation { id: "getWebhookEndpoint", method: "GET", path: "/v1/webhook-endpoints/{endpoint_id}", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const DELETE_WEBHOOK_ENDPOINT: Operation = Operation { id: "deleteWebhookEndpoint", method: "DELETE", path: "/v1/webhook-endpoints/{endpoint_id}", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const TEST_WEBHOOK_ENDPOINT: Operation = Operation { id: "testWebhookEndpoint", method: "POST", path: "/v1/webhook-endpoints/{endpoint_id}/test", idempotent: false, retry_safe: false, cursor: None, scope: "trade" };
    pub(crate) const LIST_WEBHOOK_DELIVERIES: Operation = Operation { id: "listWebhookDeliveries", method: "GET", path: "/v1/webhook-endpoints/{endpoint_id}/deliveries", idempotent: false, retry_safe: true, cursor: Some("cursor"), scope: "read" };
    pub(crate) const GET_WEBHOOK_DELIVERY: Operation = Operation { id: "getWebhookDelivery", method: "GET", path: "/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const RESEND_WEBHOOK_DELIVERY: Operation = Operation { id: "resendWebhookDelivery", method: "POST", path: "/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}/resend", idempotent: false, retry_safe: false, cursor: None, scope: "trade" };
    pub(crate) const DISABLE_WEBHOOK_ENDPOINT: Operation = Operation { id: "disableWebhookEndpoint", method: "POST", path: "/v1/webhook-endpoints/{endpoint_id}/disable", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const ENABLE_WEBHOOK_ENDPOINT: Operation = Operation { id: "enableWebhookEndpoint", method: "POST", path: "/v1/webhook-endpoints/{endpoint_id}/enable", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const LIST_TRADING_VIEW_HOOKS: Operation = Operation { id: "listTradingViewHooks", method: "GET", path: "/v1/tradingview/hooks", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const GET_TRADING_VIEW_HOOK: Operation = Operation { id: "getTradingViewHook", method: "GET", path: "/v1/tradingview/hooks/{hook_id}", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const DELETE_TRADING_VIEW_HOOK: Operation = Operation { id: "deleteTradingViewHook", method: "DELETE", path: "/v1/tradingview/hooks/{hook_id}", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const LIST_TRADING_VIEW_ALERTS: Operation = Operation { id: "listTradingViewAlerts", method: "GET", path: "/v1/tradingview/hooks/{hook_id}/alerts", idempotent: false, retry_safe: true, cursor: Some("cursor"), scope: "read" };
    pub(crate) const GET_TRADING_VIEW_ALERT: Operation = Operation { id: "getTradingViewAlert", method: "GET", path: "/v1/tradingview/hooks/{hook_id}/alerts/{alert_id}", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const DISABLE_TRADING_VIEW_HOOK: Operation = Operation { id: "disableTradingViewHook", method: "POST", path: "/v1/tradingview/hooks/{hook_id}/disable", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const ENABLE_TRADING_VIEW_HOOK: Operation = Operation { id: "enableTradingViewHook", method: "POST", path: "/v1/tradingview/hooks/{hook_id}/enable", idempotent: false, retry_safe: true, cursor: None, scope: "trade" };
    pub(crate) const PARSE_TRADING_VIEW_MESSAGE: Operation = Operation { id: "parseTradingViewMessage", method: "POST", path: "/v1/tradingview/parse", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const LIST_WEBHOOK_EVENT_TYPES: Operation = Operation { id: "listWebhookEventTypes", method: "GET", path: "/v1/webhook-event-types", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const GET_EVENT: Operation = Operation { id: "getEvent", method: "GET", path: "/v1/events/{event_id}", idempotent: false, retry_safe: true, cursor: None, scope: "read" };
    pub(crate) const LIST_FILLS: Operation = Operation { id: "listFills", method: "GET", path: "/v1/accounts/{account_id}/fills", idempotent: false, retry_safe: true, cursor: Some("cursor"), scope: "read" };

    /// Every operation, for the raw request (its route's retry-safety and budget).
    pub(crate) const ALL: &[&Operation] = &[&GET_API, &GET_SPEC, &GET_STATUS, &GET_LIMITS, &GET_ME, &LIST_MARKETS, &LIST_MARKET_STATS, &GET_MARKET, &LIST_ACCOUNTS, &CREATE_SANDBOX_ACCOUNT, &GET_ACCOUNT, &GET_RISK, &GET_TRADING_POLICY, &GET_PROGRESS, &GET_EQUITY_HISTORY, &LIST_DAILY_RESULTS, &LIST_MARGIN_SETTINGS, &LIST_POSITIONS, &GET_POSITION, &LIST_CLOSED_POSITIONS, &LIST_ORDERS, &PLACE_ORDER, &GET_ORDER, &MODIFY_ORDER, &CANCEL_ORDER, &TEST_ORDER, &CANCEL_ALL_ORDERS, &CLOSE_POSITION, &FLATTEN, &SET_MARGIN, &LIST_EVENTS, &GET_EXITS, &SET_EXITS, &SET_ORDER_EXITS, &CREATE_STRATEGY, &LIST_STRATEGIES, &GET_STRATEGY, &CANCEL_STRATEGY, &LIST_WEBHOOK_ENDPOINTS, &GET_WEBHOOK_ENDPOINT, &DELETE_WEBHOOK_ENDPOINT, &TEST_WEBHOOK_ENDPOINT, &LIST_WEBHOOK_DELIVERIES, &GET_WEBHOOK_DELIVERY, &RESEND_WEBHOOK_DELIVERY, &DISABLE_WEBHOOK_ENDPOINT, &ENABLE_WEBHOOK_ENDPOINT, &LIST_TRADING_VIEW_HOOKS, &GET_TRADING_VIEW_HOOK, &DELETE_TRADING_VIEW_HOOK, &LIST_TRADING_VIEW_ALERTS, &GET_TRADING_VIEW_ALERT, &DISABLE_TRADING_VIEW_HOOK, &ENABLE_TRADING_VIEW_HOOK, &PARSE_TRADING_VIEW_MESSAGE, &LIST_WEBHOOK_EVENT_TYPES, &GET_EVENT, &LIST_FILLS];
}

impl Client {
    /// The platform calls.
    pub fn platform(&self) -> PlatformResource<'_> {
        PlatformResource { c: self }
    }

    /// The me calls.
    pub fn me(&self) -> MeResource<'_> {
        MeResource { c: self }
    }

    /// The markets calls.
    pub fn markets(&self) -> MarketsResource<'_> {
        MarketsResource { c: self }
    }

    /// The accounts calls.
    pub fn accounts(&self) -> AccountsResource<'_> {
        AccountsResource { c: self }
    }

    /// The history calls.
    pub fn history(&self) -> HistoryResource<'_> {
        HistoryResource { c: self }
    }

    /// The margin calls.
    pub fn margin(&self) -> MarginResource<'_> {
        MarginResource { c: self }
    }

    /// The positions calls.
    pub fn positions(&self) -> PositionsResource<'_> {
        PositionsResource { c: self }
    }

    /// The orders calls.
    pub fn orders(&self) -> OrdersResource<'_> {
        OrdersResource { c: self }
    }

    /// The events calls.
    pub fn events(&self) -> EventsResource<'_> {
        EventsResource { c: self }
    }

    /// The exits calls.
    pub fn exits(&self) -> ExitsResource<'_> {
        ExitsResource { c: self }
    }

    /// The strategies calls.
    pub fn strategies(&self) -> StrategiesResource<'_> {
        StrategiesResource { c: self }
    }

    /// The webhookEndpoints calls.
    pub fn webhook_endpoints(&self) -> WebhookEndpointsResource<'_> {
        WebhookEndpointsResource { c: self }
    }

    /// The tradingview calls.
    pub fn tradingview(&self) -> TradingViewResource<'_> {
        TradingViewResource { c: self }
    }

    /// The fills calls.
    pub fn fills(&self) -> FillsResource<'_> {
        FillsResource { c: self }
    }
}

/// The platform calls (`client.platform()`).
#[derive(Clone, Copy)]
pub struct PlatformResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> PlatformResource<'a> {
    /// Get the API.
    pub async fn info(self) -> Result<Platform> {
        self.c.one(&ops::GET_API, &[], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get the API, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn info_with(self, options: &RequestOptions) -> Result<Option<Platform>> {
        self.c.one(&ops::GET_API, &[], None, None::<&()>, options).await
    }

    /// Get the OpenAPI document.
    pub async fn spec(self) -> Result<Value> {
        self.c.one(&ops::GET_SPEC, &[], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get the OpenAPI document, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn spec_with(self, options: &RequestOptions) -> Result<Option<Value>> {
        self.c.one(&ops::GET_SPEC, &[], None, None::<&()>, options).await
    }

    /// Get the status.
    pub async fn status(self) -> Result<Status> {
        self.c.one(&ops::GET_STATUS, &[], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get the status, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn status_with(self, options: &RequestOptions) -> Result<Option<Status>> {
        self.c.one(&ops::GET_STATUS, &[], None, None::<&()>, options).await
    }

    /// Get the limits.
    pub async fn limits(self) -> Result<Limits> {
        self.c.one(&ops::GET_LIMITS, &[], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get the limits, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn limits_with(self, options: &RequestOptions) -> Result<Option<Limits>> {
        self.c.one(&ops::GET_LIMITS, &[], None, None::<&()>, options).await
    }
}

/// The me calls (`client.me()`).
#[derive(Clone, Copy)]
pub struct MeResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> MeResource<'a> {
    /// Get your key.
    pub async fn get(self) -> Result<Me> {
        self.c.one(&ops::GET_ME, &[], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get your key, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_with(self, options: &RequestOptions) -> Result<Option<Me>> {
        self.c.one(&ops::GET_ME, &[], None, None::<&()>, options).await
    }
}

/// The markets calls (`client.markets()`).
#[derive(Clone, Copy)]
pub struct MarketsResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> MarketsResource<'a> {
    /// List markets.
    pub async fn list(self) -> Result<Page<Market>> {
        self.c.list(&ops::LIST_MARKETS, &[], None, &RequestOptions::default()).await
    }

    /// List markets, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_with(self, options: &RequestOptions) -> Result<Option<Page<Market>>> {
        self.c.list_or_none(&ops::LIST_MARKETS, &[], None, options).await
    }

    /// List market stats.
    pub async fn stats(self) -> Result<Page<MarketStats>> {
        self.c.list(&ops::LIST_MARKET_STATS, &[], None, &RequestOptions::default()).await
    }

    /// List market stats, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn stats_with(self, options: &RequestOptions) -> Result<Option<Page<MarketStats>>> {
        self.c.list_or_none(&ops::LIST_MARKET_STATS, &[], None, options).await
    }

    /// Get a market.
    pub async fn get(self, market: &str) -> Result<Market> {
        self.c.one(&ops::GET_MARKET, &[market], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get a market, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_with(self, market: &str, options: &RequestOptions) -> Result<Option<Market>> {
        self.c.one(&ops::GET_MARKET, &[market], None, None::<&()>, options).await
    }
}

/// The accounts calls (`client.accounts()`).
#[derive(Clone, Copy)]
pub struct AccountsResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> AccountsResource<'a> {
    /// List accounts.
    pub async fn list(self) -> Result<Page<Account>> {
        self.c.list(&ops::LIST_ACCOUNTS, &[], None, &RequestOptions::default()).await
    }

    /// List accounts, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_with(self, options: &RequestOptions) -> Result<Option<Page<Account>>> {
        self.c.list_or_none(&ops::LIST_ACCOUNTS, &[], None, options).await
    }

    /// Open a sandbox account.
    pub async fn create_sandbox(self) -> Result<Account> {
        self.create_sandbox_with(&RequestOptions::default()).await
    }

    /// Open a sandbox account, with options.
    pub async fn create_sandbox_with(self, options: &RequestOptions) -> Result<Account> {
        self.c.one(&ops::CREATE_SANDBOX_ACCOUNT, &[], None, None::<&()>, options).await
    }

    /// Get an account.
    pub async fn get(self, account_id: &str) -> Result<AccountDetail> {
        self.c.one(&ops::GET_ACCOUNT, &[account_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get an account, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_with(self, account_id: &str, options: &RequestOptions) -> Result<Option<AccountDetail>> {
        self.c.one(&ops::GET_ACCOUNT, &[account_id], None, None::<&()>, options).await
    }

    /// Get live risk.
    pub async fn risk(self, account_id: &str) -> Result<AccountRisk> {
        self.c.one(&ops::GET_RISK, &[account_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get live risk, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn risk_with(self, account_id: &str, options: &RequestOptions) -> Result<Option<AccountRisk>> {
        self.c.one(&ops::GET_RISK, &[account_id], None, None::<&()>, options).await
    }

    /// Get the trading policy.
    pub async fn trading_policy(self, account_id: &str) -> Result<TradingPolicy> {
        self.c.one(&ops::GET_TRADING_POLICY, &[account_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get the trading policy, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn trading_policy_with(self, account_id: &str, options: &RequestOptions) -> Result<Option<TradingPolicy>> {
        self.c.one(&ops::GET_TRADING_POLICY, &[account_id], None, None::<&()>, options).await
    }

    /// Get progress.
    pub async fn progress(self, account_id: &str) -> Result<Progress> {
        self.c.one(&ops::GET_PROGRESS, &[account_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get progress, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn progress_with(self, account_id: &str, options: &RequestOptions) -> Result<Option<Progress>> {
        self.c.one(&ops::GET_PROGRESS, &[account_id], None, None::<&()>, options).await
    }
}

/// The history calls (`client.history()`).
#[derive(Clone, Copy)]
pub struct HistoryResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> HistoryResource<'a> {
    /// Get the equity curve.
    pub async fn equity(self, account_id: &str, params: Option<&HistoryEquityParams>) -> Result<EquityHistory> {
        self.c.one(&ops::GET_EQUITY_HISTORY, &[account_id], params.map(|p| p.to_query()), None::<&()>, &RequestOptions::default()).await
    }

    /// Get the equity curve, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn equity_with(self, account_id: &str, params: Option<&HistoryEquityParams>, options: &RequestOptions) -> Result<Option<EquityHistory>> {
        self.c.one(&ops::GET_EQUITY_HISTORY, &[account_id], params.map(|p| p.to_query()), None::<&()>, options).await
    }

    /// List daily results.
    pub async fn daily(self, account_id: &str) -> Result<Page<DailyResult>> {
        self.c.list(&ops::LIST_DAILY_RESULTS, &[account_id], None, &RequestOptions::default()).await
    }

    /// List daily results, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn daily_with(self, account_id: &str, options: &RequestOptions) -> Result<Option<Page<DailyResult>>> {
        self.c.list_or_none(&ops::LIST_DAILY_RESULTS, &[account_id], None, options).await
    }
}

/// The margin calls (`client.margin()`).
#[derive(Clone, Copy)]
pub struct MarginResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> MarginResource<'a> {
    /// List margin settings.
    pub async fn list(self, account_id: &str) -> Result<Page<MarginSetting>> {
        self.c.list(&ops::LIST_MARGIN_SETTINGS, &[account_id], None, &RequestOptions::default()).await
    }

    /// List margin settings, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_with(self, account_id: &str, options: &RequestOptions) -> Result<Option<Page<MarginSetting>>> {
        self.c.list_or_none(&ops::LIST_MARGIN_SETTINGS, &[account_id], None, options).await
    }

    /// Set leverage and margin mode.
    pub async fn set(self, account_id: &str, market: &str, body: &MarginSettingInput) -> Result<MarginSettingResult> {
        self.set_with(account_id, market, body, &RequestOptions::default()).await
    }

    /// Set leverage and margin mode, with options.
    pub async fn set_with(self, account_id: &str, market: &str, body: &MarginSettingInput, options: &RequestOptions) -> Result<MarginSettingResult> {
        self.c.one(&ops::SET_MARGIN, &[account_id, market], None, Some(body), options).await
    }
}

/// The positions calls (`client.positions()`).
#[derive(Clone, Copy)]
pub struct PositionsResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> PositionsResource<'a> {
    /// List open positions.
    pub async fn list(self, account_id: &str, params: Option<&PositionsListParams>) -> Result<Page<Position>> {
        self.c.list(&ops::LIST_POSITIONS, &[account_id], params.map(|p| p.to_query()), &RequestOptions::default()).await
    }

    /// List open positions, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_with(self, account_id: &str, params: Option<&PositionsListParams>, options: &RequestOptions) -> Result<Option<Page<Position>>> {
        self.c.list_or_none(&ops::LIST_POSITIONS, &[account_id], params.map(|p| p.to_query()), options).await
    }

    /// Get an open position.
    pub async fn get(self, account_id: &str, market: &str) -> Result<Position> {
        self.c.one(&ops::GET_POSITION, &[account_id, market], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get an open position, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_with(self, account_id: &str, market: &str, options: &RequestOptions) -> Result<Option<Position>> {
        self.c.one(&ops::GET_POSITION, &[account_id, market], None, None::<&()>, options).await
    }

    /// List closed positions.
    pub async fn list_closed(self, account_id: &str, params: Option<&PositionsListClosedParams>) -> Result<Page<ClosedPosition>> {
        self.c.list(&ops::LIST_CLOSED_POSITIONS, &[account_id], params.map(|p| p.to_query()), &RequestOptions::default()).await
    }

    /// List closed positions, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_closed_with(self, account_id: &str, params: Option<&PositionsListClosedParams>, options: &RequestOptions) -> Result<Option<Page<ClosedPosition>>> {
        self.c.list_or_none(&ops::LIST_CLOSED_POSITIONS, &[account_id], params.map(|p| p.to_query()), options).await
    }

    /// Every item of list_closed, page after page (RequestOptions::max_items caps it).
    pub fn list_closed_auto_paging<'s>(self, account_id: &'s str, params: Option<&'s PositionsListClosedParams>, options: &'s RequestOptions) -> impl Stream<Item = Result<ClosedPosition>> + 's
    where
        'a: 's,
    {
        let c = self.c;
        auto_paging(async move { c.list(&ops::LIST_CLOSED_POSITIONS, &[account_id], params.map(|p| p.to_query()), options).await }, options)
    }

    /// Close a position.
    pub async fn close(self, account_id: &str, market: &str, body: &ClosePosition) -> Result<Order> {
        self.close_with(account_id, market, body, &RequestOptions::default()).await
    }

    /// Close a position, with options.
    pub async fn close_with(self, account_id: &str, market: &str, body: &ClosePosition, options: &RequestOptions) -> Result<Order> {
        self.c.one(&ops::CLOSE_POSITION, &[account_id, market], None, Some(body), options).await
    }

    /// Flatten the account.
    pub async fn flatten(self, account_id: &str) -> Result<FlattenResult> {
        self.flatten_with(account_id, &RequestOptions::default()).await
    }

    /// Flatten the account, with options.
    pub async fn flatten_with(self, account_id: &str, options: &RequestOptions) -> Result<FlattenResult> {
        self.c.one(&ops::FLATTEN, &[account_id], None, None::<&()>, options).await
    }
}

/// The orders calls (`client.orders()`).
#[derive(Clone, Copy)]
pub struct OrdersResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> OrdersResource<'a> {
    /// List orders.
    pub async fn list(self, account_id: &str, params: Option<&OrdersListParams>) -> Result<Page<Order>> {
        self.c.list(&ops::LIST_ORDERS, &[account_id], params.map(|p| p.to_query()), &RequestOptions::default()).await
    }

    /// List orders, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_with(self, account_id: &str, params: Option<&OrdersListParams>, options: &RequestOptions) -> Result<Option<Page<Order>>> {
        self.c.list_or_none(&ops::LIST_ORDERS, &[account_id], params.map(|p| p.to_query()), options).await
    }

    /// Every item of list, page after page (RequestOptions::max_items caps it).
    pub fn list_auto_paging<'s>(self, account_id: &'s str, params: Option<&'s OrdersListParams>, options: &'s RequestOptions) -> impl Stream<Item = Result<Order>> + 's
    where
        'a: 's,
    {
        let c = self.c;
        auto_paging(async move { c.list(&ops::LIST_ORDERS, &[account_id], params.map(|p| p.to_query()), options).await }, options)
    }

    /// Place an order.
    pub async fn create(self, account_id: &str, body: &CreateOrder) -> Result<Order> {
        self.create_with(account_id, body, &RequestOptions::default()).await
    }

    /// Place an order, with options.
    pub async fn create_with(self, account_id: &str, body: &CreateOrder, options: &RequestOptions) -> Result<Order> {
        self.c.one(&ops::PLACE_ORDER, &[account_id], None, Some(body), options).await
    }

    /// Get an order.
    pub async fn get(self, account_id: &str, order_id: &str) -> Result<Order> {
        self.c.one(&ops::GET_ORDER, &[account_id, order_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get an order, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_with(self, account_id: &str, order_id: &str, options: &RequestOptions) -> Result<Option<Order>> {
        self.c.one(&ops::GET_ORDER, &[account_id, order_id], None, None::<&()>, options).await
    }

    /// Change an order.
    pub async fn modify(self, account_id: &str, order_id: &str, body: &ModifyOrder) -> Result<Order> {
        self.modify_with(account_id, order_id, body, &RequestOptions::default()).await
    }

    /// Change an order, with options.
    pub async fn modify_with(self, account_id: &str, order_id: &str, body: &ModifyOrder, options: &RequestOptions) -> Result<Order> {
        self.c.one(&ops::MODIFY_ORDER, &[account_id, order_id], None, Some(body), options).await
    }

    /// Cancel an order.
    pub async fn cancel(self, account_id: &str, order_id: &str) -> Result<Order> {
        self.cancel_with(account_id, order_id, &RequestOptions::default()).await
    }

    /// Cancel an order, with options.
    pub async fn cancel_with(self, account_id: &str, order_id: &str, options: &RequestOptions) -> Result<Order> {
        self.c.one(&ops::CANCEL_ORDER, &[account_id, order_id], None, None::<&()>, options).await
    }

    /// Preview an order.
    pub async fn preview(self, account_id: &str, body: &CreateOrder) -> Result<WhatIf> {
        self.preview_with(account_id, body, &RequestOptions::default()).await
    }

    /// Preview an order, with options.
    pub async fn preview_with(self, account_id: &str, body: &CreateOrder, options: &RequestOptions) -> Result<WhatIf> {
        self.c.one(&ops::TEST_ORDER, &[account_id], None, Some(body), options).await
    }

    /// Cancel all orders.
    pub async fn cancel_all(self, account_id: &str, body: &CancelAll) -> Result<CancelAllResult> {
        self.cancel_all_with(account_id, body, &RequestOptions::default()).await
    }

    /// Cancel all orders, with options.
    pub async fn cancel_all_with(self, account_id: &str, body: &CancelAll, options: &RequestOptions) -> Result<CancelAllResult> {
        self.c.one(&ops::CANCEL_ALL_ORDERS, &[account_id], None, Some(body), options).await
    }

    /// Attach exits to an order.
    pub async fn set_exits(self, account_id: &str, order_id: &str, body: &OrderExits) -> Result<Order> {
        self.set_exits_with(account_id, order_id, body, &RequestOptions::default()).await
    }

    /// Attach exits to an order, with options.
    pub async fn set_exits_with(self, account_id: &str, order_id: &str, body: &OrderExits, options: &RequestOptions) -> Result<Order> {
        self.c.one(&ops::SET_ORDER_EXITS, &[account_id, order_id], None, Some(body), options).await
    }
}

/// The events calls (`client.events()`).
#[derive(Clone, Copy)]
pub struct EventsResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> EventsResource<'a> {
    /// List events.
    pub async fn list(self, params: Option<&EventsListParams>) -> Result<Page<Event>> {
        self.c.list(&ops::LIST_EVENTS, &[], params.map(|p| p.to_query()), &RequestOptions::default()).await
    }

    /// List events, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_with(self, params: Option<&EventsListParams>, options: &RequestOptions) -> Result<Option<Page<Event>>> {
        self.c.list_or_none(&ops::LIST_EVENTS, &[], params.map(|p| p.to_query()), options).await
    }

    /// Every item of list, page after page (RequestOptions::max_items caps it).
    pub fn list_auto_paging<'s>(self, params: Option<&'s EventsListParams>, options: &'s RequestOptions) -> impl Stream<Item = Result<Event>> + 's
    where
        'a: 's,
    {
        let c = self.c;
        auto_paging(async move { c.list(&ops::LIST_EVENTS, &[], params.map(|p| p.to_query()), options).await }, options)
    }

    /// Get an event.
    pub async fn get(self, event_id: &str) -> Result<Event> {
        self.c.one(&ops::GET_EVENT, &[event_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get an event, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_with(self, event_id: &str, options: &RequestOptions) -> Result<Option<Event>> {
        self.c.one(&ops::GET_EVENT, &[event_id], None, None::<&()>, options).await
    }
}

/// The exits calls (`client.exits()`).
#[derive(Clone, Copy)]
pub struct ExitsResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> ExitsResource<'a> {
    /// Get a position's exits.
    pub async fn get(self, account_id: &str, market: &str) -> Result<Exits> {
        self.c.one(&ops::GET_EXITS, &[account_id, market], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get a position's exits, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_with(self, account_id: &str, market: &str, options: &RequestOptions) -> Result<Option<Exits>> {
        self.c.one(&ops::GET_EXITS, &[account_id, market], None, None::<&()>, options).await
    }

    /// Change a position's exits.
    pub async fn set(self, account_id: &str, market: &str, body: &ExitsBatch) -> Result<Exits> {
        self.set_with(account_id, market, body, &RequestOptions::default()).await
    }

    /// Change a position's exits, with options.
    pub async fn set_with(self, account_id: &str, market: &str, body: &ExitsBatch, options: &RequestOptions) -> Result<Exits> {
        self.c.one(&ops::SET_EXITS, &[account_id, market], None, Some(body), options).await
    }
}

/// The strategies calls (`client.strategies()`).
#[derive(Clone, Copy)]
pub struct StrategiesResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> StrategiesResource<'a> {
    /// Create a strategy.
    pub async fn create(self, account_id: &str, body: &CreateStrategy) -> Result<Strategy> {
        self.create_with(account_id, body, &RequestOptions::default()).await
    }

    /// Create a strategy, with options.
    pub async fn create_with(self, account_id: &str, body: &CreateStrategy, options: &RequestOptions) -> Result<Strategy> {
        self.c.one(&ops::CREATE_STRATEGY, &[account_id], None, Some(body), options).await
    }

    /// List strategies.
    pub async fn list(self, account_id: &str, params: Option<&StrategiesListParams>) -> Result<Page<Strategy>> {
        self.c.list(&ops::LIST_STRATEGIES, &[account_id], params.map(|p| p.to_query()), &RequestOptions::default()).await
    }

    /// List strategies, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_with(self, account_id: &str, params: Option<&StrategiesListParams>, options: &RequestOptions) -> Result<Option<Page<Strategy>>> {
        self.c.list_or_none(&ops::LIST_STRATEGIES, &[account_id], params.map(|p| p.to_query()), options).await
    }

    /// Every item of list, page after page (RequestOptions::max_items caps it).
    pub fn list_auto_paging<'s>(self, account_id: &'s str, params: Option<&'s StrategiesListParams>, options: &'s RequestOptions) -> impl Stream<Item = Result<Strategy>> + 's
    where
        'a: 's,
    {
        let c = self.c;
        auto_paging(async move { c.list(&ops::LIST_STRATEGIES, &[account_id], params.map(|p| p.to_query()), options).await }, options)
    }

    /// Get a strategy.
    pub async fn get(self, account_id: &str, strategy_id: &str) -> Result<Strategy> {
        self.c.one(&ops::GET_STRATEGY, &[account_id, strategy_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get a strategy, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_with(self, account_id: &str, strategy_id: &str, options: &RequestOptions) -> Result<Option<Strategy>> {
        self.c.one(&ops::GET_STRATEGY, &[account_id, strategy_id], None, None::<&()>, options).await
    }

    /// Cancel a strategy.
    pub async fn cancel(self, account_id: &str, strategy_id: &str) -> Result<Strategy> {
        self.cancel_with(account_id, strategy_id, &RequestOptions::default()).await
    }

    /// Cancel a strategy, with options.
    pub async fn cancel_with(self, account_id: &str, strategy_id: &str, options: &RequestOptions) -> Result<Strategy> {
        self.c.one(&ops::CANCEL_STRATEGY, &[account_id, strategy_id], None, None::<&()>, options).await
    }
}

/// The webhookEndpoints calls (`client.webhook_endpoints()`).
#[derive(Clone, Copy)]
pub struct WebhookEndpointsResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> WebhookEndpointsResource<'a> {
    /// List webhook endpoints.
    pub async fn list(self) -> Result<Page<WebhookEndpoint>> {
        self.c.list(&ops::LIST_WEBHOOK_ENDPOINTS, &[], None, &RequestOptions::default()).await
    }

    /// List webhook endpoints, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_with(self, options: &RequestOptions) -> Result<Option<Page<WebhookEndpoint>>> {
        self.c.list_or_none(&ops::LIST_WEBHOOK_ENDPOINTS, &[], None, options).await
    }

    /// Get a webhook endpoint.
    pub async fn get(self, endpoint_id: &str) -> Result<WebhookEndpoint> {
        self.c.one(&ops::GET_WEBHOOK_ENDPOINT, &[endpoint_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get a webhook endpoint, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_with(self, endpoint_id: &str, options: &RequestOptions) -> Result<Option<WebhookEndpoint>> {
        self.c.one(&ops::GET_WEBHOOK_ENDPOINT, &[endpoint_id], None, None::<&()>, options).await
    }

    /// Delete an endpoint.
    pub async fn delete(self, endpoint_id: &str) -> Result<DeletedEndpoint> {
        self.delete_with(endpoint_id, &RequestOptions::default()).await
    }

    /// Delete an endpoint, with options.
    pub async fn delete_with(self, endpoint_id: &str, options: &RequestOptions) -> Result<DeletedEndpoint> {
        self.c.one(&ops::DELETE_WEBHOOK_ENDPOINT, &[endpoint_id], None, None::<&()>, options).await
    }

    /// Send a test.
    pub async fn test(self, endpoint_id: &str) -> Result<WebhookDelivery> {
        self.test_with(endpoint_id, &RequestOptions::default()).await
    }

    /// Send a test, with options.
    pub async fn test_with(self, endpoint_id: &str, options: &RequestOptions) -> Result<WebhookDelivery> {
        self.c.one(&ops::TEST_WEBHOOK_ENDPOINT, &[endpoint_id], None, None::<&()>, options).await
    }

    /// List deliveries.
    pub async fn list_deliveries(self, endpoint_id: &str, params: Option<&WebhookEndpointsListDeliveriesParams>) -> Result<Page<WebhookDelivery>> {
        self.c.list(&ops::LIST_WEBHOOK_DELIVERIES, &[endpoint_id], params.map(|p| p.to_query()), &RequestOptions::default()).await
    }

    /// List deliveries, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_deliveries_with(self, endpoint_id: &str, params: Option<&WebhookEndpointsListDeliveriesParams>, options: &RequestOptions) -> Result<Option<Page<WebhookDelivery>>> {
        self.c.list_or_none(&ops::LIST_WEBHOOK_DELIVERIES, &[endpoint_id], params.map(|p| p.to_query()), options).await
    }

    /// Every item of list_deliveries, page after page (RequestOptions::max_items caps it).
    pub fn list_deliveries_auto_paging<'s>(self, endpoint_id: &'s str, params: Option<&'s WebhookEndpointsListDeliveriesParams>, options: &'s RequestOptions) -> impl Stream<Item = Result<WebhookDelivery>> + 's
    where
        'a: 's,
    {
        let c = self.c;
        auto_paging(async move { c.list(&ops::LIST_WEBHOOK_DELIVERIES, &[endpoint_id], params.map(|p| p.to_query()), options).await }, options)
    }

    /// Get a delivery.
    pub async fn get_delivery(self, endpoint_id: &str, delivery_id: &str) -> Result<WebhookDelivery> {
        self.c.one(&ops::GET_WEBHOOK_DELIVERY, &[endpoint_id, delivery_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get a delivery, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_delivery_with(self, endpoint_id: &str, delivery_id: &str, options: &RequestOptions) -> Result<Option<WebhookDelivery>> {
        self.c.one(&ops::GET_WEBHOOK_DELIVERY, &[endpoint_id, delivery_id], None, None::<&()>, options).await
    }

    /// Send a delivery again.
    pub async fn resend_delivery(self, endpoint_id: &str, delivery_id: &str) -> Result<WebhookDelivery> {
        self.resend_delivery_with(endpoint_id, delivery_id, &RequestOptions::default()).await
    }

    /// Send a delivery again, with options.
    pub async fn resend_delivery_with(self, endpoint_id: &str, delivery_id: &str, options: &RequestOptions) -> Result<WebhookDelivery> {
        self.c.one(&ops::RESEND_WEBHOOK_DELIVERY, &[endpoint_id, delivery_id], None, None::<&()>, options).await
    }

    /// Pause an endpoint.
    pub async fn disable(self, endpoint_id: &str) -> Result<WebhookEndpoint> {
        self.disable_with(endpoint_id, &RequestOptions::default()).await
    }

    /// Pause an endpoint, with options.
    pub async fn disable_with(self, endpoint_id: &str, options: &RequestOptions) -> Result<WebhookEndpoint> {
        self.c.one(&ops::DISABLE_WEBHOOK_ENDPOINT, &[endpoint_id], None, None::<&()>, options).await
    }

    /// Resume an endpoint.
    pub async fn enable(self, endpoint_id: &str) -> Result<WebhookEndpoint> {
        self.enable_with(endpoint_id, &RequestOptions::default()).await
    }

    /// Resume an endpoint, with options.
    pub async fn enable_with(self, endpoint_id: &str, options: &RequestOptions) -> Result<WebhookEndpoint> {
        self.c.one(&ops::ENABLE_WEBHOOK_ENDPOINT, &[endpoint_id], None, None::<&()>, options).await
    }

    /// List event types.
    pub async fn event_types(self) -> Result<Page<WebhookEventType>> {
        self.c.list(&ops::LIST_WEBHOOK_EVENT_TYPES, &[], None, &RequestOptions::default()).await
    }

    /// List event types, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn event_types_with(self, options: &RequestOptions) -> Result<Option<Page<WebhookEventType>>> {
        self.c.list_or_none(&ops::LIST_WEBHOOK_EVENT_TYPES, &[], None, options).await
    }
}

/// The tradingview calls (`client.tradingview()`).
#[derive(Clone, Copy)]
pub struct TradingViewResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> TradingViewResource<'a> {
    /// List TradingView links.
    pub async fn list_hooks(self) -> Result<Page<TradingViewHook>> {
        self.c.list(&ops::LIST_TRADING_VIEW_HOOKS, &[], None, &RequestOptions::default()).await
    }

    /// List TradingView links, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_hooks_with(self, options: &RequestOptions) -> Result<Option<Page<TradingViewHook>>> {
        self.c.list_or_none(&ops::LIST_TRADING_VIEW_HOOKS, &[], None, options).await
    }

    /// Get a TradingView link.
    pub async fn get_hook(self, hook_id: &str) -> Result<TradingViewHook> {
        self.c.one(&ops::GET_TRADING_VIEW_HOOK, &[hook_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get a TradingView link, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_hook_with(self, hook_id: &str, options: &RequestOptions) -> Result<Option<TradingViewHook>> {
        self.c.one(&ops::GET_TRADING_VIEW_HOOK, &[hook_id], None, None::<&()>, options).await
    }

    /// Revoke a TradingView link.
    pub async fn delete_hook(self, hook_id: &str) -> Result<DeletedHook> {
        self.delete_hook_with(hook_id, &RequestOptions::default()).await
    }

    /// Revoke a TradingView link, with options.
    pub async fn delete_hook_with(self, hook_id: &str, options: &RequestOptions) -> Result<DeletedHook> {
        self.c.one(&ops::DELETE_TRADING_VIEW_HOOK, &[hook_id], None, None::<&()>, options).await
    }

    /// List a link's alerts.
    pub async fn list_alerts(self, hook_id: &str, params: Option<&TradingviewListAlertsParams>) -> Result<Page<TradingViewAlert>> {
        self.c.list(&ops::LIST_TRADING_VIEW_ALERTS, &[hook_id], params.map(|p| p.to_query()), &RequestOptions::default()).await
    }

    /// List a link's alerts, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_alerts_with(self, hook_id: &str, params: Option<&TradingviewListAlertsParams>, options: &RequestOptions) -> Result<Option<Page<TradingViewAlert>>> {
        self.c.list_or_none(&ops::LIST_TRADING_VIEW_ALERTS, &[hook_id], params.map(|p| p.to_query()), options).await
    }

    /// Every item of list_alerts, page after page (RequestOptions::max_items caps it).
    pub fn list_alerts_auto_paging<'s>(self, hook_id: &'s str, params: Option<&'s TradingviewListAlertsParams>, options: &'s RequestOptions) -> impl Stream<Item = Result<TradingViewAlert>> + 's
    where
        'a: 's,
    {
        let c = self.c;
        auto_paging(async move { c.list(&ops::LIST_TRADING_VIEW_ALERTS, &[hook_id], params.map(|p| p.to_query()), options).await }, options)
    }

    /// Get an alert.
    pub async fn get_alert(self, hook_id: &str, alert_id: &str) -> Result<TradingViewAlert> {
        self.c.one(&ops::GET_TRADING_VIEW_ALERT, &[hook_id, alert_id], None, None::<&()>, &RequestOptions::default()).await
    }

    /// Get an alert, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn get_alert_with(self, hook_id: &str, alert_id: &str, options: &RequestOptions) -> Result<Option<TradingViewAlert>> {
        self.c.one(&ops::GET_TRADING_VIEW_ALERT, &[hook_id, alert_id], None, None::<&()>, options).await
    }

    /// Pause a TradingView link.
    pub async fn disable_hook(self, hook_id: &str) -> Result<TradingViewHook> {
        self.disable_hook_with(hook_id, &RequestOptions::default()).await
    }

    /// Pause a TradingView link, with options.
    pub async fn disable_hook_with(self, hook_id: &str, options: &RequestOptions) -> Result<TradingViewHook> {
        self.c.one(&ops::DISABLE_TRADING_VIEW_HOOK, &[hook_id], None, None::<&()>, options).await
    }

    /// Resume a TradingView link.
    pub async fn enable_hook(self, hook_id: &str) -> Result<TradingViewHook> {
        self.enable_hook_with(hook_id, &RequestOptions::default()).await
    }

    /// Resume a TradingView link, with options.
    pub async fn enable_hook_with(self, hook_id: &str, options: &RequestOptions) -> Result<TradingViewHook> {
        self.c.one(&ops::ENABLE_TRADING_VIEW_HOOK, &[hook_id], None, None::<&()>, options).await
    }

    /// Read an alert message.
    pub async fn parse(self, body: &TradingViewParseInput) -> Result<TradingViewParse> {
        self.parse_with(body, &RequestOptions::default()).await
    }

    /// Read an alert message, with options.
    pub async fn parse_with(self, body: &TradingViewParseInput, options: &RequestOptions) -> Result<TradingViewParse> {
        self.c.one(&ops::PARSE_TRADING_VIEW_MESSAGE, &[], None, Some(body), options).await
    }
}

/// The fills calls (`client.fills()`).
#[derive(Clone, Copy)]
pub struct FillsResource<'a> {
    pub(crate) c: &'a Client,
}

impl<'a> FillsResource<'a> {
    /// List fills.
    pub async fn list(self, account_id: &str, params: Option<&FillsListParams>) -> Result<Page<Fill>> {
        self.c.list(&ops::LIST_FILLS, &[account_id], params.map(|p| p.to_query()), &RequestOptions::default()).await
    }

    /// List fills, with options: None on a 304 Not Modified (the If-None-Match given in options.headers is current).
    pub async fn list_with(self, account_id: &str, params: Option<&FillsListParams>, options: &RequestOptions) -> Result<Option<Page<Fill>>> {
        self.c.list_or_none(&ops::LIST_FILLS, &[account_id], params.map(|p| p.to_query()), options).await
    }

    /// Every item of list, page after page (RequestOptions::max_items caps it).
    pub fn list_auto_paging<'s>(self, account_id: &'s str, params: Option<&'s FillsListParams>, options: &'s RequestOptions) -> impl Stream<Item = Result<Fill>> + 's
    where
        'a: 's,
    {
        let c = self.c;
        auto_paging(async move { c.list(&ops::LIST_FILLS, &[account_id], params.map(|p| p.to_query()), options).await }, options)
    }
}
