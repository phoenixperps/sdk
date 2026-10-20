<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** WhatIfMargin */
final class WhatIfMargin implements \JsonSerializable
{
    private const WIRE = ['mode' => true, 'leverage' => true, 'required' => true, 'available_before' => true, 'available_after' => true];

    public function __construct(
        /** The margin mode the position would use. */
        public readonly ?string $mode = null,
        /** The leverage it would use. */
        public readonly ?int $leverage = null,
        /** The margin the order needs. */
        public readonly ?string $required = null,
        /** The margin available now. */
        public readonly ?string $availableBefore = null,
        /** What would remain available. */
        public readonly ?string $availableAfter = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            mode: $d['mode'] ?? null,
            leverage: $d['leverage'] ?? null,
            required: isset($d['required']) ? (string) $d['required'] : null,
            availableBefore: isset($d['available_before']) ? (string) $d['available_before'] : null,
            availableAfter: isset($d['available_after']) ? (string) $d['available_after'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->mode !== null) {
            $out['mode'] = $this->mode;
        }
        if ($this->leverage !== null) {
            $out['leverage'] = $this->leverage;
        }
        if ($this->required !== null) {
            $out['required'] = $this->required;
        }
        if ($this->availableBefore !== null) {
            $out['available_before'] = $this->availableBefore;
        }
        if ($this->availableAfter !== null) {
            $out['available_after'] = $this->availableAfter;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
