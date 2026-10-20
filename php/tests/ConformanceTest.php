<?php

declare(strict_types=1);

namespace PhoenixPerps\Tests;

use PhoenixPerps\Client;
use PhoenixPerps\Decimal;
use PhoenixPerps\Exception\AuthenticationException;
use PhoenixPerps\Exception\NotFoundException;
use PhoenixPerps\Exception\PhoenixPerpsException;
use PhoenixPerps\Exception\ServerException;
use PhoenixPerps\Exception\StreamClosedException;
use PhoenixPerps\Exception\ValidationException;
use PhoenixPerps\MarketRules;
use PhoenixPerps\Model\Market;
use PhoenixPerps\RequestOptions;
use PhoenixPerps\Tests\Generated\Dispatch;
use PhoenixPerps\WebhookVerificationException;
use PhoenixPerps\Webhooks;
use PHPUnit\Framework\Attributes\DataProvider;
use PHPUnit\Framework\TestCase;

/**
 * The conformance suite (conformance/scenarios.json) through the PHP SDK against the mock server
 * (node conformance/mock-server.js): retries, idempotency keys, Retry-After, errors, paging, open enums, decimals, the
 * rate-limit wait, the streams; and the local checks (wrong environment, the key never printed, webhook signatures,
 * exact rounding). Then this SDK's own cases, scripted on the same mock: answers (304, Deprecation, a fractional
 * Retry-After, retryable), the raw request, the page's other fields, the streams' errors, heartbeats and poll().
 */
final class ConformanceTest extends TestCase
{
    private const CLASSES = [
        'AuthenticationError' => \PhoenixPerps\Exception\AuthenticationException::class,
        'PermissionError' => \PhoenixPerps\Exception\PermissionException::class,
        'NotFoundError' => \PhoenixPerps\Exception\NotFoundException::class,
        'ConflictError' => \PhoenixPerps\Exception\ConflictException::class,
        'ValidationError' => \PhoenixPerps\Exception\ValidationException::class,
        'RuleRejectedError' => \PhoenixPerps\Exception\RuleRejectedException::class,
        'RateLimitError' => \PhoenixPerps\Exception\RateLimitException::class,
        'ServerError' => \PhoenixPerps\Exception\ServerException::class,
    ];

    /** @var resource|null */
    private static $mock = null;
    private static string $url = '';
    /** @var array<string, mixed> */
    private static array $suite = [];

    private static function suite(): array
    {
        if (!self::$suite) {
            self::$suite = json_decode((string) file_get_contents(dirname(__DIR__, 2) . '/conformance/scenarios.json'), true);
        }
        return self::$suite;
    }

    public static function setUpBeforeClass(): void
    {
        $null = PHP_OS_FAMILY === 'Windows' ? 'NUL' : '/dev/null';
        // its own stdin and stderr: no handle of the test's output outlives it
        self::$mock = proc_open(['node', dirname(__DIR__, 2) . '/conformance/mock-server.js', '0'], [0 => ['file', $null, 'r'], 1 => ['pipe', 'w'], 2 => ['file', $null, 'w']], $pipes);
        self::$url = trim((string) fgets($pipes[1]));
    }

    public static function tearDownAfterClass(): void
    {
        if (self::$mock !== null) {
            proc_terminate(self::$mock);
            proc_close(self::$mock);
        }
    }

    private static function post(array $body): void
    {
        file_get_contents(self::$url . '/__scenario', false, stream_context_create(['http' => ['method' => 'POST', 'header' => 'content-type: application/json', 'content' => json_encode($body)]]));
    }

    private static function requests(): array
    {
        return json_decode((string) file_get_contents(self::$url . '/__requests'), true);
    }

    /** A client of the mock, its streams too. */
    private static function client(?\Closure $logger = null, string $baseUrl = ''): Client
    {
        $ws = 'ws' . substr(self::$url, 4);
        return new Client(apiKey: self::suite()['key'], baseUrl: $baseUrl ?: self::$url, maxRetries: 2, logger: $logger, accountStreamUrl: "{$ws}/v1/stream", marketStreamUrl: "{$ws}/v1/market-data");
    }

    /** @return array<string, mixed> a frame the scripted server sends */
    private static function hello(int $heartbeatMs = 15000, array $more = []): array
    {
        return ['send' => ['type' => 'hello', 'heartbeat_ms' => $heartbeatMs] + $more];
    }

    /** @return array<string, mixed> */
    private static function event(string $id): array
    {
        return ['send' => ['type' => 'event', 'event' => ['id' => $id, 'type' => 'order.created', 'account_id' => 'SANDBOX-00042-001', 'time' => 1791460800000, 'source' => 'api', 'data' => []]]];
    }

    /** got, keeping only what want names (the expected objects are partial). */
    private static function pick(mixed $got, mixed $want): mixed
    {
        if (!is_array($want) || array_is_list($want)) {
            return $got;
        }
        $out = [];
        foreach ($want as $k => $v) {
            $out[$k] = self::pick(is_array($got) ? ($got[$k] ?? null) : null, $v);
        }
        return $out;
    }

    public static function scenarios(): array
    {
        $out = [];
        foreach (self::suite()['scenarios'] as $sc) {
            $out[$sc['name']] = [$sc];
        }
        return $out;
    }

    public static function streams(): array
    {
        $out = [];
        foreach (self::suite()['streams'] as $sc) {
            $out[$sc['name']] = [$sc];
        }
        return $out;
    }

    /** Every key of the vocabulary this runner plays: a new one fails here until it does. */
    private function assertPlays(array $keys, array $played, string $what): void
    {
        $this->assertSame([], array_values(array_diff($keys, $played)), "{$what} this runner does not play");
    }

    /** The error an expectation describes (every field it names). */
    private function assertError(array $want, ?PhoenixPerpsException $error): void
    {
        $this->assertNotNull($error, 'an error');
        $this->assertPlays(array_keys($want), ['class', 'code', 'status', 'retry_after_ms', 'order_id', 'request_id', 'retryable', 'doc_url', 'account_id'], 'error keys');
        if (isset($want['class'])) {
            $this->assertSame(self::CLASSES[$want['class']], $error::class);
        }
        $this->assertSame($want['code'], $error->errorCode);
        $fields = ['status' => $error->status, 'order_id' => $error->orderId, 'request_id' => $error->requestId, 'retryable' => $error->retryable, 'doc_url' => $error->docUrl, 'account_id' => $error->accountId];
        foreach (array_intersect_key($fields, $want) as $k => $v) {
            $this->assertSame($want[$k], $v, $k);
        }
        if (isset($want['retry_after_ms'])) {
            $this->assertSame($want['retry_after_ms'], (int) round($error->retryAfter * 1000));
        }
    }

