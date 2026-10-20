<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The open position; null when flat. */
final class ExitsPosition implements \JsonSerializable
{
    private const WIRE = ['side' => true, 'size' => true];

    public function __construct(
        /** Long or short. */
        public readonly ?string $side = null,
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
            side: $d['side'] ?? null,
            size: isset($d['size']) ? (string) $d['size'] : null,
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
