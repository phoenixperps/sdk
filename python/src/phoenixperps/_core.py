"""The transport (docs/BEHAVIOUR.md), the same rules as every SDK:

- config: api_key (PHOENIXPERPS_API_KEY; none: the public calls work, any other answers the API's missing_api_key, and
  no Authorization goes out), environment (from the key's prefix; PHOENIXPERPS_ENVIRONMENT; live or sandbox in any
  case; a mismatch fails here, before any request), base_url (PHOENIXPERPS_BASE_URL, else the spec's host; the REST
  address ending in /v1 is taken as its host), timeout 30 s per attempt, max_retries 2, app_info (added to the
  User-Agent), an injectable httpx client
- the key is never printed: repr shows it masked (ppk_test_****abcd)
- retries only a GET, a call with an Idempotency-Key or one the spec marks retry-safe; on a network failure, a timeout or
  an answer the API marks retryable (one without its envelope: 429, 500, 502, 503, 504); after Retry-After when given,
  else full jitter over min(8 s, 0.5 s x 2^n); a Retry-After over 60 s is not slept (RateLimitError with retry_after)
- one Idempotency-Key per logical call (UUID v4 unless given), the same on every attempt
- X-RateLimit-* kept per budget (the operation's scope and the bucket that answered, "trade:bulk") in client.rate_limits, the last in client.rate_limit; with
  nothing left, the next call of an operation that met that budget waits for the request it gives back (60 s / Limit,
  or a 429's Retry-After), never the whole window; a Deprecation header is told once, on any answer
- a list answers a Page (every field of the list: page.history_complete); page.auto_paging_iter(max_items=None) goes
  over every item, each page asked with the first call's options
- a 304 (the caller's If-None-Match matched) is no error: the call answers None (a list call too); a 2xx whose body is
  not JSON (a proxy's page) is an error (http_<status>)
- raw_response=RawResponse() on any call keeps its answer as it came (status, headers, body); client.request(method,
  path) calls any route, also one this version does not know, with the same key, retries and errors
"""
from __future__ import annotations

import asyncio
import json
import os
import platform
import random
import re
import time
import uuid
import weakref
from decimal import Decimal
from typing import Any, AsyncIterator, Dict, Generator, Generic, Iterator, List, Optional, Set, Tuple, TypeVar, cast
from urllib.parse import quote

import httpx

from . import _errors as E
from ._gen import OPS, RESOURCES, SERVERS, SPEC_VERSION, STREAMS, AsyncResources, Resources

SDK_VERSION = "1.3.0"
PREFIX = re.compile(r"^(ppk|ppo)_(live|test)_")
RETRY_AFTER_MAX_S = 60.0
# each client's key, outside the client's own attributes: vars(), repr and pickling never carry it
_KEYS: "weakref.WeakKeyDictionary[Any, Optional[str]]" = weakref.WeakKeyDictionary()


def mask(key: Optional[str]) -> Optional[str]:
    if not key:
        return None
    return f"{key[: key.index('_', 4) + 1]}****{key[-4:]}" if key.count("_") >= 2 else f"****{key[-4:]}"


def _json_default(v: Any) -> Any:
    if isinstance(v, Decimal):
        return format(v, "f")
    raise TypeError(f"{type(v).__name__} is not JSON")


def _decimals_as_strings(body: Any) -> Any:
    """Inputs take int, float or Decimal: each goes out as its plain decimal string (never an exponent: str(Decimal)
    writes 6.2E+4), so no binary noise reaches the wire."""
    if isinstance(body, dict):
        return {k: _decimals_as_strings(v) for k, v in body.items()}
    if isinstance(body, list):
        return [_decimals_as_strings(v) for v in body]
    if isinstance(body, float):
        return format(Decimal(repr(body)), "f")
    if isinstance(body, Decimal):
        return format(body, "f")
    return body


class RawResponse:
    """The answer of a call as it came: status_code, headers (Idempotent-Replayed, ETag, ...), body (bytes), request_id
    and the call's idempotency_key; json() reads the body. A call given raw_response=RawResponse() fills it."""

    def __init__(self) -> None:
        self.status_code: Optional[int] = None
        self.headers: httpx.Headers = httpx.Headers()
        self.body = b""
        self.request_id: Optional[str] = None
        self.idempotency_key: Optional[str] = None

    def json(self) -> Any:
        return json.loads(self.body) if self.body else None

    def __repr__(self) -> str:
        return f"RawResponse(status_code={self.status_code!r}, request_id={self.request_id!r})"


