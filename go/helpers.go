package phoenixperps

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"math/big"
	"net/http"
	"strconv"
	"strings"
	"time"
)

/*
Small helpers (docs/BEHAVIOUR.md "Helpers"), the same names in every SDK, in exact decimals.

MarketRules (from GET /v1/markets/{market}): RoundPrice, RoundSize (down to the step), SizeForNotional,
SizeForRisk(riskUSD, stopDistance), WorstPrice(side, price, slippagePct) rounded outward. The steps and the slippage
bounds are the market's own (nothing written in here). What they cannot take (a string that is no decimal, a side
other than buy or sell, a slippage outside the market's bounds) is an ErrValidation, before any request.

VerifyWebhook(raw, header, secrets, tolerance): the WebhookPayload (the event, with api_version, environment and test),
or an ErrWebhookVerification whose Code is missing_headers, stale, bad_signature or bad_body. X-Phoenix-Signature is
sha256=<hex HMAC-SHA256(secret, "<X-Phoenix-Timestamp>.<raw body>")>, any of its comma-separated values (a rotation
sends two), compared in constant time; X-Phoenix-Timestamp within the tolerance (300 s when zero).
*/

// RoundMode is how a value meets a step.
type RoundMode int

const (
	RoundNearest RoundMode = iota // the nearest multiple; a tie goes away from zero
	RoundDown                     // the multiple at or below
	RoundUp                       // the multiple at or above
)

// invalid is what a helper cannot take: a ValidationError of the SDK's own.
func invalid(format string, args ...any) *Error {
	return &Error{Kind: KindValidation, Code: ErrCodeInvalidParameter, Message: fmt.Sprintf(format, args...)}
}

func rat(d Decimal, what string) (*big.Rat, error) {
	r, ok := d.Rat()
	if !ok {
		return nil, invalid("The %s %q is not a decimal.", what, string(d))
	}
	return r, nil
}

func places(step Decimal) int {
	s := strings.TrimRight(string(step), "0")
	if i := strings.IndexByte(s, '.'); i >= 0 {
		return len(s) - i - 1
	}
	return 0
}

// RoundTo is x as a multiple of step, as the decimal string the API takes.
func RoundTo(x, step Decimal, mode RoundMode) (Decimal, error) {
	xr, err := rat(x, "value")
	if err != nil {
		return "", err
	}
	st, err := rat(step, "step")
	if err != nil {
		return "", err
	}
	if st.Sign() <= 0 {
		return "", invalid("The step %q is not above zero.", string(step))
	}
	return roundRat(xr, st, step, mode), nil
}

func roundRat(x, st *big.Rat, step Decimal, mode RoundMode) Decimal {
	q := new(big.Rat).Quo(x, st)
	num, den := q.Num(), q.Denom()
	n, rem := new(big.Int).QuoRem(num, den, new(big.Int)) // truncated toward zero
	switch mode {
	case RoundDown:
		if rem.Sign() < 0 {
			n.Sub(n, big.NewInt(1))
		}
	case RoundUp:
		if rem.Sign() > 0 {
			n.Add(n, big.NewInt(1))
		}
	default:
		twice := new(big.Int).Mul(new(big.Int).Abs(rem), big.NewInt(2))
		if twice.Cmp(den) >= 0 {
			if num.Sign() < 0 {
				n.Sub(n, big.NewInt(1))
			} else {
				n.Add(n, big.NewInt(1))
			}
		}
	}
	v := new(big.Rat).Mul(new(big.Rat).SetInt(n), st)
	return Decimal(v.FloatString(places(step)))
}

// MarketRules rounds prices and sizes the way a market takes them.
type MarketRules struct {
	Market   string
	TickSize Decimal
	SizeStep Decimal
	MinSize  Decimal
	Slippage *MarketMaxSlippagePct
}

// NewMarketRules reads the rules of a market (client.Markets.Get).
func NewMarketRules(m *Market) *MarketRules {
	return &MarketRules{Market: m.Market, TickSize: m.TickSize, SizeStep: m.SizeStep, MinSize: m.MinSize, Slippage: m.MaxSlippagePct}
}

// RoundPrice is price as a multiple of the tick size.
func (r *MarketRules) RoundPrice(price Decimal, mode RoundMode) (Decimal, error) {
	return RoundTo(price, r.TickSize, mode)
}

// RoundSize is size down to the size step.
func (r *MarketRules) RoundSize(size Decimal) (Decimal, error) {
	return RoundTo(size, r.SizeStep, RoundDown)
}

// SizeForNotional is the size worth usd at price, down to the step.
func (r *MarketRules) SizeForNotional(usd, price Decimal) (Decimal, error) {
	u, err := rat(usd, "usd")
	if err != nil {
		return "", err
	}
	p, err := rat(price, "price")
	if err != nil {
		return "", err
	}
	if p.Sign() == 0 {
		return "", invalid("The price is zero.")
	}
	return r.sizeOf(new(big.Rat).Quo(u, p))
}

