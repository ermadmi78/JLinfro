package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.TestGetValue;
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
        HasValue<String> strValue = Values.newHasValue();
        assertNotNull(strValue);
        assertNull(strValue.getValue());

        HasValue<Integer> intValue = Values.newHasValue(500);
        assertNotNull(intValue);
        assertEquals(new Integer(500), intValue.getValue());
    }

    @Test
    public void testOneWayLink() {
        TestGetValue<String> a = TestGetValue.newGetValue("a");
        HasValue<String> b = Values.newHasValue("b");

        Disposable link = a.flow().to(b);
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
        HasValue<String> a = Values.newHasValue("a");
        HasValue<String> b = Values.newHasValue("b");

        Disposable link = a.flow().to(b);
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
        HasValue<String> a = Values.newHasValue("a");
        HasValue<String> b = Values.newHasValue("b");
        HasValue<String> c = Values.newHasValue("c");

        // Make call loop
        a.flow().to(b);
        b.flow().to(c);
        c.flow().to(a);

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
        HasValue<String> a = Values.newHasValue("a");
        HasValue<String> b = Values.newHasValue("b");

        Disposable link = a.flow().sync().to(b);
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
        HasValue<String> b = Values.newHasValue("b");

        Disposable link = a.flow().force().to(b);
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
        HasValue<String> a = Values.newHasValue("a");
        HasValue<String> b = Values.newHasValue("b");

        Disposable link = a.flow().force().to(b);
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
}
