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

/** RestrictedCountry */
public final class RestrictedCountry {
    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    /** The country, ISO 3166-1 alpha-2. */
    public String code() {
        return code;
    }

    /** Sets code. */
    public RestrictedCountry code(String value) {
        this.code = value;
        return this;
    }

    /** Its name in English. */
    public String name() {
        return name;
    }

    /** Sets name. */
    public RestrictedCountry name(String value) {
        this.name = value;
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
        if (!(other instanceof RestrictedCountry)) {
            return false;
        }
        RestrictedCountry o = (RestrictedCountry) other;
        return Objects.equals(code, o.code)
            && Objects.equals(name, o.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }

    @Override
    public String toString() {
        return "RestrictedCountry{" + "code=" + code + ", name=" + name + "}";
    }
}
