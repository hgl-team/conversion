package org.hglteam.convertion.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;

public interface ConversionContext {
    <TS, TD> TypeConverter<TS,TD> resolve(Class<? extends TS> sourceClass, Class<? extends TD> destinationClass);
    ConversionContext register(TypeConverter<?, ?> converter);
    Collection<ConversionPair> getAvailableConversionPairs();

    @Getter
    @NoArgsConstructor
    @SuperBuilder
    class ConversionPair {
        private Type source;
        private Type target;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConversionPair that = (ConversionPair) o;
            return source.equals(that.source) && target.equals(that.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target);
        }
    }
}
