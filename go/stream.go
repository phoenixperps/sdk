package phoenixperps

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"iter"
	"log/slog"
	"math"
	mrand "math/rand/v2"
	"net/http"
	"net/url"
	"sort"
	"strings"
	"sync"
	"time"

	"github.com/coder/websocket"
)

/*
The two read-only streams (docs/BEHAVIOUR.md "Streams"; orders always go through REST).

AccountStream: the key on the upgrade; hello, one snapshot per account, snapshot_end (Ready), then events, each once
(replays are told apart by their event id) with the frame's seq and replayed. It reconnects by itself with
?resume=<last event id>, waiting min(30 s, 0.5 s x 2^n) with jitter (0 to 3 s after 1012, the server restarting).
4001 (the key ended) ends it with an AuthenticationError; 4008 (too far behind) reconnects without resume; 4013 waits
longer. A resume the server cannot cover (its resume_gap notice) and a 4008 are gaps, told to OnGap. An error frame
refuses the subscribe (too_many_accounts, account_not_found): sent again as is it would be again, so it ends the stream
with a ValidationError. Risk frames (Risk set) go to OnRisk as they come. Ping and Unsubscribe ask the server; an
account unsubscribed is left out of the subscribe a reconnect sends. Three heartbeats without a frame mean a dead
connection.

MarketStream: Subscribe(ctx, "ticker", []string{"BTC-USD"}, map[string]any{"interval_ms": 250}) answers once the server
took it; every subscription the server still holds is made again after a reconnect (it knows one by channel, symbol and
a candle's interval: a symbol subscribed again moves to its new params; Unsubscribe needs no params, and without a
candle's interval ends every interval). Three heartbeats without a frame mean a dead connection. A slow reader loses
the oldest frames, never the connection.

Both: an upgrade refused with 429 or 5xx, or a network failure, is tried again after its Retry-After (else the
backoff); any other refusal ends the stream with the API's own error, as a REST call would. Each reconnect is one line
in the client's log, with its cause.
*/

const lruSize = 2048

func backoff(n int) time.Duration {
	return time.Duration(mrand.Float64() * math.Min(30000, 500*math.Pow(2, float64(n))) * float64(time.Millisecond))
}

func (c *Client) streamHeader() http.Header {
	h := http.Header{}
	if c.key != "" {
		h.Set("Authorization", "Bearer "+c.key)
	}
	h.Set("User-Agent", c.UserAgent())
	return h
}

// dialFailed reads an upgrade that failed: a network failure, 429 or 5xx is tried again after the Retry-After (else
// the backoff), with its cause; any other refusal is the API's own error, which ends the stream.
func dialFailed(resp *http.Response, err error, attempt int) (time.Duration, string, *Error) {
	if resp == nil || resp.StatusCode == http.StatusSwitchingProtocols {
		return backoff(attempt), clip(err.Error()), nil
	}
	var body []byte
	if resp.Body != nil { // the first KB of the refusal (the API's envelope)
		body, _ = io.ReadAll(resp.Body)
	}
	e := errorFrom(resp.StatusCode, resp.Header, body, "")
	if resp.StatusCode != http.StatusTooManyRequests && resp.StatusCode < 500 {
		return 0, "", e
	}
	wait, given := retryAfter(resp.Header, time.Now())
	if !given {
		wait = backoff(attempt)
	}
	return wait, fmt.Sprintf("the upgrade was refused (%d %s)", resp.StatusCode, e.Code), nil
}

// why is the cause of a connection's end, for the log.
func why(err error) string {
	if code := websocket.CloseStatus(err); code != -1 {
		return fmt.Sprintf("closed with %d", code)
	}
	if errors.Is(err, context.DeadlineExceeded) {
		return "no frame in three heartbeats"
	}
	return clip(err.Error())
}

func closedError() *Error {
	return &Error{Kind: KindStreamClosed, Code: "stream_closed", Message: "The stream is closed."}
}

