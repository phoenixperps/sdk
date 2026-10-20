// Package phoenixperps is the Phoenix Perpetuals API for Go: every REST call, the account stream and market data, with
// retries, idempotency keys, paging and typed errors. Docs: https://docs.phoenixperpsfunding.com
//
//	client, err := phoenixperps.NewClient()                // PHOENIXPERPS_API_KEY from the environment
//	order, err := client.Orders.Create(ctx, accountID, phoenixperps.CreateOrder{
//		Market: "BTC-USD", Side: phoenixperps.OrderSideBuy, Type: phoenixperps.CreateOrderTypeMarket,
//		Size: phoenixperps.Dec("0.01"),
//	})
package phoenixperps

import (
	"bytes"
	"context"
	"crypto/rand"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"log/slog"
	"math"
	mrand "math/rand/v2"
	"net/http"
	"net/url"
	"os"
	"regexp"
	"runtime"
	"strconv"
	"strings"
	"sync"
	"time"
)

var keyPrefix = regexp.MustCompile(`^(ppk|ppo)_(live|test)_`)

const retryAfterMax = 60 * time.Second

// Client is the API. Make one with NewClient and share it: it is safe for concurrent use.
type Client struct {
	resources
	key         string
	environment string
	baseURL     string
	timeout     time.Duration
	maxRetries  int
	appInfo     string
	http        *http.Client
	logger      *slog.Logger
	streams     map[string]string

	mu         sync.Mutex
	rateLimit  *RateLimit
	rateLimits map[string]*RateLimit // per budget (an operation's x-pp-scope: read, trade, public)
	warned     map[string]bool
}

// RateLimit is what the last answer said of the rate-limit window.
type RateLimit struct {
	Limit     int
	Remaining int
	ResetAt   time.Time
}

type clientConfig struct {
	key, environment, baseURL, appInfo string
	timeout                            time.Duration
	maxRetries                         int
	http                               *http.Client
	logger                             *slog.Logger
	accountStreamURL, marketStreamURL  string
}

// ClientOption configures NewClient.
type ClientOption func(*clientConfig)

// WithAPIKey sets the key (else PHOENIXPERPS_API_KEY). Without one the public calls (Markets, Platform) work, and any
// other answers the API's missing_api_key.
func WithAPIKey(key string) ClientOption { return func(c *clientConfig) { c.key = key } }

// WithEnvironment sets live or sandbox, in any case (else the key's own, else PHOENIXPERPS_ENVIRONMENT).
func WithEnvironment(env string) ClientOption { return func(c *clientConfig) { c.environment = env } }

// WithBaseURL sets the API host (else PHOENIXPERPS_BASE_URL, else the environment's); a trailing /v1 is dropped.
func WithBaseURL(u string) ClientOption { return func(c *clientConfig) { c.baseURL = u } }

// WithTimeout sets the time one attempt may take (30 s).
func WithTimeout(d time.Duration) ClientOption { return func(c *clientConfig) { c.timeout = d } }

// WithMaxRetries sets how often a safe call is tried again (2).
func WithMaxRetries(n int) ClientOption { return func(c *clientConfig) { c.maxRetries = n } }

// WithAppInfo adds your app's name and version to the User-Agent.
func WithAppInfo(s string) ClientOption { return func(c *clientConfig) { c.appInfo = s } }

// WithHTTPClient sets the http.Client the calls go through.
func WithHTTPClient(h *http.Client) ClientOption { return func(c *clientConfig) { c.http = h } }

// WithLogger receives a debug line per retry and one warning per deprecated API version.
func WithLogger(l *slog.Logger) ClientOption { return func(c *clientConfig) { c.logger = l } }

// WithStreamURLs overrides the account and market stream URLs (an empty string keeps the default).
func WithStreamURLs(account, market string) ClientOption {
	return func(c *clientConfig) { c.accountStreamURL, c.marketStreamURL = account, market }
}

