package org.hglteam.conversion.api.context;

import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.Converter;
import org.hglteam.conversion.api.DefaultConversionKey;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ContextualConversionBuilder<T> {
    private final Converter converter;
    private final Map<Object, Object> args;
    private Type targetType;
    private Type sourceType;
    private ConversionKey conversionKey;

    public ContextualConversionBuilder(Converter converter, Type targetType) {
        this.converter = converter;
        this.targetType = targetType;
        this.args = new HashMap<>();
    }

    public ContextualConversionBuilder(Converter converter, Class<? extends T> targetClass) {
        this(converter, (Type) targetClass);
    }

    public ContextualConversionBuilder<T> withConvertionKey(ConversionKey conversionKey) {
        this.conversionKey = conversionKey;
        return this;
    }

    public ContextualConversionBuilder<T> withSourceType(Type sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public ContextualConversionBuilder<T> withTargetType(Type targetType) {
        this.targetType = targetType;
        return this;
    }

    public ContextualConversionBuilder<T> withArgs(Map<Object, Object> args) {
        this.args.clear();
        this.args.putAll(args);
        return this;
    }

    public ContextualConversionBuilder<T> withArg(Object key, Object value) {
        this.args.put(key, value);
        return this;
    }

    public <S> T convert(S source) {
        var finalConversionKey = Optional.ofNullable(this.conversionKey)
                .orElseGet(() -> DefaultConversionKey.builder()
                        .source(Objects.nonNull(sourceType) ? sourceType : source.getClass())
                        .target(targetType)
                        .build());
        var context = ConversionContext.builder()
                .converter(converter)
                .currentConversionKey(finalConversionKey)
                .arguments(args)
                .build();

        return converter.convert(source, context);
    }
}
