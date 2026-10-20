"""The two read-only streams (docs/BEHAVIOUR.md "Streams"; orders always go through REST), async (websockets).

AccountStream: the key on the upgrade; hello, one snapshot per account, snapshot_end, then events (each once: replays
are told apart by their event id; each keeps its frame's seq and replayed). It reconnects by itself with
?resume=<last event id>, waiting min(30 s, 0.5 s x 2^n) with jitter (0 to 3 s after 1012, the server restarting; the
Retry-After of an upgrade refused with 429 or 5xx), after any failure (a cut handshake, a frame that is not a JSON
object), one log line each (the client's logger, else logging "phoenixperps"). 4001 (the key ended) stops it with
AuthenticationError, any other refused upgrade with the API's own error, an error frame (too_many_accounts,
account_not_found: the subscribe, sent again as it is, would be refused again) with ValidationError (code, details,
account_id); each sets ready. 4008 (too far behind) reconnects without resume; 4013 waits longer. A resume the server
cannot cover (its notice) or a 4008 is a gap, told as {"type": "notice", "code": "resume_gap", "after": <the last event
told>}: read GET /v1/events?after= it. Risk frames (risk=True) are told as they come. Three heartbeats without a frame
mean a dead connection. ping(); unsubscribe(accounts) keeps them out of every later connection's subscribe.

    async with client.account_stream(risk=True) as stream:
        async for event in stream:          # events, then by their type: "risk" frames and "notice" (a gap)
            ...                             # stream.snapshots holds the last snapshot per account

MarketStream: await stream.subscribe("ticker", ["BTC-USD"], interval_ms=250); async for frame in stream: ...
Every subscription the server holds is made again after a reconnect, and an ask still waiting is sent again; three
heartbeats without a frame mean a dead connection; refused upgrades as the account stream's. The server knows a
subscription by its channel and symbol (and interval for candles), and so does this stream: an unsubscribe needs no
other option (without an interval: every interval; without symbols: the whole channel), and a symbol subscribed again
keeps only its latest options. list() and ping(); stream.limits are the hello's.
"""
from __future__ import annotations

import asyncio
import json
import logging
import random
from collections import OrderedDict, deque
from typing import Any, Deque, Dict, List, Optional, Tuple
from urllib.parse import quote

from . import _errors as E

LRU = 2048
LOG = logging.getLogger("phoenixperps")


def _backoff(n: int) -> float:
    return random.random() * min(30.0, 0.5 * 2 ** n)


def _connect(url: str, client: Any) -> Any:
    from websockets.asyncio.client import connect  # websockets 13 still names its legacy client websockets.connect
    auth = {"authorization": f"Bearer {client._key}"} if client._key else {}  # no key: no Authorization
    return connect(url, additional_headers={**auth, "user-agent": client.user_agent}, max_size=2 ** 22)


def _refused(err: Any) -> Tuple[Optional[E.PhoenixPerpsError], Optional[float]]:
    """An upgrade refused: 429 or 5xx is tried again, (None, its Retry-After); any other ends the stream with the API's
    own error, as a REST call would, (the error, None)."""
    r = err.response
    failure = E.from_response(r.status_code, r.headers, (r.body or b"").decode("utf-8", "replace"), None)
    return (None, failure.retry_after) if r.status_code == 429 or r.status_code >= 500 else (failure, None)


def _again(client: Any, stream: str, wait: float, cause: str) -> float:
    """The wait before a reconnect, told in one log line with its cause."""
    (client.logger or LOG).info("phoenixperps: the %s stream reconnects in %.1f s (%s)", stream, wait, cause)
    return wait


