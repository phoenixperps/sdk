//! 09: an account's history: the equity curve by day, the result of each trading day, closed positions and fills.
//!   cargo run --example 09-history
use futures_util::StreamExt;
use phoenixperps::{Client, HistoryEquityParams, PositionsListClosedParams, RequestOptions, Resolution};

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?;
    let account = client.accounts().list().await?.data[0].id.clone();

    let equity = client.history().equity(&account, Some(&HistoryEquityParams { resolution: Some(Resolution::Day) })).await?;
    println!("equity: {} points", equity.points.len());

    for d in &client.history().daily(&account).await?.data {
        println!("{}: {} trades, P&L {}, fees {}", d.date, d.trades, d.pnl, d.fees);
    }
    let params = PositionsListClosedParams { limit: Some(10), ..Default::default() };
    for p in &client.positions().list_closed(&account, Some(&params)).await?.data {
        println!("closed {} {} {}: {:?} -> {:?}, P&L {}", p.market, p.side, p.size, p.entry_price, p.exit_price, p.pnl);
    }

    let options = RequestOptions { max_items: Some(100), ..Default::default() }; // every page, at most 100
    let mut fills = Box::pin(client.fills().list_auto_paging(&account, None, &options));
    let mut n = 0;
    while let Some(fill) = fills.next().await {
        let f = fill?;
        n += 1;
        if n <= 5 {
            println!("fill {} {} @ {}", f.market, f.size, f.price);
        }
    }
    println!("{n} fills read");
    Ok(())
}