func isClosed(err error) bool {
	e, ok := err.(*Error)
	return ok && e.Kind == KindStreamClosed
}

// AccountStreamOptions chooses what an AccountStream follows, and who hears its risk frames and its gaps.
type AccountStreamOptions struct {
	Accounts []string // the accounts to follow (none: every account of the key)
	Risk     bool     // live risk frames too
	Resume   string   // start after this event id
	// OnRisk hears each risk frame as it comes (with Risk set), on the stream's goroutine: return quickly.
	OnRisk func(accountID string, risk *AccountRisk)
	// OnGap hears a gap: events after this id may be missing (a resume the server could not cover, or a 4008); read
	// them with client.Events.List after it. It runs on the stream's goroutine, before the events that follow; the
	// client's log has a warning either way.
	OnGap func(after string)
}

// StreamEvent is an event as the account stream told it: the event, the frame's seq (this connection's count, without
// a gap) and replayed (sent again after a resume).
type StreamEvent struct {
	Event
	Seq      int64 `json:"seq"`
	Replayed bool  `json:"replayed,omitempty"`
}

// AccountStream is the account stream: snapshots, then every event of the accounts, each once.
type AccountStream struct {
	c      *Client
	opts   AccountStreamOptions
	ctx    context.Context
	cancel context.CancelFunc
	items  chan StreamEvent
	ready  chan struct{}
	once   sync.Once
	done   chan struct{}

	mu        sync.Mutex
	snapshots map[string]json.RawMessage
	risks     map[string]json.RawMessage
	lastEvent string
	seen      map[string]struct{}
	order     []string
	err       error
	conn      *websocket.Conn
	open      chan struct{}                     // closed once a connection sent its subscribe
	left      map[string]bool                   // the accounts unsubscribed: never named again
	waiting   map[string][]chan json.RawMessage // the replies asked for (pong, unsubscribed), the first asked first
}

// AccountStream connects to the account stream; it runs until Close (or until ctx ends).
func (c *Client) AccountStream(ctx context.Context, opts AccountStreamOptions) *AccountStream {
	sctx, cancel := context.WithCancel(ctx)
	s := &AccountStream{c: c, opts: opts, ctx: sctx, cancel: cancel, items: make(chan StreamEvent, 1024), ready: make(chan struct{}), done: make(chan struct{}),
		snapshots: map[string]json.RawMessage{}, risks: map[string]json.RawMessage{}, lastEvent: opts.Resume, seen: map[string]struct{}{},
		open: make(chan struct{}), left: map[string]bool{}, waiting: map[string][]chan json.RawMessage{}}
	go s.run()
	return s
}

// Ready is closed once the first snapshots are in, or once the stream ended (Next tells why).
func (s *AccountStream) Ready() <-chan struct{} { return s.ready }

// Snapshot is the last snapshot frame of an account (nil before it came).
func (s *AccountStream) Snapshot(accountID string) json.RawMessage {
	s.mu.Lock()
	defer s.mu.Unlock()
	return s.snapshots[accountID]
}

// Risk is the last risk frame of an account (with Risk set).
func (s *AccountStream) Risk(accountID string) json.RawMessage {
	s.mu.Lock()
	defer s.mu.Unlock()
	return s.risks[accountID]
}

// LastEventID is the id of the last event told (the resume point).
func (s *AccountStream) LastEventID() string {
	s.mu.Lock()
	defer s.mu.Unlock()
	return s.lastEvent
}

// Next is the next event. After Close it answers a StreamClosedError; after 4001 an AuthenticationError; after a
// refused subscribe a ValidationError (its code, and its Details with the account_id it names); after a refused
// upgrade the API's own error.
func (s *AccountStream) Next(ctx context.Context) (StreamEvent, error) {
	select {
	case e, ok := <-s.items:
		if ok {
			return e, nil
		}
		return StreamEvent{}, s.endError()
	case <-ctx.Done():
		return StreamEvent{}, ctx.Err()
	}
}

