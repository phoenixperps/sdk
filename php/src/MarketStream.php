<?php

declare(strict_types=1);

namespace PhoenixPerps;

use PhoenixPerps\Exception\AuthenticationException;
use PhoenixPerps\Exception\StreamClosedException;

/**
 * The market data stream (docs/BEHAVIOUR.md "Streams"): subscribe('ticker', ['BTC-USD'], ['interval_ms' => 250])
 * answers once the server took it (a refusal throws a ValidationException with the server's details); every
 * subscription it took is made again after a reconnect (its history asked again: a trade's id, a candle's time tell
 * one told before), and an ask still unanswered goes again on the next connection. A subscription is one channel, one
 * market and (candles) one interval; its other params are settings the last subscribe sets. Three heartbeats without a
 * frame mean a dead connection: it reconnects, a log line per reconnect with its cause (an upgrade refused with 429 or
 * 5xx after its Retry-After; any other refusal ends the stream with the API's error). Frames that come while an ask
 * waits are kept for the iteration (at most 1024: the oldest go first).
 *
 *     $market = $client->marketStream();
 *     $market->subscribe('ticker', ['BTC-USD']);
 *     foreach ($market as $frame) { ... }   // ['type' => 'ticker', 'symbol' => ..., 'data' => ...]
 *     $frame = $market->poll(1.0);          // or one frame within a second, else null
 *
 * @implements \IteratorAggregate<int, array<string, mixed>>
 */
final class MarketStream implements \IteratorAggregate
{
    private ?StreamSocket $socket = null;
    private bool $closed = false;
    private int $attempt = 0;
    private int $id = 0;
    private float $heartbeat = 15.0;
    private float $alive = 0.0; // when the connection last sent a frame
    private float $reconnectAt = 0.0;
    /** @var array<string, array{channel: string, symbol: string, params: array<string, mixed>}> what the server took, by channel, market and interval */
    private array $subs = [];
    /** @var list<array<string, mixed>> */
    private array $frames = [];
    /** @var array<string, mixed>|null the hello's limits (subscriptions, symbols_per_subscribe, messages_per_s) */
    public ?array $limits = null;

    public function __construct(private readonly Client $c)
    {
    }

    /** Ends the stream. */
    public function close(): void
    {
        $this->closed = true;
        $this->socket?->close();
        $this->socket = null;
    }

    /** One subscription: a channel, a market and (candles) an interval. */
    private static function key(string $channel, string $symbol, mixed $interval): string
    {
        return $channel . '|' . strtoupper($symbol) . '|' . $interval;
    }

    private static function message(string $op, string $channel, array $symbols, array $params): array
    {
        $m = $params + ['op' => $op, 'channel' => $channel];
        if ($channel !== 'status') {
            $m['symbols'] = array_values($symbols);
        }
        return $m;
    }

    /**
     * Follows symbols on a channel (ticker, book, trades, candles, stats, status) with its params (candles: interval,
     * part of the subscription; the others are its settings).
     *
     * @param list<string> $symbols
     * @param array<string, mixed> $params
     * @param float|null $wait the seconds to wait for the answer (null: until it comes, or the stream ends)
     * @return array<string, mixed> the server's reply (the symbols taken and the ones it refused)
     */
    public function subscribe(string $channel, array $symbols = [], array $params = [], ?float $wait = null): array
    {
        $reply = $this->ask(self::message('subscribe', $channel, $symbols, $params), $wait);
        $rejected = array_map(fn ($r) => strtoupper((string) ($r['symbol'] ?? '')), $reply['rejected'] ?? []);
        foreach ($channel === 'status' ? [''] : $symbols as $s) {
            if (!in_array(strtoupper($s), $rejected, true)) { // taken: made again after a reconnect, with these settings
                $this->subs[self::key($channel, $s, $params['interval'] ?? null)] = ['channel' => $channel, 'symbol' => $s, 'params' => $params];
            }
        }
        return $reply;
    }

