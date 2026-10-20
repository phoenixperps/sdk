<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** The environment of the host you called. Open: the field is a string, and a value from a newer API version is kept as is. */
final class PlatformEnvironment
{
    public const LIVE = 'live';
    public const SANDBOX = 'sandbox';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::LIVE, self::SANDBOX];
}
