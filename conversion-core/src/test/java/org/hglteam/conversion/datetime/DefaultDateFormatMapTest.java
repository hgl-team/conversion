package org.hglteam.conversion.datetime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DefaultDateFormatMapTest {
    private static final String EXPECTED_PATTERN = "E, dd MMM yyyy HH:mm:ss";
    private static final Locale EXPECTED_LOCALE = Locale.GERMANY;
    public static final String ANOTHER_DATE_FORMAT = "yyyy-MM-dd";

    private DefaultDateFormatMap formatMap;

    @BeforeEach
    public void setup() {
        this.formatMap = new DefaultDateFormatMap();
    }

    @Nested
    class RegisterLegacyTest {
        @Test
        void given_aLegacyPattern_when_resolve_thenGetAMatchingFormatter() {
            formatMap.registerLegacy(EXPECTED_PATTERN, EXPECTED_LOCALE);
            var expectedLegacyFormatter = new SimpleDateFormat(EXPECTED_PATTERN, EXPECTED_LOCALE);
            var resolved = formatMap.resolveLegacyFormatter(EXPECTED_PATTERN, EXPECTED_LOCALE);
            var date = new Date();

            assertNotNull(resolved);
            assertEquals(expectedLegacyFormatter.format(date), resolved.format(date));
        }

        @Test
        void given_aLegacyPattern_when_resolveSamePattern_thenReturnNull() {
            formatMap.registerLegacy(EXPECTED_PATTERN, EXPECTED_LOCALE);

            var resolved = formatMap.resolveLegacyFormatter(EXPECTED_PATTERN, Locale.CANADA);

            assertNull(resolved);
        }

        @Test
        void given_aLegacyPattern_when_resolveLocale_thenReturnNull() {
            formatMap.registerLegacy(EXPECTED_PATTERN, EXPECTED_LOCALE);

            var resolved = formatMap.resolveLegacyFormatter(ANOTHER_DATE_FORMAT, EXPECTED_LOCALE);

            assertNull(resolved);
        }

        @Test
        void given_aLegacyPattern_when_resolve_thenReturnNull() {
            var resolved = formatMap.resolveLegacyFormatter(EXPECTED_PATTERN, EXPECTED_LOCALE);
            assertNull(resolved);
        }
    }

    @Nested
    class RegisterTest {
        @Test
        void given_aLegacyPattern_when_resolve_thenGetAMatchingFormatter() {
            formatMap.register(EXPECTED_PATTERN, EXPECTED_LOCALE);
            var expectedLegacyFormatter = DateTimeFormatter.ofPattern(EXPECTED_PATTERN, EXPECTED_LOCALE);
            var resolved = formatMap.resolveFormatter(EXPECTED_PATTERN, EXPECTED_LOCALE);
            var date = LocalDateTime.now();

            assertNotNull(resolved);
            assertEquals(expectedLegacyFormatter.format(date), resolved.format(date));
        }

        @Test
        void given_aLegacyPattern_when_resolveSamePattern_thenReturnNull() {
            formatMap.register(EXPECTED_PATTERN, EXPECTED_LOCALE);

            var resolved = formatMap.resolveFormatter(EXPECTED_PATTERN, Locale.CANADA);

            assertNull(resolved);
        }

        @Test
        void given_aLegacyPattern_when_resolveLocale_thenReturnNull() {
            formatMap.register(EXPECTED_PATTERN, EXPECTED_LOCALE);

            var resolved = formatMap.resolveFormatter(ANOTHER_DATE_FORMAT, EXPECTED_LOCALE);

            assertNull(resolved);
        }

        @Test
        void given_aLegacyPattern_when_resolve_thenReturnNull() {
            var resolved = formatMap.resolveFormatter(EXPECTED_PATTERN, EXPECTED_LOCALE);
            assertNull(resolved);
        }
    }
}