package org.hgl.convertion.api;

import java.util.function.Function;

public interface Converter {
    <TS, TD> TD convert(TS source, Class<TS> sourceClass, Class<TD> destinationClass);
    <TS, TD> TD convert(TS source, Class<TD> destinationClass);

    <TS, TD> Function<TS, TD> convertTo(Class<TS> sourceClass, Class<TD> destinationClass);
    <TS, TD> Function<TS, TD> convertTo(Class<TD> destinationClass);
}