    #[DataProvider('scenarios')]
    public function testRest(array $sc): void
    {
        self::post(['exchanges' => $sc['exchanges']]);
        $x = $sc['expect'];
        $this->assertPlays(array_keys($x), ['ok', 'requests', 'result', 'error', 'items', 'cursors', 'first_body', 'min_elapsed_ms', 'checks', 'page_fields', 'paths'], 'expect keys');
        $this->assertPlays(array_keys($sc['client'] ?? []), ['base_url_suffix'], 'client keys');
        $c = new Client(apiKey: self::suite()['key'], baseUrl: self::$url . ($sc['client']['base_url_suffix'] ?? ''), maxRetries: 2);
        $answer = null;
        $page = null;
        $error = null;
        try {
            foreach ($sc['calls'] ?? [$sc['call']] as $call) {
                $options = new RequestOptions(maxItems: $call['max_items'] ?? null);
                if (isset($x['page_fields'])) { // the page itself: its path parameters, its query, the options
                    $page = $c->{$call['resource']}->{$call['method']}(...[...array_values($call['path'] ?? []), $call['query'] ?? null, $options]);
                    continue;
                }
                $answer = Dispatch::run($c, $call, $options);
            }
        } catch (PhoenixPerpsException $e) {
            $error = $e;
        }
        $got = self::requests();
        $this->assertCount($x['requests'], $got, 'requests');
        $this->assertSame($x['ok'] ?? !isset($x['error']), $error === null, (string) $error?->getMessage());
        if (isset($x['error'])) {
            $this->assertError($x['error'], $error);
        } else {
            if (array_key_exists('result', $x)) { // null: the call answered nothing
                $this->assertEquals($x['result'], $x['result'] === null ? $answer['result'] : self::pick($answer['result'], $x['result']));
            }
            if (isset($x['items'])) {
                $this->assertSame($x['items'], $answer['items']);
            }
            foreach ($x['page_fields'] ?? [] as $k => $v) {
                $this->assertSame($v, $page?->extra[$k] ?? null, "the page's {$k}");
            }
        }
        if (isset($x['paths'])) {
            $this->assertSame($x['paths'], array_column($got, 'path'));
        }
        foreach ($x['cursors'] ?? [] as $i => $want) {
            $this->assertSame($want, $got[$i]['query']['cursor'] ?? null, "cursor {$i}");
        }
        if (isset($x['first_body'])) {
            $this->assertSame($x['first_body'], self::pick($got[0]['body'], $x['first_body']));
        }
        if (isset($x['min_elapsed_ms'])) {
            $this->assertGreaterThanOrEqual($x['min_elapsed_ms'], end($got)['at'] - $got[0]['at']);
        }
        foreach ($x['checks'] ?? [] as $check) {
            $h = $got[0]['headers'];
            match ($check) {
                'bearer' => $this->assertSame('Bearer ' . self::suite()['key'], $h['authorization']),
                'user_agent' => $this->assertMatchesRegularExpression('/^phoenixperps-php\/\d+\.\d+\.\d+ \(.+\) spec\//', $h['user-agent']),
                'same_idempotency_key' => $this->assertSameKey($got),
                default => $this->fail("a check this runner does not play: {$check}"),
            };
        }
    }

    private function assertSameKey(array $got): void
    {
        $keys = array_values(array_unique(array_map(fn ($r) => $r['headers']['idempotency-key'], $got)));
        $this->assertCount(1, $keys);
        $this->assertMatchesRegularExpression('/^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/', $keys[0]);
    }

    #[DataProvider('streams')]
    public function testStream(array $sc): void
    {
        self::post(['connections' => $sc['connections']]);
        $x = $sc['expect'];
        $this->assertPlays(array_keys($x), ['events', 'connections', 'resume', 'first_message', 'error', 'frames', 'resubscribe', 'unsubscribe_sent', 'limits', 'gap'], 'expect keys');
        foreach (array_merge(...$sc['connections']) as $step) {
            $this->assertPlays(array_keys($step), ['send', 'expect', 'reply', 'close', 'wait_ms', 'refuse', 'retry_after', 'body'], 'steps');
        }
        $c = self::client();
        $error = null;
        if ($sc['stream'] === 'account') {
            $gaps = [];
            $s = $c->accountStream(onGap: function (?string $after) use (&$gaps) {
                $gaps[] = $after;
            });
            $ids = [];
            try {
                foreach ($s as $e) {
                    $ids[] = $e->id;
                    if (count($ids) === count($x['events'] ?? []) && !isset($x['error'])) {
                        break;
                    }
                }
            } catch (PhoenixPerpsException $e) {
                $error = $e;
            }
            if (isset($x['error'])) {
                usleep(500000); // time enough for a wrong reconnect to show
            }
            $s->close();
            $this->assertSame($x['events'] ?? [], $ids);
            if (array_key_exists('gap', $x)) {
                $this->assertSame([$x['gap']], $gaps, 'the gap told');
            }
        } else {
            $sub = $sc['subscribe'];
            $m = $c->marketStream();
            $frames = [];
            try {
                $m->subscribe($sub['channel'], $sub['symbols'], $sub['params']);
                if (isset($sc['unsubscribe'])) {
                    $m->unsubscribe($sc['unsubscribe']['channel'], $sc['unsubscribe']['symbols'], $sc['unsubscribe']['params'] ?? []);
                }
                foreach ($m as $f) {
                    if (($f['type'] ?? null) === $sub['channel']) {
                        $frames[] = $f['data']['last'];
                    }
                    if (count($frames) === count($x['frames'])) {
                        break;
                    }
                }
            } catch (PhoenixPerpsException $e) {
                $error = $e;
            }
            $m->close();
            $this->assertSame($x['frames'], $frames);
            if (isset($x['limits'])) {
                $this->assertSame($x['limits'], $m->limits);
            }
        }
        if (isset($x['error'])) {
            $this->assertError($x['error'], $error);
        } else {
            $this->assertNull($error, (string) $error?->getMessage());
        }
        $reqs = self::requests();
        $this->assertCount($x['connections'], $reqs, 'connections');
        foreach ($x['resume'] ?? [] as $i => $want) {
            $this->assertSame($want, $reqs[$i]['query']['resume'] ?? null, "resume {$i}");
        }
        $this->assertSame('Bearer ' . self::suite()['key'], $reqs[0]['headers']['authorization']);
        if (isset($x['first_message'])) {
            $this->assertSame($x['first_message'], self::pick($reqs[0]['messages'][0], $x['first_message']));
        }
        if (isset($x['unsubscribe_sent'])) {
            $sent = array_values(array_filter($reqs[0]['messages'], fn ($m) => ($m['op'] ?? null) === 'unsubscribe'));
            $this->assertEquals($x['unsubscribe_sent'], self::pick($sent[0] ?? null, $x['unsubscribe_sent']));
            $this->assertSame([], array_values(array_diff(array_keys($sent[0]), ['op', 'channel', 'symbols', 'interval', 'id'])), 'the markets and the interval, nothing else');
        }
        if (isset($x['resubscribe'])) {
            $this->assertEquals($x['resubscribe'], self::pick(end($reqs)['messages'][0], $x['resubscribe']));
        }
    }

