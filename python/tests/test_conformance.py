"""The conformance suite (conformance/scenarios.json) through the Python SDK, sync and async, against the mock server
(node conformance/mock-server.js): retries, idempotency keys, Retry-After, errors, paging, open enums, decimals, the
rate-limit wait; the local checks (wrong environment, the key never printed, webhook signatures, the keyless client);
the rules and webhook vectors; the streams. Every scenario is played: a key or step this runner does not know fails it.
Last, the SDK's own scenarios the shared suite does not hold (each names the audit issue or BEHAVIOUR.md rule it pins)."""
from __future__ import annotations

import asyncio
import hashlib
import hmac
import inspect
import json
import logging
import pathlib
import pickle
import re
import subprocess
import urllib.request
from decimal import Decimal

import pytest

import phoenixperps as pp
from phoenixperps._errors import from_response
from phoenixperps._gen import OPS, RESOURCES

ROOT = pathlib.Path(__file__).resolve().parents[2]
S = json.loads((ROOT / "conformance" / "scenarios.json").read_text(encoding="utf-8"))
SNAKE = lambda s: re.sub(r"([a-z0-9])([A-Z])", r"\1_\2", s).lower()


@pytest.fixture(scope="module")
def mock():
    proc = subprocess.Popen(["node", str(ROOT / "conformance" / "mock-server.js"), "0"], stdout=subprocess.PIPE, text=True)
    url = proc.stdout.readline().strip()
    yield url
    proc.terminate()


def load(url, exchanges=(), connections=()):
    req = urllib.request.Request(f"{url}/__scenario", data=json.dumps({"exchanges": list(exchanges), "connections": list(connections)}).encode(), method="POST")
    urllib.request.urlopen(req).read()


def requests(url):
    return json.loads(urllib.request.urlopen(f"{url}/__requests").read())


def op_of(call):
    """The operation behind a resource method (the generated classes name it in their source)."""
    cls = RESOURCES[SNAKE(call["resource"])][0]
    import inspect
    src = inspect.getsource(getattr(cls, SNAKE(call["method"])))
    return OPS[re.search(r'OPS\["(\w+)"\]', src).group(1)]


def args_of(call):
    assert set(call) <= {"resource", "method", "path", "body", "query", "iterate", "max_items"}, f"call keys this runner does not play: {set(call)}"
    op = op_of(call)
    args = [call["path"][m] for m in re.findall(r"\{([^}]+)\}", op["path"])]
    import inspect
    params = inspect.signature(getattr(RESOURCES[SNAKE(call["resource"])][0], SNAKE(call["method"]))).parameters
    if "body" in params:
        args.append(call.get("body"))
    if "query" in params:
        args.append(call.get("query"))
    return args


def pick(obj, want):
    return {k: pick(obj.get(k, {}), v) if isinstance(v, dict) else obj.get(k) for k, v in want.items()}


# what the runners play (scenarios.json "vocabulary"); anything else fails the scenario, never skipped
EXPECT = {"ok", "requests", "result", "error", "items", "cursors", "first_body", "min_elapsed_ms", "checks", "page_fields", "paths"}
ERROR = {"class", "code", "status", "retry_after_ms", "order_id", "request_id", "retryable", "doc_url"}


def check(url, sc, result, error, items, lang="python"):
    got = requests(url)
    x = sc["expect"]
    assert set(x) <= EXPECT, f"expect keys this runner does not play: {set(x) - EXPECT}"
    assert len(got) == x["requests"]
    if "ok" in x:
        assert (error is None) == x["ok"], error
    if "error" in x:
        w = x["error"]
        assert set(w) <= ERROR, f"error keys this runner does not play: {set(w) - ERROR}"
        assert error is not None, "an error"
        if "class" in w:
            assert type(error) is getattr(pp, w["class"])
        assert error.code == w["code"]
        if "retry_after_ms" in w:
            assert error.retry_after * 1000 == w["retry_after_ms"]
        for k in ("status", "order_id", "request_id", "retryable", "doc_url"):
            if k in w:
                assert getattr(error, k) == w[k], k
    else:
        assert error is None, error
        if "result" in x:
            if x["result"] is None:
                assert result is None  # the call answered nothing (a 304)
            else:
                assert pick(result, x["result"]) == x["result"]
        if "items" in x:
            assert items == x["items"]
        if "page_fields" in x:
            assert {k: getattr(result, k) for k in x["page_fields"]} == x["page_fields"]
    if "paths" in x:
        assert [r["path"] for r in got] == x["paths"]
    if "cursors" in x:
        assert [r["query"].get("cursor") for r in got] == x["cursors"]
    if "first_body" in x:
        assert pick(got[0]["body"], x["first_body"]) == x["first_body"]
    if "min_elapsed_ms" in x:
        assert got[-1]["at"] - got[0]["at"] >= x["min_elapsed_ms"]
    for c in x.get("checks", []):
        assert c in ("bearer", "user_agent", "same_idempotency_key"), f"a check this runner does not play: {c}"
        if c == "bearer":
            assert got[0]["headers"]["authorization"] == f"Bearer {S['key']}"
        if c == "user_agent":
            assert re.match(rf"^phoenixperps-{lang}/\d+\.\d+\.\d+ \(.+\) spec/", got[0]["headers"]["user-agent"])
        if c == "same_idempotency_key":
            keys = {r["headers"]["idempotency-key"] for r in got}
            assert len(keys) == 1
            assert re.match(r"^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$", keys.pop())


def base_url(mock, sc):
    """The mock, or the mock plus the scenario's base_url_suffix (the REST address as the docs write it)."""
    client = sc.get("client") or {}
    assert set(client) <= {"base_url_suffix"}, f"client keys this runner does not play: {set(client)}"
    return mock + client.get("base_url_suffix", "")


