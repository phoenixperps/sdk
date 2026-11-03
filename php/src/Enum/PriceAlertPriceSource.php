<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** The price it watches: the market's last trade (the middle of the book until there is one), the price trailing stops follow. Open: the field is a string, and a value from a newer API version is kept as is. */
final class PriceAlertPriceSource
{
    public const LAST = 'last';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::LAST];
}
