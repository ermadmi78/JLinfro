package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-09
 * @since 1.0.0
 */
public class Flow_BiNvl_Test {
    @Test
    public void testBiNvl() throws Exception {
        HasValue<Integer> a = Flow.newHasValue();
        HasValue<Integer> b = Flow.newHasValue();

        assertNull(a.getValue());
        assertNull(b.getValue());

        Disposable link = a.biNvl(0).flow().sync().force().to(b);

        assertNull(a.getValue());
        assertEquals(new Integer(0), b.getValue());

        a.setValue(20);

        assertEquals(new Integer(20), a.getValue());
        assertEquals(new Integer(20), b.getValue());

        a.setValue(null);

        assertNull(a.getValue());
        assertEquals(new Integer(0), b.getValue());

        b.setValue(null);

        assertEquals(new Integer(0), a.getValue());
        assertNull(b.getValue());

        b.setValue(33);

        assertEquals(new Integer(33), a.getValue());
        assertEquals(new Integer(33), b.getValue());

        link.dispose();
        a.setValue(111);
        b.setValue(222);

        assertEquals(new Integer(111), a.getValue());
        assertEquals(new Integer(222), b.getValue());
    }
}
