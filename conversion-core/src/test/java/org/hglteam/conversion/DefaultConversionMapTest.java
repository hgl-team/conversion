package org.hglteam.conversion;

import org.hglteam.conversion.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DefaultConversionMapTest {

    private ConversionMap conversionTable;

    @BeforeEach
    void setup() {
        this.conversionTable = new DefaultConversionMap();
    }

    @Nested
    class RegisterTest {
        private ConversionKey key;
        private TypeConverter<?, ?> converter;

        @Test
        void put() {
            givenAWellDefinedConversionKey();
            givenACompatibleTypeConverter();
            whenPutANewConverterIntoTable();
            thenANewConversionIsRegistered();
        }

        @Test
        void putDuplicatedThrowException() {
            givenARegisteredConverter();
            givenADuplicatedConversionKey();
            givenADuplicatedConverter();
            assertThrows(ConversionMap.DuplicatedConversionKeyException.class,
                    this::whenPutANewConverterIntoTable);
        }

        private void givenARegisteredConverter() {
            conversionTable.register(new GenericTypeConverter<>(Double::intValue) { });
        }

        private void givenADuplicatedConversionKey() {
            key = DefaultConversionKey.builder()
                    .source(Double.class)
                    .target(Integer.class)
                    .build();
        }
        private void givenADuplicatedConverter() {
            this.converter = new GenericTypeConverter<>(Double::intValue) { };
        }

        private void givenAWellDefinedConversionKey() {
            key = DefaultConversionKey.builder()
                    .source(Double.class)
                    .target(Integer.class)
                    .build();
        }

        private void givenACompatibleTypeConverter() {
            this.converter = new GenericTypeConverter<>(Double::intValue) { };
        }

        private void whenPutANewConverterIntoTable() {
            conversionTable.register(
                    DefaultConversionKey.builder()
                            .source(key.getSource())
                            .target(key.getTarget())
                            .build(), converter);
        }

        private void thenANewConversionIsRegistered() {
            assertFalse(conversionTable.getAvailableConversions().isEmpty());
            assertTrue(conversionTable.getAvailableConversions().contains(key));
            assertEquals(converter, conversionTable.resolve(key));
        }
    }

    @Nested
    class ResolveTest {
        private ExplicitTypeConverter<?, ?> expectedConverter;
        private DefaultConversionKey key;
        private TypeConverter<?, ?> returnedConverter;

        @Test
        void getMatchingConverter() {
            givenAWellDefinedConverter();
            givenAMatchingConversionKey();
            whenGetCompatibleConverter();
            thenConverterIsTheExpected();
        }

        @Test
        void getCompatibleConverter() {
            givenAWellDefinedConverter();
            givenACompatibleConversionKey();
            whenGetCompatibleConverter();
            thenConverterIsTheExpected();

            var previousConverter = returnedConverter;

            whenGetCompatibleConverter();
            thenConverterIsTheExpected();
            assertEquals(previousConverter, returnedConverter);
        }

        @Test
        void getMatchingGenericTypeConverter() {
            givenAWellDefinedGenericConverter();
            givenACompatibleGenericConversionKey();
            whenGetCompatibleConverter();
            thenGenericReturnedConverterIsExpected();
        }

        @Test
        void getCompatibleGenericTypeConverter() {
            givenAWilcardGenericConverter();
            givenACompatibleGenericConversionKey();
            whenGetCompatibleConverter();
            thenGenericReturnedConverterIsExpected();
        }

        @Test
        void noConverterFound() {
            givenAWellDefinedConverter();
            givenANonDefinedConversionKey();
            assertThrows(ConversionMap.NoCompatibleKeyFoundException.class, this::whenGetCompatibleConverter);
        }

        @Test
        void noConverterFoundForType() {
            givenAWellDefinedConverter();
            givenANonDefinedConversionKeyWithDefinedTarget();
            assertThrows(ConversionMap.NoCompatibleKeyFoundException.class, this::whenGetCompatibleConverter);
            givenANonDefinedConversionKeyWithDefinedSource();
            assertThrows(ConversionMap.NoCompatibleKeyFoundException.class, this::whenGetCompatibleConverter);
            givenANonDefinedConversionKeyWithNoMatchingTypes();
            assertThrows(ConversionMap.NoCompatibleKeyFoundException.class, this::whenGetCompatibleConverter);
        }

        private void givenAWilcardGenericConverter() {
            this.expectedConverter = new GenericTypeConverter<AtomicReference<? extends CharSequence>, String>(AtomicReference::toString){};
            conversionTable.register(this.expectedConverter.getConversionKey()
                    , this.expectedConverter);
        }

        private void givenANonDefinedConversionKeyWithNoMatchingTypes() {
            this.key = DefaultConversionKey.builder()
                    .source(Double.class)
                    .target(Integer.class)
                    .build();
        }

        private void givenANonDefinedConversionKeyWithDefinedSource() {
            this.key = DefaultConversionKey.builder()
                    .source(new TypeDescriptor<AtomicReference<String>>(){}.getType())
                    .target(Integer.class)
                    .build();
        }

        private void givenANonDefinedConversionKeyWithDefinedTarget() {
            this.key = DefaultConversionKey.builder()
                    .source(Map.Entry.class)
                    .target(String.class)
                    .build();
        }

        private void givenANonDefinedConversionKey() {
            this.key = DefaultConversionKey.builder()
                    .source(String.class)
                    .target(Double.class)
                    .build();
        }

        private void givenAWellDefinedGenericConverter() {
            this.expectedConverter = new GenericTypeConverter<AtomicReference<String>, String>(AtomicReference::toString) {};
            conversionTable.register(this.expectedConverter.getConversionKey()
                    , this.expectedConverter);
        }

        private void givenACompatibleGenericConversionKey() {
            this.key = DefaultConversionKey.builder()
                    .source(new TypeDescriptor<AtomicReference<String>>(){}.getType())
                    .target(String.class)
                    .build();
        }

        private void thenGenericReturnedConverterIsExpected() {
            assertEquals(this.expectedConverter, this.returnedConverter);
        }

        private void givenACompatibleConversionKey() {
            this.key = DefaultConversionKey.builder()
                    .source(Integer.class)
                    .target(String.class)
                    .build();
        }

        private void givenAWellDefinedConverter() {
            this.expectedConverter = new GenericTypeConverter<>(Number::toString){};
            conversionTable.register(this.expectedConverter.getConversionKey()
                    , this.expectedConverter);
        }

        private void givenAMatchingConversionKey() {
            this.key = DefaultConversionKey.builder()
                    .source(Number.class)
                    .target(String.class)
                    .build();
        }

        private void whenGetCompatibleConverter() {
            this.returnedConverter = conversionTable.resolve(this.key);
        }

        private void thenConverterIsTheExpected() {
            assertEquals(this.expectedConverter, this.returnedConverter);
        }

    }

    @Nested
    class GetAvailableConversions {
        @Test
        void should_returnExpectedConversionKeys_when_getAvailableConversion() {
            var expectedConversions = Arrays
                    .<ExplicitTypeConverter<?, ?>>asList(
                    new GenericTypeConverter<Double, String>(Object::toString) {},
                    new GenericTypeConverter<Integer, String>(Object::toString) {},
                    new GenericTypeConverter<Float, String>(Object::toString) {},
                    new GenericTypeConverter<Long, String>(Object::toString) {});

            expectedConversions.forEach(conversionTable::register);

            var availableConversions = conversionTable.getAvailableConversions();

            assertEquals(expectedConversions.size(), availableConversions.size());
            assertTrue(availableConversions.containsAll(expectedConversions.stream()
                    .map(ExplicitTypeConverter::getConversionKey)
                    .collect(Collectors.toList())));
        }
    }
}
