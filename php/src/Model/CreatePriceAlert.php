<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** A new price alert. Its condition (above or below) is set from the price now. */
final class CreatePriceAlert implements \JsonSerializable
{
    private const WIRE = ['market' => true, 'price' => true, 'note' => true];

    public function __construct(
        /** A market id: the asset and USD; an option adds its venue or hours after a dot. */
        public readonly ?string $market = null,
        /** The level; not the price now. */
        public readonly string|int|float|null $price = null,
        /** Your note (at most 280 characters). */
        public readonly ?string $note = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            market: $d['market'] ?? null,
            price: $d['price'] ?? null,
            note: $d['note'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->price !== null) {
            $out['price'] = Decimal::of($this->price);
        }
        if ($this->note !== null) {
            $out['note'] = $this->note;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
