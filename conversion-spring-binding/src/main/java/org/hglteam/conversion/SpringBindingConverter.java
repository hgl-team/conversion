package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionMap;
import org.hglteam.conversion.api.Converter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Set;
import java.util.stream.Collectors;

public class SpringBindingConverter implements GenericConverter {
    private final ConversionMap conversionMap;
    private final Converter converter;

    public SpringBindingConverter(ConversionMap conversionMap, Converter converter) {
        this.conversionMap = conversionMap;
        this.converter = converter;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return this.conversionMap.getAvailableConversions().stream()
                .filter(pair -> pair.getSource() instanceof Class && pair.getTarget() instanceof Class)
                .map(pair -> new ConvertiblePair((Class<?>) pair.getSource(), (Class<?>) pair.getTarget()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Object convert(Object value, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return converter.convert(value, sourceType.getType(), targetType.getType());
    }
}
