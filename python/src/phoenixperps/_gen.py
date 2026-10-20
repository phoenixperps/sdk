# Generated from the API spec by generator/printers/python.js. Do not edit: run `node generator`.
from __future__ import annotations

from typing import TYPE_CHECKING, Any, Dict, Mapping, Optional

if TYPE_CHECKING:
    from . import types
    from ._core import Page, _AsyncPageCall

SPEC_VERSION = "1.0.0"
SERVERS = {"live":"https://api.phoenixperpsfunding.com","sandbox":"https://sandbox-api.phoenixperpsfunding.com"}
STREAMS: Dict[str, Any] = {"account":{"live":"wss://api.phoenixperpsfunding.com/v1/stream","sandbox":"wss://sandbox-api.phoenixperpsfunding.com/v1/stream"},"market":"wss://stream.phoenixperpsfunding.com/v1/market-data"}
ERROR_CODES = {"invalid_request":{"type":"invalid_request","status":400,"retryable":False},"invalid_parameter":{"type":"invalid_request","status":400,"retryable":False},"unknown_field":{"type":"invalid_request","status":400,"retryable":False},"invalid_cursor":{"type":"invalid_request","status":400,"retryable":False},"invalid_json":{"type":"invalid_request","status":400,"retryable":False},"missing_api_key":{"type":"authentication","status":401,"retryable":False},"invalid_api_key":{"type":"authentication","status":401,"retryable":False},"expired_api_key":{"type":"authentication","status":401,"retryable":False},"revoked_api_key":{"type":"authentication","status":401,"retryable":False},"invalid_token":{"type":"authentication","status":401,"retryable":False},"expired_token":{"type":"authentication","status":401,"retryable":False},"revoked_token":{"type":"authentication","status":401,"retryable":False},"wrong_resource":{"type":"authentication","status":401,"retryable":False},"wrong_environment":{"type":"authentication","status":401,"retryable":False},"ip_not_allowed":{"type":"permission","status":403,"retryable":False},"insufficient_permission":{"type":"permission","status":403,"retryable":False},"api_disabled":{"type":"permission","status":403,"retryable":False},"invalid_size_step":{"type":"invalid_request","status":400,"retryable":False},"invalid_price_tick":{"type":"invalid_request","status":400,"retryable":False},"price_out_of_band":{"type":"rule_rejection","status":422,"retryable":False},"stop_already_triggered":{"type":"rule_rejection","status":422,"retryable":False},"post_only_would_fill":{"type":"rule_rejection","status":422,"retryable":False},"limit_inside_spread":{"type":"rule_rejection","status":422,"retryable":False},"order_too_small":{"type":"rule_rejection","status":422,"retryable":False},"working_order_cap":{"type":"rule_rejection","status":422,"retryable":False},"client_order_id_conflict":{"type":"conflict","status":409,"retryable":False},"order_not_working":{"type":"conflict","status":409,"retryable":False},"idempotency_key_reused":{"type":"invalid_request","status":400,"retryable":False},"json_too_deep":{"type":"invalid_request","status":400,"retryable":False},"operation_in_progress":{"type":"conflict","status":409,"retryable":True},"key_market_not_allowed":{"type":"key_limit","status":422,"retryable":False},"key_reduce_only":{"type":"key_limit","status":422,"retryable":False},"key_max_order_notional":{"type":"key_limit","status":422,"retryable":False},"key_max_position_notional":{"type":"key_limit","status":422,"retryable":False},"key_loss_stop_active":{"type":"key_limit","status":422,"retryable":False},"insufficient_margin":{"type":"rule_rejection","status":422,"retryable":False},"exposure_cap_exceeded":{"type":"rule_rejection","status":422,"retryable":False},"reduce_only_violation":{"type":"rule_rejection","status":422,"retryable":False},"slippage_exceeded":{"type":"rule_rejection","status":422,"retryable":False},"no_price":{"type":"engine","status":503,"retryable":True},"stale_quote":{"type":"engine","status":503,"retryable":True},"market_closed":{"type":"rule_rejection","status":422,"retryable":False},"market_not_offered":{"type":"rule_rejection","status":422,"retryable":False},"market_halted":{"type":"rule_rejection","status":422,"retryable":False},"position_liquidating":{"type":"rule_rejection","status":422,"retryable":False},"account_paused":{"type":"rule_rejection","status":422,"retryable":False},"account_not_tradable":{"type":"rule_rejection","status":422,"retryable":False},"insufficient_balance":{"type":"rule_rejection","status":422,"retryable":False},"margin_mode_locked":{"type":"conflict","status":409,"retryable":False},"margin_rejected":{"type":"rule_rejection","status":422,"retryable":False},"invalid_leverage":{"type":"invalid_request","status":400,"retryable":False},"liquidation_risk":{"type":"rule_rejection","status":422,"retryable":False},"position_not_isolated":{"type":"conflict","status":409,"retryable":False},"engine_busy":{"type":"engine","status":503,"retryable":True},"order_rejected":{"type":"rule_rejection","status":422,"retryable":False},"exits_changed":{"type":"conflict","status":409,"retryable":False},"exit_operation_failed":{"type":"rule_rejection","status":422,"retryable":False},"exit_would_fill":{"type":"rule_rejection","status":422,"retryable":False},"exits_exceed_position":{"type":"rule_rejection","status":422,"retryable":False},"exit_leg_cap":{"type":"rule_rejection","status":422,"retryable":False},"strategy_limit":{"type":"rule_rejection","status":422,"retryable":False},"strategy_not_active":{"type":"conflict","status":409,"retryable":False},"strategy_not_found":{"type":"not_found","status":404,"retryable":False},"client_strategy_id_conflict":{"type":"conflict","status":409,"retryable":False},"webhook_endpoint_not_found":{"type":"not_found","status":404,"retryable":False},"tradingview_hook_not_found":{"type":"not_found","status":404,"retryable":False},"tradingview_alert_not_found":{"type":"not_found","status":404,"retryable":False},"webhook_endpoint_not_active":{"type":"conflict","status":409,"retryable":False},"delivery_not_found":{"type":"not_found","status":404,"retryable":False},"event_not_found":{"type":"not_found","status":404,"retryable":False},"rung_would_fill":{"type":"rule_rejection","status":422,"retryable":False},"strategy_type_disabled":{"type":"permission","status":403,"retryable":False},"account_not_found":{"type":"not_found","status":404,"retryable":False},"market_not_found":{"type":"not_found","status":404,"retryable":False},"order_not_found":{"type":"not_found","status":404,"retryable":False},"position_not_found":{"type":"not_found","status":404,"retryable":False},"route_not_found":{"type":"not_found","status":404,"retryable":False},"method_not_allowed":{"type":"method","status":405,"retryable":False},"payload_too_large":{"type":"payload","status":413,"retryable":False},"uri_too_long":{"type":"payload","status":414,"retryable":False},"unsupported_media_type":{"type":"payload","status":415,"retryable":False},"sandbox_limit_reached":{"type":"conflict","status":409,"retryable":False},"account_not_ready":{"type":"conflict","status":409,"retryable":True},"sandbox_opening":{"type":"conflict","status":409,"retryable":True},"idempotency_key_required":{"type":"invalid_request","status":400,"retryable":False},"invalid_idempotency_key":{"type":"invalid_request","status":400,"retryable":False},"rate_limited":{"type":"rate_limit","status":429,"retryable":True},"not_ready":{"type":"engine","status":503,"retryable":True},"state_unavailable":{"type":"engine","status":503,"retryable":True},"engine_unavailable":{"type":"engine","status":503,"retryable":True},"limiter_unavailable":{"type":"server","status":503,"retryable":True},"sandbox_disabled":{"type":"engine","status":503,"retryable":False},"environment_disabled":{"type":"engine","status":503,"retryable":False},"temporarily_unavailable":{"type":"server","status":503,"retryable":True},"capacity":{"type":"engine","status":503,"retryable":True},"internal_error":{"type":"server","status":500,"retryable":True}}

