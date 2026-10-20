<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The query of list events. */
final class EventsListParams implements \JsonSerializable
{
    private const WIRE = ['account_id' => true, 'after' => true, 'limit' => true];

    public function __construct(
        /** Only this account. */
        public readonly ?string $accountId = null,
        /** The id of the last event you have: the ones after it, oldest first. Absent: from the oldest kept. */
        public readonly ?string $after = null,
        /** Page size. */
        public readonly ?int $limit = null,
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            accountId: $d['account_id'] ?? null,
            after: $d['after'] ?? null,
            limit: $d['limit'] ?? null,
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->after !== null) {
            $out['after'] = $this->after;
        }
        if ($this->limit !== null) {
            $out['limit'] = $this->limit;
        }
        return $out;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }

    /** @return array<string, string> the query string parameters */
    public function toQuery(): array
    {
        $out = [];
        foreach ($this->toArray() as $k => $v) {
            $out[$k] = is_bool($v) ? ($v ? 'true' : 'false') : (string) $v;
        }
        return $out;
    }
}
