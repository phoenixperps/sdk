package phoenixperps

// The streams against scripted connections of the conformance mock (its { connections } control): a refused account
// subscribe, account ping and unsubscribe across a reconnect, risk frames and gaps told, a market connection left half
// open, unsubscribes without their params, list and ping, a refused upgrade.

import (
	"bytes"
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"log/slog"
	"net/http"
	"reflect"
	"strings"
	"testing"
	"time"
)

// scripted loads connection scripts into the mock and makes a client whose streams go to it.
func scripted(t *testing.T, connections string, opts ...ClientOption) *Client {
	t.Helper()
	res, err := http.Post(mockURL+"/__scenario", "application/json", bytes.NewReader([]byte(`{"connections":`+connections+`}`)))
	if err != nil {
		t.Fatal(err)
	}
	res.Body.Close()
	ws := "ws" + strings.TrimPrefix(mockURL, "http")
	c, err := NewClient(append([]ClientOption{WithAPIKey(S.Key), WithBaseURL(mockURL), WithStreamURLs(ws+"/v1/stream", ws+"/v1/market-data")}, opts...)...)
	if err != nil {
		t.Fatal(err)
	}
	return c
}

// accountEvent is an event frame of the account stream, as the server sends it.
func accountEvent(id string, seq int, replayed bool) string {
	return `{"send":{"type":"event","seq":` + fmt.Sprint(seq) + `,"replayed":` + fmt.Sprint(replayed) + `,"event":{"id":"` + id + `","type":"order.created","account_id":"SANDBOX-00042-001","time":1791460800000,"source":"api","data":{}}}}`
}

// Ping and Unsubscribe ask the server; an account unsubscribed from a stream following every account stays out after
// a reconnect (the subscribe names the hello's accounts less it); each event keeps its frame's seq and replayed.
func TestAccountStreamPingUnsubscribe(t *testing.T) {
	hello := func(accounts string) string {
		return `{"send":{"type":"hello","session":"ses_1","environment":"sandbox","heartbeat_ms":15000,"accounts":[` + accounts + `]}}`
	}
	cl := scripted(t, `[[`+hello(`"SANDBOX-00042-001","SANDBOX-00042-002"`)+`,{"expect":"subscribe"},{"send":{"type":"snapshot_end","accounts":[]}},`+accountEvent("evt_1", 1, false)+`,
		{"expect":"ping"},{"send":{"type":"pong","time":1791460800000}},
		{"expect":"unsubscribe"},{"send":{"type":"unsubscribed","accounts":["SANDBOX-00042-002"]}},{"close":1012}],
		[`+hello(`"SANDBOX-00042-001","SANDBOX-00042-002","SANDBOX-00042-003"`)+`,{"expect":"subscribe"},`+accountEvent("evt_2", 1, true)+`,{"wait_ms":3000}]]`)
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	s := cl.AccountStream(ctx, AccountStreamOptions{})
	defer s.Close()
	if e, err := s.Next(ctx); err != nil || e.ID != "evt_1" || e.Seq != 1 || e.Replayed {
		t.Fatalf("event %+v, %v", e, err)
	}
	if pong, err := s.Ping(ctx); err != nil || !strings.Contains(string(pong), `"pong"`) {
		t.Fatalf("pong %s, %v", pong, err)
	}
	if reply, err := s.Unsubscribe(ctx, []string{"SANDBOX-00042-002"}); err != nil || !strings.Contains(string(reply), `"unsubscribed"`) {
		t.Fatalf("unsubscribed %s, %v", reply, err)
	}
	if e, err := s.Next(ctx); err != nil || e.ID != "evt_2" || e.Seq != 1 || !e.Replayed {
		t.Fatalf("event %+v, %v", e, err)
	}
	got := connections(t)
	if len(got) != 2 || !reflect.DeepEqual(got[0].Messages[0], map[string]any{"op": "subscribe"}) {
		t.Fatalf("connections %+v", got)
	}
	if want := map[string]any{"op": "subscribe", "accounts": []any{"SANDBOX-00042-001", "SANDBOX-00042-003"}}; !reflect.DeepEqual(got[1].Messages[0], want) {
		t.Fatalf("resubscribe %v, want %v", got[1].Messages[0], want)
	}
	// with the accounts named: what is left of them (none left: an empty list, which follows nothing)
	named := &AccountStream{opts: AccountStreamOptions{Accounts: []string{"SANDBOX-00042-001", "SANDBOX-00042-002"}, Risk: true}, left: map[string]bool{"SANDBOX-00042-002": true}}
	if msg := string(named.subscribe(nil)); msg != `{"accounts":["SANDBOX-00042-001"],"op":"subscribe","risk":true}` {
		t.Fatal(msg)
	}
	named.left["SANDBOX-00042-001"] = true
	if msg := string(named.subscribe(nil)); msg != `{"accounts":[],"op":"subscribe","risk":true}` {
		t.Fatal(msg)
	}
}

