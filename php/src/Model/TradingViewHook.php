<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** TradingViewHook */
final class TradingViewHook implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'name' => true, 'account_id' => true, 'environment' => true, 'status' => true, 'markets' => true, 'reduce_only' => true, 'max_order_usd' => true, 'max_position_usd' => true, 'daily_loss_usd' => true, 'sizing' => true, 'position_mode' => true];

    public function __construct(
        /** The link. */
        public readonly ?string $id = null,
        /** Its name. */
        public readonly ?string $name = null,
        /** The account number. */
        public readonly ?string $accountId = null,
        /** Its environment. */
        public readonly ?string $environment = null,
        /** paused: alerts are refused (link_paused). */
        public readonly ?string $status = null,
        /** The markets it may trade; empty: every market. */
        /** @var list<string>|null */
        public readonly ?array $markets = null,
        /** It may only reduce positions. */
        public readonly ?bool $reduceOnly = null,
        /** Its largest order, in USD. */
        public readonly ?string $maxOrderUsd = null,
        /** Its largest position, in USD. */
        public readonly ?string $maxPositionUsd = null,
        /** Its daily loss stop: no new position once today's loss reaches it. */
        public readonly ?string $dailyLossUsd = null,
        /** The sizing. */
        public readonly ?TradingViewHookSizing $sizing = null,
        /** signal: an opposite alert reverses, a repeat is skipped. add: a repeat adds. target: the strategy's position size is followed. */
        public readonly ?string $positionMode = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            name: $d['name'] ?? null,
            accountId: $d['account_id'] ?? null,
            environment: $d['environment'] ?? null,
            status: $d['status'] ?? null,
            markets: $d['markets'] ?? null,
            reduceOnly: $d['reduce_only'] ?? null,
            maxOrderUsd: isset($d['max_order_usd']) ? (string) $d['max_order_usd'] : null,
            maxPositionUsd: isset($d['max_position_usd']) ? (string) $d['max_position_usd'] : null,
            dailyLossUsd: isset($d['daily_loss_usd']) ? (string) $d['daily_loss_usd'] : null,
            sizing: isset($d['sizing']) && is_array($d['sizing']) ? TradingViewHookSizing::fromArray($d['sizing']) : null,
            positionMode: $d['position_mode'] ?? null,
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
        if ($this->name !== null) {
            $out['name'] = $this->name;
        }
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->environment !== null) {
            $out['environment'] = $this->environment;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
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
        if ($this->sizing !== null) {
            $out['sizing'] = $this->sizing->toArray();
        }
        if ($this->positionMode !== null) {
            $out['position_mode'] = $this->positionMode;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
