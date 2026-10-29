// Code generated from the API spec by generator/printers/go.js. DO NOT EDIT: run `node generator`.

package phoenixperps

import (
	"encoding/json"
	"net/url"
	"strconv"
)

var _ json.RawMessage

// ErrorEnvelopeErrorType: The family of the error; it decides the HTTP status. Open: a value from a newer API
// version is kept as is.
type ErrorEnvelopeErrorType string

const (
	ErrorEnvelopeErrorTypeInvalidRequest ErrorEnvelopeErrorType = "invalid_request"
	ErrorEnvelopeErrorTypeAuthentication ErrorEnvelopeErrorType = "authentication"
	ErrorEnvelopeErrorTypePermission     ErrorEnvelopeErrorType = "permission"
	ErrorEnvelopeErrorTypeNotFound       ErrorEnvelopeErrorType = "not_found"
	ErrorEnvelopeErrorTypeMethod         ErrorEnvelopeErrorType = "method"
	ErrorEnvelopeErrorTypeConflict       ErrorEnvelopeErrorType = "conflict"
	ErrorEnvelopeErrorTypePayload        ErrorEnvelopeErrorType = "payload"
	ErrorEnvelopeErrorTypeRuleRejection  ErrorEnvelopeErrorType = "rule_rejection"
	ErrorEnvelopeErrorTypeKeyLimit       ErrorEnvelopeErrorType = "key_limit"
	ErrorEnvelopeErrorTypeRateLimit      ErrorEnvelopeErrorType = "rate_limit"
	ErrorEnvelopeErrorTypeServer         ErrorEnvelopeErrorType = "server"
	ErrorEnvelopeErrorTypeEngine         ErrorEnvelopeErrorType = "engine"
)

// PlatformEnvironment: The environment of the host you called. Open: a value from a newer API version is kept as
// is.
type PlatformEnvironment string

const (
	PlatformEnvironmentLive    PlatformEnvironment = "live"
	PlatformEnvironmentSandbox PlatformEnvironment = "sandbox"
)

// PlatformStatusEngine: Whether the trading engine is reachable. Open: a value from a newer API version is kept
// as is.
type PlatformStatusEngine string

const (
	PlatformStatusEngineUp   PlatformStatusEngine = "up"
	PlatformStatusEngineDown PlatformStatusEngine = "down"
)

// PlatformStatusMarketData: Whether the market-data stream is live, with every price feed. Open: a value from a
// newer API version is kept as is.
type PlatformStatusMarketData string

const (
	PlatformStatusMarketDataUp   PlatformStatusMarketData = "up"
	PlatformStatusMarketDataDown PlatformStatusMarketData = "down"
)

// PlatformStatusWebhooks: Whether outbound webhooks are being delivered. Open: a value from a newer API version
// is kept as is.
type PlatformStatusWebhooks string

const (
	PlatformStatusWebhooksUp   PlatformStatusWebhooks = "up"
	PlatformStatusWebhooksDown PlatformStatusWebhooks = "down"
)

// StatusApi: Whether this environment takes requests. Open: a value from a newer API version is kept as is.
type StatusApi string

const (
	StatusApiOperational StatusApi = "operational"
	StatusApiDisabled    StatusApi = "disabled"
)

// MarketStatus: Whether the market trades now. Open: a value from a newer API version is kept as is.
type MarketStatus string

const (
	MarketStatusOpen   MarketStatus = "open"
	MarketStatusHalted MarketStatus = "halted"
	MarketStatusClosed MarketStatus = "closed"
)

// TradingHours: When it trades: around the clock, its underlying market's sessions (Standard Market Hours,
// closed on weekends), or FX hours. Open: a value from a newer API version is kept as is.
type TradingHours string

const (
	TradingHoursN24_7    TradingHours = "24/7"
	TradingHoursStandard TradingHours = "standard"
	TradingHoursFx       TradingHours = "fx"
)

// RuleDailyLossBasis: What the day's loss is measured from. Open: a value from a newer API version is kept as
// is.
type RuleDailyLossBasis string

const (
	RuleDailyLossBasisPeakBalance     RuleDailyLossBasis = "peak_balance"
	RuleDailyLossBasisStartBalance    RuleDailyLossBasis = "start_balance"
	RuleDailyLossBasisPeakEquity      RuleDailyLossBasis = "peak_equity"
	RuleDailyLossBasisDayStartBalance RuleDailyLossBasis = "day_start_balance"
	RuleDailyLossBasisDayStartEquity  RuleDailyLossBasis = "day_start_equity"
)

// OnBreach: What happens when it is reached. Open: a value from a newer API version is kept as is.
type OnBreach string

const (
	OnBreachFailAccount     OnBreach = "fail_account"
	OnBreachFlatten         OnBreach = "flatten"
	OnBreachFlattenAndPause OnBreach = "flatten_and_pause"
)

// RuleMaxDrawdownMode: How the reference moves: end_of_day and end_of_trade follow the highest balance, static
// stays at the start, intraday_equity follows the highest equity. Open: a value from a newer API version is kept
// as is.
type RuleMaxDrawdownMode string

const (
	RuleMaxDrawdownModeEndOfDay       RuleMaxDrawdownMode = "end_of_day"
	RuleMaxDrawdownModeEndOfTrade     RuleMaxDrawdownMode = "end_of_trade"
	RuleMaxDrawdownModeStatic         RuleMaxDrawdownMode = "static"
	RuleMaxDrawdownModeIntradayEquity RuleMaxDrawdownMode = "intraday_equity"
)

// AccountStage: Where the account is in the programme. Open: a value from a newer API version is kept as is.
type AccountStage string

const (
	AccountStageEvaluation AccountStage = "evaluation"
	AccountStageFunded     AccountStage = "funded"
	AccountStageSandbox    AccountStage = "sandbox"
)

// AccountStatus: pending: being opened; active: trading. Open: a value from a newer API version is kept as is.
type AccountStatus string

const (
	AccountStatusPending   AccountStatus = "pending"
	AccountStatusActive    AccountStatus = "active"
	AccountStatusPassed    AccountStatus = "passed"
	AccountStatusFailed    AccountStatus = "failed"
	AccountStatusClosed    AccountStatus = "closed"
	AccountStatusSuspended AccountStatus = "suspended"
)

// WarningCode: What it is about. Open: a value from a newer API version is kept as is.
type WarningCode string

const (
	WarningCodeDailyLossUsed        WarningCode = "daily_loss_used"
	WarningCodeMaxDrawdownUsed      WarningCode = "max_drawdown_used"
	WarningCodeLiquidationNear      WarningCode = "liquidation_near"
	WarningCodeNoStopLoss           WarningCode = "no_stop_loss"
	WarningCodeMarginUsed           WarningCode = "margin_used"
	WarningCodeOrderWithoutPosition WarningCode = "order_without_position"
)

// WarningLevel: How serious. Open: a value from a newer API version is kept as is.
type WarningLevel string

const (
	WarningLevelInfo     WarningLevel = "info"
	WarningLevelWarning  WarningLevel = "warning"
	WarningLevelCritical WarningLevel = "critical"
)

// ProgressObjectivesProfitTargetStatus: Whether it is reached. Open: a value from a newer API version is kept as
// is.
type ProgressObjectivesProfitTargetStatus string

const (
	ProgressObjectivesProfitTargetStatusOpen ProgressObjectivesProfitTargetStatus = "open"
	ProgressObjectivesProfitTargetStatusMet  ProgressObjectivesProfitTargetStatus = "met"
)

// TradingPolicyMarginDefaultMode: The margin mode of a market you never set. Open: a value from a newer API
// version is kept as is.
type TradingPolicyMarginDefaultMode string

const (
	TradingPolicyMarginDefaultModeIsolated TradingPolicyMarginDefaultMode = "isolated"
	TradingPolicyMarginDefaultModeCross    TradingPolicyMarginDefaultMode = "cross"
)

// Resolution: One point per closed trade, or per trading day. Open: a value from a newer API version is kept as
// is.
type Resolution string

const (
	ResolutionTrade Resolution = "trade"
	ResolutionDay   Resolution = "day"
)

// MarginMode: Isolated: each position holds its own margin. Cross: positions share the balance. Open: a value
// from a newer API version is kept as is.
type MarginMode string

const (
	MarginModeIsolated MarginMode = "isolated"
	MarginModeCross    MarginMode = "cross"
)

// MarginSettingSource: Where it comes from: the open position, your saved setting, or the market default. Open:
// a value from a newer API version is kept as is.
type MarginSettingSource string

const (
	MarginSettingSourcePosition MarginSettingSource = "position"
	MarginSettingSourceSaved    MarginSettingSource = "saved"
	MarginSettingSourceDefault  MarginSettingSource = "default"
)

// OrderType: The order's type. Open: a value from a newer API version is kept as is.
type OrderType string

const (
	OrderTypeMarket     OrderType = "market"
	OrderTypeLimit      OrderType = "limit"
	OrderTypeStopMarket OrderType = "stop_market"
	OrderTypeStopLimit  OrderType = "stop_limit"
	OrderTypeTakeMarket OrderType = "take_market"
	OrderTypeTakeLimit  OrderType = "take_limit"
)

// PositionSide: Long or short. Open: a value from a newer API version is kept as is.
type PositionSide string

const (
	PositionSideLong  PositionSide = "long"
	PositionSideShort PositionSide = "short"
)

// OrderSide: The order's side; null for an order placed elsewhere before the API saw it. Open: a value from a
// newer API version is kept as is.
type OrderSide string

const (
	OrderSideBuy  OrderSide = "buy"
	OrderSideSell OrderSide = "sell"
)

// OrderStatus: unconfirmed: the engine has not confirmed its last state yet. Open: a value from a newer API
// version is kept as is.
type OrderStatus string

const (
	OrderStatusPending     OrderStatus = "pending"
	OrderStatusWorking     OrderStatus = "working"
	OrderStatusFilled      OrderStatus = "filled"
	OrderStatusCanceled    OrderStatus = "canceled"
	OrderStatusRejected    OrderStatus = "rejected"
	OrderStatusExpired     OrderStatus = "expired"
	OrderStatusUnconfirmed OrderStatus = "unconfirmed"
)

// TimeInForce: How long it rests. Open: a value from a newer API version is kept as is.
type TimeInForce string

const (
	TimeInForceGtc TimeInForce = "gtc"
	TimeInForceGtd TimeInForce = "gtd"
	TimeInForceIoc TimeInForce = "ioc"
	TimeInForceFok TimeInForce = "fok"
)

// OrderSource: Where it was placed: this API, an AI assistant (the MCP server), a trading platform, a strategy,
// a TradingView alert, or the system (a liquidation). Open: a value from a newer API version is kept as is.
type OrderSource string

const (
	OrderSourceAPI         OrderSource = "api"
	OrderSourceMcp         OrderSource = "mcp"
	OrderSourcePlatform    OrderSource = "platform"
	OrderSourceStrategy    OrderSource = "strategy"
	OrderSourceTradingview OrderSource = "tradingview"
	OrderSourceSystem      OrderSource = "system"
)

// OrderCancelReason: Why it was canceled. Open: a value from a newer API version is kept as is.
type OrderCancelReason string

const (
	OrderCancelReasonUser             OrderCancelReason = "user"
	OrderCancelReasonCancelAll        OrderCancelReason = "cancel_all"
	OrderCancelReasonFlatten          OrderCancelReason = "flatten"
	OrderCancelReasonCloseAll         OrderCancelReason = "close_all"
	OrderCancelReasonPositionClosed   OrderCancelReason = "position_closed"
	OrderCancelReasonPositionReversed OrderCancelReason = "position_reversed"
	OrderCancelReasonCloseAssist      OrderCancelReason = "close_assist"
	OrderCancelReasonOCO              OrderCancelReason = "oco"
	OrderCancelReasonReplaced         OrderCancelReason = "replaced"
	OrderCancelReasonStrategy         OrderCancelReason = "strategy"
	OrderCancelReasonKeyLossStop      OrderCancelReason = "key_loss_stop"
	OrderCancelReasonRiskRule         OrderCancelReason = "risk_rule"
	OrderCancelReasonSystemRestart    OrderCancelReason = "system_restart"
	OrderCancelReasonExternal         OrderCancelReason = "external"
)

// MeCredentialKind: What you called with: an API key, or an app you signed in to. Open: a value from a newer API
// version is kept as is.
type MeCredentialKind string

const (
	MeCredentialKindKey MeCredentialKind = "key"
	MeCredentialKindApp MeCredentialKind = "app"
)

// MeCredentialAccess: read: look only. trade: also place and cancel. Open: a value from a newer API version is
// kept as is.
type MeCredentialAccess string

const (
	MeCredentialAccessRead  MeCredentialAccess = "read"
	MeCredentialAccessTrade MeCredentialAccess = "trade"
)

// MeAccountScope: all: every account of the environment, new ones too. selected: only those listed. Open: a
// value from a newer API version is kept as is.
type MeAccountScope string

const (
	MeAccountScopeAll      MeAccountScope = "all"
	MeAccountScopeSelected MeAccountScope = "selected"
)

// CreateOrderType: market: now, at the best price within your worst price. limit: at your price or better.
// stop_market: a market order once the last price reaches the stop. stop_limit: a limit once the last price
// reaches the stop. Open: a value from a newer API version is kept as is.
type CreateOrderType string

const (
	CreateOrderTypeMarket     CreateOrderType = "market"
	CreateOrderTypeLimit      CreateOrderType = "limit"
	CreateOrderTypeStopMarket CreateOrderType = "stop_market"
	CreateOrderTypeStopLimit  CreateOrderType = "stop_limit"
)

// OrderExitsStopLossType: The stop's type (default stop_market). Open: a value from a newer API version is kept
// as is.
type OrderExitsStopLossType string

const (
	OrderExitsStopLossTypeStopMarket OrderExitsStopLossType = "stop_market"
	OrderExitsStopLossTypeStopLimit  OrderExitsStopLossType = "stop_limit"
)

// CreateStrategyType: trailing_stop: a stop-loss that follows the price. break_even: the stop-losses move to the
// entry once the price has gone your way far enough. oco: two orders, the first fill cancels the other. twap,
// vwap: an order in market slices over time (vwap sized by yesterday's volume over the same hours). scaled: a
// ladder of limits. chase: a limit that follows the bid or ask. Open: a value from a newer API version is kept
// as is.
type CreateStrategyType string

const (
	CreateStrategyTypeTrailingStop CreateStrategyType = "trailing_stop"
	CreateStrategyTypeBreakEven    CreateStrategyType = "break_even"
	CreateStrategyTypeOCO          CreateStrategyType = "oco"
	CreateStrategyTypeTWAP         CreateStrategyType = "twap"
	CreateStrategyTypeVWAP         CreateStrategyType = "vwap"
	CreateStrategyTypeScaled       CreateStrategyType = "scaled"
	CreateStrategyTypeChase        CreateStrategyType = "chase"
)

// CreateStrategyOrderType: Two orders that rest. Open: a value from a newer API version is kept as is.
type CreateStrategyOrderType string

const (
	CreateStrategyOrderTypeLimit      CreateStrategyOrderType = "limit"
	CreateStrategyOrderTypeStopMarket CreateStrategyOrderType = "stop_market"
	CreateStrategyOrderTypeStopLimit  CreateStrategyOrderType = "stop_limit"
)

