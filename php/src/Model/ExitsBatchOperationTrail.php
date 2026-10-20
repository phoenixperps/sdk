<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** A stop-loss that follows the price: { distance } (a price difference) or { percent }, with activation_price to start only once the price reaches it. Without a price and an activation price it starts from the price now. null removes it. */
final class ExitsBatchOperationTrail implements \JsonSerializable
{
    private const WIRE = ['distance' => true, 'percent' => true, 'activation_price' => true];

    public function __construct(
        /** How far behind the best price. */
        public readonly string|int|float|null $distance = null,
        /** How far behind, in percent of the best price. */
        public readonly string|int|float|null $percent = null,
        /** Start once the price reaches this. */
        public readonly string|int|float|null $activationPrice = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            distance: $d['distance'] ?? null,
            percent: $d['percent'] ?? null,
            activationPrice: $d['activation_price'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->distance !== null) {
            $out['distance'] = Decimal::of($this->distance);
        }
        if ($this->percent !== null) {
            $out['percent'] = Decimal::of($this->percent);
        }
        if ($this->activationPrice !== null) {
            $out['activation_price'] = Decimal::of($this->activationPrice);
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
