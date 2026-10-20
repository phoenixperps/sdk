<?php

declare(strict_types=1);

namespace PhoenixPerps;

use GuzzleHttp\Client as Http;
use GuzzleHttp\ClientInterface;
use GuzzleHttp\Exception\ConnectException;
use GuzzleHttp\Exception\GuzzleException;
use PhoenixPerps\Exception\ApiConnectionException;
use PhoenixPerps\Exception\ApiTimeoutException;
use PhoenixPerps\Exception\AuthenticationException;
use PhoenixPerps\Exception\PhoenixPerpsException;
use PhoenixPerps\Exception\ServerException;
use PhoenixPerps\Exception\ValidationException;

/**
 * The Phoenix Perpetuals API (docs/BEHAVIOUR.md): every REST call, the account stream and market data, with retries,
 * idempotency keys, paging and typed errors.
 *
 *     $client = new PhoenixPerps\Client();                 // PHOENIXPERPS_API_KEY
 *     $order = $client->orders->create($accountId, new CreateOrder(market: 'BTC-USD', side: OrderSide::BUY,
 *         type: CreateOrderType::LIMIT, size: '0.01', limitPrice: '62000'));
 */
final class Client extends BaseClient
{
    public const SDK_VERSION = '1.0.0';
    private const RETRY_AFTER_MAX = 60.0;
    private const PAGE = ['data' => true, 'has_more' => true, 'next_cursor' => true, 'as_of' => true]; // a list's own fields

    private readonly ?string $key;
    public readonly string $environment;
    public readonly string $baseUrl;
    public readonly string $accountStreamUrl;
    public readonly string $marketStreamUrl;
    private readonly ClientInterface $http;
    /** @var array{limit: int, remaining: int, resetAt: float|null}|null */
    private ?array $rateLimit = null;
    /** @var array<string, array{limit: int, remaining: int, resetAt: float|null}> the window of each budget (an operation's x-pp-scope) */
    private array $budgets = [];
    /** @var array<string, true> */
    private array $warned = [];

    /**
     * A client; a key of the other environment fails here, before any request. Without a key (none given, none in
     * PHOENIXPERPS_API_KEY) the public calls work (markets, status) and any other answers the API's missing_api_key.
     *
     * @param string|null $apiKey else PHOENIXPERPS_API_KEY
     * @param string|null $environment live or sandbox in any case (else the key's own, else PHOENIXPERPS_ENVIRONMENT)
     * @param string|null $baseUrl the REST host (else PHOENIXPERPS_BASE_URL, else the environment's); the REST address
     *     the docs write, with /v1, works too. It moves the REST calls only: the streams take their own URLs
     * @param float $timeout the seconds one attempt may take
     * @param int $maxRetries how often a safe call is tried again
     * @param string|null $appInfo your app's name and version, added to the User-Agent
     * @param (\Closure(string, string): void)|null $logger receives (level, message): a line per retry, per stream reconnect
     *     (with its cause) and per gap, and one per deprecated API version
     * @param string|null $accountStreamUrl else the environment's account stream
     * @param string|null $marketStreamUrl else the market data stream
     */
    public function __construct(
        ?string $apiKey = null,
        ?string $environment = null,
        ?string $baseUrl = null,
        public readonly float $timeout = 30.0,
        public readonly int $maxRetries = 2,
        public readonly ?string $appInfo = null,
        ?ClientInterface $httpClient = null,
        private readonly ?\Closure $logger = null,
        ?string $accountStreamUrl = null,
        ?string $marketStreamUrl = null,
    ) {
        $key = ($apiKey ?? getenv('PHOENIXPERPS_API_KEY')) ?: null;
        $inferred = $key !== null && preg_match('/^(ppk|ppo)_(live|test)_/', $key, $m) ? ($m[2] === 'live' ? 'live' : 'sandbox') : null;
        // the one given, else the key's own, else PHOENIXPERPS_ENVIRONMENT (a variable never turns a key against itself)
        $env = strtolower($environment ?? $inferred ?? (getenv('PHOENIXPERPS_ENVIRONMENT') ?: null) ?? 'sandbox');
        if ($env !== 'live' && $env !== 'sandbox') {
            throw new ValidationException('invalid_environment', 'The environment is live or sandbox.');
        }
        if ($inferred !== null && $inferred !== $env) {
            throw new AuthenticationException('wrong_environment', "This key is a {$inferred} key, not a {$env} one.", details: ['key_environment' => $inferred]);
        }
        $this->key = $key;
        $this->environment = $env;
        // the host: every path starts with /v1, so the REST address as the docs write it (host/v1) loses it here
        $this->baseUrl = preg_replace('#/v1$#', '', rtrim($baseUrl ?? (getenv('PHOENIXPERPS_BASE_URL') ?: null) ?? Meta::SERVERS[$env], '/'));
        $this->accountStreamUrl = $accountStreamUrl ?? Meta::ACCOUNT_STREAMS[$env];
        $this->marketStreamUrl = $marketStreamUrl ?? Meta::MARKET_STREAM;
        $this->http = $httpClient ?? new Http(['http_errors' => false, 'connect_timeout' => 10]);
        $this->initResources($this);
    }

