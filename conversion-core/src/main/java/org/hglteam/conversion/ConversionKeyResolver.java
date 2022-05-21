package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.DefaultConversionKey;
import org.hglteam.conversion.api.ExplicitTypeConverter;
import org.hglteam.conversion.api.TypeConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ConversionKeyResolver {
    private ConversionKeyResolver() { }

    private static final Map<Type, ConversionKey> previousMatches = new HashMap<>();

    public static ConversionKey inferConversionKey(TypeConverter<?, ?> converter) {
        if(converter instanceof ExplicitTypeConverter) {
            return ((ExplicitTypeConverter<?, ?>) converter).getConversionKey();
        } else {
            var converterType = converter.getClass();

            if (!previousMatches.containsKey(converterType)) {
                previousMatches.put(converterType, inferConversionKey(converterType, new HashMap<>()));
            }

            return previousMatches.get(converterType);
        }
    }

    private static ConversionKey inferConversionKey(Type source, Map<Type, Type> genericArgs) {
        if(source instanceof Class<?>) {
            return inferConversionKey((Class<?>) source, genericArgs);
        } else if(source instanceof ParameterizedType) {
            return inferConversionKey((ParameterizedType)source, genericArgs);
        } else {
            return null;
        }
    }

    private static ConversionKey inferConversionKey(Class<?> source, Map<Type, Type> genericArgs) {
        var resolvedTypeParams = Arrays.stream(source.getTypeParameters())
                .sequential()
                .map(typeParam -> lookForLastValue(typeParam, genericArgs))
                .collect(Collectors.toList());

        if (Objects.equals(source, TypeConverter.class)) {
            return DefaultConversionKey.builder()
                    .source(resolvedTypeParams.get(0))
                    .target(resolvedTypeParams.get(1))
                    .build();
        } else {
            var baseClass = source.getGenericSuperclass();

            Optional.of(baseClass)
                    .filter(ParameterizedType.class::isInstance)
                    .map(ParameterizedType.class::cast)
                    .ifPresent(parameterizedType -> scanTypeParameters(parameterizedType, genericArgs));

            var interfaces = source.getGenericInterfaces();

            return Arrays.stream(interfaces)
                    .map(type -> inferConversionKey(type, genericArgs))
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElseGet(() -> inferConversionKey(baseClass, genericArgs));
        }
    }

    private static ConversionKey inferConversionKey(ParameterizedType source, Map<Type, Type> genericArgs) {
        scanTypeParameters(source, genericArgs);

        return inferConversionKey(source.getRawType(), genericArgs);
    }

    private static void scanTypeParameters(ParameterizedType parameterizedType, Map<Type, Type> genericArgs) {
        var actualParameters = parameterizedType.getActualTypeArguments();
        var rawType = (Class<?>) parameterizedType.getRawType();
        var rawTypeArgs = rawType.getTypeParameters();

        IntStream.range(0, actualParameters.length)
                .mapToObj(i -> Map.entry(rawTypeArgs[i], actualParameters[i]))
                .forEach(pair -> genericArgs.put(pair.getKey(), pair.getValue()));
    }

    private static  Type lookForLastValue(Type type, Map<Type, Type> genericArgs) {
        var current = type;

        while(genericArgs.containsKey(current)) {
            current = genericArgs.get(current);
        }

        genericArgs.replace(type, current);

        return current;
    }
}
