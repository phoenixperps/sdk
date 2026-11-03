<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** above: it fires once the price reaches the level from below; below: from above. Set from the price when the level was set. Open: the field is a string, and a value from a newer API version is kept as is. */
final class PriceAlertCondition
{
    public const ABOVE = 'above';
    public const BELOW = 'below';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ABOVE, self::BELOW];
}
