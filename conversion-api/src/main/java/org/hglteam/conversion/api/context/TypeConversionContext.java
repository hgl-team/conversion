package org.hglteam.conversion.api.context;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.Converter;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class TypeConversionContext {
    private Converter converter;
    private ConversionKey currentConversionKey;
    private Object[] arguments;
}
