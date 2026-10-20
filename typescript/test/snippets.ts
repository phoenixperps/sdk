// Generated from the API spec by generator/printers/snippets.js. Do not edit: run `node generator`.
// Every docs snippet, type-checked (npm run typecheck); never run.
import { PhoenixPerps } from '..';

export async function getApi(client: PhoenixPerps): Promise<void> {
  const platform = await client.platform.info();
  console.log(platform);
}

export async function getSpec(client: PhoenixPerps): Promise<void> {
  const result = await client.platform.spec();
  console.log(result);
}

export async function getStatus(client: PhoenixPerps): Promise<void> {
  const status = await client.platform.status();
  console.log(status);
}

export async function getLimits(client: PhoenixPerps): Promise<void> {
  const limits = await client.platform.limits();
  console.log(limits);
}

export async function getMe(client: PhoenixPerps): Promise<void> {
  const me = await client.me.get();
  console.log(me);
}

export async function listMarkets(client: PhoenixPerps): Promise<void> {
  const { data } = await client.markets.list();
  for (const market of data) console.log(market.market);
}

export async function listMarketStats(client: PhoenixPerps): Promise<void> {
  const { data } = await client.markets.stats();
  for (const marketStats of data) console.log(marketStats.market);
}

export async function getMarket(client: PhoenixPerps): Promise<void> {
  const market = await client.markets.get('BTC-USD');
  console.log(market);
}

export async function listAccounts(client: PhoenixPerps): Promise<void> {
  const { data } = await client.accounts.list();
  for (const account of data) console.log(account.id);
}

export async function createSandboxAccount(client: PhoenixPerps): Promise<void> {
  const account = await client.accounts.createSandbox();
  console.log(account);
}

export async function getAccount(client: PhoenixPerps): Promise<void> {
  const accountDetail = await client.accounts.get('SANDBOX-00042-001');
  console.log(accountDetail);
}

export async function getRisk(client: PhoenixPerps): Promise<void> {
  const accountRisk = await client.accounts.risk('SANDBOX-00042-001');
  console.log(accountRisk);
}

export async function getTradingPolicy(client: PhoenixPerps): Promise<void> {
  const tradingPolicy = await client.accounts.tradingPolicy('SANDBOX-00042-001');
  console.log(tradingPolicy);
}

export async function getProgress(client: PhoenixPerps): Promise<void> {
  const progress = await client.accounts.progress('SANDBOX-00042-001');
  console.log(progress);
}

export async function getEquityHistory(client: PhoenixPerps): Promise<void> {
  const equityHistory = await client.history.equity('SANDBOX-00042-001');
  console.log(equityHistory);
}

export async function listDailyResults(client: PhoenixPerps): Promise<void> {
  const { data } = await client.history.daily('SANDBOX-00042-001');
  for (const dailyResult of data) console.log(dailyResult);
}

export async function listMarginSettings(client: PhoenixPerps): Promise<void> {
  const { data } = await client.margin.list('SANDBOX-00042-001');
  for (const marginSetting of data) console.log(marginSetting.market);
}

export async function listPositions(client: PhoenixPerps): Promise<void> {
  const { data } = await client.positions.list('SANDBOX-00042-001');
  for (const position of data) console.log(position.id);
}

export async function getPosition(client: PhoenixPerps): Promise<void> {
  const position = await client.positions.get('SANDBOX-00042-001', 'BTC-USD');
  console.log(position);
}

export async function listClosedPositions(client: PhoenixPerps): Promise<void> {
  for await (const closedPosition of client.positions.listClosed('SANDBOX-00042-001')) {
    console.log(closedPosition.market);
  }
}

export async function listOrders(client: PhoenixPerps): Promise<void> {
  for await (const order of client.orders.list('SANDBOX-00042-001')) {
    console.log(order.id);
  }
}

export async function placeOrder(client: PhoenixPerps): Promise<void> {
  const order = await client.orders.create('SANDBOX-00042-001', {
    market: 'BTC-USD',
    side: 'buy',
    type: 'market',
    size: '0.01',
    max_slippage_pct: '0.5',
    client_order_id: 'momentum-0142',
    exits: {
      take_profit: [
        {
          distance: '600',
          size_pct: '100',
        },
      ],
      stop_loss: {
        distance: '300',
      },
    },
  });
  console.log(order);
}

