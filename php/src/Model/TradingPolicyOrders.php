<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** TradingPolicyOrders */
final class TradingPolicyOrders implements \JsonSerializable
{
    private const WIRE = ['max_working_per_account' => true, 'new_per_min_account' => true, 'price_band_pct' => true, 'quote_max_age_s' => true, 'quote_max_age_slow_s' => true];

    public function __construct(
        /** Working orders the account may hold. */
        public readonly ?int $maxWorkingPerAccount = null,
        /** New orders a minute. */
        public readonly ?int $newPerMinAccount = null,
        /** How far from the market price a priced order may be, in percent. */
        public readonly ?string $priceBandPct = null,
        /** The oldest price an order is checked against, in seconds. */
        public readonly ?int $quoteMaxAgeS = null,
        /** The oldest unchanged price of a quiet market an order is checked against, while the price feed lives (other markets move), in seconds. */
        public readonly ?int $quoteMaxAgeSlowS = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            maxWorkingPerAccount: $d['max_working_per_account'] ?? null,
            newPerMinAccount: $d['new_per_min_account'] ?? null,
            priceBandPct: isset($d['price_band_pct']) ? (string) $d['price_band_pct'] : null,
            quoteMaxAgeS: $d['quote_max_age_s'] ?? null,
            quoteMaxAgeSlowS: $d['quote_max_age_slow_s'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->maxWorkingPerAccount !== null) {
            $out['max_working_per_account'] = $this->maxWorkingPerAccount;
        }
        if ($this->newPerMinAccount !== null) {
            $out['new_per_min_account'] = $this->newPerMinAccount;
        }
        if ($this->priceBandPct !== null) {
            $out['price_band_pct'] = $this->priceBandPct;
        }
        if ($this->quoteMaxAgeS !== null) {
            $out['quote_max_age_s'] = $this->quoteMaxAgeS;
        }
        if ($this->quoteMaxAgeSlowS !== null) {
            $out['quote_max_age_slow_s'] = $this->quoteMaxAgeSlowS;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
