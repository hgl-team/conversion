package org.hglteam.conversion.api;

import org.hglteam.conversion.api.context.ContextualConversionBuilder;
import org.hglteam.conversion.api.context.ConversionContext;

import java.lang.reflect.Type;
import java.util.function.Function;

public interface Converter {

    <S, T> T convert(S source, ConversionContext context);
    <S, T> T convert(S source, ConversionKey contersionKey);
    <S, T> T convert(S source, Type sourceType, Type targetType);
    <S, T> T convert(S source, Type targetType);
    <S, T> T convert(S source, Class<? extends T> targetClass);

    <S, T> Function<S, T> convertTo(ConversionContext context);
    <S, T> Function<S, T> convertTo(ConversionKey conversionKey);
    <S, T> Function<S, T> convertTo(Type sourceType, Type targetType);
    <S, T> Function<S, T> convertTo(Type targetType);
    <S, T> Function<S, T> convertTo(Class<? extends T> targetClass);

    <T> ContextualConversionBuilder<T> withContext(Type targetType);
    <T> ContextualConversionBuilder<T> withContext(Class<? extends T> targetClass);
    <T> ContextualConversionBuilder<T> withContext(TypeDescriptor<T> targetTypeDescriptor);
}