@pytest.mark.parametrize("sc", S["scenarios"], ids=[s["name"] for s in S["scenarios"]])
def test_sync(mock, sc):
    load(mock, sc["exchanges"])
    client = pp.PhoenixPerps(S["key"], base_url=base_url(mock, sc), max_retries=2)
    result = error = items = None
    try:
        for call in sc.get("calls") or [sc["call"]]:
            fn = getattr(getattr(client, SNAKE(call["resource"])), SNAKE(call["method"]))
            if call.get("iterate"):
                page = fn(*args_of(call))
                items = [x["id"] for x in page.auto_paging_iter(call.get("max_items"))]
            else:
                result = fn(*args_of(call))
    except pp.PhoenixPerpsError as err:
        error = err
    check(mock, sc, result, error, items)


@pytest.mark.parametrize("sc", S["scenarios"], ids=[s["name"] for s in S["scenarios"]])
def test_async(mock, sc):
    async def run():
        load(mock, sc["exchanges"])
        async with pp.AsyncPhoenixPerps(S["key"], base_url=base_url(mock, sc), max_retries=2) as client:
            result = error = items = None
            try:
                for call in sc.get("calls") or [sc["call"]]:
                    fn = getattr(getattr(client, SNAKE(call["resource"])), SNAKE(call["method"]))
                    if call.get("iterate"):
                        opts = {"max_items": call["max_items"]} if "max_items" in call else {}
                        items = [x["id"] async for x in fn(*args_of(call), **opts)]
                    else:
                        result = await fn(*args_of(call))
            except pp.PhoenixPerpsError as err:
                error = err
            return result, error, items
    result, error, items = asyncio.run(run())
    check(mock, sc, result, error, items)


@pytest.mark.parametrize("l", S["local"], ids=[l["name"] for l in S["local"]])
def test_local(mock, l, monkeypatch):
    check = l.get("check")
    if check is None:  # a client that fails before any request
        load(mock)
        with pytest.raises(pp.PhoenixPerpsError) as e:
            pp.PhoenixPerps(l["client"]["api_key"], environment=l["client"]["environment"], base_url=mock)
        assert e.value.code == l["expect"]["error_code"] and requests(mock) == []
    elif check == "redaction":
        c = pp.PhoenixPerps(S["key"], base_url=mock)
        for text in (repr(c), str(c), json.dumps({k: str(v) for k, v in vars(c).items()})):
            assert S["key"] not in text
        assert c.masked_key == f"ppk_test_****{S['key'][-4:]}"
        with pytest.raises(Exception):
            pickle.dumps(c)  # the client is not meant to travel (its http client neither)
    elif check == "webhook_vectors":
        v = S["webhook_vectors"]
        at = lambda sig: {"X-Phoenix-Timestamp": v["timestamp"], "X-Phoenix-Signature": sig}
        now = v["now_ms"] / 1000
        assert pick(pp.Webhooks.verify(v["body"], at(v["signature"]), v["secret"], 300, now), v["want"]) == v["want"]
        assert pick(pp.Webhooks.verify(v["body"], at(v["rotation"]), [v["secret"]], 300, now), v["want"]) == v["want"]
        for secret, when, body, code in [(v["other"], now, v["body"], "bad_signature"), (v["secret"], now + 1000, v["body"], "stale"), (v["secret"], now, v["body"] + " ", "bad_signature")]:
            with pytest.raises(pp.WebhookVerificationError) as e:
                pp.Webhooks.verify(body, at(v["signature"]), secret, 300, when)
            assert e.value.code == code
    elif check == "keyless":  # made without a key; a public call (sync and async) goes out without Authorization
        monkeypatch.delenv("PHOENIXPERPS_API_KEY", raising=False)
        page = {"respond": {"status": 200, "body": {"data": [], "has_more": False, "next_cursor": None}}}
        load(mock, [page, page])
        pp.PhoenixPerps(l["client"]["api_key"], environment=l["client"]["environment"], base_url=mock).markets.list()

        async def run():
            async with pp.AsyncPhoenixPerps(l["client"]["api_key"], environment=l["client"]["environment"], base_url=mock) as c:
                await c.markets.list()
        asyncio.run(run())
        got = requests(mock)
        assert len(got) == 2 and [("authorization" in r["headers"]) for r in got] == [False, False]
    else:
        pytest.fail(f"a local check this runner does not play: {check}")


@pytest.mark.parametrize("v", S["webhook_vectors"]["extra"], ids=[v["name"] for v in S["webhook_vectors"]["extra"]])
def test_webhook_vector(v):
    w = S["webhook_vectors"]
    assert set(v) <= {"name", "signature", "body", "timestamp", "want", "error"}, f"keys this runner does not play: {set(v)}"
    at = {"X-Phoenix-Timestamp": v["timestamp"], "X-Phoenix-Signature": v["signature"]}
    if "error" in v:
        with pytest.raises(pp.WebhookVerificationError) as e:
            pp.Webhooks.verify(v["body"], at, w["secret"], 300, w["now_ms"] / 1000)
        assert e.value.code == v["error"]
    else:
        assert pick(pp.Webhooks.verify(v["body"], at, w["secret"], 300, w["now_ms"] / 1000), v["want"]) == v["want"]


@pytest.mark.parametrize("c", S["rules_vectors"]["cases"], ids=lambda c: f"{c['fn']}({','.join(c['args'])})")
def test_market_rules_exact(c):
    r = pp.MarketRules(S["rules_vectors"]["market"])
    fn = {"round_price": lambda a: r.round_price(a[0], c.get("mode", "nearest")), "round_size": lambda a: r.round_size(a[0]),
          "size_for_notional": lambda a: r.size_for_notional(*a), "size_for_risk": lambda a: r.size_for_risk(*a),
          "worst_price": lambda a: r.worst_price(*a)}[c["fn"]]
    if c.get("error"):
        with pytest.raises(pp.ValidationError):  # the SDK's own, never the language's
            fn(c["args"])
    else:
        assert fn(c["args"]) == c["want"]
    assert r.round_price(62000.06) == "62000.1"


