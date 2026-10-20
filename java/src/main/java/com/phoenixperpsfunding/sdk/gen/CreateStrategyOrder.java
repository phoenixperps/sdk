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

/** CreateStrategyOrder */
public final class CreateStrategyOrder {
    @JsonProperty("side")
    private OrderSide side;

    @JsonProperty("type")
    private CreateStrategyOrderType type;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("limit_price")
    private BigDecimal limitPrice;

    @JsonProperty("stop_price")
    private BigDecimal stopPrice;

    @JsonProperty("post_only")
    private Boolean postOnly;

    @JsonProperty("reduce_only")
    private Boolean reduceOnly;

    /** The order's side. */
    public OrderSide side() {
        return side;
    }

    /** Sets side. */
    public CreateStrategyOrder side(OrderSide value) {
        this.side = value;
        return this;
    }

    /** Two orders that rest. */
    public CreateStrategyOrderType type() {
        return type;
    }

    /** Sets type. */
    public CreateStrategyOrder type(CreateStrategyOrderType value) {
        this.type = value;
        return this;
    }

    /** Its size. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public CreateStrategyOrder size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** Its limit price. */
    public BigDecimal limitPrice() {
        return limitPrice;
    }

    /** Sets limit_price. */
    public CreateStrategyOrder limitPrice(BigDecimal value) {
        this.limitPrice = value;
        return this;
    }

    /** Its stop price. */
    public BigDecimal stopPrice() {
        return stopPrice;
    }

    /** Sets stop_price. */
    public CreateStrategyOrder stopPrice(BigDecimal value) {
        this.stopPrice = value;
        return this;
    }

    /** Refused if it would fill at once. */
    public Boolean postOnly() {
        return postOnly;
    }

    /** Sets post_only. */
    public CreateStrategyOrder postOnly(Boolean value) {
        this.postOnly = value;
        return this;
    }

    /** It may only reduce your position. */
    public Boolean reduceOnly() {
        return reduceOnly;
    }

    /** Sets reduce_only. */
    public CreateStrategyOrder reduceOnly(Boolean value) {
        this.reduceOnly = value;
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
        if (!(other instanceof CreateStrategyOrder)) {
            return false;
        }
        CreateStrategyOrder o = (CreateStrategyOrder) other;
        return Objects.equals(side, o.side)
            && Objects.equals(type, o.type)
            && Objects.equals(size, o.size)
            && Objects.equals(limitPrice, o.limitPrice)
            && Objects.equals(stopPrice, o.stopPrice)
            && Objects.equals(postOnly, o.postOnly)
            && Objects.equals(reduceOnly, o.reduceOnly);
    }

    @Override
    public int hashCode() {
        return Objects.hash(side, type, size, limitPrice, stopPrice, postOnly, reduceOnly);
    }

    @Override
    public String toString() {
        return "CreateStrategyOrder{" + "side=" + side + ", type=" + type + ", size=" + size + ", limit_price=" + limitPrice + ", stop_price=" + stopPrice + ", post_only=" + postOnly + ", reduce_only=" + reduceOnly + "}";
    }
}
