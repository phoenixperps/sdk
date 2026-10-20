//! The conformance suite (conformance/scenarios.json) through the Rust SDK against the mock server
//! (node conformance/mock-server.js): retries, idempotency keys, Retry-After, errors, 304, paging, open enums, decimals,
//! the rate-limit wait, the streams; the local checks (wrong environment, the key never printed, webhook signatures,
//! a client without a key, exact rounding); and this SDK's own scripts on the same mock (a key given to a change, the
//! raw request, the error's headers, the streams' error frames, ping and unsubscribe, subscriptions by market and
//! interval, a refused upgrade).

mod common;

use common::{dispatch::dispatch, Answer, Call};
use phoenixperps::{dec, verify_webhook, AccountMessage, AccountStreamOptions, Client, Market, MarketRules, ModifyOrder, RequestOptions, RoundMode};
use serde_json::{json, Map, Value};
use std::io::{BufRead, BufReader};
use std::path::PathBuf;
use std::process::{Child, Command, Stdio};
use std::sync::{Arc, Mutex, OnceLock};
use std::time::{Duration, UNIX_EPOCH};

/// The mock server of one test, stopped when dropped (its own stdin and stderr: no handle of the test's output
/// outlives it).
struct Mock {
    url: String,
    child: Child,
}

impl Drop for Mock {
    fn drop(&mut self) {
        let _ = self.child.kill();
        let _ = self.child.wait();
    }
}

fn root() -> PathBuf {
    PathBuf::from(env!("CARGO_MANIFEST_DIR")).parent().unwrap().to_path_buf()
}

fn suite() -> &'static Value {
    static SUITE: OnceLock<Value> = OnceLock::new();
    SUITE.get_or_init(|| serde_json::from_str(&std::fs::read_to_string(root().join("conformance").join("scenarios.json")).unwrap()).unwrap())
}

fn start_mock() -> Mock {
    let mut child = Command::new("node").arg(root().join("conformance").join("mock-server.js")).arg("0").stdin(Stdio::null()).stdout(Stdio::piped()).stderr(Stdio::null()).spawn().unwrap();
    let mut line = String::new();
    BufReader::new(child.stdout.take().unwrap()).read_line(&mut line).unwrap();
    Mock { url: line.trim().to_string(), child }
}

fn key() -> String {
    suite()["key"].as_str().unwrap().to_string()
}

async fn load(url: &str, body: Value) {
    reqwest::Client::new().post(format!("{url}/__scenario")).body(body.to_string()).send().await.unwrap();
}

async fn requests(url: &str) -> Vec<Value> {
    let r: Value = serde_json::from_slice(&reqwest::Client::new().get(format!("{url}/__requests")).send().await.unwrap().bytes().await.unwrap()).unwrap();
    r.as_array().unwrap().clone()
}

/// got, keeping only what want names (the expected objects are partial).
fn pick(got: &Value, want: &Value) -> Value {
    match want {
        Value::Object(w) => Value::Object(w.iter().map(|(k, v)| (k.clone(), pick(got.get(k).unwrap_or(&Value::Null), v))).collect()),
        _ => got.clone(),
    }
}

fn strings(v: &Value) -> Vec<String> {
    v.as_array().into_iter().flatten().map(|s| s.as_str().unwrap().to_string()).collect()
}

/// A stream's answer to an ask, in time.
async fn answer(ask: impl Future<Output = phoenixperps::Result<Value>>) -> Value {
    tokio::time::timeout(Duration::from_secs(10), ask).await.expect("a reply in time").unwrap()
}

/// A client of the mock: REST and both streams.
fn client(url: &str) -> Client {
    let ws = format!("ws{}", &url[4..]);
    Client::builder().api_key(key()).base_url(url).stream_urls(format!("{ws}/v1/stream"), format!("{ws}/v1/market-data")).build().unwrap()
}

