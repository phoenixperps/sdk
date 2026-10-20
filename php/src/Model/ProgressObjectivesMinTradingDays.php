<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ProgressObjectivesMinTradingDays */
final class ProgressObjectivesMinTradingDays implements \JsonSerializable
{
    private const WIRE = ['required' => true, 'count' => true, 'status' => true];

    public function __construct(
        /** Days the rule needs. */
        public readonly ?int $required = null,
        /** Days with a trade so far. */
        public readonly ?int $count = null,
        /** Whether it is reached. */
        public readonly ?string $status = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            required: $d['required'] ?? null,
            count: $d['count'] ?? null,
            status: $d['status'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->required !== null) {
            $out['required'] = $this->required;
        }
        if ($this->count !== null) {
            $out['count'] = $this->count;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
