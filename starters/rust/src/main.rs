//! The starter bot: an EMA cross on one-minute candles, each entry sized so its stop loses PP_RISK_USD, previewed first,
//! placed with its stop loss on the API's servers. What it does, exactly: ../README.md. Run it on the sandbox.
//!   cargo run
use std::time::{SystemTime, UNIX_EPOCH};

use phoenixperps::{
    dec, Client, ClosePosition, CreateOrder, CreateOrderType, Decimal, Market, MarketRules, OrderExits, OrderExitsStopLoss, OrderExitsStopLossBreakEven,
    OrderExitsStopLossTrail, OrderSide, PositionsListParams, RequestOptions, RoundMode,
};
use serde_json::{json, Map, Value};

/// EMA(n): the mean of the first n values, then (x - ema) * 2 / (n + 1) + ema.
struct Ema {
    n: usize,
    seed: Vec<f64>,
    value: Option<f64>,
}

impl Ema {
    fn new(n: usize) -> Self {
        Self { n, seed: Vec::new(), value: None }
    }
    fn add(&mut self, x: f64) {
        match self.value {
            None => {
                self.seed.push(x);
                if self.seed.len() == self.n {
                    self.value = Some(self.seed.iter().sum::<f64>() / self.n as f64);
                }
            }
            Some(v) => self.value = Some((x - v) * (2.0 / (self.n as f64 + 1.0)) + v),
        }
    }
}

/// Wilder's ATR(n): the mean of the first n true ranges, then (atr * (n - 1) + tr) / n.
struct Atr {
    n: usize,
    seed: Vec<f64>,
    prev_close: Option<f64>,
    value: Option<f64>,
}

impl Atr {
    fn new(n: usize) -> Self {
        Self { n, seed: Vec::new(), prev_close: None, value: None }
    }
    fn add(&mut self, high: f64, low: f64, close: f64) {
        let tr = match self.prev_close {
            None => high - low,
            Some(pc) => (high - low).max((high - pc).abs()).max((low - pc).abs()),
        };
        self.prev_close = Some(close);
        match self.value {
            None => {
                self.seed.push(tr);
                if self.seed.len() == self.n {
                    self.value = Some(self.seed.iter().sum::<f64>() / self.n as f64);
                }
            }
            Some(v) => self.value = Some((v * (self.n as f64 - 1.0) + tr) / self.n as f64),
        }
    }
}

fn env(name: &str, fallback: &str) -> String {
    std::env::var(name).ok().filter(|v| !v.is_empty()).unwrap_or_else(|| fallback.to_string())
}

fn number<T: std::str::FromStr>(name: &str, fallback: &str) -> T {
    env(name, fallback).parse().unwrap_or_else(|_| panic!("{name} is not a number"))
}

/// .env beside it: only what the environment does not set already.
fn load_dot_env() {
    let Ok(text) = std::fs::read_to_string(".env") else { return };
    for line in text.lines() {
        let line = line.trim();
        if line.starts_with('#') {
            continue;
        }
        if let Some((k, v)) = line.split_once('=') {
            let (k, v) = (k.trim(), v.trim().trim_matches(|c| c == '"' || c == '\''));
            if !k.is_empty() && k.chars().all(|c| c.is_ascii_uppercase() || c.is_ascii_digit() || c == '_') && std::env::var_os(k).is_none() {
                std::env::set_var(k, v);
            }
        }
    }
}

struct Bot {
    client: Client,
    live: bool,
    account: String,
    market: String,
    rules: MarketRules,
    info: Market,
    tick: f64,
    risk_usd: Decimal,
    atr_mult: f64,
    trail_percent: String,
    break_even_percent: String,
    max_trades: u32,
    trades: (String, u32),
}

impl Bot {
    fn log(&self, msg: &str) {
        let now = SystemTime::now().duration_since(UNIX_EPOCH).map(|d| d.as_millis()).unwrap_or_default();
        println!("{now} {}{msg}", if self.live { "LIVE " } else { "" });
    }

