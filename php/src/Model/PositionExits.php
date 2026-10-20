<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** PositionExits */
final class PositionExits implements \JsonSerializable
{
    private const WIRE = ['take_profit' => true, 'stop_loss' => true];

    public function __construct(
        /** Limit orders on the closing side. */
        /** @var list<ExitLeg>|null */
        public readonly ?array $takeProfit = null,
        /** Stop orders on the closing side. */
        /** @var list<ExitLeg>|null */
        public readonly ?array $stopLoss = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            takeProfit: isset($d['take_profit']) && is_array($d['take_profit']) ? array_map(fn ($x) => ExitLeg::fromArray($x), $d['take_profit']) : null,
            stopLoss: isset($d['stop_loss']) && is_array($d['stop_loss']) ? array_map(fn ($x) => ExitLeg::fromArray($x), $d['stop_loss']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->takeProfit !== null) {
            $out['take_profit'] = array_map(fn ($x) => $x->toArray(), $this->takeProfit);
        }
        if ($this->stopLoss !== null) {
            $out['stop_loss'] = array_map(fn ($x) => $x->toArray(), $this->stopLoss);
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
