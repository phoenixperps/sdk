use crate::error::{clip, Error, ErrorKind};
use crate::generated::meta;
use crate::page::{NextCall, Page};
use crate::Result;
use futures_util::{stream, Stream};
use reqwest::header::{HeaderMap, HeaderName, HeaderValue};
use serde::de::DeserializeOwned;
use serde::Serialize;
use serde_json::Value;
use std::collections::{HashMap, HashSet};
use std::fmt;
use std::future::Future;
use std::sync::{Arc, Mutex};
use std::time::{Duration, SystemTime, UNIX_EPOCH};

const RETRY_AFTER_MAX: Duration = Duration::from_secs(60);

/// An operation of the API.
#[derive(Debug)]
pub(crate) struct Operation {
    pub(crate) id: &'static str,
    pub(crate) method: &'static str,
    pub(crate) path: &'static str,
    pub(crate) idempotent: bool,
    pub(crate) retry_safe: bool,
    pub(crate) cursor: Option<&'static str>,
    /// Its rate-limit budget (x-pp-scope: read, trade, public; empty: not known).
    pub(crate) scope: &'static str,
}

/// The operation of the spec for this method and path (the most literal segments win).
fn route(method: &str, path: &str) -> Option<&'static Operation> {
    let segs: Vec<&str> = path.split('/').collect();
    let mut best = None;
    let mut most = -1i32;
    for op in crate::generated::resources::ops::ALL {
        let tpl: Vec<&str> = op.path.split('/').collect();
        if op.method != method || tpl.len() != segs.len() {
            continue;
        }
        let mut literal = 0i32;
        for (t, s) in tpl.iter().zip(&segs) {
            if t.starts_with('{') && !s.is_empty() {
                continue;
            }
            if t != s {
                literal = -1;
                break;
            }
            literal += 1;
        }
        if literal > most {
            best = Some(*op);
            most = literal;
        }
    }
    best
}

/// What the last answer said of the rate-limit window.
#[derive(Clone, Debug)]
pub struct RateLimit {
    /// The requests a window allows.
    pub limit: u32,
    /// The requests left in it.
    pub remaining: u32,
    /// When it starts again.
    pub reset_at: Option<SystemTime>,
}

/// The answer of a call as it came.
#[derive(Clone, Debug)]
pub struct RawResponse {
    /// The HTTP status.
    pub status: u16,
    /// The headers.
    pub headers: HeaderMap,
    /// The body.
    pub body: Vec<u8>,
    /// The request id.
    pub request_id: Option<String>,
    /// The call's Idempotency-Key.
    pub idempotency_key: Option<String>,
}

/// Receives the answer of a call as it came.
pub type ResponseHook = Arc<dyn Fn(&RawResponse) + Send + Sync>;

/// Receives (level, message).
pub type Logger = Arc<dyn Fn(&str, &str) + Send + Sync>;

/// The options of one call.
#[derive(Clone, Default)]
pub struct RequestOptions {
    /// The call's Idempotency-Key (else a new UUID, the same on every attempt).
    pub idempotency_key: Option<String>,
    /// The time one attempt may take.
    pub timeout: Option<Duration>,
    /// How often the call is tried again when that is safe.
    pub max_retries: Option<u32>,
    /// Caps an auto-paging iteration (None or 0: every item).
    pub max_items: Option<usize>,
    /// Headers added to the request.
    pub headers: Vec<(String, String)>,
    /// Receives the answer of the call as it came.
    pub on_response: Option<ResponseHook>,
}

impl fmt::Debug for RequestOptions {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        f.debug_struct("RequestOptions").field("idempotency_key", &self.idempotency_key).field("timeout", &self.timeout).field("max_retries", &self.max_retries).field("max_items", &self.max_items).finish_non_exhaustive()
    }
}

pub(crate) struct Inner {
    key: Option<String>,
    pub(crate) environment: String,
    base_url: String,
    timeout: Duration,
    max_retries: u32,
    app_info: Option<String>,
    http: reqwest::Client,
    logger: Option<Logger>,
    pub(crate) account_stream_url: String,
    pub(crate) market_stream_url: String,
    rate: Mutex<Option<RateLimit>>,
    rates: Mutex<HashMap<&'static str, RateLimit>>,
    warned: Mutex<HashSet<String>>,
}

/// The Phoenix Perpetuals API. Make one and share it (it is cheap to clone and safe for concurrent use).
#[derive(Clone)]
pub struct Client {
    pub(crate) inner: Arc<Inner>,
}