// Events goes over the events until the stream ends (an error that ended it is yielded once).
func (s *AccountStream) Events() iter.Seq2[StreamEvent, error] {
	return func(yield func(StreamEvent, error) bool) {
		for {
			e, err := s.Next(context.Background())
			if err != nil {
				if !isClosed(err) {
					yield(StreamEvent{}, err)
				}
				return
			}
			if !yield(e, nil) {
				return
			}
		}
	}
}

// Ping answers the server's pong ({"type": "pong", "time": ...}), asked again on the next connection when this one
// drops first.
func (s *AccountStream) Ping(ctx context.Context) (json.RawMessage, error) {
	return s.ask(ctx, []byte(`{"op":"ping"}`), "pong", true)
}

// Unsubscribe stops following accounts, after a reconnect too (the subscribe it sends names what is left). It answers
// the server's reply ({"type": "unsubscribed", "accounts": [...]}), or nil when the connection dropped first.
func (s *AccountStream) Unsubscribe(ctx context.Context, accounts []string) (json.RawMessage, error) {
	s.mu.Lock()
	for _, a := range accounts {
		s.left[strings.ToUpper(a)] = true
	}
	s.mu.Unlock()
	msg, _ := json.Marshal(map[string]any{"op": "unsubscribe", "accounts": accounts})
	return s.ask(ctx, msg, "unsubscribed", false)
}

// ask sends a request on the connection once it is up and answers its reply. The account protocol's requests carry no
// id: a reply is the next frame of its type. When the connection drops first, it is asked again on the next one
// (again), or answers nil.
func (s *AccountStream) ask(ctx context.Context, msg []byte, reply string, again bool) (json.RawMessage, error) {
	for {
		s.mu.Lock()
		open := s.open
		s.mu.Unlock()
		select {
		case <-open:
		case <-ctx.Done():
			return nil, ctx.Err()
		case <-s.done:
			return nil, s.endError()
		}
		s.mu.Lock()
		conn := s.conn
		if conn == nil || s.open != open { // it dropped meanwhile: wait for the next connection
			s.mu.Unlock()
			continue
		}
		ch := make(chan json.RawMessage, 1)
		s.waiting[reply] = append(s.waiting[reply], ch)
		s.mu.Unlock()
		if err := conn.Write(ctx, websocket.MessageText, msg); err != nil && ctx.Err() != nil {
			return nil, ctx.Err()
		}
		select {
		case r, ok := <-ch:
			if ok {
				return r, nil
			}
			if !again {
				return nil, nil
			}
		case <-ctx.Done():
			return nil, ctx.Err()
		case <-s.done:
			return nil, s.endError()
		}
	}
}

// subscribe is the subscribe a connection sends: the accounts asked for (none: every account of the key) less the
// ones unsubscribed since, then named from reach (the hello's accounts); s.mu is held.
func (s *AccountStream) subscribe(reach []string) []byte {
	msg := map[string]any{"op": "subscribe"}
	accounts := s.opts.Accounts
	if len(s.left) > 0 {
		if len(accounts) == 0 {
			accounts = reach
		}
		kept := []string{} // none left: an empty list follows nothing
		for _, a := range accounts {
			if !s.left[strings.ToUpper(a)] {
				kept = append(kept, a)
			}
		}
		msg["accounts"] = kept
	} else if len(accounts) > 0 {
		msg["accounts"] = accounts
	}
	if s.opts.Risk {
		msg["risk"] = true
	}
	b, _ := json.Marshal(msg)
	return b
}

// gap tells that events after this one may be missing: OnGap, and a warning in the client's log.
func (s *AccountStream) gap(after string) {
	s.c.log(slog.LevelWarn, "phoenixperps: the account stream has a gap: GET /v1/events?after= it has what is missing", "after", after)
	if s.opts.OnGap != nil {
		s.opts.OnGap(after)
	}
}

// Close ends the stream.
func (s *AccountStream) Close() error {
	s.cancel()
	<-s.done
	return nil
}