// one test plays every scenario in order (they share its mock server), each in a task of its own: a failing scenario is
// named at the end, and the others still play
#[tokio::test(flavor = "multi_thread")]
async fn conformance() {
    let m = start_mock();
    let mut failed = Vec::new();
    for sc in suite()["scenarios"].as_array().unwrap() {
        if tokio::spawn(scenario(m.url.clone(), sc.clone())).await.is_err() {
            failed.push(sc["name"].as_str().unwrap());
        }
    }
    for sc in suite()["streams"].as_array().unwrap() {
        if tokio::spawn(stream(m.url.clone(), sc.clone())).await.is_err() {
            failed.push(sc["name"].as_str().unwrap());
        }
    }
    assert!(failed.is_empty(), "failed: {failed:#?}");

    // an unknown enum value and an unknown field: kept, also when written again
    load(&m.url, json!({ "exchanges": [{ "respond": { "status": 200, "body": { "data": { "id": "ord_1", "status": "a_status_from_the_future", "a_new_field": 1 } } } }] })).await;
    let o = client(&m.url).orders().get("SANDBOX-00042-001", "ord_1").await.unwrap();
    assert_eq!("a_status_from_the_future", o.status.as_str());
    assert_eq!(json!(1), serde_json::to_value(&o).unwrap()["a_new_field"], "kept when written again");
}

/// One REST scenario: its calls through the typed client, then what the mock got.
async fn scenario(url: String, sc: Value) {
    let name = sc["name"].as_str().unwrap();
    load(&url, json!({ "exchanges": sc["exchanges"] })).await;
    let base = format!("{url}{}", sc["client"]["base_url_suffix"].as_str().unwrap_or(""));
    let c = Client::builder().api_key(key()).base_url(base).max_retries(2).build().unwrap();
    let x = &sc["expect"];
    let calls: Vec<Value> = sc.get("calls").and_then(Value::as_array).cloned().unwrap_or_else(|| vec![sc["call"].clone()]);
    let mut answer = None;
    let mut error = None;
    for call in &calls {
        // page_fields: the page itself (the dispatch table answers its data)
        let r = if x.get("page_fields").is_some() { page(&c, call).await.map(Answer::Value) } else { dispatch(&c, &Call::new(call)).await };
        match r {
            Ok(a) => answer = Some(a),
            Err(e) => {
                error = Some(e);
                break;
            }
        }
    }
    let got = requests(&url).await;
    assert_eq!(x["requests"].as_u64().unwrap() as usize, got.len(), "{name}: requests");
    if let Some(xe) = x.get("error") {
        let e = error.unwrap_or_else(|| panic!("{name}: an error"));
        if let Some(class) = xe.get("class") {
            assert_eq!(class.as_str().unwrap(), e.kind.class(), "{name}");
        }
        assert_eq!(xe["code"].as_str().unwrap(), e.code, "{name}");
        if let Some(s) = xe.get("status") {
            assert_eq!(s.as_u64().map(|s| s as u16), e.status, "{name}");
        }
        if let Some(r) = xe.get("retryable") {
            assert_eq!(r.as_bool(), e.retryable, "{name}: retryable");
        }
        if let Some(d) = xe.get("doc_url") {
            assert_eq!(d.as_str(), e.doc_url.as_deref(), "{name}: doc_url");
        }
        if let Some(ra) = xe.get("retry_after_ms") {
            assert_eq!(ra.as_u64().unwrap() as u128, e.retry_after.unwrap().as_millis(), "{name}");
        }
        if let Some(o) = xe.get("order_id") {
            assert_eq!(o.as_str(), e.order_id.as_deref(), "{name}");
        }
        if let Some(r) = xe.get("request_id") {
            assert_eq!(r.as_str(), e.request_id.as_deref(), "{name}");
        }
    } else {
        assert!(error.is_none(), "{name}: {error:?}");
        match answer {
            Some(Answer::Value(v)) => {
                // result null: the call answered nothing
                if let Some(want) = x.get("result") {
                    assert_eq!(want, &pick(&v, want), "{name}");
                }
                if let Some(want) = x.get("page_fields") {
                    assert_eq!(want, &pick(&v, want), "{name}: the page's fields");
                }
            }
            Some(Answer::Items(items)) => assert_eq!(strings(&x["items"]), items, "{name}"),
            None => panic!("{name}: no answer"),
        }
    }
    if let Some(cursors) = x.get("cursors").and_then(Value::as_array) {
        for (i, w) in cursors.iter().enumerate() {
            assert_eq!(w.as_str(), got[i]["query"].get("cursor").and_then(Value::as_str), "{name}: cursor {i}");
        }
    }
    if let Some(paths) = x.get("paths") {
        assert_eq!(&strings(paths), &got.iter().map(|r| r["path"].as_str().unwrap().to_string()).collect::<Vec<_>>(), "{name}: paths");
    }
    if let Some(fb) = x.get("first_body") {
        assert_eq!(fb, &pick(&got[0]["body"], fb), "{name}");
    }
    if let Some(me) = x.get("min_elapsed_ms").and_then(Value::as_i64) {
        assert!(got[got.len() - 1]["at"].as_i64().unwrap() - got[0]["at"].as_i64().unwrap() >= me, "{name}: elapsed");
    }
    for check in x.get("checks").and_then(Value::as_array).cloned().unwrap_or_default() {
        let h = &got[0]["headers"];
        match check.as_str().unwrap() {
            "bearer" => assert_eq!(format!("Bearer {}", key()), h["authorization"].as_str().unwrap()),
            "user_agent" => {
                let ua = h["user-agent"].as_str().unwrap();
                assert!(ua.starts_with("phoenixperps-rust/") && ua.contains(") spec/") && ua.contains(" ("), "{ua}");
            }
            "same_idempotency_key" => {
                let keys: std::collections::HashSet<&str> = got.iter().map(|r| r["headers"]["idempotency-key"].as_str().unwrap()).collect();
                assert_eq!(1, keys.len());
                let k = keys.into_iter().next().unwrap();
                assert!(uuid_v4(k), "{k}");
            }
            other => panic!("{name}: no check {other}"),
        }
    }
}

