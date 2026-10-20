<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** A stop-loss moved to the entry price once the price has gone your way far enough. */
final class ExitsLegBreakEven implements \JsonSerializable
{
    private const WIRE = ['trigger_distance' => true, 'trigger_percent' => true, 'trigger_price' => true, 'offset_ticks' => true, 'done' => true];

    public function __construct(
        /** How far from the entry the price must go (or null: another trigger). */
        public readonly ?string $triggerDistance = null,
        /** The same, in percent of the entry. */
        public readonly ?string $triggerPercent = null,
        /** Or once the price reaches this. */
        public readonly ?string $triggerPrice = null,
        /** Ticks beyond the entry, on the profit side. */
        public readonly ?int $offsetTicks = null,
        /** The stop-loss was moved to the entry already. */
        public readonly ?bool $done = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            triggerDistance: isset($d['trigger_distance']) ? (string) $d['trigger_distance'] : null,
            triggerPercent: isset($d['trigger_percent']) ? (string) $d['trigger_percent'] : null,
            triggerPrice: isset($d['trigger_price']) ? (string) $d['trigger_price'] : null,
            offsetTicks: $d['offset_ticks'] ?? null,
            done: $d['done'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->triggerDistance !== null) {
            $out['trigger_distance'] = $this->triggerDistance;
        }
        if ($this->triggerPercent !== null) {
            $out['trigger_percent'] = $this->triggerPercent;
        }
        if ($this->triggerPrice !== null) {
            $out['trigger_price'] = $this->triggerPrice;
        }
        if ($this->offsetTicks !== null) {
            $out['offset_ticks'] = $this->offsetTicks;
        }
        if ($this->done !== null) {
            $out['done'] = $this->done;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
