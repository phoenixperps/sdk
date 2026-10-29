<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Market */
final class Market implements \JsonSerializable
{
    private const WIRE = ['market' => true, 'name' => true, 'status' => true, 'venue' => true, 'trading_hours' => true, 'option_of' => true, 'options' => true, 'tick_size' => true, 'size_step' => true, 'min_size' => true, 'min_notional' => true, 'max_leverage' => true, 'fee_rate_pct' => true, 'exposure_cap' => true, 'max_slippage_pct' => true];

    public function __construct(
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The market's name; for a currency, the pair its price is quoted in (JPY-USD: USD/JPY, yen per dollar). */
        public readonly ?string $name = null,
        /** Whether the market trades now. */
        public readonly ?string $status = null,
        /** The venue it trades on: its prices and fills are that venue's. Null for a currency on FX hours. */
        public readonly ?string $venue = null,
        /** When it trades: around the clock, its underlying market's sessions (Standard Market Hours, closed on weekends), or FX hours. */
        public readonly ?string $tradingHours = null,
        /** For an option (the same asset on another venue, or on Standard Market Hours): its market. Null for a market. */
        public readonly ?string $optionOf = null,
        /** The ways this asset can be traded, the market first, then its options: an account holds one of them at a time (market_option_held). Null when there is only the market. */
        /** @var list<MarketOption>|null */
        public readonly ?array $options = null,
        /** Prices are multiples of this. */
        public readonly ?string $tickSize = null,
        /** Sizes are multiples of this, in the base asset. */
        public readonly ?string $sizeStep = null,
        /** The smallest order size (the size step). An order must also be worth at least min_notional. */
        public readonly ?string $minSize = null,
        /** The smallest order value in USD: an order worth less is refused order_too_small. */
        public readonly ?string $minNotional = null,
        /** The highest leverage on this market. */
        public readonly ?int $maxLeverage = null,
        /** The fee on each fill, in percent of its value. */
        public readonly ?string $feeRatePct = null,
        /** The most you may hold on this market across your accounts on the same rule, in USD. */
        public readonly ?string $exposureCap = null,
        /** How far from the price a market order may fill, in percent. */
        public readonly ?MarketMaxSlippagePct $maxSlippagePct = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            market: $d['market'] ?? null,
            name: $d['name'] ?? null,
            status: $d['status'] ?? null,
            venue: $d['venue'] ?? null,
            tradingHours: $d['trading_hours'] ?? null,
            optionOf: $d['option_of'] ?? null,
            options: isset($d['options']) && is_array($d['options']) ? array_map(fn ($x) => MarketOption::fromArray($x), $d['options']) : null,
            tickSize: isset($d['tick_size']) ? (string) $d['tick_size'] : null,
            sizeStep: isset($d['size_step']) ? (string) $d['size_step'] : null,
            minSize: isset($d['min_size']) ? (string) $d['min_size'] : null,
            minNotional: isset($d['min_notional']) ? (string) $d['min_notional'] : null,
            maxLeverage: $d['max_leverage'] ?? null,
            feeRatePct: isset($d['fee_rate_pct']) ? (string) $d['fee_rate_pct'] : null,
            exposureCap: isset($d['exposure_cap']) ? (string) $d['exposure_cap'] : null,
            maxSlippagePct: isset($d['max_slippage_pct']) && is_array($d['max_slippage_pct']) ? MarketMaxSlippagePct::fromArray($d['max_slippage_pct']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->name !== null) {
            $out['name'] = $this->name;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        if ($this->venue !== null) {
            $out['venue'] = $this->venue;
        }
        if ($this->tradingHours !== null) {
            $out['trading_hours'] = $this->tradingHours;
        }
        if ($this->optionOf !== null) {
            $out['option_of'] = $this->optionOf;
        }
        if ($this->options !== null) {
            $out['options'] = array_map(fn ($x) => $x->toArray(), $this->options);
        }
        if ($this->tickSize !== null) {
            $out['tick_size'] = $this->tickSize;
        }
        if ($this->sizeStep !== null) {
            $out['size_step'] = $this->sizeStep;
        }
        if ($this->minSize !== null) {
            $out['min_size'] = $this->minSize;
        }
        if ($this->minNotional !== null) {
            $out['min_notional'] = $this->minNotional;
        }
        if ($this->maxLeverage !== null) {
            $out['max_leverage'] = $this->maxLeverage;
        }
        if ($this->feeRatePct !== null) {
            $out['fee_rate_pct'] = $this->feeRatePct;
        }
        if ($this->exposureCap !== null) {
            $out['exposure_cap'] = $this->exposureCap;
        }
        if ($this->maxSlippagePct !== null) {
            $out['max_slippage_pct'] = $this->maxSlippagePct->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
