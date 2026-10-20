<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** What to do. Open: the field is a string, and a value from a newer API version is kept as is. */
final class ExitsBatchOperationOp
{
    public const PLACE = 'place';
    public const MODIFY = 'modify';
    public const CANCEL = 'cancel';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::PLACE, self::MODIFY, self::CANCEL];
}
