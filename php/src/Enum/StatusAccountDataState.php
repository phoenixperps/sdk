<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Account figures over the last minute: degraded when more than 5% of them were older than usual (stale) or could not be read (state_unavailable). Open: the field is a string, and a value from a newer API version is kept as is. */
final class StatusAccountDataState
{
    public const OPERATIONAL = 'operational';
    public const DEGRADED = 'degraded';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::OPERATIONAL, self::DEGRADED];
}
