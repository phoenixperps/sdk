<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** WebhookDelivery */
final class WebhookDelivery implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'endpoint_id' => true, 'event_id' => true, 'event_type' => true, 'account_id' => true, 'test' => true, 'status' => true, 'attempts' => true, 'next_attempt_at' => true, 'created_at' => true, 'delivered_at' => true];

    public function __construct(
        /** The delivery. */
        public readonly ?string $id = null,
        /** Its endpoint. */
        public readonly ?string $endpointId = null,
        /** The event sent (the same id when it is sent again). */
        public readonly ?string $eventId = null,
        /** The event's type. */
        public readonly ?string $eventType = null,
        /** Its account. */
        public readonly ?string $accountId = null,
        /** A test. */
        public readonly ?bool $test = null,
        /** pending: waiting for its next try. failed: every try failed. canceled: the endpoint was disabled or deleted. */
        public readonly ?string $status = null,
        /** Every try, oldest first. */
        /** @var list<WebhookDeliveryAttempt>|null */
        public readonly ?array $attempts = null,
        /** When it is tried again. */
        public readonly ?int $nextAttemptAt = null,
        /** When it was queued. */
        public readonly ?int $createdAt = null,
        /** When your endpoint took it. */
        public readonly ?int $deliveredAt = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            endpointId: $d['endpoint_id'] ?? null,
            eventId: $d['event_id'] ?? null,
            eventType: $d['event_type'] ?? null,
            accountId: $d['account_id'] ?? null,
            test: $d['test'] ?? null,
            status: $d['status'] ?? null,
            attempts: isset($d['attempts']) && is_array($d['attempts']) ? array_map(fn ($x) => WebhookDeliveryAttempt::fromArray($x), $d['attempts']) : null,
            nextAttemptAt: $d['next_attempt_at'] ?? null,
            createdAt: $d['created_at'] ?? null,
            deliveredAt: $d['delivered_at'] ?? null,
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
        if ($this->endpointId !== null) {
            $out['endpoint_id'] = $this->endpointId;
        }
        if ($this->eventId !== null) {
            $out['event_id'] = $this->eventId;
        }
        if ($this->eventType !== null) {
            $out['event_type'] = $this->eventType;
        }
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->test !== null) {
            $out['test'] = $this->test;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        if ($this->attempts !== null) {
            $out['attempts'] = array_map(fn ($x) => $x->toArray(), $this->attempts);
        }
        if ($this->nextAttemptAt !== null) {
            $out['next_attempt_at'] = $this->nextAttemptAt;
        }
        if ($this->createdAt !== null) {
            $out['created_at'] = $this->createdAt;
        }
        if ($this->deliveredAt !== null) {
            $out['delivered_at'] = $this->deliveredAt;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
