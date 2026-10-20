// 04: a limit order far from the market, found again by your own id, then canceled. The Idempotency-Key makes a
// retried request return the first order instead of placing a second one.
//
//	go run ./examples/04-place-cancel-limit
package main

import (
	"context"
	"crypto/rand"
	"encoding/json"
	"fmt"
	"log"
	"math/big"
	"os"
	"time"

	phoenixperps "github.com/phoenixperps/sdk/go"
)

func main() {
	ctx := context.Background()
	client, err := phoenixperps.NewClient()
	if err != nil {
		log.Fatal(err)
	}
	if client.Environment() == "live" && os.Getenv("PHOENIXPERPS_ALLOW_LIVE") != "yes" {
		log.Fatal("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).")
	}
	accounts, err := client.Accounts.List(ctx)
	if err != nil {
		log.Fatal(err)
	}
	account := accounts.Data[0].ID
	btc, err := client.Markets.Get(ctx, "BTC-USD")
	if err != nil {
		log.Fatal(err)
	}
	rules := phoenixperps.NewMarketRules(btc)

	bid := bestBid(ctx, client)
	size, err := rules.SizeForNotional("100", bid) // about 100 USD
	if err != nil {
		log.Fatal(err)
	}
	b, _ := new(big.Rat).SetString(string(bid))
	limit, err := rules.RoundPrice(phoenixperps.Decimal(b.Mul(b, big.NewRat(9, 10)).FloatString(8)), phoenixperps.RoundDown) // 10% below: it rests
	if err != nil {
		log.Fatal(err)
	}
	clientOrderID := fmt.Sprintf("example-04-%d", time.Now().UnixMilli())
	order, err := client.Orders.Create(ctx, account, phoenixperps.CreateOrder{
		Market:        "BTC-USD",
		Side:          phoenixperps.OrderSideBuy,
		Type:          phoenixperps.CreateOrderTypeLimit,
		Size:          &size,
		LimitPrice:    &limit,
		ClientOrderID: phoenixperps.Ptr(clientOrderID),
	}, phoenixperps.WithIdempotencyKey(newKey()))
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("placed %s: %s, %s at %s\n", order.ID, order.Status, size, limit)

	found, err := client.Orders.List(ctx, account, &phoenixperps.OrdersListParams{ClientOrderID: phoenixperps.Ptr(clientOrderID)})
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("found by client_order_id: %s\n", found.Data[0].ID)

	canceled, err := client.Orders.Cancel(ctx, account, order.ID)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("canceled: %s\n", canceled.Status)
}

// bestBid is the market's bid now, from the market stream.
func bestBid(ctx context.Context, client *phoenixperps.Client) phoenixperps.Decimal {
	stream := client.MarketStream(ctx)
	defer stream.Close()
	if _, err := stream.Subscribe(ctx, "ticker", []string{"BTC-USD"}, nil); err != nil {
		log.Fatal(err)
	}
	for {
		f, err := stream.Next(ctx)
		if err != nil {
			log.Fatal(err)
		}
		var q struct{ Bid phoenixperps.Decimal }
		if f.Type == "ticker" && json.Unmarshal(f.Data, &q) == nil {
			return q.Bid
		}
	}
}

func newKey() string {
	b := make([]byte, 16)
	_, _ = rand.Read(b)
	return fmt.Sprintf("%x", b)
}
