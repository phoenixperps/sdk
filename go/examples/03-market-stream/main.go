// 03: live market data: prices for two markets and the trades of one, until 20 frames came.
//
//	go run ./examples/03-market-stream
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"

	phoenixperps "github.com/phoenixperps/sdk/go"
)

func main() {
	ctx := context.Background()
	client, err := phoenixperps.NewClient()
	if err != nil {
		log.Fatal(err)
	}
	stream := client.MarketStream(ctx) // subscriptions are made again after a reconnect
	defer stream.Close()
	if _, err := stream.Subscribe(ctx, "ticker", []string{"BTC-USD", "ETH-USD"}, map[string]any{"interval_ms": 500}); err != nil {
		log.Fatal(err)
	}
	if _, err := stream.Subscribe(ctx, "trades", []string{"BTC-USD"}, map[string]any{"history": 5}); err != nil {
		log.Fatal(err)
	}
	for n := 0; n < 20; {
		f, err := stream.Next(ctx)
		if err != nil {
			log.Fatal(err)
		}
		switch f.Type {
		case "ticker":
			var q struct{ Bid, Ask string }
			_ = json.Unmarshal(f.Data, &q)
			fmt.Printf("ticker %s: bid %s ask %s\n", f.Symbol, q.Bid, q.Ask)
		case "trades":
			var trades []struct{ Side, Size, Price string }
			_ = json.Unmarshal(f.Data, &trades)
			for _, t := range trades {
				fmt.Printf("trade  %s: %s %s @ %s\n", f.Symbol, t.Side, t.Size, t.Price)
			}
		default:
			continue
		}
		n++
	}
}
