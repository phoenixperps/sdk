<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Whether the market trades now. Open: the field is a string, and a value from a newer API version is kept as is. */
final class MarketStatus
{
    public const OPEN = 'open';
    public const HALTED = 'halted';
    public const CLOSED = 'closed';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::OPEN, self::HALTED, self::CLOSED];
}
