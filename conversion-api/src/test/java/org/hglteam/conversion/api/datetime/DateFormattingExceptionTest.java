package org.hglteam.conversion.api.datetime;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class DateFormattingExceptionTest {

    public static final String EXPECTED_VALUE = "123456789";
    public static final String EXPECTED_FORMAT = "yyyy-MM-dd";
    public static final Locale EXPECTED_LOCALE = Locale.GERMANY;
    public static final RuntimeException EXPECTED_CAUSE = new RuntimeException();

    @Test
    void should_matchFields_when_createException() {
        var exception = new DateFormatter.DateFormattingException(EXPECTED_VALUE, EXPECTED_FORMAT, EXPECTED_LOCALE);

        assertNull(exception.getCause());
        assertEquals(EXPECTED_VALUE, exception.getValue());
        assertEquals(EXPECTED_FORMAT, exception.getPattern());
        assertEquals(EXPECTED_LOCALE, exception.getLocale());

        exception = new DateFormatter.DateFormattingException(
                EXPECTED_CAUSE,
                EXPECTED_VALUE,
                EXPECTED_FORMAT,
                EXPECTED_LOCALE);

        assertEquals(EXPECTED_CAUSE, exception.getCause());
        assertEquals(EXPECTED_VALUE, exception.getValue());
        assertEquals(EXPECTED_FORMAT, exception.getPattern());
        assertEquals(EXPECTED_LOCALE, exception.getLocale());
    }
}