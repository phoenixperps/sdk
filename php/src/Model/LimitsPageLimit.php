<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** LimitsPageLimit */
final class LimitsPageLimit implements \JsonSerializable
{
    private const WIRE = ['default' => true, 'max' => true];

    public function __construct(
        /** Items a page holds by default. */
        public readonly ?int $default = null,
        /** The most a page may hold. */
        public readonly ?int $max = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            default: $d['default'] ?? null,
            max: $d['max'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->default !== null) {
            $out['default'] = $this->default;
        }
        if ($this->max !== null) {
            $out['max'] = $this->max;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
