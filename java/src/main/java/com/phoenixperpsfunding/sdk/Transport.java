package com.phoenixperpsfunding.sdk;

import java.util.Map;
import java.util.function.Supplier;

/** What the generated resources call: one object, one page, every item. */
public interface Transport {
    /**
     * An operation of the API.
     *
     * @param id its operationId
     * @param method the HTTP method
     * @param path the path, with {parameters}
     * @param idempotent it takes an Idempotency-Key (and so may be tried again)
     * @param retrySafe it may be tried again as it is
     * @param cursor the query parameter of the next page (null: not paged)
     * @param scope its rate-limit budget (x-pp-scope: read, trade, public; null: not known)
     */
    record Operation(String id, String method, String path, boolean idempotent, boolean retrySafe, String cursor, String scope) {
        /** An operation whose budget is not known. */
        public Operation(String id, String method, String path, boolean idempotent, boolean retrySafe, String cursor) {
            this(id, method, path, idempotent, retrySafe, cursor, null);
        }
    }

    /** A call whose answer is one object. */
    <T> T one(Operation op, String[] path, Map<String, String> query, Object body, Class<T> type, RequestOptions options);

    /** A call whose answer is a page of a list. */
    <T> Page<T> list(Operation op, String[] path, Map<String, String> query, Class<T> type, RequestOptions options);

    /** Every item of a list, page after page. */
    <T> Iterable<T> autoPaging(Supplier<Page<T>> first, RequestOptions options);
}
