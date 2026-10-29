<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps;

/** The error codes the API answers (PhoenixPerpsException::$code). */
final class ErrorCodes
{
    /** 400 invalid_request. */
    public const INVALID_REQUEST = 'invalid_request';

    /** 400 invalid_request. */
    public const INVALID_PARAMETER = 'invalid_parameter';

    /** 400 invalid_request. */
    public const UNKNOWN_FIELD = 'unknown_field';

    /** 400 invalid_request. */
    public const INVALID_CURSOR = 'invalid_cursor';

    /** 400 invalid_request. */
    public const INVALID_JSON = 'invalid_json';

    /** 401 authentication. */
    public const MISSING_API_KEY = 'missing_api_key';

    /** 401 authentication. */
    public const INVALID_API_KEY = 'invalid_api_key';

    /** 401 authentication. */
    public const EXPIRED_API_KEY = 'expired_api_key';

    /** 401 authentication. */
    public const REVOKED_API_KEY = 'revoked_api_key';

    /** 401 authentication. */
    public const INVALID_TOKEN = 'invalid_token';

    /** 401 authentication. */
    public const EXPIRED_TOKEN = 'expired_token';

    /** 401 authentication. */
    public const REVOKED_TOKEN = 'revoked_token';

    /** 401 authentication. */
    public const WRONG_RESOURCE = 'wrong_resource';

    /** 401 authentication. */
    public const WRONG_ENVIRONMENT = 'wrong_environment';

    /** 403 permission. */
    public const IP_NOT_ALLOWED = 'ip_not_allowed';

    /** 403 permission. */
    public const INSUFFICIENT_PERMISSION = 'insufficient_permission';

    /** 403 permission. */
    public const API_DISABLED = 'api_disabled';

    /** 400 invalid_request. */
    public const INVALID_SIZE_STEP = 'invalid_size_step';

    /** 400 invalid_request. */
    public const INVALID_PRICE_TICK = 'invalid_price_tick';

    /** 422 rule_rejection. */
    public const PRICE_OUT_OF_BAND = 'price_out_of_band';

    /** 422 rule_rejection. */
    public const STOP_ALREADY_TRIGGERED = 'stop_already_triggered';

    /** 422 rule_rejection. */
    public const POST_ONLY_WOULD_FILL = 'post_only_would_fill';

    /** 422 rule_rejection. */
    public const LIMIT_INSIDE_SPREAD = 'limit_inside_spread';

    /** 422 rule_rejection. */
    public const ORDER_TOO_SMALL = 'order_too_small';

    /** 422 rule_rejection. */
    public const WORKING_ORDER_CAP = 'working_order_cap';

    /** 409 conflict. */
    public const CLIENT_ORDER_ID_CONFLICT = 'client_order_id_conflict';

    /** 409 conflict. */
    public const ORDER_NOT_WORKING = 'order_not_working';

    /** 400 invalid_request. */
    public const IDEMPOTENCY_KEY_REUSED = 'idempotency_key_reused';

    /** 400 invalid_request. */
    public const JSON_TOO_DEEP = 'json_too_deep';

    /** 409 conflict, worth trying again. */
    public const OPERATION_IN_PROGRESS = 'operation_in_progress';

    /** 422 key_limit. */
    public const KEY_MARKET_NOT_ALLOWED = 'key_market_not_allowed';

    /** 422 key_limit. */
    public const KEY_REDUCE_ONLY = 'key_reduce_only';

    /** 422 key_limit. */
    public const KEY_MAX_ORDER_NOTIONAL = 'key_max_order_notional';

    /** 422 key_limit. */
    public const KEY_MAX_POSITION_NOTIONAL = 'key_max_position_notional';

    /** 422 key_limit. */
    public const KEY_LOSS_STOP_ACTIVE = 'key_loss_stop_active';

    /** 422 rule_rejection. */
    public const INSUFFICIENT_MARGIN = 'insufficient_margin';

    /** 422 rule_rejection. */
    public const EXPOSURE_CAP_EXCEEDED = 'exposure_cap_exceeded';

    /** 422 rule_rejection. */
    public const REDUCE_ONLY_VIOLATION = 'reduce_only_violation';

    /** 422 rule_rejection. */
    public const SLIPPAGE_EXCEEDED = 'slippage_exceeded';

    /** 503 engine, worth trying again. */
    public const NO_PRICE = 'no_price';

    /** 503 engine, worth trying again. */
    public const STALE_QUOTE = 'stale_quote';

    /** 422 rule_rejection. */
    public const MARKET_CLOSED = 'market_closed';

    /** 422 rule_rejection. */
    public const MARKET_NOT_OFFERED = 'market_not_offered';

    /** 422 rule_rejection. */
    public const MARKET_OPTION_HELD = 'market_option_held';

    /** 422 rule_rejection. */
    public const WEEKEND_CLOSE = 'weekend_close';

    /** 422 rule_rejection. */
    public const MARKET_HALTED = 'market_halted';

    /** 422 rule_rejection. */
    public const POSITION_LIQUIDATING = 'position_liquidating';

    /** 422 rule_rejection. */
    public const ACCOUNT_PAUSED = 'account_paused';

    /** 422 rule_rejection. */
    public const ACCOUNT_NOT_TRADABLE = 'account_not_tradable';

