<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** The order's type. Open: the field is a string, and a value from a newer API version is kept as is. */
final class OrderType
{
    public const MARKET = 'market';
    public const LIMIT = 'limit';
    public const STOP_MARKET = 'stop_market';
    public const STOP_LIMIT = 'stop_limit';
    public const TAKE_MARKET = 'take_market';
    public const TAKE_LIMIT = 'take_limit';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::MARKET, self::LIMIT, self::STOP_MARKET, self::STOP_LIMIT, self::TAKE_MARKET, self::TAKE_LIMIT];
}
