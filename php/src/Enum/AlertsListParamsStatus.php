<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** AlertsListParamsStatus Open: the field is a string, and a value from a newer API version is kept as is. */
final class AlertsListParamsStatus
{
    public const ALL = 'all';
    public const ARMED = 'armed';
    public const TRIGGERED = 'triggered';
    public const DISABLED = 'disabled';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ALL, self::ARMED, self::TRIGGERED, self::DISABLED];
}
