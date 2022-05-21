package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.ConversionMap;
import org.hglteam.conversion.api.ExplicitTypeConverter;
import org.hglteam.conversion.api.TypeConverter;

import java.util.*;

public class DefaultConversionMap implements ConversionMap {
    private final Map<ConversionKey, TypeConverter<?, ?>> converterMap = new HashMap<>();
    private final Map<ConversionKey, ConversionKey> compatibilityMap = new HashMap<>();

    @Override
    public ConversionMap register(ConversionKey key, TypeConverter<?, ?> converter) {
        if(!converterMap.containsKey(key)) {
            converterMap.put(key, converter);
            return this;
        } else {
            throw new DuplicatedConversionKeyException(key);
        }
    }

    @Override
    public ConversionMap register(TypeConverter<?, ?> converter) {
        var conversionKey = Optional.of(converter)
                .filter(ExplicitTypeConverter.class::isInstance)
                .map(ExplicitTypeConverter.class::cast)
                .map(ExplicitTypeConverter::getConversionKey)
                .orElseGet(() ->ConversionKeyResolver.inferConversionKey(converter));

        this.register(conversionKey, converter);
        return this;
    }

    @Override
    public TypeConverter<?, ?> resolve(ConversionKey key) {
        var resolvedKey = Optional.of(key)
                .filter(this.converterMap::containsKey)
                .orElseGet(() -> getFirstCompatibleFrom(key, this.converterMap.keySet()));

        return Optional.ofNullable(resolvedKey)
                .map(this.converterMap::get)
                .orElseThrow(NoCompatibleKeyFoundException.forKey(key));
    }

    @Override
    public Collection<ConversionKey> getAvailableConversions() {
        return Collections.unmodifiableSet(converterMap.keySet());
    }

    private ConversionKey getFirstCompatibleFrom(ConversionKey key, Set<ConversionKey> keySet) {
        if(!compatibilityMap.containsKey(key)) {
            keySet.stream()
                    .filter(conversionKey ->
                            TypeMatcher.isAssignableFrom(key.getSource(), conversionKey.getSource())
                           && TypeMatcher.isAssignableFrom(key.getTarget(), conversionKey.getTarget()))
                    .findAny()
                    .ifPresent(compatibleKey -> compatibilityMap.put(key, compatibleKey));
        }

        return compatibilityMap.get(key);
    }
}
