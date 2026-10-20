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

/** The query of list events. */
public final class EventsListParams {
    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("after")
    private String after;

    @JsonProperty("limit")
    private Long limit;

    /** Only this account. */
    public String accountId() {
        return accountId;
    }

    /** Sets account_id. */
    public EventsListParams accountId(String value) {
        this.accountId = value;
        return this;
    }

    /** The id of the last event you have: the ones after it, oldest first. Absent: from the oldest kept. */
    public String after() {
        return after;
    }

    /** Sets after. */
    public EventsListParams after(String value) {
        this.after = value;
        return this;
    }

    /** Page size. */
    public Long limit() {
        return limit;
    }

    /** Sets limit. */
    public EventsListParams limit(Long value) {
        this.limit = value;
        return this;
    }

    /** The query string parameters. */
    public Map<String, String> toQuery() {
        Map<String, String> q = new LinkedHashMap<>();
        if (accountId != null) {
            q.put("account_id", String.valueOf(accountId));
        }
        if (after != null) {
            q.put("after", String.valueOf(after));
        }
        if (limit != null) {
            q.put("limit", String.valueOf(limit));
        }
        return q;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EventsListParams)) {
            return false;
        }
        EventsListParams o = (EventsListParams) other;
        return Objects.equals(accountId, o.accountId)
            && Objects.equals(after, o.after)
            && Objects.equals(limit, o.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, after, limit);
    }

    @Override
    public String toString() {
        return "EventsListParams{" + "account_id=" + accountId + ", after=" + after + ", limit=" + limit + "}";
    }
}
