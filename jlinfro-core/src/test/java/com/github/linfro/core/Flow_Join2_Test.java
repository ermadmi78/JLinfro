package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.TestListener;
import org.junit.Test;

import static com.github.linfro.core.common.ObjectUtil.nvl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-06-12
 * @since 1.0.0
 */
public class Flow_Join2_Test {
    @Test
    public void testJoin2Example() throws Exception {
        HasValue<String> a = Values.newHasValue();
        HasValue<Integer> b = Values.newHasValue();
        HasValue<String> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = a.join(b,
                (_a, _b) -> nvl(_a, "null") + "_" + nvl(_b)
        ).flow().force().to(res);

        assertNull(a.getValue());
        assertNull(b.getValue());
        assertEquals("null_0", res.getValue());
        assertEquals(1, listener.getCounter());

        a.setValue("test");

        assertEquals("test", a.getValue());
        assertNull(b.getValue());
        assertEquals("test_0", res.getValue());
        assertEquals(2, listener.getCounter());

        b.setValue(23);

        assertEquals("test", a.getValue());
        assertEquals(new Integer(23), b.getValue());
        assertEquals("test_23", res.getValue());
        assertEquals(3, listener.getCounter());

        link.dispose();

        a.setValue("disposed");

        assertEquals("disposed", a.getValue());
        assertEquals(new Integer(23), b.getValue());
        assertEquals("test_23", res.getValue());
        assertEquals(3, listener.getCounter());

        b.setValue(-1);

        assertEquals("disposed", a.getValue());
        assertEquals(new Integer(-1), b.getValue());
        assertEquals("test_23", res.getValue());
        assertEquals(3, listener.getCounter());
    }
}
