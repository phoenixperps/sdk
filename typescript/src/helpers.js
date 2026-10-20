'use strict';
const crypto = require('node:crypto');
const E = require('./errors');

/*
 * Small helpers (docs/BEHAVIOUR.md "Helpers"), the same names in every SDK.
 *   MarketRules(market)    from GET /v1/markets/{market}: roundPrice, roundSize (floor to the step), sizeForNotional,
 *                          sizeForRisk(riskUsd, stopDistance) (floored, 0 included: check min_size), worstPrice(side,
 *                          price, slippagePct) rounded outward (side buy or sell in any case, the slippage within the
 *                          market's bounds); the steps and the slippage bounds are the market's own (nothing written in
 *                          here); a value that is no decimal is a ValidationError
 *   Webhooks.verify(raw, headers, secrets, toleranceSeconds = 300)   the WebhookPayload, or a WebhookVerificationError
 *                          (missing_headers, stale, bad_signature, bad_body): X-Phoenix-Signature =
 *                          sha256=<hex HMAC-SHA256(secret, "<timestamp>.<raw body>")>, any of the comma-separated values
 *                          (a rotation sends two), X-Phoenix-Timestamp within the tolerance
 */
const invalid = (message) => new E.ValidationError({ code: 'invalid_parameter', message });
// Exact decimals: a value is a fraction { n, d } of BigInts (d a power of ten when read from a decimal). An exponent
// (a number's own String, 1e-7, or '2.3e-2') is read exactly too, never through a float.
const MAX_EXPONENT = 400; // past any double (1.8e308): a longer one is no price or size
function frac(x) {
  const s = typeof x === 'number' ? (Number.isFinite(x) ? String(x) : '') : String(x).trim();
  const m = /^(-?)(\d*)(?:\.(\d*))?(?:e([+-]?\d+))?$/i.exec(s);
  if (!m || (m[2] === '' && !m[3]) || (m[4] !== undefined && Math.abs(Number(m[4])) > MAX_EXPONENT)) throw invalid(`not a decimal: ${x}`);
  const digits = `${m[2] || '0'}${m[3] || ''}`;
  const e = Number(m[4] || 0) - (m[3] || '').length;
  const n = BigInt(digits) * (m[1] ? -1n : 1n) * (e > 0 ? 10n ** BigInt(e) : 1n);
  return { n, d: e < 0 ? 10n ** BigInt(-e) : 1n };
}
const placesOf = (step) => {
  const s = String(step).replace(/(\.\d*?)0+$/, '$1');
  return s.includes('.') ? s.split('.')[1].length : 0;
};
/** The fraction n/d as a multiple of `step` (down, up or nearest: a tie goes away from zero), as a decimal string. */
function roundFrac({ n, d }, step, mode) {
  const st = frac(step);
  if (st.n <= 0n) throw new RangeError(`the step ${step} is not above zero`);
  const num = n * st.d;
  const den = d * st.n; // x / step = num / den, den > 0
  let q = num / den; // truncated toward zero
  const r = num % den;
  if (mode === 'down' && r < 0n) q -= 1n;
  else if (mode === 'up' && r > 0n) q += 1n;
  else if (mode !== 'down' && mode !== 'up' && (r < 0n ? -r : r) * 2n >= den) q += num < 0n ? -1n : 1n;
  const p = placesOf(step);
  const units = (q * st.n * 10n ** BigInt(p)) / st.d; // the result, in 10^-p
  const neg = units < 0n;
  const a = (neg ? -units : units).toString().padStart(p + 1, '0');
  return `${neg ? '-' : ''}${p ? `${a.slice(0, -p)}.${a.slice(-p)}` : a}`;
}
/** `x` to a multiple of `step`: down, up or nearest; as the decimal string the API takes. */
function roundTo(x, step, mode = 'nearest') {
  return roundFrac(frac(x), step, mode);
}
const quotient = (a, b) => {
  const x = frac(a);
  const y = frac(b);
  if (y.n === 0n) throw invalid('division by zero');
  const n = x.n * y.d;
  const d = x.d * y.n;
  return d < 0n ? { n: -n, d: -d } : { n, d };
};

