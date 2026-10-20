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

/** WhatIfExposure */
public final class WhatIfExposure {
    @JsonProperty("cap")
    private BigDecimal cap;

    @JsonProperty("open_before")
    private BigDecimal openBefore;

    @JsonProperty("room_after")
    private BigDecimal roomAfter;

    /** The market's exposure cap across your accounts of this product. */
    public BigDecimal cap() {
        return cap;
    }

    /** Sets cap. */
    public WhatIfExposure cap(BigDecimal value) {
        this.cap = value;
        return this;
    }

    /** Your open value on the market now. */
    public BigDecimal openBefore() {
        return openBefore;
    }

    /** Sets open_before. */
    public WhatIfExposure openBefore(BigDecimal value) {
        this.openBefore = value;
        return this;
    }

    /** What would remain under the cap. */
    public BigDecimal roomAfter() {
        return roomAfter;
    }

    /** Sets room_after. */
    public WhatIfExposure roomAfter(BigDecimal value) {
        this.roomAfter = value;
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
        if (!(other instanceof WhatIfExposure)) {
            return false;
        }
        WhatIfExposure o = (WhatIfExposure) other;
        return Objects.equals(cap, o.cap)
            && Objects.equals(openBefore, o.openBefore)
            && Objects.equals(roomAfter, o.roomAfter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cap, openBefore, roomAfter);
    }

    @Override
    public String toString() {
        return "WhatIfExposure{" + "cap=" + cap + ", open_before=" + openBefore + ", room_after=" + roomAfter + "}";
    }
}
