package org.hglteam.conversion.api;

public interface ExplicitTypeConverter<TS, TD> extends TypeConverter<TS, TD> {
    ConversionKey getConversionKey();
}
