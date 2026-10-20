package phoenixperps

import (
	"encoding/json"
	"fmt"
	"math/big"
	"strconv"
)

// Decimal is a price, a size or an amount exactly as the API writes it: a decimal string ("62000.5"), never a float.
// It reads a JSON number too, and goes out as a string.
type Decimal string

// UnmarshalJSON takes a string or a number.
func (d *Decimal) UnmarshalJSON(b []byte) error {
	if len(b) > 0 && b[0] == '"' {
		var s string
		if err := json.Unmarshal(b, &s); err != nil {
			return err
		}
		*d = Decimal(s)
		return nil
	}
	var n json.Number
	if err := json.Unmarshal(b, &n); err != nil {
		return fmt.Errorf("phoenixperps: a decimal is a string or a number, not %s", b)
	}
	*d = Decimal(n.String())
	return nil
}

// Rat is the exact value (false when the string is not a decimal).
func (d Decimal) Rat() (*big.Rat, bool) {
	return new(big.Rat).SetString(string(d))
}

// String is the decimal as written.
func (d Decimal) String() string { return string(d) }

// DecimalOf writes a float in plain decimal form (the shortest that reads back the same, never an exponent).
func DecimalOf(f float64) Decimal { return Decimal(strconv.FormatFloat(f, 'f', -1, 64)) }

// Dec is a pointer to a Decimal, for optional fields: Dec("62000").
func Dec(s string) *Decimal {
	d := Decimal(s)
	return &d
}

// Ptr is a pointer to any value, for optional fields: Ptr(true), Ptr(TimeInForceGtc).
func Ptr[T any](v T) *T { return &v }
