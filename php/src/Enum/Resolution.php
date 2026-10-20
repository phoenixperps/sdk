<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** One point per closed trade, or per trading day. Open: the field is a string, and a value from a newer API version is kept as is. */
final class Resolution
{
    public const TRADE = 'trade';
    public const DAY = 'day';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::TRADE, self::DAY];
}