class AccountStream:
    def __init__(self, client: Any, *, accounts: Optional[List[str]] = None, risk: bool = False, resume: Optional[str] = None) -> None:
        self._client = client
        self.accounts = accounts
        self.risk = risk
        self.last_event_id = resume
        self.snapshots: Dict[str, Any] = {}
        self.risks: Dict[str, Any] = {}
        self._seen: "OrderedDict[str, None]" = OrderedDict()
        self._queue: "asyncio.Queue[Any]" = asyncio.Queue()
        self._task: Optional[asyncio.Task] = None
        self._closed = False
        self._ws: Any = None
        self._dropped: set = set()  # accounts unsubscribed: no later subscribe names them
        # the asks waiting, by the answer's type: the account stream's carry no id, their answers come in order
        self._asks: Dict[str, Deque[Tuple[Any, Dict[str, Any]]]] = {"pong": deque(), "unsubscribed": deque()}
        self.ready = asyncio.Event()

    async def __aenter__(self) -> "AccountStream":
        self.start()
        return self

    async def __aexit__(self, *exc: Any) -> None:
        await self.close()

    def start(self) -> "AccountStream":
        if self._task is None:
            self._task = asyncio.ensure_future(self._run())
        return self

    async def close(self) -> None:
        self._closed = True
        if self._task is not None:
            self._task.cancel()
        self._settle(None)
        await self._queue.put(None)

    def _subscribe(self, reached: Optional[List[str]]) -> Dict[str, Any]:
        """A connection's subscribe: the accounts asked (none: every one the key reaches), less those unsubscribed."""
        msg: Dict[str, Any] = {"op": "subscribe"}
        accounts = self.accounts
        if not accounts and self._dropped:  # every account the hello names, less those
            accounts = reached if reached is not None else list(self.snapshots)
        if accounts:
            msg["accounts"] = [a for a in accounts if a.upper() not in self._dropped]
        if self.risk:
            msg["risk"] = True
        return msg

    async def _run(self) -> None:
        import websockets
        attempt = 0
        resume = True
        while not self._closed:
            url = self._client.stream_urls["account"]
            if resume and self.last_event_id:
                url = f"{url}?resume={quote(self.last_event_id)}"
            resume = True  # only the connection after a 4008 goes without
            wait: Optional[float] = None
            cause = "the connection ended"
            try:
                async with _connect(url, self._client) as ws:
                    heartbeat = 15.0
                    while True:
                        raw = await asyncio.wait_for(ws.recv(), timeout=heartbeat * 3)
                        f = json.loads(raw)
                        t = f.get("type")
                        if t == "hello":
                            attempt = 0
                            heartbeat = (f.get("heartbeat_ms") or 15000) / 1000
                            await ws.send(json.dumps(self._subscribe(f.get("accounts"))))
                            self._ws = ws  # an ask from now on goes at once; those still waiting go again here
                            for asks in self._asks.values():
                                for _, msg in list(asks):
                                    await ws.send(json.dumps(msg))
                        elif t == "snapshot":
                            self.snapshots[f["account_id"]] = f
                        elif t == "snapshot_end":
                            self.ready.set()
                        elif t == "error":
                            details = {**(f.get("details") or {}), **({"account_id": f["account_id"]} if f.get("account_id") else {})}
                            await self._end(E.ValidationError(f.get("message") or f.get("code") or "", code=f.get("code") or "stream_refused",
                                                              details=details or None, account_id=f.get("account_id")))
                            return
                        elif t == "risk":
                            self.risks[f["account_id"]] = f
                            await self._queue.put(f)
                        elif t == "notice":  # resume_gap: the events after f["after"] may be missing here
                            await self._queue.put(f)
                        elif t in self._asks:
                            if self._asks[t]:
                                fut, _ = self._asks[t].popleft()
                                if not fut.done():
                                    fut.set_result(f)
                        elif t == "event":
                            e = f["event"]
                            if e["id"] in self._seen:
                                continue
                            self._seen[e["id"]] = None
                            if len(self._seen) > LRU:
                                self._seen.popitem(last=False)
                            self.last_event_id = e["id"]
                            await self._queue.put({**e, "seq": f.get("seq"), "replayed": bool(f.get("replayed"))})
            except asyncio.CancelledError:
                return
            except websockets.exceptions.InvalidStatus as err:
                failure, wait = _refused(err)
                if failure is not None:
                    await self._end(failure)
                    return
                cause = f"upgrade refused with {err.response.status_code}"
            except websockets.exceptions.ConnectionClosed as err:
                code = err.rcvd.code if err.rcvd else None
                cause = f"closed with {code}" if code else "the connection dropped"
                if code == 4001:
                    await self._end(E.AuthenticationError("The key or session of this stream ended.", code="stream_credential_ended"))
                    return
                if code == 4008:
                    resume = False  # too far behind: the events since the last one told are a gap
                    await self._queue.put({"type": "notice", "code": "resume_gap", "after": self.last_event_id})
                elif code == 4013:
                    wait = 30.0 + _backoff(attempt)
                elif code == 1012:
                    wait = random.random() * 3
            except asyncio.TimeoutError:
                cause = "timed out (no frame in three heartbeats)"
            except Exception as err:  # anything else (a cut handshake, a frame not a JSON object): reconnect
                cause = f"{type(err).__name__}: {err}"[:200]
            finally:
                self._ws = None
            if self._closed:
                return
            await asyncio.sleep(_again(self._client, "account", wait if wait is not None else _backoff(attempt), cause))
            attempt += 1

    def _settle(self, err: Optional[Exception]) -> None:
        """The asks still waiting fail with the error that ended the stream (canceled when it is closed)."""
        for asks in self._asks.values():
            while asks:
                fut, _ = asks.popleft()
                if fut.done():
                    continue
                if err is None:
                    fut.cancel()
                else:
                    fut.set_exception(err)

    async def _end(self, err: Exception) -> None:
        self._closed = True
        self.ready.set()  # a waiter on ready goes on, and meets the error in the iteration
        self._settle(err)
        await self._queue.put(err)

    async def _ask(self, answer: str, msg: Dict[str, Any]) -> Any:
        self.start()
        if self._closed:
            raise E.StreamClosedError("The stream is closed.", code="stream_closed")
        fut: "asyncio.Future[Any]" = asyncio.get_running_loop().create_future()
        self._asks[answer].append((fut, msg))
        if self._ws is not None:
            try:
                await self._ws.send(json.dumps(msg))
            except Exception:  # the connection dropped: the message goes again on the next one
                pass
        return await fut

    async def ping(self) -> Any:
        """The server's pong (its time): this connection answers."""
        return await self._ask("pong", {"op": "ping"})

    async def unsubscribe(self, accounts: List[str]) -> Any:
        """Stop following accounts, on this connection and every later one (the subscribe sent again after a reconnect
        names what is left); the answer is the server's unsubscribed frame."""
        self._dropped.update(a.upper() for a in accounts)
        for kept in (self.snapshots, self.risks):
            for a in [a for a in kept if a.upper() in self._dropped]:
                del kept[a]
        return await self._ask("unsubscribed", {"op": "unsubscribe", "accounts": list(accounts)})

    def __aiter__(self) -> "AccountStream":
        self.start()
        return self

    async def __anext__(self) -> Any:
        item = await self._queue.get()
        if item is None:
            raise StopAsyncIteration
        if isinstance(item, Exception):
            self._closed = True
            raise item
        return item


