<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** How the reference moves: end_of_day and end_of_trade follow the highest balance, static stays at the start, intraday_equity follows the highest equity. Open: the field is a string, and a value from a newer API version is kept as is. */
final class RuleMaxDrawdownMode
{
    public const END_OF_DAY = 'end_of_day';
    public const END_OF_TRADE = 'end_of_trade';
    public const STATIC = 'static';
    public const INTRADAY_EQUITY = 'intraday_equity';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::END_OF_DAY, self::END_OF_TRADE, self::STATIC, self::INTRADAY_EQUITY];
}
