package org.hglteam.conversion.api.context;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.Converter;

import java.util.Map;
import java.util.Optional;

@Getter
@SuperBuilder(toBuilder = true)
public class ConversionContext {
    private Converter converter;
    private ConversionKey currentConversionKey;
    private Map<Object, Object> arguments;


    public <T> T getArgument(Object key) {
        return this.<T>argument(key).orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> argument(Object key) {
        return Optional.ofNullable(key)
                .map(arguments::get)
                .map(value -> (T) value);
    }
}
