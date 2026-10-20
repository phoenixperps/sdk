// 08: a live copy of your accounts' positions and working orders: the snapshot first, then every event applied once.
// After a reconnect the SDK resumes from the last event; a gap it cannot cover is told (OnGap). Ctrl+C to stop.
//
//	go run ./examples/08-account-stream-mirror
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"os/signal"
	"strings"

	phoenixperps "github.com/phoenixperps/sdk/go"
)

type row = map[string]any

type copyOf struct {
	positions map[string]row // by market
	orders    map[string]row // by id
}

var final = map[string]bool{"filled": true, "canceled": true, "rejected": true, "expired": true}

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt)
	defer stop()
	client, err := phoenixperps.NewClient()
	if err != nil {
		log.Fatal(err)
	}
	accounts, err := client.Accounts.List(ctx)
	if err != nil {
		log.Fatal(err)
	}
	stream := client.AccountStream(ctx, phoenixperps.AccountStreamOptions{OnGap: func(after string) {
		fmt.Printf("[gap] events after %s may be missing from the copy: client.Events.List after it has them\n", after)
	}})
	defer stream.Close()
	select {
	case <-stream.Ready():
	case <-ctx.Done():
		return
	}
	mirror := map[string]*copyOf{}
	for _, a := range accounts.Data {
		var snap struct {
			Positions, Orders []row
			Error             string // the account could not be read now: its snapshot is empty, not flat
		}
		if json.Unmarshal(stream.Snapshot(a.ID), &snap) != nil {
			continue
		}
		if snap.Error != "" {
			fmt.Printf("[snapshot] %s: not read (%s); read it again by REST\n", a.ID, snap.Error)
			continue
		}
		c := &copyOf{positions: map[string]row{}, orders: map[string]row{}}
		for _, p := range snap.Positions {
			c.positions[fmt.Sprint(p["market"])] = p
		}
		for _, o := range snap.Orders {
			c.orders[fmt.Sprint(o["id"])] = o
		}
		mirror[a.ID] = c
	}
	show(mirror, "snapshot")
	for {
		e, err := stream.Next(ctx)
		if err != nil {
			if ctx.Err() != nil {
				return
			}
			log.Fatal(err)
		}
		c := mirror[e.AccountID]
		if c == nil {
			continue
		}
		if o, ok := e.Data["order"].(map[string]any); ok {
			id := fmt.Sprint(o["id"])
			if final[fmt.Sprint(o["status"])] {
				delete(c.orders, id)
			} else {
				c.orders[id] = o
			}
		}
		if p, ok := e.Data["position"].(map[string]any); ok {
			m := fmt.Sprint(p["market"])
			if e.Type == "position.closed" {
				delete(c.positions, m)
			} else {
				c.positions[m] = p
			}
		}
		show(mirror, string(e.Type))
	}
}

func show(mirror map[string]*copyOf, why string) {
	for id, c := range mirror {
		var pos []string
		for _, p := range c.positions {
			pos = append(pos, fmt.Sprintf("%v %v %v", p["market"], p["side"], p["size"]))
		}
		if len(pos) == 0 {
			pos = []string{"none"}
		}
		fmt.Printf("[%s] %s: positions %s; %d working orders\n", why, id, strings.Join(pos, ", "), len(c.orders))
	}
}
