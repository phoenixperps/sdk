"""Small helpers (docs/BEHAVIOUR.md "Helpers"), the same names in every SDK, in exact decimals.

MarketRules(market): from GET /v1/markets/{market}: round_price, round_size (floor to the step), size_for_notional,
size_for_risk(risk_usd, stop_distance) (rounded down, even below min_size: check it before you order),
worst_price(side, price, slippage_pct) rounded outward; the steps and the slippage bounds are the market's own (nothing
written in here): a side other than buy or sell (any case), a slippage out of the market's bounds or a value that is no
decimal is a ValidationError (a ValueError too).

Webhooks.verify(raw, headers, secrets, tolerance_seconds=300): the WebhookPayload (the event, with api_version,
environment and test), or WebhookVerificationError (a PhoenixPerpsError: missing_headers, stale, bad_signature,
bad_body), whatever the body. X-Phoenix-Signature = sha256=<hex HMAC-SHA256(secret, "<timestamp>.<raw body>")>, any of
its comma-separated values (a rotation sends two), compared as bytes; X-Phoenix-Timestamp within the tolerance.
"""
from __future__ import annotations

import hashlib
import hmac
import json
import time
from decimal import ROUND_CEILING, ROUND_FLOOR, ROUND_HALF_UP, Decimal, InvalidOperation
from typing import Any, Iterable, Mapping, Optional, Union

from ._errors import PhoenixPerpsError, ValidationError

Num = Union[str, int, float, Decimal]


class _InvalidValue(ValidationError, ValueError):
    """A value a helper cannot take: the SDK's ValidationError, and the ValueError it always was."""


def _d(x: Num) -> Decimal:
    try:
        d = x if isinstance(x, Decimal) else Decimal(repr(x)) if isinstance(x, float) else Decimal(str(x))
        if d.is_finite():
            return d
    except InvalidOperation:
        pass
    raise _InvalidValue(f"{x!r} is not a decimal.", code="invalid_parameter")


def round_to(x: Num, step: Num, mode: str = "nearest") -> str:
    """`x` to a multiple of `step` (down, up or nearest: a tie goes away from zero), as the decimal string the API takes."""
    st = _d(step)
    n = (_d(x) / st).quantize(Decimal(1), rounding={"down": ROUND_FLOOR, "up": ROUND_CEILING}.get(mode, ROUND_HALF_UP))
    exp = st.normalize().as_tuple().exponent
    places = -exp if isinstance(exp, int) and exp < 0 else 0
    return f"{n * st:.{places}f}"


class MarketRules:
    def __init__(self, market: Mapping[str, Any]) -> None:
        self.market = market["market"]
        self.tick_size = market["tick_size"]
        self.size_step = market["size_step"]
        self.min_size = market.get("min_size")
        self.slippage = market.get("max_slippage_pct")

    def round_price(self, price: Num, mode: str = "nearest") -> str:
        return round_to(price, self.tick_size, mode)

    def round_size(self, size: Num) -> str:
        return round_to(size, self.size_step, "down")

    def size_for_notional(self, usd: Num, price: Num) -> str:
        return self.round_size(_d(usd) / _d(price))

    def size_for_risk(self, risk_usd: Num, stop_distance: Num) -> str:
        return self.round_size(_d(risk_usd) / abs(_d(stop_distance)))

    def worst_price(self, side: str, price: Num, slippage_pct: Optional[Num] = None) -> str:
        bounds = self.slippage or {}
        pct = slippage_pct if slippage_pct is not None else bounds.get("default")
        if pct is None:
            raise _InvalidValue("worst_price: no slippage given and the market has no default", code="invalid_parameter")
        buy = {"buy": True, "sell": False}.get(str(side).lower())
        if buy is None:
            raise _InvalidValue(f"worst_price: the side is buy or sell, not {side!r}", code="invalid_parameter")
        lo, hi = bounds.get("min"), bounds.get("max")
        if (lo is not None and _d(pct) < _d(lo)) or (hi is not None and _d(pct) > _d(hi)):
            raise _InvalidValue(f"worst_price: {self.market} takes a slippage from {lo} to {hi} %, not {pct}", code="invalid_parameter")
        f = Decimal(1) + _d(pct) / 100 if buy else Decimal(1) - _d(pct) / 100
        return round_to(_d(price) * f, self.tick_size, "up" if buy else "down")


class WebhookVerificationError(PhoenixPerpsError):
    def __init__(self, code: str, message: str) -> None:
        super().__init__(message, code=code)


def _header(headers: Mapping[str, Any], name: str) -> Optional[str]:
    for k, v in headers.items():
        if k.lower() == name:
            return v[0] if isinstance(v, (list, tuple)) else v
    return None


class Webhooks:
    @staticmethod
    def verify(raw: Union[str, bytes], headers: Mapping[str, Any], secrets: Union[str, Iterable[str]], tolerance_seconds: int = 300, now: Optional[float] = None) -> Any:
        """The WebhookPayload of a delivery (the event, with api_version, environment and test), once its signature and
        time check out. `raw`: the body exactly as received (its bytes are signed: one that is not UTF-8 fails the
        signature, never the decoding)."""
        body = raw if isinstance(raw, bytes) else raw.encode()
        ts = _header(headers, "x-phoenix-timestamp")
        sig = _header(headers, "x-phoenix-signature")
        if not ts or not sig:
            raise WebhookVerificationError("missing_headers", "X-Phoenix-Timestamp and X-Phoenix-Signature are required.")
        if not ts.isdigit() or abs((time.time() if now is None else now) - int(ts)) > tolerance_seconds:
            raise WebhookVerificationError("stale", "The delivery is too old (or the clock is off).")
        given = [s.strip().encode() for s in sig.split(",")]  # bytes: a value of any alphabet compares, never raises
        for secret in [secrets] if isinstance(secrets, str) else list(secrets):
            want = b"sha256=" + hmac.new(secret.encode(), ts.encode() + b"." + body, hashlib.sha256).hexdigest().encode()
            if any(hmac.compare_digest(g, want) for g in given):
                try:
                    event = json.loads(body)
                except ValueError:
                    event = None
                if not (isinstance(event, dict) and isinstance(event.get("id"), str) and isinstance(event.get("type"), str)):
                    raise WebhookVerificationError("bad_body", "The body is not an event.")  # signed, but not an event
                return event
        raise WebhookVerificationError("bad_signature", "The signature does not match.")