/// The options of a client.
#[derive(Default)]
pub struct ClientBuilder {
    api_key: Option<String>,
    environment: Option<String>,
    base_url: Option<String>,
    timeout: Option<Duration>,
    max_retries: Option<u32>,
    app_info: Option<String>,
    http: Option<reqwest::Client>,
    logger: Option<Logger>,
    account_stream_url: Option<String>,
    market_stream_url: Option<String>,
}

impl ClientBuilder {
    /// The key (else PHOENIXPERPS_API_KEY). Without one, the public calls (markets, status) work and any other answers
    /// the API's missing_api_key.
    pub fn api_key(mut self, v: impl Into<String>) -> Self {
        self.api_key = Some(v.into());
        self
    }

    /// live or sandbox, in any case (else the key's own, else PHOENIXPERPS_ENVIRONMENT).
    pub fn environment(mut self, v: impl Into<String>) -> Self {
        self.environment = Some(v.into());
        self
    }

    /// The API host (else PHOENIXPERPS_BASE_URL, else the environment's); the REST address the docs write, with /v1,
    /// is taken as its host.
    pub fn base_url(mut self, v: impl Into<String>) -> Self {
        self.base_url = Some(v.into());
        self
    }

    /// The time one attempt may take (30 s).
    pub fn timeout(mut self, v: Duration) -> Self {
        self.timeout = Some(v);
        self
    }

    /// How often a safe call is tried again (2).
    pub fn max_retries(mut self, v: u32) -> Self {
        self.max_retries = Some(v);
        self
    }

    /// Your app's name and version, added to the User-Agent.
    pub fn app_info(mut self, v: impl Into<String>) -> Self {
        self.app_info = Some(v.into());
        self
    }

    /// The reqwest client the calls go through.
    pub fn http_client(mut self, v: reqwest::Client) -> Self {
        self.http = Some(v);
        self
    }

    /// Receives (level, message): a line per retry and one per deprecated API version.
    pub fn logger(mut self, v: impl Fn(&str, &str) + Send + Sync + 'static) -> Self {
        self.logger = Some(Arc::new(v));
        self
    }

    /// The account and market stream URLs.
    pub fn stream_urls(mut self, account: impl Into<String>, market: impl Into<String>) -> Self {
        self.account_stream_url = Some(account.into());
        self.market_stream_url = Some(market.into());
        self
    }

    /// The client; a key of the other environment fails here, before any request.
    pub fn build(self) -> Result<Client> {
        // none: a client of the public calls (no Authorization is sent)
        let key = self.api_key.or_else(|| std::env::var("PHOENIXPERPS_API_KEY").ok()).filter(|k| !k.is_empty());
        let inferred = key.as_deref().and_then(|key| {
            ["ppk_", "ppo_"].iter().find_map(|p| {
                let rest = key.strip_prefix(p)?;
                if rest.starts_with("live_") {
                    Some("live")
                } else if rest.starts_with("test_") {
                    Some("sandbox")
                } else {
                    None
                }
            })
        });
        let env = self.environment.or_else(|| inferred.map(str::to_string)).or_else(|| std::env::var("PHOENIXPERPS_ENVIRONMENT").ok()).unwrap_or_else(|| "sandbox".to_string()).to_ascii_lowercase();
        if env != "live" && env != "sandbox" {
            return Err(Error::new(ErrorKind::Validation, "invalid_environment", "The environment is live or sandbox."));
        }
        if let Some(i) = inferred {
            if i != env {
                return Err(Error::new(ErrorKind::Authentication, "wrong_environment", format!("This key is a {i} key, not a {env} one.")));
            }
        }
        let base = self.base_url.or_else(|| std::env::var("PHOENIXPERPS_BASE_URL").ok()).unwrap_or_else(|| meta::server(&env).to_string());
        // every path starts with /v1: a base ending with it would call /v1/v1/..
        let base = base.trim_end_matches('/');
        let base = base.strip_suffix("/v1").unwrap_or(base);
        let http = match self.http {
            Some(h) => h,
            None => reqwest::Client::builder().connect_timeout(Duration::from_secs(10)).build().map_err(|e| Error::new(ErrorKind::Connection, "client_failed", e.to_string()))?,
        };
        Ok(Client {
            inner: Arc::new(Inner {
                key,
                account_stream_url: self.account_stream_url.unwrap_or_else(|| meta::account_stream(&env).to_string()),
                market_stream_url: self.market_stream_url.unwrap_or_else(|| meta::MARKET_STREAM.to_string()),
                environment: env,
                base_url: base.to_string(),
                timeout: self.timeout.unwrap_or(Duration::from_secs(30)),
                max_retries: self.max_retries.unwrap_or(2),
                app_info: self.app_info,
                http,
                logger: self.logger,
                rate: Mutex::new(None),
                rates: Mutex::new(HashMap::new()),
                warned: Mutex::new(HashSet::new()),
            }),
        })
    }
}

