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

/** ExitLeg */
public final class ExitLeg {
    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("type")
    private OrderType type;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("size")
    private BigDecimal size;

    /** The working order. */
    public String orderId() {
        return orderId;
    }

    /** Sets order_id. */
    public ExitLeg orderId(String value) {
        this.orderId = value;
        return this;
    }

    /** The order's type. */
    public OrderType type() {
        return type;
    }

    /** Sets type. */
    public ExitLeg type(OrderType value) {
        this.type = value;
        return this;
    }

    /** Its trigger or limit price. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public ExitLeg price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** Its size. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public ExitLeg size(BigDecimal value) {
        this.size = value;
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
        if (!(other instanceof ExitLeg)) {
            return false;
        }
        ExitLeg o = (ExitLeg) other;
        return Objects.equals(orderId, o.orderId)
            && Objects.equals(type, o.type)
            && Objects.equals(price, o.price)
            && Objects.equals(size, o.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, type, price, size);
    }

    @Override
    public String toString() {
        return "ExitLeg{" + "order_id=" + orderId + ", type=" + type + ", price=" + price + ", size=" + size + "}";
    }
}
