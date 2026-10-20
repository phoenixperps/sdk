<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** ExitsBatchOperationClear Open: the field is a string, and a value from a newer API version is kept as is. */
final class ExitsBatchOperationClear
{
    public const TRAIL = 'trail';
    public const BREAK_EVEN = 'break_even';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::TRAIL, self::BREAK_EVEN];
}
