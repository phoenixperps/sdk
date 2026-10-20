<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** MarginSetting */
final class MarginSetting implements \JsonSerializable
{
    private const WIRE = ['market' => true, 'leverage' => true, 'margin_mode' => true, 'max_leverage' => true, 'source' => true, 'locked' => true];

    public function __construct(
        /** A market id: the asset and USD. */
        public readonly ?string $market = null,
        /** The leverage new positions on this market get. */
        public readonly ?int $leverage = null,
        /** Isolated: each position holds its own margin. Cross: positions share the balance. */
        public readonly ?string $marginMode = null,
        /** The highest the market allows. */
        public readonly ?int $maxLeverage = null,
        /** Where it comes from: the open position, your saved setting, or the market default. */
        public readonly ?string $source = null,
        /** A position or a working order on the market: the mode cannot change now. */
        public readonly ?bool $locked = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            market: $d['market'] ?? null,
            leverage: $d['leverage'] ?? null,
            marginMode: $d['margin_mode'] ?? null,
            maxLeverage: $d['max_leverage'] ?? null,
            source: $d['source'] ?? null,
            locked: $d['locked'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->leverage !== null) {
            $out['leverage'] = $this->leverage;
        }
        if ($this->marginMode !== null) {
            $out['margin_mode'] = $this->marginMode;
        }
        if ($this->maxLeverage !== null) {
            $out['max_leverage'] = $this->maxLeverage;
        }
        if ($this->source !== null) {
            $out['source'] = $this->source;
        }
        if ($this->locked !== null) {
            $out['locked'] = $this->locked;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
