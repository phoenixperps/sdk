// Generated from the API spec by generator/printers/snippets.js. Do not edit: run `node generator`.

package phoenixperps

// Every docs snippet, compiled with the tests (go vet); never run.

import (
	"context"
	"fmt"
)

func snippetGetApi(ctx context.Context, client *Client) error {
	platform, err := client.Platform.Info(ctx)
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", platform)
	return nil
}

func snippetGetSpec(ctx context.Context, client *Client) error {
	result, err := client.Platform.Spec(ctx)
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", result)
	return nil
}

func snippetGetStatus(ctx context.Context, client *Client) error {
	status, err := client.Platform.Status(ctx)
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", status)
	return nil
}

func snippetGetLimits(ctx context.Context, client *Client) error {
	limits, err := client.Platform.Limits(ctx)
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", limits)
	return nil
}

func snippetGetMe(ctx context.Context, client *Client) error {
	me, err := client.Me.Get(ctx)
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", me)
	return nil
}

func snippetListMarkets(ctx context.Context, client *Client) error {
	page, err := client.Markets.List(ctx)
	if err != nil {
		return err
	}
	for _, market := range page.Data {
		fmt.Println(market.Market)
	}
	return nil
}

func snippetListMarketStats(ctx context.Context, client *Client) error {
	page, err := client.Markets.Stats(ctx)
	if err != nil {
		return err
	}
	for _, marketStats := range page.Data {
		fmt.Println(marketStats.Market)
	}
	return nil
}

func snippetGetMarket(ctx context.Context, client *Client) error {
	market, err := client.Markets.Get(ctx, "BTC-USD")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", market)
	return nil
}

func snippetListAccounts(ctx context.Context, client *Client) error {
	page, err := client.Accounts.List(ctx)
	if err != nil {
		return err
	}
	for _, account := range page.Data {
		fmt.Println(account.ID)
	}
	return nil
}

func snippetCreateSandboxAccount(ctx context.Context, client *Client) error {
	account, err := client.Accounts.CreateSandbox(ctx)
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", account)
	return nil
}

func snippetGetAccount(ctx context.Context, client *Client) error {
	accountDetail, err := client.Accounts.Get(ctx, "SANDBOX-00042-001")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", accountDetail)
	return nil
}

func snippetGetRisk(ctx context.Context, client *Client) error {
	accountRisk, err := client.Accounts.Risk(ctx, "SANDBOX-00042-001")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", accountRisk)
	return nil
}

func snippetGetTradingPolicy(ctx context.Context, client *Client) error {
	tradingPolicy, err := client.Accounts.TradingPolicy(ctx, "SANDBOX-00042-001")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", tradingPolicy)
	return nil
}

func snippetGetProgress(ctx context.Context, client *Client) error {
	progress, err := client.Accounts.Progress(ctx, "SANDBOX-00042-001")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", progress)
	return nil
}

func snippetGetEquityHistory(ctx context.Context, client *Client) error {
	equityHistory, err := client.History.Equity(ctx, "SANDBOX-00042-001", nil)
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", equityHistory)
	return nil
}

func snippetListDailyResults(ctx context.Context, client *Client) error {
	page, err := client.History.Daily(ctx, "SANDBOX-00042-001")
	if err != nil {
		return err
	}
	for _, dailyResult := range page.Data {
		fmt.Println(dailyResult)
	}
	return nil
}

func snippetListMarginSettings(ctx context.Context, client *Client) error {
	page, err := client.Margin.List(ctx, "SANDBOX-00042-001")
	if err != nil {
		return err
	}
	for _, marginSetting := range page.Data {
		fmt.Println(marginSetting.Market)
	}
	return nil
}

func snippetListPositions(ctx context.Context, client *Client) error {
	page, err := client.Positions.List(ctx, "SANDBOX-00042-001", nil)
	if err != nil {
		return err
	}
	for _, position := range page.Data {
		fmt.Println(position.ID)
	}
	return nil
}

func snippetGetPosition(ctx context.Context, client *Client) error {
	position, err := client.Positions.Get(ctx, "SANDBOX-00042-001", "BTC-USD")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", position)
	return nil
}

