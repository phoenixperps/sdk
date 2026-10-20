<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** A new strategy. trailing_stop and break_even act on the position's exits: the answer is that exits strategy. */
final class CreateStrategy implements \JsonSerializable
{
    private const WIRE = ['type' => true, 'market' => true, 'side' => true, 'size' => true, 'distance' => true, 'percent' => true, 'activation_price' => true, 'initial_stop_price' => true, 'replace_stops' => true, 'trigger_distance' => true, 'trigger_percent' => true, 'trigger_price' => true, 'offset_ticks' => true, 'orders' => true, 'exits' => true, 'duration_minutes' => true, 'interval_seconds' => true, 'randomize' => true, 'reduce_only' => true, 'max_price' => true, 'min_price' => true, 'max_slippage_pct' => true, 'start_price' => true, 'end_price' => true, 'rungs' => true, 'distribution' => true, 'post_only' => true, 'all_or_none' => true, 'allow_marketable' => true, 'max_minutes' => true, 'on_expiry' => true, 'allow_cross' => true, 'client_strategy_id' => true];

    public function __construct(
        /** trailing_stop: a stop-loss that follows the price. break_even: the stop-losses move to the entry once the price has gone your way far enough. oco: two orders, the first fill cancels the other. twap, vwap: an order in market slices over time (vwap sized by yesterday's volume over the same hours). scaled: a ladder of limits. chase: a limit that follows the bid or ask. */
        public readonly ?string $type = null,
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The order's side. */
        public readonly ?string $side = null,
        /** trailing_stop: its size (absent: what the stop-losses leave of the position). twap, vwap, scaled, chase: the whole size. */
        public readonly string|int|float|null $size = null,
        /** trailing_stop: how far behind the best price (or percent). */
        public readonly string|int|float|null $distance = null,
        /** trailing_stop: how far behind, in percent of the best price. */
        public readonly string|int|float|null $percent = null,
        /** trailing_stop: start once the price reaches this (until then, initial_stop_price rests, or nothing). */
        public readonly string|int|float|null $activationPrice = null,
        /** trailing_stop: where the stop rests until the trail moves it (absent: from the price now). */
        public readonly string|int|float|null $initialStopPrice = null,
        /** trailing_stop: cancel the position's other stop-losses first. */
        public readonly ?bool $replaceStops = null,
        /** break_even: how far from the entry the price must go. */
        public readonly string|int|float|null $triggerDistance = null,
        /** break_even: the same, in percent of the entry. */
        public readonly string|int|float|null $triggerPercent = null,
        /** break_even: or once the price reaches this. */
        public readonly string|int|float|null $triggerPrice = null,
        /** break_even: ticks beyond the entry, on the profit side. chase: ticks behind the bid (buy) or ask (sell). */
        public readonly ?int $offsetTicks = null,
        /** oco: the two orders. */
        /** @var list<CreateStrategyOrder>|null */
        public readonly ?array $orders = null,
        /** The exits. */
        public readonly ?OrderExits $exits = null,
        /** twap, vwap: how long the schedule runs (bounds in the runner settings, see GET /v1/limits). */
        public readonly ?int $durationMinutes = null,
        /** twap, vwap: time between slices; at most half the duration. Default: 60 (within those bounds). */
        public readonly ?int $intervalSeconds = null,
        /** twap, vwap: vary each slice's time and size a little (the sizes still add up to size). */
        public readonly ?bool $randomize = null,
        /** twap, vwap, scaled, chase: only reduce your position (a twap ends when the position is closed). */
        public readonly ?bool $reduceOnly = null,
        /** twap, vwap (buy): slices wait while the price is above this. chase (buy): its cap. */
        public readonly string|int|float|null $maxPrice = null,
        /** twap, vwap (sell): slices wait while the price is below this. chase (sell): its floor. */
        public readonly string|int|float|null $minPrice = null,
        /** twap, vwap: each slice's slippage budget, in percent. */
        public readonly string|int|float|null $maxSlippagePct = null,
        /** scaled: the first rung. */
        public readonly string|int|float|null $startPrice = null,
        /** scaled: the last rung. */
        public readonly string|int|float|null $endPrice = null,
        /** scaled: how many limits (at most the runner setting). */
        public readonly ?int $rungs = null,
        /** scaled: equal sizes, growing toward end_price, or shrinking. */
        public readonly ?string $distribution = null,
        /** scaled: each rung refused if it would fill at once. */
        public readonly ?bool $postOnly = null,
        /** scaled: one rung refused cancels the others. */
        public readonly ?bool $allOrNone = null,
        /** scaled: rungs through the price may fill at once (refused otherwise, rung_would_fill). */
        public readonly ?bool $allowMarketable = null,
        /** chase: how long it follows the price. */
        public readonly ?int $maxMinutes = null,
        /** chase: at max_minutes, cancel it, or send what is left at the market. */
        public readonly ?string $onExpiry = null,
        /** chase: it may rest through the last price (it would then fill at once). */
        public readonly ?bool $allowCross = null,
        /** Your own id for the strategy (oco, twap, vwap, scaled, chase): unique in this environment. */
        public readonly ?string $clientStrategyId = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            type: $d['type'] ?? null,
            market: $d['market'] ?? null,
            side: $d['side'] ?? null,
            size: $d['size'] ?? null,
            distance: $d['distance'] ?? null,
            percent: $d['percent'] ?? null,
            activationPrice: $d['activation_price'] ?? null,
            initialStopPrice: $d['initial_stop_price'] ?? null,
            replaceStops: $d['replace_stops'] ?? null,
            triggerDistance: $d['trigger_distance'] ?? null,
            triggerPercent: $d['trigger_percent'] ?? null,
            triggerPrice: $d['trigger_price'] ?? null,
            offsetTicks: $d['offset_ticks'] ?? null,
            orders: isset($d['orders']) && is_array($d['orders']) ? array_map(fn ($x) => CreateStrategyOrder::fromArray($x), $d['orders']) : null,
            exits: isset($d['exits']) && is_array($d['exits']) ? OrderExits::fromArray($d['exits']) : null,
            durationMinutes: $d['duration_minutes'] ?? null,
            intervalSeconds: $d['interval_seconds'] ?? null,
            randomize: $d['randomize'] ?? null,
            reduceOnly: $d['reduce_only'] ?? null,
            maxPrice: $d['max_price'] ?? null,
            minPrice: $d['min_price'] ?? null,
            maxSlippagePct: $d['max_slippage_pct'] ?? null,
            startPrice: $d['start_price'] ?? null,
            endPrice: $d['end_price'] ?? null,
            rungs: $d['rungs'] ?? null,
            distribution: $d['distribution'] ?? null,
            postOnly: $d['post_only'] ?? null,
            allOrNone: $d['all_or_none'] ?? null,
            allowMarketable: $d['allow_marketable'] ?? null,
            maxMinutes: $d['max_minutes'] ?? null,
            onExpiry: $d['on_expiry'] ?? null,
            allowCross: $d['allow_cross'] ?? null,
            clientStrategyId: $d['client_strategy_id'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->side !== null) {
            $out['side'] = $this->side;
        }
        if ($this->size !== null) {
            $out['size'] = Decimal::of($this->size);
        }
        if ($this->distance !== null) {
            $out['distance'] = Decimal::of($this->distance);
        }
        if ($this->percent !== null) {
            $out['percent'] = Decimal::of($this->percent);
        }
        if ($this->activationPrice !== null) {
            $out['activation_price'] = Decimal::of($this->activationPrice);
        }
        if ($this->initialStopPrice !== null) {
            $out['initial_stop_price'] = Decimal::of($this->initialStopPrice);
        }
        if ($this->replaceStops !== null) {
            $out['replace_stops'] = $this->replaceStops;
        }
        if ($this->triggerDistance !== null) {
            $out['trigger_distance'] = Decimal::of($this->triggerDistance);
        }
        if ($this->triggerPercent !== null) {
            $out['trigger_percent'] = Decimal::of($this->triggerPercent);
        }
        if ($this->triggerPrice !== null) {
            $out['trigger_price'] = Decimal::of($this->triggerPrice);
        }
        if ($this->offsetTicks !== null) {
            $out['offset_ticks'] = $this->offsetTicks;
        }
        if ($this->orders !== null) {
            $out['orders'] = array_map(fn ($x) => $x->toArray(), $this->orders);
        }
        if ($this->exits !== null) {
            $out['exits'] = $this->exits->toArray();
        }
        if ($this->durationMinutes !== null) {
            $out['duration_minutes'] = $this->durationMinutes;
        }
        if ($this->intervalSeconds !== null) {
            $out['interval_seconds'] = $this->intervalSeconds;
        }
        if ($this->randomize !== null) {
            $out['randomize'] = $this->randomize;
        }
        if ($this->reduceOnly !== null) {
            $out['reduce_only'] = $this->reduceOnly;
        }
        if ($this->maxPrice !== null) {
            $out['max_price'] = Decimal::of($this->maxPrice);
        }
        if ($this->minPrice !== null) {
            $out['min_price'] = Decimal::of($this->minPrice);
        }
        if ($this->maxSlippagePct !== null) {
            $out['max_slippage_pct'] = Decimal::of($this->maxSlippagePct);
        }
        if ($this->startPrice !== null) {
            $out['start_price'] = Decimal::of($this->startPrice);
        }
        if ($this->endPrice !== null) {
            $out['end_price'] = Decimal::of($this->endPrice);
        }
        if ($this->rungs !== null) {
            $out['rungs'] = $this->rungs;
        }
        if ($this->distribution !== null) {
            $out['distribution'] = $this->distribution;
        }
        if ($this->postOnly !== null) {
            $out['post_only'] = $this->postOnly;
        }
        if ($this->allOrNone !== null) {
            $out['all_or_none'] = $this->allOrNone;
        }
        if ($this->allowMarketable !== null) {
            $out['allow_marketable'] = $this->allowMarketable;
        }
        if ($this->maxMinutes !== null) {
            $out['max_minutes'] = $this->maxMinutes;
        }
        if ($this->onExpiry !== null) {
            $out['on_expiry'] = $this->onExpiry;
        }
        if ($this->allowCross !== null) {
            $out['allow_cross'] = $this->allowCross;
        }
        if ($this->clientStrategyId !== null) {
            $out['client_strategy_id'] = $this->clientStrategyId;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
