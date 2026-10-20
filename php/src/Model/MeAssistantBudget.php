<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The AI assistant's own budget, on top of the others. */
final class MeAssistantBudget implements \JsonSerializable
{
    private const WIRE = ['reads_per_min' => true, 'writes_per_min' => true, 'writes_per_hour' => true];

    public function __construct(
        /** Reads a minute. */
        public readonly ?int $readsPerMin = null,
        /** Trading calls a minute. */
        public readonly ?int $writesPerMin = null,
        /** Trading calls an hour. */
        public readonly ?int $writesPerHour = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            readsPerMin: $d['reads_per_min'] ?? null,
            writesPerMin: $d['writes_per_min'] ?? null,
            writesPerHour: $d['writes_per_hour'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->readsPerMin !== null) {
            $out['reads_per_min'] = $this->readsPerMin;
        }
        if ($this->writesPerMin !== null) {
            $out['writes_per_min'] = $this->writesPerMin;
        }
        if ($this->writesPerHour !== null) {
            $out['writes_per_hour'] = $this->writesPerHour;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
