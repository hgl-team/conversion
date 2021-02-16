package org.hglteam.convertion;

import org.hglteam.convertion.api.TypeConverter;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class GenericConverterTest {

    private ConvertionContextMap context;
    private ConvertidorEnteroABigInteger typeConverter;
    private GenericConverter converter;
    private int integerToBeConverted;
    private BigInteger bigIntegerObtained;

    @Test
    void convert() {
        givenAConvertionContext();
        givenAnIntegerToBigIntegerTypeConverter();
        givenAConverterUsingTheConvertionContext();
        givenAnIntegerToBeConverted();

        whenIntegerGetsConvertedToBigInteger();
        thenTheExpectedBigIntegerIsObtained();
    }

    private void givenAConvertionContext() {
        this.context = new ConvertionContextMap();
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
}
