<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Why it was canceled. Open: the field is a string, and a value from a newer API version is kept as is. */
final class OrderCancelReason
{
    public const USER = 'user';
    public const CANCEL_ALL = 'cancel_all';
    public const FLATTEN = 'flatten';
    public const CLOSE_ALL = 'close_all';
    public const POSITION_CLOSED = 'position_closed';
    public const POSITION_REVERSED = 'position_reversed';
    public const CLOSE_ASSIST = 'close_assist';
    public const OCO = 'oco';
    public const REPLACED = 'replaced';
    public const STRATEGY = 'strategy';
    public const KEY_LOSS_STOP = 'key_loss_stop';
    public const RISK_RULE = 'risk_rule';
    public const SYSTEM_RESTART = 'system_restart';
    public const EXTERNAL = 'external';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::USER, self::CANCEL_ALL, self::FLATTEN, self::CLOSE_ALL, self::POSITION_CLOSED, self::POSITION_REVERSED, self::CLOSE_ASSIST, self::OCO, self::REPLACED, self::STRATEGY, self::KEY_LOSS_STOP, self::RISK_RULE, self::SYSTEM_RESTART, self::EXTERNAL];
}
