<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** read: look only. trade: also place and cancel. Open: the field is a string, and a value from a newer API version is kept as is. */
final class MeCredentialAccess
{
    public const READ = 'read';
    public const TRADE = 'trade';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::READ, self::TRADE];
}
