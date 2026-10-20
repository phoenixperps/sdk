// 10: the last 100 closed one-minute candles of a market and the one forming (final false), written to candles.csv.
//
//	go run ./examples/10-candles-to-csv [BTC-USD]
package main

import (
	"context"
	"encoding/csv"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"sort"
	"strconv"
	"strings"
	"time"

	phoenixperps "github.com/phoenixperps/sdk/go"
)

type candle struct {
	Time                           int64
	Open, High, Low, Close, Volume string
	Final                          bool
}

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
	stream := client.MarketStream(ctx)
	if _, err := stream.Subscribe(ctx, "candles", []string{symbol}, map[string]any{"interval": "1m", "history": 100}); err != nil {
		log.Fatal(err)
	}
	candles := map[int64]candle{}
	for {
		f, err := stream.Next(ctx)
		if err != nil {
			log.Fatal(err)
		}
		if f.Type == "snapshot_end" {
			break
		}
		if f.Type != "candles" {
			continue
		}
		var cs []candle
		if err := json.Unmarshal(f.Data, &cs); err != nil {
			log.Fatal(err)
		}
		for _, c := range cs {
			candles[c.Time] = c
		}
	}
	_ = stream.Close()

	times := make([]int64, 0, len(candles))
	for t := range candles {
		times = append(times, t)
	}
	sort.Slice(times, func(i, j int) bool { return times[i] < times[j] })
	file, err := os.Create("candles.csv")
	if err != nil {
		log.Fatal(err)
	}
	w := csv.NewWriter(file)
	_ = w.Write([]string{"time", "open", "high", "low", "close", "volume", "final"})
	for _, t := range times {
		c := candles[t]
		_ = w.Write([]string{time.UnixMilli(t).UTC().Format(time.RFC3339), c.Open, c.High, c.Low, c.Close, c.Volume, strconv.FormatBool(c.Final)})
	}
	w.Flush()
	if err := file.Close(); err != nil {
		log.Fatal(err)
	}
	fmt.Printf("%d candles of %s in candles.csv\n", len(times), symbol)
}