impl fmt::Debug for Client {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        f.debug_struct("Client").field("environment", &self.inner.environment).field("base_url", &self.inner.base_url).field("api_key", &self.masked_key()).finish()
    }
}

impl fmt::Display for Client {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "phoenixperps::Client {{ environment: {}, base_url: {}, api_key: {} }}", self.inner.environment, self.inner.base_url, self.masked_key())
    }
}

pub(crate) fn mask(key: &str) -> String {
    let from = key.len().min(4);
    let head = key[from..].find('_').map(|i| &key[..from + i + 1]).unwrap_or("");
    let tail = if key.len() > 4 { &key[key.len() - 4..] } else { key };
    format!("{head}****{tail}")
}

/// A number in [0, 1) from the system's randomness (a v4 UUID's random bits).
pub(crate) fn random_unit() -> f64 {
    (uuid::Uuid::new_v4().as_u128() >> 75) as f64 / (1u64 << 53) as f64
}

fn jitter(attempt: u32) -> Duration {
    Duration::from_secs_f64(random_unit() * (8.0f64).min(0.5 * 2f64.powi(attempt as i32)))
}

/// The Retry-After: seconds (fractions included) or an HTTP date.
pub(crate) fn retry_after(h: &HeaderMap) -> Option<Duration> {
    let v = h.get("retry-after")?.to_str().ok()?.trim();
    if let Ok(s) = v.parse::<f64>() {
        return Duration::try_from_secs_f64(s).ok();
    }
    let at = httpdate::parse_http_date(v).ok()?;
    Some(at.duration_since(SystemTime::now()).unwrap_or(Duration::ZERO))
}

impl Client {
    /// The options of a client.
    pub fn builder() -> ClientBuilder {
        ClientBuilder::default()
    }

    /// A client from the environment (PHOENIXPERPS_API_KEY, PHOENIXPERPS_ENVIRONMENT, PHOENIXPERPS_BASE_URL).
    pub fn from_env() -> Result<Client> {
        ClientBuilder::default().build()
    }

    /// live or sandbox.
    pub fn environment(&self) -> &str {
        &self.inner.environment
    }

    /// The API host the calls go to.
    pub fn base_url(&self) -> &str {
        &self.inner.base_url
    }

    /// The key with its secret hidden (ppk_test_****abcd); empty without a key.
    pub fn masked_key(&self) -> String {
        self.inner.key.as_deref().map(mask).unwrap_or_default()
    }

    /// What the last answer said of the rate-limit window (None before the first answer).
    pub fn rate_limit(&self) -> Option<RateLimit> {
        self.inner.rate.lock().unwrap().clone()
    }

    /// What every request says of the SDK.
    pub fn user_agent(&self) -> String {
        let mut ua = format!("phoenixperps-rust/{} (rust; {}) spec/{}", crate::SDK_VERSION, std::env::consts::OS, meta::SPEC_VERSION);
        if let Some(a) = &self.inner.app_info {
            ua.push(' ');
            ua.push_str(a);
        }
        ua
    }

    pub(crate) fn key(&self) -> Option<&str> {
        self.inner.key.as_deref()
    }

    pub(crate) fn log(&self, level: &str, message: &str) {
        if let Some(l) = &self.inner.logger {
            l(level, message);
        }
    }

    /// With nothing left in the budget's window and its reset under 60 s away, waits for the reset.
    async fn pre_wait(&self, scope: &'static str) {
        let r = self.inner.rates.lock().unwrap().get(scope).cloned();
        let Some(r) = r else { return };
        if r.remaining > 0 {
            return;
        }
        if let Some(wait) = r.reset_at.and_then(|at| at.duration_since(SystemTime::now()).ok()) {
            if wait < Duration::from_secs(60) {
                tokio::time::sleep(wait).await;
            }
        }
    }

