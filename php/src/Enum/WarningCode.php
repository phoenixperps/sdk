<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** What it is about. Open: the field is a string, and a value from a newer API version is kept as is. */
final class WarningCode
{
    public const DAILY_LOSS_USED = 'daily_loss_used';
    public const MAX_DRAWDOWN_USED = 'max_drawdown_used';
    public const LIQUIDATION_NEAR = 'liquidation_near';
    public const NO_STOP_LOSS = 'no_stop_loss';
    public const MARGIN_USED = 'margin_used';
    public const ORDER_WITHOUT_POSITION = 'order_without_position';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::DAILY_LOSS_USED, self::MAX_DRAWDOWN_USED, self::LIQUIDATION_NEAR, self::NO_STOP_LOSS, self::MARGIN_USED, self::ORDER_WITHOUT_POSITION];
}
