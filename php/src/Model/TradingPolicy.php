<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** TradingPolicy */
final class TradingPolicy implements \JsonSerializable
{
    private const WIRE = ['rule' => true, 'trading_day' => true, 'slippage_pct' => true, 'margin' => true, 'orders' => true, 'controls' => true, 'budgets' => true, 'as_of' => true];

    public function __construct(
        /** The rule. */
        public readonly ?Rule $rule = null,
        /** The trading day. */
        public readonly ?TradingPolicyTradingDay $tradingDay = null,
        /** The slippage pct. */
        public readonly ?TradingPolicySlippagePct $slippagePct = null,
        /** The margin. */
        public readonly ?TradingPolicyMargin $margin = null,
        /** The orders. */
        public readonly ?TradingPolicyOrders $orders = null,
        /** The limits you set on the key. */
        public readonly ?TradingPolicyControls $controls = null,
        /** The budgets. */
        public readonly ?TradingPolicyBudgets $budgets = null,
        /** When the oldest figure here was read. */
        public readonly ?int $asOf = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            rule: isset($d['rule']) && is_array($d['rule']) ? Rule::fromArray($d['rule']) : null,
            tradingDay: isset($d['trading_day']) && is_array($d['trading_day']) ? TradingPolicyTradingDay::fromArray($d['trading_day']) : null,
            slippagePct: isset($d['slippage_pct']) && is_array($d['slippage_pct']) ? TradingPolicySlippagePct::fromArray($d['slippage_pct']) : null,
            margin: isset($d['margin']) && is_array($d['margin']) ? TradingPolicyMargin::fromArray($d['margin']) : null,
            orders: isset($d['orders']) && is_array($d['orders']) ? TradingPolicyOrders::fromArray($d['orders']) : null,
            controls: isset($d['controls']) && is_array($d['controls']) ? TradingPolicyControls::fromArray($d['controls']) : null,
            budgets: isset($d['budgets']) && is_array($d['budgets']) ? TradingPolicyBudgets::fromArray($d['budgets']) : null,
            asOf: $d['as_of'] ?? null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->rule !== null) {
            $out['rule'] = $this->rule->toArray();
        }
        if ($this->tradingDay !== null) {
            $out['trading_day'] = $this->tradingDay->toArray();
        }
        if ($this->slippagePct !== null) {
            $out['slippage_pct'] = $this->slippagePct->toArray();
        }
        if ($this->margin !== null) {
            $out['margin'] = $this->margin->toArray();
        }
        if ($this->orders !== null) {
            $out['orders'] = $this->orders->toArray();
        }
        if ($this->controls !== null) {
            $out['controls'] = $this->controls->toArray();
        }
        if ($this->budgets !== null) {
            $out['budgets'] = $this->budgets->toArray();
        }
        if ($this->asOf !== null) {
            $out['as_of'] = $this->asOf;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
