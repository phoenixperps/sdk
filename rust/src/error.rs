use reqwest::header::HeaderMap;
use serde_json::Value;
use std::fmt;
use std::time::Duration;

/// An error's family (docs/BEHAVIOUR.md "Answers"), the same classes as every other Phoenix Perpetuals SDK.
#[derive(Clone, Copy, Debug, PartialEq, Eq, Hash)]
pub enum ErrorKind {
    /// 401: the key is missing, wrong, revoked, expired or of the other environment.
    Authentication,
    /// 403: the key may not do this.
    Permission,
    /// 404.
    NotFound,
    /// 409.
    Conflict,
    /// Another 4xx: the request is not right.
    Validation,
    /// 422: the account's rules or the key's controls said no.
    RuleRejected,
    /// 429.
    RateLimit,
    /// 5xx.
    Server,
    /// No answer: the network failed.
    Connection,
    /// No answer in time.
    Timeout,
    /// A stream ended for good.
    StreamClosed,
    /// A webhook delivery did not verify: missing_headers, stale, bad_signature or bad_body (`verify_webhook`).
    WebhookVerification,
}

impl ErrorKind {
    /// The family's name in every SDK (AuthenticationError, RateLimitError...).
    pub fn class(&self) -> &'static str {
        match self {
            Self::Authentication => "AuthenticationError",
            Self::Permission => "PermissionError",
            Self::NotFound => "NotFoundError",
            Self::Conflict => "ConflictError",
            Self::Validation => "ValidationError",
            Self::RuleRejected => "RuleRejectedError",
            Self::RateLimit => "RateLimitError",
            Self::Server => "ServerError",
            Self::Connection => "ConnectionError",
            Self::Timeout => "TimeoutError",
            Self::StreamClosed => "StreamClosedError",
            Self::WebhookVerification => "WebhookVerificationError",
        }
    }

    pub(crate) fn for_status(status: u16) -> Self {
        match status {
            401 => Self::Authentication,
            403 => Self::Permission,
            404 => Self::NotFound,
            409 => Self::Conflict,
            422 => Self::RuleRejected,
            429 => Self::RateLimit,
            s if s >= 500 => Self::Server,
            _ => Self::Validation,
        }
    }
}

/// Every failure of a call: the API's code and message, the HTTP status, the request id, the details, whether it may
/// work later and the Retry-After, the order it is about, the call's Idempotency-Key and the answer's headers. Its
/// fields are read through it (`e.code`, `e.kind`).
#[derive(Clone, Debug)]
pub struct Error(Box<ErrorData>);

impl std::ops::Deref for Error {
    type Target = ErrorData;

    fn deref(&self) -> &ErrorData {
        &self.0
    }
}

impl std::ops::DerefMut for Error {
    fn deref_mut(&mut self) -> &mut ErrorData {
        &mut self.0
    }
}

/// What an Error says.
#[derive(Clone, Debug)]
pub struct ErrorData {
    /// The family.
    pub kind: ErrorKind,
    /// The HTTP status, when there was an answer.
    pub status: Option<u16>,
    /// The API's error code (`codes`), or http_<status> for an answer that is not the API's.
    pub code: String,
    /// What happened.
    pub message: String,
    /// The error type of the API's envelope.
    pub error_type: Option<String>,
    /// The request id, to quote to support.
    pub request_id: Option<String>,
    /// The details the API gave, as they came.
    pub details: Option<Value>,
    /// Whether the same call may work if sent again later (try again only when it is true): the API's flag; without its
    /// envelope, by the status (429, 500, 502, 503, 504); true for a timeout or a network failure. None without an
    /// answer of the API (an error of this SDK's own).
    pub retryable: Option<bool>,
    /// The docs of the code, when the API gave them.
    pub doc_url: Option<String>,
    /// When to try again, when the API said.
    pub retry_after: Option<Duration>,
    /// The order this is about (a refused order keeps its id).
    pub order_id: Option<String>,
    /// The call's Idempotency-Key: send the call again with it to be sure it happens once.
    pub idempotency_key: Option<String>,
    /// The answer's headers (empty without an answer).
    pub headers: HeaderMap,
}

impl Error {
    pub(crate) fn new(kind: ErrorKind, code: &str, message: impl Into<String>) -> Self {
        Self(Box::new(ErrorData {
            kind,
            status: None,
            code: code.to_string(),
            message: message.into(),
            error_type: None,
            request_id: None,
            details: None,
            retryable: None,
            doc_url: None,
            retry_after: None,
            order_id: None,
            idempotency_key: None,
            headers: HeaderMap::new(),
        }))
    }

    /// Any answer as an error: the API's envelope, a bare {"error": "text"}, or HTML or text.
    pub(crate) fn from_answer(status: u16, headers: &HeaderMap, body: &[u8], idempotency_key: Option<String>) -> Self {
        let mut e = Error::new(ErrorKind::for_status(status), &format!("http_{status}"), "");
        e.status = Some(status);
        e.request_id = headers.get("x-request-id").and_then(|v| v.to_str().ok()).map(str::to_string);
        e.retry_after = crate::client::retry_after(headers);
        e.idempotency_key = idempotency_key;
        e.headers = headers.clone();
        // the API's own flag, else the statuses that may work later
        e.retryable = Some(matches!(status, 429 | 500 | 502 | 503 | 504));
        let text = String::from_utf8_lossy(body);
        match serde_json::from_slice::<Value>(body).ok().and_then(|v| v.get("error").cloned()) {
            Some(Value::Object(o)) => {
                let s = |k: &str| o.get(k).and_then(Value::as_str).map(str::to_string);
                if let Some(c) = s("code") {
                    e.code = c;
                }
                e.message = s("message").unwrap_or_default();
                e.error_type = s("type");
                e.order_id = s("order_id");
                if let Some(r) = s("request_id") {
                    e.request_id = Some(r);
                }
                e.details = o.get("details").filter(|d| !d.is_null()).cloned();
                if let Some(r) = o.get("retryable").and_then(Value::as_bool) {
                    e.retryable = Some(r);
                }
                e.doc_url = s("doc_url");
            }
            Some(Value::String(m)) => e.message = clip(&m),
            _ => e.message = clip(&text),
        }
        if e.message.is_empty() {
            e.message = e.code.clone();
        }
        e
    }
}

pub(crate) fn clip(s: &str) -> String {
    s.chars().take(200).collect()
}

impl fmt::Display for Error {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "phoenixperps: {} ({}", self.message, self.code)?;
        if let Some(s) = self.status {
            write!(f, ", {s}")?;
        }
        if let Some(r) = &self.request_id {
            write!(f, ", {r}")?;
        }
        write!(f, ")")
    }
}

impl std::error::Error for Error {}

/// The result of a call.
pub type Result<T, E = Error> = std::result::Result<T, E>;
