<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** A stop on the loss side, at price, or at distance or percent from the fill; it may trail and move to break-even (as in a position's exits). */
final class OrderExitsStopLoss implements \JsonSerializable
{
    private const WIRE = ['price' => true, 'distance' => true, 'percent' => true, 'type' => true, 'limit_offset_ticks' => true, 'trail' => true, 'break_even' => true];

    public function __construct(
        /** An exact price. */
        public readonly string|int|float|null $price = null,
        /** A price difference from the fill price. */
        public readonly string|int|float|null $distance = null,
        /** A percent of the fill price. */
        public readonly string|int|float|null $percent = null,
        /** The stop's type (default stop_market). */
        public readonly ?string $type = null,
        /** stop_limit: how many ticks past the stop its limit sits. */
        public readonly ?int $limitOffsetTicks = null,
        /** As a position's exits take it: { distance } or { percent }, with activation_price to start only once the price reaches it. */
        public readonly ?OrderExitsStopLossTrail $trail = null,
        /** As a position's exits take it: one of trigger_distance, trigger_percent, trigger_price, and offset_ticks. */
        public readonly ?OrderExitsStopLossBreakEven $breakEven = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            price: $d['price'] ?? null,
            distance: $d['distance'] ?? null,
            percent: $d['percent'] ?? null,
            type: $d['type'] ?? null,
            limitOffsetTicks: $d['limit_offset_ticks'] ?? null,
            trail: isset($d['trail']) && is_array($d['trail']) ? OrderExitsStopLossTrail::fromArray($d['trail']) : null,
            breakEven: isset($d['break_even']) && is_array($d['break_even']) ? OrderExitsStopLossBreakEven::fromArray($d['break_even']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->price !== null) {
            $out['price'] = Decimal::of($this->price);
        }
        if ($this->distance !== null) {
            $out['distance'] = Decimal::of($this->distance);
        }
        if ($this->percent !== null) {
            $out['percent'] = Decimal::of($this->percent);
        }
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->limitOffsetTicks !== null) {
            $out['limit_offset_ticks'] = $this->limitOffsetTicks;
        }
        if ($this->trail !== null) {
            $out['trail'] = $this->trail->toArray();
        }
        if ($this->breakEven !== null) {
            $out['break_even'] = $this->breakEven->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
