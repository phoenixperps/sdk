<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** RestrictedCountry */
final class RestrictedCountry implements \JsonSerializable
{
    private const WIRE = ['code' => true, 'name' => true];

    public function __construct(
        /** The country, ISO 3166-1 alpha-2. */
        public readonly ?string $code = null,
        /** Its name in English. */
        public readonly ?string $name = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            code: $d['code'] ?? null,
            name: $d['name'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->code !== null) {
            $out['code'] = $this->code;
        }
        if ($this->name !== null) {
            $out['name'] = $this->name;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
