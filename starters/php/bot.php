<?php
// The starter bot: an EMA cross on one-minute candles, each entry sized so its stop loses PP_RISK_USD, previewed first,
// placed with its stop loss on the API's servers. What it does, exactly: ../README.md. Run it on the sandbox.
//   composer install && php bot.php
require __DIR__ . '/vendor/autoload.php';

use PhoenixPerps\Client;
use PhoenixPerps\MarketRules;
use PhoenixPerps\RequestOptions;

// .env beside it: only what the environment does not set already
if (is_file(__DIR__ . '/.env')) {
    foreach (file(__DIR__ . '/.env', FILE_IGNORE_NEW_LINES) as $line) {
        if (preg_match('/^\s*([A-Z0-9_]+)\s*=\s*(.*?)\s*$/', $line, $m) && !str_starts_with(ltrim($line), '#') && getenv($m[1]) === false) {
            putenv($m[1] . '=' . preg_replace('/^([\'"])(.*)\1$/', '$2', $m[2]));
        }
    }
}
function env(string $name, string $fallback = ''): string
{
    $v = getenv($name);
    return $v === false || $v === '' ? $fallback : $v;
}

/** EMA(n): the mean of the first n values, then (x - ema) * 2 / (n + 1) + ema. */
final class Ema
{
    public ?float $value = null;
    private array $seed = [];

    public function __construct(private readonly int $n)
    {
    }

    public function add(float $x): void
    {
        if ($this->value === null) {
            $this->seed[] = $x;
            if (count($this->seed) === $this->n) {
                $this->value = array_sum($this->seed) / $this->n;
            }
        } else {
            $this->value = ($x - $this->value) * (2 / ($this->n + 1)) + $this->value;
        }
    }
}

/** Wilder's ATR(n): the mean of the first n true ranges, then (atr * (n - 1) + tr) / n. */
final class Atr
{
    public ?float $value = null;
    private array $seed = [];
    private ?float $prevClose = null;

    public function __construct(private readonly int $n)
    {
    }

    public function add(float $high, float $low, float $close): void
    {
        $tr = $this->prevClose === null ? $high - $low : max($high - $low, abs($high - $this->prevClose), abs($low - $this->prevClose));
        $this->prevClose = $close;
        if ($this->value === null) {
            $this->seed[] = $tr;
            if (count($this->seed) === $this->n) {
                $this->value = array_sum($this->seed) / $this->n;
            }
        } else {
            $this->value = ($this->value * ($this->n - 1) + $tr) / $this->n;
        }
    }
}

$marketId = strtoupper(env('PP_MARKET', 'BTC-USD'));
$riskUsd = env('PP_RISK_USD', '50');
[$fastN, $slowN, $atrN] = [(int) env('PP_FAST', '9'), (int) env('PP_SLOW', '21'), (int) env('PP_ATR', '14')];
$atrMult = (float) env('PP_ATR_MULT', '2');
[$trailPercent, $breakEvenPercent] = [env('PP_TRAIL_PERCENT'), env('PP_BREAK_EVEN_PERCENT')];
[$maxTrades, $runCandles] = [(int) env('PP_MAX_TRADES_PER_DAY', '10'), (int) env('PP_RUN_CANDLES', '0')];

if (env('PHOENIXPERPS_API_KEY') === '') { // a client without a key reads markets only
    fwrite(STDERR, "No API key: put a sandbox key in .env (PHOENIXPERPS_API_KEY, see .env.example).\n");
    exit(1);
}
$client = new Client(apiKey: env('PHOENIXPERPS_API_KEY'), marketStreamUrl: env('PP_STREAM_URL') ?: null);
$live = $client->environment === 'live';
if ($live && env('PHOENIXPERPS_ALLOW_LIVE') !== 'yes') {
    fwrite(STDERR, "This is a live key: the starter trades real money only with PHOENIXPERPS_ALLOW_LIVE=yes. Use a sandbox key to try it.\n");
    exit(1);
}
$log = function (string $msg) use ($live): void {
    printf("%s %s%s\n", gmdate('Y-m-d\TH:i:s\Z'), $live ? 'LIVE ' : '', $msg);
};

// the account and the market's rules
$wanted = strtoupper(env('PP_ACCOUNT'));
$account = null;
foreach ($client->accounts->list()->data as $a) {
    if ($wanted !== '' ? $a->id === $wanted : $a->status === 'active') {
        $account = $a->id;
        break;
    }
}
if ($account === null) {
    fwrite(STDERR, $wanted !== '' ? "No account {$wanted} for this key.\n" : "No active account for this key.\n");
    exit(1);
}
$market = $client->markets->get($marketId);
$rules = new MarketRules($market);
$tick = (float) $market->tickSize;
$log("{$account} on {$marketId}: EMA {$fastN}/{$slowN}, stop " . env('PP_ATR_MULT', '2') . " x ATR({$atrN}), {$riskUsd} USD a trade");

