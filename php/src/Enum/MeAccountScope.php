<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** all: every account of the environment, new ones too. selected: only those listed. Open: the field is a string, and a value from a newer API version is kept as is. */
final class MeAccountScope
{
    public const ALL = 'all';
    public const SELECTED = 'selected';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::ALL, self::SELECTED];
}
