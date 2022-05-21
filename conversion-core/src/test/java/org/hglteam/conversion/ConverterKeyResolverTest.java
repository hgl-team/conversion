package org.hglteam.conversion;

import org.hglteam.conversion.api.GenericTypeConverter;
import org.hglteam.conversion.api.TypeConverter;
import org.hglteam.conversion.api.context.ConversionContext;
import org.hglteam.conversion.stub.Generic;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConverterKeyResolverTest {

    @Nested
    class GetConverterKey {
        private TypeConverter<?, ?> converter;
        private org.hglteam.conversion.api.ConversionKey converterKey;

        @Test
        void getFromGenericSourceConverter () {
            givenAGenericSourceConverter();
            whenGetConverterKey();
            thenKeyDescriptionIsExpected();

            var previousKey = this.converterKey;

            whenGetConverterKey();

            assertEquals(previousKey, this.converterKey);
        }

        @Test
        void getFromExplicitTypeConverter() {
            givenALambdaConverter();
            whenGetConverterKey();
            thenKeyDescriptionIsExpected();
        }

        private void givenALambdaConverter() {
            this.converter = new GenericTypeConverter<BigDecimal, String>((context, value) -> value.toString()) { };
        }

        private void givenAGenericSourceConverter() {
            this.converter = new GenericSourceConverter();
        }

        private void whenGetConverterKey() {
            this.converterKey = ConversionKeyResolver.inferConversionKey(this.converter);
        }

        private void thenKeyDescriptionIsExpected() {
            assertNotNull(this.converterKey);
            assertNotNull(this.converterKey.getSource());
            assertNotNull(this.converterKey.getTarget());
        }
    }

    public static class GenericSourceConverter extends BaseGenericConverter<Generic<BigInteger>, Long> {
        @Override
        public Long convert(ConversionContext context, Generic<BigInteger> source) {
            return source.number.longValue();
        }
    }

    public static abstract class BaseGenericConverter<S, T extends Number> implements TypeConverter<S, T> { }
}
