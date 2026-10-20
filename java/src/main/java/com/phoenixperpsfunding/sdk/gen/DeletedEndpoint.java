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

/** DeletedEndpoint */
public final class DeletedEndpoint {
    @JsonProperty("id")
    private String id;

    @JsonProperty("deleted")
    private Boolean deleted;

    /** The endpoint. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public DeletedEndpoint id(String value) {
        this.id = value;
        return this;
    }

    /** It was deleted. */
    public Boolean deleted() {
        return deleted;
    }

    /** Sets deleted. */
    public DeletedEndpoint deleted(Boolean value) {
        this.deleted = value;
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
        if (!(other instanceof DeletedEndpoint)) {
            return false;
        }
        DeletedEndpoint o = (DeletedEndpoint) other;
        return Objects.equals(id, o.id)
            && Objects.equals(deleted, o.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deleted);
    }

    @Override
    public String toString() {
        return "DeletedEndpoint{" + "id=" + id + ", deleted=" + deleted + "}";
    }
}
