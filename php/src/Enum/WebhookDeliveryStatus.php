<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** pending: waiting for its next try. failed: every try failed. canceled: the endpoint was disabled or deleted. Open: the field is a string, and a value from a newer API version is kept as is. */
final class WebhookDeliveryStatus
{
    public const PENDING = 'pending';
    public const DELIVERING = 'delivering';
    public const DELIVERED = 'delivered';
    public const FAILED = 'failed';
    public const CANCELED = 'canceled';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::PENDING, self::DELIVERING, self::DELIVERED, self::FAILED, self::CANCELED];
}
