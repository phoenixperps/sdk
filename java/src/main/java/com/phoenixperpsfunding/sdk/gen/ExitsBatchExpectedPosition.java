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

/** The position you saw: { side, size }, or null. */
public final class ExitsBatchExpectedPosition {
    @JsonProperty("side")
    private PositionSide side;

    @JsonProperty("size")
    private BigDecimal size;

    /** Long or short. */
    public PositionSide side() {
        return side;
    }

    /** Sets side. */
    public ExitsBatchExpectedPosition side(PositionSide value) {
        this.side = value;
        return this;
    }

    /** Its size. */
    public BigDecimal size() {
        return size;
    }

    /** Sets size. */
    public ExitsBatchExpectedPosition size(BigDecimal value) {
        this.size = value;
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
        if (!(other instanceof ExitsBatchExpectedPosition)) {
            return false;
        }
        ExitsBatchExpectedPosition o = (ExitsBatchExpectedPosition) other;
        return Objects.equals(side, o.side)
            && Objects.equals(size, o.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(side, size);
    }

    @Override
    public String toString() {
        return "ExitsBatchExpectedPosition{" + "side=" + side + ", size=" + size + "}";
    }
}