// Risk frames go to OnRisk as they come; a 4008 is a gap after the last event told, reconnecting without resume; each
// reconnect is one line in the client's log, with its cause.
func TestAccountStreamRiskAndGap(t *testing.T) {
	var log bytes.Buffer
	hello := `{"send":{"type":"hello","session":"ses_1","environment":"sandbox","heartbeat_ms":15000,"accounts":["SANDBOX-00042-001"]}},{"expect":"subscribe"},{"send":{"type":"snapshot_end","accounts":[]}}`
	cl := scripted(t, `[[`+hello+`,`+accountEvent("evt_1", 1, false)+`,{"send":{"type":"risk","account_id":"SANDBOX-00042-001","data":{"as_of":1,"equity":"10012.5"}}},{"close":4008}],
		[`+hello+`,`+accountEvent("evt_2", 1, false)+`,{"wait_ms":3000}]]`, WithLogger(slog.New(slog.NewTextHandler(&log, nil))))
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	risks, gaps := make(chan string, 4), make(chan string, 4)
	s := cl.AccountStream(ctx, AccountStreamOptions{Risk: true,
		OnRisk: func(accountID string, r *AccountRisk) { risks <- accountID + " " + string(r.Equity) },
		OnGap:  func(after string) { gaps <- after }})
	for _, want := range []string{"evt_1", "evt_2"} {
		if e, err := s.Next(ctx); err != nil || e.ID != want {
			t.Fatalf("event %+v, %v, want %s", e, err, want)
		}
	}
	s.Close()
	if r := <-risks; r != "SANDBOX-00042-001 10012.5" {
		t.Fatalf("risk %q", r)
	}
	if g := <-gaps; g != "evt_1" {
		t.Fatalf("gap after %q", g)
	}
	got := connections(t)
	if len(got) != 2 || got[0].Messages[0]["risk"] != true || got[1].Query["resume"] != "" {
		t.Fatalf("connections %+v", got)
	}
	if text := log.String(); !strings.Contains(text, `msg="phoenixperps: the account stream reconnects" cause="closed with 4008"`) || !strings.Contains(text, "has a gap") {
		t.Fatalf("log %s", text)
	}
}

func connections(t *testing.T) []seenWS {
	t.Helper()
	res, err := http.Get(mockURL + "/__requests")
	if err != nil {
		t.Fatal(err)
	}
	defer res.Body.Close()
	var got []seenWS
	if err := json.NewDecoder(res.Body).Decode(&got); err != nil {
		t.Fatal(err)
	}
	return got
}

// A refused subscribe ends the account stream with a ValidationError and closes Ready, even with no snapshot_end
// (too_many_accounts); it is never sent again.
func TestAccountStreamRefused(t *testing.T) {
	for _, c := range []struct{ frames, code, details string }{
		{`{"send":{"type":"error","code":"too_many_accounts","details":{"max":50}}}`, "too_many_accounts", `{"max":50}`},
		{`{"send":{"type":"error","code":"account_not_found","account_id":"SANDBOX-00042-099"}},{"send":{"type":"snapshot_end","accounts":[]}}`, "account_not_found", `{"account_id":"SANDBOX-00042-099"}`},
	} {
		t.Run(c.code, func(t *testing.T) {
			cl := scripted(t, `[[{"send":{"type":"hello","heartbeat_ms":15000}},{"expect":"subscribe"},`+c.frames+`,{"wait_ms":3000}]]`)
			ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
			defer cancel()
			s := cl.AccountStream(ctx, AccountStreamOptions{Accounts: []string{"SANDBOX-00042-099"}})
			defer s.Close()
			select {
			case <-s.Ready():
			case <-ctx.Done():
				t.Fatal("Ready is never closed")
			}
			_, err := s.Next(ctx)
			var e *Error
			if !errors.As(err, &e) || !errors.Is(err, ErrValidation) || e.Code != c.code || string(e.Details) != c.details {
				t.Fatalf("got %v (%+v), want ValidationError %s %s", err, e, c.code, c.details)
			}
			time.Sleep(500 * time.Millisecond) // time enough for a wrong reconnect to show
			if n := len(connections(t)); n != 1 {
				t.Fatalf("connections %d, want 1", n)
			}
		})
	}
}

