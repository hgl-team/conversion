package org.hglteam.conversion.util;

import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.DefaultConvertionKey;
import org.hglteam.conversion.api.TypeConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConversionTable {
    private final Map<Type, Map<Type, TypeConverter<?,?>>> targetTable;

    public ConversionTable() {
        this.targetTable = new HashMap<>();
    }

    public void put(Type sourceType, Type targetType, TypeConverter<?,?> converter) {
        var sourceTable = getByMatchingType(targetTable,  targetType, HashMap::new);

        if(!sourceTable.containsKey(sourceType)) {
            sourceTable.put(sourceType, converter);
        } else {
            throw new DuplicatedConversionKeyException(sourceType, targetType);
        }
    }

    public TypeConverter<?, ?> getCompatibleConverter(ConversionKey conversionKey) {
        var sourceTable = getByMatchingType(targetTable, conversionKey.getTarget(), null);
        return Optional.ofNullable(sourceTable)
                .map(table -> getByCompatibleType(table, conversionKey.getSource()))
                .orElseThrow(NoCompatibleKeyFoundException.forKey(conversionKey));
    }

    private <T> T getByMatchingType(Map<Type, T> map, Type key, Supplier<T> orElse) {
        if(!map.containsKey(key) && Objects.nonNull(orElse)) {
            map.put(key, orElse.get());
        }
        return map.get(key);
    }

    private <T> T getByCompatibleType(Map<Type, T> map, Type key) {
        if(map.containsKey(key)) {
            return map.get(key);
        } else {
            var keys = map.keySet();

            if(key instanceof ParameterizedType) {
                var parameterizedType = (ParameterizedType) key;

                return keys.stream()
                        .filter(ParameterizedType.class::isInstance)
                        .map(ParameterizedType.class::cast)
                        .filter(type -> this.isAssignableFrom(parameterizedType, type))
                        .map(map::get)
                        .findFirst()
                        .orElseThrow(NoCompatibleKeyFoundException.forKey(key));
            } else if(key instanceof Class) {
                var keyClass = (Class<?>) key;

                return keys.stream()
                        .filter(Class.class::isInstance)
                        .map(Class.class::cast)
                        .filter(type -> ((Class<?>)type).isAssignableFrom(keyClass))
                        .map(map::get)
                        .findFirst()
                        .orElseThrow(NoCompatibleKeyFoundException.forKey(key));
            } else {
                throw new NoCompatibleKeyFoundException(key);
            }
        }
    }

    private boolean isAssignableFrom(ParameterizedType source, ParameterizedType target) {
        return source.getRawType().equals(target.getRawType())
                && source.getActualTypeArguments().length == target.getActualTypeArguments().length
                && IntStream.range(0, source.getActualTypeArguments().length)
                        .allMatch(index -> Objects.equals(source.getActualTypeArguments()[index],  source.getActualTypeArguments()[index]));
    }

    public Collection<ConversionKey> getAvailableConversion() {
        return targetTable.keySet().stream()
                .flatMap(targetKey -> targetTable.get(targetKey).keySet().stream()
                        .map(sourceKey -> DefaultConvertionKey.builder()
                                .source(sourceKey)
                                .target(targetKey)
                                .build()))
                .collect(Collectors.toUnmodifiableSet());
    }

    public static class DuplicatedConversionKeyException extends IllegalArgumentException {
        public DuplicatedConversionKeyException(Type sourceType, Type targetType) {
            super(String.format("%s -> %s", sourceType, targetType));
        }
    }

    public static class NoCompatibleKeyFoundException extends IllegalArgumentException {
        public NoCompatibleKeyFoundException(Type key) {
            super(Objects.toString(key));
        }

        public NoCompatibleKeyFoundException(ConversionKey conversionKey) {
            super(String.format("%s -> %s", conversionKey.getSource(), conversionKey.getTarget()));
        }

        public static Supplier<NoCompatibleKeyFoundException> forKey(Type key) {
            return () -> new NoCompatibleKeyFoundException(key);
        }

        public static Supplier<? extends NoCompatibleKeyFoundException> forKey(ConversionKey conversionKey) {
            return () -> new NoCompatibleKeyFoundException(conversionKey);
        }
    }
}
