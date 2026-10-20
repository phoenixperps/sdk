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

/** The account's rule, as the trading engine enforces it. */
public final class Rule {
    @JsonProperty("start_balance")
    private BigDecimal startBalance;

    @JsonProperty("daily_loss")
    private RuleDailyLoss dailyLoss;

    @JsonProperty("max_drawdown")
    private RuleMaxDrawdown maxDrawdown;

    @JsonProperty("weekly_loss")
    private RuleWeeklyLoss weeklyLoss;

    @JsonProperty("profit_target")
    private RuleProfitTarget profitTarget;

    @JsonProperty("min_trading_days")
    private Long minTradingDays;

    @JsonProperty("min_calendar_days")
    private Long minCalendarDays;

    @JsonProperty("consistency")
    private RuleConsistency consistency;

    @JsonProperty("overnight_holding")
    private Boolean overnightHolding;

    @JsonProperty("weekend_holding")
    private Boolean weekendHolding;

    @JsonProperty("inactivity_days")
    private Long inactivityDays;

    /** The balance the account started with. */
    public BigDecimal startBalance() {
        return startBalance;
    }

    /** Sets start_balance. */
    public Rule startBalance(BigDecimal value) {
        this.startBalance = value;
        return this;
    }

    /** The daily loss. */
    public RuleDailyLoss dailyLoss() {
        return dailyLoss;
    }

    /** Sets daily_loss. */
    public Rule dailyLoss(RuleDailyLoss value) {
        this.dailyLoss = value;
        return this;
    }

    /** The max drawdown. */
    public RuleMaxDrawdown maxDrawdown() {
        return maxDrawdown;
    }

    /** Sets max_drawdown. */
    public Rule maxDrawdown(RuleMaxDrawdown value) {
        this.maxDrawdown = value;
        return this;
    }

    /** The weekly loss. */
    public RuleWeeklyLoss weeklyLoss() {
        return weeklyLoss;
    }

    /** Sets weekly_loss. */
    public Rule weeklyLoss(RuleWeeklyLoss value) {
        this.weeklyLoss = value;
        return this;
    }

    /** The profit target. */
    public RuleProfitTarget profitTarget() {
        return profitTarget;
    }

    /** Sets profit_target. */
    public Rule profitTarget(RuleProfitTarget value) {
        this.profitTarget = value;
        return this;
    }

    /** Days with a trade the account needs. */
    public Long minTradingDays() {
        return minTradingDays;
    }

    /** Sets min_trading_days. */
    public Rule minTradingDays(Long value) {
        this.minTradingDays = value;
        return this;
    }

    /** Calendar days the account needs. */
    public Long minCalendarDays() {
        return minCalendarDays;
    }

    /** Sets min_calendar_days. */
    public Rule minCalendarDays(Long value) {
        this.minCalendarDays = value;
        return this;
    }

    /** The consistency. */
    public RuleConsistency consistency() {
        return consistency;
    }

    /** Sets consistency. */
    public Rule consistency(RuleConsistency value) {
        this.consistency = value;
        return this;
    }

    /** Positions may stay open overnight. */
    public Boolean overnightHolding() {
        return overnightHolding;
    }

    /** Sets overnight_holding. */
    public Rule overnightHolding(Boolean value) {
        this.overnightHolding = value;
        return this;
    }

    /** Positions may stay open over the weekend. */
    public Boolean weekendHolding() {
        return weekendHolding;
    }

    /** Sets weekend_holding. */
    public Rule weekendHolding(Boolean value) {
        this.weekendHolding = value;
        return this;
    }

    /** Days without a trade after which the account fails. */
    public Long inactivityDays() {
        return inactivityDays;
    }

    /** Sets inactivity_days. */
    public Rule inactivityDays(Long value) {
        this.inactivityDays = value;
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
        if (!(other instanceof Rule)) {
            return false;
        }
        Rule o = (Rule) other;
        return Objects.equals(startBalance, o.startBalance)
            && Objects.equals(dailyLoss, o.dailyLoss)
            && Objects.equals(maxDrawdown, o.maxDrawdown)
            && Objects.equals(weeklyLoss, o.weeklyLoss)
            && Objects.equals(profitTarget, o.profitTarget)
            && Objects.equals(minTradingDays, o.minTradingDays)
            && Objects.equals(minCalendarDays, o.minCalendarDays)
            && Objects.equals(consistency, o.consistency)
            && Objects.equals(overnightHolding, o.overnightHolding)
            && Objects.equals(weekendHolding, o.weekendHolding)
            && Objects.equals(inactivityDays, o.inactivityDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startBalance, dailyLoss, maxDrawdown, weeklyLoss, profitTarget, minTradingDays, minCalendarDays, consistency, overnightHolding, weekendHolding, inactivityDays);
    }

    @Override
    public String toString() {
        return "Rule{" + "start_balance=" + startBalance + ", daily_loss=" + dailyLoss + ", max_drawdown=" + maxDrawdown + ", weekly_loss=" + weeklyLoss + ", profit_target=" + profitTarget + ", min_trading_days=" + minTradingDays + ", min_calendar_days=" + minCalendarDays + ", consistency=" + consistency + ", overnight_holding=" + overnightHolding + ", weekend_holding=" + weekendHolding + ", inactivity_days=" + inactivityDays + "}";
    }
}
