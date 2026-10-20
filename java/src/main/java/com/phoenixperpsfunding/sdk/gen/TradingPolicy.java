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

/** TradingPolicy */
public final class TradingPolicy {
    @JsonProperty("rule")
    private Rule rule;

    @JsonProperty("trading_day")
    private TradingPolicyTradingDay tradingDay;

    @JsonProperty("slippage_pct")
    private TradingPolicySlippagePct slippagePct;

    @JsonProperty("margin")
    private TradingPolicyMargin margin;

    @JsonProperty("orders")
    private TradingPolicyOrders orders;

    @JsonProperty("controls")
    private TradingPolicyControls controls;

    @JsonProperty("budgets")
    private TradingPolicyBudgets budgets;

    @JsonProperty("as_of")
    private Long asOf;

    /** The rule. */
    public Rule rule() {
        return rule;
    }

    /** Sets rule. */
    public TradingPolicy rule(Rule value) {
        this.rule = value;
        return this;
    }

    /** The trading day. */
    public TradingPolicyTradingDay tradingDay() {
        return tradingDay;
    }

    /** Sets trading_day. */
    public TradingPolicy tradingDay(TradingPolicyTradingDay value) {
        this.tradingDay = value;
        return this;
    }

    /** The slippage pct. */
    public TradingPolicySlippagePct slippagePct() {
        return slippagePct;
    }

    /** Sets slippage_pct. */
    public TradingPolicy slippagePct(TradingPolicySlippagePct value) {
        this.slippagePct = value;
        return this;
    }

    /** The margin. */
    public TradingPolicyMargin margin() {
        return margin;
    }

    /** Sets margin. */
    public TradingPolicy margin(TradingPolicyMargin value) {
        this.margin = value;
        return this;
    }

    /** The orders. */
    public TradingPolicyOrders orders() {
        return orders;
    }

    /** Sets orders. */
    public TradingPolicy orders(TradingPolicyOrders value) {
        this.orders = value;
        return this;
    }

    /** The limits you set on the key. */
    public TradingPolicyControls controls() {
        return controls;
    }

    /** Sets controls. */
    public TradingPolicy controls(TradingPolicyControls value) {
        this.controls = value;
        return this;
    }

    /** The budgets. */
    public TradingPolicyBudgets budgets() {
        return budgets;
    }

    /** Sets budgets. */
    public TradingPolicy budgets(TradingPolicyBudgets value) {
        this.budgets = value;
        return this;
    }

    /** When the oldest figure here was read. */
    public Long asOf() {
        return asOf;
    }

    /** Sets as_of. */
    public TradingPolicy asOf(Long value) {
        this.asOf = value;
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
        if (!(other instanceof TradingPolicy)) {
            return false;
        }
        TradingPolicy o = (TradingPolicy) other;
        return Objects.equals(rule, o.rule)
            && Objects.equals(tradingDay, o.tradingDay)
            && Objects.equals(slippagePct, o.slippagePct)
            && Objects.equals(margin, o.margin)
            && Objects.equals(orders, o.orders)
            && Objects.equals(controls, o.controls)
            && Objects.equals(budgets, o.budgets)
            && Objects.equals(asOf, o.asOf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rule, tradingDay, slippagePct, margin, orders, controls, budgets, asOf);
    }

    @Override
    public String toString() {
        return "TradingPolicy{" + "rule=" + rule + ", trading_day=" + tradingDay + ", slippage_pct=" + slippagePct + ", margin=" + margin + ", orders=" + orders + ", controls=" + controls + ", budgets=" + budgets + ", as_of=" + asOf + "}";
    }
}
