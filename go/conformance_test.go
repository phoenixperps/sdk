package phoenixperps

// The conformance suite (conformance/scenarios.json) through the Go SDK against the mock server
// (node conformance/mock-server.js): retries, idempotency keys, Retry-After, errors, 304, paging, open enums, decimals,
// the rate-limit wait; the local checks (wrong environment, the key never printed, webhook signatures, the keyless
// client, exact rounding); and the streams. The file is read strictly: a key this runner does not play fails it.

import (
	"bufio"
	"bytes"
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"iter"
	"net/http"
	"os"
	"os/exec"
	"path/filepath"
	"reflect"
	"regexp"
	"slices"
	"strings"
	"sync"
	"testing"
	"time"
)

type confCall struct {
	Resource string            `json:"resource"`
	Method   string            `json:"method"`
	Path     map[string]string `json:"path"`
	Body     json.RawMessage   `json:"body"`
	Query    json.RawMessage   `json:"query"`
	Iterate  bool              `json:"iterate"`
	MaxItems int               `json:"max_items"`
}

type scenario struct {
	Name   string `json:"name"`
	Client *struct {
		BaseURLSuffix string `json:"base_url_suffix"`
	} `json:"client"`
	Call      *confCall         `json:"call"`
	Calls     []confCall        `json:"calls"`
	Exchanges []json.RawMessage `json:"exchanges"`
	Expect    struct {
		OK       *bool `json:"ok"`
		Requests int   `json:"requests"`
		Error    *struct {
			Class        string `json:"class"`
			Code         string `json:"code"`
			Status       int    `json:"status"`
			RetryAfterMs *int64 `json:"retry_after_ms"`
			OrderID      string `json:"order_id"`
			RequestID    string `json:"request_id"`
			Retryable    *bool  `json:"retryable"`
			DocURL       string `json:"doc_url"`
		} `json:"error"`
		Result       json.RawMessage `json:"result"` // null: the call answered nothing
		Items        []string        `json:"items"`
		Cursors      []*string       `json:"cursors"`
		FirstBody    map[string]any  `json:"first_body"`
		MinElapsedMs int64           `json:"min_elapsed_ms"`
		Checks       []string        `json:"checks"`
		PageFields   map[string]any  `json:"page_fields"`
		Paths        []string        `json:"paths"`
	} `json:"expect"`
}

type webhookVector struct {
	Name, Signature, Body, Timestamp, Error string
	Want                                    map[string]any `json:"want"`
}

type suite struct {
	Key       string     `json:"key"`
	Scenarios []scenario `json:"scenarios"`
	Local     []struct {
		Name   string `json:"name"`
		Check  string `json:"check"`
		Client struct {
			APIKey      *string `json:"api_key"`
			Environment string  `json:"environment"`
		} `json:"client"`
		Expect struct {
			ErrorCode string `json:"error_code"`
		} `json:"expect"`
	} `json:"local"`
	Webhook struct {
		Secret, Other, Timestamp, Body, Signature, Rotation string
		NowMs                                               int64           `json:"now_ms"`
		Want                                                map[string]any  `json:"want"`
		Extra                                               []webhookVector `json:"extra"`
	} `json:"webhook_vectors"`
	Rules struct {
		Market Market `json:"market"`
		Cases  []struct {
			Fn    string   `json:"fn"`
			Args  []string `json:"args"`
			Mode  string   `json:"mode"`
			Want  string   `json:"want"`
			Error bool     `json:"error"` // a ValidationError of the SDK's own
		} `json:"cases"`
	} `json:"rules_vectors"`
	Streams    []streamScenario  `json:"streams"`
	Vocabulary map[string]string `json:"vocabulary"`
}

type seen struct {
	Method  string            `json:"method"`
	Path    string            `json:"path"`
	Query   map[string]string `json:"query"`
	Headers map[string]string `json:"headers"`
	Body    map[string]any    `json:"body"`
	At      int64             `json:"at"`
}

var (
	S       suite
	mockURL string
)

