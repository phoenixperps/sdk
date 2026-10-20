<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** chase: at max_minutes, cancel it, or send what is left at the market. Open: the field is a string, and a value from a newer API version is kept as is. */
final class CreateStrategyOnExpiry
{
    public const CANCEL = 'cancel';
    public const MARKET = 'market';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::CANCEL, self::MARKET];
}
