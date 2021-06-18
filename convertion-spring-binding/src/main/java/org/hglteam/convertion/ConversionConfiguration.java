package org.hglteam.convertion;

import org.hglteam.convertion.api.ConversionContext;
import org.hglteam.convertion.api.Converter;
import org.hglteam.convertion.api.DateFormatter;
import org.hglteam.convertion.api.DateFormattingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConversionConfiguration {
    @Bean
    public ConversionContext conversionContext() {
        return new ConversionContextMap();
    }

    @Bean
    public DateFormattingContext dateFormatterContext() {
        return new GenericDateFormattingContext();
    }

    @Bean
    public Converter converter(ConversionContext conversionContext) {
        return new GenericConverter(conversionContext);
    }

    @Bean
    public DateFormatter dateFormatter(DateFormattingContext dateFormattingContext) {
        return new GenericDateFormatter(dateFormattingContext);
    }

    @Bean
    public SpringBindingConverter springBindingConverter(ConversionContext conversionContext, Converter converter) {
        return new SpringBindingConverter(conversionContext, converter);
    }
}
