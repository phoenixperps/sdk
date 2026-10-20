use crate::client::{Client, Operation};
use crate::{RawResponse, RequestOptions, Result};
use serde::de::DeserializeOwned;
use serde::Deserialize;
use serde_json::{Map, Value};

/// One page of a list: its items, whether more follow, the cursor to them, and the list's other fields.
#[derive(Clone, Debug, Deserialize)]
#[serde(bound(deserialize = "T: DeserializeOwned"))]
pub struct Page<T> {
    /// The items.
    #[serde(default = "Vec::new")]
    pub data: Vec<T>,
    /// More items follow.
    #[serde(default)]
    pub has_more: bool,
    /// The cursor to the next page.
    #[serde(default)]
    pub next_cursor: Option<String>,
    /// When the list was read (ms).
    #[serde(default)]
    pub as_of: Option<i64>,
    /// The list's other fields (the orders list's history_complete), and those this SDK version does not know yet.
    #[serde(flatten)]
    pub extra: Map<String, Value>,
    /// The answer the page came in.
    #[serde(skip)]
    pub raw: Option<RawResponse>,
    #[serde(skip)]
    pub(crate) next: Option<NextCall>,
}

#[derive(Clone, Debug)]
pub(crate) struct NextCall {
    pub(crate) client: Client,
    pub(crate) op: &'static Operation,
    pub(crate) path: Vec<String>,
    pub(crate) query: Vec<(String, String)>,
    pub(crate) options: RequestOptions,
}

impl<T: DeserializeOwned> Page<T> {
    /// The next page, or None after the last.
    pub async fn next_page(&self) -> Result<Option<Page<T>>> {
        let (Some(cursor), Some(next)) = (self.next_cursor.as_deref().filter(|c| !c.is_empty()), self.next.as_ref()) else {
            return Ok(None);
        };
        if !self.has_more {
            return Ok(None);
        }
        let op = next.op;
        let mut query: Vec<(String, String)> = next.query.iter().filter(|(k, _)| Some(k.as_str()) != op.cursor).cloned().collect();
        query.push((op.cursor.unwrap_or("cursor").to_string(), cursor.to_string()));
        let path: Vec<&str> = next.path.iter().map(String::as_str).collect();
        next.client.list(op, &path, Some(query), &next.options).await.map(Some)
    }
}
