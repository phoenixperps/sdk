<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Whether outbound webhooks are being delivered. Open: the field is a string, and a value from a newer API version is kept as is. */
final class PlatformStatusWebhooks
{
    public const UP = 'up';
    public const DOWN = 'down';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::UP, self::DOWN];
}
