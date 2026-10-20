//! 01: who you are (the key, its access and limits) and your accounts.
//!   PHOENIXPERPS_API_KEY=ppk_test_... cargo run --example 01-accounts
use phoenixperps::Client;

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?; // the key: PHOENIXPERPS_API_KEY (a sandbox key: the sandbox)

    let me = client.me().get().await?;
    let c = &me.credential;
    println!("{} \"{}\" on {}: {} access", c.kind, c.name, c.environment, c.access);
    println!("limits: {} reads and {} trading calls a minute", me.budgets.read_per_min, me.budgets.trade_per_min);

    for a in &client.accounts().list().await?.data {
        let balance = a.balance.map(|b| b.to_string()).unwrap_or_else(|| "-".into());
        println!("{}  {}  {}  balance {}", a.id, a.stage, a.status, balance);
    }
    Ok(())
}
