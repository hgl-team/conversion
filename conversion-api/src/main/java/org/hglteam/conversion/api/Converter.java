package org.hglteam.conversion.api;

import java.util.function.Function;

public interface Converter {
    <TS, TD> TD convert(TS source, Class<? extends TS> sourceClass, Class<? extends TD> targetClass,  Object... args);
    <TS, TD> TD convert(TS source, Class<? extends TD> targetClass, Object... args);
    <TS, TD> TD convert(TS source, ConversionKey contersionKey, Object... args);

    <TS, TD> Function<TS, TD> convertTo(Class<? extends TS> sourceClass, Class<? extends TD> targetClass, Object... args);
    <TS, TD> Function<TS, TD> convertTo(Class<? extends TD> targetClass, Object... args);
    <TS, TD> Function<TS, TD> convertTo(ConversionKey conversionKey, Object... args);
}