class _Base:
    def __init__(self, api_key: Optional[str] = None, *, environment: Optional[str] = None, base_url: Optional[str] = None,
                 timeout: float = 30.0, max_retries: int = 2, app_info: Optional[str] = None, logger: Any = None,
                 account_stream_url: Optional[str] = None, market_stream_url: Optional[str] = None) -> None:
        key = api_key or os.environ.get("PHOENIXPERPS_API_KEY") or None  # none: the public calls only
        m = PREFIX.match(key) if key else None
        inferred = ("live" if m.group(2) == "live" else "sandbox") if m else None
        env = (environment or inferred or os.environ.get("PHOENIXPERPS_ENVIRONMENT") or "sandbox").lower()
        if env not in ("live", "sandbox"):
            raise E.ValidationError("environment is live or sandbox.", code="invalid_environment")
        if inferred and inferred != env:
            raise E.AuthenticationError(f"This key is a {inferred} key, not a {env} one.", code="wrong_environment", details={"key_environment": inferred})
        _KEYS[self] = key
        self.environment = env
        # the host: the docs' REST address (https://api.phoenixperpsfunding.com/v1) works too, every path has its /v1
        self.base_url = re.sub(r"/v1$", "", (base_url or os.environ.get("PHOENIXPERPS_BASE_URL") or SERVERS[env]).rstrip("/"))
        self.timeout = timeout
        self.max_retries = max_retries
        self.app_info = app_info
        self.logger = logger
        self.rate_limit: Optional[Dict[str, Any]] = None  # the last answer's
        # per budget: the operation's scope and the bucket that answered ("trade:bulk"); the budgets each operation met
        self.rate_limits: Dict[str, Dict[str, Any]] = {}
        self._met: Dict[str, Set[str]] = {}
        self.stream_urls = {"account": account_stream_url or STREAMS["account"][env], "market": market_stream_url or STREAMS["market"]}
        self._warned: Set[str] = set()
        for name, (sync_cls, async_cls) in RESOURCES.items():
            setattr(self, name, (async_cls if isinstance(self, AsyncPhoenixPerps) else sync_cls)(self))

    @property
    def _key(self) -> Optional[str]:
        return _KEYS[self]

    @property
    def masked_key(self) -> Optional[str]:
        return mask(_KEYS[self])

    def __repr__(self) -> str:
        return f"{type(self).__name__}(environment={self.environment!r}, base_url={self.base_url!r}, api_key={self.masked_key!r})"

    __str__ = __repr__

    @property
    def user_agent(self) -> str:
        ua = f"phoenixperps-python/{SDK_VERSION} (python {platform.python_version()}; {platform.system().lower()}) spec/{SPEC_VERSION}"
        return f"{ua} {self.app_info}" if self.app_info else ua

    def _url(self, op: Dict[str, Any], path: Optional[Dict[str, Any]]) -> str:
        def fill(m: "re.Match[str]") -> str:
            v = (path or {}).get(m.group(1))
            if v is None or v == "":
                raise E.ValidationError(f"{m.group(1)} is required.", code="invalid_request")
            return quote(str(v), safe="")
        return self.base_url + re.sub(r"\{([^}]+)\}", fill, op["path"])

    def _headers(self, key: Optional[str], has_body: bool, extra: Optional[Dict[str, str]]) -> Dict[str, str]:
        h = {"accept": "application/json", "user-agent": self.user_agent}
        if _KEYS[self]:
            h["authorization"] = f"Bearer {_KEYS[self]}"
        if has_body:
            h["content-type"] = "application/json"
        if key:
            h["idempotency-key"] = key
        h.update(extra or {})
        return h

    def _remember(self, headers: httpx.Headers, op: Dict[str, Any]) -> None:
        # any answer may say its version is deprecated, with or without the rate-limit headers (Q87)
        dep = headers.get("deprecation")
        if dep and dep not in self._warned:
            self._warned.add(dep)
            if self.logger is not None:
                self.logger.warning("phoenixperps: this API version is deprecated (Deprecation: %s, sunset %s)", dep, headers.get("sunset"))
        limit = headers.get("x-ratelimit-limit")
        if limit is None:
            return
        try:
            reset = float(headers.get("x-ratelimit-reset", ""))
            reset_at: Optional[float] = reset if reset > 31536000 else time.time() + reset  # epoch seconds, or seconds from now
        except ValueError:
            reset_at = None
        scope = headers.get("x-ratelimit-scope")
        remaining = int(float(headers.get("x-ratelimit-remaining", "0") or 0))
        # with nothing left, the next request comes back after a 429's Retry-After, else 60 s / the per-minute limit (Q15)
        after = E.retry_after_s(headers)
        per_token = 60.0 / float(limit) if float(limit) > 0 else 0.0
        next_at = None if remaining > 0 else time.time() + (after if after is not None else per_token)
        budget = f"{op.get('scope') or ''}:{scope or ''}"
        lim = float(limit)
        self.rate_limit = self.rate_limits[budget] = {"limit": int(lim) if lim.is_integer() else lim, "remaining": remaining, "reset_at": reset_at, "scope": scope, "next_at": next_at}
        self._met.setdefault(f"{op.get('scope') or ''}|{op['id']}", set()).add(budget)

    def _pre_wait_s(self, op: Dict[str, Any]) -> float:
        """The wait for the next request of a budget this operation met with nothing left in it, when under 60 s."""
        until = max([(self.rate_limits.get(b) or {}).get("next_at") or 0.0 for b in self._met.get(f"{op.get('scope') or ''}|{op['id']}", ())] or [0.0])
        wait = until - time.time()
        return wait if 0 < wait < 60 else 0.0

    @staticmethod
    def _lost_cancel(op: Dict[str, Any], attempt: int, failure: Any) -> bool:
        """A cancel answered as already canceled or expired: the order no longer works, as asked (its own lost answer
        asked again, or a cancel made elsewhere): the answer is that order (Q14); filled still fails."""
        return op["id"] == "cancelOrder" and getattr(failure, "code", None) == "order_not_working" \
            and isinstance(failure.details, dict) and failure.details.get("status") in ("canceled", "expired")

    @staticmethod
    def _query(query: Optional[Dict[str, Any]]) -> Dict[str, str]:
        return {k: ("true" if v is True else "false" if v is False else str(v)) for k, v in (query or {}).items() if v is not None}

    def _plan(self, op: Dict[str, Any], opts: Dict[str, Any]) -> Tuple[Optional[str], bool, int]:
        # any call carrying a key (an order change given one too) is safe to send again: the API does it once
        key = (opts.get("idempotency_key") or str(uuid.uuid4())) if op["idempotent"] else opts.get("idempotency_key")
        return key, bool(op["retry_safe"] or key), opts.get("max_retries", self.max_retries)

    @staticmethod
    def _keep(res: httpx.Response, key: Optional[str], opts: Dict[str, Any]) -> None:
        raw = opts.get("raw_response")
        if raw is not None:
            raw.status_code, raw.headers, raw.body = res.status_code, res.headers, res.content
            raw.request_id, raw.idempotency_key = res.headers.get("x-request-id"), key

    @staticmethod
    def _read(res: httpx.Response, op: Dict[str, Any], key: Optional[str]) -> Tuple[Any, Optional[E.PhoenixPerpsError]]:
        """(the body, None), or (None, the error it is): a 304 or a 204 is None, the raw call's body is not read, and a
        2xx that is not JSON (a proxy's page, nothing at all) is an error like any other answer."""
        if res.status_code in (304, 204) or (res.is_success and op.get("raw")):
            return None, None
        if res.is_success:
            try:
                return res.json(), None
            except ValueError:
                pass
        return None, E.from_response(res.status_code, res.headers, res.text, key)

    @staticmethod
    def _route(method: str, path: str) -> Dict[str, Any]:
        """The operation of client.request: tried again when a GET or a route the spec marks retry-safe (or, as any
        call, with an idempotency_key given), in that route's budget; its body is answered as it came."""
        if not path.startswith("/"):
            raise E.ValidationError("The path starts with /.", code="invalid_request")
        m = method.upper()
        known = next((o for o in OPS.values() if o["http"] == m and re.fullmatch("[^/]+".join(re.escape(p) for p in re.split(r"\{[^}]+\}", o["path"])), path.split("?")[0])), {})
        # a known route keeps its Idempotency-Key (the same on every attempt) and its retries (Q39)
        return {"id": known.get("id", "request"), "http": m, "path": path, "idempotent": bool(known.get("idempotent")),
                "retry_safe": m == "GET" or bool(known.get("retry_safe")), "paging": None, "scope": known.get("scope"), "raw": True}

    @staticmethod
    def _data(body: Any) -> Any:
        return body["data"] if isinstance(body, dict) and "data" in body else body


