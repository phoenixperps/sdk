<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** EquityHistory */
final class EquityHistory implements \JsonSerializable
{
    private const WIRE = ['resolution' => true, 'points' => true, 'live' => true];

    public function __construct(
        /** One point per closed trade, or per trading day. */
        public readonly ?string $resolution = null,
        /** Oldest first; the first point is the opening balance. */
        /** @var list<EquityHistoryPoint>|null */
        public readonly ?array $points = null,
        /** The live. */
        public readonly ?EquityHistoryLive $live = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            resolution: $d['resolution'] ?? null,
            points: isset($d['points']) && is_array($d['points']) ? array_map(fn ($x) => EquityHistoryPoint::fromArray($x), $d['points']) : null,
            live: isset($d['live']) && is_array($d['live']) ? EquityHistoryLive::fromArray($d['live']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->resolution !== null) {
            $out['resolution'] = $this->resolution;
        }
        if ($this->points !== null) {
            $out['points'] = array_map(fn ($x) => $x->toArray(), $this->points);
        }
        if ($this->live !== null) {
            $out['live'] = $this->live->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
