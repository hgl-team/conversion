package org.hgl.convertion;

import org.hgl.convertion.api.TypeConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ConverterKeyResolver {

    @SuppressWarnings("all")
    public static ConverterKey getConverterKey(TypeConverter<?,?> obj) {
        Class<?>[] genericTypes = getGenericConverterTypes(obj);

        return Optional.of(genericTypes)
                .filter(converterGenericTypes -> converterGenericTypes.length == 2)
                .map(converterGenericTypes -> new ConverterKey(converterGenericTypes[0], converterGenericTypes[1]))
                .orElseThrow(InvalidConvertionTypeException::new);
    }

    @SuppressWarnings("all")
    private static Class<?>[] getGenericConverterTypes(Object instance) {
        Map<Type, Type> argumentMap = new HashMap<>();
        Class<?> currentSuperClass = instance.getClass();
        ParameterizedType converterInterfaceType = null;

        while (currentSuperClass != null && converterInterfaceType == null) {
            getGenericTypeArguments(argumentMap, currentSuperClass);
            converterInterfaceType = getConverterInterface(currentSuperClass);
            currentSuperClass = Optional.of(currentSuperClass).map(Class::getSuperclass).orElseThrow(IllegalArgumentException::new);
        }

        if(Objects.isNull(converterInterfaceType)) {
            throw new InvalidConvertionTypeException();
        }

        return Arrays.stream(converterInterfaceType.getActualTypeArguments())
                .map(type -> Optional.of(type).filter(argumentMap::containsKey).map(argumentMap::get).orElse(type))
                .filter(Class.class::isInstance)
                .map(Class.class::cast)
                .toArray(Class[]::new);
    }

    private static ParameterizedType getConverterInterface(Class<?> superclass) {
        return Arrays.stream(superclass.getGenericInterfaces())
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .filter(ptype -> TypeConverter.class.isAssignableFrom((Class<?>)ptype.getRawType()))
                .findAny()
                .orElse(null);
    }

    private static void getGenericTypeArguments(Map<Type, Type> mapaArgumentos, Class<?> instanceClass) {
        Type superclass = instanceClass.getGenericSuperclass();

        if(superclass instanceof ParameterizedType) {
            ParameterizedType parametricSuperclass = (ParameterizedType)superclass;
            Type[] genericTypeParameters = ((Class<?>) parametricSuperclass.getRawType()).getTypeParameters();
            Type[] genericTypeArguments = parametricSuperclass.getActualTypeArguments();

            for (int i = 0; i < genericTypeParameters.length; i++) {
                mapaArgumentos.put(genericTypeParameters[i], Optional.of(genericTypeArguments[i])
                        .filter(mapaArgumentos::containsKey)
                        .map(mapaArgumentos::get)
                        .orElse(genericTypeArguments[i]));
            }
        }
    }

    private static class InvalidConvertionTypeException extends IllegalArgumentException {
    }
}
