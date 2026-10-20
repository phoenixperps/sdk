<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** PositionBreachPrices */
final class PositionBreachPrices implements \JsonSerializable
{
    private const WIRE = ['daily_loss' => true, 'max_drawdown' => true];

    public function __construct(
        /** The price at which this position alone would breach the daily loss limit. */
        public readonly ?string $dailyLoss = null,
        /** The price at which it alone would breach the max drawdown. */
        public readonly ?string $maxDrawdown = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            dailyLoss: isset($d['daily_loss']) ? (string) $d['daily_loss'] : null,
            maxDrawdown: isset($d['max_drawdown']) ? (string) $d['max_drawdown'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->dailyLoss !== null) {
            $out['daily_loss'] = $this->dailyLoss;
        }
        if ($this->maxDrawdown !== null) {
            $out['max_drawdown'] = $this->maxDrawdown;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
