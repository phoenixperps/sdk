<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** json: the signed event. discord, slack, telegram: a chat message. Open: the field is a string, and a value from a newer API version is kept as is. */
final class WebhookEndpointFormat
{
    public const JSON = 'json';
    public const DISCORD = 'discord';
    public const SLACK = 'slack';
    public const TELEGRAM = 'telegram';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::JSON, self::DISCORD, self::SLACK, self::TELEGRAM];
}
