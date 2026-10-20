//! The two read-only streams (docs/BEHAVIOUR.md "Streams"; orders always go through REST).
//!
//! AccountStream: the key on the upgrade; hello, one snapshot per account, snapshot_end (`ready()`), then events, each
//! once (replays are told apart by their event id) with their frame's seq and replayed, and risk frames as they come
//! (with risk set). It reconnects by itself with ?resume=<last event id>, waiting min(30 s, 0.5 s x 2^n) with jitter
//! (0 to 3 s after 1012, the server restarting). A resume the server cannot cover (its resume_gap notice) and a 4008
//! (too far behind: it reconnects without resume) are told as a gap after the last event told. 4001 (the key ended)
//! ends it with an Authentication error; an error frame (its subscribe refused: too_many_accounts,
//! account_not_found), which every reconnect would get again, with a Validation error; 4013 waits longer. `ping()` and
//! `unsubscribe(accounts)` answer the server's pong and unsubscribed; an account unsubscribed stays out after a
//! reconnect (the subscribe sent again names what is left).
//!
//! MarketStream: `subscribe("ticker", &["BTC-USD"], params)` answers once the server took it. A subscription is a
//! channel, a market and (candles) an interval; its other params are settings the last subscribe sets. Every
//! subscription the server took is made again after a reconnect (its history asked again), and an ask still unanswered
//! goes again on the next connection. `unsubscribe` names the markets and (candles) the interval: without one, every
//! interval of those markets. `list()` and `ping()` answer the server's list and pong; `limits()` are the hello's. A
//! slow reader loses the oldest frames, never the connection.
//!
//! Both: three heartbeats without a frame mean a dead connection. An upgrade refused with 429 or 5xx, or a network
//! failure, reconnects after its Retry-After (else the backoff); any other refusal ends the stream with the API's own
//! error, as a REST call would. One log line per reconnect, with its cause.

use crate::client::{random_unit, Client};
use crate::error::{Error, ErrorKind};
use crate::{Event, Result};
use futures_util::{SinkExt, StreamExt};
use serde_json::{json, Map, Value};
use std::collections::{BTreeMap, HashMap, HashSet, VecDeque};
use std::sync::{Arc, Mutex};
use std::time::Duration;
use tokio::sync::{broadcast, mpsc, oneshot, watch};
use tokio::task::JoinHandle;
use tokio::time::Instant;
use tokio_tungstenite::tungstenite::client::IntoClientRequest;
use tokio_tungstenite::tungstenite::http::HeaderValue;
use tokio_tungstenite::tungstenite::{self, Message};
use tokio_tungstenite::{MaybeTlsStream, WebSocketStream};

type Socket = WebSocketStream<MaybeTlsStream<tokio::net::TcpStream>>;
const LRU: usize = 2048;

fn backoff(n: u32) -> Duration {
    Duration::from_secs_f64(random_unit() * 30f64.min(0.5 * 2f64.powi(n.min(16) as i32)))
}

fn restart() -> Duration {
    Duration::from_secs_f64(random_unit() * 3.0)
}

enum Dial {
    Open(Box<Socket>),
    /// Refused before the upgrade: the API's error, as a REST call would have it.
    Refused(Error),
    /// The network failed (why).
    Failed(String),
}

async fn dial(c: &Client, url: &str) -> Dial {
    let mut req = match url.into_client_request() {
        Ok(r) => r,
        Err(e) => return Dial::Failed(e.to_string()),
    };
    let h = req.headers_mut();
    // no key: no Authorization (the server answers missing_api_key)
    if let Some(Ok(a)) = c.key().map(|k| HeaderValue::from_str(&format!("Bearer {k}"))) {
        h.insert("authorization", a);
    }
    if let Ok(u) = HeaderValue::from_str(&c.user_agent()) {
        h.insert("user-agent", u);
    }
    match tokio_tungstenite::connect_async(req).await {
        Ok((ws, _)) => Dial::Open(Box::new(ws)),
        Err(tungstenite::Error::Http(res)) => Dial::Refused(Error::from_answer(res.status().as_u16(), res.headers(), res.body().as_deref().unwrap_or_default(), None)),
        Err(e) => Dial::Failed(e.to_string()),
    }
}

