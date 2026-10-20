<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** A new order. */
final class CreateOrder implements \JsonSerializable
{
    private const WIRE = ['market' => true, 'side' => true, 'type' => true, 'size' => true, 'notional_usd' => true, 'limit_price' => true, 'stop_price' => true, 'worst_price' => true, 'max_slippage_pct' => true, 'reduce_only' => true, 'post_only' => true, 'time_in_force' => true, 'expires_at' => true, 'client_order_id' => true, 'exits' => true];

    public function __construct(
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The order's side. */
        public readonly ?string $side = null,
        /** market: now, at the best price within your worst price. limit: at your price or better. stop_market: a market order once the last price reaches the stop. stop_limit: a limit once the last price reaches the stop. */
        public readonly ?string $type = null,
        /** The size, in the base asset: a multiple of the market's size step. Give size or notional_usd. */
        public readonly string|int|float|null $size = null,
        /** The size as a value in USD, rounded down to the size step. */
        public readonly string|int|float|null $notionalUsd = null,
        /** The limit price (limit and stop_limit): a multiple of the tick size. */
        public readonly string|int|float|null $limitPrice = null,
        /** The trigger price (stop_market and stop_limit). */
        public readonly string|int|float|null $stopPrice = null,
        /** Market orders: the worst price you accept. Give worst_price or max_slippage_pct. */
        public readonly string|int|float|null $worstPrice = null,
        /** Market orders: how far from the best price you accept, in percent (the default and bounds are in GET /v1/markets). */
        public readonly string|int|float|null $maxSlippagePct = null,
        /** It may only reduce or close your position, never open or add to one. */
        public readonly ?bool $reduceOnly = null,
        /** Limit orders: refused if it would fill at once. */
        public readonly ?bool $postOnly = null,
        /** gtc: until canceled. gtd: until expires_at. ioc and fok: fill now or end (a fill is always the whole order). Default: ioc for market orders, gtc otherwise. */
        public readonly ?string $timeInForce = null,
        /** With gtd: when it ends, from orders.gtd_min_seconds to orders.gtd_max_days ahead (GET /v1/limits). */
        public readonly ?int $expiresAt = null,
        /** Your own id for the order: unique in this environment, 1 to 64 letters, digits and . _ : - */
        public readonly ?string $clientOrderId = null,
        /** The exits. */
        public readonly ?OrderExits $exits = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            market: $d['market'] ?? null,
            side: $d['side'] ?? null,
            type: $d['type'] ?? null,
            size: $d['size'] ?? null,
            notionalUsd: $d['notional_usd'] ?? null,
            limitPrice: $d['limit_price'] ?? null,
            stopPrice: $d['stop_price'] ?? null,
            worstPrice: $d['worst_price'] ?? null,
            maxSlippagePct: $d['max_slippage_pct'] ?? null,
            reduceOnly: $d['reduce_only'] ?? null,
            postOnly: $d['post_only'] ?? null,
            timeInForce: $d['time_in_force'] ?? null,
            expiresAt: $d['expires_at'] ?? null,
            clientOrderId: $d['client_order_id'] ?? null,
            exits: isset($d['exits']) && is_array($d['exits']) ? OrderExits::fromArray($d['exits']) : null,
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
        if ($this->side !== null) {
            $out['side'] = $this->side;
        }
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->size !== null) {
            $out['size'] = Decimal::of($this->size);
        }
        if ($this->notionalUsd !== null) {
            $out['notional_usd'] = Decimal::of($this->notionalUsd);
        }
        if ($this->limitPrice !== null) {
            $out['limit_price'] = Decimal::of($this->limitPrice);
        }
        if ($this->stopPrice !== null) {
            $out['stop_price'] = Decimal::of($this->stopPrice);
        }
        if ($this->worstPrice !== null) {
            $out['worst_price'] = Decimal::of($this->worstPrice);
        }
        if ($this->maxSlippagePct !== null) {
            $out['max_slippage_pct'] = Decimal::of($this->maxSlippagePct);
        }
        if ($this->reduceOnly !== null) {
            $out['reduce_only'] = $this->reduceOnly;
        }
        if ($this->postOnly !== null) {
            $out['post_only'] = $this->postOnly;
        }
        if ($this->timeInForce !== null) {
            $out['time_in_force'] = $this->timeInForce;
        }
        if ($this->expiresAt !== null) {
            $out['expires_at'] = $this->expiresAt;
        }
        if ($this->clientOrderId !== null) {
            $out['client_order_id'] = $this->clientOrderId;
        }
        if ($this->exits !== null) {
            $out['exits'] = $this->exits->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