class MarketStream:
    def __init__(self, client: Any) -> None:
        self._client = client
        self._subs: Dict[str, Dict[str, Any]] = {}
        self._queue: "asyncio.Queue[Any]" = asyncio.Queue(maxsize=1024)
        self._ws: Any = None
        self._open = asyncio.Event()
        self._pending: Dict[int, Any] = {}  # id -> (future, message): sent again on the next connection until answered
        self._id = 0
        self._task: Optional[asyncio.Task] = None
        self._closed = False
        self.limits: Any = None

    async def __aenter__(self) -> "MarketStream":
        self.start()
        return self

    async def __aexit__(self, *exc: Any) -> None:
        await self.close()

    def start(self) -> "MarketStream":
        if self._task is None:
            self._task = asyncio.ensure_future(self._run())
        return self

    async def close(self) -> None:
        self._closed = True
        if self._task is not None:
            self._task.cancel()
        for fut, _ in self._pending.values():
            fut.cancel()
        self._pending.clear()
        await self._put(None)

    async def _put(self, item: Any) -> None:
        if self._queue.full():
            self._queue.get_nowait()  # a slow reader loses the oldest frame, never the connection
        await self._queue.put(item)

    async def _run(self) -> None:
        import websockets
        attempt = 0
        while not self._closed:
            wait: Optional[float] = None
            cause = "the connection ended"
            try:
                async with _connect(self._client.stream_urls["market"], self._client) as ws:
                    self._ws = ws
                    heartbeat = 15.0
                    while True:
                        f = json.loads(await asyncio.wait_for(ws.recv(), timeout=heartbeat * 3))
                        t = f.get("type")
                        if t == "hello":
                            attempt = 0
                            heartbeat = (f.get("heartbeat_ms") or 15000) / 1000
                            self.limits = f.get("limits")
                            # every subscription the server took, again, as it was; then the asks still waiting
                            for s in self._subs.values():
                                self._id += 1
                                await ws.send(json.dumps({"op": "subscribe", "id": self._id, "channel": s["channel"], **({} if s["channel"] == "status" else {"symbols": sorted(s["symbols"])}), **s["params"]}))
                            for _, msg in list(self._pending.values()):
                                await ws.send(json.dumps(msg))
                            self._open.set()
                        elif t in ("subscribed", "unsubscribed", "error", "list", "pong"):
                            fut, _ = self._pending.pop(f.get("id"), (None, None))
                            if fut is not None and not fut.done():
                                if t == "error":
                                    fut.set_exception(E.ValidationError(f.get("message") or f.get("code"), code=f.get("code") or "stream_refused", details=f.get("details")))
                                else:
                                    fut.set_result(f)
                        elif t != "heartbeat":
                            await self._put(f)
            except asyncio.CancelledError:
                return
            except websockets.exceptions.InvalidStatus as err:
                failure, wait = _refused(err)
                if failure is not None:
                    await self._end(failure)
                    return
                cause = f"upgrade refused with {err.response.status_code}"
            except websockets.exceptions.ConnectionClosed as err:
                code = err.rcvd.code if err.rcvd else None
                cause = f"closed with {code}" if code else "the connection dropped"
                if code == 4001:
                    await self._end(E.AuthenticationError("The key of this stream ended.", code="stream_credential_ended"))
                    return
                if code == 1012:
                    wait = random.random() * 3
            except asyncio.TimeoutError:
                cause = "timed out (no frame in three heartbeats)"
            except Exception as err:  # anything else (a cut handshake, a frame not a JSON object): reconnect
                cause = f"{type(err).__name__}: {err}"[:200]
            self._open.clear()
            if self._closed:
                return
            await asyncio.sleep(_again(self._client, "market", wait if wait is not None else _backoff(attempt), cause))
            attempt += 1

    async def _end(self, err: Exception) -> None:
        self._closed = True
        for fut, _ in self._pending.values():
            if not fut.done():
                fut.set_exception(err)
        self._pending.clear()
        await self._put(err)

    async def _ask(self, msg: Dict[str, Any]) -> Any:
        self.start()
        if self._closed:
            raise E.StreamClosedError("The stream is closed.", code="stream_closed")
        self._id += 1
        message = {**msg, "id": self._id}
        fut: "asyncio.Future[Any]" = asyncio.get_running_loop().create_future()
        self._pending[self._id] = (fut, message)
        if self._open.is_set():
            try:
                await self._ws.send(json.dumps(message))
            except Exception:  # the connection dropped: the message goes again on the next one
                pass
        return await fut

    def _drop(self, channel: str, symbols: Optional[List[str]], interval: Any) -> None:
        """The symbols (none: all) leave every subscription of the channel (candles: of that interval, none: of every
        interval; status: it goes whole), whatever its other options: the server knows a subscription by channel and
        symbol (and interval) only."""
        gone = {x.upper() for x in symbols or []}
        for key, s in list(self._subs.items()):
            if s["channel"] == channel and (interval is None or s["params"].get("interval") == interval):
                s["symbols"] = s["symbols"] - gone if gone else set()
                if not s["symbols"]:
                    del self._subs[key]

    async def subscribe(self, channel: str, symbols: Optional[List[str]] = None, **params: Any) -> Any:
        reply = await self._ask({"op": "subscribe", "channel": channel, **({} if channel == "status" else {"symbols": symbols or []}), **params})
        # what the server took (its reply's symbols) is made again after a reconnect, with these options (the latest
        # ones, as on the server); what it refused (rejected) is not
        got = reply.get("symbols") if isinstance(reply, dict) else None
        took = [x.upper() for x in (got if isinstance(got, list) else symbols or [])]
        if channel != "status" and not took:
            return reply
        self._drop(channel, took, params.get("interval"))
        s = self._subs.setdefault(f"{channel}|{json.dumps(params, sort_keys=True)}", {"channel": channel, "symbols": set(), "params": params})
        s["symbols"].update(took)
        return reply

    async def unsubscribe(self, channel: str, symbols: Optional[List[str]] = None, **params: Any) -> Any:
        """The answer: the markets it ended (symbols) and those that were not followed (rejected: not_subscribed)."""
        interval = params.get("interval") if channel == "candles" else None
        self._drop(channel, symbols, interval)
        return await self._ask({"op": "unsubscribe", "channel": channel, **({} if channel == "status" else {"symbols": symbols or []}),
                                **({"interval": interval} if interval else {})})

    async def ping(self) -> Any:
        """The server's pong (its time): this connection answers."""
        return await self._ask({"op": "ping"})

    async def list(self) -> Any:
        """The subscriptions the server holds on this connection: its list frame (subscriptions)."""
        return await self._ask({"op": "list"})

    def __aiter__(self) -> "MarketStream":
        self.start()
        return self

    async def __anext__(self) -> Any:
        item = await self._queue.get()
        if item is None:
            raise StopAsyncIteration
        if isinstance(item, Exception):
            raise item
        return item
