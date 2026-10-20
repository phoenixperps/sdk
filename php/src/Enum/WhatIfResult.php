<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** What the order would do now: fill at once, rest, or be refused (reject says why). Open: the field is a string, and a value from a newer API version is kept as is. */
final class WhatIfResult
{
    public const WOULD_FILL = 'would_fill';
    public const WOULD_REST = 'would_rest';
    public const WOULD_REJECT = 'would_reject';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::WOULD_FILL, self::WOULD_REST, self::WOULD_REJECT];
}
