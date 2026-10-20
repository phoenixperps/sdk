package phoenixperps

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"errors"
	"net/http"
	"strconv"
	"testing"
	"time"
)

// WorstPrice takes buy or sell in any case; any other side is an error, never the sell side's price.
func TestWorstPriceSide(t *testing.T) {
	r := NewMarketRules(&S.Rules.Market)
	buy, err := r.WorstPrice("buy", "62000", nil)
	if err != nil {
		t.Fatal(err)
	}
	for _, side := range []string{"BUY", "Buy", string(OrderSideBuy)} {
		if got, err := r.WorstPrice(side, "62000", nil); err != nil || got != buy {
			t.Fatalf("%s: %q %v, want %q", side, got, err, buy)
		}
	}
	for _, side := range []string{"long", "", "bid"} {
		if got, err := r.WorstPrice(side, "62000", nil); !errors.Is(err, ErrValidation) {
			t.Fatalf("%q: %q %v, want a ValidationError", side, got, err)
		}
	}
}

// The slippage is the market's own min..max, both ends included; outside it is a ValidationError.
func TestWorstPriceSlippageBounds(t *testing.T) {
	r := NewMarketRules(&S.Rules.Market) // 0.05 to 5
	for pct, ok := range map[string]bool{"0.05": true, "5": true, "0.04": false, "5.01": false} {
		got, err := r.WorstPrice("sell", "62000", Dec(pct))
		if ok != (err == nil) || (!ok && !errors.Is(err, ErrValidation)) {
			t.Fatalf("%s%%: %q %v", pct, got, err)
		}
	}
}

// signed is a delivery's headers for body, signed now with secret.
func signed(secret, body string) http.Header {
	ts := strconv.FormatInt(time.Now().Unix(), 10)
	mac := hmac.New(sha256.New, []byte(secret))
	mac.Write([]byte(ts + "." + body))
	h := http.Header{}
	h.Set("X-Phoenix-Timestamp", ts)
	h.Set("X-Phoenix-Signature", "sha256="+hex.EncodeToString(mac.Sum(nil)))
	return h
}

// VerifyWebhookPayload (now VerifyWebhook) reads the whole delivery: a webhook.ping with its api_version, environment
// and test.
func TestVerifyWebhookPayload(t *testing.T) {
	secret := S.Webhook.Secret
	body := `{"id":"evt_1","type":"webhook.ping","time":1791460800123,"source":"api","data":{},"api_version":"v1","environment":"sandbox","test":true}`
	h := signed(secret, body)
	p, err := VerifyWebhookPayload([]byte(body), h, []string{secret}, 0)
	if err != nil || p.Type != WebhookPayloadTypeWebhookPing || p.APIVersion != "v1" || p.Environment != "sandbox" || p.Test == nil || !*p.Test || p.AccountID != nil {
		t.Fatalf("%+v %v", p, err)
	}
	if _, err := VerifyWebhookPayload([]byte(body+" "), h, []string{secret}, 0); err == nil {
		t.Fatal("a changed body verified")
	}
}

// A signed body that is no event (null, an object without its id and type) is bad_body; WebhookError still matches.
func TestVerifyWebhookBadBody(t *testing.T) {
	for _, body := range []string{`null`, `{}`, `{"id":"evt_1"}`} {
		_, err := VerifyWebhook([]byte(body), signed(S.Webhook.Secret, body), []string{S.Webhook.Secret}, 0)
		var we *WebhookError
		if !errors.Is(err, ErrWebhookVerification) || !errors.As(err, &we) || we.Code != "bad_body" {
			t.Fatalf("%s: %v", body, err)
		}
	}
}
