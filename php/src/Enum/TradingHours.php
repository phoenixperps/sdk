<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** When it trades: around the clock, its underlying market's sessions (Standard Market Hours, closed on weekends), or FX hours. Open: the field is a string, and a value from a newer API version is kept as is. */
final class TradingHours
{
    public const V_24_7 = '24/7';
    public const STANDARD = 'standard';
    public const FX = 'fx';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::V_24_7, self::STANDARD, self::FX];
}