OPS: Dict[str, Dict[str, Any]] = {
    "getApi": {"id":"getApi","http":"GET","path":"/v1","idempotent":False,"retry_safe":True,"scope":"public","paging":None},
    "getSpec": {"id":"getSpec","http":"GET","path":"/v1/openapi.json","idempotent":False,"retry_safe":True,"scope":"public","paging":None},
    "getStatus": {"id":"getStatus","http":"GET","path":"/v1/status","idempotent":False,"retry_safe":True,"scope":"public","paging":None},
    "getLimits": {"id":"getLimits","http":"GET","path":"/v1/limits","idempotent":False,"retry_safe":True,"scope":"public","paging":None},
    "getMe": {"id":"getMe","http":"GET","path":"/v1/me","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "listMarkets": {"id":"listMarkets","http":"GET","path":"/v1/markets","idempotent":False,"retry_safe":True,"scope":"public","paging":None},
    "listMarketStats": {"id":"listMarketStats","http":"GET","path":"/v1/markets/stats","idempotent":False,"retry_safe":True,"scope":"public","paging":None},
    "getMarket": {"id":"getMarket","http":"GET","path":"/v1/markets/{market}","idempotent":False,"retry_safe":True,"scope":"public","paging":None},
    "listAccounts": {"id":"listAccounts","http":"GET","path":"/v1/accounts","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "createSandboxAccount": {"id":"createSandboxAccount","http":"POST","path":"/v1/accounts","idempotent":True,"retry_safe":False,"scope":"trade","paging":None},
    "getAccount": {"id":"getAccount","http":"GET","path":"/v1/accounts/{account_id}","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "getRisk": {"id":"getRisk","http":"GET","path":"/v1/accounts/{account_id}/risk","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "getTradingPolicy": {"id":"getTradingPolicy","http":"GET","path":"/v1/accounts/{account_id}/trading-policy","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "getProgress": {"id":"getProgress","http":"GET","path":"/v1/accounts/{account_id}/progress","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "getEquityHistory": {"id":"getEquityHistory","http":"GET","path":"/v1/accounts/{account_id}/history/equity","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "listDailyResults": {"id":"listDailyResults","http":"GET","path":"/v1/accounts/{account_id}/history/daily","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "listMarginSettings": {"id":"listMarginSettings","http":"GET","path":"/v1/accounts/{account_id}/margin","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "listPositions": {"id":"listPositions","http":"GET","path":"/v1/accounts/{account_id}/positions","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "getPosition": {"id":"getPosition","http":"GET","path":"/v1/accounts/{account_id}/positions/{market}","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "listClosedPositions": {"id":"listClosedPositions","http":"GET","path":"/v1/accounts/{account_id}/closed-positions","idempotent":False,"retry_safe":True,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
    "listOrders": {"id":"listOrders","http":"GET","path":"/v1/accounts/{account_id}/orders","idempotent":False,"retry_safe":True,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
    "placeOrder": {"id":"placeOrder","http":"POST","path":"/v1/accounts/{account_id}/orders","idempotent":True,"retry_safe":False,"scope":"trade","paging":None},
    "getOrder": {"id":"getOrder","http":"GET","path":"/v1/accounts/{account_id}/orders/{order_id}","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "modifyOrder": {"id":"modifyOrder","http":"PATCH","path":"/v1/accounts/{account_id}/orders/{order_id}","idempotent":False,"retry_safe":False,"scope":"trade","paging":None},
    "cancelOrder": {"id":"cancelOrder","http":"DELETE","path":"/v1/accounts/{account_id}/orders/{order_id}","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "testOrder": {"id":"testOrder","http":"POST","path":"/v1/accounts/{account_id}/orders/test","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "cancelAllOrders": {"id":"cancelAllOrders","http":"POST","path":"/v1/accounts/{account_id}/orders/cancel-all","idempotent":True,"retry_safe":False,"scope":"trade","paging":None},
    "closePosition": {"id":"closePosition","http":"POST","path":"/v1/accounts/{account_id}/positions/{market}/close","idempotent":True,"retry_safe":False,"scope":"trade","paging":None},
    "flatten": {"id":"flatten","http":"POST","path":"/v1/accounts/{account_id}/flatten","idempotent":True,"retry_safe":False,"scope":"trade","paging":None},
    "setMargin": {"id":"setMargin","http":"PUT","path":"/v1/accounts/{account_id}/margin/{market}","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "listEvents": {"id":"listEvents","http":"GET","path":"/v1/events","idempotent":False,"retry_safe":True,"scope":"read","paging":{"param":"after","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
    "getExits": {"id":"getExits","http":"GET","path":"/v1/accounts/{account_id}/positions/{market}/exits","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "setExits": {"id":"setExits","http":"PUT","path":"/v1/accounts/{account_id}/positions/{market}/exits","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "setOrderExits": {"id":"setOrderExits","http":"PUT","path":"/v1/accounts/{account_id}/orders/{order_id}/exits","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "createStrategy": {"id":"createStrategy","http":"POST","path":"/v1/accounts/{account_id}/strategies","idempotent":True,"retry_safe":False,"scope":"trade","paging":None},
    "listStrategies": {"id":"listStrategies","http":"GET","path":"/v1/accounts/{account_id}/strategies","idempotent":False,"retry_safe":True,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
    "getStrategy": {"id":"getStrategy","http":"GET","path":"/v1/accounts/{account_id}/strategies/{strategy_id}","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "cancelStrategy": {"id":"cancelStrategy","http":"DELETE","path":"/v1/accounts/{account_id}/strategies/{strategy_id}","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "listWebhookEndpoints": {"id":"listWebhookEndpoints","http":"GET","path":"/v1/webhook-endpoints","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "getWebhookEndpoint": {"id":"getWebhookEndpoint","http":"GET","path":"/v1/webhook-endpoints/{endpoint_id}","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "deleteWebhookEndpoint": {"id":"deleteWebhookEndpoint","http":"DELETE","path":"/v1/webhook-endpoints/{endpoint_id}","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "testWebhookEndpoint": {"id":"testWebhookEndpoint","http":"POST","path":"/v1/webhook-endpoints/{endpoint_id}/test","idempotent":False,"retry_safe":False,"scope":"trade","paging":None},
    "listWebhookDeliveries": {"id":"listWebhookDeliveries","http":"GET","path":"/v1/webhook-endpoints/{endpoint_id}/deliveries","idempotent":False,"retry_safe":True,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
    "getWebhookDelivery": {"id":"getWebhookDelivery","http":"GET","path":"/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "resendWebhookDelivery": {"id":"resendWebhookDelivery","http":"POST","path":"/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}/resend","idempotent":False,"retry_safe":False,"scope":"trade","paging":None},
    "disableWebhookEndpoint": {"id":"disableWebhookEndpoint","http":"POST","path":"/v1/webhook-endpoints/{endpoint_id}/disable","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "enableWebhookEndpoint": {"id":"enableWebhookEndpoint","http":"POST","path":"/v1/webhook-endpoints/{endpoint_id}/enable","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "listTradingViewHooks": {"id":"listTradingViewHooks","http":"GET","path":"/v1/tradingview/hooks","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "getTradingViewHook": {"id":"getTradingViewHook","http":"GET","path":"/v1/tradingview/hooks/{hook_id}","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "deleteTradingViewHook": {"id":"deleteTradingViewHook","http":"DELETE","path":"/v1/tradingview/hooks/{hook_id}","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "listTradingViewAlerts": {"id":"listTradingViewAlerts","http":"GET","path":"/v1/tradingview/hooks/{hook_id}/alerts","idempotent":False,"retry_safe":True,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
    "getTradingViewAlert": {"id":"getTradingViewAlert","http":"GET","path":"/v1/tradingview/hooks/{hook_id}/alerts/{alert_id}","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "disableTradingViewHook": {"id":"disableTradingViewHook","http":"POST","path":"/v1/tradingview/hooks/{hook_id}/disable","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "enableTradingViewHook": {"id":"enableTradingViewHook","http":"POST","path":"/v1/tradingview/hooks/{hook_id}/enable","idempotent":False,"retry_safe":True,"scope":"trade","paging":None},
    "parseTradingViewMessage": {"id":"parseTradingViewMessage","http":"POST","path":"/v1/tradingview/parse","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "listWebhookEventTypes": {"id":"listWebhookEventTypes","http":"GET","path":"/v1/webhook-event-types","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "getEvent": {"id":"getEvent","http":"GET","path":"/v1/events/{event_id}","idempotent":False,"retry_safe":True,"scope":"read","paging":None},
    "listFills": {"id":"listFills","http":"GET","path":"/v1/accounts/{account_id}/fills","idempotent":False,"retry_safe":True,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
}


class PlatformResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def info(self, **opts: Any) -> types.Platform:
        """Get the API"""
        return self._c._one(OPS["getApi"], opts=opts)

    def spec(self, **opts: Any) -> Any:
        """Get the OpenAPI document"""
        return self._c._one(OPS["getSpec"], opts=opts)

    def status(self, **opts: Any) -> types.Status:
        """Get the status"""
        return self._c._one(OPS["getStatus"], opts=opts)

    def limits(self, **opts: Any) -> types.Limits:
        """Get the limits"""
        return self._c._one(OPS["getLimits"], opts=opts)


class MeResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def get(self, **opts: Any) -> types.Me:
        """Get your key"""
        return self._c._one(OPS["getMe"], opts=opts)


class MarketsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, **opts: Any) -> Page[types.Market]:
        """List markets"""
        return self._c._page(OPS["listMarkets"], opts=opts)

    def stats(self, **opts: Any) -> Page[types.MarketStats]:
        """List market stats"""
        return self._c._page(OPS["listMarketStats"], opts=opts)

    def get(self, market: str, **opts: Any) -> types.Market:
        """Get a market"""
        return self._c._one(OPS["getMarket"], path={"market": market}, opts=opts)


class AccountsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, **opts: Any) -> Page[types.Account]:
        """List accounts"""
        return self._c._page(OPS["listAccounts"], opts=opts)

    def create_sandbox(self, **opts: Any) -> types.Account:
        """Open a sandbox account"""
        return self._c._one(OPS["createSandboxAccount"], opts=opts)

    def get(self, account_id: str, **opts: Any) -> types.AccountDetail:
        """Get an account"""
        return self._c._one(OPS["getAccount"], path={"account_id": account_id}, opts=opts)

    def risk(self, account_id: str, **opts: Any) -> types.AccountRisk:
        """Get live risk"""
        return self._c._one(OPS["getRisk"], path={"account_id": account_id}, opts=opts)

    def trading_policy(self, account_id: str, **opts: Any) -> types.TradingPolicy:
        """Get the trading policy"""
        return self._c._one(OPS["getTradingPolicy"], path={"account_id": account_id}, opts=opts)

    def progress(self, account_id: str, **opts: Any) -> types.Progress:
        """Get progress"""
        return self._c._one(OPS["getProgress"], path={"account_id": account_id}, opts=opts)


class HistoryResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def equity(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> types.EquityHistory:
        """Get the equity curve"""
        return self._c._one(OPS["getEquityHistory"], path={"account_id": account_id}, query=query, opts=opts)

    def daily(self, account_id: str, **opts: Any) -> Page[types.DailyResult]:
        """List daily results"""
        return self._c._page(OPS["listDailyResults"], path={"account_id": account_id}, opts=opts)


class MarginResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, account_id: str, **opts: Any) -> Page[types.MarginSetting]:
        """List margin settings"""
        return self._c._page(OPS["listMarginSettings"], path={"account_id": account_id}, opts=opts)

    def set(self, account_id: str, market: str, body: Mapping[str, Any], **opts: Any) -> types.MarginSettingResult:
        """Set leverage and margin mode"""
        return self._c._one(OPS["setMargin"], path={"account_id": account_id, "market": market}, body=body, opts=opts)


class PositionsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> Page[types.Position]:
        """List open positions"""
        return self._c._page(OPS["listPositions"], path={"account_id": account_id}, query=query, opts=opts)

    def get(self, account_id: str, market: str, **opts: Any) -> types.Position:
        """Get an open position"""
        return self._c._one(OPS["getPosition"], path={"account_id": account_id, "market": market}, opts=opts)

    def list_closed(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> Page[types.ClosedPosition]:
        """List closed positions"""
        return self._c._page(OPS["listClosedPositions"], path={"account_id": account_id}, query=query, opts=opts)

    def close(self, account_id: str, market: str, body: Mapping[str, Any], **opts: Any) -> types.Order:
        """Close a position"""
        return self._c._one(OPS["closePosition"], path={"account_id": account_id, "market": market}, body=body, opts=opts)

    def flatten(self, account_id: str, **opts: Any) -> types.FlattenResult:
        """Flatten the account"""
        return self._c._one(OPS["flatten"], path={"account_id": account_id}, opts=opts)


class OrdersResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> Page[types.Order]:
        """List orders"""
        return self._c._page(OPS["listOrders"], path={"account_id": account_id}, query=query, opts=opts)

    def create(self, account_id: str, body: Mapping[str, Any], **opts: Any) -> types.Order:
        """Place an order"""
        return self._c._one(OPS["placeOrder"], path={"account_id": account_id}, body=body, opts=opts)

    def get(self, account_id: str, order_id: str, **opts: Any) -> types.Order:
        """Get an order"""
        return self._c._one(OPS["getOrder"], path={"account_id": account_id, "order_id": order_id}, opts=opts)

    def modify(self, account_id: str, order_id: str, body: Mapping[str, Any], **opts: Any) -> types.Order:
        """Change an order"""
        return self._c._one(OPS["modifyOrder"], path={"account_id": account_id, "order_id": order_id}, body=body, opts=opts)

    def cancel(self, account_id: str, order_id: str, **opts: Any) -> types.Order:
        """Cancel an order"""
        return self._c._one(OPS["cancelOrder"], path={"account_id": account_id, "order_id": order_id}, opts=opts)

    def preview(self, account_id: str, body: Mapping[str, Any], **opts: Any) -> types.WhatIf:
        """Preview an order"""
        return self._c._one(OPS["testOrder"], path={"account_id": account_id}, body=body, opts=opts)

    def cancel_all(self, account_id: str, body: Mapping[str, Any], **opts: Any) -> types.CancelAllResult:
        """Cancel all orders"""
        return self._c._one(OPS["cancelAllOrders"], path={"account_id": account_id}, body=body, opts=opts)

    def set_exits(self, account_id: str, order_id: str, body: Mapping[str, Any], **opts: Any) -> types.Order:
        """Attach exits to an order"""
        return self._c._one(OPS["setOrderExits"], path={"account_id": account_id, "order_id": order_id}, body=body, opts=opts)


class EventsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, query: Optional[Dict[str, Any]] = None, **opts: Any) -> Page[types.Event]:
        """List events"""
        return self._c._page(OPS["listEvents"], query=query, opts=opts)

    def get(self, event_id: str, **opts: Any) -> types.Event:
        """Get an event"""
        return self._c._one(OPS["getEvent"], path={"event_id": event_id}, opts=opts)


class ExitsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def get(self, account_id: str, market: str, **opts: Any) -> types.Exits:
        """Get a position's exits"""
        return self._c._one(OPS["getExits"], path={"account_id": account_id, "market": market}, opts=opts)

    def set(self, account_id: str, market: str, body: Mapping[str, Any], **opts: Any) -> types.Exits:
        """Change a position's exits"""
        return self._c._one(OPS["setExits"], path={"account_id": account_id, "market": market}, body=body, opts=opts)


class StrategiesResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def create(self, account_id: str, body: Mapping[str, Any], **opts: Any) -> types.Strategy:
        """Create a strategy"""
        return self._c._one(OPS["createStrategy"], path={"account_id": account_id}, body=body, opts=opts)

    def list(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> Page[types.Strategy]:
        """List strategies"""
        return self._c._page(OPS["listStrategies"], path={"account_id": account_id}, query=query, opts=opts)

    def get(self, account_id: str, strategy_id: str, **opts: Any) -> types.Strategy:
        """Get a strategy"""
        return self._c._one(OPS["getStrategy"], path={"account_id": account_id, "strategy_id": strategy_id}, opts=opts)

    def cancel(self, account_id: str, strategy_id: str, **opts: Any) -> types.Strategy:
        """Cancel a strategy"""
        return self._c._one(OPS["cancelStrategy"], path={"account_id": account_id, "strategy_id": strategy_id}, opts=opts)


class WebhookEndpointsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, **opts: Any) -> Page[types.WebhookEndpoint]:
        """List webhook endpoints"""
        return self._c._page(OPS["listWebhookEndpoints"], opts=opts)

    def get(self, endpoint_id: str, **opts: Any) -> types.WebhookEndpoint:
        """Get a webhook endpoint"""
        return self._c._one(OPS["getWebhookEndpoint"], path={"endpoint_id": endpoint_id}, opts=opts)

    def delete(self, endpoint_id: str, **opts: Any) -> types.DeletedEndpoint:
        """Delete an endpoint"""
        return self._c._one(OPS["deleteWebhookEndpoint"], path={"endpoint_id": endpoint_id}, opts=opts)

    def test(self, endpoint_id: str, **opts: Any) -> types.WebhookDelivery:
        """Send a test"""
        return self._c._one(OPS["testWebhookEndpoint"], path={"endpoint_id": endpoint_id}, opts=opts)

    def list_deliveries(self, endpoint_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> Page[types.WebhookDelivery]:
        """List deliveries"""
        return self._c._page(OPS["listWebhookDeliveries"], path={"endpoint_id": endpoint_id}, query=query, opts=opts)

    def get_delivery(self, endpoint_id: str, delivery_id: str, **opts: Any) -> types.WebhookDelivery:
        """Get a delivery"""
        return self._c._one(OPS["getWebhookDelivery"], path={"endpoint_id": endpoint_id, "delivery_id": delivery_id}, opts=opts)

    def resend_delivery(self, endpoint_id: str, delivery_id: str, **opts: Any) -> types.WebhookDelivery:
        """Send a delivery again"""
        return self._c._one(OPS["resendWebhookDelivery"], path={"endpoint_id": endpoint_id, "delivery_id": delivery_id}, opts=opts)

    def disable(self, endpoint_id: str, **opts: Any) -> types.WebhookEndpoint:
        """Pause an endpoint"""
        return self._c._one(OPS["disableWebhookEndpoint"], path={"endpoint_id": endpoint_id}, opts=opts)

    def enable(self, endpoint_id: str, **opts: Any) -> types.WebhookEndpoint:
        """Resume an endpoint"""
        return self._c._one(OPS["enableWebhookEndpoint"], path={"endpoint_id": endpoint_id}, opts=opts)

    def event_types(self, **opts: Any) -> Page[types.WebhookEventType]:
        """List event types"""
        return self._c._page(OPS["listWebhookEventTypes"], opts=opts)


class TradingviewResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list_hooks(self, **opts: Any) -> Page[types.TradingViewHook]:
        """List TradingView links"""
        return self._c._page(OPS["listTradingViewHooks"], opts=opts)

    def get_hook(self, hook_id: str, **opts: Any) -> types.TradingViewHook:
        """Get a TradingView link"""
        return self._c._one(OPS["getTradingViewHook"], path={"hook_id": hook_id}, opts=opts)

    def delete_hook(self, hook_id: str, **opts: Any) -> types.DeletedHook:
        """Revoke a TradingView link"""
        return self._c._one(OPS["deleteTradingViewHook"], path={"hook_id": hook_id}, opts=opts)

    def list_alerts(self, hook_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> Page[types.TradingViewAlert]:
        """List a link's alerts"""
        return self._c._page(OPS["listTradingViewAlerts"], path={"hook_id": hook_id}, query=query, opts=opts)

    def get_alert(self, hook_id: str, alert_id: str, **opts: Any) -> types.TradingViewAlert:
        """Get an alert"""
        return self._c._one(OPS["getTradingViewAlert"], path={"hook_id": hook_id, "alert_id": alert_id}, opts=opts)

    def disable_hook(self, hook_id: str, **opts: Any) -> types.TradingViewHook:
        """Pause a TradingView link"""
        return self._c._one(OPS["disableTradingViewHook"], path={"hook_id": hook_id}, opts=opts)

    def enable_hook(self, hook_id: str, **opts: Any) -> types.TradingViewHook:
        """Resume a TradingView link"""
        return self._c._one(OPS["enableTradingViewHook"], path={"hook_id": hook_id}, opts=opts)

    def parse(self, body: Mapping[str, Any], **opts: Any) -> types.TradingViewParse:
        """Read an alert message"""
        return self._c._one(OPS["parseTradingViewMessage"], body=body, opts=opts)


class FillsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> Page[types.Fill]:
        """List fills"""
        return self._c._page(OPS["listFills"], path={"account_id": account_id}, query=query, opts=opts)


class AsyncPlatformResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    async def info(self, **opts: Any) -> types.Platform:
        """Get the API"""
        return await self._c._one(OPS["getApi"], opts=opts)

    async def spec(self, **opts: Any) -> Any:
        """Get the OpenAPI document"""
        return await self._c._one(OPS["getSpec"], opts=opts)

    async def status(self, **opts: Any) -> types.Status:
        """Get the status"""
        return await self._c._one(OPS["getStatus"], opts=opts)

    async def limits(self, **opts: Any) -> types.Limits:
        """Get the limits"""
        return await self._c._one(OPS["getLimits"], opts=opts)


class AsyncMeResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    async def get(self, **opts: Any) -> types.Me:
        """Get your key"""
        return await self._c._one(OPS["getMe"], opts=opts)


class AsyncMarketsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, **opts: Any) -> _AsyncPageCall[types.Market]:
        """List markets"""
        return self._c._page(OPS["listMarkets"], opts=opts)

    def stats(self, **opts: Any) -> _AsyncPageCall[types.MarketStats]:
        """List market stats"""
        return self._c._page(OPS["listMarketStats"], opts=opts)

    async def get(self, market: str, **opts: Any) -> types.Market:
        """Get a market"""
        return await self._c._one(OPS["getMarket"], path={"market": market}, opts=opts)


class AsyncAccountsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, **opts: Any) -> _AsyncPageCall[types.Account]:
        """List accounts"""
        return self._c._page(OPS["listAccounts"], opts=opts)

    async def create_sandbox(self, **opts: Any) -> types.Account:
        """Open a sandbox account"""
        return await self._c._one(OPS["createSandboxAccount"], opts=opts)

    async def get(self, account_id: str, **opts: Any) -> types.AccountDetail:
        """Get an account"""
        return await self._c._one(OPS["getAccount"], path={"account_id": account_id}, opts=opts)

    async def risk(self, account_id: str, **opts: Any) -> types.AccountRisk:
        """Get live risk"""
        return await self._c._one(OPS["getRisk"], path={"account_id": account_id}, opts=opts)

    async def trading_policy(self, account_id: str, **opts: Any) -> types.TradingPolicy:
        """Get the trading policy"""
        return await self._c._one(OPS["getTradingPolicy"], path={"account_id": account_id}, opts=opts)

    async def progress(self, account_id: str, **opts: Any) -> types.Progress:
        """Get progress"""
        return await self._c._one(OPS["getProgress"], path={"account_id": account_id}, opts=opts)


class AsyncHistoryResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    async def equity(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> types.EquityHistory:
        """Get the equity curve"""
        return await self._c._one(OPS["getEquityHistory"], path={"account_id": account_id}, query=query, opts=opts)

    def daily(self, account_id: str, **opts: Any) -> _AsyncPageCall[types.DailyResult]:
        """List daily results"""
        return self._c._page(OPS["listDailyResults"], path={"account_id": account_id}, opts=opts)


class AsyncMarginResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, account_id: str, **opts: Any) -> _AsyncPageCall[types.MarginSetting]:
        """List margin settings"""
        return self._c._page(OPS["listMarginSettings"], path={"account_id": account_id}, opts=opts)

    async def set(self, account_id: str, market: str, body: Mapping[str, Any], **opts: Any) -> types.MarginSettingResult:
        """Set leverage and margin mode"""
        return await self._c._one(OPS["setMargin"], path={"account_id": account_id, "market": market}, body=body, opts=opts)


class AsyncPositionsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> _AsyncPageCall[types.Position]:
        """List open positions"""
        return self._c._page(OPS["listPositions"], path={"account_id": account_id}, query=query, opts=opts)

    async def get(self, account_id: str, market: str, **opts: Any) -> types.Position:
        """Get an open position"""
        return await self._c._one(OPS["getPosition"], path={"account_id": account_id, "market": market}, opts=opts)

    def list_closed(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> _AsyncPageCall[types.ClosedPosition]:
        """List closed positions"""
        return self._c._page(OPS["listClosedPositions"], path={"account_id": account_id}, query=query, opts=opts)

    async def close(self, account_id: str, market: str, body: Mapping[str, Any], **opts: Any) -> types.Order:
        """Close a position"""
        return await self._c._one(OPS["closePosition"], path={"account_id": account_id, "market": market}, body=body, opts=opts)

    async def flatten(self, account_id: str, **opts: Any) -> types.FlattenResult:
        """Flatten the account"""
        return await self._c._one(OPS["flatten"], path={"account_id": account_id}, opts=opts)


class AsyncOrdersResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> _AsyncPageCall[types.Order]:
        """List orders"""
        return self._c._page(OPS["listOrders"], path={"account_id": account_id}, query=query, opts=opts)

    async def create(self, account_id: str, body: Mapping[str, Any], **opts: Any) -> types.Order:
        """Place an order"""
        return await self._c._one(OPS["placeOrder"], path={"account_id": account_id}, body=body, opts=opts)

    async def get(self, account_id: str, order_id: str, **opts: Any) -> types.Order:
        """Get an order"""
        return await self._c._one(OPS["getOrder"], path={"account_id": account_id, "order_id": order_id}, opts=opts)

    async def modify(self, account_id: str, order_id: str, body: Mapping[str, Any], **opts: Any) -> types.Order:
        """Change an order"""
        return await self._c._one(OPS["modifyOrder"], path={"account_id": account_id, "order_id": order_id}, body=body, opts=opts)

    async def cancel(self, account_id: str, order_id: str, **opts: Any) -> types.Order:
        """Cancel an order"""
        return await self._c._one(OPS["cancelOrder"], path={"account_id": account_id, "order_id": order_id}, opts=opts)

    async def preview(self, account_id: str, body: Mapping[str, Any], **opts: Any) -> types.WhatIf:
        """Preview an order"""
        return await self._c._one(OPS["testOrder"], path={"account_id": account_id}, body=body, opts=opts)

    async def cancel_all(self, account_id: str, body: Mapping[str, Any], **opts: Any) -> types.CancelAllResult:
        """Cancel all orders"""
        return await self._c._one(OPS["cancelAllOrders"], path={"account_id": account_id}, body=body, opts=opts)

    async def set_exits(self, account_id: str, order_id: str, body: Mapping[str, Any], **opts: Any) -> types.Order:
        """Attach exits to an order"""
        return await self._c._one(OPS["setOrderExits"], path={"account_id": account_id, "order_id": order_id}, body=body, opts=opts)


class AsyncEventsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, query: Optional[Dict[str, Any]] = None, **opts: Any) -> _AsyncPageCall[types.Event]:
        """List events"""
        return self._c._page(OPS["listEvents"], query=query, opts=opts)

    async def get(self, event_id: str, **opts: Any) -> types.Event:
        """Get an event"""
        return await self._c._one(OPS["getEvent"], path={"event_id": event_id}, opts=opts)


class AsyncExitsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    async def get(self, account_id: str, market: str, **opts: Any) -> types.Exits:
        """Get a position's exits"""
        return await self._c._one(OPS["getExits"], path={"account_id": account_id, "market": market}, opts=opts)

    async def set(self, account_id: str, market: str, body: Mapping[str, Any], **opts: Any) -> types.Exits:
        """Change a position's exits"""
        return await self._c._one(OPS["setExits"], path={"account_id": account_id, "market": market}, body=body, opts=opts)


class AsyncStrategiesResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    async def create(self, account_id: str, body: Mapping[str, Any], **opts: Any) -> types.Strategy:
        """Create a strategy"""
        return await self._c._one(OPS["createStrategy"], path={"account_id": account_id}, body=body, opts=opts)

    def list(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> _AsyncPageCall[types.Strategy]:
        """List strategies"""
        return self._c._page(OPS["listStrategies"], path={"account_id": account_id}, query=query, opts=opts)

    async def get(self, account_id: str, strategy_id: str, **opts: Any) -> types.Strategy:
        """Get a strategy"""
        return await self._c._one(OPS["getStrategy"], path={"account_id": account_id, "strategy_id": strategy_id}, opts=opts)

    async def cancel(self, account_id: str, strategy_id: str, **opts: Any) -> types.Strategy:
        """Cancel a strategy"""
        return await self._c._one(OPS["cancelStrategy"], path={"account_id": account_id, "strategy_id": strategy_id}, opts=opts)


class AsyncWebhookEndpointsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, **opts: Any) -> _AsyncPageCall[types.WebhookEndpoint]:
        """List webhook endpoints"""
        return self._c._page(OPS["listWebhookEndpoints"], opts=opts)

    async def get(self, endpoint_id: str, **opts: Any) -> types.WebhookEndpoint:
        """Get a webhook endpoint"""
        return await self._c._one(OPS["getWebhookEndpoint"], path={"endpoint_id": endpoint_id}, opts=opts)

    async def delete(self, endpoint_id: str, **opts: Any) -> types.DeletedEndpoint:
        """Delete an endpoint"""
        return await self._c._one(OPS["deleteWebhookEndpoint"], path={"endpoint_id": endpoint_id}, opts=opts)

    async def test(self, endpoint_id: str, **opts: Any) -> types.WebhookDelivery:
        """Send a test"""
        return await self._c._one(OPS["testWebhookEndpoint"], path={"endpoint_id": endpoint_id}, opts=opts)

    def list_deliveries(self, endpoint_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> _AsyncPageCall[types.WebhookDelivery]:
        """List deliveries"""
        return self._c._page(OPS["listWebhookDeliveries"], path={"endpoint_id": endpoint_id}, query=query, opts=opts)

    async def get_delivery(self, endpoint_id: str, delivery_id: str, **opts: Any) -> types.WebhookDelivery:
        """Get a delivery"""
        return await self._c._one(OPS["getWebhookDelivery"], path={"endpoint_id": endpoint_id, "delivery_id": delivery_id}, opts=opts)

    async def resend_delivery(self, endpoint_id: str, delivery_id: str, **opts: Any) -> types.WebhookDelivery:
        """Send a delivery again"""
        return await self._c._one(OPS["resendWebhookDelivery"], path={"endpoint_id": endpoint_id, "delivery_id": delivery_id}, opts=opts)

    async def disable(self, endpoint_id: str, **opts: Any) -> types.WebhookEndpoint:
        """Pause an endpoint"""
        return await self._c._one(OPS["disableWebhookEndpoint"], path={"endpoint_id": endpoint_id}, opts=opts)

    async def enable(self, endpoint_id: str, **opts: Any) -> types.WebhookEndpoint:
        """Resume an endpoint"""
        return await self._c._one(OPS["enableWebhookEndpoint"], path={"endpoint_id": endpoint_id}, opts=opts)

    def event_types(self, **opts: Any) -> _AsyncPageCall[types.WebhookEventType]:
        """List event types"""
        return self._c._page(OPS["listWebhookEventTypes"], opts=opts)


class AsyncTradingviewResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list_hooks(self, **opts: Any) -> _AsyncPageCall[types.TradingViewHook]:
        """List TradingView links"""
        return self._c._page(OPS["listTradingViewHooks"], opts=opts)

    async def get_hook(self, hook_id: str, **opts: Any) -> types.TradingViewHook:
        """Get a TradingView link"""
        return await self._c._one(OPS["getTradingViewHook"], path={"hook_id": hook_id}, opts=opts)

    async def delete_hook(self, hook_id: str, **opts: Any) -> types.DeletedHook:
        """Revoke a TradingView link"""
        return await self._c._one(OPS["deleteTradingViewHook"], path={"hook_id": hook_id}, opts=opts)

    def list_alerts(self, hook_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> _AsyncPageCall[types.TradingViewAlert]:
        """List a link's alerts"""
        return self._c._page(OPS["listTradingViewAlerts"], path={"hook_id": hook_id}, query=query, opts=opts)

    async def get_alert(self, hook_id: str, alert_id: str, **opts: Any) -> types.TradingViewAlert:
        """Get an alert"""
        return await self._c._one(OPS["getTradingViewAlert"], path={"hook_id": hook_id, "alert_id": alert_id}, opts=opts)

    async def disable_hook(self, hook_id: str, **opts: Any) -> types.TradingViewHook:
        """Pause a TradingView link"""
        return await self._c._one(OPS["disableTradingViewHook"], path={"hook_id": hook_id}, opts=opts)

    async def enable_hook(self, hook_id: str, **opts: Any) -> types.TradingViewHook:
        """Resume a TradingView link"""
        return await self._c._one(OPS["enableTradingViewHook"], path={"hook_id": hook_id}, opts=opts)

    async def parse(self, body: Mapping[str, Any], **opts: Any) -> types.TradingViewParse:
        """Read an alert message"""
        return await self._c._one(OPS["parseTradingViewMessage"], body=body, opts=opts)


class AsyncFillsResource:
    def __init__(self, client: Any) -> None:
        self._c = client

    def list(self, account_id: str, query: Optional[Dict[str, Any]] = None, **opts: Any) -> _AsyncPageCall[types.Fill]:
        """List fills"""
        return self._c._page(OPS["listFills"], path={"account_id": account_id}, query=query, opts=opts)


RESOURCES = {
    "platform": (PlatformResource, AsyncPlatformResource),
    "me": (MeResource, AsyncMeResource),
    "markets": (MarketsResource, AsyncMarketsResource),
    "accounts": (AccountsResource, AsyncAccountsResource),
    "history": (HistoryResource, AsyncHistoryResource),
    "margin": (MarginResource, AsyncMarginResource),
    "positions": (PositionsResource, AsyncPositionsResource),
    "orders": (OrdersResource, AsyncOrdersResource),
    "events": (EventsResource, AsyncEventsResource),
    "exits": (ExitsResource, AsyncExitsResource),
    "strategies": (StrategiesResource, AsyncStrategiesResource),
    "webhook_endpoints": (WebhookEndpointsResource, AsyncWebhookEndpointsResource),
    "tradingview": (TradingviewResource, AsyncTradingviewResource),
    "fills": (FillsResource, AsyncFillsResource),
}


class Resources:
    platform: PlatformResource
    me: MeResource
    markets: MarketsResource
    accounts: AccountsResource
    history: HistoryResource
    margin: MarginResource
    positions: PositionsResource
    orders: OrdersResource
    events: EventsResource
    exits: ExitsResource
    strategies: StrategiesResource
    webhook_endpoints: WebhookEndpointsResource
    tradingview: TradingviewResource
    fills: FillsResource


class AsyncResources:
    platform: AsyncPlatformResource
    me: AsyncMeResource
    markets: AsyncMarketsResource
    accounts: AsyncAccountsResource
    history: AsyncHistoryResource
    margin: AsyncMarginResource
    positions: AsyncPositionsResource
    orders: AsyncOrdersResource
    events: AsyncEventsResource
    exits: AsyncExitsResource
    strategies: AsyncStrategiesResource
    webhook_endpoints: AsyncWebhookEndpointsResource
    tradingview: AsyncTradingviewResource
    fills: AsyncFillsResource