class MarketRules {
  constructor(market) {
    this.market = market.market;
    this.tickSize = market.tick_size;
    this.sizeStep = market.size_step;
    this.minSize = market.min_size ?? null;
    this.slippage = market.max_slippage_pct || null;
  }
  roundPrice(price, mode = 'nearest') {
    return roundTo(price, this.tickSize, mode);
  }
  roundSize(size) {
    return roundTo(size, this.sizeStep, 'down');
  }
  sizeForNotional(usd, price) {
    return roundFrac(quotient(usd, price), this.sizeStep, 'down');
  }
  sizeForRisk(riskUsd, stopDistance) {
    const q = quotient(riskUsd, stopDistance);
    return roundFrac({ n: q.n < 0n ? -q.n : q.n, d: q.d }, this.sizeStep, 'down');
  }
  /** The worst price a market order of `side` accepts at `price`, `slippagePct` away (default: the market's). */
  worstPrice(side, price, slippagePct = this.slippage && this.slippage.default) {
    const buy = String(side).toLowerCase() === 'buy';
    if (!buy && String(side).toLowerCase() !== 'sell') throw invalid(`side is buy or sell, not ${side}`);
    if (slippagePct === null || slippagePct === undefined) throw invalid('worstPrice: no slippage given and the market has no default');
    const p = frac(price);
    const s = frac(slippagePct);
    // within the market's max_slippage_pct min..max (a over b: a.n x b.d against b.n x a.d, both d above zero)
    const below = (a, b) => a.n * b.d < b.n * a.d;
    const { min = null, max = null } = this.slippage || {};
    if ((min !== null && below(s, frac(min))) || (max !== null && below(frac(max), s))) throw invalid(`slippage ${slippagePct} is outside ${min} to ${max}`);
    // price x (100 +- pct) / 100
    const f = { n: s.d * 100n + (buy ? s.n : -s.n), d: s.d * 100n };
    return roundFrac({ n: p.n * f.n, d: p.d * f.d }, this.tickSize, buy ? 'up' : 'down');
  }
}

/** A delivery refused: missing_headers, stale, bad_signature or bad_body (a family of the SDK's own error). */
class WebhookVerificationError extends E.PhoenixPerpsError {
  constructor(code, message) {
    super({ code, message });
  }
}

const header = (headers, name) => {
  if (!headers) return null;
  if (typeof headers.get === 'function') return headers.get(name);
  const k = Object.keys(headers).find((x) => x.toLowerCase() === name);
  const v = k ? headers[k] : null;
  return Array.isArray(v) ? v[0] : v ?? null;
};

const Webhooks = {
  /** The WebhookPayload of a delivery, once its signature and time check out. `raw`: the body exactly as received. */
  verify(raw, headers, secrets, toleranceSeconds = 300, nowMs = Date.now()) {
    const body = Buffer.isBuffer(raw) ? raw.toString('utf8') : String(raw);
    const ts = header(headers, 'x-phoenix-timestamp');
    const sig = header(headers, 'x-phoenix-signature');
    if (!ts || !sig) throw new WebhookVerificationError('missing_headers', 'X-Phoenix-Timestamp and X-Phoenix-Signature are required.');
    if (!/^\d+$/.test(ts) || Math.abs(nowMs / 1000 - Number(ts)) > toleranceSeconds) throw new WebhookVerificationError('stale', 'The delivery is too old (or the clock is off).');
    // bytes against bytes: a non-ASCII character makes a value longer in bytes than in characters
    const given = sig.split(',').map((s) => Buffer.from(s.trim()));
    const ok = [].concat(secrets).some((secret) => {
      const want = Buffer.from(`sha256=${crypto.createHmac('sha256', secret).update(`${ts}.${body}`).digest('hex')}`);
      return given.some((g) => g.length === want.length && crypto.timingSafeEqual(g, want));
    });
    if (!ok) throw new WebhookVerificationError('bad_signature', 'The signature does not match.');
    // signed, so from the API: an event (webhook.ping too), else a body this SDK cannot read
    let payload = null;
    try {
      payload = JSON.parse(body);
    } catch {
      payload = null;
    }
    if (!payload || typeof payload !== 'object' || Array.isArray(payload) || typeof payload.id !== 'string' || typeof payload.type !== 'string') {
      throw new WebhookVerificationError('bad_body', 'The delivery is signed but is not an event.');
    }
    return payload;
  },
};

module.exports = { MarketRules, Webhooks, WebhookVerificationError, roundTo };
