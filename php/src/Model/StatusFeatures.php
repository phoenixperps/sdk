<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** StatusFeatures */
final class StatusFeatures implements \JsonSerializable
{
    private const WIRE = ['stream' => true, 'market_data' => true, 'webhooks' => true, 'tradingview' => true, 'mcp' => true];

    public function __construct(
        /** The account stream. */
        public readonly ?bool $stream = null,
        /** Market data. */
        public readonly ?bool $marketData = null,
        /** Webhooks. */
        public readonly ?bool $webhooks = null,
        /** TradingView alerts. */
        public readonly ?bool $tradingview = null,
        /** AI assistants (MCP). */
        public readonly ?bool $mcp = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            stream: $d['stream'] ?? null,
            marketData: $d['market_data'] ?? null,
            webhooks: $d['webhooks'] ?? null,
            tradingview: $d['tradingview'] ?? null,
            mcp: $d['mcp'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->stream !== null) {
            $out['stream'] = $this->stream;
        }
        if ($this->marketData !== null) {
            $out['market_data'] = $this->marketData;
        }
        if ($this->webhooks !== null) {
            $out['webhooks'] = $this->webhooks;
        }
        if ($this->tradingview !== null) {
            $out['tradingview'] = $this->tradingview;
        }
        if ($this->mcp !== null) {
            $out['mcp'] = $this->mcp;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
