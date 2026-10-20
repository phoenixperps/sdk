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

/** LimitsKeys */
public final class LimitsKeys {
    @JsonProperty("per_environment")
    private Long perEnvironment;

    @JsonProperty("ip_allowlist_max")
    private Long ipAllowlistMax;

    @JsonProperty("expiry_max_days")
    private Long expiryMaxDays;

    /** Keys you may hold in each environment. */
    public Long perEnvironment() {
        return perEnvironment;
    }

    /** Sets per_environment. */
    public LimitsKeys perEnvironment(Long value) {
        this.perEnvironment = value;
        return this;
    }

    /** Addresses in a key's allowlist. */
    public Long ipAllowlistMax() {
        return ipAllowlistMax;
    }

    /** Sets ip_allowlist_max. */
    public LimitsKeys ipAllowlistMax(Long value) {
        this.ipAllowlistMax = value;
        return this;
    }

    /** The longest a key may live, in days. */
    public Long expiryMaxDays() {
        return expiryMaxDays;
    }

    /** Sets expiry_max_days. */
    public LimitsKeys expiryMaxDays(Long value) {
        this.expiryMaxDays = value;
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
        if (!(other instanceof LimitsKeys)) {
            return false;
        }
        LimitsKeys o = (LimitsKeys) other;
        return Objects.equals(perEnvironment, o.perEnvironment)
            && Objects.equals(ipAllowlistMax, o.ipAllowlistMax)
            && Objects.equals(expiryMaxDays, o.expiryMaxDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(perEnvironment, ipAllowlistMax, expiryMaxDays);
    }

    @Override
    public String toString() {
        return "LimitsKeys{" + "per_environment=" + perEnvironment + ", ip_allowlist_max=" + ipAllowlistMax + ", expiry_max_days=" + expiryMaxDays + "}";
    }
}
