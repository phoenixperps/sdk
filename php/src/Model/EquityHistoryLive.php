<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** EquityHistoryLive */
final class EquityHistoryLive implements \JsonSerializable
{
    private const WIRE = ['time' => true, 'balance' => true, 'equity' => true];

    public function __construct(
        /** When the live figures were read. */
        public readonly ?int $time = null,
        /** The balance now. */
        public readonly ?string $balance = null,
        /** The equity now. */
        public readonly ?string $equity = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            time: $d['time'] ?? null,
            balance: isset($d['balance']) ? (string) $d['balance'] : null,
            equity: isset($d['equity']) ? (string) $d['equity'] : null,
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
        if ($this->balance !== null) {
            $out['balance'] = $this->balance;
        }
        if ($this->equity !== null) {
            $out['equity'] = $this->equity;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
