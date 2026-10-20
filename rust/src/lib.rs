//! The Phoenix Perpetuals API for Rust: every REST call, the account stream and market data, with retries,
//! idempotency keys, paging and typed errors (docs/BEHAVIOUR.md).
//!
//! ```no_run
//! # async fn run() -> phoenixperps::Result<()> {
//! use phoenixperps::{CreateOrder, CreateOrderType, OrderSide};
//! use rust_decimal::Decimal;
//!
//! let client = phoenixperps::Client::from_env()?; // PHOENIXPERPS_API_KEY
//! let accounts = client.accounts().list().await?;
//! let order = client
//!     .orders()
//!     .create(&accounts.data[0].id, &CreateOrder {
//!         market: "BTC-USD".into(),
//!         side: OrderSide::Buy,
//!         r#type: CreateOrderType::Limit,
//!         size: Some(Decimal::new(1, 2)),
//!         limit_price: Some(Decimal::new(62000, 0)),
//!         ..Default::default()
//!     })
//!     .await?;
//! # Ok(())
//! # }
//! ```

mod client;
mod error;
mod helpers;
mod page;
mod streams;

/// The surface generated from the API spec: types, open enums, resources, codes (all re-exported here).
#[rustfmt::skip]
#[allow(clippy::all)]
pub mod generated {
    pub mod enums;
    pub mod meta;
    pub mod resources;
    pub mod types;
}

pub use client::{Client, ClientBuilder, Logger, RateLimit, RawResponse, RequestOptions, ResponseHook};
pub use error::{Error, ErrorData, ErrorKind, Result};
pub use generated::enums::*;
pub use generated::meta::{codes, SPEC_VERSION};
pub use generated::resources::*;
pub use generated::types::*;
pub use helpers::{verify_webhook, DecimalArg, MarketRules, RoundMode, WebhookError};
pub use page::Page;
pub use streams::{AccountMessage, AccountStream, AccountStreamOptions, MarketFrame, MarketStream, StreamEvent};

pub use rust_decimal::Decimal;

/// This SDK's version (lockstep with every other Phoenix Perpetuals SDK).
pub const SDK_VERSION: &str = env!("CARGO_PKG_VERSION");

/// A decimal written as the API writes it: `dec("62000.5")`. Panics on a string that is not a decimal (it is meant
/// for literals; parse input with `Decimal::from_str_exact`).
pub fn dec(s: &str) -> Decimal {
    Decimal::from_str_exact(s).unwrap_or_else(|_| panic!("not a decimal: {s}"))
}
