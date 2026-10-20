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

/** WhatIfReject */
public final class WhatIfReject {
    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("details")
    private JsonNode details;

    /** The error code it would get. */
    public String code() {
        return code;
    }

    /** Sets code. */
    public WhatIfReject code(String value) {
        this.code = value;
        return this;
    }

    /** What it means. */
    public String message() {
        return message;
    }

    /** Sets message. */
    public WhatIfReject message(String value) {
        this.message = value;
        return this;
    }

    /** More about it. */
    public JsonNode details() {
        return details;
    }

    /** Sets details. */
    public WhatIfReject details(JsonNode value) {
        this.details = value;
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
        if (!(other instanceof WhatIfReject)) {
            return false;
        }
        WhatIfReject o = (WhatIfReject) other;
        return Objects.equals(code, o.code)
            && Objects.equals(message, o.message)
            && Objects.equals(details, o.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, details);
    }

    @Override
    public String toString() {
        return "WhatIfReject{" + "code=" + code + ", message=" + message + ", details=" + details + "}";
    }
}
