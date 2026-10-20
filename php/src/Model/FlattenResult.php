<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** FlattenResult */
final class FlattenResult implements \JsonSerializable
{
    private const WIRE = ['account_id' => true, 'orders_canceled' => true, 'closes' => true];

    public function __construct(
        /** The account number. */
        public readonly ?string $accountId = null,
        /** How many working orders were canceled. */
        public readonly ?int $ordersCanceled = null,
        /** The orders that closed the positions. */
        /** @var list<Order>|null */
        public readonly ?array $closes = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            accountId: $d['account_id'] ?? null,
            ordersCanceled: $d['orders_canceled'] ?? null,
            closes: isset($d['closes']) && is_array($d['closes']) ? array_map(fn ($x) => Order::fromArray($x), $d['closes']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->ordersCanceled !== null) {
            $out['orders_canceled'] = $this->ordersCanceled;
        }
        if ($this->closes !== null) {
            $out['closes'] = array_map(fn ($x) => $x->toArray(), $this->closes);
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
