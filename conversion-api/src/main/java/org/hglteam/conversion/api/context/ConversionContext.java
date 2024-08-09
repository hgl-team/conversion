package org.hglteam.conversion.api.context;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.Converter;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Getter
@SuperBuilder(toBuilder = true)
public class ConversionContext {
    private Converter converter;
    private ConversionKey currentConversionKey;
    private Map<Object, Object> arguments;


    @Deprecated(forRemoval = true)
    public <T> T getArgument(Object key) {
        return this.<T>argument(key).orElse(null);
    }

    public <T> T getRequiredArgument(Object key, Class<? extends T> valueClass) {
        return argument(key, valueClass).orElseThrow();
    }

    @Deprecated(forRemoval = true)
    @SuppressWarnings("unchecked")
    public <T> Optional<T> argument(Object key) {
        return Optional.ofNullable(key)
                .map(arguments::get)
                .map(value -> (T) value);
    }

    public <T> Optional<T> argument(Object key, Class<? extends T> valueClass) {
        return Optional.ofNullable(key)
                .map(arguments::get)
                .flatMap(this.resolveIfOptional(valueClass))
                .filter(valueClass::isInstance)
                .map(valueClass::cast);
    }

    private Function<Object, Optional<?>> resolveIfOptional(Class<?> targetClass) {
        return value -> (value instanceof Optional<?> && !targetClass.equals(Optional.class))
                ? (Optional<?>) value
                : Optional.of(value);
    }
}
