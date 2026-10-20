<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** How long it rests. Open: the field is a string, and a value from a newer API version is kept as is. */
final class TimeInForce
{
    public const GTC = 'gtc';
    public const GTD = 'gtd';
    public const IOC = 'ioc';
    public const FOK = 'fok';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::GTC, self::GTD, self::IOC, self::FOK];
}
