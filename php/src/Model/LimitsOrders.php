<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** LimitsOrders */
final class LimitsOrders implements \JsonSerializable
{
    private const WIRE = ['max_working_per_account' => true, 'max_working_per_customer' => true, 'new_per_min_account' => true, 'new_per_day_account' => true, 'price_band_pct' => true, 'quote_max_age_s' => true, 'quote_max_age_slow_s' => true, 'gtd_min_seconds' => true, 'gtd_max_days' => true];

    public function __construct(
        /** Working orders an account may hold. */
        public readonly ?int $maxWorkingPerAccount = null,
        /** Working orders across your accounts. */
        public readonly ?int $maxWorkingPerCustomer = null,
        /** New orders a minute on one account. */
        public readonly ?int $newPerMinAccount = null,
        /** New orders a day on one account. */
        public readonly ?int $newPerDayAccount = null,
        /** How far from the market price a priced order may be, in percent. */
        public readonly ?string $priceBandPct = null,
        /** The oldest price an order is checked against, in seconds. */
        public readonly ?int $quoteMaxAgeS = null,
        /** The oldest unchanged price of a quiet market an order is checked against, while the price feed lives (other markets move), in seconds. */
        public readonly ?int $quoteMaxAgeSlowS = null,
        /** The nearest a gtd order may expire, in seconds from now. */
        public readonly ?int $gtdMinSeconds = null,
        /** The farthest, in days from now. */
        public readonly ?int $gtdMaxDays = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            maxWorkingPerAccount: $d['max_working_per_account'] ?? null,
            maxWorkingPerCustomer: $d['max_working_per_customer'] ?? null,
            newPerMinAccount: $d['new_per_min_account'] ?? null,
            newPerDayAccount: $d['new_per_day_account'] ?? null,
            priceBandPct: isset($d['price_band_pct']) ? (string) $d['price_band_pct'] : null,
            quoteMaxAgeS: $d['quote_max_age_s'] ?? null,
            quoteMaxAgeSlowS: $d['quote_max_age_slow_s'] ?? null,
            gtdMinSeconds: $d['gtd_min_seconds'] ?? null,
            gtdMaxDays: $d['gtd_max_days'] ?? null,
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
        if ($this->maxWorkingPerCustomer !== null) {
            $out['max_working_per_customer'] = $this->maxWorkingPerCustomer;
        }
        if ($this->newPerMinAccount !== null) {
            $out['new_per_min_account'] = $this->newPerMinAccount;
        }
        if ($this->newPerDayAccount !== null) {
            $out['new_per_day_account'] = $this->newPerDayAccount;
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
        if ($this->gtdMinSeconds !== null) {
            $out['gtd_min_seconds'] = $this->gtdMinSeconds;
        }
        if ($this->gtdMaxDays !== null) {
            $out['gtd_max_days'] = $this->gtdMaxDays;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
