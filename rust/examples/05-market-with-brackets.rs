//! 05: a market order with its exits: a take profit 1% away and a stop loss 0.5% away, placed by the API the moment
//! it fills and kept on its servers. The position is closed at the end.
//!   cargo run --example 05-market-with-brackets
use phoenixperps::{dec, Client, ClosePosition, CreateOrder, CreateOrderType, OrderExits, OrderExitsStopLoss, OrderExitsTakeProfit, OrderSide};

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?;
    if client.environment() == "live" && std::env::var("PHOENIXPERPS_ALLOW_LIVE").as_deref() != Ok("yes") {
        eprintln!("This example trades: it runs on the sandbox (PHOENIXPERPS_ALLOW_LIVE=yes to run it on live).");
        std::process::exit(1);
    }
    let account = client.accounts().list().await?.data[0].id.clone();
    let btc = client.markets().get("BTC-USD").await?;

    let order = client
        .orders()
        .create(&account, &CreateOrder {
            market: "BTC-USD".into(),
            side: OrderSide::Buy,
            r#type: CreateOrderType::Market,
            size: Some(btc.min_size),
            exits: Some(OrderExits {
                take_profit: Some(vec![OrderExitsTakeProfit { percent: Some(dec("1")), ..Default::default() }]),
                stop_loss: Some(OrderExitsStopLoss { percent: Some(dec("0.5")), ..Default::default() }),
                ..Default::default()
            }),
            ..Default::default()
        })
        .await?;
    let fill = order.average_fill_price.map(|p| p.to_string()).unwrap_or_else(|| "-".into());
    println!("market order {}: {} at {fill}", order.id, order.status);

    let exits = client.exits().get(&account, "BTC-USD").await?;
    for tp in &exits.take_profit {
        println!("take profit: {} at {:?} ({})", tp.size, tp.price, tp.status);
    }
    for sl in &exits.stop_loss {
        println!("stop loss:   {} at {:?} ({})", sl.size, sl.price, sl.status);
    }

    let closed = client.positions().close(&account, "BTC-USD", &ClosePosition::default()).await?;
    println!("closed: {} {} (the exits went with the position)", closed.id, closed.status);
    Ok(())
}
