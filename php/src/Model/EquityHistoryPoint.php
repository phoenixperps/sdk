<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** EquityHistoryPoint */
final class EquityHistoryPoint implements \JsonSerializable
{
    private const WIRE = ['time' => true, 'date' => true, 'balance' => true];

    public function __construct(
        /** When. */
        public readonly ?int $time = null,
        /** The trading day (New York), with resolution day. */
        public readonly ?string $date = null,
        /** The balance then. */
        public readonly ?string $balance = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            time: $d['time'] ?? null,
            date: $d['date'] ?? null,
            balance: isset($d['balance']) ? (string) $d['balance'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->time !== null) {
            $out['time'] = $this->time;
        }
        if ($this->date !== null) {
            $out['date'] = $this->date;
        }
        if ($this->balance !== null) {
            $out['balance'] = $this->balance;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
