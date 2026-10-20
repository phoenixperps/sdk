<?php

// Generated from the API spec by generator/printers/snippets.js. Do not edit: run `node generator`.
// Every docs snippet, linted with the suite; never run.

declare(strict_types=1);

namespace PhoenixPerps\Tests\Generated;

use PhoenixPerps\Client;

final class Snippets
{
    public static function getApi(Client $client): void
    {
        $platform = $client->platform->info();
        print_r($platform->toArray());
    }

    public static function getSpec(Client $client): void
    {
        $result = $client->platform->spec();
        print_r($result);
    }

    public static function getStatus(Client $client): void
    {
        $status = $client->platform->status();
        print_r($status->toArray());
    }

    public static function getLimits(Client $client): void
    {
        $limits = $client->platform->limits();
        print_r($limits->toArray());
    }

    public static function getMe(Client $client): void
    {
        $me = $client->me->get();
        print_r($me->toArray());
    }

    public static function listMarkets(Client $client): void
    {
        foreach ($client->markets->list()->data as $market) {
            echo $market->market, "\n";
        }
    }

    public static function listMarketStats(Client $client): void
    {
        foreach ($client->markets->stats()->data as $marketStats) {
            echo $marketStats->market, "\n";
        }
    }

    public static function getMarket(Client $client): void
    {
        $market = $client->markets->get('BTC-USD');
        print_r($market->toArray());
    }

    public static function listAccounts(Client $client): void
    {
        foreach ($client->accounts->list()->data as $account) {
            echo $account->id, "\n";
        }
    }

    public static function createSandboxAccount(Client $client): void
    {
        $account = $client->accounts->createSandbox();
        print_r($account->toArray());
    }

    public static function getAccount(Client $client): void
    {
        $accountDetail = $client->accounts->get('SANDBOX-00042-001');
        print_r($accountDetail->toArray());
    }

    public static function getRisk(Client $client): void
    {
        $accountRisk = $client->accounts->risk('SANDBOX-00042-001');
        print_r($accountRisk->toArray());
    }

    public static function getTradingPolicy(Client $client): void
    {
        $tradingPolicy = $client->accounts->tradingPolicy('SANDBOX-00042-001');
        print_r($tradingPolicy->toArray());
    }

    public static function getProgress(Client $client): void
    {
        $progress = $client->accounts->progress('SANDBOX-00042-001');
        print_r($progress->toArray());
    }

    public static function getEquityHistory(Client $client): void
    {
        $equityHistory = $client->history->equity('SANDBOX-00042-001');
        print_r($equityHistory->toArray());
    }

    public static function listDailyResults(Client $client): void
    {
        foreach ($client->history->daily('SANDBOX-00042-001')->data as $dailyResult) {
            echo json_encode($dailyResult), "\n";
        }
    }

    public static function listMarginSettings(Client $client): void
    {
        foreach ($client->margin->list('SANDBOX-00042-001')->data as $marginSetting) {
            echo $marginSetting->market, "\n";
        }
    }

    public static function listPositions(Client $client): void
    {
        foreach ($client->positions->list('SANDBOX-00042-001')->data as $position) {
            echo $position->id, "\n";
        }
    }

    public static function getPosition(Client $client): void
    {
        $position = $client->positions->get('SANDBOX-00042-001', 'BTC-USD');
        print_r($position->toArray());
    }

    public static function listClosedPositions(Client $client): void
    {
        foreach ($client->positions->listClosedAutoPaging('SANDBOX-00042-001') as $closedPosition) {
            echo $closedPosition->market, "\n";
        }
    }

    public static function listOrders(Client $client): void
    {
        foreach ($client->orders->listAutoPaging('SANDBOX-00042-001') as $order) {
            echo $order->id, "\n";
        }
    }

    public static function placeOrder(Client $client): void
    {
        $order = $client->orders->create('SANDBOX-00042-001', [
            'market' => 'BTC-USD',
            'side' => 'buy',
            'type' => 'market',
            'size' => '0.01',
            'max_slippage_pct' => '0.5',
            'client_order_id' => 'momentum-0142',
            'exits' => [
                'take_profit' => [
                    [
                        'distance' => '600',
                        'size_pct' => '100',
                    ],
                ],
                'stop_loss' => [
                    'distance' => '300',
                ],
            ],
        ]);
        print_r($order->toArray());
    }

