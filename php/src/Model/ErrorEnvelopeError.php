<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** ErrorEnvelopeError */
final class ErrorEnvelopeError implements \JsonSerializable
{
    private const WIRE = ['type' => true, 'code' => true, 'message' => true, 'request_id' => true, 'retryable' => true, 'doc_url' => true, 'details' => true, 'order_id' => true];

    public function __construct(
        /** The family of the error; it decides the HTTP status. */
        public readonly ?string $type = null,
        /** The error code: match on this. */
        public readonly ?string $code = null,
        /** A sentence for people. It may change; never match on it. */
        public readonly ?string $message = null,
        /** The id of the request; give it to support. */
        public readonly ?string $requestId = null,
        /** Whether the same request may succeed later. */
        public readonly ?bool $retryable = null,
        /** Where this code is explained. */
        public readonly ?string $docUrl = null,
        /** More about the problem: the fields at fault, a limit, where to go. */
        public readonly mixed $details = null,
        /** The order the error is about (an order the engine refused is kept, with this id). */
        public readonly ?string $orderId = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            type: $d['type'] ?? null,
            code: $d['code'] ?? null,
            message: $d['message'] ?? null,
            requestId: $d['request_id'] ?? null,
            retryable: $d['retryable'] ?? null,
            docUrl: $d['doc_url'] ?? null,
            details: $d['details'] ?? null,
            orderId: $d['order_id'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->code !== null) {
            $out['code'] = $this->code;
        }
        if ($this->message !== null) {
            $out['message'] = $this->message;
        }
        if ($this->requestId !== null) {
            $out['request_id'] = $this->requestId;
        }
        if ($this->retryable !== null) {
            $out['retryable'] = $this->retryable;
        }
        if ($this->docUrl !== null) {
            $out['doc_url'] = $this->docUrl;
        }
        if ($this->details !== null) {
            $out['details'] = $this->details;
        }
        if ($this->orderId !== null) {
            $out['order_id'] = $this->orderId;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