    public static function locals(): array
    {
        $out = [];
        foreach (self::suite()['local'] as $l) {
            $out[$l['name']] = [$l];
        }
        return $out;
    }

    #[DataProvider('locals')]
    public function testLocal(array $l): void
    {
        match ($l['check'] ?? 'client') {
            'client' => $this->clientRefused($l),
            'redaction' => $this->redaction(),
            'webhook_vectors' => $this->webhookVectors(),
            'keyless' => $this->keyless($l),
            default => $this->fail("a local check this runner does not play: {$l['check']}"),
        };
    }

    /** A client the configuration refuses, before any request. */
    private function clientRefused(array $l): void
    {
        try {
            new Client(apiKey: $l['client']['api_key'], environment: $l['client']['environment'], baseUrl: 'http://127.0.0.1:9');
            $this->fail('no error');
        } catch (PhoenixPerpsException $e) {
            $this->assertSame($l['expect']['error_code'], $e->errorCode);
        }
    }

    /** A client without a key is made; a public call sends no Authorization, the others answer missing_api_key. */
    private function keyless(array $l): void
    {
        $env = getenv('PHOENIXPERPS_API_KEY');
        putenv('PHOENIXPERPS_API_KEY');
        try {
            $c = new Client(apiKey: $l['client']['api_key'], environment: $l['client']['environment'], baseUrl: self::$url);
        } finally {
            if ($env !== false) {
                putenv("PHOENIXPERPS_API_KEY={$env}");
            }
        }
        self::post(['exchanges' => [
            ['respond' => ['status' => 200, 'body' => ['data' => ['status' => 'ok']]]],
            ['respond' => ['status' => 401, 'body' => ['error' => ['type' => 'authentication', 'code' => 'missing_api_key', 'message' => 'x', 'request_id' => 'req_1', 'retryable' => false]]]],
        ]]);
        $c->platform->status();
        try {
            $c->me->get();
            $this->fail('no error');
        } catch (AuthenticationException $e) {
            $this->assertSame('missing_api_key', $e->errorCode);
        }
        $got = self::requests();
        $this->assertSame([false, false], array_map(fn ($r) => isset($r['headers']['authorization']), $got));
        $this->assertSame($l['client']['environment'], $c->environment);
        $this->assertStringContainsString('apiKey=none', (string) $c);
    }

    private function redaction(): void
    {
        $key = self::suite()['key'];
        $lines = [];
        $c = new Client(apiKey: $key, baseUrl: 'http://127.0.0.1:9', timeout: 2, maxRetries: 0, logger: function (string $level, string $message) use (&$lines) {
            $lines[] = $message;
        });
        $c->log('info', "a failure's text quoting {$key}"); // a stream's reconnect line carries one
        foreach ([(string) $c, print_r($c, true), var_export($c->__debugInfo(), true), ...$lines] as $text) {
            $this->assertStringNotContainsString($key, $text);
        }
        ob_start();
        var_dump($c);
        $this->assertStringNotContainsString($key, (string) ob_get_clean());
        $this->assertSame('ppk_test_****' . substr($key, -4), $c->maskedKey());
        try {
            $c->me->get();
            $this->fail('no error');
        } catch (PhoenixPerpsException $e) {
            $this->assertStringNotContainsString($key, (string) $e);
        }
        try {
            serialize($c);
            $this->fail('serialized');
        } catch (\LogicException) {
        }
    }

    private function webhookVectors(): void
    {
        $v = self::suite()['webhook_vectors'];
        $this->assertPlays(array_keys($v), ['secret', 'other', 'timestamp', 'body', 'signature', 'now_ms', 'rotation', 'want', 'extra'], 'webhook vector keys');
        $at = fn (string $sig, ?string $ts = null) => ['X-Phoenix-Timestamp' => $ts ?? $v['timestamp'], 'X-Phoenix-Signature' => $sig];
        $now = $v['now_ms'] / 1000;
        $e = Webhooks::verify($v['body'], $at($v['signature']), $v['secret'], 300, $now);
        $this->assertSame($v['want'], self::pick($e->toArray(), $v['want']));
        $this->assertSame('evt_01JAB7Q8XK4M2N5P7R9S3T5V7W', Webhooks::verify($v['body'], $at($v['rotation']), [$v['secret']], 300, $now)->id);
        foreach ([[$v['other'], $now, $v['body'], 'bad_signature'], [$v['secret'], $now + 1000, $v['body'], 'stale'], [$v['secret'], $now, $v['body'] . ' ', 'bad_signature']] as [$secret, $when, $body, $code]) {
            try {
                Webhooks::verify($body, $at($v['signature']), $secret, 300, $when);
                $this->fail("no {$code}");
            } catch (WebhookVerificationException $e) {
                $this->assertSame($code, $e->errorCode);
            }
        }
        foreach ($v['extra'] as $x) {
            $this->assertPlays(array_keys($x), ['name', 'signature', 'body', 'timestamp', 'want', 'error'], 'extra vector keys');
            try {
                $p = Webhooks::verify($x['body'], $at($x['signature'], $x['timestamp']), $v['secret'], 300, $now);
                $this->assertArrayHasKey('want', $x, "{$x['name']}: verified");
                $this->assertSame($x['want'], self::pick($p->toArray(), $x['want']), $x['name']);
            } catch (WebhookVerificationException $e) {
                $this->assertSame($x['error'] ?? null, $e->errorCode, $x['name']);
                $this->assertInstanceOf(PhoenixPerpsException::class, $e, 'a family of the SDK\'s own error');
            }
        }
    }

    public function testMarketRulesVectors(): void
    {
        $rv = self::suite()['rules_vectors'];
        $r = new MarketRules(Market::fromArray($rv['market']));
        foreach ($rv['cases'] as $c) {
            $this->assertPlays(array_keys($c), ['fn', 'args', 'mode', 'want', 'error'], 'rules case keys');
            $a = $c['args'];
            try {
                $got = match ($c['fn']) {
                    'round_price' => $r->roundPrice($a[0], $c['mode'] ?? MarketRules::NEAREST),
                    'round_size' => $r->roundSize($a[0]),
                    'size_for_notional' => $r->sizeForNotional($a[0], $a[1]),
                    'size_for_risk' => $r->sizeForRisk($a[0], $a[1]),
                    'worst_price' => $r->worstPrice($a[0], $a[1], $a[2] ?? null),
                };
            } catch (ValidationException $e) { // a ValidationError of the SDK's own
                $got = ['error' => true];
            }
            $this->assertSame(empty($c['error']) ? $c['want'] : ['error' => true], $got, json_encode($c));
        }
        $this->assertSame('0.000000001', \PhoenixPerps\Decimal::of(0.000000001));
        $this->assertSame('62000.1', \PhoenixPerps\Decimal::of(62000.1));
    }

