package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.phoenixperpsfunding.sdk.gen.WebhookPayload;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Webhook deliveries (docs/BEHAVIOUR.md "Helpers"): X-Phoenix-Signature is
 * sha256=&lt;hex HMAC-SHA256(secret, "&lt;X-Phoenix-Timestamp&gt;.&lt;raw body&gt;")&gt;, any of its comma-separated values (a
 * rotation sends two), compared as bytes in constant time; X-Phoenix-Timestamp within the tolerance (300 s).
 */
public final class Webhooks {
    private Webhooks() {
    }

    /** Why a delivery did not verify: missing_headers, stale, bad_signature, or bad_body (signed, but not an event). */
    public static final class VerificationException extends PhoenixPerpsException {
        private static final long serialVersionUID = 1L;

        VerificationException(String code, String message) {
            super(code, message, null, null);
        }
    }

    /**
     * The delivery (the event, with the endpoint's api_version and environment, and test on webhook.ping) once its
     * signature and time check out. raw: the body exactly as received.
     */
    public static WebhookPayload verify(String raw, Map<String, String> headers, List<String> secrets, Duration tolerance, Instant now) {
        String ts = header(headers, "x-phoenix-timestamp");
        String sig = header(headers, "x-phoenix-signature");
        if (ts == null || ts.isEmpty() || sig == null || sig.isEmpty()) {
            throw new VerificationException("missing_headers", "X-Phoenix-Timestamp and X-Phoenix-Signature are required.");
        }
        Duration tol = tolerance != null ? tolerance : Duration.ofSeconds(300);
        Instant at = now != null ? now : Instant.now();
        if (!fresh(ts, at, tol)) {
            throw new VerificationException("stale", "The delivery is too old (or the clock is off).");
        }
        for (String secret : secrets) {
            byte[] want = ("sha256=" + hmac(secret, ts + "." + raw)).getBytes(StandardCharsets.UTF_8);
            for (String g : sig.split(",")) {
                if (MessageDigest.isEqual(g.trim().getBytes(StandardCharsets.UTF_8), want)) {
                    return payload(raw);
                }
            }
        }
        throw new VerificationException("bad_signature", "The signature does not match.");
    }

    /** Verify with one secret, now, within 300 s. */
    public static WebhookPayload verify(String raw, Map<String, String> headers, String secret) {
        return verify(raw, headers, List.of(secret), null, null);
    }

    // signed, so the body is the API's: an event is an object with its id and type, anything else is bad_body
    private static WebhookPayload payload(String raw) {
        try {
            JsonNode n = Json.MAPPER.readTree(raw);
            if (n != null && n.isObject() && n.path("id").isTextual() && n.path("type").isTextual()) {
                return Json.MAPPER.treeToValue(n, WebhookPayload.class);
            }
        } catch (java.io.IOException e) {
            // not JSON: below
        }
        throw new VerificationException("bad_body", "The body is not an event.");
    }

    // a timestamp beyond a long or any Instant is stale too, never an unchecked exception
    private static boolean fresh(String ts, Instant at, Duration tol) {
        try {
            return ts.matches("\\d+") && Duration.between(Instant.ofEpochSecond(Long.parseLong(ts)), at).abs().compareTo(tol) <= 0;
        } catch (NumberFormatException | DateTimeException e) {
            return false;
        }
    }

    private static String header(Map<String, String> headers, String name) {
        for (Map.Entry<String, String> e : headers.entrySet()) {
            if (e.getKey().equalsIgnoreCase(name)) {
                return e.getValue();
            }
        }
        return null;
    }

    private static String hmac(String secret, String text) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(text.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }
}
