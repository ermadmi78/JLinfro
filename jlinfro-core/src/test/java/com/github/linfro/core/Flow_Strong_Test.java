package com.github.linfro.core;

import com.github.linfro.core.value.AffectedListener;
import com.github.linfro.core.value.TestGetValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-03-16
 * @since 1.0.0
 */
public class Flow_Strong_Test {
    @Test
    public void testGetStrongValue() {
        AffectedListener al = new AffectedListener();
        AffectedListener bl = new AffectedListener();

        TestGetValue<String> a = TestGetValue.newGetValue("a");
        HasValue<String> b = Values.newHasValue();

        a.addChangeListener(al);
        b.addChangeListener(bl);

        a.strong().flow().to(b);

        a.update("a");
        assertEquals("a", a.getValue());
        assertNull(b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertFalse(bl.testAffectedAndReset());

        a.update("c");
        assertEquals("c", a.getValue());
        assertEquals("c", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());
    }

    @Test
    public void testHasStrongValue() {
        AffectedListener al = new AffectedListener();
        AffectedListener bl = new AffectedListener();

        HasValue<String> a = Values.newHasValue("a");
        HasValue<String> b = Values.newHasValue();

        a.addChangeListener(al);
        b.addChangeListener(bl);

        a.strong().flow().sync().to(b);

        a.setValue("a");
        assertEquals("a", a.getValue());
        assertNull(b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertFalse(bl.testAffectedAndReset());

        a.setValue("c");
        assertEquals("c", a.getValue());
        assertEquals("c", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());

        b.setValue("c");
        assertEquals("c", a.getValue());
        assertEquals("c", b.getValue());
        assertFalse(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());

        b.setValue("d");
        assertEquals("d", a.getValue());
        assertEquals("d", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());
    }

    @Test
    public void testHasStrongValueForceLink() {
        AffectedListener al = new AffectedListener();
        AffectedListener bl = new AffectedListener();

        HasValue<String> a = Values.newHasValue("a");
        HasValue<String> b = Values.newHasValue("b");

        a.addChangeListener(al);
        b.addChangeListener(bl);

        a.strong().flow().sync().force().to(b);

        assertEquals("a", a.getValue());
        assertEquals("a", b.getValue());
        assertFalse(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());

        a.setValue("a");
        assertEquals("a", a.getValue());
        assertEquals("a", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertFalse(bl.testAffectedAndReset());

        a.setValue("c");
        assertEquals("c", a.getValue());
        assertEquals("c", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());

        b.setValue("c");
        assertEquals("c", a.getValue());
        assertEquals("c", b.getValue());
        assertFalse(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());

        b.setValue("d");
        assertEquals("d", a.getValue());
        assertEquals("d", b.getValue());
        assertTrue(al.testAffectedAndReset());
        assertTrue(bl.testAffectedAndReset());
    }
}
