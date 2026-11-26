package phoenixperps

// Calls against the conformance mock: what is tried again, the base URL, a page's list-level fields, the environment,
// the keyless client, 304, the rate-limit budgets, Do's routes.

import (
	"context"
	"encoding/json"
	"errors"
	"testing"
	"time"
)

// The API's own retryable decides: 409 operation_in_progress (the same key while the first attempt, timed out here,
// still runs) is tried again with the same key; 503 sandbox_disabled, retryable false, is not.
func TestRetryableFlag(t *testing.T) {
	c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL(mockURL))
	ctx := context.Background()
	load(t, []json.RawMessage{
		json.RawMessage(`{"respond":{"status":201,"delay_ms":1500,"body":{"data":{"id":"ord_1"}}}}`),
		json.RawMessage(`{"respond":{"status":409,"headers":{"retry-after":"0"},"body":{"error":{"type":"conflict","code":"operation_in_progress","message":"x","retryable":true}}}}`),
		json.RawMessage(`{"respond":{"status":201,"body":{"data":{"id":"ord_1","status":"working"}}}}`),
	})
	o, err := c.Orders.Create(ctx, "SANDBOX-00042-001", CreateOrder{Market: "BTC-USD", Side: OrderSideBuy, Type: CreateOrderTypeLimit, Size: Dec("0.01"), LimitPrice: Dec("62000")}, WithRequestTimeout(500*time.Millisecond))
	got := requests(t)
	if err != nil || o.ID != "ord_1" || len(got) != 3 {
		t.Fatalf("operation_in_progress: %v after %d requests", err, len(got))
	}
	for _, r := range got {
		if r.Headers["idempotency-key"] != got[0].Headers["idempotency-key"] {
			t.Fatal("a new Idempotency-Key")
		}
	}

	load(t, []json.RawMessage{json.RawMessage(`{"respond":{"status":503,"body":{"error":{"type":"engine","code":"sandbox_disabled","message":"x","retryable":false}}}}`)})
	_, err = c.Markets.Get(ctx, "BTC-USD")
	var e *Error
	if !errors.Is(err, ErrServer) || len(requests(t)) != 1 {
		t.Fatalf("sandbox_disabled: %v after %d requests", err, len(requests(t)))
	}
	if !errors.As(err, &e) || e.Retryable || e.Header.Get("X-Request-Id") != "req_conformance" {
		t.Fatalf("the error keeps the API's retryable and the answer's headers: %+v", e)
	}
}

// A change sent with its own Idempotency-Key (the API honours one on PATCH) is tried again with it.
func TestRetryWithGivenKey(t *testing.T) {
	c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL(mockURL))
	load(t, []json.RawMessage{
		json.RawMessage(`{"respond":{"status":503,"headers":{"retry-after":"0"},"body":{"error":{"type":"engine","code":"engine_unavailable","message":"x","retryable":true}}}}`),
		json.RawMessage(`{"respond":{"status":200,"body":{"data":{"id":"ord_1"}}}}`),
	})
	o, err := c.Orders.Modify(context.Background(), "SANDBOX-00042-001", "ord_1", ModifyOrder{LimitPrice: Dec("61900")}, WithIdempotencyKey("chg-1"))
	got := requests(t)
	if err != nil || o.ID != "ord_1" || len(got) != 2 || got[0].Headers["idempotency-key"] != "chg-1" || got[1].Headers["idempotency-key"] != "chg-1" {
		t.Fatalf("%v after %d requests", err, len(got))
	}
}

// The environment is live or sandbox in any case: the one given, else the key's own, else PHOENIXPERPS_ENVIRONMENT; a
// key of the other one fails before any request.
func TestEnvironmentAnyCase(t *testing.T) {
	c, err := NewClient(WithAPIKey(S.Key), WithEnvironment("SANDBOX"), WithBaseURL(mockURL))
	if err != nil || c.Environment() != "sandbox" {
		t.Fatalf("%v %v", c, err)
	}
	if _, err := NewClient(WithAPIKey(S.Key), WithEnvironment("Live"), WithBaseURL(mockURL)); !errors.Is(err, &Error{Kind: KindAuthentication, Code: ErrCodeWrongEnvironment}) {
		t.Fatalf("a test key on Live: %v", err)
	}
	t.Setenv("PHOENIXPERPS_ENVIRONMENT", "LIVE")
	if c, err := NewClient(WithAPIKey(S.Key), WithBaseURL(mockURL)); err != nil || c.Environment() != "sandbox" {
		t.Fatalf("a test key goes before the variable: %v %v", c, err)
	}
	t.Setenv("PHOENIXPERPS_API_KEY", "")
	if c, err := NewClient(WithBaseURL(mockURL)); err != nil || c.Environment() != "live" {
		t.Fatalf("without a key, the variable: %v %v", c, err)
	}
}

