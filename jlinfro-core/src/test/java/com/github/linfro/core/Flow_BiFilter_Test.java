package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-16
 * @since 1.0.0
 */
public class Flow_BiFilter_Test {
    @Test
    public void testHybridFlowBiFilter() throws Exception {
        HasValue<String> a = Flow.newHasValue();
        HasValue<String> b = Flow.newHasValue();

        assertNull(a.getValue());
        assertNull(b.getValue());

        Disposable link = a.flow().biFilter((s) -> s.matches("\\d+")).sync().to(b);

        assertNull(a.getValue());
        assertNull(b.getValue());

        a.setValue("tst");
        assertEquals("tst", a.getValue());
        assertNull(b.getValue());

        a.setValue("5");
        assertEquals("5", a.getValue());
        assertEquals("5", b.getValue());

        b.setValue("ddd");
        assertEquals("5", a.getValue());
        assertEquals("ddd", b.getValue());

        b.setValue("77");
        assertEquals("77", a.getValue());
        assertEquals("77", b.getValue());

        link.dispose();

        a.setValue("345");
        b.setValue("224");
        assertEquals("345", a.getValue());
        assertEquals("224", b.getValue());
    }

    @Test
    public void testInOutFlowBiFilter() throws Exception {
        HasValue<String> a = Flow.newHasValue();
        HasValue<String> b = Flow.newHasValue();

        assertNull(a.getValue());
        assertNull(b.getValue());

        Disposable link = a.flow().sync().biFilter((s) -> s.matches("\\d+")).to(b);

        assertNull(a.getValue());
        assertNull(b.getValue());

        a.setValue("tst");
        assertEquals("tst", a.getValue());
        assertNull(b.getValue());

        a.setValue("5");
        assertEquals("5", a.getValue());
        assertEquals("5", b.getValue());

        b.setValue("ddd");
        assertEquals("5", a.getValue());
        assertEquals("ddd", b.getValue());

        b.setValue("77");
        assertEquals("77", a.getValue());
        assertEquals("77", b.getValue());

        link.dispose();

        a.setValue("345");
        b.setValue("224");
        assertEquals("345", a.getValue());
        assertEquals("224", b.getValue());
    }
}
