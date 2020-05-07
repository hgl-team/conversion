package org.hgl.convertion.api;

public interface TypeConverter<TS, TD> {
    TD convert(TS source);
}
