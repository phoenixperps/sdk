<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** The order's side; null for an order placed elsewhere before the API saw it. Open: the field is a string, and a value from a newer API version is kept as is. */
final class OrderSide
{
    public const BUY = 'buy';
    public const SELL = 'sell';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::BUY, self::SELL];
}
