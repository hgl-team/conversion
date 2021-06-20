package org.hglteam.conversion.api;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

@Getter
public abstract class GenericTypeConverter<TS, TD> implements ExplicitTypeConverter<TS, TD> {
    private final Function<TS, TD> conversionFunction;
    private final ConversionKey conversionKey;

    public GenericTypeConverter(Function<TS, TD> conversionFunction) {
        this.conversionFunction = conversionFunction;
        this.conversionKey = GenericTypeConverter.inferConversionKey(getClass());
    }

    @Override
    public TD convert(TS source) {
        return this.conversionFunction.apply(source);
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
