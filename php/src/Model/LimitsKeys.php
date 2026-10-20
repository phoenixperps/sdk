<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** LimitsKeys */
final class LimitsKeys implements \JsonSerializable
{
    private const WIRE = ['per_environment' => true, 'ip_allowlist_max' => true, 'expiry_max_days' => true];

    public function __construct(
        /** Keys you may hold in each environment. */
        public readonly ?int $perEnvironment = null,
        /** Addresses in a key's allowlist. */
        public readonly ?int $ipAllowlistMax = null,
        /** The longest a key may live, in days. */
        public readonly ?int $expiryMaxDays = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            perEnvironment: $d['per_environment'] ?? null,
            ipAllowlistMax: $d['ip_allowlist_max'] ?? null,
            expiryMaxDays: $d['expiry_max_days'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->perEnvironment !== null) {
            $out['per_environment'] = $this->perEnvironment;
        }
        if ($this->ipAllowlistMax !== null) {
            $out['ip_allowlist_max'] = $this->ipAllowlistMax;
        }
        if ($this->expiryMaxDays !== null) {
            $out['expiry_max_days'] = $this->expiryMaxDays;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
