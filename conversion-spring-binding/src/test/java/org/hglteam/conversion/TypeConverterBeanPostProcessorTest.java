package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionMap;
import org.hglteam.conversion.api.TypeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TypeConverterBeanPostProcessorTest {
    @Mock
    private ConversionMap conversionMap;
    private TypeConverterBeanPostProcessor postProcessor;

    @BeforeEach
    void setup() {
        this.postProcessor = new TypeConverterBeanPostProcessor(conversionMap);
    }

    @Test
    void should_doNothing_when_beanIsNotATypeConverter() {
        var bean = new Object();

        postProcessor.postProcessAfterInitialization(bean, null);

        Mockito.verifyNoInteractions(conversionMap);
    }

    @Test
    void should_register_when_beanIsATypeConverter() {
        var bean = Mockito.mock(TypeConverter.class);

        postProcessor.postProcessAfterInitialization(bean, null);

        Mockito.verify(conversionMap)
                .register(bean);
    }

}