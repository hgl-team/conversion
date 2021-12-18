package org.hglteam.conversion.api.context;

import lombok.Getter;
import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.Converter;
import org.hglteam.conversion.api.DefaultConvertionKey;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

@Getter
public class ContextualConversionBuilder<TD> {
    private TypeConversionContext.TypeConversionContextBuilder<?, ?> contextBuilder;
    private DefaultConvertionKey.DefaultConvertionKeyBuilder<?, ?> convertionKeyBuilder;

    public ContextualConversionBuilder(Converter converter, Type targetType) {
        contextBuilder = TypeConversionContext.builder()
                .converter(converter);
        convertionKeyBuilder = DefaultConvertionKey.builder()
                .target(targetType);
    }

    public ContextualConversionBuilder(Converter converter, Class<? extends TD> targetClass) {
        this(converter, (Type) targetClass);
    }

    public ContextualConversionBuilder<TD> withConvertionKey(ConversionKey conversionKey) {
        this.convertionKeyBuilder
                .source(conversionKey.getSource())
                .target(conversionKey.getTarget());
        return this;
    }

    public ContextualConversionBuilder<TD> withSourceType(Type sourceType) {
        this.convertionKeyBuilder.source(sourceType);
        return this;
    }

    public ContextualConversionBuilder<TD> withTargetType(Type targetType) {
        this.convertionKeyBuilder.target(targetType);
        return this;
    }

    public ContextualConversionBuilder<TD> withArgs(Object... args) {
        this.contextBuilder.arguments(args);
        return this;
    }

    public <TS> TD convert(TS source) {
        var conversionKey = Optional.of(this.convertionKeyBuilder.build())
                .map(key -> Objects.isNull(key.getSource()) ?
                        key.toBuilder().source(source.getClass()).build() :
                        key)
                .orElseThrow();
        var context = this.contextBuilder
                .currentConversionKey(conversionKey)
                .build();

        return context.getConverter().convert(source, context);
    }
}
