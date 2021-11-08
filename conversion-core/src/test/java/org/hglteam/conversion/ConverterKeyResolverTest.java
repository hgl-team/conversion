package org.hglteam.conversion;

import org.hglteam.conversion.api.GenericTypeConverter;
import org.hglteam.conversion.api.TypeConverter;
import org.hglteam.conversion.api.context.TypeConversionContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConverterKeyResolverTest {

    @Nested
    public class GetConverterKey {
        private TypeConverter<?, ?> converter;
        private org.hglteam.conversion.api.ConversionKey converterKey;

        @Test
        void getFromGenericSourceConverter () {
            givenAGenericSourceConverter();
            whenGetConverterKey();
            thenKeyDescriptionIsExpected();
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
            this.converterKey = ConversionKeyResolver.getConverterKey(this.converter);
        }

        private void thenKeyDescriptionIsExpected() {
            assertNotNull(this.converterKey);
            assertNotNull(this.converterKey.getSource());
            assertNotNull(this.converterKey.getTarget());
        }
    }

    public static class GenericSourceConverter implements TypeConverter<Generic<?>, Long>
    {
        @Override
        public Long convert(TypeConversionContext context, Generic<?> source) {
            return source.number.longValue();
        }
    }
}