    public function testOpenEnumAndUnknownField(): void
    {
        self::post(['exchanges' => [['respond' => ['status' => 200, 'body' => ['data' => ['id' => 'ord_1', 'status' => 'a_status_from_the_future', 'a_new_field' => 1]]]]]]);
        $c = new Client(apiKey: self::suite()['key'], baseUrl: self::$url);
        $o = $c->orders->get('SANDBOX-00042-001', 'ord_1');
        $this->assertSame('a_status_from_the_future', $o->status);
        $this->assertSame(1, $o->extra['a_new_field']);
        $this->assertSame(1, $o->toArray()['a_new_field'], 'kept when written again');
    }

    public function testNotModifiedIsNoError(): void
    {
        self::post(['exchanges' => [['respond' => ['status' => 304]], ['respond' => ['status' => 304]]]]);
        $c = self::client();
        $status = null;
        $c->markets->get('BTC-USD', new RequestOptions(headers: ['If-None-Match' => 'W/"abc"'], onResponse: function ($raw) use (&$status) {
            $status = $raw->status;
        }));
        $this->assertSame(304, $status);
        $this->assertSame(304, $c->request('GET', '/v1/markets/BTC-USD', options: new RequestOptions(headers: ['If-None-Match' => 'W/"abc"']))->status);
        $this->assertSame('W/"abc"', self::requests()[0]['headers']['if-none-match']);
    }

    public function testBaseUrlAsTheDocsWriteIt(): void
    {
        self::post(['exchanges' => [['respond' => ['status' => 200, 'body' => ['data' => []]]]]]);
        $c = self::client(baseUrl: self::$url . '/v1/');
        $c->platform->status();
        $this->assertSame('/v1/status', self::requests()[0]['path']);
    }

    public function testEnvironmentVariableComesAfterTheKey(): void
    {
        putenv('PHOENIXPERPS_ENVIRONMENT=LIVE');
        try {
            $this->assertSame('sandbox', (new Client(apiKey: self::suite()['key'], baseUrl: self::$url))->environment);
            $this->assertSame('live', (new Client(apiKey: 'a_key_without_a_prefix', baseUrl: self::$url))->environment, 'in any case');
            $this->assertSame('sandbox', (new Client(apiKey: self::suite()['key'], environment: 'Sandbox', baseUrl: self::$url))->environment);
        } finally {
            putenv('PHOENIXPERPS_ENVIRONMENT');
        }
    }

    public function testErrorsCarryRetryableAndDocUrl(): void
    {
        $envelope = fn (string $code, bool $retryable) => ['error' => ['type' => 'conflict', 'code' => $code, 'message' => 'm', 'request_id' => 'req_1', 'retryable' => $retryable, 'doc_url' => "https://docs.phoenixperpsfunding.com/reference/errors#{$code}"]];
        self::post(['exchanges' => [['respond' => ['status' => 409, 'body' => $envelope('account_not_ready', true)]], ['respond' => ['status' => 409, 'body' => $envelope('client_order_id_conflict', false)]], ['respond' => ['status' => 502, 'body' => '<html>bad gateway</html>']]]]);
        $c = new Client(apiKey: self::suite()['key'], baseUrl: self::$url, maxRetries: 0);
        $got = [];
        for ($i = 0; $i < 3; $i++) {
            try {
                $c->me->get();
            } catch (PhoenixPerpsException $e) {
                $got[] = [$e->errorCode, $e->retryable, $e->docUrl, $e->headers['content-type'] ?? null];
            }
        }
        $this->assertSame([
            ['account_not_ready', true, 'https://docs.phoenixperpsfunding.com/reference/errors#account_not_ready', 'application/json'],
            ['client_order_id_conflict', false, 'https://docs.phoenixperpsfunding.com/reference/errors#client_order_id_conflict', 'application/json'],
            ['http_502', true, null, 'text/html'],
        ], $got);
    }

    public function testRawRequestKeepsAKnownRoutesRules(): void
    {
        $busy = ['respond' => ['status' => 503, 'headers' => ['retry-after' => '0'], 'body' => ['error' => ['code' => 'engine_unavailable', 'retryable' => true]]]];
        $ok = ['respond' => ['status' => 200, 'body' => ['data' => ['id' => 'ord_1']]]];
        self::post(['exchanges' => [$busy, $ok, $busy, $ok]]);
        $c = self::client();
        $this->assertSame(200, $c->request('DELETE', '/v1/accounts/SANDBOX-00042-001/orders/ord_1')->status, 'a retry-safe route: tried again');
        $this->assertSame(200, $c->request('POST', '/v1/accounts/SANDBOX-00042-001/orders', null, ['market' => 'BTC-USD'])->status, 'a route with an Idempotency-Key: tried again with it');
        $got = self::requests();
        $this->assertSame(['DELETE', 'DELETE', 'POST', 'POST'], array_column($got, 'method'));
        $this->assertArrayNotHasKey('idempotency-key', $got[0]['headers']);
        $this->assertSameKey(array_slice($got, 2));
    }

    public function testAnEmptyPathParameterFailsBeforeAnyRequest(): void
    {
        self::post(['exchanges' => []]);
        try {
            self::client()->orders->cancel('SANDBOX-00042-001', '');
            $this->fail('no error');
        } catch (ValidationException $e) {
            $this->assertSame('invalid_request', $e->errorCode);
        }
        $this->assertSame([], self::requests());
    }

    public function testAChangeWithAnIdempotencyKeyIsTriedAgain(): void
    {
        self::post(['exchanges' => [['respond' => ['status' => 503, 'headers' => ['retry-after' => '0'], 'body' => ['error' => ['code' => 'engine_unavailable', 'retryable' => true]]]], ['respond' => ['status' => 200, 'body' => ['data' => ['id' => 'ord_1', 'limit_price' => '61900']]]]]]);
        $o = self::client()->orders->modify('SANDBOX-00042-001', 'ord_1', ['limit_price' => '61900'], new RequestOptions(idempotencyKey: 'change-0001'));
        $this->assertSame('61900', $o->limitPrice);
        $this->assertSame(['change-0001', 'change-0001'], array_map(fn ($r) => $r['headers']['idempotency-key'], self::requests()));
    }

