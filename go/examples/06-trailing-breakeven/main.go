// 06: a position whose stop loss trails the price by 0.5% and moves to break-even once the price has gone 0.5% your
// way, all on the API's servers. Follows the account stream for 30 s to watch the stop move, then closes.
//
//	go run ./examples/06-trailing-breakeven
package main

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"log"
	"os"
	"strings"
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

	// the stream first, so every event of the stop is told, from its placing on
	watch, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()
	stream := client.AccountStream(watch, phoenixperps.AccountStreamOptions{Accounts: []string{account}})
	<-stream.Ready()

	order, err := client.Orders.Create(ctx, account, phoenixperps.CreateOrder{
		Market: "BTC-USD",
		Side:   phoenixperps.OrderSideBuy,
		Type:   phoenixperps.CreateOrderTypeMarket,
		Size:   &btc.MinSize,
		Exits: &phoenixperps.OrderExits{StopLoss: &phoenixperps.OrderExitsStopLoss{
			Percent:   phoenixperps.Dec("1"),
			Trail:     &phoenixperps.OrderExitsStopLossTrail{Percent: phoenixperps.Dec("0.5")},
			BreakEven: &phoenixperps.OrderExitsStopLossBreakEven{TriggerPercent: phoenixperps.Dec("0.5"), OffsetTicks: phoenixperps.Ptr[int64](2)},
		}},
	})
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("bought %s BTC (%s); watching the stop for 30 s\n", btc.MinSize, order.Status)

	for {
		e, err := stream.Next(watch)
		if errors.Is(err, context.DeadlineExceeded) {
			break
		}
		if err != nil {
			log.Fatal(err)
		}
		if strings.HasPrefix(string(e.Type), "strategy.") {
			b, _ := json.Marshal(e.Data)
			fmt.Printf("%s %s %s\n", time.UnixMilli(e.Time).Format(time.RFC3339), e.Type, b)
		}
	}
	_ = stream.Close()

	closed, err := client.Positions.Close(ctx, account, "BTC-USD", phoenixperps.ClosePosition{})
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("closed: %s\n", closed.Status)
}
