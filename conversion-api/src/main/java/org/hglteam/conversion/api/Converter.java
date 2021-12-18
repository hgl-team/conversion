package org.hglteam.conversion.api;

import org.hglteam.conversion.api.context.ContextualConversionBuilder;
import org.hglteam.conversion.api.context.TypeConversionContext;

import java.lang.reflect.Type;
import java.util.function.Function;

public interface Converter {

    <TS, TD> TD convert(TS source, TypeConversionContext context);
    <TS, TD> TD convert(TS source, ConversionKey contersionKey);
    <TS, TD> TD convert(TS source, Type sourceType, Type targetType);
    <TS, TD> TD convert(TS source, Type targetType);
    <TS, TD> TD convert(TS source, Class<? extends TD> targetClass);

    <TS, TD> Function<TS, TD> convertTo(TypeConversionContext context);
    <TS, TD> Function<TS, TD> convertTo(ConversionKey conversionKey);
    <TS, TD> Function<TS, TD> convertTo(Type sourceType, Type targetType);
    <TS, TD> Function<TS, TD> convertTo(Type targetType);
    <TS, TD> Function<TS, TD> convertTo(Class<? extends TD> targetClass);

    <TD> ContextualConversionBuilder<TD> withContext(Type targetType);
    <TD> ContextualConversionBuilder<TD> withContext(Class<? extends TD> targetClass);
}
