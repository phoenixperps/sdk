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

/** StatusAccountData */
public final class StatusAccountData {
    @JsonProperty("synced_at")
    private Long syncedAt;

    /** When your keys and accounts were last confirmed with the website. */
    public Long syncedAt() {
        return syncedAt;
    }

    /** Sets synced_at. */
    public StatusAccountData syncedAt(Long value) {
        this.syncedAt = value;
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
        if (!(other instanceof StatusAccountData)) {
            return false;
        }
        StatusAccountData o = (StatusAccountData) other;
        return Objects.equals(syncedAt, o.syncedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(syncedAt);
    }

    @Override
    public String toString() {
        return "StatusAccountData{" + "synced_at=" + syncedAt + "}";
    }
}
