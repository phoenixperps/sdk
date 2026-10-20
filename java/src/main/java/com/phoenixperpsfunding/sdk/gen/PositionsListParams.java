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

/** The query of list open positions. */
public final class PositionsListParams {
    @JsonProperty("market")
    private String market;

    /** Only this market. */
    public String market() {
        return market;
    }

    /** Sets market. */
    public PositionsListParams market(String value) {
        this.market = value;
        return this;
    }

    /** The query string parameters. */
    public Map<String, String> toQuery() {
        Map<String, String> q = new LinkedHashMap<>();
        if (market != null) {
            q.put("market", String.valueOf(market));
        }
        return q;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PositionsListParams)) {
            return false;
        }
        PositionsListParams o = (PositionsListParams) other;
        return Objects.equals(market, o.market);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market);
    }

    @Override
    public String toString() {
        return "PositionsListParams{" + "market=" + market + "}";
    }
}
