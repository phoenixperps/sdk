<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** LimitsRateBulk */
final class LimitsRateBulk implements \JsonSerializable
{
    private const WIRE = ['per_min' => true, 'burst' => true];

    public function __construct(
        /** Requests a minute. */
        public readonly ?int $perMin = null,
        /** Requests at once. */
        public readonly ?int $burst = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            perMin: $d['per_min'] ?? null,
            burst: $d['burst'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->perMin !== null) {
            $out['per_min'] = $this->perMin;
        }
        if ($this->burst !== null) {
            $out['burst'] = $this->burst;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
