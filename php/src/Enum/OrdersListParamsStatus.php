<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** OrdersListParamsStatus Open: the field is a string, and a value from a newer API version is kept as is. */
final class OrdersListParamsStatus
{
    public const ALL = 'all';
    public const WORKING = 'working';
    public const HISTORY = 'history';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ALL, self::WORKING, self::HISTORY];
}