func (s *AccountStream) endError() error {
	s.mu.Lock()
	defer s.mu.Unlock()
	if s.err != nil {
		return s.err
	}
	return closedError()
}

func (s *AccountStream) fail(err error) {
	s.mu.Lock()
	s.err = err
	s.mu.Unlock()
}

func (s *AccountStream) run() {
	defer close(s.done)
	defer close(s.items)
	defer s.once.Do(func() { close(s.ready) }) // a stream that ended is never ready later
	attempt, resume := 0, true
	for s.ctx.Err() == nil {
		u := s.c.streams["account"]
		if last := s.LastEventID(); resume && last != "" {
			u += "?resume=" + url.QueryEscape(last)
		}
		var wait time.Duration
		var cause string
		conn, resp, err := websocket.Dial(s.ctx, u, &websocket.DialOptions{HTTPHeader: s.c.streamHeader()})
		if err != nil {
			var end *Error
			if wait, cause, end = dialFailed(resp, err, attempt); end != nil {
				s.fail(end)
				return
			}
		} else {
			var stop bool
			wait, resume, stop, cause = s.read(conn, &attempt)
			if stop {
				return
			}
		}
		if s.ctx.Err() != nil {
			return
		}
		s.c.log(slog.LevelInfo, "phoenixperps: the account stream reconnects", "cause", cause, "wait_ms", wait.Milliseconds())
		attempt++
		if sleep(s.ctx, wait) != nil {
			return
		}
	}
}

type streamFrame struct {
	Type        string          `json:"type"`
	ID          *int64          `json:"id"`
	HeartbeatMs int64           `json:"heartbeat_ms"`
	AccountID   string          `json:"account_id"`
	Accounts    []string        `json:"accounts"`
	Event       json.RawMessage `json:"event"`
	Seq         int64           `json:"seq"`
	Replayed    bool            `json:"replayed"`
	Data        json.RawMessage `json:"data"`
	After       string          `json:"after"`
	Limits      json.RawMessage `json:"limits"`
	Code        string          `json:"code"`
	Message     string          `json:"message"`
	Details     json.RawMessage `json:"details"`
}

// refusal is an error frame of either stream as a ValidationError: its code and details, with the account it names.
func (f *streamFrame) refusal() *Error {
	code := firstOf(f.Code, "stream_refused")
	e := &Error{Kind: KindValidation, Code: code, Message: firstOf(f.Message, code)}
	if len(f.Details) > 0 && string(f.Details) != "null" {
		e.Details = f.Details
	}
	if f.AccountID != "" {
		d := map[string]any{}
		_ = json.Unmarshal(e.Details, &d)
		d["account_id"] = f.AccountID
		e.Details, _ = json.Marshal(d)
	}
	return e
}

