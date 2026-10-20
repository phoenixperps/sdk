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

/** StatusEngine */
public final class StatusEngine {
    @JsonProperty("connected")
    private Boolean connected;

    @JsonProperty("started_at")
    private Long startedAt;

    /** Whether the API is connected to the trading engine. */
    public Boolean connected() {
        return connected;
    }

    /** Sets connected. */
    public StatusEngine connected(Boolean value) {
        this.connected = value;
        return this;
    }

    /** When the trading engine last started. */
    public Long startedAt() {
        return startedAt;
    }

    /** Sets started_at. */
    public StatusEngine startedAt(Long value) {
        this.startedAt = value;
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
        if (!(other instanceof StatusEngine)) {
            return false;
        }
        StatusEngine o = (StatusEngine) other;
        return Objects.equals(connected, o.connected)
            && Objects.equals(startedAt, o.startedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connected, startedAt);
    }

    @Override
    public String toString() {
        return "StatusEngine{" + "connected=" + connected + ", started_at=" + startedAt + "}";
    }
}
