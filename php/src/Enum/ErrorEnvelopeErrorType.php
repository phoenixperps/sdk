<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** The family of the error; it decides the HTTP status. Open: the field is a string, and a value from a newer API version is kept as is. */
final class ErrorEnvelopeErrorType
{
    public const INVALID_REQUEST = 'invalid_request';
    public const AUTHENTICATION = 'authentication';
    public const PERMISSION = 'permission';
    public const NOT_FOUND = 'not_found';
    public const METHOD = 'method';
    public const CONFLICT = 'conflict';
    public const PAYLOAD = 'payload';
    public const RULE_REJECTION = 'rule_rejection';
    public const KEY_LIMIT = 'key_limit';
    public const RATE_LIMIT = 'rate_limit';
    public const SERVER = 'server';
    public const ENGINE = 'engine';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::INVALID_REQUEST, self::AUTHENTICATION, self::PERMISSION, self::NOT_FOUND, self::METHOD, self::CONFLICT, self::PAYLOAD, self::RULE_REJECTION, self::KEY_LIMIT, self::RATE_LIMIT, self::SERVER, self::ENGINE];
}
