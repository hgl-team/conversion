package org.hglteam.convertion;

import org.hglteam.convertion.api.TypeConverter;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class GenericConverterTest {

    public static final int EXPECTED_LONG_VALUE = 123456;
    private ConversionContextMap context;
    private TypeConverter<?,?> typeConverter;
    private GenericConverter converter;
    private int integerToBeConverted;
    private BigInteger bigIntegerObtained;
    private Generic<?> generic;
    private Long longValue;

    @Test
    void convert() {
        givenAConvertionContext();
        givenAnIntegerToBigIntegerTypeConverter();
        givenAConverterUsingTheConvertionContext();
        givenAnIntegerToBeConverted();

        whenIntegerGetsConvertedToBigInteger();
        thenTheExpectedBigIntegerIsObtained();
    }

    @Test
    void convertFromGeneric() {
        givenAConvertionContext();
        givenAGenericToLongTypeConverter();
        givenAConverterUsingTheConvertionContext();
        givenAGenericToBeConverted();

        whenGenericGetsConvertedToLong();
        thenTheExpectedLongIsObtained();
    }

    private void givenAGenericToBeConverted() {
        this.generic = new Generic<BigInteger>(BigInteger.valueOf(EXPECTED_LONG_VALUE));
    }

    private void whenGenericGetsConvertedToLong() {
        this.longValue = this.converter.convert(this.generic, Long.class);
    }

    private void thenTheExpectedLongIsObtained() {
        assertNotNull(this.longValue);
        assertEquals(EXPECTED_LONG_VALUE, this.longValue);
    }

    private void givenAGenericToLongTypeConverter() {
        this.typeConverter = new GenericToLongConverter();
        this.context.register(this.typeConverter);
    }

    private void givenAConvertionContext() {
        this.context = new ConversionContextMap();
    }

    private void givenAnIntegerToBigIntegerTypeConverter() {
        this.typeConverter = new ConvertidorEnteroABigInteger();

        this.context.register(this.typeConverter);
    }

    private void givenAConverterUsingTheConvertionContext() {
        this.converter = new GenericConverter(this.context);
    }

    private void givenAnIntegerToBeConverted() {
        this.integerToBeConverted = 2345543;
    }

    private void whenIntegerGetsConvertedToBigInteger() {
        Number number = this.integerToBeConverted;
        this.bigIntegerObtained = this.converter.convert(number, Integer.class, BigInteger.class);
    }

    private void thenTheExpectedBigIntegerIsObtained() {
        assertNotNull(this.bigIntegerObtained);
        assertEquals(this.integerToBeConverted, this.bigIntegerObtained.intValue());
    }

    private static class ConvertidorEnteroABigInteger implements TypeConverter<Integer, BigInteger> {
        @Override
        public BigInteger convert(Integer source) {
            return BigInteger.valueOf(source.longValue());
        }
    }

    private static class GenericToLongConverter
            implements TypeConverter<Generic<?>, Long> {
        @Override
        public Long convert(Generic<?> source) {
            return source.number.longValue();
        }
    }

}