// NewClient makes a client. A key of the other environment fails here, before any request.
func NewClient(opts ...ClientOption) (*Client, error) {
	cfg := clientConfig{timeout: 30 * time.Second, maxRetries: 2}
	for _, o := range opts {
		o(&cfg)
	}
	if cfg.key == "" {
		cfg.key = os.Getenv("PHOENIXPERPS_API_KEY")
	}
	inferred := ""
	if m := keyPrefix.FindStringSubmatch(cfg.key); m != nil {
		inferred = map[string]string{"live": "live", "test": "sandbox"}[m[2]]
	}
	env := cfg.environment
	if env == "" {
		env = inferred
	}
	if env == "" {
		env = os.Getenv("PHOENIXPERPS_ENVIRONMENT")
	}
	if env == "" {
		env = "sandbox"
	}
	if env = strings.ToLower(env); env != "live" && env != "sandbox" {
		return nil, &Error{Kind: KindValidation, Code: "invalid_environment", Message: "The environment is live or sandbox."}
	}
	if inferred != "" && inferred != env {
		return nil, &Error{Kind: KindAuthentication, Code: "wrong_environment", Message: fmt.Sprintf("This key is a %s key, not a %s one.", inferred, env), Details: json.RawMessage(fmt.Sprintf(`{"key_environment":%q}`, inferred))}
	}
	base := cfg.baseURL
	if base == "" {
		base = os.Getenv("PHOENIXPERPS_BASE_URL")
	}
	if base == "" {
		base = servers[env]
	}
	// the host; the REST address as the docs write it (https://api.phoenixperpsfunding.com/v1) means the same
	base = strings.TrimSuffix(strings.TrimRight(base, "/"), "/v1")
	h := cfg.http
	if h == nil {
		h = &http.Client{}
	}
	c := &Client{
		key: cfg.key, environment: env, baseURL: base, timeout: cfg.timeout, maxRetries: cfg.maxRetries,
		appInfo: cfg.appInfo, http: h, logger: cfg.logger, warned: map[string]bool{}, rateLimits: map[string]*RateLimit{},
		streams: map[string]string{"account": firstOf(cfg.accountStreamURL, accountStreamURLs[env]), "market": firstOf(cfg.marketStreamURL, marketStreamURL)},
	}
	c.initResources()
	return c, nil
}

func firstOf(a, b string) string {
	if a != "" {
		return a
	}
	return b
}

// Environment is live or sandbox.
func (c *Client) Environment() string { return c.environment }

// BaseURL is the API host the calls go to.
func (c *Client) BaseURL() string { return c.baseURL }

// MaskedKey is the key with its secret hidden (ppk_test_****abcd).
func (c *Client) MaskedKey() string { return mask(c.key) }

// RateLimit is what the last answer said of the rate-limit window (nil before the first answer).
func (c *Client) RateLimit() *RateLimit {
	c.mu.Lock()
	defer c.mu.Unlock()
	if c.rateLimit == nil {
		return nil
	}
	r := *c.rateLimit
	return &r
}

// String never shows the key.
func (c *Client) String() string {
	return fmt.Sprintf("phoenixperps.Client{environment: %s, baseURL: %s, apiKey: %s}", c.environment, c.baseURL, c.MaskedKey())
}

// GoString never shows the key (%#v).
func (c *Client) GoString() string { return c.String() }

// MarshalJSON never shows the key.
func (c *Client) MarshalJSON() ([]byte, error) {
	return json.Marshal(map[string]string{"environment": c.environment, "base_url": c.baseURL, "api_key": c.MaskedKey()})
}

// UserAgent is what every request says of the SDK.
func (c *Client) UserAgent() string {
	ua := fmt.Sprintf("phoenixperps-go/%s (%s; %s) spec/%s", SDKVersion, runtime.Version(), runtime.GOOS, SpecVersion)
	if c.appInfo != "" {
		ua += " " + c.appInfo
	}
	return ua
}

func mask(key string) string {
	if key == "" {
		return ""
	}
	cut := 0
	if i := strings.Index(key[min(4, len(key)):], "_"); i >= 0 {
		cut = min(4, len(key)) + i + 1
	}
	tail := key
	if len(key) > 4 {
		tail = key[len(key)-4:]
	}
	return key[:cut] + "****" + tail
}

type operation struct {
	id, method, path string
	idempotent       bool
	retrySafe        bool
	cursor           string
	scope            string // its rate-limit budget (x-pp-scope: read, trade, public)
}

// operations are the spec's, for Do to know a route's retry-safety and budget (gen_resources.go lists them).
var operations []*operation

