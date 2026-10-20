<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** AccountRiskNearestLiquidation */
final class AccountRiskNearestLiquidation implements \JsonSerializable
{
    private const WIRE = ['market' => true, 'price' => true, 'distance_pct' => true];

    public function __construct(
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** Its liquidation price. */
        public readonly ?string $price = null,
        /** How far the price is from it, in percent. */
        public readonly ?string $distancePct = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            market: $d['market'] ?? null,
            price: isset($d['price']) ? (string) $d['price'] : null,
            distancePct: isset($d['distance_pct']) ? (string) $d['distance_pct'] : null,
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
        if ($this->price !== null) {
            $out['price'] = $this->price;
        }
        if ($this->distancePct !== null) {
            $out['distance_pct'] = $this->distancePct;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
