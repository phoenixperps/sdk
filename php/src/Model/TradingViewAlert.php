<?php

// Generated from the API spec by generator/printers/php.js. Do not edit: run `node generator`.

declare(strict_types=1);

namespace PhoenixPerps\Model;

use PhoenixPerps\Decimal;

/** TradingViewAlert */
final class TradingViewAlert implements \JsonSerializable
{
    private const WIRE = ['id' => true, 'hook_id' => true, 'account_id' => true, 'status' => true, 'reason' => true, 'reason_text' => true, 'format' => true, 'market' => true, 'action' => true, 'message' => true, 'sender' => true, 'plan' => true, 'results' => true, 'details' => true, 'duplicate_of' => true, 'repeat_count' => true, 'received_at' => true, 'finished_at' => true];

    public function __construct(
        /** The alert. */
        public readonly ?string $id = null,
        /** Its link. */
        public readonly ?string $hookId = null,
        /** The account number. */
        public readonly ?string $accountId = null,
        /** What it did. */
        public readonly ?string $status = null,
        /** Why it placed nothing (or not all): a reason code. */
        public readonly ?string $reason = null,
        /** That reason in words. */
        public readonly ?string $reasonText = null,
        /** How the message was read: universal, pickmytrade, traderspost, pineconnector, text. */
        public readonly ?string $format = null,
        /** The market it named. */
        public readonly ?string $market = null,
        /** Its action. */
        public readonly ?string $action = null,
        /** The message as kept (credentials masked). */
        public readonly ?string $message = null,
        /** The sender's address, its end masked. */
        public readonly ?string $sender = null,
        /** What it was to do: { legs } or { skip } or { refuse }. */
        /** @var array<string, mixed>|null */
        public readonly ?array $plan = null,
        /** What each leg did. */
        /** @var list<array<string, mixed>>|null */
        public readonly ?array $results = null,
        /** More about a refusal. */
        /** @var array<string, mixed>|null */
        public readonly ?array $details = null,
        /** The alert this one repeats. */
        public readonly ?string $duplicateOf = null,
        /** Refusals folded into this row (rate limits). */
        public readonly ?int $repeatCount = null,
        /** When it came. */
        public readonly ?int $receivedAt = null,
        /** When it was done. */
        public readonly ?int $finishedAt = null,
        /** @var array<string, mixed> Fields this SDK version does not know yet (a newer API's), kept as they came. */
        public readonly array $extra = [],
    ) {
    }

    /** @param array<string, mixed> $d the object as the API writes it */
    public static function fromArray(array $d): self
    {
        return new self(
            id: $d['id'] ?? null,
            hookId: $d['hook_id'] ?? null,
            accountId: $d['account_id'] ?? null,
            status: $d['status'] ?? null,
            reason: $d['reason'] ?? null,
            reasonText: $d['reason_text'] ?? null,
            format: $d['format'] ?? null,
            market: $d['market'] ?? null,
            action: $d['action'] ?? null,
            message: $d['message'] ?? null,
            sender: $d['sender'] ?? null,
            plan: $d['plan'] ?? null,
            results: $d['results'] ?? null,
            details: $d['details'] ?? null,
            duplicateOf: $d['duplicate_of'] ?? null,
            repeatCount: $d['repeat_count'] ?? null,
            receivedAt: $d['received_at'] ?? null,
            finishedAt: $d['finished_at'] ?? null,
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
        if ($this->hookId !== null) {
            $out['hook_id'] = $this->hookId;
        }
        if ($this->accountId !== null) {
            $out['account_id'] = $this->accountId;
        }
        if ($this->status !== null) {
            $out['status'] = $this->status;
        }
        if ($this->reason !== null) {
            $out['reason'] = $this->reason;
        }
        if ($this->reasonText !== null) {
            $out['reason_text'] = $this->reasonText;
        }
        if ($this->format !== null) {
            $out['format'] = $this->format;
        }
        if ($this->market !== null) {
            $out['market'] = $this->market;
        }
        if ($this->action !== null) {
            $out['action'] = $this->action;
        }
        if ($this->message !== null) {
            $out['message'] = $this->message;
        }
        if ($this->sender !== null) {
            $out['sender'] = $this->sender;
        }
        if ($this->plan !== null) {
            $out['plan'] = $this->plan;
        }
        if ($this->results !== null) {
            $out['results'] = $this->results;
        }
        if ($this->details !== null) {
            $out['details'] = $this->details;
        }
        if ($this->duplicateOf !== null) {
            $out['duplicate_of'] = $this->duplicateOf;
        }
        if ($this->repeatCount !== null) {
            $out['repeat_count'] = $this->repeatCount;
        }
        if ($this->receivedAt !== null) {
            $out['received_at'] = $this->receivedAt;
        }
        if ($this->finishedAt !== null) {
            $out['finished_at'] = $this->finishedAt;
        }
        return $out + $this->extra;
    }

    /** @return array<string, mixed> */
    public function jsonSerialize(): array
    {
        return $this->toArray();
    }
}
