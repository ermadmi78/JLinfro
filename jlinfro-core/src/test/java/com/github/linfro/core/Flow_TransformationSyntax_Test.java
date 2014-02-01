package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.RevertFunction;
import com.github.linfro.core.value.HasValue;
import com.github.linfro.core.value.TestGetValue;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-08
 * @since 1.0.0
 */
public class Flow_TransformationSyntax_Test {
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

        /*
        Disposable link = strVal.outFlow().map(
                (from) -> from == null ? null : Integer.valueOf(from)
        ).to(intVal);
        */
        Disposable link = strVal.outFlow().map(Integer::valueOf).to(intVal);

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
    public void testHybridSyncTransform() {
        HasValue<String> strVal = Flow.newHasValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        Disposable link = Flow.from(strVal).map(
                new RevertFunction<String, Integer>() {
                    @Override
                    public Integer apply(String from) {
                        return from == null ? null : Integer.valueOf(from);
                    }

                    @Override
                    public String revert(Integer to) {
                        return to == null ? null : to.toString();
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
    public void testBothWaySyncTransform() {
        HasValue<String> strVal = Flow.newHasValue();
        HasValue<Integer> intVal = Flow.newHasValue();

        Disposable link = Flow.from(strVal).sync().map(
                new RevertFunction<String, Integer>() {
                    @Override
                    public Integer apply(String from) {
                        return from == null ? null : Integer.valueOf(from);
                    }

                    @Override
                    public String revert(Integer to) {
                        return to == null ? null : to.toString();
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
}
