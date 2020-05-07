package org.hgl.convertion;

import org.hgl.convertion.api.DateFormatterContext;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GenericDateFormatterContext implements Serializable, DateFormatterContext {
    private Map<LocalizedDatePatternKey, DateFormat> legacyFormatters = new HashMap<>();
    private Map<LocalizedDatePatternKey, DateTimeFormatter> formatters = new HashMap<>();

    @Override
    public DateFormatterContext registerLegacy(String format, Locale locale) {
        legacyFormatters.put(
                new LocalizedDatePatternKey(format, locale),
                new SimpleDateFormat(format, locale));
        return this;
    }

    @Override
    public DateFormatterContext register(String format, Locale locale) {
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
