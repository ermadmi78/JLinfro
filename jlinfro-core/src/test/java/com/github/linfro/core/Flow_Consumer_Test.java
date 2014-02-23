package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.value.TestGetValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public class Flow_Consumer_Test {
    @Test
    public void testConsumerForGetFlow() throws Exception {
        TestGetValue<String> strVal = TestGetValue.newGetValue("init");
        TestConsumer consumer = new TestConsumer();

        assertEquals("init", strVal.getValue());
        assertNull(consumer.value);

        Disposable link = strVal.flow().to(consumer::consume);
        assertNull(consumer.value);

        strVal.update("test");
        assertEquals("test", consumer.value);

        link.dispose();

        strVal.update("go");
        assertEquals("test", consumer.value);
    }

    @Test
    public void testConsumerForHasFlow() throws Exception {
        HasValue<String> strVal = Flow.newHasValue("init");
        TestConsumer consumer = new TestConsumer();

        assertEquals("init", strVal.getValue());
        assertNull(consumer.value);

        Disposable link = strVal.flow().force().to(consumer::consume);
        assertEquals("init", consumer.value);

        strVal.setValue("test");
        assertEquals("test", consumer.value);

        link.dispose();

        strVal.setValue("go");
        assertEquals("test", consumer.value);
    }

    @Test
    public void testFilteredConsumer() throws Exception {
        HasValue<String> strVal = Flow.newHasValue("init");
        TestConsumer consumer = new TestConsumer();

        assertEquals("init", strVal.getValue());
        assertNull(consumer.value);

        Disposable link = strVal.filter((s) -> !"init".equals(s)).flow().force().to(consumer::consume);

        assertEquals("init", strVal.getValue());
        assertNull(consumer.value);

        strVal.setValue("ok");
        assertEquals("ok", strVal.getValue());
        assertEquals("ok", consumer.value);

        strVal.setValue("init");
        assertEquals("init", strVal.getValue());
        assertEquals("ok", consumer.value);

        link.dispose();

        strVal.setValue("disposed");
        assertEquals("disposed", strVal.getValue());
        assertEquals("ok", consumer.value);
    }
}
