<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Fill */
final class Fill implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'order_id' => true, 'market' => true, 'side' => true, 'size' => true, 'price' => true, 'fee' => true, 'liquidation' => true, 'time' => true];

    public function __construct(
        /** The fill. */
        public readonly ?string $id = null,
        /** Its order. */
        public readonly ?string $orderId = null,
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The side; null for an order placed elsewhere before the API saw it. */
        public readonly ?string $side = null,
        /** The size filled. */
        public readonly ?string $size = null,
        /** The fill price. */
        public readonly ?string $price = null,
        /** Its fee. */
        public readonly ?string $fee = null,
        /** A liquidation filled it. */
        public readonly ?bool $liquidation = null,
        /** When it filled. */
        public readonly ?int $time = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            orderId: $d['order_id'] ?? null,
            market: $d['market'] ?? null,
            side: $d['side'] ?? null,
            size: isset($d['size']) ? (string) $d['size'] : null,
            price: isset($d['price']) ? (string) $d['price'] : null,
            fee: isset($d['fee']) ? (string) $d['fee'] : null,
            liquidation: $d['liquidation'] ?? null,
            time: $d['time'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->id !== null) {
            $out['id'] = $this->id;
        }
        if ($this->orderId !== null) {
            $out['order_id'] = $this->orderId;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->side !== null) {
            $out['side'] = $this->side;
        }
        if ($this->size !== null) {
            $out['size'] = $this->size;
        }
        if ($this->price !== null) {
            $out['price'] = $this->price;
        }
        if ($this->fee !== null) {
            $out['fee'] = $this->fee;
        }
        if ($this->liquidation !== null) {
            $out['liquidation'] = $this->liquidation;
        }
        if ($this->time !== null) {
            $out['time'] = $this->time;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
