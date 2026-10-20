package com.phoenixperpsfunding.sdk;

import java.net.http.HttpHeaders;

/**
 * The answer of a call as it came: its status, headers, body, request id and the call's Idempotency-Key.
 *
 * @param statusCode the HTTP status
 * @param headers the headers
 * @param body the body
 * @param requestId the request id
 * @param idempotencyKey the call's Idempotency-Key
 */
public record RawResponse(int statusCode, HttpHeaders headers, byte[] body, String requestId, String idempotencyKey) {
}

