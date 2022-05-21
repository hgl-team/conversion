package org.hglteam.conversion.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultConversionKeyTest {

    private DefaultConversionKey key;

    @Test
    void given_aConvetionKey_when_checkIfEqualsToSelf_then_returnTrue() {
        this.key = DefaultConversionKey.builder()
                .source(Integer.class)
                .target(String.class)
                .build();
        assertEquals(this.key, this.key);
    }

    @Test
    void given_aConvetionKey_when_checkIfEqualsToNull_then_returnFalse() {
        this.key = DefaultConversionKey.builder()
                .source(Integer.class)
                .target(String.class)
                .build();
        assertNotEquals(this.key, null);
    }

    @Test
    void given_aConvetionKey_when_checkIfEqualsToOtherTypeObject_then_returnFalse() {
        this.key = DefaultConversionKey.builder()
                .source(Integer.class)
                .target(String.class)
                .build();
        assertNotEquals(this.key, "any");
    }

    @Test
    void given_aConvetionKey_when_checkIfEqualsToAConvertionKey_then_returnFalse() {
        this.key = DefaultConversionKey.builder()
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

        assertNotEquals(this.key, otherDifferentSource);
        assertNotEquals(this.key, otherDifferentTarget);
        assertNotEquals(this.key, otherNullSourceTarget);
    }

    @Test
    void given_aConvetionKey_when_checkIfHashCodeEqualsToAConvertionKey_then_resultExpected() {
        this.key = DefaultConversionKey.builder()
                .source(Integer.class)
                .target(String.class)
                .build();
        var otherEquals = DefaultConversionKey.builder()
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

        assertEquals(this.key.hashCode(), otherEquals.hashCode());
        assertNotEquals(this.key.hashCode(), otherDifferentSource);
        assertNotEquals(this.key.hashCode(), otherDifferentTarget);
        assertNotEquals(this.key.hashCode(), otherNullSourceTarget);
    }

    @Test
    void given_convertionKey_when_buildConvertionKey_then_fieldMatches() {
        var expectedSource = Integer.class;
        var expectedTarget = String.class;

        var key = DefaultConversionKey.builder()
                .source(expectedSource)
                .target(expectedTarget)
                .build();

        assertEquals(expectedSource, key.getSource());
        assertEquals(expectedTarget, key.getTarget());

        key = key.toBuilder()
                .target(Double.class)
                .build();

        assertEquals(Double.class, key.getTarget());
    }

    @Test
    void shoudl_returnString() {
        var value = DefaultConversionKey.builder().toString();

        assertNotNull(value);
    }

}