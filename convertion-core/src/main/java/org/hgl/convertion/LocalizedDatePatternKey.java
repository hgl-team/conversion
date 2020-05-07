package org.hgl.convertion;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class LocalizedDatePatternKey {
    private String pattern;
    private Locale locale;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalizedDatePatternKey that = (LocalizedDatePatternKey) o;
        return pattern.equals(that.pattern) &&
                locale.equals(that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, locale);
    }
}
