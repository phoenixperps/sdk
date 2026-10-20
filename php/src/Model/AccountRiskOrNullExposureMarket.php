<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** AccountRiskOrNullExposureMarket */
final class AccountRiskOrNullExposureMarket implements \JsonSerializable
{
    private const WIRE = ['market' => true, 'notional' => true, 'leverage' => true, 'cap' => true, 'cap_used_pct' => true];

    public function __construct(
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The value held on it. */
        public readonly ?string $notional = null,
        /** That value over equity. */
        public readonly ?string $leverage = null,
        /** The exposure cap of the market. */
        public readonly ?string $cap = null,
        /** How much of the cap is used, counting your other accounts on the same rule, in percent. */
        public readonly ?string $capUsedPct = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            market: $d['market'] ?? null,
            notional: isset($d['notional']) ? (string) $d['notional'] : null,
            leverage: isset($d['leverage']) ? (string) $d['leverage'] : null,
            cap: isset($d['cap']) ? (string) $d['cap'] : null,
            capUsedPct: isset($d['cap_used_pct']) ? (string) $d['cap_used_pct'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->notional !== null) {
            $out['notional'] = $this->notional;
        }
        if ($this->leverage !== null) {
            $out['leverage'] = $this->leverage;
        }
        if ($this->cap !== null) {
            $out['cap'] = $this->cap;
        }
        if ($this->capUsedPct !== null) {
            $out['cap_used_pct'] = $this->capUsedPct;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
