<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The live risk; null while the account is being opened. */
final class AccountRiskOrNull implements \JsonSerializable
{
    private const WIRE = ['as_of' => true, 'stale' => true, 'balance' => true, 'equity' => true, 'unrealized_pnl' => true, 'pnl_today' => true, 'realized_today' => true, 'fees_today' => true, 'trades_today' => true, 'margin_used' => true, 'margin_available' => true, 'exposure' => true, 'daily_loss' => true, 'max_drawdown' => true, 'profit_target' => true, 'trading_days' => true, 'nearest_liquidation' => true, 'trading_day_ends_at' => true, 'warnings' => true];

    public function __construct(
        /** When the oldest figure here was read. */
        public readonly ?int $asOf = null,
        /** Some figures are older than usual (the engine was busy). */
        public readonly ?bool $stale = null,
        /** The balance. */
        public readonly ?string $balance = null,
        /** The balance plus the open P&L at the market price. */
        public readonly ?string $equity = null,
        /** The open P&L. */
        public readonly ?string $unrealizedPnl = null,
        /** The equity change since midnight New York, open positions included: what the daily loss rule counts. */
        public readonly ?string $pnlToday = null,
        /** The P&L closed today. */
        public readonly ?string $realizedToday = null,
        /** The fees paid today. */
        public readonly ?string $feesToday = null,
        /** Trades closed today. */
        public readonly ?int $tradesToday = null,
        /** The margin your positions hold. */
        public readonly ?string $marginUsed = null,
        /** What is left of the balance for new positions. */
        public readonly ?string $marginAvailable = null,
        /** The exposure. */
        public readonly ?AccountRiskOrNullExposure $exposure = null,
        /** The daily loss. */
        public readonly ?Limit $dailyLoss = null,
        /** The max drawdown. */
        public readonly ?Limit $maxDrawdown = null,
        /** The profit target. */
        public readonly ?AccountRiskOrNullProfitTarget $profitTarget = null,
        /** The trading days. */
        public readonly ?AccountRiskOrNullTradingDays $tradingDays = null,
        /** The nearest liquidation. */
        public readonly ?AccountRiskOrNullNearestLiquidation $nearestLiquidation = null,
        /** When the trading day ends (midnight New York). */
        public readonly ?int $tradingDayEndsAt = null,
        /** What needs your attention, most serious first. */
        /** @var list<Warning>|null */
        public readonly ?array $warnings = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            asOf: $d['as_of'] ?? null,
            stale: $d['stale'] ?? null,
            balance: isset($d['balance']) ? (string) $d['balance'] : null,
            equity: isset($d['equity']) ? (string) $d['equity'] : null,
            unrealizedPnl: isset($d['unrealized_pnl']) ? (string) $d['unrealized_pnl'] : null,
            pnlToday: isset($d['pnl_today']) ? (string) $d['pnl_today'] : null,
            realizedToday: isset($d['realized_today']) ? (string) $d['realized_today'] : null,
            feesToday: isset($d['fees_today']) ? (string) $d['fees_today'] : null,
            tradesToday: $d['trades_today'] ?? null,
            marginUsed: isset($d['margin_used']) ? (string) $d['margin_used'] : null,
            marginAvailable: isset($d['margin_available']) ? (string) $d['margin_available'] : null,
            exposure: isset($d['exposure']) && is_array($d['exposure']) ? AccountRiskOrNullExposure::fromArray($d['exposure']) : null,
            dailyLoss: isset($d['daily_loss']) && is_array($d['daily_loss']) ? Limit::fromArray($d['daily_loss']) : null,
            maxDrawdown: isset($d['max_drawdown']) && is_array($d['max_drawdown']) ? Limit::fromArray($d['max_drawdown']) : null,
            profitTarget: isset($d['profit_target']) && is_array($d['profit_target']) ? AccountRiskOrNullProfitTarget::fromArray($d['profit_target']) : null,
            tradingDays: isset($d['trading_days']) && is_array($d['trading_days']) ? AccountRiskOrNullTradingDays::fromArray($d['trading_days']) : null,
            nearestLiquidation: isset($d['nearest_liquidation']) && is_array($d['nearest_liquidation']) ? AccountRiskOrNullNearestLiquidation::fromArray($d['nearest_liquidation']) : null,
            tradingDayEndsAt: $d['trading_day_ends_at'] ?? null,
            warnings: isset($d['warnings']) && is_array($d['warnings']) ? array_map(fn ($x) => Warning::fromArray($x), $d['warnings']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->asOf !== null) {
            $out['as_of'] = $this->asOf;
        }
        if ($this->stale !== null) {
            $out['stale'] = $this->stale;
        }
        if ($this->balance !== null) {
            $out['balance'] = $this->balance;
        }
        if ($this->equity !== null) {
            $out['equity'] = $this->equity;
        }
        if ($this->unrealizedPnl !== null) {
            $out['unrealized_pnl'] = $this->unrealizedPnl;
        }
        if ($this->pnlToday !== null) {
            $out['pnl_today'] = $this->pnlToday;
        }
        if ($this->realizedToday !== null) {
            $out['realized_today'] = $this->realizedToday;
        }
        if ($this->feesToday !== null) {
            $out['fees_today'] = $this->feesToday;
        }
        if ($this->tradesToday !== null) {
            $out['trades_today'] = $this->tradesToday;
        }
        if ($this->marginUsed !== null) {
            $out['margin_used'] = $this->marginUsed;
        }
        if ($this->marginAvailable !== null) {
            $out['margin_available'] = $this->marginAvailable;
        }
        if ($this->exposure !== null) {
            $out['exposure'] = $this->exposure->toArray();
        }
        if ($this->dailyLoss !== null) {
            $out['daily_loss'] = $this->dailyLoss->toArray();
        }
        if ($this->maxDrawdown !== null) {
            $out['max_drawdown'] = $this->maxDrawdown->toArray();
        }
        if ($this->profitTarget !== null) {
            $out['profit_target'] = $this->profitTarget->toArray();
        }
        if ($this->tradingDays !== null) {
            $out['trading_days'] = $this->tradingDays->toArray();
        }
        if ($this->nearestLiquidation !== null) {
            $out['nearest_liquidation'] = $this->nearestLiquidation->toArray();
        }
        if ($this->tradingDayEndsAt !== null) {
            $out['trading_day_ends_at'] = $this->tradingDayEndsAt;
        }
        if ($this->warnings !== null) {
            $out['warnings'] = array_map(fn ($x) => $x->toArray(), $this->warnings);
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
