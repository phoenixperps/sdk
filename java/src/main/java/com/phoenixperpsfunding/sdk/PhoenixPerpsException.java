package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.http.HttpHeaders;
import java.time.Duration;

/**
 * Every failure of a call (docs/BEHAVIOUR.md "Answers"): the API's code and message, the HTTP status, the request id,
 * the details, the Retry-After, whether trying again may help, the page that explains it, the order it is about, the
 * call's Idempotency-Key and the answer's headers. Its class tells the family: AuthenticationException (401),
 * PermissionException (403), NotFoundException, ConflictException (409), ValidationException (400),
 * RuleRejectedException (422), RateLimitException, ServerException, ApiConnectionException, ApiTimeoutException,
 * StreamClosedException.
 */
public class PhoenixPerpsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String code;
    private final Integer status;
    String errorType;
    String requestId;
    transient JsonNode details;
    Duration retryAfter;
    boolean retryable;
    String docUrl;
    String orderId;
    String idempotencyKey;
    transient HttpHeaders headers;

    /** A failure with its code, message and status. */
    public PhoenixPerpsException(String code, String message, Integer status, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = status;
    }

    /** The API's error code (gen.ErrorCodes), or http_&lt;status&gt; for an answer that is not the API's. */
    public String code() {
        return code;
    }

    /** The HTTP status, when there was an answer. */
    public Integer status() {
        return status;
    }

    /** The error type of the API's envelope. */
    public String errorType() {
        return errorType;
    }

    /** The request id, to quote to support. */
    public String requestId() {
        return requestId;
    }

    /** The details the API gave, as they came. */
    public JsonNode details() {
        return details;
    }

    /** When to try again, when the API said. */
    public Duration retryAfter() {
        return retryAfter;
    }

    /** Trying again may help: the API's flag (without its envelope: 429, 500, 502, 503, 504); a timeout or a network failure. */
    public boolean retryable() {
        return retryable;
    }

    /** The page of the docs about this error, when the API gave one. */
    public String docUrl() {
        return docUrl;
    }

    /** The order this is about (a refused order keeps its id). */
    public String orderId() {
        return orderId;
    }

    /** The call's Idempotency-Key: send the call again with it to be sure it happens once. */
    public String idempotencyKey() {
        return idempotencyKey;
    }

    /** The headers of the answer, when there was one. */
    public HttpHeaders headers() {
        return headers;
    }

    /** 401: the key is missing, wrong, revoked, expired or of the other environment. */
    public static final class AuthenticationException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public AuthenticationException(String code, String message, Integer status) {
            super(code, message, status, null);
        }
    }

    /** 403: the key may not do this. */
    public static final class PermissionException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public PermissionException(String code, String message, Integer status) {
            super(code, message, status, null);
        }
    }

    /** 404. */
    public static final class NotFoundException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public NotFoundException(String code, String message, Integer status) {
            super(code, message, status, null);
        }
    }

    /** 409. */
    public static final class ConflictException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public ConflictException(String code, String message, Integer status) {
            super(code, message, status, null);
        }
    }

    /** Another 4xx: the request is not right. */
    public static final class ValidationException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public ValidationException(String code, String message, Integer status) {
            super(code, message, status, null);
        }
    }

    /** 422: the account's rules or the key's controls said no. */
    public static final class RuleRejectedException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public RuleRejectedException(String code, String message, Integer status) {
            super(code, message, status, null);
        }
    }

    /** 429. */
    public static final class RateLimitException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public RateLimitException(String code, String message, Integer status) {
            super(code, message, status, null);
        }
    }

    /** 5xx. */
    public static final class ServerException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public ServerException(String code, String message, Integer status, Throwable cause) {
            super(code, message, status, cause);
        }
    }

    /** No answer: the network failed. */
    public static final class ApiConnectionException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public ApiConnectionException(String code, String message, Throwable cause) {
            super(code, message, null, cause);
            retryable = true;
        }
    }

    /** No answer in time. */
    public static final class ApiTimeoutException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public ApiTimeoutException(String code, String message, Throwable cause) {
            super(code, message, null, cause);
            retryable = true;
        }
    }

    /** A stream ended for good. */
    public static final class StreamClosedException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        /** The failure. */
        public StreamClosedException(String code, String message) {
            super(code, message, null, null);
        }
    }

    static PhoenixPerpsException forStatus(int status, String code, String message) {
        if (status == 401) {
            return new AuthenticationException(code, message, status);
        }
        if (status == 403) {
            return new PermissionException(code, message, status);
        }
        if (status == 404) {
            return new NotFoundException(code, message, status);
        }
        if (status == 409) {
            return new ConflictException(code, message, status);
        }
        if (status == 422) {
            return new RuleRejectedException(code, message, status);
        }
        if (status == 429) {
            return new RateLimitException(code, message, status);
        }
        if (status >= 500) {
            return new ServerException(code, message, status, null);
        }
        return new ValidationException(code, message, status);
    }
}