func TestMain(m *testing.M) {
	root, _ := filepath.Abs("..")
	b, err := os.ReadFile(filepath.Join(root, "conformance", "scenarios.json"))
	if err != nil {
		panic(err)
	}
	dec := json.NewDecoder(bytes.NewReader(b))
	dec.DisallowUnknownFields() // a new key is played here, never skipped
	if err := dec.Decode(&S); err != nil {
		panic(fmt.Sprintf("conformance/scenarios.json: %v", err))
	}
	cmd := exec.Command("node", filepath.Join(root, "conformance", "mock-server.js"), "0")
	out, _ := cmd.StdoutPipe()
	if err := cmd.Start(); err != nil {
		panic(err)
	}
	line, _ := bufio.NewReader(out).ReadString('\n')
	mockURL = strings.TrimSpace(line)
	code := m.Run()
	_ = cmd.Process.Kill()
	os.Exit(code)
}

func into(raw json.RawMessage, v any) error {
	if len(raw) == 0 || string(raw) == "null" {
		return nil
	}
	return json.Unmarshal(raw, v)
}

func collect[T any](seq iter.Seq2[T, error]) ([]string, error) {
	ids := []string{}
	for item, err := range seq {
		if err != nil {
			return ids, err
		}
		b, _ := json.Marshal(item)
		var m struct {
			ID string `json:"id"`
		}
		_ = json.Unmarshal(b, &m)
		ids = append(ids, m.ID)
	}
	return ids, nil
}

func load(t *testing.T, exchanges []json.RawMessage) {
	b, _ := json.Marshal(map[string]any{"exchanges": exchanges})
	res, err := http.Post(mockURL+"/__scenario", "application/json", bytes.NewReader(b))
	if err != nil {
		t.Fatal(err)
	}
	res.Body.Close()
}

func requests(t *testing.T) []seen {
	res, err := http.Get(mockURL + "/__requests")
	if err != nil {
		t.Fatal(err)
	}
	defer res.Body.Close()
	var got []seen
	if err := json.NewDecoder(res.Body).Decode(&got); err != nil {
		t.Fatal(err)
	}
	return got
}

// pick keeps of got only what want names (the expected objects are partial).
func pick(got any, want any) any {
	w, ok := want.(map[string]any)
	if !ok {
		return got
	}
	g, _ := got.(map[string]any)
	out := map[string]any{}
	for k, v := range w {
		out[k] = pick(g[k], v)
	}
	return out
}

