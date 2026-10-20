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

/** WhatIfMargin */
public final class WhatIfMargin {
    @JsonProperty("mode")
    private WhatIfMarginMode mode;

    @JsonProperty("leverage")
    private Long leverage;

    @JsonProperty("required")
    private BigDecimal required;

    @JsonProperty("available_before")
    private BigDecimal availableBefore;

    @JsonProperty("available_after")
    private BigDecimal availableAfter;

    /** The margin mode the position would use. */
    public WhatIfMarginMode mode() {
        return mode;
    }

    /** Sets mode. */
    public WhatIfMargin mode(WhatIfMarginMode value) {
        this.mode = value;
        return this;
    }

    /** The leverage it would use. */
    public Long leverage() {
        return leverage;
    }

    /** Sets leverage. */
    public WhatIfMargin leverage(Long value) {
        this.leverage = value;
        return this;
    }

    /** The margin the order needs. */
    public BigDecimal required() {
        return required;
    }

    /** Sets required. */
    public WhatIfMargin required(BigDecimal value) {
        this.required = value;
        return this;
    }

    /** The margin available now. */
    public BigDecimal availableBefore() {
        return availableBefore;
    }

    /** Sets available_before. */
    public WhatIfMargin availableBefore(BigDecimal value) {
        this.availableBefore = value;
        return this;
    }

    /** What would remain available. */
    public BigDecimal availableAfter() {
        return availableAfter;
    }

    /** Sets available_after. */
    public WhatIfMargin availableAfter(BigDecimal value) {
        this.availableAfter = value;
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
        if (!(other instanceof WhatIfMargin)) {
            return false;
        }
        WhatIfMargin o = (WhatIfMargin) other;
        return Objects.equals(mode, o.mode)
            && Objects.equals(leverage, o.leverage)
            && Objects.equals(required, o.required)
            && Objects.equals(availableBefore, o.availableBefore)
            && Objects.equals(availableAfter, o.availableAfter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, leverage, required, availableBefore, availableAfter);
    }

    @Override
    public String toString() {
        return "WhatIfMargin{" + "mode=" + mode + ", leverage=" + leverage + ", required=" + required + ", available_before=" + availableBefore + ", available_after=" + availableAfter + "}";
    }
}
