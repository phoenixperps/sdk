<?php

declare(strict_types=1);

namespace PhoenixPerps;

/** The options of one call. */
final class RequestOptions
{
    /**
     * @param string|null $idempotencyKey the call's Idempotency-Key (else a new UUID, the same on every attempt)
     * @param float|null $timeout the seconds one attempt may take
     * @param int|null $maxRetries how often the call is tried again when that is safe
     * @param int|null $maxItems caps an AutoPaging iteration (null or 0: every item)
     * @param array<string, string> $headers headers added to the request
     * @param (\Closure(RawResponse): void)|null $onResponse receives the answer of the call as it came
     */
    public function __construct(
        public readonly ?string $idempotencyKey = null,
        public readonly ?float $timeout = null,
        public readonly ?int $maxRetries = null,
        public readonly ?int $maxItems = null,
        public readonly array $headers = [],
        public readonly ?\Closure $onResponse = null,
    ) {
    }
}
