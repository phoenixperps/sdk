<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Enum;

/** Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert. Open: the field is a string, and a value from a newer API version is kept as is. */
final class EventSource
{
    public const API = 'api';
    public const MCP = 'mcp';
    public const PLATFORM = 'platform';
    public const ENGINE = 'engine';
    public const STRATEGY = 'strategy';
    public const TRADINGVIEW = 'tradingview';

    /** Every value this SDK version knows. */
    public const ALL_VALUES = [self::API, self::MCP, self::PLATFORM, self::ENGINE, self::STRATEGY, self::TRADINGVIEW];
}
