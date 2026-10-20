<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** pending: being opened; active: trading. Open: the field is a string, and a value from a newer API version is kept as is. */
final class AccountStatus
{
    public const PENDING = 'pending';
    public const ACTIVE = 'active';
    public const PASSED = 'passed';
    public const FAILED = 'failed';
    public const CLOSED = 'closed';
    public const SUSPENDED = 'suspended';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::PENDING, self::ACTIVE, self::PASSED, self::FAILED, self::CLOSED, self::SUSPENDED];
}
