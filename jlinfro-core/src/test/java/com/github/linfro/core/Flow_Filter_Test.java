package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.TestGetValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-08
 * @since 1.0.0
 */
public class Flow_Filter_Test {
    @Test
    public void testGetDirectFilter() throws Exception {
        TestGetValue<String> srcVal = TestGetValue.newGetValue("test");
        HasValue<String> dstValue = Flow.newHasValue();

        assertEquals("test", srcVal.getValue());
        assertNull(dstValue.getValue());

        Disposable link = srcVal.filter((s) -> !"test".equals(s)).
                mapNotNull((s) -> s + "_").flow().force().to(dstValue);

        assertEquals("test", srcVal.getValue());
        assertNull(dstValue.getValue());

        srcVal.update("ok");

        assertEquals("ok", srcVal.getValue());
        assertEquals("ok_", dstValue.getValue());

        srcVal.update("test");

        assertEquals("test", srcVal.getValue());
        assertEquals("ok_", dstValue.getValue());

        srcVal.fireValueChanged();

        assertEquals("test", srcVal.getValue());
        assertEquals("ok_", dstValue.getValue());

        srcVal.update(null);

        assertNull(srcVal.getValue());
        assertNull(dstValue.getValue());

        link.dispose();
        srcVal.update("disposed");

        assertEquals("disposed", srcVal.getValue());
        assertNull(dstValue.getValue());
    }

    @Test
    public void testHasDirectFilter() throws Exception {
        HasValue<String> srcVal = Flow.newHasValue("test");
        HasValue<String> dstValue = Flow.newHasValue();

        assertEquals("test", srcVal.getValue());
        assertNull(dstValue.getValue());

        Disposable link = srcVal.filter((s) -> !"test".equals(s)).
                mapNotNull((s) -> s + "_").flow().force().to(dstValue);

        assertEquals("test", srcVal.getValue());
        assertNull(dstValue.getValue());

        srcVal.setValue("ok");

        assertEquals("ok", srcVal.getValue());
        assertEquals("ok_", dstValue.getValue());

        srcVal.setValue("test");

        assertEquals("test", srcVal.getValue());
        assertEquals("ok_", dstValue.getValue());

        srcVal.setValue("test");

        assertEquals("test", srcVal.getValue());
        assertEquals("ok_", dstValue.getValue());

        srcVal.setValue(null);

        assertNull(srcVal.getValue());
        assertNull(dstValue.getValue());

        link.dispose();
        srcVal.setValue("disposed");

        assertEquals("disposed", srcVal.getValue());
        assertNull(dstValue.getValue());
    }

    @Test
    public void testSyncFilter() throws Exception {
        HasValue<String> srcVal = Flow.newHasValue("srcTest");
        HasValue<String> dstValue = Flow.newHasValue("ddd");

        assertEquals("srcTest", srcVal.getValue());
        assertEquals("ddd", dstValue.getValue());

        Disposable link = srcVal.filter(
                (s) -> !"srcTest".equals(s),
                (s) -> !"dstTest".equals(s)
        ).map(
                (s) -> s == null ? "" : s,
                (s) -> s == null ? "" : s
        ).flow().sync().strong().force().to(
                dstValue.filter(
                        (s) -> !"ddd".equals(s),
                        (s) -> !"ddd".equals(s)
                )
        );

        assertEquals("srcTest", srcVal.getValue());
        assertEquals("ddd", dstValue.getValue());

        srcVal.setValue("A");

        assertEquals("A", srcVal.getValue());
        assertEquals("A", dstValue.getValue());

        srcVal.setValue("ddd");

        assertEquals("ddd", srcVal.getValue());
        assertEquals("A", dstValue.getValue());

        dstValue.setValue(null);

        assertEquals("", srcVal.getValue());
        assertNull(dstValue.getValue());

        dstValue.setValue("dstTest");

        assertEquals("", srcVal.getValue());
        assertEquals("dstTest", dstValue.getValue());

        dstValue.setValue("srcTest");

        assertEquals("srcTest", srcVal.getValue());
        assertEquals("srcTest", dstValue.getValue());

        dstValue.setValue("ddd");

        assertEquals("srcTest", srcVal.getValue());
        assertEquals("ddd", dstValue.getValue());

        dstValue.setValue("ddd");

        assertEquals("srcTest", srcVal.getValue());
        assertEquals("ddd", dstValue.getValue());

        link.dispose();

        dstValue.setValue("R");

        assertEquals("srcTest", srcVal.getValue());
        assertEquals("R", dstValue.getValue());

        srcVal.setValue("M");

        assertEquals("M", srcVal.getValue());
        assertEquals("R", dstValue.getValue());

        dstValue.setValue("Y");

        assertEquals("M", srcVal.getValue());
        assertEquals("Y", dstValue.getValue());
    }

    @Test
    public void testFilteredUnsafeMappingFlow() throws Exception {
        HasValue<Integer> srcVal = Flow.newHasValue();
        HasValue<String> dstVal = Flow.newHasValue("test");

        assertNull(srcVal.getValue());
        assertEquals("test", dstVal.getValue());

        srcVal.filter((i) -> i != null).map(Object::toString).flow().force().to(dstVal);

        assertNull(srcVal.getValue());
        assertEquals("test", dstVal.getValue());

        srcVal.setValue(222);

        assertEquals(new Integer(222), srcVal.getValue());
        assertEquals("222", dstVal.getValue());

        srcVal.setValue(null);

        assertNull(srcVal.getValue());
        assertEquals("222", dstVal.getValue());
    }

    @Test
    public void testFilteredUnsafeMappingWrapper() throws Exception {
        HasValue<Integer> srcVal = Flow.newHasValue(0);
        GetValue<String> wrapVal = srcVal.filter((i) -> i != null).map(Object::toString);

        assertEquals(new Integer(0), srcVal.getValue());
        assertTrue(wrapVal.isValueValid());
        assertEquals("0", wrapVal.getValue());

        srcVal.setValue(222);

        assertEquals(new Integer(222), srcVal.getValue());
        assertTrue(wrapVal.isValueValid());
        assertEquals("222", wrapVal.getValue());

        srcVal.setValue(null);

        assertNull(srcVal.getValue());
        assertFalse(wrapVal.isValueValid());

        try {
            wrapVal.getValue();
            fail("Wrapper value must throw NullPointerException");
        } catch (NullPointerException e) {
            // Ok
        }
    }
}