    public function testEachBudgetWaitsForItsOwnReset(): void
    {
        $op = fn (string $scope) => ['id' => "a {$scope} call", 'method' => 'GET', 'path' => "/v1/{$scope}", 'idempotent' => false, 'retrySafe' => true, 'cursor' => null, 'scope' => $scope];
        $spent = ['respond' => ['status' => 200, 'headers' => ['x-ratelimit-limit' => '10', 'x-ratelimit-remaining' => '0', 'x-ratelimit-reset' => '1'], 'body' => ['data' => []]]];
        $ok = ['respond' => ['status' => 200, 'body' => ['data' => []]]];
        self::post(['exchanges' => [$spent, $ok, $ok]]);
        $c = self::client();
        $c->send($op('trade'), [], null, null, null); // the trade budget used up for a second
        $c->send($op('read'), [], null, null, null); // another budget: at once
        $c->send($op('trade'), [], null, null, null); // the same: after its reset
        $got = self::requests();
        $this->assertLessThan(500, $got[1]['at'] - $got[0]['at']);
        $this->assertGreaterThanOrEqual(800, $got[2]['at'] - $got[0]['at']);
    }

    public function testRawRequest(): void
    {
        self::post(['exchanges' => [['respond' => ['status' => 503, 'headers' => ['retry-after' => '0'], 'body' => ['error' => ['code' => 'not_ready']]]], ['respond' => ['status' => 200, 'body' => ['data' => ['api' => 'ok']]]], ['respond' => ['status' => 503, 'body' => ['error' => ['code' => 'not_ready']]]]]]);
        $c = self::client();
        $raw = $c->request('get', '/v1/a-route-newer-than-this-sdk', ['n' => 1]);
        $this->assertSame([200, ['data' => ['api' => 'ok']]], [$raw->status, json_decode($raw->body, true)]);
        try {
            $c->request('POST', '/v1/another', null, ['a' => 1]); // no key: never sent twice
            $this->fail('no error');
        } catch (ServerException $e) {
            $this->assertSame('not_ready', $e->errorCode);
        }
        $got = self::requests();
        $this->assertSame([['GET', '/v1/a-route-newer-than-this-sdk', ['n' => '1']], ['GET', '/v1/a-route-newer-than-this-sdk', ['n' => '1']], ['POST', '/v1/another', []]], array_map(fn ($r) => [$r['method'], $r['path'], $r['query']], $got));
        $this->assertSame(['a' => 1], $got[2]['body']);
        $this->assertSame('Bearer ' . self::suite()['key'], $got[2]['headers']['authorization']);
    }

    public function testPageKeepsTheListsOtherFields(): void
    {
        self::post(['exchanges' => [['respond' => ['status' => 200, 'body' => ['data' => [], 'has_more' => false, 'as_of' => 1791460800000, 'history_complete' => false]]]]]);
        $this->assertSame(['history_complete' => false], self::client()->orders->list('SANDBOX-00042-001', ['status' => 'all'])->extra);
    }

    public function testPageDataIsAPlainArray(): void
    {
        self::post(['exchanges' => [['respond' => ['status' => 200, 'body' => ['data' => [['id' => 'ord_1'], ['id' => 'ord_2']], 'has_more' => false]]]]]);
        $page = self::client()->orders->list('SANDBOX-00042-001');
        $this->assertSame('ord_2', end($page->data)->id);
    }

    public function testDeprecationIsLoggedOnAnyAnswer(): void
    {
        self::post(['exchanges' => [['respond' => ['status' => 200, 'headers' => ['deprecation' => '@1798761600'], 'body' => ['data' => []]]]]]);
        $lines = [];
        self::client(function (string $level, string $message) use (&$lines) {
            $lines[] = "{$level} {$message}";
        })->platform->status();
        $this->assertSame(['warn phoenixperps: this API version is deprecated (Deprecation: @1798761600)'], $lines);
    }

    public function testFractionalRetryAfterIsWaited(): void
    {
        self::post(['exchanges' => [['respond' => ['status' => 503, 'headers' => ['retry-after' => '1.5'], 'body' => ['error' => ['code' => 'not_ready']]]], ['respond' => ['status' => 200, 'body' => ['data' => []]]]]]);
        self::client()->platform->status();
        $got = self::requests();
        $this->assertGreaterThanOrEqual(1450, $got[1]['at'] - $got[0]['at']);
    }

    public function testDecimalOfLargeFloats(): void
    {
        $this->assertSame('150000000000000000000', Decimal::of(1.5e20));
        $this->assertSame('100000000000000000000', Decimal::of(1.0E+20));
        $this->assertSame('123456789012345680', Decimal::of(123456789012345678.0));
        $this->assertSame('-0.000000025', Decimal::of(-2.5e-8));
    }

    public function testWorstPriceSideInAnyCase(): void
    {
        $r = new MarketRules(Market::fromArray(self::suite()['rules_vectors']['market']));
        $this->assertSame(['62620.0', '61380.0'], [$r->worstPrice('BUY', '62000'), $r->worstPrice('Sell', '62000')]);
        $this->expectException(ValidationException::class);
        $r->worstPrice('long', '62000');
    }

    public function testWorstPriceKeepsTheMarketsSlippageBounds(): void
    {
        $r = new MarketRules(Market::fromArray(self::suite()['rules_vectors']['market'])); // 0.05 to 5 %
        foreach (['50', '0.01'] as $pct) {
            try {
                $r->worstPrice('buy', '62000', $pct);
                $this->fail("{$pct} % taken");
            } catch (ValidationException $e) {
                $this->assertSame('max_slippage_pct', $e->details[0]['field']);
            }
        }
        $this->assertSame('65100.0', $r->worstPrice('buy', '62000', '5'));
    }

    public function testMarketRulesRefuseWhatIsNoDecimal(): void
    {
        $r = new MarketRules(Market::fromArray(self::suite()['rules_vectors']['market']));
        $calls = [
            fn () => $r->roundSize('1e-3'), fn () => $r->roundPrice(' 62000.05'), fn () => $r->sizeForNotional('', '62000'),
            fn () => $r->sizeForNotional('100', '0'), fn () => $r->sizeForRisk('100', '-0.00'),
        ];
        foreach ($calls as $call) {
            try {
                $call();
                $this->fail('taken');
            } catch (ValidationException $e) {
                $this->assertSame('invalid_request', $e->errorCode);
            }
        }
    }

    public function testWebhookWithoutASecret(): void
    {
        $v = self::suite()['webhook_vectors'];
        foreach ([false, '', null, ['']] as $secret) {
            try {
                Webhooks::verify($v['body'], ['X-Phoenix-Timestamp' => $v['timestamp'], 'X-Phoenix-Signature' => $v['signature']], $secret, 300, $v['now_ms'] / 1000);
                $this->fail('verified without a secret');
            } catch (\InvalidArgumentException $e) {
                $this->assertStringContainsString('secret', $e->getMessage());
            }
        }
    }

    public function testWebhookPayload(): void
    {
        $v = self::suite()['webhook_vectors'];
        $p = Webhooks::verifyPayload($v['body'], ['X-Phoenix-Timestamp' => $v['timestamp'], 'X-Phoenix-Signature' => $v['signature']], $v['secret'], 300, $v['now_ms'] / 1000);
        $this->assertSame(['order.filled', 'v1', 'sandbox', []], [$p->type, $p->apiVersion, $p->environment, $p->extra]);
    }

