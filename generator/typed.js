'use strict';

/*
 * The spec's schemas as named types, for the printers of typed languages (Go, C#, Java, Rust): every component schema
 * and every inline object becomes a struct; every enum becomes an open enum (unknown values are kept, never fatal).
 *
 *   model(ir) -> { structs: [{ name, description, fields: [{ wire, description, type, required }] }],
 *                  enums: [{ name, values, description }] }
 *   type: { kind, nullable } with kind one of
 *     string | int | bool | number | decimal (a string on the wire) | decimalIn (an input: string or number) |
 *     enum (name) | ref (name: a struct) | array (of: type) | map (free object) | raw (any JSON)
 *
 * Names: an inline object is its parent's name and its field's (AccountRisk.exposure -> AccountRiskExposure; an array's
 * items take the field's singular: AccountRiskExposure.markets -> AccountRiskExposureMarket). An enum is named after
 * its field when the same values appear in two places or more and no other enum has that field (time_in_force ->
 * TimeInForce), else after its owner and field (Strategy.status -> StrategyStatus). Names the cores use are never
 * given out (RESERVED). Nothing depends on the order of the paths.
 */
const pascal = (s) => String(s).split(/[_\s.-]+/).filter(Boolean).map((w) => w.charAt(0).toUpperCase() + w.slice(1)).join('');
const singular = (s) => (/[^su]s$/.test(s) ? s.slice(0, -1) : s);
const RESERVED = ['Client', 'Error', 'Kind', 'Page', 'Decimal', 'Option', 'Options', 'Response', 'Result', 'Stream', 'Code', 'Op', 'Type', 'Format', 'Status', 'Value', 'Level', 'Access', 'Stage', 'Engine', 'Api', 'Webhooks', 'Environment', 'MarketData', 'Basis', 'Event'];
// schemas whose own name is a core's (the error envelope is not the SDKs' Error)
const RENAMED = { Error: 'ErrorEnvelope' };
// value sets named outright: an order's type is OrderType wherever it appears (an exit leg's too), not after the first
// schema that happens to carry it
const ENUM_NAMES = { 'type=market|limit|stop_market|stop_limit|take_market|take_limit': 'OrderType' };
const typeName = (n) => RENAMED[n] || n;

function model(ir) {
  const structs = [];
  const enums = [];
  const taken = new Set([...Object.keys(ir.schemas).map(typeName), ...RESERVED]);
  const unique = (name) => {
    let n = name;
    for (let i = 2; taken.has(n); i += 1) n = `${name}${i}`;
    taken.add(n);
    return n;
  };

  // enum names: field -> the distinct value sets it has anywhere; field=values -> how many places use it
  const setsByField = new Map();
  const places = new Map();
  const visit = (schema, field) => {
    if (!schema || typeof schema !== 'object') return;
    if (schema.enum && field) {
      const key = schema.enum.filter((v) => v !== null).join('|');
      if (!setsByField.has(field)) setsByField.set(field, new Set());
      setsByField.get(field).add(key);
      places.set(`${field}=${key}`, (places.get(`${field}=${key}`) || 0) + 1);
    }
    for (const [k, v] of Object.entries(schema.properties || {})) visit(v, k);
    if (schema.items) visit(schema.items, singular(field || 'item'));
  };
  for (const s of Object.values(ir.schemas)) visit(s, null);
  for (const op of ir.operations) for (const q of op.query) visit(q.schema, q.name);
  const enumByKey = new Map();
  const enumFor = (schema, owner, field) => {
    const values = schema.enum.filter((v) => v !== null);
    const key = `${field}=${values.join('|')}`;
    if (enumByKey.has(key)) return enumByKey.get(key);
    const shared = setsByField.get(field).size === 1 && places.get(key) > 1 && !taken.has(pascal(field));
    const name = unique(ENUM_NAMES[key] || (shared ? pascal(field) : `${owner}${pascal(field)}`));
    enums.push({ name, values, description: schema.description || '' });
    enumByKey.set(key, name);
    return name;
  };

  const typeOf = (schema, owner, field) => {
    if (!schema) return { kind: 'raw', nullable: true };
    if (schema.$ref) {
      const name = schema.$ref.split('/').pop();
      const target = ir.schemas[name];
      const nullable = Boolean(target && Array.isArray(target.type) && target.type.includes('null'));
      return { kind: 'ref', name: typeName(name), nullable };
    }
    const types = Array.isArray(schema.type) ? schema.type : schema.type ? [schema.type] : [];
    const nullable = types.includes('null');
    const base = types.filter((t) => t !== 'null');
    if (schema.enum) return { kind: 'enum', name: enumFor(schema, owner, field), nullable };
    if (base.includes('string') && base.includes('number')) return { kind: 'decimalIn', nullable };
    if (base.length > 1) return { kind: 'raw', nullable };
    const t = base[0] || (schema.properties ? 'object' : null);
    if (t === 'string') return { kind: schema.format === 'decimal' ? 'decimal' : 'string', nullable };
    if (t === 'integer') return { kind: 'int', nullable };
    if (t === 'number') return { kind: 'number', nullable };
    if (t === 'boolean') return { kind: 'bool', nullable };
    if (t === 'array') return { kind: 'array', of: typeOf(schema.items, owner, singular(field || 'item')), nullable };
    if (t === 'object' && schema.properties) return { kind: 'ref', name: struct(schema, unique(`${owner}${pascal(field)}`)), nullable };
    if (t === 'object') return { kind: 'map', nullable };
    return { kind: 'raw', nullable: true };
  };

  function struct(schema, name) {
    const s = { name, description: schema.description || '', fields: [] };
    structs.push(s);
    const req = new Set(schema.required || []);
    for (const [wire, v] of Object.entries(schema.properties || {})) {
      s.fields.push({ wire, description: v.description || '', type: typeOf(v, name, wire), required: req.has(wire) });
    }
    return name;
  }

  for (const [name, schema] of Object.entries(ir.schemas)) struct(schema, typeName(name));
  // query parameters: one struct per operation that has any (<Resource><Method>Params)
  const params = {};
  for (const op of ir.operations) {
    if (!op.query.length) continue;
    const name = unique(`${pascal(op.resource)}${pascal(op.method)}Params`);
    params[op.id] = name;
    structs.push({ name, description: `The query of ${op.summary.toLowerCase() || op.id}.`, query: true, fields: op.query.map((q) => ({ wire: q.name, description: q.description, type: typeOf(q.schema, name, q.name), required: false })) });
  }
  return { structs, enums, params };
}

module.exports = { model, pascal, singular, typeName };
