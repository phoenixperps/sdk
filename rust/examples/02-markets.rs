//! 02: the markets and their rules, then a quote (bid, ask, last) from the market stream.
//!   cargo run --example 02-markets [BTC-USD]
use phoenixperps::Client;
use serde_json::{json, Map};

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?;
    let symbol = std::env::args().nth(1).unwrap_or_else(|| "BTC-USD".into()).to_uppercase();

    for m in &client.markets().list().await?.data {
        println!("{:<10} tick {}  step {}  min {}  up to {}x  {}", m.market, m.tick_size, m.size_step, m.min_size, m.max_leverage, m.status);
    }

    let mut market = client.market_stream();
    let mut params = Map::new();
    params.insert("interval_ms".into(), json!(250));
    let reply = market.subscribe("ticker", &[symbol.as_str()], params).await?;
    if let Some(r) = reply["rejected"].as_array().and_then(|r| r.first()) {
        eprintln!("{symbol}: {}", r["code"]);
        std::process::exit(1);
    }
    while let Some(frame) = market.next().await {
        let frame = frame?;
        if let (true, Some(q)) = (frame.r#type == "ticker", frame.data) {
            println!("{symbol}: bid {}  ask {}  last {}", q["bid"], q["ask"], q["last"]);
            break;
        }
    }
    Ok(())
}
