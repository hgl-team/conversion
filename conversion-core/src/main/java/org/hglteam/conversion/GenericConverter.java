package org.hglteam.conversion;

import org.hglteam.conversion.api.*;
import org.hglteam.conversion.api.context.TypeConversionContext;

import java.util.function.Function;

public class GenericConverter implements Converter {
    private final ConversionContext conversionContext;

    public GenericConverter(ConversionContext conversionContext) {
        this.conversionContext = conversionContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TS, TD> TD convert(TS source, ConversionKey contersionKey, Object... args) {
        var converter = (TypeConverter<TS, TD>) conversionContext.resolve(contersionKey);
        var context = TypeConversionContext.builder()
                .converter(this)
                .currentConversionKey(contersionKey)
                .arguments(args)
                .build();

        return converter.convert(context, source);
    }

    @Override
    public <TS, TD> TD convert(TS source, Class<? extends TS> sourceClass, Class<? extends TD> targetClass, Object... args) {
        return this.convert(source, ConversionKeyResolver.getConverterKey(sourceClass, targetClass), args);
    }

    @Override
    public <TS, TD> TD convert(TS source, Class<? extends TD> targetClass, Object... args) {
        return convert(source, source.getClass(), targetClass, args);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(Class<? extends TS> sourceClass, Class<? extends TD> targetClass, Object... args) {
        return source -> convert(source, sourceClass, targetClass, args);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(Class<? extends TD> targetClass, Object... args) {
        return source -> convert(source, targetClass, args);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(ConversionKey conversionKey, Object... args) {
        return source -> convert(source, conversionKey, args);
    }
}
