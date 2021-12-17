package org.hglteam.conversion.api.context;

import lombok.Getter;
import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.Converter;
import org.hglteam.conversion.api.DefaultConvertionKey;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

@Getter
public class ContextConversionBuilder<TD> {
    private Converter converter;
    private Type targetType;

    private Type sourceType;
    private ConversionKey conversionKey;
    private Object[] args;

    public ContextConversionBuilder(Converter converter, Type targetType) {
        this.converter = converter;
        this.targetType = targetType;
    }

    public ContextConversionBuilder<TD> withConvertionKey(ConversionKey conversionKey) {
        this.conversionKey = conversionKey;
        return this;
    }

    public ContextConversionBuilder<TD> withSourceType(Type sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public ContextConversionBuilder<TD> withTargetType(Type targetType) {
        this.targetType = targetType;
        return this;
    }

    public ContextConversionBuilder<TD> withArgs(Object... args) {
        this.args = args;
        return this;
    }

    public <TS> TD convert(TS source) {
        sourceType = Optional.ofNullable(sourceType)
                .orElse(source.getClass());

        Objects.requireNonNull(targetType);

        conversionKey = Optional.ofNullable(conversionKey)
                .orElseGet(() -> DefaultConvertionKey.builder()
                        .source(sourceType)
                        .target(targetType)
                        .build());

        return converter.convert(source, TypeConversionContext.builder()
                .converter(converter)
                .currentConversionKey(conversionKey)
                .arguments(args)
                .build());
    }
}
