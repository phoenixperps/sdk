// Generated from the API spec by generator/printers/rust.js. Do not edit: run `node generator`.

/// The version of the API spec this SDK was generated from.
pub const SPEC_VERSION: &str = "1.0.0";

pub(crate) fn server(environment: &str) -> &'static str {
    match environment {
        "live" => "https://api.phoenixperpsfunding.com",
        "sandbox" => "https://sandbox-api.phoenixperpsfunding.com",
        _ => "https://sandbox-api.phoenixperpsfunding.com",
    }
}

pub(crate) fn account_stream(environment: &str) -> &'static str {
    match environment {
        "live" => "wss://api.phoenixperpsfunding.com/v1/stream",
        "sandbox" => "wss://sandbox-api.phoenixperpsfunding.com/v1/stream",
        _ => "wss://sandbox-api.phoenixperpsfunding.com/v1/stream",
    }
}

pub(crate) const MARKET_STREAM: &str = "wss://stream.phoenixperpsfunding.com/v1/market-data";

/// The error codes the API answers (`Error::code`).
pub mod codes {
    /// 400 invalid_request.
    pub const INVALID_REQUEST: &str = "invalid_request";
    /// 400 invalid_request.
    pub const INVALID_PARAMETER: &str = "invalid_parameter";
    /// 400 invalid_request.
    pub const UNKNOWN_FIELD: &str = "unknown_field";
    /// 400 invalid_request.
    pub const INVALID_CURSOR: &str = "invalid_cursor";
    /// 400 invalid_request.
    pub const INVALID_JSON: &str = "invalid_json";
    /// 401 authentication.
    pub const MISSING_API_KEY: &str = "missing_api_key";
    /// 401 authentication.
    pub const INVALID_API_KEY: &str = "invalid_api_key";
    /// 401 authentication.
    pub const EXPIRED_API_KEY: &str = "expired_api_key";
    /// 401 authentication.
    pub const REVOKED_API_KEY: &str = "revoked_api_key";
    /// 401 authentication.
    pub const INVALID_TOKEN: &str = "invalid_token";
    /// 401 authentication.
    pub const EXPIRED_TOKEN: &str = "expired_token";
    /// 401 authentication.
    pub const REVOKED_TOKEN: &str = "revoked_token";
    /// 401 authentication.
    pub const WRONG_RESOURCE: &str = "wrong_resource";
    /// 401 authentication.
    pub const WRONG_ENVIRONMENT: &str = "wrong_environment";
    /// 403 permission.
    pub const IP_NOT_ALLOWED: &str = "ip_not_allowed";
    /// 403 permission.
    pub const INSUFFICIENT_PERMISSION: &str = "insufficient_permission";
    /// 403 permission.
    pub const API_DISABLED: &str = "api_disabled";
    /// 400 invalid_request.
    pub const INVALID_SIZE_STEP: &str = "invalid_size_step";
    /// 400 invalid_request.
    pub const INVALID_PRICE_TICK: &str = "invalid_price_tick";
    /// 422 rule_rejection.
    pub const PRICE_OUT_OF_BAND: &str = "price_out_of_band";
    /// 422 rule_rejection.
    pub const STOP_ALREADY_TRIGGERED: &str = "stop_already_triggered";
    /// 422 rule_rejection.
    pub const POST_ONLY_WOULD_FILL: &str = "post_only_would_fill";
    /// 422 rule_rejection.
    pub const LIMIT_INSIDE_SPREAD: &str = "limit_inside_spread";
    /// 422 rule_rejection.
    pub const ORDER_TOO_SMALL: &str = "order_too_small";
    /// 422 rule_rejection.
    pub const WORKING_ORDER_CAP: &str = "working_order_cap";
    /// 409 conflict.
    pub const CLIENT_ORDER_ID_CONFLICT: &str = "client_order_id_conflict";
    /// 409 conflict.
    pub const ORDER_NOT_WORKING: &str = "order_not_working";
    /// 400 invalid_request.
    pub const IDEMPOTENCY_KEY_REUSED: &str = "idempotency_key_reused";
    /// 400 invalid_request.
    pub const JSON_TOO_DEEP: &str = "json_too_deep";
    /// 409 conflict, worth trying again.
    pub const OPERATION_IN_PROGRESS: &str = "operation_in_progress";
    /// 422 key_limit.
    pub const KEY_MARKET_NOT_ALLOWED: &str = "key_market_not_allowed";
    /// 422 key_limit.
    pub const KEY_REDUCE_ONLY: &str = "key_reduce_only";
    /// 422 key_limit.
    pub const KEY_MAX_ORDER_NOTIONAL: &str = "key_max_order_notional";
    /// 422 key_limit.
    pub const KEY_MAX_POSITION_NOTIONAL: &str = "key_max_position_notional";
    /// 422 key_limit.
    pub const KEY_LOSS_STOP_ACTIVE: &str = "key_loss_stop_active";
    /// 422 rule_rejection.
    pub const INSUFFICIENT_MARGIN: &str = "insufficient_margin";
    /// 422 rule_rejection.
    pub const EXPOSURE_CAP_EXCEEDED: &str = "exposure_cap_exceeded";
    /// 422 rule_rejection.
    pub const REDUCE_ONLY_VIOLATION: &str = "reduce_only_violation";
    /// 422 rule_rejection.
    pub const SLIPPAGE_EXCEEDED: &str = "slippage_exceeded";
    /// 503 engine, worth trying again.
    pub const NO_PRICE: &str = "no_price";
    /// 503 engine, worth trying again.
    pub const STALE_QUOTE: &str = "stale_quote";
    /// 422 rule_rejection.
    pub const MARKET_CLOSED: &str = "market_closed";
    /// 422 rule_rejection.
    pub const MARKET_NOT_OFFERED: &str = "market_not_offered";
    /// 422 rule_rejection.
    pub const MARKET_HALTED: &str = "market_halted";
    /// 422 rule_rejection.
    pub const POSITION_LIQUIDATING: &str = "position_liquidating";
    /// 422 rule_rejection.
    pub const ACCOUNT_PAUSED: &str = "account_paused";
    /// 422 rule_rejection.
    pub const ACCOUNT_NOT_TRADABLE: &str = "account_not_tradable";
    /// 422 rule_rejection.
    pub const INSUFFICIENT_BALANCE: &str = "insufficient_balance";
    /// 409 conflict.
    pub const MARGIN_MODE_LOCKED: &str = "margin_mode_locked";
    /// 422 rule_rejection.
    pub const MARGIN_REJECTED: &str = "margin_rejected";
    /// 400 invalid_request.
    pub const INVALID_LEVERAGE: &str = "invalid_leverage";
    /// 422 rule_rejection.
    pub const LIQUIDATION_RISK: &str = "liquidation_risk";
    /// 409 conflict.
    pub const POSITION_NOT_ISOLATED: &str = "position_not_isolated";
    /// 503 engine, worth trying again.
    pub const ENGINE_BUSY: &str = "engine_busy";
    /// 422 rule_rejection.
    pub const ORDER_REJECTED: &str = "order_rejected";
    /// 409 conflict.
    pub const EXITS_CHANGED: &str = "exits_changed";
    /// 422 rule_rejection.
    pub const EXIT_OPERATION_FAILED: &str = "exit_operation_failed";
    /// 422 rule_rejection.
    pub const EXIT_WOULD_FILL: &str = "exit_would_fill";
    /// 422 rule_rejection.
    pub const EXITS_EXCEED_POSITION: &str = "exits_exceed_position";
    /// 422 rule_rejection.
    pub const EXIT_LEG_CAP: &str = "exit_leg_cap";
    /// 422 rule_rejection.
    pub const STRATEGY_LIMIT: &str = "strategy_limit";
    /// 409 conflict.
    pub const STRATEGY_NOT_ACTIVE: &str = "strategy_not_active";
    /// 404 not_found.
    pub const STRATEGY_NOT_FOUND: &str = "strategy_not_found";
    /// 409 conflict.
    pub const CLIENT_STRATEGY_ID_CONFLICT: &str = "client_strategy_id_conflict";
    /// 404 not_found.
    pub const WEBHOOK_ENDPOINT_NOT_FOUND: &str = "webhook_endpoint_not_found";
    /// 404 not_found.
    pub const TRADINGVIEW_HOOK_NOT_FOUND: &str = "tradingview_hook_not_found";
    /// 404 not_found.
    pub const TRADINGVIEW_ALERT_NOT_FOUND: &str = "tradingview_alert_not_found";
    /// 409 conflict.
    pub const WEBHOOK_ENDPOINT_NOT_ACTIVE: &str = "webhook_endpoint_not_active";
    /// 404 not_found.
    pub const DELIVERY_NOT_FOUND: &str = "delivery_not_found";
    /// 404 not_found.
    pub const EVENT_NOT_FOUND: &str = "event_not_found";
    /// 422 rule_rejection.
    pub const RUNG_WOULD_FILL: &str = "rung_would_fill";
    /// 403 permission.
    pub const STRATEGY_TYPE_DISABLED: &str = "strategy_type_disabled";
    /// 404 not_found.
    pub const ACCOUNT_NOT_FOUND: &str = "account_not_found";
    /// 404 not_found.
    pub const MARKET_NOT_FOUND: &str = "market_not_found";
    /// 404 not_found.
    pub const ORDER_NOT_FOUND: &str = "order_not_found";
    /// 404 not_found.
    pub const POSITION_NOT_FOUND: &str = "position_not_found";
    /// 404 not_found.
    pub const ROUTE_NOT_FOUND: &str = "route_not_found";
    /// 405 method.
    pub const METHOD_NOT_ALLOWED: &str = "method_not_allowed";
    /// 413 payload.
    pub const PAYLOAD_TOO_LARGE: &str = "payload_too_large";
    /// 414 payload.
    pub const URI_TOO_LONG: &str = "uri_too_long";
    /// 415 payload.
    pub const UNSUPPORTED_MEDIA_TYPE: &str = "unsupported_media_type";
    /// 409 conflict.
    pub const SANDBOX_LIMIT_REACHED: &str = "sandbox_limit_reached";
    /// 409 conflict, worth trying again.
    pub const ACCOUNT_NOT_READY: &str = "account_not_ready";
    /// 409 conflict, worth trying again.
    pub const SANDBOX_OPENING: &str = "sandbox_opening";
    /// 400 invalid_request.
    pub const IDEMPOTENCY_KEY_REQUIRED: &str = "idempotency_key_required";
    /// 400 invalid_request.
    pub const INVALID_IDEMPOTENCY_KEY: &str = "invalid_idempotency_key";
    /// 429 rate_limit, worth trying again.
    pub const RATE_LIMITED: &str = "rate_limited";
    /// 503 engine, worth trying again.
    pub const NOT_READY: &str = "not_ready";
    /// 503 engine, worth trying again.
    pub const STATE_UNAVAILABLE: &str = "state_unavailable";
    /// 503 engine, worth trying again.
    pub const ENGINE_UNAVAILABLE: &str = "engine_unavailable";
    /// 503 server, worth trying again.
    pub const LIMITER_UNAVAILABLE: &str = "limiter_unavailable";
    /// 503 engine.
    pub const SANDBOX_DISABLED: &str = "sandbox_disabled";
    /// 503 engine.
    pub const ENVIRONMENT_DISABLED: &str = "environment_disabled";
    /// 503 server, worth trying again.
    pub const TEMPORARILY_UNAVAILABLE: &str = "temporarily_unavailable";
    /// 503 engine, worth trying again.
    pub const CAPACITY: &str = "capacity";
    /// 500 server, worth trying again.
    pub const INTERNAL_ERROR: &str = "internal_error";
}