    /**
     * Stops following symbols on a channel (none: every one of it); candles: the interval in $params, else every one.
     *
     * @param list<string> $symbols
     * @param array<string, mixed> $params only the interval counts
     * @param float|null $wait the seconds to wait for the answer (null: until it comes, or the stream ends)
     * @return array<string, mixed> the server's reply (the symbols ended and, rejected, the ones not followed)
     */
    public function unsubscribe(string $channel, array $symbols = [], array $params = [], ?float $wait = null): array
    {
        $interval = $params['interval'] ?? null;
        $named = array_map('strtoupper', $symbols);
        foreach ($this->subs as $k => $sub) {
            if ($sub['channel'] === $channel && (!$named || in_array(strtoupper($sub['symbol']), $named, true)) && ($interval === null || ($sub['params']['interval'] ?? null) === $interval)) {
                unset($this->subs[$k]);
            }
        }
        // the markets and (candles) the interval, nothing else
        return $this->ask(self::message('unsubscribe', $channel, $symbols, $interval === null ? [] : ['interval' => $interval]), $wait);
    }

    /** @return array<string, mixed> the server's list of this connection's subscriptions (subscriptions: channel, symbol, interval) */
    public function list(float $wait = 10.0): array
    {
        return $this->ask(['op' => 'list'], $wait);
    }

    /** @return array<string, mixed> the server's pong */
    public function ping(float $wait = 10.0): array
    {
        return $this->ask(['op' => 'ping'], $wait);
    }

    /** Connects (again) when needed: true once open (hello read, subscriptions made again). */
    private function ensure(float $deadline): bool
    {
        while ($this->socket === null) {
            if ($this->closed) {
                throw new StreamClosedException('stream_closed', 'The stream is closed.');
            }
            if (!$this->pause($this->reconnectAt - microtime(true), $deadline)) {
                return false;
            }
            try {
                $s = new StreamSocket($this->c, $this->c->marketStreamUrl);
            } catch (\RuntimeException $e) {
                if (StreamSocket::forGood($e)) {
                    $this->closed = true;
                    throw $e;
                }
                $this->retry(StreamSocket::after($e, $this->attempt), StreamSocket::cause($e));
                continue;
            }
            $hello = $s->next(15.0); // part of connecting, as the upgrade (its own 15 s), whatever the caller's wait
            if (($hello['frame']['type'] ?? null) !== 'hello') {
                $s->close();
                $this->retry(StreamSocket::backoff($this->attempt), 'no hello');
                continue;
            }
            $this->attempt = 0;
            $this->heartbeat = ($hello['frame']['heartbeat_ms'] ?? 0) > 0 ? $hello['frame']['heartbeat_ms'] / 1000 : 15.0;
            $this->alive = microtime(true);
            $this->limits = $hello['frame']['limits'] ?? null;
            // every subscription the server took, again, with its settings: one subscribe per channel and settings
            $again = [];
            foreach ($this->subs as $sub) {
                $g = $sub['channel'] . '|' . json_encode($sub['params']);
                $again[$g] ??= ['channel' => $sub['channel'], 'params' => $sub['params'], 'symbols' => []];
                $again[$g]['symbols'][] = $sub['symbol'];
            }
            foreach ($again as $a) {
                foreach (array_chunk($a['symbols'], max(1, (int) ($this->limits['symbols_per_subscribe'] ?? 50))) as $symbols) {
                    $s->send(self::message('subscribe', $a['channel'], $symbols, $a['params']) + ['id' => ++$this->id]);
                }
            }
            $this->socket = $s;
        }
        return true;
    }

    /** The next connection after $wait seconds (a log line says when, and why). */
    private function retry(float $wait, string $cause): void
    {
        $this->reconnectAt = microtime(true) + $wait;
        $this->attempt++;
        $this->c->log('info', sprintf('phoenixperps: market stream reconnecting in %d ms (%s)', (int) ($wait * 1000), $cause));
    }

    /** Waits $seconds, or until the deadline when it comes first (false then). */
    private function pause(float $seconds, float $deadline): bool
    {
        if ($seconds <= 0) {
            return true;
        }
        $late = microtime(true) + $seconds > $deadline;
        usleep((int) (max(0.0, $late ? $deadline - microtime(true) : $seconds) * 1e6));
        return !$late;
    }