/// A list call's page as JSON (its other fields), for page_fields: the dispatch table answers only its data.
async fn page(c: &Client, call: &Value) -> phoenixperps::Result<Value> {
    let path = |n: &str| call["path"][n].as_str().unwrap().to_string();
    let page = match (call["resource"].as_str().unwrap(), call["method"].as_str().unwrap()) {
        ("orders", "list") => c.orders().list(&path("account_id"), None).await?,
        other => panic!("no page call for {other:?}"),
    };
    Ok(Value::Object(page.extra))
}

/// One stream scenario: the stream against the scripted connections, then what the mock got.
async fn stream(url: String, sc: Value) {
    let name = sc["name"].as_str().unwrap();
    load(&url, json!({ "connections": sc["connections"] })).await;
    let c = client(&url);
    let x = &sc["expect"];
    let mut error = None;
    if sc["stream"] == "account" {
        let want = strings(&x["events"]);
        let mut s = c.account_stream(AccountStreamOptions::default());
        let (mut ids, mut gaps) = (Vec::new(), Vec::new());
        while ids.len() < want.len() {
            match tokio::time::timeout(Duration::from_secs(10), s.next_message()).await.expect("an event in time") {
                Some(Ok(AccountMessage::Event(e))) => ids.push(e.event.id),
                Some(Ok(AccountMessage::Gap { after })) => gaps.push(after),
                Some(Ok(_)) => {}
                Some(Err(e)) => {
                    error = Some(e);
                    break;
                }
                None => break,
            }
        }
        if x.get("error").is_some() && error.is_none() {
            while let Some(m) = tokio::time::timeout(Duration::from_secs(10), s.next_message()).await.expect("the end in time") {
                if let Err(e) = m {
                    error = Some(e);
                    break;
                }
            }
            tokio::time::sleep(Duration::from_millis(500)).await; // time enough for a wrong reconnect to show
        }
        drop(s);
        assert_eq!(want, ids, "{name}");
        if let Some(g) = x.get("gap") {
            assert_eq!(Some(&g.as_str().map(str::to_string)), gaps.first(), "{name}: the gap");
        }
    } else {
        let sub = &sc["subscribe"];
        let channel = sub["channel"].as_str().unwrap();
        let symbols = strings(&sub["symbols"]);
        let symbols: Vec<&str> = symbols.iter().map(String::as_str).collect();
        let mut mk = c.market_stream();
        answer(mk.subscribe(channel, &symbols, sub["params"].as_object().cloned().unwrap_or_default())).await;
        if let Some(u) = sc.get("unsubscribe") {
            let symbols = strings(&u["symbols"]);
            let symbols: Vec<&str> = symbols.iter().map(String::as_str).collect();
            answer(mk.unsubscribe(u["channel"].as_str().unwrap(), &symbols, u["params"].as_object().cloned().unwrap_or_default())).await;
        }
        let want = strings(&x["frames"]);
        let mut frames = Vec::new();
        while frames.len() < want.len() {
            let f = tokio::time::timeout(Duration::from_secs(10), mk.next()).await.expect("a frame in time").unwrap().unwrap();
            if f.r#type == channel {
                frames.push(f.data.unwrap()["last"].as_str().unwrap().to_string());
            }
        }
        if let Some(limits) = x.get("limits") {
            assert_eq!(Some(limits), mk.limits().as_ref(), "{name}: the hello's limits");
        }
        drop(mk);
        assert_eq!(want, frames, "{name}");
    }
    let reqs = requests(&url).await;
    assert_eq!(x["connections"].as_u64().unwrap() as usize, reqs.len(), "{name}: connections");
    for (i, w) in x.get("resume").and_then(Value::as_array).cloned().unwrap_or_default().iter().enumerate() {
        assert_eq!(w.as_str(), reqs[i]["query"].get("resume").and_then(Value::as_str), "{name}: resume {i}");
    }
    assert_eq!(format!("Bearer {}", key()), reqs[0]["headers"]["authorization"].as_str().unwrap(), "{name}");
    if let Some(fm) = x.get("first_message") {
        assert_eq!(fm, &pick(&reqs[0]["messages"][0], fm), "{name}");
    }
    if let Some(rs) = x.get("resubscribe") {
        assert_eq!(rs, &pick(&reqs[reqs.len() - 1]["messages"][0], rs), "{name}: the subscribe made again");
    }
    if let Some(us) = x.get("unsubscribe_sent") {
        let sent = reqs[0]["messages"].as_array().unwrap().iter().find(|m| m["op"] == "unsubscribe").unwrap_or_else(|| panic!("{name}: an unsubscribe"));
        assert_eq!(us, &pick(sent, us), "{name}: the unsubscribe");
        assert!(sent.get("interval_ms").is_none(), "{name}: an unsubscribe names markets and an interval, nothing else");
    }
    if let Some(xe) = x.get("error") {
        let e = error.unwrap_or_else(|| panic!("{name}: an error ends it"));
        if let Some(class) = xe.get("class") {
            assert_eq!(class.as_str().unwrap(), e.kind.class(), "{name}");
        }
        assert_eq!(xe["code"].as_str().unwrap(), e.code, "{name}");
        if let Some(a) = xe.get("account_id") {
            assert_eq!(Some(a), e.details.as_ref().and_then(|d| d.get("account_id")), "{name}: the account");
        }
    }
}

