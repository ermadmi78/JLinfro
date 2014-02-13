package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.HasValue;
import com.github.linfro.core.value.TestGetValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-08
 * @since 1.0.0
 */
public class Flow_Filter_Test {
    @Test
    public void testOneWayDirectFilter() throws Exception {
        TestGetValue<String> srcVal = TestGetValue.newGetValue("test");
        HasValue<String> dstValue = Flow.newHasValue();

        assertEquals("test", srcVal.getValue());
        assertNull(dstValue.getValue());

        Disposable link = srcVal.flow().filter((s) -> !"test".equals(s)).
                mapNotNull((s) -> s + "_").force().to(dstValue);

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
    public void testHybridDirectFilter() throws Exception {
        HasValue<String> srcVal = Flow.newHasValue("test");
        HasValue<String> dstValue = Flow.newHasValue();

        assertEquals("test", srcVal.getValue());
        assertNull(dstValue.getValue());

        Disposable link = srcVal.flow().filter((s) -> !"test".equals(s)).
                mapNotNull((s) -> s + "_").force().to(dstValue);

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
    public void testHybridSyncFilter() throws Exception {
        HasValue<String> srcVal = Flow.newHasValue("srcTest");
        HasValue<String> dstValue = Flow.newHasValue("ddd");

        assertEquals("srcTest", srcVal.getValue());
        assertEquals("ddd", dstValue.getValue());

        Disposable link = srcVal.flow().filter(
                (s) -> !"srcTest".equals(s),
                (s) -> !"dstTest".equals(s)
        ).map(
                (s) -> s == null ? "" : s,
                (s) -> s == null ? "" : s
        ).sync().strong().force().to(
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
    public void testBothWaySyncFilter() throws Exception {
        HasValue<String> srcVal = Flow.newHasValue("srcTest");
        HasValue<String> dstValue = Flow.newHasValue("ddd");

        assertEquals("srcTest", srcVal.getValue());
        assertEquals("ddd", dstValue.getValue());

        Disposable link = srcVal.flow().sync().filter(
                (s) -> !"srcTest".equals(s),
                (s) -> !"dstTest".equals(s)
        ).map(
                (s) -> s == null ? "" : s,
                (s) -> s == null ? "" : s
        ).strong().force().to(
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
}