/// The wait after a refused upgrade: 429 or 5xx, its Retry-After (else the backoff); None for any other, which ends the
/// stream.
fn refusal_wait(e: &Error, attempt: u32) -> Option<Duration> {
    let s = e.status.unwrap_or(0);
    (s == 429 || s >= 500).then(|| e.retry_after.unwrap_or_else(|| backoff(attempt)))
}

/// The wait before the next connection, told in one log line with its cause.
fn reconnect(c: &Client, stream: &str, wait: Duration, cause: &str) -> Duration {
    c.log("info", &format!("phoenixperps: the {stream} stream reconnects in {} ms ({cause})", wait.as_millis()));
    wait
}

fn close_code(m: &Message) -> Option<u16> {
    match m {
        Message::Close(Some(f)) => Some(u16::from(f.code)),
        Message::Close(None) => Some(1005),
        _ => None,
    }
}

/// What an account stream follows.
#[derive(Clone, Debug, Default)]
pub struct AccountStreamOptions {
    /// The accounts to follow (empty: every account of the key).
    pub accounts: Vec<String>,
    /// Live risk frames too.
    pub risk: bool,
    /// Start after this event id.
    pub resume: Option<String>,
}

/// An event of the account stream, with its frame's seq (this connection's count) and replayed (sent again after a
/// resume).
#[derive(Clone, Debug)]
pub struct StreamEvent {
    /// The event.
    pub event: Event,
    /// Its count on this connection.
    pub seq: Option<i64>,
    /// Sent again after a resume.
    pub replayed: bool,
}

/// What the account stream tells, in order (`AccountStream::next_message`).
#[derive(Clone, Debug)]
#[non_exhaustive]
pub enum AccountMessage {
    /// An event of the accounts, told once.
    Event(StreamEvent),
    /// An account's risk as it changed (with risk set): the frame as it came ({ type, account_id, data }).
    Risk(Value),
    /// Events may be missing here (a resume the server could not cover, or a reconnect without resume after 4008):
    /// read them from GET /v1/events?after=<after>.
    Gap {
        /// The last event told (None: none was).
        after: Option<String>,
    },
}

#[derive(Default)]
struct Shared {
    snapshots: HashMap<String, Value>,
    risks: HashMap<String, Value>,
    last_event_id: Option<String>,
}

/// A ping or an unsubscribe of the account stream, and who waits for its answer.
struct AccountAsk {
    message: Value,
    reply: oneshot::Sender<Result<Value>>,
}

/// The account stream: snapshots, then every event of the accounts, each once.
pub struct AccountStream {
    rx: mpsc::Receiver<Result<AccountMessage>>,
    cmd: mpsc::UnboundedSender<AccountAsk>,
    shared: Arc<Mutex<Shared>>,
    ready: watch::Receiver<bool>,
    task: JoinHandle<()>,
}

impl Client {
    /// Connects to the account stream (inside a tokio runtime); it runs until dropped.
    pub fn account_stream(&self, options: AccountStreamOptions) -> AccountStream {
        let (tx, rx) = mpsc::channel(1024);
        let (cmd, cmd_rx) = mpsc::unbounded_channel();
        let (ready_tx, ready) = watch::channel(false);
        let shared = Arc::new(Mutex::new(Shared { last_event_id: options.resume.clone(), ..Default::default() }));
        let task = tokio::spawn(run_account(self.clone(), options, tx, cmd_rx, shared.clone(), ready_tx));
        AccountStream { rx, cmd, shared, ready, task }
    }

    /// Connects to the market data stream (inside a tokio runtime); it runs until dropped.
    pub fn market_stream(&self) -> MarketStream {
        let (cmd, cmd_rx) = mpsc::unbounded_channel();
        let (frames_tx, frames) = broadcast::channel(1024);
        let limits = Arc::new(Mutex::new(None));
        let task = tokio::spawn(run_market(self.clone(), cmd_rx, frames_tx, limits.clone()));
        MarketStream { cmd, frames, limits, task }
    }
}

