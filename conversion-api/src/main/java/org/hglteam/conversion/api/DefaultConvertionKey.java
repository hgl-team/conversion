package org.hglteam.conversion.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Type;
import java.util.Objects;

@Getter
@NoArgsConstructor
@SuperBuilder
public class DefaultConvertionKey implements ConversionKey {
    private Type source;
    private Type target;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultConvertionKey that = (DefaultConvertionKey) o;
        return ConversionKey.areEqual(this, that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
