<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps;

/** The operations of the API. */
final class Ops
{
    public const GET_API = ['id' => 'getApi', 'method' => 'GET', 'path' => '/v1', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'public'];
    public const GET_SPEC = ['id' => 'getSpec', 'method' => 'GET', 'path' => '/v1/openapi.json', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'public'];
    public const GET_STATUS = ['id' => 'getStatus', 'method' => 'GET', 'path' => '/v1/status', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'public'];
    public const GET_LIMITS = ['id' => 'getLimits', 'method' => 'GET', 'path' => '/v1/limits', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'public'];
    public const GET_ME = ['id' => 'getMe', 'method' => 'GET', 'path' => '/v1/me', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const LIST_MARKETS = ['id' => 'listMarkets', 'method' => 'GET', 'path' => '/v1/markets', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'public'];
    public const LIST_MARKET_STATS = ['id' => 'listMarketStats', 'method' => 'GET', 'path' => '/v1/markets/stats', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'public'];
    public const GET_MARKET = ['id' => 'getMarket', 'method' => 'GET', 'path' => '/v1/markets/{market}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'public'];
    public const LIST_ACCOUNTS = ['id' => 'listAccounts', 'method' => 'GET', 'path' => '/v1/accounts', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const CREATE_SANDBOX_ACCOUNT = ['id' => 'createSandboxAccount', 'method' => 'POST', 'path' => '/v1/accounts', 'idempotent' => true, 'retrySafe' => false, 'cursor' => null, 'scope' => 'trade'];
    public const GET_ACCOUNT = ['id' => 'getAccount', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const GET_RISK = ['id' => 'getRisk', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/risk', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const GET_TRADING_POLICY = ['id' => 'getTradingPolicy', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/trading-policy', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const GET_PROGRESS = ['id' => 'getProgress', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/progress', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const GET_EQUITY_HISTORY = ['id' => 'getEquityHistory', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/history/equity', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const LIST_DAILY_RESULTS = ['id' => 'listDailyResults', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/history/daily', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const LIST_MARGIN_SETTINGS = ['id' => 'listMarginSettings', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/margin', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const LIST_POSITIONS = ['id' => 'listPositions', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/positions', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const GET_POSITION = ['id' => 'getPosition', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/positions/{market}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const LIST_CLOSED_POSITIONS = ['id' => 'listClosedPositions', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/closed-positions', 'idempotent' => false, 'retrySafe' => true, 'cursor' => 'cursor', 'scope' => 'read'];
    public const LIST_ORDERS = ['id' => 'listOrders', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/orders', 'idempotent' => false, 'retrySafe' => true, 'cursor' => 'cursor', 'scope' => 'read'];
    public const PLACE_ORDER = ['id' => 'placeOrder', 'method' => 'POST', 'path' => '/v1/accounts/{account_id}/orders', 'idempotent' => true, 'retrySafe' => false, 'cursor' => null, 'scope' => 'trade'];
    public const GET_ORDER = ['id' => 'getOrder', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/orders/{order_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const MODIFY_ORDER = ['id' => 'modifyOrder', 'method' => 'PATCH', 'path' => '/v1/accounts/{account_id}/orders/{order_id}', 'idempotent' => false, 'retrySafe' => false, 'cursor' => null, 'scope' => 'trade'];
    public const CANCEL_ORDER = ['id' => 'cancelOrder', 'method' => 'DELETE', 'path' => '/v1/accounts/{account_id}/orders/{order_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const TEST_ORDER = ['id' => 'testOrder', 'method' => 'POST', 'path' => '/v1/accounts/{account_id}/orders/test', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const CANCEL_ALL_ORDERS = ['id' => 'cancelAllOrders', 'method' => 'POST', 'path' => '/v1/accounts/{account_id}/orders/cancel-all', 'idempotent' => true, 'retrySafe' => false, 'cursor' => null, 'scope' => 'trade'];
    public const CLOSE_POSITION = ['id' => 'closePosition', 'method' => 'POST', 'path' => '/v1/accounts/{account_id}/positions/{market}/close', 'idempotent' => true, 'retrySafe' => false, 'cursor' => null, 'scope' => 'trade'];
    public const FLATTEN = ['id' => 'flatten', 'method' => 'POST', 'path' => '/v1/accounts/{account_id}/flatten', 'idempotent' => true, 'retrySafe' => false, 'cursor' => null, 'scope' => 'trade'];
    public const SET_MARGIN = ['id' => 'setMargin', 'method' => 'PUT', 'path' => '/v1/accounts/{account_id}/margin/{market}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const LIST_EVENTS = ['id' => 'listEvents', 'method' => 'GET', 'path' => '/v1/events', 'idempotent' => false, 'retrySafe' => true, 'cursor' => 'after', 'scope' => 'read'];
    public const GET_EXITS = ['id' => 'getExits', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/positions/{market}/exits', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const SET_EXITS = ['id' => 'setExits', 'method' => 'PUT', 'path' => '/v1/accounts/{account_id}/positions/{market}/exits', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const SET_ORDER_EXITS = ['id' => 'setOrderExits', 'method' => 'PUT', 'path' => '/v1/accounts/{account_id}/orders/{order_id}/exits', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const CREATE_STRATEGY = ['id' => 'createStrategy', 'method' => 'POST', 'path' => '/v1/accounts/{account_id}/strategies', 'idempotent' => true, 'retrySafe' => false, 'cursor' => null, 'scope' => 'trade'];
    public const LIST_STRATEGIES = ['id' => 'listStrategies', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/strategies', 'idempotent' => false, 'retrySafe' => true, 'cursor' => 'cursor', 'scope' => 'read'];
    public const GET_STRATEGY = ['id' => 'getStrategy', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/strategies/{strategy_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const CANCEL_STRATEGY = ['id' => 'cancelStrategy', 'method' => 'DELETE', 'path' => '/v1/accounts/{account_id}/strategies/{strategy_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const LIST_WEBHOOK_ENDPOINTS = ['id' => 'listWebhookEndpoints', 'method' => 'GET', 'path' => '/v1/webhook-endpoints', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const GET_WEBHOOK_ENDPOINT = ['id' => 'getWebhookEndpoint', 'method' => 'GET', 'path' => '/v1/webhook-endpoints/{endpoint_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const DELETE_WEBHOOK_ENDPOINT = ['id' => 'deleteWebhookEndpoint', 'method' => 'DELETE', 'path' => '/v1/webhook-endpoints/{endpoint_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const TEST_WEBHOOK_ENDPOINT = ['id' => 'testWebhookEndpoint', 'method' => 'POST', 'path' => '/v1/webhook-endpoints/{endpoint_id}/test', 'idempotent' => false, 'retrySafe' => false, 'cursor' => null, 'scope' => 'trade'];
    public const LIST_WEBHOOK_DELIVERIES = ['id' => 'listWebhookDeliveries', 'method' => 'GET', 'path' => '/v1/webhook-endpoints/{endpoint_id}/deliveries', 'idempotent' => false, 'retrySafe' => true, 'cursor' => 'cursor', 'scope' => 'read'];
    public const GET_WEBHOOK_DELIVERY = ['id' => 'getWebhookDelivery', 'method' => 'GET', 'path' => '/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const RESEND_WEBHOOK_DELIVERY = ['id' => 'resendWebhookDelivery', 'method' => 'POST', 'path' => '/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}/resend', 'idempotent' => false, 'retrySafe' => false, 'cursor' => null, 'scope' => 'trade'];
    public const DISABLE_WEBHOOK_ENDPOINT = ['id' => 'disableWebhookEndpoint', 'method' => 'POST', 'path' => '/v1/webhook-endpoints/{endpoint_id}/disable', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const ENABLE_WEBHOOK_ENDPOINT = ['id' => 'enableWebhookEndpoint', 'method' => 'POST', 'path' => '/v1/webhook-endpoints/{endpoint_id}/enable', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const LIST_TRADING_VIEW_HOOKS = ['id' => 'listTradingViewHooks', 'method' => 'GET', 'path' => '/v1/tradingview/hooks', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const GET_TRADING_VIEW_HOOK = ['id' => 'getTradingViewHook', 'method' => 'GET', 'path' => '/v1/tradingview/hooks/{hook_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const DELETE_TRADING_VIEW_HOOK = ['id' => 'deleteTradingViewHook', 'method' => 'DELETE', 'path' => '/v1/tradingview/hooks/{hook_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const LIST_TRADING_VIEW_ALERTS = ['id' => 'listTradingViewAlerts', 'method' => 'GET', 'path' => '/v1/tradingview/hooks/{hook_id}/alerts', 'idempotent' => false, 'retrySafe' => true, 'cursor' => 'cursor', 'scope' => 'read'];
    public const GET_TRADING_VIEW_ALERT = ['id' => 'getTradingViewAlert', 'method' => 'GET', 'path' => '/v1/tradingview/hooks/{hook_id}/alerts/{alert_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const DISABLE_TRADING_VIEW_HOOK = ['id' => 'disableTradingViewHook', 'method' => 'POST', 'path' => '/v1/tradingview/hooks/{hook_id}/disable', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const ENABLE_TRADING_VIEW_HOOK = ['id' => 'enableTradingViewHook', 'method' => 'POST', 'path' => '/v1/tradingview/hooks/{hook_id}/enable', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'trade'];
    public const PARSE_TRADING_VIEW_MESSAGE = ['id' => 'parseTradingViewMessage', 'method' => 'POST', 'path' => '/v1/tradingview/parse', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const LIST_WEBHOOK_EVENT_TYPES = ['id' => 'listWebhookEventTypes', 'method' => 'GET', 'path' => '/v1/webhook-event-types', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const GET_EVENT = ['id' => 'getEvent', 'method' => 'GET', 'path' => '/v1/events/{event_id}', 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => 'read'];
    public const LIST_FILLS = ['id' => 'listFills', 'method' => 'GET', 'path' => '/v1/accounts/{account_id}/fills', 'idempotent' => false, 'retrySafe' => true, 'cursor' => 'cursor', 'scope' => 'read'];
}