    public static function getOrder(Client $client): void
    {
        $order = $client->orders->get('SANDBOX-00042-001', 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($order->toArray());
    }

    public static function modifyOrder(Client $client): void
    {
        $order = $client->orders->modify('SANDBOX-00042-001', 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W', [
            'limit_price' => '61900',
        ]);
        print_r($order->toArray());
    }

    public static function cancelOrder(Client $client): void
    {
        $order = $client->orders->cancel('SANDBOX-00042-001', 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($order->toArray());
    }

    public static function testOrder(Client $client): void
    {
        $whatIf = $client->orders->preview('SANDBOX-00042-001', [
            'market' => 'BTC-USD',
            'side' => 'buy',
            'type' => 'market',
            'size' => '0.01',
            'max_slippage_pct' => '0.5',
        ]);
        print_r($whatIf->toArray());
    }

    public static function cancelAllOrders(Client $client): void
    {
        $cancelAllResult = $client->orders->cancelAll('SANDBOX-00042-001', [
            'market' => 'BTC-USD',
        ]);
        print_r($cancelAllResult->toArray());
    }

    public static function closePosition(Client $client): void
    {
        $order = $client->positions->close('SANDBOX-00042-001', 'BTC-USD', [
            'size' => '0.005',
        ]);
        print_r($order->toArray());
    }

    public static function flatten(Client $client): void
    {
        $flattenResult = $client->positions->flatten('SANDBOX-00042-001');
        print_r($flattenResult->toArray());
    }

    public static function setMargin(Client $client): void
    {
        $marginSettingResult = $client->margin->set('SANDBOX-00042-001', 'BTC-USD', [
            'margin_mode' => 'isolated',
            'leverage' => 5,
        ]);
        print_r($marginSettingResult->toArray());
    }

    public static function listEvents(Client $client): void
    {
        foreach ($client->events->listAutoPaging() as $event) {
            echo $event->id, "\n";
        }
    }

    public static function getExits(Client $client): void
    {
        $exits = $client->exits->get('SANDBOX-00042-001', 'BTC-USD');
        print_r($exits->toArray());
    }

    public static function setExits(Client $client): void
    {
        $exits = $client->exits->set('SANDBOX-00042-001', 'BTC-USD', [
            'expected_position' => [
                'side' => 'long',
                'size' => '0.01',
            ],
            'expected_exits' => [
                [
                    'id' => 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W',
                    'size' => '0.01',
                    'price' => '61700',
                ],
            ],
            'operations' => [
                [
                    'op' => 'modify',
                    'id' => 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W',
                    'price' => '61900',
                ],
                [
                    'op' => 'place',
                    'group' => 'take_profit',
                    'type' => 'limit',
                    'price' => '63000',
                    'size' => '0.01',
                ],
            ],
        ]);
        print_r($exits->toArray());
    }

    public static function setOrderExits(Client $client): void
    {
        $order = $client->orders->setExits('SANDBOX-00042-001', 'ord_01JAB7Q8XK4M2N5P7R9S3T5V7W', [
            'take_profit' => [
                [
                    'distance' => '600',
                    'size_pct' => '50',
                ],
                [
                    'distance' => '1200',
                    'size_pct' => '50',
                ],
            ],
            'stop_loss' => [
                'distance' => '300',
                'type' => 'stop_market',
            ],
        ]);
        print_r($order->toArray());
    }

    public static function createStrategy(Client $client): void
    {
        $strategy = $client->strategies->create('SANDBOX-00042-001', [
            'type' => 'twap',
            'market' => 'BTC-USD',
            'side' => 'buy',
            'size' => '0.05',
            'duration_minutes' => 30,
            'interval_seconds' => 60,
            'client_strategy_id' => 'twap-07',
        ]);
        print_r($strategy->toArray());
    }

    public static function listStrategies(Client $client): void
    {
        foreach ($client->strategies->listAutoPaging('SANDBOX-00042-001') as $strategy) {
            echo $strategy->id, "\n";
        }
    }

    public static function getStrategy(Client $client): void
    {
        $strategy = $client->strategies->get('SANDBOX-00042-001', 'stg_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($strategy->toArray());
    }

    public static function cancelStrategy(Client $client): void
    {
        $strategy = $client->strategies->cancel('SANDBOX-00042-001', 'stg_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($strategy->toArray());
    }

    public static function listWebhookEndpoints(Client $client): void
    {
        foreach ($client->webhookEndpoints->list()->data as $webhookEndpoint) {
            echo $webhookEndpoint->id, "\n";
        }
    }

    public static function getWebhookEndpoint(Client $client): void
    {
        $webhookEndpoint = $client->webhookEndpoints->get('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($webhookEndpoint->toArray());
    }

    public static function deleteWebhookEndpoint(Client $client): void
    {
        $deletedEndpoint = $client->webhookEndpoints->delete('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($deletedEndpoint->toArray());
    }

    public static function testWebhookEndpoint(Client $client): void
    {
        $webhookDelivery = $client->webhookEndpoints->test('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($webhookDelivery->toArray());
    }

    public static function listWebhookDeliveries(Client $client): void
    {
        foreach ($client->webhookEndpoints->listDeliveriesAutoPaging('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W') as $webhookDelivery) {
            echo $webhookDelivery->id, "\n";
        }
    }

    public static function getWebhookDelivery(Client $client): void
    {
        $webhookDelivery = $client->webhookEndpoints->getDelivery('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W', 'dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($webhookDelivery->toArray());
    }

    public static function resendWebhookDelivery(Client $client): void
    {
        $webhookDelivery = $client->webhookEndpoints->resendDelivery('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W', 'dlv_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($webhookDelivery->toArray());
    }

    public static function disableWebhookEndpoint(Client $client): void
    {
        $webhookEndpoint = $client->webhookEndpoints->disable('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($webhookEndpoint->toArray());
    }

    public static function enableWebhookEndpoint(Client $client): void
    {
        $webhookEndpoint = $client->webhookEndpoints->enable('whk_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($webhookEndpoint->toArray());
    }

    public static function listTradingViewHooks(Client $client): void
    {
        foreach ($client->tradingView->listHooks()->data as $tradingViewHook) {
            echo $tradingViewHook->id, "\n";
        }
    }

    public static function getTradingViewHook(Client $client): void
    {
        $tradingViewHook = $client->tradingView->getHook('K7x2mQ9pLr4A');
        print_r($tradingViewHook->toArray());
    }

    public static function deleteTradingViewHook(Client $client): void
    {
        $deletedHook = $client->tradingView->deleteHook('K7x2mQ9pLr4A');
        print_r($deletedHook->toArray());
    }

    public static function listTradingViewAlerts(Client $client): void
    {
        foreach ($client->tradingView->listAlertsAutoPaging('K7x2mQ9pLr4A') as $tradingViewAlert) {
            echo $tradingViewAlert->id, "\n";
        }
    }

    public static function getTradingViewAlert(Client $client): void
    {
        $tradingViewAlert = $client->tradingView->getAlert('K7x2mQ9pLr4A', 'tva_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($tradingViewAlert->toArray());
    }

    public static function disableTradingViewHook(Client $client): void
    {
        $tradingViewHook = $client->tradingView->disableHook('K7x2mQ9pLr4A');
        print_r($tradingViewHook->toArray());
    }

    public static function enableTradingViewHook(Client $client): void
    {
        $tradingViewHook = $client->tradingView->enableHook('K7x2mQ9pLr4A');
        print_r($tradingViewHook->toArray());
    }

    public static function parseTradingViewMessage(Client $client): void
    {
        $tradingViewParse = $client->tradingView->parse([
            'message' => 'buy BTCUSD qty 0.01 tp 1% sl 61500',
        ]);
        print_r($tradingViewParse->toArray());
    }

    public static function listWebhookEventTypes(Client $client): void
    {
        foreach ($client->webhookEndpoints->eventTypes()->data as $webhookEventType) {
            echo json_encode($webhookEventType), "\n";
        }
    }

    public static function getEvent(Client $client): void
    {
        $event = $client->events->get('evt_01JAB7Q8XK4M2N5P7R9S3T5V7W');
        print_r($event->toArray());
    }

    public static function listFills(Client $client): void
    {
        foreach ($client->fills->listAutoPaging('SANDBOX-00042-001') as $fill) {
            echo $fill->id, "\n";
        }
    }
}