class _Fields:
    """Every other field of the list answer is the page's too, as the API names it (page.history_complete)."""

    def __getattr__(self, name: str) -> Any:
        body = self.__dict__.get("_body") or {}
        if name in body:
            return body[name]
        raise AttributeError(f"{type(self).__name__!r} object has no attribute {name!r}")


T = TypeVar("T")  # a list's item: its TypedDict of phoenixperps.types (Page[Order])


class Page(_Fields, Generic[T]):
    """One page of a list: data, has_more, next_cursor (and every other field of the list); next_page() and
    auto_paging_iter(max_items=None), each next page asked with the first call's options. Iterated (for x in page),
    every item across pages, as the async client: the call's max_items caps it."""

    def __init__(self, client: "PhoenixPerps", op: Dict[str, Any], call: Dict[str, Any], body: Dict[str, Any], opts: Optional[Dict[str, Any]] = None, max_items: Optional[int] = None) -> None:
        self.data: List[T] = body.get("data") or []
        self.has_more = bool(body.get("has_more"))
        self.next_cursor = body.get("next_cursor")
        self.as_of = body.get("as_of")
        self._client, self._op, self._call, self._opts, self._body = client, op, call, opts, body
        self._max = max_items

    def next_page(self) -> Optional["Page[T]"]:
        if not self.has_more or not self.next_cursor:
            return None
        query = dict(self._call.get("query") or {})
        query[self._op["paging"]["param"]] = self.next_cursor
        return self._client._list(self._op, {**self._call, "query": query}, self._opts)

    def auto_paging_iter(self, max_items: Optional[int] = None) -> Iterator[T]:
        cap = max_items if max_items is not None else self._max
        page: Optional[Page[T]] = self
        n = 0
        while page is not None:
            for item in page.data:
                if cap is not None and n >= cap:
                    return
                n += 1
                yield item
            if cap is not None and n >= cap:
                return
            page = page.next_page()

    def __iter__(self) -> Iterator[T]:
        return self.auto_paging_iter()


