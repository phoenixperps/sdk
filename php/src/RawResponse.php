<?php

declare(strict_types=1);

namespace PhoenixPerps;

/** The answer of a call as it came. */
final class RawResponse
{
    /**
     * @param array<string, string> $headers lowercase names
     */
    public function __construct(
        public readonly int $status,
        public readonly array $headers,
        public readonly string $body,
        public readonly ?string $requestId,
        public readonly ?string $idempotencyKey,
    ) {
    }
}
