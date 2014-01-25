package com.github.linfro.core.reflection;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-20
 * @since 1.0.0
 */
@RunWith(Theories.class)
public class PropertyInvokerTest {
    private static interface TestFactory {
        PropertyInvoker createPropertyInvoker(Class<?> beanClass, String propertyName, InvokerFactory factory);
    }

    @DataPoints
    public static TestFactory[][] FACTORIES = new TestFactory[][]{
            {new TestFactory() {
                @Override
                public PropertyInvoker createPropertyInvoker(Class<?> beanClass, String propertyName, InvokerFactory factory) {
                    simpleCounter.incrementAndGet();
                    return ReflectionUtil.createSimplePropertyInvoker(beanClass, propertyName, factory);
                }
            }},
            {new TestFactory() {
                @Override
                public PropertyInvoker createPropertyInvoker(Class<?> beanClass, String propertyName, InvokerFactory factory) {
                    compositeCounter.incrementAndGet();
                    return new CompositePropertyInvoker(beanClass, propertyName, factory);
                }
            }}
    };

    private static final AtomicInteger simpleCounter = new AtomicInteger(0);
    private static final AtomicInteger compositeCounter = new AtomicInteger(0);

    @BeforeClass
    public static void clearStatistics() throws Exception {
        simpleCounter.set(0);
        compositeCounter.set(0);
    }

    @AfterClass
    public static void checkStatistics() throws Exception {
        final int simpleCount = simpleCounter.get();
        final int compositeCount = compositeCounter.get();
        System.out.println("## simpleCount = " + simpleCount + " compositeCount = " + compositeCount);

        assertEquals(12, simpleCount);
        assertEquals(12, compositeCount);
        clearStatistics();
    }

    private final InvokerFactoryDecorator factory = new InvokerFactoryDecorator(new ReflectiveInvokerFactory());

    @Before
    public void clearFactory() {
        factory.dispose();
    }

    @Test
    public void testSimplePropertyInvokerInvalidName() throws Exception {
        SimplePropertyInvoker invoker = ReflectionUtil.createSimplePropertyInvoker(FieldsBean.class, "5invalid_name", factory);

        assertNotNull(invoker);
        assertEquals(FieldsBean.class, invoker.getBeanClass());
        assertEquals("5invalid_name", invoker.getPropertyName());
        assertEquals(void.class, invoker.getPropertyType());
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

    @Test(expected = IllegalArgumentException.class)
    public void testComplexPropertyInvokerInvalidName() throws Exception {
        new CompositePropertyInvoker(FieldsBean.class, "5invalid_name", factory);
    }

    @Theory
    public void testNoSuchPropertyName(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(FieldsBean.class, "no_such_property", factory);

        assertNotNull(invoker);
        assertEquals(FieldsBean.class, invoker.getBeanClass());
        assertEquals("no_such_property", invoker.getPropertyName());
        assertEquals(void.class, invoker.getPropertyType());
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

    @Theory
    public void testWrapFieldProperty(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(FieldsBean.class, "wrapString", factory);

        assertNotNull(invoker);
        assertEquals(FieldsBean.class, invoker.getBeanClass());
        assertEquals("wrapString", invoker.getPropertyName());
        assertEquals(String.class, invoker.getPropertyType());
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

    @Theory
    public void testPrimitiveFieldProperty(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(FieldsBean.class, "primInt", factory);

        assertNotNull(invoker);
        assertEquals(FieldsBean.class, invoker.getBeanClass());
        assertEquals("primInt", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
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

    @Theory
    public void testWrapMethodProperty(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(MethodsBean.class, "wrapString", factory);

        assertNotNull(invoker);
        assertEquals(MethodsBean.class, invoker.getBeanClass());
        assertEquals("wrapString", invoker.getPropertyName());
        assertEquals(String.class, invoker.getPropertyType());
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

    @Theory
    public void testPrimitiveMethodProperty(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(MethodsBean.class, "primInt", factory);

        assertNotNull(invoker);
        assertEquals(MethodsBean.class, invoker.getBeanClass());
        assertEquals("primInt", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
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

    @Theory
    public void testBoolMethodProperty(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(MethodsBean.class, "primBoolean", factory);

        assertNotNull(invoker);
        assertEquals(MethodsBean.class, invoker.getBeanClass());
        assertEquals("primBoolean", invoker.getPropertyName());
        assertEquals(Boolean.TYPE, invoker.getPropertyType());
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

    @Theory
    public void testReadOnlyProperty(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(TestBean.class, "readOnlyInt", factory);

        assertNotNull(invoker);
        assertEquals(TestBean.class, invoker.getBeanClass());
        assertEquals("readOnlyInt", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
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

    @Theory
    public void testPreferGetter(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(TestBean.class, "preferGetter", factory);

        assertNotNull(invoker);
        assertEquals(TestBean.class, invoker.getBeanClass());
        assertEquals("preferGetter", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
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

    @Theory
    public void testPreferSetter(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(TestBean.class, "preferSetter", factory);

        assertNotNull(invoker);
        assertEquals(TestBean.class, invoker.getBeanClass());
        assertEquals("preferSetter", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
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

    @Theory
    public void testPreferMethods(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(TestBean.class, "preferMethods", factory);

        assertNotNull(invoker);
        assertEquals(TestBean.class, invoker.getBeanClass());
        assertEquals("preferMethods", invoker.getPropertyName());
        assertEquals(String.class, invoker.getPropertyType());
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

    @Theory
    public void testInvalidGetter(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(TestBean.class, "invalidGetter", factory);

        assertNotNull(invoker);
        assertEquals(TestBean.class, invoker.getBeanClass());
        assertEquals("invalidGetter", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
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

    @Theory
    public void testInvalidSetter(TestFactory... factories) throws Exception {
        PropertyInvoker invoker = factories[0].createPropertyInvoker(TestBean.class, "invalidSetter", factory);

        assertNotNull(invoker);
        assertEquals(TestBean.class, invoker.getBeanClass());
        assertEquals("invalidSetter", invoker.getPropertyName());
        assertEquals(Integer.TYPE, invoker.getPropertyType());
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
