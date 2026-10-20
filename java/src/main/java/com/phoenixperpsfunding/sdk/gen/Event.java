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

/** Event */
public final class Event {
    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private EventType type;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("time")
    private Long time;

    @JsonProperty("source")
    private EventSource source;

    @JsonProperty("data")
    private Map<String, Object> data;

    /** The event; pass it as after (GET /v1/events) or resume (the stream) to continue from it. */
    public String id() {
        return id;
    }

    /** Sets id. */
    public Event id(String value) {
        this.id = value;
        return this;
    }

    /** What happened (GET /v1/webhook-event-types says what each type means). */
    public EventType type() {
        return type;
    }

    /** Sets type. */
    public Event type(EventType value) {
        this.type = value;
        return this;
    }

    /** The account number. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public Event accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** When it happened. */
    public Long time() {
        return time;
    }

    /** Sets time. */
    public Event time(Long value) {
        this.time = value;
        return this;
    }

    /** Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert. */
    public EventSource source() {
        return source;
    }

    /** Sets source. */
    public Event source(EventSource value) {
        this.source = value;
        return this;
    }

    /** order or fill (the objects REST answers), with changes or close_reason when they apply. A position.* event carries position { market, side, size, average_price } as the engine reported it, and previous_size: read GET .../positions/{market} for its margin, P&amp;L and liquidation. */
    public Map<String, Object> data() {
        return data;
    }

    /** Sets data. */
    public Event data(Map<String, Object> value) {
        this.data = value;
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
        if (!(other instanceof Event)) {
            return false;
        }
        Event o = (Event) other;
        return Objects.equals(id, o.id)
            && Objects.equals(type, o.type)
            && Objects.equals(accountId, o.accountId)
            && Objects.equals(time, o.time)
            && Objects.equals(source, o.source)
            && Objects.equals(data, o.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, accountId, time, source, data);
    }

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", type=" + type + ", account_id=" + accountId + ", time=" + time + ", source=" + source + ", data=" + data + "}";
    }
}
