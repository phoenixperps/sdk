<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** The group that receives it; null: every endpoint (the test). Open: the field is a string, and a value from a newer API version is kept as is. */
final class WebhookEventTypeGroup
{
    public const ORDERS = 'orders';
    public const POSITIONS = 'positions';
    public const ACCOUNT = 'account';
    public const RISK = 'risk';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ORDERS, self::POSITIONS, self::ACCOUNT, self::RISK];
}
