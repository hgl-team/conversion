package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = ConversionConfiguration.class
)
class ConversionConfigurationTest {
    @Autowired
    private ConversionMap conversionMap;
    @Autowired
    private TypeConverterBeanPostProcessor typeConverterBeanPostProcessor;

    @Test
    void should_setupExpectedValues() {
        assertNotNull(conversionMap);
        assertNotNull(typeConverterBeanPostProcessor);
    }

}