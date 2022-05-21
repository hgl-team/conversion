package org.hglteam.conversion.api.datetime;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface DateFormatMap {

    DateFormatMap registerLegacy(String format, Locale locale);
    DateFormatMap register(String format, Locale locale);

    DateFormat resolveLegacyFormatter(String pattern, Locale locale);
    DateTimeFormatter resolveFormatter(String pattern, Locale locale);
}
