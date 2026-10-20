// Code generated from the API spec by generator/printers/go.js. DO NOT EDIT: run `node generator`.

package phoenixperps

import (
	"context"
	"iter"
)

var (
	opGetApi                  = &operation{id: "getApi", method: "GET", path: "/v1", idempotent: false, retrySafe: true, cursor: "", scope: "public"}
	opGetSpec                 = &operation{id: "getSpec", method: "GET", path: "/v1/openapi.json", idempotent: false, retrySafe: true, cursor: "", scope: "public"}
	opGetStatus               = &operation{id: "getStatus", method: "GET", path: "/v1/status", idempotent: false, retrySafe: true, cursor: "", scope: "public"}
	opGetLimits               = &operation{id: "getLimits", method: "GET", path: "/v1/limits", idempotent: false, retrySafe: true, cursor: "", scope: "public"}
	opGetMe                   = &operation{id: "getMe", method: "GET", path: "/v1/me", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opListMarkets             = &operation{id: "listMarkets", method: "GET", path: "/v1/markets", idempotent: false, retrySafe: true, cursor: "", scope: "public"}
	opListMarketStats         = &operation{id: "listMarketStats", method: "GET", path: "/v1/markets/stats", idempotent: false, retrySafe: true, cursor: "", scope: "public"}
	opGetMarket               = &operation{id: "getMarket", method: "GET", path: "/v1/markets/{market}", idempotent: false, retrySafe: true, cursor: "", scope: "public"}
	opListAccounts            = &operation{id: "listAccounts", method: "GET", path: "/v1/accounts", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opCreateSandboxAccount    = &operation{id: "createSandboxAccount", method: "POST", path: "/v1/accounts", idempotent: true, retrySafe: false, cursor: "", scope: "trade"}
	opGetAccount              = &operation{id: "getAccount", method: "GET", path: "/v1/accounts/{account_id}", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opGetRisk                 = &operation{id: "getRisk", method: "GET", path: "/v1/accounts/{account_id}/risk", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opGetTradingPolicy        = &operation{id: "getTradingPolicy", method: "GET", path: "/v1/accounts/{account_id}/trading-policy", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opGetProgress             = &operation{id: "getProgress", method: "GET", path: "/v1/accounts/{account_id}/progress", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opGetEquityHistory        = &operation{id: "getEquityHistory", method: "GET", path: "/v1/accounts/{account_id}/history/equity", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opListDailyResults        = &operation{id: "listDailyResults", method: "GET", path: "/v1/accounts/{account_id}/history/daily", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opListMarginSettings      = &operation{id: "listMarginSettings", method: "GET", path: "/v1/accounts/{account_id}/margin", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opListPositions           = &operation{id: "listPositions", method: "GET", path: "/v1/accounts/{account_id}/positions", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opGetPosition             = &operation{id: "getPosition", method: "GET", path: "/v1/accounts/{account_id}/positions/{market}", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opListClosedPositions     = &operation{id: "listClosedPositions", method: "GET", path: "/v1/accounts/{account_id}/closed-positions", idempotent: false, retrySafe: true, cursor: "cursor", scope: "read"}
	opListOrders              = &operation{id: "listOrders", method: "GET", path: "/v1/accounts/{account_id}/orders", idempotent: false, retrySafe: true, cursor: "cursor", scope: "read"}
	opPlaceOrder              = &operation{id: "placeOrder", method: "POST", path: "/v1/accounts/{account_id}/orders", idempotent: true, retrySafe: false, cursor: "", scope: "trade"}
	opGetOrder                = &operation{id: "getOrder", method: "GET", path: "/v1/accounts/{account_id}/orders/{order_id}", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opModifyOrder             = &operation{id: "modifyOrder", method: "PATCH", path: "/v1/accounts/{account_id}/orders/{order_id}", idempotent: false, retrySafe: false, cursor: "", scope: "trade"}
	opCancelOrder             = &operation{id: "cancelOrder", method: "DELETE", path: "/v1/accounts/{account_id}/orders/{order_id}", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opTestOrder               = &operation{id: "testOrder", method: "POST", path: "/v1/accounts/{account_id}/orders/test", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opCancelAllOrders         = &operation{id: "cancelAllOrders", method: "POST", path: "/v1/accounts/{account_id}/orders/cancel-all", idempotent: true, retrySafe: false, cursor: "", scope: "trade"}
	opClosePosition           = &operation{id: "closePosition", method: "POST", path: "/v1/accounts/{account_id}/positions/{market}/close", idempotent: true, retrySafe: false, cursor: "", scope: "trade"}
	opFlatten                 = &operation{id: "flatten", method: "POST", path: "/v1/accounts/{account_id}/flatten", idempotent: true, retrySafe: false, cursor: "", scope: "trade"}
	opSetMargin               = &operation{id: "setMargin", method: "PUT", path: "/v1/accounts/{account_id}/margin/{market}", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opListEvents              = &operation{id: "listEvents", method: "GET", path: "/v1/events", idempotent: false, retrySafe: true, cursor: "after", scope: "read"}
	opGetExits                = &operation{id: "getExits", method: "GET", path: "/v1/accounts/{account_id}/positions/{market}/exits", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opSetExits                = &operation{id: "setExits", method: "PUT", path: "/v1/accounts/{account_id}/positions/{market}/exits", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opSetOrderExits           = &operation{id: "setOrderExits", method: "PUT", path: "/v1/accounts/{account_id}/orders/{order_id}/exits", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opCreateStrategy          = &operation{id: "createStrategy", method: "POST", path: "/v1/accounts/{account_id}/strategies", idempotent: true, retrySafe: false, cursor: "", scope: "trade"}
	opListStrategies          = &operation{id: "listStrategies", method: "GET", path: "/v1/accounts/{account_id}/strategies", idempotent: false, retrySafe: true, cursor: "cursor", scope: "read"}
	opGetStrategy             = &operation{id: "getStrategy", method: "GET", path: "/v1/accounts/{account_id}/strategies/{strategy_id}", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opCancelStrategy          = &operation{id: "cancelStrategy", method: "DELETE", path: "/v1/accounts/{account_id}/strategies/{strategy_id}", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opListWebhookEndpoints    = &operation{id: "listWebhookEndpoints", method: "GET", path: "/v1/webhook-endpoints", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opGetWebhookEndpoint      = &operation{id: "getWebhookEndpoint", method: "GET", path: "/v1/webhook-endpoints/{endpoint_id}", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opDeleteWebhookEndpoint   = &operation{id: "deleteWebhookEndpoint", method: "DELETE", path: "/v1/webhook-endpoints/{endpoint_id}", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opTestWebhookEndpoint     = &operation{id: "testWebhookEndpoint", method: "POST", path: "/v1/webhook-endpoints/{endpoint_id}/test", idempotent: false, retrySafe: false, cursor: "", scope: "trade"}
	opListWebhookDeliveries   = &operation{id: "listWebhookDeliveries", method: "GET", path: "/v1/webhook-endpoints/{endpoint_id}/deliveries", idempotent: false, retrySafe: true, cursor: "cursor", scope: "read"}
	opGetWebhookDelivery      = &operation{id: "getWebhookDelivery", method: "GET", path: "/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opResendWebhookDelivery   = &operation{id: "resendWebhookDelivery", method: "POST", path: "/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}/resend", idempotent: false, retrySafe: false, cursor: "", scope: "trade"}
	opDisableWebhookEndpoint  = &operation{id: "disableWebhookEndpoint", method: "POST", path: "/v1/webhook-endpoints/{endpoint_id}/disable", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opEnableWebhookEndpoint   = &operation{id: "enableWebhookEndpoint", method: "POST", path: "/v1/webhook-endpoints/{endpoint_id}/enable", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opListTradingViewHooks    = &operation{id: "listTradingViewHooks", method: "GET", path: "/v1/tradingview/hooks", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opGetTradingViewHook      = &operation{id: "getTradingViewHook", method: "GET", path: "/v1/tradingview/hooks/{hook_id}", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opDeleteTradingViewHook   = &operation{id: "deleteTradingViewHook", method: "DELETE", path: "/v1/tradingview/hooks/{hook_id}", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opListTradingViewAlerts   = &operation{id: "listTradingViewAlerts", method: "GET", path: "/v1/tradingview/hooks/{hook_id}/alerts", idempotent: false, retrySafe: true, cursor: "cursor", scope: "read"}
	opGetTradingViewAlert     = &operation{id: "getTradingViewAlert", method: "GET", path: "/v1/tradingview/hooks/{hook_id}/alerts/{alert_id}", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opDisableTradingViewHook  = &operation{id: "disableTradingViewHook", method: "POST", path: "/v1/tradingview/hooks/{hook_id}/disable", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opEnableTradingViewHook   = &operation{id: "enableTradingViewHook", method: "POST", path: "/v1/tradingview/hooks/{hook_id}/enable", idempotent: false, retrySafe: true, cursor: "", scope: "trade"}
	opParseTradingViewMessage = &operation{id: "parseTradingViewMessage", method: "POST", path: "/v1/tradingview/parse", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opListWebhookEventTypes   = &operation{id: "listWebhookEventTypes", method: "GET", path: "/v1/webhook-event-types", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opGetEvent                = &operation{id: "getEvent", method: "GET", path: "/v1/events/{event_id}", idempotent: false, retrySafe: true, cursor: "", scope: "read"}
	opListFills               = &operation{id: "listFills", method: "GET", path: "/v1/accounts/{account_id}/fills", idempotent: false, retrySafe: true, cursor: "cursor", scope: "read"}
)

// the operations, for Client.Do (its route's retry-safety and budget)
func init() {
	operations = []*operation{opGetApi, opGetSpec, opGetStatus, opGetLimits, opGetMe, opListMarkets, opListMarketStats, opGetMarket, opListAccounts, opCreateSandboxAccount, opGetAccount, opGetRisk, opGetTradingPolicy, opGetProgress, opGetEquityHistory, opListDailyResults, opListMarginSettings, opListPositions, opGetPosition, opListClosedPositions, opListOrders, opPlaceOrder, opGetOrder, opModifyOrder, opCancelOrder, opTestOrder, opCancelAllOrders, opClosePosition, opFlatten, opSetMargin, opListEvents, opGetExits, opSetExits, opSetOrderExits, opCreateStrategy, opListStrategies, opGetStrategy, opCancelStrategy, opListWebhookEndpoints, opGetWebhookEndpoint, opDeleteWebhookEndpoint, opTestWebhookEndpoint, opListWebhookDeliveries, opGetWebhookDelivery, opResendWebhookDelivery, opDisableWebhookEndpoint, opEnableWebhookEndpoint, opListTradingViewHooks, opGetTradingViewHook, opDeleteTradingViewHook, opListTradingViewAlerts, opGetTradingViewAlert, opDisableTradingViewHook, opEnableTradingViewHook, opParseTradingViewMessage, opListWebhookEventTypes, opGetEvent, opListFills}
}

// resources are the services of a Client, one per resource of the API.
type resources struct {
	Platform         *PlatformService
	Me               *MeService
	Markets          *MarketsService
	Accounts         *AccountsService
	History          *HistoryService
	Margin           *MarginService
	Positions        *PositionsService
	Orders           *OrdersService
	Events           *EventsService
	Exits            *ExitsService
	Strategies       *StrategiesService
	WebhookEndpoints *WebhookEndpointsService
	TradingView      *TradingViewService
	Fills            *FillsService
}

func (c *Client) initResources() {
	c.Platform = &PlatformService{c}
	c.Me = &MeService{c}
	c.Markets = &MarketsService{c}
	c.Accounts = &AccountsService{c}
	c.History = &HistoryService{c}
	c.Margin = &MarginService{c}
	c.Positions = &PositionsService{c}
	c.Orders = &OrdersService{c}
	c.Events = &EventsService{c}
	c.Exits = &ExitsService{c}
	c.Strategies = &StrategiesService{c}
	c.WebhookEndpoints = &WebhookEndpointsService{c}
	c.TradingView = &TradingViewService{c}
	c.Fills = &FillsService{c}
}

// PlatformService: the platform calls (client.Platform).
type PlatformService struct{ c *Client }

// Info: Get the API.
func (r *PlatformService) Info(ctx context.Context, opts ...CallOption) (*Platform, error) {
	return oneOf[Platform](ctx, r.c, opGetApi, nil, nil, nil, opts)
}

// Spec: Get the OpenAPI document.
func (r *PlatformService) Spec(ctx context.Context, opts ...CallOption) (map[string]any, error) {
	var out map[string]any
	if err := r.c.one(ctx, opGetSpec, nil, nil, nil, &out, opts); err != nil {
		return nil, err
	}
	return out, nil
}

// Status: Get the status.
func (r *PlatformService) Status(ctx context.Context, opts ...CallOption) (*Status, error) {
	return oneOf[Status](ctx, r.c, opGetStatus, nil, nil, nil, opts)
}

// Limits: Get the limits.
func (r *PlatformService) Limits(ctx context.Context, opts ...CallOption) (*Limits, error) {
	return oneOf[Limits](ctx, r.c, opGetLimits, nil, nil, nil, opts)
}

// MeService: the me calls (client.Me).
type MeService struct{ c *Client }

// Get: Get your key.
func (r *MeService) Get(ctx context.Context, opts ...CallOption) (*Me, error) {
	return oneOf[Me](ctx, r.c, opGetMe, nil, nil, nil, opts)
}

// MarketsService: the markets calls (client.Markets).
type MarketsService struct{ c *Client }

// List: List markets.
func (r *MarketsService) List(ctx context.Context, opts ...CallOption) (*Page[Market], error) {
	return listPage[Market](ctx, r.c, opListMarkets, nil, nil, opts)
}

// Stats: List market stats.
func (r *MarketsService) Stats(ctx context.Context, opts ...CallOption) (*Page[MarketStats], error) {
	return listPage[MarketStats](ctx, r.c, opListMarketStats, nil, nil, opts)
}

// Get: Get a market.
func (r *MarketsService) Get(ctx context.Context, market string, opts ...CallOption) (*Market, error) {
	return oneOf[Market](ctx, r.c, opGetMarket, []string{market}, nil, nil, opts)
}

// AccountsService: the accounts calls (client.Accounts).
type AccountsService struct{ c *Client }

// List: List accounts.
func (r *AccountsService) List(ctx context.Context, opts ...CallOption) (*Page[Account], error) {
	return listPage[Account](ctx, r.c, opListAccounts, nil, nil, opts)
}

// CreateSandbox: Open a sandbox account.
func (r *AccountsService) CreateSandbox(ctx context.Context, opts ...CallOption) (*Account, error) {
	return oneOf[Account](ctx, r.c, opCreateSandboxAccount, nil, nil, nil, opts)
}

// Get: Get an account.
func (r *AccountsService) Get(ctx context.Context, accountID string, opts ...CallOption) (*AccountDetail, error) {
	return oneOf[AccountDetail](ctx, r.c, opGetAccount, []string{accountID}, nil, nil, opts)
}

// Risk: Get live risk.
func (r *AccountsService) Risk(ctx context.Context, accountID string, opts ...CallOption) (*AccountRisk, error) {
	return oneOf[AccountRisk](ctx, r.c, opGetRisk, []string{accountID}, nil, nil, opts)
}

// TradingPolicy: Get the trading policy.
func (r *AccountsService) TradingPolicy(ctx context.Context, accountID string, opts ...CallOption) (*TradingPolicy, error) {
	return oneOf[TradingPolicy](ctx, r.c, opGetTradingPolicy, []string{accountID}, nil, nil, opts)
}

// Progress: Get progress.
func (r *AccountsService) Progress(ctx context.Context, accountID string, opts ...CallOption) (*Progress, error) {
	return oneOf[Progress](ctx, r.c, opGetProgress, []string{accountID}, nil, nil, opts)
}

// HistoryService: the history calls (client.History).
type HistoryService struct{ c *Client }

// Equity: Get the equity curve.
func (r *HistoryService) Equity(ctx context.Context, accountID string, params *HistoryEquityParams, opts ...CallOption) (*EquityHistory, error) {
	return oneOf[EquityHistory](ctx, r.c, opGetEquityHistory, []string{accountID}, params.values(), nil, opts)
}

// Daily: List daily results.
func (r *HistoryService) Daily(ctx context.Context, accountID string, opts ...CallOption) (*Page[DailyResult], error) {
	return listPage[DailyResult](ctx, r.c, opListDailyResults, []string{accountID}, nil, opts)
}

// MarginService: the margin calls (client.Margin).
type MarginService struct{ c *Client }

// List: List margin settings.
func (r *MarginService) List(ctx context.Context, accountID string, opts ...CallOption) (*Page[MarginSetting], error) {
	return listPage[MarginSetting](ctx, r.c, opListMarginSettings, []string{accountID}, nil, opts)
}

// Set: Set leverage and margin mode.
func (r *MarginService) Set(ctx context.Context, accountID string, market string, body MarginSettingInput, opts ...CallOption) (*MarginSettingResult, error) {
	return oneOf[MarginSettingResult](ctx, r.c, opSetMargin, []string{accountID, market}, nil, body, opts)
}

// PositionsService: the positions calls (client.Positions).
type PositionsService struct{ c *Client }

// List: List open positions.
func (r *PositionsService) List(ctx context.Context, accountID string, params *PositionsListParams, opts ...CallOption) (*Page[Position], error) {
	return listPage[Position](ctx, r.c, opListPositions, []string{accountID}, params.values(), opts)
}

// Get: Get an open position.
func (r *PositionsService) Get(ctx context.Context, accountID string, market string, opts ...CallOption) (*Position, error) {
	return oneOf[Position](ctx, r.c, opGetPosition, []string{accountID, market}, nil, nil, opts)
}

// ListClosed: List closed positions.
func (r *PositionsService) ListClosed(ctx context.Context, accountID string, params *PositionsListClosedParams, opts ...CallOption) (*Page[ClosedPosition], error) {
	return listPage[ClosedPosition](ctx, r.c, opListClosedPositions, []string{accountID}, params.values(), opts)
}

// ListClosedAutoPaging: every item of ListClosed, page after page (WithMaxItems caps it).
func (r *PositionsService) ListClosedAutoPaging(ctx context.Context, accountID string, params *PositionsListClosedParams, opts ...CallOption) iter.Seq2[ClosedPosition, error] {
	return autoPaging(ctx, func() (*Page[ClosedPosition], error) { return r.ListClosed(ctx, accountID, params, opts...) }, opts)
}

// Close: Close a position.
func (r *PositionsService) Close(ctx context.Context, accountID string, market string, body ClosePosition, opts ...CallOption) (*Order, error) {
	return oneOf[Order](ctx, r.c, opClosePosition, []string{accountID, market}, nil, body, opts)
}

// Flatten: Flatten the account.
func (r *PositionsService) Flatten(ctx context.Context, accountID string, opts ...CallOption) (*FlattenResult, error) {
	return oneOf[FlattenResult](ctx, r.c, opFlatten, []string{accountID}, nil, nil, opts)
}

// OrdersService: the orders calls (client.Orders).
type OrdersService struct{ c *Client }

// List: List orders.
func (r *OrdersService) List(ctx context.Context, accountID string, params *OrdersListParams, opts ...CallOption) (*Page[Order], error) {
	return listPage[Order](ctx, r.c, opListOrders, []string{accountID}, params.values(), opts)
}

// ListAutoPaging: every item of List, page after page (WithMaxItems caps it).
func (r *OrdersService) ListAutoPaging(ctx context.Context, accountID string, params *OrdersListParams, opts ...CallOption) iter.Seq2[Order, error] {
	return autoPaging(ctx, func() (*Page[Order], error) { return r.List(ctx, accountID, params, opts...) }, opts)
}

// Create: Place an order.
func (r *OrdersService) Create(ctx context.Context, accountID string, body CreateOrder, opts ...CallOption) (*Order, error) {
	return oneOf[Order](ctx, r.c, opPlaceOrder, []string{accountID}, nil, body, opts)
}

// Get: Get an order.
func (r *OrdersService) Get(ctx context.Context, accountID string, orderID string, opts ...CallOption) (*Order, error) {
	return oneOf[Order](ctx, r.c, opGetOrder, []string{accountID, orderID}, nil, nil, opts)
}

// Modify: Change an order.
func (r *OrdersService) Modify(ctx context.Context, accountID string, orderID string, body ModifyOrder, opts ...CallOption) (*Order, error) {
	return oneOf[Order](ctx, r.c, opModifyOrder, []string{accountID, orderID}, nil, body, opts)
}

// Cancel: Cancel an order.
func (r *OrdersService) Cancel(ctx context.Context, accountID string, orderID string, opts ...CallOption) (*Order, error) {
	return oneOf[Order](ctx, r.c, opCancelOrder, []string{accountID, orderID}, nil, nil, opts)
}

// Preview: Preview an order.
func (r *OrdersService) Preview(ctx context.Context, accountID string, body CreateOrder, opts ...CallOption) (*WhatIf, error) {
	return oneOf[WhatIf](ctx, r.c, opTestOrder, []string{accountID}, nil, body, opts)
}

// CancelAll: Cancel all orders.
func (r *OrdersService) CancelAll(ctx context.Context, accountID string, body CancelAll, opts ...CallOption) (*CancelAllResult, error) {
	return oneOf[CancelAllResult](ctx, r.c, opCancelAllOrders, []string{accountID}, nil, body, opts)
}

// SetExits: Attach exits to an order.
func (r *OrdersService) SetExits(ctx context.Context, accountID string, orderID string, body OrderExits, opts ...CallOption) (*Order, error) {
	return oneOf[Order](ctx, r.c, opSetOrderExits, []string{accountID, orderID}, nil, body, opts)
}

// EventsService: the events calls (client.Events).
type EventsService struct{ c *Client }

// List: List events.
func (r *EventsService) List(ctx context.Context, params *EventsListParams, opts ...CallOption) (*Page[Event], error) {
	return listPage[Event](ctx, r.c, opListEvents, nil, params.values(), opts)
}

// ListAutoPaging: every item of List, page after page (WithMaxItems caps it).
func (r *EventsService) ListAutoPaging(ctx context.Context, params *EventsListParams, opts ...CallOption) iter.Seq2[Event, error] {
	return autoPaging(ctx, func() (*Page[Event], error) { return r.List(ctx, params, opts...) }, opts)
}

// Get: Get an event.
func (r *EventsService) Get(ctx context.Context, eventID string, opts ...CallOption) (*Event, error) {
	return oneOf[Event](ctx, r.c, opGetEvent, []string{eventID}, nil, nil, opts)
}

// ExitsService: the exits calls (client.Exits).
type ExitsService struct{ c *Client }

// Get: Get a position's exits.
func (r *ExitsService) Get(ctx context.Context, accountID string, market string, opts ...CallOption) (*Exits, error) {
	return oneOf[Exits](ctx, r.c, opGetExits, []string{accountID, market}, nil, nil, opts)
}

// Set: Change a position's exits.
func (r *ExitsService) Set(ctx context.Context, accountID string, market string, body ExitsBatch, opts ...CallOption) (*Exits, error) {
	return oneOf[Exits](ctx, r.c, opSetExits, []string{accountID, market}, nil, body, opts)
}

// StrategiesService: the strategies calls (client.Strategies).
type StrategiesService struct{ c *Client }

// Create: Create a strategy.
func (r *StrategiesService) Create(ctx context.Context, accountID string, body CreateStrategy, opts ...CallOption) (*Strategy, error) {
	return oneOf[Strategy](ctx, r.c, opCreateStrategy, []string{accountID}, nil, body, opts)
}

// List: List strategies.
func (r *StrategiesService) List(ctx context.Context, accountID string, params *StrategiesListParams, opts ...CallOption) (*Page[Strategy], error) {
	return listPage[Strategy](ctx, r.c, opListStrategies, []string{accountID}, params.values(), opts)
}

// ListAutoPaging: every item of List, page after page (WithMaxItems caps it).
func (r *StrategiesService) ListAutoPaging(ctx context.Context, accountID string, params *StrategiesListParams, opts ...CallOption) iter.Seq2[Strategy, error] {
	return autoPaging(ctx, func() (*Page[Strategy], error) { return r.List(ctx, accountID, params, opts...) }, opts)
}

// Get: Get a strategy.
func (r *StrategiesService) Get(ctx context.Context, accountID string, strategyID string, opts ...CallOption) (*Strategy, error) {
	return oneOf[Strategy](ctx, r.c, opGetStrategy, []string{accountID, strategyID}, nil, nil, opts)
}

// Cancel: Cancel a strategy.
func (r *StrategiesService) Cancel(ctx context.Context, accountID string, strategyID string, opts ...CallOption) (*Strategy, error) {
	return oneOf[Strategy](ctx, r.c, opCancelStrategy, []string{accountID, strategyID}, nil, nil, opts)
}

// WebhookEndpointsService: the webhookEndpoints calls (client.WebhookEndpoints).
type WebhookEndpointsService struct{ c *Client }

// List: List webhook endpoints.
func (r *WebhookEndpointsService) List(ctx context.Context, opts ...CallOption) (*Page[WebhookEndpoint], error) {
	return listPage[WebhookEndpoint](ctx, r.c, opListWebhookEndpoints, nil, nil, opts)
}

// Get: Get a webhook endpoint.
func (r *WebhookEndpointsService) Get(ctx context.Context, endpointID string, opts ...CallOption) (*WebhookEndpoint, error) {
	return oneOf[WebhookEndpoint](ctx, r.c, opGetWebhookEndpoint, []string{endpointID}, nil, nil, opts)
}

// Delete: Delete an endpoint.
func (r *WebhookEndpointsService) Delete(ctx context.Context, endpointID string, opts ...CallOption) (*DeletedEndpoint, error) {
	return oneOf[DeletedEndpoint](ctx, r.c, opDeleteWebhookEndpoint, []string{endpointID}, nil, nil, opts)
}

// Test: Send a test.
func (r *WebhookEndpointsService) Test(ctx context.Context, endpointID string, opts ...CallOption) (*WebhookDelivery, error) {
	return oneOf[WebhookDelivery](ctx, r.c, opTestWebhookEndpoint, []string{endpointID}, nil, nil, opts)
}

// ListDeliveries: List deliveries.
func (r *WebhookEndpointsService) ListDeliveries(ctx context.Context, endpointID string, params *WebhookEndpointsListDeliveriesParams, opts ...CallOption) (*Page[WebhookDelivery], error) {
	return listPage[WebhookDelivery](ctx, r.c, opListWebhookDeliveries, []string{endpointID}, params.values(), opts)
}

// ListDeliveriesAutoPaging: every item of ListDeliveries, page after page (WithMaxItems caps it).
func (r *WebhookEndpointsService) ListDeliveriesAutoPaging(ctx context.Context, endpointID string, params *WebhookEndpointsListDeliveriesParams, opts ...CallOption) iter.Seq2[WebhookDelivery, error] {
	return autoPaging(ctx, func() (*Page[WebhookDelivery], error) { return r.ListDeliveries(ctx, endpointID, params, opts...) }, opts)
}

// GetDelivery: Get a delivery.
func (r *WebhookEndpointsService) GetDelivery(ctx context.Context, endpointID string, deliveryID string, opts ...CallOption) (*WebhookDelivery, error) {
	return oneOf[WebhookDelivery](ctx, r.c, opGetWebhookDelivery, []string{endpointID, deliveryID}, nil, nil, opts)
}

// ResendDelivery: Send a delivery again.
func (r *WebhookEndpointsService) ResendDelivery(ctx context.Context, endpointID string, deliveryID string, opts ...CallOption) (*WebhookDelivery, error) {
	return oneOf[WebhookDelivery](ctx, r.c, opResendWebhookDelivery, []string{endpointID, deliveryID}, nil, nil, opts)
}

// Disable: Pause an endpoint.
func (r *WebhookEndpointsService) Disable(ctx context.Context, endpointID string, opts ...CallOption) (*WebhookEndpoint, error) {
	return oneOf[WebhookEndpoint](ctx, r.c, opDisableWebhookEndpoint, []string{endpointID}, nil, nil, opts)
}

// Enable: Resume an endpoint.
func (r *WebhookEndpointsService) Enable(ctx context.Context, endpointID string, opts ...CallOption) (*WebhookEndpoint, error) {
	return oneOf[WebhookEndpoint](ctx, r.c, opEnableWebhookEndpoint, []string{endpointID}, nil, nil, opts)
}

// EventTypes: List event types.
func (r *WebhookEndpointsService) EventTypes(ctx context.Context, opts ...CallOption) (*Page[WebhookEventType], error) {
	return listPage[WebhookEventType](ctx, r.c, opListWebhookEventTypes, nil, nil, opts)
}

// TradingViewService: the tradingview calls (client.TradingView).
type TradingViewService struct{ c *Client }

// ListHooks: List TradingView links.
func (r *TradingViewService) ListHooks(ctx context.Context, opts ...CallOption) (*Page[TradingViewHook], error) {
	return listPage[TradingViewHook](ctx, r.c, opListTradingViewHooks, nil, nil, opts)
}

// GetHook: Get a TradingView link.
func (r *TradingViewService) GetHook(ctx context.Context, hookID string, opts ...CallOption) (*TradingViewHook, error) {
	return oneOf[TradingViewHook](ctx, r.c, opGetTradingViewHook, []string{hookID}, nil, nil, opts)
}

// DeleteHook: Revoke a TradingView link.
func (r *TradingViewService) DeleteHook(ctx context.Context, hookID string, opts ...CallOption) (*DeletedHook, error) {
	return oneOf[DeletedHook](ctx, r.c, opDeleteTradingViewHook, []string{hookID}, nil, nil, opts)
}

// ListAlerts: List a link's alerts.
func (r *TradingViewService) ListAlerts(ctx context.Context, hookID string, params *TradingviewListAlertsParams, opts ...CallOption) (*Page[TradingViewAlert], error) {
	return listPage[TradingViewAlert](ctx, r.c, opListTradingViewAlerts, []string{hookID}, params.values(), opts)
}

// ListAlertsAutoPaging: every item of ListAlerts, page after page (WithMaxItems caps it).
func (r *TradingViewService) ListAlertsAutoPaging(ctx context.Context, hookID string, params *TradingviewListAlertsParams, opts ...CallOption) iter.Seq2[TradingViewAlert, error] {
	return autoPaging(ctx, func() (*Page[TradingViewAlert], error) { return r.ListAlerts(ctx, hookID, params, opts...) }, opts)
}

// GetAlert: Get an alert.
func (r *TradingViewService) GetAlert(ctx context.Context, hookID string, alertID string, opts ...CallOption) (*TradingViewAlert, error) {
	return oneOf[TradingViewAlert](ctx, r.c, opGetTradingViewAlert, []string{hookID, alertID}, nil, nil, opts)
}

// DisableHook: Pause a TradingView link.
func (r *TradingViewService) DisableHook(ctx context.Context, hookID string, opts ...CallOption) (*TradingViewHook, error) {
	return oneOf[TradingViewHook](ctx, r.c, opDisableTradingViewHook, []string{hookID}, nil, nil, opts)
}

// EnableHook: Resume a TradingView link.
func (r *TradingViewService) EnableHook(ctx context.Context, hookID string, opts ...CallOption) (*TradingViewHook, error) {
	return oneOf[TradingViewHook](ctx, r.c, opEnableTradingViewHook, []string{hookID}, nil, nil, opts)
}

// Parse: Read an alert message.
func (r *TradingViewService) Parse(ctx context.Context, body TradingViewParseInput, opts ...CallOption) (*TradingViewParse, error) {
	return oneOf[TradingViewParse](ctx, r.c, opParseTradingViewMessage, nil, nil, body, opts)
}

// FillsService: the fills calls (client.Fills).
type FillsService struct{ c *Client }

// List: List fills.
func (r *FillsService) List(ctx context.Context, accountID string, params *FillsListParams, opts ...CallOption) (*Page[Fill], error) {
	return listPage[Fill](ctx, r.c, opListFills, []string{accountID}, params.values(), opts)
}

// ListAutoPaging: every item of List, page after page (WithMaxItems caps it).
func (r *FillsService) ListAutoPaging(ctx context.Context, accountID string, params *FillsListParams, opts ...CallOption) iter.Seq2[Fill, error] {
	return autoPaging(ctx, func() (*Page[Fill], error) { return r.List(ctx, accountID, params, opts...) }, opts)
}
