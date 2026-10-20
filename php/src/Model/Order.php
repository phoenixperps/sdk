<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Order */
final class Order implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'client_order_id' => true, 'account_id' => true, 'market' => true, 'side' => true, 'type' => true, 'status' => true, 'size' => true, 'filled_size' => true, 'limit_price' => true, 'stop_price' => true, 'average_fill_price' => true, 'fee' => true, 'reduce_only' => true, 'time_in_force' => true, 'expires_at' => true, 'post_only' => true, 'source' => true, 'strategy_id' => true, 'exits' => true, 'cancel_reason' => true, 'reject' => true, 'created_at' => true, 'filled_at' => true, 'canceled_at' => true, 'updated_at' => true];

    public function __construct(
        /** The order. */
        public readonly ?string $id = null,
        /** Your own id for it, when it was placed through the API. */
        public readonly ?string $clientOrderId = null,
        /** The account number. */
        public readonly ?string $accountId = null,
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The order's side; null for an order placed elsewhere before the API saw it. */
        public readonly ?string $side = null,
        /** The order's type. */
        public readonly ?string $type = null,
        /** unconfirmed: the engine has not confirmed its last state yet. */
        public readonly ?string $status = null,
        /** The size, in the base asset. */
        public readonly ?string $size = null,
        /** How much filled. */
        public readonly ?string $filledSize = null,
        /** Its limit price. */
        public readonly ?string $limitPrice = null,
        /** Its trigger price. */
        public readonly ?string $stopPrice = null,
        /** The average fill price. */
        public readonly ?string $averageFillPrice = null,
        /** The fee its fill paid. */
        public readonly ?string $fee = null,
        /** It may only reduce a position; null when not known (placed elsewhere). */
        public readonly ?bool $reduceOnly = null,
        /** How long it rests. */
        public readonly ?string $timeInForce = null,
        /** A gtd order: when it expires (it ends expired then); null otherwise. */
        public readonly ?int $expiresAt = null,
        /** It may only rest, never fill at once; null when not known (placed elsewhere). */
        public readonly ?bool $postOnly = null,
        /** Where it was placed: this API, an AI assistant (the MCP server), a trading platform, a strategy, a TradingView alert, or the system (a liquidation). */
        public readonly ?string $source = null,
        /** The strategy that placed it (source strategy). */
        public readonly ?string $strategyId = null,
        /** Exits attached to it (orders placed through the API). */
        /** @var array<string, mixed>|null */
        public readonly ?array $exits = null,
        /** Why it was canceled. */
        public readonly ?string $cancelReason = null,
        /** The reject. */
        public readonly ?OrderReject $reject = null,
        /** When it was placed. */
        public readonly ?int $createdAt = null,
        /** When it filled. */
        public readonly ?int $filledAt = null,
        /** When it was canceled. */
        public readonly ?int $canceledAt = null,
        /** Its latest change. */
        public readonly ?int $updatedAt = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            clientOrderId: $d['client_order_id'] ?? null,
            accountId: $d['account_id'] ?? null,
            market: $d['market'] ?? null,
            side: $d['side'] ?? null,
            type: $d['type'] ?? null,
            status: $d['status'] ?? null,
            size: isset($d['size']) ? (string) $d['size'] : null,
            filledSize: isset($d['filled_size']) ? (string) $d['filled_size'] : null,
            limitPrice: isset($d['limit_price']) ? (string) $d['limit_price'] : null,
            stopPrice: isset($d['stop_price']) ? (string) $d['stop_price'] : null,
            averageFillPrice: isset($d['average_fill_price']) ? (string) $d['average_fill_price'] : null,
            fee: isset($d['fee']) ? (string) $d['fee'] : null,
            reduceOnly: $d['reduce_only'] ?? null,
            timeInForce: $d['time_in_force'] ?? null,
            expiresAt: $d['expires_at'] ?? null,
            postOnly: $d['post_only'] ?? null,
            source: $d['source'] ?? null,
            strategyId: $d['strategy_id'] ?? null,
            exits: $d['exits'] ?? null,
            cancelReason: $d['cancel_reason'] ?? null,
            reject: isset($d['reject']) && is_array($d['reject']) ? OrderReject::fromArray($d['reject']) : null,
            createdAt: $d['created_at'] ?? null,
            filledAt: $d['filled_at'] ?? null,
            canceledAt: $d['canceled_at'] ?? null,
            updatedAt: $d['updated_at'] ?? null,
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
        if ($this->clientOrderId !== null) {
            $out['client_order_id'] = $this->clientOrderId;
        }
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->side !== null) {
            $out['side'] = $this->side;
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
        if ($this->filledSize !== null) {
            $out['filled_size'] = $this->filledSize;
        }
        if ($this->limitPrice !== null) {
            $out['limit_price'] = $this->limitPrice;
        }
        if ($this->stopPrice !== null) {
            $out['stop_price'] = $this->stopPrice;
        }
        if ($this->averageFillPrice !== null) {
            $out['average_fill_price'] = $this->averageFillPrice;
        }
        if ($this->fee !== null) {
            $out['fee'] = $this->fee;
        }
        if ($this->reduceOnly !== null) {
            $out['reduce_only'] = $this->reduceOnly;
        }
        if ($this->timeInForce !== null) {
            $out['time_in_force'] = $this->timeInForce;
        }
        if ($this->expiresAt !== null) {
            $out['expires_at'] = $this->expiresAt;
        }
        if ($this->postOnly !== null) {
            $out['post_only'] = $this->postOnly;
        }
        if ($this->source !== null) {
            $out['source'] = $this->source;
        }
        if ($this->strategyId !== null) {
            $out['strategy_id'] = $this->strategyId;
        }
        if ($this->exits !== null) {
            $out['exits'] = $this->exits;
        }
        if ($this->cancelReason !== null) {
            $out['cancel_reason'] = $this->cancelReason;
        }
        if ($this->reject !== null) {
            $out['reject'] = $this->reject->toArray();
        }
        if ($this->createdAt !== null) {
            $out['created_at'] = $this->createdAt;
        }
        if ($this->filledAt !== null) {
            $out['filled_at'] = $this->filledAt;
        }
        if ($this->canceledAt !== null) {
            $out['canceled_at'] = $this->canceledAt;
        }
        if ($this->updatedAt !== null) {
            $out['updated_at'] = $this->updatedAt;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