func TestConformance(t *testing.T) {
	for _, sc := range S.Scenarios {
		t.Run(sc.Name, func(t *testing.T) {
			load(t, sc.Exchanges)
			base := mockURL
			if sc.Client != nil {
				base += sc.Client.BaseURLSuffix
			}
			c, err := NewClient(WithAPIKey(S.Key), WithBaseURL(base), WithMaxRetries(2))
			if err != nil {
				t.Fatal(err)
			}
			calls := sc.Calls
			if sc.Call != nil {
				calls = []confCall{*sc.Call}
			}
			var raw *RawResponse
			var items []string
			for _, call := range calls {
				raw, items, err = dispatch(context.Background(), c, call)
				if err != nil {
					break
				}
			}
			got := requests(t)
			x := sc.Expect
			if len(got) != x.Requests {
				t.Fatalf("requests: %d, want %d", len(got), x.Requests)
			}
			if x.OK != nil && *x.OK != (err == nil) {
				t.Fatalf("ok %v, want %v (%v)", err == nil, *x.OK, err)
			}
			if x.Error != nil {
				var e *Error
				if !errors.As(err, &e) {
					t.Fatalf("want an *Error, got %v", err)
				}
				w := x.Error
				if (w.Class != "" && string(e.Kind) != w.Class) || e.Code != w.Code || (w.Status != 0 && e.Status != w.Status) {
					t.Fatalf("got %s %s %d, want %s %s %d", e.Kind, e.Code, e.Status, w.Class, w.Code, w.Status)
				}
				if w.RetryAfterMs != nil && e.RetryAfter.Milliseconds() != *w.RetryAfterMs {
					t.Fatalf("retry after %v", e.RetryAfter)
				}
				if w.OrderID != "" && e.OrderID != w.OrderID {
					t.Fatalf("order id %q", e.OrderID)
				}
				if w.RequestID != "" && e.RequestID != w.RequestID {
					t.Fatalf("request id %q", e.RequestID)
				}
				if w.Retryable != nil && e.Retryable != *w.Retryable {
					t.Fatalf("retryable %v", e.Retryable)
				}
				if w.DocURL != "" && e.DocURL != w.DocURL {
					t.Fatalf("doc url %q", e.DocURL)
				}
			} else {
				if err != nil {
					t.Fatal(err)
				}
				var env struct {
					Data any `json:"data"`
				}
				_ = json.Unmarshal(raw.Body, &env)
				switch {
				case string(x.Result) == "null": // the call answered nothing
					if env.Data != nil {
						t.Fatalf("result %v, want none", env.Data)
					}
				case len(x.Result) > 0:
					var want map[string]any
					_ = json.Unmarshal(x.Result, &want)
					if p := pick(env.Data, want); !reflect.DeepEqual(p, any(want)) {
						t.Fatalf("result %v, want %v", p, want)
					}
				}
				if x.Items != nil && !reflect.DeepEqual(items, x.Items) {
					t.Fatalf("items %v, want %v", items, x.Items)
				}
				if x.PageFields != nil { // the page the list call reads: its fields, as it marshals them
					var page Page[json.RawMessage]
					_ = json.Unmarshal(raw.Body, &page)
					var fields map[string]any
					b, _ := json.Marshal(page)
					_ = json.Unmarshal(b, &fields)
					if p := pick(fields, x.PageFields); !reflect.DeepEqual(p, any(x.PageFields)) {
						t.Fatalf("page fields %v, want %v", p, x.PageFields)
					}
				}
			}
			for i, p := range x.Paths {
				if got[i].Path != p {
					t.Fatalf("path %d: %s, want %s", i, got[i].Path, p)
				}
			}
			if x.Cursors != nil {
				for i, r := range got {
					c, ok := r.Query["cursor"]
					want := x.Cursors[i]
					if (want == nil) == ok || (want != nil && *want != c) {
						t.Fatalf("cursor %d: %q (%v), want %v", i, c, ok, want)
					}
				}
			}
			if x.FirstBody != nil {
				if p := pick(got[0].Body, x.FirstBody); !reflect.DeepEqual(p, any(x.FirstBody)) {
					t.Fatalf("first body %v, want %v", p, x.FirstBody)
				}
			}
			if x.MinElapsedMs > 0 && got[len(got)-1].At-got[0].At < x.MinElapsedMs {
				t.Fatalf("elapsed %d ms, want %d", got[len(got)-1].At-got[0].At, x.MinElapsedMs)
			}
			for _, check := range x.Checks {
				switch check {
				case "bearer":
					if got[0].Headers["authorization"] != "Bearer "+S.Key {
						t.Fatal("bearer")
					}
				case "user_agent":
					if !regexp.MustCompile(`^phoenixperps-go/\d+\.\d+\.\d+ \(.+\) spec/`).MatchString(got[0].Headers["user-agent"]) {
						t.Fatalf("user agent %q", got[0].Headers["user-agent"])
					}
				case "same_idempotency_key":
					keys := map[string]bool{}
					for _, r := range got {
						keys[r.Headers["idempotency-key"]] = true
					}
					if len(keys) != 1 {
						t.Fatalf("keys %v", keys)
					}
					for k := range keys {
						if !regexp.MustCompile(`^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$`).MatchString(k) {
							t.Fatalf("key %q", k)
						}
					}
				}
			}
		})
	}
}

// TestLocal plays the local checks, each by its check (none: a client that fails before any request).
func TestLocal(t *testing.T) {
	for _, l := range S.Local {
		t.Run(l.Name, func(t *testing.T) {
			switch l.Check {
			case "":
				_, err := NewClient(WithAPIKey(*l.Client.APIKey), WithEnvironment(l.Client.Environment), WithBaseURL("http://127.0.0.1:9"))
				var e *Error
				if !errors.As(err, &e) || e.Code != l.Expect.ErrorCode || !errors.Is(err, ErrAuthentication) {
					t.Fatalf("got %v", err)
				}
			case "redaction":
				localRedaction(t)
			case "webhook_vectors":
				localWebhooks(t)
			case "keyless":
				localKeyless(t, l.Client.APIKey, l.Client.Environment)
			default:
				t.Fatalf("no check %q in this runner", l.Check)
			}
		})
	}
}

