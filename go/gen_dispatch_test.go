// Code generated from the API spec by generator/printers/go.js. DO NOT EDIT: run `node generator`.

package phoenixperps

import (
	"context"
	"fmt"
)

// dispatch makes the call a conformance scenario names (resource + method) through the typed client.
func dispatch(ctx context.Context, c *Client, call confCall) (*RawResponse, []string, error) {
	var raw RawResponse
	opts := []CallOption{WithRawResponse(&raw)}
	switch call.Resource + "." + call.Method {
	case "platform.info":
		_, err := c.Platform.Info(ctx, opts...)
		return &raw, nil, err
	case "platform.spec":
		_, err := c.Platform.Spec(ctx, opts...)
		return &raw, nil, err
	case "platform.status":
		_, err := c.Platform.Status(ctx, opts...)
		return &raw, nil, err
	case "platform.limits":
		_, err := c.Platform.Limits(ctx, opts...)
		return &raw, nil, err
	case "me.get":
		_, err := c.Me.Get(ctx, opts...)
		return &raw, nil, err
	case "markets.list":
		_, err := c.Markets.List(ctx, opts...)
		return &raw, nil, err
	case "markets.stats":
		_, err := c.Markets.Stats(ctx, opts...)
		return &raw, nil, err
	case "markets.get":
		_, err := c.Markets.Get(ctx, call.Path["market"], opts...)
		return &raw, nil, err
	case "accounts.list":
		_, err := c.Accounts.List(ctx, opts...)
		return &raw, nil, err
	case "accounts.createSandbox":
		_, err := c.Accounts.CreateSandbox(ctx, opts...)
		return &raw, nil, err
	case "accounts.get":
		_, err := c.Accounts.Get(ctx, call.Path["account_id"], opts...)
		return &raw, nil, err
	case "accounts.risk":
		_, err := c.Accounts.Risk(ctx, call.Path["account_id"], opts...)
		return &raw, nil, err
	case "accounts.tradingPolicy":
		_, err := c.Accounts.TradingPolicy(ctx, call.Path["account_id"], opts...)
		return &raw, nil, err
	case "accounts.progress":
		_, err := c.Accounts.Progress(ctx, call.Path["account_id"], opts...)
		return &raw, nil, err
	case "history.equity":
		var params *HistoryEquityParams
		if call.Query != nil {
			params = new(HistoryEquityParams)
			if err := into(call.Query, params); err != nil {
				return nil, nil, err
			}
		}
		_, err := c.History.Equity(ctx, call.Path["account_id"], params, opts...)
		return &raw, nil, err
	case "history.daily":
		_, err := c.History.Daily(ctx, call.Path["account_id"], opts...)
		return &raw, nil, err
	case "margin.list":
		_, err := c.Margin.List(ctx, call.Path["account_id"], opts...)
		return &raw, nil, err
	case "positions.list":
		var params *PositionsListParams
		if call.Query != nil {
			params = new(PositionsListParams)
			if err := into(call.Query, params); err != nil {
				return nil, nil, err
			}
		}
		_, err := c.Positions.List(ctx, call.Path["account_id"], params, opts...)
		return &raw, nil, err
	case "positions.get":
		_, err := c.Positions.Get(ctx, call.Path["account_id"], call.Path["market"], opts...)
		return &raw, nil, err
	case "positions.listClosed":
		var params *PositionsListClosedParams
		if call.Query != nil {
			params = new(PositionsListClosedParams)
			if err := into(call.Query, params); err != nil {
				return nil, nil, err
			}
		}
		if call.Iterate {
			ids, err := collect(c.Positions.ListClosedAutoPaging(ctx, call.Path["account_id"], params, append(opts, WithMaxItems(call.MaxItems))...))
			return &raw, ids, err
		}
		_, err := c.Positions.ListClosed(ctx, call.Path["account_id"], params, opts...)
		return &raw, nil, err
	case "orders.list":
		var params *OrdersListParams
		if call.Query != nil {
			params = new(OrdersListParams)
			if err := into(call.Query, params); err != nil {
				return nil, nil, err
			}
		}
		if call.Iterate {
			ids, err := collect(c.Orders.ListAutoPaging(ctx, call.Path["account_id"], params, append(opts, WithMaxItems(call.MaxItems))...))
			return &raw, ids, err
		}
		_, err := c.Orders.List(ctx, call.Path["account_id"], params, opts...)
		return &raw, nil, err
	case "orders.create":
		body := new(CreateOrder)
		if err := into(call.Body, body); err != nil {
			return nil, nil, err
		}
		_, err := c.Orders.Create(ctx, call.Path["account_id"], *body, opts...)
		return &raw, nil, err
	case "orders.get":
		_, err := c.Orders.Get(ctx, call.Path["account_id"], call.Path["order_id"], opts...)
		return &raw, nil, err
	case "orders.modify":
		body := new(ModifyOrder)
		if err := into(call.Body, body); err != nil {
			return nil, nil, err
		}
		_, err := c.Orders.Modify(ctx, call.Path["account_id"], call.Path["order_id"], *body, opts...)
		return &raw, nil, err
	case "orders.cancel":
		_, err := c.Orders.Cancel(ctx, call.Path["account_id"], call.Path["order_id"], opts...)
		return &raw, nil, err
	case "orders.preview":
		body := new(CreateOrder)
		if err := into(call.Body, body); err != nil {
			return nil, nil, err
		}
		_, err := c.Orders.Preview(ctx, call.Path["account_id"], *body, opts...)
		return &raw, nil, err
	case "orders.cancelAll":
		body := new(CancelAll)
		if err := into(call.Body, body); err != nil {
			return nil, nil, err
		}
		_, err := c.Orders.CancelAll(ctx, call.Path["account_id"], *body, opts...)
		return &raw, nil, err
	case "positions.close":
		body := new(ClosePosition)
		if err := into(call.Body, body); err != nil {
			return nil, nil, err
		}
		_, err := c.Positions.Close(ctx, call.Path["account_id"], call.Path["market"], *body, opts...)
		return &raw, nil, err
	case "positions.flatten":
		_, err := c.Positions.Flatten(ctx, call.Path["account_id"], opts...)
		return &raw, nil, err
	case "margin.set":
		body := new(MarginSettingInput)
		if err := into(call.Body, body); err != nil {
			return nil, nil, err
		}
		_, err := c.Margin.Set(ctx, call.Path["account_id"], call.Path["market"], *body, opts...)
		return &raw, nil, err
	case "events.list":
		var params *EventsListParams
		if call.Query != nil {
			params = new(EventsListParams)
			if err := into(call.Query, params); err != nil {
				return nil, nil, err
			}
		}
		if call.Iterate {
			ids, err := collect(c.Events.ListAutoPaging(ctx, params, append(opts, WithMaxItems(call.MaxItems))...))
			return &raw, ids, err
		}
		_, err := c.Events.List(ctx, params, opts...)
		return &raw, nil, err
	case "exits.get":
		_, err := c.Exits.Get(ctx, call.Path["account_id"], call.Path["market"], opts...)
		return &raw, nil, err
	case "exits.set":
		body := new(ExitsBatch)
		if err := into(call.Body, body); err != nil {
			return nil, nil, err
		}
		_, err := c.Exits.Set(ctx, call.Path["account_id"], call.Path["market"], *body, opts...)
		return &raw, nil, err
	case "orders.setExits":
		body := new(OrderExits)
		if err := into(call.Body, body); err != nil {
			return nil, nil, err
		}
		_, err := c.Orders.SetExits(ctx, call.Path["account_id"], call.Path["order_id"], *body, opts...)
		return &raw, nil, err
	case "strategies.create":
		body := new(CreateStrategy)
		if err := into(call.Body, body); err != nil {
			return nil, nil, err
		}
		_, err := c.Strategies.Create(ctx, call.Path["account_id"], *body, opts...)
		return &raw, nil, err
	case "strategies.list":
		var params *StrategiesListParams
		if call.Query != nil {
			params = new(StrategiesListParams)
			if err := into(call.Query, params); err != nil {
				return nil, nil, err
			}
		}
		if call.Iterate {
			ids, err := collect(c.Strategies.ListAutoPaging(ctx, call.Path["account_id"], params, append(opts, WithMaxItems(call.MaxItems))...))
			return &raw, ids, err
		}
		_, err := c.Strategies.List(ctx, call.Path["account_id"], params, opts...)
		return &raw, nil, err
	case "strategies.get":
		_, err := c.Strategies.Get(ctx, call.Path["account_id"], call.Path["strategy_id"], opts...)
		return &raw, nil, err
	case "strategies.cancel":
		_, err := c.Strategies.Cancel(ctx, call.Path["account_id"], call.Path["strategy_id"], opts...)
		return &raw, nil, err
	case "webhookEndpoints.list":
		_, err := c.WebhookEndpoints.List(ctx, opts...)
		return &raw, nil, err
	case "webhookEndpoints.get":
		_, err := c.WebhookEndpoints.Get(ctx, call.Path["endpoint_id"], opts...)
		return &raw, nil, err
	case "webhookEndpoints.delete":
		_, err := c.WebhookEndpoints.Delete(ctx, call.Path["endpoint_id"], opts...)
		return &raw, nil, err
	case "webhookEndpoints.test":
		_, err := c.WebhookEndpoints.Test(ctx, call.Path["endpoint_id"], opts...)
		return &raw, nil, err
	case "webhookEndpoints.listDeliveries":
		var params *WebhookEndpointsListDeliveriesParams
		if call.Query != nil {
			params = new(WebhookEndpointsListDeliveriesParams)
			if err := into(call.Query, params); err != nil {
				return nil, nil, err
			}
		}
		if call.Iterate {
			ids, err := collect(c.WebhookEndpoints.ListDeliveriesAutoPaging(ctx, call.Path["endpoint_id"], params, append(opts, WithMaxItems(call.MaxItems))...))
			return &raw, ids, err
		}
		_, err := c.WebhookEndpoints.ListDeliveries(ctx, call.Path["endpoint_id"], params, opts...)
		return &raw, nil, err
	case "webhookEndpoints.getDelivery":
		_, err := c.WebhookEndpoints.GetDelivery(ctx, call.Path["endpoint_id"], call.Path["delivery_id"], opts...)
		return &raw, nil, err
	case "webhookEndpoints.resendDelivery":
		_, err := c.WebhookEndpoints.ResendDelivery(ctx, call.Path["endpoint_id"], call.Path["delivery_id"], opts...)
		return &raw, nil, err
	case "webhookEndpoints.disable":
		_, err := c.WebhookEndpoints.Disable(ctx, call.Path["endpoint_id"], opts...)
		return &raw, nil, err
	case "webhookEndpoints.enable":
		_, err := c.WebhookEndpoints.Enable(ctx, call.Path["endpoint_id"], opts...)
		return &raw, nil, err
	case "tradingview.listHooks":
		_, err := c.TradingView.ListHooks(ctx, opts...)
		return &raw, nil, err
	case "tradingview.getHook":
		_, err := c.TradingView.GetHook(ctx, call.Path["hook_id"], opts...)
		return &raw, nil, err
	case "tradingview.deleteHook":
		_, err := c.TradingView.DeleteHook(ctx, call.Path["hook_id"], opts...)
		return &raw, nil, err
	case "tradingview.listAlerts":
		var params *TradingviewListAlertsParams
		if call.Query != nil {
			params = new(TradingviewListAlertsParams)
			if err := into(call.Query, params); err != nil {
				return nil, nil, err
			}
		}
		if call.Iterate {
			ids, err := collect(c.TradingView.ListAlertsAutoPaging(ctx, call.Path["hook_id"], params, append(opts, WithMaxItems(call.MaxItems))...))
			return &raw, ids, err
		}
		_, err := c.TradingView.ListAlerts(ctx, call.Path["hook_id"], params, opts...)
		return &raw, nil, err
	case "tradingview.getAlert":
		_, err := c.TradingView.GetAlert(ctx, call.Path["hook_id"], call.Path["alert_id"], opts...)
		return &raw, nil, err
	case "tradingview.disableHook":
		_, err := c.TradingView.DisableHook(ctx, call.Path["hook_id"], opts...)
		return &raw, nil, err
	case "tradingview.enableHook":
		_, err := c.TradingView.EnableHook(ctx, call.Path["hook_id"], opts...)
		return &raw, nil, err
	case "tradingview.parse":
		body := new(TradingViewParseInput)
		if err := into(call.Body, body); err != nil {
			return nil, nil, err
		}
		_, err := c.TradingView.Parse(ctx, *body, opts...)
		return &raw, nil, err
	case "webhookEndpoints.eventTypes":
		_, err := c.WebhookEndpoints.EventTypes(ctx, opts...)
		return &raw, nil, err
	case "events.get":
		_, err := c.Events.Get(ctx, call.Path["event_id"], opts...)
		return &raw, nil, err
	case "fills.list":
		var params *FillsListParams
		if call.Query != nil {
			params = new(FillsListParams)
			if err := into(call.Query, params); err != nil {
				return nil, nil, err
			}
		}
		if call.Iterate {
			ids, err := collect(c.Fills.ListAutoPaging(ctx, call.Path["account_id"], params, append(opts, WithMaxItems(call.MaxItems))...))
			return &raw, ids, err
		}
		_, err := c.Fills.List(ctx, call.Path["account_id"], params, opts...)
		return &raw, nil, err
	}
	return nil, nil, fmt.Errorf("no operation %s.%s", call.Resource, call.Method)
}
