package org.hgl.convertion;

import org.hgl.convertion.api.Converter;
import org.hgl.convertion.api.ConverterContext;

import java.util.function.Function;

public class GenericConverter implements Converter {
    private ConverterContext convertionContext;

    public GenericConverter(ConverterContext convertionContext) {
        this.convertionContext = convertionContext;
    }

    @Override
    public <TS, TD> TD convert(TS source, Class<TS> sourceClass, Class<TD> destinationClass) {
        return convertionContext.resolve(sourceClass, destinationClass)
                .convert(source);
    }

    @Override
    public <TS, TD> TD convert(TS source, Class<TD> destinationClass) {
        return convert(source, (Class<TS>) source.getClass(), destinationClass);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(Class<TS> sourceClass, Class<TD> destinationClass) {
        return source -> convert(source, sourceClass, destinationClass);
    }

    @Override
    public <TS, TD> Function<TS, TD> convertTo(Class<TD> destinationClass) {
        return source -> convert(source, destinationClass);
    }
}
