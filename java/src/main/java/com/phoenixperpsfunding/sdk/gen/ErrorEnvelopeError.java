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

/** ErrorEnvelopeError */
public final class ErrorEnvelopeError {
    @JsonProperty("type")
    private ErrorEnvelopeErrorType type;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("retryable")
    private Boolean retryable;

    @JsonProperty("doc_url")
    private String docUrl;

    @JsonProperty("details")
    private JsonNode details;

    @JsonProperty("order_id")
    private String orderId;

    /** The family of the error; it decides the HTTP status. */
    public ErrorEnvelopeErrorType type() {
        return type;
    }

    /** Sets type. */
    public ErrorEnvelopeError type(ErrorEnvelopeErrorType value) {
        this.type = value;
        return this;
    }

    /** The error code: match on this. */
    public String code() {
        return code;
    }

    /** Sets code. */
    public ErrorEnvelopeError code(String value) {
        this.code = value;
        return this;
    }

    /** A sentence for people. It may change; never match on it. */
    public String message() {
        return message;
    }

    /** Sets message. */
    public ErrorEnvelopeError message(String value) {
        this.message = value;
        return this;
    }

    /** The id of the request; give it to support. */
    public String requestId() {
        return requestId;
    }

    /** Sets request_id. */
    public ErrorEnvelopeError requestId(String value) {
        this.requestId = value;
        return this;
    }

    /** Whether the same request may succeed later. */
    public Boolean retryable() {
        return retryable;
    }

    /** Sets retryable. */
    public ErrorEnvelopeError retryable(Boolean value) {
        this.retryable = value;
        return this;
    }

    /** Where this code is explained. */
    public String docUrl() {
        return docUrl;
    }

    /** Sets doc_url. */
    public ErrorEnvelopeError docUrl(String value) {
        this.docUrl = value;
        return this;
    }

    /** More about the problem: the fields at fault, a limit, where to go. */
    public JsonNode details() {
        return details;
    }

    /** Sets details. */
    public ErrorEnvelopeError details(JsonNode value) {
        this.details = value;
        return this;
    }

    /** The order the error is about (an order the engine refused is kept, with this id). */
    public String orderId() {
        return orderId;
    }

    /** Sets order_id. */
    public ErrorEnvelopeError orderId(String value) {
        this.orderId = value;
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
        if (!(other instanceof ErrorEnvelopeError)) {
            return false;
        }
        ErrorEnvelopeError o = (ErrorEnvelopeError) other;
        return Objects.equals(type, o.type)
            && Objects.equals(code, o.code)
            && Objects.equals(message, o.message)
            && Objects.equals(requestId, o.requestId)
            && Objects.equals(retryable, o.retryable)
            && Objects.equals(docUrl, o.docUrl)
            && Objects.equals(details, o.details)
            && Objects.equals(orderId, o.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, code, message, requestId, retryable, docUrl, details, orderId);
    }

    @Override
    public String toString() {
        return "ErrorEnvelopeError{" + "type=" + type + ", code=" + code + ", message=" + message + ", request_id=" + requestId + ", retryable=" + retryable + ", doc_url=" + docUrl + ", details=" + details + ", order_id=" + orderId + "}";
    }
}
