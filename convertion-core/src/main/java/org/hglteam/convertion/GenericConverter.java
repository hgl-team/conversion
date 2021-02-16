package org.hglteam.convertion;

import org.hglteam.convertion.api.Converter;
import org.hglteam.convertion.api.ConverterContext;

import java.util.function.Function;

public class GenericConverter implements Converter {
    private final ConverterContext convertionContext;

    public GenericConverter(ConverterContext convertionContext) {
        this.convertionContext = convertionContext;
    }

    @Override
    public <TS, TD> TD convert(TS source, Class<? extends TS> sourceClass, Class<? extends TD> destinationClass) {
        var converter = convertionContext.<TS, TD>resolve(sourceClass, destinationClass);
        return converter.convert(source);
    }

    @Override
    public <TS, TD> TD convert(TS source, Class<? extends TD> destinationClass) {
        return convert(source, source.getClass(), destinationClass);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(Class<? extends TS> sourceClass, Class<? extends TD> destinationClass) {
        return source -> convert(source, sourceClass, destinationClass);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(Class<? extends TD> destinationClass) {
        return source -> convert(source, destinationClass);
    }
}
