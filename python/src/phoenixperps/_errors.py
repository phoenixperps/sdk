"""Errors (docs/BEHAVIOUR.md "Errors"): one base class with the API's code, status, message, request id, details,
retryable, doc_url, the Retry-After and the answer's headers; a subclass per family. Any body becomes an error: the
API's envelope, a bare {"error": "text"}, or HTML or plain text (code http_<status>, at most 200 characters of the body)."""
from __future__ import annotations

import email.utils
import json
import math
import time
from typing import Any, Mapping, Optional

# an answer without the API's envelope (a proxy's 502 page) is retryable by its status alone
RETRY_STATUS = {429, 500, 502, 503, 504}


class PhoenixPerpsError(Exception):
    def __init__(self, message: str = "", *, status: Optional[int] = None, code: str = "error", request_id: Optional[str] = None,
                 details: Any = None, retry_after: Optional[float] = None, idempotency_key: Optional[str] = None,
                 order_id: Optional[str] = None, type: Optional[str] = None, retryable: bool = False,
                 doc_url: Optional[str] = None, headers: Optional[Mapping[str, str]] = None, account_id: Optional[str] = None) -> None:
        super().__init__(message or code)
        self.message = message or code
        self.status = status
        self.code = code
        self.request_id = request_id
        self.details = details
        self.retry_after = retry_after  # seconds
        self.idempotency_key = idempotency_key
        self.order_id = order_id
        self.type = type
        self.retryable = retryable  # the same call may work later: the API's own flag, else by the status
        self.doc_url = doc_url
        self.headers = headers  # the answer's (None without one: a timeout, a network failure)
        self.account_id = account_id  # the account a stream's error frame is about


class AuthenticationError(PhoenixPerpsError): pass
class PermissionDeniedError(PhoenixPerpsError): pass
class NotFoundError(PhoenixPerpsError): pass
class ConflictError(PhoenixPerpsError): pass
class ValidationError(PhoenixPerpsError): pass
class RuleRejectedError(PhoenixPerpsError): pass
class RateLimitError(PhoenixPerpsError): pass
class ServerError(PhoenixPerpsError): pass
class APIConnectionError(PhoenixPerpsError): pass
class APITimeoutError(PhoenixPerpsError): pass
class StreamClosedError(PhoenixPerpsError): pass


def class_for(status: int) -> type:
    if status == 401: return AuthenticationError
    if status == 403: return PermissionDeniedError
    if status == 404: return NotFoundError
    if status == 409: return ConflictError
    if status == 422: return RuleRejectedError
    if status == 429: return RateLimitError
    if status >= 500: return ServerError
    if status >= 400: return ValidationError
    return PhoenixPerpsError


def retry_after_s(headers: Mapping[str, str]) -> Optional[float]:
    """A Retry-After header (seconds, fractions included, or an HTTP date) in seconds; None when absent or unreadable."""
    v = headers.get("retry-after")
    if v is None or v == "":
        return None
    try:
        s = float(v)
        return s if math.isfinite(s) and s >= 0 else None
    except ValueError:
        pass
    try:
        return max(0.0, email.utils.parsedate_to_datetime(v).timestamp() - time.time())
    except (TypeError, ValueError):
        return None


def from_response(status: int, headers: Mapping[str, str], text: str, idempotency_key: Optional[str]) -> PhoenixPerpsError:
    cls = class_for(status)
    request_id = headers.get("x-request-id")
    retry_after = retry_after_s(headers)
    retryable = status in RETRY_STATUS
    try:
        body = json.loads(text) if text else None
    except ValueError:
        body = None
    if isinstance(body, dict) and isinstance(body.get("error"), dict):
        e = body["error"]
        return cls(e.get("message") or e.get("code") or "", status=status, code=e.get("code") or f"http_{status}",
                   request_id=e.get("request_id") or request_id, details=e.get("details"), retry_after=retry_after,
                   idempotency_key=idempotency_key, order_id=e.get("order_id"), type=e.get("type"), doc_url=e.get("doc_url"),
                   retryable=e["retryable"] if isinstance(e.get("retryable"), bool) else retryable, headers=headers)
    if isinstance(body, dict) and isinstance(body.get("error"), str):
        return cls(body["error"][:200], status=status, code=f"http_{status}", request_id=request_id, retry_after=retry_after, idempotency_key=idempotency_key, retryable=retryable, headers=headers)
    return cls(text[:200] or f"HTTP {status}", status=status, code=f"http_{status}", request_id=request_id, retry_after=retry_after, idempotency_key=idempotency_key, retryable=retryable, headers=headers)
