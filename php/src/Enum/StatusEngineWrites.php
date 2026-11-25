<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Closes, flattens, cancel-alls and margin changes over the last minute: degraded when the engine was too busy to take one (engine_busy). Open: the field is a string, and a value from a newer API version is kept as is. */
final class StatusEngineWrites
{
    public const OPERATIONAL = 'operational';
    public const DEGRADED = 'degraded';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::OPERATIONAL, self::DEGRADED];
}
