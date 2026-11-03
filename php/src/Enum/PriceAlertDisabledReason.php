<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Why it was disabled. Open: the field is a string, and a value from a newer API version is kept as is. */
final class PriceAlertDisabledReason
{
    public const ACCOUNT_REMOVED = 'account_removed';
    public const MARKET_REMOVED = 'market_removed';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ACCOUNT_REMOVED, self::MARKET_REMOVED];
}
