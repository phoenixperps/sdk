<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\Market;
use PhoenixPerps\Model\MarketStats;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The markets calls ($client->markets). */
final class MarketsResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** List markets. */
    /** @return Page<Market>|null null on a 304 Not Modified */
    public function list(?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_MARKETS, [], null, [Market::class, 'fromArray'], $options);
    }

    /** List market stats. */
    /** @return Page<MarketStats>|null null on a 304 Not Modified */
    public function stats(?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_MARKET_STATS, [], null, [MarketStats::class, 'fromArray'], $options);
    }

    /** Get a market. */
    /** @return Market|null null on a 304 Not Modified */
    public function get(string $market, ?RequestOptions $options = null): ?Market
    {
        $d = $this->c->one(Ops::GET_MARKET, [$market], null, null, $options);
        return $d === null ? null : Market::fromArray($d);
    }
}
