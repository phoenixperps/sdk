package phoenixperps

import (
	"encoding/json"
	"fmt"
	"math/big"
	"regexp"
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

// decimalForm is a decimal as every SDK reads it (BEHAVIOUR.md): a sign, digits with a fraction, an exponent; nothing
// else (big.Rat alone would take 1/3 and 0x10: Q97).
var decimalForm = regexp.MustCompile(`^-?(\d+\.?\d*|\.\d+)(?:[eE]([+-]?\d+))?$`)

// maxExponent: past any float64 (1.8e308), no price or size (and no exponent big.Rat would spend memory on).
const maxExponent = 400

// Rat is the exact value (false when the string is not a decimal).
func (d Decimal) Rat() (*big.Rat, bool) {
	m := decimalForm.FindStringSubmatch(string(d))
	if m == nil {
		return nil, false
	}
	if m[2] != "" {
		if e, err := strconv.Atoi(m[2]); err != nil || e > maxExponent || e < -maxExponent {
			return nil, false
		}
	}
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

// withNulls adds "name": null to a marshaled object for each name set (a request field the API takes as null).
func withNulls(b []byte, set map[string]bool) ([]byte, error) {
	var m map[string]json.RawMessage
	if err := json.Unmarshal(b, &m); err != nil {
		return nil, err
	}
	for k, on := range set {
		if on {
			m[k] = json.RawMessage("null")
		}
	}
	return json.Marshal(m)
}
