package org.hglteam.convertion;

import org.hglteam.convertion.api.ConverterContext;
import org.hglteam.convertion.api.TypeConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConvertionContextMap implements ConverterContext {
    private final Map<ConverterKey, TypeConverter<?,?>> converterMap = new HashMap<>();

    @Override
    public <TS, TD> TypeConverter<TS, TD> resolve(Class<? extends TS> sourceClass, Class<? extends TD> destinationClass) {
        return Optional.of(new ConverterKey(sourceClass, destinationClass))
                .map(this::<TS, TD>getConverter)
                .orElseThrow(() -> new ConverterNotFoundException(sourceClass, destinationClass));
    }

    @Override
    public ConverterContext register(TypeConverter<?, ?> converter) {
        converterMap.put(ConverterKeyResolver.getConverterKey(converter), converter);
        return this;
    }

    private <TS, TD> TypeConverter<TS, TD> getConverter(ConverterKey key) {
        return (TypeConverter<TS, TD>) converterMap.get(key);
    }

    public static class ConverterNotFoundException extends IllegalArgumentException {
        private final Class<?> sourceClass;
        private final Class<?> destinationClass;

        public ConverterNotFoundException(Class<?> sourceClass, Class<?> destinationClass) {
            this.sourceClass = sourceClass;
            this.destinationClass = destinationClass;
        }

        public Class<?> getSourceClass() {
            return sourceClass;
        }

        public Class<?> getDestinationClass() {
            return destinationClass;
        }
    }
}
