// 09: an account's history: the equity curve by day, the result of each trading day, closed positions and fills.
//
//	go run ./examples/09-history
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
	account := accounts.Data[0].ID

	equity, err := client.History.Equity(ctx, account, &phoenixperps.HistoryEquityParams{Resolution: phoenixperps.Ptr(phoenixperps.ResolutionDay)})
	if err != nil {
		log.Fatal(err)
	}
	last := equity.Points[max(0, len(equity.Points)-3):]
	fmt.Printf("equity: %d points, last %+v\n", len(equity.Points), last)

	daily, err := client.History.Daily(ctx, account)
	if err != nil {
		log.Fatal(err)
	}
	for _, d := range daily.Data {
		fmt.Printf("%s: %d trades, P&L %s, fees %s\n", d.Date, d.Trades, d.PnL, d.Fees)
	}
	closed, err := client.Positions.ListClosed(ctx, account, &phoenixperps.PositionsListClosedParams{Limit: phoenixperps.Ptr[int64](10)})
	if err != nil {
		log.Fatal(err)
	}
	for _, p := range closed.Data {
		fmt.Printf("closed %s %s %s: %s -> %s, P&L %s\n", p.Market, p.Side, p.Size, or(p.EntryPrice), or(p.ExitPrice), p.PnL)
	}
	n := 0
	for f, err := range client.Fills.ListAutoPaging(ctx, account, nil, phoenixperps.WithMaxItems(100)) { // every page, at most 100
		if err != nil {
			log.Fatal(err)
		}
		n++
		if n <= 5 {
			fmt.Printf("fill %s %s @ %s\n", f.Market, f.Size, f.Price)
		}
	}
	fmt.Printf("%d fills read\n", n)
}
