<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** A message to read, sent nowhere. */
final class TradingViewParseInput implements \JsonSerializable
{
    private const WIRE = ['message' => true, 'hook_id' => true];

    public function __construct(
        /** The alert's message, as TradingView would send it. */
        public readonly ?string $message = null,
        /** A link: the plan is then worked out on its account and settings, as of now. */
        public readonly ?string $hookId = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            message: $d['message'] ?? null,
            hookId: $d['hook_id'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->message !== null) {
            $out['message'] = $this->message;
        }
        if ($this->hookId !== null) {
            $out['hook_id'] = $this->hookId;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
