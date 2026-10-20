<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** DailyResult */
final class DailyResult implements \JsonSerializable
{
    private const WIRE = ['date' => true, 'trades' => true, 'gross_pnl' => true, 'fees' => true, 'pnl' => true, 'withdrawn' => true, 'balance_end' => true];

    public function __construct(
        /** The trading day (midnight to midnight New York). */
        public readonly ?string $date = null,
        /** Trades closed that day. */
        public readonly ?int $trades = null,
        /** Their P&L before fees. */
        public readonly ?string $grossPnl = null,
        /** Their fees. */
        public readonly ?string $fees = null,
        /** Their P&L after fees. */
        public readonly ?string $pnl = null,
        /** Paid out that day. */
        public readonly ?string $withdrawn = null,
        /** The balance at the day's end. */
        public readonly ?string $balanceEnd = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            date: $d['date'] ?? null,
            trades: $d['trades'] ?? null,
            grossPnl: isset($d['gross_pnl']) ? (string) $d['gross_pnl'] : null,
            fees: isset($d['fees']) ? (string) $d['fees'] : null,
            pnl: isset($d['pnl']) ? (string) $d['pnl'] : null,
            withdrawn: isset($d['withdrawn']) ? (string) $d['withdrawn'] : null,
            balanceEnd: isset($d['balance_end']) ? (string) $d['balance_end'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->date !== null) {
            $out['date'] = $this->date;
        }
        if ($this->trades !== null) {
            $out['trades'] = $this->trades;
        }
        if ($this->grossPnl !== null) {
            $out['gross_pnl'] = $this->grossPnl;
        }
        if ($this->fees !== null) {
            $out['fees'] = $this->fees;
        }
        if ($this->pnl !== null) {
            $out['pnl'] = $this->pnl;
        }
        if ($this->withdrawn !== null) {
            $out['withdrawn'] = $this->withdrawn;
        }
        if ($this->balanceEnd !== null) {
            $out['balance_end'] = $this->balanceEnd;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
