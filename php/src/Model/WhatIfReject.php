<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** WhatIfReject */
final class WhatIfReject implements \JsonSerializable
{
    private const WIRE = ['code' => true, 'message' => true, 'details' => true];

    public function __construct(
        /** The error code it would get. */
        public readonly ?string $code = null,
        /** What it means. */
        public readonly ?string $message = null,
        /** More about it. */
        public readonly mixed $details = null,
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
            details: $d['details'] ?? null,
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
        if ($this->details !== null) {
            $out['details'] = $this->details;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
