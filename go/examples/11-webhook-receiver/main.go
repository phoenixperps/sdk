// 11: receive webhooks: every delivery is checked (its signature and its time) before it is trusted.
//
//	PHOENIXPERPS_WEBHOOK_SECRET=ppwhsec_... go run ./examples/11-webhook-receiver
//
// Then point a webhook endpoint (dashboard, Developers, Webhooks) at https://<this machine>/webhooks (port 8080 here).
package main

import (
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"time"

	phoenixperps "github.com/phoenixperps/sdk/go"
)

func main() {
	secret := os.Getenv("PHOENIXPERPS_WEBHOOK_SECRET")
	if secret == "" {
		log.Fatal("Set PHOENIXPERPS_WEBHOOK_SECRET to the endpoint's signing secret.")
	}
	http.HandleFunc("POST /webhooks", func(w http.ResponseWriter, r *http.Request) {
		raw, err := io.ReadAll(io.LimitReader(r.Body, 1<<20))
		if err != nil {
			w.WriteHeader(http.StatusBadRequest)
			return
		}
		event, err := phoenixperps.VerifyWebhook(raw, r.Header, []string{secret}, 5*time.Minute) // the body exactly as received
		var bad *phoenixperps.Error
		if errors.Is(err, phoenixperps.ErrWebhookVerification) && errors.As(err, &bad) {
			fmt.Printf("refused a delivery: %s\n", bad.Code) // missing_headers, stale, bad_signature or bad_body
			w.WriteHeader(http.StatusBadRequest)
			return
		}
		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			return
		}
		account := "no account" // a webhook.ping (the Test button) names none
		if event.AccountID != nil {
			account = *event.AccountID
		}
		data, _ := json.Marshal(event.Data)
		fmt.Printf("%s on %s (%s): %s\n", event.Type, account, event.Environment, data)
		w.WriteHeader(http.StatusOK)
	})
	fmt.Println("listening on :8080/webhooks")
	log.Fatal(http.ListenAndServe(":8080", nil))
}
