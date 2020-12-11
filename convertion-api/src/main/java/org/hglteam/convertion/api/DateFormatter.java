package org.hglteam.convertion.api;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface DateFormatter {
    String format(Date date, String pattern, Locale locale);
    String format(Date date, String pattern);
    String format(TemporalAccessor temporal, String pattern, Locale locale);
    String format(TemporalAccessor temporal, String pattern);
    Function<Date, String> format(String pattern, Locale locale);
    Function<Date, String> format(String pattern);
    Function<? extends TemporalAccessor, String> formatTemporal(String pattern, Locale locale);
    Function<? extends TemporalAccessor, String> formatTemporal(String pattern);

    Date toDate(String value, String pattern, Locale locale);
    Date toDate(String value, String pattern);
    <T extends TemporalAccessor> T toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String value, String pattern, Locale locale);
    <T extends TemporalAccessor> T toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String value, String pattern);
    <T extends TemporalAccessor> Function<String, T> toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String pattern, Locale locale);
    <T extends TemporalAccessor> Function<String, T> toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String pattern);

    class DateFormattingException extends RuntimeException {
        private String pattern;
        private String value;
        private Locale locale;

        public DateFormattingException(String value, String pattern, Locale locale) {
            this.pattern = pattern;
            this.value = value;
            this.locale = locale;
        }

        public DateFormattingException(Throwable cause, String value, String pattern, Locale locale) {
            super(cause);
            this.pattern = pattern;
            this.value = value;
            this.locale = locale;
        }

        public String getPattern() {
            return pattern;
        }

        public String getValue() {
            return value;
        }

        public Locale getLocale() {
            return locale;
        }
    }
}
