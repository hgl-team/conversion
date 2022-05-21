package org.hglteam.conversion.api;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenericConversionKeyTest {
    private GenericConversionKey<?, ?> key;

    @Test
    void given_aConvetionKey_when_checkIfEqualsToSelf_then_returnTrue() {
        this.key = new GenericConversionKey<List<Integer>, String>(){};
        assertEquals(this.key, this.key);
    }

    @Test
    void given_aConvetionKey_when_checkIfEqualsToNull_then_returnFalse() {
        this.key = new GenericConversionKey<List<Integer>, String>(){};
        assertNotEquals(this.key, null);
    }

    @Test
    void given_aConvetionKey_when_checkIfEqualsToOtherTypeObject_then_returnFalse() {
        this.key = new GenericConversionKey<List<Integer>, String>(){};
        assertNotEquals(this.key, "any");
    }

    @Test
    void given_aConvetionKey_when_checkIfEqualsToAConvertionKey_then_returnFalse() {
        this.key = new GenericConversionKey<List<Integer>, String>(){};
        var otherDifferentSource = new GenericConversionKey<List<Double>, String>(){};
        var otherDifferentTarget = new GenericConversionKey<List<Integer>, Double>(){};
        var otherNullSourceTarget = DefaultConversionKey.builder().build();

        assertNotEquals(this.key, otherDifferentSource);
        assertNotEquals(this.key, otherDifferentTarget);
        assertNotEquals(this.key, otherNullSourceTarget);
    }

    @Test
    void given_aConvetionKey_when_checkIfHashCodeEqualsToAConvertionKey_then_resultExpected() {
        this.key = new GenericConversionKey<List<Integer>, String>(){};
        var otherEquals = new GenericConversionKey<List<Integer>, String>(){};

        var otherDifferentSource = new GenericConversionKey<List<Double>, String>(){};
        var otherDifferentTarget = new GenericConversionKey<List<Integer>, Double>(){};
        var otherNullSourceTarget = DefaultConversionKey.builder().build();

        assertEquals(this.key.hashCode(), otherEquals.hashCode());
        assertNotEquals(this.key.hashCode(), otherDifferentSource);
        assertNotEquals(this.key.hashCode(), otherDifferentTarget);
        assertNotEquals(this.key.hashCode(), otherNullSourceTarget);
    }

    @Test
    void should_throwException_when_subclass() {
        assertThrows(IllegalArgumentException.class, SecondaryConvertionKey::new);
    }

    private static class PrimaryConvertionKey extends GenericConversionKey<Integer, Double> {
    }

    private static class SecondaryConvertionKey extends PrimaryConvertionKey {
    }
}