<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** Limits */
final class Limits implements \JsonSerializable
{
    private const WIRE = ['environment' => true, 'rate' => true, 'keys' => true, 'orders' => true, 'sandbox' => true, 'strategies' => true, 'body_max_bytes' => true, 'page_limit' => true];

    public function __construct(
        /** The environment of the host you called. */
        public readonly ?string $environment = null,
        /** The request budgets. A request takes from every budget it belongs to. */
        public readonly ?LimitsRate $rate = null,
        /** The keys. */
        public readonly ?LimitsKeys $keys = null,
        /** The orders. */
        public readonly ?LimitsOrders $orders = null,
        /** The sandbox. */
        public readonly ?LimitsSandbox $sandbox = null,
        /** The strategies. */
        public readonly ?LimitsStrategies $strategies = null,
        /** The largest request body. */
        public readonly ?int $bodyMaxBytes = null,
        /** The page limit. */
        public readonly ?LimitsPageLimit $pageLimit = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            environment: $d['environment'] ?? null,
            rate: isset($d['rate']) && is_array($d['rate']) ? LimitsRate::fromArray($d['rate']) : null,
            keys: isset($d['keys']) && is_array($d['keys']) ? LimitsKeys::fromArray($d['keys']) : null,
            orders: isset($d['orders']) && is_array($d['orders']) ? LimitsOrders::fromArray($d['orders']) : null,
            sandbox: isset($d['sandbox']) && is_array($d['sandbox']) ? LimitsSandbox::fromArray($d['sandbox']) : null,
            strategies: isset($d['strategies']) && is_array($d['strategies']) ? LimitsStrategies::fromArray($d['strategies']) : null,
            bodyMaxBytes: $d['body_max_bytes'] ?? null,
            pageLimit: isset($d['page_limit']) && is_array($d['page_limit']) ? LimitsPageLimit::fromArray($d['page_limit']) : null,
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
        if ($this->rate !== null) {
            $out['rate'] = $this->rate->toArray();
        }
        if ($this->keys !== null) {
            $out['keys'] = $this->keys->toArray();
        }
        if ($this->orders !== null) {
            $out['orders'] = $this->orders->toArray();
        }
        if ($this->sandbox !== null) {
            $out['sandbox'] = $this->sandbox->toArray();
        }
        if ($this->strategies !== null) {
            $out['strategies'] = $this->strategies->toArray();
        }
        if ($this->bodyMaxBytes !== null) {
            $out['body_max_bytes'] = $this->bodyMaxBytes;
        }
        if ($this->pageLimit !== null) {
            $out['page_limit'] = $this->pageLimit->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
