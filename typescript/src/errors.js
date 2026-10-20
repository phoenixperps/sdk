'use strict';

/*
 * Errors (docs/BEHAVIOUR.md "Answers"): one base class with the API's code, status, message, request id, details, the
 * Retry-After, retryable (the API's flag; without its envelope, by the status) and doc_url; a subclass per family. Any
 * body becomes an error: the API's envelope, a bare { error: 'text' }, or HTML or plain text (code http_<status>, at
 * most 200 characters of the body).
 */
class PhoenixPerpsError extends Error {
  constructor({ status = null, code = 'error', message = code, requestId = null, details = null, retryAfter = null, retryable = false, docUrl = null, idempotencyKey = null, orderId = null, accountId = null, type = null, headers = null } = {}) {
    super(message);
    this.name = this.constructor.name;
    Object.assign(this, { status, code, requestId, details, retryAfter, retryable, docUrl, idempotencyKey, orderId, accountId, type });
    // the answer's headers (Idempotent-Replayed, ...), null when none came; not enumerable, as a result's meta
    Object.defineProperty(this, 'headers', { value: headers, enumerable: false });
  }
}
class AuthenticationError extends PhoenixPerpsError {}
class PermissionError extends PhoenixPerpsError {}
class NotFoundError extends PhoenixPerpsError {}
class ConflictError extends PhoenixPerpsError {}
class ValidationError extends PhoenixPerpsError {}
class RuleRejectedError extends PhoenixPerpsError {}
class RateLimitError extends PhoenixPerpsError {}
class ServerError extends PhoenixPerpsError {}
class ConnectionError extends PhoenixPerpsError {}
class TimeoutError extends PhoenixPerpsError {}
class StreamClosedError extends PhoenixPerpsError {}

// an answer without the API's envelope may be tried again on these
const RETRY_STATUS = new Set([429, 500, 502, 503, 504]);

function classFor(status) {
  if (status === 401) return AuthenticationError;
  if (status === 403) return PermissionError;
  if (status === 404) return NotFoundError;
  if (status === 409) return ConflictError;
  if (status === 422) return RuleRejectedError;
  if (status === 429) return RateLimitError;
  if (status >= 500) return ServerError;
  if (status >= 400) return ValidationError;
  return PhoenixPerpsError;
}

/** The seconds (fractions too) or HTTP date of a Retry-After header, in ms; null when absent or unreadable. */
function retryAfterMs(headers, now = Date.now()) {
  const v = headers && headers.get ? headers.get('retry-after') : null;
  if (v === null || v === undefined || v === '') return null;
  if (/^\d+(\.\d+)?$/.test(v)) return Math.round(Number(v) * 1000);
  const t = Date.parse(v);
  return Number.isFinite(t) ? Math.max(0, t - now) : null;
}

/** The error a response is (its body read once). */
async function fromResponse(res, idempotencyKey) {
  return fromText(res, await res.text().catch(() => ''), idempotencyKey);
}

/** The error an answer is, from its body as read. */
function fromText(res, text, idempotencyKey) {
  let body = null;
  try {
    body = text ? JSON.parse(text) : null;
  } catch {
    body = null;
  }
  const requestId = res.headers.get('x-request-id');
  const retryAfter = retryAfterMs(res.headers);
  const retryable = RETRY_STATUS.has(res.status);
  const { headers } = res;
  const Cls = classFor(res.status);
  if (body && body.error && typeof body.error === 'object') {
    const e = body.error;
    return new Cls({ status: res.status, code: e.code || `http_${res.status}`, message: e.message || e.code, requestId: e.request_id || requestId, details: e.details ?? null, retryAfter, retryable: typeof e.retryable === 'boolean' ? e.retryable : retryable, docUrl: e.doc_url || null, idempotencyKey, orderId: e.order_id || null, type: e.type || null, headers });
  }
  if (body && typeof body.error === 'string') return new Cls({ status: res.status, code: `http_${res.status}`, message: body.error.slice(0, 200), requestId, retryAfter, retryable, idempotencyKey, headers });
  return new Cls({ status: res.status, code: `http_${res.status}`, message: text.slice(0, 200) || `HTTP ${res.status}`, requestId, retryAfter, retryable, idempotencyKey, headers });
}

module.exports = {
  PhoenixPerpsError, AuthenticationError, PermissionError, NotFoundError, ConflictError, ValidationError, RuleRejectedError,
  RateLimitError, ServerError, ConnectionError, TimeoutError, StreamClosedError, classFor, fromResponse, fromText, retryAfterMs,
};
