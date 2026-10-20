<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Through the MCP server (an AI assistant): how its trades are confirmed and its budget. Null for any other call. */
final class MeAssistant implements \JsonSerializable
{
    private const WIRE = ['confirm_trades' => true, 'confirm_seconds' => true, 'budget' => true];

    public function __construct(
        /** Each trade is shown first and placed only when repeated with its confirmation (preview, then confirm). */
        public readonly ?bool $confirmTrades = null,
        /** How long a preview's confirmation holds, in seconds. */
        public readonly ?int $confirmSeconds = null,
        /** The AI assistant's own budget, on top of the others. */
        public readonly ?MeAssistantBudget $budget = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            confirmTrades: $d['confirm_trades'] ?? null,
            confirmSeconds: $d['confirm_seconds'] ?? null,
            budget: isset($d['budget']) && is_array($d['budget']) ? MeAssistantBudget::fromArray($d['budget']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->confirmTrades !== null) {
            $out['confirm_trades'] = $this->confirmTrades;
        }
        if ($this->confirmSeconds !== null) {
            $out['confirm_seconds'] = $this->confirmSeconds;
        }
        if ($this->budget !== null) {
            $out['budget'] = $this->budget->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
