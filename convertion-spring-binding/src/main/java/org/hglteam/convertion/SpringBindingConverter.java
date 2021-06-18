package org.hglteam.convertion;

import org.hglteam.convertion.api.ConversionContext;
import org.hglteam.convertion.api.Converter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Set;
import java.util.stream.Collectors;

public class SpringBindingConverter implements GenericConverter {
    private final ConversionContext conversionContext;
    private final Converter converter;

    public SpringBindingConverter(ConversionContext conversionContext, Converter converter) {
        this.conversionContext = conversionContext;
        this.converter = converter;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return this.conversionContext.getAvailableConversionPairs().stream()
                .filter(pair -> pair.getSource() instanceof Class && pair.getTarget() instanceof Class)
                .map(pair -> new ConvertiblePair((Class<?>) pair.getSource(), (Class<?>) pair.getTarget()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Object convert(Object value, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return converter.convert(value, sourceType.getType(), targetType.getType());
    }
}
