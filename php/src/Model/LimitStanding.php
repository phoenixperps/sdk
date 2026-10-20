<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** LimitStanding */
final class LimitStanding implements \JsonSerializable
{
    private const WIRE = ['limit' => true, 'floor' => true, 'room' => true, 'used_pct' => true, 'resets_at' => true, 'on_breach' => true, 'estimated' => true, 'mode' => true, 'at_least' => true, 'lifted' => true, 'status' => true];

    public function __construct(
        /** The limit, in USD. */
        public readonly ?string $limit = null,
        /** The equity at which it is breached. */
        public readonly ?string $floor = null,
        /** How much more the account may lose before it. */
        public readonly ?string $room = null,
        /** How much of it is used, in percent. */
        public readonly ?string $usedPct = null,
        /** Daily loss only: when it resets (midnight New York). */
        public readonly ?int $resetsAt = null,
        /** Daily loss only: what happens when it is reached. */
        public readonly ?string $onBreach = null,
        /** Daily loss only: the day's start is estimated (the engine does not publish it). */
        public readonly ?bool $estimated = null,
        /** Max drawdown only: how the reference moves. */
        public readonly ?string $mode = null,
        /** Max drawdown only: the engine may hold a higher floor than shown (it follows equity highs). */
        public readonly ?bool $atLeast = null,
        /** The rule no longer applies this limit (its cut-off was reached). */
        public readonly ?bool $lifted = null,
        /** Where it stands. */
        public readonly ?string $status = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            limit: isset($d['limit']) ? (string) $d['limit'] : null,
            floor: isset($d['floor']) ? (string) $d['floor'] : null,
            room: isset($d['room']) ? (string) $d['room'] : null,
            usedPct: isset($d['used_pct']) ? (string) $d['used_pct'] : null,
            resetsAt: $d['resets_at'] ?? null,
            onBreach: $d['on_breach'] ?? null,
            estimated: $d['estimated'] ?? null,
            mode: $d['mode'] ?? null,
            atLeast: $d['at_least'] ?? null,
            lifted: $d['lifted'] ?? null,
            status: $d['status'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->limit !== null) {
            $out['limit'] = $this->limit;
        }
        if ($this->floor !== null) {
            $out['floor'] = $this->floor;
        }
        if ($this->room !== null) {
            $out['room'] = $this->room;
        }
        if ($this->usedPct !== null) {
            $out['used_pct'] = $this->usedPct;
        }
        if ($this->resetsAt !== null) {
            $out['resets_at'] = $this->resetsAt;
        }
        if ($this->onBreach !== null) {
            $out['on_breach'] = $this->onBreach;
        }
        if ($this->estimated !== null) {
            $out['estimated'] = $this->estimated;
        }
        if ($this->mode !== null) {
            $out['mode'] = $this->mode;
        }
        if ($this->atLeast !== null) {
            $out['at_least'] = $this->atLeast;
        }
        if ($this->lifted !== null) {
            $out['lifted'] = $this->lifted;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
