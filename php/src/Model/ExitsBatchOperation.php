<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ExitsBatchOperation */
final class ExitsBatchOperation implements \JsonSerializable
{
    private const WIRE = ['op' => true, 'id' => true, 'group' => true, 'type' => true, 'price' => true, 'limit_price' => true, 'size' => true, 'trail' => true, 'break_even' => true, 'clear' => true];

    public function __construct(
        /** What to do. */
        public readonly ?string $op = null,
        /** modify, cancel: the leg. */
        public readonly ?string $id = null,
        /** place: the group of the new leg. */
        public readonly ?string $group = null,
        /** place: limit for a take-profit (the default); stop_market (the default) or stop_limit for a stop-loss. */
        public readonly ?string $type = null,
        /** The take-profit price, or the stop price. */
        public readonly string|int|float|null $price = null,
        /** The limit of a stop_limit leg. */
        public readonly string|int|float|null $limitPrice = null,
        /** The size; absent on a place: what the group leaves of the position. */
        public readonly string|int|float|null $size = null,
        /** A stop-loss that follows the price: { distance } (a price difference) or { percent }, with activation_price to start only once the price reaches it. Without a price and an activation price it starts from the price now. null removes it. */
        public readonly ?ExitsBatchOperationTrail $trail = null,
        /** Move the stop-loss to the entry price (plus offset_ticks) once the price has gone your way by trigger_distance, trigger_percent or to trigger_price. Without a price, the stop is placed only then. null removes it. */
        public readonly ?ExitsBatchOperationBreakEven $breakEven = null,
        /** modify: the settings to remove from a stop-loss (the same as sending them null). */
        /** @var list<string>|null */
        public readonly ?array $clear = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            op: $d['op'] ?? null,
            id: $d['id'] ?? null,
            group: $d['group'] ?? null,
            type: $d['type'] ?? null,
            price: $d['price'] ?? null,
            limitPrice: $d['limit_price'] ?? null,
            size: $d['size'] ?? null,
            trail: isset($d['trail']) && is_array($d['trail']) ? ExitsBatchOperationTrail::fromArray($d['trail']) : null,
            breakEven: isset($d['break_even']) && is_array($d['break_even']) ? ExitsBatchOperationBreakEven::fromArray($d['break_even']) : null,
            clear: $d['clear'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->op !== null) {
            $out['op'] = $this->op;
        }
        if ($this->id !== null) {
            $out['id'] = $this->id;
        }
        if ($this->group !== null) {
            $out['group'] = $this->group;
        }
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->price !== null) {
            $out['price'] = Decimal::of($this->price);
        }
        if ($this->limitPrice !== null) {
            $out['limit_price'] = Decimal::of($this->limitPrice);
        }
        if ($this->size !== null) {
            $out['size'] = Decimal::of($this->size);
        }
        if ($this->trail !== null) {
            $out['trail'] = $this->trail->toArray();
        }
        if ($this->breakEven !== null) {
            $out['break_even'] = $this->breakEven->toArray();
        }
        if ($this->clear !== null) {
            $out['clear'] = $this->clear;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
