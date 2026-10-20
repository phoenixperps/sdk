<?php

declare(strict_types=1);

namespace PhoenixPerps;

use PhoenixPerps\Exception\ApiTimeoutException;
use PhoenixPerps\Exception\AuthenticationException;
use PhoenixPerps\Exception\StreamClosedException;
use PhoenixPerps\Model\Event;

/**
 * The account stream (docs/BEHAVIOUR.md "Streams"; read only, orders go through REST): the key on the upgrade; hello,
 * one snapshot per account, snapshot_end, then events, each once (replays are told apart by their event id; each keeps
 * its frame's seq and replayed in $extra). It reconnects by itself with ?resume=<last event id>, waiting
 * min(30 s, 0.5 s x 2^n) with jitter (0 to 3 s after 1012, the server restarting; an upgrade refused with 429 or 5xx
 * after its Retry-After), a log line per reconnect with its cause. 4001 (the key ended) ends it with an
 * AuthenticationException; an error frame (its subscribe refused: too_many_accounts, account_not_found with its
 * accountId, the same on every reconnect) with a ValidationException; any other refused upgrade with the API's error.
 * 4008 (too far behind) reconnects without resume; 4013 waits longer. A resume the server could not cover (its
 * resume_gap notice) or a 4008 is a gap, told to onGap with the last event id told. Risk frames (risk: true) are told
 * to onRisk as they come. Three heartbeats without a frame mean a dead connection. A snapshot carrying error
 * (state_unavailable) is an account that could not be read then (read it by REST), not a flat one.
 *
 *     foreach ($client->accountStream(risk: true) as $event) { ... }   // blocks; close() from a handler ends it
 *     while ($running) { $event = $stream->poll(1.0); ... }            // null after a second without one
 *
 * @implements \IteratorAggregate<int, Event>
 */
final class AccountStream implements \IteratorAggregate
{
    private const LRU = 2048;

    /** @var array<string, array<string, mixed>> */
    private array $snapshots = [];
    /** @var array<string, array<string, mixed>> */
    private array $risks = [];
    /** @var array<string, true> */
    private array $seen = [];
    /** @var list<Event> events read while ping() or unsubscribe() waited, for the next poll() */
    private array $events = [];
    /** @var array<string, true> accounts unsubscribed: left out of the subscribe after a reconnect */
    private array $dropped = [];
    private bool $closed = false;
    private bool $ready = false;
    private ?StreamSocket $socket = null;
    private int $attempt = 0;
    private bool $resume = true;
    private float $heartbeat = 15.0;
    private float $alive = 0.0; // when the connection last sent a frame
    private float $reconnectAt = 0.0;

    /**
     * @param list<string> $accounts
     * @param (\Closure(array<string, mixed>): void)|null $onRisk receives each risk frame
     * @param (\Closure(string|null): void)|null $onGap receives the last event id told when events after it may be missing
     */
    public function __construct(private readonly Client $c, private readonly array $accounts = [], private readonly bool $risk = false, private ?string $lastEventId = null, private readonly ?\Closure $onRisk = null, private readonly ?\Closure $onGap = null)
    {
    }

    /** The last snapshot frame of an account (its error set when the account could not be read then). */
    public function snapshot(string $accountId): ?array
    {
        return $this->snapshots[$accountId] ?? null;
    }

    /** The last risk frame of an account (with risk set). */
    public function risk(string $accountId): ?array
    {
        return $this->risks[$accountId] ?? null;
    }

    /** The id of the last event told (the resume point). */
    public function lastEventId(): ?string
    {
        return $this->lastEventId;
    }

    /** The first snapshots are in. */
    public function isReady(): bool
    {
        return $this->ready;
    }

    /** Ends the stream (the iteration returns, poll() answers null). */
    public function close(): void
    {
        $this->closed = true;
        $this->socket?->close();
        $this->socket = null;
    }

