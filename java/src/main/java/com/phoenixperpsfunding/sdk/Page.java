package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * One page of a list: its items, whether more follow, the cursor to them, and every other field of the list
 * (extra(): history_complete...).
 *
 * @param <T> the item
 */
public final class Page<T> {
    @JsonProperty("data")
    private List<T> data = new ArrayList<>();

    @JsonProperty("has_more")
    private boolean hasMore;

    @JsonProperty("next_cursor")
    private String nextCursor;

    @JsonProperty("as_of")
    private Long asOf;

    @JsonIgnore
    private final Map<String, Object> extra = new LinkedHashMap<>();

    @JsonIgnore
    private RawResponse raw;

    @JsonIgnore
    private Function<String, Page<T>> next;

    /** The items. */
    public List<T> data() {
        return data;
    }

    /** More items follow. */
    public boolean hasMore() {
        return hasMore;
    }

    /** The cursor to the next page. */
    public String nextCursor() {
        return nextCursor;
    }

    /** When the list was read (ms). */
    public Long asOf() {
        return asOf;
    }

    /** The list's other fields, as they came (history_complete: with past orders, false when the history reaches further back). */
    @JsonAnyGetter
    public Map<String, Object> extra() {
        return extra;
    }

    @JsonAnySetter
    void putExtra(String key, Object value) {
        extra.put(key, value);
    }

    /** The answer the page came in. */
    public RawResponse raw() {
        return raw;
    }

    /** The next page, or null after the last. */
    public Page<T> nextPage() {
        if (!hasMore || nextCursor == null || nextCursor.isEmpty() || next == null) {
            return null;
        }
        return next.apply(nextCursor);
    }

    void link(RawResponse raw, Function<String, Page<T>> next) {
        this.raw = raw;
        this.next = next;
    }
}