    /** The key with its secret hidden (ppk_test_****abcd); empty without a key. */
    public function maskedKey(): string
    {
        if ($this->key === null) {
            return '';
        }
        $from = min(4, strlen($this->key));
        $i = strpos($this->key, '_', $from);
        return ($i === false ? '' : substr($this->key, 0, $i + 1)) . '****' . (strlen($this->key) > 4 ? substr($this->key, -4) : $this->key);
    }

    /** @return array{limit: int, remaining: int, resetAt: float|null}|null what the last answer said of the rate-limit window */
    public function rateLimit(): ?array
    {
        return $this->rateLimit;
    }

    /** What every request says of the SDK. */
    public function userAgent(): string
    {
        return 'phoenixperps-php/' . self::SDK_VERSION . ' (php ' . PHP_VERSION . '; ' . strtolower(PHP_OS_FAMILY) . ') spec/' . Meta::SPEC_VERSION . ($this->appInfo === null ? '' : ' ' . $this->appInfo);
    }

    /** Never shows the key. */
    public function __toString(): string
    {
        return "PhoenixPerps\\Client{environment={$this->environment}, baseUrl={$this->baseUrl}, apiKey=" . ($this->maskedKey() ?: 'none') . '}';
    }

    /** @return array<string, string|null> never the key (var_dump, print_r) */
    public function __debugInfo(): array
    {
        return ['environment' => $this->environment, 'baseUrl' => $this->baseUrl, 'apiKey' => $this->maskedKey() ?: null];
    }

    /** The client is not meant to travel (its key neither). */
    public function __serialize(): array
    {
        throw new \LogicException('A PhoenixPerps client is not serializable.');
    }

    /** @internal the key, for the streams' upgrade (null without one) */
    public function bearer(): ?string
    {
        return $this->key === null ? null : 'Bearer ' . $this->key;
    }

    /**
     * Connects to the account stream (each event once, resumed after reconnects).
     *
     * @param list<string> $accounts else every account of the key
     * @param bool $risk risk frames too, each told to $onRisk as it comes
     * @param string|null $resume the event to continue after
     * @param (\Closure(array<string, mixed>): void)|null $onRisk receives each risk frame
     * @param (\Closure(string|null): void)|null $onGap receives the last event id told when events after it may be
     *     missing (a resume the server could not cover, a 4008): read GET /v1/events?after= it
     */
    public function accountStream(array $accounts = [], bool $risk = false, ?string $resume = null, ?\Closure $onRisk = null, ?\Closure $onGap = null): AccountStream
    {
        return new AccountStream($this, $accounts, $risk, $resume, $onRisk, $onGap);
    }

    /** Connects to the market data stream. */
    public function marketStream(): MarketStream
    {
        return new MarketStream($this);
    }

