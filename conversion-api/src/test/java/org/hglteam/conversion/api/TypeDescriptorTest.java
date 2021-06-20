package org.hglteam.conversion.api;

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeDescriptorTest {

    private TypeDescriptor<?> descriptor;
    private Type typeReturned;

    @Test
    void getType() {
        givenAGenericTypeDescriptor();
        whenGetGenericType();
        thenParameterizedTypeIsReturned();
    }

    private void givenAGenericTypeDescriptor() {
        this.descriptor = new TypeDescriptor<List<String>>(){ };
    }

    private void whenGetGenericType() {
        this.typeReturned = this.descriptor.getType();
    }

    private void thenParameterizedTypeIsReturned() {
        assertTrue(this.typeReturned instanceof ParameterizedType);
    }
}
