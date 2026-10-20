<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** AccountRiskExposure */
final class AccountRiskExposure implements \JsonSerializable
{
    private const WIRE = ['gross' => true, 'long' => true, 'short' => true, 'effective_leverage' => true, 'markets' => true];

    public function __construct(
        /** The value of every open position. */
        public readonly ?string $gross = null,
        /** The value of the long ones. */
        public readonly ?string $long = null,
        /** The value of the short ones. */
        public readonly ?string $short = null,
        /** Gross exposure over equity. */
        public readonly ?string $effectiveLeverage = null,
        /** Per market, largest first. */
        /** @var list<AccountRiskExposureMarket>|null */
        public readonly ?array $markets = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            gross: isset($d['gross']) ? (string) $d['gross'] : null,
            long: isset($d['long']) ? (string) $d['long'] : null,
            short: isset($d['short']) ? (string) $d['short'] : null,
            effectiveLeverage: isset($d['effective_leverage']) ? (string) $d['effective_leverage'] : null,
            markets: isset($d['markets']) && is_array($d['markets']) ? array_map(fn ($x) => AccountRiskExposureMarket::fromArray($x), $d['markets']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->gross !== null) {
            $out['gross'] = $this->gross;
        }
        if ($this->long !== null) {
            $out['long'] = $this->long;
        }
        if ($this->short !== null) {
            $out['short'] = $this->short;
        }
        if ($this->effectiveLeverage !== null) {
            $out['effective_leverage'] = $this->effectiveLeverage;
        }
        if ($this->markets !== null) {
            $out['markets'] = array_map(fn ($x) => $x->toArray(), $this->markets);
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
