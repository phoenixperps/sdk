<?php

declare(strict_types=1);

namespace PhoenixPerps\Exception;

/**
 * Every failure of a call (docs/BEHAVIOUR.md "Answers"): the API's code and message, the HTTP status, the request id,
 * the details, the Retry-After, the order it is about, the call's Idempotency-Key, whether the same call may work
 * later (retryable), the code's page (docUrl), the answer's headers and (a stream's error frame) the account it names.
 * Its class tells the family:
 * AuthenticationException (401), PermissionException (403), NotFoundException, ConflictException (409),
 * ValidationException (400), RuleRejectedException (422), RateLimitException, ServerException, ApiConnectionException,
 * ApiTimeoutException, StreamClosedException.
 */
class PhoenixPerpsException extends \RuntimeException
{
    /**
     * @param string $errorCode the API's error code (ErrorCodes), or http_<status> for an answer that is not the API's
     * @param int|null $status the HTTP status, when there was an answer
     * @param string|null $errorType the error type of the API's envelope
     * @param string|null $requestId the request id, to quote to support
     * @param mixed $details the details the API gave, as they came
     * @param float|null $retryAfter when to try again (seconds), when the API said
     * @param string|null $orderId the order this is about (a refused order keeps its id)
     * @param string|null $idempotencyKey the call's Idempotency-Key: send the call again with it to be sure it happens once
     * @param bool $retryable whether the same call may work if sent again later: the API's own word; without its
     *     envelope, a 429 or 5xx, a network failure or a timeout
     * @param string|null $docUrl the page of the code in the API's reference
     * @param array<string, string> $headers the answer's headers (lowercase names), when there was an answer
     * @param string|null $accountId the account an account stream's error frame names
     */
    public function __construct(
        public readonly string $errorCode,
        string $message,
        public readonly ?int $status = null,
        public readonly ?string $errorType = null,
        public readonly ?string $requestId = null,
        public readonly mixed $details = null,
        public readonly ?float $retryAfter = null,
        public readonly ?string $orderId = null,
        public readonly ?string $idempotencyKey = null,
        ?\Throwable $previous = null,
        public readonly bool $retryable = false,
        public readonly ?string $docUrl = null,
        public readonly array $headers = [],
        public readonly ?string $accountId = null,
    ) {
        parent::__construct($message, 0, $previous);
    }

    /** The family of an HTTP status. */
    public static function classFor(int $status): string
    {
        return match (true) {
            $status === 401 => AuthenticationException::class,
            $status === 403 => PermissionException::class,
            $status === 404 => NotFoundException::class,
            $status === 409 => ConflictException::class,
            $status === 422 => RuleRejectedException::class,
            $status === 429 => RateLimitException::class,
            $status >= 500 => ServerException::class,
            default => ValidationException::class,
        };
    }

    /**
     * Any answer as an error: the API's envelope, a bare {"error": "text"}, or HTML or text.
     *
     * @param array<string, string> $headers lowercase names
     */
    public static function fromAnswer(int $status, array $headers, string $body, ?float $retryAfter, ?string $idempotencyKey): self
    {
        $code = "http_{$status}";
        $message = '';
        $type = null;
        $orderId = null;
        $requestId = $headers['x-request-id'] ?? null;
        $details = null;
        $retryable = in_array($status, [429, 500, 502, 503, 504], true); // an answer without the API's word on it
        $docUrl = null;
        $json = json_decode($body, true);
        $e = is_array($json) ? ($json['error'] ?? null) : null;
        if (is_array($e)) {
            $code = is_string($e['code'] ?? null) && $e['code'] !== '' ? $e['code'] : $code;
            $message = (string) ($e['message'] ?? '');
            $type = isset($e['type']) ? (string) $e['type'] : null;
            $orderId = isset($e['order_id']) ? (string) $e['order_id'] : null;
            $requestId = isset($e['request_id']) ? (string) $e['request_id'] : $requestId;
            $details = $e['details'] ?? null;
            $retryable = is_bool($e['retryable'] ?? null) ? $e['retryable'] : $retryable;
            $docUrl = isset($e['doc_url']) ? (string) $e['doc_url'] : null;
        } elseif (is_string($e)) {
            $message = mb_substr($e, 0, 200);
        } else {
            $message = mb_substr($body, 0, 200);
        }
        if ($message === '') {
            $message = $code;
        }
        $class = self::classFor($status);
        return new $class($code, $message, $status, $type, $requestId, $details, $retryAfter, $orderId, $idempotencyKey, retryable: $retryable, docUrl: $docUrl, headers: $headers);
    }
}
