<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** CancelAllResult */
final class CancelAllResult implements \JsonSerializable
{
    private const WIRE = ['account_id' => true, 'market' => true, 'canceled' => true];

    public function __construct(
        /** The account number. */
        public readonly ?string $accountId = null,
        /** The market, or null for every market. */
        public readonly ?string $market = null,
        /** How many working orders were canceled. */
        public readonly ?int $canceled = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            accountId: $d['account_id'] ?? null,
            market: $d['market'] ?? null,
            canceled: $d['canceled'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->canceled !== null) {
            $out['canceled'] = $this->canceled;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
