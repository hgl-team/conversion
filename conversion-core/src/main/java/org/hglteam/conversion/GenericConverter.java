package org.hglteam.conversion;

import org.hglteam.conversion.api.*;
import org.hglteam.conversion.api.context.ContextConversionBuilder;
import org.hglteam.conversion.api.context.TypeConversionContext;

import java.lang.reflect.Type;
import java.util.function.Function;

public class GenericConverter implements Converter {
    private final ConversionContext conversionContext;

    public GenericConverter(ConversionContext conversionContext) {
        this.conversionContext = conversionContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TS, TD> TD convert(TS source, TypeConversionContext context) {
        var converter = (TypeConverter<TS, TD>) conversionContext.resolve(context.getCurrentConversionKey());
        return converter.convert(context, source);
    }

    @Override

    public <TS, TD> TD convert(TS source, ConversionKey contersionKey) {
        return this.convert(source, TypeConversionContext.builder()
                .converter(this)
                .currentConversionKey(contersionKey)
                .arguments(null)
                .build());
    }

    @Override
    public <TS, TD> TD convert(TS source, Type sourceType, Type targetType) {
        return this.convert(source, ConversionKeyResolver.getConverterKey(sourceType, targetType));
    }

    @Override
    public <TS, TD> TD convert(TS source, Type targetType) {
        return convert(source, source.getClass(), targetType);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(Type sourceType, Type targetType) {
        return source -> convert(source, sourceType, targetType);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(Type targetType) {
        return source -> convert(source, targetType);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(ConversionKey conversionKey) {
        return source -> convert(source, conversionKey);
    }

    @Override
    public <TD> ContextConversionBuilder<TD> withContext(Type targetClass) {
        return new ContextConversionBuilder<>(this, targetClass);
    }
}
