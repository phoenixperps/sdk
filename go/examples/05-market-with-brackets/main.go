// 05: a market order with its exits: a take profit 1% away and a stop loss 0.5% away, placed by the API the moment
// it fills and kept on its servers. The position is closed at the end.
//
//	go run ./examples/05-market-with-brackets
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"

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

	order, err := client.Orders.Create(ctx, account, phoenixperps.CreateOrder{
		Market: "BTC-USD",
		Side:   phoenixperps.OrderSideBuy,
		Type:   phoenixperps.CreateOrderTypeMarket,
		Size:   &btc.MinSize,
		Exits: &phoenixperps.OrderExits{
			TakeProfit: []phoenixperps.OrderExitsTakeProfit{{Percent: phoenixperps.Dec("1")}},
			StopLoss:   &phoenixperps.OrderExitsStopLoss{Percent: phoenixperps.Dec("0.5")},
		},
	})
	if err != nil {
		log.Fatal(err)
	}
	fill := "-"
	if order.AverageFillPrice != nil {
		fill = string(*order.AverageFillPrice)
	}
	fmt.Printf("market order %s: %s at %s\n", order.ID, order.Status, fill)

	exits, err := client.Exits.Get(ctx, account, "BTC-USD")
	if err != nil {
		log.Fatal(err)
	}
	for _, tp := range exits.TakeProfit {
		b, _ := json.Marshal(tp)
		fmt.Printf("take profit: %s\n", b)
	}
	for _, sl := range exits.StopLoss {
		b, _ := json.Marshal(sl)
		fmt.Printf("stop loss:   %s\n", b)
	}

	closed, err := client.Positions.Close(ctx, account, "BTC-USD", phoenixperps.ClosePosition{})
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("closed: %s %s (the exits went with the position)\n", closed.ID, closed.Status)
}