STREAM_EXPECT = {"events", "connections", "resume", "first_message", "error", "frames", "resubscribe", "unsubscribe_sent", "limits", "gap"}
STREAM_STEPS = {"send", "expect", "reply", "close", "wait_ms", "refuse", "retry_after", "body"}


@pytest.mark.parametrize("sc", S["streams"], ids=[s["name"] for s in S["streams"]])
def test_stream(mock, sc):
    x = sc["expect"]
    assert set(x) <= STREAM_EXPECT, f"expect keys this runner does not play: {set(x) - STREAM_EXPECT}"
    assert all(set(step) <= STREAM_STEPS for conn in sc["connections"] for step in conn), "a step this runner does not play"
    assert set(x.get("error", {})) <= {"class", "code", "account_id"}
    sub_pick = lambda o, w: {k: (o or {}).get(k) for k in w}
    sent = lambda m: {k: v for k, v in m.items() if k != "id"}  # a message as the SDK made it (its id aside)
    seen = {"ids": [], "gaps": [], "frames": [], "error": None, "limits": None}

    async def run():
        load(mock, connections=sc["connections"])
        async with streaming(mock) as c:
            if sc["stream"] == "account":
                async with c.account_stream() as s:
                    try:  # what it tells: events, gaps; until the events expected (and the gap, the error) came
                        while len(seen["ids"]) < len(x.get("events", [])) or "error" in x or ("gap" in x and not seen["gaps"]):
                            item = await asyncio.wait_for(s.__anext__(), 10)
                            if item["type"] == "notice":
                                seen["gaps"].append(item["after"])
                            else:
                                seen["ids"].append(item["id"])
                    except pp.PhoenixPerpsError as e:
                        seen["error"] = e
                    if "error" in x:
                        await asyncio.sleep(0.5)  # time enough for a wrong reconnect to show
            else:
                assert set(sc) <= {"name", "stream", "subscribe", "unsubscribe", "connections", "expect"}
                async with c.market_stream() as m:
                    sub = sc["subscribe"]
                    await asyncio.wait_for(m.subscribe(sub["channel"], sub["symbols"], **sub.get("params", {})), 10)
                    if "unsubscribe" in sc:
                        u = sc["unsubscribe"]
                        await asyncio.wait_for(m.unsubscribe(u["channel"], u.get("symbols"), **u.get("params", {})), 10)
                    while len(seen["frames"]) < len(x["frames"]):
                        f = await asyncio.wait_for(m.__anext__(), 10)
                        if f.get("type") == sub["channel"]:
                            seen["frames"].append(f["data"]["last"])
                    seen["limits"] = m.limits

    asyncio.run(run())
    reqs = requests(mock)
    assert len(reqs) == x["connections"]
    for i, want in enumerate(x.get("resume", [])):
        assert reqs[i]["query"].get("resume") == want
    assert reqs[0]["headers"]["authorization"] == f"Bearer {S['key']}"
    if "events" in x:
        assert seen["ids"] == x["events"]
    if "gap" in x:
        assert seen["gaps"] == [x["gap"]]
    if "error" in x:
        error = seen["error"]
        assert error is not None, "an error"
        if "class" in x["error"]:
            assert type(error).__name__ == x["error"]["class"]
        assert error.code == x["error"]["code"]
        if "account_id" in x["error"]:
            assert error.account_id == x["error"]["account_id"]
    if "frames" in x:
        assert seen["frames"] == x["frames"]
    if "limits" in x:
        assert seen["limits"] == x["limits"]
    if "first_message" in x:
        assert sub_pick(reqs[0]["messages"][0], x["first_message"]) == x["first_message"]
    if "resubscribe" in x:
        assert sent(reqs[-1]["messages"][0]) == x["resubscribe"]
    if "unsubscribe_sent" in x:
        assert sent(next(m for m in reqs[0]["messages"] if m.get("op") == "unsubscribe")) == x["unsubscribe_sent"]


def test_snippets_compile():
    """Every docs snippet (tests/snippets_check.py, generated) compiles."""
    import py_compile
    py_compile.compile(str(pathlib.Path(__file__).with_name("snippets_check.py")), doraise=True)


# The SDK's own scenarios: what the shared suite does not hold yet, each with the audit issue it pins.
ACCOUNT = "SANDBOX-00042-001"
ORDER = {"market": "BTC-USD", "side": "buy", "type": "limit", "size": "0.01", "limit_price": "62000"}
FLAVOURS = pytest.mark.parametrize("flavour", ["sync", "async"])


def envelope(status, code, retryable, after="0"):
    body = {"error": {"type": "x", "code": code, "message": "x", "request_id": "req_1", "retryable": retryable,
                      "doc_url": f"https://docs.phoenixperpsfunding.com/reference/errors#{code}"}}
    return {"respond": {"status": status, "headers": {"retry-after": after}, "body": body}}


def through(flavour, url, call):
    """call(client) on a sync client, or on an async one (awaited)."""
    if flavour == "sync":
        with pp.PhoenixPerps(S["key"], base_url=url) as c:
            return call(c)

    async def run():
        async with pp.AsyncPhoenixPerps(S["key"], base_url=url) as c:
            r = call(c)
            return await r if inspect.isawaitable(r) else r
    return asyncio.run(run())


@FLAVOURS
def test_a_change_with_its_key_is_sent_again(mock, flavour):
    """Calls: a call carrying an Idempotency-Key is safe to send again, an order change too (the API does it once)."""
    load(mock, [envelope(503, "engine_unavailable", True), {"respond": {"status": 200, "body": {"data": {"id": "ord_1"}}}}])
    assert through(flavour, mock, lambda c: c.orders.modify(ACCOUNT, "ord_1", {"limit_price": "61900"}, idempotency_key="chg-1"))["id"] == "ord_1"
    assert [g["headers"].get("idempotency-key") for g in requests(mock)] == ["chg-1", "chg-1"]


