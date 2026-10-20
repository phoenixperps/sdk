<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** What to change: at least one field. The order keeps its place in the engine (changed in place, never canceled and placed again). */
final class ModifyOrder implements \JsonSerializable
{
    private const WIRE = ['size' => true, 'limit_price' => true, 'stop_price' => true];

    public function __construct(
        /** The new size. */
        public readonly string|int|float|null $size = null,
        /** The new limit price. */
        public readonly string|int|float|null $limitPrice = null,
        /** The new trigger price. */
        public readonly string|int|float|null $stopPrice = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            size: $d['size'] ?? null,
            limitPrice: $d['limit_price'] ?? null,
            stopPrice: $d['stop_price'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->size !== null) {
            $out['size'] = Decimal::of($this->size);
        }
        if ($this->limitPrice !== null) {
            $out['limit_price'] = Decimal::of($this->limitPrice);
        }
        if ($this->stopPrice !== null) {
            $out['stop_price'] = Decimal::of($this->stopPrice);
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
