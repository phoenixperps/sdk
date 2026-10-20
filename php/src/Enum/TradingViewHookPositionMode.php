<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** signal: an opposite alert reverses, a repeat is skipped. add: a repeat adds. target: the strategy's position size is followed. Open: the field is a string, and a value from a newer API version is kept as is. */
final class TradingViewHookPositionMode
{
    public const SIGNAL = 'signal';
    public const ADD = 'add';
    public const TARGET = 'target';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::SIGNAL, self::ADD, self::TARGET];
}
