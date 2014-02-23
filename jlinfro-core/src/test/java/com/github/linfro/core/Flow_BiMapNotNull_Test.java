package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-09
 * @since 1.0.0
 */
public class Flow_BiMapNotNull_Test {
    @Test
    public void testHybridFlowBiMapNotNull() throws Exception {
        HasValue<Boolean> a = Flow.newHasValue();
        HasValue<Boolean> b = Flow.newHasValue();

        assertNull(a.getValue());
        assertNull(b.getValue());

        Disposable link = a.flow().biMapNotNull((val) -> !val).sync().force().to(b);

        assertNull(a.getValue());
        assertNull(b.getValue());

        a.setValue(true);

        assertTrue(a.getValue());
        assertFalse(b.getValue());

        b.setValue(true);

        assertFalse(a.getValue());
        assertTrue(b.getValue());

        a.setValue(null);

        assertNull(a.getValue());
        assertNull(b.getValue());

        a.setValue(false);

        assertFalse(a.getValue());
        assertTrue(b.getValue());

        b.setValue(null);

        assertNull(a.getValue());
        assertNull(b.getValue());

        link.dispose();
        a.setValue(false);
        b.setValue(false);

        assertFalse(a.getValue());
        assertFalse(b.getValue());
    }

    @Test
    public void testInOutFlowBiMapNotNull() throws Exception {
        HasValue<Boolean> a = Flow.newHasValue();
        HasValue<Boolean> b = Flow.newHasValue();

        assertNull(a.getValue());
        assertNull(b.getValue());

        Disposable link = a.flow().sync().biMapNotNull((val) -> !val).force().to(b);

        assertNull(a.getValue());
        assertNull(b.getValue());

        a.setValue(true);

        assertTrue(a.getValue());
        assertFalse(b.getValue());

        b.setValue(true);

        assertFalse(a.getValue());
        assertTrue(b.getValue());

        a.setValue(null);

        assertNull(a.getValue());
        assertNull(b.getValue());

        a.setValue(false);

        assertFalse(a.getValue());
        assertTrue(b.getValue());

        b.setValue(null);

        assertNull(a.getValue());
        assertNull(b.getValue());

        link.dispose();
        a.setValue(false);
        b.setValue(false);

        assertFalse(a.getValue());
        assertFalse(b.getValue());
    }
}
