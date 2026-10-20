//! Small helpers (docs/BEHAVIOUR.md "Helpers"), the same names in every SDK, in exact decimals.
//!
//! MarketRules (from `client.markets().get(..)`): round_price, round_size (down to the step), size_for_notional,
//! size_for_risk(risk_usd, stop_distance), worst_price(side, price, slippage_pct) rounded outward. The steps and the
//! slippage bounds are the market's own (nothing written in here). They take a Decimal or its string, and fail with a
//! Validation error (invalid_parameter): a string that is no decimal, a side other than buy or sell (in any case), a
//! slippage outside the market's bounds.
//!
//! verify_webhook(raw, headers, secrets, tolerance, now): the WebhookPayload, or an Error of kind WebhookVerification.
//! X-Phoenix-Signature is sha256=<hex HMAC-SHA256(secret, "<X-Phoenix-Timestamp>.<raw body>")>, any of its
//! comma-separated values (a rotation sends two); X-Phoenix-Timestamp within the tolerance (300 s).

use crate::error::{Error, ErrorKind};
use crate::{Market, MarketMaxSlippagePct, Result, WebhookPayload};
use hmac::{Hmac, Mac};
use rust_decimal::{Decimal, RoundingStrategy};
use serde_json::Value;
use sha2::Sha256;
use std::time::{Duration, SystemTime, UNIX_EPOCH};

/// How a value meets a step.
#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub enum RoundMode {
    /// The nearest multiple; a tie goes away from zero.
    Nearest,
    /// The multiple at or below.
    Down,
    /// The multiple at or above.
    Up,
}

/// A decimal a helper takes: a Decimal, or its string as the API writes it ("62000.5").
pub trait DecimalArg {
    /// The decimal; a string that is no decimal is a Validation error.
    fn decimal(self) -> Result<Decimal>;
}

impl DecimalArg for Decimal {
    fn decimal(self) -> Result<Decimal> {
        Ok(self)
    }
}

impl DecimalArg for &str {
    fn decimal(self) -> Result<Decimal> {
        Decimal::from_str_exact(self.trim()).map_err(|_| invalid(format!("{self:?} is not a decimal.")))
    }
}

fn invalid(message: String) -> Error {
    Error::new(ErrorKind::Validation, "invalid_parameter", message)
}

/// Rounds prices and sizes the way a market takes them.
#[derive(Clone, Debug)]
pub struct MarketRules {
    /// The market.
    pub market: String,
    /// The tick size.
    pub tick_size: Decimal,
    /// The size step.
    pub size_step: Decimal,
    /// The smallest size.
    pub min_size: Decimal,
    /// The slippage bounds of market orders.
    pub slippage: Option<MarketMaxSlippagePct>,
}

impl MarketRules {
    /// The rules of a market.
    pub fn new(m: &Market) -> Self {
        Self { market: m.market.clone(), tick_size: m.tick_size, size_step: m.size_step, min_size: m.min_size, slippage: m.max_slippage_pct.clone() }
    }

    /// x as a multiple of step, at the step's decimals: the Decimal an order takes (its Display, the API's text). A step
    /// not above zero is a Validation error.
    pub fn round_to(x: impl DecimalArg, step: Decimal, mode: RoundMode) -> Result<Decimal> {
        let x = x.decimal()?;
        let q = x.checked_div(step).filter(|_| step > Decimal::ZERO).ok_or_else(|| invalid(format!("The step {step} is not above zero.")))?;
        let n = match mode {
            RoundMode::Down => q.floor(),
            RoundMode::Up => q.ceil(),
            RoundMode::Nearest => q.round_dp_with_strategy(0, RoundingStrategy::MidpointAwayFromZero),
        };
        let mut v = n.checked_mul(step).ok_or_else(|| invalid(format!("{x} is out of range.")))?;
        v.rescale(step.normalize().scale());
        Ok(v)
    }

    /// The price as a multiple of the tick size.
    pub fn round_price(&self, price: impl DecimalArg, mode: RoundMode) -> Result<Decimal> {
        Self::round_to(price, self.tick_size, mode)
    }

    /// The size down to the size step.
    pub fn round_size(&self, size: impl DecimalArg) -> Result<Decimal> {
        Self::round_to(size, self.size_step, RoundMode::Down)
    }

    /// The size worth usd at price, down to the step: as it rounds, even below min_size (it may be 0), so check it
    /// against min_size before you order.
    pub fn size_for_notional(&self, usd: impl DecimalArg, price: impl DecimalArg) -> Result<Decimal> {
        let (usd, price) = (usd.decimal()?, price.decimal()?);
        self.round_size(usd.checked_div(price).ok_or_else(|| invalid(format!("No size at the price {price}.")))?)
    }