impl AccountStream {
    /// The next event; None once the stream ended (after the error that ended it, if any). A gap comes as an error
    /// that does not end it (ErrorKind::Connection, resume_gap, details { after }); risk frames are told by
    /// `next_message()`.
    pub async fn next(&mut self) -> Option<Result<Event>> {
        loop {
            match self.next_message().await? {
                Ok(AccountMessage::Event(e)) => return Some(Ok(e.event)),
                Ok(AccountMessage::Gap { after }) => return Some(Err(gap(after))),
                Ok(_) => {}
                Err(e) => return Some(Err(e)),
            }
        }
    }

    /// The next message: an event (each once, with its seq and replayed), a risk frame (with risk set) or a gap; None
    /// once the stream ended (after the error that ended it, if any).
    pub async fn next_message(&mut self) -> Option<Result<AccountMessage>> {
        self.rx.recv().await
    }

    /// Waits until the first snapshots are in, or the stream ended (`next()` then tells why).
    pub async fn ready(&mut self) {
        let _ = self.ready.wait_for(|r| *r).await;
    }

    /// The server's pong.
    pub async fn ping(&self) -> Result<Value> {
        self.ask(json!({ "op": "ping" })).await
    }

    /// Stops following accounts, also after a reconnect; the server's unsubscribed frame.
    pub async fn unsubscribe(&self, accounts: &[&str]) -> Result<Value> {
        self.ask(json!({ "op": "unsubscribe", "accounts": accounts })).await
    }

    async fn ask(&self, message: Value) -> Result<Value> {
        let (reply, rx) = oneshot::channel();
        self.cmd.send(AccountAsk { message, reply }).map_err(|_| closed())?;
        rx.await.map_err(|_| closed())?
    }

    /// The last snapshot frame of an account.
    pub fn snapshot(&self, account_id: &str) -> Option<Value> {
        self.shared.lock().unwrap().snapshots.get(account_id).cloned()
    }

    /// The last risk frame of an account (with risk set).
    pub fn risk(&self, account_id: &str) -> Option<Value> {
        self.shared.lock().unwrap().risks.get(account_id).cloned()
    }

    /// The id of the last event told (the resume point).
    pub fn last_event_id(&self) -> Option<String> {
        self.shared.lock().unwrap().last_event_id.clone()
    }
}

impl Drop for AccountStream {
    fn drop(&mut self) {
        self.task.abort();
    }
}

/// A gap as `next()` tells it: an error that does not end the stream.
fn gap(after: Option<String>) -> Error {
    let mut e = Error::new(ErrorKind::Connection, "resume_gap", "Events may be missing here: read them from GET /v1/events?after= the last event told.");
    e.details = Some(json!({ "after": after }));
    e
}

/// The end of the account stream: its error to the reader, and to every ask still waiting (those not taken yet too).
async fn finish(tx: &mpsc::Sender<Result<AccountMessage>>, cmd: &mut mpsc::UnboundedReceiver<AccountAsk>, pending: &mut VecDeque<AccountAsk>, e: Error) {
    while let Ok(a) = cmd.try_recv() {
        pending.push_back(a);
    }
    for p in pending.drain(..) {
        let _ = p.reply.send(Err(e.clone()));
    }
    let _ = tx.send(Err(e)).await;
}

/// Takes an ask: an account unsubscribed stays out of every subscribe sent again.
fn take_ask(removed: &mut HashSet<String>, pending: &mut VecDeque<AccountAsk>, a: AccountAsk) {
    if a.message["op"] == "unsubscribe" {
        removed.extend(a.message["accounts"].as_array().into_iter().flatten().filter_map(Value::as_str).map(str::to_uppercase));
    }
    pending.push_back(a);
}

