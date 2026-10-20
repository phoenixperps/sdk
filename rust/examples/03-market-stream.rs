//! 03: live market data: prices for two markets and the trades of one, until 20 frames came.
//!   cargo run --example 03-market-stream
use phoenixperps::Client;
use serde_json::{json, Map};

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?;
    let mut market = client.market_stream(); // subscriptions are made again after a reconnect
    let mut ticker = Map::new();
    ticker.insert("interval_ms".into(), json!(500));
    market.subscribe("ticker", &["BTC-USD", "ETH-USD"], ticker).await?;
    let mut trades = Map::new();
    trades.insert("history".into(), json!(5));
    market.subscribe("trades", &["BTC-USD"], trades).await?;

    let mut n = 0;
    while let Some(frame) = market.next().await {
        let frame = frame?;
        let (symbol, data) = (frame.symbol.unwrap_or_default(), frame.data.unwrap_or_default());
        match frame.r#type.as_str() {
            "ticker" => println!("ticker {symbol}: bid {} ask {}", data["bid"], data["ask"]),
            "trades" => {
                for t in data.as_array().into_iter().flatten() {
                    println!("trade  {symbol}: {} {} @ {}", t["side"], t["size"], t["price"]);
                }
            }
            _ => continue,
        }
        n += 1;
        if n >= 20 {
            break;
        }
    }
    Ok(())
}
