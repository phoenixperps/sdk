<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** CreateStrategyOrder */
final class CreateStrategyOrder implements \JsonSerializable
{
    private const WIRE = ['side' => true, 'type' => true, 'size' => true, 'limit_price' => true, 'stop_price' => true, 'post_only' => true, 'reduce_only' => true];

    public function __construct(
        /** The order's side. */
        public readonly ?string $side = null,
        /** Two orders that rest. */
        public readonly ?string $type = null,
        /** Its size. */
        public readonly string|int|float|null $size = null,
        /** Its limit price. */
        public readonly string|int|float|null $limitPrice = null,
        /** Its stop price. */
        public readonly string|int|float|null $stopPrice = null,
        /** Refused if it would fill at once. */
        public readonly ?bool $postOnly = null,
        /** It may only reduce your position. */
        public readonly ?bool $reduceOnly = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            side: $d['side'] ?? null,
            type: $d['type'] ?? null,
            size: $d['size'] ?? null,
            limitPrice: $d['limit_price'] ?? null,
            stopPrice: $d['stop_price'] ?? null,
            postOnly: $d['post_only'] ?? null,
            reduceOnly: $d['reduce_only'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->side !== null) {
            $out['side'] = $this->side;
        }
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->size !== null) {
            $out['size'] = Decimal::of($this->size);
        }
        if ($this->limitPrice !== null) {
            $out['limit_price'] = Decimal::of($this->limitPrice);
        }
        if ($this->stopPrice !== null) {
            $out['stop_price'] = Decimal::of($this->stopPrice);
        }
        if ($this->postOnly !== null) {
            $out['post_only'] = $this->postOnly;
        }
        if ($this->reduceOnly !== null) {
            $out['reduce_only'] = $this->reduceOnly;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
