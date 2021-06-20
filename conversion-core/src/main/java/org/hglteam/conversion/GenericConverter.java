package org.hglteam.conversion;

import org.hglteam.conversion.api.*;

import java.util.function.Function;

public class GenericConverter implements Converter {
    private final ConversionContext conversionContext;

    public GenericConverter(ConversionContext conversionContext) {
        this.conversionContext = conversionContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TS, TD> TD convert(TS source, ConversionKey contersionKey) {
        var converter = (TypeConverter<TS, TD>) conversionContext.resolve(contersionKey);
        return converter.convert(source);
    }

    @Override
    public <TS, TD> TD convert(TS source, Class<? extends TS> sourceClass, Class<? extends TD> targetClass) {
        return this.convert(source, ConversionKeyResolver.getConverterKey(sourceClass, targetClass));
    }

    @Override
    public <TS, TD> TD convert(TS source, Class<? extends TD> targetClass) {
        return convert(source, source.getClass(), targetClass);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(Class<? extends TS> sourceClass, Class<? extends TD> targetClass) {
        return source -> convert(source, sourceClass, targetClass);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(Class<? extends TD> targetClass) {
        return source -> convert(source, targetClass);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(ConversionKey conversionKey) {
        return source -> convert(source, conversionKey);
    }
}
