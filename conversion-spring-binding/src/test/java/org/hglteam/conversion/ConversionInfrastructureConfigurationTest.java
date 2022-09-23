package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionMap;
import org.hglteam.conversion.api.Converter;
import org.hglteam.conversion.api.datetime.DateFormatMap;
import org.hglteam.conversion.api.datetime.DateFormatter;
import org.hglteam.conversion.datetime.DefaultDateFormatMap;
import org.hglteam.conversion.datetime.DefaultDateFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = ConversionInfrastructureConfiguration.class
)
class ConversionInfrastructureConfigurationTest {

    @Autowired
    private ConversionMap conversionMap;
    @Autowired
    private Converter converter;
    @Autowired
    private DateFormatMap dateFormatMap;
    @Autowired
    private DateFormatter dateFormatter;
    @Autowired
    private SpringBindingConverter bindingConverter;

    @Test
    void should_setupExpectedBeans() {
        assertTrue(conversionMap instanceof DefaultConversionMap);
        assertTrue(converter instanceof DefaultConverter);
        assertTrue(dateFormatMap instanceof DefaultDateFormatMap);
        assertTrue(dateFormatter instanceof DefaultDateFormatter);
    }
}