package org.hglteam.convertion;

import org.hglteam.convertion.api.ConversionContext;
import org.hglteam.convertion.api.TypeConverter;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ConversionContextMap implements ConversionContext {
    private final Map<ConversionKey, TypeConverter<?,?>> converterMap = new HashMap<>();

    @Override
    public <TS, TD> TypeConverter<TS, TD> resolve(Class<? extends TS> sourceClass, Class<? extends TD> destinationClass) {
        return Optional.of(new ConversionKey(sourceClass, destinationClass))
                .map(this::<TS, TD>getConverter)
                .orElseThrow(() -> ConverterNotFoundException.of(sourceClass, destinationClass));
    }

    @Override
    public ConversionContext register(TypeConverter<?, ?> converter) {
        converterMap.put(ConversionKeyResolver.getConverterKey(converter), converter);
        return this;
    }

    @Override
    public Collection<ConversionPair> getAvailableConversionPairs() {
        return converterMap.keySet().stream()
                .map(key -> ConversionPair.builder()
                        .source(key.getSourceClass())
                        .target(key.getTargetClass())
                        .build())
                .collect(Collectors.toUnmodifiableSet());
    }

    @SuppressWarnings("unchecked")
    private <TS, TD> TypeConverter<TS, TD> getConverter(ConversionKey key) {
        return converterMap.keySet().stream()
                .filter(k -> k.equals(key))
                .findAny()
                .map(converterMap::get)
                .map(TypeConverter.class::cast)
                .orElseThrow(() -> ConverterNotFoundException.of(key));
    }

    public static class ConverterNotFoundException extends IllegalArgumentException {
        private static final String CONVERSION_PAIR = "%s -> %s";
        private final Type sourceType;
        private final Type targetType;

        public ConverterNotFoundException(Type sourceType, Type targetType) {
            super(String.format(CONVERSION_PAIR, sourceType, targetType));
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public Type getSourceType() {
            return sourceType;
        }

        public Type getTargetType() {
            return targetType;
        }

        public static ConverterNotFoundException of(Type sourceType, Type targetType) {
            return new ConverterNotFoundException(sourceType, targetType);
        }

        public static ConverterNotFoundException of(ConversionKey key) {
            return ConverterNotFoundException.of(key.getSourceClass(), key.getTargetClass());
        }
    }
}