@FLAVOURS
def test_errors_carry_retryable_doc_url_and_headers(mock, flavour):
    """I78: the envelope's retryable and doc_url; an answer that is not the API's own is retryable by its status.
    Answers: the error keeps the answer's headers."""
    e = from_response(409, {}, json.dumps(envelope(409, "account_not_ready", True)["respond"]["body"]), None)
    assert (e.retryable, e.doc_url) == (True, "https://docs.phoenixperpsfunding.com/reference/errors#account_not_ready")
    assert from_response(502, {}, "<html>Bad Gateway</html>", None).retryable is True
    assert from_response(400, {}, "<html>Bad Request</html>", None).retryable is False
    load(mock, [{"respond": {"status": 404, "headers": {"x-trace": "t-1"}, "body": envelope(404, "order_not_found", False)["respond"]["body"]}}])
    with pytest.raises(pp.NotFoundError) as err:
        through(flavour, mock, lambda c: c.orders.get(ACCOUNT, "ord_1"))
    assert (err.value.headers["x-trace"], err.value.headers["x-request-id"]) == ("t-1", "req_conformance")


def test_decimals_never_go_out_with_an_exponent(mock):
    """I45: a Decimal from quantize or arithmetic (str() writes 6.74E+4, 1.0E-7) goes out in plain decimal form."""
    load(mock, [{"respond": {"status": 201, "body": {"data": {"id": "ord_1"}}}}])
    pp.PhoenixPerps(S["key"], base_url=mock).orders.create(ACCOUNT, {**ORDER, "size": Decimal("0.00000010"),
                                                                     "limit_price": Decimal("67384.8").quantize(Decimal("1E+2")),
                                                                     "tags": (Decimal("6.2E+4"), Decimal("1E-7"))})
    body = requests(mock)[0]["body"]
    assert [body["size"], body["limit_price"], body["tags"]] == ["0.00000010", "67400", ["62000", "0.0000001"]]


async def collect(items):
    return [x["id"] async for x in items]


@FLAVOURS
def test_not_modified_is_no_error(mock, flavour):
    """I74: If-None-Match with the ETag: an unchanged answer is an empty 304, the call answers None (a list call too,
    never an empty page; iterated, nothing)."""
    load(mock, [{"respond": {"status": 304, "headers": {"etag": 'W/"abc"'}}}] * 3)
    assert through(flavour, mock, lambda c: c.markets.get("BTC-USD", headers={"if-none-match": 'W/"abc"'})) is None
    raw = pp.RawResponse()  # the 304 told apart, with the ETag
    assert through(flavour, mock, lambda c: c.orders.list(ACCOUNT, headers={"if-none-match": 'W/"abc"'}, raw_response=raw)) is None
    assert raw.status_code == 304 and raw.headers["etag"] == 'W/"abc"'
    assert requests(mock)[0]["headers"]["if-none-match"] == 'W/"abc"'
    if flavour == "async":
        assert through(flavour, mock, lambda c: collect(c.orders.list(ACCOUNT, headers={"if-none-match": 'W/"abc"'}))) == []


@FLAVOURS
def test_next_pages_keep_the_first_calls_options(mock, flavour):
    """Answers: iterating a list asks every page with the first call's options (its headers here)."""
    page = lambda ids, cursor: {"respond": {"status": 200, "body": {"data": [{"id": i} for i in ids], "has_more": bool(cursor), "next_cursor": cursor}}}
    load(mock, [page(["o1"], "c1"), page(["o2"], None)])
    every = (lambda c: [o["id"] for o in c.orders.list(ACCOUNT, headers={"x-trace": "t-1"}).auto_paging_iter()]) if flavour == "sync" \
        else (lambda c: collect(c.orders.list(ACCOUNT, headers={"x-trace": "t-1"})))
    assert through(flavour, mock, every) == ["o1", "o2"]
    assert [(g["query"].get("cursor"), g["headers"].get("x-trace")) for g in requests(mock)] == [(None, "t-1"), ("c1", "t-1")]


def test_a_sync_page_iterates_every_page(mock):
    """Answers: `for x in` a sync list call goes over every page, as `async for` does; the call's max_items caps it
    (and the page past it is never asked)."""
    page = lambda ids, cursor: {"respond": {"status": 200, "body": {"data": [{"id": i} for i in ids], "has_more": bool(cursor), "next_cursor": cursor}}}
    load(mock, [page(["o1", "o2"], "c1"), page(["o3"], None), page(["o1", "o2"], "c1")])
    with pp.PhoenixPerps(S["key"], base_url=mock) as c:
        assert [o["id"] for o in c.orders.list(ACCOUNT)] == ["o1", "o2", "o3"]
        assert [o["id"] for o in c.orders.list(ACCOUNT, max_items=2)] == ["o1", "o2"]
    assert len(requests(mock)) == 3


def test_rate_limits_per_budget(mock, monkeypatch):
    """Calls: X-RateLimit-* kept per budget (an operation's x-pp-scope): a read budget used up holds the next read until
    its reset, never a trading call. (The generated operations do not carry their scope yet: set here as the spec has
    it.)"""
    monkeypatch.setitem(OPS["getMe"], "scope", "read")
    monkeypatch.setitem(OPS["placeOrder"], "scope", "trade")
    spent = {"x-ratelimit-limit": "10", "x-ratelimit-remaining": "0", "x-ratelimit-reset": "1"}
    load(mock, [{"respond": {"status": 200, "headers": spent, "body": {"data": {"n": 1}}}}, {"respond": {"status": 201, "body": {"data": {"id": "ord_1"}}}},
                {"respond": {"status": 200, "body": {"data": {"n": 2}}}}])
    with pp.PhoenixPerps(S["key"], base_url=mock) as c:
        c.me.get()
        c.orders.create(ACCOUNT, ORDER)
        c.me.get()
        assert c.rate_limits["read"]["remaining"] == 0 and "trade" not in c.rate_limits
    at = [g["at"] for g in requests(mock)]
    assert at[1] - at[0] < 500 and at[2] - at[0] >= 800


