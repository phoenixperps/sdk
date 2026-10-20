<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Strategy */
final class Strategy implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'client_strategy_id' => true, 'account_id' => true, 'type' => true, 'market' => true, 'side' => true, 'status' => true, 'end_reason' => true, 'params' => true, 'progress' => true, 'orders' => true, 'created_at' => true, 'updated_at' => true, 'ended_at' => true];

    public function __construct(
        /** The strategy. */
        public readonly ?string $id = null,
        /** Your own id for it. */
        public readonly ?string $clientStrategyId = null,
        /** The account number. */
        public readonly ?string $accountId = null,
        /** exits: a position's take-profit and stop-loss legs. bracket: an order with exits once it fills. oco: two orders, the first fill cancels the other. twap, vwap: an order in slices over time. scaled: a ladder of limits. chase: a limit that follows the price. */
        public readonly ?string $type = null,
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The side of its orders (exits: the closing side). */
        public readonly ?string $side = null,
        /** waiting: armed, nothing placed yet. working: running. completed: done as asked. canceled: you ended it. ended: it stopped for end_reason. */
        public readonly ?string $status = null,
        /** Why it stopped. */
        public readonly ?string $endReason = null,
        /** What it was created with. */
        /** @var array<string, mixed>|null */
        public readonly ?array $params = null,
        /** How far it is (by type). */
        /** @var array<string, mixed>|null */
        public readonly ?array $progress = null,
        /** Every order it placed, oldest first. */
        /** @var list<Order>|null */
        public readonly ?array $orders = null,
        /** When it was created. */
        public readonly ?int $createdAt = null,
        /** Its latest change. */
        public readonly ?int $updatedAt = null,
        /** When it stopped. */
        public readonly ?int $endedAt = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            clientStrategyId: $d['client_strategy_id'] ?? null,
            accountId: $d['account_id'] ?? null,
            type: $d['type'] ?? null,
            market: $d['market'] ?? null,
            side: $d['side'] ?? null,
            status: $d['status'] ?? null,
            endReason: $d['end_reason'] ?? null,
            params: $d['params'] ?? null,
            progress: $d['progress'] ?? null,
            orders: isset($d['orders']) && is_array($d['orders']) ? array_map(fn ($x) => Order::fromArray($x), $d['orders']) : null,
            createdAt: $d['created_at'] ?? null,
            updatedAt: $d['updated_at'] ?? null,
            endedAt: $d['ended_at'] ?? null,
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
        if ($this->clientStrategyId !== null) {
            $out['client_strategy_id'] = $this->clientStrategyId;
        }
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->side !== null) {
            $out['side'] = $this->side;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        if ($this->endReason !== null) {
            $out['end_reason'] = $this->endReason;
        }
        if ($this->params !== null) {
            $out['params'] = $this->params;
        }
        if ($this->progress !== null) {
            $out['progress'] = $this->progress;
        }
        if ($this->orders !== null) {
            $out['orders'] = array_map(fn ($x) => $x->toArray(), $this->orders);
        }
        if ($this->createdAt !== null) {
            $out['created_at'] = $this->createdAt;
        }
        if ($this->updatedAt !== null) {
            $out['updated_at'] = $this->updatedAt;
        }
        if ($this->endedAt !== null) {
            $out['ended_at'] = $this->endedAt;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
