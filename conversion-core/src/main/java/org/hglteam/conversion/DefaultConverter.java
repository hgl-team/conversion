package org.hglteam.conversion;

import org.hglteam.conversion.api.*;
import org.hglteam.conversion.api.context.ContextualConversionBuilder;
import org.hglteam.conversion.api.context.ConversionContext;

import java.lang.reflect.Type;
import java.util.function.Function;

public class DefaultConverter implements Converter {
    private final ConversionMap conversionMap;

    public DefaultConverter(ConversionMap conversionMap) {
        this.conversionMap = conversionMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S, T> T convert(S source, ConversionContext context) {
        var converter = (TypeConverter<S, T>) conversionMap.resolve(context.getCurrentConversionKey());
        return converter.convert(context, source);
    }

    @Override
    public <S, T> T convert(S source, ConversionKey contersionKey) {
        return this.convert(source, ConversionContext.builder()
                .converter(this)
                .currentConversionKey(contersionKey)
                .arguments(null)
                .build());
    }

    @Override
    public <S, T> T convert(S source, Type sourceType, Type targetType) {
        return this.convert(source, DefaultConversionKey.builder()
                .source(sourceType)
                .target(targetType)
                .build());
    }

    @Override
    public <S, T> T convert(S source, Type targetType) {
        return convert(source, source.getClass(), targetType);
    }

    @Override
    public <S, T> T convert(S source, Class<? extends T> targetClass) {
        return this.convert(source, (Type) targetClass);
    }

    @Override
    public <S, T> Function<S, T> convertTo(ConversionContext context) {
        return source -> this.convert(source, context);
    }

    @Override
    public <S, T> Function<S, T> convertTo(Type sourceType, Type targetType) {
        return source -> convert(source, sourceType, targetType);
    }

    @Override
    public <S, T> Function<S, T> convertTo(Type targetType) {
        return source -> convert(source, targetType);
    }

    @Override
    public <S, T> Function<S, T> convertTo(Class<? extends T> targetClass) {
        return convertTo((Type) targetClass);
    }

    @Override
    public <S, T> Function<S, T> convertTo(ConversionKey conversionKey) {
        return source -> convert(source, conversionKey);
    }

    @Override
    public <T> ContextualConversionBuilder<T> withContext(Type targetClass) {
        return new ContextualConversionBuilder<>(this, targetClass);
    }

    @Override
    public <T> ContextualConversionBuilder<T> withContext(Class<? extends T> targetClass) {
        return new ContextualConversionBuilder<>(this, targetClass);
    }

    @Override
    public <T> ContextualConversionBuilder<T> withContext(TypeDescriptor<T> targetTypeDescriptor) {
        return new ContextualConversionBuilder<>(this, targetTypeDescriptor.getType());
    }
}