// Three heartbeats without a frame: the market stream drops the half-open connection and subscribes again on a new one.
func TestMarketStreamSilence(t *testing.T) {
	sub := `{"expect":"subscribe"},{"reply":{"type":"subscribed","channel":"ticker","symbols":["BTC-USD"],"rejected":[]}}`
	cl := scripted(t, `[[{"send":{"type":"hello","heartbeat_ms":100}},`+sub+`],
		[{"send":{"type":"hello","heartbeat_ms":15000}},`+sub+`,{"send":{"type":"ticker","symbol":"BTC-USD","data":{"last":"62000.2"}}},{"wait_ms":3000}]]`)
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	m := cl.MarketStream(ctx)
	defer m.Close()
	if _, err := m.Subscribe(ctx, "ticker", []string{"BTC-USD"}, nil); err != nil {
		t.Fatal(err)
	}
	f, err := m.Next(ctx)
	if err != nil || f.Type != "ticker" || !strings.Contains(string(f.Data), "62000.2") {
		t.Fatalf("frame %+v, %v", f, err)
	}
	if n := len(connections(t)); n != 2 {
		t.Fatalf("connections %d, want 2", n)
	}
}

// An unsubscribe without the subscribe's params forgets the symbol all the same, so a reconnect makes again only what
// the server still holds; a candle's unsubscribe carries its interval.
func TestMarketStreamUnsubscribe(t *testing.T) {
	ok := func(op, symbols string) string {
		return `{"expect":"` + op + `"},{"reply":{"type":"` + op + `d","channel":"x","symbols":[` + symbols + `],"rejected":[]}}`
	}
	btc, eth := `"BTC-USD"`, `"ETH-USD"`
	cl := scripted(t, `[[{"send":{"type":"hello","heartbeat_ms":15000}},`+ok("subscribe", btc+","+eth)+`,`+ok("subscribe", btc)+`,`+ok("subscribe", btc)+`,`+ok("unsubscribe", eth)+`,`+ok("unsubscribe", btc)+`,{"close":1012}],
		[{"send":{"type":"hello","heartbeat_ms":15000}},`+ok("subscribe", btc)+`,{"send":{"type":"ticker","symbol":"BTC-USD","data":{"last":"62000.2"}}},{"wait_ms":3000}]]`)
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	m := cl.MarketStream(ctx)
	defer m.Close()
	for _, x := range []struct {
		op, channel string
		symbols     []string
		params      map[string]any
	}{
		{"subscribe", "ticker", []string{"BTC-USD", "ETH-USD"}, map[string]any{"interval_ms": 1000}},
		{"subscribe", "ticker", []string{"btc-usd"}, map[string]any{"interval_ms": 250}}, // BTC moves to 250
		{"subscribe", "candles", []string{"BTC-USD"}, map[string]any{"interval": "1m", "history": 0}},
		{"unsubscribe", "ticker", []string{"ETH-USD"}, nil},
		{"unsubscribe", "candles", []string{"BTC-USD"}, map[string]any{"interval": "1m"}},
	} {
		call := m.Subscribe
		if x.op == "unsubscribe" {
			call = m.Unsubscribe
		}
		if _, err := call(ctx, x.channel, x.symbols, x.params); err != nil {
			t.Fatalf("%s %s: %v", x.op, x.channel, err)
		}
	}
	if f, err := m.Next(ctx); err != nil || f.Type != "ticker" {
		t.Fatalf("frame %+v, %v", f, err)
	}
	time.Sleep(200 * time.Millisecond) // a second resubscribe would be in by now
	got := connections(t)
	if len(got) != 2 {
		t.Fatalf("connections %d, want 2", len(got))
	}
	if iv := got[0].Messages[4]["interval"]; iv != "1m" {
		t.Fatalf("candles unsubscribe %v", got[0].Messages[4])
	}
	want := []map[string]any{{"op": "subscribe", "channel": "ticker", "symbols": []any{"BTC-USD"}, "interval_ms": float64(250)}}
	for _, msg := range got[1].Messages {
		delete(msg, "id")
	}
	if !reflect.DeepEqual(got[1].Messages, want) {
		t.Fatalf("resubscribe %v, want %v", got[1].Messages, want)
	}
}

