package org.hglteam.conversion.api;

import org.hglteam.conversion.api.context.ContextualConversionBuilder;
import org.hglteam.conversion.api.context.TypeConversionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ContextualConversionBuilderTest {

    @Mock
    private Converter converter;
    private ContextualConversionBuilder<?> conversionBuilder;
    private TypeConversionContext context;
    private Integer source;
    private Object result;

    @BeforeEach
    public void setupTest() {
        conversionBuilder = new ContextualConversionBuilder<>(converter, BigInteger.class);
    }

    @Test
    public void sourceTypeResolvedFromConversionSource() {
        givenAConversionToLookIntoConversionContext();
        givenAnIntegerObjectSource();
        whenTheSourceGetsConverted();
        thenContextHasTheSourceType();
    }

    @Test
    public void withCustomSourceType() {
        givenAConversionToLookIntoConversionContext();
        givenAnIntegerObjectSource();
        givenACustomSourceType();
        whenTheSourceGetsConverted();
        thenContextHasTheCustomSourceType();
    }

    @Test
    public void withCustomTargetType() {
        givenAConversionToLookIntoConversionContext();
        givenAnIntegerObjectSource();
        givenACustomTargetType();
        whenTheSourceGetsConverted();
        thenContextHasTheTargetSourceType();
    }

    @Test
    public void withArguments() {
        givenAConversionToLookIntoConversionContext();
        givenAnIntegerObjectSource();
        givenSomeArguments();
        whenTheSourceGetsConverted();
        thenContextHasExpectedArguments();
    }

    private void thenContextHasExpectedArguments() {
        assertNotNull(this.context.getArguments());
        assertEquals(4, this.context.getArguments().length);
        assertEquals(1, this.context.getArguments()[0]);
        assertEquals('C', this.context.getArguments()[1]);
        assertEquals(23.4, this.context.getArguments()[2]);
        assertEquals("23.4", this.context.getArguments()[3]);
    }

    private void givenSomeArguments() {
        this.conversionBuilder.withArgs(1, 'C', 23.4, "23.4");
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
                .convert(Mockito.any(), Mockito.any(TypeConversionContext.class));
    }

    private Object extractConversionContext(InvocationOnMock invocationOnMock) {
        this.context = invocationOnMock.getArgument(1);
        return BigInteger.ZERO;
    }
}
