<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** What the day's loss is measured from. Open: the field is a string, and a value from a newer API version is kept as is. */
final class RuleDailyLossBasis
{
    public const PEAK_BALANCE = 'peak_balance';
    public const START_BALANCE = 'start_balance';
    public const PEAK_EQUITY = 'peak_equity';
    public const DAY_START_BALANCE = 'day_start_balance';
    public const DAY_START_EQUITY = 'day_start_equity';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::PEAK_BALANCE, self::START_BALANCE, self::PEAK_EQUITY, self::DAY_START_BALANCE, self::DAY_START_EQUITY];
}
