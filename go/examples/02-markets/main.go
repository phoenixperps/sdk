// 02: the markets and their rules, then a quote (bid, ask, last) from the market stream.
//
//	go run ./examples/02-markets [BTC-USD]
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"strings"

	phoenixperps "github.com/phoenixperps/sdk/go"
)

func main() {
	ctx := context.Background()
	client, err := phoenixperps.NewClient()
	if err != nil {
		log.Fatal(err)
	}
	symbol := "BTC-USD"
	if len(os.Args) > 1 {
		symbol = strings.ToUpper(os.Args[1])
	}
	markets, err := client.Markets.List(ctx)
	if err != nil {
		log.Fatal(err)
	}
	for _, m := range markets.Data {
		fmt.Printf("%-10s tick %s  step %s  min %s  up to %dx  %s\n", m.Market, m.TickSize, m.SizeStep, m.MinSize, m.MaxLeverage, m.Status)
	}

	stream := client.MarketStream(ctx)
	defer stream.Close()
	reply, err := stream.Subscribe(ctx, "ticker", []string{symbol}, map[string]any{"interval_ms": 250})
	if err != nil {
		log.Fatal(err)
	}
	var sub struct {
		Rejected []struct{ Symbol, Code string }
	}
	_ = json.Unmarshal(reply, &sub)
	if len(sub.Rejected) > 0 {
		log.Fatalf("%s: %s", symbol, sub.Rejected[0].Code)
	}
	for {
		f, err := stream.Next(ctx)
		if err != nil {
			log.Fatal(err)
		}
		if f.Type != "ticker" {
			continue
		}
		var q struct{ Bid, Ask, Last string }
		if err := json.Unmarshal(f.Data, &q); err != nil {
			log.Fatal(err)
		}
		fmt.Printf("%s: bid %s  ask %s  last %s\n", symbol, q.Bid, q.Ask, q.Last)
		return
	}
}
