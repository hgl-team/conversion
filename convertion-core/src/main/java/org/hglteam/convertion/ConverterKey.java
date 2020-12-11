package org.hglteam.convertion;

import java.util.Objects;

public class ConverterKey {
    private final Class<?> sourceClass;
    private final Class<?> destinationClass;

    public ConverterKey(Class<?> sourceClass, Class<?> destinationClass) {
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
    }

    public Class<?> getSourceClass() {
        return sourceClass;
    }

    public Class<?> getDestinationClass() {
        return destinationClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConverterKey that = (ConverterKey) o;
        return sourceClass.equals(that.sourceClass) &&
                destinationClass.equals(that.destinationClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceClass, destinationClass);
    }
}


