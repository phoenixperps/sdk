<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** MarketOption */
final class MarketOption implements \JsonSerializable
{
    private const WIRE = ['market' => true, 'venue' => true, 'trading_hours' => true];

    public function __construct(
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** Its venue. */
        public readonly ?string $venue = null,
        /** When it trades. */
        public readonly ?string $tradingHours = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            market: $d['market'] ?? null,
            venue: $d['venue'] ?? null,
            tradingHours: $d['trading_hours'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->venue !== null) {
            $out['venue'] = $this->venue;
        }
        if ($this->tradingHours !== null) {
            $out['trading_hours'] = $this->tradingHours;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
