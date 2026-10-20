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

/** WhatIfExecution */
public final class WhatIfExecution {
    @JsonProperty("marketable")
    private Boolean marketable;

    @JsonProperty("expected_price")
    private BigDecimal expectedPrice;

    @JsonProperty("worst_price")
    private BigDecimal worstPrice;

    /** It would fill at once. */
    public Boolean marketable() {
        return marketable;
    }

    /** Sets marketable. */
    public WhatIfExecution marketable(Boolean value) {
        this.marketable = value;
        return this;
    }

    /** The price it would fill at (at once) or rest at. */
    public BigDecimal expectedPrice() {
        return expectedPrice;
    }

    /** Sets expected_price. */
    public WhatIfExecution expectedPrice(BigDecimal value) {
        this.expectedPrice = value;
        return this;
    }

    /** A market order's worst price. */
    public BigDecimal worstPrice() {
        return worstPrice;
    }

    /** Sets worst_price. */
    public WhatIfExecution worstPrice(BigDecimal value) {
        this.worstPrice = value;
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
        if (!(other instanceof WhatIfExecution)) {
            return false;
        }
        WhatIfExecution o = (WhatIfExecution) other;
        return Objects.equals(marketable, o.marketable)
            && Objects.equals(expectedPrice, o.expectedPrice)
            && Objects.equals(worstPrice, o.worstPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marketable, expectedPrice, worstPrice);
    }

    @Override
    public String toString() {
        return "WhatIfExecution{" + "marketable=" + marketable + ", expected_price=" + expectedPrice + ", worst_price=" + worstPrice + "}";
    }
}
