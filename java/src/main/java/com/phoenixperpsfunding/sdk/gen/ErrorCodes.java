// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

/** The error codes the API answers (PhoenixPerpsException.code()). */
public final class ErrorCodes {
    private ErrorCodes() {
    }

    /** 400 invalid_request. */
    public static final String INVALID_REQUEST = "invalid_request";

    /** 400 invalid_request. */
    public static final String INVALID_PARAMETER = "invalid_parameter";

    /** 400 invalid_request. */
    public static final String UNKNOWN_FIELD = "unknown_field";

    /** 400 invalid_request. */
    public static final String INVALID_CURSOR = "invalid_cursor";

    /** 400 invalid_request. */
    public static final String INVALID_JSON = "invalid_json";

    /** 401 authentication. */
    public static final String MISSING_API_KEY = "missing_api_key";

    /** 401 authentication. */
    public static final String INVALID_API_KEY = "invalid_api_key";

    /** 401 authentication. */
    public static final String EXPIRED_API_KEY = "expired_api_key";

    /** 401 authentication. */
    public static final String REVOKED_API_KEY = "revoked_api_key";

    /** 401 authentication. */
    public static final String INVALID_TOKEN = "invalid_token";

    /** 401 authentication. */
    public static final String EXPIRED_TOKEN = "expired_token";

    /** 401 authentication. */
    public static final String REVOKED_TOKEN = "revoked_token";

    /** 401 authentication. */
    public static final String WRONG_RESOURCE = "wrong_resource";

    /** 401 authentication. */
    public static final String WRONG_ENVIRONMENT = "wrong_environment";

    /** 403 permission. */
    public static final String IP_NOT_ALLOWED = "ip_not_allowed";

    /** 403 permission. */
    public static final String INSUFFICIENT_PERMISSION = "insufficient_permission";

    /** 403 permission. */
    public static final String API_DISABLED = "api_disabled";

    /** 400 invalid_request. */
    public static final String INVALID_SIZE_STEP = "invalid_size_step";

    /** 400 invalid_request. */
    public static final String INVALID_PRICE_TICK = "invalid_price_tick";

    /** 422 rule_rejection. */
    public static final String PRICE_OUT_OF_BAND = "price_out_of_band";

    /** 422 rule_rejection. */
    public static final String STOP_ALREADY_TRIGGERED = "stop_already_triggered";

    /** 422 rule_rejection. */
    public static final String POST_ONLY_WOULD_FILL = "post_only_would_fill";

    /** 422 rule_rejection. */
    public static final String LIMIT_INSIDE_SPREAD = "limit_inside_spread";

    /** 422 rule_rejection. */
    public static final String ORDER_TOO_SMALL = "order_too_small";

    /** 422 rule_rejection. */
    public static final String WORKING_ORDER_CAP = "working_order_cap";

    /** 409 conflict. */
    public static final String CLIENT_ORDER_ID_CONFLICT = "client_order_id_conflict";

    /** 409 conflict. */
    public static final String ORDER_NOT_WORKING = "order_not_working";

    /** 400 invalid_request. */
    public static final String IDEMPOTENCY_KEY_REUSED = "idempotency_key_reused";

    /** 400 invalid_request. */
    public static final String JSON_TOO_DEEP = "json_too_deep";

    /** 409 conflict, worth trying again. */
    public static final String OPERATION_IN_PROGRESS = "operation_in_progress";

    /** 422 key_limit. */
    public static final String KEY_MARKET_NOT_ALLOWED = "key_market_not_allowed";

    /** 422 key_limit. */
    public static final String KEY_REDUCE_ONLY = "key_reduce_only";

    /** 422 key_limit. */
    public static final String KEY_MAX_ORDER_NOTIONAL = "key_max_order_notional";

    /** 422 key_limit. */
    public static final String KEY_MAX_POSITION_NOTIONAL = "key_max_position_notional";

    /** 422 key_limit. */
    public static final String KEY_LOSS_STOP_ACTIVE = "key_loss_stop_active";

    /** 422 rule_rejection. */
    public static final String INSUFFICIENT_MARGIN = "insufficient_margin";

    /** 422 rule_rejection. */
    public static final String EXPOSURE_CAP_EXCEEDED = "exposure_cap_exceeded";

    /** 422 rule_rejection. */
    public static final String REDUCE_ONLY_VIOLATION = "reduce_only_violation";

    /** 422 rule_rejection. */
    public static final String SLIPPAGE_EXCEEDED = "slippage_exceeded";

    /** 503 engine, worth trying again. */
    public static final String NO_PRICE = "no_price";

    /** 503 engine, worth trying again. */
    public static final String STALE_QUOTE = "stale_quote";

    /** 422 rule_rejection. */
    public static final String MARKET_CLOSED = "market_closed";

    /** 422 rule_rejection. */
    public static final String MARKET_NOT_OFFERED = "market_not_offered";

    /** 422 rule_rejection. */
    public static final String MARKET_HALTED = "market_halted";

    /** 422 rule_rejection. */
    public static final String POSITION_LIQUIDATING = "position_liquidating";

    /** 422 rule_rejection. */
    public static final String ACCOUNT_PAUSED = "account_paused";

    /** 422 rule_rejection. */
    public static final String ACCOUNT_NOT_TRADABLE = "account_not_tradable";

