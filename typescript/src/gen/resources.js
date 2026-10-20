// Generated from the API spec by generator/printers/typescript.js. Do not edit: run `node generator`.
'use strict';

const OPS = {
  getApi: {"id":"getApi","http":"GET","path":"/v1","idempotent":false,"retrySafe":true,"scope":"public","paging":null},
  getSpec: {"id":"getSpec","http":"GET","path":"/v1/openapi.json","idempotent":false,"retrySafe":true,"scope":"public","paging":null},
  getStatus: {"id":"getStatus","http":"GET","path":"/v1/status","idempotent":false,"retrySafe":true,"scope":"public","paging":null},
  getLimits: {"id":"getLimits","http":"GET","path":"/v1/limits","idempotent":false,"retrySafe":true,"scope":"public","paging":null},
  getMe: {"id":"getMe","http":"GET","path":"/v1/me","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  listMarkets: {"id":"listMarkets","http":"GET","path":"/v1/markets","idempotent":false,"retrySafe":true,"scope":"public","paging":null},
  listMarketStats: {"id":"listMarketStats","http":"GET","path":"/v1/markets/stats","idempotent":false,"retrySafe":true,"scope":"public","paging":null},
  getMarket: {"id":"getMarket","http":"GET","path":"/v1/markets/{market}","idempotent":false,"retrySafe":true,"scope":"public","paging":null},
  listAccounts: {"id":"listAccounts","http":"GET","path":"/v1/accounts","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  createSandboxAccount: {"id":"createSandboxAccount","http":"POST","path":"/v1/accounts","idempotent":true,"retrySafe":false,"scope":"trade","paging":null},
  getAccount: {"id":"getAccount","http":"GET","path":"/v1/accounts/{account_id}","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  getRisk: {"id":"getRisk","http":"GET","path":"/v1/accounts/{account_id}/risk","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  getTradingPolicy: {"id":"getTradingPolicy","http":"GET","path":"/v1/accounts/{account_id}/trading-policy","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  getProgress: {"id":"getProgress","http":"GET","path":"/v1/accounts/{account_id}/progress","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  getEquityHistory: {"id":"getEquityHistory","http":"GET","path":"/v1/accounts/{account_id}/history/equity","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  listDailyResults: {"id":"listDailyResults","http":"GET","path":"/v1/accounts/{account_id}/history/daily","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  listMarginSettings: {"id":"listMarginSettings","http":"GET","path":"/v1/accounts/{account_id}/margin","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  listPositions: {"id":"listPositions","http":"GET","path":"/v1/accounts/{account_id}/positions","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  getPosition: {"id":"getPosition","http":"GET","path":"/v1/accounts/{account_id}/positions/{market}","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  listClosedPositions: {"id":"listClosedPositions","http":"GET","path":"/v1/accounts/{account_id}/closed-positions","idempotent":false,"retrySafe":true,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
  listOrders: {"id":"listOrders","http":"GET","path":"/v1/accounts/{account_id}/orders","idempotent":false,"retrySafe":true,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
  placeOrder: {"id":"placeOrder","http":"POST","path":"/v1/accounts/{account_id}/orders","idempotent":true,"retrySafe":false,"scope":"trade","paging":null},
  getOrder: {"id":"getOrder","http":"GET","path":"/v1/accounts/{account_id}/orders/{order_id}","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  modifyOrder: {"id":"modifyOrder","http":"PATCH","path":"/v1/accounts/{account_id}/orders/{order_id}","idempotent":false,"retrySafe":false,"scope":"trade","paging":null},
  cancelOrder: {"id":"cancelOrder","http":"DELETE","path":"/v1/accounts/{account_id}/orders/{order_id}","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  testOrder: {"id":"testOrder","http":"POST","path":"/v1/accounts/{account_id}/orders/test","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  cancelAllOrders: {"id":"cancelAllOrders","http":"POST","path":"/v1/accounts/{account_id}/orders/cancel-all","idempotent":true,"retrySafe":false,"scope":"trade","paging":null},
  closePosition: {"id":"closePosition","http":"POST","path":"/v1/accounts/{account_id}/positions/{market}/close","idempotent":true,"retrySafe":false,"scope":"trade","paging":null},
  flatten: {"id":"flatten","http":"POST","path":"/v1/accounts/{account_id}/flatten","idempotent":true,"retrySafe":false,"scope":"trade","paging":null},
  setMargin: {"id":"setMargin","http":"PUT","path":"/v1/accounts/{account_id}/margin/{market}","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  listEvents: {"id":"listEvents","http":"GET","path":"/v1/events","idempotent":false,"retrySafe":true,"scope":"read","paging":{"param":"after","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
  getExits: {"id":"getExits","http":"GET","path":"/v1/accounts/{account_id}/positions/{market}/exits","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  setExits: {"id":"setExits","http":"PUT","path":"/v1/accounts/{account_id}/positions/{market}/exits","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  setOrderExits: {"id":"setOrderExits","http":"PUT","path":"/v1/accounts/{account_id}/orders/{order_id}/exits","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  createStrategy: {"id":"createStrategy","http":"POST","path":"/v1/accounts/{account_id}/strategies","idempotent":true,"retrySafe":false,"scope":"trade","paging":null},
  listStrategies: {"id":"listStrategies","http":"GET","path":"/v1/accounts/{account_id}/strategies","idempotent":false,"retrySafe":true,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
  getStrategy: {"id":"getStrategy","http":"GET","path":"/v1/accounts/{account_id}/strategies/{strategy_id}","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  cancelStrategy: {"id":"cancelStrategy","http":"DELETE","path":"/v1/accounts/{account_id}/strategies/{strategy_id}","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  listWebhookEndpoints: {"id":"listWebhookEndpoints","http":"GET","path":"/v1/webhook-endpoints","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  getWebhookEndpoint: {"id":"getWebhookEndpoint","http":"GET","path":"/v1/webhook-endpoints/{endpoint_id}","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  deleteWebhookEndpoint: {"id":"deleteWebhookEndpoint","http":"DELETE","path":"/v1/webhook-endpoints/{endpoint_id}","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  testWebhookEndpoint: {"id":"testWebhookEndpoint","http":"POST","path":"/v1/webhook-endpoints/{endpoint_id}/test","idempotent":false,"retrySafe":false,"scope":"trade","paging":null},
  listWebhookDeliveries: {"id":"listWebhookDeliveries","http":"GET","path":"/v1/webhook-endpoints/{endpoint_id}/deliveries","idempotent":false,"retrySafe":true,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
  getWebhookDelivery: {"id":"getWebhookDelivery","http":"GET","path":"/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  resendWebhookDelivery: {"id":"resendWebhookDelivery","http":"POST","path":"/v1/webhook-endpoints/{endpoint_id}/deliveries/{delivery_id}/resend","idempotent":false,"retrySafe":false,"scope":"trade","paging":null},
  disableWebhookEndpoint: {"id":"disableWebhookEndpoint","http":"POST","path":"/v1/webhook-endpoints/{endpoint_id}/disable","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  enableWebhookEndpoint: {"id":"enableWebhookEndpoint","http":"POST","path":"/v1/webhook-endpoints/{endpoint_id}/enable","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  listTradingViewHooks: {"id":"listTradingViewHooks","http":"GET","path":"/v1/tradingview/hooks","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  getTradingViewHook: {"id":"getTradingViewHook","http":"GET","path":"/v1/tradingview/hooks/{hook_id}","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  deleteTradingViewHook: {"id":"deleteTradingViewHook","http":"DELETE","path":"/v1/tradingview/hooks/{hook_id}","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  listTradingViewAlerts: {"id":"listTradingViewAlerts","http":"GET","path":"/v1/tradingview/hooks/{hook_id}/alerts","idempotent":false,"retrySafe":true,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
  getTradingViewAlert: {"id":"getTradingViewAlert","http":"GET","path":"/v1/tradingview/hooks/{hook_id}/alerts/{alert_id}","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  disableTradingViewHook: {"id":"disableTradingViewHook","http":"POST","path":"/v1/tradingview/hooks/{hook_id}/disable","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  enableTradingViewHook: {"id":"enableTradingViewHook","http":"POST","path":"/v1/tradingview/hooks/{hook_id}/enable","idempotent":false,"retrySafe":true,"scope":"trade","paging":null},
  parseTradingViewMessage: {"id":"parseTradingViewMessage","http":"POST","path":"/v1/tradingview/parse","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  listWebhookEventTypes: {"id":"listWebhookEventTypes","http":"GET","path":"/v1/webhook-event-types","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  getEvent: {"id":"getEvent","http":"GET","path":"/v1/events/{event_id}","idempotent":false,"retrySafe":true,"scope":"read","paging":null},
  listFills: {"id":"listFills","http":"GET","path":"/v1/accounts/{account_id}/fills","idempotent":false,"retrySafe":true,"scope":"read","paging":{"param":"cursor","items":"data","next":"next_cursor","has_more":"has_more","limit":"limit"}},
};

/** The resources of a client: each method sends its operation through the client's transport. */
function resources(c) {
  return {
    platform: {
      /** Get the API */
      info: (opts) => c._one(OPS.getApi, {}, opts),
      /** Get the OpenAPI document */
      spec: (opts) => c._one(OPS.getSpec, {}, opts),
      /** Get the status */
      status: (opts) => c._one(OPS.getStatus, {}, opts),
      /** Get the limits */
      limits: (opts) => c._one(OPS.getLimits, {}, opts),
    },
    me: {
      /** Get your key */
      get: (opts) => c._one(OPS.getMe, {}, opts),
    },
    markets: {
      /** List markets */
      list: (opts) => c._page(OPS.listMarkets, {}, opts),
      /** List market stats */
      stats: (opts) => c._page(OPS.listMarketStats, {}, opts),
      /** Get a market */
      get: (market, opts) => c._one(OPS.getMarket, { path: { market: market } }, opts),
    },
    accounts: {
      /** List accounts */
      list: (opts) => c._page(OPS.listAccounts, {}, opts),
      /** Open a sandbox account */
      createSandbox: (opts) => c._one(OPS.createSandboxAccount, {}, opts),
      /** Get an account */
      get: (accountId, opts) => c._one(OPS.getAccount, { path: { account_id: accountId } }, opts),
      /** Get live risk */
      risk: (accountId, opts) => c._one(OPS.getRisk, { path: { account_id: accountId } }, opts),
      /** Get the trading policy */
      tradingPolicy: (accountId, opts) => c._one(OPS.getTradingPolicy, { path: { account_id: accountId } }, opts),
      /** Get progress */
      progress: (accountId, opts) => c._one(OPS.getProgress, { path: { account_id: accountId } }, opts),
    },
    history: {
      /** Get the equity curve */
      equity: (accountId, query, opts) => c._one(OPS.getEquityHistory, { path: { account_id: accountId }, query }, opts),
      /** List daily results */
      daily: (accountId, opts) => c._page(OPS.listDailyResults, { path: { account_id: accountId } }, opts),
    },
    margin: {
      /** List margin settings */
      list: (accountId, opts) => c._page(OPS.listMarginSettings, { path: { account_id: accountId } }, opts),
      /** Set leverage and margin mode */
      set: (accountId, market, body, opts) => c._one(OPS.setMargin, { path: { account_id: accountId, market: market }, body }, opts),
    },
    positions: {
      /** List open positions */
      list: (accountId, query, opts) => c._page(OPS.listPositions, { path: { account_id: accountId }, query }, opts),
      /** Get an open position */
      get: (accountId, market, opts) => c._one(OPS.getPosition, { path: { account_id: accountId, market: market } }, opts),
      /** List closed positions */
      listClosed: (accountId, query, opts) => c._page(OPS.listClosedPositions, { path: { account_id: accountId }, query }, opts),
      /** Close a position */
      close: (accountId, market, body, opts) => c._one(OPS.closePosition, { path: { account_id: accountId, market: market }, body }, opts),
      /** Flatten the account */
      flatten: (accountId, opts) => c._one(OPS.flatten, { path: { account_id: accountId } }, opts),
    },
    orders: {
      /** List orders */
      list: (accountId, query, opts) => c._page(OPS.listOrders, { path: { account_id: accountId }, query }, opts),
      /** Place an order */
      create: (accountId, body, opts) => c._one(OPS.placeOrder, { path: { account_id: accountId }, body }, opts),
      /** Get an order */
      get: (accountId, orderId, opts) => c._one(OPS.getOrder, { path: { account_id: accountId, order_id: orderId } }, opts),
      /** Change an order */
      modify: (accountId, orderId, body, opts) => c._one(OPS.modifyOrder, { path: { account_id: accountId, order_id: orderId }, body }, opts),
      /** Cancel an order */
      cancel: (accountId, orderId, opts) => c._one(OPS.cancelOrder, { path: { account_id: accountId, order_id: orderId } }, opts),
      /** Preview an order */
      preview: (accountId, body, opts) => c._one(OPS.testOrder, { path: { account_id: accountId }, body }, opts),
      /** Cancel all orders */
      cancelAll: (accountId, body, opts) => c._one(OPS.cancelAllOrders, { path: { account_id: accountId }, body }, opts),
      /** Attach exits to an order */
      setExits: (accountId, orderId, body, opts) => c._one(OPS.setOrderExits, { path: { account_id: accountId, order_id: orderId }, body }, opts),
    },
    events: {
      /** List events */
      list: (query, opts) => c._page(OPS.listEvents, { query }, opts),
      /** Get an event */
      get: (eventId, opts) => c._one(OPS.getEvent, { path: { event_id: eventId } }, opts),
    },
    exits: {
      /** Get a position's exits */
      get: (accountId, market, opts) => c._one(OPS.getExits, { path: { account_id: accountId, market: market } }, opts),
      /** Change a position's exits */
      set: (accountId, market, body, opts) => c._one(OPS.setExits, { path: { account_id: accountId, market: market }, body }, opts),
    },
    strategies: {
      /** Create a strategy */
      create: (accountId, body, opts) => c._one(OPS.createStrategy, { path: { account_id: accountId }, body }, opts),
      /** List strategies */
      list: (accountId, query, opts) => c._page(OPS.listStrategies, { path: { account_id: accountId }, query }, opts),
      /** Get a strategy */
      get: (accountId, strategyId, opts) => c._one(OPS.getStrategy, { path: { account_id: accountId, strategy_id: strategyId } }, opts),
      /** Cancel a strategy */
      cancel: (accountId, strategyId, opts) => c._one(OPS.cancelStrategy, { path: { account_id: accountId, strategy_id: strategyId } }, opts),
    },
    webhookEndpoints: {
      /** List webhook endpoints */
      list: (opts) => c._page(OPS.listWebhookEndpoints, {}, opts),
      /** Get a webhook endpoint */
      get: (endpointId, opts) => c._one(OPS.getWebhookEndpoint, { path: { endpoint_id: endpointId } }, opts),
      /** Delete an endpoint */
      delete: (endpointId, opts) => c._one(OPS.deleteWebhookEndpoint, { path: { endpoint_id: endpointId } }, opts),
      /** Send a test */
      test: (endpointId, opts) => c._one(OPS.testWebhookEndpoint, { path: { endpoint_id: endpointId } }, opts),
      /** List deliveries */
      listDeliveries: (endpointId, query, opts) => c._page(OPS.listWebhookDeliveries, { path: { endpoint_id: endpointId }, query }, opts),
      /** Get a delivery */
      getDelivery: (endpointId, deliveryId, opts) => c._one(OPS.getWebhookDelivery, { path: { endpoint_id: endpointId, delivery_id: deliveryId } }, opts),
      /** Send a delivery again */
      resendDelivery: (endpointId, deliveryId, opts) => c._one(OPS.resendWebhookDelivery, { path: { endpoint_id: endpointId, delivery_id: deliveryId } }, opts),
      /** Pause an endpoint */
      disable: (endpointId, opts) => c._one(OPS.disableWebhookEndpoint, { path: { endpoint_id: endpointId } }, opts),
      /** Resume an endpoint */
      enable: (endpointId, opts) => c._one(OPS.enableWebhookEndpoint, { path: { endpoint_id: endpointId } }, opts),
      /** List event types */
      eventTypes: (opts) => c._page(OPS.listWebhookEventTypes, {}, opts),
    },
    tradingview: {
      /** List TradingView links */
      listHooks: (opts) => c._page(OPS.listTradingViewHooks, {}, opts),
      /** Get a TradingView link */
      getHook: (hookId, opts) => c._one(OPS.getTradingViewHook, { path: { hook_id: hookId } }, opts),
      /** Revoke a TradingView link */
      deleteHook: (hookId, opts) => c._one(OPS.deleteTradingViewHook, { path: { hook_id: hookId } }, opts),
      /** List a link's alerts */
      listAlerts: (hookId, query, opts) => c._page(OPS.listTradingViewAlerts, { path: { hook_id: hookId }, query }, opts),
      /** Get an alert */
      getAlert: (hookId, alertId, opts) => c._one(OPS.getTradingViewAlert, { path: { hook_id: hookId, alert_id: alertId } }, opts),
      /** Pause a TradingView link */
      disableHook: (hookId, opts) => c._one(OPS.disableTradingViewHook, { path: { hook_id: hookId } }, opts),
      /** Resume a TradingView link */
      enableHook: (hookId, opts) => c._one(OPS.enableTradingViewHook, { path: { hook_id: hookId } }, opts),
      /** Read an alert message */
      parse: (body, opts) => c._one(OPS.parseTradingViewMessage, { body }, opts),
    },
    fills: {
      /** List fills */
      list: (accountId, query, opts) => c._page(OPS.listFills, { path: { account_id: accountId }, query }, opts),
    },
  };
}

module.exports = { OPS, resources };
