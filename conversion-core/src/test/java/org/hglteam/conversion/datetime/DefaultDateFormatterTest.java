package org.hglteam.conversion.datetime;

import org.hglteam.conversion.api.datetime.DateFormatMap;
import org.hglteam.conversion.api.datetime.DateFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DefaultDateFormatterTest {
    private static final String EXPECTED_PATTERN = "E, dd MMM yyyy HH:mm:ss";
    private static final Locale EXPECTED_LOCALE = Locale.GERMANY;
    private static final SimpleDateFormat EXPECTED_LEGACY_FORMATTER = new SimpleDateFormat(EXPECTED_PATTERN, EXPECTED_LOCALE);
    private static final SimpleDateFormat EXPECTED_LEGACY_DEFAULT_FORMATTER = new SimpleDateFormat(EXPECTED_PATTERN, Locale.getDefault());
    private static final DateTimeFormatter EXPECTED_FORMATTER = DateTimeFormatter.ofPattern(EXPECTED_PATTERN, EXPECTED_LOCALE);
    private static final DateTimeFormatter EXPECTED_DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(EXPECTED_PATTERN, Locale.getDefault());

    private DateFormatMap map;
    private DefaultDateFormatter formatter;

    @BeforeEach
    public void setup() {
        this.map = new DefaultDateFormatMap();
        this.formatter = new DefaultDateFormatter(map);
    }

    @Nested
    class FormatDateTest {

        @Test
        void should_returnLegacyExpectedFormat_when_formatWithPatternAndLocale() {
            var date = new Date();
            assertEquals(EXPECTED_LEGACY_FORMATTER.format(date),
                    formatter.format(date, EXPECTED_PATTERN, EXPECTED_LOCALE));
        }

        @Test
        void should_returnLegacyExpectedFormat_when_formatWithPattern() {
            var date = new Date();

            assertEquals(EXPECTED_LEGACY_DEFAULT_FORMATTER.format(date),
                    formatter.format(date, EXPECTED_PATTERN));
        }

        @Test
        void should_returnLegacyExpectedFormat_when_formatToWithPatternAndLocale() {
            var date = new Date();
            assertEquals(EXPECTED_LEGACY_FORMATTER.format(date),
                    formatter.format(EXPECTED_PATTERN, EXPECTED_LOCALE).apply(date));
        }

        @Test
        void should_returnLegacyExpectedFormat_when_formatToWithPattern() {
            var date = new Date();

            assertEquals(EXPECTED_LEGACY_DEFAULT_FORMATTER.format(date),
                    formatter.format(EXPECTED_PATTERN).apply(date));
        }

        // --------------

        @Test
        void should_returnExpectedFormat_when_formatWithPatternAndLocale() {
            var date = LocalDateTime.now();
            assertEquals(EXPECTED_FORMATTER.format(date),
                    formatter.format(date, EXPECTED_PATTERN, EXPECTED_LOCALE));
        }

        @Test
        void should_returnExpectedFormat_when_formatWithPattern() {
            var date = LocalDateTime.now();

            assertEquals(EXPECTED_DEFAULT_FORMATTER.format(date),
                    formatter.format(date, EXPECTED_PATTERN));
        }

        @Test
        void should_returnExpectedFormat_when_formatToWithPatternAndLocale() {
            var date = LocalDateTime.now();
            assertEquals(EXPECTED_FORMATTER.format(date),
                    formatter.formatTemporal(EXPECTED_PATTERN, EXPECTED_LOCALE)
                            .apply(date));
        }

        @Test
        void should_returnExpectedFormat_when_formatToWithPattern() {
            var date = LocalDateTime.now();

            assertEquals(EXPECTED_DEFAULT_FORMATTER.format(date),
                    formatter.formatTemporal(EXPECTED_PATTERN)
                            .apply(date));
        }
    }

    @Nested
    class GetDateFromStringTest {
        @Test
        void given_aWrongLegacyDate_when_toDate_then_throwDateFormattingException() {
            assertThrows(DateFormatter.DateFormattingException.class,
                    () -> formatter.toDate("wrong-date", EXPECTED_PATTERN));
        }

        @Test
        void given_aWrongPattern_when_toDate_then_throwIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> formatter.toDate("2022-01-01", "wrong-pattern"));
        }

        @Test
        void given_aValidDateAndPattern_when_toDate_then_getsExpectedDate() {
            var expectedDate = "2022-01-01";
            var date = formatter.toDate(expectedDate, "yyyy-MM-dd");
            var dateString = formatter.format(date, "yyyy-MM-dd");

            assertEquals(expectedDate, dateString);
        }

        // -------------------

        @Test
        void given_aWrongDate_when_toTemporal_then_throwDateFormattingException() {
            assertThrows(DateTimeParseException.class,
                    () -> formatter.toTemporal(LocalDate::parse, "wrong-date", EXPECTED_PATTERN));
        }

        @Test
        void given_aWrongPattern_when_toTemporal_then_throwIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> formatter.toTemporal(LocalDate::parse, "2022-01-01", "wrong-pattern"));
        }

        @Test
        void given_aValidDateAndPattern_when_toTemporal_then_getsExpectedDate() {
            var expectedDate = "2022-01-01";
            var date = formatter.toTemporal(LocalDate::parse, expectedDate, "yyyy-MM-dd");
            var dateString = formatter.format(date, "yyyy-MM-dd");

            assertEquals(expectedDate, dateString);
        }

        // -----------------

        @Test
        void given_aWrongDate_when_toTemporalFunc_then_throwDateFormattingException() {
            assertThrows(DateTimeParseException.class,
                    () -> formatter.toTemporal(LocalDate::parse, EXPECTED_PATTERN)
                            .apply("wrong-date"));
        }

        @Test
        void given_aWrongPattern_when_toTemporalFunc_then_throwIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> formatter.toTemporal(LocalDate::parse, "wrong-pattern")
                            .apply("2022-01-01"));
        }

        @Test
        void given_aValidDateAndPattern_when_toTemporalFunc_then_getsExpectedDate() {
            var expectedDate = "2022-01-01";
            var date = formatter.toTemporal(LocalDate::parse, "yyyy-MM-dd")
                    .apply(expectedDate);
            var dateString = formatter.format(date, "yyyy-MM-dd");

            assertEquals(expectedDate, dateString);
        }
    }
}