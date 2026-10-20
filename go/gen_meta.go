// Code generated from the API spec by generator/printers/go.js. DO NOT EDIT: run `node generator`.

package phoenixperps

// SpecVersion is the version of the API spec this SDK was generated from.
const SpecVersion = "1.0.0"

var servers = map[string]string{
	"live":    "https://api.phoenixperpsfunding.com",
	"sandbox": "https://sandbox-api.phoenixperpsfunding.com",
}

var accountStreamURLs = map[string]string{
	"live":    "wss://api.phoenixperpsfunding.com/v1/stream",
	"sandbox": "wss://sandbox-api.phoenixperpsfunding.com/v1/stream",
}

const marketStreamURL = "wss://stream.phoenixperpsfunding.com/v1/market-data"

// The error codes the API answers (Error.Code).
const (
	ErrCodeInvalidRequest           = "invalid_request"
	ErrCodeInvalidParameter         = "invalid_parameter"
	ErrCodeUnknownField             = "unknown_field"
	ErrCodeInvalidCursor            = "invalid_cursor"
	ErrCodeInvalidJSON              = "invalid_json"
	ErrCodeMissingAPIKey            = "missing_api_key"
	ErrCodeInvalidAPIKey            = "invalid_api_key"
	ErrCodeExpiredAPIKey            = "expired_api_key"
	ErrCodeRevokedAPIKey            = "revoked_api_key"
	ErrCodeInvalidToken             = "invalid_token"
	ErrCodeExpiredToken             = "expired_token"
	ErrCodeRevokedToken             = "revoked_token"
	ErrCodeWrongResource            = "wrong_resource"
	ErrCodeWrongEnvironment         = "wrong_environment"
	ErrCodeIPNotAllowed             = "ip_not_allowed"
	ErrCodeInsufficientPermission   = "insufficient_permission"
	ErrCodeAPIDisabled              = "api_disabled"
	ErrCodeInvalidSizeStep          = "invalid_size_step"
	ErrCodeInvalidPriceTick         = "invalid_price_tick"
	ErrCodePriceOutOfBand           = "price_out_of_band"
	ErrCodeStopAlreadyTriggered     = "stop_already_triggered"
	ErrCodePostOnlyWouldFill        = "post_only_would_fill"
	ErrCodeLimitInsideSpread        = "limit_inside_spread"
	ErrCodeOrderTooSmall            = "order_too_small"
	ErrCodeWorkingOrderCap          = "working_order_cap"
	ErrCodeClientOrderIDConflict    = "client_order_id_conflict"
	ErrCodeOrderNotWorking          = "order_not_working"
	ErrCodeIdempotencyKeyReused     = "idempotency_key_reused"
	ErrCodeJSONTooDeep              = "json_too_deep"
	ErrCodeOperationInProgress      = "operation_in_progress"
	ErrCodeKeyMarketNotAllowed      = "key_market_not_allowed"
	ErrCodeKeyReduceOnly            = "key_reduce_only"
	ErrCodeKeyMaxOrderNotional      = "key_max_order_notional"
	ErrCodeKeyMaxPositionNotional   = "key_max_position_notional"
	ErrCodeKeyLossStopActive        = "key_loss_stop_active"
	ErrCodeInsufficientMargin       = "insufficient_margin"
	ErrCodeExposureCapExceeded      = "exposure_cap_exceeded"
	ErrCodeReduceOnlyViolation      = "reduce_only_violation"
	ErrCodeSlippageExceeded         = "slippage_exceeded"
	ErrCodeNoPrice                  = "no_price"
	ErrCodeStaleQuote               = "stale_quote"
	ErrCodeMarketClosed             = "market_closed"
	ErrCodeMarketNotOffered         = "market_not_offered"
	ErrCodeMarketHalted             = "market_halted"
	ErrCodePositionLiquidating      = "position_liquidating"
	ErrCodeAccountPaused            = "account_paused"
	ErrCodeAccountNotTradable       = "account_not_tradable"
	ErrCodeInsufficientBalance      = "insufficient_balance"
	ErrCodeMarginModeLocked         = "margin_mode_locked"
	ErrCodeMarginRejected           = "margin_rejected"
	ErrCodeInvalidLeverage          = "invalid_leverage"
	ErrCodeLiquidationRisk          = "liquidation_risk"
	ErrCodePositionNotIsolated      = "position_not_isolated"
	ErrCodeEngineBusy               = "engine_busy"
	ErrCodeOrderRejected            = "order_rejected"
	ErrCodeExitsChanged             = "exits_changed"
	ErrCodeExitOperationFailed      = "exit_operation_failed"
	ErrCodeExitWouldFill            = "exit_would_fill"
	ErrCodeExitsExceedPosition      = "exits_exceed_position"
	ErrCodeExitLegCap               = "exit_leg_cap"
	ErrCodeStrategyLimit            = "strategy_limit"
	ErrCodeStrategyNotActive        = "strategy_not_active"
	ErrCodeStrategyNotFound         = "strategy_not_found"
	ErrCodeClientStrategyIDConflict = "client_strategy_id_conflict"
	ErrCodeWebhookEndpointNotFound  = "webhook_endpoint_not_found"
	ErrCodeTradingviewHookNotFound  = "tradingview_hook_not_found"
	ErrCodeTradingviewAlertNotFound = "tradingview_alert_not_found"
	ErrCodeWebhookEndpointNotActive = "webhook_endpoint_not_active"
	ErrCodeDeliveryNotFound         = "delivery_not_found"
	ErrCodeEventNotFound            = "event_not_found"
	ErrCodeRungWouldFill            = "rung_would_fill"
	ErrCodeStrategyTypeDisabled     = "strategy_type_disabled"
	ErrCodeAccountNotFound          = "account_not_found"
	ErrCodeMarketNotFound           = "market_not_found"
	ErrCodeOrderNotFound            = "order_not_found"
	ErrCodePositionNotFound         = "position_not_found"
	ErrCodeRouteNotFound            = "route_not_found"
	ErrCodeMethodNotAllowed         = "method_not_allowed"
	ErrCodePayloadTooLarge          = "payload_too_large"
	ErrCodeUriTooLong               = "uri_too_long"
	ErrCodeUnsupportedMediaType     = "unsupported_media_type"
	ErrCodeSandboxLimitReached      = "sandbox_limit_reached"
	ErrCodeAccountNotReady          = "account_not_ready"
	ErrCodeSandboxOpening           = "sandbox_opening"
	ErrCodeIdempotencyKeyRequired   = "idempotency_key_required"
	ErrCodeInvalidIdempotencyKey    = "invalid_idempotency_key"
	ErrCodeRateLimited              = "rate_limited"
	ErrCodeNotReady                 = "not_ready"
	ErrCodeStateUnavailable         = "state_unavailable"
	ErrCodeEngineUnavailable        = "engine_unavailable"
	ErrCodeLimiterUnavailable       = "limiter_unavailable"
	ErrCodeSandboxDisabled          = "sandbox_disabled"
	ErrCodeEnvironmentDisabled      = "environment_disabled"
	ErrCodeTemporarilyUnavailable   = "temporarily_unavailable"
	ErrCodeCapacity                 = "capacity"
	ErrCodeInternalError            = "internal_error"
)