func snippetListClosedPositions(ctx context.Context, client *Client) error {
	for closedPosition, err := range client.Positions.ListClosedAutoPaging(ctx, "SANDBOX-00042-001", nil) {
		if err != nil {
			return err
		}
		fmt.Println(closedPosition.Market)
	}
	return nil
}

func snippetListOrders(ctx context.Context, client *Client) error {
	for order, err := range client.Orders.ListAutoPaging(ctx, "SANDBOX-00042-001", nil) {
		if err != nil {
			return err
		}
		fmt.Println(order.ID)
	}
	return nil
}

func snippetPlaceOrder(ctx context.Context, client *Client) error {
	order, err := client.Orders.Create(ctx, "SANDBOX-00042-001", CreateOrder{
		Market:         "BTC-USD",
		Side:           OrderSideBuy,
		Type:           CreateOrderTypeMarket,
		Size:           Dec("0.01"),
		MaxSlippagePct: Dec("0.5"),
		ClientOrderID:  Ptr("momentum-0142"),
		Exits: &OrderExits{
			TakeProfit: []OrderExitsTakeProfit{
				{
					Distance: Dec("600"),
					SizePct:  Dec("100"),
				},
			},
			StopLoss: &OrderExitsStopLoss{
				Distance: Dec("300"),
			},
		},
	})
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", order)
	return nil
}

func snippetGetOrder(ctx context.Context, client *Client) error {
	order, err := client.Orders.Get(ctx, "SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", order)
	return nil
}

func snippetModifyOrder(ctx context.Context, client *Client) error {
	order, err := client.Orders.Modify(ctx, "SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W", ModifyOrder{
		LimitPrice: Dec("61900"),
	})
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", order)
	return nil
}

func snippetCancelOrder(ctx context.Context, client *Client) error {
	order, err := client.Orders.Cancel(ctx, "SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", order)
	return nil
}

func snippetTestOrder(ctx context.Context, client *Client) error {
	whatIf, err := client.Orders.Preview(ctx, "SANDBOX-00042-001", CreateOrder{
		Market:         "BTC-USD",
		Side:           OrderSideBuy,
		Type:           CreateOrderTypeMarket,
		Size:           Dec("0.01"),
		MaxSlippagePct: Dec("0.5"),
	})
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", whatIf)
	return nil
}

func snippetCancelAllOrders(ctx context.Context, client *Client) error {
	cancelAllResult, err := client.Orders.CancelAll(ctx, "SANDBOX-00042-001", CancelAll{
		Market: Ptr("BTC-USD"),
	})
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", cancelAllResult)
	return nil
}

func snippetClosePosition(ctx context.Context, client *Client) error {
	order, err := client.Positions.Close(ctx, "SANDBOX-00042-001", "BTC-USD", ClosePosition{
		Size: Dec("0.005"),
	})
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", order)
	return nil
}

func snippetFlatten(ctx context.Context, client *Client) error {
	flattenResult, err := client.Positions.Flatten(ctx, "SANDBOX-00042-001")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", flattenResult)
	return nil
}

func snippetSetMargin(ctx context.Context, client *Client) error {
	marginSettingResult, err := client.Margin.Set(ctx, "SANDBOX-00042-001", "BTC-USD", MarginSettingInput{
		MarginMode: MarginModeIsolated,
		Leverage:   5,
	})
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", marginSettingResult)
	return nil
}

func snippetListEvents(ctx context.Context, client *Client) error {
	for event, err := range client.Events.ListAutoPaging(ctx, nil) {
		if err != nil {
			return err
		}
		fmt.Println(event.ID)
	}
	return nil
}

func snippetGetExits(ctx context.Context, client *Client) error {
	exits, err := client.Exits.Get(ctx, "SANDBOX-00042-001", "BTC-USD")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", exits)
	return nil
}

