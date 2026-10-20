<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Resource;

use PhoenixPerps\Client;
use PhoenixPerps\Model\Account;
use PhoenixPerps\Model\AccountDetail;
use PhoenixPerps\Model\AccountRisk;
use PhoenixPerps\Model\Progress;
use PhoenixPerps\Model\TradingPolicy;
use PhoenixPerps\Ops;
use PhoenixPerps\Page;
use PhoenixPerps\RequestOptions;

/** The accounts calls ($client->accounts). */
final class AccountsResource
{
    public function __construct(private readonly Client $c)
    {
    }

    /** List accounts. */
    /** @return Page<Account>|null null on a 304 Not Modified */
    public function list(?RequestOptions $options = null): ?Page
    {
        return $this->c->list(Ops::LIST_ACCOUNTS, [], null, [Account::class, 'fromArray'], $options);
    }

    /** Open a sandbox account. */
    public function createSandbox(?RequestOptions $options = null): Account
    {
        return Account::fromArray($this->c->one(Ops::CREATE_SANDBOX_ACCOUNT, [], null, null, $options) ?? []);
    }

    /** Get an account. */
    /** @return AccountDetail|null null on a 304 Not Modified */
    public function get(string $accountId, ?RequestOptions $options = null): ?AccountDetail
    {
        $d = $this->c->one(Ops::GET_ACCOUNT, [$accountId], null, null, $options);
        return $d === null ? null : AccountDetail::fromArray($d);
    }

    /** Get live risk. */
    /** @return AccountRisk|null null on a 304 Not Modified */
    public function risk(string $accountId, ?RequestOptions $options = null): ?AccountRisk
    {
        $d = $this->c->one(Ops::GET_RISK, [$accountId], null, null, $options);
        return $d === null ? null : AccountRisk::fromArray($d);
    }

    /** Get the trading policy. */
    /** @return TradingPolicy|null null on a 304 Not Modified */
    public function tradingPolicy(string $accountId, ?RequestOptions $options = null): ?TradingPolicy
    {
        $d = $this->c->one(Ops::GET_TRADING_POLICY, [$accountId], null, null, $options);
        return $d === null ? null : TradingPolicy::fromArray($d);
    }

    /** Get progress. */
    /** @return Progress|null null on a 304 Not Modified */
    public function progress(string $accountId, ?RequestOptions $options = null): ?Progress
    {
        $d = $this->c->one(Ops::GET_PROGRESS, [$accountId], null, null, $options);
        return $d === null ? null : Progress::fromArray($d);
    }
}