    /** @return array<string, array{array<string, mixed>, array<string, mixed>, list<array<string, mixed>>}> the error frame, its details, what follows it */
    public static function refusedSubscribes(): array
    {
        return [
            'too_many_accounts (no snapshot_end)' => [['code' => 'too_many_accounts', 'details' => ['max' => 50]], ['max' => 50], []],
            'account_not_found (then snapshot_end)' => [['code' => 'account_not_found', 'account_id' => 'SANDBOX-00015-001'], ['account_id' => 'SANDBOX-00015-001'], [['send' => ['type' => 'snapshot_end', 'accounts' => []]]]],
        ];
    }

    #[DataProvider('refusedSubscribes')]
    public function testAccountStreamEndsOnARefusedSubscribe(array $error, array $details, array $then): void
    {
        self::post(['connections' => [
            [self::hello(), ['expect' => 'subscribe'], ['send' => ['type' => 'error'] + $error], ...$then, ['wait_ms' => 1000], ['close' => 1000]],
            [self::hello(), ['expect' => 'subscribe'], self::event('evt_1'), ['wait_ms' => 1000]], // what a wrong reconnect would get
        ]]);
        $s = self::client()->accountStream(accounts: ['SANDBOX-00015-001']);
        $ids = [];
        $err = null;
        try {
            foreach ($s as $e) {
                $ids[] = $e->id;
                break;
            }
        } catch (ValidationException $err) {
        }
        $s->close();
        $this->assertSame([], $ids);
        $this->assertSame([$error['code'], $details], [$err?->errorCode, $err?->details]);
        $this->assertCount(1, self::requests(), 'connections');
    }

    /** A link phrity found closed fails here (the stream then resumes), never phrity's own silent reconnect. */
    public function testAClosedLinkIsAFailureNeverASilentReconnect(): void
    {
        self::post(['connections' => [[self::hello(), ['wait_ms' => 3000]], [self::hello(), ['wait_ms' => 3000]]]]);
        $ws = 'ws' . substr(self::$url, 4);
        $socket = new \PhoenixPerps\StreamSocket(self::client(), "{$ws}/v1/stream");
        $this->assertSame('hello', $socket->next(2.0)['frame']['type'] ?? null);
        // closed under the SDK, as a peer or the network would
        $inner = (new \ReflectionProperty($socket, 'ws'))->getValue($socket);
        $inner->disconnect();
        $this->assertSame(['failed' => true], $socket->next(1.0));
        $this->assertFalse($socket->send(['op' => 'ping']));
        $this->assertCount(1, self::requests(), 'no connection opened behind the stream');
    }

    public function testAccountStreamPoll(): void
    {
        self::post(['connections' => [[
            self::hello(),
            ['expect' => 'subscribe'],
            ['send' => ['type' => 'snapshot', 'account_id' => 'SANDBOX-00042-001', 'orders' => [], 'positions' => [], 'risk' => null, 'error' => 'state_unavailable']],
            ['send' => ['type' => 'snapshot_end', 'accounts' => ['SANDBOX-00042-001']]],
            ['wait_ms' => 1200],
            self::event('evt_1'),
            ['wait_ms' => 2000],
        ]]]);
        $s = self::client()->accountStream();
        $t = microtime(true);
        $this->assertNull($s->poll(0.5), 'a quiet stream gives control back');
        $this->assertLessThan(1.0, microtime(true) - $t);
        $this->assertTrue($s->isReady());
        $this->assertSame('state_unavailable', $s->snapshot('SANDBOX-00042-001')['error']);
        $this->assertSame('evt_1', $s->poll(3.0)?->id);
        $s->close();
        $this->assertNull($s->poll(0.1));
    }

    public function testAccountStreamPingAndUnsubscribe(): void
    {
        $hello = self::hello(15000, ['accounts' => ['SANDBOX-00042-001', 'SANDBOX-00042-002']]);
        self::post(['connections' => [
            [$hello, ['expect' => 'subscribe'], ['send' => ['type' => 'snapshot_end']], ['expect' => 'ping'], self::event('evt_1'), ['reply' => ['type' => 'pong', 'time' => 1]], ['expect' => 'unsubscribe'], ['reply' => ['type' => 'unsubscribed', 'accounts' => ['SANDBOX-00042-002']]], ['close' => 1012]],
            [$hello, ['expect' => 'subscribe'], ['send' => ['type' => 'snapshot_end']], ['wait_ms' => 3000]],
        ]]);
        $s = self::client()->accountStream();
        $this->assertSame('pong', $s->ping(3.0)['type']);
        $this->assertSame('evt_1', $s->poll(0.1)?->id, 'an event read while ping() waited is kept');
        $this->assertSame(['SANDBOX-00042-002'], $s->unsubscribe(['SANDBOX-00042-002'], 3.0)['accounts']);
        for ($t = microtime(true); !(self::requests()[1]['messages'] ?? null) && microtime(true) - $t < 6;) {
            $s->poll(0.2); // 1012: the next connection, 0 to 3 s later
        }
        $s->close();
        $got = self::requests();
        $this->assertCount(2, $got, 'connections');
        $this->assertContains(['op' => 'ping'], $got[0]['messages']);
        $this->assertSame(['op' => 'unsubscribe', 'accounts' => ['SANDBOX-00042-002']], end($got[0]['messages']));
        $this->assertSame([['op' => 'subscribe', 'accounts' => ['SANDBOX-00042-001']]], $got[1]['messages'], 'the account unsubscribed stays out');
    }

    public function testAccountStreamTellsRiskFramesAndKeepsSeq(): void
    {
        $risk = ['type' => 'risk', 'account_id' => 'SANDBOX-00042-001', 'risk' => ['daily_loss' => ['used_pct' => '41.2']]];
        self::post(['connections' => [[
            self::hello(),
            ['expect' => 'subscribe'],
            ['send' => ['type' => 'snapshot_end', 'accounts' => []]],
            ['send' => $risk],
            ['send' => ['type' => 'event', 'seq' => 7, 'replayed' => true] + self::event('evt_1')['send']],
            ['wait_ms' => 2000],
        ]]]);
        $told = [];
        $s = self::client()->accountStream(risk: true, onRisk: function (array $f) use (&$told) {
            $told[] = $f;
        });
        $e = $s->poll(3.0);
        $s->close();
        $this->assertSame([$risk], $told, 'told as it came, before the event after it');
        $this->assertSame(['evt_1', 7, true], [$e?->id, $e?->extra['seq'] ?? null, $e?->extra['replayed'] ?? null]);
        $this->assertSame(['op' => 'subscribe', 'risk' => true], self::requests()[0]['messages'][0]);
    }

