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

/** WebhookEndpoint */
public final class WebhookEndpoint {
    @JsonProperty("id")
    private String id;

    @JsonProperty("url")
    private String url;

    @JsonProperty("format")
    private WebhookEndpointFormat format;

    @JsonProperty("events")
    private List<WebhookEndpointEvent> events;

    @JsonProperty("status")
    private WebhookEndpointStatus status;

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("environment")
    private PlatformEnvironment environment;

    /** The endpoint. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public WebhookEndpoint id(String value) {
        this.id = value;
        return this;
    }

    /** Its address: the host and the start of the path (never whole: chat addresses carry tokens). */
    public String url() {
        return url;
    }

    /** Sets url. */
    public WebhookEndpoint url(String value) {
        this.url = value;
        return this;
    }

    /** json: the signed event. discord, slack, telegram: a chat message. */
    public WebhookEndpointFormat format() {
        return format;
    }

    /** Sets format. */
    public WebhookEndpoint format(WebhookEndpointFormat value) {
        this.format = value;
        return this;
    }

    /** The groups it receives (see GET /v1/webhook-event-types). */
    public List<WebhookEndpointEvent> events() {
        return events;
    }

    /** Sets events. */
    public WebhookEndpoint events(List<WebhookEndpointEvent> value) {
        this.events = value;
        return this;
    }

    /** disabled: we stopped after failures in a row; turn it on again from your dashboard. */
    public WebhookEndpointStatus status() {
        return status;
    }

    /** Sets status. */
    public WebhookEndpoint status(WebhookEndpointStatus value) {
        this.status = value;
        return this;
    }

    /** The version its events are written in. */
    public String apiVersion() {
        return apiVersion;
    }

    /** Sets api_version. */
    public WebhookEndpoint apiVersion(String value) {
        this.apiVersion = value;
        return this;
    }

    /** Its environment: it receives that environment's events only. */
    public PlatformEnvironment environment() {
        return environment;
    }

    /** Sets environment. */
    public WebhookEndpoint environment(PlatformEnvironment value) {
        this.environment = value;
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
        if (!(other instanceof WebhookEndpoint)) {
            return false;
        }
        WebhookEndpoint o = (WebhookEndpoint) other;
        return Objects.equals(id, o.id)
            && Objects.equals(url, o.url)
            && Objects.equals(format, o.format)
            && Objects.equals(events, o.events)
            && Objects.equals(status, o.status)
            && Objects.equals(apiVersion, o.apiVersion)
            && Objects.equals(environment, o.environment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, format, events, status, apiVersion, environment);
    }

    @Override
    public String toString() {
        return "WebhookEndpoint{" + "id=" + id + ", url=" + url + ", format=" + format + ", events=" + events + ", status=" + status + ", api_version=" + apiVersion + ", environment=" + environment + "}";
    }
}