// CreateStrategyDistribution: scaled: equal sizes, growing toward end_price, or shrinking. Open: a value from a
// newer API version is kept as is.
type CreateStrategyDistribution string

const (
	CreateStrategyDistributionFlat       CreateStrategyDistribution = "flat"
	CreateStrategyDistributionAscending  CreateStrategyDistribution = "ascending"
	CreateStrategyDistributionDescending CreateStrategyDistribution = "descending"
)

// CreateStrategyOnExpiry: chase: at max_minutes, cancel it, or send what is left at the market. Open: a value
// from a newer API version is kept as is.
type CreateStrategyOnExpiry string

const (
	CreateStrategyOnExpiryCancel CreateStrategyOnExpiry = "cancel"
	CreateStrategyOnExpiryMarket CreateStrategyOnExpiry = "market"
)

// WhatIfResult: What the order would do now: fill at once, rest, or be refused (reject says why). Open: a value
// from a newer API version is kept as is.
type WhatIfResult string

const (
	WhatIfResultWouldFill   WhatIfResult = "would_fill"
	WhatIfResultWouldRest   WhatIfResult = "would_rest"
	WhatIfResultWouldReject WhatIfResult = "would_reject"
)

// WhatIfMarginMode: The margin mode the position would use. Open: a value from a newer API version is kept as
// is.
type WhatIfMarginMode string

const (
	WhatIfMarginModeIsolated WhatIfMarginMode = "isolated"
	WhatIfMarginModeCross    WhatIfMarginMode = "cross"
)

// EventType: What happened (GET /v1/webhook-event-types says what each type means). Open: a value from a newer
// API version is kept as is.
type EventType string

const (
	EventTypeOrderCreated             EventType = "order.created"
	EventTypeOrderEdited              EventType = "order.edited"
	EventTypeOrderFilled              EventType = "order.filled"
	EventTypeOrderCanceled            EventType = "order.canceled"
	EventTypeOrderRejected            EventType = "order.rejected"
	EventTypeOrderExpired             EventType = "order.expired"
	EventTypeFillCreated              EventType = "fill.created"
	EventTypePositionOpened           EventType = "position.opened"
	EventTypePositionChanged          EventType = "position.changed"
	EventTypePositionClosed           EventType = "position.closed"
	EventTypeStrategyCreated          EventType = "strategy.created"
	EventTypeStrategyLegPlaced        EventType = "strategy.leg_placed"
	EventTypeStrategyLegModified      EventType = "strategy.leg_modified"
	EventTypeStrategyLegFilled        EventType = "strategy.leg_filled"
	EventTypeStrategyLegCanceled      EventType = "strategy.leg_canceled"
	EventTypeStrategyLegRejected      EventType = "strategy.leg_rejected"
	EventTypeStrategyOrphanRepaired   EventType = "strategy.orphan_repaired"
	EventTypeStrategySlice            EventType = "strategy.slice"
	EventTypeStrategyEnded            EventType = "strategy.ended"
	EventTypeTradingviewAlertExecuted EventType = "tradingview.alert_executed"
	EventTypeTradingviewAlertRefused  EventType = "tradingview.alert_refused"
	EventTypeAccountUpdated           EventType = "account.updated"
	EventTypeAccountPassed            EventType = "account.passed"
	EventTypeAccountFailed            EventType = "account.failed"
	EventTypeAccountPayoutEligible    EventType = "account.payout_eligible"
	EventTypeAccountDisabled          EventType = "account.disabled"
	EventTypeAccountRemoved           EventType = "account.removed"
	EventTypeKeyLossStop              EventType = "key.loss_stop"
	EventTypeKeyExpiring              EventType = "key.expiring"
	EventTypeSystemRestarted          EventType = "system.restarted"
	EventTypeRiskWarning              EventType = "risk.warning"
	EventTypeRiskCleared              EventType = "risk.cleared"
)

// EventSource: Where it came from: an order placed through this API or by an AI assistant (the MCP server), one
// placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert.
// Open: a value from a newer API version is kept as is.
type EventSource string

const (
	EventSourceAPI         EventSource = "api"
	EventSourceMcp         EventSource = "mcp"
	EventSourcePlatform    EventSource = "platform"
	EventSourceEngine      EventSource = "engine"
	EventSourceStrategy    EventSource = "strategy"
	EventSourceTradingview EventSource = "tradingview"
)

// WebhookPayloadType: What happened; webhook.ping is a test. Open: a value from a newer API version is kept as
// is.
type WebhookPayloadType string

const (
	WebhookPayloadTypeOrderCreated             WebhookPayloadType = "order.created"
	WebhookPayloadTypeOrderEdited              WebhookPayloadType = "order.edited"
	WebhookPayloadTypeOrderFilled              WebhookPayloadType = "order.filled"
	WebhookPayloadTypeOrderCanceled            WebhookPayloadType = "order.canceled"
	WebhookPayloadTypeOrderRejected            WebhookPayloadType = "order.rejected"
	WebhookPayloadTypeOrderExpired             WebhookPayloadType = "order.expired"
	WebhookPayloadTypeFillCreated              WebhookPayloadType = "fill.created"
	WebhookPayloadTypePositionOpened           WebhookPayloadType = "position.opened"
	WebhookPayloadTypePositionChanged          WebhookPayloadType = "position.changed"
	WebhookPayloadTypePositionClosed           WebhookPayloadType = "position.closed"
	WebhookPayloadTypeStrategyCreated          WebhookPayloadType = "strategy.created"
	WebhookPayloadTypeStrategyLegPlaced        WebhookPayloadType = "strategy.leg_placed"
	WebhookPayloadTypeStrategyLegModified      WebhookPayloadType = "strategy.leg_modified"
	WebhookPayloadTypeStrategyLegFilled        WebhookPayloadType = "strategy.leg_filled"
	WebhookPayloadTypeStrategyLegCanceled      WebhookPayloadType = "strategy.leg_canceled"
	WebhookPayloadTypeStrategyLegRejected      WebhookPayloadType = "strategy.leg_rejected"
	WebhookPayloadTypeStrategyOrphanRepaired   WebhookPayloadType = "strategy.orphan_repaired"
	WebhookPayloadTypeStrategySlice            WebhookPayloadType = "strategy.slice"
	WebhookPayloadTypeStrategyEnded            WebhookPayloadType = "strategy.ended"
	WebhookPayloadTypeTradingviewAlertExecuted WebhookPayloadType = "tradingview.alert_executed"
	WebhookPayloadTypeTradingviewAlertRefused  WebhookPayloadType = "tradingview.alert_refused"
	WebhookPayloadTypeAccountUpdated           WebhookPayloadType = "account.updated"
	WebhookPayloadTypeAccountPassed            WebhookPayloadType = "account.passed"
	WebhookPayloadTypeAccountFailed            WebhookPayloadType = "account.failed"
	WebhookPayloadTypeAccountPayoutEligible    WebhookPayloadType = "account.payout_eligible"
	WebhookPayloadTypeAccountDisabled          WebhookPayloadType = "account.disabled"
	WebhookPayloadTypeAccountRemoved           WebhookPayloadType = "account.removed"
	WebhookPayloadTypeKeyLossStop              WebhookPayloadType = "key.loss_stop"
	WebhookPayloadTypeKeyExpiring              WebhookPayloadType = "key.expiring"
	WebhookPayloadTypeSystemRestarted          WebhookPayloadType = "system.restarted"
	WebhookPayloadTypeRiskWarning              WebhookPayloadType = "risk.warning"
	WebhookPayloadTypeRiskCleared              WebhookPayloadType = "risk.cleared"
	WebhookPayloadTypeWebhookPing              WebhookPayloadType = "webhook.ping"
)

// ExitsLegGroup: take_profit: a limit on the profit side. stop_loss: a stop on the loss side. Open: a value from
// a newer API version is kept as is.
type ExitsLegGroup string

const (
	ExitsLegGroupTakeProfit ExitsLegGroup = "take_profit"
	ExitsLegGroupStopLoss   ExitsLegGroup = "stop_loss"
)

// ExitsLegStatus: The order's status; waiting: a stop-loss not placed yet (it waits for its activation price or
// its break-even). Open: a value from a newer API version is kept as is.
type ExitsLegStatus string

const (
	ExitsLegStatusPending     ExitsLegStatus = "pending"
	ExitsLegStatusWorking     ExitsLegStatus = "working"
	ExitsLegStatusFilled      ExitsLegStatus = "filled"
	ExitsLegStatusCanceled    ExitsLegStatus = "canceled"
	ExitsLegStatusRejected    ExitsLegStatus = "rejected"
	ExitsLegStatusExpired     ExitsLegStatus = "expired"
	ExitsLegStatusUnconfirmed ExitsLegStatus = "unconfirmed"
	ExitsLegStatusWaiting     ExitsLegStatus = "waiting"
)

// ExitsBatchOperationOp: What to do. Open: a value from a newer API version is kept as is.
type ExitsBatchOperationOp string

const (
	ExitsBatchOperationOpPlace  ExitsBatchOperationOp = "place"
	ExitsBatchOperationOpModify ExitsBatchOperationOp = "modify"
	ExitsBatchOperationOpCancel ExitsBatchOperationOp = "cancel"
)

// ExitsBatchOperationClear: one of the values below. Open: a value from a newer API version is kept as is.
type ExitsBatchOperationClear string

const (
	ExitsBatchOperationClearTrail     ExitsBatchOperationClear = "trail"
	ExitsBatchOperationClearBreakEven ExitsBatchOperationClear = "break_even"
)

// StrategyType: exits: a position's take-profit and stop-loss legs. bracket: an order with exits once it fills.
// oco: two orders, the first fill cancels the other. twap, vwap: an order in slices over time. scaled: a ladder
// of limits. chase: a limit that follows the price. Open: a value from a newer API version is kept as is.
type StrategyType string

const (
	StrategyTypeExits   StrategyType = "exits"
	StrategyTypeBracket StrategyType = "bracket"
	StrategyTypeOCO     StrategyType = "oco"
	StrategyTypeTWAP    StrategyType = "twap"
	StrategyTypeVWAP    StrategyType = "vwap"
	StrategyTypeScaled  StrategyType = "scaled"
	StrategyTypeChase   StrategyType = "chase"
)

// StrategyStatus: waiting: armed, nothing placed yet. working: running. completed: done as asked. canceled: you
// ended it. ended: it stopped for end_reason. Open: a value from a newer API version is kept as is.
type StrategyStatus string

const (
	StrategyStatusWaiting   StrategyStatus = "waiting"
	StrategyStatusWorking   StrategyStatus = "working"
	StrategyStatusCompleted StrategyStatus = "completed"
	StrategyStatusCanceled  StrategyStatus = "canceled"
	StrategyStatusEnded     StrategyStatus = "ended"
)

// StrategyEndReason: Why it stopped. Open: a value from a newer API version is kept as is.
type StrategyEndReason string

const (
	StrategyEndReasonTakeProfit       StrategyEndReason = "take_profit"
	StrategyEndReasonStopLoss         StrategyEndReason = "stop_loss"
	StrategyEndReasonFilled           StrategyEndReason = "filled"
	StrategyEndReasonPositionClosed   StrategyEndReason = "position_closed"
	StrategyEndReasonPositionReversed StrategyEndReason = "position_reversed"
	StrategyEndReasonCanceledByUser   StrategyEndReason = "canceled_by_user"
	StrategyEndReasonExternal         StrategyEndReason = "external"
	StrategyEndReasonKeyRevoked       StrategyEndReason = "key_revoked"
	StrategyEndReasonAccountClosed    StrategyEndReason = "account_closed"
	StrategyEndReasonEngineRestart    StrategyEndReason = "engine_restart"
	StrategyEndReasonChildRejected    StrategyEndReason = "child_rejected"
	StrategyEndReasonPriceBound       StrategyEndReason = "price_bound"
	StrategyEndReasonExpired          StrategyEndReason = "expired"
)

// WebhookEndpointFormat: json: the signed event. discord, slack, telegram: a chat message. Open: a value from a
// newer API version is kept as is.
type WebhookEndpointFormat string

const (
	WebhookEndpointFormatJSON     WebhookEndpointFormat = "json"
	WebhookEndpointFormatDiscord  WebhookEndpointFormat = "discord"
	WebhookEndpointFormatSlack    WebhookEndpointFormat = "slack"
	WebhookEndpointFormatTelegram WebhookEndpointFormat = "telegram"
)

// WebhookEndpointEvent: A group of events. Open: a value from a newer API version is kept as is.
type WebhookEndpointEvent string

const (
	WebhookEndpointEventOrders    WebhookEndpointEvent = "orders"
	WebhookEndpointEventPositions WebhookEndpointEvent = "positions"
	WebhookEndpointEventAccount   WebhookEndpointEvent = "account"
	WebhookEndpointEventRisk      WebhookEndpointEvent = "risk"
)

// WebhookEndpointStatus: disabled: we stopped after failures in a row; turn it on again from your dashboard.
// Open: a value from a newer API version is kept as is.
type WebhookEndpointStatus string

const (
	WebhookEndpointStatusActive   WebhookEndpointStatus = "active"
	WebhookEndpointStatusPaused   WebhookEndpointStatus = "paused"
	WebhookEndpointStatusDisabled WebhookEndpointStatus = "disabled"
)

// WebhookDeliveryStatus: pending: waiting for its next try. failed: every try failed. canceled: the endpoint was
// disabled or deleted. Open: a value from a newer API version is kept as is.
type WebhookDeliveryStatus string

const (
	WebhookDeliveryStatusPending    WebhookDeliveryStatus = "pending"
	WebhookDeliveryStatusDelivering WebhookDeliveryStatus = "delivering"
	WebhookDeliveryStatusDelivered  WebhookDeliveryStatus = "delivered"
	WebhookDeliveryStatusFailed     WebhookDeliveryStatus = "failed"
	WebhookDeliveryStatusCanceled   WebhookDeliveryStatus = "canceled"
)

// WebhookEventTypeGroup: The group that receives it; null: every endpoint (the test). Open: a value from a newer
// API version is kept as is.
type WebhookEventTypeGroup string

const (
	WebhookEventTypeGroupOrders    WebhookEventTypeGroup = "orders"
	WebhookEventTypeGroupPositions WebhookEventTypeGroup = "positions"
	WebhookEventTypeGroupAccount   WebhookEventTypeGroup = "account"
	WebhookEventTypeGroupRisk      WebhookEventTypeGroup = "risk"
)

// TradingViewHookStatus: paused: alerts are refused (link_paused). Open: a value from a newer API version is
// kept as is.
type TradingViewHookStatus string

const (
	TradingViewHookStatusActive TradingViewHookStatus = "active"
	TradingViewHookStatusPaused TradingViewHookStatus = "paused"
)

// TradingViewHookSizingMode: from_alert: the size the alert gives. qty, usd, pct_balance: the link's own. Open:
// a value from a newer API version is kept as is.
type TradingViewHookSizingMode string

const (
	TradingViewHookSizingModeFromAlert  TradingViewHookSizingMode = "from_alert"
	TradingViewHookSizingModeQty        TradingViewHookSizingMode = "qty"
	TradingViewHookSizingModeUSD        TradingViewHookSizingMode = "usd"
	TradingViewHookSizingModePctBalance TradingViewHookSizingMode = "pct_balance"
)

