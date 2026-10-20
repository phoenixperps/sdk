<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Position */
final class Position implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'market' => true, 'side' => true, 'size' => true, 'entry_price' => true, 'mark_price' => true, 'notional' => true, 'unrealized_pnl' => true, 'leverage' => true, 'margin_mode' => true, 'initial_margin' => true, 'added_margin' => true, 'margin' => true, 'liquidation' => true, 'breach_prices' => true, 'exits' => true, 'protected' => true, 'opened_at' => true];

    public function __construct(
        /** This position, while it stays open on the same side. */
        public readonly ?string $id = null,
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** Long or short. */
        public readonly ?string $side = null,
        /** The size, in the base asset. */
        public readonly ?string $size = null,
        /** The average entry price. */
        public readonly ?string $entryPrice = null,
        /** The market price it is valued at. */
        public readonly ?string $markPrice = null,
        /** Its value at that price. */
        public readonly ?string $notional = null,
        /** Its open P&L. */
        public readonly ?string $unrealizedPnl = null,
        /** Its leverage. */
        public readonly ?int $leverage = null,
        /** Its margin mode. */
        public readonly ?string $marginMode = null,
        /** The margin it took at entry. */
        public readonly ?string $initialMargin = null,
        /** Margin you added to it. */
        public readonly ?string $addedMargin = null,
        /** What it holds of the balance. */
        public readonly ?string $margin = null,
        /** The liquidation. */
        public readonly ?PositionLiquidation $liquidation = null,
        /** The breach prices. */
        public readonly ?PositionBreachPrices $breachPrices = null,
        /** The exits. */
        public readonly ?PositionExits $exits = null,
        /** At least one stop loss rests on it. */
        public readonly ?bool $protected = null,
        /** When it opened. */
        public readonly ?int $openedAt = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            market: $d['market'] ?? null,
            side: $d['side'] ?? null,
            size: isset($d['size']) ? (string) $d['size'] : null,
            entryPrice: isset($d['entry_price']) ? (string) $d['entry_price'] : null,
            markPrice: isset($d['mark_price']) ? (string) $d['mark_price'] : null,
            notional: isset($d['notional']) ? (string) $d['notional'] : null,
            unrealizedPnl: isset($d['unrealized_pnl']) ? (string) $d['unrealized_pnl'] : null,
            leverage: $d['leverage'] ?? null,
            marginMode: $d['margin_mode'] ?? null,
            initialMargin: isset($d['initial_margin']) ? (string) $d['initial_margin'] : null,
            addedMargin: isset($d['added_margin']) ? (string) $d['added_margin'] : null,
            margin: isset($d['margin']) ? (string) $d['margin'] : null,
            liquidation: isset($d['liquidation']) && is_array($d['liquidation']) ? PositionLiquidation::fromArray($d['liquidation']) : null,
            breachPrices: isset($d['breach_prices']) && is_array($d['breach_prices']) ? PositionBreachPrices::fromArray($d['breach_prices']) : null,
            exits: isset($d['exits']) && is_array($d['exits']) ? PositionExits::fromArray($d['exits']) : null,
            protected: $d['protected'] ?? null,
            openedAt: $d['opened_at'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->id !== null) {
            $out['id'] = $this->id;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->side !== null) {
            $out['side'] = $this->side;
        }
        if ($this->size !== null) {
            $out['size'] = $this->size;
        }
        if ($this->entryPrice !== null) {
            $out['entry_price'] = $this->entryPrice;
        }
        if ($this->markPrice !== null) {
            $out['mark_price'] = $this->markPrice;
        }
        if ($this->notional !== null) {
            $out['notional'] = $this->notional;
        }
        if ($this->unrealizedPnl !== null) {
            $out['unrealized_pnl'] = $this->unrealizedPnl;
        }
        if ($this->leverage !== null) {
            $out['leverage'] = $this->leverage;
        }
        if ($this->marginMode !== null) {
            $out['margin_mode'] = $this->marginMode;
        }
        if ($this->initialMargin !== null) {
            $out['initial_margin'] = $this->initialMargin;
        }
        if ($this->addedMargin !== null) {
            $out['added_margin'] = $this->addedMargin;
        }
        if ($this->margin !== null) {
            $out['margin'] = $this->margin;
        }
        if ($this->liquidation !== null) {
            $out['liquidation'] = $this->liquidation->toArray();
        }
        if ($this->breachPrices !== null) {
            $out['breach_prices'] = $this->breachPrices->toArray();
        }
        if ($this->exits !== null) {
            $out['exits'] = $this->exits->toArray();
        }
        if ($this->protected !== null) {
            $out['protected'] = $this->protected;
        }
        if ($this->openedAt !== null) {
            $out['opened_at'] = $this->openedAt;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
