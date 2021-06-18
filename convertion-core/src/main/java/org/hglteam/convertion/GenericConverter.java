package org.hglteam.convertion;

import org.hglteam.convertion.api.Converter;
import org.hglteam.convertion.api.ConversionContext;

import java.util.function.Function;

public class GenericConverter implements Converter {
    private final ConversionContext conversionContext;

    public GenericConverter(ConversionContext conversionContext) {
        this.conversionContext = conversionContext;
    }

    @Override
    public <TS, TD> TD convert(TS source, Class<? extends TS> sourceClass, Class<? extends TD> targetClass) {
        var converter = conversionContext.<TS, TD>resolve(sourceClass, targetClass);
        return converter.convert(source);
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
}
