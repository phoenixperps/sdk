<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The limits you set on the key. */
final class MeControls implements \JsonSerializable
{
    private const WIRE = ['markets' => true, 'reduce_only' => true, 'max_order_usd' => true, 'max_position_usd' => true, 'daily_loss_usd' => true];

    public function __construct(
        /** The markets the key may trade; empty: every market. */
        /** @var list<string>|null */
        public readonly ?array $markets = null,
        /** The key may only reduce positions. */
        public readonly ?bool $reduceOnly = null,
        /** The largest order the key may place, in USD. */
        public readonly ?string $maxOrderUsd = null,
        /** The largest position the key may hold, in USD. */
        public readonly ?string $maxPositionUsd = null,
        /** The loss in a trading day at which the key stops trading the account. */
        public readonly ?string $dailyLossUsd = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            markets: $d['markets'] ?? null,
            reduceOnly: $d['reduce_only'] ?? null,
            maxOrderUsd: isset($d['max_order_usd']) ? (string) $d['max_order_usd'] : null,
            maxPositionUsd: isset($d['max_position_usd']) ? (string) $d['max_position_usd'] : null,
            dailyLossUsd: isset($d['daily_loss_usd']) ? (string) $d['daily_loss_usd'] : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->markets !== null) {
            $out['markets'] = $this->markets;
        }
        if ($this->reduceOnly !== null) {
            $out['reduce_only'] = $this->reduceOnly;
        }
        if ($this->maxOrderUsd !== null) {
            $out['max_order_usd'] = $this->maxOrderUsd;
        }
        if ($this->maxPositionUsd !== null) {
            $out['max_position_usd'] = $this->maxPositionUsd;
        }
        if ($this->dailyLossUsd !== null) {
            $out['daily_loss_usd'] = $this->dailyLossUsd;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
