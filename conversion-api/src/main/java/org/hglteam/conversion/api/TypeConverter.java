package org.hglteam.conversion.api;

import org.hglteam.conversion.api.context.TypeConversionContext;

public interface TypeConverter<TS, TD> {
    TD convert(TypeConversionContext context, TS source);
}
