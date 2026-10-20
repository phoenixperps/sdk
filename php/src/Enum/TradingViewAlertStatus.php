<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** What it did. Open: the field is a string, and a value from a newer API version is kept as is. */
final class TradingViewAlertStatus
{
    public const PROCESSING = 'processing';
    public const PLACED = 'placed';
    public const PARTIAL = 'partial';
    public const DRY_RUN = 'dry_run';
    public const DUPLICATE = 'duplicate';
    public const SKIPPED = 'skipped';
    public const REFUSED = 'refused';
    public const FAILED = 'failed';
    public const RATE_LIMITED = 'rate_limited';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::PROCESSING, self::PLACED, self::PARTIAL, self::DRY_RUN, self::DUPLICATE, self::SKIPPED, self::REFUSED, self::FAILED, self::RATE_LIMITED];
}