// read follows one connection; it answers the wait before the next one, whether to resume, whether to stop, and why
// the connection ended.
func (s *AccountStream) read(conn *websocket.Conn, attempt *int) (time.Duration, bool, bool, string) {
	defer func() {
		conn.CloseNow()
		s.mu.Lock()
		s.conn = nil
		s.open = make(chan struct{})
		for reply, chs := range s.waiting { // asked on this connection: Ping asks again, Unsubscribe is done
			for _, ch := range chs {
				close(ch)
			}
			delete(s.waiting, reply)
		}
		s.mu.Unlock()
	}()
	conn.SetReadLimit(4 << 20)
	heartbeat := 15 * time.Second
	for {
		rctx, cancel := context.WithTimeout(s.ctx, 3*heartbeat)
		_, data, err := conn.Read(rctx)
		cancel()
		if err != nil {
			if s.ctx.Err() != nil {
				return 0, true, true, ""
			}
			switch websocket.CloseStatus(err) {
			case 4001:
				s.fail(&Error{Kind: KindAuthentication, Code: "stream_credential_ended", Message: "The key or session of this stream ended."})
				return 0, false, true, ""
			case 4008: // too far behind: what was not sent is a gap
				s.gap(s.LastEventID())
				return backoff(*attempt), false, false, why(err)
			case 4013:
				return 30*time.Second + backoff(*attempt), true, false, why(err)
			case 1012:
				return time.Duration(mrand.Float64() * 3 * float64(time.Second)), true, false, why(err)
			}
			return backoff(*attempt), true, false, why(err)
		}
		var f streamFrame
		if json.Unmarshal(data, &f) != nil {
			continue
		}
		switch f.Type {
		case "hello":
			*attempt = 0
			if f.HeartbeatMs > 0 {
				heartbeat = time.Duration(f.HeartbeatMs) * time.Millisecond
			}
			s.mu.Lock()
			msg := s.subscribe(f.Accounts)
			s.mu.Unlock()
			if err := conn.Write(s.ctx, websocket.MessageText, msg); err != nil {
				return backoff(*attempt), true, false, why(err)
			}
			s.mu.Lock()
			if s.conn == nil {
				s.conn = conn
				close(s.open)
			}
			s.mu.Unlock()
		case "notice": // before the snapshot: the resume asked could not be covered
			if f.Code == "resume_gap" {
				s.gap(f.After)
			}
		case "snapshot":
			s.mu.Lock()
			s.snapshots[f.AccountID] = data
			s.mu.Unlock()
		case "snapshot_end":
			s.once.Do(func() { close(s.ready) })
		case "error": // the subscribe refused: sent again as is, it would be again
			s.fail(f.refusal())
			return 0, false, true, ""
		case "risk":
			s.mu.Lock()
			s.risks[f.AccountID] = data
			s.mu.Unlock()
			if s.opts.OnRisk != nil {
				if r := new(AccountRisk); json.Unmarshal(f.Data, r) == nil {
					s.opts.OnRisk(f.AccountID, r)
				}
			}
		case "pong", "unsubscribed":
			s.mu.Lock()
			var ch chan json.RawMessage
			if chs := s.waiting[f.Type]; len(chs) > 0 {
				ch, s.waiting[f.Type] = chs[0], chs[1:]
			}
			s.mu.Unlock()
			if ch != nil {
				ch <- data
			}
		case "event":
			var e Event
			if json.Unmarshal(f.Event, &e) != nil {
				continue
			}
			s.mu.Lock()
			_, dup := s.seen[e.ID]
			if !dup {
				s.seen[e.ID] = struct{}{}
				s.order = append(s.order, e.ID)
				if len(s.order) > lruSize {
					delete(s.seen, s.order[0])
					s.order = s.order[1:]
				}
				s.lastEvent = e.ID
			}
			s.mu.Unlock()
			if dup {
				continue
			}
			select {
			case s.items <- StreamEvent{Event: e, Seq: f.Seq, Replayed: f.Replayed}:
			case <-s.ctx.Done():
				return 0, true, true, ""
			}
		}
	}
}

// MarketFrame is one frame of the market stream: its type (the channel: ticker, book, trades, candles, stats, status;
// or snapshot_end), its symbol, its interval (candles), its data, and the frame as it came.
type MarketFrame struct {
	Type     string          `json:"type"`
	Symbol   string          `json:"symbol"`
	Interval string          `json:"interval,omitempty"`
	Data     json.RawMessage `json:"data"`
	Raw      json.RawMessage `json:"-"`
}

type marketSub struct {
	channel string
	symbols map[string]bool
	params  map[string]any
	live    bool // the server took it once: made again after a reconnect
}

type marketReply struct {
	frame json.RawMessage
	err   error
}

// MarketStream is the market data stream.
type MarketStream struct {
	c      *Client
	ctx    context.Context
	cancel context.CancelFunc
	frames chan MarketFrame
	done   chan struct{}

	mu      sync.Mutex
	subs    map[string]*marketSub
	pending map[int64]chan marketReply
	nextID  int64
	conn    *websocket.Conn
	open    chan struct{}
	limits  json.RawMessage
	err     error
}

