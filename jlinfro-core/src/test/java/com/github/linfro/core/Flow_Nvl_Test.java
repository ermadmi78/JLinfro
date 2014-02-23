package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.TestGetValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-09
 * @since 1.0.0
 */
public class Flow_Nvl_Test {
    @Test
    public void testOneWayGetFlowNvl() throws Exception {
        TestGetValue<Integer> a = TestGetValue.newGetValue();
        HasValue<Integer> b = Flow.newHasValue();

        assertNull(a.getValue());
        assertNull(b.getValue());

        Disposable link = a.nvl(0).flow().force().to(b);

        assertNull(a.getValue());
        assertEquals(new Integer(0), b.getValue());

        a.update(20);

        assertEquals(new Integer(20), a.getValue());
        assertEquals(new Integer(20), b.getValue());

        a.update(null);

        assertNull(a.getValue());
        assertEquals(new Integer(0), b.getValue());

        link.dispose();
        a.update(44);

        assertEquals(new Integer(44), a.getValue());
        assertEquals(new Integer(0), b.getValue());
    }

    @Test
    public void testOneWayHasFlowNvl() throws Exception {
        HasValue<Integer> a = Flow.newHasValue();
        HasValue<Integer> b = Flow.newHasValue();

        assertNull(a.getValue());
        assertNull(b.getValue());

        Disposable link = a.nvl(0).flow().force().to(b);

        assertNull(a.getValue());
        assertEquals(new Integer(0), b.getValue());

        a.setValue(20);

        assertEquals(new Integer(20), a.getValue());
        assertEquals(new Integer(20), b.getValue());

        a.setValue(null);

        assertNull(a.getValue());
        assertEquals(new Integer(0), b.getValue());

        link.dispose();
        a.setValue(44);

        assertEquals(new Integer(44), a.getValue());
        assertEquals(new Integer(0), b.getValue());
    }

    @Test
    public void testBothWayHasFlowNvl() throws Exception {
        HasValue<Integer> a = Flow.newHasValue();
        HasValue<Integer> b = Flow.newHasValue();

        assertNull(a.getValue());
        assertNull(b.getValue());

        Disposable link = a.nvl(-1, -2).flow().sync().force().to(b);

        assertNull(a.getValue());
        assertEquals(new Integer(-1), b.getValue());

        a.setValue(20);

        assertEquals(new Integer(20), a.getValue());
        assertEquals(new Integer(20), b.getValue());

        a.setValue(null);

        assertNull(a.getValue());
        assertEquals(new Integer(-1), b.getValue());

        b.setValue(null);

        assertEquals(new Integer(-2), a.getValue());
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
