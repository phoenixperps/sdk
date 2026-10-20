// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/** The family of the error; it decides the HTTP status. Open: a value from a newer API version is kept as is. */
public final class ErrorEnvelopeErrorType {
    /** invalid_request */
    public static final ErrorEnvelopeErrorType INVALID_REQUEST = new ErrorEnvelopeErrorType("invalid_request");

    /** authentication */
    public static final ErrorEnvelopeErrorType AUTHENTICATION = new ErrorEnvelopeErrorType("authentication");

    /** permission */
    public static final ErrorEnvelopeErrorType PERMISSION = new ErrorEnvelopeErrorType("permission");

    /** not_found */
    public static final ErrorEnvelopeErrorType NOT_FOUND = new ErrorEnvelopeErrorType("not_found");

    /** method */
    public static final ErrorEnvelopeErrorType METHOD = new ErrorEnvelopeErrorType("method");

    /** conflict */
    public static final ErrorEnvelopeErrorType CONFLICT = new ErrorEnvelopeErrorType("conflict");

    /** payload */
    public static final ErrorEnvelopeErrorType PAYLOAD = new ErrorEnvelopeErrorType("payload");

    /** rule_rejection */
    public static final ErrorEnvelopeErrorType RULE_REJECTION = new ErrorEnvelopeErrorType("rule_rejection");

    /** key_limit */
    public static final ErrorEnvelopeErrorType KEY_LIMIT = new ErrorEnvelopeErrorType("key_limit");

    /** rate_limit */
    public static final ErrorEnvelopeErrorType RATE_LIMIT = new ErrorEnvelopeErrorType("rate_limit");

    /** server */
    public static final ErrorEnvelopeErrorType SERVER = new ErrorEnvelopeErrorType("server");

    /** engine */
    public static final ErrorEnvelopeErrorType ENGINE = new ErrorEnvelopeErrorType("engine");

    private final String value;

    private ErrorEnvelopeErrorType(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /** The value of its wire form (a known constant, or a new value kept as is). */
    @JsonCreator
    public static ErrorEnvelopeErrorType of(String value) {
        switch (value) {
            case "invalid_request":
                return INVALID_REQUEST;
            case "authentication":
                return AUTHENTICATION;
            case "permission":
                return PERMISSION;
            case "not_found":
                return NOT_FOUND;
            case "method":
                return METHOD;
            case "conflict":
                return CONFLICT;
            case "payload":
                return PAYLOAD;
            case "rule_rejection":
                return RULE_REJECTION;
            case "key_limit":
                return KEY_LIMIT;
            case "rate_limit":
                return RATE_LIMIT;
            case "server":
                return SERVER;
            case "engine":
                return ENGINE;
            default:
                return new ErrorEnvelopeErrorType(value);
        }
    }

    /** The value as the API writes it. */
    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ErrorEnvelopeErrorType && ((ErrorEnvelopeErrorType) o).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