    /// Keeps the window an answer gives, as the last one and as its budget's.
    fn remember(&self, scope: &'static str, h: &HeaderMap) {
        let get = |n: &str| h.get(n).and_then(|v| v.to_str().ok()).map(str::to_string);
        let Some(limit) = get("x-ratelimit-limit") else { return };
        let reset_at = get("x-ratelimit-reset").and_then(|v| v.parse::<f64>().ok()).map(|reset| {
            // Reset as epoch seconds (above a year in seconds) or as seconds from now
            if reset > 31_536_000.0 {
                UNIX_EPOCH + Duration::from_secs_f64(reset)
            } else {
                SystemTime::now() + Duration::from_secs_f64(reset.max(0.0))
            }
        });
        let r = RateLimit { limit: limit.parse().unwrap_or(0), remaining: get("x-ratelimit-remaining").and_then(|v| v.parse().ok()).unwrap_or(0), reset_at };
        self.inner.rates.lock().unwrap().insert(scope, r.clone());
        *self.inner.rate.lock().unwrap() = Some(r);
        if let Some(dep) = get("deprecation") {
            if self.inner.warned.lock().unwrap().insert(dep.clone()) {
                self.log("warn", &format!("phoenixperps: this API version is deprecated (Deprecation: {dep})"));
            }
        }
    }

    fn url(&self, op: &Operation, path: &[&str], query: &Option<Vec<(String, String)>>) -> Result<reqwest::Url> {
        let mut out = String::with_capacity(self.inner.base_url.len() + op.path.len());
        out.push_str(&self.inner.base_url);
        let mut values = path.iter();
        let mut rest = op.path;
        while let Some(i) = rest.find('{') {
            out.push_str(&rest[..i]);
            let end = rest[i..].find('}').map(|j| i + j).unwrap_or(rest.len() - 1);
            let v = values.next().copied().unwrap_or("");
            if v.is_empty() {
                return Err(Error::new(ErrorKind::Validation, "invalid_request", format!("{} is required.", &rest[i + 1..end])));
            }
            for b in v.bytes() {
                if b.is_ascii_alphanumeric() || b"-._~".contains(&b) {
                    out.push(b as char);
                } else {
                    out.push_str(&format!("%{b:02X}"));
                }
            }
            rest = &rest[end + 1..];
        }
        out.push_str(rest);
        let mut url = reqwest::Url::parse(&out).map_err(|e| Error::new(ErrorKind::Validation, "invalid_request", e.to_string()))?;
        if let Some(q) = query {
            if !q.is_empty() {
                url.query_pairs_mut().extend_pairs(q.iter());
            }
        }
        Ok(url)
    }

    /// Any call, a route this version does not know yet among them (v1 only grows), with the client's key, retries
    /// and errors: method (GET, POST, PUT, PATCH or DELETE), path (/v1/...), its query and its JSON body. It is tried
    /// again when that is safe: a GET, a route the spec marks retry-safe, or a call with an Idempotency-Key
    /// (`options.idempotency_key`). A 2xx or a 304 (Not Modified) comes as it came; any other is an Error.
    pub async fn request(&self, method: &str, path: &str, query: &[(&str, &str)], body: Option<&Value>, options: &RequestOptions) -> Result<RawResponse> {
        let invalid = |m: String| Error::new(ErrorKind::Validation, "invalid_request", m);
        let Some(method) = ["GET", "POST", "PUT", "PATCH", "DELETE"].into_iter().find(|m| m.eq_ignore_ascii_case(method)) else {
            return Err(invalid("The method is GET, POST, PUT, PATCH or DELETE.".into()));
        };
        if !path.starts_with('/') {
            return Err(invalid("The path starts with /.".into()));
        }
        let mut url = reqwest::Url::parse(&format!("{}{path}", self.inner.base_url)).map_err(|e| invalid(e.to_string()))?;
        if !query.is_empty() {
            url.query_pairs_mut().extend_pairs(query);
        }
        let known = route(method, path);
        let op = Operation { id: "request", method, path: "", idempotent: false, retry_safe: method == "GET" || known.is_some_and(|k| k.retry_safe), cursor: None, scope: known.map_or("", |k| k.scope) };
        self.attempts(&op, url, body.map(|b| b.to_string().into_bytes()), options).await
    }

    /// One logical call: its attempts, and its answer.
    pub(crate) async fn send(&self, op: &Operation, path: &[&str], query: Option<Vec<(String, String)>>, body: Option<Vec<u8>>, o: &RequestOptions) -> Result<RawResponse> {
        let url = self.url(op, path, &query)?;
        self.attempts(op, url, body, o).await
    }

