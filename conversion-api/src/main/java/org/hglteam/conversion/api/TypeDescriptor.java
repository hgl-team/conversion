package org.hglteam.conversion.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeDescriptor<T> {
    private final Type type;

    protected TypeDescriptor() {
        type = TypeDescriptor.getSuperclassGenericType(getClass());
    }

    public Type getType() {
        return type;
    }

    public  T descriptor() {
        return null;
    }

    private static Type getSuperclassGenericType(Class<?> aClass) {
        var superclass = aClass.getGenericSuperclass();
        if(superclass instanceof Class) {
            throw new IllegalArgumentException();
        } else {
            var parameterizedClass = (ParameterizedType) superclass;
            return parameterizedClass.getActualTypeArguments()[0];
        }
    }
}
