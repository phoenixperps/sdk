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

/** EquityHistory */
public final class EquityHistory {
    @JsonProperty("resolution")
    private Resolution resolution;

    @JsonProperty("points")
    private List<EquityHistoryPoint> points;

    @JsonProperty("live")
    private EquityHistoryLive live;

    /** One point per closed trade, or per trading day. */
    public Resolution resolution() {
        return resolution;
    }

    /** Sets resolution. */
    public EquityHistory resolution(Resolution value) {
        this.resolution = value;
        return this;
    }

    /** Oldest first; the first point is the opening balance. */
    public List<EquityHistoryPoint> points() {
        return points;
    }

    /** Sets points. */
    public EquityHistory points(List<EquityHistoryPoint> value) {
        this.points = value;
        return this;
    }

    /** The live. */
    public EquityHistoryLive live() {
        return live;
    }

    /** Sets live. */
    public EquityHistory live(EquityHistoryLive value) {
        this.live = value;
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
        if (!(other instanceof EquityHistory)) {
            return false;
        }
        EquityHistory o = (EquityHistory) other;
        return Objects.equals(resolution, o.resolution)
            && Objects.equals(points, o.points)
            && Objects.equals(live, o.live);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resolution, points, live);
    }

    @Override
    public String toString() {
        return "EquityHistory{" + "resolution=" + resolution + ", points=" + points + ", live=" + live + "}";
    }
}
