package com.github.linfro.core;

import com.github.linfro.core.auxiliary.TestGetValue;
import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.HasValue;
import org.junit.Test;

import static com.github.linfro.core.ValueDSL.linkFrom;
import static com.github.linfro.core.ValueDSL.newHasValue;
import static com.github.linfro.core.auxiliary.TestGetValue.newGetValue;
import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-06
 * @since 1.0.0
 */
public class ValueDslTest {
    @Test
    public void testNewHasValue() {
        HasValue<String> strValue = newHasValue();
        assertNotNull(strValue);
        assertNull(strValue.getValue());

        HasValue<Integer> intValue = newHasValue(500);
        assertNotNull(intValue);
        assertEquals(new Integer(500), intValue.getValue());
    }

    @Test
    public void testSimpleBothWayLink() {
        TestGetValue<String> a = newGetValue("a");
        HasValue<String> b = newHasValue("b");

        Disposable link = linkFrom(a).to(b);
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
    public void testSimpleOneWayLink() {
        HasValue<String> a = newHasValue("a");
        HasValue<String> b = newHasValue("b");

        Disposable link = linkFrom(a).to(b);
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
}
