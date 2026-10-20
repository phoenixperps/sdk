<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The query of get the equity curve. */
final class HistoryEquityParams implements \JsonSerializable
{
    private const WIRE = ['resolution' => true];

    public function __construct(
        /** One point per closed trade, or per trading day. */
        public readonly ?string $resolution = null,
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            resolution: $d['resolution'] ?? null,
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->resolution !== null) {
            $out['resolution'] = $this->resolution;
        }
        return $out;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }

    /** @return array<string, string> the query string parameters */
    public function toQuery(): array
    {
        $out = [];
        foreach ($this->toArray() as $k => $v) {
            $out[$k] = is_bool($v) ? ($v ? 'true' : 'false') : (string) $v;
        }
        return $out;
    }
}