// TradingViewHookPositionMode: signal: an opposite alert reverses, a repeat is skipped. add: a repeat adds.
// target: the strategy's position size is followed. Open: a value from a newer API version is kept as is.
type TradingViewHookPositionMode string

const (
	TradingViewHookPositionModeSignal TradingViewHookPositionMode = "signal"
	TradingViewHookPositionModeAdd    TradingViewHookPositionMode = "add"
	TradingViewHookPositionModeTarget TradingViewHookPositionMode = "target"
)

// TradingViewAlertStatus: What it did. Open: a value from a newer API version is kept as is.
type TradingViewAlertStatus string

const (
	TradingViewAlertStatusProcessing  TradingViewAlertStatus = "processing"
	TradingViewAlertStatusPlaced      TradingViewAlertStatus = "placed"
	TradingViewAlertStatusPartial     TradingViewAlertStatus = "partial"
	TradingViewAlertStatusDryRun      TradingViewAlertStatus = "dry_run"
	TradingViewAlertStatusDuplicate   TradingViewAlertStatus = "duplicate"
	TradingViewAlertStatusSkipped     TradingViewAlertStatus = "skipped"
	TradingViewAlertStatusRefused     TradingViewAlertStatus = "refused"
	TradingViewAlertStatusFailed      TradingViewAlertStatus = "failed"
	TradingViewAlertStatusRateLimited TradingViewAlertStatus = "rate_limited"
)

// LimitStandingStatus: Where it stands. Open: a value from a newer API version is kept as is.
type LimitStandingStatus string

const (
	LimitStandingStatusOk       LimitStandingStatus = "ok"
	LimitStandingStatusWarning  LimitStandingStatus = "warning"
	LimitStandingStatusCritical LimitStandingStatus = "critical"
	LimitStandingStatusBreached LimitStandingStatus = "breached"
)

// OrdersListParamsStatus: one of the values below. Open: a value from a newer API version is kept as is.
type OrdersListParamsStatus string

const (
	OrdersListParamsStatusAll     OrdersListParamsStatus = "all"
	OrdersListParamsStatusWorking OrdersListParamsStatus = "working"
	OrdersListParamsStatusHistory OrdersListParamsStatus = "history"
)

// StrategiesListParamsStatus: one of the values below. Open: a value from a newer API version is kept as is.
type StrategiesListParamsStatus string

const (
	StrategiesListParamsStatusAll    StrategiesListParamsStatus = "all"
	StrategiesListParamsStatusActive StrategiesListParamsStatus = "active"
	StrategiesListParamsStatusEnded  StrategiesListParamsStatus = "ended"
)

// ErrorEnvelope: Every error has this shape.
type ErrorEnvelope struct {
	Error ErrorEnvelopeError `json:"error"`
}

// ErrorEnvelopeError: see the API reference.
type ErrorEnvelopeError struct {
	// The family of the error; it decides the HTTP status.
	Type ErrorEnvelopeErrorType `json:"type"`
	// The error code: match on this.
	Code string `json:"code"`
	// A sentence for people. It may change; never match on it.
	Message string `json:"message"`
	// The id of the request; give it to support.
	RequestID string `json:"request_id"`
	// Whether the same request may succeed later.
	Retryable bool `json:"retryable"`
	// Where this code is explained.
	DocURL string `json:"doc_url"`
	// More about the problem: the fields at fault, a limit, where to go.
	Details json.RawMessage `json:"details,omitempty"`
	// The order the error is about (an order the engine refused is kept, with this id).
	OrderID *string `json:"order_id,omitempty"`
}

// Platform: see the API reference.
type Platform struct {
	// The environment of the host you called.
	Environment PlatformEnvironment `json:"environment"`
	// The version of this API.
	Version string `json:"version"`
	// The time on the server.
	ServerTime int64 `json:"server_time"`
	// The documentation.
	DocsURL string `json:"docs_url"`
	// This API as OpenAPI 3.1.
	SpecURL string         `json:"spec_url"`
	Status  PlatformStatus `json:"status"`
}

// PlatformStatus: see the API reference.
type PlatformStatus struct {
	// Whether the trading engine is reachable.
	Engine PlatformStatusEngine `json:"engine"`
	// When the trading engine last started.
	EngineStartedAt *int64 `json:"engine_started_at"`
	// Whether the market-data stream is live, with every price feed.
	MarketData PlatformStatusMarketData `json:"market_data"`
	// Whether outbound webhooks are being delivered.
	Webhooks PlatformStatusWebhooks `json:"webhooks"`
}

// Status: see the API reference.
type Status struct {
	// The environment of the host you called.
	Environment PlatformEnvironment `json:"environment"`
	// Whether this environment takes requests.
	API         StatusApi         `json:"api"`
	Engine      StatusEngine      `json:"engine"`
	AccountData StatusAccountData `json:"account_data"`
	MarketData  StatusMarketData  `json:"market_data"`
	Features    StatusFeatures    `json:"features"`
	// The time on the server.
	ServerTime int64 `json:"server_time"`
}

// StatusEngine: see the API reference.
type StatusEngine struct {
	// Whether the API is connected to the trading engine.
	Connected bool `json:"connected"`
	// When the trading engine last started.
	StartedAt *int64 `json:"started_at"`
}

// StatusAccountData: see the API reference.
type StatusAccountData struct {
	// When your keys and accounts were last confirmed with the website.
	SyncedAt *int64 `json:"synced_at"`
}

// StatusMarketData: see the API reference.
type StatusMarketData struct {
	// Whether the market-data stream is live, with every price feed.
	Live bool `json:"live"`
	// When that was last checked.
	CheckedAt *int64 `json:"checked_at"`
}

// StatusFeatures: see the API reference.
type StatusFeatures struct {
	// The account stream.
	Stream bool `json:"stream"`
	// Market data.
	MarketData bool `json:"market_data"`
	// Webhooks.
	Webhooks bool `json:"webhooks"`
	// TradingView alerts.
	Tradingview bool `json:"tradingview"`
	// AI assistants (MCP).
	Mcp bool `json:"mcp"`
}

// Limits: see the API reference.
type Limits struct {
	// The environment of the host you called.
	Environment PlatformEnvironment `json:"environment"`
	// The request budgets. A request takes from every budget it belongs to.
	Rate       LimitsRate       `json:"rate"`
	Keys       LimitsKeys       `json:"keys"`
	Orders     LimitsOrders     `json:"orders"`
	Sandbox    *LimitsSandbox   `json:"sandbox,omitempty"`
	Strategies LimitsStrategies `json:"strategies"`
	// The largest request body.
	BodyMaxBytes int64           `json:"body_max_bytes"`
	PageLimit    LimitsPageLimit `json:"page_limit"`
}

// LimitsRate: The request budgets. A request takes from every budget it belongs to.
type LimitsRate struct {
	PublicPerIP     LimitsRatePublicPerIp     `json:"public_per_ip"`
	PerIP           LimitsRatePerIp           `json:"per_ip"`
	PerKeyRead      LimitsRatePerKeyRead      `json:"per_key_read"`
	PerKeyTrade     LimitsRatePerKeyTrade     `json:"per_key_trade"`
	PerAccountTrade LimitsRatePerAccountTrade `json:"per_account_trade"`
	PerCustomer     LimitsRatePerCustomer     `json:"per_customer"`
	Bulk            LimitsRateBulk            `json:"bulk"`
	WhatIf          LimitsRateWhatIf          `json:"what_if"`
}

// LimitsRatePublicPerIp: see the API reference.
type LimitsRatePublicPerIp struct {
	// Requests a minute.
	PerMin int64 `json:"per_min"`
	// Requests at once.
	Burst int64 `json:"burst"`
}

// LimitsRatePerIp: see the API reference.
type LimitsRatePerIp struct {
	// Requests a minute.
	PerMin int64 `json:"per_min"`
	// Requests at once.
	Burst int64 `json:"burst"`
}

// LimitsRatePerKeyRead: see the API reference.
type LimitsRatePerKeyRead struct {
	// Requests a minute.
	PerMin int64 `json:"per_min"`
	// Requests at once.
	Burst int64 `json:"burst"`
}

// LimitsRatePerKeyTrade: see the API reference.
type LimitsRatePerKeyTrade struct {
	// Requests a minute.
	PerMin int64 `json:"per_min"`
	// Requests at once.
	Burst int64 `json:"burst"`
}

// LimitsRatePerAccountTrade: see the API reference.
type LimitsRatePerAccountTrade struct {
	// Requests a minute.
	PerMin int64 `json:"per_min"`
	// Requests at once.
	Burst int64 `json:"burst"`
}

// LimitsRatePerCustomer: see the API reference.
type LimitsRatePerCustomer struct {
	// Requests a minute.
	PerMin int64 `json:"per_min"`
	// Requests at once.
	Burst int64 `json:"burst"`
}

// LimitsRateBulk: see the API reference.
type LimitsRateBulk struct {
	// Requests a minute.
	PerMin int64 `json:"per_min"`
	// Requests at once.
	Burst int64 `json:"burst"`
}

// LimitsRateWhatIf: see the API reference.
type LimitsRateWhatIf struct {
	// Requests a minute.
	PerMin int64 `json:"per_min"`
	// Requests at once.
	Burst int64 `json:"burst"`
}

// LimitsKeys: see the API reference.
type LimitsKeys struct {
	// Keys you may hold in each environment.
	PerEnvironment int64 `json:"per_environment"`
	// Addresses in a key's allowlist.
	IPAllowlistMax int64 `json:"ip_allowlist_max"`
	// The longest a key may live, in days.
	ExpiryMaxDays int64 `json:"expiry_max_days"`
}

// LimitsOrders: see the API reference.
type LimitsOrders struct {
	// Working orders an account may hold.
	MaxWorkingPerAccount int64 `json:"max_working_per_account"`
	// Working orders across your accounts.
	MaxWorkingPerCustomer int64 `json:"max_working_per_customer"`
	// New orders a minute on one account.
	NewPerMinAccount int64 `json:"new_per_min_account"`
	// New orders a day on one account.
	NewPerDayAccount int64 `json:"new_per_day_account"`
	// How far from the market price a priced order may be, in percent.
	PriceBandPct Decimal `json:"price_band_pct"`
	// The oldest price an order is checked against, in seconds.
	QuoteMaxAgeS int64 `json:"quote_max_age_s"`
	// The oldest unchanged price of a quiet market an order is checked against, while the price feed lives (other
	// markets move), in seconds.
	QuoteMaxAgeSlowS int64 `json:"quote_max_age_slow_s"`
	// The nearest a gtd order may expire, in seconds from now.
	GtdMinSeconds int64 `json:"gtd_min_seconds"`
	// The farthest, in days from now.
	GtdMaxDays int64 `json:"gtd_max_days"`
}

// LimitsSandbox: see the API reference.
type LimitsSandbox struct {
	// Sandbox accounts you may open, ever.
	AccountsForLife int64 `json:"accounts_for_life"`
	// The balance a sandbox account starts with.
	StartBalance Decimal `json:"start_balance"`
	// Hours without use before a sandbox account is parked.
	ParkedAfterHours int64 `json:"parked_after_hours"`
}

// LimitsStrategies: see the API reference.
type LimitsStrategies struct {
	// Running strategies an account may hold.
	PerAccount int64 `json:"per_account"`
	// Of those, programs (oco, twap, vwap, scaled, chase).
	ProgramsPerAccount int64 `json:"programs_per_account"`
	// Working orders of strategies on one account.
	LegsPerAccount int64 `json:"legs_per_account"`
	// Running strategies across your accounts.
	PerCustomer int64 `json:"per_customer"`
	// Take-profit legs on one position.
	TakeProfitsPerPosition int64 `json:"take_profits_per_position"`
	// Stop-loss legs on one position.
	StopLossesPerPosition int64 `json:"stop_losses_per_position"`
	// The shortest twap or vwap, in minutes.
	TWAPMinMinutes int64 `json:"twap_min_minutes"`
	// The longest twap or vwap, in minutes.
	TWAPMaxMinutes int64 `json:"twap_max_minutes"`
	// The shortest time between slices, in seconds.
	SliceMinSeconds int64 `json:"slice_min_seconds"`
	// The most limits in a scaled ladder.
	ScaledMaxRungs int64 `json:"scaled_max_rungs"`
	// Whether chase strategies may start.
	ChaseEnabled bool `json:"chase_enabled"`
	// The longest a chase may run, in minutes.
	ChaseMaxMinutes int64 `json:"chase_max_minutes"`
}

// LimitsPageLimit: see the API reference.
type LimitsPageLimit struct {
	// Items a page holds by default.
	Default int64 `json:"default"`
	// The most a page may hold.
	Max int64 `json:"max"`
}

// Market: see the API reference.
type Market struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The market's name; for a currency, the pair its price is quoted in (JPY-USD: USD/JPY, yen per dollar).
	Name string `json:"name"`
	// Whether the market trades now.
	Status MarketStatus `json:"status"`
	// The venue it trades on: its prices and fills are that venue's. Null for a currency on FX hours.
	Venue *string `json:"venue"`
	// When it trades: around the clock, its underlying market's sessions (Standard Market Hours, closed on
	// weekends), or FX hours.
	TradingHours TradingHours `json:"trading_hours"`
	// For an option (the same asset on another venue, or on Standard Market Hours): its market. Null for a market.
	OptionOf *string `json:"option_of"`
	// The ways this asset can be traded, the market first, then its options: an account holds one of them at a time
	// (market_option_held). Null when there is only the market.
	Options []MarketOption `json:"options"`
	// Prices are multiples of this.
	TickSize Decimal `json:"tick_size"`
	// Sizes are multiples of this, in the base asset.
	SizeStep Decimal `json:"size_step"`
	// The smallest order size (the size step). An order must also be worth at least min_notional.
	MinSize Decimal `json:"min_size"`
	// The smallest order value in USD: an order worth less is refused order_too_small.
	MinNotional *Decimal `json:"min_notional"`
	// The highest leverage on this market.
	MaxLeverage int64 `json:"max_leverage"`
	// The fee on each fill, in percent of its value.
	FeeRatePct Decimal `json:"fee_rate_pct"`
	// The most you may hold on this market across your accounts on the same rule, in USD.
	ExposureCap *Decimal `json:"exposure_cap"`
	// How far from the price a market order may fill, in percent.
	MaxSlippagePct *MarketMaxSlippagePct `json:"max_slippage_pct"`
}

// MarketOption: see the API reference.
type MarketOption struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// Its venue.
	Venue *string `json:"venue"`
	// When it trades.
	TradingHours TradingHours `json:"trading_hours"`
}

// MarketMaxSlippagePct: How far from the price a market order may fill, in percent.
type MarketMaxSlippagePct struct {
	// The slippage bound a market order gets when it names none.
	Default *Decimal `json:"default"`
	// The tightest bound an order may ask.
	Min *Decimal `json:"min"`
	// The widest bound an order may ask.
	Max *Decimal `json:"max"`
}

