package com.github.linfro.core.value;

import com.github.linfro.core.GetValue;
import com.github.linfro.core.HasValue;
import com.github.linfro.core.Values;
import org.junit.Test;

import static com.github.linfro.core.value.TestUtil.assertDisposed;
import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-03-16
 * @since 1.0.0
 */
public class DisposeTest {
    @Test
    public void testGetTransformedValue() throws Exception {
        HasValue<Integer> srcVal = Values.newHasValue(3);
        GetValue<String> midVal = srcVal.mapNotNull(Object::toString);
        GetValue<String> dstVal = midVal.mapNotNull((s) -> s + "_t");

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertEquals("3", midVal.getValue());
        assertFalse(midVal.canDispose());
        assertEquals("3_t", dstVal.getValue());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertEquals(new Integer(3), srcVal.getValue());
        assertDisposed(midVal::getValue);
        assertDisposed(dstVal::getValue);

        midVal = srcVal.mapNotNull(Object::toString);
        dstVal = midVal.mapNotNull((s) -> s + "_t");

        TestListener listener = new TestListener();
        midVal.addChangeListener(listener);

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertEquals("3", midVal.getValue());
        assertFalse(midVal.canDispose());
        assertEquals("3_t", dstVal.getValue());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertEquals(new Integer(3), srcVal.getValue());
        assertEquals("3", midVal.getValue());
        assertFalse(midVal.canDispose());
        assertDisposed(dstVal::getValue);

        midVal.removeChangeListener(listener);
        assertTrue(midVal.canDispose());

        midVal.dispose();
        assertEquals(new Integer(3), srcVal.getValue());
        assertDisposed(midVal::getValue);
    }

    @Test
    public void testHasTransformedValue() throws Exception {
        HasValue<Integer> srcVal = Values.newHasValue(3);
        HasValue<String> midVal = srcVal.mapNotNull(Object::toString, Integer::valueOf);
        HasValue<String> dstVal = midVal.mapNotNull((s) -> s + "_t", (s) -> s.substring(0, s.length() - 2));

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertEquals("3", midVal.getValue());
        assertFalse(midVal.canDispose());
        assertEquals("3_t", dstVal.getValue());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertEquals(new Integer(3), srcVal.getValue());
        assertDisposed(midVal::getValue);
        assertDisposed(dstVal::getValue);

        midVal = srcVal.mapNotNull(Object::toString, Integer::valueOf);
        dstVal = midVal.mapNotNull((s) -> s + "_t", (s) -> s.substring(0, s.length() - 2));

        TestListener listener = new TestListener();
        midVal.addChangeListener(listener);

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertEquals("3", midVal.getValue());
        assertFalse(midVal.canDispose());
        assertEquals("3_t", dstVal.getValue());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertEquals(new Integer(3), srcVal.getValue());
        assertEquals("3", midVal.getValue());
        assertFalse(midVal.canDispose());
        assertDisposed(dstVal::getValue);

        midVal.removeChangeListener(listener);
        assertTrue(midVal.canDispose());

        midVal.dispose();
        assertEquals(new Integer(3), srcVal.getValue());
        assertDisposed(midVal::getValue);
    }

    @Test
    public void testGetFilteredValue() throws Exception {
        HasValue<Integer> srcVal = Values.newHasValue();
        GetValue<Integer> midVal = srcVal.filter((i) -> i != null);
        GetValue<Integer> dstVal = midVal.filter((i) -> i != 0);

        assertNull(srcVal.getValue());
        assertTrue(srcVal.isValueValid());
        assertTrue(srcVal.canDispose());
        assertFalse(midVal.isValueValid());
        assertFalse(midVal.canDispose());
        assertFalse(dstVal.isValueValid());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertNull(srcVal.getValue());
        assertTrue(srcVal.isValueValid());
        assertTrue(srcVal.canDispose());
        assertDisposed(midVal::isValueValid);
        assertDisposed(dstVal::isValueValid);

        midVal = srcVal.filter((i) -> i != null);
        dstVal = midVal.filter((i) -> i != 0);

        TestListener listener = new TestListener();
        midVal.addChangeListener(listener);

        assertNull(srcVal.getValue());
        assertTrue(srcVal.isValueValid());
        assertTrue(srcVal.canDispose());
        assertFalse(midVal.isValueValid());
        assertFalse(midVal.canDispose());
        assertFalse(dstVal.isValueValid());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertNull(srcVal.getValue());
        assertTrue(srcVal.isValueValid());
        assertTrue(srcVal.canDispose());
        assertFalse(midVal.isValueValid());
        assertFalse(midVal.canDispose());
        assertDisposed(dstVal::isValueValid);

        midVal.removeChangeListener(listener);
        assertTrue(midVal.canDispose());

        midVal.dispose();

        assertNull(srcVal.getValue());
        assertTrue(srcVal.isValueValid());
        assertTrue(srcVal.canDispose());
        assertDisposed(midVal::isValueValid);
    }

