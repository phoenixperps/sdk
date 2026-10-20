//! 04: a limit order far from the market, found again by your own id, then canceled. The Idempotency-Key makes a
//! retried request return the first order instead of placing a second one.
//!   cargo run --example 04-place-cancel-limit
use std::time::{SystemTime, UNIX_EPOCH};

use phoenixperps::{dec, Client, CreateOrder, CreateOrderType, Decimal, MarketRules, OrderSide, OrdersListParams, RequestOptions, RoundMode};
use serde_json::Map;

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?;
    if client.environment() == "live" && std::env::var("PHOENIXPERPS_ALLOW_LIVE").as_deref() != Ok("yes") {
        eprintln!("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).");
        std::process::exit(1);
    }
    let account = client.accounts().list().await?.data[0].id.clone();
    let rules = MarketRules::new(&client.markets().get("BTC-USD").await?);

    let mut market = client.market_stream();
    market.subscribe("ticker", &["BTC-USD"], Map::new()).await?;
    let mut bid = Decimal::ZERO;
    while let Some(frame) = market.next().await {
        let frame = frame?;
        if let (true, Some(q)) = (frame.r#type == "ticker", frame.data) {
            bid = dec(q["bid"].as_str().unwrap_or("0"));
            break;
        }
    }
    drop(market);

    let millis = SystemTime::now().duration_since(UNIX_EPOCH).map(|d| d.as_millis()).unwrap_or_default();
    let client_order_id = format!("example-04-{millis}");
    let size = rules.size_for_notional(dec("100"), bid).expect("a price"); // about 100 USD
    let limit = rules.round_price(bid * dec("0.9"), RoundMode::Down).expect("a price"); // 10% below: it rests
    let body = CreateOrder {
        market: "BTC-USD".into(),
        side: OrderSide::Buy,
        r#type: CreateOrderType::Limit,
        size: Some(size),
        limit_price: Some(limit),
        client_order_id: Some(client_order_id.clone()),
        ..Default::default()
    };
    let options = RequestOptions { idempotency_key: Some(uuid::Uuid::new_v4().to_string()), ..Default::default() };
    let order = client.orders().create_with(&account, &body, &options).await?;
    println!("placed {}: {}, {size} at {limit}", order.id, order.status);

    let params = OrdersListParams { client_order_id: Some(client_order_id), ..Default::default() };
    let found = client.orders().list(&account, Some(&params)).await?;
    println!("found by client_order_id: {}", found.data[0].id);

    let canceled = client.orders().cancel(&account, &order.id).await?;
    println!("canceled: {}", canceled.status);
    Ok(())
}
