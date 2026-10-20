<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** trailing_stop: a stop-loss that follows the price. break_even: the stop-losses move to the entry once the price has gone your way far enough. oco: two orders, the first fill cancels the other. twap, vwap: an order in market slices over time (vwap sized by yesterday's volume over the same hours). scaled: a ladder of limits. chase: a limit that follows the bid or ask. Open: the field is a string, and a value from a newer API version is kept as is. */
final class CreateStrategyType
{
    public const TRAILING_STOP = 'trailing_stop';
    public const BREAK_EVEN = 'break_even';
    public const OCO = 'oco';
    public const TWAP = 'twap';
    public const VWAP = 'vwap';
    public const SCALED = 'scaled';
    public const CHASE = 'chase';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::TRAILING_STOP, self::BREAK_EVEN, self::OCO, self::TWAP, self::VWAP, self::SCALED, self::CHASE];
}
