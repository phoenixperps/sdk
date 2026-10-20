<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\Limits;
use PhoenixPerps\Model\Platform;
use PhoenixPerps\Model\Status;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The platform calls ($client->platform). */
final class PlatformResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** Get the API. */
    /** @return Platform|null null on a 304 Not Modified */
    public function info(?RequestOptions $options = null): ?Platform
    {
        $d = $this->c->one(Ops::GET_API, [], null, null, $options);
        return $d === null ? null : Platform::fromArray($d);
    }

    /** Get the OpenAPI document. */
    /** @return array<string, mixed>|null null on a 304 Not Modified */
    public function spec(?RequestOptions $options = null): ?array
    {
        return $this->c->one(Ops::GET_SPEC, [], null, null, $options);
    }

    /** Get the status. */
    /** @return Status|null null on a 304 Not Modified */
    public function status(?RequestOptions $options = null): ?Status
    {
        $d = $this->c->one(Ops::GET_STATUS, [], null, null, $options);
        return $d === null ? null : Status::fromArray($d);
    }

    /** Get the limits. */
    /** @return Limits|null null on a 304 Not Modified */
    public function limits(?RequestOptions $options = null): ?Limits
    {
        $d = $this->c->one(Ops::GET_LIMITS, [], null, null, $options);
        return $d === null ? null : Limits::fromArray($d);
    }
}
