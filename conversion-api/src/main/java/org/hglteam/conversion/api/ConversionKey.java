package org.hglteam.conversion.api;

import java.lang.reflect.Type;
import java.util.Objects;

public interface ConversionKey {
    Type getSource();
    Type getTarget();

    static boolean areEqual(ConversionKey a, ConversionKey b) {
        if(Objects.isNull(a) && Objects.isNull(b)) {
            return true;
        } else if(Objects.isNull(a) || Objects.isNull(b)){
            return false;
        } else {
            return Objects.equals(a.getSource(), b.getSource())
                    && Objects.equals(a.getTarget(), b.getTarget());
        }
    }
}
