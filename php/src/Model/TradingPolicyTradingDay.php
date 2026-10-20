<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** TradingPolicyTradingDay */
final class TradingPolicyTradingDay implements \JsonSerializable
{
    private const WIRE = ['timezone' => true, 'ends_at' => true];

    public function __construct(
        /** The trading day runs midnight to midnight in this zone. */
        public readonly ?string $timezone = null,
        /** When this one ends. */
        public readonly ?int $endsAt = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            timezone: $d['timezone'] ?? null,
            endsAt: $d['ends_at'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->timezone !== null) {
            $out['timezone'] = $this->timezone;
        }
        if ($this->endsAt !== null) {
            $out['ends_at'] = $this->endsAt;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
