<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The query of list closed positions. */
final class PositionsListClosedParams implements \JsonSerializable
{
    private const WIRE = ['market' => true, 'limit' => true, 'cursor' => true];

    public function __construct(
        /** Only this market. */
        public readonly ?string $market = null,
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
            market: $d['market'] ?? null,
            limit: $d['limit'] ?? null,
            cursor: $d['cursor'] ?? null,
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->market !== null) {
            $out['market'] = $this->market;
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
