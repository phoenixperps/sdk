<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The account's rule, as the trading engine enforces it. */
final class Rule implements \JsonSerializable
{
    private const WIRE = ['start_balance' => true, 'daily_loss' => true, 'max_drawdown' => true, 'weekly_loss' => true, 'profit_target' => true, 'min_trading_days' => true, 'min_calendar_days' => true, 'consistency' => true, 'overnight_holding' => true, 'weekend_holding' => true, 'inactivity_days' => true];

    public function __construct(
        /** The balance the account started with. */
        public readonly ?string $startBalance = null,
        /** The daily loss. */
        public readonly ?RuleDailyLoss $dailyLoss = null,
        /** The max drawdown. */
        public readonly ?RuleMaxDrawdown $maxDrawdown = null,
        /** The weekly loss. */
        public readonly ?RuleWeeklyLoss $weeklyLoss = null,
        /** The profit target. */
        public readonly ?RuleProfitTarget $profitTarget = null,
        /** Days with a trade the account needs. */
        public readonly ?int $minTradingDays = null,
        /** Calendar days the account needs. */
        public readonly ?int $minCalendarDays = null,
        /** The consistency. */
        public readonly ?RuleConsistency $consistency = null,
        /** Positions may stay open overnight. */
        public readonly ?bool $overnightHolding = null,
        /** Positions may stay open over the weekend. */
        public readonly ?bool $weekendHolding = null,
        /** Days without a trade after which the account fails. */
        public readonly ?int $inactivityDays = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            startBalance: isset($d['start_balance']) ? (string) $d['start_balance'] : null,
            dailyLoss: isset($d['daily_loss']) && is_array($d['daily_loss']) ? RuleDailyLoss::fromArray($d['daily_loss']) : null,
            maxDrawdown: isset($d['max_drawdown']) && is_array($d['max_drawdown']) ? RuleMaxDrawdown::fromArray($d['max_drawdown']) : null,
            weeklyLoss: isset($d['weekly_loss']) && is_array($d['weekly_loss']) ? RuleWeeklyLoss::fromArray($d['weekly_loss']) : null,
            profitTarget: isset($d['profit_target']) && is_array($d['profit_target']) ? RuleProfitTarget::fromArray($d['profit_target']) : null,
            minTradingDays: $d['min_trading_days'] ?? null,
            minCalendarDays: $d['min_calendar_days'] ?? null,
            consistency: isset($d['consistency']) && is_array($d['consistency']) ? RuleConsistency::fromArray($d['consistency']) : null,
            overnightHolding: $d['overnight_holding'] ?? null,
            weekendHolding: $d['weekend_holding'] ?? null,
            inactivityDays: $d['inactivity_days'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->startBalance !== null) {
            $out['start_balance'] = $this->startBalance;
        }
        if ($this->dailyLoss !== null) {
            $out['daily_loss'] = $this->dailyLoss->toArray();
        }
        if ($this->maxDrawdown !== null) {
            $out['max_drawdown'] = $this->maxDrawdown->toArray();
        }
        if ($this->weeklyLoss !== null) {
            $out['weekly_loss'] = $this->weeklyLoss->toArray();
        }
        if ($this->profitTarget !== null) {
            $out['profit_target'] = $this->profitTarget->toArray();
        }
        if ($this->minTradingDays !== null) {
            $out['min_trading_days'] = $this->minTradingDays;
        }
        if ($this->minCalendarDays !== null) {
            $out['min_calendar_days'] = $this->minCalendarDays;
        }
        if ($this->consistency !== null) {
            $out['consistency'] = $this->consistency->toArray();
        }
        if ($this->overnightHolding !== null) {
            $out['overnight_holding'] = $this->overnightHolding;
        }
        if ($this->weekendHolding !== null) {
            $out['weekend_holding'] = $this->weekendHolding;
        }
        if ($this->inactivityDays !== null) {
            $out['inactivity_days'] = $this->inactivityDays;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
