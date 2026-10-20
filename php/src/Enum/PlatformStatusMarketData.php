<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Whether the market-data stream is live, with every price feed. Open: the field is a string, and a value from a newer API version is kept as is. */
final class PlatformStatusMarketData
{
    public const UP = 'up';
    public const DOWN = 'down';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::UP, self::DOWN];
}
