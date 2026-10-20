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

/** ProgressPayout */
public final class ProgressPayout {
    @JsonProperty("eligible")
    private Boolean eligible;

    @JsonProperty("available")
    private BigDecimal available;

    @JsonProperty("min_payout")
    private BigDecimal minPayout;

    @JsonProperty("paid_out")
    private BigDecimal paidOut;

    /** A payout may be requested now. */
    public Boolean eligible() {
        return eligible;
    }

    /** Sets eligible. */
    public ProgressPayout eligible(Boolean value) {
        this.eligible = value;
        return this;
    }

    /** What may be paid out. */
    public BigDecimal available() {
        return available;
    }

    /** Sets available. */
    public ProgressPayout available(BigDecimal value) {
        this.available = value;
        return this;
    }

    /** The smallest payout. */
    public BigDecimal minPayout() {
        return minPayout;
    }

    /** Sets min_payout. */
    public ProgressPayout minPayout(BigDecimal value) {
        this.minPayout = value;
        return this;
    }

    /** Paid out so far. */
    public BigDecimal paidOut() {
        return paidOut;
    }

    /** Sets paid_out. */
    public ProgressPayout paidOut(BigDecimal value) {
        this.paidOut = value;
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
        if (!(other instanceof ProgressPayout)) {
            return false;
        }
        ProgressPayout o = (ProgressPayout) other;
        return Objects.equals(eligible, o.eligible)
            && Objects.equals(available, o.available)
            && Objects.equals(minPayout, o.minPayout)
            && Objects.equals(paidOut, o.paidOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eligible, available, minPayout, paidOut);
    }

    @Override
    public String toString() {
        return "ProgressPayout{" + "eligible=" + eligible + ", available=" + available + ", min_payout=" + minPayout + ", paid_out=" + paidOut + "}";
    }
}
