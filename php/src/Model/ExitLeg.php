<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ExitLeg */
final class ExitLeg implements \JsonSerializable
{
    private const WIRE = ['order_id' => true, 'type' => true, 'price' => true, 'size' => true];

    public function __construct(
        /** The working order. */
        public readonly ?string $orderId = null,
        /** The order's type. */
        public readonly ?string $type = null,
        /** Its trigger or limit price. */
        public readonly ?string $price = null,
        /** Its size. */
        public readonly ?string $size = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            orderId: $d['order_id'] ?? null,
            type: $d['type'] ?? null,
            price: isset($d['price']) ? (string) $d['price'] : null,
            size: isset($d['size']) ? (string) $d['size'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->orderId !== null) {
            $out['order_id'] = $this->orderId;
        }
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->price !== null) {
            $out['price'] = $this->price;
        }
        if ($this->size !== null) {
            $out['size'] = $this->size;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
