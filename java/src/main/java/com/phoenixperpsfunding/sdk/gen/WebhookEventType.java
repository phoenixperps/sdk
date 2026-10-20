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

/** WebhookEventType */
public final class WebhookEventType {
    @JsonProperty("type")
    private String type;

    @JsonProperty("group")
    private WebhookEventTypeGroup group;

    @JsonProperty("description")
    private String description;

    /** The type. */
    public String type() {
        return type;
    }

    /** Sets type. */
    public WebhookEventType type(String value) {
        this.type = value;
        return this;
    }

    /** The group that receives it; null: every endpoint (the test). */
    public WebhookEventTypeGroup group() {
        return group;
    }

    /** Sets group. */
    public WebhookEventType group(WebhookEventTypeGroup value) {
        this.group = value;
        return this;
    }

    /** What it means. */
    public String description() {
        return description;
    }

    /** Sets description. */
    public WebhookEventType description(String value) {
        this.description = value;
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
        if (!(other instanceof WebhookEventType)) {
            return false;
        }
        WebhookEventType o = (WebhookEventType) other;
        return Objects.equals(type, o.type)
            && Objects.equals(group, o.group)
            && Objects.equals(description, o.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, group, description);
    }

    @Override
    public String toString() {
        return "WebhookEventType{" + "type=" + type + ", group=" + group + ", description=" + description + "}";
    }
}