fn uuid_v4(k: &str) -> bool {
    let b = k.as_bytes();
    k.len() == 36 && b[8] == b'-' && b[13] == b'-' && b[14] == b'4' && b[18] == b'-' && b"89ab".contains(&b[19]) && b[23] == b'-' && k.chars().all(|c| c == '-' || c.is_ascii_hexdigit() && !c.is_ascii_uppercase())
}

fn local(check: &str) -> &'static Value {
    suite()["local"].as_array().unwrap().iter().find(|l| l["check"] == check || l["expect"]["error_code"] == check).unwrap()
}

#[test]
fn every_local_check_is_played() {
    for l in suite()["local"].as_array().unwrap() {
        let check = l["check"].as_str().or(l["expect"]["error_code"].as_str()).unwrap();
        assert!(["wrong_environment", "redaction", "webhook_vectors", "keyless"].contains(&check), "a local check this runner does not play: {check}");
    }
}

#[test]
fn wrong_environment() {
    let l = local("wrong_environment");
    let e = Client::builder().api_key(l["client"]["api_key"].as_str().unwrap()).environment(l["client"]["environment"].as_str().unwrap()).base_url("http://127.0.0.1:9").build().unwrap_err();
    assert_eq!(l["expect"]["error_code"].as_str().unwrap(), e.code);
    assert_eq!("AuthenticationError", e.kind.class());
}

#[tokio::test]
async fn redaction() {
    let c = Client::builder().api_key(key()).base_url("http://127.0.0.1:9").max_retries(0).timeout(Duration::from_secs(2)).build().unwrap();
    for text in [format!("{c}"), format!("{c:?}"), format!("{c:#?}")] {
        assert!(!text.contains(&key()), "{text}");
    }
    let k = key();
    assert_eq!(format!("ppk_test_****{}", &k[k.len() - 4..]), c.masked_key());
    let e = c.me().get().await.unwrap_err();
    assert!(!format!("{e} {e:?}").contains(&key()));
    assert_eq!(Some(true), e.retryable, "a network failure may work later");
}

