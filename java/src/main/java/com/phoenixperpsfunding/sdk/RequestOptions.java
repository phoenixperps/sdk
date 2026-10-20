package com.phoenixperpsfunding.sdk;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The options of one call: its Idempotency-Key (else a new UUID, the same on every attempt), the time one attempt may
 * take, how often it is tried again when that is safe, a cap on an AutoPaging iteration, extra headers, and a hook that
 * receives the answer as it came.
 */
public final class RequestOptions {
    final String idempotencyKey;
    final Duration timeout;
    final Integer maxRetries;
    final Integer maxItems;
    final Map<String, String> headers;
    final Consumer<RawResponse> onResponse;

    private RequestOptions(String idempotencyKey, Duration timeout, Integer maxRetries, Integer maxItems, Map<String, String> headers, Consumer<RawResponse> onResponse) {
        this.idempotencyKey = idempotencyKey;
        this.timeout = timeout;
        this.maxRetries = maxRetries;
        this.maxItems = maxItems;
        this.headers = headers;
        this.onResponse = onResponse;
    }

    /** No option set. */
    public static RequestOptions none() {
        return new RequestOptions(null, null, null, null, null, null);
    }

    /** With this Idempotency-Key. */
    public RequestOptions withIdempotencyKey(String key) {
        return new RequestOptions(key, timeout, maxRetries, maxItems, headers, onResponse);
    }

    /** With this time per attempt. */
    public RequestOptions withTimeout(Duration d) {
        return new RequestOptions(idempotencyKey, d, maxRetries, maxItems, headers, onResponse);
    }

    /** With this many retries at most. */
    public RequestOptions withMaxRetries(Integer n) {
        return new RequestOptions(idempotencyKey, timeout, n, maxItems, headers, onResponse);
    }

    /** With an AutoPaging iteration capped at n items (null or 0: every item). */
    public RequestOptions withMaxItems(Integer n) {
        return new RequestOptions(idempotencyKey, timeout, maxRetries, n, headers, onResponse);
    }

    /** With these extra headers. */
    public RequestOptions withHeaders(Map<String, String> h) {
        return new RequestOptions(idempotencyKey, timeout, maxRetries, maxItems, Map.copyOf(h), onResponse);
    }

    /** With a hook that receives the answer as it came. */
    public RequestOptions withOnResponse(Consumer<RawResponse> hook) {
        return new RequestOptions(idempotencyKey, timeout, maxRetries, maxItems, headers, hook);
    }
}
