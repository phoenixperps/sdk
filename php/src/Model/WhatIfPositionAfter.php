<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** WhatIfPositionAfter */
final class WhatIfPositionAfter implements \JsonSerializable
{
    private const WIRE = ['size' => true, 'side' => true, 'average_price' => true];

    public function __construct(
        /** The position's size after the fill. */
        public readonly ?string $size = null,
        /** Its side; null when flat. */
        public readonly ?string $side = null,
        /** Its average price. */
        public readonly ?string $averagePrice = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            size: isset($d['size']) ? (string) $d['size'] : null,
            side: $d['side'] ?? null,
            averagePrice: isset($d['average_price']) ? (string) $d['average_price'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->size !== null) {
            $out['size'] = $this->size;
        }
        if ($this->side !== null) {
            $out['side'] = $this->side;
        }
        if ($this->averagePrice !== null) {
            $out['average_price'] = $this->averagePrice;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
