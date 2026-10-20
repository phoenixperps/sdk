<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\DeletedHook;
use PhoenixPerps\Model\TradingViewAlert;
use PhoenixPerps\Model\TradingViewHook;
use PhoenixPerps\Model\TradingViewParse;
use PhoenixPerps\Model\TradingViewParseInput;
use PhoenixPerps\Model\TradingviewListAlertsParams;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The tradingview calls ($client->tradingView). */
final class TradingViewResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** List TradingView links. */
    /** @return Page<TradingViewHook>|null null on a 304 Not Modified */
    public function listHooks(?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_TRADING_VIEW_HOOKS, [], null, [TradingViewHook::class, 'fromArray'], $options);
    }

    /** Get a TradingView link. */
    /** @return TradingViewHook|null null on a 304 Not Modified */
    public function getHook(string $hookId, ?RequestOptions $options = null): ?TradingViewHook
    {
        $d = $this->c->one(Ops::GET_TRADING_VIEW_HOOK, [$hookId], null, null, $options);
        return $d === null ? null : TradingViewHook::fromArray($d);
    }

    /** Revoke a TradingView link. */
    public function deleteHook(string $hookId, ?RequestOptions $options = null): DeletedHook
    {
        return DeletedHook::fromArray($this->c->one(Ops::DELETE_TRADING_VIEW_HOOK, [$hookId], null, null, $options) ?? []);
    }

    /** List a link's alerts. */
    /** @return Page<TradingViewAlert>|null null on a 304 Not Modified */
    public function listAlerts(string $hookId, TradingviewListAlertsParams|array|null $params = null, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_TRADING_VIEW_ALERTS, [$hookId], $params, [TradingViewAlert::class, 'fromArray'], $options);
    }

    /** Every item of listAlerts, page after page (RequestOptions::$maxItems caps it). */
    /** @return \Generator<int, TradingViewAlert> */
    public function listAlertsAutoPaging(string $hookId, TradingviewListAlertsParams|array|null $params = null, ?RequestOptions $options = null): \Generator
    {
        return $this->listAlerts($hookId, $params, $options)?->autoPagingIterator($options?->maxItems) ?? (static fn (): \Generator => yield from [])();
    }

    /** Get an alert. */
    /** @return TradingViewAlert|null null on a 304 Not Modified */
    public function getAlert(string $hookId, string $alertId, ?RequestOptions $options = null): ?TradingViewAlert
    {
        $d = $this->c->one(Ops::GET_TRADING_VIEW_ALERT, [$hookId, $alertId], null, null, $options);
        return $d === null ? null : TradingViewAlert::fromArray($d);
    }

    /** Pause a TradingView link. */
    public function disableHook(string $hookId, ?RequestOptions $options = null): TradingViewHook
    {
        return TradingViewHook::fromArray($this->c->one(Ops::DISABLE_TRADING_VIEW_HOOK, [$hookId], null, null, $options) ?? []);
    }

    /** Resume a TradingView link. */
    public function enableHook(string $hookId, ?RequestOptions $options = null): TradingViewHook
    {
        return TradingViewHook::fromArray($this->c->one(Ops::ENABLE_TRADING_VIEW_HOOK, [$hookId], null, null, $options) ?? []);
    }

    /** Read an alert message. */
    public function parse(TradingViewParseInput|array $body, ?RequestOptions $options = null): TradingViewParse
    {
        return TradingViewParse::fromArray($this->c->one(Ops::PARSE_TRADING_VIEW_MESSAGE, [], null, $body, $options) ?? []);
    }
}