    public function testAccountStream4008IsAGap(): void
    {
        self::post(['connections' => [
            [self::hello(), ['expect' => 'subscribe'], self::event('evt_1'), ['close' => 4008]],
            [self::hello(), ['expect' => 'subscribe'], ['send' => ['type' => 'snapshot_end', 'accounts' => []]], self::event('evt_2'), ['wait_ms' => 2000]],
        ]]);
        [$gaps, $lines] = [[], []];
        $s = self::client(function (string $level, string $message) use (&$lines) {
            $lines[] = "{$level} {$message}";
        })->accountStream(onGap: function (?string $after) use (&$gaps) {
            $gaps[] = $after;
        });
        $ids = [];
        foreach ($s as $e) {
            $ids[] = $e->id;
            if (count($ids) === 2) {
                break;
            }
        }
        $s->close();
        $this->assertSame([['evt_1', 'evt_2'], ['evt_1']], [$ids, $gaps]);
        $this->assertSame([null, null], array_map(fn ($r) => $r['query']['resume'] ?? null, self::requests()));
        $this->assertSame('warn phoenixperps: account stream gap after evt_1: read GET /v1/events?after= it', $lines[0]);
        $this->assertMatchesRegularExpression('/^info phoenixperps: account stream reconnecting in \d+ ms \(close 4008\)$/', $lines[1]);
    }

    public function testRefusedUpgradeWaitsItsRetryAfterOrEndsTheStream(): void
    {
        $envelope = fn (string $code) => ['error' => ['type' => 'x', 'code' => $code, 'message' => 'x', 'request_id' => 'req_1', 'retryable' => false]];
        self::post(['connections' => [
            [['refuse' => 503, 'retry_after' => 1, 'body' => $envelope('engine_unavailable')]],
            [self::hello(), ['expect' => 'subscribe'], self::event('evt_1'), ['wait_ms' => 2000]],
        ]]);
        $lines = [];
        $s = self::client(function (string $level, string $message) use (&$lines) {
            $lines[] = $message;
        })->accountStream();
        $this->assertSame('evt_1', $s->poll(5.0)?->id);
        $s->close();
        $got = self::requests();
        $this->assertGreaterThanOrEqual(950, $got[1]['at'] - $got[0]['at'], 'its Retry-After');
        $this->assertSame(['phoenixperps: account stream reconnecting in 1000 ms (upgrade refused: 503 engine_unavailable)'], $lines);

        // any other refusal is the API's error, as a REST call would answer it: never a reconnect
        self::post(['connections' => [[['refuse' => 404, 'body' => $envelope('not_found')]], [self::hello(), ['wait_ms' => 1000]]]]);
        try {
            self::client()->marketStream()->subscribe('ticker', ['BTC-USD'], [], 3.0);
            $this->fail('no error');
        } catch (NotFoundException $e) {
            $this->assertSame('not_found', $e->errorCode);
        }
        $this->assertCount(1, self::requests(), 'connections');
    }

    public function testMarketSubscriptionIsAChannelAMarketAndAnInterval(): void
    {
        $subscribed = ['reply' => ['type' => 'subscribed', 'channel' => 'x', 'symbols' => [], 'rejected' => []]];
        self::post(['connections' => [
            [self::hello(), ...array_merge(...array_fill(0, 4, [['expect' => 'subscribe'], $subscribed])), ['expect' => 'unsubscribe'], ['reply' => ['type' => 'unsubscribed', 'channel' => 'candles', 'symbols' => ['BTC-USD'], 'rejected' => []]], ['close' => 1012]],
            [self::hello(), ['expect' => 'subscribe'], $subscribed, ['wait_ms' => 1500]],
        ]]);
        $m = self::client()->marketStream();
        $m->subscribe('candles', ['BTC-USD'], ['interval' => '1m', 'history' => 10]);
        $m->subscribe('candles', ['BTC-USD'], ['interval' => '5m']);
        $m->subscribe('ticker', ['ETH-USD'], ['interval_ms' => 250]);
        $m->subscribe('ticker', ['ETH-USD'], ['interval_ms' => 1000]); // a setting: the last subscribe sets it
        $m->unsubscribe('candles', ['BTC-USD'], ['history' => 10]); // no interval: every interval of the market
        for ($t = microtime(true); count(self::requests()[1]['messages'] ?? []) < 1 && microtime(true) - $t < 6;) {
            $m->poll(0.2); // 1012: the next connection, 0 to 3 s later
        }
        $m->poll(0.5);
        $m->close();
        $got = self::requests();
        $this->assertSame(['op' => 'unsubscribe', 'channel' => 'candles', 'symbols' => ['BTC-USD']], array_diff_key(end($got[0]['messages']), ['id' => 0]), 'the markets, nothing else');
        $this->assertSame([['interval_ms' => 1000, 'op' => 'subscribe', 'channel' => 'ticker', 'symbols' => ['ETH-USD']]], array_map(fn ($x) => array_diff_key($x, ['id' => 0]), $got[1]['messages']));
    }

    public function testMarketSubscribeFailsOnlyWhenTheStreamEnds(): void
    {
        $subscribed = ['reply' => ['type' => 'subscribed', 'channel' => 'ticker', 'symbols' => ['BTC-USD'], 'rejected' => []]];
        self::post(['connections' => [
            [self::hello(), ['expect' => 'subscribe'], ['wait_ms' => 10500], ['close' => 1012]], // no answer for longer than any default
            [self::hello(), ['expect' => 'subscribe'], $subscribed, ['wait_ms' => 1000]],
        ]]);
        $m = self::client()->marketStream();
        $reply = $m->subscribe('ticker', ['BTC-USD']); // sent again on the next connection, answered there
        $m->close();
        $this->assertSame('subscribed', $reply['type']);
        $this->assertSame([[['op' => 'subscribe', 'channel' => 'ticker', 'symbols' => ['BTC-USD'], 'id' => 1]], [['op' => 'subscribe', 'channel' => 'ticker', 'symbols' => ['BTC-USD'], 'id' => 1]]], array_column(self::requests(), 'messages'));
        try {
            $m->subscribe('ticker', ['ETH-USD']);
            $this->fail('no error');
        } catch (StreamClosedException) {
        }
    }

    public function testMarketUnsubscribeNamesTheInterval(): void
    {
        self::post(['connections' => [[self::hello(), ['expect' => 'subscribe'], ['reply' => ['type' => 'subscribed', 'channel' => 'candles', 'symbols' => ['BTC-USD'], 'rejected' => []]], ['expect' => 'unsubscribe'], ['reply' => ['type' => 'unsubscribed', 'channel' => 'candles', 'symbols' => ['BTC-USD'], 'rejected' => []]], ['wait_ms' => 1000]]]]);
        $m = self::client()->marketStream();
        $m->subscribe('candles', ['BTC-USD'], ['interval' => '1m', 'history' => 10]);
        $m->unsubscribe('candles', ['BTC-USD'], ['interval' => '1m', 'history' => 10]);
        $m->close();
        $this->assertSame(['interval' => '1m', 'op' => 'unsubscribe', 'channel' => 'candles', 'symbols' => ['BTC-USD']], array_diff_key(self::requests()[0]['messages'][1], ['id' => 0]));
    }

