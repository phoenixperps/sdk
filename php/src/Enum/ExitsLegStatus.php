<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** The order's status; waiting: a stop-loss not placed yet (it waits for its activation price or its break-even). Open: the field is a string, and a value from a newer API version is kept as is. */
final class ExitsLegStatus
{
    public const PENDING = 'pending';
    public const WORKING = 'working';
    public const FILLED = 'filled';
    public const CANCELED = 'canceled';
    public const REJECTED = 'rejected';
    public const EXPIRED = 'expired';
    public const UNCONFIRMED = 'unconfirmed';
    public const WAITING = 'waiting';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::PENDING, self::WORKING, self::FILLED, self::CANCELED, self::REJECTED, self::EXPIRED, self::UNCONFIRMED, self::WAITING];
}
