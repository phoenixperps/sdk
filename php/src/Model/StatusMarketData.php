<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** StatusMarketData */
final class StatusMarketData implements \JsonSerializable
{
    private const WIRE = ['live' => true, 'checked_at' => true];

    public function __construct(
        /** Whether the market-data stream is live, with every price feed. */
        public readonly ?bool $live = null,
        /** When that was last checked. */
        public readonly ?int $checkedAt = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            live: $d['live'] ?? null,
            checkedAt: $d['checked_at'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->live !== null) {
            $out['live'] = $this->live;
        }
        if ($this->checkedAt !== null) {
            $out['checked_at'] = $this->checkedAt;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
