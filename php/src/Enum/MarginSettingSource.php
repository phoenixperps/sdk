<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Where it comes from: the open position, your saved setting, or the market default. Open: the field is a string, and a value from a newer API version is kept as is. */
final class MarginSettingSource
{
    public const POSITION = 'position';
    public const SAVED = 'saved';
    public const DEFAULT = 'default';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::POSITION, self::SAVED, self::DEFAULT];
}
