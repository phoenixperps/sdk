package phoenixperps

import (
	"context"
	"encoding/json"
	"errors"
	"net/http"
	"net/http/httptest"
	"net/url"
	"sync/atomic"
	"testing"
)

// Do: the client's key on any path, a GET tried again, a POST only with its Idempotency-Key, errors as every call's.
func TestDo(t *testing.T) {
	var calls atomic.Int32
	srv := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		n := calls.Add(1)
		if r.Header.Get("Authorization") != "Bearer "+S.Key {
			w.WriteHeader(401)
			return
		}
		switch r.URL.Path {
		case "/v1/flaky":
			if n == 1 {
				w.WriteHeader(503)
				return
			}
			_, _ = w.Write([]byte(`{"data":{"q":"` + r.URL.Query().Get("q") + `"}}`))
		case "/v1/write":
			w.Header().Set("X-Request-Id", "req_1")
			w.WriteHeader(503)
			_, _ = w.Write([]byte(`{"error":{"code":"engine_unavailable","message":"Later."}}`))
		}
	}))
	defer srv.Close()
	c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL(srv.URL))
	ctx := context.Background()

	res, err := c.Do(ctx, "get", "/v1/flaky", url.Values{"q": {"x y"}}, nil, WithRequestMaxRetries(1))
	if err != nil || calls.Load() != 2 {
		t.Fatalf("GET: %v after %d calls", err, calls.Load())
	}
	var out struct{ Data struct{ Q string } }
	if json.Unmarshal(res.Body, &out) != nil || out.Data.Q != "x y" {
		t.Fatalf("body %s", res.Body)
	}

	calls.Store(0)
	_, err = c.Do(ctx, "POST", "/v1/write", nil, map[string]string{"a": "1"}, WithRequestMaxRetries(1))
	var e *Error
	if !errors.As(err, &e) || e.Code != "engine_unavailable" || e.RequestID != "req_1" || calls.Load() != 1 {
		t.Fatalf("POST without a key: %v after %d calls", err, calls.Load())
	}
	calls.Store(0)
	var raw RawResponse
	_, err = c.Do(ctx, "POST", "/v1/write", nil, nil, WithIdempotencyKey("k-1"), WithRequestMaxRetries(1), WithRawResponse(&raw))
	if err == nil || calls.Load() != 2 || raw.IdempotencyKey != "k-1" || raw.StatusCode != 503 {
		t.Fatalf("POST with a key: %v after %d calls, %+v", err, calls.Load(), raw)
	}
	if _, err := c.Do(ctx, "GET", "v1/x", nil, nil); !errors.Is(err, ErrValidation) {
		t.Fatalf("relative path: %v", err)
	}
}