    /** 422 rule_rejection. */
    public const INSUFFICIENT_BALANCE = 'insufficient_balance';

    /** 409 conflict. */
    public const MARGIN_MODE_LOCKED = 'margin_mode_locked';

    /** 422 rule_rejection. */
    public const MARGIN_REJECTED = 'margin_rejected';

    /** 400 invalid_request. */
    public const INVALID_LEVERAGE = 'invalid_leverage';

    /** 422 rule_rejection. */
    public const LIQUIDATION_RISK = 'liquidation_risk';

    /** 409 conflict. */
    public const POSITION_NOT_ISOLATED = 'position_not_isolated';

    /** 503 engine, worth trying again. */
    public const ENGINE_BUSY = 'engine_busy';

    /** 422 rule_rejection. */
    public const ORDER_REJECTED = 'order_rejected';

    /** 409 conflict. */
    public const EXITS_CHANGED = 'exits_changed';

    /** 422 rule_rejection. */
    public const EXIT_OPERATION_FAILED = 'exit_operation_failed';

    /** 422 rule_rejection. */
    public const EXIT_WOULD_FILL = 'exit_would_fill';

    /** 422 rule_rejection. */
    public const EXITS_EXCEED_POSITION = 'exits_exceed_position';

    /** 422 rule_rejection. */
    public const EXIT_LEG_CAP = 'exit_leg_cap';

    /** 422 rule_rejection. */
    public const STRATEGY_LIMIT = 'strategy_limit';

    /** 409 conflict. */
    public const STRATEGY_NOT_ACTIVE = 'strategy_not_active';

    /** 404 not_found. */
    public const STRATEGY_NOT_FOUND = 'strategy_not_found';

    /** 409 conflict. */
    public const CLIENT_STRATEGY_ID_CONFLICT = 'client_strategy_id_conflict';

    /** 404 not_found. */
    public const WEBHOOK_ENDPOINT_NOT_FOUND = 'webhook_endpoint_not_found';

    /** 404 not_found. */
    public const TRADINGVIEW_HOOK_NOT_FOUND = 'tradingview_hook_not_found';

    /** 404 not_found. */
    public const TRADINGVIEW_ALERT_NOT_FOUND = 'tradingview_alert_not_found';

    /** 409 conflict. */
    public const WEBHOOK_ENDPOINT_NOT_ACTIVE = 'webhook_endpoint_not_active';

    /** 404 not_found. */
    public const DELIVERY_NOT_FOUND = 'delivery_not_found';

    /** 404 not_found. */
    public const EVENT_NOT_FOUND = 'event_not_found';

    /** 422 rule_rejection. */
    public const RUNG_WOULD_FILL = 'rung_would_fill';

    /** 403 permission. */
    public const STRATEGY_TYPE_DISABLED = 'strategy_type_disabled';

    /** 404 not_found. */
    public const ACCOUNT_NOT_FOUND = 'account_not_found';

    /** 404 not_found. */
    public const MARKET_NOT_FOUND = 'market_not_found';

    /** 404 not_found. */
    public const ORDER_NOT_FOUND = 'order_not_found';

    /** 404 not_found. */
    public const POSITION_NOT_FOUND = 'position_not_found';

    /** 404 not_found. */
    public const ROUTE_NOT_FOUND = 'route_not_found';

    /** 405 method. */
    public const METHOD_NOT_ALLOWED = 'method_not_allowed';

    /** 413 payload. */
    public const PAYLOAD_TOO_LARGE = 'payload_too_large';

    /** 414 payload. */
    public const URI_TOO_LONG = 'uri_too_long';

    /** 415 payload. */
    public const UNSUPPORTED_MEDIA_TYPE = 'unsupported_media_type';

    /** 409 conflict. */
    public const SANDBOX_LIMIT_REACHED = 'sandbox_limit_reached';

    /** 409 conflict, worth trying again. */
    public const ACCOUNT_NOT_READY = 'account_not_ready';

    /** 409 conflict, worth trying again. */
    public const SANDBOX_OPENING = 'sandbox_opening';

    /** 400 invalid_request. */
    public const IDEMPOTENCY_KEY_REQUIRED = 'idempotency_key_required';

    /** 400 invalid_request. */
    public const INVALID_IDEMPOTENCY_KEY = 'invalid_idempotency_key';

    /** 429 rate_limit, worth trying again. */
    public const RATE_LIMITED = 'rate_limited';

    /** 503 engine, worth trying again. */
    public const NOT_READY = 'not_ready';

    /** 503 engine, worth trying again. */
    public const STATE_UNAVAILABLE = 'state_unavailable';

    /** 503 engine, worth trying again. */
    public const ENGINE_UNAVAILABLE = 'engine_unavailable';

    /** 503 server, worth trying again. */
    public const LIMITER_UNAVAILABLE = 'limiter_unavailable';

    /** 503 engine. */
    public const SANDBOX_DISABLED = 'sandbox_disabled';

    /** 503 engine. */
    public const ENVIRONMENT_DISABLED = 'environment_disabled';

    /** 503 server, worth trying again. */
    public const TEMPORARILY_UNAVAILABLE = 'temporarily_unavailable';

    /** 503 engine, worth trying again. */
    public const CAPACITY = 'capacity';

    /** 500 server, worth trying again. */
    public const INTERNAL_ERROR = 'internal_error';
}
