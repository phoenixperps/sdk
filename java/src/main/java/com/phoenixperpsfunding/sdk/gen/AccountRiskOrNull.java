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

/** The live risk; null while the account is being opened. */
public final class AccountRiskOrNull {
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
    private AccountRiskOrNullExposure exposure;

    @JsonProperty("daily_loss")
    private Limit dailyLoss;

    @JsonProperty("max_drawdown")
    private Limit maxDrawdown;

    @JsonProperty("profit_target")
    private AccountRiskOrNullProfitTarget profitTarget;

    @JsonProperty("trading_days")
    private AccountRiskOrNullTradingDays tradingDays;

    @JsonProperty("nearest_liquidation")
    private AccountRiskOrNullNearestLiquidation nearestLiquidation;

    @JsonProperty("trading_day_ends_at")
    private Long tradingDayEndsAt;

    @JsonProperty("warnings")
    private List<Warning> warnings;

    /** When the oldest figure here was read. */
    public Long asOf() {
        return asOf;
    }

    /** Sets as_of. */
    public AccountRiskOrNull asOf(Long value) {
        this.asOf = value;
        return this;
    }

    /** Some figures are older than usual (the engine was busy). */
    public Boolean stale() {
        return stale;
    }

    /** Sets stale. */
    public AccountRiskOrNull stale(Boolean value) {
        this.stale = value;
        return this;
    }

    /** The balance. */
    public BigDecimal balance() {
        return balance;
    }

    /** Sets balance. */
    public AccountRiskOrNull balance(BigDecimal value) {
        this.balance = value;
        return this;
    }

    /** The balance plus the open P&amp;L at the market price. */
    public BigDecimal equity() {
        return equity;
    }

    /** Sets equity. */
    public AccountRiskOrNull equity(BigDecimal value) {
        this.equity = value;
        return this;
    }

    /** The open P&amp;L. */
    public BigDecimal unrealizedPnl() {
        return unrealizedPnl;
    }

    /** Sets unrealized_pnl. */
    public AccountRiskOrNull unrealizedPnl(BigDecimal value) {
        this.unrealizedPnl = value;
        return this;
    }

    /** The equity change since midnight New York, open positions included: what the daily loss rule counts. */
    public BigDecimal pnlToday() {
        return pnlToday;
    }

    /** Sets pnl_today. */
    public AccountRiskOrNull pnlToday(BigDecimal value) {
        this.pnlToday = value;
        return this;
    }

    /** The P&amp;L closed today. */
    public BigDecimal realizedToday() {
        return realizedToday;
    }

    /** Sets realized_today. */
    public AccountRiskOrNull realizedToday(BigDecimal value) {
        this.realizedToday = value;
        return this;
    }

    /** The fees paid today. */
    public BigDecimal feesToday() {
        return feesToday;
    }

    /** Sets fees_today. */
    public AccountRiskOrNull feesToday(BigDecimal value) {
        this.feesToday = value;
        return this;
    }

    /** Trades closed today. */
    public Long tradesToday() {
        return tradesToday;
    }

    /** Sets trades_today. */
    public AccountRiskOrNull tradesToday(Long value) {
        this.tradesToday = value;
        return this;
    }

    /** The margin your positions hold. */
    public BigDecimal marginUsed() {
        return marginUsed;
    }

    /** Sets margin_used. */
    public AccountRiskOrNull marginUsed(BigDecimal value) {
        this.marginUsed = value;
        return this;
    }

    /** What is left of the balance for new positions. */
    public BigDecimal marginAvailable() {
        return marginAvailable;
    }

    /** Sets margin_available. */
    public AccountRiskOrNull marginAvailable(BigDecimal value) {
        this.marginAvailable = value;
        return this;
    }

    /** The exposure. */
    public AccountRiskOrNullExposure exposure() {
        return exposure;
    }

    /** Sets exposure. */
    public AccountRiskOrNull exposure(AccountRiskOrNullExposure value) {
        this.exposure = value;
        return this;
    }

    /** The daily loss. */
    public Limit dailyLoss() {
        return dailyLoss;
    }

    /** Sets daily_loss. */
    public AccountRiskOrNull dailyLoss(Limit value) {
        this.dailyLoss = value;
        return this;
    }

    /** The max drawdown. */
    public Limit maxDrawdown() {
        return maxDrawdown;
    }

    /** Sets max_drawdown. */
    public AccountRiskOrNull maxDrawdown(Limit value) {
        this.maxDrawdown = value;
        return this;
    }

    /** The profit target. */
    public AccountRiskOrNullProfitTarget profitTarget() {
        return profitTarget;
    }

    /** Sets profit_target. */
    public AccountRiskOrNull profitTarget(AccountRiskOrNullProfitTarget value) {
        this.profitTarget = value;
        return this;
    }

    /** The trading days. */
    public AccountRiskOrNullTradingDays tradingDays() {
        return tradingDays;
    }

    /** Sets trading_days. */
    public AccountRiskOrNull tradingDays(AccountRiskOrNullTradingDays value) {
        this.tradingDays = value;
        return this;
    }

    /** The nearest liquidation. */
    public AccountRiskOrNullNearestLiquidation nearestLiquidation() {
        return nearestLiquidation;
    }

    /** Sets nearest_liquidation. */
    public AccountRiskOrNull nearestLiquidation(AccountRiskOrNullNearestLiquidation value) {
        this.nearestLiquidation = value;
        return this;
    }

    /** When the trading day ends (midnight New York). */
    public Long tradingDayEndsAt() {
        return tradingDayEndsAt;
    }

    /** Sets trading_day_ends_at. */
    public AccountRiskOrNull tradingDayEndsAt(Long value) {
        this.tradingDayEndsAt = value;
        return this;
    }

    /** What needs your attention, most serious first. */
    public List<Warning> warnings() {
        return warnings;
    }

    /** Sets warnings. */
    public AccountRiskOrNull warnings(List<Warning> value) {
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
        if (!(other instanceof AccountRiskOrNull)) {
            return false;
        }
        AccountRiskOrNull o = (AccountRiskOrNull) other;
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
        return "AccountRiskOrNull{" + "as_of=" + asOf + ", stale=" + stale + ", balance=" + balance + ", equity=" + equity + ", unrealized_pnl=" + unrealizedPnl + ", pnl_today=" + pnlToday + ", realized_today=" + realizedToday + ", fees_today=" + feesToday + ", trades_today=" + tradesToday + ", margin_used=" + marginUsed + ", margin_available=" + marginAvailable + ", exposure=" + exposure + ", daily_loss=" + dailyLoss + ", max_drawdown=" + maxDrawdown + ", profit_target=" + profitTarget + ", trading_days=" + tradingDays + ", nearest_liquidation=" + nearestLiquidation + ", trading_day_ends_at=" + tradingDayEndsAt + ", warnings=" + warnings + "}";
    }
}
