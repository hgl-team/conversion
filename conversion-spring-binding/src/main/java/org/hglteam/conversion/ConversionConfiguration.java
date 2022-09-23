package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ ConversionInfrastructureConfiguration.class })
public class ConversionConfiguration {

    @Bean
    public TypeConverterBeanPostProcessor typeConverterBeanPostProcessor(ConversionMap conversionMap) {
        return new TypeConverterBeanPostProcessor(conversionMap);
    }
}
