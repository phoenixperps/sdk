<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** market: now, at the best price within your worst price. limit: at your price or better. stop_market: a market order once the last price reaches the stop. stop_limit: a limit once the last price reaches the stop. Open: the field is a string, and a value from a newer API version is kept as is. */
final class CreateOrderType
{
    public const MARKET = 'market';
    public const LIMIT = 'limit';
    public const STOP_MARKET = 'stop_market';
    public const STOP_LIMIT = 'stop_limit';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::MARKET, self::LIMIT, self::STOP_MARKET, self::STOP_LIMIT];
}
