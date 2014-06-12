package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.TestListener;
import org.junit.Test;

import static com.github.linfro.core.common.ObjectUtil.nvl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Dmitry Ermakov
 * @version 2014-06-12
 * @since 1.0.0
 */
public class Flow_Merge_Test {
    @Test
    public void testExampleMerge() throws Exception {
        HasValue<String> a = Values.newHasValue();
        HasValue<Integer> b = Values.newHasValue();
        HasValue<Long> c = Values.newHasValue();

        HasValue<String> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = a.named("a").merge(b.named("b"), c.named("c")).map(map -> {
            String aVal = nvl((String) map.get("a"), "null");
            Integer bVal = nvl((Integer) map.get("b"));
            Long cVal = nvl((Long) map.get("c"));

            return aVal + "_" + bVal + "_" + cVal;
        }).flow().force().to(res);

        assertEquals("null_0_0", res.getValue());
        assertEquals(1, listener.getCounter());

        a.setValue("str");
        assertEquals("str_0_0", res.getValue());
        assertEquals(2, listener.getCounter());

        b.setValue(12);
        assertEquals("str_12_0", res.getValue());
        assertEquals(3, listener.getCounter());

        c.setValue(356L);
        assertEquals("str_12_356", res.getValue());
        assertEquals(4, listener.getCounter());

        link.dispose();

        a.setValue("disposed");
        assertEquals("str_12_356", res.getValue());
        assertEquals(4, listener.getCounter());

        b.setValue(-978);
        assertEquals("str_12_356", res.getValue());
        assertEquals(4, listener.getCounter());

        c.setValue(3L);
        assertEquals("str_12_356", res.getValue());
        assertEquals(4, listener.getCounter());
    }

    @Test
    public void testDuplicate1() throws Exception {
        HasValue<String> a = Values.newHasValue();
        HasValue<String> b = Values.newHasValue();

        try {
            a.named("test").merge(b.named("test"));
            fail("Merge must throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Argument key duplicate found: test", e.getMessage());
        }
    }

    @Test
    public void testDuplicate2() throws Exception {
        HasValue<String> a = Values.newHasValue();
        HasValue<String> b = Values.newHasValue();
        HasValue<String> c = Values.newHasValue();

        try {
            a.merge(b.named("test"), c.named("test"));
            fail("Merge must throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Argument key duplicate found: test", e.getMessage());
        }
    }

    @Test
    public void testDuplicate3() throws Exception {
        HasValue<String> a = Values.newHasValue();
        HasValue<String> b = Values.newHasValue();
        HasValue<String> c = Values.newHasValue();
        HasValue<String> d = Values.newHasValue();

        try {
            a.merge(b.named("test"), c, d.named("test"));
            fail("Merge must throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Argument key duplicate found: test", e.getMessage());
        }
    }
}
