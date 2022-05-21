package org.hglteam.conversion.api;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class NoCompatibleKeyFoundExceptionTest {
    private Function<ConversionKey, String> expectedFormatter = conversionKey ->
            String.format("%s -> %s",
                    conversionKey.getSource(),
                    conversionKey.getTarget());
    @Test
    void should_containKey_when_createFromConversionKey() {
        var key = new GenericConversionKey<Integer, String>(){};
        var exception = ConversionMap.NoCompatibleKeyFoundException.forKey(key)
                .get();

        assertEquals(expectedFormatter.apply(key), exception.getMessage());
    }
}