// MarketStats: A market's 24-hour figures and open interest. A figure not known yet is null.
type MarketStats struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The last trade.
	Last *Decimal `json:"last"`
	// The change over 24 hours.
	Change24h *Decimal `json:"change_24h"`
	// That change in percent.
	Change24hPct *Decimal `json:"change_24h_pct"`
	// The highest in 24 hours.
	High24h *Decimal `json:"high_24h"`
	// The lowest in 24 hours.
	Low24h *Decimal `json:"low_24h"`
	// The size traded in 24 hours, in the base asset.
	Volume24h *Decimal `json:"volume_24h"`
	// The value traded in 24 hours, in USD.
	NotionalVolume24h *Decimal `json:"notional_volume_24h"`
	// The market's open interest: the size held open in positions, in the base asset.
	OpenInterest *Decimal `json:"open_interest"`
	// That open interest in USD, at the last price.
	NotionalOpenInterest *Decimal `json:"notional_open_interest"`
}

// Rule: The account's rule, as the trading engine enforces it.
type Rule struct {
	// The balance the account started with.
	StartBalance Decimal           `json:"start_balance"`
	DailyLoss    *RuleDailyLoss    `json:"daily_loss"`
	MaxDrawdown  *RuleMaxDrawdown  `json:"max_drawdown"`
	WeeklyLoss   *RuleWeeklyLoss   `json:"weekly_loss"`
	ProfitTarget *RuleProfitTarget `json:"profit_target"`
	// Days with a trade the account needs.
	MinTradingDays *int64 `json:"min_trading_days"`
	// Calendar days the account needs.
	MinCalendarDays *int64           `json:"min_calendar_days"`
	Consistency     *RuleConsistency `json:"consistency"`
	// Positions may stay open overnight.
	OvernightHolding bool `json:"overnight_holding"`
	// Positions may stay open over the weekend.
	WeekendHolding bool `json:"weekend_holding"`
	// Days without a trade after which the account fails.
	InactivityDays *int64 `json:"inactivity_days"`
}

// RuleDailyLoss: see the API reference.
type RuleDailyLoss struct {
	// The most the account may lose in a trading day, in USD.
	Amount *Decimal `json:"amount"`
	// The same limit in percent of its basis.
	Pct *Decimal `json:"pct"`
	// What the day's loss is measured from.
	Basis RuleDailyLossBasis `json:"basis"`
	// What happens when it is reached.
	OnBreach OnBreach `json:"on_breach"`
}

// RuleMaxDrawdown: see the API reference.
type RuleMaxDrawdown struct {
	// The most the account may fall from its reference, in USD.
	Amount *Decimal `json:"amount"`
	// The same limit in percent.
	Pct *Decimal `json:"pct"`
	// How the reference moves: end_of_day and end_of_trade follow the highest balance, static stays at the start,
	// intraday_equity follows the highest equity.
	Mode RuleMaxDrawdownMode `json:"mode"`
}

// RuleWeeklyLoss: see the API reference.
type RuleWeeklyLoss struct {
	// The most the account may lose in a week, in USD.
	Amount *Decimal `json:"amount"`
	// The same limit in percent.
	Pct *Decimal `json:"pct"`
}

// RuleProfitTarget: see the API reference.
type RuleProfitTarget struct {
	// The profit to reach, in USD.
	Amount *Decimal `json:"amount"`
	// The same target in percent.
	Pct *Decimal `json:"pct"`
	// Positions must be closed when it is reached.
	RequiresFlat bool `json:"requires_flat"`
}

// RuleConsistency: see the API reference.
type RuleConsistency struct {
	// The most one day may bring, in USD.
	Amount *Decimal `json:"amount"`
	// The most one day may bring, in percent of the total.
	Pct *Decimal `json:"pct"`
}

// Account: see the API reference.
type Account struct {
	// The account number.
	ID string `json:"id"`
	// The environment the account lives in.
	Environment PlatformEnvironment `json:"environment"`
	// Where the account is in the programme.
	Stage AccountStage `json:"stage"`
	// pending: being opened; active: trading.
	Status AccountStatus `json:"status"`
	// The product it was bought as.
	Product *string `json:"product"`
	// Its size as sold.
	Tier *string `json:"tier"`
	// The balance it started with.
	StartBalance *Decimal `json:"start_balance"`
	// The balance: closed trades and fees only.
	Balance *Decimal `json:"balance"`
	Rule    *Rule    `json:"rule"`
	// A payout may be requested now.
	PayoutEligible bool `json:"payout_eligible"`
	// Sandbox only: parked after a while unused; it wakes the moment you use it.
	Parked *bool `json:"parked,omitempty"`
	// When the account was made.
	CreatedAt *int64 `json:"created_at"`
}

// Limit: A loss limit and the room left to it.
type Limit struct {
	// The limit, in USD.
	Limit *Decimal `json:"limit,omitempty"`
	// The equity at which it is breached.
	Floor *Decimal `json:"floor,omitempty"`
	// How much more the account may lose before it.
	Room *Decimal `json:"room,omitempty"`
	// How much of it is used, in percent.
	UsedPct *Decimal `json:"used_pct,omitempty"`
	// Daily loss only: when it resets (midnight New York).
	ResetsAt *int64 `json:"resets_at,omitempty"`
	// Daily loss only: what happens when it is reached.
	OnBreach *OnBreach `json:"on_breach,omitempty"`
	// Daily loss only: the day's start is estimated (the engine does not publish it).
	Estimated *bool `json:"estimated,omitempty"`
	// Max drawdown only: how the reference moves.
	Mode *RuleMaxDrawdownMode `json:"mode,omitempty"`
	// Max drawdown only: the engine may hold a higher floor than shown (it follows equity highs).
	AtLeast *bool `json:"at_least,omitempty"`
	// The rule no longer applies this limit (its cut-off was reached).
	Lifted bool `json:"lifted"`
}

// Warning: see the API reference.
type Warning struct {
	// What it is about.
	Code WarningCode `json:"code"`
	// How serious.
	Level WarningLevel `json:"level"`
	// The market it is about.
	Market *string `json:"market,omitempty"`
	// The figure behind it, in percent.
	Value *Decimal `json:"value,omitempty"`
}

// AccountRisk: see the API reference.
type AccountRisk struct {
	// When the oldest figure here was read.
	AsOf int64 `json:"as_of"`
	// Some figures are older than usual (the engine was busy).
	Stale bool `json:"stale"`
	// The balance.
	Balance Decimal `json:"balance"`
	// The balance plus the open P&L at the market price.
	Equity Decimal `json:"equity"`
	// The open P&L.
	UnrealizedPnL Decimal `json:"unrealized_pnl"`
	// The equity change since midnight New York, open positions included: what the daily loss rule counts.
	PnLToday Decimal `json:"pnl_today"`
	// The P&L closed today.
	RealizedToday Decimal `json:"realized_today"`
	// The fees paid today.
	FeesToday Decimal `json:"fees_today"`
	// Trades closed today.
	TradesToday int64 `json:"trades_today"`
	// The margin your positions hold.
	MarginUsed Decimal `json:"margin_used"`
	// What is left of the balance for new positions.
	MarginAvailable    *Decimal                       `json:"margin_available"`
	Exposure           AccountRiskExposure            `json:"exposure"`
	DailyLoss          *Limit                         `json:"daily_loss"`
	MaxDrawdown        *Limit                         `json:"max_drawdown"`
	ProfitTarget       *AccountRiskProfitTarget       `json:"profit_target"`
	TradingDays        AccountRiskTradingDays         `json:"trading_days"`
	NearestLiquidation *AccountRiskNearestLiquidation `json:"nearest_liquidation"`
	// When the trading day ends (midnight New York).
	TradingDayEndsAt int64 `json:"trading_day_ends_at"`
	// What needs your attention, most serious first.
	Warnings []Warning `json:"warnings"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x AccountRisk) MarshalJSON() ([]byte, error) {
	type plain AccountRisk
	p := plain(x)
	if p.Warnings == nil {
		p.Warnings = []Warning{}
	}
	return json.Marshal(p)
}

// AccountRiskExposure: see the API reference.
type AccountRiskExposure struct {
	// The value of every open position.
	Gross Decimal `json:"gross"`
	// The value of the long ones.
	Long Decimal `json:"long"`
	// The value of the short ones.
	Short Decimal `json:"short"`
	// Gross exposure over equity.
	EffectiveLeverage *Decimal `json:"effective_leverage"`
	// Per market, largest first.
	Markets []AccountRiskExposureMarket `json:"markets"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x AccountRiskExposure) MarshalJSON() ([]byte, error) {
	type plain AccountRiskExposure
	p := plain(x)
	if p.Markets == nil {
		p.Markets = []AccountRiskExposureMarket{}
	}
	return json.Marshal(p)
}

// AccountRiskExposureMarket: see the API reference.
type AccountRiskExposureMarket struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The value held on it.
	Notional Decimal `json:"notional"`
	// That value over equity.
	Leverage *Decimal `json:"leverage"`
	// The exposure cap of the market.
	Cap *Decimal `json:"cap"`
	// How much of the cap is used, counting your other accounts on the same rule, in percent.
	CapUsedPct *Decimal `json:"cap_used_pct"`
}

// AccountRiskProfitTarget: see the API reference.
type AccountRiskProfitTarget struct {
	// The balance that reaches it.
	GoalBalance Decimal `json:"goal_balance"`
	// Still to make.
	Remaining Decimal `json:"remaining"`
	// How far, in percent.
	ProgressPct *Decimal `json:"progress_pct"`
	// Positions must be closed when it is reached.
	RequiresFlat bool `json:"requires_flat"`
}

// AccountRiskTradingDays: see the API reference.
type AccountRiskTradingDays struct {
	// Days with a trade so far.
	Count *int64 `json:"count"`
	// Days the rule needs.
	Required *int64 `json:"required"`
}

// AccountRiskNearestLiquidation: see the API reference.
type AccountRiskNearestLiquidation struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// Its liquidation price.
	Price Decimal `json:"price"`
	// How far the price is from it, in percent.
	DistancePct Decimal `json:"distance_pct"`
}

// Progress: see the API reference.
type Progress struct {
	// Where the account is in the programme.
	Stage      AccountStage       `json:"stage"`
	Objectives ProgressObjectives `json:"objectives"`
	Limits     ProgressLimits     `json:"limits"`
	Payout     *ProgressPayout    `json:"payout"`
	// When the oldest figure here was read.
	AsOf int64 `json:"as_of"`
}

// ProgressObjectives: see the API reference.
type ProgressObjectives struct {
	ProfitTarget    *ProgressObjectivesProfitTarget    `json:"profit_target"`
	MinTradingDays  *ProgressObjectivesMinTradingDays  `json:"min_trading_days"`
	MinCalendarDays *ProgressObjectivesMinCalendarDays `json:"min_calendar_days"`
}

// ProgressObjectivesProfitTarget: see the API reference.
type ProgressObjectivesProfitTarget struct {
	// The balance that reaches it.
	GoalBalance Decimal `json:"goal_balance"`
	// Still to make.
	Remaining Decimal `json:"remaining"`
	// How far, in percent.
	ProgressPct *Decimal `json:"progress_pct"`
	// Positions must be closed when it is reached.
	RequiresFlat bool `json:"requires_flat"`
	// Whether it is reached.
	Status ProgressObjectivesProfitTargetStatus `json:"status"`
}

// ProgressObjectivesMinTradingDays: see the API reference.
type ProgressObjectivesMinTradingDays struct {
	// Days the rule needs.
	Required int64 `json:"required"`
	// Days with a trade so far.
	Count *int64 `json:"count"`
	// Whether it is reached.
	Status ProgressObjectivesProfitTargetStatus `json:"status"`
}

// ProgressObjectivesMinCalendarDays: see the API reference.
type ProgressObjectivesMinCalendarDays struct {
	// Calendar days the rule needs.
	Required int64 `json:"required"`
	// Calendar days so far.
	Count int64 `json:"count"`
	// Whether it is reached.
	Status ProgressObjectivesProfitTargetStatus `json:"status"`
}

// ProgressLimits: see the API reference.
type ProgressLimits struct {
	DailyLoss   *LimitStanding `json:"daily_loss"`
	MaxDrawdown *LimitStanding `json:"max_drawdown"`
}

// ProgressPayout: see the API reference.
type ProgressPayout struct {
	// A payout may be requested now.
	Eligible bool `json:"eligible"`
	// What may be paid out.
	Available Decimal `json:"available"`
	// The smallest payout.
	MinPayout *Decimal `json:"min_payout"`
	// Paid out so far.
	PaidOut Decimal `json:"paid_out"`
}

// TradingPolicy: see the API reference.
type TradingPolicy struct {
	Rule        *Rule                    `json:"rule"`
	TradingDay  TradingPolicyTradingDay  `json:"trading_day"`
	SlippagePct TradingPolicySlippagePct `json:"slippage_pct"`
	Margin      TradingPolicyMargin      `json:"margin"`
	Orders      TradingPolicyOrders      `json:"orders"`
	// The limits you set on the key.
	Controls TradingPolicyControls `json:"controls"`
	Budgets  TradingPolicyBudgets  `json:"budgets"`
	// When the oldest figure here was read.
	AsOf int64 `json:"as_of"`
}

// TradingPolicyTradingDay: see the API reference.
type TradingPolicyTradingDay struct {
	// The trading day runs midnight to midnight in this zone.
	Timezone string `json:"timezone"`
	// When this one ends.
	EndsAt int64 `json:"ends_at"`
}

// TradingPolicySlippagePct: see the API reference.
type TradingPolicySlippagePct struct {
	// The bound a market order gets when it names none, in percent.
	Default *Decimal `json:"default"`
	// The tightest it may ask.
	Min *Decimal `json:"min"`
	// The widest it may ask.
	Max *Decimal `json:"max"`
}

// TradingPolicyMargin: see the API reference.
type TradingPolicyMargin struct {
	// The margin mode of a market you never set.
	DefaultMode TradingPolicyMarginDefaultMode `json:"default_mode"`
	// The share of a position's margin kept as maintenance, in percent.
	MaintenanceMarginPct *Decimal `json:"maintenance_margin_pct"`
}

// TradingPolicyOrders: see the API reference.
type TradingPolicyOrders struct {
	// Working orders the account may hold.
	MaxWorkingPerAccount int64 `json:"max_working_per_account"`
	// New orders a minute.
	NewPerMinAccount int64 `json:"new_per_min_account"`
	// How far from the market price a priced order may be, in percent.
	PriceBandPct Decimal `json:"price_band_pct"`
	// The oldest price an order is checked against, in seconds.
	QuoteMaxAgeS int64 `json:"quote_max_age_s"`
	// The oldest unchanged price of a quiet market an order is checked against, while the price feed lives (other
	// markets move), in seconds.
	QuoteMaxAgeSlowS int64 `json:"quote_max_age_slow_s"`
}

