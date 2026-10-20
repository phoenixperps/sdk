<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Why it stopped. Open: the field is a string, and a value from a newer API version is kept as is. */
final class StrategyEndReason
{
    public const TAKE_PROFIT = 'take_profit';
    public const STOP_LOSS = 'stop_loss';
    public const FILLED = 'filled';
    public const POSITION_CLOSED = 'position_closed';
    public const POSITION_REVERSED = 'position_reversed';
    public const CANCELED_BY_USER = 'canceled_by_user';
    public const EXTERNAL = 'external';
    public const KEY_REVOKED = 'key_revoked';
    public const ACCOUNT_CLOSED = 'account_closed';
    public const ENGINE_RESTART = 'engine_restart';
    public const CHILD_REJECTED = 'child_rejected';
    public const PRICE_BOUND = 'price_bound';
    public const EXPIRED = 'expired';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::TAKE_PROFIT, self::STOP_LOSS, self::FILLED, self::POSITION_CLOSED, self::POSITION_REVERSED, self::CANCELED_BY_USER, self::EXTERNAL, self::KEY_REVOKED, self::ACCOUNT_CLOSED, self::ENGINE_RESTART, self::CHILD_REJECTED, self::PRICE_BOUND, self::EXPIRED];
}
