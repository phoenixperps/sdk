//! 07: what an order would do now, without placing it: fill or rest, the price, the fee, the margin, the position
//! after. Nothing is sent to the market.
//!   cargo run --example 07-what-if
use phoenixperps::{dec, Client, CreateOrder, CreateOrderType, Decimal, OrderSide};

fn or(d: Option<Decimal>) -> String {
    d.map(|d| d.to_string()).unwrap_or_else(|| "-".into())
}

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?;
    let account = client.accounts().list().await?.data[0].id.clone();

    let w = client
        .orders()
        .preview(&account, &CreateOrder {
            market: "BTC-USD".into(),
            side: OrderSide::Buy,
            r#type: CreateOrderType::Market,
            notional_usd: Some(dec("1000")),
            ..Default::default()
        })
        .await?;
    let refused = w.reject.as_ref().map(|r| format!(" ({})", r.code)).unwrap_or_default();
    println!("result:         {}{refused}", w.result);
    println!("size:           {}", or(w.size));
    if let Some(x) = &w.execution {
        println!("price:          {} (worst {})", x.expected_price, or(x.worst_price));
    }
    println!("notional, fee:  {}, {}", or(w.notional), or(w.fee));
    if let Some(m) = &w.margin {
        println!("margin:         {} needed, {} left after", m.required, or(m.available_after));
    }
    if let Some(p) = &w.position_after {
        let side = p.side.as_ref().map(|s| s.to_string()).unwrap_or_else(|| "flat".into());
        println!("position after: {side} {}", p.size);
    }
    Ok(())
}
