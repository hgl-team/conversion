package org.hglteam.conversion.datetime;

import org.hglteam.conversion.datetime.LocalizedDatePatternKey;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LocalizedDatePatternKeyTest {

    public static final String EXPECTED_FORMAT = "yyyy-MM-dd";

    @Test
    void given_dateKey_should_matchFields() {
        var key = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);

        assertEquals(EXPECTED_FORMAT, key.getPattern());
        assertEquals(Locale.GERMANY, key.getLocale());
    }

    @Nested
    class EqualsTest {

        @Test
        void given_dateKey_should_returnFalse_when_checkEqualsWithSelf() {
            var key = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);
            assertEquals(key, key);
        }

        @Test
        void given_dateKey_should_returnFalse_when_checkEqualsWithNull() {
            var key = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);
            assertFalse(key.equals(null));
        }

        @Test
        void given_dateKey_should_returnFalse_when_checkEqualsWithAnyObject() {
            var key = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);
            assertFalse(key.equals("any"));
        }

        @Test
        void given_dateKey_should_returnTrue_when_checkEqualsWithOther() {
            var key = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);
            var copy = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);
            assertEquals(key, copy);
        }

        @Test
        void given_dateKey_should_returnTrue_when_checkEqualsWithOtherDifferentPattern() {
            var key = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);
            var copy = new LocalizedDatePatternKey("different", Locale.GERMANY);
            assertNotEquals(key, copy);
        }

        @Test
        void given_dateKey_should_returnTrue_when_checkEqualsWithOtherDifferentLocale() {
            var key = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);
            var copy = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.CANADA);
            assertNotEquals(key, copy);
        }

        @Test
        void given_dateKey_should_returnTrue_when_checkEqualsWithDifferentOther() {
            var key = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);
            var copy = new LocalizedDatePatternKey("different", Locale.CANADA);
            assertNotEquals(key, copy);
        }
    }

    @Nested
    class HashCodeTest {
        @Test
        void given_dateKey_should_returnTrue_when_checkHashCodeWithOther() {
            var key = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);
            var copy = new LocalizedDatePatternKey(EXPECTED_FORMAT, Locale.GERMANY);
            assertEquals(key.hashCode(), copy.hashCode());
        }
    }

}