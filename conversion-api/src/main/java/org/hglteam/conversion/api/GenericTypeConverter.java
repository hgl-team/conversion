package org.hglteam.conversion.api;

import lombok.Getter;
import org.hglteam.conversion.api.context.TypeConversionContext;

import java.lang.reflect.ParameterizedType;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public abstract class GenericTypeConverter<TS, TD> implements ExplicitTypeConverter<TS, TD> {
    private final BiFunction<TypeConversionContext, TS, TD> conversionFunction;
    private final ConversionKey conversionKey;

    public GenericTypeConverter(BiFunction<TypeConversionContext, TS, TD> conversionFunction) {
        this.conversionFunction = conversionFunction;
        this.conversionKey = GenericTypeConverter.inferConversionKey(getClass());
    }

    public GenericTypeConverter(Function<TS, TD> conversionFunction) {
        this.conversionFunction = ((context, ts) -> conversionFunction.apply(ts));
        this.conversionKey = GenericTypeConverter.inferConversionKey(getClass());
    }

    @Override
    public TD convert(TypeConversionContext context, TS source) {
        return this.conversionFunction.apply(context, source);
    }

    private static ConversionKey inferConversionKey(Class<?> aClass) {
        var superclass = aClass.getGenericSuperclass();

        if(superclass instanceof ParameterizedType) {
            var parameterizedClass = (ParameterizedType) superclass;

            return DefaultConvertionKey.builder()
                    .source(parameterizedClass.getActualTypeArguments()[0])
                    .target(parameterizedClass.getActualTypeArguments()[1])
                    .build();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
