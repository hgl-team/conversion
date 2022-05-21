package org.hglteam.conversion.api;

import lombok.Getter;
import org.hglteam.conversion.api.context.ConversionContext;

import java.lang.reflect.ParameterizedType;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class GenericTypeConverter<S, T> implements ExplicitTypeConverter<S, T> {
    private final BiFunction<ConversionContext, S, T> conversionFunction;
    @Getter
    private final ConversionKey conversionKey;

    public GenericTypeConverter(BiFunction<ConversionContext, S, T> conversionFunction) {
        this.conversionFunction = conversionFunction;
        this.conversionKey = GenericTypeConverter.inferConversionKey(getClass());
    }

    public GenericTypeConverter(Function<S, T> conversionFunction) {
        this.conversionFunction = ((context, source) -> conversionFunction.apply(source));
        this.conversionKey = GenericTypeConverter.inferConversionKey(getClass());
    }

    @Override
    public T convert(ConversionContext context, S source) {
        return this.conversionFunction.apply(context, source);
    }

    private static ConversionKey inferConversionKey(Class<?> aClass) {
        var superclass = aClass.getGenericSuperclass();

        if(superclass instanceof ParameterizedType) {
            var parameterizedClass = (ParameterizedType) superclass;

            return DefaultConversionKey.builder()
                    .source(parameterizedClass.getActualTypeArguments()[0])
                    .target(parameterizedClass.getActualTypeArguments()[1])
                    .build();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
