<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** What happens when it is reached. Open: the field is a string, and a value from a newer API version is kept as is. */
final class OnBreach
{
    public const FAIL_ACCOUNT = 'fail_account';
    public const FLATTEN = 'flatten';
    public const FLATTEN_AND_PAUSE = 'flatten_and_pause';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::FAIL_ACCOUNT, self::FLATTEN, self::FLATTEN_AND_PAUSE];
}
