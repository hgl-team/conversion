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
        private ConverterKey converterKey;

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
            this.converterKey = ConverterKeyResolver.getConverterKey(this.converter);
        }

        private void thenKeyDescriptionIsExpected() {
            assertNotNull(this.converterKey);
            assertNotNull(this.converterKey.getSourceClass());
            assertNotNull(this.converterKey.getTargetClass());
            return;
        }
    }

    @Nested
    public class ConvertionKeyCompatibility {
        private TypeConverter<?, ?> converter;
        private ConverterKey converterKey;
        private ConverterKey compatibleKey;
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
            this.compatibleKey = new ConverterKey(new TypeDescriptor<Generic<?>>(){}.getType(), Long.class);
        }

        private void whenCheckKeyEquality() {
            this.compatible = this.converterKey.equals(this.compatibleKey);
        }

        private void thenKeysAreCompatible() {
            assertTrue(this.compatible);
        }

        private void givenACompatibleConvertionKey() {
            var gtype = new Generic<Integer>(1);
            this.compatibleKey = new ConverterKey(gtype.getClass(), Long.class);
        }

        private void givenAGenericSourceConverter() {
            this.converter = new GenericSourceConverter();
        }

        private void givenAConvertionKeyFromTheConverter() {
            this.converterKey = ConverterKeyResolver.getConverterKey(this.converter);
        }

    }

    public class GenericSourceConverter
        implements TypeConverter<Generic<?>, Long>
    {
        @Override
        public Long convert(Generic<?> source) {
            return source.number.longValue();
        }
    }

    public class GenericTargetConverter
            implements TypeConverter<Integer, Generic<?>> {
        @Override
        public Generic<?> convert(Integer source) {
            return new Generic<Integer>(source);
        }
    }

    public class GenericSourceTargetConverter
            implements TypeConverter<Generic<?>, Generic<?>> {
        @Override
        public Generic<?> convert(Generic<?> source) {
            return new Generic<Long>(source.number.longValue());
        }
    }

}
