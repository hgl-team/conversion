package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionMap;
import org.hglteam.conversion.api.Converter;
import org.hglteam.conversion.api.datetime.DateFormatter;
import org.hglteam.conversion.api.datetime.DateFormatMap;
import org.hglteam.conversion.datetime.DefaultDateFormatMap;
import org.hglteam.conversion.datetime.DefaultDateFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConversionConfiguration {
    @Bean
    public ConversionMap conversionMap() {
        return new DefaultConversionMap();
    }

    @Bean
    public DateFormatMap dateFormatMap() {
        return new DefaultDateFormatMap();
    }

    @Bean
    public Converter converter(ConversionMap conversionMap) {
        return new DefaultConverter(conversionMap);
    }

    @Bean
    public DateFormatter dateFormatter(DateFormatMap dateFormatMap) {
        return new DefaultDateFormatter(dateFormatMap);
    }

    @Bean
    public SpringBindingConverter springBindingConverter(ConversionMap conversionMap, Converter converter) {
        return new SpringBindingConverter(conversionMap, converter);
    }
}