    /** 422 rule_rejection. */
    public static final String INSUFFICIENT_BALANCE = "insufficient_balance";

    /** 409 conflict. */
    public static final String MARGIN_MODE_LOCKED = "margin_mode_locked";

    /** 422 rule_rejection. */
    public static final String MARGIN_REJECTED = "margin_rejected";

    /** 400 invalid_request. */
    public static final String INVALID_LEVERAGE = "invalid_leverage";

    /** 422 rule_rejection. */
    public static final String LIQUIDATION_RISK = "liquidation_risk";

    /** 409 conflict. */
    public static final String POSITION_NOT_ISOLATED = "position_not_isolated";

    /** 503 engine, worth trying again. */
    public static final String ENGINE_BUSY = "engine_busy";

    /** 422 rule_rejection. */
    public static final String ORDER_REJECTED = "order_rejected";

    /** 409 conflict. */
    public static final String EXITS_CHANGED = "exits_changed";

    /** 422 rule_rejection. */
    public static final String EXIT_OPERATION_FAILED = "exit_operation_failed";

    /** 422 rule_rejection. */
    public static final String EXIT_WOULD_FILL = "exit_would_fill";

    /** 422 rule_rejection. */
    public static final String EXITS_EXCEED_POSITION = "exits_exceed_position";

    /** 422 rule_rejection. */
    public static final String EXIT_LEG_CAP = "exit_leg_cap";

    /** 422 rule_rejection. */
    public static final String STRATEGY_LIMIT = "strategy_limit";

    /** 409 conflict. */
    public static final String STRATEGY_NOT_ACTIVE = "strategy_not_active";

    /** 404 not_found. */
    public static final String STRATEGY_NOT_FOUND = "strategy_not_found";

    /** 409 conflict. */
    public static final String CLIENT_STRATEGY_ID_CONFLICT = "client_strategy_id_conflict";

    /** 404 not_found. */
    public static final String WEBHOOK_ENDPOINT_NOT_FOUND = "webhook_endpoint_not_found";

    /** 404 not_found. */
    public static final String TRADINGVIEW_HOOK_NOT_FOUND = "tradingview_hook_not_found";

    /** 404 not_found. */
    public static final String TRADINGVIEW_ALERT_NOT_FOUND = "tradingview_alert_not_found";

    /** 409 conflict. */
    public static final String WEBHOOK_ENDPOINT_NOT_ACTIVE = "webhook_endpoint_not_active";

    /** 404 not_found. */
    public static final String DELIVERY_NOT_FOUND = "delivery_not_found";

    /** 404 not_found. */
    public static final String EVENT_NOT_FOUND = "event_not_found";

    /** 422 rule_rejection. */
    public static final String RUNG_WOULD_FILL = "rung_would_fill";

    /** 403 permission. */
    public static final String STRATEGY_TYPE_DISABLED = "strategy_type_disabled";

    /** 404 not_found. */
    public static final String ACCOUNT_NOT_FOUND = "account_not_found";

    /** 404 not_found. */
    public static final String MARKET_NOT_FOUND = "market_not_found";

    /** 404 not_found. */
    public static final String ORDER_NOT_FOUND = "order_not_found";

    /** 404 not_found. */
    public static final String POSITION_NOT_FOUND = "position_not_found";

    /** 404 not_found. */
    public static final String ROUTE_NOT_FOUND = "route_not_found";

    /** 405 method. */
    public static final String METHOD_NOT_ALLOWED = "method_not_allowed";

    /** 413 payload. */
    public static final String PAYLOAD_TOO_LARGE = "payload_too_large";

    /** 414 payload. */
    public static final String URI_TOO_LONG = "uri_too_long";

    /** 415 payload. */
    public static final String UNSUPPORTED_MEDIA_TYPE = "unsupported_media_type";

    /** 409 conflict. */
    public static final String SANDBOX_LIMIT_REACHED = "sandbox_limit_reached";

    /** 409 conflict, worth trying again. */
    public static final String ACCOUNT_NOT_READY = "account_not_ready";

    /** 409 conflict, worth trying again. */
    public static final String SANDBOX_OPENING = "sandbox_opening";

    /** 400 invalid_request. */
    public static final String IDEMPOTENCY_KEY_REQUIRED = "idempotency_key_required";

    /** 400 invalid_request. */
    public static final String INVALID_IDEMPOTENCY_KEY = "invalid_idempotency_key";

    /** 429 rate_limit, worth trying again. */
    public static final String RATE_LIMITED = "rate_limited";

    /** 503 engine, worth trying again. */
    public static final String NOT_READY = "not_ready";

    /** 503 engine, worth trying again. */
    public static final String STATE_UNAVAILABLE = "state_unavailable";

    /** 503 engine, worth trying again. */
    public static final String ENGINE_UNAVAILABLE = "engine_unavailable";

    /** 503 server, worth trying again. */
    public static final String LIMITER_UNAVAILABLE = "limiter_unavailable";

    /** 503 engine. */
    public static final String SANDBOX_DISABLED = "sandbox_disabled";

    /** 503 engine. */
    public static final String ENVIRONMENT_DISABLED = "environment_disabled";

    /** 503 server, worth trying again. */
    public static final String TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";

    /** 503 engine, worth trying again. */
    public static final String CAPACITY = "capacity";

    /** 500 server, worth trying again. */
    public static final String INTERNAL_ERROR = "internal_error";
}
