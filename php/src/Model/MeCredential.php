<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** MeCredential */
final class MeCredential implements \JsonSerializable
{
    private const WIRE = ['kind' => true, 'id' => true, 'name' => true, 'environment' => true, 'access' => true, 'expires_at' => true, 'created_at' => true];

    public function __construct(
        /** What you called with: an API key, or an app you signed in to. */
        public readonly ?string $kind = null,
        /** The key's id (the part after ppk_test_ or ppk_live_), or the app access's id. */
        public readonly ?string $id = null,
        /** Its name (an app: the app's name). */
        public readonly ?string $name = null,
        /** Its environment. */
        public readonly ?string $environment = null,
        /** read: look only. trade: also place and cancel. */
        public readonly ?string $access = null,
        /** When it expires; null: never. */
        public readonly ?int $expiresAt = null,
        /** When it was made. */
        public readonly ?int $createdAt = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            kind: $d['kind'] ?? null,
            id: $d['id'] ?? null,
            name: $d['name'] ?? null,
            environment: $d['environment'] ?? null,
            access: $d['access'] ?? null,
            expiresAt: $d['expires_at'] ?? null,
            createdAt: $d['created_at'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->kind !== null) {
            $out['kind'] = $this->kind;
        }
        if ($this->id !== null) {
            $out['id'] = $this->id;
        }
        if ($this->name !== null) {
            $out['name'] = $this->name;
        }
        if ($this->environment !== null) {
            $out['environment'] = $this->environment;
        }
        if ($this->access !== null) {
            $out['access'] = $this->access;
        }
        if ($this->expiresAt !== null) {
            $out['expires_at'] = $this->expiresAt;
        }
        if ($this->createdAt !== null) {
            $out['created_at'] = $this->createdAt;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
