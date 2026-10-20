<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Exits */
final class Exits implements \JsonSerializable
{
    private const WIRE = ['account_id' => true, 'market' => true, 'strategy_id' => true, 'position' => true, 'take_profit' => true, 'stop_loss' => true, 'resize_with_position' => true];

    public function __construct(
        /** The account number. */
        public readonly ?string $accountId = null,
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The strategy that keeps these exits (type exits); null when there are none. */
        public readonly ?string $strategyId = null,
        /** The open position; null when flat. */
        public readonly ?ExitsPosition $position = null,
        /** The take-profit legs: together at most the position. */
        /** @var list<ExitsLeg>|null */
        public readonly ?array $takeProfit = null,
        /** The stop-loss legs: together at most the position. */
        /** @var list<ExitsLeg>|null */
        public readonly ?array $stopLoss = null,
        /** The legs grow with the position, keeping their share of it (rounded down to the size step). */
        public readonly ?bool $resizeWithPosition = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            accountId: $d['account_id'] ?? null,
            market: $d['market'] ?? null,
            strategyId: $d['strategy_id'] ?? null,
            position: isset($d['position']) && is_array($d['position']) ? ExitsPosition::fromArray($d['position']) : null,
            takeProfit: isset($d['take_profit']) && is_array($d['take_profit']) ? array_map(fn ($x) => ExitsLeg::fromArray($x), $d['take_profit']) : null,
            stopLoss: isset($d['stop_loss']) && is_array($d['stop_loss']) ? array_map(fn ($x) => ExitsLeg::fromArray($x), $d['stop_loss']) : null,
            resizeWithPosition: $d['resize_with_position'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->strategyId !== null) {
            $out['strategy_id'] = $this->strategyId;
        }
        if ($this->position !== null) {
            $out['position'] = $this->position->toArray();
        }
        if ($this->takeProfit !== null) {
            $out['take_profit'] = array_map(fn ($x) => $x->toArray(), $this->takeProfit);
        }
        if ($this->stopLoss !== null) {
            $out['stop_loss'] = array_map(fn ($x) => $x->toArray(), $this->stopLoss);
        }
        if ($this->resizeWithPosition !== null) {
            $out['resize_with_position'] = $this->resizeWithPosition;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
