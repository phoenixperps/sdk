<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** WebhookEndpoint */
final class WebhookEndpoint implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'url' => true, 'format' => true, 'events' => true, 'status' => true, 'api_version' => true, 'environment' => true];

    public function __construct(
        /** The endpoint. */
        public readonly ?string $id = null,
        /** Its address: the host and the start of the path (never whole: chat addresses carry tokens). */
        public readonly ?string $url = null,
        /** json: the signed event. discord, slack, telegram: a chat message. */
        public readonly ?string $format = null,
        /** The groups it receives (see GET /v1/webhook-event-types). */
        /** @var list<string>|null */
        public readonly ?array $events = null,
        /** disabled: we stopped after failures in a row; turn it on again from your dashboard. */
        public readonly ?string $status = null,
        /** The version its events are written in. */
        public readonly ?string $apiVersion = null,
        /** Its environment: it receives that environment's events only. */
        public readonly ?string $environment = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            url: $d['url'] ?? null,
            format: $d['format'] ?? null,
            events: $d['events'] ?? null,
            status: $d['status'] ?? null,
            apiVersion: $d['api_version'] ?? null,
            environment: $d['environment'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->id !== null) {
            $out['id'] = $this->id;
        }
        if ($this->url !== null) {
            $out['url'] = $this->url;
        }
        if ($this->format !== null) {
            $out['format'] = $this->format;
        }
        if ($this->events !== null) {
            $out['events'] = $this->events;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        if ($this->apiVersion !== null) {
            $out['api_version'] = $this->apiVersion;
        }
        if ($this->environment !== null) {
            $out['environment'] = $this->environment;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
