<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Where the account is in the programme. Open: the field is a string, and a value from a newer API version is kept as is. */
final class AccountStage
{
    public const EVALUATION = 'evaluation';
    public const FUNDED = 'funded';
    public const SANDBOX = 'sandbox';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::EVALUATION, self::FUNDED, self::SANDBOX];
}