// A client without a key is made, and a public call sends no Authorization.
func localKeyless(t *testing.T, key *string, environment string) {
	t.Setenv("PHOENIXPERPS_API_KEY", "")
	if key != nil {
		t.Fatalf("a keyless check with a key %q", *key)
	}
	c, err := NewClient(WithEnvironment(environment), WithBaseURL(mockURL))
	if err != nil {
		t.Fatal(err)
	}
	load(t, []json.RawMessage{json.RawMessage(`{"respond":{"status":200,"body":{"data":[],"has_more":false,"next_cursor":null,"as_of":1}}}`)})
	if _, err := c.Markets.List(context.Background()); err != nil {
		t.Fatal(err)
	}
	if got := requests(t); len(got) != 1 || got[0].Headers["authorization"] != "" {
		t.Fatalf("sent %+v", got)
	}
}

func localRedaction(t *testing.T) {
	c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL("http://127.0.0.1:9"))
	j, _ := json.Marshal(c)
	for _, text := range []string{fmt.Sprint(c), fmt.Sprintf("%v %+v %#v %s", c, c, c, c), string(j)} {
		if strings.Contains(text, S.Key) {
			t.Fatalf("the key shows: %s", text)
		}
	}
	if c.MaskedKey() != "ppk_test_****"+S.Key[len(S.Key)-4:] {
		t.Fatal(c.MaskedKey())
	}
	_, err := c.Me.Get(context.Background(), WithRequestTimeout(time.Second), WithRequestMaxRetries(0))
	if err == nil || strings.Contains(err.Error(), S.Key) {
		t.Fatalf("error: %v", err)
	}
}

func localWebhooks(t *testing.T) {
	v := S.Webhook
	at := func(ts, sig string) http.Header {
		h := http.Header{}
		h.Set("X-Phoenix-Timestamp", ts)
		h.Set("X-Phoenix-Signature", sig)
		return h
	}
	now := time.UnixMilli(v.NowMs)
	// wants checks the payload against a vector's want (in the API's names)
	wants := func(p *WebhookPayload, want map[string]any) {
		t.Helper()
		var got map[string]any
		b, _ := json.Marshal(p)
		_ = json.Unmarshal(b, &got)
		if p := pick(got, want); !reflect.DeepEqual(p, any(want)) {
			t.Fatalf("payload %v, want %v", got, want)
		}
	}
	p, err := VerifyWebhookAt([]byte(v.Body), at(v.Timestamp, v.Signature), []string{v.Secret}, 0, now)
	if err != nil {
		t.Fatal(err)
	}
	wants(p, v.Want)
	if p, err := VerifyWebhookAt([]byte(v.Body), at(v.Timestamp, v.Rotation), []string{v.Secret}, 0, now); err != nil {
		t.Fatal(err)
	} else {
		wants(p, v.Want)
	}
	for _, c := range []struct {
		secret, body, code string
		now                time.Time
	}{{v.Other, v.Body, "bad_signature", now}, {v.Secret, v.Body, "stale", now.Add(1000 * time.Second)}, {v.Secret, v.Body + " ", "bad_signature", now}} {
		_, err := VerifyWebhookAt([]byte(c.body), at(v.Timestamp, v.Signature), []string{c.secret}, 0, c.now)
		var e *Error
		if !errors.Is(err, ErrWebhookVerification) || !errors.As(err, &e) || e.Code != c.code {
			t.Fatalf("want %s, got %v", c.code, err)
		}
	}
	for _, x := range v.Extra {
		p, err := VerifyWebhookAt([]byte(x.Body), at(x.Timestamp, x.Signature), []string{v.Secret}, 0, now)
		if x.Error != "" {
			var e *Error
			if !errors.Is(err, ErrWebhookVerification) || !errors.As(err, &e) || e.Code != x.Error {
				t.Fatalf("%s: want %s, got %v", x.Name, x.Error, err)
			}
			continue
		}
		if err != nil {
			t.Fatalf("%s: %v", x.Name, err)
		}
		wants(p, x.Want)
	}
}

