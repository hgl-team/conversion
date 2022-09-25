package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionMap;
import org.hglteam.conversion.api.TypeConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Optional;

public class TypeConverterBeanPostProcessor implements BeanPostProcessor {
    private final ConversionMap conversionMap;

    public TypeConverterBeanPostProcessor(ConversionMap conversionMap) {
        this.conversionMap = conversionMap;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return Optional.of(bean)
                .filter(TypeConverter.class::isInstance)
                .<TypeConverter<?, ?>>map(TypeConverter.class::cast)
                .map(this::register)
                .orElse(bean);
    }

    public Object register(TypeConverter<?, ?> converter) {
        this.conversionMap.register(converter);
        return converter;
    }
}