async fn run_account(c: Client, o: AccountStreamOptions, tx: mpsc::Sender<Result<AccountMessage>>, mut cmd: mpsc::UnboundedReceiver<AccountAsk>, shared: Arc<Mutex<Shared>>, ready: watch::Sender<bool>) {
    let mut attempt = 0u32;
    let mut resume = true;
    let mut seen: HashSet<String> = HashSet::new();
    let mut order: VecDeque<String> = VecDeque::new();
    // the accounts unsubscribed, and the asks unanswered (sent again on the next connection)
    let mut removed: HashSet<String> = HashSet::new();
    let mut pending: VecDeque<AccountAsk> = VecDeque::new();
    loop {
        let last = shared.lock().unwrap().last_event_id.clone();
        let url = match (&last, resume) {
            (Some(id), true) => format!("{}?resume={}", c.inner.account_stream_url, encode(id)),
            _ => c.inner.account_stream_url.clone(),
        };
        resume = true;
        let (wait, cause) = match dial(&c, &url).await {
            Dial::Refused(e) => match refusal_wait(&e, attempt) {
                Some(w) => (w, format!("upgrade refused: {}", e.status.unwrap_or(0))),
                None => return finish(&tx, &mut cmd, &mut pending, e).await,
            },
            Dial::Failed(why) => (backoff(attempt), why),
            Dial::Open(mut ws) => {
                let mut open = false;
                let (mut heartbeat, mut alive) = (Duration::from_secs(15), Instant::now());
                let w = loop {
                    tokio::select! {
                        m = ws.next() => {
                            alive = Instant::now();
                            let m = match m {
                                Some(Ok(m)) => m,
                                Some(Err(e)) => break (backoff(attempt), e.to_string()),
                                None => break (backoff(attempt), "closed".to_string()),
                            };
                            if let Some(code) = close_code(&m) {
                                match code {
                                    4001 => return finish(&tx, &mut cmd, &mut pending, Error::new(ErrorKind::Authentication, "stream_credential_ended", "The key or session of this stream ended.")).await,
                                    4008 => {
                                        resume = false;
                                        // what the server dropped is a gap after the last event told
                                        let after = shared.lock().unwrap().last_event_id.clone();
                                        if tx.send(Ok(AccountMessage::Gap { after })).await.is_err() {
                                            return;
                                        }
                                        break (backoff(attempt), "4008: too far behind".to_string());
                                    }
                                    4013 => break (Duration::from_secs(30) + backoff(attempt), "4013: too many streams".to_string()),
                                    1012 => break (restart(), "1012: the server restarts".to_string()),
                                    _ => break (backoff(attempt), format!("closed {code}")),
                                }
                            }
                            let Message::Text(text) = m else { continue };
                            let Ok(f) = serde_json::from_str::<Value>(text.as_str()) else { continue };
                            match f.get("type").and_then(Value::as_str).unwrap_or("") {
                                "hello" => {
                                    attempt = 0;
                                    open = true;
                                    if let Some(ms) = f.get("heartbeat_ms").and_then(Value::as_u64).filter(|m| *m > 0) {
                                        heartbeat = Duration::from_millis(ms);
                                    }
                                    // the subscribe names what is left after an unsubscribe (every account: the hello's)
                                    let mut msg = json!({ "op": "subscribe" });
                                    let wanted = if !o.accounts.is_empty() {
                                        Some(o.accounts.clone())
                                    } else if removed.is_empty() {
                                        None
                                    } else {
                                        f["accounts"].as_array().map(|a| a.iter().filter_map(Value::as_str).map(str::to_string).collect::<Vec<_>>())
                                    };
                                    if let Some(wanted) = wanted {
                                        msg["accounts"] = json!(wanted.into_iter().filter(|a| !removed.contains(&a.to_uppercase())).collect::<Vec<_>>());
                                    }
                                    if o.risk {
                                        msg["risk"] = json!(true);
                                    }
                                    let mut sent = ws.send(Message::text(msg.to_string())).await.is_ok();
                                    for p in &pending {
                                        sent = sent && ws.send(Message::text(p.message.to_string())).await.is_ok();
                                    }
                                    if !sent {
                                        break (backoff(attempt), "the subscribe could not be sent".to_string());
                                    }
                                }
                                "snapshot" => {
                                    let id = f.get("account_id").and_then(Value::as_str).unwrap_or("").to_string();
                                    shared.lock().unwrap().snapshots.insert(id, f);
                                }
                                "snapshot_end" => {
                                    let _ = ready.send(true);
                                }
                                "risk" => {
                                    let id = f.get("account_id").and_then(Value::as_str).unwrap_or("").to_string();
                                    shared.lock().unwrap().risks.insert(id, f.clone());
                                    if tx.send(Ok(AccountMessage::Risk(f))).await.is_err() {
                                        return;
                                    }
                                }
                                "notice" if f["code"] == "resume_gap" => {
                                    let after = shared.lock().unwrap().last_event_id.clone().or_else(|| f["after"].as_str().map(str::to_string));
                                    if tx.send(Ok(AccountMessage::Gap { after })).await.is_err() {
                                        return;
                                    }
                                }
                                t @ ("pong" | "unsubscribed") => {
                                    let op = if t == "pong" { "ping" } else { "unsubscribe" };
                                    if let Some(p) = pending.iter().position(|p| p.message["op"] == op).and_then(|i| pending.remove(i)) {
                                        let _ = p.reply.send(Ok(f));
                                    }
                                }
                                "error" => {
                                    // the subscribe refused, as it would be on every reconnect (it is sent again as it is)
                                    let _ = ws.close(None).await;
                                    return finish(&tx, &mut cmd, &mut pending, refusal(&f)).await;
                                }
                                "event" => {
                                    let Some(e) = f.get("event").cloned().and_then(|e| serde_json::from_value::<Event>(e).ok()) else { continue };
                                    if !seen.insert(e.id.clone()) {
                                        continue;
                                    }
                                    order.push_back(e.id.clone());
                                    if order.len() > LRU {
                                        if let Some(old) = order.pop_front() {
                                            seen.remove(&old);
                                        }
                                    }
                                    shared.lock().unwrap().last_event_id = Some(e.id.clone());
                                    let e = StreamEvent { seq: f.get("seq").and_then(Value::as_i64), replayed: f.get("replayed").and_then(Value::as_bool).unwrap_or(false), event: e };
                                    if tx.send(Ok(AccountMessage::Event(e))).await.is_err() {
                                        return; // nobody reads any more
                                    }
                                }
                                _ => {}
                            }
                        }
                        // a deadline, not a timeout on each read: asks coming in must not keep a dead connection open
                        _ = tokio::time::sleep_until(alive + heartbeat * 3) => break (backoff(attempt), "no heartbeat".to_string()),
                        a = cmd.recv() => {
                            let Some(a) = a else { return };
                            let failed = open && ws.send(Message::text(a.message.to_string())).await.is_err();
                            take_ask(&mut removed, &mut pending, a);
                            if failed {
                                break (backoff(attempt), "an ask could not be sent".to_string());
                            }
                        }
                    }
                };
                let _ = ws.close(None).await;
                w
            }
        };
        attempt += 1;
        let sleep = tokio::time::sleep(reconnect(&c, "account", wait, &cause));
        tokio::pin!(sleep);
        loop {
            tokio::select! {
                _ = &mut sleep => break,
                a = cmd.recv() => {
                    let Some(a) = a else { return };
                    take_ask(&mut removed, &mut pending, a); // goes out on the next connection
                }
            }
        }
    }
}

