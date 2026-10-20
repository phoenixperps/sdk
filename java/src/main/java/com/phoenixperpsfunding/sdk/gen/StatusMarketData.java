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

/** StatusMarketData */
public final class StatusMarketData {
    @JsonProperty("live")
    private Boolean live;

    @JsonProperty("checked_at")
    private Long checkedAt;

    /** Whether the market-data stream is live, with every price feed. */
    public Boolean live() {
        return live;
    }

    /** Sets live. */
    public StatusMarketData live(Boolean value) {
        this.live = value;
        return this;
    }

    /** When that was last checked. */
    public Long checkedAt() {
        return checkedAt;
    }

    /** Sets checked_at. */
    public StatusMarketData checkedAt(Long value) {
        this.checkedAt = value;
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
        if (!(other instanceof StatusMarketData)) {
            return false;
        }
        StatusMarketData o = (StatusMarketData) other;
        return Objects.equals(live, o.live)
            && Objects.equals(checkedAt, o.checkedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(live, checkedAt);
    }

    @Override
    public String toString() {
        return "StatusMarketData{" + "live=" + live + ", checked_at=" + checkedAt + "}";
    }
}
