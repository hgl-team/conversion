package org.hglteam.conversion.api;

import java.util.Collection;
import java.util.function.Supplier;

public interface ConversionMap {
    ConversionMap register(ConversionKey key, TypeConverter<?, ?> converter);
    ConversionMap register(TypeConverter<?, ?> converter);
    TypeConverter<?, ?> resolve(ConversionKey key);
    Collection<ConversionKey> getAvailableConversions();

    class DuplicatedConversionKeyException extends IllegalArgumentException {
        public DuplicatedConversionKeyException(ConversionKey conversionKey) {
            super(String.format("(%s): %s -> %s",
                    conversionKey,
                    conversionKey.getSource(),
                    conversionKey.getTarget()));
        }
    }

    class NoCompatibleKeyFoundException extends IllegalArgumentException {
        public NoCompatibleKeyFoundException(ConversionKey conversionKey) {
            super(String.format("%s -> %s",
                    conversionKey.getSource(),
                    conversionKey.getTarget()));
        }

        public static Supplier<NoCompatibleKeyFoundException> forKey(ConversionKey conversionKey) {
            return () -> new NoCompatibleKeyFoundException(conversionKey);
        }
    }
}
