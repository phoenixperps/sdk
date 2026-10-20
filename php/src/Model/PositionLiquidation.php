<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** PositionLiquidation */
final class PositionLiquidation implements \JsonSerializable
{
    private const WIRE = ['price' => true, 'distance_pct' => true, 'account_wide' => true];

    public function __construct(
        /** The price at which it is liquidated; null when there is none or it depends on several positions. */
        public readonly ?string $price = null,
        /** How far the market price is from it, in percent. */
        public readonly ?string $distancePct = null,
        /** Cross margin on several markets: no single price, every one of them moves it. */
        public readonly ?bool $accountWide = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            price: isset($d['price']) ? (string) $d['price'] : null,
            distancePct: isset($d['distance_pct']) ? (string) $d['distance_pct'] : null,
            accountWide: $d['account_wide'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->price !== null) {
            $out['price'] = $this->price;
        }
        if ($this->distancePct !== null) {
            $out['distance_pct'] = $this->distancePct;
        }
        if ($this->accountWide !== null) {
            $out['account_wide'] = $this->accountWide;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
