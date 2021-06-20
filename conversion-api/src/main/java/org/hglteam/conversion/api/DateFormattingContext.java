package org.hglteam.conversion.api;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface DateFormattingContext {

    DateFormattingContext registerLegacy(String format, Locale locale);
    DateFormattingContext register(String format, Locale locale);

    DateFormat resolveLegacyFormatter(String pattern, Locale locale);
    DateTimeFormatter resolveFormatter(String pattern, Locale locale);
}