// route is the operation a raw request is (the one with the most literal path segments), or nil.
func route(method, path string) *operation {
	var best *operation
	most := -1
	segs := strings.Split(path, "/")
	for _, op := range operations {
		tpl := strings.Split(op.path, "/")
		if op.method != method || len(tpl) != len(segs) {
			continue
		}
		literal := 0
		for i, t := range tpl {
			if strings.HasPrefix(t, "{") && segs[i] != "" {
				continue
			}
			if t != segs[i] {
				literal = -1
				break
			}
			literal++
		}
		if literal > most {
			best, most = op, literal
		}
	}
	return best
}

type callConfig struct {
	idempotencyKey string
	timeout        time.Duration
	maxRetries     int
	maxItems       int
	header         http.Header
	raw            *RawResponse
}

// CallOption configures one call.
type CallOption func(*callConfig)

// WithIdempotencyKey sets the call's Idempotency-Key (else a new UUID, the same on every attempt).
func WithIdempotencyKey(k string) CallOption { return func(c *callConfig) { c.idempotencyKey = k } }

// WithRequestTimeout sets the time one attempt of this call may take.
func WithRequestTimeout(d time.Duration) CallOption { return func(c *callConfig) { c.timeout = d } }

// WithRequestMaxRetries sets how often this call is tried again when that is safe.
func WithRequestMaxRetries(n int) CallOption { return func(c *callConfig) { c.maxRetries = n } }

// WithMaxItems caps an AutoPaging iteration (0: every item).
func WithMaxItems(n int) CallOption { return func(c *callConfig) { c.maxItems = n } }

// WithHeader adds a header to the request.
func WithHeader(k, v string) CallOption {
	return func(c *callConfig) {
		if c.header == nil {
			c.header = http.Header{}
		}
		c.header.Set(k, v)
	}
}

// WithRawResponse keeps the answer of the call (its status, headers, body, request id and Idempotency-Key).
func WithRawResponse(r *RawResponse) CallOption { return func(c *callConfig) { c.raw = r } }

// RawResponse is the answer of a call as it came.
type RawResponse struct {
	StatusCode     int
	Header         http.Header
	Body           []byte
	RequestID      string
	IdempotencyKey string
}

// retryStatus are the statuses tried again when the answer has no API envelope (a proxy's 502 page); with one, its own
// retryable decides (409 operation_in_progress is, 503 sandbox_disabled is not).
var retryStatus = map[int]bool{429: true, 500: true, 502: true, 503: true, 504: true}

func (c *Client) log(level slog.Level, msg string, args ...any) {
	if c.logger != nil {
		c.logger.Log(context.Background(), level, msg, args...)
	}
}

// preWait waits out the budget's exhausted rate-limit window when it ends soon (its last answer said none were left).
func (c *Client) preWait(ctx context.Context, scope string) error {
	c.mu.Lock()
	r := c.rateLimits[scope]
	c.mu.Unlock()
	if r == nil || r.Remaining > 0 || r.ResetAt.IsZero() {
		return nil
	}
	if d := time.Until(r.ResetAt); d > 0 && d < time.Minute {
		return sleep(ctx, d)
	}
	return nil
}

func (c *Client) remember(scope string, h http.Header) {
	limit := h.Get("X-RateLimit-Limit")
	if limit == "" {
		return
	}
	r := &RateLimit{}
	r.Limit, _ = strconv.Atoi(limit)
	r.Remaining, _ = strconv.Atoi(h.Get("X-RateLimit-Remaining"))
	if reset, err := strconv.ParseFloat(h.Get("X-RateLimit-Reset"), 64); err == nil {
		// Reset as epoch seconds (above a year in seconds) or as seconds from now
		if reset > 31536000 {
			r.ResetAt = time.UnixMilli(int64(reset * 1000))
		} else {
			r.ResetAt = time.Now().Add(time.Duration(reset * float64(time.Second)))
		}
	}
	c.mu.Lock()
	c.rateLimit, c.rateLimits[scope] = r, r
	dep := h.Get("Deprecation")
	first := dep != "" && !c.warned[dep]
	if first {
		c.warned[dep] = true
	}
	c.mu.Unlock()
	if first {
		c.log(slog.LevelWarn, "phoenixperps: this API version is deprecated", "deprecation", dep, "sunset", h.Get("Sunset"))
	}
}