// TradingPolicyControls: The limits you set on the key.
type TradingPolicyControls struct {
	// The markets the key may trade; empty: every market.
	Markets []string `json:"markets"`
	// The key may only reduce positions.
	ReduceOnly bool `json:"reduce_only"`
	// The largest order the key may place, in USD.
	MaxOrderUSD *Decimal `json:"max_order_usd"`
	// The largest position the key may hold, in USD.
	MaxPositionUSD *Decimal `json:"max_position_usd"`
	// The loss in a trading day at which the key stops trading the account.
	DailyLossUSD *Decimal `json:"daily_loss_usd"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x TradingPolicyControls) MarshalJSON() ([]byte, error) {
	type plain TradingPolicyControls
	p := plain(x)
	if p.Markets == nil {
		p.Markets = []string{}
	}
	return json.Marshal(p)
}

// TradingPolicyBudgets: see the API reference.
type TradingPolicyBudgets struct {
	// Reads a minute for this key.
	ReadPerMin int64 `json:"read_per_min"`
	// Trading calls a minute for this key.
	TradePerMin int64 `json:"trade_per_min"`
}

// EquityHistory: see the API reference.
type EquityHistory struct {
	// One point per closed trade, or per trading day.
	Resolution Resolution `json:"resolution"`
	// Oldest first; the first point is the opening balance.
	Points []EquityHistoryPoint `json:"points"`
	Live   EquityHistoryLive    `json:"live"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x EquityHistory) MarshalJSON() ([]byte, error) {
	type plain EquityHistory
	p := plain(x)
	if p.Points == nil {
		p.Points = []EquityHistoryPoint{}
	}
	return json.Marshal(p)
}

// EquityHistoryPoint: see the API reference.
type EquityHistoryPoint struct {
	// When.
	Time int64 `json:"time"`
	// The trading day (New York), with resolution day.
	Date *string `json:"date,omitempty"`
	// The balance then.
	Balance Decimal `json:"balance"`
}

// EquityHistoryLive: see the API reference.
type EquityHistoryLive struct {
	// When the live figures were read.
	Time *int64 `json:"time"`
	// The balance now.
	Balance Decimal `json:"balance"`
	// The equity now.
	Equity Decimal `json:"equity"`
}

// DailyResult: see the API reference.
type DailyResult struct {
	// The trading day (midnight to midnight New York).
	Date string `json:"date"`
	// Trades closed that day.
	Trades int64 `json:"trades"`
	// Their P&L before fees.
	GrossPnL Decimal `json:"gross_pnl"`
	// Their fees.
	Fees Decimal `json:"fees"`
	// Their P&L after fees.
	PnL Decimal `json:"pnl"`
	// Paid out that day.
	Withdrawn Decimal `json:"withdrawn"`
	// The balance at the day's end.
	BalanceEnd *Decimal `json:"balance_end"`
}

// MarginSetting: see the API reference.
type MarginSetting struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The leverage new positions on this market get.
	Leverage int64 `json:"leverage"`
	// Isolated: each position holds its own margin. Cross: positions share the balance.
	MarginMode MarginMode `json:"margin_mode"`
	// The highest this account may use on the market (its trading rule's). null: the market is not available on this
	// account.
	MaxLeverage *int64 `json:"max_leverage"`
	// Where it comes from: the open position, your saved setting, or the market default.
	Source MarginSettingSource `json:"source"`
	// A position or a working order on the market: the mode cannot change now.
	Locked bool `json:"locked"`
}

// ExitLeg: see the API reference.
type ExitLeg struct {
	// The working order.
	OrderID string `json:"order_id"`
	// The order's type.
	Type OrderType `json:"type"`
	// Its trigger or limit price.
	Price *Decimal `json:"price"`
	// Its size.
	Size *Decimal `json:"size"`
}

// Position: see the API reference.
type Position struct {
	// This position, while it stays open on the same side.
	ID string `json:"id"`
	// A market id: the asset and USD.
	Market string `json:"market"`
	// Long or short.
	Side PositionSide `json:"side"`
	// The size, in the base asset.
	Size Decimal `json:"size"`
	// The average entry price.
	EntryPrice *Decimal `json:"entry_price"`
	// The market price it is valued at.
	MarkPrice *Decimal `json:"mark_price"`
	// Its value at that price.
	Notional *Decimal `json:"notional"`
	// Its open P&L.
	UnrealizedPnL *Decimal `json:"unrealized_pnl"`
	// Its leverage.
	Leverage int64 `json:"leverage"`
	// Its margin mode.
	MarginMode MarginMode `json:"margin_mode"`
	// The margin it took at entry.
	InitialMargin *Decimal `json:"initial_margin"`
	// Margin you added to it.
	AddedMargin *Decimal `json:"added_margin"`
	// What it holds of the balance.
	Margin       *Decimal             `json:"margin"`
	Liquidation  *PositionLiquidation `json:"liquidation"`
	BreachPrices PositionBreachPrices `json:"breach_prices"`
	Exits        PositionExits        `json:"exits"`
	// At least one stop loss rests on it.
	Protected bool `json:"protected"`
	// When it opened.
	OpenedAt *int64 `json:"opened_at"`
}

// PositionLiquidation: see the API reference.
type PositionLiquidation struct {
	// The price at which it is liquidated; null when there is none or it depends on several positions.
	Price *Decimal `json:"price"`
	// How far the market price is from it, in percent.
	DistancePct *Decimal `json:"distance_pct"`
	// Cross margin on several markets: no single price, every one of them moves it.
	AccountWide bool `json:"account_wide"`
}

// PositionBreachPrices: see the API reference.
type PositionBreachPrices struct {
	// The price at which this position alone would breach the daily loss limit.
	DailyLoss *Decimal `json:"daily_loss"`
	// The price at which it alone would breach the max drawdown.
	MaxDrawdown *Decimal `json:"max_drawdown"`
}

// PositionExits: see the API reference.
type PositionExits struct {
	// Limit orders on the closing side.
	TakeProfit []ExitLeg `json:"take_profit"`
	// Stop orders on the closing side.
	StopLoss []ExitLeg `json:"stop_loss"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x PositionExits) MarshalJSON() ([]byte, error) {
	type plain PositionExits
	p := plain(x)
	if p.TakeProfit == nil {
		p.TakeProfit = []ExitLeg{}
	}
	if p.StopLoss == nil {
		p.StopLoss = []ExitLeg{}
	}
	return json.Marshal(p)
}

// ClosedPosition: see the API reference.
type ClosedPosition struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// Long or short.
	Side PositionSide `json:"side"`
	// The size closed.
	Size Decimal `json:"size"`
	// The average entry price.
	EntryPrice *Decimal `json:"entry_price"`
	// The average exit price.
	ExitPrice *Decimal `json:"exit_price"`
	// The P&L before fees.
	GrossPnL Decimal `json:"gross_pnl"`
	// The fees.
	Fees Decimal `json:"fees"`
	// The P&L after fees.
	PnL Decimal `json:"pnl"`
	// When it opened.
	OpenedAt int64 `json:"opened_at"`
	// When it was fully closed.
	ClosedAt int64 `json:"closed_at"`
	// The pieces it was closed in.
	Lots []ClosedPositionLot `json:"lots"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x ClosedPosition) MarshalJSON() ([]byte, error) {
	type plain ClosedPosition
	p := plain(x)
	if p.Lots == nil {
		p.Lots = []ClosedPositionLot{}
	}
	return json.Marshal(p)
}

// ClosedPositionLot: see the API reference.
type ClosedPositionLot struct {
	// The size.
	Size Decimal `json:"size"`
	// Entry.
	EntryPrice *Decimal `json:"entry_price"`
	// Exit.
	ExitPrice *Decimal `json:"exit_price"`
	// P&L after fees.
	PnL Decimal `json:"pnl"`
	// Fees.
	Fees Decimal `json:"fees"`
	// Opened.
	OpenedAt *int64 `json:"opened_at"`
	// Closed.
	ClosedAt int64 `json:"closed_at"`
}

// Order: see the API reference.
type Order struct {
	// The order.
	ID string `json:"id"`
	// Your own id for it, when it was placed through the API.
	ClientOrderID *string `json:"client_order_id"`
	// The account number.
	AccountID string `json:"account_id"`
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The order's side; null for an order placed elsewhere before the API saw it.
	Side *OrderSide `json:"side"`
	// The order's type.
	Type *OrderType `json:"type"`
	// unconfirmed: the engine has not confirmed its last state yet.
	Status OrderStatus `json:"status"`
	// The size, in the base asset.
	Size *Decimal `json:"size"`
	// How much filled.
	FilledSize *Decimal `json:"filled_size"`
	// Its limit price.
	LimitPrice *Decimal `json:"limit_price"`
	// Its trigger price.
	StopPrice *Decimal `json:"stop_price"`
	// The average fill price.
	AverageFillPrice *Decimal `json:"average_fill_price"`
	// The fee its fill paid.
	Fee *Decimal `json:"fee"`
	// It may only reduce a position; null when not known (placed elsewhere).
	ReduceOnly *bool `json:"reduce_only"`
	// How long it rests.
	TimeInForce TimeInForce `json:"time_in_force"`
	// A gtd order: when it expires (it ends expired then); null otherwise.
	ExpiresAt *int64 `json:"expires_at"`
	// It may only rest, never fill at once; null when not known (placed elsewhere).
	PostOnly *bool `json:"post_only"`
	// Where it was placed: this API, an AI assistant (the MCP server), a trading platform, a strategy, a TradingView
	// alert, or the system (a liquidation).
	Source OrderSource `json:"source"`
	// The strategy that placed it (source strategy).
	StrategyID *string `json:"strategy_id"`
	// Exits attached to it (orders placed through the API).
	Exits map[string]any `json:"exits"`
	// Why it was canceled.
	CancelReason *OrderCancelReason `json:"cancel_reason"`
	Reject       *OrderReject       `json:"reject"`
	// When it was placed.
	CreatedAt *int64 `json:"created_at"`
	// When it filled.
	FilledAt *int64 `json:"filled_at"`
	// When it was canceled.
	CanceledAt *int64 `json:"canceled_at"`
	// Its latest change.
	UpdatedAt *int64 `json:"updated_at,omitempty"`
}

// OrderReject: see the API reference.
type OrderReject struct {
	// The error code.
	Code string `json:"code"`
	// What happened.
	Message *string `json:"message"`
}

// Fill: see the API reference.
type Fill struct {
	// The fill.
	ID string `json:"id"`
	// Its order.
	OrderID string `json:"order_id"`
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The side; null for an order placed elsewhere before the API saw it.
	Side *OrderSide `json:"side"`
	// The size filled.
	Size Decimal `json:"size"`
	// The fill price.
	Price Decimal `json:"price"`
	// Its fee.
	Fee *Decimal `json:"fee"`
	// A liquidation filled it.
	Liquidation bool `json:"liquidation"`
	// When it filled.
	Time int64 `json:"time"`
}

// Me: see the API reference.
type Me struct {
	Credential MeCredential `json:"credential"`
	// The accounts it reaches.
	Accounts []string `json:"accounts"`
	// all: every account of the environment, new ones too. selected: only those listed.
	AccountScope MeAccountScope `json:"account_scope"`
	// The limits you set on the key.
	Controls MeControls `json:"controls"`
	Budgets  MeBudgets  `json:"budgets"`
	// Through the MCP server (an AI assistant): how its trades are confirmed and its budget. Null for any other
	// call.
	Assistant *MeAssistant `json:"assistant"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x Me) MarshalJSON() ([]byte, error) {
	type plain Me
	p := plain(x)
	if p.Accounts == nil {
		p.Accounts = []string{}
	}
	return json.Marshal(p)
}

// MeCredential: see the API reference.
type MeCredential struct {
	// What you called with: an API key, or an app you signed in to.
	Kind MeCredentialKind `json:"kind"`
	// The key's id (the part after ppk_test_ or ppk_live_), or the app access's id.
	ID string `json:"id"`
	// Its name (an app: the app's name).
	Name string `json:"name"`
	// Its environment.
	Environment PlatformEnvironment `json:"environment"`
	// read: look only. trade: also place and cancel.
	Access MeCredentialAccess `json:"access"`
	// When it expires; null: never.
	ExpiresAt *int64 `json:"expires_at"`
	// When it was made.
	CreatedAt *int64 `json:"created_at"`
}

// MeControls: The limits you set on the key.
type MeControls struct {
	// The markets the key may trade; empty: every market.
	Markets []string `json:"markets"`
	// The key may only reduce positions.
	ReduceOnly bool `json:"reduce_only"`
	// The largest order the key may place, in USD.
	MaxOrderUSD *Decimal `json:"max_order_usd"`
	// The largest position the key may hold, in USD.
	MaxPositionUSD *Decimal `json:"max_position_usd"`
	// The loss in a trading day at which the key stops trading the account.
	DailyLossUSD *Decimal `json:"daily_loss_usd"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x MeControls) MarshalJSON() ([]byte, error) {
	type plain MeControls
	p := plain(x)
	if p.Markets == nil {
		p.Markets = []string{}
	}
	return json.Marshal(p)
}

// MeBudgets: see the API reference.
type MeBudgets struct {
	// Reads a minute for this key.
	ReadPerMin int64 `json:"read_per_min"`
	// Trading calls a minute for this key.
	TradePerMin int64 `json:"trade_per_min"`
}

// MeAssistant: Through the MCP server (an AI assistant): how its trades are confirmed and its budget. Null for
// any other call.
type MeAssistant struct {
	// Each trade is shown first and placed only when repeated with its confirmation (preview, then confirm).
	ConfirmTrades bool `json:"confirm_trades"`
	// How long a preview's confirmation holds, in seconds.
	ConfirmSeconds int64 `json:"confirm_seconds"`
	// The AI assistant's own budget, on top of the others.
	Budget MeAssistantBudget `json:"budget"`
}

// MeAssistantBudget: The AI assistant's own budget, on top of the others.
type MeAssistantBudget struct {
	// Reads a minute.
	ReadsPerMin int64 `json:"reads_per_min"`
	// Trading calls a minute.
	WritesPerMin int64 `json:"writes_per_min"`
	// Trading calls an hour.
	WritesPerHour int64 `json:"writes_per_hour"`
}

// CreateOrder: A new order.
type CreateOrder struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The order's side.
	Side OrderSide `json:"side"`
	// market: now, at the best price within your worst price. limit: at your price or better. stop_market: a market
	// order once the last price reaches the stop. stop_limit: a limit once the last price reaches the stop.
	Type CreateOrderType `json:"type"`
	// The size, in the base asset: a multiple of the market's size step. Give size or notional_usd.
	Size *Decimal `json:"size,omitempty"`
	// The size as a value in USD, rounded down to the size step.
	NotionalUSD *Decimal `json:"notional_usd,omitempty"`
	// The limit price (limit and stop_limit): a multiple of the tick size.
	LimitPrice *Decimal `json:"limit_price,omitempty"`
	// The trigger price (stop_market and stop_limit).
	StopPrice *Decimal `json:"stop_price,omitempty"`
	// Market orders: the worst price you accept. Give worst_price or max_slippage_pct.
	WorstPrice *Decimal `json:"worst_price,omitempty"`
	// Market orders: how far from the best price you accept, in percent (the default and bounds are in GET
	// /v1/markets).
	MaxSlippagePct *Decimal `json:"max_slippage_pct,omitempty"`
	// It may only reduce or close your position, never open or add to one.
	ReduceOnly *bool `json:"reduce_only,omitempty"`
	// Limit orders: refused if it would fill at once.
	PostOnly *bool `json:"post_only,omitempty"`
	// gtc: until canceled. gtd: until expires_at. ioc and fok: fill now or end (a fill is always the whole order).
	// Default: ioc for market orders, gtc otherwise.
	TimeInForce *TimeInForce `json:"time_in_force,omitempty"`
	// With gtd: when it ends, from orders.gtd_min_seconds to orders.gtd_max_days ahead (GET /v1/limits).
	ExpiresAt *int64 `json:"expires_at,omitempty"`
	// Your own id for the order: unique in this environment, 1 to 64 letters, digits and . _ : -
	ClientOrderID *string     `json:"client_order_id,omitempty"`
	Exits         *OrderExits `json:"exits,omitempty"`
}