    /**
     * The next event within $wait seconds, else null (none came, or the stream is closed): a loop that does other things
     * between events. Connects, and reconnects, as the iteration does; throws what ended the stream.
     */
    public function poll(float $wait): ?Event
    {
        $deadline = microtime(true) + $wait;
        while (!$this->closed) {
            if ($this->events) {
                return array_shift($this->events);
            }
            if ($this->socket === null && !$this->connect($deadline)) {
                return null;
            }
            $f = $this->frame($deadline);
            if ($f !== null && ($e = $this->handle($f)) !== null) {
                return $e;
            }
            if (microtime(true) >= $deadline) {
                return null;
            }
        }
        return null;
    }

    /** @return \Generator<int, Event> every event until close(); throws what ended the stream */
    public function getIterator(): \Generator
    {
        while (!$this->closed) {
            $e = $this->poll(60.0);
            if ($e !== null) {
                yield $e;
            }
        }
    }

    /** @return array<string, mixed> the server's pong */
    public function ping(float $wait = 10.0): array
    {
        return $this->ask(['op' => 'ping'], 'pong', $wait);
    }

    /**
     * Stops following accounts; they stay out after a reconnect.
     *
     * @param list<string> $accounts
     * @return array<string, mixed> the server's reply (the accounts it stopped)
     */
    public function unsubscribe(array $accounts, float $wait = 10.0): array
    {
        foreach ($accounts as $a) {
            $this->dropped[strtoupper($a)] = true;
        }
        return $this->ask(['op' => 'unsubscribe', 'accounts' => array_values($accounts)], 'unsubscribed', $wait);
    }

    /**
     * Sends a message (again on each new connection) and reads until its reply; the events read meanwhile wait for
     * poll().
     *
     * @param array<string, mixed> $message
     * @return array<string, mixed>
     */
    private function ask(array $message, string $reply, float $wait): array
    {
        $deadline = microtime(true) + $wait;
        $on = null;
        while (!$this->closed && microtime(true) < $deadline) {
            if ($this->socket === null && !$this->connect($deadline)) {
                break;
            }
            if ($on !== $this->socket) {
                $on = $this->socket;
                if (!$on->send($message)) {
                    $this->drop(StreamSocket::backoff($this->attempt), 'connection lost');
                    continue;
                }
            }
            $f = $this->frame($deadline);
            if ($f === null) {
                continue;
            }
            if (($f['type'] ?? null) === $reply) {
                return $f;
            }
            $e = $this->handle($f);
            if ($e !== null) {
                $this->events[] = $e;
            }
        }
        if ($this->closed) {
            throw new StreamClosedException('stream_closed', 'The stream is closed.');
        }
        throw new ApiTimeoutException('timeout', sprintf('No answer within %.3g s.', $wait), retryable: true);
    }

    /** Opens a connection once the reconnect wait is over: false when the deadline comes first. */
    private function connect(float $deadline): bool
    {
        while (!$this->closed) {
            $wait = $this->reconnectAt - microtime(true);
            if ($wait > 0) {
                usleep((int) (max(0.0, min($wait, $deadline - microtime(true))) * 1e6));
                if (microtime(true) < $this->reconnectAt) {
                    return false;
                }
            }
            $url = $this->c->accountStreamUrl . ($this->resume && $this->lastEventId !== null ? '?resume=' . rawurlencode($this->lastEventId) : '');
            try {
                $this->socket = new StreamSocket($this->c, $url);
            } catch (\RuntimeException $e) {
                if (StreamSocket::forGood($e)) {
                    $this->closed = true;
                    throw $e;
                }
                $this->drop(StreamSocket::after($e, $this->attempt), StreamSocket::cause($e));
                continue;
            }
            $this->resume = true;
            $this->heartbeat = 15.0;
            $this->alive = microtime(true);
            return true;
        }
        return false;
    }

