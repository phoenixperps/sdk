<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\MarginSetting;
use PhoenixPerps\Model\MarginSettingInput;
use PhoenixPerps\Model\MarginSettingResult;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The margin calls ($client->margin). */
final class MarginResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** List margin settings. */
    /** @return Page<MarginSetting>|null null on a 304 Not Modified */
    public function list(string $accountId, ?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_MARGIN_SETTINGS, [$accountId], null, [MarginSetting::class, 'fromArray'], $options);
    }

    /** Set leverage and margin mode. */
    public function set(string $accountId, string $market, MarginSettingInput|array $body, ?RequestOptions $options = null): MarginSettingResult
    {
        return MarginSettingResult::fromArray($this->c->one(Ops::SET_MARGIN, [$accountId, $market], null, $body, $options) ?? []);
    }
}