    /**
     * Sends a message (again on each new connection) and reads until its reply, within $wait seconds (null: until the
     * stream ends).
     *
     * @param array<string, mixed> $message
     */
    private function ask(array $message, ?float $wait): array
    {
        $deadline = $wait === null ? INF : microtime(true) + $wait;
        $message['id'] = ++$this->id;
        while (microtime(true) < $deadline) {
            if (!$this->ensure($deadline)) {
                break;
            }
            if (!$this->socket->send($message)) {
                $this->drop();
                $this->retry(StreamSocket::backoff($this->attempt), 'connection lost');
                continue; // goes again on the next connection
            }
            while (microtime(true) < $deadline) {
                $it = $this->read(max(0.05, $deadline - microtime(true)));
                if ($it === null) {
                    break; // dropped: ask again on the next connection
                }
                if (isset($it['timeout'])) {
                    continue;
                }
                $f = $it['frame'];
                if (in_array($f['type'] ?? '', ['subscribed', 'unsubscribed', 'error', 'list', 'pong'], true) && ($f['id'] ?? null) === $message['id']) {
                    if ($f['type'] === 'error') {
                        throw StreamSocket::refused($f);
                    }
                    return $f;
                }
                $this->keep($f);
            }
        }
        throw new Exception\ApiTimeoutException('timeout', sprintf('No answer within %.3g s.', $wait), retryable: true);
    }

    /**
     * The next item within $wait seconds, the wait cut at three heartbeats after the last frame.
     *
     * @return array{frame?: array<string, mixed>, timeout?: true}|null null when the connection ended or went silent
     *     (it reconnects, after a wait)
     */
    private function read(float $wait): ?array
    {
        $dead = $this->alive + 3 * $this->heartbeat;
        $it = $this->socket->next(max(0.001, min($wait, $dead - microtime(true))));
        if (isset($it['frame'])) {
            $this->alive = microtime(true);
            return $it;
        }
        if (isset($it['timeout']) && microtime(true) < $dead) {
            return $it;
        }
        $code = $it['close'] ?? null;
        if ($code === 4001) {
            $this->close();
            throw new AuthenticationException('stream_credential_ended', 'The key of this stream ended.');
        }
        $this->drop();
        $this->retry($code === 1012 ? StreamSocket::restart() : StreamSocket::backoff($this->attempt), match (true) {
            $code !== null => "close {$code}",
            isset($it['timeout']) => 'three heartbeats without a frame',
            default => 'connection lost',
        });
        return null;
    }

    private function drop(): void
    {
        $this->socket?->close();
        $this->socket = null;
    }

    /** @param array<string, mixed> $f */
    private function keep(array $f): void
    {
        $t = $f['type'] ?? '';
        if (in_array($t, ['heartbeat', 'subscribed', 'unsubscribed', 'error', 'list', 'pong', 'hello'], true)) {
            return;
        }
        $this->frames[] = $f;
        if (count($this->frames) > 1024) {
            array_shift($this->frames); // a slow reader loses the oldest frame, never the connection
        }
    }

    /**
     * The next frame within $wait seconds, else null (none came, or the stream is closed): a loop that does other things
     * between frames. Reconnects as the iteration does; throws what ended the stream.
     *
     * @return array<string, mixed>|null
     */
    public function poll(float $wait): ?array
    {
        $deadline = microtime(true) + $wait;
        while (!$this->closed) {
            if ($this->frames) {
                return array_shift($this->frames);
            }
            if (!$this->ensure($deadline)) {
                return null;
            }
            $it = $this->read(max(0.001, min(60.0, $deadline - microtime(true))));
            if ($it !== null && isset($it['frame'])) {
                $this->keep($it['frame']);
            } elseif (microtime(true) >= $deadline) {
                return null;
            }
        }
        return null;
    }

    /** @return \Generator<int, array<string, mixed>> every frame until close(); throws what ended the stream */
    public function getIterator(): \Generator
    {
        while (!$this->closed) {
            $f = $this->poll(60.0);
            if ($f !== null) {
                yield $f;
            }
        }
    }
}
