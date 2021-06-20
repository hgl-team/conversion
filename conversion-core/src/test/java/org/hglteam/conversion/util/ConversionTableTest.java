package org.hglteam.conversion.util;

import org.hglteam.conversion.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ConversionTableTest {

    private ConversionTable conversionTable;

    @BeforeEach
    void setup() {
        this.conversionTable = new ConversionTable();
    }

    @Nested
    public class PutConverterTest {
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
            assertThrows(ConversionTable.DuplicatedConversionKeyException.class,
                    this::whenPutANewConverterIntoTable);
        }

        private void givenARegisteredConverter() {
            conversionTable.put(Double.class, Integer.class, new GenericTypeConverter<>(Double::intValue) { });
        }

        private void givenADuplicatedConversionKey() {
            key = DefaultConvertionKey.builder()
                    .source(Double.class)
                    .target(Integer.class)
                    .build();
        }

        private void givenADuplicatedConverter() {
            this.converter = new GenericTypeConverter<>(Double::intValue) { };
        }

        private void givenAWellDefinedConversionKey() {
            key = DefaultConvertionKey.builder()
                    .source(Double.class)
                    .target(Integer.class)
                    .build();
        }

        private void givenACompatibleTypeConverter() {
            this.converter = new GenericTypeConverter<>(Double::intValue) { };
        }

        private void whenPutANewConverterIntoTable() {
            conversionTable.put(key.getSource(), key.getTarget(), converter);
        }

        private void thenANewConversionIsRegistered() {
            assertFalse(conversionTable.getAvailableConversion().isEmpty());
            assertTrue(conversionTable.getAvailableConversion().contains(key));
            assertEquals(converter, conversionTable.getCompatibleConverter(key));
        }
    }

    @Nested
    public class GetCompatibleConverter {
        private ExplicitTypeConverter<?, ?> expectedConverter;
        private DefaultConvertionKey key;
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
        }

        @Test
        void getMatchingGenericTypeConverter() {
            givenAWellDefinedGenericConverter();
            givenACompatibleGenericConversionKey();
            whenGetCompatibleConverter();
            thenGenericReturnedConverterIsExpected();
        }

        @Test
        void noConverterFound() {
            givenAWellDefinedConverter();
            givenANonDefinedConversionKey();
            assertThrows(ConversionTable.NoCompatibleKeyFoundException.class, this::whenGetCompatibleConverter);
        }

        @Test
        void noConverterFoundForType() {
            givenAWellDefinedConverter();
            givenANonDefinedConversionKeyWithDefinedTarget();
            assertThrows(ConversionTable.NoCompatibleKeyFoundException.class, this::whenGetCompatibleConverter);
        }

        private void givenANonDefinedConversionKeyWithDefinedTarget() {
            this.key = DefaultConvertionKey.builder()
                    .source(Map.Entry.class)
                    .target(String.class)
                    .build();
        }

        private void givenANonDefinedConversionKey() {
            this.key = DefaultConvertionKey.builder()
                    .source(String.class)
                    .target(Double.class)
                    .build();
        }

        private void givenAWellDefinedGenericConverter() {
            this.expectedConverter = new GenericTypeConverter<AtomicReference<String>, String>(AtomicReference::toString) {};
            conversionTable.put(this.expectedConverter.getConversionKey().getSource()
                    , this.expectedConverter.getConversionKey().getTarget()
                    , this.expectedConverter);
        }

        private void givenACompatibleGenericConversionKey() {
            this.key = DefaultConvertionKey.builder()
                    .source(new TypeDescriptor<AtomicReference<String>>(){}.getType())
                    .target(String.class)
                    .build();
        }

        private void thenGenericReturnedConverterIsExpected() {
            assertEquals(this.expectedConverter, this.returnedConverter);
        }

        private void givenACompatibleConversionKey() {
            this.key = DefaultConvertionKey.builder()
                    .source(Integer.class)
                    .target(String.class)
                    .build();
        }

        private void givenAWellDefinedConverter() {
            this.expectedConverter = new GenericTypeConverter<>(Number::toString){};
            conversionTable.put(this.expectedConverter.getConversionKey().getSource()
                    , this.expectedConverter.getConversionKey().getTarget()
                    , this.expectedConverter);
        }

        private void givenAMatchingConversionKey() {
            this.key = DefaultConvertionKey.builder()
                    .source(Number.class)
                    .target(String.class)
                    .build();
        }

        private void whenGetCompatibleConverter() {
            this.returnedConverter = conversionTable.getCompatibleConverter(this.key);
        }

        private void thenConverterIsTheExpected() {
            assertEquals(this.expectedConverter, this.returnedConverter);
        }

    }
}
