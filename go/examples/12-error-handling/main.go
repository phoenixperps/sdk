// 12: what failures look like: each has a family (errors.Is), the API's code, the request id to quote to support, and
// details. A write whose outcome is unknown (a timeout) is repeated with the same Idempotency-Key, never a new one.
//
//	go run ./examples/12-error-handling
package main

import (
	"context"
	"crypto/rand"
	"errors"
	"fmt"
	"log"
	"time"

	phoenixperps "github.com/phoenixperps/sdk/go"
)

func show(err error) {
	var e *phoenixperps.Error
	if !errors.As(err, &e) {
		log.Fatal(err)
	}
	fmt.Printf("%s: %s (HTTP %d, %s) %s\n", e.Kind, e.Code, e.Status, e.RequestID, e.Message)
	if len(e.Details) > 0 {
		fmt.Printf("  details: %s\n", e.Details)
	}
	fmt.Printf("  retryable: %v, more: %s\n", e.Retryable, e.DocURL) // retryable: the same call may succeed later
}

func main() {
	ctx := context.Background()
	client, err := phoenixperps.NewClient()
	if err != nil {
		log.Fatal(err)
	}
	accounts, err := client.Accounts.List(ctx)
	if err != nil {
		log.Fatal(err)
	}
	account := accounts.Data[0].ID

	if _, err := client.Orders.Get(ctx, account, "ord_does_not_exist"); errors.Is(err, phoenixperps.ErrNotFound) {
		show(err)
	} else if err != nil {
		log.Fatal(err)
	}

	_, err = client.Orders.Preview(ctx, account, phoenixperps.CreateOrder{
		Market: "BTC-USD", Side: phoenixperps.OrderSideBuy, Type: phoenixperps.CreateOrderTypeMarket, Size: phoenixperps.Dec("0.0000001"),
	})
	if errors.Is(err, phoenixperps.ErrValidation) || errors.Is(err, phoenixperps.ErrRuleRejected) {
		show(err)
	} else if err != nil {
		log.Fatal(err)
	}
}

// placeOnce places an order whose outcome may stay unknown: the same key again is safe, a new key could place it twice.
// ErrTimeout is the SDK's own timeout; a call your ctx ended answers ctx.Err() (unknown too: a fresh ctx, the same key).
func placeOnce(ctx context.Context, client *phoenixperps.Client, account string, body phoenixperps.CreateOrder) (*phoenixperps.Order, error) {
	b := make([]byte, 16)
	_, _ = rand.Read(b)
	key := fmt.Sprintf("%x", b)
	for attempt := 1; ; attempt++ {
		order, err := client.Orders.Create(ctx, account, body, phoenixperps.WithIdempotencyKey(key))
		var e *phoenixperps.Error
		switch {
		case err == nil:
			return order, nil
		case attempt < 3 && errors.Is(err, phoenixperps.ErrTimeout):
			continue // still unknown after the SDK's own retries: the same key
		case attempt < 3 && errors.As(err, &e) && e.Kind == phoenixperps.KindRateLimit && e.RetryAfter > 0:
			time.Sleep(e.RetryAfter)
			continue
		default:
			return nil, err
		}
	}
}