export async function getOrder(client: PhoenixPerps): Promise<void> {
  const order = await client.orders.get('SANDBOX-00042-001', 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(order);
}

export async function modifyOrder(client: PhoenixPerps): Promise<void> {
  const order = await client.orders.modify('SANDBOX-00042-001', 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W', {
    limit_price: '61900',
  });
  console.log(order);
}

export async function cancelOrder(client: PhoenixPerps): Promise<void> {
  const order = await client.orders.cancel('SANDBOX-00042-001', 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(order);
}

export async function testOrder(client: PhoenixPerps): Promise<void> {
  const whatIf = await client.orders.preview('SANDBOX-00042-001', {
    market: 'BTC-USD',
    side: 'buy',
    type: 'market',
    size: '0.01',
    max_slippage_pct: '0.5',
  });
  console.log(whatIf);
}

export async function cancelAllOrders(client: PhoenixPerps): Promise<void> {
  const cancelAllResult = await client.orders.cancelAll('SANDBOX-00042-001', {
    market: 'BTC-USD',
  });
  console.log(cancelAllResult);
}

export async function closePosition(client: PhoenixPerps): Promise<void> {
  const order = await client.positions.close('SANDBOX-00042-001', 'BTC-USD', {
    size: '0.005',
  });
  console.log(order);
}

export async function flatten(client: PhoenixPerps): Promise<void> {
  const flattenResult = await client.positions.flatten('SANDBOX-00042-001');
  console.log(flattenResult);
}

export async function setMargin(client: PhoenixPerps): Promise<void> {
  const marginSettingResult = await client.margin.set('SANDBOX-00042-001', 'BTC-USD', {
    margin_mode: 'isolated',
    leverage: 5,
  });
  console.log(marginSettingResult);
}

export async function listEvents(client: PhoenixPerps): Promise<void> {
  for await (const event of client.events.list()) {
    console.log(event.id);
  }
}

export async function getExits(client: PhoenixPerps): Promise<void> {
  const exits = await client.exits.get('SANDBOX-00042-001', 'BTC-USD');
  console.log(exits);
}

export async function setExits(client: PhoenixPerps): Promise<void> {
  const exits = await client.exits.set('SANDBOX-00042-001', 'BTC-USD', {
    expected_position: {
      side: 'long',
      size: '0.01',
    },
    expected_exits: [
      {
        id: 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W',
        size: '0.01',
        price: '61700',
      },
    ],
    operations: [
      {
        op: 'modify',
        id: 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W',
        price: '61900',
      },
      {
        op: 'place',
        group: 'take_profit',
        type: 'limit',
        price: '63000',
        size: '0.01',
      },
    ],
  });
  console.log(exits);
}

export async function setOrderExits(client: PhoenixPerps): Promise<void> {
  const order = await client.orders.setExits('SANDBOX-00042-001', 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W', {
    take_profit: [
      {
        distance: '600',
        size_pct: '50',
      },
      {
        distance: '1200',
        size_pct: '50',
      },
    ],
    stop_loss: {
      distance: '300',
      type: 'stop_market',
    },
  });
  console.log(order);
}

export async function createStrategy(client: PhoenixPerps): Promise<void> {
  const strategy = await client.strategies.create('SANDBOX-00042-001', {
    type: 'twap',
    market: 'BTC-USD',
    side: 'buy',
    size: '0.05',
    duration_minutes: 30,
    interval_seconds: 60,
    client_strategy_id: 'twap-07',
  });
  console.log(strategy);
}

export async function listStrategies(client: PhoenixPerps): Promise<void> {
  for await (const strategy of client.strategies.list('SANDBOX-00042-001')) {
    console.log(strategy.id);
  }
}