// ErrorCodeInfo is what the spec says of an error code.
type ErrorCodeInfo struct {
	Type      string
	Status    int
	Retryable bool
}

// ErrorCodes describes every code the API answers.
var ErrorCodes = map[string]ErrorCodeInfo{
	"invalid_request":             {"invalid_request", 400, false},
	"invalid_parameter":           {"invalid_request", 400, false},
	"unknown_field":               {"invalid_request", 400, false},
	"invalid_cursor":              {"invalid_request", 400, false},
	"invalid_json":                {"invalid_request", 400, false},
	"missing_api_key":             {"authentication", 401, false},
	"invalid_api_key":             {"authentication", 401, false},
	"expired_api_key":             {"authentication", 401, false},
	"revoked_api_key":             {"authentication", 401, false},
	"invalid_token":               {"authentication", 401, false},
	"expired_token":               {"authentication", 401, false},
	"revoked_token":               {"authentication", 401, false},
	"wrong_resource":              {"authentication", 401, false},
	"wrong_environment":           {"authentication", 401, false},
	"ip_not_allowed":              {"permission", 403, false},
	"insufficient_permission":     {"permission", 403, false},
	"api_disabled":                {"permission", 403, false},
	"invalid_size_step":           {"invalid_request", 400, false},
	"invalid_price_tick":          {"invalid_request", 400, false},
	"price_out_of_band":           {"rule_rejection", 422, false},
	"stop_already_triggered":      {"rule_rejection", 422, false},
	"post_only_would_fill":        {"rule_rejection", 422, false},
	"limit_inside_spread":         {"rule_rejection", 422, false},
	"order_too_small":             {"rule_rejection", 422, false},
	"working_order_cap":           {"rule_rejection", 422, false},
	"client_order_id_conflict":    {"conflict", 409, false},
	"order_not_working":           {"conflict", 409, false},
	"idempotency_key_reused":      {"invalid_request", 400, false},
	"json_too_deep":               {"invalid_request", 400, false},
	"operation_in_progress":       {"conflict", 409, true},
	"key_market_not_allowed":      {"key_limit", 422, false},
	"key_reduce_only":             {"key_limit", 422, false},
	"key_max_order_notional":      {"key_limit", 422, false},
	"key_max_position_notional":   {"key_limit", 422, false},
	"key_loss_stop_active":        {"key_limit", 422, false},
	"insufficient_margin":         {"rule_rejection", 422, false},
	"exposure_cap_exceeded":       {"rule_rejection", 422, false},
	"reduce_only_violation":       {"rule_rejection", 422, false},
	"slippage_exceeded":           {"rule_rejection", 422, false},
	"no_price":                    {"engine", 503, true},
	"stale_quote":                 {"engine", 503, true},
	"market_closed":               {"rule_rejection", 422, false},
	"market_not_offered":          {"rule_rejection", 422, false},
	"market_halted":               {"rule_rejection", 422, false},
	"position_liquidating":        {"rule_rejection", 422, false},
	"account_paused":              {"rule_rejection", 422, false},
	"account_not_tradable":        {"rule_rejection", 422, false},
	"insufficient_balance":        {"rule_rejection", 422, false},
	"margin_mode_locked":          {"conflict", 409, false},
	"margin_rejected":             {"rule_rejection", 422, false},
	"invalid_leverage":            {"invalid_request", 400, false},
	"liquidation_risk":            {"rule_rejection", 422, false},
	"position_not_isolated":       {"conflict", 409, false},
	"engine_busy":                 {"engine", 503, true},
	"order_rejected":              {"rule_rejection", 422, false},
	"exits_changed":               {"conflict", 409, false},
	"exit_operation_failed":       {"rule_rejection", 422, false},
	"exit_would_fill":             {"rule_rejection", 422, false},
	"exits_exceed_position":       {"rule_rejection", 422, false},
	"exit_leg_cap":                {"rule_rejection", 422, false},
	"strategy_limit":              {"rule_rejection", 422, false},
	"strategy_not_active":         {"conflict", 409, false},
	"strategy_not_found":          {"not_found", 404, false},
	"client_strategy_id_conflict": {"conflict", 409, false},
	"webhook_endpoint_not_found":  {"not_found", 404, false},
	"tradingview_hook_not_found":  {"not_found", 404, false},
	"tradingview_alert_not_found": {"not_found", 404, false},
	"webhook_endpoint_not_active": {"conflict", 409, false},
	"delivery_not_found":          {"not_found", 404, false},
	"event_not_found":             {"not_found", 404, false},
	"rung_would_fill":             {"rule_rejection", 422, false},
	"strategy_type_disabled":      {"permission", 403, false},
	"account_not_found":           {"not_found", 404, false},
	"market_not_found":            {"not_found", 404, false},
	"order_not_found":             {"not_found", 404, false},
	"position_not_found":          {"not_found", 404, false},
	"route_not_found":             {"not_found", 404, false},
	"method_not_allowed":          {"method", 405, false},
	"payload_too_large":           {"payload", 413, false},
	"uri_too_long":                {"payload", 414, false},
	"unsupported_media_type":      {"payload", 415, false},
	"sandbox_limit_reached":       {"conflict", 409, false},
	"account_not_ready":           {"conflict", 409, true},
	"sandbox_opening":             {"conflict", 409, true},
	"idempotency_key_required":    {"invalid_request", 400, false},
	"invalid_idempotency_key":     {"invalid_request", 400, false},
	"rate_limited":                {"rate_limit", 429, true},
	"not_ready":                   {"engine", 503, true},
	"state_unavailable":           {"engine", 503, true},
	"engine_unavailable":          {"engine", 503, true},
	"limiter_unavailable":         {"server", 503, true},
	"sandbox_disabled":            {"engine", 503, false},
	"environment_disabled":        {"engine", 503, false},
	"temporarily_unavailable":     {"server", 503, true},
	"capacity":                    {"engine", 503, true},
	"internal_error":              {"server", 500, true},
}