/// A stream's error frame as a Validation error: its code, its message (else the code), its details with the account
/// it is about.
fn refusal(f: &Value) -> Error {
    let code = f.get("code").and_then(Value::as_str).unwrap_or("stream_refused");
    let mut e = Error::new(ErrorKind::Validation, code, f.get("message").and_then(Value::as_str).unwrap_or(code));
    let mut details = f.get("details").and_then(Value::as_object).cloned().unwrap_or_default();
    if let Some(a) = f.get("account_id") {
        details.insert("account_id".into(), a.clone());
    }
    if !details.is_empty() {
        e.details = Some(Value::Object(details));
    }
    e
}

fn encode(s: &str) -> String {
    s.bytes().map(|b| if b.is_ascii_alphanumeric() || b"-._~".contains(&b) { (b as char).to_string() } else { format!("%{b:02X}") }).collect()
}

/// One frame of the market stream: its type (the channel: ticker, book, trades, candles, stats, status; or
/// snapshot_end), its symbol and data, and the frame as it came.
#[derive(Clone, Debug)]
pub struct MarketFrame {
    /// The channel.
    pub r#type: String,
    /// The market.
    pub symbol: Option<String>,
    /// The data.
    pub data: Option<Value>,
    /// The frame as it came.
    pub raw: Value,
}