// An unsubscribe ends what the server ends: candles without an interval every interval, and naming no market every
// market of the channel; a reconnect makes again only what is left.
func TestMarketStreamUnsubscribeEvery(t *testing.T) {
	ok := func(op, symbols string) string {
		return `{"expect":"` + op + `"},{"reply":{"type":"` + op + `d","channel":"x","symbols":[` + symbols + `],"rejected":[]}}`
	}
	btc, both := `"BTC-USD"`, `"BTC-USD","ETH-USD"`
	cl := scripted(t, `[[{"send":{"type":"hello","heartbeat_ms":15000}},`+ok("subscribe", btc)+`,`+ok("subscribe", btc)+`,`+ok("subscribe", both)+`,`+ok("subscribe", btc)+`,`+ok("unsubscribe", btc)+`,`+ok("unsubscribe", both)+`,{"close":1012}],
		[{"send":{"type":"hello","heartbeat_ms":15000}},`+ok("subscribe", btc)+`,{"send":{"type":"trades","symbol":"BTC-USD","data":[]}},{"wait_ms":3000}]]`)
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()
	m := cl.MarketStream(ctx)
	defer m.Close()
	for _, x := range []struct {
		op, channel string
		symbols     []string
		params      map[string]any
	}{
		{"subscribe", "candles", []string{"BTC-USD"}, map[string]any{"interval": "1m"}},
		{"subscribe", "candles", []string{"BTC-USD"}, map[string]any{"interval": "5m"}},
		{"subscribe", "ticker", []string{"BTC-USD", "ETH-USD"}, nil},
		{"subscribe", "trades", []string{"BTC-USD"}, nil},
		{"unsubscribe", "candles", []string{"BTC-USD"}, nil}, // every interval
		{"unsubscribe", "ticker", nil, nil},                  // every market
	} {
		call := m.Subscribe
		if x.op == "unsubscribe" {
			call = m.Unsubscribe
		}
		if _, err := call(ctx, x.channel, x.symbols, x.params); err != nil {
			t.Fatalf("%s %s: %v", x.op, x.channel, err)
		}
	}
	if f, err := m.Next(ctx); err != nil || f.Type != "trades" {
		t.Fatalf("frame %+v, %v", f, err)
	}
	got := connections(t)
	if _, named := got[0].Messages[4]["interval"]; named || len(got[0].Messages[5]["symbols"].([]any)) != 0 {
		t.Fatalf("unsubscribes %v %v", got[0].Messages[4], got[0].Messages[5])
	}
	delete(got[1].Messages[0], "id")
	if want := map[string]any{"op": "subscribe", "channel": "trades", "symbols": []any{"BTC-USD"}}; len(got) != 2 || len(got[1].Messages) != 1 || !reflect.DeepEqual(got[1].Messages[0], want) {
		t.Fatalf("resubscribe %v, want %v", got[1].Messages, want)
	}
}

// An upgrade refused with another status than 429 or 5xx ends the market stream with the API's own error; a client
// without a key sends no Authorization.
func TestMarketStreamRefused(t *testing.T) {
	t.Setenv("PHOENIXPERPS_API_KEY", "")
	scripted(t, `[[{"refuse":403,"body":{"error":{"type":"permission","code":"insufficient_permission","message":"x","retryable":false}}}]]`) // the script; a keyless client plays it
	ws := "ws" + strings.TrimPrefix(mockURL, "http")
	cl, err := NewClient(WithBaseURL(mockURL), WithStreamURLs("", ws+"/v1/market-data"))
	if err != nil {
		t.Fatal(err)
	}
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	m := cl.MarketStream(ctx)
	defer m.Close()
	_, err = m.Subscribe(ctx, "ticker", []string{"BTC-USD"}, nil)
	if !errors.Is(err, &Error{Kind: KindPermission, Code: ErrCodeInsufficientPermission}) {
		t.Fatalf("got %v", err)
	}
	if got := connections(t); len(got) != 1 || got[0].Headers["authorization"] != "" {
		t.Fatalf("connections %+v", got)
	}
}

// List and Ping ask the server; a candles frame tells its interval.
func TestMarketStreamListPing(t *testing.T) {
	cl := scripted(t, `[[{"send":{"type":"hello","heartbeat_ms":15000}},
		{"expect":"list"},{"reply":{"type":"list","subscriptions":[{"channel":"candles","symbol":"BTC-USD","interval":"5m"}]}},
		{"expect":"ping"},{"reply":{"type":"pong","time":1791460800000}},
		{"send":{"type":"candles","symbol":"BTC-USD","interval":"5m","data":[]}},{"wait_ms":3000}]]`)
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	m := cl.MarketStream(ctx)
	defer m.Close()
	list, err := m.List(ctx)
	if err != nil || !strings.Contains(string(list), `"interval":"5m"`) {
		t.Fatalf("list %s, %v", list, err)
	}
	pong, err := m.Ping(ctx)
	if err != nil || !strings.Contains(string(pong), `"pong"`) {
		t.Fatalf("pong %s, %v", pong, err)
	}
	if f, err := m.Next(ctx); err != nil || f.Interval != "5m" {
		t.Fatalf("frame %+v, %v", f, err)
	}
	if msg := connections(t)[0].Messages[0]; !reflect.DeepEqual(msg, map[string]any{"op": "list", "id": float64(1)}) {
		t.Fatalf("list request %v", msg)
	}
}
