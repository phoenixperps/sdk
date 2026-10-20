<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ExitsLeg */
final class ExitsLeg implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'group' => true, 'type' => true, 'status' => true, 'size' => true, 'price' => true, 'limit_price' => true, 'trail' => true, 'break_even' => true];

    public function __construct(
        /** The leg: an order of this API, also listed under the account's orders. */
        public readonly ?string $id = null,
        /** take_profit: a limit on the profit side. stop_loss: a stop on the loss side. */
        public readonly ?string $group = null,
        /** take_profit legs are limits; stop_loss legs are stop_market or stop_limit. */
        public readonly ?string $type = null,
        /** The order's status; waiting: a stop-loss not placed yet (it waits for its activation price or its break-even). */
        public readonly ?string $status = null,
        /** Its size. */
        public readonly ?string $size = null,
        /** The take-profit price, or the stop price. */
        public readonly ?string $price = null,
        /** The limit of a stop_limit leg. */
        public readonly ?string $limitPrice = null,
        /** A trailing stop-loss: how it follows the price. */
        public readonly ?ExitsLegTrail $trail = null,
        /** A stop-loss moved to the entry price once the price has gone your way far enough. */
        public readonly ?ExitsLegBreakEven $breakEven = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            group: $d['group'] ?? null,
            type: $d['type'] ?? null,
            status: $d['status'] ?? null,
            size: isset($d['size']) ? (string) $d['size'] : null,
            price: isset($d['price']) ? (string) $d['price'] : null,
            limitPrice: isset($d['limit_price']) ? (string) $d['limit_price'] : null,
            trail: isset($d['trail']) && is_array($d['trail']) ? ExitsLegTrail::fromArray($d['trail']) : null,
            breakEven: isset($d['break_even']) && is_array($d['break_even']) ? ExitsLegBreakEven::fromArray($d['break_even']) : null,
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
        if ($this->group !== null) {
            $out['group'] = $this->group;
        }
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        if ($this->size !== null) {
            $out['size'] = $this->size;
        }
        if ($this->price !== null) {
            $out['price'] = $this->price;
        }
        if ($this->limitPrice !== null) {
            $out['limit_price'] = $this->limitPrice;
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
