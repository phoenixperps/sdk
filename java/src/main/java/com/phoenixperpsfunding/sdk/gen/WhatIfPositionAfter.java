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

/** WhatIfPositionAfter */
public final class WhatIfPositionAfter {
    @JsonProperty("size")
    private BigDecimal size;

    @JsonProperty("side")
    private PositionSide side;

    @JsonProperty("average_price")
    private BigDecimal averagePrice;

    /** The position's size after the fill. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public WhatIfPositionAfter size(BigDecimal value) {
        this.size = value;
        return this;
    }

    /** Its side; null when flat. */
    public PositionSide side() {
        return side;
    }

    /** Sets side. */
    public WhatIfPositionAfter side(PositionSide value) {
        this.side = value;
        return this;
    }

    /** Its average price. */
    public BigDecimal averagePrice() {
        return averagePrice;
    }

    /** Sets average_price. */
    public WhatIfPositionAfter averagePrice(BigDecimal value) {
        this.averagePrice = value;
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
        if (!(other instanceof WhatIfPositionAfter)) {
            return false;
        }
        WhatIfPositionAfter o = (WhatIfPositionAfter) other;
        return Objects.equals(size, o.size)
            && Objects.equals(side, o.side)
            && Objects.equals(averagePrice, o.averagePrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, side, averagePrice);
    }

    @Override
    public String toString() {
        return "WhatIfPositionAfter{" + "size=" + size + ", side=" + side + ", average_price=" + averagePrice + "}";
    }
}
