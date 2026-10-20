package com.phoenixperpsfunding.sdk;

import com.phoenixperpsfunding.sdk.PhoenixPerpsException.ValidationException;
import com.phoenixperpsfunding.sdk.gen.Market;
import com.phoenixperpsfunding.sdk.gen.MarketMaxSlippagePct;
import com.phoenixperpsfunding.sdk.gen.OrderSide;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Rounds prices and sizes the way a market takes them (docs/BEHAVIOUR.md "Helpers"), in exact decimals: roundPrice,
 * roundSize (down to the step), sizeForNotional, sizeForRisk(riskUsd, stopDistance), worstPrice(side, price,
 * slippagePct) rounded outward. The steps and the slippage bounds are the market's own (nothing written in here). Each
 * answers the BigDecimal an order takes, at the step's decimals. A size is answered as it rounds, 0 included: check it
 * against minSize() before you order. Each takes decimal strings too; a string that is no decimal, a side other than buy
 * or sell, a price or stop distance of 0, or a slippage outside the market's bounds is a ValidationException.
 */
public final class MarketRules {
    /** How a value meets a step. */
    public enum Mode {
        /** The nearest multiple; a tie goes away from zero. */
        NEAREST,
        /** The multiple at or below. */
        DOWN,
        /** The multiple at or above. */
        UP
    }

    private static final MathContext DIV = new MathContext(40);
    private final String market;
    private final BigDecimal tickSize;
    private final BigDecimal sizeStep;
    private final BigDecimal minSize;
    private final MarketMaxSlippagePct slippage;

    /** The rules of a market (client.markets().get(...)). */
    public MarketRules(Market m) {
        this.market = m.market();
        this.tickSize = m.tickSize();
        this.sizeStep = m.sizeStep();
        this.minSize = m.minSize();
        this.slippage = m.maxSlippagePct();
    }

    /** The market. */
    public String market() {
        return market;
    }

    /** The smallest size. */
    public BigDecimal minSize() {
        return minSize;
    }

    /** x as a multiple of step, at the step's decimals: the BigDecimal an order takes (its toPlainString(), the API's text). */
    public static BigDecimal roundTo(BigDecimal x, BigDecimal step, Mode mode) {
        if (step.signum() <= 0) {
            throw new IllegalArgumentException("The step is not above zero.");
        }
        BigDecimal q = x.divide(step, DIV);
        RoundingMode rm = mode == Mode.DOWN ? RoundingMode.FLOOR : mode == Mode.UP ? RoundingMode.CEILING : RoundingMode.HALF_UP;
        BigDecimal n = q.setScale(0, rm);
        int places = Math.max(0, step.stripTrailingZeros().scale());
        return n.multiply(step).setScale(places, RoundingMode.UNNECESSARY);
    }

    /** A divisor above 0, else the SDK's ValidationException (never Java's ArithmeticException). */
    private static BigDecimal positive(String name, BigDecimal d) {
        if (d.signum() <= 0) {
            throw new ValidationException("invalid_parameter", name + " must be above 0, not " + d.toPlainString() + ".", null);
        }
        return d;
    }

    /** A decimal string as a BigDecimal (null stays null); one that is no decimal is a ValidationException. */
    static BigDecimal decimal(String name, String v) {
        try {
            return v == null ? null : new BigDecimal(v.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("invalid_parameter", name + " is no decimal: " + v + ".", null);
        }
    }

    /** The price as a multiple of the tick size. */
    public BigDecimal roundPrice(BigDecimal price, Mode mode) {
        return roundTo(price, tickSize, mode);
    }

    /** The price as a multiple of the tick size, from a decimal string. */
    public BigDecimal roundPrice(String price, Mode mode) {
        return roundPrice(decimal("price", price), mode);
    }

    /** The price, nearest the tick size. */
    public BigDecimal roundPrice(BigDecimal price) {
        return roundPrice(price, Mode.NEAREST);
    }

    /** The size down to the size step. */
    public BigDecimal roundSize(BigDecimal size) {
        return roundTo(size, sizeStep, Mode.DOWN);
    }

    /** The size down to the size step, from a decimal string. */
    public BigDecimal roundSize(String size) {
        return roundSize(decimal("size", size));
    }

    /** The size worth usd at price, down to the step (below minSize() included: it may be 0). */
    public BigDecimal sizeForNotional(BigDecimal usd, BigDecimal price) {
        return roundSize(usd.divide(positive("price", price), DIV));
    }

    /** The same from decimal strings. */
    public BigDecimal sizeForNotional(String usd, String price) {
        return sizeForNotional(decimal("usd", usd), decimal("price", price));
    }

    /** The size that loses riskUsd over stopDistance, down to the step (below minSize() included: it may be 0). */
    public BigDecimal sizeForRisk(BigDecimal riskUsd, BigDecimal stopDistance) {
        return roundSize(riskUsd.divide(positive("stopDistance", stopDistance.abs()), DIV));
    }

    /** The same from decimal strings. */
    public BigDecimal sizeForRisk(String riskUsd, String stopDistance) {
        return sizeForRisk(decimal("riskUsd", riskUsd), decimal("stopDistance", stopDistance));
    }

    /** The worst price a market order of side accepts at price, slippagePct away (null: the market's default), rounded outward. */
    public BigDecimal worstPrice(OrderSide side, BigDecimal price, BigDecimal slippagePct) {
        return worstPrice(side.value(), price, slippagePct);
    }

    /** The same from strings: the side buy or sell in any case, the price and slippage decimal strings. */
    public BigDecimal worstPrice(String side, String price, String slippagePct) {
        return worstPrice(side, decimal("price", price), decimal("slippagePct", slippagePct));
    }

    /**
     * The same with the side as a string, "buy" or "sell" in any case (anything else is refused, never taken as a sell);
     * a slippage outside the market's max_slippage_pct min..max is refused too.
     */
    public BigDecimal worstPrice(String side, BigDecimal price, BigDecimal slippagePct) {
        boolean buy = "buy".equalsIgnoreCase(side);
        if (!buy && !"sell".equalsIgnoreCase(side)) {
            throw new ValidationException("invalid_parameter", "The side is buy or sell, not " + side + ".", null);
        }
        BigDecimal pct = slippagePct != null ? slippagePct : slippage != null ? slippage.defaultValue() : null;
        if (pct == null) {
            throw new IllegalStateException("No slippage given and the market has no default.");
        }
        if (slippage != null && ((slippage.min() != null && pct.compareTo(slippage.min()) < 0) || (slippage.max() != null && pct.compareTo(slippage.max()) > 0))) {
            throw new ValidationException("invalid_parameter", "The slippage is " + slippage.min() + " to " + slippage.max() + " % on " + market + ", not " + pct + ".", null);
        }
        BigDecimal f = pct.divide(BigDecimal.valueOf(100), DIV);
        return buy ? roundTo(price.multiply(BigDecimal.ONE.add(f)), tickSize, Mode.UP) : roundTo(price.multiply(BigDecimal.ONE.subtract(f)), tickSize, Mode.DOWN);
    }
}
