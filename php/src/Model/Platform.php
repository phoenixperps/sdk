<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Platform */
final class Platform implements \JsonSerializable
{
    private const WIRE = ['environment' => true, 'version' => true, 'server_time' => true, 'docs_url' => true, 'spec_url' => true, 'status' => true];

    public function __construct(
        /** The environment of the host you called. */
        public readonly ?string $environment = null,
        /** The version of this API. */
        public readonly ?string $version = null,
        /** The time on the server. */
        public readonly ?int $serverTime = null,
        /** The documentation. */
        public readonly ?string $docsUrl = null,
        /** This API as OpenAPI 3.1. */
        public readonly ?string $specUrl = null,
        /** The status. */
        public readonly ?PlatformStatus $status = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            environment: $d['environment'] ?? null,
            version: $d['version'] ?? null,
            serverTime: $d['server_time'] ?? null,
            docsUrl: $d['docs_url'] ?? null,
            specUrl: $d['spec_url'] ?? null,
            status: isset($d['status']) && is_array($d['status']) ? PlatformStatus::fromArray($d['status']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->environment !== null) {
            $out['environment'] = $this->environment;
        }
        if ($this->version !== null) {
            $out['version'] = $this->version;
        }
        if ($this->serverTime !== null) {
            $out['server_time'] = $this->serverTime;
        }
        if ($this->docsUrl !== null) {
            $out['docs_url'] = $this->docsUrl;
        }
        if ($this->specUrl !== null) {
            $out['spec_url'] = $this->specUrl;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
