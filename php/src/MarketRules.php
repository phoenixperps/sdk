<?php

declare(strict_types=1);

namespace PhoenixPerps;

use PhoenixPerps\Enum\OrderSide;
use PhoenixPerps\Exception\ValidationException;
use PhoenixPerps\Model\Market;

/**
 * Rounds prices and sizes the way a market takes them (docs/BEHAVIOUR.md "Helpers"), in exact decimals (bcmath):
 * roundPrice, roundSize (down to the step), sizeForNotional, sizeForRisk(riskUsd, stopDistance),
 * worstPrice(side, price, slippagePct) rounded outward. The steps and the slippage bounds are the market's own. What the
 * API would refuse fails here with its ValidationException (invalid_request): a value that is no plain decimal, a side
 * other than buy or sell (any case), a slippage out of the market's bounds.
 */
final class MarketRules
{
    public const NEAREST = 'nearest';
    public const DOWN = 'down';
    public const UP = 'up';
    private const SCALE = 30;

    public readonly string $market;
    public readonly string $tickSize;
    public readonly string $sizeStep;
    public readonly ?string $minSize;
    public readonly ?string $defaultSlippagePct;
    public readonly ?string $minSlippagePct;
    public readonly ?string $maxSlippagePct;

    public function __construct(Market $m)
    {
        if (!extension_loaded('bcmath')) {
            throw new \RuntimeException('MarketRules needs the bcmath extension.');
        }
        $this->market = (string) $m->market;
        $this->tickSize = (string) $m->tickSize;
        $this->sizeStep = (string) $m->sizeStep;
        $this->minSize = $m->minSize;
        $this->defaultSlippagePct = $m->maxSlippagePct?->default;
        $this->minSlippagePct = $m->maxSlippagePct?->min;
        $this->maxSlippagePct = $m->maxSlippagePct?->max;
    }

    private static function places(string $step): int
    {
        $s = rtrim($step, '0');
        $i = strpos($s, '.');
        return $i === false ? 0 : strlen($s) - $i - 1;
    }

    /** x as the decimal string bcmath takes (digits, at most one point), else the API's own refusal. */
    private static function dec(string|int|float $x, string $field): string
    {
        $s = Decimal::of($x);
        if (!preg_match('/^[+-]?(\d+(\.\d*)?|\.\d+)$/', $s)) {
            throw new ValidationException('invalid_request', "{$field} is not a decimal: '{$s}'.", details: [['field' => $field, 'issue' => 'a decimal: digits and at most one point']]);
        }
        return $s;
    }

    /** x as a multiple of step (nearest: a tie goes away from zero), as the decimal string the API takes. */
    public static function roundTo(string|int|float $x, string|int|float $step, string $mode = self::NEAREST): string
    {
        $x = self::dec($x, 'x');
        $step = self::dec($step, 'step');
        if (bccomp($step, '0', self::SCALE) <= 0) {
            throw new \InvalidArgumentException('The step is not above zero.');
        }
        $q = bcdiv($x, $step, self::SCALE);
        $whole = bcadd($q, '0', 0); // toward zero
        $rest = bcsub($q, $whole, self::SCALE);
        $n = $whole;
        if ($mode === self::DOWN && bccomp($rest, '0', self::SCALE) < 0) {
            $n = bcsub($whole, '1', 0);
        } elseif ($mode === self::UP && bccomp($rest, '0', self::SCALE) > 0) {
            $n = bcadd($whole, '1', 0);
        } elseif ($mode === self::NEAREST && bccomp(ltrim($rest, '-'), '0.5', self::SCALE) >= 0) {
            $n = bccomp($q, '0', self::SCALE) < 0 ? bcsub($whole, '1', 0) : bcadd($whole, '1', 0);
        }
        return bcmul($n, $step, self::places($step));
    }

    /** The price as a multiple of the tick size. */
    public function roundPrice(string|int|float $price, string $mode = self::NEAREST): string
    {
        return self::roundTo(self::dec($price, 'price'), $this->tickSize, $mode);
    }

    /** The size down to the size step. */
    public function roundSize(string|int|float $size): string
    {
        return self::roundTo(self::dec($size, 'size'), $this->sizeStep, self::DOWN);
    }

    /** The size worth usd at price, down to the step. */
    public function sizeForNotional(string|int|float $usd, string|int|float $price): string
    {
        return $this->roundSize(bcdiv(self::dec($usd, 'usd'), self::positive(self::dec($price, 'price'), 'price'), self::SCALE));
    }

    /** The size that loses riskUsd over stopDistance, down to the step. */
    public function sizeForRisk(string|int|float $riskUsd, string|int|float $stopDistance): string
    {
        return $this->roundSize(bcdiv(self::dec($riskUsd, 'riskUsd'), self::positive(ltrim(self::dec($stopDistance, 'stopDistance'), '-'), 'stopDistance'), self::SCALE));
    }

    /** A divisor above 0, else the SDK's ValidationException (never PHP's DivisionByZeroError). */
    private static function positive(string $d, string $field): string
    {
        if (bccomp($d, '0', self::SCALE) <= 0) {
            throw new ValidationException('invalid_request', "{$field} must be above 0, not '{$d}'.", details: [['field' => $field, 'issue' => 'above 0']]);
        }
        return $d;
    }

    /**
     * The worst price a market order of side (OrderSide: "buy" or "sell", any case) accepts at price, slippagePct away
     * (null: the market's default, else within its bounds), rounded outward.
     */
    public function worstPrice(string $side, string|int|float $price, string|int|float|null $slippagePct = null): string
    {
        $buy = match (strtolower($side)) {
            OrderSide::BUY => true,
            OrderSide::SELL => false,
            default => throw new ValidationException('invalid_request', "The side is buy or sell, not '{$side}'.", details: [['field' => 'side', 'issue' => 'buy or sell']]),
        };
        $pct = $slippagePct === null ? $this->defaultSlippagePct : self::dec($slippagePct, 'slippagePct');
        if ($pct === null) {
            throw new \LogicException('No slippage given and the market has no default.');
        }
        $low = $this->minSlippagePct !== null && bccomp($pct, $this->minSlippagePct, self::SCALE) < 0;
        if ($low || ($this->maxSlippagePct !== null && bccomp($pct, $this->maxSlippagePct, self::SCALE) > 0)) {
            $issue = $low ? "at least {$this->minSlippagePct}" : "at most {$this->maxSlippagePct}";
            throw new ValidationException('invalid_request', "{$this->market} takes a slippage of {$issue} percent, not {$pct}.", details: [['field' => 'max_slippage_pct', 'issue' => $issue]]);
        }
        $f = bcdiv($pct, '100', self::SCALE);
        $p = self::dec($price, 'price');
        return $buy
            ? self::roundTo(bcmul($p, bcadd('1', $f, self::SCALE), self::SCALE), $this->tickSize, self::UP)
            : self::roundTo(bcmul($p, bcsub('1', $f, self::SCALE), self::SCALE), $this->tickSize, self::DOWN);
    }
}
