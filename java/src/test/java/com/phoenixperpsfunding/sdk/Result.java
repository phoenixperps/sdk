package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/** What a dispatch answers: the result as JSON (null: nothing), the ids of the items iterated, a page's other fields. */
record Result(JsonNode result, List<String> items, JsonNode pageFields) {
    static Result of(Object value) {
        if (value instanceof Page<?> p) {
            return new Result(Json.MAPPER.valueToTree(p.data()), null, Json.MAPPER.valueToTree(p.extra()));
        }
        return new Result(value == null ? null : Json.MAPPER.valueToTree(value), null, null);
    }

    static Result items(List<String> ids) {
        return new Result(null, ids, null);
    }
}
