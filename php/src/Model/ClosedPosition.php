<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ClosedPosition */
final class ClosedPosition implements \JsonSerializable
{
    private const WIRE = ['market' => true, 'side' => true, 'size' => true, 'entry_price' => true, 'exit_price' => true, 'gross_pnl' => true, 'fees' => true, 'pnl' => true, 'opened_at' => true, 'closed_at' => true, 'lots' => true];

    public function __construct(
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** Long or short. */
        public readonly ?string $side = null,
        /** The size closed. */
        public readonly ?string $size = null,
        /** The average entry price. */
        public readonly ?string $entryPrice = null,
        /** The average exit price. */
        public readonly ?string $exitPrice = null,
        /** The P&L before fees. */
        public readonly ?string $grossPnl = null,
        /** The fees. */
        public readonly ?string $fees = null,
        /** The P&L after fees. */
        public readonly ?string $pnl = null,
        /** When it opened. */
        public readonly ?int $openedAt = null,
        /** When it was fully closed. */
        public readonly ?int $closedAt = null,
        /** The pieces it was closed in. */
        /** @var list<ClosedPositionLot>|null */
        public readonly ?array $lots = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            market: $d['market'] ?? null,
            side: $d['side'] ?? null,
            size: isset($d['size']) ? (string) $d['size'] : null,
            entryPrice: isset($d['entry_price']) ? (string) $d['entry_price'] : null,
            exitPrice: isset($d['exit_price']) ? (string) $d['exit_price'] : null,
            grossPnl: isset($d['gross_pnl']) ? (string) $d['gross_pnl'] : null,
            fees: isset($d['fees']) ? (string) $d['fees'] : null,
            pnl: isset($d['pnl']) ? (string) $d['pnl'] : null,
            openedAt: $d['opened_at'] ?? null,
            closedAt: $d['closed_at'] ?? null,
            lots: isset($d['lots']) && is_array($d['lots']) ? array_map(fn ($x) => ClosedPositionLot::fromArray($x), $d['lots']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->side !== null) {
            $out['side'] = $this->side;
        }
        if ($this->size !== null) {
            $out['size'] = $this->size;
        }
        if ($this->entryPrice !== null) {
            $out['entry_price'] = $this->entryPrice;
        }
        if ($this->exitPrice !== null) {
            $out['exit_price'] = $this->exitPrice;
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
        if ($this->openedAt !== null) {
            $out['opened_at'] = $this->openedAt;
        }
        if ($this->closedAt !== null) {
            $out['closed_at'] = $this->closedAt;
        }
        if ($this->lots !== null) {
            $out['lots'] = array_map(fn ($x) => $x->toArray(), $this->lots);
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
