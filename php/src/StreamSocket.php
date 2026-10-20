<?php

declare(strict_types=1);

namespace PhoenixPerps;

use PhoenixPerps\Exception\PhoenixPerpsException;
use PhoenixPerps\Exception\ValidationException;
use Psr\Http\Message\MessageInterface;
use Psr\Http\Message\ResponseInterface;
use WebSocket\Client as WsClient;
use WebSocket\Connection;
use WebSocket\Exception\ConnectionTimeoutException;
use WebSocket\Exception\Exception as WsException;
use WebSocket\Exception\HandshakeException;
use WebSocket\Message\Close;
use WebSocket\Message\Text;
use WebSocket\Middleware\CloseHandler;
use WebSocket\Middleware\PingResponder;
use WebSocket\Middleware\ProcessHttpIncomingInterface;
use WebSocket\Middleware\ProcessHttpStack;

/**
 * One WebSocket connection of a stream (phrity/websocket), read as items: a frame (decoded JSON), a close (its code),
 * a timeout, or a failure. The key goes on the upgrade; a refused upgrade answers the REST error envelope, read here.
 *
 * @internal
 */
final class StreamSocket
{
    private WsClient $ws;

    /**
     * @throws PhoenixPerpsException the API's error (its code, status, request id, details) when the upgrade was refused
     * @throws \RuntimeException when no connection came (tried again after a backoff)
     */
    public function __construct(Client $c, string $url)
    {
        $refusal = new class () implements ProcessHttpIncomingInterface {
            public string $body = '';

            // phrity reads an answer's head only: the body of a refusal (its Content-Length, at most 64 KiB) is read here
            public function processHttpIncoming(ProcessHttpStack $stack, Connection $connection): MessageInterface
            {
                $m = $stack->handleHttpIncoming();
                $n = $m instanceof ResponseInterface && $m->getStatusCode() !== 101 ? min(65536, (int) $m->getHeaderLine('Content-Length')) : 0;
                try {
                    while (strlen($this->body) < $n && ($chunk = $connection->getStream()->read($n - strlen($this->body))) !== '') {
                        $this->body .= $chunk;
                    }
                } catch (\Throwable) {
                }
                return $m;
            }

            public function __toString(): string
            {
                return 'PhoenixPerps refusal body';
            }
        };
        $this->ws = new WsClient($url);
        if ($c->bearer() !== null) {
            $this->ws->addHeader('Authorization', $c->bearer());
        }
        $this->ws->addHeader('User-Agent', $c->userAgent())
            ->addMiddleware(new CloseHandler())->addMiddleware(new PingResponder())->addMiddleware($refusal)->setTimeout(15);
        try {
            $this->ws->connect();
        } catch (HandshakeException $e) {
            $r = $e->getResponse();
            $h = [];
            foreach ($r->getHeaders() as $name => $values) {
                $h[strtolower((string) $name)] = implode(',', $values);
            }
            $ra = isset($h['retry-after']) && is_numeric($h['retry-after']) ? (float) $h['retry-after'] : null;
            throw PhoenixPerpsException::fromAnswer($r->getStatusCode(), $h, $refusal->body, $ra, null);
        } catch (\Throwable $e) {
            throw new \RuntimeException('connection failed', 0, $e); // tried again after a backoff
        }
    }

    /** The upgrade refused for good (anything but 429 and 5xx): what a reconnect cannot change ends the stream. */
    public static function forGood(\RuntimeException $e): bool
    {
        return $e instanceof PhoenixPerpsException && $e->status !== 429 && $e->status < 500;
    }

    /** Why a connection did not come: the refusal's status and code, else the failure. */
    public static function cause(\RuntimeException $e): string
    {
        return $e instanceof PhoenixPerpsException ? "upgrade refused: {$e->status} {$e->errorCode}" : 'connection failed: ' . ($e->getPrevious()?->getMessage() ?? $e->getMessage());
    }

    /** The wait before the next connection after a refusal or a failure: its Retry-After, else the backoff. */
    public static function after(\RuntimeException $e, int $attempt): float
    {
        return $e instanceof PhoenixPerpsException && $e->retryAfter !== null ? $e->retryAfter : self::backoff($attempt);
    }

    /**
     * A stream's error frame (an op of ours the server refused) as the SDK's error, its details kept: the market's
     * {code, details}, the account stream's {code, details, account_id} (its accountId, and in the details too).
     *
     * @param array<string, mixed> $f
     */
    public static function refused(array $f): ValidationException
    {
        $code = (string) ($f['code'] ?? 'stream_refused');
        $details = is_array($f['details'] ?? null) ? $f['details'] : [];
        if (isset($f['account_id'])) {
            $details['account_id'] = $f['account_id'];
        }
        return new ValidationException($code, (string) ($f['message'] ?? $code), details: $details ?: null, accountId: isset($f['account_id']) ? (string) $f['account_id'] : null);
    }

    /**
     * The next item within $wait seconds.
     *
     * @return array{frame?: array<string, mixed>, close?: int, timeout?: true, failed?: true}
     */
    public function next(float $wait): array
    {
        // phrity opens a new connection by itself in receive() and send() when it finds the old one closed: that
        // reconnect would skip the stream's resume (events lost, no gap told), so a closed link is a failure here
        if (!$this->ws->isConnected()) {
            return ['failed' => true];
        }
        $this->ws->setTimeout($wait);
        try {
            while (true) {
                $m = $this->ws->receive();
                if ($m instanceof Close) {
                    return ['close' => (int) ($m->getCloseStatus() ?? 1005)];
                }
                if ($m instanceof Text) {
                    $f = json_decode($m->getContent(), true);
                    if (is_array($f)) {
                        return ['frame' => $f];
                    }
                }
            }
        } catch (ConnectionTimeoutException) {
            return ['timeout' => true];
        } catch (WsException | \Throwable) {
            return ['failed' => true];
        }
    }

    /** @param array<string, mixed> $message */
    public function send(array $message): bool
    {
        if (!$this->ws->isConnected()) {
            return false; // never phrity's own reconnect (see next())
        }
        try {
            $this->ws->send(new Text(json_encode($message, JSON_UNESCAPED_SLASHES)));
            return true;
        } catch (\Throwable) {
            return false;
        }
    }

    public function close(): void
    {
        try {
            $this->ws->disconnect();
        } catch (\Throwable) {
        }
    }

    public static function backoff(int $n): float
    {
        return (random_int(0, PHP_INT_MAX) / PHP_INT_MAX) * min(30.0, 0.5 * 2 ** min($n, 16));
    }

    public static function restart(): float
    {
        return (random_int(0, PHP_INT_MAX) / PHP_INT_MAX) * 3.0;
    }
}
