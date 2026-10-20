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

/** AccountRiskOrNullNearestLiquidation */
public final class AccountRiskOrNullNearestLiquidation {
    @JsonProperty("market")
    private String market;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("distance_pct")
    private BigDecimal distancePct;

    /** A market id: the asset and USD. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public AccountRiskOrNullNearestLiquidation market(String value) {
        this.market = value;
        return this;
    }

    /** Its liquidation price. */
    public BigDecimal price() {
        return price;
    }

    /** Sets price. */
    public AccountRiskOrNullNearestLiquidation price(BigDecimal value) {
        this.price = value;
        return this;
    }

    /** How far the price is from it, in percent. */
    public BigDecimal distancePct() {
        return distancePct;
    }

    /** Sets distance_pct. */
    public AccountRiskOrNullNearestLiquidation distancePct(BigDecimal value) {
        this.distancePct = value;
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
        if (!(other instanceof AccountRiskOrNullNearestLiquidation)) {
            return false;
        }
        AccountRiskOrNullNearestLiquidation o = (AccountRiskOrNullNearestLiquidation) other;
        return Objects.equals(market, o.market)
            && Objects.equals(price, o.price)
            && Objects.equals(distancePct, o.distancePct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, price, distancePct);
    }

    @Override
    public String toString() {
        return "AccountRiskOrNullNearestLiquidation{" + "market=" + market + ", price=" + price + ", distance_pct=" + distancePct + "}";
    }
}
