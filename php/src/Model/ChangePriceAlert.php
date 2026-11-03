<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** What to change on an armed alert: at least one field. */
final class ChangePriceAlert implements \JsonSerializable
{
    private const WIRE = ['price' => true, 'note' => true];

    public function __construct(
        /** A new level; its condition is set again from the price now. */
        public readonly string|int|float|null $price = null,
        /** A new note; null removes it. */
        public readonly ?string $note = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            price: $d['price'] ?? null,
            note: $d['note'] ?? null,
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