func sleep(ctx context.Context, d time.Duration) error {
	t := time.NewTimer(d)
	defer t.Stop()
	select {
	case <-t.C:
		return nil
	case <-ctx.Done():
		return ctx.Err()
	}
}

func uuid4() string {
	var b [16]byte
	_, _ = rand.Read(b[:])
	b[6] = b[6]&0x0f | 0x40
	b[8] = b[8]&0x3f | 0x80
	return fmt.Sprintf("%x-%x-%x-%x-%x", b[0:4], b[4:6], b[6:8], b[8:10], b[10:16])
}

func (c *Client) url(op *operation, path []string, query url.Values) (string, error) {
	p := op.path
	for _, v := range path {
		i := strings.Index(p, "{")
		j := strings.Index(p, "}")
		if i < 0 || j < i {
			break
		}
		if v == "" {
			return "", &Error{Kind: KindValidation, Code: "invalid_request", Message: p[i+1:j] + " is required."}
		}
		p = p[:i] + url.PathEscape(v) + p[j+1:]
	}
	u := c.baseURL + p
	if len(query) > 0 {
		u += "?" + query.Encode()
	}
	return u, nil
}

// send makes one logical call: its attempts, and its answer (a 304 Not Modified too: no error, and no data).
func (c *Client) send(ctx context.Context, op *operation, path []string, query url.Values, body any, opts []CallOption) (*RawResponse, error) {
	cfg := callConfig{timeout: c.timeout, maxRetries: c.maxRetries}
	for _, o := range opts {
		o(&cfg)
	}
	key := cfg.idempotencyKey
	if key == "" && op.idempotent {
		key = uuid4()
	}
	retryable := op.retrySafe || key != "" // a GET, a retry-safe route, or a call with its Idempotency-Key
	u, err := c.url(op, path, query)
	if err != nil {
		return nil, err
	}
	var payload []byte
	if body != nil {
		if payload, err = json.Marshal(body); err != nil {
			return nil, &Error{Kind: KindValidation, Code: "invalid_request", Message: err.Error(), Err: err}
		}
	}
	for attempt := 0; ; attempt++ {
		if err := c.preWait(ctx, op.scope); err != nil {
			return nil, err
		}
		res, failure := c.attempt(ctx, op, u, payload, key, cfg)
		if failure != nil && ctx.Err() != nil {
			return nil, ctx.Err()
		}
		if res != nil {
			c.remember(op.scope, res.Header)
			if cfg.raw != nil {
				*cfg.raw = *res
			}
			if res.StatusCode == http.StatusNotModified { // the caller's If-None-Match: its copy is current
				return res, nil
			}
			if res.StatusCode >= 200 && res.StatusCode < 300 {
				// a proxy's page, or nothing at all (but for a 204 No Content): not the API's answer
				if b := bytes.TrimSpace(res.Body); (len(b) > 0 || res.StatusCode != http.StatusNoContent) && !json.Valid(b) {
					e := errorFrom(res.StatusCode, res.Header, res.Body, key)
					e.Kind = KindServer
					return nil, e
				}
				return res, nil
			}
			failure = errorFrom(res.StatusCode, res.Header, res.Body, key)
		}
		if !retryable || attempt >= cfg.maxRetries || !failure.Retryable {
			return nil, failure
		}
		wait, given := retryAfter(failure.Header, time.Now())
		if given && wait > retryAfterMax {
			return nil, failure
		}
		if !given {
			wait = time.Duration(mrand.Float64() * math.Min(8000, 500*math.Pow(2, float64(attempt))) * float64(time.Millisecond))
		}
		c.log(slog.LevelDebug, "phoenixperps: retrying", "operation", op.id, "attempt", attempt+1, "wait_ms", wait.Milliseconds(), "status", failure.Status)
		if err := sleep(ctx, wait); err != nil {
			return nil, err
		}
	}
}

