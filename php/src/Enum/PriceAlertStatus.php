<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** armed: it waits for its level. triggered: it fired (rearm arms it again). disabled: its account or market went away before it fired. Open: the field is a string, and a value from a newer API version is kept as is. */
final class PriceAlertStatus
{
    public const ARMED = 'armed';
    public const TRIGGERED = 'triggered';
    public const DISABLED = 'disabled';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ARMED, self::TRIGGERED, self::DISABLED];
}
