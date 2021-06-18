package org.hglteam.convertion;

import org.hglteam.convertion.api.TypeConverter;
import org.hglteam.convertion.api.TypeDescriptor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConverterKeyResolverTest {

    @Nested
    public class GetConverterKey {
        private TypeConverter<?, ?> converter;
        private ConversionKey converterKey;

        @Test
        void getFromGenericSourceConverter () {
            givenAGenericSourceConverter();
            whenGetConverterKey();
            thenKeyDescriptionIsExpected();
        }

        private void givenAGenericSourceConverter() {
            this.converter = new GenericSourceConverter();
        }

        private void whenGetConverterKey() {
            this.converterKey = ConversionKeyResolver.getConverterKey(this.converter);
        }

        private void thenKeyDescriptionIsExpected() {
            assertNotNull(this.converterKey);
            assertNotNull(this.converterKey.getSourceClass());
            assertNotNull(this.converterKey.getTargetClass());
        }
    }

    @Nested
    public class ConvertionKeyCompatibility {
        private TypeConverter<?, ?> converter;
        private ConversionKey converterKey;
        private ConversionKey compatibleKey;
        private boolean compatible;

        @Test
        void keyCompatibleWithGenericSourceConverter () {
            givenAGenericSourceConverter();
            givenAConvertionKeyFromTheConverter();
            givenACompatibleConvertionKey();
            whenCheckKeyEquality();
            thenKeysAreCompatible();
        }

        @Test
        void GenericKeyCompatibleWithGenericSourceConverter() {
            givenAGenericSourceConverter();
            givenAConvertionKeyFromTheConverter();
            givenACompatibleGenericConvertionKey();
            whenCheckKeyEquality();
            thenKeysAreCompatible();
        }

        private void givenACompatibleGenericConvertionKey() {
            this.compatibleKey = new ConversionKey(new TypeDescriptor<Generic<?>>(){}.getType(), Long.class);
        }

        private void whenCheckKeyEquality() {
            this.compatible = this.converterKey.equals(this.compatibleKey);
        }

        private void thenKeysAreCompatible() {
            assertTrue(this.compatible);
        }

        private void givenACompatibleConvertionKey() {
            var gtype = new Generic<>(1);
            this.compatibleKey = new ConversionKey(gtype.getClass(), Long.class);
        }

        private void givenAGenericSourceConverter() {
            this.converter = new GenericSourceConverter();
        }

        private void givenAConvertionKeyFromTheConverter() {
            this.converterKey = ConversionKeyResolver.getConverterKey(this.converter);
        }

    }

    public static class GenericSourceConverter implements TypeConverter<Generic<?>, Long>
    {
        @Override
        public Long convert(Generic<?> source) {
            return source.number.longValue();
        }
    }
}
