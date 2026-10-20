<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** AccountDetail */
final class AccountDetail implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'environment' => true, 'stage' => true, 'status' => true, 'product' => true, 'tier' => true, 'start_balance' => true, 'balance' => true, 'rule' => true, 'payout_eligible' => true, 'parked' => true, 'created_at' => true, 'risk' => true];

    public function __construct(
        /** The account number. */
        public readonly ?string $id = null,
        /** The environment the account lives in. */
        public readonly ?string $environment = null,
        /** Where the account is in the programme. */
        public readonly ?string $stage = null,
        /** pending: being opened; active: trading. */
        public readonly ?string $status = null,
        /** The product it was bought as. */
        public readonly ?string $product = null,
        /** Its size as sold. */
        public readonly ?string $tier = null,
        /** The balance it started with. */
        public readonly ?string $startBalance = null,
        /** The balance: closed trades and fees only. */
        public readonly ?string $balance = null,
        /** The rule. */
        public readonly ?Rule $rule = null,
        /** A payout may be requested now. */
        public readonly ?bool $payoutEligible = null,
        /** Sandbox only: parked after a while unused; it wakes the moment you use it. */
        public readonly ?bool $parked = null,
        /** When the account was made. */
        public readonly ?int $createdAt = null,
        /** The risk. */
        public readonly ?AccountRiskOrNull $risk = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            environment: $d['environment'] ?? null,
            stage: $d['stage'] ?? null,
            status: $d['status'] ?? null,
            product: $d['product'] ?? null,
            tier: $d['tier'] ?? null,
            startBalance: isset($d['start_balance']) ? (string) $d['start_balance'] : null,
            balance: isset($d['balance']) ? (string) $d['balance'] : null,
            rule: isset($d['rule']) && is_array($d['rule']) ? Rule::fromArray($d['rule']) : null,
            payoutEligible: $d['payout_eligible'] ?? null,
            parked: $d['parked'] ?? null,
            createdAt: $d['created_at'] ?? null,
            risk: isset($d['risk']) && is_array($d['risk']) ? AccountRiskOrNull::fromArray($d['risk']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->id !== null) {
            $out['id'] = $this->id;
        }
        if ($this->environment !== null) {
            $out['environment'] = $this->environment;
        }
        if ($this->stage !== null) {
            $out['stage'] = $this->stage;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        if ($this->product !== null) {
            $out['product'] = $this->product;
        }
        if ($this->tier !== null) {
            $out['tier'] = $this->tier;
        }
        if ($this->startBalance !== null) {
            $out['start_balance'] = $this->startBalance;
        }
        if ($this->balance !== null) {
            $out['balance'] = $this->balance;
        }
        if ($this->rule !== null) {
            $out['rule'] = $this->rule->toArray();
        }
        if ($this->payoutEligible !== null) {
            $out['payout_eligible'] = $this->payoutEligible;
        }
        if ($this->parked !== null) {
            $out['parked'] = $this->parked;
        }
        if ($this->createdAt !== null) {
            $out['created_at'] = $this->createdAt;
        }
        if ($this->risk !== null) {
            $out['risk'] = $this->risk->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
