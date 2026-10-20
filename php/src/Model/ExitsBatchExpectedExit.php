<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ExitsBatchExpectedExit */
final class ExitsBatchExpectedExit implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'size' => true, 'price' => true];

    public function __construct(
        /** A leg. */
        public readonly ?string $id = null,
        /** Its size. */
        public readonly string|int|float|null $size = null,
        /** Its price; null for a stop-loss that waits (as GET exits shows it). */
        public readonly string|int|float|null $price = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            size: $d['size'] ?? null,
            price: $d['price'] ?? null,
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
        if ($this->size !== null) {
            $out['size'] = Decimal::of($this->size);
        }
        if ($this->price !== null) {
            $out['price'] = Decimal::of($this->price);
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
