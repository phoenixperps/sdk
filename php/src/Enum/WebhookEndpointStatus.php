<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** disabled: we stopped after failures in a row; turn it on again from your dashboard. Open: the field is a string, and a value from a newer API version is kept as is. */
final class WebhookEndpointStatus
{
    public const ACTIVE = 'active';
    public const PAUSED = 'paused';
    public const DISABLED = 'disabled';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ACTIVE, self::PAUSED, self::DISABLED];
}