@FLAVOURS
def test_keyless_client(mock, flavour, monkeypatch):
    """Configuration: a client without a key is made (repr shows none); a call that needs one goes out without
    Authorization and fails with the API's missing_api_key; its account stream's upgrade carries none either."""
    monkeypatch.delenv("PHOENIXPERPS_API_KEY", raising=False)
    refusal = {"error": {"type": "authentication", "code": "missing_api_key", "message": "x", "request_id": "req_1", "retryable": False}}
    load(mock, [{"respond": {"status": 401, "body": refusal}}], connections=[[{"refuse": 401, "body": refusal}]])
    assert "api_key=None" in repr(pp.PhoenixPerps(base_url=mock))

    async def stream():
        async with pp.AsyncPhoenixPerps(base_url=mock, account_stream_url=f"{mock.replace('http', 'ws', 1)}/v1/stream") as c:
            async with c.account_stream() as s:
                return await asyncio.wait_for(s.__anext__(), 10)
    if flavour == "sync":
        with pytest.raises(pp.AuthenticationError) as e:
            pp.PhoenixPerps(base_url=mock).me.get()
    else:
        with pytest.raises(pp.AuthenticationError) as e:
            asyncio.run(stream())
    assert (e.value.code, e.value.status) == ("missing_api_key", 401)
    got = requests(mock)
    assert len(got) == 1 and "authorization" not in got[0]["headers"]


def test_environment_in_any_case(monkeypatch):
    """Configuration: the environment is live or sandbox in any case, given or from PHOENIXPERPS_ENVIRONMENT."""
    assert pp.PhoenixPerps("a-key-without-prefix", environment="LIVE", base_url="http://127.0.0.1:9").environment == "live"
    assert pp.PhoenixPerps(S["key"], environment="Sandbox", base_url="http://127.0.0.1:9").environment == "sandbox"
    monkeypatch.setenv("PHOENIXPERPS_ENVIRONMENT", "SANDBOX")
    assert pp.PhoenixPerps("a-key-without-prefix", base_url="http://127.0.0.1:9").environment == "sandbox"


def test_the_rest_address_as_base_url(mock):
    """I75: the REST address as the docs write it (https://.../v1) is the host, not a prefix of every path."""
    load(mock, [{"respond": {"status": 200, "body": {"data": {"credential": {"kind": "key"}}}}}])
    assert pp.PhoenixPerps(S["key"], base_url=f"{mock}/v1/").base_url == mock
    pp.PhoenixPerps(S["key"], base_url=f"{mock}/v1").me.get()
    assert requests(mock)[0]["path"] == "/v1/me"


def test_the_keys_environment_before_the_variable(monkeypatch):
    """I77: PHOENIXPERPS_ENVIRONMENT only for a key without its environment's prefix."""
    monkeypatch.setenv("PHOENIXPERPS_ENVIRONMENT", "live")
    assert pp.PhoenixPerps(S["key"], base_url="http://127.0.0.1:9").environment == "sandbox"
    assert pp.PhoenixPerps("a-key-without-prefix", base_url="http://127.0.0.1:9").environment == "live"


@FLAVOURS
def test_raw_response(mock, flavour):
    """I80: a call's answer as it came: Idempotent-Replayed, a success's request id, the key it carried."""
    load(mock, [{"respond": {"status": 201, "headers": {"idempotent-replayed": "true"}, "body": {"data": {"id": "ord_1"}}}}])
    raw = pp.RawResponse()
    assert through(flavour, mock, lambda c: c.orders.create(ACCOUNT, ORDER, idempotency_key="k-1", raw_response=raw))["id"] == "ord_1"
    assert (raw.status_code, raw.headers["idempotent-replayed"], raw.request_id, raw.idempotency_key) == (201, "true", "req_conformance", "k-1")
    assert raw.json() == {"data": {"id": "ord_1"}}


@FLAVOURS
def test_any_route(mock, flavour):
    """I81: a route this SDK version does not know, with its key, retries (a GET) and errors (a change without a key is
    never sent twice). Calls: a route the spec marks retry-safe is tried again too, and a body is answered as it came."""
    ok = lambda body: {"respond": {"status": 200, "body": body}}
    load(mock, [envelope(503, "engine_unavailable", True), ok({"data": {"later": True}}), envelope(503, "engine_unavailable", True),
                envelope(503, "engine_unavailable", True), ok({"data": {"mode": "isolated"}}), ok("a,b\n1,2")])
    r = through(flavour, mock, lambda c: c.request("get", "/v1/a-later-route", query={"n": 1}))
    assert (r.status_code, r.json()) == (200, {"data": {"later": True}})
    with pytest.raises(pp.ServerError):
        through(flavour, mock, lambda c: c.request("POST", "/v1/a-later-route", body={"n": 1}))
    assert through(flavour, mock, lambda c: c.request("PUT", f"/v1/accounts/{ACCOUNT}/margin/BTC-USD", body={"mode": "isolated"})).json() == {"data": {"mode": "isolated"}}
    assert through(flavour, mock, lambda c: c.request("GET", "/v1/a-later-route.csv")).body == b"a,b\n1,2"
    got = requests(mock)
    assert [(g["method"], g["path"]) for g in got] == [("GET", "/v1/a-later-route")] * 2 + [("POST", "/v1/a-later-route")] \
        + [("PUT", f"/v1/accounts/{ACCOUNT}/margin/BTC-USD")] * 2 + [("GET", "/v1/a-later-route.csv")]
    assert got[1]["query"] == {"n": "1"} and got[1]["headers"]["authorization"] == f"Bearer {S['key']}" and got[2]["body"] == {"n": 1}
    with pytest.raises(pp.ValidationError):
        pp.PhoenixPerps(S["key"], base_url=mock).request("GET", "v1/status")


