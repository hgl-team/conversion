package org.hglteam.conversion.api;

import org.hglteam.conversion.api.context.ConversionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericTypeConverterTest {
    public static final int EXPECTED_SOURCE = 1;
    @Mock
    private Function<Integer, String> converterFunction;
    @Mock
    private BiFunction<ConversionContext, Integer, String> converterBifunction;
    @Mock
    private ConversionContext context;

    private GenericTypeConverter<Integer, String> converter;


    @Test
    void given_convertionFunction_when_convert_then_convertionIsPerformed() {
        converter = new GenericTypeConverter<>(converterFunction) { };

        doAnswer(invocationOnMock -> Objects.toString(invocationOnMock.getArgument(0)))
                .when(converterFunction)
                .apply(any());

        var result = converter.convert(context, EXPECTED_SOURCE);

        assertEquals(Integer.toString(EXPECTED_SOURCE), result);
        Mockito.verify(converterFunction)
                .apply(EXPECTED_SOURCE);
    }

    @Test
    void given_convertionBiFunction_when_convert_then_convertionIsPerformed() {
        converter = new GenericTypeConverter<>(converterBifunction) { };

        doAnswer(invocationOnMock -> Objects.toString(invocationOnMock.getArgument(1)))
                .when(converterBifunction)
                .apply(Mockito.eq(context), any());

        var result = converter.convert(context, EXPECTED_SOURCE);

        assertEquals(Integer.toString(EXPECTED_SOURCE), result);
        Mockito.verify(converterBifunction)
                .apply(context, EXPECTED_SOURCE);
    }

    @Test
    void should_throwException_when_IsSubclass() {
        assertThrows(IllegalArgumentException.class, () -> new SecondGenericConverter(converterFunction));
    }

    @Test
    void should_getConvertionKey() {
        converter = new GenericTypeConverter<>(converterBifunction) { };
        var convertionKey = converter.getConversionKey();
        assertEquals(convertionKey.getSource(), Integer.class);
        assertEquals(convertionKey.getTarget(), String.class);
    }

    public static class PrimaryGenericConverter extends GenericTypeConverter<Integer, String> {
        public PrimaryGenericConverter(Function<Integer, String> conversionFunction) {
            super(conversionFunction);
        }
    }

    public static class SecondGenericConverter extends PrimaryGenericConverter {
        public SecondGenericConverter(Function<Integer, String> conversionFunction) {
            super(conversionFunction);
        }
    }

}