<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** StatusEngine */
final class StatusEngine implements \JsonSerializable
{
    private const WIRE = ['connected' => true, 'started_at' => true, 'writes' => true];

    public function __construct(
        /** Whether the API is connected to the trading engine. */
        public readonly ?bool $connected = null,
        /** When the trading engine last started. */
        public readonly ?int $startedAt = null,
        /** Closes, flattens, cancel-alls and margin changes over the last minute: degraded when the engine was too busy to take one (engine_busy). */
        public readonly ?string $writes = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            connected: $d['connected'] ?? null,
            startedAt: $d['started_at'] ?? null,
            writes: $d['writes'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->connected !== null) {
            $out['connected'] = $this->connected;
        }
        if ($this->startedAt !== null) {
            $out['started_at'] = $this->startedAt;
        }
        if ($this->writes !== null) {
            $out['writes'] = $this->writes;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
