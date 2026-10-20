package phoenixperps

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"strings"
	"time"
)

// Kind is an error's family (docs/BEHAVIOUR.md "Answers"), the same classes as every other Phoenix Perpetuals SDK.
type Kind string

const (
	KindAuthentication Kind = "AuthenticationError" // 401: the key is missing, wrong, revoked, expired or of the other environment
	KindPermission     Kind = "PermissionError"     // 403: the key may not do this
	KindNotFound       Kind = "NotFoundError"       // 404
	KindConflict       Kind = "ConflictError"       // 409
	KindValidation     Kind = "ValidationError"     // other 4xx: the request is not right
	KindRuleRejected   Kind = "RuleRejectedError"   // 422: the account's rules or the key's controls said no
	KindRateLimit      Kind = "RateLimitError"      // 429
	KindServer         Kind = "ServerError"         // 5xx
	KindConnection     Kind = "ConnectionError"     // no answer: the network failed
	KindTimeout        Kind = "TimeoutError"        // no answer in time
	KindStreamClosed   Kind = "StreamClosedError"   // a stream ended for good
	// a webhook delivery that did not verify: missing_headers, stale, bad_signature or bad_body
	KindWebhookVerification Kind = "WebhookVerificationError"
)

// Error is every failure of a call: the API's code and message, the HTTP status, the request id, the details, the
// Retry-After, whether it may be tried again (the API's own retryable; without its envelope, by the status; true for
// a timeout or a network failure), its doc page, the order it is about, the call's Idempotency-Key and the answer's
// headers. errors.Is(err, ErrRateLimit) tells the family.
type Error struct {
	Kind           Kind
	Status         int
	Code           string
	Message        string
	Type           string
	RequestID      string
	Details        json.RawMessage
	RetryAfter     time.Duration
	Retryable      bool
	DocURL         string
	OrderID        string
	IdempotencyKey string
	Header         http.Header
	Err            error
}

// WebhookError is the error VerifyWebhook answers (KindWebhookVerification, its Code one of missing_headers, stale,
// bad_signature, bad_body).
//
// Deprecated: it is Error; use errors.Is(err, ErrWebhookVerification).
type WebhookError = Error

func (e *Error) Error() string {
	s := fmt.Sprintf("phoenixperps: %s (%s", e.Message, e.Code)
	if e.Status != 0 {
		s += fmt.Sprintf(", %d", e.Status)
	}
	if e.RequestID != "" {
		s += ", " + e.RequestID
	}
	return s + ")"
}

func (e *Error) Unwrap() error { return e.Err }

// Is matches a sentinel of the same family (ErrRateLimit...) or an Error with the same Kind and Code.
func (e *Error) Is(target error) bool {
	t, ok := target.(*Error)
	if !ok {
		return false
	}
	return t.Kind == e.Kind && (t.Code == "" || t.Code == e.Code)
}

// The families, for errors.Is.
var (
	ErrAuthentication = &Error{Kind: KindAuthentication}
	ErrPermission     = &Error{Kind: KindPermission}
	ErrNotFound       = &Error{Kind: KindNotFound}
	ErrConflict       = &Error{Kind: KindConflict}
	ErrValidation     = &Error{Kind: KindValidation}
	ErrRuleRejected   = &Error{Kind: KindRuleRejected}
	ErrRateLimit      = &Error{Kind: KindRateLimit}
	ErrServer         = &Error{Kind: KindServer}
	ErrConnection     = &Error{Kind: KindConnection}
	ErrTimeout        = &Error{Kind: KindTimeout}
	ErrStreamClosed   = &Error{Kind: KindStreamClosed}

	ErrWebhookVerification = &Error{Kind: KindWebhookVerification}
)

func kindFor(status int) Kind {
	switch {
	case status == 401:
		return KindAuthentication
	case status == 403:
		return KindPermission
	case status == 404:
		return KindNotFound
	case status == 409:
		return KindConflict
	case status == 422:
		return KindRuleRejected
	case status == 429:
		return KindRateLimit
	case status >= 500:
		return KindServer
	default:
		return KindValidation
	}
}

// retryAfter reads Retry-After (seconds, fractions included, or an HTTP date); false when absent or unreadable.
func retryAfter(h http.Header, now time.Time) (time.Duration, bool) {
	v := strings.TrimSpace(h.Get("Retry-After"))
	if v == "" {
		return 0, false
	}
	if n, err := strconv.ParseFloat(v, 64); err == nil && n >= 0 && n < 1e9 {
		return time.Duration(n * float64(time.Second)), true
	}
	if t, err := http.ParseTime(v); err == nil {
		if d := t.Sub(now); d > 0 {
			return d, true
		}
		return 0, true
	}
	return 0, false
}

// errorFrom turns any answer into an Error: the API's envelope, a bare {"error": "text"}, or HTML or text.
func errorFrom(status int, h http.Header, body []byte, idempotencyKey string) *Error {
	e := &Error{Kind: kindFor(status), Status: status, Code: fmt.Sprintf("http_%d", status), RequestID: h.Get("X-Request-Id"), IdempotencyKey: idempotencyKey, Header: h, Retryable: retryStatus[status]}
	if d, ok := retryAfter(h, time.Now()); ok {
		e.RetryAfter = d
	}
	var env struct {
		Error json.RawMessage `json:"error"`
	}
	if json.Unmarshal(body, &env) == nil && len(env.Error) > 0 {
		var api struct {
			Type      string          `json:"type"`
			Code      string          `json:"code"`
			Message   string          `json:"message"`
			RequestID string          `json:"request_id"`
			Details   json.RawMessage `json:"details"`
			OrderID   string          `json:"order_id"`
			Retryable *bool           `json:"retryable"`
			DocURL    string          `json:"doc_url"`
		}
		var text string
		switch {
		case json.Unmarshal(env.Error, &api) == nil:
			if api.Code != "" {
				e.Code = api.Code
			}
			if api.Retryable != nil { // the API's own (409 operation_in_progress is, 503 sandbox_disabled is not)
				e.Retryable = *api.Retryable
			}
			e.Message, e.Type, e.OrderID, e.DocURL = api.Message, api.Type, api.OrderID, api.DocURL
			if api.RequestID != "" {
				e.RequestID = api.RequestID
			}
			if len(api.Details) > 0 && string(api.Details) != "null" {
				e.Details = api.Details
			}
		case json.Unmarshal(env.Error, &text) == nil:
			e.Message = clip(text)
		}
	} else {
		e.Message = clip(string(body))
	}
	if e.Message == "" {
		e.Message = e.Code
	}
	return e
}

func clip(s string) string {
	if len(s) > 200 {
		return s[:200]
	}
	return s
}
