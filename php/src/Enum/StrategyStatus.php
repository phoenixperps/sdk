<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** waiting: armed, nothing placed yet. working: running. completed: done as asked. canceled: you ended it. ended: it stopped for end_reason. Open: the field is a string, and a value from a newer API version is kept as is. */
final class StrategyStatus
{
    public const WAITING = 'waiting';
    public const WORKING = 'working';
    public const COMPLETED = 'completed';
    public const CANCELED = 'canceled';
    public const ENDED = 'ended';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::WAITING, self::WORKING, self::COMPLETED, self::CANCELED, self::ENDED];
}
