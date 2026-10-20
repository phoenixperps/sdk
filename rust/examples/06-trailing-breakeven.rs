//! 06: a position whose stop loss trails the price by 0.5% and moves to break-even once the price has gone 0.5% your
//! way, all on the API's servers. Follows the account stream for 30 s to watch the stop move, then closes.
//!   cargo run --example 06-trailing-breakeven
use std::time::Duration;

use phoenixperps::{
    dec, AccountStreamOptions, Client, ClosePosition, CreateOrder, CreateOrderType, OrderExits, OrderExitsStopLoss, OrderExitsStopLossBreakEven,
    OrderExitsStopLossTrail, OrderSide,
};

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?;
    if client.environment() == "live" && std::env::var("PHOENIXPERPS_ALLOW_LIVE").as_deref() != Ok("yes") {
        eprintln!("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).");
        std::process::exit(1);
    }
    let account = client.accounts().list().await?.data[0].id.clone();
    let btc = client.markets().get("BTC-USD").await?;

    // the stream first, so every event of the stop is told, from its placing on
    let mut stream = client.account_stream(AccountStreamOptions { accounts: vec![account.clone()], ..Default::default() });
    stream.ready().await;

    let trail = OrderExitsStopLossTrail { percent: Some(dec("0.5")), ..Default::default() };
    let break_even = OrderExitsStopLossBreakEven { trigger_percent: Some(dec("0.5")), offset_ticks: Some(2), ..Default::default() };
    let order = client
        .orders()
        .create(&account, &CreateOrder {
            market: "BTC-USD".into(),
            side: OrderSide::Buy,
            r#type: CreateOrderType::Market,
            size: Some(btc.min_size),
            exits: Some(OrderExits {
                stop_loss: Some(OrderExitsStopLoss { percent: Some(dec("1")), trail: Some(trail), break_even: Some(break_even), ..Default::default() }),
                ..Default::default()
            }),
            ..Default::default()
        })
        .await?;
    println!("bought BTC ({}); watching the stop for 30 s", order.status);

    let watch = async {
        while let Some(event) = stream.next().await {
            let event = event?;
            if event.r#type.to_string().starts_with("strategy.") {
                println!("{} {} {:?}", event.time, event.r#type, event.data);
            }
        }
        Ok::<(), phoenixperps::Error>(())
    };
    if let Ok(done) = tokio::time::timeout(Duration::from_secs(30), watch).await {
        done?;
    }

    let closed = client.positions().close(&account, "BTC-USD", &ClosePosition::default()).await?;
    println!("closed: {}", closed.status);
    Ok(())
}
