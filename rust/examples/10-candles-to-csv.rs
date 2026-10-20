//! 10: the last 100 closed one-minute candles of a market and the one forming (final false), written to candles.csv.
//!   cargo run --example 10-candles-to-csv [BTC-USD]
use std::collections::BTreeMap;
use std::io::Write;

use phoenixperps::Client;
use serde_json::{json, Map, Value};

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let client = Client::from_env()?;
    let symbol = std::env::args().nth(1).unwrap_or_else(|| "BTC-USD".into()).to_uppercase();
    let mut market = client.market_stream();
    let mut params = Map::new();
    params.insert("interval".into(), json!("1m"));
    params.insert("history".into(), json!(100));
    market.subscribe("candles", &[symbol.as_str()], params).await?;

    let mut candles: BTreeMap<i64, Value> = BTreeMap::new();
    while let Some(frame) = market.next().await {
        let frame = frame?;
        match frame.r#type.as_str() {
            "snapshot_end" => break,
            "candles" => {
                for c in frame.data.as_ref().and_then(Value::as_array).into_iter().flatten() {
                    candles.insert(c["time"].as_i64().unwrap_or_default(), c.clone());
                }
            }
            _ => {}
        }
    }

    let mut csv = std::fs::File::create("candles.csv")?;
    writeln!(csv, "time,open,high,low,close,volume,final")?;
    let text = |v: &Value| v.as_str().map(str::to_string).unwrap_or_else(|| v.to_string());
    for (t, c) in &candles {
        writeln!(csv, "{t},{},{},{},{},{},{}", text(&c["open"]), text(&c["high"]), text(&c["low"]), text(&c["close"]), text(&c["volume"]), c["final"])?;
    }
    println!("{} candles of {symbol} in candles.csv (time in ms)", candles.len());
    Ok(())
}