def test_an_async_page_iterates_its_data(mock):
    """I125: the page an async list answers iterates its items, as the sync Page does."""
    load(mock, [{"respond": {"status": 200, "body": {"data": [{"id": "a1"}, {"id": "a2"}], "has_more": False, "next_cursor": None}}}])
    page = through("async", mock, lambda c: c.accounts.list())
    assert isinstance(page, pp.AsyncPage) and [a["id"] for a in page] == ["a1", "a2"]


def test_worst_price_side():
    """I86: the side in any case; anything else is refused, never priced as a sell: a ValidationError (the shared
    vectors), still the ValueError it was."""
    r = pp.MarketRules(S["rules_vectors"]["market"])
    assert r.worst_price("BUY", "62000") == r.worst_price("buy", "62000") == "62620.0"
    assert r.worst_price("Sell", "62000.05") == "61380.0"
    with pytest.raises(ValueError) as e:
        r.worst_price("long", "62000")
    assert isinstance(e.value, pp.ValidationError)


def test_worst_price_slippage_bounds():
    """I87: the market's slippage bounds (0.05 to 5 % here) hold; a value that is no decimal is a ValidationError too."""
    r = pp.MarketRules(S["rules_vectors"]["market"])
    assert r.worst_price("buy", "62000", "5") == "65100.0"
    for pct in ("50", "0.01", "NaN", "five"):
        with pytest.raises(ValueError) as e:
            r.worst_price("buy", "62000", pct)
        assert isinstance(e.value, pp.ValidationError)


def test_webhook_any_bad_body_is_a_verification_error():
    """I126: a body that is not UTF-8 fails the signature (never UnicodeDecodeError), a signed one that is not JSON is
    bad_body; the error is a PhoenixPerpsError."""
    v = S["webhook_vectors"]
    at = {"X-Phoenix-Timestamp": v["timestamp"], "X-Phoenix-Signature": v["signature"]}
    with pytest.raises(pp.PhoenixPerpsError) as e:
        pp.Webhooks.verify(b"\xff\xfe{}", at, v["secret"], 300, v["now_ms"] / 1000)
    assert type(e.value) is pp.WebhookVerificationError and e.value.code == "bad_signature"
    signed = {**at, "X-Phoenix-Signature": "sha256=" + hmac.new(v["secret"].encode(), v["timestamp"].encode() + b".\xff{", hashlib.sha256).hexdigest()}
    with pytest.raises(pp.WebhookVerificationError) as e:
        pp.Webhooks.verify(b"\xff{", signed, v["secret"], 300, v["now_ms"] / 1000)
    assert e.value.code == "bad_body"
    assert pp.Webhooks.verify(v["body"].encode(), at, v["secret"], 300, v["now_ms"] / 1000)["type"] == v["want"]["type"]


HELLO = {"send": {"type": "hello", "stream_id": "str_1", "environment": "sandbox", "heartbeat_ms": 15000}}


def streaming(url, ws_port=None):
    """An async client whose streams go to the mock (or to a port in front of it)."""
    ws = f"ws://127.0.0.1:{ws_port}" if ws_port else url.replace("http", "ws", 1)
    return pp.AsyncPhoenixPerps(S["key"], base_url=url, account_stream_url=f"{ws}/v1/stream", market_stream_url=f"{ws}/v1/market-data")


@pytest.mark.parametrize("frame,details", [
    ({"type": "error", "code": "too_many_accounts", "details": {"max": 50}}, {"max": 50}),  # no snapshot_end follows
    ({"type": "error", "code": "account_not_found", "account_id": "SANDBOX-00014-001"}, {"account_id": "SANDBOX-00014-001"}),
], ids=["too_many_accounts", "account_not_found"])
def test_account_stream_refused_subscribe(mock, frame, details):
    """I21: an error frame answers the stream's own subscribe, sent again as it is on every reconnect: it ends the
    stream with ValidationError, and ready is set (no snapshot_end comes after too_many_accounts)."""
    after = [{"send": {"type": "snapshot_end", "accounts": []}}] if frame["code"] == "account_not_found" else []
    load(mock, connections=[[HELLO, {"expect": "subscribe"}, {"send": frame}, *after, {"wait_ms": 3000}], [HELLO, {"wait_ms": 3000}]])

    async def run():
        async with streaming(mock) as c:
            async with c.account_stream(accounts=["SANDBOX-00014-001"]) as s:
                await asyncio.wait_for(s.ready.wait(), 10)
                with pytest.raises(pp.ValidationError) as e:
                    await asyncio.wait_for(s.__anext__(), 10)
                await asyncio.sleep(0.5)  # time enough for a wrong reconnect to show
                return e.value
    err = asyncio.run(run())
    assert (err.code, err.details) == (frame["code"], details)
    assert len(requests(mock)) == 1


def ok(op):
    """A market stream's ask, expected and answered."""
    return [{"expect": op}, {"reply": {"type": f"{op}d"}}]


async def settled(mock, connections, messages):
    """The mock's requests once it saw that many connections, the last with that many messages (a reconnect after 1012
    takes 0 to 3 s)."""
    for _ in range(100):
        got = requests(mock)
        if len(got) == connections and len(got[-1]["messages"]) >= messages:
            break
        await asyncio.sleep(0.1)
    return got