enum Command {
    Ask { message: Map<String, Value>, reply: oneshot::Sender<Result<Value>> },
}

/// The market data stream.
pub struct MarketStream {
    cmd: mpsc::UnboundedSender<Command>,
    frames: broadcast::Receiver<Result<MarketFrame>>,
    limits: Arc<Mutex<Option<Value>>>,
    task: JoinHandle<()>,
}

impl MarketStream {
    /// Follows symbols on a channel with its params (candles: its interval; the others are settings, the last
    /// subscribe's); the server's reply (the symbols taken and the ones it refused).
    pub async fn subscribe(&self, channel: &str, symbols: &[&str], params: Map<String, Value>) -> Result<Value> {
        self.ask(sub_message("subscribe", channel, symbols, &params)).await
    }

    /// Stops following symbols on a channel: params names the interval (candles) and nothing else; without one, every
    /// interval of those markets. The server's reply (the symbols ended, and the ones that were not followed).
    pub async fn unsubscribe(&self, channel: &str, symbols: &[&str], params: Map<String, Value>) -> Result<Value> {
        let interval: Map<String, Value> = params.into_iter().filter(|(k, _)| k == "interval").collect();
        self.ask(sub_message("unsubscribe", channel, symbols, &interval)).await
    }

    /// Your subscriptions as the server holds them (its list frame).
    pub async fn list(&self) -> Result<Value> {
        self.ask(Map::from_iter([("op".to_string(), json!("list"))])).await
    }

    /// The server's pong.
    pub async fn ping(&self) -> Result<Value> {
        self.ask(Map::from_iter([("op".to_string(), json!("ping"))])).await
    }

    async fn ask(&self, message: Map<String, Value>) -> Result<Value> {
        let (reply, rx) = oneshot::channel();
        self.cmd.send(Command::Ask { message, reply }).map_err(|_| closed())?;
        rx.await.map_err(|_| closed())?
    }

    /// The next frame; None once the stream ended (after the error that ended it, if any).
    pub async fn next(&mut self) -> Option<Result<MarketFrame>> {
        loop {
            match self.frames.recv().await {
                Ok(f) => return Some(f),
                Err(broadcast::error::RecvError::Lagged(_)) => continue, // the oldest frames are gone
                Err(broadcast::error::RecvError::Closed) => return None,
            }
        }
    }

    /// The per-connection limits the server announced in its hello (subscriptions, symbols_per_subscribe,
    /// messages_per_s).
    pub fn limits(&self) -> Option<Value> {
        self.limits.lock().unwrap().clone()
    }
}

impl Drop for MarketStream {
    fn drop(&mut self) {
        self.task.abort();
    }
}

fn closed() -> Error {
    Error::new(ErrorKind::StreamClosed, "stream_closed", "The stream is closed.")
}

fn sub_message(op: &str, channel: &str, symbols: &[&str], params: &Map<String, Value>) -> Map<String, Value> {
    let mut m = params.clone();
    m.insert("op".into(), json!(op));
    m.insert("channel".into(), json!(channel));
    if channel != "status" {
        m.insert("symbols".into(), json!(symbols));
    }
    m
}

struct Pending {
    message: Map<String, Value>,
    reply: oneshot::Sender<Result<Value>>,
}

/// A subscription: its channel, its market ("" for status) and its interval (candles).
type SubKey = (String, String, Option<String>);

