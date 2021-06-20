package org.hglteam.conversion.api;

public interface TypeConverter<TS, TD> {
    TD convert(TS source);
}