class PhoenixPerps(_Base, Resources):
    """The client: PhoenixPerps(api_key=..., environment=...) or PhoenixPerps() with PHOENIXPERPS_API_KEY set."""

    def __init__(self, api_key: Optional[str] = None, *, http_client: Optional[httpx.Client] = None, **kw: Any) -> None:
        super().__init__(api_key, **kw)
        self._http = http_client or httpx.Client()

    def close(self) -> None:
        self._http.close()

    def __enter__(self) -> "PhoenixPerps":
        return self

    def __exit__(self, *exc: Any) -> None:
        self.close()

    def _send(self, op: Dict[str, Any], call: Dict[str, Any], opts: Dict[str, Any]) -> Any:
        key, retryable, max_retries = self._plan(op, opts)
        body = call.get("body")
        content = None if body is None else json.dumps(_decimals_as_strings(body), default=_json_default)
        attempt = 0
        while True:
            wait = self._pre_wait_s(op)
            if wait:
                time.sleep(wait)
            res = None
            try:
                res = self._http.request(op["http"], self._url(op, call.get("path")), params=self._query(call.get("query")),
                                         headers=self._headers(key, content is not None, opts.get("headers")), content=content,
                                         timeout=opts.get("timeout", self.timeout))
            except httpx.TimeoutException:
                failure: E.PhoenixPerpsError = E.APITimeoutError(f"No answer within {opts.get('timeout', self.timeout)} s.", code="timeout", idempotency_key=key, retryable=True)
            except httpx.HTTPError as err:
                failure = E.APIConnectionError(str(err)[:200], code="connection_failed", idempotency_key=key, retryable=True)
            if res is not None:
                self._remember(res.headers, op)
                self._keep(res, key, opts)
                answer, refused = self._read(res, op, key)
                if refused is None:
                    return answer
                failure = refused
            if self._lost_cancel(op, attempt, failure):
                return self._send(OPS["getOrder"], {"path": call.get("path")}, {**opts, "idempotency_key": None})
            if not (retryable and attempt < max_retries and failure.retryable):
                raise failure
            ra = E.retry_after_s(res.headers) if res is not None else None
            if ra is not None and ra > RETRY_AFTER_MAX_S:
                raise failure
            time.sleep(ra if ra is not None else random.random() * min(8.0, 0.5 * 2 ** attempt))
            attempt += 1

    def request(self, method: str, path: str, *, query: Optional[Dict[str, Any]] = None, body: Any = None, **opts: Any) -> RawResponse:
        """Any route, also one this SDK version does not know (v1 only grows): client.request("GET", "/v1/status").json()."""
        raw = RawResponse()
        self._send(self._route(method, path), {"query": query, "body": body}, {**opts, "raw_response": raw})
        return raw

    def _one(self, op: Dict[str, Any], path: Optional[Dict[str, Any]] = None, body: Any = None, query: Any = None, opts: Optional[Dict[str, Any]] = None) -> Any:
        return self._data(self._send(op, {"path": path, "body": body, "query": query}, opts or {}))

    def _list(self, op: Dict[str, Any], call: Dict[str, Any], opts: Optional[Dict[str, Any]] = None) -> Optional[Page[Any]]:
        body = self._send(op, call, opts or {})
        return None if body is None else Page(self, op, call, body, opts)  # None: a 304, nothing changed

    def _page(self, op: Dict[str, Any], path: Optional[Dict[str, Any]] = None, query: Any = None, opts: Optional[Dict[str, Any]] = None) -> Optional[Page[Any]]:
        o = dict(opts or {})
        max_items = o.pop("max_items", None)
        page = self._list(op, {"path": path, "query": query}, o)
        if page is not None:
            page._max = max_items
        return page

    def account_stream(self, **kw: Any) -> Any:
        from .streams import AccountStream
        return AccountStream(self, **kw)

    def market_stream(self, **kw: Any) -> Any:
        from .streams import MarketStream
        return MarketStream(self, **kw)