// MarketStream connects to the market data stream; it runs until Close (or until ctx ends).
func (c *Client) MarketStream(ctx context.Context) *MarketStream {
	sctx, cancel := context.WithCancel(ctx)
	m := &MarketStream{c: c, ctx: sctx, cancel: cancel, frames: make(chan MarketFrame, 1024), done: make(chan struct{}),
		subs: map[string]*marketSub{}, pending: map[int64]chan marketReply{}, open: make(chan struct{})}
	go m.run()
	return m
}

// Limits are the per-connection limits the hello announced: {"subscriptions", "symbols_per_subscribe", "messages_per_s"}
// (nil before it).
func (m *MarketStream) Limits() json.RawMessage {
	m.mu.Lock()
	defer m.mu.Unlock()
	return m.limits
}

// drop takes symbols off the channel's subscriptions other than keep (the server knows one by channel, symbol and a
// candle's interval, never by its other params) and forgets the ones left with none; m.mu is held. An unsubscribe
// (no keep) ends what the server ends: without an interval every interval, and naming no symbol every one.
func (m *MarketStream) drop(channel string, symbols []string, params map[string]any, keep string) {
	interval, named := params["interval"]
	unsubscribe := keep == ""
	for k, s := range m.subs {
		if k == keep || s.channel != channel {
			continue
		}
		if channel == "candles" && (named || !unsubscribe) && fmt.Sprint(s.params["interval"]) != fmt.Sprint(interval) {
			continue
		}
		if unsubscribe && len(symbols) == 0 {
			clear(s.symbols)
		}
		for _, x := range symbols {
			delete(s.symbols, strings.ToUpper(x))
		}
		if len(s.symbols) == 0 {
			delete(m.subs, k)
		}
	}
}

