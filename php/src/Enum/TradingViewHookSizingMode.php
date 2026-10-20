<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** from_alert: the size the alert gives. qty, usd, pct_balance: the link's own. Open: the field is a string, and a value from a newer API version is kept as is. */
final class TradingViewHookSizingMode
{
    public const FROM_ALERT = 'from_alert';
    public const QTY = 'qty';
    public const USD = 'usd';
    public const PCT_BALANCE = 'pct_balance';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::FROM_ALERT, self::QTY, self::USD, self::PCT_BALANCE];
}
