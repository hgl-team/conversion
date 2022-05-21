package org.hglteam.conversion.api;

import org.hglteam.conversion.api.context.ConversionContext;

public interface TypeConverter<S, T> {
    T convert(ConversionContext context, S source);
}