// Without a key no Authorization goes out: a call that needs one answers the API's own missing_api_key.
func TestKeylessPrivateCall(t *testing.T) {
	t.Setenv("PHOENIXPERPS_API_KEY", "")
	c, err := NewClient(WithBaseURL(mockURL))
	if err != nil {
		t.Fatal(err)
	}
	load(t, []json.RawMessage{json.RawMessage(`{"respond":{"status":401,"body":{"error":{"type":"authentication","code":"missing_api_key","message":"x","retryable":false}}}}`)})
	_, err = c.Me.Get(context.Background())
	if got := requests(t); !errors.Is(err, &Error{Kind: KindAuthentication, Code: ErrCodeMissingAPIKey}) || len(got) != 1 || got[0].Headers["authorization"] != "" {
		t.Fatalf("%v, sent %+v", err, got)
	}
}

// A 304 Not Modified is no error: a list answers no page, a one-object call nil, and the meta keeps the 304 and ETag.
func TestNotModified(t *testing.T) {
	c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL(mockURL))
	ctx := context.Background()
	notModified := json.RawMessage(`{"respond":{"status":304,"headers":{"etag":"\"v1-abc\""}}}`)
	load(t, []json.RawMessage{notModified, notModified})
	var raw RawResponse
	page, err := c.Orders.List(ctx, "SANDBOX-00042-001", nil, WithHeader("If-None-Match", `"v1-abc"`), WithRawResponse(&raw))
	if page != nil || err != nil || raw.StatusCode != 304 || raw.Header.Get("ETag") != `"v1-abc"` {
		t.Fatalf("list: %+v %v, meta %d %q", page, err, raw.StatusCode, raw.Header.Get("ETag"))
	}
	if m, err := oneOf[Market](ctx, c, opGetMarket, []string{"BTC-USD"}, nil, nil, nil); m != nil || err != nil {
		t.Fatalf("one object: %+v %v", m, err)
	}
	if got := requests(t); len(got) != 2 || got[0].Headers["if-none-match"] != `"v1-abc"` {
		t.Fatalf("sent %+v", got)
	}
}

// The rate-limit wait is per budget (x-pp-scope): a trade window used up holds the next trade, never a read.
// The next request of a used-up budget comes 60 s / Limit later (1 s at 60 a minute: Q15), and holds that budget only.
func TestRateLimitPerBudget(t *testing.T) {
	c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL(mockURL))
	ctx := context.Background()
	trade := &operation{id: "trade", method: "POST", path: "/v1/t", scope: "trade"}
	read := &operation{id: "read", method: "GET", path: "/v1/r", retrySafe: true, scope: "read"}
	ok := json.RawMessage(`{"respond":{"status":200,"body":{"data":{}}}}`)
	load(t, []json.RawMessage{json.RawMessage(`{"respond":{"status":200,"headers":{"x-ratelimit-limit":"60","x-ratelimit-remaining":"0","x-ratelimit-reset":"1"},"body":{"data":{}}}}`), ok, ok})
	for _, call := range []struct {
		op          *operation
		least, most time.Duration
	}{{trade, 0, time.Minute}, {read, 0, 500 * time.Millisecond}, {trade, 800 * time.Millisecond, time.Minute}} {
		start := time.Now()
		if _, err := c.send(ctx, call.op, nil, nil, nil, nil); err != nil {
			t.Fatal(err)
		}
		if d := time.Since(start); d < call.least || d > call.most {
			t.Fatalf("%s took %v", call.op.id, d)
		}
	}
}

