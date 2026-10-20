<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** exits: a position's take-profit and stop-loss legs. bracket: an order with exits once it fills. oco: two orders, the first fill cancels the other. twap, vwap: an order in slices over time. scaled: a ladder of limits. chase: a limit that follows the price. Open: the field is a string, and a value from a newer API version is kept as is. */
final class StrategyType
{
    public const EXITS = 'exits';
    public const BRACKET = 'bracket';
    public const OCO = 'oco';
    public const TWAP = 'twap';
    public const VWAP = 'vwap';
    public const SCALED = 'scaled';
    public const CHASE = 'chase';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::EXITS, self::BRACKET, self::OCO, self::TWAP, self::VWAP, self::SCALED, self::CHASE];
}