func snippetSetExits(ctx context.Context, client *Client) error {
	exits, err := client.Exits.Set(ctx, "SANDBOX-00042-001", "BTC-USD", ExitsBatch{
		ExpectedPosition: &ExitsBatchExpectedPosition{
			Side: PositionSideLong,
			Size: "0.01",
		},
		ExpectedExits: []ExitsBatchExpectedExit{
			{
				ID:    "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W",
				Size:  "0.01",
				Price: Dec("61700"),
			},
		},
		Operations: []ExitsBatchOperation{
			{
				Op:    ExitsBatchOperationOpModify,
				ID:    Ptr("ord_01JAB7Q8XK4M2N5P7R9S3T5V7W"),
				Price: Dec("61900"),
			},
			{
				Op:    ExitsBatchOperationOpPlace,
				Group: Ptr(ExitsLegGroupTakeProfit),
				Type:  Ptr(CreateStrategyOrderTypeLimit),
				Price: Dec("63000"),
				Size:  Dec("0.01"),
			},
		},
	})
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", exits)
	return nil
}

func snippetSetOrderExits(ctx context.Context, client *Client) error {
	order, err := client.Orders.SetExits(ctx, "SANDBOX-00042-001", "ord_01JAB7Q8XK4M2N5P7R9S3T5V7W", OrderExits{
		TakeProfit: []OrderExitsTakeProfit{
			{
				Distance: Dec("600"),
				SizePct:  Dec("50"),
			},
			{
				Distance: Dec("1200"),
				SizePct:  Dec("50"),
			},
		},
		StopLoss: &OrderExitsStopLoss{
			Distance: Dec("300"),
			Type:     Ptr(OrderExitsStopLossTypeStopMarket),
		},
	})
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", order)
	return nil
}

func snippetCreateStrategy(ctx context.Context, client *Client) error {
	strategy, err := client.Strategies.Create(ctx, "SANDBOX-00042-001", CreateStrategy{
		Type:             CreateStrategyTypeTWAP,
		Market:           "BTC-USD",
		Side:             Ptr(OrderSideBuy),
		Size:             Dec("0.05"),
		DurationMinutes:  Ptr(int64(30)),
		IntervalSeconds:  Ptr(int64(60)),
		ClientStrategyID: Ptr("twap-07"),
	})
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", strategy)
	return nil
}

func snippetListStrategies(ctx context.Context, client *Client) error {
	for strategy, err := range client.Strategies.ListAutoPaging(ctx, "SANDBOX-00042-001", nil) {
		if err != nil {
			return err
		}
		fmt.Println(strategy.ID)
	}
	return nil
}

func snippetGetStrategy(ctx context.Context, client *Client) error {
	strategy, err := client.Strategies.Get(ctx, "SANDBOX-00042-001", "stg_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", strategy)
	return nil
}

func snippetCancelStrategy(ctx context.Context, client *Client) error {
	strategy, err := client.Strategies.Cancel(ctx, "SANDBOX-00042-001", "stg_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", strategy)
	return nil
}

func snippetListWebhookEndpoints(ctx context.Context, client *Client) error {
	page, err := client.WebhookEndpoints.List(ctx)
	if err != nil {
		return err
	}
	for _, webhookEndpoint := range page.Data {
		fmt.Println(webhookEndpoint.ID)
	}
	return nil
}

func snippetGetWebhookEndpoint(ctx context.Context, client *Client) error {
	webhookEndpoint, err := client.WebhookEndpoints.Get(ctx, "whk_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", webhookEndpoint)
	return nil
}

func snippetDeleteWebhookEndpoint(ctx context.Context, client *Client) error {
	deletedEndpoint, err := client.WebhookEndpoints.Delete(ctx, "whk_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", deletedEndpoint)
	return nil
}

func snippetTestWebhookEndpoint(ctx context.Context, client *Client) error {
	webhookDelivery, err := client.WebhookEndpoints.Test(ctx, "whk_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", webhookDelivery)
	return nil
}

func snippetListWebhookDeliveries(ctx context.Context, client *Client) error {
	for webhookDelivery, err := range client.WebhookEndpoints.ListDeliveriesAutoPaging(ctx, "whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", nil) {
		if err != nil {
			return err
		}
		fmt.Println(webhookDelivery.ID)
	}
	return nil
}

