package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.TestGetValue;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-08
 * @since 1.0.0
 */
public class Flow_Map_Test {
    /*
    @Test
    public void testOneWayDirectTransform() {
        TestGetValue<String> strVal = TestGetValue.newGetValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        Disposable link = Flow.from(strVal).map(
                new Function<String, Integer>() {
                    @Override
                    public Integer apply(String from) {
                        return from == null ? null : Integer.valueOf(from);
                    }
                }
        ).to(intVal);

        strVal.update("50");
        assertEquals("50", strVal.getValue());
        assertEquals(new Integer(50), intVal.getValue());

        link.dispose();
        strVal.update("700");
        assertEquals("700", strVal.getValue());
        assertEquals(new Integer(50), intVal.getValue());
    }

    @Test
    public void testOneWayDirectMap() {
        TestGetValue<String> strVal = TestGetValue.newGetValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        Disposable link = strVal.flow().map(
                (from) -> from == null ? null : Integer.valueOf(from)
        ).to(intVal);

        strVal.update("50");
        assertEquals("50", strVal.getValue());
        assertEquals(new Integer(50), intVal.getValue());

        link.dispose();
        strVal.update("700");
        assertEquals("700", strVal.getValue());
        assertEquals(new Integer(50), intVal.getValue());
    }

    //******************************************************************************************************************

    @Test
    public void testHybridDirectTransform() {
        HasValue<String> strVal = Flow.newHasValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        Disposable link = Flow.from(strVal).map(
                new Function<String, Integer>() {
                    @Override
                    public Integer apply(String from) {
                        return from == null ? null : Integer.valueOf(from);
                    }
                }
        ).to(intVal);

        strVal.setValue("50");
        assertEquals("50", strVal.getValue());
        assertEquals(new Integer(50), intVal.getValue());

        link.dispose();
        strVal.setValue("700");
        assertEquals("700", strVal.getValue());
        assertEquals(new Integer(50), intVal.getValue());
    }

    @Test
    public void testHybridSync2FunctionsTransform() {
        HasValue<String> strVal = Flow.newHasValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        Disposable link = Flow.from(strVal).map(
                new Function<String, Integer>() {
                    @Override
                    public Integer apply(String from) {
                        return from == null ? null : Integer.valueOf(from);
                    }
                },
                new Function<Integer, String>() {
                    @Override
                    public String apply(Integer from) {
                        return from == null ? null : from.toString();
                    }
                }
        ).sync().to(intVal);

        strVal.setValue("50");
        assertEquals("50", strVal.getValue());
        assertEquals(new Integer(50), intVal.getValue());

        intVal.setValue(60);
        assertEquals("60", strVal.getValue());
        assertEquals(new Integer(60), intVal.getValue());

        link.dispose();

        strVal.setValue("500");
        assertEquals("500", strVal.getValue());
        assertEquals(new Integer(60), intVal.getValue());

        intVal.setValue(600);
        assertEquals("500", strVal.getValue());
        assertEquals(new Integer(600), intVal.getValue());
    }

    //******************************************************************************************************************

    @Test
    public void testBothWaySyncMap() throws Exception {
        HasValue<String> strVal = Flow.newHasValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        Disposable link = strVal.map(Integer::valueOf, (to) -> to.toString()).flow().sync().to(intVal);

        strVal.setValue("50");
        assertEquals("50", strVal.getValue());
        assertEquals(new Integer(50), intVal.getValue());

        intVal.setValue(60);
        assertEquals("60", strVal.getValue());
        assertEquals(new Integer(60), intVal.getValue());

        link.dispose();

        strVal.setValue("500");
        assertEquals("500", strVal.getValue());
        assertEquals(new Integer(60), intVal.getValue());

        intVal.setValue(600);
        assertEquals("500", strVal.getValue());
        assertEquals(new Integer(600), intVal.getValue());
    }

    @Test
    public void testBothWaySync2FunctionsTransform() {
        HasValue<String> strVal = Flow.newHasValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        Disposable link = Flow.from(strVal).sync().map(
                new Function<String, Integer>() {
                    @Override
                    public Integer apply(String from) {
                        return from == null ? null : Integer.valueOf(from);
                    }
                },
                new Function<Integer, String>() {
                    @Override
                    public String apply(Integer from) {
                        return from == null ? null : from.toString();
                    }
                }
        ).to(intVal);

        strVal.setValue("50");
        assertEquals("50", strVal.getValue());
        assertEquals(new Integer(50), intVal.getValue());

        intVal.setValue(60);
        assertEquals("60", strVal.getValue());
        assertEquals(new Integer(60), intVal.getValue());

        link.dispose();

        strVal.setValue("500");
        assertEquals("500", strVal.getValue());
        assertEquals(new Integer(60), intVal.getValue());

        intVal.setValue(600);
        assertEquals("500", strVal.getValue());
        assertEquals(new Integer(600), intVal.getValue());
    }

    @Test
    public void testBothWaySync2FunctionsSyncTransform() {
        HasValue<String> strVal = Flow.newHasValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        Disposable link = Flow.from(strVal).sync().map(
                new Function<String, Integer>() {
                    @Override
                    public Integer apply(String from) {
                        return from == null ? null : Integer.valueOf(from);
                    }
                },
                new Function<Integer, String>() {
                    @Override
                    public String apply(Integer from) {
                        return from == null ? null : from.toString();
                    }
                }
        ).sync().to(intVal);

        strVal.setValue("50");
        assertEquals("50", strVal.getValue());
        assertEquals(new Integer(50), intVal.getValue());

        intVal.setValue(60);
        assertEquals("60", strVal.getValue());
        assertEquals(new Integer(60), intVal.getValue());

        link.dispose();

        strVal.setValue("500");
        assertEquals("500", strVal.getValue());
        assertEquals(new Integer(60), intVal.getValue());

        intVal.setValue(600);
        assertEquals("500", strVal.getValue());
        assertEquals(new Integer(600), intVal.getValue());
    }

    // Not null mapping ************************************************************************************************

    @Test
    public void testNotNullOutMappingForIOutFlow() throws Exception {
        TestGetValue<String> strVal = TestGetValue.newGetValue();
        HasValue<Integer> intVal = Flow.newHasValue(5);

        assertNull(strVal.getValue());
        assertEquals(new Integer(5), intVal.getValue());

        Disposable link = strVal.flow().mapNotNull(Integer::valueOf).force().to(intVal);

        assertNull(strVal.getValue());
        assertNull(intVal.getValue());

        strVal.update("20");
        assertEquals("20", strVal.getValue());
        assertEquals(new Integer(20), intVal.getValue());

        strVal.update(null);
        assertNull(strVal.getValue());
        assertNull(intVal.getValue());

        link.dispose();

        strVal.update("100");
        assertEquals("100", strVal.getValue());
        assertNull(intVal.getValue());
    }

    @Test
    public void testNotNullOutMappingForIHybridFlow() throws Exception {
        HasValue<String> strVal = Flow.newHasValue();
        HasValue<Integer> intVal = Flow.newHasValue(5);

        assertNull(strVal.getValue());
        assertEquals(new Integer(5), intVal.getValue());

        Disposable link = strVal.flow().mapNotNull(Integer::valueOf).force().to(intVal);

        assertNull(strVal.getValue());
        assertNull(intVal.getValue());

        strVal.setValue("20");
        assertEquals("20", strVal.getValue());
        assertEquals(new Integer(20), intVal.getValue());

        strVal.setValue(null);
        assertNull(strVal.getValue());
        assertNull(intVal.getValue());

        link.dispose();

        strVal.setValue("100");
        assertEquals("100", strVal.getValue());
        assertNull(intVal.getValue());
    }

    @Test
    public void testNotNullInOutMappingForIInOutFlow() throws Exception {
        HasValue<String> strVal = Flow.newHasValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        assertNull(strVal.getValue());
        assertNull(intVal.getValue());

        Disposable link = strVal.flow().sync().mapNotNull((s) -> Integer.valueOf(s), (i) -> i.toString()).to(intVal);

        strVal.setValue("20");
        assertEquals("20", strVal.getValue());
        assertEquals(new Integer(20), intVal.getValue());

        strVal.setValue(null);
        assertNull(strVal.getValue());
        assertNull(intVal.getValue());

        intVal.setValue(375);
        assertEquals("375", strVal.getValue());
        assertEquals(new Integer(375), intVal.getValue());

        intVal.setValue(null);
        assertNull(strVal.getValue());
        assertNull(intVal.getValue());

        link.dispose();

        strVal.setValue("test");
        intVal.setValue(-123);

        assertEquals("test", strVal.getValue());
        assertEquals(new Integer(-123), intVal.getValue());
    }

    @Test
    public void testNotNullInOutMappingForIHybridFlow() throws Exception {
        HasValue<String> strVal = Flow.newHasValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        assertNull(strVal.getValue());
        assertNull(intVal.getValue());

        Disposable link = strVal.flow().mapNotNull((s) -> Integer.valueOf(s), (i) -> i.toString()).sync().to(intVal);

        strVal.setValue("20");
        assertEquals("20", strVal.getValue());
        assertEquals(new Integer(20), intVal.getValue());

        strVal.setValue(null);
        assertNull(strVal.getValue());
        assertNull(intVal.getValue());

        intVal.setValue(375);
        assertEquals("375", strVal.getValue());
        assertEquals(new Integer(375), intVal.getValue());

        intVal.setValue(null);
        assertNull(strVal.getValue());
        assertNull(intVal.getValue());

        link.dispose();

        strVal.setValue("test");
        intVal.setValue(-123);

        assertEquals("test", strVal.getValue());
        assertEquals(new Integer(-123), intVal.getValue());
    }
    */
}
