<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The query of list strategies. */
final class StrategiesListParams implements \JsonSerializable
{
    private const WIRE = ['status' => true, 'market' => true, 'client_strategy_id' => true, 'limit' => true, 'cursor' => true];

    public function __construct(
        /** active: waiting or working. ended: completed, canceled or ended. all: both. */
        public readonly ?string $status = null,
        /** Only this market. */
        public readonly ?string $market = null,
        /** The strategy you created with this id. */
        public readonly ?string $clientStrategyId = null,
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
            market: $d['market'] ?? null,
            clientStrategyId: $d['client_strategy_id'] ?? null,
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
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->clientStrategyId !== null) {
            $out['client_strategy_id'] = $this->clientStrategyId;
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