    /** @internal the logger's line, for the streams too (a failure's text in it never shows the key) */
    public function log(string $level, string $message): void
    {
        if ($this->logger !== null) {
            ($this->logger)($level, $this->hideKey($message));
        }
    }

    private function preWait(string $scope): void
    {
        $r = $this->budgets[$scope] ?? null;
        if ($r === null || $r['remaining'] > 0 || $r['resetAt'] === null) {
            return;
        }
        $wait = $r['resetAt'] - microtime(true);
        if ($wait > 0 && $wait < 60) {
            usleep((int) ($wait * 1e6));
        }
    }

    /** @param array<string, string> $h */
    private function remember(array $h, string $scope): void
    {
        // any answer may say so, also one without the rate-limit headers (a 401, an unknown route)
        $dep = $h['deprecation'] ?? null;
        if ($dep !== null && !isset($this->warned[$dep])) {
            $this->warned[$dep] = true;
            $this->log('warn', "phoenixperps: this API version is deprecated (Deprecation: {$dep})");
        }
        if (!isset($h['x-ratelimit-limit'])) {
            return;
        }
        $resetAt = null;
        if (isset($h['x-ratelimit-reset']) && is_numeric($h['x-ratelimit-reset'])) {
            $reset = (float) $h['x-ratelimit-reset'];
            // Reset as epoch seconds (above a year in seconds) or as seconds from now
            $resetAt = $reset > 31536000 ? $reset : microtime(true) + $reset;
        }
        $this->rateLimit = $this->budgets[$scope] = ['limit' => (int) $h['x-ratelimit-limit'], 'remaining' => (int) ($h['x-ratelimit-remaining'] ?? 0), 'resetAt' => $resetAt];
    }

    /** @param array<string, string> $h */
    private static function retryAfter(array $h): ?float
    {
        $v = trim($h['retry-after'] ?? '');
        if ($v === '') {
            return null;
        }
        if (is_numeric($v)) {
            return max(0.0, (float) $v); // seconds, a fraction too (1.5)
        }
        $t = strtotime($v);
        return $t === false ? null : max(0.0, $t - microtime(true));
    }

    /** A text quoting the key, with it masked. */
    private function hideKey(string $text): string
    {
        return $this->key === null ? $text : str_replace($this->key, $this->maskedKey(), $text);
    }

    private static function jitter(int $attempt): float
    {
        return (random_int(0, PHP_INT_MAX) / PHP_INT_MAX) * min(8.0, 0.5 * 2 ** $attempt);
    }

    /** @param array<string, mixed> $op */
    private function url(array $op, array $path, ?array $query): string
    {
        $values = array_values($path);
        $n = 0;
        $p = preg_replace_callback('/\{([^}]+)\}/', function ($m) use ($values, &$n) {
            $v = $values[$n++] ?? '';
            if ($v === '') {
                throw new ValidationException('invalid_request', "{$m[1]} is required.");
            }
            return rawurlencode((string) $v);
        }, $op['path']);
        $q = $query ? '?' . http_build_query($query, '', '&', PHP_QUERY_RFC3986) : '';
        return $this->baseUrl . $p . $q;
    }

