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

/** What to change: at least one field. The order keeps its place in the engine (changed in place, never canceled and placed again). */
public final class ModifyOrder {
    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("limit_price")
    private BigDecimal limitPrice;

    @JsonProperty("stop_price")
    private BigDecimal stopPrice;

    /** The new size. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public ModifyOrder size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** The new limit price. */
    public BigDecimal limitPrice() {
        return limitPrice;
    }

    /** Sets limit_price. */
    public ModifyOrder limitPrice(BigDecimal value) {
        this.limitPrice = value;
        return this;
    }

    /** The new trigger price. */
    public BigDecimal stopPrice() {
        return stopPrice;
    }

    /** Sets stop_price. */
    public ModifyOrder stopPrice(BigDecimal value) {
        this.stopPrice = value;
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
        if (!(other instanceof ModifyOrder)) {
            return false;
        }
        ModifyOrder o = (ModifyOrder) other;
        return Objects.equals(size, o.size)
            && Objects.equals(limitPrice, o.limitPrice)
            && Objects.equals(stopPrice, o.stopPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, limitPrice, stopPrice);
    }

    @Override
    public String toString() {
        return "ModifyOrder{" + "size=" + size + ", limit_price=" + limitPrice + ", stop_price=" + stopPrice + "}";
    }
}
