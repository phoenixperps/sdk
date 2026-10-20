<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Isolated: each position holds its own margin. Cross: positions share the balance. Open: the field is a string, and a value from a newer API version is kept as is. */
final class MarginMode
{
    public const ISOLATED = 'isolated';
    public const CROSS = 'cross';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ISOLATED, self::CROSS];
}
