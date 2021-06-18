package org.hglteam.convertion;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public class ConversionKey {
    private final Type sourceClass;
    private final Type targetClass;

    public ConversionKey(Type sourceClass, Type targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    public Type getSourceClass() {
        return sourceClass;
    }

    public Type getTargetClass() {
        return targetClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionKey that = (ConversionKey) o;

        var sourceCompatibility = checkTypeCompatibility(sourceClass, that.sourceClass);
        var targetCompatibility = checkTypeCompatibility(targetClass, that.targetClass);

        return sourceCompatibility && targetCompatibility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceClass, targetClass);
    }

    private boolean checkTypeCompatibility(Type thisType, Type thatType) {
        var thisClass =
                (thisType instanceof Class) ? (Class<?>) thisType
                : (thisType instanceof ParameterizedType) ? (Class<?>)((ParameterizedType) thisType).getRawType()
                : null;

        var thatClass =
                (thatType instanceof Class) ? (Class<?>) thatType
                : (thatType instanceof ParameterizedType) ? (Class<?>)((ParameterizedType) thatType).getRawType()
                : null;

        if(Objects.nonNull(thisClass) && Objects.nonNull(thatClass)) {
            return thisClass.isAssignableFrom(thatClass);
        } else {
            return Objects.equals(thisType, thatType);
        }
    }
}