    /// One live candle: the position first, then at most one order (../README.md step 4).
    async fn on_live(&mut self, time: i64, prev: (Option<f64>, Option<f64>), now: (Option<f64>, Option<f64>, Option<f64>)) -> phoenixperps::Result<()> {
        let (Some(pf), Some(ps), Some(f), Some(s), Some(atr)) = (prev.0, prev.1, now.0, now.1, now.2) else { return Ok(()) };
        let (up, down) = (pf <= ps && f > s, pf >= ps && f < s);
        let params = PositionsListParams { market: Some(self.market.clone()) };
        let positions = self.client.positions().list(&self.account, Some(&params)).await?;
        let position = positions.data.into_iter().find(|p| !p.size.is_zero());
        if let Some(p) = &position {
            if !p.protected {
                self.log(&format!("the {} position has no stop: closing it", p.side));
                self.client.positions().close(&self.account, &self.market, &ClosePosition::default()).await?;
                return Ok(());
            }
        }
        if !up && !down {
            return Ok(());
        }
        let cross = if up { "up" } else { "down" };
        if let Some(p) = &position {
            if (p.side.to_string() == "long") != up {
                self.log(&format!("cross {cross}: closing the {} position", p.side));
                self.client.positions().close(&self.account, &self.market, &ClosePosition::default()).await?;
            }
            return Ok(());
        }
        let day = time / 86_400_000;
        if self.trades.0 != day.to_string() {
            self.trades = (day.to_string(), 0);
        }
        if self.trades.1 >= self.max_trades {
            self.log(&format!("cross {cross}: {} entries today already (PP_MAX_TRADES_PER_DAY)", self.trades.1));
            return Ok(());
        }
        let ticks = (atr * self.atr_mult / self.tick + 0.5).floor();
        if ticks < 1.0 {
            return Ok(());
        }
        let distance = self.rules.round_price(dec(&format!("{}", ticks * self.tick)), RoundMode::Nearest)?;
        let size = self.rules.size_for_risk(self.risk_usd, distance)?;
        if size < self.info.min_size || size.is_zero() {
            self.log(&format!("cross {cross}: {} USD over a {distance} stop is below the market's smallest size", self.risk_usd));
            return Ok(());
        }
        let side = if up { OrderSide::Buy } else { OrderSide::Sell };
        let probe = CreateOrder { market: self.market.clone(), side: side.clone(), r#type: CreateOrderType::Market, size: Some(size), ..Default::default() };
        let preview = self.client.orders().preview(&self.account, &probe).await?;
        if preview.result.to_string() == "would_reject" {
            let code = preview.reject.map(|r| r.code).unwrap_or_default();
            self.log(&format!("cross {cross}: the API would refuse {side} {size} ({code})"));
            return Ok(());
        }
        let mut stop_loss = OrderExitsStopLoss { distance: Some(distance), ..Default::default() };
        if !self.trail_percent.is_empty() {
            stop_loss.trail = Some(OrderExitsStopLossTrail { percent: Some(dec(&self.trail_percent)), ..Default::default() });
        }
        if !self.break_even_percent.is_empty() {
            stop_loss.break_even =
                Some(OrderExitsStopLossBreakEven { trigger_percent: Some(dec(&self.break_even_percent)), offset_ticks: Some(1), ..Default::default() });
        }
        let body = CreateOrder {
            client_order_id: Some(format!("starter-{time}")),
            exits: Some(OrderExits { stop_loss: Some(stop_loss), ..Default::default() }),
            ..probe
        };
        let options = RequestOptions { idempotency_key: Some(format!("starter-{}-{time}", self.market)), ..Default::default() };
        let order = self.client.orders().create_with(&self.account, &body, &options).await?;
        self.trades.1 += 1;
        self.log(&format!("cross {cross}: {side} {size} {} ({}), stop {distance} away", self.market, order.status));
        Ok(())
    }
}

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    load_dot_env();
    let stream_url = env("PP_STREAM_URL", "");
    let mut builder = Client::builder();
    if !stream_url.is_empty() {
        builder = builder.stream_urls("", stream_url);
    }
    let client = builder.build()?;
    let live = client.environment() == "live";
    if live && env("PHOENIXPERPS_ALLOW_LIVE", "") != "yes" {
        eprintln!("This is a live key: the starter trades real money only with PHOENIXPERPS_ALLOW_LIVE=yes. Use a sandbox key to try it.");
        std::process::exit(1);
    }
    let market = env("PP_MARKET", "BTC-USD").to_uppercase();
    let (fast_n, slow_n, atr_n): (usize, usize, usize) = (number("PP_FAST", "9"), number("PP_SLOW", "21"), number("PP_ATR", "14"));
    let run_candles: u32 = number("PP_RUN_CANDLES", "0");

