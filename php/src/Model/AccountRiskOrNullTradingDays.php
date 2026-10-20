<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** AccountRiskOrNullTradingDays */
final class AccountRiskOrNullTradingDays implements \JsonSerializable
{
    private const WIRE = ['count' => true, 'required' => true];

    public function __construct(
        /** Days with a trade so far. */
        public readonly ?int $count = null,
        /** Days the rule needs. */
        public readonly ?int $required = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            count: $d['count'] ?? null,
            required: $d['required'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->count !== null) {
            $out['count'] = $this->count;
        }
        if ($this->required !== null) {
            $out['required'] = $this->required;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
