//! 08: a live copy of your accounts' positions and working orders: the snapshot first, then every event applied once.
//! After a reconnect the SDK resumes from the last event; when the server cannot cover that, the stream tells a gap
//! and the copy reads what it missed by REST. Ctrl+C to stop.
//!   cargo run --example 08-account-stream-mirror
use std::collections::HashMap;

use futures_util::StreamExt;
use phoenixperps::{AccountMessage, AccountStreamOptions, Client, Event, EventsListParams, RequestOptions};
use serde_json::Value;

#[derive(Default)]
struct Copy {
    positions: HashMap<String, Value>, // by market
    orders: HashMap<String, Value>,    // by id
}

const FINAL: [&str; 4] = ["filled", "canceled", "rejected", "expired"];

fn show(mirror: &HashMap<String, Copy>, why: &str) {
    for (id, c) in mirror {
        let positions: Vec<String> = c.positions.values().map(|p| format!("{} {} {}", p["market"], p["side"], p["size"])).collect();
        let positions = if positions.is_empty() { "none".to_string() } else { positions.join(", ") };
        println!("[{why}] {id}: positions {positions}; {} working orders", c.orders.len());
    }
}

fn apply(mirror: &mut HashMap<String, Copy>, event: &Event) {
    let Some(c) = mirror.get_mut(&event.account_id) else { return };
    if let Some(order) = event.data.get("order") {
        let id = order["id"].as_str().unwrap_or_default().to_string();
        if FINAL.contains(&order["status"].as_str().unwrap_or_default()) {
            c.orders.remove(&id);
        } else {
            c.orders.insert(id, order.clone());
        }
    }
    if let Some(position) = event.data.get("position") {
        let market = position["market"].as_str().unwrap_or_default().to_string();
        if event.r#type.to_string() == "position.closed" {
            c.positions.remove(&market);
        } else {
            c.positions.insert(market, position.clone());
        }
    }
    show(mirror, &event.r#type.to_string());
}

#[tokio::main]
async fn main() -> phoenixperps::Result<()> {
    let client = Client::from_env()?;
    let accounts = client.accounts().list().await?;
    let mut stream = client.account_stream(AccountStreamOptions::default());
    stream.ready().await;

    let mut mirror: HashMap<String, Copy> = HashMap::new();
    for a in &accounts.data {
        let Some(snap) = stream.snapshot(&a.id) else { continue };
        if let Some(why) = snap["error"].as_str() {
            // not readable now: an empty snapshot here is not a flat account
            println!("[snapshot] {}: unreadable ({why}), read it again by REST", a.id);
            continue;
        }
        let mut c = Copy::default();
        for p in snap["positions"].as_array().into_iter().flatten() {
            c.positions.insert(p["market"].as_str().unwrap_or_default().to_string(), p.clone());
        }
        for o in snap["orders"].as_array().into_iter().flatten() {
            c.orders.insert(o["id"].as_str().unwrap_or_default().to_string(), o.clone());
        }
        mirror.insert(a.id.clone(), c);
    }
    show(&mirror, "snapshot");

    while let Some(message) = stream.next_message().await {
        match message? {
            AccountMessage::Event(e) => apply(&mut mirror, &e.event),
            AccountMessage::Gap { after } => {
                // events may be missing here: read them by REST, after the last one told
                let (params, options) = (EventsListParams { after, ..Default::default() }, RequestOptions::default());
                let mut missed = client.events().list_auto_paging(Some(&params), &options);
                while let Some(event) = missed.next().await {
                    apply(&mut mirror, &event?);
                }
            }
            _ => {} // risk frames (AccountStreamOptions::risk)
        }
    }
    Ok(())
}
