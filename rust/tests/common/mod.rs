pub mod dispatch;

use futures_util::{Stream, StreamExt};
use phoenixperps::Result;
use serde::de::DeserializeOwned;
use serde::Serialize;
use serde_json::Value;

/// A call a conformance scenario names: resource, method, path, body, query.
pub struct Call {
    pub resource: String,
    pub method: String,
    pub iterate: bool,
    pub max_items: Option<usize>,
    raw: Value,
}

impl Call {
    pub fn new(raw: &Value) -> Self {
        Self {
            resource: raw["resource"].as_str().unwrap().to_string(),
            method: raw["method"].as_str().unwrap().to_string(),
            iterate: raw["iterate"].as_bool().unwrap_or(false),
            max_items: raw["max_items"].as_u64().map(|n| n as usize),
            raw: raw.clone(),
        }
    }

    pub fn path(&self, name: &str) -> String {
        self.raw["path"][name].as_str().unwrap().to_string()
    }

    pub fn body<T: DeserializeOwned + Default>(&self) -> T {
        self.raw.get("body").map(|b| serde_json::from_value(b.clone()).unwrap()).unwrap_or_default()
    }

    pub fn query<T: DeserializeOwned>(&self) -> Option<T> {
        self.raw.get("query").map(|q| serde_json::from_value(q.clone()).unwrap())
    }
}

/// What a dispatch answers: the result as JSON, or the ids of the items iterated.
pub enum Answer {
    Value(Value),
    Items(Vec<String>),
}

pub async fn ids<T: Serialize>(items: impl Stream<Item = Result<T>> + Unpin) -> Result<Vec<String>> {
    let mut items = items;
    let mut out = Vec::new();
    while let Some(item) = items.next().await {
        out.push(serde_json::to_value(item?).unwrap()["id"].as_str().unwrap_or("").to_string());
    }
    Ok(out)
}