func TestLocalMarketRules(t *testing.T) {
	r := NewMarketRules(&S.Rules.Market)
	modes := map[string]RoundMode{"": RoundNearest, "nearest": RoundNearest, "down": RoundDown, "up": RoundUp}
	for _, c := range S.Rules.Cases {
		a := c.Args
		var got Decimal
		var err error
		switch c.Fn {
		case "round_price":
			got, err = r.RoundPrice(Decimal(a[0]), modes[c.Mode])
		case "round_size":
			got, err = r.RoundSize(Decimal(a[0]))
		case "size_for_notional":
			got, err = r.SizeForNotional(Decimal(a[0]), Decimal(a[1]))
		case "size_for_risk":
			got, err = r.SizeForRisk(Decimal(a[0]), Decimal(a[1]))
		case "worst_price":
			var pct *Decimal
			if len(a) > 2 {
				pct = Dec(a[2])
			}
			got, err = r.WorstPrice(a[0], Decimal(a[1]), pct)
		}
		if c.Error {
			if !errors.Is(err, ErrValidation) || got != "" {
				t.Errorf("%s(%v %s) = %q %v, want a ValidationError", c.Fn, a, c.Mode, got, err)
			}
			continue
		}
		if err != nil || string(got) != c.Want {
			t.Errorf("%s(%v %s) = %q %v, want %q", c.Fn, a, c.Mode, got, err, c.Want)
		}
	}
	if d := DecimalOf(0.000000001); d != "0.000000001" {
		t.Fatal(d)
	}
}

// An unknown enum value and an unknown field: the typed answer keeps the value and ignores the field.
func TestOpenEnum(t *testing.T) {
	load(t, []json.RawMessage{json.RawMessage(`{"respond":{"status":200,"body":{"data":{"id":"ord_1","status":"a_status_from_the_future","a_new_field":1}}}}`)})
	c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL(mockURL))
	o, err := c.Orders.Get(context.Background(), "SANDBOX-00042-001", "ord_1")
	if err != nil || o.Status != OrderStatus("a_status_from_the_future") {
		t.Fatalf("%v %v", o, err)
	}
}

type marketCall struct {
	Channel string         `json:"channel"`
	Symbols []string       `json:"symbols"`
	Params  map[string]any `json:"params"`
}

type streamScenario struct {
	Name        string              `json:"name"`
	Stream      string              `json:"stream"`
	Connections [][]json.RawMessage `json:"connections"`
	Subscribe   *marketCall         `json:"subscribe"`
	Unsubscribe *marketCall         `json:"unsubscribe"`
	Expect      struct {
		Events          []string       `json:"events"`
		Frames          []string       `json:"frames"`
		Connections     int            `json:"connections"`
		Resume          []*string      `json:"resume"`
		FirstMessage    map[string]any `json:"first_message"`
		Resubscribe     map[string]any `json:"resubscribe"`
		UnsubscribeSent map[string]any `json:"unsubscribe_sent"`
		Limits          map[string]any `json:"limits"`
		Gap             *string        `json:"gap"`
		Error           *struct {
			Class     string `json:"class"`
			Code      string `json:"code"`
			AccountID string `json:"account_id"`
		} `json:"error"`
	} `json:"expect"`
}

type seenWS struct {
	WS       bool              `json:"ws"`
	Query    map[string]string `json:"query"`
	Headers  map[string]string `json:"headers"`
	Messages []map[string]any  `json:"messages"`
}

