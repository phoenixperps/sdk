// Generated from the API spec by generator/printers/java.js. Do not edit: run `node generator`.
package com.phoenixperpsfunding.sdk.gen;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** The take-profit and stop-loss legs of a position, changed together. */
public final class ExitsBatch {
    @JsonProperty("expected_position")
    private ExitsBatchExpectedPosition expectedPosition;

    @JsonProperty("expected_exits")
    private List<ExitsBatchExpectedExit> expectedExits;

    @JsonProperty("operations")
    private List<ExitsBatchOperation> operations;

    @JsonProperty("resize_with_position")
    private Boolean resizeWithPosition;

    /** The position you saw: { side, size }, or null. */
    public ExitsBatchExpectedPosition expectedPosition() {
        return expectedPosition;
    }

    /** Sets expected_position. */
    public ExitsBatch expectedPosition(ExitsBatchExpectedPosition value) {
        this.expectedPosition = value;
        return this;
    }

    /** Every leg you saw (an empty list when there were none): if anything differs now, nothing is done and the answer is 409 exits_changed with the state now. */
    public List<ExitsBatchExpectedExit> expectedExits() {
        return expectedExits;
    }

    /** Sets expected_exits. */
    public ExitsBatch expectedExits(List<ExitsBatchExpectedExit> value) {
        this.expectedExits = value;
        return this;
    }

    /** Applied in this order: cancels, changes, then new legs (stop-losses first). An engine refusal stops the batch: 422 exit_operation_failed says what each operation did. */
    public List<ExitsBatchOperation> operations() {
        return operations;
    }

    /** Sets operations. */
    public ExitsBatch operations(List<ExitsBatchOperation> value) {
        this.operations = value;
        return this;
    }

    /** The legs grow with the position (default true). */
    public Boolean resizeWithPosition() {
        return resizeWithPosition;
    }

    /** Sets resize_with_position. */
    public ExitsBatch resizeWithPosition(Boolean value) {
        this.resizeWithPosition = value;
        return this;
    }

    @JsonIgnore
    private final Map<String, Object> extra = new LinkedHashMap<>();

    /** Fields this SDK version does not know yet (a newer API's), kept as they came. */
    @JsonAnyGetter
    public Map<String, Object> extra() {
        return extra;
    }

    @JsonAnySetter
    void putExtra(String key, Object value) {
        extra.put(key, value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ExitsBatch)) {
            return false;
        }
        ExitsBatch o = (ExitsBatch) other;
        return Objects.equals(expectedPosition, o.expectedPosition)
            && Objects.equals(expectedExits, o.expectedExits)
            && Objects.equals(operations, o.operations)
            && Objects.equals(resizeWithPosition, o.resizeWithPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectedPosition, expectedExits, operations, resizeWithPosition);
    }

    @Override
    public String toString() {
        return "ExitsBatch{" + "expected_position=" + expectedPosition + ", expected_exits=" + expectedExits + ", operations=" + operations + ", resize_with_position=" + resizeWithPosition + "}";
    }
}