// OrderExits: Exits placed when the order fills: the stop-loss for the filled size first, then the take-profits.
// From then on they are the position's exits. An empty object removes them.
type OrderExits struct {
	// Limit orders on the profit side, each at price, or at distance or percent from the fill.
	TakeProfit []OrderExitsTakeProfit `json:"take_profit,omitempty"`
	// A stop on the loss side, at price, or at distance or percent from the fill; it may trail and move to
	// break-even (as in a position's exits).
	StopLoss *OrderExitsStopLoss `json:"stop_loss,omitempty"`
}

// OrderExitsTakeProfit: see the API reference.
type OrderExitsTakeProfit struct {
	// An exact price.
	Price *Decimal `json:"price,omitempty"`
	// A price difference from the fill price.
	Distance *Decimal `json:"distance,omitempty"`
	// A percent of the fill price.
	Percent *Decimal `json:"percent,omitempty"`
	// Its share of the filled size, in percent (absent: the take-profits split it evenly).
	SizePct *Decimal `json:"size_pct,omitempty"`
}

// OrderExitsStopLoss: A stop on the loss side, at price, or at distance or percent from the fill; it may trail
// and move to break-even (as in a position's exits).
type OrderExitsStopLoss struct {
	// An exact price.
	Price *Decimal `json:"price,omitempty"`
	// A price difference from the fill price.
	Distance *Decimal `json:"distance,omitempty"`
	// A percent of the fill price.
	Percent *Decimal `json:"percent,omitempty"`
	// The stop's type (default stop_market).
	Type *OrderExitsStopLossType `json:"type,omitempty"`
	// stop_limit: how many ticks past the stop its limit sits.
	LimitOffsetTicks *int64 `json:"limit_offset_ticks,omitempty"`
	// As a position's exits take it: { distance } or { percent }, with activation_price to start only once the price
	// reaches it.
	Trail *OrderExitsStopLossTrail `json:"trail,omitempty"`
	// As a position's exits take it: one of trigger_distance, trigger_percent, trigger_price, and offset_ticks.
	BreakEven *OrderExitsStopLossBreakEven `json:"break_even,omitempty"`
}

// OrderExitsStopLossTrail: As a position's exits take it: { distance } or { percent }, with activation_price to
// start only once the price reaches it.
type OrderExitsStopLossTrail struct {
	// How far behind the best price.
	Distance *Decimal `json:"distance,omitempty"`
	// How far behind, in percent of the best price.
	Percent *Decimal `json:"percent,omitempty"`
	// Start once the price reaches this.
	ActivationPrice *Decimal `json:"activation_price,omitempty"`
}

// OrderExitsStopLossBreakEven: As a position's exits take it: one of trigger_distance, trigger_percent,
// trigger_price, and offset_ticks.
type OrderExitsStopLossBreakEven struct {
	// How far from the entry.
	TriggerDistance *Decimal `json:"trigger_distance,omitempty"`
	// How far from the entry, in percent.
	TriggerPercent *Decimal `json:"trigger_percent,omitempty"`
	// Or this price.
	TriggerPrice *Decimal `json:"trigger_price,omitempty"`
	// Ticks beyond the entry, on the profit side.
	OffsetTicks *int64 `json:"offset_ticks,omitempty"`
}

// CreateStrategy: A new strategy. trailing_stop and break_even act on the position's exits: the answer is that
// exits strategy.
type CreateStrategy struct {
	// trailing_stop: a stop-loss that follows the price. break_even: the stop-losses move to the entry once the
	// price has gone your way far enough. oco: two orders, the first fill cancels the other. twap, vwap: an order in
	// market slices over time (vwap sized by yesterday's volume over the same hours). scaled: a ladder of limits.
	// chase: a limit that follows the bid or ask.
	Type CreateStrategyType `json:"type"`
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The order's side.
	Side *OrderSide `json:"side,omitempty"`
	// trailing_stop: its size (absent: what the stop-losses leave of the position). twap, vwap, scaled, chase: the
	// whole size.
	Size *Decimal `json:"size,omitempty"`
	// trailing_stop: how far behind the best price (or percent).
	Distance *Decimal `json:"distance,omitempty"`
	// trailing_stop: how far behind, in percent of the best price.
	Percent *Decimal `json:"percent,omitempty"`
	// trailing_stop: start once the price reaches this (until then, initial_stop_price rests, or nothing).
	ActivationPrice *Decimal `json:"activation_price,omitempty"`
	// trailing_stop: where the stop rests until the trail moves it (absent: from the price now).
	InitialStopPrice *Decimal `json:"initial_stop_price,omitempty"`
	// trailing_stop: cancel the position's other stop-losses first.
	ReplaceStops *bool `json:"replace_stops,omitempty"`
	// break_even: how far from the entry the price must go.
	TriggerDistance *Decimal `json:"trigger_distance,omitempty"`
	// break_even: the same, in percent of the entry.
	TriggerPercent *Decimal `json:"trigger_percent,omitempty"`
	// break_even: or once the price reaches this.
	TriggerPrice *Decimal `json:"trigger_price,omitempty"`
	// break_even: ticks beyond the entry, on the profit side. chase: ticks behind the bid (buy) or ask (sell).
	OffsetTicks *int64 `json:"offset_ticks,omitempty"`
	// oco: the two orders.
	Orders []CreateStrategyOrder `json:"orders,omitempty"`
	Exits  *OrderExits           `json:"exits,omitempty"`
	// twap, vwap: how long the schedule runs (bounds in the runner settings, see GET /v1/limits).
	DurationMinutes *int64 `json:"duration_minutes,omitempty"`
	// twap, vwap: time between slices; at most half the duration. Default: 60 (within those bounds).
	IntervalSeconds *int64 `json:"interval_seconds,omitempty"`
	// twap, vwap: vary each slice's time and size a little (the sizes still add up to size).
	Randomize *bool `json:"randomize,omitempty"`
	// twap, vwap, scaled, chase: only reduce your position (a twap ends when the position is closed).
	ReduceOnly *bool `json:"reduce_only,omitempty"`
	// twap, vwap (buy): slices wait while the price is above this. chase (buy): its cap.
	MaxPrice *Decimal `json:"max_price,omitempty"`
	// twap, vwap (sell): slices wait while the price is below this. chase (sell): its floor.
	MinPrice *Decimal `json:"min_price,omitempty"`
	// twap, vwap: each slice's slippage budget, in percent.
	MaxSlippagePct *Decimal `json:"max_slippage_pct,omitempty"`
	// scaled: the first rung.
	StartPrice *Decimal `json:"start_price,omitempty"`
	// scaled: the last rung.
	EndPrice *Decimal `json:"end_price,omitempty"`
	// scaled: how many limits (at most the runner setting).
	Rungs *int64 `json:"rungs,omitempty"`
	// scaled: equal sizes, growing toward end_price, or shrinking.
	Distribution *CreateStrategyDistribution `json:"distribution,omitempty"`
	// scaled: each rung refused if it would fill at once.
	PostOnly *bool `json:"post_only,omitempty"`
	// scaled: one rung refused cancels the others.
	AllOrNone *bool `json:"all_or_none,omitempty"`
	// scaled: rungs through the price may fill at once (refused otherwise, rung_would_fill).
	AllowMarketable *bool `json:"allow_marketable,omitempty"`
	// chase: how long it follows the price.
	MaxMinutes *int64 `json:"max_minutes,omitempty"`
	// chase: at max_minutes, cancel it, or send what is left at the market.
	OnExpiry *CreateStrategyOnExpiry `json:"on_expiry,omitempty"`
	// chase: it may rest through the last price (it would then fill at once).
	AllowCross *bool `json:"allow_cross,omitempty"`
	// Your own id for the strategy (oco, twap, vwap, scaled, chase): unique in this environment.
	ClientStrategyID *string `json:"client_strategy_id,omitempty"`
}

// CreateStrategyOrder: see the API reference.
type CreateStrategyOrder struct {
	// The order's side.
	Side OrderSide `json:"side"`
	// Two orders that rest.
	Type CreateStrategyOrderType `json:"type"`
	// Its size.
	Size Decimal `json:"size"`
	// Its limit price.
	LimitPrice *Decimal `json:"limit_price,omitempty"`
	// Its stop price.
	StopPrice *Decimal `json:"stop_price,omitempty"`
	// Refused if it would fill at once.
	PostOnly *bool `json:"post_only,omitempty"`
	// It may only reduce your position.
	ReduceOnly *bool `json:"reduce_only,omitempty"`
}

// ModifyOrder: What to change: at least one field. The order keeps its place in the engine (changed in place,
// never canceled and placed again).
type ModifyOrder struct {
	// The new size.
	Size *Decimal `json:"size,omitempty"`
	// The new limit price.
	LimitPrice *Decimal `json:"limit_price,omitempty"`
	// The new trigger price.
	StopPrice *Decimal `json:"stop_price,omitempty"`
}

// ClosePosition: What to close.
type ClosePosition struct {
	// How much to close; absent: the whole position.
	Size *Decimal `json:"size,omitempty"`
	// Your own id for the close order (a partial close).
	ClientOrderID *string `json:"client_order_id,omitempty"`
}

// CancelAll: Optional: only this market.
type CancelAll struct {
	// A market id: the asset and USD.
	Market *string `json:"market,omitempty"`
}

// MarginSettingInput: The market's setting. The mode cannot change while a position or a working order is open
// on the market.
type MarginSettingInput struct {
	// isolated: the position has its own margin. cross: positions share the account's balance.
	MarginMode MarginMode `json:"margin_mode"`
	// A whole number from 1 to the market's max_leverage.
	Leverage int64 `json:"leverage"`
}

// MarginSettingResult: see the API reference.
type MarginSettingResult struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The margin mode.
	MarginMode MarginMode `json:"margin_mode"`
	// The leverage.
	Leverage int64 `json:"leverage"`
}

// WhatIf: see the API reference.
type WhatIf struct {
	// What the order would do now: fill at once, rest, or be refused (reject says why).
	Result WhatIfResult  `json:"result"`
	Reject *WhatIfReject `json:"reject"`
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The order's side.
	Side OrderSide `json:"side"`
	// The order's type.
	Type CreateOrderType `json:"type"`
	// Its size (after a notional_usd is rounded to the size step).
	Size      *Decimal         `json:"size"`
	Execution *WhatIfExecution `json:"execution"`
	// Its value in USD.
	Notional *Decimal `json:"notional"`
	// The fee its fill would pay.
	Fee           *Decimal             `json:"fee"`
	Margin        *WhatIfMargin        `json:"margin"`
	PositionAfter *WhatIfPositionAfter `json:"position_after"`
	Exposure      *WhatIfExposure      `json:"exposure"`
}

// WhatIfReject: see the API reference.
type WhatIfReject struct {
	// The error code it would get.
	Code string `json:"code"`
	// What it means.
	Message *string `json:"message"`
	// More about it.
	Details json.RawMessage `json:"details,omitempty"`
}

// WhatIfExecution: see the API reference.
type WhatIfExecution struct {
	// It would fill at once.
	Marketable bool `json:"marketable"`
	// The price it would fill at (at once) or rest at.
	ExpectedPrice Decimal `json:"expected_price"`
	// A market order's worst price.
	WorstPrice *Decimal `json:"worst_price"`
}

// WhatIfMargin: see the API reference.
type WhatIfMargin struct {
	// The margin mode the position would use.
	Mode WhatIfMarginMode `json:"mode"`
	// The leverage it would use.
	Leverage int64 `json:"leverage"`
	// The margin the order needs.
	Required Decimal `json:"required"`
	// The margin available now.
	AvailableBefore *Decimal `json:"available_before"`
	// What would remain available.
	AvailableAfter *Decimal `json:"available_after"`
}

// WhatIfPositionAfter: see the API reference.
type WhatIfPositionAfter struct {
	// The position's size after the fill.
	Size Decimal `json:"size"`
	// Its side; null when flat.
	Side *PositionSide `json:"side"`
	// Its average price.
	AveragePrice *Decimal `json:"average_price"`
}

// WhatIfExposure: see the API reference.
type WhatIfExposure struct {
	// The market's exposure cap across your accounts of this product.
	Cap Decimal `json:"cap"`
	// Your open value on the market now.
	OpenBefore Decimal `json:"open_before"`
	// What would remain under the cap.
	RoomAfter Decimal `json:"room_after"`
}

// Event: see the API reference.
type Event struct {
	// The event; pass it as after (GET /v1/events) or resume (the stream) to continue from it.
	ID string `json:"id"`
	// What happened (GET /v1/webhook-event-types says what each type means).
	Type EventType `json:"type"`
	// The account number.
	AccountID string `json:"account_id"`
	// When it happened.
	Time int64 `json:"time"`
	// Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a
	// trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert.
	Source EventSource `json:"source"`
	// order or fill (the objects REST answers), with changes or close_reason when they apply. A position.* event
	// carries position { market, side, size, average_price } as the engine reported it, and previous_size: read GET
	// .../positions/{market} for its margin, P&L and liquidation.
	Data map[string]any `json:"data"`
}

// WebhookPayload: What a json webhook endpoint receives: the event as GET /v1/events shows it, with the
// endpoint's API version and environment.
type WebhookPayload struct {
	// The event; pass it as after (GET /v1/events) or resume (the stream) to continue from it.
	ID string `json:"id"`
	// What happened; webhook.ping is a test.
	Type WebhookPayloadType `json:"type"`
	// The account number (none on webhook.ping).
	AccountID *string `json:"account_id,omitempty"`
	// When it happened.
	Time int64 `json:"time"`
	// Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a
	// trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert.
	Source EventSource `json:"source"`
	// order or fill (the objects REST answers), with changes or close_reason when they apply. A position.* event
	// carries position { market, side, size, average_price } as the engine reported it, and previous_size: read GET
	// .../positions/{market} for its margin, P&L and liquidation.
	Data map[string]any `json:"data"`
	// The API version the endpoint receives.
	APIVersion string `json:"api_version"`
	// The environment of the endpoint and the account.
	Environment PlatformEnvironment `json:"environment"`
	// A test (webhook.ping) or a delivery resent as one.
	Test *bool `json:"test,omitempty"`
}

// ExitsLeg: see the API reference.
type ExitsLeg struct {
	// The leg: an order of this API, also listed under the account's orders.
	ID string `json:"id"`
	// take_profit: a limit on the profit side. stop_loss: a stop on the loss side.
	Group ExitsLegGroup `json:"group"`
	// take_profit legs are limits; stop_loss legs are stop_market or stop_limit.
	Type CreateStrategyOrderType `json:"type"`
	// The order's status; waiting: a stop-loss not placed yet (it waits for its activation price or its break-even).
	Status ExitsLegStatus `json:"status"`
	// Its size.
	Size Decimal `json:"size"`
	// The take-profit price, or the stop price.
	Price *Decimal `json:"price"`
	// The limit of a stop_limit leg.
	LimitPrice *Decimal `json:"limit_price"`
	// A trailing stop-loss: how it follows the price.
	Trail *ExitsLegTrail `json:"trail"`
	// A stop-loss moved to the entry price once the price has gone your way far enough.
	BreakEven *ExitsLegBreakEven `json:"break_even"`
}

