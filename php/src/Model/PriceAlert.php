<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** A level on a market, told once when the price reaches it (alert.triggered: the account stream, webhooks, GET /v1/events). It never places or changes an order. */
final class PriceAlert implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'account_id' => true, 'market' => true, 'price' => true, 'condition' => true, 'note' => true, 'status' => true, 'price_source' => true, 'trigger_price' => true, 'triggered_at' => true, 'disabled_reason' => true, 'created_at' => true, 'updated_at' => true];

    public function __construct(
        /** The alert. */
        public readonly ?string $id = null,
        /** The account number. */
        public readonly ?string $accountId = null,
        /** A market id: the asset and USD; an option adds its venue or hours after a dot. */
        public readonly ?string $market = null,
        /** Its level. */
        public readonly ?string $price = null,
        /** above: it fires once the price reaches the level from below; below: from above. Set from the price when the level was set. */
        public readonly ?string $condition = null,
        /** Your note, sent with it. */
        public readonly ?string $note = null,
        /** armed: it waits for its level. triggered: it fired (rearm arms it again). disabled: its account or market went away before it fired. */
        public readonly ?string $status = null,
        /** The price it watches: the market's last trade (the middle of the book until there is one), the price trailing stops follow. */
        public readonly ?string $priceSource = null,
        /** The price it fired at. */
        public readonly ?string $triggerPrice = null,
        /** When it fired. */
        public readonly ?int $triggeredAt = null,
        /** Why it was disabled. */
        public readonly ?string $disabledReason = null,
        /** When it was set. */
        public readonly ?int $createdAt = null,
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
            accountId: $d['account_id'] ?? null,
            market: $d['market'] ?? null,
            price: isset($d['price']) ? (string) $d['price'] : null,
            condition: $d['condition'] ?? null,
            note: $d['note'] ?? null,
            status: $d['status'] ?? null,
            priceSource: $d['price_source'] ?? null,
            triggerPrice: isset($d['trigger_price']) ? (string) $d['trigger_price'] : null,
            triggeredAt: $d['triggered_at'] ?? null,
            disabledReason: $d['disabled_reason'] ?? null,
            createdAt: $d['created_at'] ?? null,
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
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->price !== null) {
            $out['price'] = $this->price;
        }
        if ($this->condition !== null) {
            $out['condition'] = $this->condition;
        }
        if ($this->note !== null) {
            $out['note'] = $this->note;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        if ($this->priceSource !== null) {
            $out['price_source'] = $this->priceSource;
        }
        if ($this->triggerPrice !== null) {
            $out['trigger_price'] = $this->triggerPrice;
        }
        if ($this->triggeredAt !== null) {
            $out['triggered_at'] = $this->triggeredAt;
        }
        if ($this->disabledReason !== null) {
            $out['disabled_reason'] = $this->disabledReason;
        }
        if ($this->createdAt !== null) {
            $out['created_at'] = $this->createdAt;
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