#[tokio::test]
async fn keyless() {
    let l = local("keyless");
    assert!(l["client"]["api_key"].is_null());
    let missing = json!({ "respond": { "status": 401, "body": { "error": { "type": "authentication", "code": "missing_api_key", "message": "x", "request_id": "req_1", "retryable": false } } } });
    let m = start_mock();
    load(&m.url, json!({ "exchanges": [{ "respond": { "status": 200, "body": { "data": [] } } }, missing] })).await;
    let c = Client::builder().environment(l["client"]["environment"].as_str().unwrap()).base_url(&m.url).build().unwrap();
    assert_eq!("", c.masked_key());
    c.markets().list().await.unwrap();
    assert_eq!("missing_api_key", c.me().get().await.unwrap_err().code, "any other call: the API's answer");
    for r in requests(&m.url).await {
        assert!(r["headers"].get("authorization").is_none(), "no Authorization without a key");
    }
}

#[test]
fn environment_in_any_case() {
    let c = Client::builder().api_key(key()).environment("SANDBOX").base_url("http://127.0.0.1:9").build().unwrap();
    assert_eq!("sandbox", c.environment());
}

#[test]
fn webhook_vectors() {
    let v = &suite()["webhook_vectors"];
    let s = |n: &str| v[n].as_str().unwrap();
    let now = UNIX_EPOCH + Duration::from_millis(v["now_ms"].as_u64().unwrap());
    let verify = |body: &str, ts: &str, sig: &str, secret: &str, now| verify_webhook(body, [("X-Phoenix-Timestamp", ts), ("X-Phoenix-Signature", sig)], &[secret], None, Some(now));
    let e = verify(s("body"), s("timestamp"), s("signature"), s("secret"), now).unwrap();
    assert_eq!(v["want"], pick(&serde_json::to_value(&e).unwrap(), &v["want"]), "a real delivery reads as the WebhookPayload");
    assert_eq!("evt_01JAB7Q8XK4M2N5P7R9S3T5V7W", verify(s("body"), s("timestamp"), s("rotation"), s("secret"), now).unwrap().id);
    let fails = |r: phoenixperps::Result<phoenixperps::WebhookPayload>| r.map(|_| ()).map_err(|e| (e.kind.class(), e.code.clone())).unwrap_err();
    assert_eq!(("WebhookVerificationError", "bad_signature".to_string()), fails(verify(s("body"), s("timestamp"), s("signature"), s("other"), now)));
    assert_eq!("stale", fails(verify(s("body"), s("timestamp"), s("signature"), s("secret"), now + Duration::from_secs(1000))).1);
    assert_eq!("bad_signature", fails(verify(&format!("{} ", s("body")), s("timestamp"), s("signature"), s("secret"), now)).1);
    assert_eq!("missing_headers", fails(verify_webhook(s("body"), [("X-Phoenix-Signature", s("signature"))], &[s("secret")], None, Some(now))).1);
    for x in v["extra"].as_array().unwrap() {
        let r = verify(x["body"].as_str().unwrap(), x["timestamp"].as_str().unwrap(), x["signature"].as_str().unwrap(), s("secret"), now);
        match x.get("error") {
            Some(code) => assert_eq!(("WebhookVerificationError", code.as_str().unwrap().to_string()), fails(r), "{}", x["name"]),
            None => assert_eq!(x["want"], pick(&serde_json::to_value(r.unwrap()).unwrap(), &x["want"]), "{}", x["name"]),
        }
    }
}

#[test]
fn market_rules_vectors() {
    let rv = &suite()["rules_vectors"];
    let market: Market = serde_json::from_value(rv["market"].clone()).unwrap();
    let r = MarketRules::new(&market);
    for c in rv["cases"].as_array().unwrap() {
        // the arguments as strings: one that is no decimal is the SDK's own Validation error
        let a: Vec<&str> = c["args"].as_array().unwrap().iter().map(|x| x.as_str().unwrap()).collect();
        let mode = match c.get("mode").and_then(Value::as_str) {
            Some("down") => RoundMode::Down,
            Some("up") => RoundMode::Up,
            _ => RoundMode::Nearest,
        };
        let got = match c["fn"].as_str().unwrap() {
            "round_price" => r.round_price(a[0], mode),
            "round_size" => r.round_size(a[0]),
            "size_for_notional" => r.size_for_notional(a[0], a[1]),
            "size_for_risk" => r.size_for_risk(a[0], a[1]),
            "worst_price" => r.worst_price(a[0], a[1], a.get(2).map(|s| dec(s))),
            other => panic!("no rule {other}"),
        };
        match c.get("want") {
            // the Decimal an order takes, at the step's decimals: its Display is the vector's want
            Some(want) => assert_eq!(want.as_str().map(String::from), got.as_ref().ok().map(|d| d.to_string()), "{c}"),
            None => {
                assert_eq!(Some(true), c["error"].as_bool(), "{c}");
                assert_eq!("ValidationError", got.unwrap_err().kind.class(), "{c}");
            }
        }
    }
}

