<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\DailyResult;
use PhoenixPerps\Model\EquityHistory;
use PhoenixPerps\Model\HistoryEquityParams;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The history calls ($client->history). */
final class HistoryResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** Get the equity curve. */
    /** @return EquityHistory|null null on a 304 Not Modified */
    public function equity(string $accountId, HistoryEquityParams|array|null $params = null, ?RequestOptions $options = null): ?EquityHistory
    {
        $d = $this->c->one(Ops::GET_EQUITY_HISTORY, [$accountId], $params, null, $options);
        return $d === null ? null : EquityHistory::fromArray($d);
    }

    /** List daily results. */
    /** @return Page<DailyResult>|null null on a 304 Not Modified */
    public function daily(string $accountId, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_DAILY_RESULTS, [$accountId], null, [DailyResult::class, 'fromArray'], $options);
    }
}
