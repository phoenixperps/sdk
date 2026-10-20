<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** LimitsSandbox */
final class LimitsSandbox implements \JsonSerializable
{
    private const WIRE = ['accounts_for_life' => true, 'start_balance' => true, 'parked_after_hours' => true];

    public function __construct(
        /** Sandbox accounts you may open, ever. */
        public readonly ?int $accountsForLife = null,
        /** The balance a sandbox account starts with. */
        public readonly ?string $startBalance = null,
        /** Hours without use before a sandbox account is parked. */
        public readonly ?int $parkedAfterHours = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            accountsForLife: $d['accounts_for_life'] ?? null,
            startBalance: isset($d['start_balance']) ? (string) $d['start_balance'] : null,
            parkedAfterHours: $d['parked_after_hours'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->accountsForLife !== null) {
            $out['accounts_for_life'] = $this->accountsForLife;
        }
        if ($this->startBalance !== null) {
            $out['start_balance'] = $this->startBalance;
        }
        if ($this->parkedAfterHours !== null) {
            $out['parked_after_hours'] = $this->parkedAfterHours;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