class AsyncPage(_Fields, Generic[T]):
    """One page of a list (async): every item across pages with async for (like Page's for, Q91), this page's own data in
    page.data (and with a plain for), every other field of the list, next_page() and auto_paging_iter(max_items=None)."""

    def __init__(self, client: "AsyncPhoenixPerps", op: Dict[str, Any], call: Dict[str, Any], body: Dict[str, Any], opts: Optional[Dict[str, Any]] = None) -> None:
        self.data: List[T] = body.get("data") or []
        self.has_more = bool(body.get("has_more"))
        self.next_cursor = body.get("next_cursor")
        self.as_of = body.get("as_of")
        self._client, self._op, self._call, self._opts, self._body = client, op, call, opts, body

    async def next_page(self) -> Optional["AsyncPage[T]"]:
        if not self.has_more or not self.next_cursor:
            return None
        query = dict(self._call.get("query") or {})
        query[self._op["paging"]["param"]] = self.next_cursor
        return await self._client._list(self._op, {**self._call, "query": query}, self._opts)

    async def auto_paging_iter(self, max_items: Optional[int] = None) -> AsyncIterator[T]:
        page: Optional[AsyncPage[T]] = self
        n = 0
        while page is not None:
            for item in page.data:
                if max_items is not None and n >= max_items:
                    return
                n += 1
                yield item
            if max_items is not None and n >= max_items:
                return
            page = await page.next_page()

    def __aiter__(self) -> AsyncIterator[T]:
        return self.auto_paging_iter(self.__dict__.get("_max"))

    def __iter__(self) -> Iterator[T]:
        return iter(self.data)


class _AsyncPageCall(Generic[T]):
    """A list call (async): awaited, its first page; iterated with async for, every item across pages."""

    def __init__(self, load: Any, max_items: Optional[int]) -> None:
        self._load, self._max, self._task = load, max_items, None

    def _first(self) -> Any:
        if self._task is None:
            self._task = asyncio.ensure_future(self._load())
        return self._task

    def __await__(self) -> Generator[Any, None, AsyncPage[T]]:
        # None only on a 304 (an If-None-Match the caller sent), typed as the sync list is: the answer (Q90)
        return cast(Generator[Any, None, AsyncPage[T]], self._first().__await__())

    async def __aiter__(self) -> AsyncIterator[T]:
        page = await self._first()
        if page is not None:  # None: a 304, nothing changed
            async for item in page.auto_paging_iter(self._max):
                yield item


