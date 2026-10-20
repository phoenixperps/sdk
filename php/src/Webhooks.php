<?php

declare(strict_types=1);

namespace PhoenixPerps;

use PhoenixPerps\Model\WebhookPayload;

/**
 * Webhook deliveries (docs/BEHAVIOUR.md "Helpers"): X-Phoenix-Signature is
 * sha256=<hex HMAC-SHA256(secret, "<X-Phoenix-Timestamp>.<raw body>")>, any of its comma-separated values (a rotation
 * sends two), compared in constant time; X-Phoenix-Timestamp within the tolerance (300 s). No secret (an unset
 * variable: getenv's false, or an empty one) is an InvalidArgumentException, never a check against an empty key.
 */
final class Webhooks
{
    /**
     * The delivery once its signature and time check out: the event with the endpoint's apiVersion and environment,
     * and test on a webhook.ping (POST /test) or a delivery resent as one. Else a WebhookVerificationException.
     *
     * @param string $raw the body exactly as received
     * @param array<string, string|list<string>> $headers any case
     * @param string|list<string>|false|null $secrets the endpoint's (two during a rotation)
     */
    public static function verify(string $raw, array $headers, string|array|false|null $secrets, int $toleranceSeconds = 300, ?float $now = null): WebhookPayload
    {
        return WebhookPayload::fromArray(self::verified($raw, $headers, $secrets, $toleranceSeconds, $now));
    }

    /**
     * verify(), by its earlier name.
     *
     * @param array<string, string|list<string>> $headers
     * @param string|list<string>|false|null $secrets
     */
    public static function verifyPayload(string $raw, array $headers, string|array|false|null $secrets, int $toleranceSeconds = 300, ?float $now = null): WebhookPayload
    {
        return self::verify($raw, $headers, $secrets, $toleranceSeconds, $now);
    }

    /**
     * @param array<string, string|list<string>> $headers
     * @param string|list<string>|false|null $secrets
     * @return array<string, mixed> the body, read
     */
    private static function verified(string $raw, array $headers, string|array|false|null $secrets, int $toleranceSeconds, ?float $now): array
    {
        $secrets = array_filter((array) $secrets, fn ($s) => is_string($s) && $s !== '');
        if (!$secrets) {
            throw new \InvalidArgumentException("No webhook secret: give the endpoint's signing secret (ppwhsec_...).");
        }
        $h = [];
        foreach ($headers as $k => $v) {
            $h[strtolower((string) $k)] = is_array($v) ? (string) ($v[0] ?? '') : (string) $v;
        }
        $ts = $h['x-phoenix-timestamp'] ?? '';
        $sig = $h['x-phoenix-signature'] ?? '';
        if ($ts === '' || $sig === '') {
            throw new WebhookVerificationException('missing_headers', 'X-Phoenix-Timestamp and X-Phoenix-Signature are required.');
        }
        if (!ctype_digit($ts) || abs(($now ?? microtime(true)) - (int) $ts) > $toleranceSeconds) {
            throw new WebhookVerificationException('stale', 'The delivery is too old (or the clock is off).');
        }
        $given = array_map('trim', explode(',', $sig));
        foreach ($secrets as $secret) {
            $want = 'sha256=' . hash_hmac('sha256', "{$ts}.{$raw}", $secret);
            foreach ($given as $g) {
                if (hash_equals($want, $g)) {
                    $d = json_decode($raw, true);
                    if (!is_array($d) || !is_string($d['id'] ?? null) || !is_string($d['type'] ?? null)) { // signed, but no event
                        throw new WebhookVerificationException('bad_body', 'The body is not an event.');
                    }
                    return $d;
                }
            }
        }
        throw new WebhookVerificationException('bad_signature', 'The signature does not match.');
    }
}
