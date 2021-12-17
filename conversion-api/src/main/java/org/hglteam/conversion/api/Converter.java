package org.hglteam.conversion.api;

import org.hglteam.conversion.api.context.ContextConversionBuilder;
import org.hglteam.conversion.api.context.TypeConversionContext;

import java.lang.reflect.Type;
import java.util.function.Function;

public interface Converter {
    <TS, TD> TD convert(TS source, Type sourceType, Type targetType);
    <TS, TD> TD convert(TS source, Type targetType);
    <TS, TD> TD convert(TS source, ConversionKey contersionKey);
    <TS, TD> TD convert(TS source, TypeConversionContext context);

    <TS, TD> Function<TS, TD> convertTo(Type sourceClass, Type targetClass);
    <TS, TD> Function<TS, TD> convertTo(Type targetClass);
    <TS, TD> Function<TS, TD> convertTo(ConversionKey conversionKey);

    <TD> ContextConversionBuilder<TD> withContext(Type targetClass);
}