struct Market {
    /// What the server took, with its settings.
    subs: BTreeMap<SubKey, Map<String, Value>>,
    pending: BTreeMap<u64, Pending>,
    next_id: u64,
}

/// The channel, the markets (upper case; none named: [""] for status, [] for every one of the channel) and the
/// interval an ask names.
fn names(m: &Map<String, Value>) -> (String, Vec<String>, Option<String>) {
    let channel = m.get("channel").and_then(Value::as_str).unwrap_or("").to_string();
    let symbols = match m.get("symbols").and_then(Value::as_array) {
        Some(a) => a.iter().filter_map(Value::as_str).map(str::to_uppercase).collect(),
        None => vec![String::new()],
    };
    (channel, symbols, m.get("interval").and_then(Value::as_str).map(str::to_string))
}

impl Market {
    /// Takes an ask: an id; the message to send now (when open).
    fn take(&mut self, mut message: Map<String, Value>, reply: oneshot::Sender<Result<Value>>) -> Value {
        self.next_id += 1;
        message.insert("id".into(), json!(self.next_id));
        let v = Value::Object(message.clone());
        self.pending.insert(self.next_id, Pending { message, reply });
        v
    }

    /// What the server answered an ask: a subscribe taken sets its markets (its settings the last ones), an
    /// unsubscribe ends those it named.
    fn answered(&mut self, ask: &Map<String, Value>, f: &Value) {
        let (channel, symbols, interval) = names(ask);
        match ask.get("op").and_then(Value::as_str) {
            Some("subscribe") => {
                let settings: Map<String, Value> = ask.iter().filter(|(k, _)| !matches!(k.as_str(), "op" | "channel" | "symbols" | "interval" | "id")).map(|(k, v)| (k.clone(), v.clone())).collect();
                let taken: Vec<String> = match f.get("symbols").and_then(Value::as_array) {
                    Some(a) if ask.contains_key("symbols") => a.iter().filter_map(Value::as_str).map(str::to_uppercase).collect(),
                    _ => symbols,
                };
                for s in taken {
                    self.subs.insert((channel.clone(), s, interval.clone()), settings.clone());
                }
            }
            Some("unsubscribe") => self.subs.retain(|(c, s, i), _| !(c == &channel && (symbols.is_empty() || symbols.contains(s)) && (interval.is_none() || i == &interval))),
            _ => {}
        }
    }

    /// Every subscription again, as few subscribes as its channels, intervals and settings allow.
    fn again(&mut self) -> Vec<Value> {
        // by channel, interval and settings (as JSON): their markets
        let mut groups: BTreeMap<(String, Option<String>, String), Vec<String>> = BTreeMap::new();
        for ((channel, symbol, interval), settings) in &self.subs {
            groups.entry((channel.clone(), interval.clone(), Value::Object(settings.clone()).to_string())).or_default().push(symbol.clone());
        }
        groups
            .into_iter()
            .map(|((channel, interval, settings), symbols)| {
                let mut params: Map<String, Value> = serde_json::from_str(&settings).unwrap_or_default();
                if let Some(i) = interval {
                    params.insert("interval".into(), json!(i));
                }
                let symbols: Vec<&str> = symbols.iter().map(String::as_str).collect();
                let mut m = sub_message("subscribe", &channel, &symbols, &params);
                self.next_id += 1;
                m.insert("id".into(), json!(self.next_id));
                Value::Object(m)
            })
            .collect()
    }
}

