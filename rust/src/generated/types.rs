// Generated from the API spec by generator/printers/rust.js. Do not edit: run `node generator`.

use super::enums::*;
use rust_decimal::Decimal;
use serde::{Deserialize, Serialize};
use serde_json::{Map, Value};

/// Every error has this shape.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ErrorEnvelope {
    /// The error.
    pub error: ErrorEnvelopeError,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ErrorEnvelopeError
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ErrorEnvelopeError {
    /// The family of the error; it decides the HTTP status.
    pub r#type: ErrorEnvelopeErrorType,
    /// The error code: match on this.
    pub code: String,
    /// A sentence for people. It may change; never match on it.
    pub message: String,
    /// The id of the request; give it to support.
    pub request_id: String,
    /// Whether the same request may succeed later.
    pub retryable: bool,
    /// Where this code is explained.
    pub doc_url: String,
    /// More about the problem: the fields at fault, a limit, where to go.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub details: Option<Value>,
    /// The order the error is about (an order the engine refused is kept, with this id).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub order_id: Option<String>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Platform
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Platform {
    /// The environment of the host you called.
    pub environment: PlatformEnvironment,
    /// The version of this API.
    pub version: String,
    /// The time on the server.
    pub server_time: i64,
    /// The documentation.
    pub docs_url: String,
    /// This API as OpenAPI 3.1.
    pub spec_url: String,
    /// The status.
    pub status: PlatformStatus,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// PlatformStatus
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct PlatformStatus {
    /// Whether the trading engine is reachable.
    pub engine: PlatformStatusEngine,
    /// When the trading engine last started.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub engine_started_at: Option<i64>,
    /// Whether the market-data stream is live, with every price feed.
    pub market_data: PlatformStatusMarketData,
    /// Whether outbound webhooks are being delivered.
    pub webhooks: PlatformStatusWebhooks,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Status
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Status {
    /// The environment of the host you called.
    pub environment: PlatformEnvironment,
    /// Whether this environment takes requests.
    pub api: StatusApi,
    /// The engine.
    pub engine: StatusEngine,
    /// The account data.
    pub account_data: StatusAccountData,
    /// The market data.
    pub market_data: StatusMarketData,
    /// The features.
    pub features: StatusFeatures,
    /// The time on the server.
    pub server_time: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// StatusEngine
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct StatusEngine {
    /// Whether the API is connected to the trading engine.
    pub connected: bool,
    /// When the trading engine last started.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub started_at: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// StatusAccountData
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct StatusAccountData {
    /// When your keys and accounts were last confirmed with the website.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub synced_at: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// StatusMarketData
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct StatusMarketData {
    /// Whether the market-data stream is live, with every price feed.
    pub live: bool,
    /// When that was last checked.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub checked_at: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// StatusFeatures
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct StatusFeatures {
    /// The account stream.
    pub stream: bool,
    /// Market data.
    pub market_data: bool,
    /// Webhooks.
    pub webhooks: bool,
    /// TradingView alerts.
    pub tradingview: bool,
    /// AI assistants (MCP).
    pub mcp: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Limits
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Limits {
    /// The environment of the host you called.
    pub environment: PlatformEnvironment,
    /// The request budgets. A request takes from every budget it belongs to.
    pub rate: LimitsRate,
    /// The keys.
    pub keys: LimitsKeys,
    /// The orders.
    pub orders: LimitsOrders,
    /// The sandbox.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub sandbox: Option<LimitsSandbox>,
    /// The strategies.
    pub strategies: LimitsStrategies,
    /// The largest request body.
    pub body_max_bytes: i64,
    /// The page limit.
    pub page_limit: LimitsPageLimit,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The request budgets. A request takes from every budget it belongs to.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsRate {
    /// The public per ip.
    pub public_per_ip: LimitsRatePublicPerIp,
    /// The per ip.
    pub per_ip: LimitsRatePerIp,
    /// The per key read.
    pub per_key_read: LimitsRatePerKeyRead,
    /// The per key trade.
    pub per_key_trade: LimitsRatePerKeyTrade,
    /// The per account trade.
    pub per_account_trade: LimitsRatePerAccountTrade,
    /// The per customer.
    pub per_customer: LimitsRatePerCustomer,
    /// The bulk.
    pub bulk: LimitsRateBulk,
    /// The what if.
    pub what_if: LimitsRateWhatIf,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsRatePublicPerIp
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsRatePublicPerIp {
    /// Requests a minute.
    pub per_min: i64,
    /// Requests at once.
    pub burst: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsRatePerIp
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsRatePerIp {
    /// Requests a minute.
    pub per_min: i64,
    /// Requests at once.
    pub burst: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsRatePerKeyRead
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsRatePerKeyRead {
    /// Requests a minute.
    pub per_min: i64,
    /// Requests at once.
    pub burst: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsRatePerKeyTrade
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsRatePerKeyTrade {
    /// Requests a minute.
    pub per_min: i64,
    /// Requests at once.
    pub burst: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsRatePerAccountTrade
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsRatePerAccountTrade {
    /// Requests a minute.
    pub per_min: i64,
    /// Requests at once.
    pub burst: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsRatePerCustomer
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsRatePerCustomer {
    /// Requests a minute.
    pub per_min: i64,
    /// Requests at once.
    pub burst: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsRateBulk
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsRateBulk {
    /// Requests a minute.
    pub per_min: i64,
    /// Requests at once.
    pub burst: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsRateWhatIf
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsRateWhatIf {
    /// Requests a minute.
    pub per_min: i64,
    /// Requests at once.
    pub burst: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsKeys
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsKeys {
    /// Keys you may hold in each environment.
    pub per_environment: i64,
    /// Addresses in a key's allowlist.
    pub ip_allowlist_max: i64,
    /// The longest a key may live, in days.
    pub expiry_max_days: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsOrders
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsOrders {
    /// Working orders an account may hold.
    pub max_working_per_account: i64,
    /// Working orders across your accounts.
    pub max_working_per_customer: i64,
    /// New orders a minute on one account.
    pub new_per_min_account: i64,
    /// New orders a day on one account.
    pub new_per_day_account: i64,
    /// How far from the market price a priced order may be, in percent.
    pub price_band_pct: Decimal,
    /// The oldest price an order is checked against, in seconds.
    pub quote_max_age_s: i64,
    /// The oldest unchanged price of a quiet market an order is checked against, while the price feed lives (other markets move), in seconds.
    pub quote_max_age_slow_s: i64,
    /// The nearest a gtd order may expire, in seconds from now.
    pub gtd_min_seconds: i64,
    /// The farthest, in days from now.
    pub gtd_max_days: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsSandbox
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsSandbox {
    /// Sandbox accounts you may open, ever.
    pub accounts_for_life: i64,
    /// The balance a sandbox account starts with.
    pub start_balance: Decimal,
    /// Hours without use before a sandbox account is parked.
    pub parked_after_hours: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsStrategies
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsStrategies {
    /// Running strategies an account may hold.
    pub per_account: i64,
    /// Of those, programs (oco, twap, vwap, scaled, chase).
    pub programs_per_account: i64,
    /// Working orders of strategies on one account.
    pub legs_per_account: i64,
    /// Running strategies across your accounts.
    pub per_customer: i64,
    /// Take-profit legs on one position.
    pub take_profits_per_position: i64,
    /// Stop-loss legs on one position.
    pub stop_losses_per_position: i64,
    /// The shortest twap or vwap, in minutes.
    pub twap_min_minutes: i64,
    /// The longest twap or vwap, in minutes.
    pub twap_max_minutes: i64,
    /// The shortest time between slices, in seconds.
    pub slice_min_seconds: i64,
    /// The most limits in a scaled ladder.
    pub scaled_max_rungs: i64,
    /// Whether chase strategies may start.
    pub chase_enabled: bool,
    /// The longest a chase may run, in minutes.
    pub chase_max_minutes: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitsPageLimit
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitsPageLimit {
    /// Items a page holds by default.
    pub default: i64,
    /// The most a page may hold.
    pub max: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Market
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Market {
    /// A market id: the asset and USD.
    pub market: String,
    /// The market's name; for a currency, the pair its price is quoted in (JPY-USD: USD/JPY, yen per dollar).
    pub name: String,
    /// Whether the market trades now.
    pub status: MarketStatus,
    /// The venue it trades on: its prices and fills are that venue's. Null for a currency on FX hours.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub venue: Option<String>,
    /// When it trades: around the clock, its underlying market's sessions (Standard Market Hours, closed on weekends), or FX hours.
    pub trading_hours: TradingHours,
    /// For an option (the same asset on another venue, or on Standard Market Hours): its market. Null for a market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub option_of: Option<String>,
    /// The ways this asset can be traded, the market first, then its options: an account holds one of them at a time (market_option_held). Null when there is only the market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub options: Option<Vec<MarketOption>>,
    /// Prices are multiples of this.
    pub tick_size: Decimal,
    /// Sizes are multiples of this, in the base asset.
    pub size_step: Decimal,
    /// The smallest order size (the size step). An order must also be worth at least min_notional.
    pub min_size: Decimal,
    /// The smallest order value in USD: an order worth less is refused order_too_small.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub min_notional: Option<Decimal>,
    /// The highest leverage on this market.
    pub max_leverage: i64,
    /// The fee on each fill, in percent of its value.
    pub fee_rate_pct: Decimal,
    /// The most you may hold on this market across your accounts on the same rule, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub exposure_cap: Option<Decimal>,
    /// How far from the price a market order may fill, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_slippage_pct: Option<MarketMaxSlippagePct>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// MarketOption
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MarketOption {
    /// A market id: the asset and USD.
    pub market: String,
    /// Its venue.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub venue: Option<String>,
    /// When it trades.
    pub trading_hours: TradingHours,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// How far from the price a market order may fill, in percent.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MarketMaxSlippagePct {
    /// The slippage bound a market order gets when it names none.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub default: Option<Decimal>,
    /// The tightest bound an order may ask.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub min: Option<Decimal>,
    /// The widest bound an order may ask.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// A market's 24-hour figures and open interest. A figure not known yet is null.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MarketStats {
    /// A market id: the asset and USD.
    pub market: String,
    /// The last trade.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub last: Option<Decimal>,
    /// The change over 24 hours.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub change_24h: Option<Decimal>,
    /// That change in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub change_24h_pct: Option<Decimal>,
    /// The highest in 24 hours.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub high_24h: Option<Decimal>,
    /// The lowest in 24 hours.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub low_24h: Option<Decimal>,
    /// The size traded in 24 hours, in the base asset.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub volume_24h: Option<Decimal>,
    /// The value traded in 24 hours, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub notional_volume_24h: Option<Decimal>,
    /// The market's open interest: the size held open in positions, in the base asset.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub open_interest: Option<Decimal>,
    /// That open interest in USD, at the last price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub notional_open_interest: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The account's rule, as the trading engine enforces it.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Rule {
    /// The balance the account started with.
    pub start_balance: Decimal,
    /// The daily loss.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub daily_loss: Option<RuleDailyLoss>,
    /// The max drawdown.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_drawdown: Option<RuleMaxDrawdown>,
    /// The weekly loss.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub weekly_loss: Option<RuleWeeklyLoss>,
    /// The profit target.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub profit_target: Option<RuleProfitTarget>,
    /// Days with a trade the account needs.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub min_trading_days: Option<i64>,
    /// Calendar days the account needs.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub min_calendar_days: Option<i64>,
    /// The consistency.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub consistency: Option<RuleConsistency>,
    /// Positions may stay open overnight.
    pub overnight_holding: bool,
    /// Positions may stay open over the weekend.
    pub weekend_holding: bool,
    /// Days without a trade after which the account fails.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub inactivity_days: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// RuleDailyLoss
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct RuleDailyLoss {
    /// The most the account may lose in a trading day, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub amount: Option<Decimal>,
    /// The same limit in percent of its basis.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub pct: Option<Decimal>,
    /// What the day's loss is measured from.
    pub basis: RuleDailyLossBasis,
    /// What happens when it is reached.
    pub on_breach: OnBreach,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// RuleMaxDrawdown
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct RuleMaxDrawdown {
    /// The most the account may fall from its reference, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub amount: Option<Decimal>,
    /// The same limit in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub pct: Option<Decimal>,
    /// How the reference moves: end_of_day and end_of_trade follow the highest balance, static stays at the start, intraday_equity follows the highest equity.
    pub mode: RuleMaxDrawdownMode,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// RuleWeeklyLoss
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct RuleWeeklyLoss {
    /// The most the account may lose in a week, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub amount: Option<Decimal>,
    /// The same limit in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub pct: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// RuleProfitTarget
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct RuleProfitTarget {
    /// The profit to reach, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub amount: Option<Decimal>,
    /// The same target in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub pct: Option<Decimal>,
    /// Positions must be closed when it is reached.
    pub requires_flat: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// RuleConsistency
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct RuleConsistency {
    /// The most one day may bring, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub amount: Option<Decimal>,
    /// The most one day may bring, in percent of the total.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub pct: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Account
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Account {
    /// The account number.
    pub id: String,
    /// The environment the account lives in.
    pub environment: PlatformEnvironment,
    /// Where the account is in the programme.
    pub stage: AccountStage,
    /// pending: being opened; active: trading.
    pub status: AccountStatus,
    /// The product it was bought as.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub product: Option<String>,
    /// Its size as sold.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub tier: Option<String>,
    /// The balance it started with.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub start_balance: Option<Decimal>,
    /// The balance: closed trades and fees only.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub balance: Option<Decimal>,
    /// The rule.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub rule: Option<Rule>,
    /// A payout may be requested now.
    pub payout_eligible: bool,
    /// Sandbox only: parked after a while unused; it wakes the moment you use it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub parked: Option<bool>,
    /// When the account was made.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub created_at: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// A loss limit and the room left to it.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Limit {
    /// The limit, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit: Option<Decimal>,
    /// The equity at which it is breached.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub floor: Option<Decimal>,
    /// How much more the account may lose before it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub room: Option<Decimal>,
    /// How much of it is used, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub used_pct: Option<Decimal>,
    /// Daily loss only: when it resets (midnight New York).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub resets_at: Option<i64>,
    /// Daily loss only: what happens when it is reached.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub on_breach: Option<OnBreach>,
    /// Daily loss only: the day's start is estimated (the engine does not publish it).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub estimated: Option<bool>,
    /// Max drawdown only: how the reference moves.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub mode: Option<RuleMaxDrawdownMode>,
    /// Max drawdown only: the engine may hold a higher floor than shown (it follows equity highs).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub at_least: Option<bool>,
    /// The rule no longer applies this limit (its cut-off was reached).
    pub lifted: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Warning
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Warning {
    /// What it is about.
    pub code: WarningCode,
    /// How serious.
    pub level: WarningLevel,
    /// The market it is about.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub market: Option<String>,
    /// The figure behind it, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub value: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRisk
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRisk {
    /// When the oldest figure here was read.
    pub as_of: i64,
    /// Some figures are older than usual (the engine was busy).
    pub stale: bool,
    /// The balance.
    pub balance: Decimal,
    /// The balance plus the open P&L at the market price.
    pub equity: Decimal,
    /// The open P&L.
    pub unrealized_pnl: Decimal,
    /// The equity change since midnight New York, open positions included: what the daily loss rule counts.
    pub pnl_today: Decimal,
    /// The P&L closed today.
    pub realized_today: Decimal,
    /// The fees paid today.
    pub fees_today: Decimal,
    /// Trades closed today.
    pub trades_today: i64,
    /// The margin your positions hold.
    pub margin_used: Decimal,
    /// What is left of the balance for new positions.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub margin_available: Option<Decimal>,
    /// The exposure.
    pub exposure: AccountRiskExposure,
    /// The daily loss.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub daily_loss: Option<Limit>,
    /// The max drawdown.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_drawdown: Option<Limit>,
    /// The profit target.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub profit_target: Option<AccountRiskProfitTarget>,
    /// The trading days.
    pub trading_days: AccountRiskTradingDays,
    /// The nearest liquidation.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub nearest_liquidation: Option<AccountRiskNearestLiquidation>,
    /// When the trading day ends (midnight New York).
    pub trading_day_ends_at: i64,
    /// What needs your attention, most serious first.
    pub warnings: Vec<Warning>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRiskExposure
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskExposure {
    /// The value of every open position.
    pub gross: Decimal,
    /// The value of the long ones.
    pub long: Decimal,
    /// The value of the short ones.
    pub short: Decimal,
    /// Gross exposure over equity.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub effective_leverage: Option<Decimal>,
    /// Per market, largest first.
    pub markets: Vec<AccountRiskExposureMarket>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRiskExposureMarket
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskExposureMarket {
    /// A market id: the asset and USD.
    pub market: String,
    /// The value held on it.
    pub notional: Decimal,
    /// That value over equity.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub leverage: Option<Decimal>,
    /// The exposure cap of the market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cap: Option<Decimal>,
    /// How much of the cap is used, counting your other accounts on the same rule, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cap_used_pct: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRiskProfitTarget
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskProfitTarget {
    /// The balance that reaches it.
    pub goal_balance: Decimal,
    /// Still to make.
    pub remaining: Decimal,
    /// How far, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub progress_pct: Option<Decimal>,
    /// Positions must be closed when it is reached.
    pub requires_flat: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRiskTradingDays
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskTradingDays {
    /// Days with a trade so far.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub count: Option<i64>,
    /// Days the rule needs.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub required: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRiskNearestLiquidation
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskNearestLiquidation {
    /// A market id: the asset and USD.
    pub market: String,
    /// Its liquidation price.
    pub price: Decimal,
    /// How far the price is from it, in percent.
    pub distance_pct: Decimal,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Progress
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Progress {
    /// Where the account is in the programme.
    pub stage: AccountStage,
    /// The objectives.
    pub objectives: ProgressObjectives,
    /// The limits.
    pub limits: ProgressLimits,
    /// The payout.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub payout: Option<ProgressPayout>,
    /// When the oldest figure here was read.
    pub as_of: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ProgressObjectives
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ProgressObjectives {
    /// The profit target.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub profit_target: Option<ProgressObjectivesProfitTarget>,
    /// The min trading days.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub min_trading_days: Option<ProgressObjectivesMinTradingDays>,
    /// The min calendar days.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub min_calendar_days: Option<ProgressObjectivesMinCalendarDays>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ProgressObjectivesProfitTarget
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ProgressObjectivesProfitTarget {
    /// The balance that reaches it.
    pub goal_balance: Decimal,
    /// Still to make.
    pub remaining: Decimal,
    /// How far, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub progress_pct: Option<Decimal>,
    /// Positions must be closed when it is reached.
    pub requires_flat: bool,
    /// Whether it is reached.
    pub status: ProgressObjectivesProfitTargetStatus,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ProgressObjectivesMinTradingDays
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ProgressObjectivesMinTradingDays {
    /// Days the rule needs.
    pub required: i64,
    /// Days with a trade so far.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub count: Option<i64>,
    /// Whether it is reached.
    pub status: ProgressObjectivesProfitTargetStatus,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ProgressObjectivesMinCalendarDays
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ProgressObjectivesMinCalendarDays {
    /// Calendar days the rule needs.
    pub required: i64,
    /// Calendar days so far.
    pub count: i64,
    /// Whether it is reached.
    pub status: ProgressObjectivesProfitTargetStatus,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ProgressLimits
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ProgressLimits {
    /// The daily loss.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub daily_loss: Option<LimitStanding>,
    /// The max drawdown.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_drawdown: Option<LimitStanding>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ProgressPayout
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ProgressPayout {
    /// A payout may be requested now.
    pub eligible: bool,
    /// What may be paid out.
    pub available: Decimal,
    /// The smallest payout.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub min_payout: Option<Decimal>,
    /// Paid out so far.
    pub paid_out: Decimal,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// TradingPolicy
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingPolicy {
    /// The rule.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub rule: Option<Rule>,
    /// The trading day.
    pub trading_day: TradingPolicyTradingDay,
    /// The slippage pct.
    pub slippage_pct: TradingPolicySlippagePct,
    /// The margin.
    pub margin: TradingPolicyMargin,
    /// The orders.
    pub orders: TradingPolicyOrders,
    /// The limits you set on the key.
    pub controls: TradingPolicyControls,
    /// The budgets.
    pub budgets: TradingPolicyBudgets,
    /// When the oldest figure here was read.
    pub as_of: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// TradingPolicyTradingDay
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingPolicyTradingDay {
    /// The trading day runs midnight to midnight in this zone.
    pub timezone: String,
    /// When this one ends.
    pub ends_at: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// TradingPolicySlippagePct
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingPolicySlippagePct {
    /// The bound a market order gets when it names none, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub default: Option<Decimal>,
    /// The tightest it may ask.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub min: Option<Decimal>,
    /// The widest it may ask.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// TradingPolicyMargin
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingPolicyMargin {
    /// The margin mode of a market you never set.
    pub default_mode: TradingPolicyMarginDefaultMode,
    /// The share of a position's margin kept as maintenance, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub maintenance_margin_pct: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// TradingPolicyOrders
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingPolicyOrders {
    /// Working orders the account may hold.
    pub max_working_per_account: i64,
    /// New orders a minute.
    pub new_per_min_account: i64,
    /// How far from the market price a priced order may be, in percent.
    pub price_band_pct: Decimal,
    /// The oldest price an order is checked against, in seconds.
    pub quote_max_age_s: i64,
    /// The oldest unchanged price of a quiet market an order is checked against, while the price feed lives (other markets move), in seconds.
    pub quote_max_age_slow_s: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The limits you set on the key.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingPolicyControls {
    /// The markets the key may trade; empty: every market.
    pub markets: Vec<String>,
    /// The key may only reduce positions.
    pub reduce_only: bool,
    /// The largest order the key may place, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_order_usd: Option<Decimal>,
    /// The largest position the key may hold, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_position_usd: Option<Decimal>,
    /// The loss in a trading day at which the key stops trading the account.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub daily_loss_usd: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// TradingPolicyBudgets
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingPolicyBudgets {
    /// Reads a minute for this key.
    pub read_per_min: i64,
    /// Trading calls a minute for this key.
    pub trade_per_min: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// EquityHistory
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct EquityHistory {
    /// One point per closed trade, or per trading day.
    pub resolution: Resolution,
    /// Oldest first; the first point is the opening balance.
    pub points: Vec<EquityHistoryPoint>,
    /// The live.
    pub live: EquityHistoryLive,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// EquityHistoryPoint
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct EquityHistoryPoint {
    /// When.
    pub time: i64,
    /// The trading day (New York), with resolution day.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub date: Option<String>,
    /// The balance then.
    pub balance: Decimal,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// EquityHistoryLive
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct EquityHistoryLive {
    /// When the live figures were read.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub time: Option<i64>,
    /// The balance now.
    pub balance: Decimal,
    /// The equity now.
    pub equity: Decimal,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// DailyResult
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct DailyResult {
    /// The trading day (midnight to midnight New York).
    pub date: String,
    /// Trades closed that day.
    pub trades: i64,
    /// Their P&L before fees.
    pub gross_pnl: Decimal,
    /// Their fees.
    pub fees: Decimal,
    /// Their P&L after fees.
    pub pnl: Decimal,
    /// Paid out that day.
    pub withdrawn: Decimal,
    /// The balance at the day's end.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub balance_end: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// MarginSetting
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MarginSetting {
    /// A market id: the asset and USD.
    pub market: String,
    /// The leverage new positions on this market get.
    pub leverage: i64,
    /// Isolated: each position holds its own margin. Cross: positions share the balance.
    pub margin_mode: MarginMode,
    /// The highest this account may use on the market (its trading rule's). null: the market is not available on this account.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_leverage: Option<i64>,
    /// Where it comes from: the open position, your saved setting, or the market default.
    pub source: MarginSettingSource,
    /// A position or a working order on the market: the mode cannot change now.
    pub locked: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ExitLeg
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitLeg {
    /// The working order.
    pub order_id: String,
    /// The order's type.
    pub r#type: OrderType,
    /// Its trigger or limit price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub price: Option<Decimal>,
    /// Its size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub size: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Position
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Position {
    /// This position, while it stays open on the same side.
    pub id: String,
    /// A market id: the asset and USD.
    pub market: String,
    /// Long or short.
    pub side: PositionSide,
    /// The size, in the base asset.
    pub size: Decimal,
    /// The average entry price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub entry_price: Option<Decimal>,
    /// The market price it is valued at.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub mark_price: Option<Decimal>,
    /// Its value at that price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub notional: Option<Decimal>,
    /// Its open P&L.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub unrealized_pnl: Option<Decimal>,
    /// Its leverage.
    pub leverage: i64,
    /// Its margin mode.
    pub margin_mode: MarginMode,
    /// The margin it took at entry.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub initial_margin: Option<Decimal>,
    /// Margin you added to it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub added_margin: Option<Decimal>,
    /// What it holds of the balance.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub margin: Option<Decimal>,
    /// The liquidation.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub liquidation: Option<PositionLiquidation>,
    /// The breach prices.
    pub breach_prices: PositionBreachPrices,
    /// The exits.
    pub exits: PositionExits,
    /// At least one stop loss rests on it.
    pub protected: bool,
    /// When it opened.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub opened_at: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// PositionLiquidation
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct PositionLiquidation {
    /// The price at which it is liquidated; null when there is none or it depends on several positions.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub price: Option<Decimal>,
    /// How far the market price is from it, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub distance_pct: Option<Decimal>,
    /// Cross margin on several markets: no single price, every one of them moves it.
    pub account_wide: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// PositionBreachPrices
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct PositionBreachPrices {
    /// The price at which this position alone would breach the daily loss limit.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub daily_loss: Option<Decimal>,
    /// The price at which it alone would breach the max drawdown.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_drawdown: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// PositionExits
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct PositionExits {
    /// Limit orders on the closing side.
    pub take_profit: Vec<ExitLeg>,
    /// Stop orders on the closing side.
    pub stop_loss: Vec<ExitLeg>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ClosedPosition
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ClosedPosition {
    /// A market id: the asset and USD.
    pub market: String,
    /// Long or short.
    pub side: PositionSide,
    /// The size closed.
    pub size: Decimal,
    /// The average entry price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub entry_price: Option<Decimal>,
    /// The average exit price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub exit_price: Option<Decimal>,
    /// The P&L before fees.
    pub gross_pnl: Decimal,
    /// The fees.
    pub fees: Decimal,
    /// The P&L after fees.
    pub pnl: Decimal,
    /// When it opened.
    pub opened_at: i64,
    /// When it was fully closed.
    pub closed_at: i64,
    /// The pieces it was closed in.
    pub lots: Vec<ClosedPositionLot>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ClosedPositionLot
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ClosedPositionLot {
    /// The size.
    pub size: Decimal,
    /// Entry.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub entry_price: Option<Decimal>,
    /// Exit.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub exit_price: Option<Decimal>,
    /// P&L after fees.
    pub pnl: Decimal,
    /// Fees.
    pub fees: Decimal,
    /// Opened.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub opened_at: Option<i64>,
    /// Closed.
    pub closed_at: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Order
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Order {
    /// The order.
    pub id: String,
    /// Your own id for it, when it was placed through the API.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub client_order_id: Option<String>,
    /// The account number.
    pub account_id: String,
    /// A market id: the asset and USD.
    pub market: String,
    /// The order's side; null for an order placed elsewhere before the API saw it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub side: Option<OrderSide>,
    /// The order's type.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub r#type: Option<OrderType>,
    /// unconfirmed: the engine has not confirmed its last state yet.
    pub status: OrderStatus,
    /// The size, in the base asset.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub size: Option<Decimal>,
    /// How much filled.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub filled_size: Option<Decimal>,
    /// Its limit price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit_price: Option<Decimal>,
    /// Its trigger price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub stop_price: Option<Decimal>,
    /// The average fill price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub average_fill_price: Option<Decimal>,
    /// The fee its fill paid.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub fee: Option<Decimal>,
    /// It may only reduce a position; null when not known (placed elsewhere).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub reduce_only: Option<bool>,
    /// How long it rests.
    pub time_in_force: TimeInForce,
    /// A gtd order: when it expires (it ends expired then); null otherwise.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub expires_at: Option<i64>,
    /// It may only rest, never fill at once; null when not known (placed elsewhere).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub post_only: Option<bool>,
    /// Where it was placed: this API, an AI assistant (the MCP server), a trading platform, a strategy, a TradingView alert, or the system (a liquidation).
    pub source: OrderSource,
    /// The strategy that placed it (source strategy).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub strategy_id: Option<String>,
    /// Exits attached to it (orders placed through the API).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub exits: Option<Map<String, Value>>,
    /// Why it was canceled.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cancel_reason: Option<OrderCancelReason>,
    /// The reject.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub reject: Option<OrderReject>,
    /// When it was placed.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub created_at: Option<i64>,
    /// When it filled.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub filled_at: Option<i64>,
    /// When it was canceled.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub canceled_at: Option<i64>,
    /// Its latest change.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub updated_at: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// OrderReject
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct OrderReject {
    /// The error code.
    pub code: String,
    /// What happened.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub message: Option<String>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Fill
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Fill {
    /// The fill.
    pub id: String,
    /// Its order.
    pub order_id: String,
    /// A market id: the asset and USD.
    pub market: String,
    /// The side; null for an order placed elsewhere before the API saw it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub side: Option<OrderSide>,
    /// The size filled.
    pub size: Decimal,
    /// The fill price.
    pub price: Decimal,
    /// Its fee.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub fee: Option<Decimal>,
    /// A liquidation filled it.
    pub liquidation: bool,
    /// When it filled.
    pub time: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Me
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Me {
    /// The credential.
    pub credential: MeCredential,
    /// The accounts it reaches.
    pub accounts: Vec<String>,
    /// all: every account of the environment, new ones too. selected: only those listed.
    pub account_scope: MeAccountScope,
    /// The limits you set on the key.
    pub controls: MeControls,
    /// The budgets.
    pub budgets: MeBudgets,
    /// Through the MCP server (an AI assistant): how its trades are confirmed and its budget. Null for any other call.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub assistant: Option<MeAssistant>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// MeCredential
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MeCredential {
    /// What you called with: an API key, or an app you signed in to.
    pub kind: MeCredentialKind,
    /// The key's id (the part after ppk_test_ or ppk_live_), or the app access's id.
    pub id: String,
    /// Its name (an app: the app's name).
    pub name: String,
    /// Its environment.
    pub environment: PlatformEnvironment,
    /// read: look only. trade: also place and cancel.
    pub access: MeCredentialAccess,
    /// When it expires; null: never.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub expires_at: Option<i64>,
    /// When it was made.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub created_at: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The limits you set on the key.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MeControls {
    /// The markets the key may trade; empty: every market.
    pub markets: Vec<String>,
    /// The key may only reduce positions.
    pub reduce_only: bool,
    /// The largest order the key may place, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_order_usd: Option<Decimal>,
    /// The largest position the key may hold, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_position_usd: Option<Decimal>,
    /// The loss in a trading day at which the key stops trading the account.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub daily_loss_usd: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// MeBudgets
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MeBudgets {
    /// Reads a minute for this key.
    pub read_per_min: i64,
    /// Trading calls a minute for this key.
    pub trade_per_min: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Through the MCP server (an AI assistant): how its trades are confirmed and its budget. Null for any other call.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MeAssistant {
    /// Each trade is shown first and placed only when repeated with its confirmation (preview, then confirm).
    pub confirm_trades: bool,
    /// How long a preview's confirmation holds, in seconds.
    pub confirm_seconds: i64,
    /// The AI assistant's own budget, on top of the others.
    pub budget: MeAssistantBudget,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The AI assistant's own budget, on top of the others.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MeAssistantBudget {
    /// Reads a minute.
    pub reads_per_min: i64,
    /// Trading calls a minute.
    pub writes_per_min: i64,
    /// Trading calls an hour.
    pub writes_per_hour: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// A new order.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct CreateOrder {
    /// A market id: the asset and USD.
    pub market: String,
    /// The order's side.
    pub side: OrderSide,
    /// market: now, at the best price within your worst price. limit: at your price or better. stop_market: a market order once the last price reaches the stop. stop_limit: a limit once the last price reaches the stop.
    pub r#type: CreateOrderType,
    /// The size, in the base asset: a multiple of the market's size step. Give size or notional_usd.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub size: Option<Decimal>,
    /// The size as a value in USD, rounded down to the size step.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub notional_usd: Option<Decimal>,
    /// The limit price (limit and stop_limit): a multiple of the tick size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit_price: Option<Decimal>,
    /// The trigger price (stop_market and stop_limit).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub stop_price: Option<Decimal>,
    /// Market orders: the worst price you accept. Give worst_price or max_slippage_pct.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub worst_price: Option<Decimal>,
    /// Market orders: how far from the best price you accept, in percent (the default and bounds are in GET /v1/markets).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_slippage_pct: Option<Decimal>,
    /// It may only reduce or close your position, never open or add to one.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub reduce_only: Option<bool>,
    /// Limit orders: refused if it would fill at once.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub post_only: Option<bool>,
    /// gtc: until canceled. gtd: until expires_at. ioc and fok: fill now or end (a fill is always the whole order). Default: ioc for market orders, gtc otherwise.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub time_in_force: Option<TimeInForce>,
    /// With gtd: when it ends, from orders.gtd_min_seconds to orders.gtd_max_days ahead (GET /v1/limits).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub expires_at: Option<i64>,
    /// Your own id for the order: unique in this environment, 1 to 64 letters, digits and . _ : -
    #[serde(skip_serializing_if = "Option::is_none")]
    pub client_order_id: Option<String>,
    /// The exits.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub exits: Option<OrderExits>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Exits placed when the order fills: the stop-loss for the filled size first, then the take-profits. From then on they are the position's exits. An empty object removes them.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct OrderExits {
    /// Limit orders on the profit side, each at price, or at distance or percent from the fill.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub take_profit: Option<Vec<OrderExitsTakeProfit>>,
    /// A stop on the loss side, at price, or at distance or percent from the fill; it may trail and move to break-even (as in a position's exits).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub stop_loss: Option<OrderExitsStopLoss>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// OrderExitsTakeProfit
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct OrderExitsTakeProfit {
    /// An exact price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub price: Option<Decimal>,
    /// A price difference from the fill price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub distance: Option<Decimal>,
    /// A percent of the fill price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub percent: Option<Decimal>,
    /// Its share of the filled size, in percent (absent: the take-profits split it evenly).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub size_pct: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// A stop on the loss side, at price, or at distance or percent from the fill; it may trail and move to break-even (as in a position's exits).
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct OrderExitsStopLoss {
    /// An exact price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub price: Option<Decimal>,
    /// A price difference from the fill price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub distance: Option<Decimal>,
    /// A percent of the fill price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub percent: Option<Decimal>,
    /// The stop's type (default stop_market).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub r#type: Option<OrderExitsStopLossType>,
    /// stop_limit: how many ticks past the stop its limit sits.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit_offset_ticks: Option<i64>,
    /// As a position's exits take it: { distance } or { percent }, with activation_price to start only once the price reaches it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trail: Option<OrderExitsStopLossTrail>,
    /// As a position's exits take it: one of trigger_distance, trigger_percent, trigger_price, and offset_ticks.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub break_even: Option<OrderExitsStopLossBreakEven>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// As a position's exits take it: { distance } or { percent }, with activation_price to start only once the price reaches it.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct OrderExitsStopLossTrail {
    /// How far behind the best price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub distance: Option<Decimal>,
    /// How far behind, in percent of the best price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub percent: Option<Decimal>,
    /// Start once the price reaches this.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub activation_price: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// As a position's exits take it: one of trigger_distance, trigger_percent, trigger_price, and offset_ticks.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct OrderExitsStopLossBreakEven {
    /// How far from the entry.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_distance: Option<Decimal>,
    /// How far from the entry, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_percent: Option<Decimal>,
    /// Or this price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_price: Option<Decimal>,
    /// Ticks beyond the entry, on the profit side.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub offset_ticks: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// A new strategy. trailing_stop and break_even act on the position's exits: the answer is that exits strategy.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct CreateStrategy {
    /// trailing_stop: a stop-loss that follows the price. break_even: the stop-losses move to the entry once the price has gone your way far enough. oco: two orders, the first fill cancels the other. twap, vwap: an order in market slices over time (vwap sized by yesterday's volume over the same hours). scaled: a ladder of limits. chase: a limit that follows the bid or ask.
    pub r#type: CreateStrategyType,
    /// A market id: the asset and USD.
    pub market: String,
    /// The order's side.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub side: Option<OrderSide>,
    /// trailing_stop: its size (absent: what the stop-losses leave of the position). twap, vwap, scaled, chase: the whole size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub size: Option<Decimal>,
    /// trailing_stop: how far behind the best price (or percent).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub distance: Option<Decimal>,
    /// trailing_stop: how far behind, in percent of the best price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub percent: Option<Decimal>,
    /// trailing_stop: start once the price reaches this (until then, initial_stop_price rests, or nothing).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub activation_price: Option<Decimal>,
    /// trailing_stop: where the stop rests until the trail moves it (absent: from the price now).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub initial_stop_price: Option<Decimal>,
    /// trailing_stop: cancel the position's other stop-losses first.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub replace_stops: Option<bool>,
    /// break_even: how far from the entry the price must go.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_distance: Option<Decimal>,
    /// break_even: the same, in percent of the entry.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_percent: Option<Decimal>,
    /// break_even: or once the price reaches this.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_price: Option<Decimal>,
    /// break_even: ticks beyond the entry, on the profit side. chase: ticks behind the bid (buy) or ask (sell).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub offset_ticks: Option<i64>,
    /// oco: the two orders.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub orders: Option<Vec<CreateStrategyOrder>>,
    /// The exits.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub exits: Option<OrderExits>,
    /// twap, vwap: how long the schedule runs (bounds in the runner settings, see GET /v1/limits).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub duration_minutes: Option<i64>,
    /// twap, vwap: time between slices; at most half the duration. Default: 60 (within those bounds).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub interval_seconds: Option<i64>,
    /// twap, vwap: vary each slice's time and size a little (the sizes still add up to size).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub randomize: Option<bool>,
    /// twap, vwap, scaled, chase: only reduce your position (a twap ends when the position is closed).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub reduce_only: Option<bool>,
    /// twap, vwap (buy): slices wait while the price is above this. chase (buy): its cap.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_price: Option<Decimal>,
    /// twap, vwap (sell): slices wait while the price is below this. chase (sell): its floor.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub min_price: Option<Decimal>,
    /// twap, vwap: each slice's slippage budget, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_slippage_pct: Option<Decimal>,
    /// scaled: the first rung.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub start_price: Option<Decimal>,
    /// scaled: the last rung.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub end_price: Option<Decimal>,
    /// scaled: how many limits (at most the runner setting).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub rungs: Option<i64>,
    /// scaled: equal sizes, growing toward end_price, or shrinking.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub distribution: Option<CreateStrategyDistribution>,
    /// scaled: each rung refused if it would fill at once.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub post_only: Option<bool>,
    /// scaled: one rung refused cancels the others.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub all_or_none: Option<bool>,
    /// scaled: rungs through the price may fill at once (refused otherwise, rung_would_fill).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub allow_marketable: Option<bool>,
    /// chase: how long it follows the price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_minutes: Option<i64>,
    /// chase: at max_minutes, cancel it, or send what is left at the market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub on_expiry: Option<CreateStrategyOnExpiry>,
    /// chase: it may rest through the last price (it would then fill at once).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub allow_cross: Option<bool>,
    /// Your own id for the strategy (oco, twap, vwap, scaled, chase): unique in this environment.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub client_strategy_id: Option<String>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// CreateStrategyOrder
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct CreateStrategyOrder {
    /// The order's side.
    pub side: OrderSide,
    /// Two orders that rest.
    pub r#type: CreateStrategyOrderType,
    /// Its size.
    pub size: Decimal,
    /// Its limit price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit_price: Option<Decimal>,
    /// Its stop price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub stop_price: Option<Decimal>,
    /// Refused if it would fill at once.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub post_only: Option<bool>,
    /// It may only reduce your position.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub reduce_only: Option<bool>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// What to change: at least one field. The order keeps its place in the engine (changed in place, never canceled and placed again).
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ModifyOrder {
    /// The new size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub size: Option<Decimal>,
    /// The new limit price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit_price: Option<Decimal>,
    /// The new trigger price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub stop_price: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// What to close.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ClosePosition {
    /// How much to close; absent: the whole position.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub size: Option<Decimal>,
    /// Your own id for the close order (a partial close).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub client_order_id: Option<String>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Optional: only this market.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct CancelAll {
    /// A market id: the asset and USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub market: Option<String>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The market's setting. The mode cannot change while a position or a working order is open on the market.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MarginSettingInput {
    /// isolated: the position has its own margin. cross: positions share the account's balance.
    pub margin_mode: MarginMode,
    /// A whole number from 1 to the market's max_leverage.
    pub leverage: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// MarginSettingResult
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct MarginSettingResult {
    /// A market id: the asset and USD.
    pub market: String,
    /// The margin mode.
    pub margin_mode: MarginMode,
    /// The leverage.
    pub leverage: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// WhatIf
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WhatIf {
    /// What the order would do now: fill at once, rest, or be refused (reject says why).
    pub result: WhatIfResult,
    /// The reject.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub reject: Option<WhatIfReject>,
    /// A market id: the asset and USD.
    pub market: String,
    /// The order's side.
    pub side: OrderSide,
    /// The order's type.
    pub r#type: CreateOrderType,
    /// Its size (after a notional_usd is rounded to the size step).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub size: Option<Decimal>,
    /// The execution.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub execution: Option<WhatIfExecution>,
    /// Its value in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub notional: Option<Decimal>,
    /// The fee its fill would pay.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub fee: Option<Decimal>,
    /// The margin.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub margin: Option<WhatIfMargin>,
    /// The position after.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub position_after: Option<WhatIfPositionAfter>,
    /// The exposure.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub exposure: Option<WhatIfExposure>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// WhatIfReject
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WhatIfReject {
    /// The error code it would get.
    pub code: String,
    /// What it means.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub message: Option<String>,
    /// More about it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub details: Option<Value>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// WhatIfExecution
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WhatIfExecution {
    /// It would fill at once.
    pub marketable: bool,
    /// The price it would fill at (at once) or rest at.
    pub expected_price: Decimal,
    /// A market order's worst price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub worst_price: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// WhatIfMargin
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WhatIfMargin {
    /// The margin mode the position would use.
    pub mode: WhatIfMarginMode,
    /// The leverage it would use.
    pub leverage: i64,
    /// The margin the order needs.
    pub required: Decimal,
    /// The margin available now.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub available_before: Option<Decimal>,
    /// What would remain available.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub available_after: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// WhatIfPositionAfter
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WhatIfPositionAfter {
    /// The position's size after the fill.
    pub size: Decimal,
    /// Its side; null when flat.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub side: Option<PositionSide>,
    /// Its average price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub average_price: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// WhatIfExposure
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WhatIfExposure {
    /// The market's exposure cap across your accounts of this product.
    pub cap: Decimal,
    /// Your open value on the market now.
    pub open_before: Decimal,
    /// What would remain under the cap.
    pub room_after: Decimal,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Event
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Event {
    /// The event; pass it as after (GET /v1/events) or resume (the stream) to continue from it.
    pub id: String,
    /// What happened (GET /v1/webhook-event-types says what each type means).
    pub r#type: EventType,
    /// The account number.
    pub account_id: String,
    /// When it happened.
    pub time: i64,
    /// Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert.
    pub source: EventSource,
    /// order or fill (the objects REST answers), with changes or close_reason when they apply. A position.* event carries position { market, side, size, average_price } as the engine reported it, and previous_size: read GET .../positions/{market} for its margin, P&L and liquidation.
    pub data: Map<String, Value>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// What a json webhook endpoint receives: the event as GET /v1/events shows it, with the endpoint's API version and environment.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WebhookPayload {
    /// The event; pass it as after (GET /v1/events) or resume (the stream) to continue from it.
    pub id: String,
    /// What happened; webhook.ping is a test.
    pub r#type: WebhookPayloadType,
    /// The account number (none on webhook.ping).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub account_id: Option<String>,
    /// When it happened.
    pub time: i64,
    /// Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert.
    pub source: EventSource,
    /// order or fill (the objects REST answers), with changes or close_reason when they apply. A position.* event carries position { market, side, size, average_price } as the engine reported it, and previous_size: read GET .../positions/{market} for its margin, P&L and liquidation.
    pub data: Map<String, Value>,
    /// The API version the endpoint receives.
    pub api_version: String,
    /// The environment of the endpoint and the account.
    pub environment: PlatformEnvironment,
    /// A test (webhook.ping) or a delivery resent as one.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub test: Option<bool>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ExitsLeg
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitsLeg {
    /// The leg: an order of this API, also listed under the account's orders.
    pub id: String,
    /// take_profit: a limit on the profit side. stop_loss: a stop on the loss side.
    pub group: ExitsLegGroup,
    /// take_profit legs are limits; stop_loss legs are stop_market or stop_limit.
    pub r#type: CreateStrategyOrderType,
    /// The order's status; waiting: a stop-loss not placed yet (it waits for its activation price or its break-even).
    pub status: ExitsLegStatus,
    /// Its size.
    pub size: Decimal,
    /// The take-profit price, or the stop price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub price: Option<Decimal>,
    /// The limit of a stop_limit leg.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit_price: Option<Decimal>,
    /// A trailing stop-loss: how it follows the price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trail: Option<ExitsLegTrail>,
    /// A stop-loss moved to the entry price once the price has gone your way far enough.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub break_even: Option<ExitsLegBreakEven>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// A trailing stop-loss: how it follows the price.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitsLegTrail {
    /// How far behind the best price (or null: percent).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub distance: Option<Decimal>,
    /// How far behind, in percent of the best price (or null: distance).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub percent: Option<Decimal>,
    /// It starts once the price reaches this; null: from the start.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub activation_price: Option<Decimal>,
    /// It follows the price now (its activation price was reached, or it had none).
    pub active: bool,
    /// The best price since it started, which the stop follows.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub best_price: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// A stop-loss moved to the entry price once the price has gone your way far enough.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitsLegBreakEven {
    /// How far from the entry the price must go (or null: another trigger).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_distance: Option<Decimal>,
    /// The same, in percent of the entry.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_percent: Option<Decimal>,
    /// Or once the price reaches this.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_price: Option<Decimal>,
    /// Ticks beyond the entry, on the profit side.
    pub offset_ticks: i64,
    /// The stop-loss was moved to the entry already.
    pub done: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Exits
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Exits {
    /// The account number.
    pub account_id: String,
    /// A market id: the asset and USD.
    pub market: String,
    /// The strategy that keeps these exits (type exits); null when there are none.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub strategy_id: Option<String>,
    /// The open position; null when flat.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub position: Option<ExitsPosition>,
    /// The take-profit legs: together at most the position.
    pub take_profit: Vec<ExitsLeg>,
    /// The stop-loss legs: together at most the position.
    pub stop_loss: Vec<ExitsLeg>,
    /// The legs grow with the position, keeping their share of it (rounded down to the size step).
    pub resize_with_position: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The open position; null when flat.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitsPosition {
    /// Long or short.
    pub side: PositionSide,
    /// Its size.
    pub size: Decimal,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The take-profit and stop-loss legs of a position, changed together.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitsBatch {
    /// The position you saw: { side, size }, or null.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub expected_position: Option<ExitsBatchExpectedPosition>,
    /// Every leg you saw (an empty list when there were none): if anything differs now, nothing is done and the answer is 409 exits_changed with the state now.
    pub expected_exits: Vec<ExitsBatchExpectedExit>,
    /// Applied in this order: cancels, changes, then new legs (stop-losses first). An engine refusal stops the batch: 422 exit_operation_failed says what each operation did.
    pub operations: Vec<ExitsBatchOperation>,
    /// The legs grow with the position (default true).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub resize_with_position: Option<bool>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The position you saw: { side, size }, or null.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitsBatchExpectedPosition {
    /// Long or short.
    pub side: PositionSide,
    /// Its size.
    pub size: Decimal,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ExitsBatchExpectedExit
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitsBatchExpectedExit {
    /// A leg.
    pub id: String,
    /// Its size.
    pub size: Decimal,
    /// Its price; null for a stop-loss that waits (as GET exits shows it).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub price: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// ExitsBatchOperation
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitsBatchOperation {
    /// What to do.
    pub op: ExitsBatchOperationOp,
    /// modify, cancel: the leg.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub id: Option<String>,
    /// place: the group of the new leg.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub group: Option<ExitsLegGroup>,
    /// place: limit for a take-profit (the default); stop_market (the default) or stop_limit for a stop-loss.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub r#type: Option<CreateStrategyOrderType>,
    /// The take-profit price, or the stop price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub price: Option<Decimal>,
    /// The limit of a stop_limit leg.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit_price: Option<Decimal>,
    /// The size; absent on a place: what the group leaves of the position.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub size: Option<Decimal>,
    /// A stop-loss that follows the price: { distance } (a price difference) or { percent }, with activation_price to start only once the price reaches it. Without a price and an activation price it starts from the price now. null removes it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trail: Option<ExitsBatchOperationTrail>,
    /// Move the stop-loss to the entry price (plus offset_ticks) once the price has gone your way by trigger_distance, trigger_percent or to trigger_price. Without a price, the stop is placed only then. null removes it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub break_even: Option<ExitsBatchOperationBreakEven>,
    /// modify: the settings to remove from a stop-loss (the same as sending them null).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub clear: Option<Vec<ExitsBatchOperationClear>>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// A stop-loss that follows the price: { distance } (a price difference) or { percent }, with activation_price to start only once the price reaches it. Without a price and an activation price it starts from the price now. null removes it.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitsBatchOperationTrail {
    /// How far behind the best price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub distance: Option<Decimal>,
    /// How far behind, in percent of the best price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub percent: Option<Decimal>,
    /// Start once the price reaches this.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub activation_price: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Move the stop-loss to the entry price (plus offset_ticks) once the price has gone your way by trigger_distance, trigger_percent or to trigger_price. Without a price, the stop is placed only then. null removes it.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct ExitsBatchOperationBreakEven {
    /// How far from the entry.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_distance: Option<Decimal>,
    /// How far from the entry, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_percent: Option<Decimal>,
    /// Or this price.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub trigger_price: Option<Decimal>,
    /// Ticks beyond the entry, on the profit side.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub offset_ticks: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// Strategy
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct Strategy {
    /// The strategy.
    pub id: String,
    /// Your own id for it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub client_strategy_id: Option<String>,
    /// The account number.
    pub account_id: String,
    /// exits: a position's take-profit and stop-loss legs. bracket: an order with exits once it fills. oco: two orders, the first fill cancels the other. twap, vwap: an order in slices over time. scaled: a ladder of limits. chase: a limit that follows the price.
    pub r#type: StrategyType,
    /// A market id: the asset and USD.
    pub market: String,
    /// The side of its orders (exits: the closing side).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub side: Option<OrderSide>,
    /// waiting: armed, nothing placed yet. working: running. completed: done as asked. canceled: you ended it. ended: it stopped for end_reason.
    pub status: StrategyStatus,
    /// Why it stopped.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub end_reason: Option<StrategyEndReason>,
    /// What it was created with.
    pub params: Map<String, Value>,
    /// How far it is (by type).
    pub progress: Map<String, Value>,
    /// Every order it placed, oldest first.
    pub orders: Vec<Order>,
    /// When it was created.
    pub created_at: i64,
    /// Its latest change.
    pub updated_at: i64,
    /// When it stopped.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub ended_at: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// WebhookEndpoint
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WebhookEndpoint {
    /// The endpoint.
    pub id: String,
    /// Its address: the host and the start of the path (never whole: chat addresses carry tokens).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub url: Option<String>,
    /// json: the signed event. discord, slack, telegram: a chat message.
    pub format: WebhookEndpointFormat,
    /// The groups it receives (see GET /v1/webhook-event-types).
    pub events: Vec<WebhookEndpointEvent>,
    /// disabled: we stopped after failures in a row; turn it on again from your dashboard.
    pub status: WebhookEndpointStatus,
    /// The version its events are written in.
    pub api_version: String,
    /// Its environment: it receives that environment's events only.
    pub environment: PlatformEnvironment,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// WebhookDelivery
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WebhookDelivery {
    /// The delivery.
    pub id: String,
    /// Its endpoint.
    pub endpoint_id: String,
    /// The event sent (the same id when it is sent again).
    pub event_id: String,
    /// The event's type.
    pub event_type: String,
    /// Its account.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub account_id: Option<String>,
    /// A test.
    pub test: bool,
    /// pending: waiting for its next try. failed: every try failed. canceled: the endpoint was disabled or deleted.
    pub status: WebhookDeliveryStatus,
    /// Every try, oldest first.
    pub attempts: Vec<WebhookDeliveryAttempt>,
    /// When it is tried again.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub next_attempt_at: Option<i64>,
    /// When it was queued.
    pub created_at: i64,
    /// When your endpoint took it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub delivered_at: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// WebhookDeliveryAttempt
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WebhookDeliveryAttempt {
    /// Which try.
    pub attempt: i64,
    /// When.
    pub at: i64,
    /// What your endpoint answered; null when it could not be reached.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub http_status: Option<i64>,
    /// Why it failed, when it did (timeout, redirect, private_address, http_500, ...).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub error: Option<String>,
    /// How long it took.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub duration_ms: Option<i64>,
    /// The start of the answer (up to 512 bytes).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub response: Option<String>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// WebhookEventType
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WebhookEventType {
    /// The type.
    pub r#type: String,
    /// The group that receives it; null: every endpoint (the test).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub group: Option<WebhookEventTypeGroup>,
    /// What it means.
    pub description: String,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// DeletedEndpoint
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct DeletedEndpoint {
    /// The endpoint.
    pub id: String,
    /// It was deleted.
    pub deleted: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// TradingViewHook
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingViewHook {
    /// The link.
    pub id: String,
    /// Its name.
    pub name: String,
    /// The account number.
    pub account_id: String,
    /// Its environment.
    pub environment: PlatformEnvironment,
    /// paused: alerts are refused (link_paused).
    pub status: TradingViewHookStatus,
    /// The markets it may trade; empty: every market.
    pub markets: Vec<String>,
    /// It may only reduce positions.
    pub reduce_only: bool,
    /// Its largest order, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_order_usd: Option<Decimal>,
    /// Its largest position, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_position_usd: Option<Decimal>,
    /// Its daily loss stop: no new position once today's loss reaches it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub daily_loss_usd: Option<Decimal>,
    /// The sizing.
    pub sizing: TradingViewHookSizing,
    /// signal: an opposite alert reverses, a repeat is skipped. add: a repeat adds. target: the strategy's position size is followed.
    pub position_mode: TradingViewHookPositionMode,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// TradingViewHookSizing
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingViewHookSizing {
    /// from_alert: the size the alert gives. qty, usd, pct_balance: the link's own.
    pub mode: TradingViewHookSizingMode,
    /// The link's size, in its mode's unit.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub value: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// TradingViewAlert
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingViewAlert {
    /// The alert.
    pub id: String,
    /// Its link.
    pub hook_id: String,
    /// The account number.
    pub account_id: String,
    /// What it did.
    pub status: TradingViewAlertStatus,
    /// Why it placed nothing (or not all): a reason code.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub reason: Option<String>,
    /// That reason in words.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub reason_text: Option<String>,
    /// How the message was read: universal, pickmytrade, traderspost, pineconnector, text.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub format: Option<String>,
    /// The market it named.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub market: Option<String>,
    /// Its action.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub action: Option<String>,
    /// The message as kept (credentials masked).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub message: Option<String>,
    /// The sender's address, its end masked.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub sender: Option<String>,
    /// What it was to do: { legs } or { skip } or { refuse }.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub plan: Option<Map<String, Value>>,
    /// What each leg did.
    pub results: Vec<Map<String, Value>>,
    /// More about a refusal.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub details: Option<Map<String, Value>>,
    /// The alert this one repeats.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub duplicate_of: Option<String>,
    /// Refusals folded into this row (rate limits).
    pub repeat_count: i64,
    /// When it came.
    pub received_at: i64,
    /// When it was done.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub finished_at: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// A message to read, sent nowhere.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingViewParseInput {
    /// The alert's message, as TradingView would send it.
    pub message: String,
    /// A link: the plan is then worked out on its account and settings, as of now.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub hook_id: Option<String>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// TradingViewParse
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingViewParse {
    /// It reads, and names a market we offer.
    pub ok: bool,
    /// Why not, when it does not.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub code: Option<String>,
    /// The field at fault.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub field: Option<String>,
    /// What went wrong.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub message: Option<String>,
    /// The message as read: action, side, sizes, prices, exits.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub signal: Option<Map<String, Value>>,
    /// The market it names (ALL for every market).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub market: Option<String>,
    /// The market meant, when the ticker names one we offer only scaled.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub suggestion: Option<String>,
    /// With hook_id: what the alert would do now ({ legs } / { skip } / { refuse }).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub plan: Option<Map<String, Value>>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// DeletedHook
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct DeletedHook {
    /// The link.
    pub id: String,
    /// It was revoked: its address answers 404 from now on.
    pub deleted: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// CancelAllResult
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct CancelAllResult {
    /// The account number.
    pub account_id: String,
    /// The market, or null for every market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub market: Option<String>,
    /// How many working orders were canceled.
    pub canceled: i64,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// FlattenResult
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct FlattenResult {
    /// The account number.
    pub account_id: String,
    /// How many working orders were canceled.
    pub orders_canceled: i64,
    /// The orders that closed the positions.
    pub closes: Vec<Order>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The live risk; null while the account is being opened.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskOrNull {
    /// When the oldest figure here was read.
    pub as_of: i64,
    /// Some figures are older than usual (the engine was busy).
    pub stale: bool,
    /// The balance.
    pub balance: Decimal,
    /// The balance plus the open P&L at the market price.
    pub equity: Decimal,
    /// The open P&L.
    pub unrealized_pnl: Decimal,
    /// The equity change since midnight New York, open positions included: what the daily loss rule counts.
    pub pnl_today: Decimal,
    /// The P&L closed today.
    pub realized_today: Decimal,
    /// The fees paid today.
    pub fees_today: Decimal,
    /// Trades closed today.
    pub trades_today: i64,
    /// The margin your positions hold.
    pub margin_used: Decimal,
    /// What is left of the balance for new positions.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub margin_available: Option<Decimal>,
    /// The exposure.
    pub exposure: AccountRiskOrNullExposure,
    /// The daily loss.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub daily_loss: Option<Limit>,
    /// The max drawdown.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub max_drawdown: Option<Limit>,
    /// The profit target.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub profit_target: Option<AccountRiskOrNullProfitTarget>,
    /// The trading days.
    pub trading_days: AccountRiskOrNullTradingDays,
    /// The nearest liquidation.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub nearest_liquidation: Option<AccountRiskOrNullNearestLiquidation>,
    /// When the trading day ends (midnight New York).
    pub trading_day_ends_at: i64,
    /// What needs your attention, most serious first.
    pub warnings: Vec<Warning>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRiskOrNullExposure
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskOrNullExposure {
    /// The value of every open position.
    pub gross: Decimal,
    /// The value of the long ones.
    pub long: Decimal,
    /// The value of the short ones.
    pub short: Decimal,
    /// Gross exposure over equity.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub effective_leverage: Option<Decimal>,
    /// Per market, largest first.
    pub markets: Vec<AccountRiskOrNullExposureMarket>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRiskOrNullExposureMarket
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskOrNullExposureMarket {
    /// A market id: the asset and USD.
    pub market: String,
    /// The value held on it.
    pub notional: Decimal,
    /// That value over equity.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub leverage: Option<Decimal>,
    /// The exposure cap of the market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cap: Option<Decimal>,
    /// How much of the cap is used, counting your other accounts on the same rule, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cap_used_pct: Option<Decimal>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRiskOrNullProfitTarget
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskOrNullProfitTarget {
    /// The balance that reaches it.
    pub goal_balance: Decimal,
    /// Still to make.
    pub remaining: Decimal,
    /// How far, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub progress_pct: Option<Decimal>,
    /// Positions must be closed when it is reached.
    pub requires_flat: bool,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRiskOrNullTradingDays
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskOrNullTradingDays {
    /// Days with a trade so far.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub count: Option<i64>,
    /// Days the rule needs.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub required: Option<i64>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountRiskOrNullNearestLiquidation
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountRiskOrNullNearestLiquidation {
    /// A market id: the asset and USD.
    pub market: String,
    /// Its liquidation price.
    pub price: Decimal,
    /// How far the price is from it, in percent.
    pub distance_pct: Decimal,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// AccountDetail
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct AccountDetail {
    /// The account number.
    pub id: String,
    /// The environment the account lives in.
    pub environment: PlatformEnvironment,
    /// Where the account is in the programme.
    pub stage: AccountStage,
    /// pending: being opened; active: trading.
    pub status: AccountStatus,
    /// The product it was bought as.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub product: Option<String>,
    /// Its size as sold.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub tier: Option<String>,
    /// The balance it started with.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub start_balance: Option<Decimal>,
    /// The balance: closed trades and fees only.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub balance: Option<Decimal>,
    /// The rule.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub rule: Option<Rule>,
    /// A payout may be requested now.
    pub payout_eligible: bool,
    /// Sandbox only: parked after a while unused; it wakes the moment you use it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub parked: Option<bool>,
    /// When the account was made.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub created_at: Option<i64>,
    /// The risk.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub risk: Option<AccountRiskOrNull>,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// LimitStanding
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct LimitStanding {
    /// The limit, in USD.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit: Option<Decimal>,
    /// The equity at which it is breached.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub floor: Option<Decimal>,
    /// How much more the account may lose before it.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub room: Option<Decimal>,
    /// How much of it is used, in percent.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub used_pct: Option<Decimal>,
    /// Daily loss only: when it resets (midnight New York).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub resets_at: Option<i64>,
    /// Daily loss only: what happens when it is reached.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub on_breach: Option<OnBreach>,
    /// Daily loss only: the day's start is estimated (the engine does not publish it).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub estimated: Option<bool>,
    /// Max drawdown only: how the reference moves.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub mode: Option<RuleMaxDrawdownMode>,
    /// Max drawdown only: the engine may hold a higher floor than shown (it follows equity highs).
    #[serde(skip_serializing_if = "Option::is_none")]
    pub at_least: Option<bool>,
    /// The rule no longer applies this limit (its cut-off was reached).
    pub lifted: bool,
    /// Where it stands.
    pub status: LimitStandingStatus,
    /// Fields this SDK version does not know yet (a newer API's), kept as they came.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
}

/// The query of get the equity curve.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct HistoryEquityParams {
    /// One point per closed trade, or per trading day.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub resolution: Option<Resolution>,
}

impl HistoryEquityParams {
    pub(crate) fn to_query(&self) -> Vec<(String, String)> {
        let mut q = Vec::new();
        if let Some(v) = &self.resolution {
            q.push(("resolution".to_string(), v.as_str().to_string()));
        }
        q
    }
}

/// The query of list open positions.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct PositionsListParams {
    /// Only this market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub market: Option<String>,
}

impl PositionsListParams {
    pub(crate) fn to_query(&self) -> Vec<(String, String)> {
        let mut q = Vec::new();
        if let Some(v) = &self.market {
            q.push(("market".to_string(), v.to_string()));
        }
        q
    }
}

/// The query of list closed positions.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct PositionsListClosedParams {
    /// Only this market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub market: Option<String>,
    /// Page size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit: Option<i64>,
    /// next_cursor from the previous page, with the same filters.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cursor: Option<String>,
}

impl PositionsListClosedParams {
    pub(crate) fn to_query(&self) -> Vec<(String, String)> {
        let mut q = Vec::new();
        if let Some(v) = &self.market {
            q.push(("market".to_string(), v.to_string()));
        }
        if let Some(v) = &self.limit {
            q.push(("limit".to_string(), v.to_string()));
        }
        if let Some(v) = &self.cursor {
            q.push(("cursor".to_string(), v.to_string()));
        }
        q
    }
}

/// The query of list orders.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct OrdersListParams {
    /// working: the complete set of working orders. history: past orders. all: both.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub status: Option<OrdersListParamsStatus>,
    /// Only this market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub market: Option<String>,
    /// The order you placed through the API with this id.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub client_order_id: Option<String>,
    /// Page size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit: Option<i64>,
    /// next_cursor from the previous page, with the same filters.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cursor: Option<String>,
}

impl OrdersListParams {
    pub(crate) fn to_query(&self) -> Vec<(String, String)> {
        let mut q = Vec::new();
        if let Some(v) = &self.status {
            q.push(("status".to_string(), v.as_str().to_string()));
        }
        if let Some(v) = &self.market {
            q.push(("market".to_string(), v.to_string()));
        }
        if let Some(v) = &self.client_order_id {
            q.push(("client_order_id".to_string(), v.to_string()));
        }
        if let Some(v) = &self.limit {
            q.push(("limit".to_string(), v.to_string()));
        }
        if let Some(v) = &self.cursor {
            q.push(("cursor".to_string(), v.to_string()));
        }
        q
    }
}

/// The query of list events.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct EventsListParams {
    /// Only this account.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub account_id: Option<String>,
    /// The id of the last event you have: the ones after it, oldest first. Absent: from the oldest kept.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub after: Option<String>,
    /// Page size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit: Option<i64>,
}

impl EventsListParams {
    pub(crate) fn to_query(&self) -> Vec<(String, String)> {
        let mut q = Vec::new();
        if let Some(v) = &self.account_id {
            q.push(("account_id".to_string(), v.to_string()));
        }
        if let Some(v) = &self.after {
            q.push(("after".to_string(), v.to_string()));
        }
        if let Some(v) = &self.limit {
            q.push(("limit".to_string(), v.to_string()));
        }
        q
    }
}

/// The query of list strategies.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct StrategiesListParams {
    /// active: waiting or working. ended: completed, canceled or ended. all: both.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub status: Option<StrategiesListParamsStatus>,
    /// Only this market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub market: Option<String>,
    /// The strategy you created with this id.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub client_strategy_id: Option<String>,
    /// Page size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit: Option<i64>,
    /// next_cursor from the previous page, with the same filters.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cursor: Option<String>,
}

impl StrategiesListParams {
    pub(crate) fn to_query(&self) -> Vec<(String, String)> {
        let mut q = Vec::new();
        if let Some(v) = &self.status {
            q.push(("status".to_string(), v.as_str().to_string()));
        }
        if let Some(v) = &self.market {
            q.push(("market".to_string(), v.to_string()));
        }
        if let Some(v) = &self.client_strategy_id {
            q.push(("client_strategy_id".to_string(), v.to_string()));
        }
        if let Some(v) = &self.limit {
            q.push(("limit".to_string(), v.to_string()));
        }
        if let Some(v) = &self.cursor {
            q.push(("cursor".to_string(), v.to_string()));
        }
        q
    }
}

/// The query of list deliveries.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct WebhookEndpointsListDeliveriesParams {
    /// Page size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit: Option<i64>,
    /// next_cursor from the previous page, with the same filters.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cursor: Option<String>,
}

impl WebhookEndpointsListDeliveriesParams {
    pub(crate) fn to_query(&self) -> Vec<(String, String)> {
        let mut q = Vec::new();
        if let Some(v) = &self.limit {
            q.push(("limit".to_string(), v.to_string()));
        }
        if let Some(v) = &self.cursor {
            q.push(("cursor".to_string(), v.to_string()));
        }
        q
    }
}

/// The query of list a link's alerts.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct TradingviewListAlertsParams {
    /// Only alerts with this status.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub status: Option<String>,
    /// Page size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit: Option<i64>,
    /// next_cursor from the previous page, with the same filters.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cursor: Option<String>,
}

impl TradingviewListAlertsParams {
    pub(crate) fn to_query(&self) -> Vec<(String, String)> {
        let mut q = Vec::new();
        if let Some(v) = &self.status {
            q.push(("status".to_string(), v.to_string()));
        }
        if let Some(v) = &self.limit {
            q.push(("limit".to_string(), v.to_string()));
        }
        if let Some(v) = &self.cursor {
            q.push(("cursor".to_string(), v.to_string()));
        }
        q
    }
}

/// The query of list fills.
#[derive(Clone, Debug, Default, PartialEq, Serialize, Deserialize)]
#[serde(default)]
pub struct FillsListParams {
    /// Only this market.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub market: Option<String>,
    /// Page size.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub limit: Option<i64>,
    /// next_cursor from the previous page, with the same filters.
    #[serde(skip_serializing_if = "Option::is_none")]
    pub cursor: Option<String>,
}

impl FillsListParams {
    pub(crate) fn to_query(&self) -> Vec<(String, String)> {
        let mut q = Vec::new();
        if let Some(v) = &self.market {
            q.push(("market".to_string(), v.to_string()));
        }
        if let Some(v) = &self.limit {
            q.push(("limit".to_string(), v.to_string()));
        }
        if let Some(v) = &self.cursor {
            q.push(("cursor".to_string(), v.to_string()));
        }
        q
    }
}
