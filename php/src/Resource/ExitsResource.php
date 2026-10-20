<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\Exits;
use PhoenixPerps\Model\ExitsBatch;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The exits calls ($client->exits). */
final class ExitsResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** Get a position's exits. */
    /** @return Exits|null null on a 304 Not Modified */
    public function get(string $accountId, string $market, ?RequestOptions $options = null): ?Exits
    {
        $d = $this->c->one(Ops::GET_EXITS, [$accountId, $market], null, null, $options);
        return $d === null ? null : Exits::fromArray($d);
    }

    /** Change a position's exits. */
    public function set(string $accountId, string $market, ExitsBatch|array $body, ?RequestOptions $options = null): Exits
    {
        return Exits::fromArray($this->c->one(Ops::SET_EXITS, [$accountId, $market], null, $body, $options) ?? []);
    }
}