    /// The attempts of one call to url, and its answer.
    async fn attempts(&self, op: &Operation, url: reqwest::Url, body: Option<Vec<u8>>, o: &RequestOptions) -> Result<RawResponse> {
        let key = o.idempotency_key.clone().or_else(|| op.idempotent.then(|| uuid::Uuid::new_v4().to_string()));
        // safe to send again: a read, or a call with an Idempotency-Key (its own, or one given)
        let retryable = op.retry_safe || key.is_some();
        let max = o.max_retries.unwrap_or(self.inner.max_retries);
        let timeout = o.timeout.unwrap_or(self.inner.timeout);
        let method = reqwest::Method::from_bytes(op.method.as_bytes()).expect("an HTTP method");
        let mut attempt = 0u32;
        loop {
            self.pre_wait(op.scope).await;
            let mut req = self
                .inner
                .http
                .request(method.clone(), url.clone())
                .timeout(timeout)
                .header("accept", "application/json")
                .header("user-agent", self.user_agent());
            // no key: no Authorization (a public call)
            if let Some(k) = &self.inner.key {
                req = req.header("authorization", format!("Bearer {k}"));
            }
            if let Some(k) = &key {
                req = req.header("idempotency-key", k);
            }
            for (k, v) in &o.headers {
                if let (Ok(n), Ok(v)) = (HeaderName::from_bytes(k.as_bytes()), HeaderValue::from_str(v)) {
                    req = req.header(n, v);
                }
            }
            if let Some(b) = &body {
                req = req.header("content-type", "application/json").body(b.clone());
            }
            let failure = match req.send().await {
                Ok(res) => {
                    let status = res.status().as_u16();
                    let headers = res.headers().clone();
                    match res.bytes().await {
                        Ok(bytes) => {
                            self.remember(op.scope, &headers);
                            let raw = RawResponse { status, request_id: headers.get("x-request-id").and_then(|v| v.to_str().ok()).map(str::to_string), headers, body: bytes.to_vec(), idempotency_key: key.clone() };
                            if let Some(hook) = &o.on_response {
                                hook(&raw);
                            }
                            // a 304: nothing changed since the If-None-Match sent, no error
                            if (200..300).contains(&status) || status == 304 {
                                return Ok(raw);
                            }
                            let err = Error::from_answer(status, &raw.headers, &raw.body, key.clone());
                            // asked again only when the API says it may work (its flag, else the status)
                            let again = retryable && attempt < max && err.retryable == Some(true);
                            if !again || err.retry_after.is_some_and(|d| d > RETRY_AFTER_MAX) {
                                return Err(err);
                            }
                            let wait = err.retry_after.unwrap_or_else(|| jitter(attempt));
                            self.log("debug", &format!("phoenixperps: retrying {} after {status} in {} ms (attempt {})", op.id, wait.as_millis(), attempt + 1));
                            tokio::time::sleep(wait).await;
                            attempt += 1;
                            continue;
                        }
                        Err(e) => e,
                    }
                }
                Err(e) => e,
            };
            let mut err = if failure.is_timeout() {
                Error::new(ErrorKind::Timeout, "timeout", format!("No answer within {} ms.", timeout.as_millis()))
            } else {
                let text = failure.to_string();
                let text = match &self.inner.key {
                    Some(k) => text.replace(k.as_str(), &self.masked_key()),
                    None => text,
                };
                Error::new(ErrorKind::Connection, "connection_failed", clip(&text))
            };
            err.idempotency_key = key.clone();
            err.retryable = Some(true);
            if !(retryable && attempt < max) {
                return Err(err);
            }
            let wait = jitter(attempt);
            self.log("debug", &format!("phoenixperps: retrying {} after {} in {} ms (attempt {})", op.id, err.code, wait.as_millis(), attempt + 1));
            tokio::time::sleep(wait).await;
            attempt += 1;
        }
    }

    /// A call whose answer is one object ({"data": ...}).
    pub(crate) async fn one<T: DeserializeOwned, B: Serialize + ?Sized>(&self, op: &Operation, path: &[&str], query: Option<Vec<(String, String)>>, body: Option<&B>, o: &RequestOptions) -> Result<T> {
        let payload = match body {
            Some(b) => Some(serde_json::to_vec(b).map_err(|e| Error::new(ErrorKind::Validation, "invalid_request", e.to_string()))?),
            None => None,
        };
        let raw = self.send(op, path, query, payload, o).await?;
        let mut v = json(&raw)?;
        if let Some(d) = v.as_object_mut().and_then(|m| m.remove("data")) {
            v = d;
        }
        serde_json::from_value(v).map_err(|e| unreadable(&raw, e))
    }

