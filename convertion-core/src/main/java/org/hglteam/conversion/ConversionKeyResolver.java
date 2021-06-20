package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.DefaultConvertionKey;
import org.hglteam.conversion.api.ExplicitTypeConverter;
import org.hglteam.conversion.api.TypeConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ConversionKeyResolver {

    public static ConversionKey getConverterKey(TypeConverter<?,?> obj) {
        if(obj instanceof ExplicitTypeConverter) {
            return ((ExplicitTypeConverter<?, ?>) obj).getConversionKey();
        } else {
            Type[] genericTypes = getGenericConverterTypes(obj);

            return Optional.of(genericTypes)
                    .filter(converterGenericTypes -> converterGenericTypes.length == 2)
                    .map(converterGenericTypes -> DefaultConvertionKey.builder()
                            .source(converterGenericTypes[0])
                            .target(converterGenericTypes[1])
                            .build())
                    .orElseThrow(() -> InvalidConvertionTypeException.fromPartialResult(genericTypes));
        }
    }

    public static ConversionKey getConverterKey(Type source, Type target) {
        return DefaultConvertionKey.builder()
                .source(source)
                .target(target)
                .build();
    }

    private static Type[] getGenericConverterTypes(Object instance) {
        Map<Type, Type> argumentMap = new HashMap<>();
        Class<?> currentSuperClass = instance.getClass();
        ParameterizedType converterInterfaceType = null;

        while (currentSuperClass != null && converterInterfaceType == null) {
            getGenericTypeArguments(argumentMap, currentSuperClass);
            converterInterfaceType = getConverterInterface(currentSuperClass);
            currentSuperClass = Optional.of(currentSuperClass).map(Class::getSuperclass).orElseThrow(IllegalArgumentException::new);
        }

        if(Objects.isNull(converterInterfaceType)) {
            throw InvalidConvertionTypeException.fromInstance(instance);
        }

        return Arrays.stream(converterInterfaceType.getActualTypeArguments())
                .map(type -> Optional.of(type).filter(argumentMap::containsKey).map(argumentMap::get).orElse(type))
                .toArray(Type[]::new);
    }

    private static ParameterizedType getConverterInterface(Class<?> superclass) {
        return Arrays.stream(superclass.getGenericInterfaces())
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .filter(ptype -> TypeConverter.class.isAssignableFrom((Class<?>)ptype.getRawType()))
                .findAny()
                .orElse(null);
    }

    private static void getGenericTypeArguments(Map<Type, Type> argumentType, Class<?> instanceClass) {
        Type superclass = instanceClass.getGenericSuperclass();

        if(superclass instanceof ParameterizedType) {
            ParameterizedType parametricSuperclass = (ParameterizedType)superclass;
            Type[] genericTypeParameters = ((Class<?>) parametricSuperclass.getRawType()).getTypeParameters();
            Type[] genericTypeArguments = parametricSuperclass.getActualTypeArguments();

            for (int i = 0; i < genericTypeParameters.length; i++) {
                argumentType.put(genericTypeParameters[i], Optional.of(genericTypeArguments[i])
                        .filter(argumentType::containsKey)
                        .map(argumentType::get)
                        .orElse(genericTypeArguments[i]));
            }
        }
    }

    private static class InvalidConvertionTypeException extends IllegalArgumentException {
        private static final String PARTIAL_RESULT_MESSAGE_FORMAT = "Partial types found: %s";
        private static final String INVALID_INSTANCE_MESSAGE_FORMAT = "Illegal instance type %s";

        public InvalidConvertionTypeException(String message) {
            super(message);
        }

        public static InvalidConvertionTypeException fromPartialResult(Type[] partialResults) {
            return new InvalidConvertionTypeException(String.format(PARTIAL_RESULT_MESSAGE_FORMAT,
                    Arrays.toString(partialResults)));
        }

        public static InvalidConvertionTypeException fromInstance(Object instance) {
            return new InvalidConvertionTypeException(String.format(INVALID_INSTANCE_MESSAGE_FORMAT,
                    instance.getClass()));
        }
    }
}
