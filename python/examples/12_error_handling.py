"""12: what failures look like: each has a class by family, the API's code, the request id to quote to support, and
details. A write whose outcome is unknown (a timeout) is repeated with the same Idempotency-Key, never a new one.

    python examples/12_error_handling.py
"""
import time
import uuid
from typing import Any, Dict

from phoenixperps import (APITimeoutError, NotFoundError, PhoenixPerps, PhoenixPerpsError, RateLimitError,
                          RuleRejectedError, ValidationError)

client = PhoenixPerps()
account = client.accounts.list().data[0]


def show(err: PhoenixPerpsError) -> None:
    print(f"{type(err).__name__}: {err.code} (HTTP {err.status}, {err.request_id}) {err}")
    if err.details:
        print(f"  details: {err.details}")


try:
    client.orders.get(account["id"], "ord_does_not_exist")
except NotFoundError as err:
    show(err)

try:
    client.orders.preview(account["id"], {"market": "BTC-USD", "side": "buy", "type": "market", "size": "0.0000001"})
except (ValidationError, RuleRejectedError) as err:
    show(err)


def place_once(body: Dict[str, Any]) -> Any:
    """A write that may or may not have happened: the same key again is safe, a new key could place it twice."""
    key = str(uuid.uuid4())
    for attempt in range(1, 4):
        try:
            return client.orders.create(account["id"], body, idempotency_key=key)
        except APITimeoutError:
            if attempt == 3:
                raise  # still unknown after the SDK's own retries and ours: read the orders before anything else
        except RateLimitError as err:
            if attempt == 3 or err.retry_after is None:
                raise
            time.sleep(err.retry_after)
    raise RuntimeError("unreachable")