/// A mock playing one script, and a client of it (REST and both streams).
async fn scripted(script: Value) -> (Mock, Client) {
    let m = start_mock();
    load(&m.url, script).await;
    let c = client(&m.url);
    (m, c)
}

/// Polls the mock until a connection's messages are in (the stream reconnects by itself).
async fn connection(url: &str, n: usize) -> Value {
    for _ in 0..100 {
        let reqs = requests(url).await;
        if reqs.len() > n && reqs[n]["messages"].as_array().is_some_and(|m| !m.is_empty()) {
            tokio::time::sleep(Duration::from_millis(300)).await; // the rest of what it sends
            return requests(url).await[n].clone();
        }
        tokio::time::sleep(Duration::from_millis(100)).await;
    }
    panic!("no connection {n}");
}

#[tokio::test]
async fn a_change_given_a_key_is_tried_again() {
    let busy = json!({ "respond": { "status": 503, "headers": { "retry-after": "0" }, "body": { "error": { "type": "engine", "code": "engine_busy", "message": "x", "retryable": true } } } });
    let (m, c) = scripted(json!({ "exchanges": [busy, { "respond": { "status": 200, "body": { "data": { "id": "ord_1", "status": "working" } } } }] })).await;
    let o = RequestOptions { idempotency_key: Some("my-key-1".into()), ..Default::default() };
    assert_eq!("ord_1", c.orders().modify_with("SANDBOX-00042-001", "ord_1", &ModifyOrder::default(), &o).await.unwrap().id);
    let keys: Vec<String> = requests(&m.url).await.iter().map(|r| r["headers"]["idempotency-key"].as_str().unwrap_or("").to_string()).collect();
    assert_eq!(vec!["my-key-1"; 2], keys, "sent again with the key given");
}

#[tokio::test]
async fn error_keeps_the_answer_headers() {
    let envelope = json!({ "type": "conflict", "code": "account_not_ready", "message": "x", "request_id": "req_1", "retryable": false });
    let (_m, c) = scripted(json!({ "exchanges": [{ "respond": { "status": 409, "headers": { "x-ratelimit-remaining": "7" }, "body": { "error": envelope } } }] })).await;
    let e = c.me().get().await.unwrap_err();
    assert_eq!(Some("7"), e.headers.get("x-ratelimit-remaining").and_then(|v| v.to_str().ok()));
}

#[tokio::test]
async fn raw_request() {
    let busy = json!({ "respond": { "status": 503, "headers": { "retry-after": "0" }, "body": { "error": { "type": "engine", "code": "engine_busy", "message": "x" } } } });
    let not_modified = json!({ "respond": { "status": 304, "headers": { "etag": "\"v1-abc\"" } } });
    let (m, c) = scripted(json!({ "exchanges": [busy, { "respond": { "status": 200, "body": { "data": { "a_new_route": true } } } }, busy, not_modified] })).await;
    let r = c.request("get", "/v1/a-new-route", &[("market", "BTC-USD")], None, &RequestOptions::default()).await.unwrap();
    assert_eq!((200, json!({ "data": { "a_new_route": true } })), (r.status, serde_json::from_slice::<Value>(&r.body).unwrap()));
    let e = c.request("POST", "/v1/a-new-route", &[], Some(&json!({ "size": "0.01" })), &RequestOptions::default()).await.unwrap_err();
    assert_eq!(("ServerError", "engine_busy", Some(503)), (e.kind.class(), e.code.as_str(), e.status));
    // a 304 is no error: the answer as it came, its ETag with it
    let etag = RequestOptions { headers: vec![("If-None-Match".into(), "\"v1-abc\"".into())], ..Default::default() };
    let r = c.request("GET", "/v1/me", &[], None, &etag).await.unwrap();
    assert_eq!((304, Some("\"v1-abc\"")), (r.status, r.headers.get("etag").and_then(|v| v.to_str().ok())));
    let got = requests(&m.url).await;
    assert_eq!(4, got.len(), "a GET tried again, a POST without a key not");
    assert_eq!((&json!("GET"), &json!("/v1/a-new-route"), &json!({ "market": "BTC-USD" })), (&got[0]["method"], &got[0]["path"], &got[0]["query"]));
    assert_eq!(format!("Bearer {}", key()), got[0]["headers"]["authorization"].as_str().unwrap());
    assert_eq!((&json!("POST"), &json!({ "size": "0.01" })), (&got[2]["method"], &got[2]["body"]));
    assert_eq!("\"v1-abc\"", got[3]["headers"]["if-none-match"]);
    assert_eq!("invalid_request", c.request("GET", "v1/me", &[], None, &RequestOptions::default()).await.unwrap_err().code);
}

