package org.hglteam.conversion;

import org.hglteam.conversion.api.*;
import org.hglteam.conversion.api.context.ConversionContext;
import org.hglteam.conversion.stub.Generic;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DefaultConverterTest {
    private static final int EXPECTED_LONG_VALUE = 123456;
    public static final String EXPECTED_DATE_TIME_FORMATTER = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private ConversionMap context;
    private TypeConverter<?,?> typeConverter;
    private DefaultConverter converter;


    @Nested
    class ConvertUsingSourceAndTargetTypeTest {
        private BigInteger bigIntegerObtained;
        private int integerToBeConverted;

        @Test
        void given_sourceAndTargetType_when_convert_then_shouldReturnValue() {
            givenAConvertionContext();
            givenAnIntegerToBigIntegerTypeConverter();
            givenAConverterUsingTheConvertionContext();
            givenAnIntegerToBeConverted();

            whenIntegerGetsConvertedToBigInteger();
            thenTheExpectedBigIntegerIsObtained();
        }

        @Test
        void given_targetType_when_convert_then_shouldReturnValue() {
            givenAConvertionContext();
            givenAnIntegerToBigIntegerTypeConverter();
            givenAConverterUsingTheConvertionContext();
            givenAnIntegerToBeConverted();

            whenIntegerGetsConvertedToBigIntegerByClass();
            thenTheExpectedBigIntegerIsObtained();
        }

        @Test
        void given_sourceAndTargetType_when_convertTo_then_shouldReturnValue() {
            givenAConvertionContext();
            givenAnIntegerToBigIntegerTypeConverter();
            givenAConverterUsingTheConvertionContext();
            givenAnIntegerToBeConverted();

            whenIntegerGetsConvertedToBigIntegerUsingConvertTo();
            thenTheExpectedBigIntegerIsObtained();
        }

        @Test
        void given_targetType_when_convertTo_then_shouldReturnValue() {
            givenAConvertionContext();
            givenAnIntegerToBigIntegerTypeConverter();
            givenAConverterUsingTheConvertionContext();
            givenAnIntegerToBeConverted();

            whenIntegerGetsConvertedToBigIntegerByClassUsingConvertTo();
            thenTheExpectedBigIntegerIsObtained();
        }

        private void whenIntegerGetsConvertedToBigIntegerUsingConvertTo() {
            this.bigIntegerObtained = converter.<Integer, BigInteger>convertTo(Integer.class, BigInteger.class)
                    .apply(this.integerToBeConverted);
        }

        private void whenIntegerGetsConvertedToBigIntegerByClassUsingConvertTo() {
            this.bigIntegerObtained = converter.<Integer, BigInteger>convertTo(BigInteger.class)
                    .apply(this.integerToBeConverted);
        }

        private void whenIntegerGetsConvertedToBigIntegerByClass() {
            this.bigIntegerObtained = converter.convert(this.integerToBeConverted, BigInteger.class);
        }

        private void givenAnIntegerToBigIntegerTypeConverter() {
            typeConverter = new IntegerToBigIntegerConverter();
            context.register(typeConverter);
        }

        private void givenAnIntegerToBeConverted() {
            this.integerToBeConverted = 2345543;
        }

        private void whenIntegerGetsConvertedToBigInteger() {
            Number number = this.integerToBeConverted;
            this.bigIntegerObtained = converter.convert(number, Integer.class, BigInteger.class);
        }

        private void thenTheExpectedBigIntegerIsObtained() {
            assertNotNull(this.bigIntegerObtained);
            assertEquals(this.integerToBeConverted, this.bigIntegerObtained.intValue());
        }
    }

    @Nested
    class ConvertUsingConversionKeyTest {
        private Long longValue;
        private Generic<?> generic;

        @Test
        void given_aConversionKey_when_convert_then_convertUsingConversionKey() {
            givenAConvertionContext();
            givenAGenericToLongTypeConverter();
            givenAConverterUsingTheConvertionContext();
            givenAGenericToBeConverted();

            whenGenericGetsConvertedToLong();
            thenTheExpectedLongIsObtained();
        }

        @Test
        void given_aConversionKey_when_convertTo_then_convertUsingConversionKey() {
            givenAConvertionContext();
            givenAGenericToLongTypeConverter();
            givenAConverterUsingTheConvertionContext();
            givenAGenericToBeConverted();

            whenGenericGetsConvertedToLongUsingConvertTo();
            thenTheExpectedLongIsObtained();
        }

        private void whenGenericGetsConvertedToLongUsingConvertTo() {
            this.longValue = converter.<Generic<?>, Long>convertTo(new GenericConversionKey<Generic<?>, Long>(){})
                    .apply(this.generic);
        }

        private void givenAGenericToLongTypeConverter() {
            typeConverter = new GenericToLongConverter();
            context.register(typeConverter);
        }

        private void givenAGenericToBeConverted() {
            this.generic = new Generic<>(BigInteger.valueOf(EXPECTED_LONG_VALUE));
        }

        private void whenGenericGetsConvertedToLong() {
            this.longValue = converter.convert(this.generic, new GenericConversionKey<Generic<?>, Long>(){});
        }

        private void thenTheExpectedLongIsObtained() {
            assertNotNull(this.longValue);
            assertEquals(EXPECTED_LONG_VALUE, this.longValue);
        }
    }

    @Nested
    class ConvertUsingContextTest {
        private final DateTimeFormatter expectedFormatter = DateTimeFormatter
                .ofPattern(EXPECTED_DATE_TIME_FORMATTER);
        private LocalDateTime localDateTime;
        private String result;

        @Test
        void given_conversionContext_when_convert_then_convertUsingContext() {
            givenAConvertionContext();
            givenALocalDateTimeToStringConverter();
            givenAConverterUsingTheConvertionContext();
            givenALocalDateTimeToConvert();
            whenConvertLocalDateTimeToString();
            thenResultIsExpected();
        }

        @Test
        void given_conversionContext_when_convertToByType_then_convertUsingContext() {
            givenAConvertionContext();
            givenALocalDateTimeToStringConverter();
            givenAConverterUsingTheConvertionContext();
            givenALocalDateTimeToConvert();
            whenConvertLocalDateTimeToStringUsingConvertToByType();
            thenResultIsExpected();
        }

        private void whenConvertLocalDateTimeToStringUsingConvertToByType() {
            Type type = String.class;

            this.result = converter.<String>withContext(type)
                    .withArg("format", EXPECTED_DATE_TIME_FORMATTER)
                    .convert(this.localDateTime);
        }

        @Test
        void given_conversionContext_when_convertTo_then_convertUsingContext() {
            givenAConvertionContext();
            givenALocalDateTimeToStringConverter();
            givenAConverterUsingTheConvertionContext();
            givenALocalDateTimeToConvert();
            whenConvertLocalDateTimeToStringUsingConvertTo();
            thenResultIsExpected();
        }

        private void whenConvertLocalDateTimeToStringUsingConvertTo() {
            var context = ConversionContext.builder()
                    .converter(converter)
                    .currentConversionKey(DefaultConversionKey.builder()
                            .source(LocalDateTime.class)
                            .target(String.class)
                            .build())
                    .arguments(Map.of("format", EXPECTED_DATE_TIME_FORMATTER))
                    .build();
            this.result = converter.<LocalDateTime, String>convertTo(context)
                    .apply(this.localDateTime);
        }

        private void givenALocalDateTimeToStringConverter() {
            typeConverter = new GenericTypeConverter<>(DefaultConverterTest::dateToStringConverter){};
            context.register(typeConverter);
        }

        private void givenALocalDateTimeToConvert() {
            this.localDateTime = LocalDateTime.now();
        }

        private void whenConvertLocalDateTimeToString() {
            this.result = converter.withContext(String.class)
                    .withArg("format", EXPECTED_DATE_TIME_FORMATTER)
                    .convert(this.localDateTime);
        }

        private void thenResultIsExpected() {
            assertEquals(expectedFormatter.format(localDateTime), this.result);
        }
    }

    @Nested
    class ConvertUsingTargetTypeTest {
        private String source;
        private List<String> result;

        @Test
        void given_conversionContext_when_convert_then_convertUsingContext() {
            givenAConvertionContext();
            givenAStringToListStringConverter();
            givenAConverterUsingTheConvertionContext();
            givenAStringToConvert();
            whenConvertStringToListString();
            thenResultIsExpected();
        }

        @Test
        void given_conversionContext_when_convertTo_then_convertUsingContext() {
            givenAConvertionContext();
            givenAStringToListStringConverter();
            givenAConverterUsingTheConvertionContext();
            givenAStringToConvert();
            whenConvertStringToListStringUsingConvertTo();
            thenResultIsExpected();
        }

        private void whenConvertStringToListStringUsingConvertTo() {
            this.result = converter.<String, List<String>>convertTo(new TypeDescriptor<List<String>>(){}.getType())
                    .apply(source);
        }

        private void givenAStringToListStringConverter() {
            typeConverter = new StringToListStringConverter();
            context.register(typeConverter);
        }

        private void givenAStringToConvert() {
            this.source = "a,b,c";
        }

        private void whenConvertStringToListString() {
            this.result = converter.convert(source, new TypeDescriptor<List<String>>(){}.getType());
        }

        private void thenResultIsExpected() {
            assertTrue(Arrays.stream(this.source.split(","))
                    .allMatch(this.result::contains));
        }
    }

    private static String dateToStringConverter(ConversionContext context, LocalDateTime source) {
        var format = context.<String>getArgument("format");
        var locale = context.<Locale>argument("locale")
                .orElse(Locale.getDefault());
        var formatter = DateTimeFormatter.ofPattern(format, locale);

        return source.format(formatter);
    }

    private void givenAConvertionContext() {
        this.context = new DefaultConversionMap();
    }

    private void givenAConverterUsingTheConvertionContext() {
        this.converter = new DefaultConverter(this.context);
    }

    private static class IntegerToBigIntegerConverter implements TypeConverter<Integer, BigInteger> {
        @Override
        public BigInteger convert(ConversionContext context, Integer source) {
            return BigInteger.valueOf(source.longValue());
        }
    }

    private static class GenericToLongConverter implements TypeConverter<Generic<?>, Long> {
        @Override
        public Long convert(ConversionContext context, Generic<?> source) {
            return source.number.longValue();
        }
    }

    private static class StringToListStringConverter implements TypeConverter<String, List<String>> {

        @Override
        public List<String> convert(ConversionContext context, String source) {
            return Arrays.stream(source.split(","))
                    .collect(Collectors.toList());
        }
    }
}
