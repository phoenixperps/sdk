<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Event */
final class Event implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'type' => true, 'account_id' => true, 'time' => true, 'source' => true, 'data' => true];

    public function __construct(
        /** The event; pass it as after (GET /v1/events) or resume (the stream) to continue from it. */
        public readonly ?string $id = null,
        /** What happened (GET /v1/webhook-event-types says what each type means). */
        public readonly ?string $type = null,
        /** The account number. */
        public readonly ?string $accountId = null,
        /** When it happened. */
        public readonly ?int $time = null,
        /** Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert. */
        public readonly ?string $source = null,
        /** order or fill (the objects REST answers), with changes or close_reason when they apply. A position.* event carries position { market, side, size, average_price } as the engine reported it, and previous_size: read GET .../positions/{market} for its margin, P&L and liquidation. */
        /** @var array<string, mixed>|null */
        public readonly ?array $data = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            type: $d['type'] ?? null,
            accountId: $d['account_id'] ?? null,
            time: $d['time'] ?? null,
            source: $d['source'] ?? null,
            data: $d['data'] ?? null,
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
        if ($this->type !== null) {
            $out['type'] = $this->type;
        }
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->time !== null) {
            $out['time'] = $this->time;
        }
        if ($this->source !== null) {
            $out['source'] = $this->source;
        }
        if ($this->data !== null) {
            $out['data'] = $this->data;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
