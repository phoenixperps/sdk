package phoenixperps

import (
	"context"
	"encoding/json"
	"iter"
	"net/http"
	"net/url"
)

// Page is one page of a list: its items, whether more follow, and the cursor to them.
type Page[T any] struct {
	Data       []T     `json:"data"`
	HasMore    bool    `json:"has_more"`
	NextCursor *string `json:"next_cursor"`
	AsOf       *int64  `json:"as_of"`
	// HistoryComplete is false when the history reaches further back than the list covers (the orders list; nil on
	// the others).
	HistoryComplete *bool `json:"history_complete"`
	// Raw is the answer the page came in.
	Raw  *RawResponse `json:"-"`
	next func(ctx context.Context, cursor string) (*Page[T], error)
}

// GetNextPage is the next page, or nil after the last.
func (p *Page[T]) GetNextPage(ctx context.Context) (*Page[T], error) {
	if p == nil || !p.HasMore || p.NextCursor == nil || *p.NextCursor == "" || p.next == nil {
		return nil, nil
	}
	return p.next(ctx, *p.NextCursor)
}

func listPage[T any](ctx context.Context, c *Client, op *operation, path []string, query url.Values, opts []CallOption) (*Page[T], error) {
	res, err := c.send(ctx, op, path, query, nil, opts)
	if err != nil || res.StatusCode == http.StatusNotModified { // 304: the caller's copy is current, no page
		return nil, err
	}
	page := &Page[T]{Raw: res}
	if err := json.Unmarshal(res.Body, page); err != nil {
		return nil, &Error{Kind: KindServer, Status: res.StatusCode, Code: "invalid_response", Message: "The answer could not be read: " + err.Error(), RequestID: res.RequestID, Err: err}
	}
	if op.cursor != "" {
		page.next = func(ctx context.Context, cursor string) (*Page[T], error) {
			q := url.Values{}
			for k, v := range query {
				q[k] = v
			}
			q.Set(op.cursor, cursor)
			return listPage[T](ctx, c, op, path, q, opts)
		}
	}
	return page, nil
}

// autoPaging goes over every item of every page; an error ends it (yielded once).
func autoPaging[T any](ctx context.Context, first func() (*Page[T], error), opts []CallOption) iter.Seq2[T, error] {
	var cfg callConfig
	for _, o := range opts {
		o(&cfg)
	}
	return func(yield func(T, error) bool) {
		page, err := first()
		n := 0
		for {
			if err != nil {
				var zero T
				yield(zero, err)
				return
			}
			if page == nil {
				return
			}
			for _, item := range page.Data {
				if cfg.maxItems > 0 && n >= cfg.maxItems {
					return
				}
				n++
				if !yield(item, nil) {
					return
				}
			}
			if cfg.maxItems > 0 && n >= cfg.maxItems {
				return
			}
			page, err = page.GetNextPage(ctx)
		}
	}
}
