<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Me */
final class Me implements \JsonSerializable
{
    private const WIRE = ['credential' => true, 'accounts' => true, 'account_scope' => true, 'controls' => true, 'budgets' => true, 'assistant' => true];

    public function __construct(
        /** The credential. */
        public readonly ?MeCredential $credential = null,
        /** The accounts it reaches. */
        /** @var list<string>|null */
        public readonly ?array $accounts = null,
        /** all: every account of the environment, new ones too. selected: only those listed. */
        public readonly ?string $accountScope = null,
        /** The limits you set on the key. */
        public readonly ?MeControls $controls = null,
        /** The budgets. */
        public readonly ?MeBudgets $budgets = null,
        /** Through the MCP server (an AI assistant): how its trades are confirmed and its budget. Null for any other call. */
        public readonly ?MeAssistant $assistant = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            credential: isset($d['credential']) && is_array($d['credential']) ? MeCredential::fromArray($d['credential']) : null,
            accounts: $d['accounts'] ?? null,
            accountScope: $d['account_scope'] ?? null,
            controls: isset($d['controls']) && is_array($d['controls']) ? MeControls::fromArray($d['controls']) : null,
            budgets: isset($d['budgets']) && is_array($d['budgets']) ? MeBudgets::fromArray($d['budgets']) : null,
            assistant: isset($d['assistant']) && is_array($d['assistant']) ? MeAssistant::fromArray($d['assistant']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->credential !== null) {
            $out['credential'] = $this->credential->toArray();
        }
        if ($this->accounts !== null) {
            $out['accounts'] = $this->accounts;
        }
        if ($this->accountScope !== null) {
            $out['account_scope'] = $this->accountScope;
        }
        if ($this->controls !== null) {
            $out['controls'] = $this->controls->toArray();
        }
        if ($this->budgets !== null) {
            $out['budgets'] = $this->budgets->toArray();
        }
        if ($this->assistant !== null) {
            $out['assistant'] = $this->assistant->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
