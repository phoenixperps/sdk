<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ProgressLimits */
final class ProgressLimits implements \JsonSerializable
{
    private const WIRE = ['daily_loss' => true, 'max_drawdown' => true];

    public function __construct(
        /** The daily loss. */
        public readonly ?LimitStanding $dailyLoss = null,
        /** The max drawdown. */
        public readonly ?LimitStanding $maxDrawdown = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            dailyLoss: isset($d['daily_loss']) && is_array($d['daily_loss']) ? LimitStanding::fromArray($d['daily_loss']) : null,
            maxDrawdown: isset($d['max_drawdown']) && is_array($d['max_drawdown']) ? LimitStanding::fromArray($d['max_drawdown']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->dailyLoss !== null) {
            $out['daily_loss'] = $this->dailyLoss->toArray();
        }
        if ($this->maxDrawdown !== null) {
            $out['max_drawdown'] = $this->maxDrawdown->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