#[tokio::test(flavor = "multi_thread")]
async fn market_stream_list_and_ping() {
    let script = json!([{ "send": { "type": "hello", "heartbeat_ms": 15000 } }, { "expect": "list" }, { "reply": { "type": "list", "subscriptions": [] } }, { "expect": "ping" }, { "reply": { "type": "pong", "time": 1 } }, { "wait_ms": 3000 }]);
    let (m, c) = scripted(json!({ "connections": [script] })).await;
    let mk = c.market_stream();
    assert_eq!(json!([]), tokio::time::timeout(Duration::from_secs(5), mk.list()).await.expect("a list in time").unwrap()["subscriptions"]);
    assert_eq!("pong", tokio::time::timeout(Duration::from_secs(5), mk.ping()).await.expect("a pong in time").unwrap()["type"]);
    drop(mk);
    assert_eq!(json!([{ "op": "list", "id": 1 }, { "op": "ping", "id": 2 }]), requests(&m.url).await[0]["messages"]);
}

#[tokio::test(flavor = "multi_thread")]
async fn market_subscriptions_by_market_and_interval() {
    let subscribed = |channel: &str| json!({ "reply": { "type": "subscribed", "channel": channel, "symbols": ["BTC-USD"], "rejected": [] } });
    let hello = json!({ "send": { "type": "hello", "heartbeat_ms": 15000 } });
    let first = json!([hello, { "expect": "subscribe" }, subscribed("candles"), { "expect": "subscribe" }, subscribed("candles"), { "expect": "subscribe" }, subscribed("ticker"), { "expect": "subscribe" }, subscribed("ticker"),
        { "expect": "unsubscribe" }, { "reply": { "type": "unsubscribed", "channel": "candles", "symbols": ["BTC-USD"], "rejected": [] } }, { "close": 1012 }]);
    let (m, c) = scripted(json!({ "connections": [first, [hello, { "expect": "subscribe" }, subscribed("ticker"), { "wait_ms": 3000 }]] })).await;
    let mk = c.market_stream();
    let params = |v: Value| v.as_object().cloned().unwrap();
    answer(mk.subscribe("candles", &["BTC-USD"], params(json!({ "interval": "1m", "history": 10 })))).await;
    answer(mk.subscribe("candles", &["BTC-USD"], params(json!({ "interval": "5m" })))).await;
    answer(mk.subscribe("ticker", &["BTC-USD"], params(json!({ "interval_ms": 250 })))).await;
    answer(mk.subscribe("ticker", &["BTC-USD"], params(json!({ "interval_ms": 500 })))).await;
    // no interval: every interval of the market; its other params are not named
    answer(mk.unsubscribe("candles", &["BTC-USD"], params(json!({ "history": 10 })))).await;
    let again = connection(&m.url, 1).await;
    drop(mk);
    assert_eq!(json!({ "op": "unsubscribe", "channel": "candles", "symbols": ["BTC-USD"], "id": 5 }), requests(&m.url).await[0]["messages"][4]);
    assert_eq!(json!([{ "op": "subscribe", "channel": "ticker", "symbols": ["BTC-USD"], "interval_ms": 500, "id": 6 }]), again["messages"], "one ticker subscription, its last settings; no candles");
}

#[tokio::test(flavor = "multi_thread")]
async fn market_stream_refused() {
    let refused = json!({ "refuse": 403, "body": { "error": { "type": "permission", "code": "insufficient_permission", "message": "x", "request_id": "req_1", "retryable": false } } });
    let (m, c) = scripted(json!({ "connections": [[refused]] })).await;
    let mk = c.market_stream();
    let e = tokio::time::timeout(Duration::from_secs(5), mk.subscribe("ticker", &["BTC-USD"], Map::new())).await.expect("the end in time").unwrap_err();
    assert_eq!(("PermissionError", "insufficient_permission", Some(403)), (e.kind.class(), e.code.as_str(), e.status));
    tokio::time::sleep(Duration::from_millis(500)).await;
    drop(mk);
    assert_eq!(1, requests(&m.url).await.len(), "never reconnecting");
}

