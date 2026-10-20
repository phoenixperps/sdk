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

/** WhatIf */
public final class WhatIf {
    @JsonProperty("result")
    private WhatIfResult result;

    @JsonProperty("reject")
    private WhatIfReject reject;

    @JsonProperty("market")
    private String market;

    @JsonProperty("side")
    private OrderSide side;

    @JsonProperty("type")
    private CreateOrderType type;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("execution")
    private WhatIfExecution execution;

    @JsonProperty("notional")
    private BigDecimal notional;

    @JsonProperty("fee")
    private BigDecimal fee;

    @JsonProperty("margin")
    private WhatIfMargin margin;

    @JsonProperty("position_after")
    private WhatIfPositionAfter positionAfter;

    @JsonProperty("exposure")
    private WhatIfExposure exposure;

    /** What the order would do now: fill at once, rest, or be refused (reject says why). */
    public WhatIfResult result() {
        return result;
    }

    /** Sets result. */
    public WhatIf result(WhatIfResult value) {
        this.result = value;
        return this;
    }

    /** The reject. */
    public WhatIfReject reject() {
        return reject;
    }

    /** Sets reject. */
    public WhatIf reject(WhatIfReject value) {
        this.reject = value;
        return this;
    }

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public WhatIf market(String value) {
        this.market = value;
        return this;
    }

    /** The order's side. */
    public OrderSide side() {
        return side;
    }

    /** Sets side. */
    public WhatIf side(OrderSide value) {
        this.side = value;
        return this;
    }

    /** The order's type. */
    public CreateOrderType type() {
        return type;
    }

    /** Sets type. */
    public WhatIf type(CreateOrderType value) {
        this.type = value;
        return this;
    }

    /** Its size (after a notional_usd is rounded to the size step). */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public WhatIf size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** The execution. */
    public WhatIfExecution execution() {
        return execution;
    }

    /** Sets execution. */
    public WhatIf execution(WhatIfExecution value) {
        this.execution = value;
        return this;
    }

    /** Its value in USD. */
    public BigDecimal notional() {
        return notional;
    }

    /** Sets notional. */
    public WhatIf notional(BigDecimal value) {
        this.notional = value;
        return this;
    }

    /** The fee its fill would pay. */
    public BigDecimal fee() {
        return fee;
    }

    /** Sets fee. */
    public WhatIf fee(BigDecimal value) {
        this.fee = value;
        return this;
    }

    /** The margin. */
    public WhatIfMargin margin() {
        return margin;
    }

    /** Sets margin. */
    public WhatIf margin(WhatIfMargin value) {
        this.margin = value;
        return this;
    }

    /** The position after. */
    public WhatIfPositionAfter positionAfter() {
        return positionAfter;
    }

    /** Sets position_after. */
    public WhatIf positionAfter(WhatIfPositionAfter value) {
        this.positionAfter = value;
        return this;
    }

    /** The exposure. */
    public WhatIfExposure exposure() {
        return exposure;
    }

    /** Sets exposure. */
    public WhatIf exposure(WhatIfExposure value) {
        this.exposure = value;
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
        if (!(other instanceof WhatIf)) {
            return false;
        }
        WhatIf o = (WhatIf) other;
        return Objects.equals(result, o.result)
            && Objects.equals(reject, o.reject)
            && Objects.equals(market, o.market)
            && Objects.equals(side, o.side)
            && Objects.equals(type, o.type)
            && Objects.equals(size, o.size)
            && Objects.equals(execution, o.execution)
            && Objects.equals(notional, o.notional)
            && Objects.equals(fee, o.fee)
            && Objects.equals(margin, o.margin)
            && Objects.equals(positionAfter, o.positionAfter)
            && Objects.equals(exposure, o.exposure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, reject, market, side, type, size, execution, notional, fee, margin, positionAfter, exposure);
    }

    @Override
    public String toString() {
        return "WhatIf{" + "result=" + result + ", reject=" + reject + ", market=" + market + ", side=" + side + ", type=" + type + ", size=" + size + ", execution=" + execution + ", notional=" + notional + ", fee=" + fee + ", margin=" + margin + ", position_after=" + positionAfter + ", exposure=" + exposure + "}";
    }
}
