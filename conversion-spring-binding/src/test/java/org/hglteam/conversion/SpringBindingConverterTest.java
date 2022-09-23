package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.ConversionMap;
import org.hglteam.conversion.api.Converter;
import org.hglteam.conversion.api.DefaultConversionKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SpringBindingConverterTest {
    @Mock
    private Converter converter;
    @Mock
    private ConversionMap conversionMap;

    private SpringBindingConverter bindingConverter;

    @BeforeEach
    void setup() {
        this.bindingConverter = new SpringBindingConverter(conversionMap, converter);
    }

    @Nested
    class GetConvertibleTypesTest {
        private List<DefaultConversionKey> conversionKeys;
        private Set<GenericConverter.ConvertiblePair> result;

        @Test
        void should_registerTypes() {
            givenConversionKeys();
            whenGetConvertibleTypes();
            whenReturnExpectedConvertiblePairs();
        }

        private void givenConversionKeys() {
            this.conversionKeys = List.of(
                    DefaultConversionKey.builder()
                            .source(Mockito.mock(ParameterizedType.class))
                            .target(Mockito.mock(ParameterizedType.class))
                            .build(),
                    DefaultConversionKey.builder()
                            .source(Mockito.mock(ParameterizedType.class))
                            .target(Integer.class)
                            .build(),
                    DefaultConversionKey.builder()
                            .source(Integer.class)
                            .target(Mockito.mock(ParameterizedType.class))
                            .build(),
                    DefaultConversionKey.builder()
                            .source(Integer.class)
                            .target(Double.class)
                            .build(),
                    DefaultConversionKey.builder()
                            .source(Integer.class)
                            .target(List.class)
                            .build());

            Mockito.doReturn(conversionKeys)
                    .when(conversionMap)
                    .getAvailableConversions();
        }

        private void whenGetConvertibleTypes() {
            this.result = bindingConverter.getConvertibleTypes();
        }

        private void whenReturnExpectedConvertiblePairs() {
            var expectedKeys = this.conversionKeys.stream()
                    .filter(key -> key.getSource() instanceof Class && key.getTarget() instanceof Class)
                    .collect(Collectors.<ConversionKey>toSet());

            var obtainedKeys = result.stream()
                    .map(pair -> DefaultConversionKey.builder()
                            .source(pair.getSourceType())
                            .target(pair.getTargetType())
                            .build())
                    .collect(Collectors.<ConversionKey>toSet());

            assertEquals(expectedKeys.size(), obtainedKeys.size());

            assertTrue(obtainedKeys.stream()
                    .allMatch(this.isExpectedPair(expectedKeys)));
        }

        private Predicate<ConversionKey> isExpectedPair(Set<ConversionKey> expectedKeys) {
            return conversionKey -> {
                assertTrue(expectedKeys.contains(conversionKey));

                return true;
            };
        }
    }

    @Nested
    class ConvertTest {
        @Test
        void given_anObject_when_convert_thenObjectGetsConverted() {
            var objectToConvert = 12;
            var sourceTypeDescriptor = TypeDescriptor.valueOf(Integer.class);
            var targetTypeDescriptor = TypeDescriptor.valueOf(Double.class);

            bindingConverter.convert(objectToConvert, sourceTypeDescriptor, targetTypeDescriptor);

            Mockito.verify(converter)
                    .convert(objectToConvert, sourceTypeDescriptor.getType(), targetTypeDescriptor.getType());
        }
    }
}
