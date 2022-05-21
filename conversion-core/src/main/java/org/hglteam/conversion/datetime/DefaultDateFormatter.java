package org.hglteam.conversion.datetime;

import org.hglteam.conversion.api.datetime.DateFormatter;
import org.hglteam.conversion.api.datetime.DateFormatMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DefaultDateFormatter implements DateFormatter {
    private final DateFormatMap context;

    public DefaultDateFormatter(DateFormatMap context) {
        this.context = context;
    }

    private DateFormat getLegacyFormatter(String pattern, Locale locale) {
        var formatter = context.resolveLegacyFormatter(pattern, locale);

        if(Objects.isNull(formatter)) {
            context.registerLegacy(pattern, locale);
            formatter = context.resolveLegacyFormatter(pattern, locale);
        }

        return formatter;
    }
    private DateTimeFormatter getFormatter(String pattern, Locale locale) {
        var formatter = context.resolveFormatter(pattern, locale);

        if(Objects.isNull(formatter)) {
            context.register(pattern, locale);
            formatter = context.resolveFormatter(pattern, locale);
        }

        return formatter;
    }

    public String format(Date date, String pattern, Locale locale) {
        return getLegacyFormatter(pattern, locale).format(date);
    }
    public String format(Date date, String pattern) {
        return format(date, pattern, Locale.getDefault());
    }
    public String format(TemporalAccessor temporal, String pattern, Locale locale) {
        return getFormatter(pattern, locale).format(temporal);
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
    public <T extends TemporalAccessor> Function<T, String> formatTemporal(String pattern, Locale locale) {
        return temporal -> format(temporal, pattern, locale);
    }
    public <T extends TemporalAccessor> Function<T, String> formatTemporal(String pattern) {
        return temporal -> format(temporal, pattern);
    }

    public Date toDate(String value, String pattern, Locale locale) {
        try {
            return getLegacyFormatter(pattern, locale).parse(value);
        } catch (ParseException e) {
            throw new DateFormattingException(e, value, pattern, locale);
        }
    }
    public Date toDate(String value, String pattern) {
        return toDate(value, pattern, Locale.getDefault());
    }
    public <T extends TemporalAccessor> T toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String value, String pattern, Locale locale) {
        return parser.apply(value, getFormatter(pattern, locale));
    }
    public <T extends TemporalAccessor> T toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String value, String pattern) {
        return toTemporal(parser, value, pattern, Locale.getDefault());
    }
    public <T extends TemporalAccessor> Function<String, T> toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String pattern, Locale locale) {
        return value -> toTemporal(parser, value, pattern, locale);
    }
    public <T extends TemporalAccessor> Function<String, T> toTemporal(BiFunction<String, DateTimeFormatter, T> parser, String pattern) {
        return toTemporal(parser, pattern, Locale.getDefault());
    }
}
