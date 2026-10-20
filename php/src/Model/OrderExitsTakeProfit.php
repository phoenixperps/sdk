<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** OrderExitsTakeProfit */
final class OrderExitsTakeProfit implements \JsonSerializable
{
    private const WIRE = ['price' => true, 'distance' => true, 'percent' => true, 'size_pct' => true];

    public function __construct(
        /** An exact price. */
        public readonly string|int|float|null $price = null,
        /** A price difference from the fill price. */
        public readonly string|int|float|null $distance = null,
        /** A percent of the fill price. */
        public readonly string|int|float|null $percent = null,
        /** Its share of the filled size, in percent (absent: the take-profits split it evenly). */
        public readonly string|int|float|null $sizePct = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            price: $d['price'] ?? null,
            distance: $d['distance'] ?? null,
            percent: $d['percent'] ?? null,
            sizePct: $d['size_pct'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->price !== null) {
            $out['price'] = Decimal::of($this->price);
        }
        if ($this->distance !== null) {
            $out['distance'] = Decimal::of($this->distance);
        }
        if ($this->percent !== null) {
            $out['percent'] = Decimal::of($this->percent);
        }
        if ($this->sizePct !== null) {
            $out['size_pct'] = Decimal::of($this->sizePct);
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
