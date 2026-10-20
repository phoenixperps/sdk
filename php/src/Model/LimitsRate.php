<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** The request budgets. A request takes from every budget it belongs to. */
final class LimitsRate implements \JsonSerializable
{
    private const WIRE = ['public_per_ip' => true, 'per_ip' => true, 'per_key_read' => true, 'per_key_trade' => true, 'per_account_trade' => true, 'per_customer' => true, 'bulk' => true, 'what_if' => true];

    public function __construct(
        /** The public per ip. */
        public readonly ?LimitsRatePublicPerIp $publicPerIp = null,
        /** The per ip. */
        public readonly ?LimitsRatePerIp $perIp = null,
        /** The per key read. */
        public readonly ?LimitsRatePerKeyRead $perKeyRead = null,
        /** The per key trade. */
        public readonly ?LimitsRatePerKeyTrade $perKeyTrade = null,
        /** The per account trade. */
        public readonly ?LimitsRatePerAccountTrade $perAccountTrade = null,
        /** The per customer. */
        public readonly ?LimitsRatePerCustomer $perCustomer = null,
        /** The bulk. */
        public readonly ?LimitsRateBulk $bulk = null,
        /** The what if. */
        public readonly ?LimitsRateWhatIf $whatIf = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            publicPerIp: isset($d['public_per_ip']) && is_array($d['public_per_ip']) ? LimitsRatePublicPerIp::fromArray($d['public_per_ip']) : null,
            perIp: isset($d['per_ip']) && is_array($d['per_ip']) ? LimitsRatePerIp::fromArray($d['per_ip']) : null,
            perKeyRead: isset($d['per_key_read']) && is_array($d['per_key_read']) ? LimitsRatePerKeyRead::fromArray($d['per_key_read']) : null,
            perKeyTrade: isset($d['per_key_trade']) && is_array($d['per_key_trade']) ? LimitsRatePerKeyTrade::fromArray($d['per_key_trade']) : null,
            perAccountTrade: isset($d['per_account_trade']) && is_array($d['per_account_trade']) ? LimitsRatePerAccountTrade::fromArray($d['per_account_trade']) : null,
            perCustomer: isset($d['per_customer']) && is_array($d['per_customer']) ? LimitsRatePerCustomer::fromArray($d['per_customer']) : null,
            bulk: isset($d['bulk']) && is_array($d['bulk']) ? LimitsRateBulk::fromArray($d['bulk']) : null,
            whatIf: isset($d['what_if']) && is_array($d['what_if']) ? LimitsRateWhatIf::fromArray($d['what_if']) : null,
            extra: array_diff_key($d, self::WIRE),
        );
    }

    /** @return array<string, mixed> the object as the API takes it (nulls left out) */
    public function toArray(): array
    {
        $out = [];
        if ($this->publicPerIp !== null) {
            $out['public_per_ip'] = $this->publicPerIp->toArray();
        }
        if ($this->perIp !== null) {
            $out['per_ip'] = $this->perIp->toArray();
        }
        if ($this->perKeyRead !== null) {
            $out['per_key_read'] = $this->perKeyRead->toArray();
        }
        if ($this->perKeyTrade !== null) {
            $out['per_key_trade'] = $this->perKeyTrade->toArray();
        }
        if ($this->perAccountTrade !== null) {
            $out['per_account_trade'] = $this->perAccountTrade->toArray();
        }
        if ($this->perCustomer !== null) {
            $out['per_customer'] = $this->perCustomer->toArray();
        }
        if ($this->bulk !== null) {
            $out['bulk'] = $this->bulk->toArray();
        }
        if ($this->whatIf !== null) {
            $out['what_if'] = $this->whatIf->toArray();
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
