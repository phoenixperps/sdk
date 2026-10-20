<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** WebhookEventType */
final class WebhookEventType implements \JsonSerializable
{
    private const WIRE = ['type' => true, 'group' => true, 'description' => true];

    public function __construct(
        /** The type. */
        public readonly ?string $type = null,
        /** The group that receives it; null: every endpoint (the test). */
        public readonly ?string $group = null,
        /** What it means. */
        public readonly ?string $description = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            type: $d['type'] ?? null,
            group: $d['group'] ?? null,
            description: $d['description'] ?? null,
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
        if ($this->group !== null) {
            $out['group'] = $this->group;
        }
        if ($this->description !== null) {
            $out['description'] = $this->description;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
