package org.hglteam.conversion.datetime;

import org.hglteam.conversion.api.datetime.DateFormatMap;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefaultDateFormatMap implements Serializable, DateFormatMap {
    private final Map<LocalizedDatePatternKey, DateFormat> legacyFormatters = new HashMap<>();
    private final Map<LocalizedDatePatternKey, DateTimeFormatter> formatters = new HashMap<>();

    @Override
    public DateFormatMap registerLegacy(String format, Locale locale) {
        legacyFormatters.put(
                new LocalizedDatePatternKey(format, locale),
                new SimpleDateFormat(format, locale));
        return this;
    }

    @Override
    public DateFormatMap register(String format, Locale locale) {
        formatters.put(
                new LocalizedDatePatternKey(format, locale),
                DateTimeFormatter.ofPattern(format, locale));
        return this;
    }

    @Override
    public DateFormat resolveLegacyFormatter(String pattern, Locale locale) {
        return legacyFormatters.get(new LocalizedDatePatternKey(pattern, locale));
    }

    @Override
    public DateTimeFormatter resolveFormatter(String pattern, Locale locale) {
        return formatters.get(new LocalizedDatePatternKey(pattern, locale));
    }
}
