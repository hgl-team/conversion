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

    @Test
    void should_return_null_when_getDescriptor() {
        givenAGenericTypeDescriptor();
        assertNull(this.descriptor.descriptor());
    }

    @Test
    void should_throwException_when_descriptorIsASuperclass() {
        assertThrows(IllegalArgumentException.class, SecondTypeDescriptorSubclass::new);
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

    private static class SecondTypeDescriptorSubclass extends TypeDescriptorSubclass {

    }
    private static class TypeDescriptorSubclass extends TypeDescriptor<Integer> {

    }
}
