<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Status */
final class Status implements \JsonSerializable
{
    private const WIRE = ['environment' => true, 'api' => true, 'engine' => true, 'account_data' => true, 'market_data' => true, 'features' => true, 'server_time' => true];

    public function __construct(
        /** The environment of the host you called. */
        public readonly ?string $environment = null,
        /** Whether this environment takes requests. */
        public readonly ?string $api = null,
        /** The engine. */
        public readonly ?StatusEngine $engine = null,
        /** The account data. */
        public readonly ?StatusAccountData $accountData = null,
        /** The market data. */
        public readonly ?StatusMarketData $marketData = null,
        /** The features. */
        public readonly ?StatusFeatures $features = null,
        /** The time on the server. */
        public readonly ?int $serverTime = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            environment: $d['environment'] ?? null,
            api: $d['api'] ?? null,
            engine: isset($d['engine']) && is_array($d['engine']) ? StatusEngine::fromArray($d['engine']) : null,
            accountData: isset($d['account_data']) && is_array($d['account_data']) ? StatusAccountData::fromArray($d['account_data']) : null,
            marketData: isset($d['market_data']) && is_array($d['market_data']) ? StatusMarketData::fromArray($d['market_data']) : null,
            features: isset($d['features']) && is_array($d['features']) ? StatusFeatures::fromArray($d['features']) : null,
            serverTime: $d['server_time'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->environment !== null) {
            $out['environment'] = $this->environment;
        }
        if ($this->api !== null) {
            $out['api'] = $this->api;
        }
        if ($this->engine !== null) {
            $out['engine'] = $this->engine->toArray();
        }
        if ($this->accountData !== null) {
            $out['account_data'] = $this->accountData->toArray();
        }
        if ($this->marketData !== null) {
            $out['market_data'] = $this->marketData->toArray();
        }
        if ($this->features !== null) {
            $out['features'] = $this->features->toArray();
        }
        if ($this->serverTime !== null) {
            $out['server_time'] = $this->serverTime;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
