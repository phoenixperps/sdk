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

/** TradingPolicyTradingDay */
public final class TradingPolicyTradingDay {
    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("ends_at")
    private Long endsAt;

    /** The trading day runs midnight to midnight in this zone. */
    public String timezone() {
        return timezone;
    }

    /** Sets timezone. */
    public TradingPolicyTradingDay timezone(String value) {
        this.timezone = value;
        return this;
    }

    /** When this one ends. */
    public Long endsAt() {
        return endsAt;
    }

    /** Sets ends_at. */
    public TradingPolicyTradingDay endsAt(Long value) {
        this.endsAt = value;
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
        if (!(other instanceof TradingPolicyTradingDay)) {
            return false;
        }
        TradingPolicyTradingDay o = (TradingPolicyTradingDay) other;
        return Objects.equals(timezone, o.timezone)
            && Objects.equals(endsAt, o.endsAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timezone, endsAt);
    }

    @Override
    public String toString() {
        return "TradingPolicyTradingDay{" + "timezone=" + timezone + ", ends_at=" + endsAt + "}";
    }
}
