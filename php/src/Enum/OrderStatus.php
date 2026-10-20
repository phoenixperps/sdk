<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** unconfirmed: the engine has not confirmed its last state yet. Open: the field is a string, and a value from a newer API version is kept as is. */
final class OrderStatus
{
    public const PENDING = 'pending';
    public const WORKING = 'working';
    public const FILLED = 'filled';
    public const CANCELED = 'canceled';
    public const REJECTED = 'rejected';
    public const EXPIRED = 'expired';
    public const UNCONFIRMED = 'unconfirmed';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::PENDING, self::WORKING, self::FILLED, self::CANCELED, self::REJECTED, self::EXPIRED, self::UNCONFIRMED];
}
