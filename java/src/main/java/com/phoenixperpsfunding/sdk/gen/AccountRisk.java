// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** AccountRisk */
public final class AccountRisk {
    @JsonProperty("as_of")
    private Long asOf;

    @JsonProperty("stale")
    private Boolean stale;

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("equity")
    private BigDecimal equity;

    @JsonProperty("unrealized_pnl")
    private BigDecimal unrealizedPnl;

    @JsonProperty("pnl_today")
    private BigDecimal pnlToday;

    @JsonProperty("realized_today")
    private BigDecimal realizedToday;

    @JsonProperty("fees_today")
    private BigDecimal feesToday;

    @JsonProperty("trades_today")
    private Long tradesToday;

    @JsonProperty("margin_used")
    private BigDecimal marginUsed;

    @JsonProperty("margin_available")
    private BigDecimal marginAvailable;

    @JsonProperty("exposure")
    private AccountRiskExposure exposure;

    @JsonProperty("daily_loss")
    private Limit dailyLoss;

    @JsonProperty("max_drawdown")
    private Limit maxDrawdown;

    @JsonProperty("profit_target")
    private AccountRiskProfitTarget profitTarget;

    @JsonProperty("trading_days")
    private AccountRiskTradingDays tradingDays;

    @JsonProperty("nearest_liquidation")
    private AccountRiskNearestLiquidation nearestLiquidation;

    @JsonProperty("trading_day_ends_at")
    private Long tradingDayEndsAt;

    @JsonProperty("warnings")
    private List<Warning> warnings;

    /** When the oldest figure here was read. */
    public Long asOf() {
        return asOf;
    }

    /** Sets as_of. */
    public AccountRisk asOf(Long value) {
        this.asOf = value;
        return this;
    }

    /** Some figures are older than usual (the engine was busy). */
    public Boolean stale() {
        return stale;
    }

    /** Sets stale. */
    public AccountRisk stale(Boolean value) {
        this.stale = value;
        return this;
    }

    /** The balance. */
    public BigDecimal balance() {
        return balance;
    }

    /** Sets balance. */
    public AccountRisk balance(BigDecimal value) {
        this.balance = value;
        return this;
    }

    /** The balance plus the open P&amp;L at the market price. */
    public BigDecimal equity() {
        return equity;
    }

    /** Sets equity. */
    public AccountRisk equity(BigDecimal value) {
        this.equity = value;
        return this;
    }

    /** The open P&amp;L. */
    public BigDecimal unrealizedPnl() {
        return unrealizedPnl;
    }

    /** Sets unrealized_pnl. */
    public AccountRisk unrealizedPnl(BigDecimal value) {
        this.unrealizedPnl = value;
        return this;
    }

    /** The equity change since midnight New York, open positions included: what the daily loss rule counts. */
    public BigDecimal pnlToday() {
        return pnlToday;
    }

    /** Sets pnl_today. */
    public AccountRisk pnlToday(BigDecimal value) {
        this.pnlToday = value;
        return this;
    }

    /** The P&amp;L closed today. */
    public BigDecimal realizedToday() {
        return realizedToday;
    }

    /** Sets realized_today. */
    public AccountRisk realizedToday(BigDecimal value) {
        this.realizedToday = value;
        return this;
    }

    /** The fees paid today. */
    public BigDecimal feesToday() {
        return feesToday;
    }

    /** Sets fees_today. */
    public AccountRisk feesToday(BigDecimal value) {
        this.feesToday = value;
        return this;
    }

    /** Trades closed today. */
    public Long tradesToday() {
        return tradesToday;
    }

    /** Sets trades_today. */
    public AccountRisk tradesToday(Long value) {
        this.tradesToday = value;
        return this;
    }

    /** The margin your positions hold. */
    public BigDecimal marginUsed() {
        return marginUsed;
    }

    /** Sets margin_used. */
    public AccountRisk marginUsed(BigDecimal value) {
        this.marginUsed = value;
        return this;
    }

    /** What is left of the balance for new positions. */
    public BigDecimal marginAvailable() {
        return marginAvailable;
    }

    /** Sets margin_available. */
    public AccountRisk marginAvailable(BigDecimal value) {
        this.marginAvailable = value;
        return this;
    }

    /** The exposure. */
    public AccountRiskExposure exposure() {
        return exposure;
    }

    /** Sets exposure. */
    public AccountRisk exposure(AccountRiskExposure value) {
        this.exposure = value;
        return this;
    }

    /** The daily loss. */
    public Limit dailyLoss() {
        return dailyLoss;
    }