def test_market_unsubscribe_without_the_options(mock):
    """I25: the server knows a subscription by channel and symbol (and interval for candles): an unsubscribe without
    the subscribe's options, or a symbol subscribed again with others, is what comes back after a reconnect."""
    load(mock, connections=[
        [HELLO, *ok("subscribe"), *ok("subscribe"), *ok("subscribe"), *ok("unsubscribe"), *ok("unsubscribe"), *ok("unsubscribe"), *ok("subscribe"), {"close": 1012}],
        [HELLO, *ok("subscribe"), *ok("subscribe"), {"wait_ms": 3000}],
    ])

    async def run():
        async with streaming(mock) as c:
            async with c.market_stream() as m:
                await m.subscribe("ticker", ["BTC-USD", "ETH-USD", "SOL-USD"], interval_ms=250)
                await m.subscribe("candles", ["BTC-USD"], interval="1m", history=10)
                await m.subscribe("status")
                await m.unsubscribe("ticker", ["ETH-USD"])
                await m.unsubscribe("candles", ["BTC-USD"], interval="1m")
                await m.unsubscribe("status")
                await m.subscribe("ticker", ["SOL-USD"], interval_ms=1000)
                await settled(mock, 2, 2)  # the reconnect and its subscribes
                await asyncio.sleep(0.5)  # time enough for a wrong subscribe to show
    asyncio.run(run())
    first, second = [[{k: v for k, v in m.items() if k != "id"} for m in r["messages"]] for r in requests(mock)]
    assert second == [{"op": "subscribe", "channel": "ticker", "symbols": ["BTC-USD"], "interval_ms": 250},
                      {"op": "subscribe", "channel": "ticker", "symbols": ["SOL-USD"], "interval_ms": 1000}]
    # what the server needs to stop each: the interval of candles, no symbols for status
    assert first[3:6] == [{"op": "unsubscribe", "channel": "ticker", "symbols": ["ETH-USD"]},
                          {"op": "unsubscribe", "channel": "candles", "symbols": ["BTC-USD"], "interval": "1m"},
                          {"op": "unsubscribe", "channel": "status"}]


async def cut_first_handshake(port):
    """A port in front of the mock whose first connection drops during the WebSocket handshake (no HTTP answer)."""
    seen = []

    async def handle(reader, writer):
        seen.append(1)
        if len(seen) == 1:
            await reader.readuntil(b"\r\n\r\n")  # the whole upgrade read, so the close is a clean one (no reset)
            writer.close()
            return
        r, w = await asyncio.open_connection("127.0.0.1", port)

        async def pipe(src, dst):
            while data := await src.read(65536):
                dst.write(data)
                await dst.drain()
            dst.close()
        await asyncio.gather(pipe(reader, w), pipe(r, writer), return_exceptions=True)
    return await asyncio.start_server(handle, "127.0.0.1", 0), seen


EVENT = {"send": {"type": "event", "event": {"id": "evt_1", "type": "order.created", "account_id": ACCOUNT, "time": 1791460800000, "source": "api", "data": {}}}}


@pytest.mark.parametrize("failure", ["cut handshake", "frame not a JSON object"])
def test_streams_reconnect_after_any_failure(mock, failure):
    """I46: a handshake cut short or a frame that is not a JSON object ends that connection, never the stream: both
    streams reconnect (the account stream's events and the market stream's subscribe go on)."""
    async def run(stream):
        server, seen = await cut_first_handshake(int(mock.rsplit(":", 1)[1])) if failure == "cut handshake" else (None, [])
        try:
            async with streaming(mock, server and server.sockets[0].getsockname()[1]) as c:
                if stream == "account":
                    async with c.account_stream() as s:
                        return (await asyncio.wait_for(s.__anext__(), 10))["id"], len(seen)
                async with c.market_stream() as m:
                    return (await asyncio.wait_for(m.subscribe("ticker", ["BTC-USD"]), 10))["type"], len(seen)
        finally:
            if server:
                server.close()
    bad = [] if failure == "cut handshake" else [[HELLO, {"send": "not a JSON object"}, {"wait_ms": 3000}]]
    load(mock, connections=[*bad, [HELLO, {"expect": "subscribe"}, EVENT, {"wait_ms": 3000}]])
    assert asyncio.run(run("account")) == ("evt_1", 2 if failure == "cut handshake" else 0)
    load(mock, connections=[*bad, [HELLO, {"expect": "subscribe"}, {"reply": {"type": "subscribed"}}, {"wait_ms": 3000}]])
    assert asyncio.run(run("market")) == ("subscribed", 2 if failure == "cut handshake" else 0)
    assert len(requests(mock)) == 1 + len(bad)


def told(mock, n, **kw):
    """The first n things an account stream tells (account_stream(**kw)) against the mock."""
    async def run():
        async with streaming(mock) as c:
            async with c.account_stream(**kw) as s:
                return [await asyncio.wait_for(s.__anext__(), 10) for _ in range(n)], s
    return asyncio.run(run())


def test_account_stream_tells_risk_frames_and_each_events_seq(mock):
    """Streams: risk frames, asked with risk=True, are told as they come (and kept in stream.risks); each event keeps its
    frame's seq and replayed."""
    risk = {"type": "risk", "account_id": ACCOUNT, "data": {"equity": "50000.00"}}
    load(mock, connections=[[HELLO, {"expect": "subscribe"}, {"send": risk}, {"send": {**EVENT["send"], "seq": 7, "replayed": True}}, {"wait_ms": 3000}]])
    (frame, event), s = told(mock, 2, risk=True)
    assert frame == risk and s.risks == {ACCOUNT: risk}
    assert (event["id"], event["seq"], event["replayed"]) == ("evt_1", 7, True)
    assert requests(mock)[0]["messages"] == [{"op": "subscribe", "risk": True}]


def test_account_stream_4008_is_a_gap(mock):
    """Streams: a 4008 (too far behind) is a gap too, told after the last event before the stream goes on (no resume)."""
    later = {"send": {"type": "event", "seq": 1, "event": {**EVENT["send"]["event"], "id": "evt_2"}}}
    load(mock, connections=[[HELLO, {"expect": "subscribe"}, EVENT, {"close": 4008}], [HELLO, {"expect": "subscribe"}, later, {"wait_ms": 3000}]])
    items, _ = told(mock, 3)
    assert items[1] == {"type": "notice", "code": "resume_gap", "after": "evt_1"}
    assert [items[0]["id"], items[2]["id"]] == ["evt_1", "evt_2"]
    assert [r["query"].get("resume") for r in requests(mock)] == [None, None]


