<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** conditional, with another trigger_market: send the order once it trades at or above trigger_price, or at or below it. On this market it is set by the side and not given. Open: the field is a string, and a value from a newer API version is kept as is. */
final class CreateStrategyTriggerDirection
{
    public const ABOVE = 'above';
    public const BELOW = 'below';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ABOVE, self::BELOW];
}
