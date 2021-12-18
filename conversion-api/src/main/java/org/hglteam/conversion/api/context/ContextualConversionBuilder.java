package org.hglteam.conversion.api.context;

import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.Converter;
import org.hglteam.conversion.api.DefaultConvertionKey;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

public class ContextualConversionBuilder<TD> {
    private Converter converter;
    private Type targetType;
    private Type sourceType;
    private ConversionKey conversionKey;
    private Object[] args;

    public ContextualConversionBuilder(Converter converter, Type targetType) {
        this.converter = converter;
        this.targetType = targetType;
    }

    public ContextualConversionBuilder(Converter converter, Class<? extends TD> targetClass) {
        this(converter, (Type) targetClass);
    }

    public ContextualConversionBuilder<TD> withConvertionKey(ConversionKey conversionKey) {
        this.conversionKey = conversionKey;
        return this;
    }

    public ContextualConversionBuilder<TD> withSourceType(Type sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public ContextualConversionBuilder<TD> withTargetType(Type targetType) {
        this.targetType = targetType;
        return this;
    }

    public ContextualConversionBuilder<TD> withArgs(Object... args) {
        this.args = args;
        return this;
    }

    public <TS> TD convert(TS source) {
        var conversionKey = Optional.ofNullable(this.conversionKey)
                .orElseGet(() -> DefaultConvertionKey.builder()
                        .source(Objects.nonNull(sourceType) ? sourceType : source.getClass())
                        .target(targetType)
                        .build());
        var context = TypeConversionContext.builder()
                .converter(converter)
                .currentConversionKey(conversionKey)
                .arguments(args)
                .build();

        return converter.convert(source, context);
    }
}
