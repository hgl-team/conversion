package org.hglteam.conversion;

import org.hglteam.conversion.api.TypeDescriptor;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeMatcherTest {
    @Test
    void given_class_when_checkIfMatches_then_result_expected() {
        var type = CustomList.class;

        assertTrue(TypeMatcher.isAssignableFrom(type, Object.class));
        assertTrue(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<List<Number>>(){}.getType()));
        assertTrue(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<AbstractCollection<Number>>(){}.getType()));
        assertTrue(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<Iterable<Object>>(){}.getType()));
        assertTrue(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<List<? extends Number>>(){}.getType()));

        assertFalse(TypeMatcher.isAssignableFrom(type, String.class));
        assertFalse(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<Set<Number>>(){}.getType()));
        assertFalse(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<List<Integer>>(){}.getType()));
    }

    @Test
    void given_type_when_checkIfMatches_then_result_expected() {
        var type = new TypeDescriptor<ArrayList<Integer>>(){}.getType();

        assertTrue(TypeMatcher.isAssignableFrom(type, Object.class));
        assertTrue(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<List<Number>>(){}.getType()));
        assertTrue(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<AbstractCollection<Number>>(){}.getType()));
        assertTrue(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<Iterable<Object>>(){}.getType()));
        assertTrue(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<List<? extends Number>>(){}.getType()));

        assertFalse(TypeMatcher.isAssignableFrom(type, String.class));
        assertFalse(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<Set<Number>>(){}.getType()));
        assertFalse(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<List<Double>>(){}.getType()));
        assertFalse(TypeMatcher.isAssignableFrom(type, new TypeDescriptor<Map<Number, String>>(){}.getType()));
    }

    @Test
    void given_type_when_checkIfMatchesTwice_then_result_expected() {
        var type = new TypeDescriptor<ArrayList<Integer>>(){}.getType();

        assertTrue(TypeMatcher.isAssignableFrom(type, Object.class));
        assertTrue(TypeMatcher.isAssignableFrom(type, Object.class));
    }

    public static class CustomList extends ArrayList<Number> {
    }
}
