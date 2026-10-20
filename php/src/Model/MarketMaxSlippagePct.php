<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** How far from the price a market order may fill, in percent. */
final class MarketMaxSlippagePct implements \JsonSerializable
{
    private const WIRE = ['default' => true, 'min' => true, 'max' => true];

    public function __construct(
        /** The slippage bound a market order gets when it names none. */
        public readonly ?string $default = null,
        /** The tightest bound an order may ask. */
        public readonly ?string $min = null,
        /** The widest bound an order may ask. */
        public readonly ?string $max = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            default: isset($d['default']) ? (string) $d['default'] : null,
            min: isset($d['min']) ? (string) $d['min'] : null,
            max: isset($d['max']) ? (string) $d['max'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->default !== null) {
            $out['default'] = $this->default;
        }
        if ($this->min !== null) {
            $out['min'] = $this->min;
        }
        if ($this->max !== null) {
            $out['max'] = $this->max;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
