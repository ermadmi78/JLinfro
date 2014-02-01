package com.github.linfro.core.reflection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-01
 * @since 1.0.0
 */
public class AnonymousPropertyInvokerTest {
    @Test(expected = IllegalArgumentException.class)
    public void invalidPropertyNameTest() throws Exception {
        new AnonymousPropertyInvoker("invalid name");
    }

    @Test
    public void simpleAnonymousPropertyInvokerTest() throws Exception {
        AnonymousPropertyInvoker invoker = new AnonymousPropertyInvoker("wrapString");
        assertEquals("wrapString", invoker.getPropertyName());

        FieldsBean fieldsBean = new FieldsBean();
        fieldsBean.wrapString = "Fields test";

        MethodsBean methodsBean = new MethodsBean();
        methodsBean.setWrapString("Methods test");

        assertEquals("Fields test", invoker.getPropertyValue(fieldsBean));
        assertEquals("Methods test", invoker.getPropertyValue(methodsBean));

        invoker.setPropertyValue(fieldsBean, "Fields modified test");
        invoker.setPropertyValue(methodsBean, "Methods modified test");

        assertEquals("Fields modified test", fieldsBean.wrapString);
        assertEquals("Methods modified test", methodsBean.getWrapString());
    }

    @Test
    public void compositeAnonymousPropertyInvokerTest() throws Exception {
        AnonymousPropertyInvoker invoker = new AnonymousPropertyInvoker("first.second.value");
        assertEquals("first.second.value", invoker.getPropertyName());

        TestRootBean firstBean = new TestRootBean();
        TestAnotherBean secondBean = new TestAnotherBean();

        assertEquals(1, firstBean.getFirst().getSecond().getValue());
        assertEquals(1, secondBean.first.getSecond().getValue());

        invoker.setPropertyValue(firstBean, 1111);
        invoker.setPropertyValue(secondBean, 2222);

        assertEquals(1111, firstBean.getFirst().getSecond().getValue());
        assertEquals(2222, secondBean.first.getSecond().getValue());

        firstBean.getFirst().getSecond().setValue(11);
        secondBean.first.getSecond().setValue(22);

        assertEquals(11, invoker.getPropertyValue(firstBean));
        assertEquals(22, invoker.getPropertyValue(secondBean));
    }
}
