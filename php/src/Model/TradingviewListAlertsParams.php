<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The query of list a link's alerts. */
final class TradingviewListAlertsParams implements \JsonSerializable
{
    private const WIRE = ['status' => true, 'limit' => true, 'cursor' => true];

    public function __construct(
        /** Only alerts with this status. */
        public readonly ?string $status = null,
        /** Page size. */
        public readonly ?int $limit = null,
        /** next_cursor from the previous page, with the same filters. */
        public readonly ?string $cursor = null,
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            status: $d['status'] ?? null,
            limit: $d['limit'] ?? null,
            cursor: $d['cursor'] ?? null,
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        if ($this->limit !== null) {
            $out['limit'] = $this->limit;
        }
        if ($this->cursor !== null) {
            $out['cursor'] = $this->cursor;
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
