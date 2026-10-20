<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps;

use PhoenixPerps\Resource\PlatformResource;
use PhoenixPerps\Resource\MeResource;
use PhoenixPerps\Resource\MarketsResource;
use PhoenixPerps\Resource\AccountsResource;
use PhoenixPerps\Resource\HistoryResource;
use PhoenixPerps\Resource\MarginResource;
use PhoenixPerps\Resource\PositionsResource;
use PhoenixPerps\Resource\OrdersResource;
use PhoenixPerps\Resource\EventsResource;
use PhoenixPerps\Resource\ExitsResource;
use PhoenixPerps\Resource\StrategiesResource;
use PhoenixPerps\Resource\WebhookEndpointsResource;
use PhoenixPerps\Resource\TradingViewResource;
use PhoenixPerps\Resource\FillsResource;

/** The resources of a client, one per resource of the API. */
abstract class BaseClient
{
    /** The platform calls. */
    public readonly PlatformResource $platform;

    /** The me calls. */
    public readonly MeResource $me;

    /** The markets calls. */
    public readonly MarketsResource $markets;

    /** The accounts calls. */
    public readonly AccountsResource $accounts;

    /** The history calls. */
    public readonly HistoryResource $history;

    /** The margin calls. */
    public readonly MarginResource $margin;

    /** The positions calls. */
    public readonly PositionsResource $positions;

    /** The orders calls. */
    public readonly OrdersResource $orders;

    /** The events calls. */
    public readonly EventsResource $events;

    /** The exits calls. */
    public readonly ExitsResource $exits;

    /** The strategies calls. */
    public readonly StrategiesResource $strategies;

    /** The webhookEndpoints calls. */
    public readonly WebhookEndpointsResource $webhookEndpoints;

    /** The tradingview calls. */
    public readonly TradingViewResource $tradingView;

    /** The fills calls. */
    public readonly FillsResource $fills;

    protected function initResources(Client $c): void
    {
        $this->platform = new PlatformResource($c);
        $this->me = new MeResource($c);
        $this->markets = new MarketsResource($c);
        $this->accounts = new AccountsResource($c);
        $this->history = new HistoryResource($c);
        $this->margin = new MarginResource($c);
        $this->positions = new PositionsResource($c);
        $this->orders = new OrdersResource($c);
        $this->events = new EventsResource($c);
        $this->exits = new ExitsResource($c);
        $this->strategies = new StrategiesResource($c);
        $this->webhookEndpoints = new WebhookEndpointsResource($c);
        $this->tradingView = new TradingViewResource($c);
        $this->fills = new FillsResource($c);
    }
}