async fn run_market(c: Client, mut cmd: mpsc::UnboundedReceiver<Command>, frames: broadcast::Sender<Result<MarketFrame>>, limits: Arc<Mutex<Option<Value>>>) {
    let mut st = Market { subs: BTreeMap::new(), pending: BTreeMap::new(), next_id: 0 };
    let mut attempt = 0u32;
    // the end: its error to every ask still waiting (those not taken yet too), and to the reader
    let end = |st: &mut Market, cmd: &mut mpsc::UnboundedReceiver<Command>, e: Error| {
        while let Ok(Command::Ask { message, reply }) = cmd.try_recv() {
            st.take(message, reply);
        }
        for (_, p) in std::mem::take(&mut st.pending) {
            let _ = p.reply.send(Err(e.clone()));
        }
        let _ = frames.send(Err(e));
    };
    loop {
        let (wait, cause) = match dial(&c, &c.inner.market_stream_url).await {
            Dial::Refused(e) => match refusal_wait(&e, attempt) {
                Some(w) => (w, format!("upgrade refused: {}", e.status.unwrap_or(0))),
                None => return end(&mut st, &mut cmd, e),
            },
            Dial::Failed(why) => (backoff(attempt), why),
            Dial::Open(mut ws) => {
                let mut open = false;
                let (mut heartbeat, mut alive) = (Duration::from_secs(15), Instant::now());
                let w = loop {
                    tokio::select! {
                        m = ws.next() => {
                            alive = Instant::now();
                            let m = match m {
                                Some(Ok(m)) => m,
                                Some(Err(e)) => break (backoff(attempt), e.to_string()),
                                None => break (backoff(attempt), "closed".to_string()),
                            };
                            if let Some(code) = close_code(&m) {
                                if code == 4001 {
                                    end(&mut st, &mut cmd, Error::new(ErrorKind::Authentication, "stream_credential_ended", "The key of this stream ended."));
                                    return;
                                }
                                break if code == 1012 { (restart(), "1012: the server restarts".to_string()) } else { (backoff(attempt), format!("closed {code}")) };
                            }
                            let Message::Text(text) = m else { continue };
                            let Ok(f) = serde_json::from_str::<Value>(text.as_str()) else { continue };
                            let t = f.get("type").and_then(Value::as_str).unwrap_or("").to_string();
                            match t.as_str() {
                                "hello" => {
                                    attempt = 0;
                                    open = true;
                                    if let Some(ms) = f.get("heartbeat_ms").and_then(Value::as_u64).filter(|m| *m > 0) {
                                        heartbeat = Duration::from_millis(ms);
                                    }
                                    *limits.lock().unwrap() = f.get("limits").cloned();
                                    // every subscription the server took, again; then the asks still unanswered
                                    let mut again = st.again();
                                    again.extend(st.pending.values().map(|p| Value::Object(p.message.clone())));
                                    for m in again {
                                        let _ = ws.send(Message::text(m.to_string())).await;
                                    }
                                }
                                "subscribed" | "unsubscribed" | "error" | "list" | "pong" => {
                                    let Some(p) = f.get("id").and_then(Value::as_u64).and_then(|id| st.pending.remove(&id)) else { continue };
                                    if t == "error" {
                                        let _ = p.reply.send(Err(refusal(&f)));
                                    } else {
                                        st.answered(&p.message, &f);
                                        let _ = p.reply.send(Ok(f));
                                    }
                                }
                                "heartbeat" => {}
                                _ => {
                                    let _ = frames.send(Ok(MarketFrame { symbol: f.get("symbol").and_then(Value::as_str).map(str::to_string), data: f.get("data").cloned(), r#type: t, raw: f }));
                                }
                            }
                        }
                        // a deadline, not a timeout on each read: asks coming in must not keep a dead connection open
                        _ = tokio::time::sleep_until(alive + heartbeat * 3) => break (backoff(attempt), "no heartbeat".to_string()),
                        c = cmd.recv() => {
                            let Some(Command::Ask { message, reply }) = c else { return };
                            let m = st.take(message, reply);
                            if open {
                                let _ = ws.send(Message::text(m.to_string())).await;
                            }
                        }
                    }
                };
                let _ = ws.close(None).await;
                w
            }
        };
        attempt += 1;
        let sleep = tokio::time::sleep(reconnect(&c, "market", wait, &cause));
        tokio::pin!(sleep);
        loop {
            tokio::select! {
                _ = &mut sleep => break,
                c = cmd.recv() => {
                    let Some(Command::Ask { message, reply }) = c else { return };
                    st.take(message, reply); // goes out on the next connection
                }
            }
        }
    }
}
