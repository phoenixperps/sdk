<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ProgressPayout */
final class ProgressPayout implements \JsonSerializable
{
    private const WIRE = ['eligible' => true, 'available' => true, 'min_payout' => true, 'paid_out' => true];

    public function __construct(
        /** A payout may be requested now. */
        public readonly ?bool $eligible = null,
        /** What may be paid out. */
        public readonly ?string $available = null,
        /** The smallest payout. */
        public readonly ?string $minPayout = null,
        /** Paid out so far. */
        public readonly ?string $paidOut = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            eligible: $d['eligible'] ?? null,
            available: isset($d['available']) ? (string) $d['available'] : null,
            minPayout: isset($d['min_payout']) ? (string) $d['min_payout'] : null,
            paidOut: isset($d['paid_out']) ? (string) $d['paid_out'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->eligible !== null) {
            $out['eligible'] = $this->eligible;
        }
        if ($this->available !== null) {
            $out['available'] = $this->available;
        }
        if ($this->minPayout !== null) {
            $out['min_payout'] = $this->minPayout;
        }
        if ($this->paidOut !== null) {
            $out['paid_out'] = $this->paidOut;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
