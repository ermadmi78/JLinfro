package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.HasAggregateValue;
import com.github.linfro.core.value.HasValue;
import org.junit.Test;

import static com.github.linfro.core.ValueDSL.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public class ValueDsl_AggregationSyntax_Test {
    @Test
    public void testValidAggregateBindingAnd() {
        HasValue<Boolean> a = newHasValue();
        HasValue<Boolean> b = newHasValue();
        HasAggregateValue<Boolean> aggregate = andValue();

        assertNull(a.getValue());
        assertNull(b.getValue());
        assertEquals(Boolean.FALSE, aggregate.getValue());

        Disposable linkA = linkFrom(a).to(aggregate);
        Disposable linkB = linkFrom(b).to(aggregate);

        a.setValue(true);
        assertEquals(Boolean.FALSE, aggregate.getValue());

        b.setValue(true);
        assertEquals(Boolean.TRUE, aggregate.getValue());

        a.setValue(false);
        assertEquals(Boolean.FALSE, aggregate.getValue());

        linkA.dispose();
        assertEquals(Boolean.TRUE, aggregate.getValue());

        linkB.dispose();
        assertEquals(Boolean.FALSE, aggregate.getValue());
    }

    @Test
    public void testValidAggregateBindingOr() {
        HasValue<Boolean> a = newHasValue();
        HasValue<Boolean> b = newHasValue();
        HasAggregateValue<Boolean> aggregate = orValue();

        assertNull(a.getValue());
        assertNull(b.getValue());
        assertEquals(Boolean.FALSE, aggregate.getValue());

        Disposable linkA = linkFrom(a).to(aggregate);
        Disposable linkB = linkFrom(b).to(aggregate);

        a.setValue(true);
        assertEquals(Boolean.TRUE, aggregate.getValue());

        a.setValue(false);
        assertEquals(Boolean.FALSE, aggregate.getValue());

        b.setValue(true);
        assertEquals(Boolean.TRUE, aggregate.getValue());

        b.setValue(false);
        assertEquals(Boolean.FALSE, aggregate.getValue());

        //***********************************

        a.setValue(true);
        assertEquals(Boolean.TRUE, aggregate.getValue());
        linkA.dispose();
        assertEquals(Boolean.FALSE, aggregate.getValue());

        b.setValue(true);
        assertEquals(Boolean.TRUE, aggregate.getValue());
        linkB.dispose();
        assertEquals(Boolean.FALSE, aggregate.getValue());
    }

    @Test
    public void notFunctionTest() {
        HasValue<Boolean> a = newHasValue();
        HasValue<Boolean> b = newHasValue();

        linkFrom(a).transform(not()).sync().to(b);

        a.setValue(false);
        assertEquals(Boolean.TRUE, b.getValue());

        a.setValue(true);
        assertEquals(Boolean.FALSE, b.getValue());

        a.setValue(null);
        assertEquals(Boolean.TRUE, b.getValue());

        b.setValue(false);
        assertEquals(Boolean.TRUE, a.getValue());

        b.setValue(true);
        assertEquals(Boolean.FALSE, a.getValue());

        b.setValue(null);
        assertEquals(Boolean.TRUE, a.getValue());
    }
}