class AsyncPhoenixPerps(_Base, AsyncResources):
    """The async client: await client.orders.create(...); async for order in client.orders.list(...)."""

    def __init__(self, api_key: Optional[str] = None, *, http_client: Optional[httpx.AsyncClient] = None, **kw: Any) -> None:
        super().__init__(api_key, **kw)
        self._http = http_client or httpx.AsyncClient()

    async def aclose(self) -> None:
        await self._http.aclose()

    async def __aenter__(self) -> "AsyncPhoenixPerps":
        return self

    async def __aexit__(self, *exc: Any) -> None:
        await self.aclose()

    async def _send(self, op: Dict[str, Any], call: Dict[str, Any], opts: Dict[str, Any]) -> Any:
        key, retryable, max_retries = self._plan(op, opts)
        body = call.get("body")
        content = None if body is None else json.dumps(_decimals_as_strings(body), default=_json_default)
        attempt = 0
        while True:
            wait = self._pre_wait_s(op)
            if wait:
                await asyncio.sleep(wait)
            res = None
            try:
                res = await self._http.request(op["http"], self._url(op, call.get("path")), params=self._query(call.get("query")),
                                               headers=self._headers(key, content is not None, opts.get("headers")), content=content,
                                               timeout=opts.get("timeout", self.timeout))
            except httpx.TimeoutException:
                failure: E.PhoenixPerpsError = E.APITimeoutError(f"No answer within {opts.get('timeout', self.timeout)} s.", code="timeout", idempotency_key=key, retryable=True)
            except httpx.HTTPError as err:
                failure = E.APIConnectionError(str(err)[:200], code="connection_failed", idempotency_key=key, retryable=True)
            if res is not None:
                self._remember(res.headers, op)
                self._keep(res, key, opts)
                answer, refused = self._read(res, op, key)
                if refused is None:
                    return answer
                failure = refused
            if self._lost_cancel(op, attempt, failure):
                return await self._send(OPS["getOrder"], {"path": call.get("path")}, {**opts, "idempotency_key": None})
            if not (retryable and attempt < max_retries and failure.retryable):
                raise failure
            ra = E.retry_after_s(res.headers) if res is not None else None
            if ra is not None and ra > RETRY_AFTER_MAX_S:
                raise failure
            await asyncio.sleep(ra if ra is not None else random.random() * min(8.0, 0.5 * 2 ** attempt))
            attempt += 1

    async def request(self, method: str, path: str, *, query: Optional[Dict[str, Any]] = None, body: Any = None, **opts: Any) -> RawResponse:
        """Any route, also one this SDK version does not know: (await client.request("GET", "/v1/status")).json()."""
        raw = RawResponse()
        await self._send(self._route(method, path), {"query": query, "body": body}, {**opts, "raw_response": raw})
        return raw

    async def _one(self, op: Dict[str, Any], path: Optional[Dict[str, Any]] = None, body: Any = None, query: Any = None, opts: Optional[Dict[str, Any]] = None) -> Any:
        return self._data(await self._send(op, {"path": path, "body": body, "query": query}, opts or {}))

    async def _list(self, op: Dict[str, Any], call: Dict[str, Any], opts: Optional[Dict[str, Any]] = None) -> Optional[AsyncPage[Any]]:
        body = await self._send(op, call, opts or {})
        return None if body is None else AsyncPage(self, op, call, body, opts)  # None: a 304, nothing changed

    def _page(self, op: Dict[str, Any], path: Optional[Dict[str, Any]] = None, query: Any = None, opts: Optional[Dict[str, Any]] = None) -> _AsyncPageCall[Any]:
        o = dict(opts or {})
        max_items = o.pop("max_items", None)
        return _AsyncPageCall(lambda: self._list(op, {"path": path, "query": query}, o), max_items)

    def account_stream(self, **kw: Any) -> Any:
        from .streams import AccountStream
        return AccountStream(self, **kw)

    def market_stream(self, **kw: Any) -> Any:
        from .streams import MarketStream
        return MarketStream(self, **kw)


__all__ = ["PhoenixPerps", "AsyncPhoenixPerps", "Page", "AsyncPage", "RawResponse", "SDK_VERSION", "SPEC_VERSION", "OPS", "mask"]
