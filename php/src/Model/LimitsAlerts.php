<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** LimitsAlerts */
final class LimitsAlerts implements \JsonSerializable
{
    private const WIRE = ['armed_per_account' => true, 'armed_per_customer' => true, 'kept_days' => true];

    public function __construct(
        /** Price alerts armed at once on one account. */
        public readonly ?int $armedPerAccount = null,
        /** Price alerts armed at once across your accounts. */
        public readonly ?int $armedPerCustomer = null,
        /** How long an alert that fired (or was disabled) stays listed, in days. */
        public readonly ?int $keptDays = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            armedPerAccount: $d['armed_per_account'] ?? null,
            armedPerCustomer: $d['armed_per_customer'] ?? null,
            keptDays: $d['kept_days'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->armedPerAccount !== null) {
            $out['armed_per_account'] = $this->armedPerAccount;
        }
        if ($this->armedPerCustomer !== null) {
            $out['armed_per_customer'] = $this->armedPerCustomer;
        }
        if ($this->keptDays !== null) {
            $out['kept_days'] = $this->keptDays;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
