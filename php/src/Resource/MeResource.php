<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\Me;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The me calls ($client->me). */
final class MeResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** Get your key. */
    /** @return Me|null null on a 304 Not Modified */
    public function get(?RequestOptions $options = null): ?Me
    {
        $d = $this->c->one(Ops::GET_ME, [], null, null, $options);
        return $d === null ? null : Me::fromArray($d);
    }
}
