package org.hglteam.convertion.api;

public interface ConverterContext {
    public <TS, TD> TypeConverter<TS, TD> resolve(Class<TS> sourceClass, Class<TD> destinationClass);
    ConverterContext register(TypeConverter<?, ?> converter);
}
