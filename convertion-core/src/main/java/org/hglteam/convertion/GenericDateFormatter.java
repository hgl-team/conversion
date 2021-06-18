package org.hglteam.convertion;

import org.hglteam.convertion.api.DateFormatter;
import org.hglteam.convertion.api.DateFormattingContext;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GenericDateFormatter implements DateFormatter {
    private final DateFormattingContext context;

    public GenericDateFormatter(DateFormattingContext context) {
        this.context = context;
    }

    public String format(Date date, String pattern, Locale locale) {
        return context.resolveLegacyFormatter(pattern, locale).format(date);
    }
    public String format(Date date, String pattern) {
        return format(date, pattern, Locale.getDefault());
    }
    public String format(TemporalAccessor temporal, String pattern, Locale locale) {
        return context.resolveFormatter(pattern, locale).format(temporal);
    }
    public String format(TemporalAccessor temporal, String pattern) {
        return format(temporal, pattern, Locale.getDefault());
    }
    public Function<Date, String> format(String pattern, Locale locale) {
        return date -> format(date, pattern, locale);
    }
    public Function<Date, String> format(String pattern) {
        return date -> format(date, pattern);
    }
    public Function<? extends TemporalAccessor, String> formatTemporal(String pattern, Locale locale) {
        return temporal -> format(temporal, pattern, locale);
    }
    public Function<? extends TemporalAccessor, String> formatTemporal(String pattern) {
        return temporal -> format(temporal, pattern);
    }

    public Date toDate(String value, String pattern, Locale locale) {
        try {
            return context.resolveLegacyFormatter(pattern, locale).parse(value);
        } catch (ParseException e) {
            throw new DateFormattingException(e, value, pattern, locale);
        }
    }
    public Date toDate(String value, String pattern) {
        return toDate(value, pattern, Locale.getDefault());
    }
    public <T extends TemporalAccessor> T toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String value, String pattern, Locale locale) {
        return parser.apply(value, context.resolveFormatter(pattern, locale));
    }
    public <T extends TemporalAccessor> T toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String value, String pattern) {
        return toTemporal(parser, value, pattern, Locale.getDefault());
    }
    public <T extends TemporalAccessor> Function<String, T> toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String pattern, Locale locale) {
        return value -> toTemporal(parser, value, pattern, locale);
    }
    public <T extends TemporalAccessor> Function<String, T> toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String pattern) {
        return value -> toTemporal(parser, value, pattern);
    }
}