#[tokio::test(flavor = "multi_thread")]
async fn account_stream_error_frames() {
    let hello = json!({ "send": { "type": "hello", "stream_id": "str_1", "environment": "sandbox", "heartbeat_ms": 15000 } });
    // too_many_accounts refuses the whole subscribe: no snapshot_end follows
    let refused = json!({ "send": { "type": "error", "code": "too_many_accounts", "details": { "max": 50 } } });
    let (m, c) = scripted(json!({ "connections": [[hello, { "expect": "subscribe" }, refused, { "wait_ms": 3000 }]] })).await;
    let mut s = c.account_stream(AccountStreamOptions::default());
    tokio::time::timeout(Duration::from_secs(5), s.ready()).await.expect("ready() not left waiting");
    let e = tokio::time::timeout(Duration::from_secs(5), s.next()).await.expect("the error in time").unwrap().unwrap_err();
    assert_eq!(("ValidationError", "too_many_accounts", Some(json!({ "max": 50 }))), (e.kind.class(), e.code.as_str(), e.details.clone()));
    assert!(s.next().await.is_none(), "the stream ended");
    assert_eq!("stream_closed", s.ping().await.unwrap_err().code, "an ask after the end");
    drop(s);
    assert_eq!(1, requests(&m.url).await.len(), "never reconnecting");
}

#[tokio::test(flavor = "multi_thread")]
async fn account_stream_ping_unsubscribe_and_risk() {
    let accounts = json!(["SANDBOX-00042-001", "SANDBOX-00042-002"]);
    let hello = json!({ "send": { "type": "hello", "stream_id": "str_1", "environment": "sandbox", "heartbeat_ms": 15000, "accounts": accounts } });
    let event = json!({ "id": "evt_1", "type": "order.created", "account_id": "SANDBOX-00042-001", "time": 1791460800000u64, "source": "api", "data": {} });
    let risk = json!({ "type": "risk", "account_id": "SANDBOX-00042-001", "data": { "equity": "10000" } });
    let first = json!([hello, { "expect": "subscribe" }, { "send": { "type": "snapshot_end", "accounts": accounts } }, { "send": risk }, { "send": { "type": "event", "seq": 1, "replayed": true, "event": event } },
        { "expect": "ping" }, { "reply": { "type": "pong", "time": 1 } }, { "expect": "unsubscribe" }, { "reply": { "type": "unsubscribed", "accounts": ["SANDBOX-00042-002"] } }, { "close": 1012 }]);
    let m = start_mock();
    load(&m.url, json!({ "connections": [first, [hello, { "expect": "subscribe" }, { "wait_ms": 3000 }]] })).await;
    let lines = Arc::new(Mutex::new(Vec::new()));
    let log = lines.clone();
    let ws = format!("ws{}", &m.url[4..]);
    let c = Client::builder().api_key(key()).base_url(&m.url).stream_urls(format!("{ws}/v1/stream"), "").logger(move |_, line| log.lock().unwrap().push(line.to_string())).build().unwrap();
    let mut s = c.account_stream(AccountStreamOptions { risk: true, ..Default::default() });
    // the risk frame as it came, then the event with its seq and replayed
    let next = async |s: &mut phoenixperps::AccountStream| tokio::time::timeout(Duration::from_secs(5), s.next_message()).await.expect("a message in time").unwrap().unwrap();
    assert!(matches!(next(&mut s).await, AccountMessage::Risk(r) if r == risk));
    let AccountMessage::Event(e) = next(&mut s).await else { panic!("an event") };
    assert_eq!(("evt_1", Some(1), true), (e.event.id.as_str(), e.seq, e.replayed));
    assert_eq!("pong", answer(s.ping()).await["type"]);
    assert_eq!(json!(["SANDBOX-00042-002"]), answer(s.unsubscribe(&["SANDBOX-00042-002"])).await["accounts"]);
    let again = connection(&m.url, 1).await;
    drop(s);
    assert_eq!(json!([{ "op": "subscribe", "risk": true }, { "op": "ping" }, { "op": "unsubscribe", "accounts": ["SANDBOX-00042-002"] }]), requests(&m.url).await[0]["messages"]);
    assert_eq!(json!({ "op": "subscribe", "accounts": ["SANDBOX-00042-001"], "risk": true }), again["messages"][0], "the account unsubscribed stays out");
    assert!(lines.lock().unwrap().iter().any(|l| l.contains("account stream reconnects") && l.contains("1012")), "one log line per reconnect, with its cause");
}
