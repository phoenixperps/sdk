//! 11: receive webhooks: every delivery is checked (its signature and its time) before it is trusted.
//!   PHOENIXPERPS_WEBHOOK_SECRET=ppwhsec_... cargo run --example 11-webhook-receiver
//! Then point a webhook endpoint (dashboard, Developers, Webhooks) at https://<this machine>/webhooks (port 8080 here).
//! A plain HTTP/1.1 reader on std only, to keep the example to the SDK; a real service would use its web framework.
use std::io::{BufRead, BufReader, Read, Write};
use std::net::TcpListener;

use phoenixperps::verify_webhook;

fn main() -> std::io::Result<()> {
    let secret = std::env::var("PHOENIXPERPS_WEBHOOK_SECRET").expect("Set PHOENIXPERPS_WEBHOOK_SECRET to the endpoint's signing secret.");
    let listener = TcpListener::bind("0.0.0.0:8080")?;
    println!("listening on :8080/webhooks");
    for stream in listener.incoming() {
        let mut stream = stream?;
        let mut reader = BufReader::new(stream.try_clone()?);
        let mut request_line = String::new();
        reader.read_line(&mut request_line)?;
        let mut headers: Vec<(String, String)> = Vec::new();
        loop {
            let mut line = String::new();
            reader.read_line(&mut line)?;
            let line = line.trim_end();
            if line.is_empty() {
                break;
            }
            if let Some((k, v)) = line.split_once(':') {
                headers.push((k.trim().to_ascii_lowercase(), v.trim().to_string()));
            }
        }
        let length = headers.iter().find(|(k, _)| k == "content-length").and_then(|(_, v)| v.parse().ok()).unwrap_or(0usize);
        let mut body = vec![0; length.min(1 << 20)];
        reader.read_exact(&mut body)?;
        let status = if !request_line.starts_with("POST /webhooks ") {
            "404 Not Found"
        } else {
            match verify_webhook(&String::from_utf8_lossy(&body), headers, &[secret.as_str()], None, None) {
                Ok(event) => {
                    // webhook.ping (a test) names no account
                    println!("{} on {}: {:?}", event.r#type, event.account_id.as_deref().unwrap_or("-"), event.data);
                    "200 OK"
                }
                Err(err) => {
                    println!("refused a delivery: {}", err.code);
                    "400 Bad Request"
                }
            }
        };
        write!(stream, "HTTP/1.1 {status}\r\ncontent-length: 0\r\nconnection: close\r\n\r\n")?;
    }
    Ok(())
}
