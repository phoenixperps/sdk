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

/** What a json webhook endpoint receives: the event as GET /v1/events shows it, with the endpoint's API version and environment. */
public final class WebhookPayload {
    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private WebhookPayloadType type;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("time")
    private Long time;

    @JsonProperty("source")
    private EventSource source;

    @JsonProperty("data")
    private Map<String, Object> data;

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("environment")
    private PlatformEnvironment environment;

    @JsonProperty("test")
    private Boolean test;

    /** The event; pass it as after (GET /v1/events) or resume (the stream) to continue from it. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public WebhookPayload id(String value) {
        this.id = value;
        return this;
    }

    /** What happened; webhook.ping is a test. */
    public WebhookPayloadType type() {
        return type;
    }

    /** Sets type. */
    public WebhookPayload type(WebhookPayloadType value) {
        this.type = value;
        return this;
    }

    /** The account number (none on webhook.ping). */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public WebhookPayload accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** When it happened. */
    public Long time() {
        return time;
    }

    /** Sets time. */
    public WebhookPayload time(Long value) {
        this.time = value;
        return this;
    }

    /** Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert. */
    public EventSource source() {
        return source;
    }

    /** Sets source. */
    public WebhookPayload source(EventSource value) {
        this.source = value;
        return this;
    }

    /** order or fill (the objects REST answers), with changes or close_reason when they apply. A position.* event carries position { market, side, size, average_price } as the engine reported it, and previous_size: read GET .../positions/{market} for its margin, P&amp;L and liquidation. */
    public Map<String, Object> data() {
        return data;
    }

    /** Sets data. */
    public WebhookPayload data(Map<String, Object> value) {
        this.data = value;
        return this;
    }

    /** The API version the endpoint receives. */
    public String apiVersion() {
        return apiVersion;
    }

    /** Sets api_version. */
    public WebhookPayload apiVersion(String value) {
        this.apiVersion = value;
        return this;
    }

    /** The environment of the endpoint and the account. */
    public PlatformEnvironment environment() {
        return environment;
    }

    /** Sets environment. */
    public WebhookPayload environment(PlatformEnvironment value) {
        this.environment = value;
        return this;
    }

    /** A test (webhook.ping) or a delivery resent as one. */
    public Boolean test() {
        return test;
    }

    /** Sets test. */
    public WebhookPayload test(Boolean value) {
        this.test = value;
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
        if (!(other instanceof WebhookPayload)) {
            return false;
        }
        WebhookPayload o = (WebhookPayload) other;
        return Objects.equals(id, o.id)
            && Objects.equals(type, o.type)
            && Objects.equals(accountId, o.accountId)
            && Objects.equals(time, o.time)
            && Objects.equals(source, o.source)
            && Objects.equals(data, o.data)
            && Objects.equals(apiVersion, o.apiVersion)
            && Objects.equals(environment, o.environment)
            && Objects.equals(test, o.test);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, accountId, time, source, data, apiVersion, environment, test);
    }

    @Override
    public String toString() {
        return "WebhookPayload{" + "id=" + id + ", type=" + type + ", account_id=" + accountId + ", time=" + time + ", source=" + source + ", data=" + data + ", api_version=" + apiVersion + ", environment=" + environment + ", test=" + test + "}";
    }
}
