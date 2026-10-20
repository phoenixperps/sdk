<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** scaled: equal sizes, growing toward end_price, or shrinking. Open: the field is a string, and a value from a newer API version is kept as is. */
final class CreateStrategyDistribution
{
    public const FLAT = 'flat';
    public const ASCENDING = 'ascending';
    public const DESCENDING = 'descending';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::FLAT, self::ASCENDING, self::DESCENDING];
}