    /**
     * One logical call: its attempts, and its answer.
     *
     * @param array<string, mixed> $op
     * @param list<string> $path
     * @param array<string, string>|null $query
     * @internal
     */
    public function send(array $op, array $path, ?array $query, ?string $body, ?RequestOptions $o): RawResponse
    {
        $key = $o?->idempotencyKey ?? ($op['idempotent'] ? self::uuid4() : null);
        $safe = $op['retrySafe'] || $key !== null; // sent again only when that is safe: a read, a retry-safe route, a key
        $scope = $op['scope'] ?? ''; // its budget
        $max = $o?->maxRetries ?? $this->maxRetries;
        $timeout = $o?->timeout ?? $this->timeout;
        $url = $this->url($op, $path, $query);
        for ($attempt = 0;; $attempt++) {
            $this->preWait($scope);
            $headers = ['Accept' => 'application/json', 'User-Agent' => $this->userAgent()] + ($o?->headers ?? []);
            if ($this->key !== null) {
                $headers['Authorization'] = 'Bearer ' . $this->key;
            }
            if ($key !== null) {
                $headers['Idempotency-Key'] = $key;
            }
            if ($body !== null) {
                $headers['Content-Type'] = 'application/json';
            }
            $failure = null;
            try {
                $res = $this->http->request($op['method'], $url, ['headers' => $headers, 'body' => $body, 'timeout' => $timeout, 'http_errors' => false]);
            } catch (ConnectException $e) {
                $timedOut = str_contains(strtolower($e->getMessage()), 'timed out') || str_contains(strtolower($e->getMessage()), 'timeout');
                $failure = $timedOut
                    ? new ApiTimeoutException('timeout', sprintf('No answer within %.3g s.', $timeout), idempotencyKey: $key, previous: $e, retryable: true)
                    : new ApiConnectionException('connection_failed', mb_substr($this->hideKey($e->getMessage()), 0, 200), idempotencyKey: $key, previous: $e, retryable: true);
            } catch (GuzzleException $e) {
                $failure = new ApiConnectionException('connection_failed', mb_substr($this->hideKey($e->getMessage()), 0, 200), idempotencyKey: $key, previous: $e, retryable: true);
            }
            if ($failure === null) {
                $h = [];
                foreach ($res->getHeaders() as $name => $values) {
                    $h[strtolower($name)] = implode(',', $values);
                }
                $this->remember($h, $scope);
                $raw = new RawResponse($res->getStatusCode(), $h, (string) $res->getBody(), $h['x-request-id'] ?? null, $key);
                if ($o?->onResponse !== null) {
                    ($o->onResponse)($raw);
                }
                $status = $raw->status;
                // a 304 answers the caller's own If-None-Match: nothing changed, no error (and no body)
                if (($status >= 200 && $status < 300) || $status === 304) {
                    return $raw;
                }
                $ra = self::retryAfter($h);
                $err = PhoenixPerpsException::fromAnswer($status, $h, $raw->body, $ra, $key);
                // again only when the API says it may work (its retryable; without its envelope, 429 and 5xx)
                if (!($safe && $attempt < $max && $err->retryable) || ($ra !== null && $ra > self::RETRY_AFTER_MAX)) {
                    throw $err;
                }
                $wait = $ra ?? self::jitter($attempt);
                $this->log('debug', sprintf('phoenixperps: retrying %s after %d in %d ms (attempt %d)', $op['id'], $status, (int) ($wait * 1000), $attempt + 1));
                usleep((int) ($wait * 1e6));
                continue;
            }
            if (!($safe && $attempt < $max)) {
                throw $failure;
            }
            $wait = self::jitter($attempt);
            $this->log('debug', sprintf('phoenixperps: retrying %s after %s in %d ms (attempt %d)', $op['id'], $failure->errorCode, (int) ($wait * 1000), $attempt + 1));
            usleep((int) ($wait * 1e6));
        }
    }

    /**
     * Any route, also one this SDK version does not know (v1 only grows), with the key, retries and errors of every
     * call: $client->request('GET', '/v1/status')->body. A route this SDK knows keeps its own rules (tried again when
     * retry-safe, its Idempotency-Key); another is tried again when a GET or sent with RequestOptions::$idempotencyKey.
     *
     * @param string $path /v1/...
     * @param array<string, mixed>|null $query
     * @param mixed $body the JSON body (an array or a model), or null
     */
    public function request(string $method, string $path, ?array $query = null, mixed $body = null, ?RequestOptions $options = null): RawResponse
    {
        if (!str_starts_with($path, '/')) {
            throw new ValidationException('invalid_request', 'The path starts with /.');
        }
        $m = strtoupper($method);
        $known = null;
        foreach ((new \ReflectionClass(Ops::class))->getConstants() as $op) {
            if ($op['method'] === $m && preg_match('#^' . preg_replace('#\\\\\{[^/]+\\\\\}#', '[^/]+', preg_quote($op['path'], '#')) . '$#', $path)) {
                $known = $op;
                break;
            }
        }
        $op = ['method' => $m, 'path' => $path] + ($known ?? ['id' => "{$m} {$path}", 'idempotent' => false, 'retrySafe' => $m === 'GET', 'cursor' => null]);
        return $this->send($op, [], self::query($query), self::encode($body), $options);
    }

