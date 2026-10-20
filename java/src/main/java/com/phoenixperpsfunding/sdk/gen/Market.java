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

/** Market */
public final class Market {
    @JsonProperty("market")
    private String market;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private MarketStatus status;

    @JsonProperty("tick_size")
    private BigDecimal tickSize;

    @JsonProperty("size_step")
    private BigDecimal sizeStep;

    @JsonProperty("min_size")
    private BigDecimal minSize;

    @JsonProperty("min_notional")
    private BigDecimal minNotional;

    @JsonProperty("max_leverage")
    private Long maxLeverage;

    @JsonProperty("fee_rate_pct")
    private BigDecimal feeRatePct;

    @JsonProperty("exposure_cap")
    private BigDecimal exposureCap;

    @JsonProperty("max_slippage_pct")
    private MarketMaxSlippagePct maxSlippagePct;

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public Market market(String value) {
        this.market = value;
        return this;
    }

    /** The market's name; for a currency, the pair its price is quoted in (JPY-USD: USD/JPY, yen per dollar). */
    public String name() {
        return name;
    }

    /** Sets name. */
    public Market name(String value) {
        this.name = value;
        return this;
    }

    /** Whether the market trades now. */
    public MarketStatus status() {
        return status;
    }

    /** Sets status. */
    public Market status(MarketStatus value) {
        this.status = value;
        return this;
    }

    /** Prices are multiples of this. */
    public BigDecimal tickSize() {
        return tickSize;
    }

    /** Sets tick_size. */
    public Market tickSize(BigDecimal value) {
        this.tickSize = value;
        return this;
    }

    /** Sizes are multiples of this, in the base asset. */
    public BigDecimal sizeStep() {
        return sizeStep;
    }

    /** Sets size_step. */
    public Market sizeStep(BigDecimal value) {
        this.sizeStep = value;
        return this;
    }

    /** The smallest order size (the size step). An order must also be worth at least min_notional. */
    public BigDecimal minSize() {
        return minSize;
    }

    /** Sets min_size. */
    public Market minSize(BigDecimal value) {
        this.minSize = value;
        return this;
    }

    /** The smallest order value in USD: an order worth less is refused order_too_small. */
    public BigDecimal minNotional() {
        return minNotional;
    }

    /** Sets min_notional. */
    public Market minNotional(BigDecimal value) {
        this.minNotional = value;
        return this;
    }

    /** The highest leverage on this market. */
    public Long maxLeverage() {
        return maxLeverage;
    }

    /** Sets max_leverage. */
    public Market maxLeverage(Long value) {
        this.maxLeverage = value;
        return this;
    }

    /** The fee on each fill, in percent of its value. */
    public BigDecimal feeRatePct() {
        return feeRatePct;
    }

    /** Sets fee_rate_pct. */
    public Market feeRatePct(BigDecimal value) {
        this.feeRatePct = value;
        return this;
    }

    /** The most you may hold on this market across your accounts on the same rule, in USD. */
    public BigDecimal exposureCap() {
        return exposureCap;
    }

    /** Sets exposure_cap. */
    public Market exposureCap(BigDecimal value) {
        this.exposureCap = value;
        return this;
    }

    /** How far from the price a market order may fill, in percent. */
    public MarketMaxSlippagePct maxSlippagePct() {
        return maxSlippagePct;
    }

    /** Sets max_slippage_pct. */
    public Market maxSlippagePct(MarketMaxSlippagePct value) {
        this.maxSlippagePct = value;
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
        if (!(other instanceof Market)) {
            return false;
        }
        Market o = (Market) other;
        return Objects.equals(market, o.market)
            && Objects.equals(name, o.name)
            && Objects.equals(status, o.status)
            && Objects.equals(tickSize, o.tickSize)
            && Objects.equals(sizeStep, o.sizeStep)
            && Objects.equals(minSize, o.minSize)
            && Objects.equals(minNotional, o.minNotional)
            && Objects.equals(maxLeverage, o.maxLeverage)
            && Objects.equals(feeRatePct, o.feeRatePct)
            && Objects.equals(exposureCap, o.exposureCap)
            && Objects.equals(maxSlippagePct, o.maxSlippagePct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, name, status, tickSize, sizeStep, minSize, minNotional, maxLeverage, feeRatePct, exposureCap, maxSlippagePct);
    }

    @Override
    public String toString() {
        return "Market{" + "market=" + market + ", name=" + name + ", status=" + status + ", tick_size=" + tickSize + ", size_step=" + sizeStep + ", min_size=" + minSize + ", min_notional=" + minNotional + ", max_leverage=" + maxLeverage + ", fee_rate_pct=" + feeRatePct + ", exposure_cap=" + exposureCap + ", max_slippage_pct=" + maxSlippagePct + "}";
    }
}
