"""The Phoenix Perpetuals API for Python: REST (sync and async), the account stream and market data, with retries,
idempotency, paging and typed errors.

    from phoenixperps import PhoenixPerps
    client = PhoenixPerps()                     # PHOENIXPERPS_API_KEY from the environment
    order = client.orders.create("SANDBOX-00042-001", {"market": "BTC-USD", "side": "buy", "type": "limit",
                                                       "size": "0.01", "limit_price": "62000"})
"""
from ._core import SDK_VERSION, SPEC_VERSION, AsyncPage, AsyncPhoenixPerps, Page, PhoenixPerps, RawResponse
from ._errors import (APIConnectionError, APITimeoutError, AuthenticationError, ConflictError, NotFoundError,
                      PermissionDeniedError, PhoenixPerpsError, RateLimitError, RuleRejectedError, ServerError,
                      StreamClosedError, ValidationError)
from . import types  # what each call answers, as TypedDicts: from phoenixperps.types import Order
from .helpers import MarketRules, WebhookVerificationError, Webhooks

__version__ = SDK_VERSION
__all__ = [
    "PhoenixPerps", "AsyncPhoenixPerps", "Page", "AsyncPage", "RawResponse", "MarketRules", "Webhooks", "WebhookVerificationError", "types",
    "PhoenixPerpsError", "AuthenticationError", "PermissionDeniedError", "NotFoundError", "ConflictError", "ValidationError",
    "RuleRejectedError", "RateLimitError", "ServerError", "APIConnectionError", "APITimeoutError", "StreamClosedError",
    "SDK_VERSION", "SPEC_VERSION",
]
