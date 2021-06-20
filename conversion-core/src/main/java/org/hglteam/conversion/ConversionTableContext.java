package org.hglteam.conversion;

import org.hglteam.conversion.api.ConversionContext;
import org.hglteam.conversion.api.ConversionKey;
import org.hglteam.conversion.api.DefaultConvertionKey;
import org.hglteam.conversion.api.TypeConverter;
import org.hglteam.conversion.util.ConversionTable;

import java.lang.reflect.Type;
import java.util.Collection;

public class ConversionTableContext implements ConversionContext {
    private final ConversionTable conversionTable = new ConversionTable();

    @Override
    public ConversionContext register(ConversionKey key, TypeConverter<?, ?> converter) {
        conversionTable.put(key.getSource(), key.getTarget(), converter);
        return this;
    }

    @Override
    public ConversionContext register(TypeConverter<?, ?> converter) {
        var conversionKey = ConversionKeyResolver.getConverterKey(converter);
        this.register(conversionKey, converter);
        return this;
    }

    @Override
    public TypeConverter<?, ?> resolve(ConversionKey key) {
        return this.conversionTable.getCompatibleConverter(key);
    }

    @Override
    public TypeConverter<?, ?> resolve(Type source, Type target) {
        return this.resolve(DefaultConvertionKey.builder()
                .source(source)
                .target(target)
                .build());
    }

    @Override
    public Collection<ConversionKey> getAvailableConversions() {
        return this.conversionTable.getAvailableConversion();
    }
}
