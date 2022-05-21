package org.hglteam.conversion.api.context;

import org.hglteam.conversion.api.Converter;
import org.hglteam.conversion.api.DefaultConversionKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContextualConversionBuilderTest {

    @Mock
    private Converter converter;
    private ContextualConversionBuilder<?> conversionBuilder;
    private ConversionContext context;
    private Integer source;
    private Object result;
    private DefaultConversionKey expectedConversionKey;

    @BeforeEach
    public void setupTest() {
        conversionBuilder = new ContextualConversionBuilder<>(converter, BigInteger.class);
    }

    @Test
    void sourceTypeResolvedFromConversionSource() {
        givenAConversionToLookIntoConversionContext();
        givenAnIntegerObjectSource();
        whenTheSourceGetsConverted();
        thenContextHasTheSourceType();
    }

    @Test
    void withCustomSourceType() {
        givenAConversionToLookIntoConversionContext();
        givenAnIntegerObjectSource();
        givenACustomSourceType();
        whenTheSourceGetsConverted();
        thenContextHasTheCustomSourceType();
    }

    @Test
    void withCustomTargetType() {
        givenAConversionToLookIntoConversionContext();
        givenAnIntegerObjectSource();
        givenACustomTargetType();
        whenTheSourceGetsConverted();
        thenContextHasTheTargetSourceType();
    }

    @Test
    void withArguments() {
        givenAConversionToLookIntoConversionContext();
        givenAnIntegerObjectSource();
        givenSomeArguments();
        whenTheSourceGetsConverted();
        thenContextHasExpectedArguments();
    }

    @Test
    void withConvertionKey() {
        givenAConversionToLookIntoConversionContext();
        givenAnIntegerObjectSource();
        givenACustomConvertionKey();
        whenTheSourceGetsConverted();
        thenContextHasExpectedKey();
    }

    private void givenACustomConvertionKey() {
        this.expectedConversionKey = DefaultConversionKey.builder().build();
        this.conversionBuilder.withConvertionKey(this.expectedConversionKey);
    }

    private void thenContextHasExpectedKey() {
        assertEquals(this.expectedConversionKey, this.context.getCurrentConversionKey());
    }

    private void thenContextHasExpectedArguments() {
        assertNotNull(this.context.getArguments());
        assertEquals(4, this.context.getArguments().size());
        assertEquals(1, this.context.getArguments().get("number"));
        assertEquals('C', this.context.getArguments().get("character"));
        assertEquals(23.4, this.context.getArguments().get(Double.class));
        assertEquals("23.4", this.context.getArguments().get(234));
    }

    private void givenSomeArguments() {
        this.conversionBuilder.withArgs(Map.ofEntries(
                        Map.entry("number", 1),
                        Map.entry("character", 'C')))
                .withArg(Double.class, 23.4)
                .withArg(234, "23.4");
    }

    private void givenACustomTargetType() {
        this.conversionBuilder.withTargetType(Number.class);
    }

    private void thenContextHasTheTargetSourceType() {
        assertEquals(Number.class, this.context.getCurrentConversionKey().getTarget());
    }

    private void thenContextHasTheCustomSourceType() {
        assertEquals(Number.class, this.context.getCurrentConversionKey().getSource());
    }

    private void givenACustomSourceType() {
        this.conversionBuilder.withSourceType(Number.class);
    }

    private void thenContextHasTheSourceType() {
        assertEquals(this.source.getClass(), this.context.getCurrentConversionKey().getSource());
    }

    private void givenAnIntegerObjectSource() {
        this.source = 12567;
    }

    private void whenTheSourceGetsConverted() {
        this.result = this.conversionBuilder.convert(this.source);
    }

    private void givenAConversionToLookIntoConversionContext() {
        Mockito.doAnswer(this::extractConversionContext)
                .when(converter)
                .convert(Mockito.any(), Mockito.any(ConversionContext.class));
    }

    private Object extractConversionContext(InvocationOnMock invocationOnMock) {
        this.context = invocationOnMock.getArgument(1);
        return BigInteger.ZERO;
    }
}
