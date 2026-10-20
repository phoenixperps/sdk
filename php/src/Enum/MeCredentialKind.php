<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** What you called with: an API key, or an app you signed in to. Open: the field is a string, and a value from a newer API version is kept as is. */
final class MeCredentialKind
{
    public const KEY = 'key';
    public const APP = 'app';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::KEY, self::APP];
}
