<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** A market's 24-hour figures and open interest. A figure not known yet is null. */
final class MarketStats implements \JsonSerializable
{
    private const WIRE = ['market' => true, 'last' => true, 'change_24h' => true, 'change_24h_pct' => true, 'high_24h' => true, 'low_24h' => true, 'volume_24h' => true, 'notional_volume_24h' => true, 'open_interest' => true, 'notional_open_interest' => true];

    public function __construct(
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The last trade. */
        public readonly ?string $last = null,
        /** The change over 24 hours. */
        public readonly ?string $change24h = null,
        /** That change in percent. */
        public readonly ?string $change24hPct = null,
        /** The highest in 24 hours. */
        public readonly ?string $high24h = null,
        /** The lowest in 24 hours. */
        public readonly ?string $low24h = null,
        /** The size traded in 24 hours, in the base asset. */
        public readonly ?string $volume24h = null,
        /** The value traded in 24 hours, in USD. */
        public readonly ?string $notionalVolume24h = null,
        /** The market's open interest: the size held open in positions, in the base asset. */
        public readonly ?string $openInterest = null,
        /** That open interest in USD, at the last price. */
        public readonly ?string $notionalOpenInterest = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            market: $d['market'] ?? null,
            last: isset($d['last']) ? (string) $d['last'] : null,
            change24h: isset($d['change_24h']) ? (string) $d['change_24h'] : null,
            change24hPct: isset($d['change_24h_pct']) ? (string) $d['change_24h_pct'] : null,
            high24h: isset($d['high_24h']) ? (string) $d['high_24h'] : null,
            low24h: isset($d['low_24h']) ? (string) $d['low_24h'] : null,
            volume24h: isset($d['volume_24h']) ? (string) $d['volume_24h'] : null,
            notionalVolume24h: isset($d['notional_volume_24h']) ? (string) $d['notional_volume_24h'] : null,
            openInterest: isset($d['open_interest']) ? (string) $d['open_interest'] : null,
            notionalOpenInterest: isset($d['notional_open_interest']) ? (string) $d['notional_open_interest'] : null,
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
        if ($this->last !== null) {
            $out['last'] = $this->last;
        }
        if ($this->change24h !== null) {
            $out['change_24h'] = $this->change24h;
        }
        if ($this->change24hPct !== null) {
            $out['change_24h_pct'] = $this->change24hPct;
        }
        if ($this->high24h !== null) {
            $out['high_24h'] = $this->high24h;
        }
        if ($this->low24h !== null) {
            $out['low_24h'] = $this->low24h;
        }
        if ($this->volume24h !== null) {
            $out['volume_24h'] = $this->volume24h;
        }
        if ($this->notionalVolume24h !== null) {
            $out['notional_volume_24h'] = $this->notionalVolume24h;
        }
        if ($this->openInterest !== null) {
            $out['open_interest'] = $this->openInterest;
        }
        if ($this->notionalOpenInterest !== null) {
            $out['notional_open_interest'] = $this->notionalOpenInterest;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
