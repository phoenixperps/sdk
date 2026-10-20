<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Where it was placed: this API, an AI assistant (the MCP server), a trading platform, a strategy, a TradingView alert, or the system (a liquidation). Open: the field is a string, and a value from a newer API version is kept as is. */
final class OrderSource
{
    public const API = 'api';
    public const MCP = 'mcp';
    public const PLATFORM = 'platform';
    public const STRATEGY = 'strategy';
    public const TRADINGVIEW = 'tradingview';
    public const SYSTEM = 'system';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::API, self::MCP, self::PLATFORM, self::STRATEGY, self::TRADINGVIEW, self::SYSTEM];
}
