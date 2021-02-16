package org.hglteam.convertion.api;

public interface ConverterContext {
    <TS, TD> TypeConverter<TS,TD> resolve(Class<? extends TS> sourceClass, Class<? extends TD> destinationClass);
    ConverterContext register(TypeConverter<?, ?> converter);
}
