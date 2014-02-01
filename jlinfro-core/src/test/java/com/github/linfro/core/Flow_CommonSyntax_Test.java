package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.Getter;
import com.github.linfro.core.value.HasValue;
import com.github.linfro.core.value.TestGetValue;
import com.github.linfro.core.value.ValueChangeListener;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public class Flow_CommonSyntax_Test {
    @Test
    public void testNewHasValue() {
        HasValue<String> strValue = Flow.newHasValue();
        assertNotNull(strValue);
        assertNull(strValue.getValue());

        HasValue<Integer> intValue = Flow.newHasValue(500);
        assertNotNull(intValue);
        assertEquals(new Integer(500), intValue.getValue());
    }

    @Test
    public void testOneWayLink() {
        TestGetValue<String> a = TestGetValue.newGetValue("a");
        HasValue<String> b = Flow.newHasValue("b");

        Disposable link = Flow.from(a).to(b);
        assertNotNull(link);
        assertEquals("a", a.getValue());
        assertEquals("b", b.getValue());

        a.update("new a");
        assertEquals("new a", a.getValue());
        assertEquals("new a", b.getValue());

        b.setValue("new b");
        assertEquals("new a", a.getValue());
        assertEquals("new b", b.getValue());

        link.dispose();
        a.update("test");
        assertEquals("test", a.getValue());
        assertEquals("new b", b.getValue());
    }

    @Test
    public void testBothWayLink() {
        HasValue<String> a = Flow.newHasValue("a");
        HasValue<String> b = Flow.newHasValue("b");

        Disposable link = Flow.from(a).to(b);
        assertNotNull(link);
        assertEquals("a", a.getValue());
        assertEquals("b", b.getValue());

        a.setValue("new a");
        assertEquals("new a", a.getValue());
        assertEquals("new a", b.getValue());

        b.setValue("new b");
        assertEquals("new a", a.getValue());
        assertEquals("new b", b.getValue());

        link.dispose();
        a.setValue("test");
        assertEquals("test", a.getValue());
        assertEquals("new b", b.getValue());
    }

    @Test
    public void testCallLoop() {
        HasValue<String> a = Flow.newHasValue("a");
        HasValue<String> b = Flow.newHasValue("b");
        HasValue<String> c = Flow.newHasValue("c");

        // Make call loop
        Flow.from(a).to(b);
        Flow.from(b).to(c);
        Flow.from(c).to(a);

        try {
            a.setValue("loop"); // Start call loop
            fail("Call loop is not detected");
        } catch (IllegalStateException e) {
            assertEquals("Call loop detected", e.getMessage());
        }

        assertEquals("loop", a.getValue());
        assertEquals("loop", b.getValue());
        assertEquals("loop", c.getValue());
    }

    @Test
    public void testBothWaySyncLink() {
        HasValue<String> a = Flow.newHasValue("a");
        HasValue<String> b = Flow.newHasValue("b");

        Disposable link = Flow.from(a).sync().to(b);
        assertNotNull(link);
        assertEquals("a", a.getValue());
        assertEquals("b", b.getValue());

        a.setValue("new a");
        assertEquals("new a", a.getValue());
        assertEquals("new a", b.getValue());

        b.setValue("new b");
        assertEquals("new b", a.getValue());
        assertEquals("new b", b.getValue());

        link.dispose();
        a.setValue("test a");
        assertEquals("test a", a.getValue());
        assertEquals("new b", b.getValue());

        b.setValue("test b");
        assertEquals("test a", a.getValue());
        assertEquals("test b", b.getValue());
    }

    @Test
    public void testOneWayForceLink() {
        TestGetValue<String> a = TestGetValue.newGetValue("a");
        HasValue<String> b = Flow.newHasValue("b");

        Disposable link = Flow.from(a).force().to(b);
        assertEquals("a", a.getValue());
        assertEquals("a", b.getValue());

        a.update("new a");
        assertEquals("new a", a.getValue());
        assertEquals("new a", b.getValue());

        b.setValue("new b");
        assertEquals("new a", a.getValue());
        assertEquals("new b", b.getValue());

        link.dispose();
        a.update("test");
        assertEquals("test", a.getValue());
        assertEquals("new b", b.getValue());
    }

    @Test
    public void testBothWayForceLink() {
        HasValue<String> a = Flow.newHasValue("a");
        HasValue<String> b = Flow.newHasValue("b");

        Disposable link = Flow.from(a).force().to(b);
        assertEquals("a", a.getValue());
        assertEquals("a", b.getValue());

        a.setValue("new a");
        assertEquals("new a", a.getValue());
        assertEquals("new a", b.getValue());

        b.setValue("new b");
        assertEquals("new a", a.getValue());
        assertEquals("new b", b.getValue());

        link.dispose();
        a.setValue("test");
        assertEquals("test", a.getValue());
        assertEquals("new b", b.getValue());
    }

    @Test
    public void testOneWayStrongLink() {
        AffectedListener al = new AffectedListener();
        AffectedListener bl = new AffectedListener();

        TestGetValue<String> a = TestGetValue.newGetValue("a");
        HasValue<String> b = Flow.newHasValue("b");

        a.addChangeListener(al);
        b.addChangeListener(bl);

        Flow.from(a).strong().to(b);

        a.update("b");
        assertEquals("b", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertFalse(bl.testAffectedAndReset());

        a.update("c");
        assertEquals("c", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());
    }

    @Test
    public void testBothWayStrongLink() {
        AffectedListener al = new AffectedListener();
        AffectedListener bl = new AffectedListener();

        HasValue<String> a = Flow.newHasValue("a");
        HasValue<String> b = Flow.newHasValue("b");

        a.addChangeListener(al);
        b.addChangeListener(bl);

        Flow.from(a).strong().to(b);

        a.setValue("b");
        assertEquals("b", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertFalse(bl.testAffectedAndReset());

        a.setValue("c");
        assertEquals("c", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());
    }

    @Test
    public void testBothWaySyncStrongLink() {
        AffectedListener al = new AffectedListener();
        AffectedListener bl = new AffectedListener();

        HasValue<String> a = Flow.newHasValue("a");
        HasValue<String> b = Flow.newHasValue("b");

        a.addChangeListener(al);
        b.addChangeListener(bl);

        Flow.from(a).sync().strong().to(b);

        a.setValue("b");
        assertEquals("b", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertFalse(bl.testAffectedAndReset());

        a.setValue("c");
        assertEquals("c", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());

        b.setValue("c");
        assertEquals("c", a.getValue());
        assertFalse(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());

        b.setValue("d");
        assertEquals("d", a.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());
    }

    @Test
    public void testBothWaySyncStrongForceLink() {
        AffectedListener al = new AffectedListener();
        AffectedListener bl = new AffectedListener();

        HasValue<String> a = Flow.newHasValue("a");
        HasValue<String> b = Flow.newHasValue("b");

        a.addChangeListener(al);
        b.addChangeListener(bl);

        Flow.from(a).sync().strong().force().to(b);

        assertEquals("a", b.getValue());
        assertFalse(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());

        a.setValue("a");
        assertEquals("a", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertFalse(bl.testAffectedAndReset());

        a.setValue("c");
        assertEquals("c", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());

        b.setValue("c");
        assertEquals("c", a.getValue());
        assertFalse(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());

        b.setValue("d");
        assertEquals("d", a.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());
    }

    private static class AffectedListener implements ValueChangeListener<Object> {
        private boolean affected = false;

        public boolean testAffectedAndReset() {
            boolean res = affected;
            affected = false;
            return res;
        }

        @Override
        public void valueChanged(Getter<?> getter) {
            affected = true;
        }
    }
}