    /// A call whose answer is a page of a list (a 304 is an error here: http_304).
    pub(crate) async fn list<T: DeserializeOwned>(&self, op: &'static Operation, path: &[&str], query: Option<Vec<(String, String)>>, o: &RequestOptions) -> Result<Page<T>> {
        self.page_of(op, path, query, o).await?.map_err(|raw| Error::from_answer(raw.status, &raw.headers, &raw.body, raw.idempotency_key))
    }

    /// A call whose answer is a page of a list, or None on a 304 Not Modified (the If-None-Match given is current).
    pub(crate) async fn list_or_none<T: DeserializeOwned>(&self, op: &'static Operation, path: &[&str], query: Option<Vec<(String, String)>>, o: &RequestOptions) -> Result<Option<Page<T>>> {
        Ok(self.page_of(op, path, query, o).await?.ok())
    }

    /// The page of a list, or the 304 Not Modified that came instead.
    async fn page_of<T: DeserializeOwned>(&self, op: &'static Operation, path: &[&str], query: Option<Vec<(String, String)>>, o: &RequestOptions) -> Result<std::result::Result<Page<T>, Box<RawResponse>>> {
        let raw = self.send(op, path, query.clone(), None, o).await?;
        if raw.status == 304 {
            return Ok(Err(Box::new(raw)));
        }
        let mut page: Page<T> = serde_json::from_value(json(&raw)?).map_err(|e| unreadable(&raw, e))?;
        page.raw = Some(raw);
        if op.cursor.is_some() {
            page.next = Some(NextCall { client: self.clone(), op, path: path.iter().map(|s| s.to_string()).collect(), query: query.unwrap_or_default(), options: o.clone() });
        }
        Ok(Ok(page))
    }
}

/// The answer's JSON (null for a 204 or a 304, which have no body). A 2xx whose body is not JSON (a proxy's page,
/// nothing at all) is an error, as any other answer that is not the API's (http_<status>).
fn json(raw: &RawResponse) -> Result<Value> {
    if raw.body.is_empty() && (raw.status == 204 || raw.status == 304) {
        return Ok(Value::Null);
    }
    serde_json::from_slice(&raw.body).map_err(|_| {
        let mut e = Error::from_answer(raw.status, &raw.headers, &raw.body, raw.idempotency_key.clone());
        e.kind = ErrorKind::Server;
        e
    })
}

/// JSON that is not the type the call answers.
fn unreadable(raw: &RawResponse, e: serde_json::Error) -> Error {
    // a 304 to a call whose answer cannot be nothing (only Option<_> can): http_304
    if raw.status == 304 {
        return Error::from_answer(raw.status, &raw.headers, &raw.body, raw.idempotency_key.clone());
    }
    let mut x = Error::new(ErrorKind::Server, "invalid_response", format!("The answer could not be read: {e}"));
    x.status = Some(raw.status);
    x.request_id = raw.request_id.clone();
    x.idempotency_key = raw.idempotency_key.clone();
    x.headers = raw.headers.clone();
    x
}

enum Paging<F, T> {
    Start(F),
    At(Box<Page<T>>, usize),
    Done,
}

/// Every item of a list, page after page; an error ends it (yielded once).
pub(crate) fn auto_paging<'s, T, F>(first: F, o: &RequestOptions) -> impl Stream<Item = Result<T>> + 's
where
    T: DeserializeOwned + Clone + 's,
    F: Future<Output = Result<Page<T>>> + 's,
{
    let max = o.max_items.filter(|m| *m > 0);
    let items = stream::unfold((Paging::Start(Box::pin(first)), 0usize), move |(state, n)| async move {
        if max.is_some_and(|m| n >= m) {
            return None;
        }
        let mut state = state;
        loop {
            match state {
                Paging::Start(f) => match f.await {
                    Ok(p) => state = Paging::At(Box::new(p), 0),
                    Err(e) => return Some((Err(e), (Paging::Done, n))),
                },
                Paging::At(p, i) if i < p.data.len() => {
                    let item = p.data[i].clone();
                    return Some((Ok(item), (Paging::At(p, i + 1), n + 1)));
                }
                Paging::At(p, _) => match p.next_page().await {
                    Ok(Some(next)) => state = Paging::At(Box::new(next), 0),
                    Ok(None) => return None,
                    Err(e) => return Some((Err(e), (Paging::Done, n))),
                },
                Paging::Done => return None,
            }
        }
    });
    Box::pin(items)
}
