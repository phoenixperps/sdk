<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Exits placed when the order fills: the stop-loss for the filled size first, then the take-profits. From then on they are the position's exits. An empty object removes them. */
final class OrderExits implements \JsonSerializable
{
    private const WIRE = ['take_profit' => true, 'stop_loss' => true];

    public function __construct(
        /** Limit orders on the profit side, each at price, or at distance or percent from the fill. */
        /** @var list<OrderExitsTakeProfit>|null */
        public readonly ?array $takeProfit = null,
        /** A stop on the loss side, at price, or at distance or percent from the fill; it may trail and move to break-even (as in a position's exits). */
        public readonly ?OrderExitsStopLoss $stopLoss = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            takeProfit: isset($d['take_profit']) && is_array($d['take_profit']) ? array_map(fn ($x) => OrderExitsTakeProfit::fromArray($x), $d['take_profit']) : null,
            stopLoss: isset($d['stop_loss']) && is_array($d['stop_loss']) ? OrderExitsStopLoss::fromArray($d['stop_loss']) : null,
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
            $out['stop_loss'] = $this->stopLoss->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
