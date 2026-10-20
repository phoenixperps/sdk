// 07: what an order would do now, without placing it: fill or rest, the price, the fee, the margin, the position
// after. Nothing is sent to the market.
//
//	go run ./examples/07-what-if
package main

import (
	"context"
	"fmt"
	"log"

	phoenixperps "github.com/phoenixperps/sdk/go"
)

func or(d *phoenixperps.Decimal) string {
	if d == nil {
		return "-"
	}
	return string(*d)
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
	w, err := client.Orders.Preview(ctx, accounts.Data[0].ID, phoenixperps.CreateOrder{
		Market:      "BTC-USD",
		Side:        phoenixperps.OrderSideBuy,
		Type:        phoenixperps.CreateOrderTypeMarket,
		NotionalUSD: phoenixperps.Dec("1000"),
	})
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("result:         %s\n", w.Result)
	if w.Reject != nil {
		fmt.Printf("refused:        %+v\n", *w.Reject)
	}
	fmt.Printf("size:           %s\n", or(w.Size))
	if w.Execution != nil {
		fmt.Printf("price:          %s (worst %s)\n", w.Execution.ExpectedPrice, or(w.Execution.WorstPrice))
	}
	fmt.Printf("notional, fee:  %s, %s\n", or(w.Notional), or(w.Fee))
	if w.Margin != nil {
		fmt.Printf("margin:         %s needed, %s left after\n", w.Margin.Required, or(w.Margin.AvailableAfter))
	}
	if w.PositionAfter != nil {
		side := "flat"
		if w.PositionAfter.Side != nil {
			side = string(*w.PositionAfter.Side)
		}
		fmt.Printf("position after: %s %s\n", side, w.PositionAfter.Size)
	}
}
