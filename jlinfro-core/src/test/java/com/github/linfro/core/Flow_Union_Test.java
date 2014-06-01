package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.ObjectUtil;
import com.github.linfro.core.value.TestGetValue;
import com.github.linfro.core.value.TestListener;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

import static com.github.linfro.core.value.TestUtil.assertDisposed;
import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-16
 * @since 1.0.0
 */
public class Flow_Union_Test {
    @SafeVarargs
    private static <T> void assertList(List<T> list, T... values) {
        assertNotNull(list);
        assertNotNull(values);
        assertEquals(list.size(), values.length);
        for (int i = 0; i < values.length; i++) {
            assertEquals("arg" + i, values[i], list.get(i));
        }
    }

    @Test
    public void testGetFlowUnion() throws Exception {
        TestGetValue<Integer> a = TestGetValue.newGetValue();
        TestGetValue<Integer> b = TestGetValue.newGetValue();
        TestGetValue<Integer> c = TestGetValue.newGetValue();

        HasValue<Integer> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = a.union(b, c).map(
                args -> {
                    return (Integer) args.stream().mapToInt(ObjectUtil::nvl).sum();
                }
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

    @Test
    public void testUnionMetaInfo() throws Exception {
        HasValue<String> arg0 = Values.<String>newHasValue().named("arg0");
        HasValue<String> arg1 = Values.<String>newHasValue().named("arg1");
        HasValue<String> arg2 = Values.<String>newHasValue().named("arg2");

        GetValue<List<String>> union = arg0.union(arg1, arg2);

        assertNull(arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertList(union.getValue(), null, null, null);
        assertEquals("arg0", union.getMetaName());

        arg1.setValue("d");

        assertNull(arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertNull(arg2.getValue());
        assertList(union.getValue(), null, "d", null);
        assertEquals("arg1", union.getMetaName());

        arg2.setValue("d");

        assertNull(arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertEquals("d", arg2.getValue());
        assertList(union.getValue(), null, "d", "d");
        assertEquals("arg2", union.getMetaName());

        arg0.setValue("d");

        assertEquals("d", arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertEquals("d", arg2.getValue());
        assertList(union.getValue(), "d", "d", "d");
        assertEquals("arg0", union.getMetaName());

        //****** dispose **************************

        assertFalse(arg0.canDispose());
        assertFalse(arg1.canDispose());
        assertFalse(arg2.canDispose());
        assertTrue(union.canDispose());

        union.dispose();

        assertDisposed(union::getValue);
        assertDisposed(union::getMetaName);
        assertDisposed(union::isValueValid);

        assertDisposed(arg0::getValue);
        assertDisposed(arg0::getMetaName);
        assertDisposed(arg0::isValueValid);

        assertDisposed(arg1::getValue);
        assertDisposed(arg1::getMetaName);
        assertDisposed(arg1::isValueValid);

        assertDisposed(arg2::getValue);
        assertDisposed(arg2::getMetaName);
        assertDisposed(arg2::isValueValid);
    }

    @Test
    public void testUnionIsValid() throws Exception {
        HasValue<String> arg0 = Values.newHasValue("arg0");
        HasValue<String> arg1 = Values.newHasValue("arg1");
        HasValue<String> arg2 = Values.newHasValue("arg2");

        GetValue<List<String>> union = arg0.filter(Objects::nonNull).union(
                arg1.filter(Objects::nonNull),
                arg2.filter(Objects::nonNull)
        );

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertTrue(union.isValueValid());
        assertList(union.getValue(), "arg0", "arg1", "arg2");

        arg0.setValue(null);

        assertNull(arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertFalse(union.isValueValid());

        arg0.setValue("arg0");
        arg1.setValue(null);

        assertEquals("arg0", arg0.getValue());
        assertNull(arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertFalse(union.isValueValid());

        arg1.setValue("arg1");
        arg2.setValue(null);

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertNull(arg2.getValue());
        assertFalse(union.isValueValid());

        arg0.setValue(null);
        arg1.setValue(null);

        assertNull(arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertFalse(union.isValueValid());

        arg0.setValue("a0");
        arg1.setValue("a1");
        arg2.setValue("a2");

        assertEquals("a0", arg0.getValue());
        assertEquals("a1", arg1.getValue());
        assertEquals("a2", arg2.getValue());
        assertTrue(union.isValueValid());
        assertList(union.getValue(), "a0", "a1", "a2");
        assertTrue(union.canDispose());

        union.dispose();

        assertEquals("a0", arg0.getValue());
        assertEquals("a1", arg1.getValue());
        assertEquals("a2", arg2.getValue());

        assertDisposed(union::getValue);
        assertDisposed(union::getMetaName);
        assertDisposed(union::isValueValid);
    }

    @Test
    public void testUnion1() throws Exception {
        HasValue<String> arg0 = Values.newHasValue();
        HasValue<String> arg1 = Values.newHasValue();
        HasValue<List<String>> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = arg0.union(arg1).flow().force().to(res);

        assertNull(arg0.getValue());
        assertNull(arg1.getValue());
        assertEquals(1, listener.getCounter());
        assertList(res.getValue(), null, null);

        arg0.setValue("arg0");

        assertEquals("arg0", arg0.getValue());
        assertNull(arg1.getValue());
        assertEquals(2, listener.getCounter());
        assertList(res.getValue(), "arg0", null);

        arg1.setValue("arg1");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals(3, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1");

        link.dispose();

        arg0.setValue("d");
        arg1.setValue("d");

        assertEquals("d", arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertEquals(3, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1");
    }

    @Test
    public void testUnion2() throws Exception {
        HasValue<String> arg0 = Values.newHasValue();
        HasValue<String> arg1 = Values.newHasValue();
        HasValue<String> arg2 = Values.newHasValue();
        HasValue<List<String>> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = arg0.union(arg1, arg2).flow().to(res);

        assertNull(arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertEquals(0, listener.getCounter());
        assertNull(res.getValue());

        arg0.setValue("arg0");

        assertEquals("arg0", arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertEquals(1, listener.getCounter());
        assertList(res.getValue(), "arg0", null, null);

        arg1.setValue("arg1");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertNull(arg2.getValue());
        assertEquals(2, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", null);

        arg2.setValue("arg2");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals(3, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2");

        link.dispose();

        arg0.setValue("d");
        arg1.setValue("d");
        arg2.setValue("d");

        assertEquals("d", arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertEquals("d", arg2.getValue());
        assertEquals(3, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2");
    }

    @Test
    public void testUnion3() throws Exception {
        HasValue<String> arg0 = Values.newHasValue();
        HasValue<String> arg1 = Values.newHasValue();
        HasValue<String> arg2 = Values.newHasValue();
        HasValue<String> arg3 = Values.newHasValue();
        HasValue<List<String>> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = arg0.union(arg1, arg2, arg3).flow().to(res);

        assertNull(arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertEquals(0, listener.getCounter());
        assertNull(res.getValue());

        arg0.setValue("arg0");

        assertEquals("arg0", arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertEquals(1, listener.getCounter());
        assertList(res.getValue(), "arg0", null, null, null);

        arg1.setValue("arg1");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertEquals(2, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", null, null);

        arg2.setValue("arg2");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertNull(arg3.getValue());
        assertEquals(3, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", null);

        arg3.setValue("arg3");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals(4, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3");

        link.dispose();

        arg0.setValue("d");
        arg1.setValue("d");
        arg2.setValue("d");
        arg3.setValue("d");

        assertEquals("d", arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertEquals("d", arg2.getValue());
        assertEquals("d", arg3.getValue());
        assertEquals(4, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3");
    }

    @Test
    public void testUnion4() throws Exception {
        HasValue<String> arg0 = Values.newHasValue();
        HasValue<String> arg1 = Values.newHasValue();
        HasValue<String> arg2 = Values.newHasValue();
        HasValue<String> arg3 = Values.newHasValue();
        HasValue<String> arg4 = Values.newHasValue();
        HasValue<List<String>> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = arg0.union(arg1, arg2, arg3, arg4).flow().to(res);

        assertNull(arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertEquals(0, listener.getCounter());
        assertNull(res.getValue());

        arg0.setValue("arg0");

        assertEquals("arg0", arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertEquals(1, listener.getCounter());
        assertList(res.getValue(), "arg0", null, null, null, null);

        arg1.setValue("arg1");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertEquals(2, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", null, null, null);

        arg2.setValue("arg2");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertEquals(3, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", null, null);

        arg3.setValue("arg3");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertNull(arg4.getValue());
        assertEquals(4, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", null);

        arg4.setValue("arg4");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals(5, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4");

        link.dispose();

        arg0.setValue("d");
        arg1.setValue("d");
        arg2.setValue("d");
        arg3.setValue("d");
        arg4.setValue("d");

        assertEquals("d", arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertEquals("d", arg2.getValue());
        assertEquals("d", arg3.getValue());
        assertEquals("d", arg4.getValue());
        assertEquals(5, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4");
    }

    @Test
    public void testUnion5() throws Exception {
        HasValue<String> arg0 = Values.newHasValue();
        HasValue<String> arg1 = Values.newHasValue();
        HasValue<String> arg2 = Values.newHasValue();
        HasValue<String> arg3 = Values.newHasValue();
        HasValue<String> arg4 = Values.newHasValue();
        HasValue<String> arg5 = Values.newHasValue();
        HasValue<List<String>> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = arg0.union(arg1, arg2, arg3, arg4, arg5).flow().to(res);

        assertNull(arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertEquals(0, listener.getCounter());
        assertNull(res.getValue());

        arg0.setValue("arg0");

        assertEquals("arg0", arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertEquals(1, listener.getCounter());
        assertList(res.getValue(), "arg0", null, null, null, null, null);

        arg1.setValue("arg1");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertEquals(2, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", null, null, null, null);

        arg2.setValue("arg2");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertEquals(3, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", null, null, null);

        arg3.setValue("arg3");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertEquals(4, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", null, null);

        arg4.setValue("arg4");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertNull(arg5.getValue());
        assertEquals(5, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", null);

        arg5.setValue("arg5");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals("arg5", arg5.getValue());
        assertEquals(6, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5");

        link.dispose();

        arg0.setValue("d");
        arg1.setValue("d");
        arg2.setValue("d");
        arg3.setValue("d");
        arg4.setValue("d");
        arg5.setValue("d");

        assertEquals("d", arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertEquals("d", arg2.getValue());
        assertEquals("d", arg3.getValue());
        assertEquals("d", arg4.getValue());
        assertEquals("d", arg5.getValue());
        assertEquals(6, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5");
    }

    @Test
    public void testUnion6() throws Exception {
        HasValue<String> arg0 = Values.newHasValue();
        HasValue<String> arg1 = Values.newHasValue();
        HasValue<String> arg2 = Values.newHasValue();
        HasValue<String> arg3 = Values.newHasValue();
        HasValue<String> arg4 = Values.newHasValue();
        HasValue<String> arg5 = Values.newHasValue();
        HasValue<String> arg6 = Values.newHasValue();
        HasValue<List<String>> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = arg0.union(arg1, arg2, arg3, arg4, arg5, arg6).flow().to(res);

        assertNull(arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertEquals(0, listener.getCounter());
        assertNull(res.getValue());

        arg0.setValue("arg0");

        assertEquals("arg0", arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertEquals(1, listener.getCounter());
        assertList(res.getValue(), "arg0", null, null, null, null, null, null);

        arg1.setValue("arg1");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertEquals(2, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", null, null, null, null, null);

        arg2.setValue("arg2");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertEquals(3, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", null, null, null, null);

        arg3.setValue("arg3");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertEquals(4, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", null, null, null);

        arg4.setValue("arg4");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertEquals(5, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", null, null);

        arg5.setValue("arg5");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals("arg5", arg5.getValue());
        assertNull(arg6.getValue());
        assertEquals(6, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", null);

        arg6.setValue("arg6");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals("arg5", arg5.getValue());
        assertEquals("arg6", arg6.getValue());
        assertEquals(7, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6");

        link.dispose();

        arg0.setValue("d");
        arg1.setValue("d");
        arg2.setValue("d");
        arg3.setValue("d");
        arg4.setValue("d");
        arg5.setValue("d");
        arg6.setValue("d");

        assertEquals("d", arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertEquals("d", arg2.getValue());
        assertEquals("d", arg3.getValue());
        assertEquals("d", arg4.getValue());
        assertEquals("d", arg5.getValue());
        assertEquals("d", arg6.getValue());
        assertEquals(7, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6");
    }

    @Test
    public void testUnion7() throws Exception {
        HasValue<String> arg0 = Values.newHasValue();
        HasValue<String> arg1 = Values.newHasValue();
        HasValue<String> arg2 = Values.newHasValue();
        HasValue<String> arg3 = Values.newHasValue();
        HasValue<String> arg4 = Values.newHasValue();
        HasValue<String> arg5 = Values.newHasValue();
        HasValue<String> arg6 = Values.newHasValue();
        HasValue<String> arg7 = Values.newHasValue();
        HasValue<List<String>> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = arg0.union(arg1, arg2, arg3, arg4, arg5, arg6, arg7).flow().to(res);

        assertNull(arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertEquals(0, listener.getCounter());
        assertNull(res.getValue());

        arg0.setValue("arg0");

        assertEquals("arg0", arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertEquals(1, listener.getCounter());
        assertList(res.getValue(), "arg0", null, null, null, null, null, null, null);

        arg1.setValue("arg1");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertEquals(2, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", null, null, null, null, null, null);

        arg2.setValue("arg2");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertEquals(3, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", null, null, null, null, null);

        arg3.setValue("arg3");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertEquals(4, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", null, null, null, null);

        arg4.setValue("arg4");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertEquals(5, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", null, null, null);

        arg5.setValue("arg5");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals("arg5", arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertEquals(6, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", null, null);

        arg6.setValue("arg6");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals("arg5", arg5.getValue());
        assertEquals("arg6", arg6.getValue());
        assertNull(arg7.getValue());
        assertEquals(7, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", null);

        arg7.setValue("arg7");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals("arg5", arg5.getValue());
        assertEquals("arg6", arg6.getValue());
        assertEquals("arg7", arg7.getValue());
        assertEquals(8, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7");

        link.dispose();

        arg0.setValue("d");
        arg1.setValue("d");
        arg2.setValue("d");
        arg3.setValue("d");
        arg4.setValue("d");
        arg5.setValue("d");
        arg6.setValue("d");
        arg7.setValue("d");

        assertEquals("d", arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertEquals("d", arg2.getValue());
        assertEquals("d", arg3.getValue());
        assertEquals("d", arg4.getValue());
        assertEquals("d", arg5.getValue());
        assertEquals("d", arg6.getValue());
        assertEquals("d", arg7.getValue());
        assertEquals(8, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7");
    }

    @Test
    public void testUnion8() throws Exception {
        HasValue<String> arg0 = Values.newHasValue();
        HasValue<String> arg1 = Values.newHasValue();
        HasValue<String> arg2 = Values.newHasValue();
        HasValue<String> arg3 = Values.newHasValue();
        HasValue<String> arg4 = Values.newHasValue();
        HasValue<String> arg5 = Values.newHasValue();
        HasValue<String> arg6 = Values.newHasValue();
        HasValue<String> arg7 = Values.newHasValue();
        HasValue<String> arg8 = Values.newHasValue();
        HasValue<List<String>> res = Values.newHasValue();

        TestListener listener = new TestListener();
        res.addChangeListener(listener);

        Disposable link = arg0.union(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8).flow().to(res);

        assertNull(arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertNull(arg8.getValue());
        assertEquals(0, listener.getCounter());
        assertNull(res.getValue());

        arg0.setValue("arg0");

        assertEquals("arg0", arg0.getValue());
        assertNull(arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertNull(arg8.getValue());
        assertEquals(1, listener.getCounter());
        assertList(res.getValue(), "arg0", null, null, null, null, null, null, null, null);

        arg1.setValue("arg1");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertNull(arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertNull(arg8.getValue());
        assertEquals(2, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", null, null, null, null, null, null, null);

        arg2.setValue("arg2");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertNull(arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertNull(arg8.getValue());
        assertEquals(3, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", null, null, null, null, null, null);

        arg3.setValue("arg3");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertNull(arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertNull(arg8.getValue());
        assertEquals(4, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", null, null, null, null, null);

        arg4.setValue("arg4");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertNull(arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertNull(arg8.getValue());
        assertEquals(5, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", null, null, null, null);

        arg5.setValue("arg5");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals("arg5", arg5.getValue());
        assertNull(arg6.getValue());
        assertNull(arg7.getValue());
        assertNull(arg8.getValue());
        assertEquals(6, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", null, null, null);

        arg6.setValue("arg6");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals("arg5", arg5.getValue());
        assertEquals("arg6", arg6.getValue());
        assertNull(arg7.getValue());
        assertNull(arg8.getValue());
        assertEquals(7, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", null, null);

        arg7.setValue("arg7");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals("arg5", arg5.getValue());
        assertEquals("arg6", arg6.getValue());
        assertEquals("arg7", arg7.getValue());
        assertNull(arg8.getValue());
        assertEquals(8, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7", null);

        arg8.setValue("arg8");

        assertEquals("arg0", arg0.getValue());
        assertEquals("arg1", arg1.getValue());
        assertEquals("arg2", arg2.getValue());
        assertEquals("arg3", arg3.getValue());
        assertEquals("arg4", arg4.getValue());
        assertEquals("arg5", arg5.getValue());
        assertEquals("arg6", arg6.getValue());
        assertEquals("arg7", arg7.getValue());
        assertEquals("arg8", arg8.getValue());
        assertEquals(9, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7", "arg8");

        link.dispose();

        arg0.setValue("d");
        arg1.setValue("d");
        arg2.setValue("d");
        arg3.setValue("d");
        arg4.setValue("d");
        arg5.setValue("d");
        arg6.setValue("d");
        arg7.setValue("d");
        arg8.setValue("d");

        assertEquals("d", arg0.getValue());
        assertEquals("d", arg1.getValue());
        assertEquals("d", arg2.getValue());
        assertEquals("d", arg3.getValue());
        assertEquals("d", arg4.getValue());
        assertEquals("d", arg5.getValue());
        assertEquals("d", arg6.getValue());
        assertEquals("d", arg7.getValue());
        assertEquals("d", arg8.getValue());
        assertEquals(9, listener.getCounter());
        assertList(res.getValue(), "arg0", "arg1", "arg2", "arg3", "arg4", "arg5", "arg6", "arg7", "arg8");
    }
}
