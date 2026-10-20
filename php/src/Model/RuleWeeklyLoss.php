<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** RuleWeeklyLoss */
final class RuleWeeklyLoss implements \JsonSerializable
{
    private const WIRE = ['amount' => true, 'pct' => true];

    public function __construct(
        /** The most the account may lose in a week, in USD. */
        public readonly ?string $amount = null,
        /** The same limit in percent. */
        public readonly ?string $pct = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            amount: isset($d['amount']) ? (string) $d['amount'] : null,
            pct: isset($d['pct']) ? (string) $d['pct'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->amount !== null) {
            $out['amount'] = $this->amount;
        }
        if ($this->pct !== null) {
            $out['pct'] = $this->pct;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
