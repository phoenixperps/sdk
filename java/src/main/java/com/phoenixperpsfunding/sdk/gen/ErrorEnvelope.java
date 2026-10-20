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

/** Every error has this shape. */
public final class ErrorEnvelope {
    @JsonProperty("error")
    private ErrorEnvelopeError error;

    /** The error. */
    public ErrorEnvelopeError error() {
        return error;
    }

    /** Sets error. */
    public ErrorEnvelope error(ErrorEnvelopeError value) {
        this.error = value;
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
        if (!(other instanceof ErrorEnvelope)) {
            return false;
        }
        ErrorEnvelope o = (ErrorEnvelope) other;
        return Objects.equals(error, o.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error);
    }

    @Override
    public String toString() {
        return "ErrorEnvelope{" + "error=" + error + "}";
    }
}
