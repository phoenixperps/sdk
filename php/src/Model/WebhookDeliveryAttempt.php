<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** WebhookDeliveryAttempt */
final class WebhookDeliveryAttempt implements \JsonSerializable
{
    private const WIRE = ['attempt' => true, 'at' => true, 'http_status' => true, 'error' => true, 'duration_ms' => true, 'response' => true];

    public function __construct(
        /** Which try. */
        public readonly ?int $attempt = null,
        /** When. */
        public readonly ?int $at = null,
        /** What your endpoint answered; null when it could not be reached. */
        public readonly ?int $httpStatus = null,
        /** Why it failed, when it did (timeout, redirect, private_address, http_500, ...). */
        public readonly ?string $error = null,
        /** How long it took. */
        public readonly ?int $durationMs = null,
        /** The start of the answer (up to 512 bytes). */
        public readonly ?string $response = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            attempt: $d['attempt'] ?? null,
            at: $d['at'] ?? null,
            httpStatus: $d['http_status'] ?? null,
            error: $d['error'] ?? null,
            durationMs: $d['duration_ms'] ?? null,
            response: $d['response'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->attempt !== null) {
            $out['attempt'] = $this->attempt;
        }
        if ($this->at !== null) {
            $out['at'] = $this->at;
        }
        if ($this->httpStatus !== null) {
            $out['http_status'] = $this->httpStatus;
        }
        if ($this->error !== null) {
            $out['error'] = $this->error;
        }
        if ($this->durationMs !== null) {
            $out['duration_ms'] = $this->durationMs;
        }
        if ($this->response !== null) {
            $out['response'] = $this->response;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
