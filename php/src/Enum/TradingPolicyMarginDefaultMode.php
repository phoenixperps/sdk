<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** The margin mode of a market you never set. Open: the field is a string, and a value from a newer API version is kept as is. */
final class TradingPolicyMarginDefaultMode
{
    public const ISOLATED = 'isolated';
    public const CROSS = 'cross';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ISOLATED, self::CROSS];
}
