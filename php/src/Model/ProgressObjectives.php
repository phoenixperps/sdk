<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ProgressObjectives */
final class ProgressObjectives implements \JsonSerializable
{
    private const WIRE = ['profit_target' => true, 'min_trading_days' => true, 'min_calendar_days' => true];

    public function __construct(
        /** The profit target. */
        public readonly ?ProgressObjectivesProfitTarget $profitTarget = null,
        /** The min trading days. */
        public readonly ?ProgressObjectivesMinTradingDays $minTradingDays = null,
        /** The min calendar days. */
        public readonly ?ProgressObjectivesMinCalendarDays $minCalendarDays = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            profitTarget: isset($d['profit_target']) && is_array($d['profit_target']) ? ProgressObjectivesProfitTarget::fromArray($d['profit_target']) : null,
            minTradingDays: isset($d['min_trading_days']) && is_array($d['min_trading_days']) ? ProgressObjectivesMinTradingDays::fromArray($d['min_trading_days']) : null,
            minCalendarDays: isset($d['min_calendar_days']) && is_array($d['min_calendar_days']) ? ProgressObjectivesMinCalendarDays::fromArray($d['min_calendar_days']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->profitTarget !== null) {
            $out['profit_target'] = $this->profitTarget->toArray();
        }
        if ($this->minTradingDays !== null) {
            $out['min_trading_days'] = $this->minTradingDays->toArray();
        }
        if ($this->minCalendarDays !== null) {
            $out['min_calendar_days'] = $this->minCalendarDays->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
