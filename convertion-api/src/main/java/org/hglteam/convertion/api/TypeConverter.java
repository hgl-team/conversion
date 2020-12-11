package org.hglteam.convertion.api;

public interface TypeConverter<TS, TD> {
    TD convert(TS source);
}
