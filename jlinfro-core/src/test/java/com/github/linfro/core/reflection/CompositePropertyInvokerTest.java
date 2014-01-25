package com.github.linfro.core.reflection;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-25
 * @since 1.0.0
 */
public class CompositePropertyInvokerTest {
    private static final InvokerFactory FACTORY = new ReflectiveInvokerFactory();

    @Test
    public void testGetValue() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.second.value", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.second.value", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        TestRootBean bean = new TestRootBean();
        assertEquals(1, bean.getFirst().getSecond().getValue());

        Integer res = (Integer) invoker.getPropertyValue(bean);
        assertEquals(new Integer(1), res);
    }

    @Test
    public void testSetValue() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.second.value", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.second.value", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        TestRootBean bean = new TestRootBean();
        assertEquals(1, bean.getFirst().getSecond().getValue());

        invoker.setPropertyValue(bean, 22);
        assertEquals(22, bean.getFirst().getSecond().getValue());
    }

    @Test
    public void testGetMiddleValue() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.second", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.second", invoker.getPropertyName());
        assertEquals(TestSecondInnerBean.class, invoker.getPropertyType());
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        TestRootBean bean = new TestRootBean();
        assertEquals(1, bean.getFirst().getSecond().getValue());

        TestSecondInnerBean second = (TestSecondInnerBean) invoker.getPropertyValue(bean);
        assertNotNull(second);
        assertEquals(1, second.getValue());
    }

    @Test
    public void testSetMiddleValue() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.second", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.second", invoker.getPropertyName());
        assertEquals(TestSecondInnerBean.class, invoker.getPropertyType());
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        TestRootBean bean = new TestRootBean();
        assertEquals(1, bean.getFirst().getSecond().getValue());

        invoker.setPropertyValue(bean, new TestSecondInnerBean(33));
        assertEquals(33, bean.getFirst().getSecond().getValue());
    }

    @Test
    public void testChildGetValue() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.second.value", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.second.value", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        TestRootBean bean = new TestRootBean();
        bean.getFirst().setSecond(new TestChildSecondInnerBean(100));
        assertEquals(100, bean.getFirst().getSecond().getValue());

        Integer res = (Integer) invoker.getPropertyValue(bean);
        assertEquals(new Integer(100), res);
    }

    @Test
    public void testChildSetValue() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.second.value", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.second.value", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        TestRootBean bean = new TestRootBean();
        bean.getFirst().setSecond(new TestChildSecondInnerBean(100));
        assertEquals(100, bean.getFirst().getSecond().getValue());

        invoker.setPropertyValue(bean, 220);
        assertEquals(220, bean.getFirst().getSecond().getValue());
    }

    @Test
    public void testEmptyMiddleGetValue() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.second.value", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.second.value", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        TestRootBean bean = new TestRootBean();
        bean.getFirst().setSecond(null);
        assertNull(bean.getFirst().getSecond());

        Integer res = (Integer) invoker.getPropertyValue(bean);
        assertNull(res);
    }

    @Test
    public void testEmptyMiddleSetValue() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.second.value", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.second.value", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        TestRootBean bean = new TestRootBean();
        bean.getFirst().setSecond(null);
        assertNull(bean.getFirst().getSecond());

        invoker.setPropertyValue(bean, 115);
        assertNull(bean.getFirst().getSecond());
    }

    @Test
    public void testReadOnlyGetValue() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.readOnly.value", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.readOnly.value", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        TestRootBean bean = new TestRootBean();
        assertEquals(25, bean.getFirst().getReadOnly().getValue());

        Integer res = (Integer) invoker.getPropertyValue(bean);
        assertEquals(new Integer(25), res);
    }

    @Test
    public void testReadOnlySetValue() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.readOnly.value", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.readOnly.value", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        TestRootBean bean = new TestRootBean();
        assertEquals(25, bean.getFirst().getReadOnly().getValue());

        invoker.setPropertyValue(bean, 98);
        assertEquals(98, bean.getFirst().getReadOnly().getValue());
    }

    @Test
    public void testInvalidName() throws Exception {
        PropertyInvoker invoker = new CompositePropertyInvoker(TestRootBean.class, "first.invalid.value", FACTORY);
        assertEquals(TestRootBean.class, invoker.getBeanClass());
        assertEquals("first.invalid.value", invoker.getPropertyName());
        assertEquals(void.class, invoker.getPropertyType());
        assertFalse(invoker.canRead());
        assertFalse(invoker.canWrite());

        TestRootBean bean = new TestRootBean();

        try {
            invoker.getPropertyValue(bean);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Cannot find property 'invalid' for bean: com.github.linfro.core.reflection.TestFirstInnerBean"
                    , e.getMessage());
        }

        try {
            invoker.setPropertyValue(bean, 76);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Cannot find property 'invalid' for bean: com.github.linfro.core.reflection.TestFirstInnerBean"
                    , e.getMessage());
        }
    }
}
