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

/** PositionLiquidation */
public final class PositionLiquidation {
    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("distance_pct")
    private BigDecimal distancePct;

    @JsonProperty("account_wide")
    private Boolean accountWide;

    /** The price at which it is liquidated; null when there is none or it depends on several positions. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public PositionLiquidation price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** How far the market price is from it, in percent. */
    public BigDecimal distancePct() {
        return distancePct;
    }

    /** Sets distance_pct. */
    public PositionLiquidation distancePct(BigDecimal value) {
        this.distancePct = value;
        return this;
    }

    /** Cross margin on several markets: no single price, every one of them moves it. */
    public Boolean accountWide() {
        return accountWide;
    }

    /** Sets account_wide. */
    public PositionLiquidation accountWide(Boolean value) {
        this.accountWide = value;
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
        if (!(other instanceof PositionLiquidation)) {
            return false;
        }
        PositionLiquidation o = (PositionLiquidation) other;
        return Objects.equals(price, o.price)
            && Objects.equals(distancePct, o.distancePct)
            && Objects.equals(accountWide, o.accountWide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, distancePct, accountWide);
    }

    @Override
    public String toString() {
        return "PositionLiquidation{" + "price=" + price + ", distance_pct=" + distancePct + ", account_wide=" + accountWide + "}";
    }
}
