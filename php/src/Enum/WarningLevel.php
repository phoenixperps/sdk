<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** How serious. Open: the field is a string, and a value from a newer API version is kept as is. */
final class WarningLevel
{
    public const INFO = 'info';
    public const WARNING = 'warning';
    public const CRITICAL = 'critical';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::INFO, self::WARNING, self::CRITICAL];
}
