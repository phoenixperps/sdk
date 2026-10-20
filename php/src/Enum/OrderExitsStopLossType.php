<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** The stop's type (default stop_market). Open: the field is a string, and a value from a newer API version is kept as is. */
final class OrderExitsStopLossType
{
    public const STOP_MARKET = 'stop_market';
    public const STOP_LIMIT = 'stop_limit';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::STOP_MARKET, self::STOP_LIMIT];
}
