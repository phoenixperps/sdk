<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** TradingPolicyBudgets */
final class TradingPolicyBudgets implements \JsonSerializable
{
    private const WIRE = ['read_per_min' => true, 'trade_per_min' => true];

    public function __construct(
        /** Reads a minute for this key. */
        public readonly ?int $readPerMin = null,
        /** Trading calls a minute for this key. */
        public readonly ?int $tradePerMin = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            readPerMin: $d['read_per_min'] ?? null,
            tradePerMin: $d['trade_per_min'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->readPerMin !== null) {
            $out['read_per_min'] = $this->readPerMin;
        }
        if ($this->tradePerMin !== null) {
            $out['trade_per_min'] = $this->tradePerMin;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
