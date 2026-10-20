//! 12: what failures look like: each has a kind (its family), the API's code, the request id to quote to support, and
//! details. A write whose outcome is unknown (a timeout) is repeated with the same Idempotency-Key, never a new one.
//!   cargo run --example 12-error-handling
use phoenixperps::{dec, Client, CreateOrder, CreateOrderType, Error, ErrorKind, Order, OrderSide, RequestOptions};

fn show(err: &Error) {
    println!("{:?}: {} (HTTP {:?}, {:?}) {}", err.kind, err.code, err.status, err.request_id, err.message);
    if let Some(details) = &err.details {
        println!("  details: {details}");
    }
}

/// A write that may or may not have happened: the same key again is safe, a new key could place it twice.
pub async fn place_once(client: &Client, account: &str, body: &CreateOrder) -> phoenixperps::Result<Order> {
    let options = RequestOptions { idempotency_key: Some(uuid::Uuid::new_v4().to_string()), ..Default::default() };
    let mut attempt = 1;
    loop {
        match client.orders().create_with(account, body, &options).await {
            Err(err) if attempt < 3 && matches!(err.kind, ErrorKind::Timeout) => {} // still unknown: the same key
            Err(err) if attempt < 3 && matches!(err.kind, ErrorKind::RateLimit) && err.retry_after.is_some() => {
                tokio::time::sleep(err.retry_after.unwrap_or_default()).await;
            }
            done => return done,
        }
        attempt += 1;
    }
}

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?;
    let account = client.accounts().list().await?.data[0].id.clone();

    match client.orders().get(&account, "ord_does_not_exist").await {
        Err(err) if matches!(err.kind, ErrorKind::NotFound) => show(&err),
        other => {
            other?;
        }
    }

    let tiny = CreateOrder { market: "BTC-USD".into(), side: OrderSide::Buy, r#type: CreateOrderType::Market, size: Some(dec("0.0000001")), ..Default::default() };
    match client.orders().preview(&account, &tiny).await {
        Err(err) if matches!(err.kind, ErrorKind::Validation | ErrorKind::RuleRejected) => show(&err),
        other => {
            other?;
        }
    }
    Ok(())
}
