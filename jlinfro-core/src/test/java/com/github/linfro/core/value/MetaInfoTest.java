package com.github.linfro.core.value;

import com.github.linfro.core.Flow;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-15
 * @since 1.0.0
 */
public class MetaInfoTest {
    @Test
    public void testGetSimpleNamed() throws Exception {
        TestGetValue<String> src = TestGetValue.newGetValue();
        assertNull(src.findMetaName());
        assertNull(src.getValue());

        GetValue<String> res = src.named("test");
        assertEquals("test", res.findMetaName());
        assertNull(res.getValue());

        TestListener listener = new TestListener();
        res.addChangeListener(listener);
        assertEquals(0, listener.getCounter());

        src.update("val");
        assertEquals("val", src.getValue());
        assertEquals("val", res.getValue());
        assertEquals(1, listener.getCounter());

        if (res.isAutoDispose()) {
            res.dispose();
        }

        src.update("disposed");
        assertEquals("disposed", src.getValue());
        assertEquals("test", res.findMetaName());
        assertEquals(1, listener.getCounter());

        try {
            res.getValue();
            fail("Disposed wrapper must throw exception");
        } catch (IllegalStateException e) {
            assertEquals("Value is disposed", e.getMessage());
        }
    }

    @Test
    public void testHasSimpleNamed() throws Exception {
        HasValue<String> src = Flow.newHasValue();
        assertNull(src.findMetaName());
        assertNull(src.getValue());

        HasValue<String> res = src.named("test");
        assertEquals("test", res.findMetaName());
        assertNull(res.getValue());

        TestListener listener = new TestListener();
        res.addChangeListener(listener);
        assertEquals(0, listener.getCounter());

        src.setValue("val");
        assertEquals("val", src.getValue());
        assertEquals("val", res.getValue());
        assertEquals(1, listener.getCounter());

        res.setValue("new");
        assertEquals("new", src.getValue());
        assertEquals("new", res.getValue());
        assertEquals(2, listener.getCounter());

        if (res.isAutoDispose()) {
            res.dispose();
        }

        src.setValue("disposed");
        assertEquals("disposed", src.getValue());
        assertEquals("test", res.findMetaName());
        assertEquals(2, listener.getCounter());

        try {
            res.getValue();
            fail("Disposed wrapper must throw exception");
        } catch (IllegalStateException e) {
            assertEquals("Value is disposed", e.getMessage());
        }

        try {
            res.setValue("Should throw exception");
            fail("Disposed wrapper must throw exception");
        } catch (IllegalStateException e) {
            assertEquals("Value is disposed", e.getMessage());
        }
    }

    @Test
    public void testGetNamedNamed() throws Exception {
        TestGetValue<String> src = TestGetValue.newGetValue();
        assertNull(src.findMetaName());
        assertNull(src.getValue());

        GetValue<String> res = src.named("first").named("second");
        assertEquals("second", res.findMetaName());
        assertNull(res.getValue());

        TestListener listener = new TestListener();
        res.addChangeListener(listener);
        assertEquals(0, listener.getCounter());

        src.update("val");
        assertEquals("val", src.getValue());
        assertEquals("val", res.getValue());
        assertEquals(1, listener.getCounter());

        if (res.isAutoDispose()) {
            res.dispose();
        }

        src.update("disposed");
        assertEquals("disposed", src.getValue());
        assertEquals("second", res.findMetaName());
        assertEquals(1, listener.getCounter());

        try {
            res.getValue();
            fail("Disposed wrapper must throw exception");
        } catch (IllegalStateException e) {
            assertEquals("Value is disposed", e.getMessage());
        }
    }

    @Test
    public void testHasNamedNamed() throws Exception {
        HasValue<String> src = Flow.newHasValue();
        assertNull(src.findMetaName());
        assertNull(src.getValue());

        HasValue<String> res = src.named("first").named("second");
        assertEquals("second", res.findMetaName());
        assertNull(res.getValue());

        TestListener listener = new TestListener();
        res.addChangeListener(listener);
        assertEquals(0, listener.getCounter());

        src.setValue("val");
        assertEquals("val", src.getValue());
        assertEquals("val", res.getValue());
        assertEquals(1, listener.getCounter());

        res.setValue("new");
        assertEquals("new", src.getValue());
        assertEquals("new", res.getValue());
        assertEquals(2, listener.getCounter());

        if (res.isAutoDispose()) {
            res.dispose();
        }

        src.setValue("disposed");
        assertEquals("disposed", src.getValue());
        assertEquals("second", res.findMetaName());
        assertEquals(2, listener.getCounter());

        try {
            res.getValue();
            fail("Disposed wrapper must throw exception");
        } catch (IllegalStateException e) {
            assertEquals("Value is disposed", e.getMessage());
        }

        try {
            res.setValue("Should throw exception");
            fail("Disposed wrapper must throw exception");
        } catch (IllegalStateException e) {
            assertEquals("Value is disposed", e.getMessage());
        }
    }

    @Test
    public void testGetNamedMap() throws Exception {
        TestGetValue<Integer> src = TestGetValue.newGetValue();
        GetValue<String> res = src.named("test").nvl(0).map(Object::toString);
        assertNull(src.getValue());
        assertEquals("0", res.getValue());
        assertEquals("test", res.findMetaName());

        src.update(11);
        assertEquals(new Integer(11), src.getValue());
        assertEquals("11", res.getValue());
        assertEquals("test", res.findMetaName());
    }

    @Test
    public void testHasNamedMap() throws Exception {
        HasValue<Integer> src = Flow.newHasValue();
        HasValue<String> res = src.named("test").nvl(0, 0).map(Object::toString, Integer::valueOf);
        assertNull(src.getValue());
        assertEquals("0", res.getValue());
        assertEquals("test", res.findMetaName());

        src.setValue(11);
        assertEquals(new Integer(11), src.getValue());
        assertEquals("11", res.getValue());
        assertEquals("test", res.findMetaName());

        res.setValue("215");
        assertEquals(new Integer(215), src.getValue());
        assertEquals("215", res.getValue());
        assertEquals("test", res.findMetaName());
    }

    @Test
    public void testGetNamedFilter() throws Exception {
        TestGetValue<String> src = TestGetValue.newGetValue("U");
        GetValue<String> res = src.named("test").filter("ok"::equals);

        assertEquals("U", src.getValue());
        assertEquals("test", res.findMetaName());

        try {
            res.getValue();
            fail("Filtered value must throw exception");
        } catch (FilterException e) {
            //Ok
        }

        src.update("ok");
        assertEquals("ok", src.getValue());
        assertEquals("ok", res.getValue());
        assertEquals("test", res.findMetaName());
    }

    @Test
    public void testHasNamedFilter() throws Exception {
        HasValue<String> src = Flow.newHasValue("U");
        HasValue<String> res = src.named("test").filter("ok"::equals, "ok"::equals);

        assertEquals("U", src.getValue());
        assertEquals("test", res.findMetaName());

        try {
            res.getValue();
            fail("Filtered value must throw exception");
        } catch (FilterException e) {
            //Ok
        }

        res.setValue("ok");
        assertEquals("ok", src.getValue());
        assertEquals("ok", res.getValue());
        assertEquals("test", res.findMetaName());

        res.setValue("not");
        assertEquals("ok", src.getValue());
        assertEquals("ok", res.getValue());
        assertEquals("test", res.findMetaName());
    }
}
