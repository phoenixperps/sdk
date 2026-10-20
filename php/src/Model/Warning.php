<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Warning */
final class Warning implements \JsonSerializable
{
    private const WIRE = ['code' => true, 'level' => true, 'market' => true, 'value' => true];

    public function __construct(
        /** What it is about. */
        public readonly ?string $code = null,
        /** How serious. */
        public readonly ?string $level = null,
        /** The market it is about. */
        public readonly ?string $market = null,
        /** The figure behind it, in percent. */
        public readonly ?string $value = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            code: $d['code'] ?? null,
            level: $d['level'] ?? null,
            market: $d['market'] ?? null,
            value: isset($d['value']) ? (string) $d['value'] : null,
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
        if ($this->level !== null) {
            $out['level'] = $this->level;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->value !== null) {
            $out['value'] = $this->value;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
