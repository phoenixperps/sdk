package com.phoenixperpsfunding.sdk;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * The JSON of the API: fields by their wire names, nulls left out, unknown fields kept (extra()), decimals as strings
 * exactly as given (never an exponent) and read from strings or numbers.
 */
public final class Json {
    private Json() {
    }

    /** The mapper every call uses. */
    public static final ObjectMapper MAPPER = create();

    private static ObjectMapper create() {
        SimpleModule decimals = new SimpleModule("phoenixperps-decimals");
        decimals.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toPlainString());
            }
        });
        ObjectMapper m = new ObjectMapper();
        m.registerModule(decimals);
        m.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        m.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        m.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        m.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        return m;
    }
}
