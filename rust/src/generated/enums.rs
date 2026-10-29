// Generated from the API spec by generator/printers/rust.js. Do not edit: run `node generator`.

use serde::{Deserialize, Deserializer, Serialize, Serializer};

/// The family of the error; it decides the HTTP status. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum ErrorEnvelopeErrorType {
    /// `invalid_request`
    InvalidRequest,
    /// `authentication`
    Authentication,
    /// `permission`
    Permission,
    /// `not_found`
    NotFound,
    /// `method`
    Method,
    /// `conflict`
    Conflict,
    /// `payload`
    Payload,
    /// `rule_rejection`
    RuleRejection,
    /// `key_limit`
    KeyLimit,
    /// `rate_limit`
    RateLimit,
    /// `server`
    Server,
    /// `engine`
    Engine,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl ErrorEnvelopeErrorType {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::InvalidRequest => "invalid_request",
            Self::Authentication => "authentication",
            Self::Permission => "permission",
            Self::NotFound => "not_found",
            Self::Method => "method",
            Self::Conflict => "conflict",
            Self::Payload => "payload",
            Self::RuleRejection => "rule_rejection",
            Self::KeyLimit => "key_limit",
            Self::RateLimit => "rate_limit",
            Self::Server => "server",
            Self::Engine => "engine",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for ErrorEnvelopeErrorType {
    fn from(s: &str) -> Self {
        match s {
            "invalid_request" => Self::InvalidRequest,
            "authentication" => Self::Authentication,
            "permission" => Self::Permission,
            "not_found" => Self::NotFound,
            "method" => Self::Method,
            "conflict" => Self::Conflict,
            "payload" => Self::Payload,
            "rule_rejection" => Self::RuleRejection,
            "key_limit" => Self::KeyLimit,
            "rate_limit" => Self::RateLimit,
            "server" => Self::Server,
            "engine" => Self::Engine,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for ErrorEnvelopeErrorType {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for ErrorEnvelopeErrorType {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for ErrorEnvelopeErrorType {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for ErrorEnvelopeErrorType {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// The environment of the host you called. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum PlatformEnvironment {
    /// `live`
    Live,
    /// `sandbox`
    Sandbox,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl PlatformEnvironment {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Live => "live",
            Self::Sandbox => "sandbox",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for PlatformEnvironment {
    fn from(s: &str) -> Self {
        match s {
            "live" => Self::Live,
            "sandbox" => Self::Sandbox,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for PlatformEnvironment {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for PlatformEnvironment {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for PlatformEnvironment {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for PlatformEnvironment {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Whether the trading engine is reachable. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum PlatformStatusEngine {
    /// `up`
    Up,
    /// `down`
    Down,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl PlatformStatusEngine {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Up => "up",
            Self::Down => "down",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for PlatformStatusEngine {
    fn from(s: &str) -> Self {
        match s {
            "up" => Self::Up,
            "down" => Self::Down,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for PlatformStatusEngine {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for PlatformStatusEngine {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for PlatformStatusEngine {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for PlatformStatusEngine {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Whether the market-data stream is live, with every price feed. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum PlatformStatusMarketData {
    /// `up`
    Up,
    /// `down`
    Down,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl PlatformStatusMarketData {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Up => "up",
            Self::Down => "down",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for PlatformStatusMarketData {
    fn from(s: &str) -> Self {
        match s {
            "up" => Self::Up,
            "down" => Self::Down,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for PlatformStatusMarketData {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for PlatformStatusMarketData {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for PlatformStatusMarketData {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for PlatformStatusMarketData {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Whether outbound webhooks are being delivered. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum PlatformStatusWebhooks {
    /// `up`
    Up,
    /// `down`
    Down,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl PlatformStatusWebhooks {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Up => "up",
            Self::Down => "down",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for PlatformStatusWebhooks {
    fn from(s: &str) -> Self {
        match s {
            "up" => Self::Up,
            "down" => Self::Down,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for PlatformStatusWebhooks {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for PlatformStatusWebhooks {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for PlatformStatusWebhooks {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for PlatformStatusWebhooks {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Whether this environment takes requests. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum StatusApi {
    /// `operational`
    Operational,
    /// `disabled`
    Disabled,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl StatusApi {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Operational => "operational",
            Self::Disabled => "disabled",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for StatusApi {
    fn from(s: &str) -> Self {
        match s {
            "operational" => Self::Operational,
            "disabled" => Self::Disabled,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for StatusApi {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for StatusApi {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for StatusApi {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for StatusApi {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Whether the market trades now. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum MarketStatus {
    /// `open`
    Open,
    /// `halted`
    Halted,
    /// `closed`
    Closed,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl MarketStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Open => "open",
            Self::Halted => "halted",
            Self::Closed => "closed",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for MarketStatus {
    fn from(s: &str) -> Self {
        match s {
            "open" => Self::Open,
            "halted" => Self::Halted,
            "closed" => Self::Closed,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for MarketStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for MarketStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for MarketStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for MarketStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// When it trades: around the clock, its underlying market's sessions (Standard Market Hours, closed on weekends), or FX hours. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum TradingHours {
    /// `24/7`
    V24_7,
    /// `standard`
    Standard,
    /// `fx`
    Fx,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl TradingHours {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::V24_7 => "24/7",
            Self::Standard => "standard",
            Self::Fx => "fx",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for TradingHours {
    fn from(s: &str) -> Self {
        match s {
            "24/7" => Self::V24_7,
            "standard" => Self::Standard,
            "fx" => Self::Fx,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for TradingHours {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for TradingHours {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for TradingHours {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for TradingHours {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// What the day's loss is measured from. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum RuleDailyLossBasis {
    /// `peak_balance`
    PeakBalance,
    /// `start_balance`
    StartBalance,
    /// `peak_equity`
    PeakEquity,
    /// `day_start_balance`
    DayStartBalance,
    /// `day_start_equity`
    DayStartEquity,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl RuleDailyLossBasis {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::PeakBalance => "peak_balance",
            Self::StartBalance => "start_balance",
            Self::PeakEquity => "peak_equity",
            Self::DayStartBalance => "day_start_balance",
            Self::DayStartEquity => "day_start_equity",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for RuleDailyLossBasis {
    fn from(s: &str) -> Self {
        match s {
            "peak_balance" => Self::PeakBalance,
            "start_balance" => Self::StartBalance,
            "peak_equity" => Self::PeakEquity,
            "day_start_balance" => Self::DayStartBalance,
            "day_start_equity" => Self::DayStartEquity,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for RuleDailyLossBasis {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for RuleDailyLossBasis {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for RuleDailyLossBasis {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for RuleDailyLossBasis {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// What happens when it is reached. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum OnBreach {
    /// `fail_account`
    FailAccount,
    /// `flatten`
    Flatten,
    /// `flatten_and_pause`
    FlattenAndPause,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl OnBreach {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::FailAccount => "fail_account",
            Self::Flatten => "flatten",
            Self::FlattenAndPause => "flatten_and_pause",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for OnBreach {
    fn from(s: &str) -> Self {
        match s {
            "fail_account" => Self::FailAccount,
            "flatten" => Self::Flatten,
            "flatten_and_pause" => Self::FlattenAndPause,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for OnBreach {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for OnBreach {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for OnBreach {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for OnBreach {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// How the reference moves: end_of_day and end_of_trade follow the highest balance, static stays at the start, intraday_equity follows the highest equity. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum RuleMaxDrawdownMode {
    /// `end_of_day`
    EndOfDay,
    /// `end_of_trade`
    EndOfTrade,
    /// `static`
    Static,
    /// `intraday_equity`
    IntradayEquity,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl RuleMaxDrawdownMode {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::EndOfDay => "end_of_day",
            Self::EndOfTrade => "end_of_trade",
            Self::Static => "static",
            Self::IntradayEquity => "intraday_equity",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for RuleMaxDrawdownMode {
    fn from(s: &str) -> Self {
        match s {
            "end_of_day" => Self::EndOfDay,
            "end_of_trade" => Self::EndOfTrade,
            "static" => Self::Static,
            "intraday_equity" => Self::IntradayEquity,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for RuleMaxDrawdownMode {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for RuleMaxDrawdownMode {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for RuleMaxDrawdownMode {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for RuleMaxDrawdownMode {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Where the account is in the programme. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum AccountStage {
    /// `evaluation`
    Evaluation,
    /// `funded`
    Funded,
    /// `sandbox`
    Sandbox,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl AccountStage {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Evaluation => "evaluation",
            Self::Funded => "funded",
            Self::Sandbox => "sandbox",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for AccountStage {
    fn from(s: &str) -> Self {
        match s {
            "evaluation" => Self::Evaluation,
            "funded" => Self::Funded,
            "sandbox" => Self::Sandbox,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for AccountStage {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for AccountStage {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for AccountStage {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for AccountStage {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// pending: being opened; active: trading. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum AccountStatus {
    /// `pending`
    Pending,
    /// `active`
    Active,
    /// `passed`
    Passed,
    /// `failed`
    Failed,
    /// `closed`
    Closed,
    /// `suspended`
    Suspended,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl AccountStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Pending => "pending",
            Self::Active => "active",
            Self::Passed => "passed",
            Self::Failed => "failed",
            Self::Closed => "closed",
            Self::Suspended => "suspended",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for AccountStatus {
    fn from(s: &str) -> Self {
        match s {
            "pending" => Self::Pending,
            "active" => Self::Active,
            "passed" => Self::Passed,
            "failed" => Self::Failed,
            "closed" => Self::Closed,
            "suspended" => Self::Suspended,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for AccountStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for AccountStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for AccountStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for AccountStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// What it is about. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum WarningCode {
    /// `daily_loss_used`
    DailyLossUsed,
    /// `max_drawdown_used`
    MaxDrawdownUsed,
    /// `liquidation_near`
    LiquidationNear,
    /// `no_stop_loss`
    NoStopLoss,
    /// `margin_used`
    MarginUsed,
    /// `order_without_position`
    OrderWithoutPosition,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl WarningCode {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::DailyLossUsed => "daily_loss_used",
            Self::MaxDrawdownUsed => "max_drawdown_used",
            Self::LiquidationNear => "liquidation_near",
            Self::NoStopLoss => "no_stop_loss",
            Self::MarginUsed => "margin_used",
            Self::OrderWithoutPosition => "order_without_position",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for WarningCode {
    fn from(s: &str) -> Self {
        match s {
            "daily_loss_used" => Self::DailyLossUsed,
            "max_drawdown_used" => Self::MaxDrawdownUsed,
            "liquidation_near" => Self::LiquidationNear,
            "no_stop_loss" => Self::NoStopLoss,
            "margin_used" => Self::MarginUsed,
            "order_without_position" => Self::OrderWithoutPosition,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for WarningCode {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for WarningCode {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for WarningCode {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for WarningCode {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// How serious. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum WarningLevel {
    /// `info`
    Info,
    /// `warning`
    Warning,
    /// `critical`
    Critical,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl WarningLevel {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Info => "info",
            Self::Warning => "warning",
            Self::Critical => "critical",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for WarningLevel {
    fn from(s: &str) -> Self {
        match s {
            "info" => Self::Info,
            "warning" => Self::Warning,
            "critical" => Self::Critical,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for WarningLevel {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for WarningLevel {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for WarningLevel {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for WarningLevel {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Whether it is reached. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum ProgressObjectivesProfitTargetStatus {
    /// `open`
    Open,
    /// `met`
    Met,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl ProgressObjectivesProfitTargetStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Open => "open",
            Self::Met => "met",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for ProgressObjectivesProfitTargetStatus {
    fn from(s: &str) -> Self {
        match s {
            "open" => Self::Open,
            "met" => Self::Met,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for ProgressObjectivesProfitTargetStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for ProgressObjectivesProfitTargetStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for ProgressObjectivesProfitTargetStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for ProgressObjectivesProfitTargetStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// The margin mode of a market you never set. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum TradingPolicyMarginDefaultMode {
    /// `isolated`
    Isolated,
    /// `cross`
    Cross,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl TradingPolicyMarginDefaultMode {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Isolated => "isolated",
            Self::Cross => "cross",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for TradingPolicyMarginDefaultMode {
    fn from(s: &str) -> Self {
        match s {
            "isolated" => Self::Isolated,
            "cross" => Self::Cross,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for TradingPolicyMarginDefaultMode {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for TradingPolicyMarginDefaultMode {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for TradingPolicyMarginDefaultMode {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for TradingPolicyMarginDefaultMode {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// One point per closed trade, or per trading day. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum Resolution {
    /// `trade`
    Trade,
    /// `day`
    Day,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl Resolution {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Trade => "trade",
            Self::Day => "day",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for Resolution {
    fn from(s: &str) -> Self {
        match s {
            "trade" => Self::Trade,
            "day" => Self::Day,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for Resolution {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for Resolution {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for Resolution {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for Resolution {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Isolated: each position holds its own margin. Cross: positions share the balance. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum MarginMode {
    /// `isolated`
    Isolated,
    /// `cross`
    Cross,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl MarginMode {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Isolated => "isolated",
            Self::Cross => "cross",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for MarginMode {
    fn from(s: &str) -> Self {
        match s {
            "isolated" => Self::Isolated,
            "cross" => Self::Cross,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for MarginMode {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for MarginMode {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for MarginMode {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for MarginMode {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Where it comes from: the open position, your saved setting, or the market default. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum MarginSettingSource {
    /// `position`
    Position,
    /// `saved`
    Saved,
    /// `default`
    Default,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl MarginSettingSource {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Position => "position",
            Self::Saved => "saved",
            Self::Default => "default",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for MarginSettingSource {
    fn from(s: &str) -> Self {
        match s {
            "position" => Self::Position,
            "saved" => Self::Saved,
            "default" => Self::Default,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for MarginSettingSource {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for MarginSettingSource {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for MarginSettingSource {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for MarginSettingSource {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// The order's type. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum OrderType {
    /// `market`
    Market,
    /// `limit`
    Limit,
    /// `stop_market`
    StopMarket,
    /// `stop_limit`
    StopLimit,
    /// `take_market`
    TakeMarket,
    /// `take_limit`
    TakeLimit,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl OrderType {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Market => "market",
            Self::Limit => "limit",
            Self::StopMarket => "stop_market",
            Self::StopLimit => "stop_limit",
            Self::TakeMarket => "take_market",
            Self::TakeLimit => "take_limit",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for OrderType {
    fn from(s: &str) -> Self {
        match s {
            "market" => Self::Market,
            "limit" => Self::Limit,
            "stop_market" => Self::StopMarket,
            "stop_limit" => Self::StopLimit,
            "take_market" => Self::TakeMarket,
            "take_limit" => Self::TakeLimit,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for OrderType {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for OrderType {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for OrderType {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for OrderType {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Long or short. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum PositionSide {
    /// `long`
    Long,
    /// `short`
    Short,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl PositionSide {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Long => "long",
            Self::Short => "short",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for PositionSide {
    fn from(s: &str) -> Self {
        match s {
            "long" => Self::Long,
            "short" => Self::Short,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for PositionSide {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for PositionSide {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for PositionSide {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for PositionSide {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// The order's side; null for an order placed elsewhere before the API saw it. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum OrderSide {
    /// `buy`
    Buy,
    /// `sell`
    Sell,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl OrderSide {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Buy => "buy",
            Self::Sell => "sell",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for OrderSide {
    fn from(s: &str) -> Self {
        match s {
            "buy" => Self::Buy,
            "sell" => Self::Sell,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for OrderSide {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for OrderSide {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for OrderSide {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for OrderSide {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// unconfirmed: the engine has not confirmed its last state yet. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum OrderStatus {
    /// `pending`
    Pending,
    /// `working`
    Working,
    /// `filled`
    Filled,
    /// `canceled`
    Canceled,
    /// `rejected`
    Rejected,
    /// `expired`
    Expired,
    /// `unconfirmed`
    Unconfirmed,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl OrderStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Pending => "pending",
            Self::Working => "working",
            Self::Filled => "filled",
            Self::Canceled => "canceled",
            Self::Rejected => "rejected",
            Self::Expired => "expired",
            Self::Unconfirmed => "unconfirmed",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for OrderStatus {
    fn from(s: &str) -> Self {
        match s {
            "pending" => Self::Pending,
            "working" => Self::Working,
            "filled" => Self::Filled,
            "canceled" => Self::Canceled,
            "rejected" => Self::Rejected,
            "expired" => Self::Expired,
            "unconfirmed" => Self::Unconfirmed,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for OrderStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for OrderStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for OrderStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for OrderStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// How long it rests. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum TimeInForce {
    /// `gtc`
    Gtc,
    /// `gtd`
    Gtd,
    /// `ioc`
    Ioc,
    /// `fok`
    Fok,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl TimeInForce {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Gtc => "gtc",
            Self::Gtd => "gtd",
            Self::Ioc => "ioc",
            Self::Fok => "fok",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for TimeInForce {
    fn from(s: &str) -> Self {
        match s {
            "gtc" => Self::Gtc,
            "gtd" => Self::Gtd,
            "ioc" => Self::Ioc,
            "fok" => Self::Fok,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for TimeInForce {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for TimeInForce {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for TimeInForce {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for TimeInForce {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Where it was placed: this API, an AI assistant (the MCP server), a trading platform, a strategy, a TradingView alert, or the system (a liquidation). Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum OrderSource {
    /// `api`
    Api,
    /// `mcp`
    Mcp,
    /// `platform`
    Platform,
    /// `strategy`
    Strategy,
    /// `tradingview`
    Tradingview,
    /// `system`
    System,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl OrderSource {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Api => "api",
            Self::Mcp => "mcp",
            Self::Platform => "platform",
            Self::Strategy => "strategy",
            Self::Tradingview => "tradingview",
            Self::System => "system",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for OrderSource {
    fn from(s: &str) -> Self {
        match s {
            "api" => Self::Api,
            "mcp" => Self::Mcp,
            "platform" => Self::Platform,
            "strategy" => Self::Strategy,
            "tradingview" => Self::Tradingview,
            "system" => Self::System,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for OrderSource {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for OrderSource {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for OrderSource {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for OrderSource {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Why it was canceled. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum OrderCancelReason {
    /// `user`
    User,
    /// `cancel_all`
    CancelAll,
    /// `flatten`
    Flatten,
    /// `close_all`
    CloseAll,
    /// `position_closed`
    PositionClosed,
    /// `position_reversed`
    PositionReversed,
    /// `close_assist`
    CloseAssist,
    /// `oco`
    Oco,
    /// `replaced`
    Replaced,
    /// `strategy`
    Strategy,
    /// `key_loss_stop`
    KeyLossStop,
    /// `risk_rule`
    RiskRule,
    /// `system_restart`
    SystemRestart,
    /// `external`
    External,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl OrderCancelReason {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::User => "user",
            Self::CancelAll => "cancel_all",
            Self::Flatten => "flatten",
            Self::CloseAll => "close_all",
            Self::PositionClosed => "position_closed",
            Self::PositionReversed => "position_reversed",
            Self::CloseAssist => "close_assist",
            Self::Oco => "oco",
            Self::Replaced => "replaced",
            Self::Strategy => "strategy",
            Self::KeyLossStop => "key_loss_stop",
            Self::RiskRule => "risk_rule",
            Self::SystemRestart => "system_restart",
            Self::External => "external",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for OrderCancelReason {
    fn from(s: &str) -> Self {
        match s {
            "user" => Self::User,
            "cancel_all" => Self::CancelAll,
            "flatten" => Self::Flatten,
            "close_all" => Self::CloseAll,
            "position_closed" => Self::PositionClosed,
            "position_reversed" => Self::PositionReversed,
            "close_assist" => Self::CloseAssist,
            "oco" => Self::Oco,
            "replaced" => Self::Replaced,
            "strategy" => Self::Strategy,
            "key_loss_stop" => Self::KeyLossStop,
            "risk_rule" => Self::RiskRule,
            "system_restart" => Self::SystemRestart,
            "external" => Self::External,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for OrderCancelReason {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for OrderCancelReason {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for OrderCancelReason {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for OrderCancelReason {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// What you called with: an API key, or an app you signed in to. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum MeCredentialKind {
    /// `key`
    Key,
    /// `app`
    App,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl MeCredentialKind {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Key => "key",
            Self::App => "app",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for MeCredentialKind {
    fn from(s: &str) -> Self {
        match s {
            "key" => Self::Key,
            "app" => Self::App,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for MeCredentialKind {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for MeCredentialKind {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for MeCredentialKind {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for MeCredentialKind {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// read: look only. trade: also place and cancel. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum MeCredentialAccess {
    /// `read`
    Read,
    /// `trade`
    Trade,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl MeCredentialAccess {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Read => "read",
            Self::Trade => "trade",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for MeCredentialAccess {
    fn from(s: &str) -> Self {
        match s {
            "read" => Self::Read,
            "trade" => Self::Trade,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for MeCredentialAccess {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for MeCredentialAccess {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for MeCredentialAccess {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for MeCredentialAccess {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// all: every account of the environment, new ones too. selected: only those listed. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum MeAccountScope {
    /// `all`
    All,
    /// `selected`
    Selected,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl MeAccountScope {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::All => "all",
            Self::Selected => "selected",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for MeAccountScope {
    fn from(s: &str) -> Self {
        match s {
            "all" => Self::All,
            "selected" => Self::Selected,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for MeAccountScope {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for MeAccountScope {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for MeAccountScope {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for MeAccountScope {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// market: now, at the best price within your worst price. limit: at your price or better. stop_market: a market order once the last price reaches the stop. stop_limit: a limit once the last price reaches the stop. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum CreateOrderType {
    /// `market`
    Market,
    /// `limit`
    Limit,
    /// `stop_market`
    StopMarket,
    /// `stop_limit`
    StopLimit,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl CreateOrderType {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Market => "market",
            Self::Limit => "limit",
            Self::StopMarket => "stop_market",
            Self::StopLimit => "stop_limit",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for CreateOrderType {
    fn from(s: &str) -> Self {
        match s {
            "market" => Self::Market,
            "limit" => Self::Limit,
            "stop_market" => Self::StopMarket,
            "stop_limit" => Self::StopLimit,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for CreateOrderType {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for CreateOrderType {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for CreateOrderType {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for CreateOrderType {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// The stop's type (default stop_market). Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum OrderExitsStopLossType {
    /// `stop_market`
    StopMarket,
    /// `stop_limit`
    StopLimit,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl OrderExitsStopLossType {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::StopMarket => "stop_market",
            Self::StopLimit => "stop_limit",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for OrderExitsStopLossType {
    fn from(s: &str) -> Self {
        match s {
            "stop_market" => Self::StopMarket,
            "stop_limit" => Self::StopLimit,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for OrderExitsStopLossType {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for OrderExitsStopLossType {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for OrderExitsStopLossType {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for OrderExitsStopLossType {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// trailing_stop: a stop-loss that follows the price. break_even: the stop-losses move to the entry once the price has gone your way far enough. oco: two orders, the first fill cancels the other. twap, vwap: an order in market slices over time (vwap sized by yesterday's volume over the same hours). scaled: a ladder of limits. chase: a limit that follows the bid or ask. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum CreateStrategyType {
    /// `trailing_stop`
    TrailingStop,
    /// `break_even`
    BreakEven,
    /// `oco`
    Oco,
    /// `twap`
    Twap,
    /// `vwap`
    Vwap,
    /// `scaled`
    Scaled,
    /// `chase`
    Chase,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl CreateStrategyType {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::TrailingStop => "trailing_stop",
            Self::BreakEven => "break_even",
            Self::Oco => "oco",
            Self::Twap => "twap",
            Self::Vwap => "vwap",
            Self::Scaled => "scaled",
            Self::Chase => "chase",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for CreateStrategyType {
    fn from(s: &str) -> Self {
        match s {
            "trailing_stop" => Self::TrailingStop,
            "break_even" => Self::BreakEven,
            "oco" => Self::Oco,
            "twap" => Self::Twap,
            "vwap" => Self::Vwap,
            "scaled" => Self::Scaled,
            "chase" => Self::Chase,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for CreateStrategyType {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for CreateStrategyType {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for CreateStrategyType {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for CreateStrategyType {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Two orders that rest. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum CreateStrategyOrderType {
    /// `limit`
    Limit,
    /// `stop_market`
    StopMarket,
    /// `stop_limit`
    StopLimit,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl CreateStrategyOrderType {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Limit => "limit",
            Self::StopMarket => "stop_market",
            Self::StopLimit => "stop_limit",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for CreateStrategyOrderType {
    fn from(s: &str) -> Self {
        match s {
            "limit" => Self::Limit,
            "stop_market" => Self::StopMarket,
            "stop_limit" => Self::StopLimit,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for CreateStrategyOrderType {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for CreateStrategyOrderType {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for CreateStrategyOrderType {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for CreateStrategyOrderType {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// scaled: equal sizes, growing toward end_price, or shrinking. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum CreateStrategyDistribution {
    /// `flat`
    Flat,
    /// `ascending`
    Ascending,
    /// `descending`
    Descending,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl CreateStrategyDistribution {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Flat => "flat",
            Self::Ascending => "ascending",
            Self::Descending => "descending",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for CreateStrategyDistribution {
    fn from(s: &str) -> Self {
        match s {
            "flat" => Self::Flat,
            "ascending" => Self::Ascending,
            "descending" => Self::Descending,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for CreateStrategyDistribution {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for CreateStrategyDistribution {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for CreateStrategyDistribution {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for CreateStrategyDistribution {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// chase: at max_minutes, cancel it, or send what is left at the market. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum CreateStrategyOnExpiry {
    /// `cancel`
    Cancel,
    /// `market`
    Market,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl CreateStrategyOnExpiry {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Cancel => "cancel",
            Self::Market => "market",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for CreateStrategyOnExpiry {
    fn from(s: &str) -> Self {
        match s {
            "cancel" => Self::Cancel,
            "market" => Self::Market,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for CreateStrategyOnExpiry {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for CreateStrategyOnExpiry {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for CreateStrategyOnExpiry {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for CreateStrategyOnExpiry {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// What the order would do now: fill at once, rest, or be refused (reject says why). Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum WhatIfResult {
    /// `would_fill`
    WouldFill,
    /// `would_rest`
    WouldRest,
    /// `would_reject`
    WouldReject,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl WhatIfResult {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::WouldFill => "would_fill",
            Self::WouldRest => "would_rest",
            Self::WouldReject => "would_reject",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for WhatIfResult {
    fn from(s: &str) -> Self {
        match s {
            "would_fill" => Self::WouldFill,
            "would_rest" => Self::WouldRest,
            "would_reject" => Self::WouldReject,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for WhatIfResult {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for WhatIfResult {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for WhatIfResult {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for WhatIfResult {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// The margin mode the position would use. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum WhatIfMarginMode {
    /// `isolated`
    Isolated,
    /// `cross`
    Cross,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl WhatIfMarginMode {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Isolated => "isolated",
            Self::Cross => "cross",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for WhatIfMarginMode {
    fn from(s: &str) -> Self {
        match s {
            "isolated" => Self::Isolated,
            "cross" => Self::Cross,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for WhatIfMarginMode {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for WhatIfMarginMode {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for WhatIfMarginMode {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for WhatIfMarginMode {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// What happened (GET /v1/webhook-event-types says what each type means). Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum EventType {
    /// `order.created`
    OrderCreated,
    /// `order.edited`
    OrderEdited,
    /// `order.filled`
    OrderFilled,
    /// `order.canceled`
    OrderCanceled,
    /// `order.rejected`
    OrderRejected,
    /// `order.expired`
    OrderExpired,
    /// `fill.created`
    FillCreated,
    /// `position.opened`
    PositionOpened,
    /// `position.changed`
    PositionChanged,
    /// `position.closed`
    PositionClosed,
    /// `strategy.created`
    StrategyCreated,
    /// `strategy.leg_placed`
    StrategyLegPlaced,
    /// `strategy.leg_modified`
    StrategyLegModified,
    /// `strategy.leg_filled`
    StrategyLegFilled,
    /// `strategy.leg_canceled`
    StrategyLegCanceled,
    /// `strategy.leg_rejected`
    StrategyLegRejected,
    /// `strategy.orphan_repaired`
    StrategyOrphanRepaired,
    /// `strategy.slice`
    StrategySlice,
    /// `strategy.ended`
    StrategyEnded,
    /// `tradingview.alert_executed`
    TradingviewAlertExecuted,
    /// `tradingview.alert_refused`
    TradingviewAlertRefused,
    /// `account.updated`
    AccountUpdated,
    /// `account.passed`
    AccountPassed,
    /// `account.failed`
    AccountFailed,
    /// `account.payout_eligible`
    AccountPayoutEligible,
    /// `account.disabled`
    AccountDisabled,
    /// `account.removed`
    AccountRemoved,
    /// `key.loss_stop`
    KeyLossStop,
    /// `key.expiring`
    KeyExpiring,
    /// `system.restarted`
    SystemRestarted,
    /// `risk.warning`
    RiskWarning,
    /// `risk.cleared`
    RiskCleared,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl EventType {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::OrderCreated => "order.created",
            Self::OrderEdited => "order.edited",
            Self::OrderFilled => "order.filled",
            Self::OrderCanceled => "order.canceled",
            Self::OrderRejected => "order.rejected",
            Self::OrderExpired => "order.expired",
            Self::FillCreated => "fill.created",
            Self::PositionOpened => "position.opened",
            Self::PositionChanged => "position.changed",
            Self::PositionClosed => "position.closed",
            Self::StrategyCreated => "strategy.created",
            Self::StrategyLegPlaced => "strategy.leg_placed",
            Self::StrategyLegModified => "strategy.leg_modified",
            Self::StrategyLegFilled => "strategy.leg_filled",
            Self::StrategyLegCanceled => "strategy.leg_canceled",
            Self::StrategyLegRejected => "strategy.leg_rejected",
            Self::StrategyOrphanRepaired => "strategy.orphan_repaired",
            Self::StrategySlice => "strategy.slice",
            Self::StrategyEnded => "strategy.ended",
            Self::TradingviewAlertExecuted => "tradingview.alert_executed",
            Self::TradingviewAlertRefused => "tradingview.alert_refused",
            Self::AccountUpdated => "account.updated",
            Self::AccountPassed => "account.passed",
            Self::AccountFailed => "account.failed",
            Self::AccountPayoutEligible => "account.payout_eligible",
            Self::AccountDisabled => "account.disabled",
            Self::AccountRemoved => "account.removed",
            Self::KeyLossStop => "key.loss_stop",
            Self::KeyExpiring => "key.expiring",
            Self::SystemRestarted => "system.restarted",
            Self::RiskWarning => "risk.warning",
            Self::RiskCleared => "risk.cleared",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for EventType {
    fn from(s: &str) -> Self {
        match s {
            "order.created" => Self::OrderCreated,
            "order.edited" => Self::OrderEdited,
            "order.filled" => Self::OrderFilled,
            "order.canceled" => Self::OrderCanceled,
            "order.rejected" => Self::OrderRejected,
            "order.expired" => Self::OrderExpired,
            "fill.created" => Self::FillCreated,
            "position.opened" => Self::PositionOpened,
            "position.changed" => Self::PositionChanged,
            "position.closed" => Self::PositionClosed,
            "strategy.created" => Self::StrategyCreated,
            "strategy.leg_placed" => Self::StrategyLegPlaced,
            "strategy.leg_modified" => Self::StrategyLegModified,
            "strategy.leg_filled" => Self::StrategyLegFilled,
            "strategy.leg_canceled" => Self::StrategyLegCanceled,
            "strategy.leg_rejected" => Self::StrategyLegRejected,
            "strategy.orphan_repaired" => Self::StrategyOrphanRepaired,
            "strategy.slice" => Self::StrategySlice,
            "strategy.ended" => Self::StrategyEnded,
            "tradingview.alert_executed" => Self::TradingviewAlertExecuted,
            "tradingview.alert_refused" => Self::TradingviewAlertRefused,
            "account.updated" => Self::AccountUpdated,
            "account.passed" => Self::AccountPassed,
            "account.failed" => Self::AccountFailed,
            "account.payout_eligible" => Self::AccountPayoutEligible,
            "account.disabled" => Self::AccountDisabled,
            "account.removed" => Self::AccountRemoved,
            "key.loss_stop" => Self::KeyLossStop,
            "key.expiring" => Self::KeyExpiring,
            "system.restarted" => Self::SystemRestarted,
            "risk.warning" => Self::RiskWarning,
            "risk.cleared" => Self::RiskCleared,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for EventType {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for EventType {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for EventType {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for EventType {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Where it came from: an order placed through this API or by an AI assistant (the MCP server), one placed on a trading platform, the engine itself (positions), a strategy of this API, or a TradingView alert. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum EventSource {
    /// `api`
    Api,
    /// `mcp`
    Mcp,
    /// `platform`
    Platform,
    /// `engine`
    Engine,
    /// `strategy`
    Strategy,
    /// `tradingview`
    Tradingview,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl EventSource {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Api => "api",
            Self::Mcp => "mcp",
            Self::Platform => "platform",
            Self::Engine => "engine",
            Self::Strategy => "strategy",
            Self::Tradingview => "tradingview",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for EventSource {
    fn from(s: &str) -> Self {
        match s {
            "api" => Self::Api,
            "mcp" => Self::Mcp,
            "platform" => Self::Platform,
            "engine" => Self::Engine,
            "strategy" => Self::Strategy,
            "tradingview" => Self::Tradingview,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for EventSource {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for EventSource {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for EventSource {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for EventSource {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// What happened; webhook.ping is a test. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum WebhookPayloadType {
    /// `order.created`
    OrderCreated,
    /// `order.edited`
    OrderEdited,
    /// `order.filled`
    OrderFilled,
    /// `order.canceled`
    OrderCanceled,
    /// `order.rejected`
    OrderRejected,
    /// `order.expired`
    OrderExpired,
    /// `fill.created`
    FillCreated,
    /// `position.opened`
    PositionOpened,
    /// `position.changed`
    PositionChanged,
    /// `position.closed`
    PositionClosed,
    /// `strategy.created`
    StrategyCreated,
    /// `strategy.leg_placed`
    StrategyLegPlaced,
    /// `strategy.leg_modified`
    StrategyLegModified,
    /// `strategy.leg_filled`
    StrategyLegFilled,
    /// `strategy.leg_canceled`
    StrategyLegCanceled,
    /// `strategy.leg_rejected`
    StrategyLegRejected,
    /// `strategy.orphan_repaired`
    StrategyOrphanRepaired,
    /// `strategy.slice`
    StrategySlice,
    /// `strategy.ended`
    StrategyEnded,
    /// `tradingview.alert_executed`
    TradingviewAlertExecuted,
    /// `tradingview.alert_refused`
    TradingviewAlertRefused,
    /// `account.updated`
    AccountUpdated,
    /// `account.passed`
    AccountPassed,
    /// `account.failed`
    AccountFailed,
    /// `account.payout_eligible`
    AccountPayoutEligible,
    /// `account.disabled`
    AccountDisabled,
    /// `account.removed`
    AccountRemoved,
    /// `key.loss_stop`
    KeyLossStop,
    /// `key.expiring`
    KeyExpiring,
    /// `system.restarted`
    SystemRestarted,
    /// `risk.warning`
    RiskWarning,
    /// `risk.cleared`
    RiskCleared,
    /// `webhook.ping`
    WebhookPing,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl WebhookPayloadType {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::OrderCreated => "order.created",
            Self::OrderEdited => "order.edited",
            Self::OrderFilled => "order.filled",
            Self::OrderCanceled => "order.canceled",
            Self::OrderRejected => "order.rejected",
            Self::OrderExpired => "order.expired",
            Self::FillCreated => "fill.created",
            Self::PositionOpened => "position.opened",
            Self::PositionChanged => "position.changed",
            Self::PositionClosed => "position.closed",
            Self::StrategyCreated => "strategy.created",
            Self::StrategyLegPlaced => "strategy.leg_placed",
            Self::StrategyLegModified => "strategy.leg_modified",
            Self::StrategyLegFilled => "strategy.leg_filled",
            Self::StrategyLegCanceled => "strategy.leg_canceled",
            Self::StrategyLegRejected => "strategy.leg_rejected",
            Self::StrategyOrphanRepaired => "strategy.orphan_repaired",
            Self::StrategySlice => "strategy.slice",
            Self::StrategyEnded => "strategy.ended",
            Self::TradingviewAlertExecuted => "tradingview.alert_executed",
            Self::TradingviewAlertRefused => "tradingview.alert_refused",
            Self::AccountUpdated => "account.updated",
            Self::AccountPassed => "account.passed",
            Self::AccountFailed => "account.failed",
            Self::AccountPayoutEligible => "account.payout_eligible",
            Self::AccountDisabled => "account.disabled",
            Self::AccountRemoved => "account.removed",
            Self::KeyLossStop => "key.loss_stop",
            Self::KeyExpiring => "key.expiring",
            Self::SystemRestarted => "system.restarted",
            Self::RiskWarning => "risk.warning",
            Self::RiskCleared => "risk.cleared",
            Self::WebhookPing => "webhook.ping",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for WebhookPayloadType {
    fn from(s: &str) -> Self {
        match s {
            "order.created" => Self::OrderCreated,
            "order.edited" => Self::OrderEdited,
            "order.filled" => Self::OrderFilled,
            "order.canceled" => Self::OrderCanceled,
            "order.rejected" => Self::OrderRejected,
            "order.expired" => Self::OrderExpired,
            "fill.created" => Self::FillCreated,
            "position.opened" => Self::PositionOpened,
            "position.changed" => Self::PositionChanged,
            "position.closed" => Self::PositionClosed,
            "strategy.created" => Self::StrategyCreated,
            "strategy.leg_placed" => Self::StrategyLegPlaced,
            "strategy.leg_modified" => Self::StrategyLegModified,
            "strategy.leg_filled" => Self::StrategyLegFilled,
            "strategy.leg_canceled" => Self::StrategyLegCanceled,
            "strategy.leg_rejected" => Self::StrategyLegRejected,
            "strategy.orphan_repaired" => Self::StrategyOrphanRepaired,
            "strategy.slice" => Self::StrategySlice,
            "strategy.ended" => Self::StrategyEnded,
            "tradingview.alert_executed" => Self::TradingviewAlertExecuted,
            "tradingview.alert_refused" => Self::TradingviewAlertRefused,
            "account.updated" => Self::AccountUpdated,
            "account.passed" => Self::AccountPassed,
            "account.failed" => Self::AccountFailed,
            "account.payout_eligible" => Self::AccountPayoutEligible,
            "account.disabled" => Self::AccountDisabled,
            "account.removed" => Self::AccountRemoved,
            "key.loss_stop" => Self::KeyLossStop,
            "key.expiring" => Self::KeyExpiring,
            "system.restarted" => Self::SystemRestarted,
            "risk.warning" => Self::RiskWarning,
            "risk.cleared" => Self::RiskCleared,
            "webhook.ping" => Self::WebhookPing,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for WebhookPayloadType {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for WebhookPayloadType {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for WebhookPayloadType {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for WebhookPayloadType {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// take_profit: a limit on the profit side. stop_loss: a stop on the loss side. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum ExitsLegGroup {
    /// `take_profit`
    TakeProfit,
    /// `stop_loss`
    StopLoss,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl ExitsLegGroup {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::TakeProfit => "take_profit",
            Self::StopLoss => "stop_loss",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for ExitsLegGroup {
    fn from(s: &str) -> Self {
        match s {
            "take_profit" => Self::TakeProfit,
            "stop_loss" => Self::StopLoss,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for ExitsLegGroup {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for ExitsLegGroup {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for ExitsLegGroup {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for ExitsLegGroup {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// The order's status; waiting: a stop-loss not placed yet (it waits for its activation price or its break-even). Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum ExitsLegStatus {
    /// `pending`
    Pending,
    /// `working`
    Working,
    /// `filled`
    Filled,
    /// `canceled`
    Canceled,
    /// `rejected`
    Rejected,
    /// `expired`
    Expired,
    /// `unconfirmed`
    Unconfirmed,
    /// `waiting`
    Waiting,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl ExitsLegStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Pending => "pending",
            Self::Working => "working",
            Self::Filled => "filled",
            Self::Canceled => "canceled",
            Self::Rejected => "rejected",
            Self::Expired => "expired",
            Self::Unconfirmed => "unconfirmed",
            Self::Waiting => "waiting",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for ExitsLegStatus {
    fn from(s: &str) -> Self {
        match s {
            "pending" => Self::Pending,
            "working" => Self::Working,
            "filled" => Self::Filled,
            "canceled" => Self::Canceled,
            "rejected" => Self::Rejected,
            "expired" => Self::Expired,
            "unconfirmed" => Self::Unconfirmed,
            "waiting" => Self::Waiting,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for ExitsLegStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for ExitsLegStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for ExitsLegStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for ExitsLegStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// What to do. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum ExitsBatchOperationOp {
    /// `place`
    Place,
    /// `modify`
    Modify,
    /// `cancel`
    Cancel,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl ExitsBatchOperationOp {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Place => "place",
            Self::Modify => "modify",
            Self::Cancel => "cancel",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for ExitsBatchOperationOp {
    fn from(s: &str) -> Self {
        match s {
            "place" => Self::Place,
            "modify" => Self::Modify,
            "cancel" => Self::Cancel,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for ExitsBatchOperationOp {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for ExitsBatchOperationOp {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for ExitsBatchOperationOp {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for ExitsBatchOperationOp {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// ExitsBatchOperationClear Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum ExitsBatchOperationClear {
    /// `trail`
    Trail,
    /// `break_even`
    BreakEven,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl ExitsBatchOperationClear {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Trail => "trail",
            Self::BreakEven => "break_even",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for ExitsBatchOperationClear {
    fn from(s: &str) -> Self {
        match s {
            "trail" => Self::Trail,
            "break_even" => Self::BreakEven,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for ExitsBatchOperationClear {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for ExitsBatchOperationClear {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for ExitsBatchOperationClear {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for ExitsBatchOperationClear {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// exits: a position's take-profit and stop-loss legs. bracket: an order with exits once it fills. oco: two orders, the first fill cancels the other. twap, vwap: an order in slices over time. scaled: a ladder of limits. chase: a limit that follows the price. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum StrategyType {
    /// `exits`
    Exits,
    /// `bracket`
    Bracket,
    /// `oco`
    Oco,
    /// `twap`
    Twap,
    /// `vwap`
    Vwap,
    /// `scaled`
    Scaled,
    /// `chase`
    Chase,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl StrategyType {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Exits => "exits",
            Self::Bracket => "bracket",
            Self::Oco => "oco",
            Self::Twap => "twap",
            Self::Vwap => "vwap",
            Self::Scaled => "scaled",
            Self::Chase => "chase",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for StrategyType {
    fn from(s: &str) -> Self {
        match s {
            "exits" => Self::Exits,
            "bracket" => Self::Bracket,
            "oco" => Self::Oco,
            "twap" => Self::Twap,
            "vwap" => Self::Vwap,
            "scaled" => Self::Scaled,
            "chase" => Self::Chase,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for StrategyType {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for StrategyType {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for StrategyType {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for StrategyType {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// waiting: armed, nothing placed yet. working: running. completed: done as asked. canceled: you ended it. ended: it stopped for end_reason. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum StrategyStatus {
    /// `waiting`
    Waiting,
    /// `working`
    Working,
    /// `completed`
    Completed,
    /// `canceled`
    Canceled,
    /// `ended`
    Ended,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl StrategyStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Waiting => "waiting",
            Self::Working => "working",
            Self::Completed => "completed",
            Self::Canceled => "canceled",
            Self::Ended => "ended",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for StrategyStatus {
    fn from(s: &str) -> Self {
        match s {
            "waiting" => Self::Waiting,
            "working" => Self::Working,
            "completed" => Self::Completed,
            "canceled" => Self::Canceled,
            "ended" => Self::Ended,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for StrategyStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for StrategyStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for StrategyStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for StrategyStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Why it stopped. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum StrategyEndReason {
    /// `take_profit`
    TakeProfit,
    /// `stop_loss`
    StopLoss,
    /// `filled`
    Filled,
    /// `position_closed`
    PositionClosed,
    /// `position_reversed`
    PositionReversed,
    /// `canceled_by_user`
    CanceledByUser,
    /// `external`
    External,
    /// `key_revoked`
    KeyRevoked,
    /// `account_closed`
    AccountClosed,
    /// `engine_restart`
    EngineRestart,
    /// `child_rejected`
    ChildRejected,
    /// `price_bound`
    PriceBound,
    /// `expired`
    Expired,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl StrategyEndReason {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::TakeProfit => "take_profit",
            Self::StopLoss => "stop_loss",
            Self::Filled => "filled",
            Self::PositionClosed => "position_closed",
            Self::PositionReversed => "position_reversed",
            Self::CanceledByUser => "canceled_by_user",
            Self::External => "external",
            Self::KeyRevoked => "key_revoked",
            Self::AccountClosed => "account_closed",
            Self::EngineRestart => "engine_restart",
            Self::ChildRejected => "child_rejected",
            Self::PriceBound => "price_bound",
            Self::Expired => "expired",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for StrategyEndReason {
    fn from(s: &str) -> Self {
        match s {
            "take_profit" => Self::TakeProfit,
            "stop_loss" => Self::StopLoss,
            "filled" => Self::Filled,
            "position_closed" => Self::PositionClosed,
            "position_reversed" => Self::PositionReversed,
            "canceled_by_user" => Self::CanceledByUser,
            "external" => Self::External,
            "key_revoked" => Self::KeyRevoked,
            "account_closed" => Self::AccountClosed,
            "engine_restart" => Self::EngineRestart,
            "child_rejected" => Self::ChildRejected,
            "price_bound" => Self::PriceBound,
            "expired" => Self::Expired,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for StrategyEndReason {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for StrategyEndReason {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for StrategyEndReason {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for StrategyEndReason {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// json: the signed event. discord, slack, telegram: a chat message. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum WebhookEndpointFormat {
    /// `json`
    Json,
    /// `discord`
    Discord,
    /// `slack`
    Slack,
    /// `telegram`
    Telegram,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl WebhookEndpointFormat {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Json => "json",
            Self::Discord => "discord",
            Self::Slack => "slack",
            Self::Telegram => "telegram",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for WebhookEndpointFormat {
    fn from(s: &str) -> Self {
        match s {
            "json" => Self::Json,
            "discord" => Self::Discord,
            "slack" => Self::Slack,
            "telegram" => Self::Telegram,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for WebhookEndpointFormat {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for WebhookEndpointFormat {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for WebhookEndpointFormat {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for WebhookEndpointFormat {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// A group of events. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum WebhookEndpointEvent {
    /// `orders`
    Orders,
    /// `positions`
    Positions,
    /// `account`
    Account,
    /// `risk`
    Risk,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl WebhookEndpointEvent {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Orders => "orders",
            Self::Positions => "positions",
            Self::Account => "account",
            Self::Risk => "risk",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for WebhookEndpointEvent {
    fn from(s: &str) -> Self {
        match s {
            "orders" => Self::Orders,
            "positions" => Self::Positions,
            "account" => Self::Account,
            "risk" => Self::Risk,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for WebhookEndpointEvent {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for WebhookEndpointEvent {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for WebhookEndpointEvent {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for WebhookEndpointEvent {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// disabled: we stopped after failures in a row; turn it on again from your dashboard. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum WebhookEndpointStatus {
    /// `active`
    Active,
    /// `paused`
    Paused,
    /// `disabled`
    Disabled,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl WebhookEndpointStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Active => "active",
            Self::Paused => "paused",
            Self::Disabled => "disabled",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for WebhookEndpointStatus {
    fn from(s: &str) -> Self {
        match s {
            "active" => Self::Active,
            "paused" => Self::Paused,
            "disabled" => Self::Disabled,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for WebhookEndpointStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for WebhookEndpointStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for WebhookEndpointStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for WebhookEndpointStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// pending: waiting for its next try. failed: every try failed. canceled: the endpoint was disabled or deleted. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum WebhookDeliveryStatus {
    /// `pending`
    Pending,
    /// `delivering`
    Delivering,
    /// `delivered`
    Delivered,
    /// `failed`
    Failed,
    /// `canceled`
    Canceled,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl WebhookDeliveryStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Pending => "pending",
            Self::Delivering => "delivering",
            Self::Delivered => "delivered",
            Self::Failed => "failed",
            Self::Canceled => "canceled",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for WebhookDeliveryStatus {
    fn from(s: &str) -> Self {
        match s {
            "pending" => Self::Pending,
            "delivering" => Self::Delivering,
            "delivered" => Self::Delivered,
            "failed" => Self::Failed,
            "canceled" => Self::Canceled,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for WebhookDeliveryStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for WebhookDeliveryStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for WebhookDeliveryStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for WebhookDeliveryStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// The group that receives it; null: every endpoint (the test). Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum WebhookEventTypeGroup {
    /// `orders`
    Orders,
    /// `positions`
    Positions,
    /// `account`
    Account,
    /// `risk`
    Risk,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl WebhookEventTypeGroup {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Orders => "orders",
            Self::Positions => "positions",
            Self::Account => "account",
            Self::Risk => "risk",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for WebhookEventTypeGroup {
    fn from(s: &str) -> Self {
        match s {
            "orders" => Self::Orders,
            "positions" => Self::Positions,
            "account" => Self::Account,
            "risk" => Self::Risk,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for WebhookEventTypeGroup {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for WebhookEventTypeGroup {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for WebhookEventTypeGroup {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for WebhookEventTypeGroup {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// paused: alerts are refused (link_paused). Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum TradingViewHookStatus {
    /// `active`
    Active,
    /// `paused`
    Paused,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl TradingViewHookStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Active => "active",
            Self::Paused => "paused",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for TradingViewHookStatus {
    fn from(s: &str) -> Self {
        match s {
            "active" => Self::Active,
            "paused" => Self::Paused,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for TradingViewHookStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for TradingViewHookStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for TradingViewHookStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for TradingViewHookStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// from_alert: the size the alert gives. qty, usd, pct_balance: the link's own. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum TradingViewHookSizingMode {
    /// `from_alert`
    FromAlert,
    /// `qty`
    Qty,
    /// `usd`
    Usd,
    /// `pct_balance`
    PctBalance,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl TradingViewHookSizingMode {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::FromAlert => "from_alert",
            Self::Qty => "qty",
            Self::Usd => "usd",
            Self::PctBalance => "pct_balance",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for TradingViewHookSizingMode {
    fn from(s: &str) -> Self {
        match s {
            "from_alert" => Self::FromAlert,
            "qty" => Self::Qty,
            "usd" => Self::Usd,
            "pct_balance" => Self::PctBalance,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for TradingViewHookSizingMode {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for TradingViewHookSizingMode {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for TradingViewHookSizingMode {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for TradingViewHookSizingMode {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// signal: an opposite alert reverses, a repeat is skipped. add: a repeat adds. target: the strategy's position size is followed. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum TradingViewHookPositionMode {
    /// `signal`
    Signal,
    /// `add`
    Add,
    /// `target`
    Target,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl TradingViewHookPositionMode {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Signal => "signal",
            Self::Add => "add",
            Self::Target => "target",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for TradingViewHookPositionMode {
    fn from(s: &str) -> Self {
        match s {
            "signal" => Self::Signal,
            "add" => Self::Add,
            "target" => Self::Target,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for TradingViewHookPositionMode {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for TradingViewHookPositionMode {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for TradingViewHookPositionMode {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for TradingViewHookPositionMode {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// What it did. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum TradingViewAlertStatus {
    /// `processing`
    Processing,
    /// `placed`
    Placed,
    /// `partial`
    Partial,
    /// `dry_run`
    DryRun,
    /// `duplicate`
    Duplicate,
    /// `skipped`
    Skipped,
    /// `refused`
    Refused,
    /// `failed`
    Failed,
    /// `rate_limited`
    RateLimited,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl TradingViewAlertStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Processing => "processing",
            Self::Placed => "placed",
            Self::Partial => "partial",
            Self::DryRun => "dry_run",
            Self::Duplicate => "duplicate",
            Self::Skipped => "skipped",
            Self::Refused => "refused",
            Self::Failed => "failed",
            Self::RateLimited => "rate_limited",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for TradingViewAlertStatus {
    fn from(s: &str) -> Self {
        match s {
            "processing" => Self::Processing,
            "placed" => Self::Placed,
            "partial" => Self::Partial,
            "dry_run" => Self::DryRun,
            "duplicate" => Self::Duplicate,
            "skipped" => Self::Skipped,
            "refused" => Self::Refused,
            "failed" => Self::Failed,
            "rate_limited" => Self::RateLimited,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for TradingViewAlertStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for TradingViewAlertStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for TradingViewAlertStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for TradingViewAlertStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// Where it stands. Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum LimitStandingStatus {
    /// `ok`
    Ok,
    /// `warning`
    Warning,
    /// `critical`
    Critical,
    /// `breached`
    Breached,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl LimitStandingStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::Ok => "ok",
            Self::Warning => "warning",
            Self::Critical => "critical",
            Self::Breached => "breached",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for LimitStandingStatus {
    fn from(s: &str) -> Self {
        match s {
            "ok" => Self::Ok,
            "warning" => Self::Warning,
            "critical" => Self::Critical,
            "breached" => Self::Breached,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for LimitStandingStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for LimitStandingStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for LimitStandingStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for LimitStandingStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// OrdersListParamsStatus Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum OrdersListParamsStatus {
    /// `all`
    All,
    /// `working`
    Working,
    /// `history`
    History,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl OrdersListParamsStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::All => "all",
            Self::Working => "working",
            Self::History => "history",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for OrdersListParamsStatus {
    fn from(s: &str) -> Self {
        match s {
            "all" => Self::All,
            "working" => Self::Working,
            "history" => Self::History,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for OrdersListParamsStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for OrdersListParamsStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for OrdersListParamsStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for OrdersListParamsStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}

/// StrategiesListParamsStatus Open: a value from a newer API version is kept in `Other`.
#[derive(Clone, Debug, PartialEq, Eq, Hash)]
pub enum StrategiesListParamsStatus {
    /// `all`
    All,
    /// `active`
    Active,
    /// `ended`
    Ended,
    /// A value this SDK version does not know yet.
    Other(String),
}

impl StrategiesListParamsStatus {
    /// The value as the API writes it.
    pub fn as_str(&self) -> &str {
        match self {
            Self::All => "all",
            Self::Active => "active",
            Self::Ended => "ended",
            Self::Other(s) => s,
        }
    }
}

impl From<&str> for StrategiesListParamsStatus {
    fn from(s: &str) -> Self {
        match s {
            "all" => Self::All,
            "active" => Self::Active,
            "ended" => Self::Ended,
            other => Self::Other(other.to_string()),
        }
    }
}

impl Default for StrategiesListParamsStatus {
    fn default() -> Self {
        Self::Other(String::new())
    }
}

impl std::fmt::Display for StrategiesListParamsStatus {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        f.write_str(self.as_str())
    }
}

impl Serialize for StrategiesListParamsStatus {
    fn serialize<S: Serializer>(&self, s: S) -> Result<S::Ok, S::Error> {
        s.serialize_str(self.as_str())
    }
}

impl<'de> Deserialize<'de> for StrategiesListParamsStatus {
    fn deserialize<D: Deserializer<'de>>(d: D) -> Result<Self, D::Error> {
        Ok(Self::from(String::deserialize(d)?.as_str()))
    }
}
