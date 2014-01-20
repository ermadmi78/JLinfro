package com.github.linfro.core.reflection;

import org.junit.Before;
import org.junit.Test;

import static com.github.linfro.core.reflection.ReflectionUtil.createPropertyInvoker;
import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-20
 * @since 1.0.0
 */
public class CreatePropertyInvokerTest {
    private final InvokerFactoryDecorator factory = new InvokerFactoryDecorator(new ReflectiveInvokerFactory());

    @Before
    public void clearFactory() {
        factory.dispose();
    }

    @Test
    public void testInvalidName() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(FieldsBean.class, "5invalid_name", factory);

        assertNotNull(invoker);
        assertFalse(invoker.canRead());
        assertFalse(invoker.canWrite());

        assertEquals(0, factory.getMethodCount());
        assertEquals(0, factory.getGetterCount());
        assertEquals(0, factory.getSetterCount());

        FieldsBean bean = new FieldsBean();

        try {
            invoker.getPropertyValue(bean);
            fail("Getter must be invalid");
        } catch (IllegalStateException e) {
            assertEquals("Invalid property name: 5invalid_name", e.getMessage());
        }

        try {
            invoker.setPropertyValue(bean, "val");
            fail("Setter must be invalid");
        } catch (IllegalStateException e) {
            assertEquals("Invalid property name: 5invalid_name", e.getMessage());
        }
    }

    @Test
    public void testNoSuchPropertyName() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(FieldsBean.class, "no_such_property", factory);

        assertNotNull(invoker);
        assertFalse(invoker.canRead());
        assertFalse(invoker.canWrite());

        assertEquals(0, factory.getMethodCount());
        assertEquals(0, factory.getGetterCount());
        assertEquals(0, factory.getSetterCount());

        FieldsBean bean = new FieldsBean();

        try {
            invoker.getPropertyValue(bean);
            fail("Getter must be invalid");
        } catch (IllegalStateException e) {
            assertEquals("Cannot find property 'no_such_property' for bean: com.github.linfro.core.reflection.FieldsBean"
                    , e.getMessage());
        }

        try {
            invoker.setPropertyValue(bean, "val");
            fail("Setter must be invalid");
        } catch (IllegalStateException e) {
            assertEquals("Cannot find property 'no_such_property' for bean: com.github.linfro.core.reflection.FieldsBean"
                    , e.getMessage());
        }
    }

    @Test
    public void testWrapFieldProperty() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(FieldsBean.class, "wrapString", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        assertEquals(0, factory.getMethodCount());
        assertEquals(1, factory.getGetterCount());
        assertEquals(1, factory.getSetterCount());

        assertEquals(1, factory.getGetterCount("wrapString"));
        assertEquals(1, factory.getSetterCount("wrapString"));

        FieldsBean bean = new FieldsBean();
        bean.wrapString = "test";
        assertEquals("test", invoker.getPropertyValue(bean));

        invoker.setPropertyValue(bean, "new value");
        assertEquals("new value", bean.wrapString);
    }

    @Test
    public void testPrimitiveFieldProperty() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(FieldsBean.class, "primInt", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        assertEquals(0, factory.getMethodCount());
        assertEquals(1, factory.getGetterCount());
        assertEquals(1, factory.getSetterCount());

        assertEquals(1, factory.getGetterCount("primInt"));
        assertEquals(1, factory.getSetterCount("primInt"));

        FieldsBean bean = new FieldsBean();
        bean.primInt = 2;
        assertEquals(2, invoker.getPropertyValue(bean));

        invoker.setPropertyValue(bean, 12);
        assertEquals(12, bean.primInt);
    }

    @Test
    public void testWrapMethodProperty() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(MethodsBean.class, "wrapString", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        assertEquals(2, factory.getMethodCount());
        assertEquals(0, factory.getGetterCount());
        assertEquals(0, factory.getSetterCount());

        assertEquals(1, factory.getMethodCount("getWrapString"));
        assertEquals(1, factory.getMethodCount("setWrapString"));

        MethodsBean bean = new MethodsBean();
        bean.setWrapString("test");
        assertEquals("test", invoker.getPropertyValue(bean));

        invoker.setPropertyValue(bean, "new value");
        assertEquals("new value", bean.getWrapString());
    }

    @Test
    public void testPrimitiveMethodProperty() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(MethodsBean.class, "primInt", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        assertEquals(2, factory.getMethodCount());
        assertEquals(0, factory.getGetterCount());
        assertEquals(0, factory.getSetterCount());

        assertEquals(1, factory.getMethodCount("getPrimInt"));
        assertEquals(1, factory.getMethodCount("setPrimInt"));

        MethodsBean bean = new MethodsBean();
        bean.setPrimInt(2);
        assertEquals(2, invoker.getPropertyValue(bean));

        invoker.setPropertyValue(bean, 12);
        assertEquals(12, bean.getPrimInt());
    }

    @Test
    public void testBoolMethodProperty() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(MethodsBean.class, "primBoolean", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        assertEquals(2, factory.getMethodCount());
        assertEquals(0, factory.getGetterCount());
        assertEquals(0, factory.getSetterCount());

        assertEquals(1, factory.getMethodCount("isPrimBoolean"));
        assertEquals(1, factory.getMethodCount("setPrimBoolean"));

        MethodsBean bean = new MethodsBean();
        assertFalse((Boolean) invoker.getPropertyValue(bean));

        invoker.setPropertyValue(bean, true);
        assertTrue(bean.isPrimBoolean());
    }

    @Test
    public void testReadOnlyProperty() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(TestBean.class, "readOnlyInt", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertFalse(invoker.canWrite());

        assertEquals(1, factory.getMethodCount());
        assertEquals(0, factory.getGetterCount());
        assertEquals(0, factory.getSetterCount());

        assertEquals(1, factory.getMethodCount("getReadOnlyInt"));

        TestBean bean = new TestBean();
        assertEquals(5, invoker.getPropertyValue(bean));

        try {
            invoker.setPropertyValue(bean, 12);
            fail("Writing read-only property");
        } catch (IllegalStateException e) {
            assertEquals("Property 'readOnlyInt' is read only for bean: com.github.linfro.core.reflection.TestBean"
                    , e.getMessage());
        }
    }

    @Test
    public void testPreferGetter() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(TestBean.class, "preferGetter", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        assertEquals(1, factory.getMethodCount());
        assertEquals(0, factory.getGetterCount());
        assertEquals(1, factory.getSetterCount());

        assertEquals(1, factory.getMethodCount("getPreferGetter"));
        assertEquals(1, factory.getSetterCount("preferGetter"));

        TestBean bean = new TestBean();
        assertFalse(bean.getterInvoked);

        assertEquals(7, invoker.getPropertyValue(bean));
        assertTrue(bean.getterInvoked);

        invoker.setPropertyValue(bean, 12);
        assertEquals(12, bean.preferGetter);
    }

    @Test
    public void testPreferSetter() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(TestBean.class, "preferSetter", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        assertEquals(1, factory.getMethodCount());
        assertEquals(1, factory.getGetterCount());
        assertEquals(0, factory.getSetterCount());

        assertEquals(1, factory.getMethodCount("setPreferSetter"));
        assertEquals(1, factory.getGetterCount("preferSetter"));

        TestBean bean = new TestBean();
        assertEquals(9, invoker.getPropertyValue(bean));

        assertFalse(bean.setterInvoked);
        invoker.setPropertyValue(bean, 12);
        assertEquals(12, bean.preferSetter);
        assertTrue(bean.setterInvoked);
    }

    @Test
    public void testPreferMethods() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(TestBean.class, "preferMethods", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        assertEquals(2, factory.getMethodCount());
        assertEquals(0, factory.getGetterCount());
        assertEquals(0, factory.getSetterCount());

        assertEquals(1, factory.getMethodCount("getPreferMethods"));
        assertEquals(1, factory.getMethodCount("setPreferMethods"));

        TestBean bean = new TestBean();

        assertFalse(bean.preferMethodsGetterInvoked);
        assertEquals("pm", invoker.getPropertyValue(bean));
        assertTrue(bean.preferMethodsGetterInvoked);

        assertFalse(bean.preferMethodsSetterInvoked);
        invoker.setPropertyValue(bean, "test");
        assertEquals("test", bean.preferMethods);
        assertTrue(bean.preferMethodsSetterInvoked);
    }

    @Test
    public void testInvalidGetter() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(TestBean.class, "invalidGetter", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        assertEquals(0, factory.getMethodCount());
        assertEquals(1, factory.getGetterCount());
        assertEquals(1, factory.getSetterCount());

        assertEquals(1, factory.getGetterCount("invalidGetter"));
        assertEquals(1, factory.getSetterCount("invalidGetter"));

        TestBean bean = new TestBean();
        assertFalse(bean.invalidGetterInvoked);

        assertEquals(15, invoker.getPropertyValue(bean));
        assertFalse(bean.invalidGetterInvoked);

        invoker.setPropertyValue(bean, 22);
        assertEquals(22, bean.invalidGetter);
    }

    @Test
    public void testInvalidSetter() throws Exception {
        PropertyInvoker invoker = createPropertyInvoker(TestBean.class, "invalidSetter", factory);

        assertNotNull(invoker);
        assertTrue(invoker.canRead());
        assertTrue(invoker.canWrite());

        assertEquals(0, factory.getMethodCount());
        assertEquals(1, factory.getGetterCount());
        assertEquals(1, factory.getSetterCount());

        assertEquals(1, factory.getGetterCount("invalidSetter"));
        assertEquals(1, factory.getSetterCount("invalidSetter"));

        TestBean bean = new TestBean();
        assertEquals(17, invoker.getPropertyValue(bean));

        assertFalse(bean.invalidSetterInvoked);
        invoker.setPropertyValue(bean, 32);
        assertEquals(32, bean.invalidSetter);
        assertFalse(bean.invalidSetterInvoked);
    }
}
