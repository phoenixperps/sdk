<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** As a position's exits take it: one of trigger_distance, trigger_percent, trigger_price, and offset_ticks. */
final class OrderExitsStopLossBreakEven implements \JsonSerializable
{
    private const WIRE = ['trigger_distance' => true, 'trigger_percent' => true, 'trigger_price' => true, 'offset_ticks' => true];

    public function __construct(
        /** How far from the entry. */
        public readonly string|int|float|null $triggerDistance = null,
        /** How far from the entry, in percent. */
        public readonly string|int|float|null $triggerPercent = null,
        /** Or this price. */
        public readonly string|int|float|null $triggerPrice = null,
        /** Ticks beyond the entry, on the profit side. */
        public readonly ?int $offsetTicks = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            triggerDistance: $d['trigger_distance'] ?? null,
            triggerPercent: $d['trigger_percent'] ?? null,
            triggerPrice: $d['trigger_price'] ?? null,
            offsetTicks: $d['offset_ticks'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
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
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