$fast = new Ema($fastN);
$slow = new Ema($slowN);
$atr = new Atr($atrN);
$trades = ['day' => '', 'n' => 0];

// one live candle: the position first, then at most one order (../README.md step 4)
$onLive = function (int $time, ?float $prevFast, ?float $prevSlow) use ($client, $account, $marketId, $market, $rules, $tick, $riskUsd, $atrMult, $trailPercent, $breakEvenPercent, $maxTrades, $fast, $slow, $atr, &$trades, $log): void {
    if ($fast->value === null || $slow->value === null || $atr->value === null || $prevFast === null || $prevSlow === null) {
        return;
    }
    $up = $prevFast <= $prevSlow && $fast->value > $slow->value;
    $down = $prevFast >= $prevSlow && $fast->value < $slow->value;
    $position = null;
    foreach ($client->positions->list($account, ['market' => $marketId])->data as $p) {
        if (bccomp($p->size ?? '0', '0', 18) !== 0) {
            $position = $p;
            break;
        }
    }
    if ($position !== null && !$position->protected) {
        $log("the {$position->side} position has no stop: closing it");
        $client->positions->close($account, $marketId, []);
        return;
    }
    if (!$up && !$down) {
        return;
    }
    $cross = $up ? 'up' : 'down';
    if ($position !== null) {
        if (($position->side === 'long') !== $up) {
            $log("cross {$cross}: closing the {$position->side} position");
            $client->positions->close($account, $marketId, []);
        }
        return;
    }
    $day = gmdate('Y-m-d', intdiv($time, 1000));
    if ($trades['day'] !== $day) {
        $trades = ['day' => $day, 'n' => 0];
    }
    if ($trades['n'] >= $maxTrades) {
        $log("cross {$cross}: {$trades['n']} entries today already (PP_MAX_TRADES_PER_DAY)");
        return;
    }
    $ticks = floor($atr->value * $atrMult / $tick + 0.5);
    if ($ticks < 1) {
        return;
    }
    $distance = $rules->roundPrice(rtrim(rtrim(sprintf('%.12F', $ticks * $tick), '0'), '.'));
    $size = $rules->sizeForRisk($riskUsd, $distance);
    if (bccomp($size, $market->minSize, 18) < 0 || bccomp($size, '0', 18) === 0) {
        $log("cross {$cross}: {$riskUsd} USD over a {$distance} stop is below the market's smallest size");
        return;
    }
    $side = $up ? 'buy' : 'sell';
    $preview = $client->orders->preview($account, ['market' => $marketId, 'side' => $side, 'type' => 'market', 'size' => $size]);
    if ($preview->result === 'would_reject') {
        $log("cross {$cross}: the API would refuse {$side} {$size} (" . ($preview->reject?->code ?? '') . ')');
        return;
    }
    $stopLoss = ['distance' => $distance];
    if ($trailPercent !== '') {
        $stopLoss['trail'] = ['percent' => $trailPercent];
    }
    if ($breakEvenPercent !== '') {
        $stopLoss['break_even'] = ['trigger_percent' => $breakEvenPercent, 'offset_ticks' => 1];
    }
    $order = $client->orders->create($account, [
        'market' => $marketId, 'side' => $side, 'type' => 'market', 'size' => $size,
        'client_order_id' => "starter-{$time}", 'exits' => ['stop_loss' => $stopLoss],
    ], new RequestOptions(idempotencyKey: "starter-{$marketId}-{$time}"));
    $trades['n']++;
    $log("cross {$cross}: {$side} {$size} {$marketId} ({$order->status}), stop {$distance} away");
};

// candles in, one at a time: history warms the indicators up, live ones trade
$stream = $client->marketStream();
$stream->subscribe('candles', [$marketId], ['interval' => '1m', 'history' => max(3 * $slowN, 50)]);
[$last, $warm, $liveCandles] = [0, false, 0];
foreach ($stream as $frame) {
    if ($frame['type'] === 'snapshot_end') {
        if (!$warm) {
            $log('warmed up: trading on the next candles');
        }
        $warm = true;
        continue;
    }
    if ($frame['type'] !== 'candles') {
        continue;
    }
    foreach ($frame['data'] as $c) {
        if (!$c['final'] || $c['time'] <= $last) {
            continue;
        }
        $last = $c['time'];
        [$prevFast, $prevSlow] = [$fast->value, $slow->value];
        $close = (float) $c['close'];
        $fast->add($close);
        $slow->add($close);
        $atr->add((float) $c['high'], (float) $c['low'], $close);
        if (!$warm) {
            continue;
        }
        $onLive($c['time'], $prevFast, $prevSlow);
        if ($runCandles > 0 && ++$liveCandles >= $runCandles) {
            $stream->close();
            exit(0);
        }
    }
}