// ExitsLegTrail: A trailing stop-loss: how it follows the price.
type ExitsLegTrail struct {
	// How far behind the best price (or null: percent).
	Distance *Decimal `json:"distance"`
	// How far behind, in percent of the best price (or null: distance).
	Percent *Decimal `json:"percent"`
	// It starts once the price reaches this; null: from the start.
	ActivationPrice *Decimal `json:"activation_price"`
	// It follows the price now (its activation price was reached, or it had none).
	Active bool `json:"active"`
	// The best price since it started, which the stop follows.
	BestPrice *Decimal `json:"best_price"`
}

// ExitsLegBreakEven: A stop-loss moved to the entry price once the price has gone your way far enough.
type ExitsLegBreakEven struct {
	// How far from the entry the price must go (or null: another trigger).
	TriggerDistance *Decimal `json:"trigger_distance"`
	// The same, in percent of the entry.
	TriggerPercent *Decimal `json:"trigger_percent"`
	// Or once the price reaches this.
	TriggerPrice *Decimal `json:"trigger_price"`
	// Ticks beyond the entry, on the profit side.
	OffsetTicks int64 `json:"offset_ticks"`
	// The stop-loss was moved to the entry already.
	Done bool `json:"done"`
}

// Exits: see the API reference.
type Exits struct {
	// The account number.
	AccountID string `json:"account_id"`
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The strategy that keeps these exits (type exits); null when there are none.
	StrategyID *string `json:"strategy_id"`
	// The open position; null when flat.
	Position *ExitsPosition `json:"position"`
	// The take-profit legs: together at most the position.
	TakeProfit []ExitsLeg `json:"take_profit"`
	// The stop-loss legs: together at most the position.
	StopLoss []ExitsLeg `json:"stop_loss"`
	// The legs grow with the position, keeping their share of it (rounded down to the size step).
	ResizeWithPosition bool `json:"resize_with_position"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x Exits) MarshalJSON() ([]byte, error) {
	type plain Exits
	p := plain(x)
	if p.TakeProfit == nil {
		p.TakeProfit = []ExitsLeg{}
	}
	if p.StopLoss == nil {
		p.StopLoss = []ExitsLeg{}
	}
	return json.Marshal(p)
}

// ExitsPosition: The open position; null when flat.
type ExitsPosition struct {
	// Long or short.
	Side PositionSide `json:"side"`
	// Its size.
	Size Decimal `json:"size"`
}

// ExitsBatch: The take-profit and stop-loss legs of a position, changed together.
type ExitsBatch struct {
	// The position you saw: { side, size }, or null.
	ExpectedPosition *ExitsBatchExpectedPosition `json:"expected_position"`
	// Every leg you saw (an empty list when there were none): if anything differs now, nothing is done and the
	// answer is 409 exits_changed with the state now.
	ExpectedExits []ExitsBatchExpectedExit `json:"expected_exits"`
	// Applied in this order: cancels, changes, then new legs (stop-losses first). An engine refusal stops the batch:
	// 422 exit_operation_failed says what each operation did.
	Operations []ExitsBatchOperation `json:"operations"`
	// The legs grow with the position (default true).
	ResizeWithPosition *bool `json:"resize_with_position,omitempty"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x ExitsBatch) MarshalJSON() ([]byte, error) {
	type plain ExitsBatch
	p := plain(x)
	if p.ExpectedExits == nil {
		p.ExpectedExits = []ExitsBatchExpectedExit{}
	}
	if p.Operations == nil {
		p.Operations = []ExitsBatchOperation{}
	}
	return json.Marshal(p)
}

// ExitsBatchExpectedPosition: The position you saw: { side, size }, or null.
type ExitsBatchExpectedPosition struct {
	// Long or short.
	Side PositionSide `json:"side"`
	// Its size.
	Size Decimal `json:"size"`
}

// ExitsBatchExpectedExit: see the API reference.
type ExitsBatchExpectedExit struct {
	// A leg.
	ID string `json:"id"`
	// Its size.
	Size Decimal `json:"size"`
	// Its price; null for a stop-loss that waits (as GET exits shows it).
	Price *Decimal `json:"price"`
}

// ExitsBatchOperation: see the API reference.
type ExitsBatchOperation struct {
	// What to do.
	Op ExitsBatchOperationOp `json:"op"`
	// modify, cancel: the leg.
	ID *string `json:"id,omitempty"`
	// place: the group of the new leg.
	Group *ExitsLegGroup `json:"group,omitempty"`
	// place: limit for a take-profit (the default); stop_market (the default) or stop_limit for a stop-loss.
	Type *CreateStrategyOrderType `json:"type,omitempty"`
	// The take-profit price, or the stop price.
	Price *Decimal `json:"price,omitempty"`
	// The limit of a stop_limit leg.
	LimitPrice *Decimal `json:"limit_price,omitempty"`
	// The size; absent on a place: what the group leaves of the position.
	Size *Decimal `json:"size,omitempty"`
	// A stop-loss that follows the price: { distance } (a price difference) or { percent }, with activation_price to
	// start only once the price reaches it. Without a price and an activation price it starts from the price now.
	// null removes it.
	Trail *ExitsBatchOperationTrail `json:"trail,omitempty"`
	// Move the stop-loss to the entry price (plus offset_ticks) once the price has gone your way by
	// trigger_distance, trigger_percent or to trigger_price. Without a price, the stop is placed only then. null
	// removes it.
	BreakEven *ExitsBatchOperationBreakEven `json:"break_even,omitempty"`
	// modify: the settings to remove from a stop-loss (the same as sending them null).
	Clear []ExitsBatchOperationClear `json:"clear,omitempty"`
}

// ExitsBatchOperationTrail: A stop-loss that follows the price: { distance } (a price difference) or { percent
// }, with activation_price to start only once the price reaches it. Without a price and an activation price it
// starts from the price now. null removes it.
type ExitsBatchOperationTrail struct {
	// How far behind the best price.
	Distance *Decimal `json:"distance,omitempty"`
	// How far behind, in percent of the best price.
	Percent *Decimal `json:"percent,omitempty"`
	// Start once the price reaches this.
	ActivationPrice *Decimal `json:"activation_price,omitempty"`
}

// ExitsBatchOperationBreakEven: Move the stop-loss to the entry price (plus offset_ticks) once the price has
// gone your way by trigger_distance, trigger_percent or to trigger_price. Without a price, the stop is placed
// only then. null removes it.
type ExitsBatchOperationBreakEven struct {
	// How far from the entry.
	TriggerDistance *Decimal `json:"trigger_distance,omitempty"`
	// How far from the entry, in percent.
	TriggerPercent *Decimal `json:"trigger_percent,omitempty"`
	// Or this price.
	TriggerPrice *Decimal `json:"trigger_price,omitempty"`
	// Ticks beyond the entry, on the profit side.
	OffsetTicks *int64 `json:"offset_ticks,omitempty"`
}

// Strategy: see the API reference.
type Strategy struct {
	// The strategy.
	ID string `json:"id"`
	// Your own id for it.
	ClientStrategyID *string `json:"client_strategy_id"`
	// The account number.
	AccountID string `json:"account_id"`
	// exits: a position's take-profit and stop-loss legs. bracket: an order with exits once it fills. oco: two
	// orders, the first fill cancels the other. twap, vwap: an order in slices over time. scaled: a ladder of
	// limits. chase: a limit that follows the price.
	Type StrategyType `json:"type"`
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The side of its orders (exits: the closing side).
	Side *OrderSide `json:"side"`
	// waiting: armed, nothing placed yet. working: running. completed: done as asked. canceled: you ended it. ended:
	// it stopped for end_reason.
	Status StrategyStatus `json:"status"`
	// Why it stopped.
	EndReason *StrategyEndReason `json:"end_reason"`
	// What it was created with.
	Params map[string]any `json:"params"`
	// How far it is (by type).
	Progress map[string]any `json:"progress"`
	// Every order it placed, oldest first.
	Orders []Order `json:"orders"`
	// When it was created.
	CreatedAt int64 `json:"created_at"`
	// Its latest change.
	UpdatedAt int64 `json:"updated_at"`
	// When it stopped.
	EndedAt *int64 `json:"ended_at"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x Strategy) MarshalJSON() ([]byte, error) {
	type plain Strategy
	p := plain(x)
	if p.Orders == nil {
		p.Orders = []Order{}
	}
	return json.Marshal(p)
}

// WebhookEndpoint: see the API reference.
type WebhookEndpoint struct {
	// The endpoint.
	ID string `json:"id"`
	// Its address: the host and the start of the path (never whole: chat addresses carry tokens).
	URL *string `json:"url"`
	// json: the signed event. discord, slack, telegram: a chat message.
	Format WebhookEndpointFormat `json:"format"`
	// The groups it receives (see GET /v1/webhook-event-types).
	Events []WebhookEndpointEvent `json:"events"`
	// disabled: we stopped after failures in a row; turn it on again from your dashboard.
	Status WebhookEndpointStatus `json:"status"`
	// The version its events are written in.
	APIVersion string `json:"api_version"`
	// Its environment: it receives that environment's events only.
	Environment PlatformEnvironment `json:"environment"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x WebhookEndpoint) MarshalJSON() ([]byte, error) {
	type plain WebhookEndpoint
	p := plain(x)
	if p.Events == nil {
		p.Events = []WebhookEndpointEvent{}
	}
	return json.Marshal(p)
}

// WebhookDelivery: see the API reference.
type WebhookDelivery struct {
	// The delivery.
	ID string `json:"id"`
	// Its endpoint.
	EndpointID string `json:"endpoint_id"`
	// The event sent (the same id when it is sent again).
	EventID string `json:"event_id"`
	// The event's type.
	EventType string `json:"event_type"`
	// Its account.
	AccountID *string `json:"account_id"`
	// A test.
	Test bool `json:"test"`
	// pending: waiting for its next try. failed: every try failed. canceled: the endpoint was disabled or deleted.
	Status WebhookDeliveryStatus `json:"status"`
	// Every try, oldest first.
	Attempts []WebhookDeliveryAttempt `json:"attempts"`
	// When it is tried again.
	NextAttemptAt *int64 `json:"next_attempt_at"`
	// When it was queued.
	CreatedAt int64 `json:"created_at"`
	// When your endpoint took it.
	DeliveredAt *int64 `json:"delivered_at"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x WebhookDelivery) MarshalJSON() ([]byte, error) {
	type plain WebhookDelivery
	p := plain(x)
	if p.Attempts == nil {
		p.Attempts = []WebhookDeliveryAttempt{}
	}
	return json.Marshal(p)
}

// WebhookDeliveryAttempt: see the API reference.
type WebhookDeliveryAttempt struct {
	// Which try.
	Attempt int64 `json:"attempt"`
	// When.
	At int64 `json:"at"`
	// What your endpoint answered; null when it could not be reached.
	HTTPStatus *int64 `json:"http_status"`
	// Why it failed, when it did (timeout, redirect, private_address, http_500, ...).
	Error *string `json:"error"`
	// How long it took.
	DurationMs *int64 `json:"duration_ms"`
	// The start of the answer (up to 512 bytes).
	Response *string `json:"response"`
}

// WebhookEventType: see the API reference.
type WebhookEventType struct {
	// The type.
	Type string `json:"type"`
	// The group that receives it; null: every endpoint (the test).
	Group *WebhookEventTypeGroup `json:"group"`
	// What it means.
	Description string `json:"description"`
}

// DeletedEndpoint: see the API reference.
type DeletedEndpoint struct {
	// The endpoint.
	ID string `json:"id"`
	// It was deleted.
	Deleted bool `json:"deleted"`
}

// TradingViewHook: see the API reference.
type TradingViewHook struct {
	// The link.
	ID string `json:"id"`
	// Its name.
	Name string `json:"name"`
	// The account number.
	AccountID string `json:"account_id"`
	// Its environment.
	Environment PlatformEnvironment `json:"environment"`
	// paused: alerts are refused (link_paused).
	Status TradingViewHookStatus `json:"status"`
	// The markets it may trade; empty: every market.
	Markets []string `json:"markets"`
	// It may only reduce positions.
	ReduceOnly bool `json:"reduce_only"`
	// Its largest order, in USD.
	MaxOrderUSD *Decimal `json:"max_order_usd"`
	// Its largest position, in USD.
	MaxPositionUSD *Decimal `json:"max_position_usd"`
	// Its daily loss stop: no new position once today's loss reaches it.
	DailyLossUSD *Decimal              `json:"daily_loss_usd"`
	Sizing       TradingViewHookSizing `json:"sizing"`
	// signal: an opposite alert reverses, a repeat is skipped. add: a repeat adds. target: the strategy's position
	// size is followed.
	PositionMode TradingViewHookPositionMode `json:"position_mode"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x TradingViewHook) MarshalJSON() ([]byte, error) {
	type plain TradingViewHook
	p := plain(x)
	if p.Markets == nil {
		p.Markets = []string{}
	}
	return json.Marshal(p)
}

// TradingViewHookSizing: see the API reference.
type TradingViewHookSizing struct {
	// from_alert: the size the alert gives. qty, usd, pct_balance: the link's own.
	Mode TradingViewHookSizingMode `json:"mode"`
	// The link's size, in its mode's unit.
	Value *Decimal `json:"value"`
}

