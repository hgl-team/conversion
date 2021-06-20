package org.hglteam.conversion.api;

import java.lang.reflect.Type;
import java.util.Collection;

public interface ConversionContext {
    ConversionContext register(ConversionKey key, TypeConverter<?, ?> converter);
    ConversionContext register(TypeConverter<?, ?> converter);

    TypeConverter<?, ?> resolve(ConversionKey key);
    TypeConverter<?, ?> resolve(Type source, Type target);

    Collection<ConversionKey> getAvailableConversions();
}
