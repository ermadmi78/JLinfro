package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.ObjectUtil;
import com.github.linfro.core.value.TestGetValue;
import com.github.linfro.core.value.TestListener;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-16
 * @since 1.0.0
 */
public class Flow_Union_Test {
    @Test
    public void testGetFlowUnion() throws Exception {
        TestGetValue<Integer> a = TestGetValue.newGetValue();
        TestGetValue<Integer> b = TestGetValue.newGetValue();
        TestGetValue<Integer> c = TestGetValue.newGetValue();

        HasValue<Integer> res = Flow.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = a.union(b, c).map(
                (args) -> args.stream().mapToInt(ObjectUtil::nvl).sum()
        ).flow().to(res);

        assertNull(res.getValue());
        assertEquals(0, listener.getCounter());

        a.update(1);
        assertEquals(new Integer(1), res.getValue());
        assertEquals(1, listener.getCounter());

        b.update(2);
        assertEquals(new Integer(3), res.getValue());
        assertEquals(2, listener.getCounter());

        c.update(3);
        assertEquals(new Integer(6), res.getValue());
        assertEquals(3, listener.getCounter());

        link.dispose();

        a.update(10);
        assertEquals(new Integer(6), res.getValue());
        assertEquals(3, listener.getCounter());

        b.update(20);
        assertEquals(new Integer(6), res.getValue());
        assertEquals(3, listener.getCounter());

        c.update(30);
        assertEquals(new Integer(6), res.getValue());
        assertEquals(3, listener.getCounter());
    }
}
