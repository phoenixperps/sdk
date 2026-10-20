// 01: who you are (the key, its access and limits) and your accounts.
//
//	PHOENIXPERPS_API_KEY=ppk_test_... go run ./examples/01-accounts
package main

import (
	"context"
	"fmt"
	"log"

	phoenixperps "github.com/phoenixperps/sdk/go"
)

func main() {
	ctx := context.Background()
	client, err := phoenixperps.NewClient() // the key: PHOENIXPERPS_API_KEY (a sandbox key: the sandbox)
	if err != nil {
		log.Fatal(err)
	}
	me, err := client.Me.Get(ctx)
	if err != nil {
		log.Fatal(err)
	}
	c := me.Credential
	fmt.Printf("%s %q on %s: %s access\n", c.Kind, c.Name, c.Environment, c.Access)
	fmt.Printf("limits: %d reads and %d trading calls a minute\n", me.Budgets.ReadPerMin, me.Budgets.TradePerMin)

	accounts, err := client.Accounts.List(ctx)
	if err != nil {
		log.Fatal(err)
	}
	for _, a := range accounts.Data {
		balance := "-"
		if a.Balance != nil {
			balance = string(*a.Balance)
		}
		fmt.Printf("%s  %s  %s  balance %s\n", a.ID, a.Stage, a.Status, balance)
	}
}