    public function testKeylessStreamSendsNoAuthorization(): void
    {
        $env = getenv('PHOENIXPERPS_API_KEY');
        putenv('PHOENIXPERPS_API_KEY');
        $ws = 'ws' . substr(self::$url, 4);
        try {
            $c = new Client(environment: 'sandbox', baseUrl: self::$url, marketStreamUrl: "{$ws}/v1/market-data");
        } finally {
            if ($env !== false) {
                putenv("PHOENIXPERPS_API_KEY={$env}");
            }
        }
        self::post(['connections' => [[self::hello(), ['expect' => 'subscribe'], ['reply' => ['type' => 'subscribed', 'channel' => 'ticker', 'symbols' => ['BTC-USD'], 'rejected' => []]], ['wait_ms' => 1000]]]]);
        $m = $c->marketStream();
        $m->subscribe('ticker', ['BTC-USD']);
        $m->close();
        $this->assertArrayNotHasKey('authorization', self::requests()[0]['headers']);
    }

    public function testMarketStreamReconnectsAfterThreeSilentHeartbeats(): void
    {
        $ticker = fn (string $last) => ['send' => ['type' => 'ticker', 'symbol' => 'BTC-USD', 'data' => ['last' => $last]]];
        $subscribed = ['reply' => ['type' => 'subscribed', 'channel' => 'ticker', 'symbols' => ['BTC-USD'], 'rejected' => []]];
        self::post(['connections' => [
            [self::hello(100), ['expect' => 'subscribe'], $subscribed, $ticker('62000.1'), ['wait_ms' => 6000], ['close' => 1000]], // up, but silent
            [self::hello(100), ['expect' => 'subscribe'], $subscribed, $ticker('62000.2'), ['wait_ms' => 1000]],
        ]]);
        $m = self::client()->marketStream();
        $t = microtime(true);
        $m->subscribe('ticker', ['BTC-USD']);
        $frames = [];
        foreach ($m as $f) {
            $frames[] = $f['data']['last'];
            if (count($frames) === 2) {
                break;
            }
        }
        $m->close();
        $this->assertSame(['62000.1', '62000.2'], $frames);
        $this->assertLessThan(3.0, microtime(true) - $t, 'reconnected after 3 x 100 ms of silence, not at the close');
        $this->assertCount(2, self::requests(), 'connections');
    }

    public function testMarketStreamPoll(): void
    {
        self::post(['connections' => [[self::hello(), ['expect' => 'subscribe'], ['reply' => ['type' => 'subscribed', 'channel' => 'status', 'symbols' => [], 'rejected' => []]], ['wait_ms' => 800], ['send' => ['type' => 'status', 'data' => ['feed' => ['state' => 'live']]]], ['wait_ms' => 2000]]]]);
        $m = self::client()->marketStream();
        $m->subscribe('status');
        $t = microtime(true);
        $this->assertNull($m->poll(0.3), 'a quiet stream gives control back');
        $this->assertLessThan(0.8, microtime(true) - $t);
        $this->assertSame('status', $m->poll(3.0)['type'] ?? null);
        $m->close();
        $this->assertNull($m->poll(0.1));
    }

    public function testMarketStreamListAndPing(): void
    {
        self::post(['connections' => [[self::hello(), ['expect' => 'list'], ['reply' => ['type' => 'list', 'subscriptions' => [['channel' => 'ticker', 'symbol' => 'BTC-USD']]]], ['expect' => 'ping'], ['reply' => ['type' => 'pong', 'time' => 1]], ['wait_ms' => 1000]]]]);
        $m = self::client()->marketStream();
        $this->assertSame([['channel' => 'ticker', 'symbol' => 'BTC-USD']], $m->list(3.0)['subscriptions']);
        $this->assertSame('pong', $m->ping(3.0)['type']);
        $m->close();
    }

    public function testMarketStreamRefusalKeepsTheServersDetails(): void
    {
        self::post(['connections' => [[self::hello(), ['expect' => 'subscribe'], ['reply' => ['type' => 'error', 'code' => 'invalid_parameter', 'details' => ['field' => 'interval', 'issue' => '1m 3m 5m']]], ['wait_ms' => 1000]]]]);
        $m = self::client()->marketStream();
        try {
            $m->subscribe('candles', ['BTC-USD'], ['interval' => '2m'], 3.0);
            $this->fail('no error');
        } catch (ValidationException $e) {
            $this->assertSame(['invalid_parameter', ['field' => 'interval', 'issue' => '1m 3m 5m']], [$e->errorCode, $e->details]);
        } finally {
            $m->close();
        }
    }

    public function testRefusedStreamUpgradeCarriesTheApisError(): void
    {
        $null = PHP_OS_FAMILY === 'Windows' ? 'NUL' : '/dev/null';
        $body = json_encode(['error' => ['type' => 'authentication', 'code' => 'wrong_environment', 'message' => 'This key is a sandbox key, not a live one.', 'request_id' => 'req_refused', 'retryable' => false, 'details' => ['expected_host' => 'sandbox-api.phoenixperpsfunding.com']]]);
        // a server refusing every upgrade as the API does: the status, then the REST error envelope
        $js = 'const b=process.argv[1];require("net").createServer((s)=>s.once("data",()=>s.end(`HTTP/1.1 401 Unauthorized\r\ncontent-type: application/json\r\ncontent-length: ${Buffer.byteLength(b)}\r\nconnection: close\r\n\r\n${b}`))).listen(0,"127.0.0.1",function(){console.log(this.address().port)})';
        $server = proc_open(['node', '-e', $js, $body], [0 => ['file', $null, 'r'], 1 => ['pipe', 'w'], 2 => ['file', $null, 'w']], $pipes);
        $ws = 'ws://127.0.0.1:' . trim((string) fgets($pipes[1]));
        $c = new Client(apiKey: self::suite()['key'], baseUrl: self::$url, accountStreamUrl: "{$ws}/v1/stream", marketStreamUrl: "{$ws}/v1/market-data");
        try {
            $got = [];
            foreach ([fn () => iterator_to_array($c->accountStream()), fn () => $c->marketStream()->subscribe('ticker', ['BTC-USD'], [], 3.0)] as $open) {
                try {
                    $open();
                } catch (AuthenticationException $e) {
                    $got[] = [$e->errorCode, $e->status, $e->requestId, $e->details];
                }
            }
            $this->assertSame(array_fill(0, 2, ['wrong_environment', 401, 'req_refused', ['expected_host' => 'sandbox-api.phoenixperpsfunding.com']]), $got);
        } finally {
            proc_terminate($server);
            proc_close($server);
        }
    }
}
