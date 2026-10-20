<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Whether it is reached. Open: the field is a string, and a value from a newer API version is kept as is. */
final class ProgressObjectivesProfitTargetStatus
{
    public const OPEN = 'open';
    public const MET = 'met';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::OPEN, self::MET];
}
