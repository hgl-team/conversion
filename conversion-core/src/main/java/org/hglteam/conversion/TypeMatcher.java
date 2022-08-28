package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.DefaultConversionKey;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TypeMatcher {
    private TypeMatcher() { }
    private static final Map<ConversionKey, Boolean> previousMatches = new HashMap<>();

    public static boolean isAssignableFrom(Type source, Type target) {
        var convertionKey = DefaultConversionKey.builder()
                .source(source)
                .target(target)
                .build();

        if(!previousMatches.containsKey(convertionKey)) {
            previousMatches.put(convertionKey, isAssignableFrom(source, target, new HashMap<>()));
        }

        return previousMatches.get(convertionKey);
    }

    private static boolean isAssignableFrom(Type source, Type target, Map<Type, Type> genericArgs) {
        if(Objects.isNull(source) || Objects.equals(Object.class, source)) {
            return false;
        } else if(source instanceof Class<?>) {
            return isAssignableFrom((Class<?>) source, target, genericArgs);
        } else if(source instanceof ParameterizedType) {
            return isAssignableFrom((ParameterizedType)source, target, genericArgs);
        } else {
            return false;
        }
    }

    private static boolean isAssignableFrom(Class<?> source, Type target, Map<Type, Type> genericArgs) {
        if(target instanceof Class<?> && ((Class<?>)target).isAssignableFrom(source)) {
            return true;
        } else if(target instanceof WildcardType) {
            var wildcard = (WildcardType) target;

            return Arrays.stream(wildcard.getUpperBounds())
                    .allMatch(bound -> isAssignableFrom(source, bound));
        } else if(target instanceof ParameterizedType){
            var parameterizedTarget = (ParameterizedType) target;
            var resolvedTypeParams = Arrays.stream(source.getTypeParameters())
                    .sequential()
                    .map(typeParam -> lookForLastValue(typeParam, genericArgs))
                    .collect(Collectors.toList());
            var targetClass = (Class<?>) parameterizedTarget.getRawType();

            if (Objects.equals(source, targetClass)) {
                var resolvedTargetArgs = Arrays.asList(parameterizedTarget.getActualTypeArguments());

                return IntStream.range(0, resolvedTargetArgs.size())
                        .allMatch(i -> isAssignableFrom(resolvedTypeParams.get(i), resolvedTargetArgs.get(i), new HashMap<>()));
            } else {
                var baseClass = source.getGenericSuperclass();
                var interfaces = source.getGenericInterfaces();
                return isAssignableFrom(baseClass, target, genericArgs)
                        || Arrays.stream(interfaces).anyMatch(type -> isAssignableFrom(type, target, genericArgs));
            }
        } else {
            return false;
        }
    }

    private static boolean isAssignableFrom(ParameterizedType type, Type target, Map<Type, Type> genericArgs) {
        scanTypeParameters(type, genericArgs);

        return isAssignableFrom((Class<?>) type.getRawType(), target, genericArgs);
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
