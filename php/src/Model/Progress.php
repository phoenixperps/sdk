<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Progress */
final class Progress implements \JsonSerializable
{
    private const WIRE = ['stage' => true, 'objectives' => true, 'limits' => true, 'payout' => true, 'as_of' => true];

    public function __construct(
        /** Where the account is in the programme. */
        public readonly ?string $stage = null,
        /** The objectives. */
        public readonly ?ProgressObjectives $objectives = null,
        /** The limits. */
        public readonly ?ProgressLimits $limits = null,
        /** The payout. */
        public readonly ?ProgressPayout $payout = null,
        /** When the oldest figure here was read. */
        public readonly ?int $asOf = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            stage: $d['stage'] ?? null,
            objectives: isset($d['objectives']) && is_array($d['objectives']) ? ProgressObjectives::fromArray($d['objectives']) : null,
            limits: isset($d['limits']) && is_array($d['limits']) ? ProgressLimits::fromArray($d['limits']) : null,
            payout: isset($d['payout']) && is_array($d['payout']) ? ProgressPayout::fromArray($d['payout']) : null,
            asOf: $d['as_of'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->stage !== null) {
            $out['stage'] = $this->stage;
        }
        if ($this->objectives !== null) {
            $out['objectives'] = $this->objectives->toArray();
        }
        if ($this->limits !== null) {
            $out['limits'] = $this->limits->toArray();
        }
        if ($this->payout !== null) {
            $out['payout'] = $this->payout->toArray();
        }
        if ($this->asOf !== null) {
            $out['as_of'] = $this->asOf;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
