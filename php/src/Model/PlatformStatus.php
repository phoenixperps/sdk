<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** PlatformStatus */
final class PlatformStatus implements \JsonSerializable
{
    private const WIRE = ['engine' => true, 'engine_started_at' => true, 'market_data' => true, 'webhooks' => true];

    public function __construct(
        /** Whether the trading engine is reachable. */
        public readonly ?string $engine = null,
        /** When the trading engine last started. */
        public readonly ?int $engineStartedAt = null,
        /** Whether the market-data stream is live, with every price feed. */
        public readonly ?string $marketData = null,
        /** Whether outbound webhooks are being delivered. */
        public readonly ?string $webhooks = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            engine: $d['engine'] ?? null,
            engineStartedAt: $d['engine_started_at'] ?? null,
            marketData: $d['market_data'] ?? null,
            webhooks: $d['webhooks'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->engine !== null) {
            $out['engine'] = $this->engine;
        }
        if ($this->engineStartedAt !== null) {
            $out['engine_started_at'] = $this->engineStartedAt;
        }
        if ($this->marketData !== null) {
            $out['market_data'] = $this->marketData;
        }
        if ($this->webhooks !== null) {
            $out['webhooks'] = $this->webhooks;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
