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

/** Platform */
public final class Platform {
    @JsonProperty("environment")
    private PlatformEnvironment environment;

    @JsonProperty("version")
    private String version;

    @JsonProperty("server_time")
    private Long serverTime;

    @JsonProperty("docs_url")
    private String docsUrl;

    @JsonProperty("spec_url")
    private String specUrl;

    @JsonProperty("status")
    private PlatformStatus status;

    /** The environment of the host you called. */
    public PlatformEnvironment environment() {
        return environment;
    }

    /** Sets environment. */
    public Platform environment(PlatformEnvironment value) {
        this.environment = value;
        return this;
    }

    /** The version of this API. */
    public String version() {
        return version;
    }

    /** Sets version. */
    public Platform version(String value) {
        this.version = value;
        return this;
    }

    /** The time on the server. */
    public Long serverTime() {
        return serverTime;
    }

    /** Sets server_time. */
    public Platform serverTime(Long value) {
        this.serverTime = value;
        return this;
    }

    /** The documentation. */
    public String docsUrl() {
        return docsUrl;
    }

    /** Sets docs_url. */
    public Platform docsUrl(String value) {
        this.docsUrl = value;
        return this;
    }

    /** This API as OpenAPI 3.1. */
    public String specUrl() {
        return specUrl;
    }

    /** Sets spec_url. */
    public Platform specUrl(String value) {
        this.specUrl = value;
        return this;
    }

    /** The status. */
    public PlatformStatus status() {
        return status;
    }

    /** Sets status. */
    public Platform status(PlatformStatus value) {
        this.status = value;
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
        if (!(other instanceof Platform)) {
            return false;
        }
        Platform o = (Platform) other;
        return Objects.equals(environment, o.environment)
            && Objects.equals(version, o.version)
            && Objects.equals(serverTime, o.serverTime)
            && Objects.equals(docsUrl, o.docsUrl)
            && Objects.equals(specUrl, o.specUrl)
            && Objects.equals(status, o.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(environment, version, serverTime, docsUrl, specUrl, status);
    }

    @Override
    public String toString() {
        return "Platform{" + "environment=" + environment + ", version=" + version + ", server_time=" + serverTime + ", docs_url=" + docsUrl + ", spec_url=" + specUrl + ", status=" + status + "}";
    }
}