    /// The size that loses risk_usd over stop_distance, down to the step: as it rounds, even below min_size (it may
    /// be 0), so check it against min_size before you order.
    pub fn size_for_risk(&self, risk_usd: impl DecimalArg, stop_distance: impl DecimalArg) -> Result<Decimal> {
        let (risk, distance) = (risk_usd.decimal()?, stop_distance.decimal()?);
        self.round_size(risk.checked_div(distance.abs()).ok_or_else(|| invalid(format!("No size for a stop distance of {distance}.")))?)
    }

    /// The worst price a market order of side (buy or sell, in any case) accepts at price, slippage_pct away (None:
    /// the market's default; within its min..max), rounded outward.
    pub fn worst_price(&self, side: &str, price: impl DecimalArg, slippage_pct: Option<Decimal>) -> Result<Decimal> {
        let buy = match side.to_ascii_lowercase().as_str() {
            "buy" => true,
            "sell" => false,
            _ => return Err(invalid(format!("The side is buy or sell, not {side:?}."))),
        };
        let price = price.decimal()?;
        let bounds = self.slippage.as_ref();
        let pct = slippage_pct.or_else(|| bounds.and_then(|s| s.r#default)).ok_or_else(|| invalid(format!("{} has no default slippage: give one.", self.market)))?;
        if bounds.and_then(|s| s.min).is_some_and(|min| pct < min) || bounds.and_then(|s| s.max).is_some_and(|max| pct > max) {
            return Err(invalid(format!("A slippage of {pct}% is outside {}'s bounds.", self.market)));
        }
        let f = pct / Decimal::ONE_HUNDRED;
        if buy {
            Self::round_to(price * (Decimal::ONE + f), self.tick_size, RoundMode::Up)
        } else {
            Self::round_to(price * (Decimal::ONE - f), self.tick_size, RoundMode::Down)
        }
    }
}

/// What verify_webhook fails with: an Error of kind WebhookVerification (code missing_headers, stale, bad_signature or
/// bad_body).
pub type WebhookError = Error;

fn refused(code: &str, message: &str) -> Error {
    Error::new(ErrorKind::WebhookVerification, code, message)
}

/// The delivery once its signature and time check out: the WebhookPayload (the event, with api_version, environment
/// and test). raw: the body exactly as received; headers: any (name, value) pairs; secrets: the endpoint's (two during
/// a rotation); tolerance: 300 s when None; now: the clock. Fails with an Error of kind WebhookVerification:
/// missing_headers, stale (a timestamp outside the tolerance, or no number), bad_signature, bad_body (signed, but not
/// an event).
pub fn verify_webhook<I, K, V>(raw: &str, headers: I, secrets: &[&str], tolerance: Option<Duration>, now: Option<SystemTime>) -> Result<WebhookPayload>
where
    I: IntoIterator<Item = (K, V)>,
    K: AsRef<str>,
    V: AsRef<str>,
{
    let (mut ts, mut sig) = (None, None);
    for (k, v) in headers {
        match k.as_ref().to_ascii_lowercase().as_str() {
            "x-phoenix-timestamp" => ts = Some(v.as_ref().to_string()),
            "x-phoenix-signature" => sig = Some(v.as_ref().to_string()),
            _ => {}
        }
    }
    let (Some(ts), Some(sig)) = (ts.filter(|t| !t.is_empty()), sig.filter(|s| !s.is_empty())) else {
        return Err(refused("missing_headers", "X-Phoenix-Timestamp and X-Phoenix-Signature are required."));
    };
    let stale = || refused("stale", "The delivery is too old (or the clock is off).");
    let t: u64 = ts.parse().map_err(|_| stale())?;
    let at = now.unwrap_or_else(SystemTime::now).duration_since(UNIX_EPOCH).unwrap_or_default().as_secs_f64();
    if (at - t as f64).abs() > tolerance.unwrap_or(Duration::from_secs(300)).as_secs_f64() {
        return Err(stale());
    }
    for secret in secrets {
        let mut mac = Hmac::<Sha256>::new_from_slice(secret.as_bytes()).expect("any key length");
        mac.update(format!("{ts}.{raw}").as_bytes());
        let want = format!("sha256={}", mac.finalize().into_bytes().iter().map(|b| format!("{b:02x}")).collect::<String>());
        if sig.split(',').any(|g| same(g.trim().as_bytes(), want.as_bytes())) {
            // an event: an object with its id and type (an array would read as a struct's fields in order)
            let event = serde_json::from_str::<Value>(raw).ok().filter(|v| v.get("id").is_some_and(Value::is_string) && v.get("type").is_some_and(Value::is_string));
            return event.and_then(|v| serde_json::from_value(v).ok()).ok_or_else(|| refused("bad_body", "The body is not an event."));
        }
    }
    Err(refused("bad_signature", "The signature does not match."))
}

/// Equal, in a time that does not depend on where they differ.
fn same(a: &[u8], b: &[u8]) -> bool {
    a.len() == b.len() && a.iter().zip(b).fold(0u8, |acc, (x, y)| acc | (x ^ y)) == 0
}
