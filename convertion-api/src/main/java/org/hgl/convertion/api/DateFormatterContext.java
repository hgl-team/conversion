package org.hgl.convertion.api;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface DateFormatterContext {

    DateFormatterContext registerLegacy(String format, Locale locale);
    DateFormatterContext register(String format, Locale locale);

    DateFormat resolveLegacyFormatter(String pattern, Locale locale);
    DateTimeFormatter resolveFormatter(String pattern, Locale locale);
}
