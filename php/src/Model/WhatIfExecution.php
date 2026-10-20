<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** WhatIfExecution */
final class WhatIfExecution implements \JsonSerializable
{
    private const WIRE = ['marketable' => true, 'expected_price' => true, 'worst_price' => true];

    public function __construct(
        /** It would fill at once. */
        public readonly ?bool $marketable = null,
        /** The price it would fill at (at once) or rest at. */
        public readonly ?string $expectedPrice = null,
        /** A market order's worst price. */
        public readonly ?string $worstPrice = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            marketable: $d['marketable'] ?? null,
            expectedPrice: isset($d['expected_price']) ? (string) $d['expected_price'] : null,
            worstPrice: isset($d['worst_price']) ? (string) $d['worst_price'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->marketable !== null) {
            $out['marketable'] = $this->marketable;
        }
        if ($this->expectedPrice !== null) {
            $out['expected_price'] = $this->expectedPrice;
        }
        if ($this->worstPrice !== null) {
            $out['worst_price'] = $this->worstPrice;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