    @Test
    public void testHasFilteredValue() throws Exception {
        HasValue<Integer> srcVal = Values.newHasValue();
        HasValue<Integer> midVal = srcVal.biFilter((i) -> i != null);
        HasValue<Integer> dstVal = midVal.biFilter((i) -> i != 0);

        assertNull(srcVal.getValue());
        assertTrue(srcVal.isValueValid());
        assertTrue(srcVal.canDispose());
        assertFalse(midVal.isValueValid());
        assertFalse(midVal.canDispose());
        assertFalse(dstVal.isValueValid());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertNull(srcVal.getValue());
        assertTrue(srcVal.isValueValid());
        assertTrue(srcVal.canDispose());
        assertDisposed(midVal::isValueValid);
        assertDisposed(dstVal::isValueValid);

        midVal = srcVal.biFilter((i) -> i != null);
        dstVal = midVal.biFilter((i) -> i != 0);

        TestListener listener = new TestListener();
        midVal.addChangeListener(listener);

        assertNull(srcVal.getValue());
        assertTrue(srcVal.isValueValid());
        assertTrue(srcVal.canDispose());
        assertFalse(midVal.isValueValid());
        assertFalse(midVal.canDispose());
        assertFalse(dstVal.isValueValid());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertNull(srcVal.getValue());
        assertTrue(srcVal.isValueValid());
        assertTrue(srcVal.canDispose());
        assertFalse(midVal.isValueValid());
        assertFalse(midVal.canDispose());
        assertDisposed(dstVal::isValueValid);

        midVal.removeChangeListener(listener);
        assertTrue(midVal.canDispose());

        midVal.dispose();

        assertNull(srcVal.getValue());
        assertTrue(srcVal.isValueValid());
        assertTrue(srcVal.canDispose());
        assertDisposed(midVal::isValueValid);
    }

    @Test
    public void testGetMetaInfoValue() throws Exception {
        TestGetValue<Integer> srcVal = TestGetValue.newGetValue(3);
        GetValue<Integer> midVal = srcVal.named("midVal");
        GetValue<Integer> dstVal = midVal.named("dstVal");

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertEquals("midVal", midVal.getMetaName());
        assertFalse(midVal.canDispose());
        assertEquals("dstVal", dstVal.getMetaName());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertDisposed(midVal::getMetaName);
        assertDisposed(dstVal::getMetaName);

        midVal = srcVal.named("midVal");
        dstVal = midVal.named("dstVal");

        TestListener listener = new TestListener();
        midVal.addChangeListener(listener);

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertEquals("midVal", midVal.getMetaName());
        assertFalse(midVal.canDispose());
        assertEquals("dstVal", dstVal.getMetaName());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertEquals("midVal", midVal.getMetaName());
        assertFalse(midVal.canDispose());
        assertDisposed(dstVal::getMetaName);

        midVal.removeChangeListener(listener);
        assertTrue(midVal.canDispose());

        midVal.dispose();

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertDisposed(midVal::getMetaName);
    }

    @Test
    public void testHasMetaInfoValue() throws Exception {
        HasValue<Integer> srcVal = Values.newHasValue(3);
        HasValue<Integer> midVal = srcVal.named("midVal");
        HasValue<Integer> dstVal = midVal.named("dstVal");

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertEquals("midVal", midVal.getMetaName());
        assertFalse(midVal.canDispose());
        assertEquals("dstVal", dstVal.getMetaName());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertDisposed(midVal::getMetaName);
        assertDisposed(dstVal::getMetaName);

        midVal = srcVal.named("midVal");
        dstVal = midVal.named("dstVal");

        TestListener listener = new TestListener();
        midVal.addChangeListener(listener);

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertEquals("midVal", midVal.getMetaName());
        assertFalse(midVal.canDispose());
        assertEquals("dstVal", dstVal.getMetaName());
        assertTrue(dstVal.canDispose());

        dstVal.dispose();

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertEquals("midVal", midVal.getMetaName());
        assertFalse(midVal.canDispose());
        assertDisposed(dstVal::getMetaName);

        midVal.removeChangeListener(listener);
        assertTrue(midVal.canDispose());

        midVal.dispose();

        assertEquals(new Integer(3), srcVal.getValue());
        assertTrue(srcVal.canDispose());
        assertDisposed(midVal::getMetaName);
    }
}