func (c *Client) attempt(ctx context.Context, op *operation, u string, payload []byte, key string, cfg callConfig) (*RawResponse, *Error) {
	actx, cancel := context.WithTimeout(ctx, cfg.timeout)
	defer cancel()
	var rd io.Reader
	if payload != nil {
		rd = bytes.NewReader(payload)
	}
	req, err := http.NewRequestWithContext(actx, op.method, u, rd)
	if err != nil {
		return nil, &Error{Kind: KindValidation, Code: "invalid_request", Message: err.Error(), Err: err}
	}
	if c.key != "" { // none without a key: the public calls work, the others answer missing_api_key
		req.Header.Set("Authorization", "Bearer "+c.key)
	}
	req.Header.Set("Accept", "application/json")
	req.Header.Set("User-Agent", c.UserAgent())
	if payload != nil {
		req.Header.Set("Content-Type", "application/json")
	}
	if key != "" {
		req.Header.Set("Idempotency-Key", key)
	}
	for k, v := range cfg.header {
		req.Header[k] = v
	}
	resp, err := c.http.Do(req)
	if err == nil {
		var b []byte
		b, err = io.ReadAll(resp.Body)
		resp.Body.Close()
		if err == nil {
			return &RawResponse{StatusCode: resp.StatusCode, Header: resp.Header, Body: b, RequestID: resp.Header.Get("X-Request-Id"), IdempotencyKey: key}, nil
		}
	}
	if errors.Is(err, context.DeadlineExceeded) && ctx.Err() == nil {
		return nil, &Error{Kind: KindTimeout, Code: "timeout", Message: fmt.Sprintf("No answer within %s.", cfg.timeout), Retryable: true, IdempotencyKey: key, Err: err}
	}
	return nil, &Error{Kind: KindConnection, Code: "connection_failed", Message: clip(strings.ReplaceAll(err.Error(), c.key, mask(c.key))), Retryable: true, IdempotencyKey: key, Err: err}
}

// one makes a call whose answer is one object ({"data": ...}) and reads it into out.
func (c *Client) one(ctx context.Context, op *operation, path []string, query url.Values, body any, out any, opts []CallOption) error {
	res, err := c.send(ctx, op, path, query, body, opts)
	if err != nil {
		return err
	}
	return decodeData(res, out)
}

// oneOf makes a call whose answer is one object: nil, and no error, on a 304 Not Modified (the caller's copy is
// current). The generated one-object calls are to answer through it (generator/printers/go.js).
func oneOf[T any](ctx context.Context, c *Client, op *operation, path []string, query url.Values, body any, opts []CallOption) (*T, error) {
	res, err := c.send(ctx, op, path, query, body, opts)
	if err != nil || res.StatusCode == http.StatusNotModified {
		return nil, err
	}
	out := new(T)
	if err := decodeData(res, out); err != nil {
		return nil, err
	}
	return out, nil
}

// Do sends any request to the API (an operation newer than this SDK, or one made at run time) with the client's key,
// retries and errors. path is the API path (/v1/...). It is tried again when that is safe: a GET, a route the spec
// marks retry-safe, or a call with its Idempotency-Key (WithIdempotencyKey). A 2xx or 304 answer comes as it came; any
// other, and a 2xx that is no JSON (a proxy's page), is an *Error.
func (c *Client) Do(ctx context.Context, method, path string, query url.Values, body any, opts ...CallOption) (*RawResponse, error) {
	if !strings.HasPrefix(path, "/") {
		return nil, &Error{Kind: KindValidation, Code: "invalid_request", Message: "The path starts with /."}
	}
	m := strings.ToUpper(method)
	op := &operation{id: "do", method: m, path: path, retrySafe: m == http.MethodGet}
	if r := route(m, path); r != nil {
		op.retrySafe, op.scope = op.retrySafe || r.retrySafe, r.scope
	}
	return c.send(ctx, op, nil, query, body, opts)
}

func decodeData(res *RawResponse, out any) error {
	b := res.Body
	if len(bytes.TrimSpace(b)) == 0 {
		return nil
	}
	var env map[string]json.RawMessage
	if json.Unmarshal(b, &env) == nil {
		if d, ok := env["data"]; ok {
			b = d
		}
	}
	if err := json.Unmarshal(b, out); err != nil {
		return &Error{Kind: KindServer, Status: res.StatusCode, Code: "invalid_response", Message: "The answer could not be read: " + err.Error(), RequestID: res.RequestID, IdempotencyKey: res.IdempotencyKey, Err: err}
	}
	return nil
}

// optionalBody is nil for a nil pointer, so no body is sent.
func optionalBody[T any](b *T) any {
	if b == nil {
		return nil
	}
	return b
}
