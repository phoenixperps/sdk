package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.databind.JsonNode;

/** A call a conformance scenario names (resource, method, path, body, query), and what a dispatch answers. */
final class Call {
    private final JsonNode e;

    Call(JsonNode e) {
        this.e = e;
    }

    String resource() {
        return e.get("resource").asText();
    }

    String method() {
        return e.get("method").asText();
    }

    boolean iterate() {
        return e.path("iterate").asBoolean(false);
    }

    Integer maxItems() {
        return e.has("max_items") ? e.get("max_items").asInt() : null;
    }

    String path(String name) {
        return e.get("path").get(name).asText();
    }

    <T> T body(Class<T> type) {
        try {
            return e.has("body") ? Json.MAPPER.treeToValue(e.get("body"), type) : null;
        } catch (java.io.IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    <T> T query(Class<T> type) {
        try {
            return e.has("query") ? Json.MAPPER.treeToValue(e.get("query"), type) : null;
        } catch (java.io.IOException x) {
            throw new IllegalArgumentException(x);
        }
    }
}