    // the account and the market's rules
    let wanted = env("PP_ACCOUNT", "").to_uppercase();
    let accounts = client.accounts().list().await?;
    let account = accounts
        .data
        .iter()
        .find(|a| if wanted.is_empty() { a.status.to_string() == "active" } else { a.id == wanted })
        .map(|a| a.id.clone())
        .unwrap_or_else(|| panic!("No active account (or no PP_ACCOUNT) for this key."));
    let info = client.markets().get(&market).await?;
    let mut bot = Bot {
        live,
        account,
        rules: MarketRules::new(&info),
        tick: info.tick_size.to_string().parse().unwrap_or(0.0),
        info,
        risk_usd: dec(&env("PP_RISK_USD", "50")),
        atr_mult: number("PP_ATR_MULT", "2"),
        trail_percent: env("PP_TRAIL_PERCENT", ""),
        break_even_percent: env("PP_BREAK_EVEN_PERCENT", ""),
        max_trades: number("PP_MAX_TRADES_PER_DAY", "10"),
        trades: (String::new(), 0),
        market: market.clone(),
        client,
    };
    bot.log(&format!("{} on {market}: EMA {fast_n}/{slow_n}, stop {} x ATR({atr_n}), {} USD a trade", bot.account, env("PP_ATR_MULT", "2"), bot.risk_usd));

    // candles in, one at a time: history warms the indicators up, live ones trade
    let (mut fast, mut slow, mut atr) = (Ema::new(fast_n), Ema::new(slow_n), Atr::new(atr_n));
    let mut stream = bot.client.market_stream();
    let mut params = Map::new();
    params.insert("interval".into(), json!("1m"));
    params.insert("history".into(), json!((3 * slow_n).max(50)));
    stream.subscribe("candles", &[market.as_str()], params).await?;
    let (mut last, mut warm, mut live_candles) = (0i64, false, 0u32);
    let num = |v: &Value| v.as_str().and_then(|s| s.parse::<f64>().ok()).unwrap_or(0.0);
    while let Some(frame) = stream.next().await {
        let frame = frame?;
        match frame.r#type.as_str() {
            "snapshot_end" => {
                if !warm {
                    bot.log("warmed up: trading on the next candles");
                }
                warm = true;
                continue;
            }
            "candles" => {}
            _ => continue,
        }
        for c in frame.data.as_ref().and_then(Value::as_array).into_iter().flatten() {
            let time = c["time"].as_i64().unwrap_or_default();
            if !c["final"].as_bool().unwrap_or(false) || time <= last {
                continue;
            }
            last = time;
            let prev = (fast.value, slow.value);
            let close = num(&c["close"]);
            fast.add(close);
            slow.add(close);
            atr.add(num(&c["high"]), num(&c["low"]), close);
            if !warm {
                continue;
            }
            bot.on_live(time, prev, (fast.value, slow.value, atr.value)).await?;
            live_candles += 1;
            if run_candles > 0 && live_candles >= run_candles {
                return Ok(());
            }
        }
    }
    Ok(())
}
