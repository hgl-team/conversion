package org.hglteam.conversion.api;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class DuplicatedConversionKeyExceptionTest {
    private Function<ConversionKey, String> expectedFormatter = conversionKey ->
            String.format("(%s): %s -> %s",
                    conversionKey,
                    conversionKey.getSource(),
                    conversionKey.getTarget());

    @Test
    void should_haveKeyTypesInMessage_when_creatException() {
        var key = new GenericConversionKey<Integer, String>(){};
        var exception = new ConversionMap.DuplicatedConversionKeyException(key);

        assertEquals(expectedFormatter.apply(key), exception.getMessage());
    }

}