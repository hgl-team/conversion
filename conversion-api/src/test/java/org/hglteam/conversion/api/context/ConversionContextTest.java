package org.hglteam.conversion.api.context;

import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.Converter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.Closeable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConversionContextTest {

    public static final long EXPECTED_ARG_VALUE = 12342L;
    public static final String ARGUMENT_KEY = "entry";

    @Test
    void given_contextComponents_when_buildTypeConvertionContext_then_fieldMatches() {
        var expectedConverter = Mockito.mock(Converter.class);
        var expectedConvertionKey = Mockito.mock(ConversionKey.class);
        var expectedArguments = new HashMap<>();

        var context = ConversionContext.builder()
                .converter(expectedConverter)
                .currentConversionKey(expectedConvertionKey)
                .arguments(expectedArguments)
                .build();

        assertEquals(expectedConverter, context.getConverter());
        assertEquals(expectedConvertionKey, context.getCurrentConversionKey());
        assertEquals(expectedArguments, context.getArguments());
    }

    @Test
    void given_contextComponents_when_buildTypeConvertionContextFromBuilder_then_fieldMatches() {
        var expectedConverter = Mockito.mock(Converter.class);
        var expectedConvertionKey = Mockito.mock(ConversionKey.class);
        var expectedArguments = new HashMap<>();

        var context = ConversionContext.builder()
                .converter(expectedConverter)
                .currentConversionKey(expectedConvertionKey)
                .arguments(expectedArguments)
                .build();

        assertEquals(expectedConverter, context.getConverter());
        assertEquals(expectedConvertionKey, context.getCurrentConversionKey());
        assertEquals(expectedArguments, context.getArguments());

        var anotherArguments = new HashMap<>();

        context = context.toBuilder()
                .arguments(anotherArguments)
                .build();

        assertEquals(anotherArguments, context.getArguments());
    }

    @Test
    void should_returnString() {
        var value = ConversionContext.builder().toString();

        assertNotNull(value);
    }

    @Test
    void should_getArgument_when_argumentExists() {
        var context = ConversionContext.builder()
                .arguments(Map.ofEntries(
                        Map.entry(ARGUMENT_KEY, EXPECTED_ARG_VALUE)))
                .build();

        assertEquals(EXPECTED_ARG_VALUE, context.<Long>getArgument(ARGUMENT_KEY));
    }

    @Test
    void given_noArgumentInContext_when_getArgumentWithNullKey_then_returnNull() {
        var context = ConversionContext.builder()
                .arguments(Collections.emptyMap())
                .build();

        assertNull(context.<Long>getArgument(null));
    }

    @Test
    void given_noArgumentInContext_when_getArgument_then_returnNull() {
        var context = ConversionContext.builder()
                .arguments(Collections.emptyMap())
                .build();

        assertNull(context.<Long>getArgument(ARGUMENT_KEY));
    }

    @Test
    void given_noArgumentsInContext_when_getArgumentWithDifferentType_then_throwException() {
        var context = ConversionContext.builder()
                .arguments(Map.ofEntries(
                        Map.entry(ARGUMENT_KEY, EXPECTED_ARG_VALUE)))
                .build();

        assertThrows(ClassCastException.class, () -> this.whenGetArgumentAsCloseable(context));
    }

    private Closeable whenGetArgumentAsCloseable(ConversionContext context) {
        Closeable value = context.getArgument(ARGUMENT_KEY);

        return value;
    }
}