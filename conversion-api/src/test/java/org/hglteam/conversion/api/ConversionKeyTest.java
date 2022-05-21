package org.hglteam.conversion.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConversionKeyTest {

    @Test
    void when_checkIfNullEqualsToNull_then_returnTrue() {
        assertTrue(ConversionKey.areEqual(null, null));
    }

    @Test
    void given_aConvetionKey_when_checkIfEqualsToSelf_then_returnTrue() {
        var key = DefaultConversionKey.builder()
                .source(Integer.class)
                .target(String.class)
                .build();
        assertTrue(ConversionKey.areEqual(key, key));
    }

    @Test
    void given_aConvetionKey_when_checkIfEqualsToNull_then_returnFalse() {
        var key = DefaultConversionKey.builder()
                .source(Integer.class)
                .target(String.class)
                .build();
        assertFalse(ConversionKey.areEqual(key, null));
        assertFalse(ConversionKey.areEqual(null, key));
    }

    @Test
    void given_aConvetionKey_when_checkIfEqualsToAConvertionKey_then_returnFalse() {
        var key = DefaultConversionKey.builder()
                .source(Integer.class)
                .target(String.class)
                .build();
        var otherDifferentSource = DefaultConversionKey.builder()
                .source(Double.class)
                .target(String.class)
                .build();
        var otherDifferentTarget = DefaultConversionKey.builder()
                .source(Integer.class)
                .target(Double.class)
                .build();
        var otherNullSourceTarget = DefaultConversionKey.builder().build();

        assertFalse(ConversionKey.areEqual(key, otherDifferentSource));
        assertFalse(ConversionKey.areEqual(key, otherDifferentTarget));
        assertFalse(ConversionKey.areEqual(key, otherNullSourceTarget));
    }
}