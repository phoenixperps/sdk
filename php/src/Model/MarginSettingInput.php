<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The market's setting. The mode cannot change while a position or a working order is open on the market. */
final class MarginSettingInput implements \JsonSerializable
{
    private const WIRE = ['margin_mode' => true, 'leverage' => true];

    public function __construct(
        /** isolated: the position has its own margin. cross: positions share the account's balance. */
        public readonly ?string $marginMode = null,
        /** A whole number from 1 to the market's max_leverage. */
        public readonly ?int $leverage = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            marginMode: $d['margin_mode'] ?? null,
            leverage: $d['leverage'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->marginMode !== null) {
            $out['margin_mode'] = $this->marginMode;
        }
        if ($this->leverage !== null) {
            $out['leverage'] = $this->leverage;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
