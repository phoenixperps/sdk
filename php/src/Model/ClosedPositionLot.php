<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ClosedPositionLot */
final class ClosedPositionLot implements \JsonSerializable
{
    private const WIRE = ['size' => true, 'entry_price' => true, 'exit_price' => true, 'pnl' => true, 'fees' => true, 'opened_at' => true, 'closed_at' => true];

    public function __construct(
        /** The size. */
        public readonly ?string $size = null,
        /** Entry. */
        public readonly ?string $entryPrice = null,
        /** Exit. */
        public readonly ?string $exitPrice = null,
        /** P&L after fees. */
        public readonly ?string $pnl = null,
        /** Fees. */
        public readonly ?string $fees = null,
        /** Opened. */
        public readonly ?int $openedAt = null,
        /** Closed. */
        public readonly ?int $closedAt = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            size: isset($d['size']) ? (string) $d['size'] : null,
            entryPrice: isset($d['entry_price']) ? (string) $d['entry_price'] : null,
            exitPrice: isset($d['exit_price']) ? (string) $d['exit_price'] : null,
            pnl: isset($d['pnl']) ? (string) $d['pnl'] : null,
            fees: isset($d['fees']) ? (string) $d['fees'] : null,
            openedAt: $d['opened_at'] ?? null,
            closedAt: $d['closed_at'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->size !== null) {
            $out['size'] = $this->size;
        }
        if ($this->entryPrice !== null) {
            $out['entry_price'] = $this->entryPrice;
        }
        if ($this->exitPrice !== null) {
            $out['exit_price'] = $this->exitPrice;
        }
        if ($this->pnl !== null) {
            $out['pnl'] = $this->pnl;
        }
        if ($this->fees !== null) {
            $out['fees'] = $this->fees;
        }
        if ($this->openedAt !== null) {
            $out['opened_at'] = $this->openedAt;
        }
        if ($this->closedAt !== null) {
            $out['closed_at'] = $this->closedAt;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