// Do knows a route the spec marks retry-safe (an order's DELETE) from the operations, and tries it again; a POST
// without its Idempotency-Key is sent once.
func TestDoRetrySafeRoute(t *testing.T) {
	saved := operations
	operations = []*operation{opPlaceOrder, opCancelOrder, opCancelAllOrders, opModifyOrder}
	defer func() { operations = saved }()
	if r := route("POST", "/v1/accounts/A/orders/cancel-all"); r != opCancelAllOrders {
		t.Fatalf("route %+v", r)
	}
	c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL(mockURL))
	unavailable := json.RawMessage(`{"respond":{"status":503,"headers":{"retry-after":"0"},"body":{"error":{"type":"engine","code":"engine_unavailable","message":"x","retryable":true}}}}`)
	load(t, []json.RawMessage{unavailable, json.RawMessage(`{"respond":{"status":200,"body":{"data":{"id":"ord_1"}}}}`)})
	if _, err := c.Do(context.Background(), "DELETE", "/v1/accounts/A/orders/ord_1", nil, nil); err != nil || len(requests(t)) != 2 {
		t.Fatalf("DELETE: %v after %d requests", err, len(requests(t)))
	}
	// a POST to a route that takes an Idempotency-Key carries one, the same when asked again (Q39)
	load(t, []json.RawMessage{unavailable, json.RawMessage(`{"respond":{"status":201,"body":{"data":{"id":"ord_1"}}}}`)})
	if _, err := c.Do(context.Background(), "POST", "/v1/accounts/A/orders", nil, map[string]string{"market": "BTC-USD"}); err != nil {
		t.Fatalf("POST: %v", err)
	}
	got := requests(t)
	if len(got) != 2 || got[0].Headers["idempotency-key"] == "" || got[0].Headers["idempotency-key"] != got[1].Headers["idempotency-key"] {
		t.Fatalf("POST: %d requests, keys %q %q", len(got), got[0].Headers["idempotency-key"], got[len(got)-1].Headers["idempotency-key"])
	}
}

// The REST address as the docs write it (…/v1) is the host.
func TestBaseURLWithVersion(t *testing.T) {
	load(t, []json.RawMessage{json.RawMessage(`{"respond":{"status":200,"body":{"data":{}}}}`)})
	c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL(mockURL+"/v1/"))
	if _, err := c.Me.Get(context.Background()); err != nil || c.BaseURL() != mockURL {
		t.Fatalf("%v, base %s", err, c.BaseURL())
	}
	if got := requests(t); got[0].Path != "/v1/me" {
		t.Fatalf("path %s", got[0].Path)
	}
}

// The orders list's history_complete reaches the page; the lists without it leave it nil.
func TestPageHistoryComplete(t *testing.T) {
	c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL(mockURL))
	load(t, []json.RawMessage{
		json.RawMessage(`{"respond":{"status":200,"body":{"data":[],"has_more":false,"next_cursor":null,"as_of":1,"history_complete":false}}}`),
		json.RawMessage(`{"respond":{"status":200,"body":{"data":[],"has_more":false,"next_cursor":null,"as_of":1,"stale":true}}}`),
	})
	orders, err := c.Orders.List(context.Background(), "SANDBOX-00042-001", nil)
	if err != nil || orders.HistoryComplete == nil || *orders.HistoryComplete {
		t.Fatalf("orders: %+v %v", orders, err)
	}
	fills, err := c.Fills.List(context.Background(), "SANDBOX-00042-001", nil)
	if err != nil || fills.HistoryComplete != nil {
		t.Fatalf("fills: %+v %v", fills, err)
	}
	// R7: a list read from figures older than usual says so (stale), as on every account list
	if fills.Stale == nil || !*fills.Stale || orders.Stale != nil {
		t.Fatalf("stale: fills %v, orders %v", fills.Stale, orders.Stale)
	}
}

// Q88: a field the API takes as null ("null removes it") is sent as null when its Null flag is set, never otherwise.
func TestExplicitNull(t *testing.T) {
	b, _ := json.Marshal(ChangePriceAlert{NoteNull: true})
	if string(b) != `{"note":null}` {
		t.Fatalf("null: %s", b)
	}
	b, _ = json.Marshal(ChangePriceAlert{Price: Dec("62000")})
	if string(b) != `{"price":"62000"}` {
		t.Fatalf("absent: %s", b)
	}
	b, _ = json.Marshal(ChangePriceAlert{Note: Ptr("moved"), NoteNull: true})
	if string(b) != `{"note":"moved"}` {
		t.Fatalf("set wins: %s", b)
	}
}

// Q63: Frames yields market data only; snapshot_end comes through Next.
func TestFramesDataOnly(t *testing.T) {
	m := &MarketStream{frames: make(chan MarketFrame, 4), done: make(chan struct{})}
	m.frames <- MarketFrame{Type: "snapshot_end"}
	m.frames <- MarketFrame{Type: "ticker", Symbol: "BTC-USD"}
	close(m.frames)
	for f, err := range m.Frames() {
		if err != nil {
			break
		}
		if f.Type != "ticker" {
			t.Fatalf("frame %s", f.Type)
		}
		return
	}
	t.Fatal("no frame")
}
