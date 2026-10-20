<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** OrderReject */
final class OrderReject implements \JsonSerializable
{
    private const WIRE = ['code' => true, 'message' => true];

    public function __construct(
        /** The error code. */
        public readonly ?string $code = null,
        /** What happened. */
        public readonly ?string $message = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            code: $d['code'] ?? null,
            message: $d['message'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->code !== null) {
            $out['code'] = $this->code;
        }
        if ($this->message !== null) {
            $out['message'] = $this->message;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
