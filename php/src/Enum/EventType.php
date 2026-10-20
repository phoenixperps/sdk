<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** What happened (GET /v1/webhook-event-types says what each type means). Open: the field is a string, and a value from a newer API version is kept as is. */
final class EventType
{
    public const ORDER_CREATED = 'order.created';
    public const ORDER_EDITED = 'order.edited';
    public const ORDER_FILLED = 'order.filled';
    public const ORDER_CANCELED = 'order.canceled';
    public const ORDER_REJECTED = 'order.rejected';
    public const ORDER_EXPIRED = 'order.expired';
    public const FILL_CREATED = 'fill.created';
    public const POSITION_OPENED = 'position.opened';
    public const POSITION_CHANGED = 'position.changed';
    public const POSITION_CLOSED = 'position.closed';
    public const STRATEGY_CREATED = 'strategy.created';
    public const STRATEGY_LEG_PLACED = 'strategy.leg_placed';
    public const STRATEGY_LEG_MODIFIED = 'strategy.leg_modified';
    public const STRATEGY_LEG_FILLED = 'strategy.leg_filled';
    public const STRATEGY_LEG_CANCELED = 'strategy.leg_canceled';
    public const STRATEGY_LEG_REJECTED = 'strategy.leg_rejected';
    public const STRATEGY_ORPHAN_REPAIRED = 'strategy.orphan_repaired';
    public const STRATEGY_SLICE = 'strategy.slice';
    public const STRATEGY_ENDED = 'strategy.ended';
    public const TRADINGVIEW_ALERT_EXECUTED = 'tradingview.alert_executed';
    public const TRADINGVIEW_ALERT_REFUSED = 'tradingview.alert_refused';
    public const ACCOUNT_UPDATED = 'account.updated';
    public const ACCOUNT_PASSED = 'account.passed';
    public const ACCOUNT_FAILED = 'account.failed';
    public const ACCOUNT_PAYOUT_ELIGIBLE = 'account.payout_eligible';
    public const ACCOUNT_DISABLED = 'account.disabled';
    public const ACCOUNT_REMOVED = 'account.removed';
    public const KEY_LOSS_STOP = 'key.loss_stop';
    public const KEY_EXPIRING = 'key.expiring';
    public const SYSTEM_RESTARTED = 'system.restarted';
    public const RISK_WARNING = 'risk.warning';
    public const RISK_CLEARED = 'risk.cleared';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ORDER_CREATED, self::ORDER_EDITED, self::ORDER_FILLED, self::ORDER_CANCELED, self::ORDER_REJECTED, self::ORDER_EXPIRED, self::FILL_CREATED, self::POSITION_OPENED, self::POSITION_CHANGED, self::POSITION_CLOSED, self::STRATEGY_CREATED, self::STRATEGY_LEG_PLACED, self::STRATEGY_LEG_MODIFIED, self::STRATEGY_LEG_FILLED, self::STRATEGY_LEG_CANCELED, self::STRATEGY_LEG_REJECTED, self::STRATEGY_ORPHAN_REPAIRED, self::STRATEGY_SLICE, self::STRATEGY_ENDED, self::TRADINGVIEW_ALERT_EXECUTED, self::TRADINGVIEW_ALERT_REFUSED, self::ACCOUNT_UPDATED, self::ACCOUNT_PASSED, self::ACCOUNT_FAILED, self::ACCOUNT_PAYOUT_ELIGIBLE, self::ACCOUNT_DISABLED, self::ACCOUNT_REMOVED, self::KEY_LOSS_STOP, self::KEY_EXPIRING, self::SYSTEM_RESTARTED, self::RISK_WARNING, self::RISK_CLEARED];
}
