package org.hglteam.conversion.api;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Type;
import java.util.Objects;

@Getter
@SuperBuilder(toBuilder = true)
public class DefaultConversionKey implements ConversionKey {
    private Type source;
    private Type target;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultConversionKey that = (DefaultConversionKey) o;
        return ConversionKey.areEqual(this, that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
