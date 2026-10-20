<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps;

/** The spec this SDK was generated from: its version, hosts and stream URLs. */
final class Meta
{
    /** The version of the API spec. */
    public const SPEC_VERSION = '1.0.0';

    /** The API host of each environment. */
    public const SERVERS = [
        'live' => 'https://api.phoenixperpsfunding.com',
        'sandbox' => 'https://sandbox-api.phoenixperpsfunding.com',
    ];

    /** The account stream URL of each environment. */
    public const ACCOUNT_STREAMS = [
        'live' => 'wss://api.phoenixperpsfunding.com/v1/stream',
        'sandbox' => 'wss://sandbox-api.phoenixperpsfunding.com/v1/stream',
    ];

    /** The market stream URL. */
    public const MARKET_STREAM = 'wss://stream.phoenixperpsfunding.com/v1/market-data';
}
