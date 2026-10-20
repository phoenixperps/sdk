<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ProgressObjectivesProfitTarget */
final class ProgressObjectivesProfitTarget implements \JsonSerializable
{
    private const WIRE = ['goal_balance' => true, 'remaining' => true, 'progress_pct' => true, 'requires_flat' => true, 'status' => true];

    public function __construct(
        /** The balance that reaches it. */
        public readonly ?string $goalBalance = null,
        /** Still to make. */
        public readonly ?string $remaining = null,
        /** How far, in percent. */
        public readonly ?string $progressPct = null,
        /** Positions must be closed when it is reached. */
        public readonly ?bool $requiresFlat = null,
        /** Whether it is reached. */
        public readonly ?string $status = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            goalBalance: isset($d['goal_balance']) ? (string) $d['goal_balance'] : null,
            remaining: isset($d['remaining']) ? (string) $d['remaining'] : null,
            progressPct: isset($d['progress_pct']) ? (string) $d['progress_pct'] : null,
            requiresFlat: $d['requires_flat'] ?? null,
            status: $d['status'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->goalBalance !== null) {
            $out['goal_balance'] = $this->goalBalance;
        }
        if ($this->remaining !== null) {
            $out['remaining'] = $this->remaining;
        }
        if ($this->progressPct !== null) {
            $out['progress_pct'] = $this->progressPct;
        }
        if ($this->requiresFlat !== null) {
            $out['requires_flat'] = $this->requiresFlat;
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
