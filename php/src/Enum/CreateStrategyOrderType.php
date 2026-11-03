<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** conditional: the order it sends (default market). Open: the field is a string, and a value from a newer API version is kept as is. */
final class CreateStrategyOrderType
{
    public const MARKET = 'market';
    public const LIMIT = 'limit';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::MARKET, self::LIMIT];
}