func subKey(channel string, params map[string]any) string {
	keys := make([]string, 0, len(params))
	for k := range params {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	var b strings.Builder
	b.WriteString(channel)
	for _, k := range keys {
		v, _ := json.Marshal(params[k])
		fmt.Fprintf(&b, "|%s=%s", k, v)
	}
	return b.String()
}

func subMessage(op string, id int64, channel string, symbols []string, params map[string]any) []byte {
	msg := map[string]any{}
	for k, v := range params {
		msg[k] = v
	}
	msg["op"], msg["id"] = op, id
	if channel != "" { // list and ping name none
		msg["channel"] = channel
	}
	if channel != "" && channel != "status" {
		if symbols == nil {
			symbols = []string{}
		}
		msg["symbols"] = symbols
	}
	b, _ := json.Marshal(msg)
	return b
}

// Subscribe follows symbols on a channel (ticker, book, trades, candles, stats, status) with its params; it answers
// the server's reply (the symbols taken and the ones it refused).
func (m *MarketStream) Subscribe(ctx context.Context, channel string, symbols []string, params map[string]any) (json.RawMessage, error) {
	m.mu.Lock()
	k := subKey(channel, params)
	m.drop(channel, symbols, params, k) // the server keeps a symbol once, with the params of its last subscribe
	s := m.subs[k]
	if s == nil {
		s = &marketSub{channel: channel, symbols: map[string]bool{}, params: params}
		m.subs[k] = s
	}
	var added []string
	for _, x := range symbols {
		if u := strings.ToUpper(x); !s.symbols[u] {
			s.symbols[u] = true
			added = append(added, u)
		}
	}
	m.mu.Unlock()
	reply, err := m.ask(ctx, "subscribe", channel, symbols, params)
	// what the server refused (the whole ask, or a symbol of its reply's rejected) is not made again after a reconnect
	var r struct {
		Symbols *[]string `json:"symbols"`
	}
	took := map[string]bool{}
	if err == nil && json.Unmarshal(reply, &r) == nil && r.Symbols != nil {
		for _, x := range *r.Symbols {
			took[strings.ToUpper(x)] = true
		}
	} else if err == nil {
		for _, u := range added {
			took[u] = true
		}
	}
	m.mu.Lock()
	defer m.mu.Unlock()
	for _, u := range added {
		if !took[u] {
			delete(s.symbols, u)
		}
	}
	if err == nil {
		s.live = true
	}
	if channel != "status" && len(s.symbols) == 0 && m.subs[k] == s {
		delete(m.subs, k)
	}
	return reply, err
}

// Unsubscribe stops following symbols on a channel (none: every one), whatever params they were subscribed with
// (candles: those of params' interval; without one, every interval).
func (m *MarketStream) Unsubscribe(ctx context.Context, channel string, symbols []string, params map[string]any) (json.RawMessage, error) {
	m.mu.Lock()
	m.drop(channel, symbols, params, "")
	m.mu.Unlock()
	var wire map[string]any
	if iv, ok := params["interval"]; ok && channel == "candles" {
		wire = map[string]any{"interval": iv}
	}
	return m.ask(ctx, "unsubscribe", channel, symbols, wire)
}

// List is the server's list of this connection's subscriptions ({"type": "list", "subscriptions": [...]}).
func (m *MarketStream) List(ctx context.Context) (json.RawMessage, error) {
	return m.ask(ctx, "list", "", nil, nil)
}

// Ping answers the server's pong ({"type": "pong", "time": ...}).
func (m *MarketStream) Ping(ctx context.Context) (json.RawMessage, error) {
	return m.ask(ctx, "ping", "", nil, nil)
}

func (m *MarketStream) ask(ctx context.Context, op, channel string, symbols []string, params map[string]any) (json.RawMessage, error) {
	for {
		m.mu.Lock()
		open, err := m.open, m.err
		m.mu.Unlock()
		if err != nil {
			return nil, err
		}
		select {
		case <-open:
		case <-ctx.Done():
			return nil, ctx.Err()
		case <-m.done:
			return nil, m.endError()
		}
		m.mu.Lock()
		conn := m.conn
		if conn == nil || m.open != open { // it dropped meanwhile: wait for the next connection
			m.mu.Unlock()
			continue
		}
		m.nextID++
		id := m.nextID
		reply := make(chan marketReply, 1)
		m.pending[id] = reply
		m.mu.Unlock()
		if err := conn.Write(ctx, websocket.MessageText, subMessage(op, id, channel, symbols, params)); err != nil {
			m.mu.Lock()
			delete(m.pending, id)
			m.mu.Unlock()
			if ctx.Err() != nil {
				return nil, ctx.Err()
			}
			continue // the connection dropped: once open again, the request goes on the new one
		}
		select {
		case r, ok := <-reply:
			if !ok {
				continue // the connection dropped before the reply: ask again on the next one
			}
			return r.frame, r.err
		case <-ctx.Done():
			return nil, ctx.Err()
		case <-m.done:
			return nil, m.endError()
		}
	}
}

// Next is the next frame. After Close it answers a StreamClosedError.
func (m *MarketStream) Next(ctx context.Context) (MarketFrame, error) {
	select {
	case f, ok := <-m.frames:
		if ok {
			return f, nil
		}
		return MarketFrame{}, m.endError()
	case <-ctx.Done():
		return MarketFrame{}, ctx.Err()
	}
}

// Frames goes over the frames until the stream ends (an error that ended it is yielded once).
func (m *MarketStream) Frames() iter.Seq2[MarketFrame, error] {
	return func(yield func(MarketFrame, error) bool) {
		for {
			f, err := m.Next(context.Background())
			if err != nil {
				if !isClosed(err) {
					yield(MarketFrame{}, err)
				}
				return
			}
			if !yield(f, nil) {
				return
			}
		}
	}
}

// Close ends the stream.
func (m *MarketStream) Close() error {
	m.cancel()
	<-m.done
	return nil
}

func (m *MarketStream) endError() error {
	m.mu.Lock()
	defer m.mu.Unlock()
	if m.err != nil {
		return m.err
	}
	return closedError()
}

func (m *MarketStream) put(f MarketFrame) {
	select {
	case m.frames <- f:
	default:
		select { // a slow reader loses the oldest frame, never the connection
		case <-m.frames:
		default:
		}
		select {
		case m.frames <- f:
		default:
		}
	}
}

func (m *MarketStream) run() {
	defer close(m.done)
	defer close(m.frames)
	attempt := 0
	for m.ctx.Err() == nil {
		var wait time.Duration
		var cause string
		conn, resp, err := websocket.Dial(m.ctx, m.c.streams["market"], &websocket.DialOptions{HTTPHeader: m.c.streamHeader()})
		if err != nil {
			var end *Error
			if wait, cause, end = dialFailed(resp, err, attempt); end != nil {
				m.mu.Lock()
				m.err = end
				m.mu.Unlock()
				return
			}
		} else {
			var stop bool
			wait, stop, cause = m.read(conn, &attempt)
			if stop {
				return
			}
		}
		if m.ctx.Err() != nil {
			return
		}
		m.c.log(slog.LevelInfo, "phoenixperps: the market stream reconnects", "cause", cause, "wait_ms", wait.Milliseconds())
		attempt++
		if sleep(m.ctx, wait) != nil {
			return
		}
	}
}

func (m *MarketStream) read(conn *websocket.Conn, attempt *int) (time.Duration, bool, string) {
	defer func() {
		conn.CloseNow()
		m.mu.Lock()
		m.conn = nil
		m.open = make(chan struct{})
		for id, ch := range m.pending {
			delete(m.pending, id)
			close(ch)
		}
		m.mu.Unlock()
	}()
	conn.SetReadLimit(4 << 20)
	heartbeat := 15 * time.Second
	for {
		rctx, cancel := context.WithTimeout(m.ctx, 3*heartbeat)
		_, data, err := conn.Read(rctx)
		cancel()
		if err != nil {
			if m.ctx.Err() != nil {
				return 0, true, ""
			}
			switch websocket.CloseStatus(err) {
			case 4001:
				m.mu.Lock()
				m.err = &Error{Kind: KindAuthentication, Code: "stream_credential_ended", Message: "The key of this stream ended."}
				m.mu.Unlock()
				return 0, true, ""
			case 1012:
				return time.Duration(mrand.Float64() * 3 * float64(time.Second)), false, why(err)
			}
			return backoff(*attempt), false, why(err)
		}
		var f streamFrame
		if json.Unmarshal(data, &f) != nil {
			continue
		}
		switch f.Type {
		case "hello":
			*attempt = 0
			if f.HeartbeatMs > 0 {
				heartbeat = time.Duration(f.HeartbeatMs) * time.Millisecond
			}
			m.mu.Lock()
			m.limits = f.Limits
			m.conn = conn
			subs := make([]*marketSub, 0, len(m.subs))
			for _, s := range m.subs {
				if s.live { // one not taken yet goes with its own Subscribe
					subs = append(subs, s)
				}
			}
			m.mu.Unlock()
			for _, s := range subs { // every subscription again, as it was
				m.mu.Lock()
				m.nextID++
				id := m.nextID
				syms := make([]string, 0, len(s.symbols))
				for x := range s.symbols {
					syms = append(syms, x)
				}
				m.mu.Unlock()
				sort.Strings(syms)
				_ = conn.Write(m.ctx, websocket.MessageText, subMessage("subscribe", id, s.channel, syms, s.params))
			}
			m.mu.Lock()
			close(m.open)
			m.mu.Unlock()
		case "subscribed", "unsubscribed", "error", "list", "pong":
			m.mu.Lock()
			var ch chan marketReply
			if f.ID != nil {
				ch = m.pending[*f.ID]
				delete(m.pending, *f.ID)
			}
			m.mu.Unlock()
			if ch != nil {
				r := marketReply{frame: data}
				if f.Type == "error" {
					r = marketReply{err: f.refusal()}
				}
				ch <- r
			}
		case "heartbeat":
		default:
			mf := MarketFrame{Raw: data}
			_ = json.Unmarshal(data, &mf)
			m.put(mf)
		}
	}
}