export async function getStrategy(client: PhoenixPerps): Promise<void> {
  const strategy = await client.strategies.get('SANDBOX-00042-001', 'stg_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(strategy);
}

export async function cancelStrategy(client: PhoenixPerps): Promise<void> {
  const strategy = await client.strategies.cancel('SANDBOX-00042-001', 'stg_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(strategy);
}

export async function listWebhookEndpoints(client: PhoenixPerps): Promise<void> {
  const { data } = await client.webhookEndpoints.list();
  for (const webhookEndpoint of data) console.log(webhookEndpoint.id);
}

export async function getWebhookEndpoint(client: PhoenixPerps): Promise<void> {
  const webhookEndpoint = await client.webhookEndpoints.get('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(webhookEndpoint);
}

export async function deleteWebhookEndpoint(client: PhoenixPerps): Promise<void> {
  const deletedEndpoint = await client.webhookEndpoints.delete('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(deletedEndpoint);
}

export async function testWebhookEndpoint(client: PhoenixPerps): Promise<void> {
  const webhookDelivery = await client.webhookEndpoints.test('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(webhookDelivery);
}

export async function listWebhookDeliveries(client: PhoenixPerps): Promise<void> {
  for await (const webhookDelivery of client.webhookEndpoints.listDeliveries('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W')) {
    console.log(webhookDelivery.id);
  }
}

export async function getWebhookDelivery(client: PhoenixPerps): Promise<void> {
  const webhookDelivery = await client.webhookEndpoints.getDelivery('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W', 'dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(webhookDelivery);
}

export async function resendWebhookDelivery(client: PhoenixPerps): Promise<void> {
  const webhookDelivery = await client.webhookEndpoints.resendDelivery('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W', 'dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(webhookDelivery);
}

export async function disableWebhookEndpoint(client: PhoenixPerps): Promise<void> {
  const webhookEndpoint = await client.webhookEndpoints.disable('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(webhookEndpoint);
}

export async function enableWebhookEndpoint(client: PhoenixPerps): Promise<void> {
  const webhookEndpoint = await client.webhookEndpoints.enable('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(webhookEndpoint);
}

export async function listTradingViewHooks(client: PhoenixPerps): Promise<void> {
  const { data } = await client.tradingview.listHooks();
  for (const tradingViewHook of data) console.log(tradingViewHook.id);
}

export async function getTradingViewHook(client: PhoenixPerps): Promise<void> {
  const tradingViewHook = await client.tradingview.getHook('K7x2mQ9pLr4A');
  console.log(tradingViewHook);
}

export async function deleteTradingViewHook(client: PhoenixPerps): Promise<void> {
  const deletedHook = await client.tradingview.deleteHook('K7x2mQ9pLr4A');
  console.log(deletedHook);
}

export async function listTradingViewAlerts(client: PhoenixPerps): Promise<void> {
  for await (const tradingViewAlert of client.tradingview.listAlerts('K7x2mQ9pLr4A')) {
    console.log(tradingViewAlert.id);
  }
}

export async function getTradingViewAlert(client: PhoenixPerps): Promise<void> {
  const tradingViewAlert = await client.tradingview.getAlert('K7x2mQ9pLr4A', 'tva_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(tradingViewAlert);
}

export async function disableTradingViewHook(client: PhoenixPerps): Promise<void> {
  const tradingViewHook = await client.tradingview.disableHook('K7x2mQ9pLr4A');
  console.log(tradingViewHook);
}

export async function enableTradingViewHook(client: PhoenixPerps): Promise<void> {
  const tradingViewHook = await client.tradingview.enableHook('K7x2mQ9pLr4A');
  console.log(tradingViewHook);
}

export async function parseTradingViewMessage(client: PhoenixPerps): Promise<void> {
  const tradingViewParse = await client.tradingview.parse({
    message: 'buy BTCUSD qty 0.01 tp 1% sl 61500',
  });
  console.log(tradingViewParse);
}

export async function listWebhookEventTypes(client: PhoenixPerps): Promise<void> {
  const { data } = await client.webhookEndpoints.eventTypes();
  for (const webhookEventType of data) console.log(webhookEventType);
}

export async function getEvent(client: PhoenixPerps): Promise<void> {
  const event = await client.events.get('evt_01JAB7Q8XK4M2N5P7R9S3T5V7W');
  console.log(event);
}

export async function listFills(client: PhoenixPerps): Promise<void> {
  for await (const fill of client.fills.list('SANDBOX-00042-001')) {
    console.log(fill.id);
  }
}