// SizeForRisk is the size that loses riskUSD over stopDistance, down to the step.
func (r *MarketRules) SizeForRisk(riskUSD, stopDistance Decimal) (Decimal, error) {
	u, err := rat(riskUSD, "risk")
	if err != nil {
		return "", err
	}
	d, err := rat(stopDistance, "stop distance")
	if err != nil {
		return "", err
	}
	if d.Sign() == 0 {
		return "", invalid("The stop distance is zero.")
	}
	return r.sizeOf(new(big.Rat).Quo(u, new(big.Rat).Abs(d)))
}

func (r *MarketRules) sizeOf(x *big.Rat) (Decimal, error) {
	st, err := rat(r.SizeStep, "size step")
	if err != nil {
		return "", err
	}
	return roundRat(x, st, r.SizeStep, RoundDown), nil
}

// WorstPrice is the worst price a market order of side ("buy" or "sell", in any case; anything else is an error)
// accepts at price, slippagePct away (nil: the market's default; outside its min..max, an error), rounded outward to
// the tick.
func (r *MarketRules) WorstPrice(side string, price Decimal, slippagePct *Decimal) (Decimal, error) {
	buy := strings.EqualFold(side, string(OrderSideBuy))
	if !buy && !strings.EqualFold(side, string(OrderSideSell)) {
		return "", invalid("The side %q is not buy or sell.", side)
	}
	pct := slippagePct
	if pct == nil && r.Slippage != nil {
		pct = r.Slippage.Default
	}
	if pct == nil {
		return "", invalid("No slippage given, and the market has no default.")
	}
	p, err := rat(price, "price")
	if err != nil {
		return "", err
	}
	s, err := rat(*pct, "slippage")
	if err != nil {
		return "", err
	}
	if b := r.Slippage; b != nil && (cmpTo(s, b.Min) < 0 || cmpTo(s, b.Max) > 0) {
		return "", invalid("The slippage %s%% is outside the market's %v to %v.", *pct, b.Min, b.Max)
	}
	tick, err := rat(r.TickSize, "tick size")
	if err != nil {
		return "", err
	}
	f := new(big.Rat).Quo(s, big.NewRat(100, 1))
	mode := RoundUp
	if buy {
		f.Add(big.NewRat(1, 1), f)
	} else {
		f.Sub(big.NewRat(1, 1), f)
		mode = RoundDown
	}
	return roundRat(new(big.Rat).Mul(p, f), tick, r.TickSize, mode), nil
}

// cmpTo compares x to a bound of the market's (0 when it gives none).
func cmpTo(x *big.Rat, bound *Decimal) int {
	if bound == nil {
		return 0
	}
	if v, ok := bound.Rat(); ok {
		return x.Cmp(v)
	}
	return 0
}

// VerifyWebhook is a delivery once its signature and time check out: the WebhookPayload (the event, with the
// endpoint's api_version and environment, and test on a webhook.ping or a delivery resent as one). raw is the body
// exactly as received; secrets are the endpoint's (two during a rotation). A delivery that does not verify is an
// ErrWebhookVerification (Code: missing_headers, stale, bad_signature or bad_body).
func VerifyWebhook(raw []byte, header http.Header, secrets []string, tolerance time.Duration) (*WebhookPayload, error) {
	return VerifyWebhookAt(raw, header, secrets, tolerance, time.Now())
}

// VerifyWebhookPayload is VerifyWebhook.
//
// Deprecated: VerifyWebhook answers the WebhookPayload.
func VerifyWebhookPayload(raw []byte, header http.Header, secrets []string, tolerance time.Duration) (*WebhookPayload, error) {
	return VerifyWebhook(raw, header, secrets, tolerance)
}

// VerifyWebhookAt is VerifyWebhook at a given time.
func VerifyWebhookAt(raw []byte, header http.Header, secrets []string, tolerance time.Duration, now time.Time) (*WebhookPayload, error) {
	refused := func(code, message string) (*WebhookPayload, error) {
		return nil, &Error{Kind: KindWebhookVerification, Code: code, Message: message}
	}
	if tolerance <= 0 {
		tolerance = 300 * time.Second
	}
	ts, sig := header.Get("X-Phoenix-Timestamp"), header.Get("X-Phoenix-Signature")
	if ts == "" || sig == "" {
		return refused("missing_headers", "X-Phoenix-Timestamp and X-Phoenix-Signature are required.")
	}
	t, err := strconv.ParseInt(ts, 10, 64)
	if err != nil || now.Sub(time.Unix(t, 0)).Abs() > tolerance {
		return refused("stale", "The delivery is too old (or the clock is off).")
	}
	given := strings.Split(sig, ",")
	for _, secret := range secrets {
		mac := hmac.New(sha256.New, []byte(secret))
		mac.Write([]byte(ts + "." + string(raw)))
		want := []byte("sha256=" + hex.EncodeToString(mac.Sum(nil)))
		for _, g := range given {
			if hmac.Equal([]byte(strings.TrimSpace(g)), want) {
				p := new(WebhookPayload)
				if json.Unmarshal(raw, p) != nil || p.ID == "" || p.Type == "" { // signed, but no event
					return refused("bad_body", "The body is not an event.")
				}
				return p, nil
			}
		}
	}
	return refused("bad_signature", "The signature does not match.")
}
