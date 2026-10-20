<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** WhatIfExposure */
final class WhatIfExposure implements \JsonSerializable
{
    private const WIRE = ['cap' => true, 'open_before' => true, 'room_after' => true];

    public function __construct(
        /** The market's exposure cap across your accounts of this product. */
        public readonly ?string $cap = null,
        /** Your open value on the market now. */
        public readonly ?string $openBefore = null,
        /** What would remain under the cap. */
        public readonly ?string $roomAfter = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            cap: isset($d['cap']) ? (string) $d['cap'] : null,
            openBefore: isset($d['open_before']) ? (string) $d['open_before'] : null,
            roomAfter: isset($d['room_after']) ? (string) $d['room_after'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->cap !== null) {
            $out['cap'] = $this->cap;
        }
        if ($this->openBefore !== null) {
            $out['open_before'] = $this->openBefore;
        }
        if ($this->roomAfter !== null) {
            $out['room_after'] = $this->roomAfter;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
