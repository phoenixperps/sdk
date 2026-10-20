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

/** The query of get the equity curve. */
public final class HistoryEquityParams {
    @JsonProperty("resolution")
    private Resolution resolution;

    /** One point per closed trade, or per trading day. */
    public Resolution resolution() {
        return resolution;
    }

    /** Sets resolution. */
    public HistoryEquityParams resolution(Resolution value) {
        this.resolution = value;
        return this;
    }

    /** The query string parameters. */
    public Map<String, String> toQuery() {
        Map<String, String> q = new LinkedHashMap<>();
        if (resolution != null) {
            q.put("resolution", String.valueOf(resolution));
        }
        return q;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof HistoryEquityParams)) {
            return false;
        }
        HistoryEquityParams o = (HistoryEquityParams) other;
        return Objects.equals(resolution, o.resolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resolution);
    }

    @Override
    public String toString() {
        return "HistoryEquityParams{" + "resolution=" + resolution + "}";
    }
}
