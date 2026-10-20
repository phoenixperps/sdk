<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** TradingPolicyMargin */
final class TradingPolicyMargin implements \JsonSerializable
{
    private const WIRE = ['default_mode' => true, 'maintenance_margin_pct' => true];

    public function __construct(
        /** The margin mode of a market you never set. */
        public readonly ?string $defaultMode = null,
        /** The share of a position's margin kept as maintenance, in percent. */
        public readonly ?string $maintenanceMarginPct = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            defaultMode: $d['default_mode'] ?? null,
            maintenanceMarginPct: isset($d['maintenance_margin_pct']) ? (string) $d['maintenance_margin_pct'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->defaultMode !== null) {
            $out['default_mode'] = $this->defaultMode;
        }
        if ($this->maintenanceMarginPct !== null) {
            $out['maintenance_margin_pct'] = $this->maintenanceMarginPct;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