// TradingViewAlert: see the API reference.
type TradingViewAlert struct {
	// The alert.
	ID string `json:"id"`
	// Its link.
	HookID string `json:"hook_id"`
	// The account number.
	AccountID string `json:"account_id"`
	// What it did.
	Status TradingViewAlertStatus `json:"status"`
	// Why it placed nothing (or not all): a reason code.
	Reason *string `json:"reason"`
	// That reason in words.
	ReasonText *string `json:"reason_text"`
	// How the message was read: universal, pickmytrade, traderspost, pineconnector, text.
	Format *string `json:"format"`
	// The market it named.
	Market *string `json:"market"`
	// Its action.
	Action *string `json:"action"`
	// The message as kept (credentials masked).
	Message *string `json:"message"`
	// The sender's address, its end masked.
	Sender *string `json:"sender"`
	// What it was to do: { legs } or { skip } or { refuse }.
	Plan map[string]any `json:"plan"`
	// What each leg did.
	Results []map[string]any `json:"results"`
	// More about a refusal.
	Details map[string]any `json:"details"`
	// The alert this one repeats.
	DuplicateOf *string `json:"duplicate_of"`
	// Refusals folded into this row (rate limits).
	RepeatCount int64 `json:"repeat_count"`
	// When it came.
	ReceivedAt int64 `json:"received_at"`
	// When it was done.
	FinishedAt *int64 `json:"finished_at"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x TradingViewAlert) MarshalJSON() ([]byte, error) {
	type plain TradingViewAlert
	p := plain(x)
	if p.Results == nil {
		p.Results = []map[string]any{}
	}
	return json.Marshal(p)
}

// TradingViewParseInput: A message to read, sent nowhere.
type TradingViewParseInput struct {
	// The alert's message, as TradingView would send it.
	Message string `json:"message"`
	// A link: the plan is then worked out on its account and settings, as of now.
	HookID *string `json:"hook_id,omitempty"`
}

// TradingViewParse: see the API reference.
type TradingViewParse struct {
	// It reads, and names a market we offer.
	Ok bool `json:"ok"`
	// Why not, when it does not.
	Code *string `json:"code"`
	// The field at fault.
	Field *string `json:"field"`
	// What went wrong.
	Message *string `json:"message"`
	// The message as read: action, side, sizes, prices, exits.
	Signal map[string]any `json:"signal"`
	// The market it names (ALL for every market).
	Market *string `json:"market"`
	// The market meant, when the ticker names one we offer only scaled.
	Suggestion *string `json:"suggestion"`
	// With hook_id: what the alert would do now ({ legs } / { skip } / { refuse }).
	Plan map[string]any `json:"plan"`
}

// DeletedHook: see the API reference.
type DeletedHook struct {
	// The link.
	ID string `json:"id"`
	// It was revoked: its address answers 404 from now on.
	Deleted bool `json:"deleted"`
}

// CancelAllResult: see the API reference.
type CancelAllResult struct {
	// The account number.
	AccountID string `json:"account_id"`
	// The market, or null for every market.
	Market *string `json:"market"`
	// How many working orders were canceled.
	Canceled int64 `json:"canceled"`
}

// FlattenResult: see the API reference.
type FlattenResult struct {
	// The account number.
	AccountID string `json:"account_id"`
	// How many working orders were canceled.
	OrdersCanceled int64 `json:"orders_canceled"`
	// The orders that closed the positions.
	Closes []Order `json:"closes"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x FlattenResult) MarshalJSON() ([]byte, error) {
	type plain FlattenResult
	p := plain(x)
	if p.Closes == nil {
		p.Closes = []Order{}
	}
	return json.Marshal(p)
}

// AccountRiskOrNull: The live risk; null while the account is being opened.
type AccountRiskOrNull struct {
	// When the oldest figure here was read.
	AsOf int64 `json:"as_of"`
	// Some figures are older than usual (the engine was busy).
	Stale bool `json:"stale"`
	// The balance.
	Balance Decimal `json:"balance"`
	// The balance plus the open P&L at the market price.
	Equity Decimal `json:"equity"`
	// The open P&L.
	UnrealizedPnL Decimal `json:"unrealized_pnl"`
	// The equity change since midnight New York, open positions included: what the daily loss rule counts.
	PnLToday Decimal `json:"pnl_today"`
	// The P&L closed today.
	RealizedToday Decimal `json:"realized_today"`
	// The fees paid today.
	FeesToday Decimal `json:"fees_today"`
	// Trades closed today.
	TradesToday int64 `json:"trades_today"`
	// The margin your positions hold.
	MarginUsed Decimal `json:"margin_used"`
	// What is left of the balance for new positions.
	MarginAvailable    *Decimal                             `json:"margin_available"`
	Exposure           AccountRiskOrNullExposure            `json:"exposure"`
	DailyLoss          *Limit                               `json:"daily_loss"`
	MaxDrawdown        *Limit                               `json:"max_drawdown"`
	ProfitTarget       *AccountRiskOrNullProfitTarget       `json:"profit_target"`
	TradingDays        AccountRiskOrNullTradingDays         `json:"trading_days"`
	NearestLiquidation *AccountRiskOrNullNearestLiquidation `json:"nearest_liquidation"`
	// When the trading day ends (midnight New York).
	TradingDayEndsAt int64 `json:"trading_day_ends_at"`
	// What needs your attention, most serious first.
	Warnings []Warning `json:"warnings"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x AccountRiskOrNull) MarshalJSON() ([]byte, error) {
	type plain AccountRiskOrNull
	p := plain(x)
	if p.Warnings == nil {
		p.Warnings = []Warning{}
	}
	return json.Marshal(p)
}

// AccountRiskOrNullExposure: see the API reference.
type AccountRiskOrNullExposure struct {
	// The value of every open position.
	Gross Decimal `json:"gross"`
	// The value of the long ones.
	Long Decimal `json:"long"`
	// The value of the short ones.
	Short Decimal `json:"short"`
	// Gross exposure over equity.
	EffectiveLeverage *Decimal `json:"effective_leverage"`
	// Per market, largest first.
	Markets []AccountRiskOrNullExposureMarket `json:"markets"`
}

// MarshalJSON sends a required list left nil as [], never null.
func (x AccountRiskOrNullExposure) MarshalJSON() ([]byte, error) {
	type plain AccountRiskOrNullExposure
	p := plain(x)
	if p.Markets == nil {
		p.Markets = []AccountRiskOrNullExposureMarket{}
	}
	return json.Marshal(p)
}

// AccountRiskOrNullExposureMarket: see the API reference.
type AccountRiskOrNullExposureMarket struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// The value held on it.
	Notional Decimal `json:"notional"`
	// That value over equity.
	Leverage *Decimal `json:"leverage"`
	// The exposure cap of the market.
	Cap *Decimal `json:"cap"`
	// How much of the cap is used, counting your other accounts on the same rule, in percent.
	CapUsedPct *Decimal `json:"cap_used_pct"`
}

// AccountRiskOrNullProfitTarget: see the API reference.
type AccountRiskOrNullProfitTarget struct {
	// The balance that reaches it.
	GoalBalance Decimal `json:"goal_balance"`
	// Still to make.
	Remaining Decimal `json:"remaining"`
	// How far, in percent.
	ProgressPct *Decimal `json:"progress_pct"`
	// Positions must be closed when it is reached.
	RequiresFlat bool `json:"requires_flat"`
}

// AccountRiskOrNullTradingDays: see the API reference.
type AccountRiskOrNullTradingDays struct {
	// Days with a trade so far.
	Count *int64 `json:"count"`
	// Days the rule needs.
	Required *int64 `json:"required"`
}

// AccountRiskOrNullNearestLiquidation: see the API reference.
type AccountRiskOrNullNearestLiquidation struct {
	// A market id: the asset and USD.
	Market string `json:"market"`
	// Its liquidation price.
	Price Decimal `json:"price"`
	// How far the price is from it, in percent.
	DistancePct Decimal `json:"distance_pct"`
}

// AccountDetail: see the API reference.
type AccountDetail struct {
	// The account number.
	ID string `json:"id"`
	// The environment the account lives in.
	Environment PlatformEnvironment `json:"environment"`
	// Where the account is in the programme.
	Stage AccountStage `json:"stage"`
	// pending: being opened; active: trading.
	Status AccountStatus `json:"status"`
	// The product it was bought as.
	Product *string `json:"product"`
	// Its size as sold.
	Tier *string `json:"tier"`
	// The balance it started with.
	StartBalance *Decimal `json:"start_balance"`
	// The balance: closed trades and fees only.
	Balance *Decimal `json:"balance"`
	Rule    *Rule    `json:"rule"`
	// A payout may be requested now.
	PayoutEligible bool `json:"payout_eligible"`
	// Sandbox only: parked after a while unused; it wakes the moment you use it.
	Parked *bool `json:"parked,omitempty"`
	// When the account was made.
	CreatedAt *int64             `json:"created_at"`
	Risk      *AccountRiskOrNull `json:"risk"`
}

// LimitStanding: see the API reference.
type LimitStanding struct {
	// The limit, in USD.
	Limit *Decimal `json:"limit,omitempty"`
	// The equity at which it is breached.
	Floor *Decimal `json:"floor,omitempty"`
	// How much more the account may lose before it.
	Room *Decimal `json:"room,omitempty"`
	// How much of it is used, in percent.
	UsedPct *Decimal `json:"used_pct,omitempty"`
	// Daily loss only: when it resets (midnight New York).
	ResetsAt *int64 `json:"resets_at,omitempty"`
	// Daily loss only: what happens when it is reached.
	OnBreach *OnBreach `json:"on_breach,omitempty"`
	// Daily loss only: the day's start is estimated (the engine does not publish it).
	Estimated *bool `json:"estimated,omitempty"`
	// Max drawdown only: how the reference moves.
	Mode *RuleMaxDrawdownMode `json:"mode,omitempty"`
	// Max drawdown only: the engine may hold a higher floor than shown (it follows equity highs).
	AtLeast *bool `json:"at_least,omitempty"`
	// The rule no longer applies this limit (its cut-off was reached).
	Lifted bool `json:"lifted"`
	// Where it stands.
	Status LimitStandingStatus `json:"status"`
}

// HistoryEquityParams: The query of get the equity curve.
type HistoryEquityParams struct {
	// One point per closed trade, or per trading day.
	Resolution *Resolution `json:"resolution,omitempty"`
}

func (p *HistoryEquityParams) values() url.Values {
	v := url.Values{}
	if p == nil {
		return v
	}
	if p.Resolution != nil {
		v.Set("resolution", string(*p.Resolution))
	}
	return v
}

// PositionsListParams: The query of list open positions.
type PositionsListParams struct {
	// Only this market.
	Market *string `json:"market,omitempty"`
}

func (p *PositionsListParams) values() url.Values {
	v := url.Values{}
	if p == nil {
		return v
	}
	if p.Market != nil {
		v.Set("market", string(*p.Market))
	}
	return v
}

// PositionsListClosedParams: The query of list closed positions.
type PositionsListClosedParams struct {
	// Only this market.
	Market *string `json:"market,omitempty"`
	// Page size.
	Limit *int64 `json:"limit,omitempty"`
	// next_cursor from the previous page, with the same filters.
	Cursor *string `json:"cursor,omitempty"`
}

func (p *PositionsListClosedParams) values() url.Values {
	v := url.Values{}
	if p == nil {
		return v
	}
	if p.Market != nil {
		v.Set("market", string(*p.Market))
	}
	if p.Limit != nil {
		v.Set("limit", strconv.FormatInt(*p.Limit, 10))
	}
	if p.Cursor != nil {
		v.Set("cursor", string(*p.Cursor))
	}
	return v
}

// OrdersListParams: The query of list orders.
type OrdersListParams struct {
	// working: the complete set of working orders. history: past orders. all: both.
	Status *OrdersListParamsStatus `json:"status,omitempty"`
	// Only this market.
	Market *string `json:"market,omitempty"`
	// The order you placed through the API with this id.
	ClientOrderID *string `json:"client_order_id,omitempty"`
	// Page size.
	Limit *int64 `json:"limit,omitempty"`
	// next_cursor from the previous page, with the same filters.
	Cursor *string `json:"cursor,omitempty"`
}

func (p *OrdersListParams) values() url.Values {
	v := url.Values{}
	if p == nil {
		return v
	}
	if p.Status != nil {
		v.Set("status", string(*p.Status))
	}
	if p.Market != nil {
		v.Set("market", string(*p.Market))
	}
	if p.ClientOrderID != nil {
		v.Set("client_order_id", string(*p.ClientOrderID))
	}
	if p.Limit != nil {
		v.Set("limit", strconv.FormatInt(*p.Limit, 10))
	}
	if p.Cursor != nil {
		v.Set("cursor", string(*p.Cursor))
	}
	return v
}

// EventsListParams: The query of list events.
type EventsListParams struct {
	// Only this account.
	AccountID *string `json:"account_id,omitempty"`
	// The id of the last event you have: the ones after it, oldest first. Absent: from the oldest kept.
	After *string `json:"after,omitempty"`
	// Page size.
	Limit *int64 `json:"limit,omitempty"`
}

func (p *EventsListParams) values() url.Values {
	v := url.Values{}
	if p == nil {
		return v
	}
	if p.AccountID != nil {
		v.Set("account_id", string(*p.AccountID))
	}
	if p.After != nil {
		v.Set("after", string(*p.After))
	}
	if p.Limit != nil {
		v.Set("limit", strconv.FormatInt(*p.Limit, 10))
	}
	return v
}

// StrategiesListParams: The query of list strategies.
type StrategiesListParams struct {
	// active: waiting or working. ended: completed, canceled or ended. all: both.
	Status *StrategiesListParamsStatus `json:"status,omitempty"`
	// Only this market.
	Market *string `json:"market,omitempty"`
	// The strategy you created with this id.
	ClientStrategyID *string `json:"client_strategy_id,omitempty"`
	// Page size.
	Limit *int64 `json:"limit,omitempty"`
	// next_cursor from the previous page, with the same filters.
	Cursor *string `json:"cursor,omitempty"`
}

func (p *StrategiesListParams) values() url.Values {
	v := url.Values{}
	if p == nil {
		return v
	}
	if p.Status != nil {
		v.Set("status", string(*p.Status))
	}
	if p.Market != nil {
		v.Set("market", string(*p.Market))
	}
	if p.ClientStrategyID != nil {
		v.Set("client_strategy_id", string(*p.ClientStrategyID))
	}
	if p.Limit != nil {
		v.Set("limit", strconv.FormatInt(*p.Limit, 10))
	}
	if p.Cursor != nil {
		v.Set("cursor", string(*p.Cursor))
	}
	return v
}

// WebhookEndpointsListDeliveriesParams: The query of list deliveries.
type WebhookEndpointsListDeliveriesParams struct {
	// Page size.
	Limit *int64 `json:"limit,omitempty"`
	// next_cursor from the previous page, with the same filters.
	Cursor *string `json:"cursor,omitempty"`
}

func (p *WebhookEndpointsListDeliveriesParams) values() url.Values {
	v := url.Values{}
	if p == nil {
		return v
	}
	if p.Limit != nil {
		v.Set("limit", strconv.FormatInt(*p.Limit, 10))
	}
	if p.Cursor != nil {
		v.Set("cursor", string(*p.Cursor))
	}
	return v
}

// TradingviewListAlertsParams: The query of list a link's alerts.
type TradingviewListAlertsParams struct {
	// Only alerts with this status.
	Status *string `json:"status,omitempty"`
	// Page size.
	Limit *int64 `json:"limit,omitempty"`
	// next_cursor from the previous page, with the same filters.
	Cursor *string `json:"cursor,omitempty"`
}

func (p *TradingviewListAlertsParams) values() url.Values {
	v := url.Values{}
	if p == nil {
		return v
	}
	if p.Status != nil {
		v.Set("status", string(*p.Status))
	}
	if p.Limit != nil {
		v.Set("limit", strconv.FormatInt(*p.Limit, 10))
	}
	if p.Cursor != nil {
		v.Set("cursor", string(*p.Cursor))
	}
	return v
}

// FillsListParams: The query of list fills.
type FillsListParams struct {
	// Only this market.
	Market *string `json:"market,omitempty"`
	// Page size.
	Limit *int64 `json:"limit,omitempty"`
	// next_cursor from the previous page, with the same filters.
	Cursor *string `json:"cursor,omitempty"`
}

func (p *FillsListParams) values() url.Values {
	v := url.Values{}
	if p == nil {
		return v
	}
	if p.Market != nil {
		v.Set("market", string(*p.Market))
	}
	if p.Limit != nil {
		v.Set("limit", strconv.FormatInt(*p.Limit, 10))
	}
	if p.Cursor != nil {
		v.Set("cursor", string(*p.Cursor))
	}
	return v
}