func snippetGetWebhookDelivery(ctx context.Context, client *Client) error {
	webhookDelivery, err := client.WebhookEndpoints.GetDelivery(ctx, "whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", "dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", webhookDelivery)
	return nil
}

func snippetResendWebhookDelivery(ctx context.Context, client *Client) error {
	webhookDelivery, err := client.WebhookEndpoints.ResendDelivery(ctx, "whk_01JAB7Q8XK4M2N5P7R9S3T5V7W", "dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", webhookDelivery)
	return nil
}

func snippetDisableWebhookEndpoint(ctx context.Context, client *Client) error {
	webhookEndpoint, err := client.WebhookEndpoints.Disable(ctx, "whk_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", webhookEndpoint)
	return nil
}

func snippetEnableWebhookEndpoint(ctx context.Context, client *Client) error {
	webhookEndpoint, err := client.WebhookEndpoints.Enable(ctx, "whk_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", webhookEndpoint)
	return nil
}

func snippetListTradingViewHooks(ctx context.Context, client *Client) error {
	page, err := client.TradingView.ListHooks(ctx)
	if err != nil {
		return err
	}
	for _, tradingViewHook := range page.Data {
		fmt.Println(tradingViewHook.ID)
	}
	return nil
}

func snippetGetTradingViewHook(ctx context.Context, client *Client) error {
	tradingViewHook, err := client.TradingView.GetHook(ctx, "K7x2mQ9pLr4A")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", tradingViewHook)
	return nil
}

func snippetDeleteTradingViewHook(ctx context.Context, client *Client) error {
	deletedHook, err := client.TradingView.DeleteHook(ctx, "K7x2mQ9pLr4A")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", deletedHook)
	return nil
}

func snippetListTradingViewAlerts(ctx context.Context, client *Client) error {
	for tradingViewAlert, err := range client.TradingView.ListAlertsAutoPaging(ctx, "K7x2mQ9pLr4A", nil) {
		if err != nil {
			return err
		}
		fmt.Println(tradingViewAlert.ID)
	}
	return nil
}

func snippetGetTradingViewAlert(ctx context.Context, client *Client) error {
	tradingViewAlert, err := client.TradingView.GetAlert(ctx, "K7x2mQ9pLr4A", "tva_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", tradingViewAlert)
	return nil
}

func snippetDisableTradingViewHook(ctx context.Context, client *Client) error {
	tradingViewHook, err := client.TradingView.DisableHook(ctx, "K7x2mQ9pLr4A")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", tradingViewHook)
	return nil
}

func snippetEnableTradingViewHook(ctx context.Context, client *Client) error {
	tradingViewHook, err := client.TradingView.EnableHook(ctx, "K7x2mQ9pLr4A")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", tradingViewHook)
	return nil
}

func snippetParseTradingViewMessage(ctx context.Context, client *Client) error {
	tradingViewParse, err := client.TradingView.Parse(ctx, TradingViewParseInput{
		Message: "buy BTCUSD qty 0.01 tp 1% sl 61500",
	})
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", tradingViewParse)
	return nil
}

func snippetListWebhookEventTypes(ctx context.Context, client *Client) error {
	page, err := client.WebhookEndpoints.EventTypes(ctx)
	if err != nil {
		return err
	}
	for _, webhookEventType := range page.Data {
		fmt.Println(webhookEventType)
	}
	return nil
}

func snippetGetEvent(ctx context.Context, client *Client) error {
	event, err := client.Events.Get(ctx, "evt_01JAB7Q8XK4M2N5P7R9S3T5V7W")
	if err != nil {
		return err
	}
	fmt.Printf("%+v\n", event)
	return nil
}

func snippetListFills(ctx context.Context, client *Client) error {
	for fill, err := range client.Fills.ListAutoPaging(ctx, "SANDBOX-00042-001", nil) {
		if err != nil {
			return err
		}
		fmt.Println(fill.ID)
	}
	return nil
}