    /** @return array<string, mixed>|null the next frame before the deadline; null at the deadline or when the connection ended */
    private function frame(float $deadline): ?array
    {
        $dead = $this->alive + 3 * $this->heartbeat;
        $it = $this->socket->next(max(0.001, min($deadline, $dead) - microtime(true)));
        if (isset($it['frame'])) {
            $this->alive = microtime(true);
            return $it['frame'];
        }
        $code = $it['close'] ?? null;
        if (isset($it['timeout'])) {
            if (microtime(true) < $dead) {
                return null; // the caller's deadline
            }
        } elseif ($code === 4001) {
            $this->close();
            throw new AuthenticationException('stream_credential_ended', 'The key or session of this stream ended.');
        } elseif ($code === 4008) {
            $this->resume = false;
            $this->gap(); // the next connection starts from now: what came after the last event told is not sent
        }
        // dead (three heartbeats without a frame), dropped or closed: the next connection after a wait
        $this->drop(match ($code) {
            4013 => 30 + StreamSocket::backoff($this->attempt),
            1012 => StreamSocket::restart(),
            default => StreamSocket::backoff($this->attempt),
        }, match (true) {
            $code !== null => "close {$code}",
            isset($it['timeout']) => 'three heartbeats without a frame',
            default => 'connection lost',
        });
        return null;
    }

    /** Ends this connection: the next one opens after $wait seconds (a log line says when, and why). */
    private function drop(float $wait, string $cause): void
    {
        $this->socket?->close();
        $this->socket = null;
        $this->reconnectAt = microtime(true) + $wait;
        $this->attempt++;
        $this->c->log('info', sprintf('phoenixperps: account stream reconnecting in %d ms (%s)', (int) ($wait * 1000), $cause));
    }

    /** Events after the last one told may be missing: told, never hidden. */
    private function gap(): void
    {
        $this->c->log('warn', 'phoenixperps: account stream gap after ' . ($this->lastEventId ?? 'its start') . ': read GET /v1/events?after= it');
        if ($this->onGap !== null) {
            ($this->onGap)($this->lastEventId);
        }
    }

    /**
     * @param array<string, mixed> $f
     * @return Event|null an event not told before
     */
    private function handle(array $f): ?Event
    {
        switch ($f['type'] ?? '') {
            case 'hello':
                $this->attempt = 0;
                $this->heartbeat = isset($f['heartbeat_ms']) && $f['heartbeat_ms'] > 0 ? $f['heartbeat_ms'] / 1000 : 15.0;
                $msg = ['op' => 'subscribe'];
                // the accounts asked (else every account, the hello's), less the ones unsubscribed
                $accounts = $this->accounts ?: ($this->dropped ? (array) ($f['accounts'] ?? []) : []);
                if ($accounts) {
                    $msg['accounts'] = array_values(array_filter($accounts, fn ($a) => !isset($this->dropped[strtoupper((string) $a)])));
                }
                if ($this->risk) {
                    $msg['risk'] = true;
                }
                $this->socket->send($msg);
                break;
            case 'snapshot':
                $this->snapshots[(string) ($f['account_id'] ?? '')] = $f;
                break;
            case 'snapshot_end':
                $this->ready = true;
                break;
            case 'risk':
                $this->risks[(string) ($f['account_id'] ?? '')] = $f;
                if ($this->onRisk !== null) {
                    ($this->onRisk)($f);
                }
                break;
            case 'notice':
                if (($f['code'] ?? null) === 'resume_gap') { // the resume could not be covered: a snapshot follows
                    $this->gap();
                }
                break;
            case 'error':
                // the subscribe refused: sent again as it is on every reconnect, it would be refused again
                $this->close();
                throw StreamSocket::refused($f);
            case 'event':
                // the frame's seq and replayed kept on the event ($extra)
                $e = Event::fromArray((is_array($f['event'] ?? null) ? $f['event'] : []) + ['seq' => $f['seq'] ?? null, 'replayed' => (bool) ($f['replayed'] ?? false)]);
                if ($e->id === null || isset($this->seen[$e->id])) {
                    break;
                }
                $this->seen[$e->id] = true;
                if (count($this->seen) > self::LRU) {
                    unset($this->seen[array_key_first($this->seen)]);
                }
                $this->lastEventId = $e->id;
                return $e;
        }
        return null;
    }
}
