<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** LimitsStrategies */
final class LimitsStrategies implements \JsonSerializable
{
    private const WIRE = ['per_account' => true, 'programs_per_account' => true, 'legs_per_account' => true, 'per_customer' => true, 'take_profits_per_position' => true, 'stop_losses_per_position' => true, 'twap_min_minutes' => true, 'twap_max_minutes' => true, 'slice_min_seconds' => true, 'scaled_max_rungs' => true, 'chase_enabled' => true, 'chase_max_minutes' => true];

    public function __construct(
        /** Running strategies an account may hold. */
        public readonly ?int $perAccount = null,
        /** Of those, programs (oco, twap, vwap, scaled, chase). */
        public readonly ?int $programsPerAccount = null,
        /** Working orders of strategies on one account. */
        public readonly ?int $legsPerAccount = null,
        /** Running strategies across your accounts. */
        public readonly ?int $perCustomer = null,
        /** Take-profit legs on one position. */
        public readonly ?int $takeProfitsPerPosition = null,
        /** Stop-loss legs on one position. */
        public readonly ?int $stopLossesPerPosition = null,
        /** The shortest twap or vwap, in minutes. */
        public readonly ?int $twapMinMinutes = null,
        /** The longest twap or vwap, in minutes. */
        public readonly ?int $twapMaxMinutes = null,
        /** The shortest time between slices, in seconds. */
        public readonly ?int $sliceMinSeconds = null,
        /** The most limits in a scaled ladder. */
        public readonly ?int $scaledMaxRungs = null,
        /** Whether chase strategies may start. */
        public readonly ?bool $chaseEnabled = null,
        /** The longest a chase may run, in minutes. */
        public readonly ?int $chaseMaxMinutes = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            perAccount: $d['per_account'] ?? null,
            programsPerAccount: $d['programs_per_account'] ?? null,
            legsPerAccount: $d['legs_per_account'] ?? null,
            perCustomer: $d['per_customer'] ?? null,
            takeProfitsPerPosition: $d['take_profits_per_position'] ?? null,
            stopLossesPerPosition: $d['stop_losses_per_position'] ?? null,
            twapMinMinutes: $d['twap_min_minutes'] ?? null,
            twapMaxMinutes: $d['twap_max_minutes'] ?? null,
            sliceMinSeconds: $d['slice_min_seconds'] ?? null,
            scaledMaxRungs: $d['scaled_max_rungs'] ?? null,
            chaseEnabled: $d['chase_enabled'] ?? null,
            chaseMaxMinutes: $d['chase_max_minutes'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->perAccount !== null) {
            $out['per_account'] = $this->perAccount;
        }
        if ($this->programsPerAccount !== null) {
            $out['programs_per_account'] = $this->programsPerAccount;
        }
        if ($this->legsPerAccount !== null) {
            $out['legs_per_account'] = $this->legsPerAccount;
        }
        if ($this->perCustomer !== null) {
            $out['per_customer'] = $this->perCustomer;
        }
        if ($this->takeProfitsPerPosition !== null) {
            $out['take_profits_per_position'] = $this->takeProfitsPerPosition;
        }
        if ($this->stopLossesPerPosition !== null) {
            $out['stop_losses_per_position'] = $this->stopLossesPerPosition;
        }
        if ($this->twapMinMinutes !== null) {
            $out['twap_min_minutes'] = $this->twapMinMinutes;
        }
        if ($this->twapMaxMinutes !== null) {
            $out['twap_max_minutes'] = $this->twapMaxMinutes;
        }
        if ($this->sliceMinSeconds !== null) {
            $out['slice_min_seconds'] = $this->sliceMinSeconds;
        }
        if ($this->scaledMaxRungs !== null) {
            $out['scaled_max_rungs'] = $this->scaledMaxRungs;
        }
        if ($this->chaseEnabled !== null) {
            $out['chase_enabled'] = $this->chaseEnabled;
        }
        if ($this->chaseMaxMinutes !== null) {
            $out['chase_max_minutes'] = $this->chaseMaxMinutes;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
