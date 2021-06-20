package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionContext;
import org.hglteam.conversion.api.Converter;
import org.hglteam.conversion.api.DateFormatter;
import org.hglteam.conversion.api.DateFormattingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConversionConfiguration {
    @Bean
    public ConversionContext conversionContext() {
        return new ConversionTableContext();
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