def test_account_stream_ping_and_unsubscribe(mock):
    """Streams: ping() answers the pong; unsubscribe(accounts) answers unsubscribed, and the subscribe sent again after a
    reconnect names what is left (of the hello's accounts, when none were asked)."""
    other = "SANDBOX-00042-002"
    hello = {"send": {**HELLO["send"], "accounts": [ACCOUNT, other]}}
    load(mock, connections=[
        [hello, {"expect": "subscribe"}, {"expect": "ping"}, {"send": {"type": "pong", "time": 1}}, {"expect": "unsubscribe"},
         {"send": {"type": "unsubscribed", "accounts": [other]}}, {"close": 1012}],
        [hello, {"expect": "subscribe"}, {"wait_ms": 3000}]])

    async def run():
        async with streaming(mock) as c:
            async with c.account_stream() as s:
                pong = await asyncio.wait_for(s.ping(), 10)
                gone = await asyncio.wait_for(s.unsubscribe([other]), 10)
                await settled(mock, 2, 1)
                return pong, gone
    pong, gone = asyncio.run(run())
    assert (pong["type"], gone["accounts"]) == ("pong", [other])
    first, second = [r["messages"] for r in requests(mock)]
    assert first == [{"op": "subscribe"}, {"op": "ping"}, {"op": "unsubscribe", "accounts": [other]}]
    assert second == [{"op": "subscribe", "accounts": [ACCOUNT]}]


def test_stream_reconnects_tell_one_log_line_with_the_cause(mock, caplog):
    """Streams: one log line per reconnect, with its cause (an upgrade refused with 429, waited its Retry-After; a 1012)."""
    refused = {"refuse": 429, "retry_after": 0, "body": envelope(429, "rate_limited", True)["respond"]["body"]}
    load(mock, connections=[[refused], [HELLO, {"expect": "subscribe"}, {"close": 1012}], [HELLO, {"expect": "subscribe"}, EVENT, {"wait_ms": 3000}]])
    caplog.set_level(logging.INFO, logger="phoenixperps")
    (event,), _ = told(mock, 1)
    lines = [r.getMessage() for r in caplog.records if r.name == "phoenixperps"]
    assert event["id"] == "evt_1" and len(lines) == 2
    assert "in 0.0 s" in lines[0] and "429" in lines[0] and "1012" in lines[1]


def test_market_stream_list_ping_and_error_details(mock):
    """Streams: the market stream offers list() and ping(); an error frame keeps its details on the error told."""
    issue = {"field": "depth", "issue": "1, 5, 10 or 20"}
    load(mock, connections=[[HELLO, {"expect": "list"}, {"reply": {"type": "list", "subscriptions": []}}, {"expect": "ping"},
                             {"reply": {"type": "pong", "time": 1}}, {"expect": "subscribe"},
                             {"reply": {"type": "error", "code": "invalid_parameter", "details": issue}}, {"wait_ms": 3000}]])

    async def run():
        async with streaming(mock) as c:
            async with c.market_stream() as m:
                listed = await asyncio.wait_for(m.list(), 10)
                pong = await asyncio.wait_for(m.ping(), 10)
                with pytest.raises(pp.ValidationError) as e:
                    await asyncio.wait_for(m.subscribe("book", ["BTC-USD"], depth=7), 10)
                return listed, pong, e.value
    listed, pong, err = asyncio.run(run())
    assert (listed["subscriptions"], pong["type"]) == ([], "pong")
    assert (err.code, err.details) == ("invalid_parameter", issue)
    assert [m["op"] for m in requests(mock)[0]["messages"]] == ["list", "ping", "subscribe"]


def test_market_stream_refused_upgrade_ends_with_the_apis_error(mock):
    """Streams: an upgrade refused with other than 429 or 5xx ends the stream with the API's own error (an ask waiting
    fails with it), never reconnecting."""
    body = {"error": {"type": "permission", "code": "ip_not_allowed", "message": "x", "request_id": "req_1", "retryable": False}}
    load(mock, connections=[[{"refuse": 403, "body": body}]])

    async def run():
        async with streaming(mock) as c:
            async with c.market_stream() as m:
                with pytest.raises(pp.PermissionDeniedError) as e:
                    await asyncio.wait_for(m.subscribe("ticker", ["BTC-USD"]), 10)
                await asyncio.sleep(0.5)  # time enough for a wrong reconnect to show
                return e.value
    assert asyncio.run(run()).code == "ip_not_allowed"
    assert len(requests(mock)) == 1


def test_market_unsubscribe_every_interval_and_the_whole_channel(mock):
    """Streams: an unsubscribe without an interval ends every interval of those candles, one without symbols the whole
    channel; neither is made again after a reconnect."""
    load(mock, connections=[
        [HELLO, *ok("subscribe"), *ok("subscribe"), *ok("subscribe"), *ok("subscribe"), *ok("unsubscribe"), *ok("unsubscribe"), {"close": 1012}],
        [HELLO, *ok("subscribe"), {"wait_ms": 3000}]])

    async def run():
        async with streaming(mock) as c:
            async with c.market_stream() as m:
                await m.subscribe("ticker", ["BTC-USD"])
                await m.subscribe("candles", ["BTC-USD"], interval="1m")
                await m.subscribe("candles", ["BTC-USD"], interval="5m")
                await m.subscribe("book", ["BTC-USD", "ETH-USD"], depth=5)
                await m.unsubscribe("candles", ["BTC-USD"])
                await m.unsubscribe("book")
                await settled(mock, 2, 1)
                await asyncio.sleep(0.5)  # time enough for a wrong subscribe to show
    asyncio.run(run())
    first, second = [[{k: v for k, v in m.items() if k != "id"} for m in r["messages"]] for r in requests(mock)]
    assert first[4:] == [{"op": "unsubscribe", "channel": "candles", "symbols": ["BTC-USD"]}, {"op": "unsubscribe", "channel": "book", "symbols": []}]
    assert second == [{"op": "subscribe", "channel": "ticker", "symbols": ["BTC-USD"]}]