    private static function uuid4(): string
    {
        $b = random_bytes(16);
        $b[6] = chr(ord($b[6]) & 0x0f | 0x40);
        $b[8] = chr(ord($b[8]) & 0x3f | 0x80);
        return vsprintf('%s%s-%s-%s-%s-%s%s%s', str_split(bin2hex($b), 4));
    }

    /** @return mixed the request body as JSON text, or null */
    private static function encode(mixed $body): ?string
    {
        if ($body === null) {
            return null;
        }
        if (is_object($body) && method_exists($body, 'toArray')) {
            $body = $body->toArray();
        }
        if ($body === []) {
            return '{}'; // a body is always an object: PHP's empty array is no list here
        }
        $json = json_encode($body, JSON_UNESCAPED_SLASHES | JSON_PRESERVE_ZERO_FRACTION);
        if ($json === false) {
            throw new ValidationException('invalid_request', json_last_error_msg());
        }
        return $json;
    }

    private static function decode(RawResponse $raw): mixed
    {
        if ($raw->body === '' && ($raw->status === 304 || $raw->status === 204)) {
            return null;
        }
        try {
            return json_decode($raw->body, true, 512, JSON_THROW_ON_ERROR | JSON_BIGINT_AS_STRING);
        } catch (\JsonException $e) {
            // a 2xx that is not JSON (a proxy's page, nothing at all) is no answer of the API's
            throw new ServerException("http_{$raw->status}", 'The answer is not JSON: ' . mb_substr($raw->body, 0, 200), $raw->status, requestId: $raw->requestId, idempotencyKey: $raw->idempotencyKey, previous: $e, headers: $raw->headers);
        }
    }

    /**
     * A call whose answer is one object ({"data": ...}).
     *
     * @param array<string, mixed> $op
     * @param list<string> $path
     * @internal
     */
    public function one(array $op, array $path, mixed $query, mixed $body, ?RequestOptions $o): ?array
    {
        $raw = $this->send($op, $path, self::query($query), self::encode($body), $o);
        $v = self::decode($raw);
        if (is_array($v) && array_key_exists('data', $v)) {
            $v = $v['data'];
        }
        return is_array($v) ? $v : null;
    }

    /** @return array<string, string>|null */
    private static function query(mixed $q): ?array
    {
        if ($q === null) {
            return null;
        }
        if (is_object($q) && method_exists($q, 'toQuery')) {
            return $q->toQuery();
        }
        return array_map(fn ($v) => is_bool($v) ? ($v ? 'true' : 'false') : (string) $v, (array) $q);
    }

    /**
     * A call whose answer is a page of a list: null on a 304 Not Modified (the If-None-Match given is current).
     *
     * @param array<string, mixed> $op
     * @param list<string> $path
     * @param callable(array<string, mixed>): mixed $item
     * @internal
     */
    public function list(array $op, array $path, mixed $query, callable $item, ?RequestOptions $o): ?Page
    {
        $q = self::query($query) ?? [];
        $raw = $this->send($op, $path, $q, null, $o);
        if ($raw->status === 304) {
            return null;
        }
        $v = self::decode($raw);
        $v = is_array($v) ? $v : [];
        $next = $op['cursor'] === null ? null : function (string $cursor) use ($op, $path, $q, $item, $o): ?Page {
            return $this->list($op, $path, [$op['cursor'] => $cursor] + $q, $item, $o);
        };
        return new Page(array_map(fn ($x) => $item(is_array($x) ? $x : []), $v['data'] ?? []), (bool) ($v['has_more'] ?? false), isset($v['next_cursor']) ? (string) $v['next_cursor'] : null, isset($v['as_of']) ? (int) $v['as_of'] : null, $raw, $next, array_diff_key($v, self::PAGE));
    }
}
