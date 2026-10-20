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

/** Fill */
public final class Fill {
    @JsonProperty("id")
    private String id;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("market")
    private String market;

    @JsonProperty("side")
    private OrderSide side;

    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("fee")
    private BigDecimal fee;

    @JsonProperty("liquidation")
    private Boolean liquidation;

    @JsonProperty("time")
    private Long time;

    /** The fill. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public Fill id(String value) {
        this.id = value;
        return this;
    }

    /** Its order. */
    public String orderId() {
        return orderId;
    }

    /** Sets order_id. */
    public Fill orderId(String value) {
        this.orderId = value;
        return this;
    }

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public Fill market(String value) {
        this.market = value;
        return this;
    }

    /** The side; null for an order placed elsewhere before the API saw it. */
    public OrderSide side() {
        return side;
    }

    /** Sets side. */
    public Fill side(OrderSide value) {
        this.side = value;
        return this;
    }

    /** The size filled. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public Fill size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** The fill price. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public Fill price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** Its fee. */
    public BigDecimal fee() {
        return fee;
    }

    /** Sets fee. */
    public Fill fee(BigDecimal value) {
        this.fee = value;
        return this;
    }

    /** A liquidation filled it. */
    public Boolean liquidation() {
        return liquidation;
    }

    /** Sets liquidation. */
    public Fill liquidation(Boolean value) {
        this.liquidation = value;
        return this;
    }

    /** When it filled. */
    public Long time() {
        return time;
    }

    /** Sets time. */
    public Fill time(Long value) {
        this.time = value;
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
        if (!(other instanceof Fill)) {
            return false;
        }
        Fill o = (Fill) other;
        return Objects.equals(id, o.id)
            && Objects.equals(orderId, o.orderId)
            && Objects.equals(market, o.market)
            && Objects.equals(side, o.side)
            && Objects.equals(size, o.size)
            && Objects.equals(price, o.price)
            && Objects.equals(fee, o.fee)
            && Objects.equals(liquidation, o.liquidation)
            && Objects.equals(time, o.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, market, side, size, price, fee, liquidation, time);
    }

    @Override
    public String toString() {
        return "Fill{" + "id=" + id + ", order_id=" + orderId + ", market=" + market + ", side=" + side + ", size=" + size + ", price=" + price + ", fee=" + fee + ", liquidation=" + liquidation + ", time=" + time + "}";
    }
}
