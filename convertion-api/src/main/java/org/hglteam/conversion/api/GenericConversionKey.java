package org.hglteam.conversion.api;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

@Getter
public abstract class GenericConversionKey<TS, TD> implements ConversionKey {
    private final Type source;
    private final Type target;

    public GenericConversionKey() {
        var superclass = getClass().getGenericSuperclass();

        if(superclass instanceof ParameterizedType) {
            var parameterizedClass = (ParameterizedType) superclass;

            this.source = parameterizedClass.getActualTypeArguments()[0];
            this.target = parameterizedClass.getActualTypeArguments()[1];
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConversionKey)) return false;
        ConversionKey that = (ConversionKey) o;
        return ConversionKey.areEqual(this, that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