    /** Sets daily_loss. */
    public AccountRisk dailyLoss(Limit value) {
        this.dailyLoss = value;
        return this;
    }

    /** The max drawdown. */
    public Limit maxDrawdown() {
        return maxDrawdown;
    }

    /** Sets max_drawdown. */
    public AccountRisk maxDrawdown(Limit value) {
        this.maxDrawdown = value;
        return this;
    }

    /** The profit target. */
    public AccountRiskProfitTarget profitTarget() {
        return profitTarget;
    }

    /** Sets profit_target. */
    public AccountRisk profitTarget(AccountRiskProfitTarget value) {
        this.profitTarget = value;
        return this;
    }

    /** The trading days. */
    public AccountRiskTradingDays tradingDays() {
        return tradingDays;
    }

    /** Sets trading_days. */
    public AccountRisk tradingDays(AccountRiskTradingDays value) {
        this.tradingDays = value;
        return this;
    }

    /** The nearest liquidation. */
    public AccountRiskNearestLiquidation nearestLiquidation() {
        return nearestLiquidation;
    }

    /** Sets nearest_liquidation. */
    public AccountRisk nearestLiquidation(AccountRiskNearestLiquidation value) {
        this.nearestLiquidation = value;
        return this;
    }

    /** When the trading day ends (midnight New York). */
    public Long tradingDayEndsAt() {
        return tradingDayEndsAt;
    }

    /** Sets trading_day_ends_at. */
    public AccountRisk tradingDayEndsAt(Long value) {
        this.tradingDayEndsAt = value;
        return this;
    }

    /** What needs your attention, most serious first. */
    public List<Warning> warnings() {
        return warnings;
    }

    /** Sets warnings. */
    public AccountRisk warnings(List<Warning> value) {
        this.warnings = value;
        return this;
    }

    @JsonIgnore
    private final Map<String, Object> extra = new LinkedHashMap<>();

    /** Fields this SDK version does not know yet (a newer API's), kept as they came. */
    @JsonAnyGetter
    public Map<String, Object> extra() {
        return extra;
    }

    @JsonAnySetter
    void putExtra(String key, Object value) {
        extra.put(key, value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AccountRisk)) {
            return false;
        }
        AccountRisk o = (AccountRisk) other;
        return Objects.equals(asOf, o.asOf)
            && Objects.equals(stale, o.stale)
            && Objects.equals(balance, o.balance)
            && Objects.equals(equity, o.equity)
            && Objects.equals(unrealizedPnl, o.unrealizedPnl)
            && Objects.equals(pnlToday, o.pnlToday)
            && Objects.equals(realizedToday, o.realizedToday)
            && Objects.equals(feesToday, o.feesToday)
            && Objects.equals(tradesToday, o.tradesToday)
            && Objects.equals(marginUsed, o.marginUsed)
            && Objects.equals(marginAvailable, o.marginAvailable)
            && Objects.equals(exposure, o.exposure)
            && Objects.equals(dailyLoss, o.dailyLoss)
            && Objects.equals(maxDrawdown, o.maxDrawdown)
            && Objects.equals(profitTarget, o.profitTarget)
            && Objects.equals(tradingDays, o.tradingDays)
            && Objects.equals(nearestLiquidation, o.nearestLiquidation)
            && Objects.equals(tradingDayEndsAt, o.tradingDayEndsAt)
            && Objects.equals(warnings, o.warnings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asOf, stale, balance, equity, unrealizedPnl, pnlToday, realizedToday, feesToday, tradesToday, marginUsed, marginAvailable, exposure, dailyLoss, maxDrawdown, profitTarget, tradingDays, nearestLiquidation, tradingDayEndsAt, warnings);
    }

    @Override
    public String toString() {
        return "AccountRisk{" + "as_of=" + asOf + ", stale=" + stale + ", balance=" + balance + ", equity=" + equity + ", unrealized_pnl=" + unrealizedPnl + ", pnl_today=" + pnlToday + ", realized_today=" + realizedToday + ", fees_today=" + feesToday + ", trades_today=" + tradesToday + ", margin_used=" + marginUsed + ", margin_available=" + marginAvailable + ", exposure=" + exposure + ", daily_loss=" + dailyLoss + ", max_drawdown=" + maxDrawdown + ", profit_target=" + profitTarget + ", trading_days=" + tradingDays + ", nearest_liquidation=" + nearestLiquidation + ", trading_day_ends_at=" + tradingDayEndsAt + ", warnings=" + warnings + "}";
    }
}