func TestStreams(t *testing.T) {
	for _, sc := range S.Streams {
		t.Run(sc.Name, func(t *testing.T) {
			body, _ := json.Marshal(map[string]any{"connections": sc.Connections})
			res, err := http.Post(mockURL+"/__scenario", "application/json", bytes.NewReader(body))
			if err != nil {
				t.Fatal(err)
			}
			res.Body.Close()
			ws := "ws" + strings.TrimPrefix(mockURL, "http")
			c, _ := NewClient(WithAPIKey(S.Key), WithBaseURL(mockURL), WithStreamURLs(ws+"/v1/stream", ws+"/v1/market-data"))
			ctx, cancel := context.WithTimeout(context.Background(), 15*time.Second)
			defer cancel()
			x := sc.Expect
			switch sc.Stream {
			case "account":
				var mu sync.Mutex
				gaps := []string{}
				s := c.AccountStream(ctx, AccountStreamOptions{OnGap: func(after string) {
					mu.Lock()
					gaps = append(gaps, after)
					mu.Unlock()
				}})
				ids := []string{}
				var err error
				for len(ids) < len(x.Events) {
					e, nerr := s.Next(ctx)
					if nerr != nil {
						err = nerr
						break
					}
					ids = append(ids, e.ID)
				}
				if x.Error != nil && err == nil {
					_, err = s.Next(ctx)
					time.Sleep(500 * time.Millisecond) // time enough for a wrong reconnect to show
				}
				s.Close()
				if x.Events != nil && !reflect.DeepEqual(ids, x.Events) {
					t.Fatalf("events %v, want %v", ids, x.Events)
				}
				if x.Error != nil {
					var e *Error
					if !errors.As(err, &e) || (x.Error.Class != "" && string(e.Kind) != x.Error.Class) || e.Code != x.Error.Code {
						t.Fatalf("error %v, want %s %s", err, x.Error.Class, x.Error.Code)
					}
					var details struct {
						AccountID string `json:"account_id"`
					}
					_ = json.Unmarshal(e.Details, &details)
					if x.Error.AccountID != "" && details.AccountID != x.Error.AccountID {
						t.Fatalf("error details %s, want the account %s", e.Details, x.Error.AccountID)
					}
				}
				mu.Lock()
				defer mu.Unlock()
				if x.Gap != nil && !slices.Contains(gaps, *x.Gap) {
					t.Fatalf("gaps told %v, want one after %s", gaps, *x.Gap)
				}
			case "market":
				m := c.MarketStream(ctx)
				if _, err := m.Subscribe(ctx, sc.Subscribe.Channel, sc.Subscribe.Symbols, sc.Subscribe.Params); err != nil {
					t.Fatal(err)
				}
				if u := sc.Unsubscribe; u != nil {
					if _, err := m.Unsubscribe(ctx, u.Channel, u.Symbols, u.Params); err != nil {
						t.Fatal(err)
					}
				}
				got := []string{}
				for len(got) < len(x.Frames) {
					f, err := m.Next(ctx)
					if err != nil {
						t.Fatal(err)
					}
					if f.Type == sc.Subscribe.Channel {
						var d struct {
							Last string `json:"last"`
						}
						_ = json.Unmarshal(f.Data, &d)
						got = append(got, d.Last)
					}
				}
				var limits map[string]any
				_ = json.Unmarshal(m.Limits(), &limits)
				m.Close()
				if !reflect.DeepEqual(got, x.Frames) {
					t.Fatalf("frames %v, want %v", got, x.Frames)
				}
				if x.Limits != nil && !reflect.DeepEqual(limits, x.Limits) {
					t.Fatalf("limits %v, want %v", limits, x.Limits)
				}
			default:
				t.Fatalf("no stream %q in this runner", sc.Stream)
			}
			resp, err := http.Get(mockURL + "/__requests")
			if err != nil {
				t.Fatal(err)
			}
			var reqs []seenWS
			_ = json.NewDecoder(resp.Body).Decode(&reqs)
			resp.Body.Close()
			if len(reqs) != x.Connections {
				t.Fatalf("connections %d, want %d", len(reqs), x.Connections)
			}
			for i, want := range x.Resume {
				got, ok := reqs[i].Query["resume"]
				if (want == nil) == ok || (want != nil && got != *want) {
					t.Fatalf("connection %d resume %q (%v), want %v", i, got, ok, want)
				}
			}
			if reqs[0].Headers["authorization"] != "Bearer "+S.Key {
				t.Fatal("no key on the upgrade")
			}
			if x.FirstMessage != nil && (len(reqs[0].Messages) == 0 || !reflect.DeepEqual(pick(reqs[0].Messages[0], x.FirstMessage), any(x.FirstMessage))) {
				t.Fatalf("first message %v", reqs[0].Messages)
			}
			if x.Resubscribe != nil {
				last := reqs[len(reqs)-1]
				if len(last.Messages) == 0 || !reflect.DeepEqual(pick(last.Messages[0], x.Resubscribe), any(x.Resubscribe)) {
					t.Fatalf("resubscribe %v, want %v", last.Messages, x.Resubscribe)
				}
			}
			if x.UnsubscribeSent != nil {
				var sent map[string]any
				for _, r := range reqs {
					for _, msg := range r.Messages {
						if msg["op"] == "unsubscribe" && sent == nil {
							sent = msg
						}
					}
				}
				if !reflect.DeepEqual(pick(sent, x.UnsubscribeSent), any(x.UnsubscribeSent)) {
					t.Fatalf("unsubscribe %v, want %v", sent, x.UnsubscribeSent)
				}
				if _, extra := sent["interval_ms"]; extra { // an unsubscribe names the markets and a candle's interval only
					t.Fatalf("unsubscribe %v carries the subscribe's params", sent)
				}
			}
		})
	}
}
