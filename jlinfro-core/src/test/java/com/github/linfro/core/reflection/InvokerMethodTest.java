package com.github.linfro.core.reflection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-12
 * @since 1.0.0
 */
@RunWith(Theories.class)
public class InvokerMethodTest {
    private static final Map<Class<? extends InvokerFactory>, Integer> factoryCounter = new HashMap<>();

    private static synchronized void incFactoryCounter(Class<? extends InvokerFactory> clazz) {
        assertNotNull(clazz);

        Integer cnt = factoryCounter.get(clazz);
        if (cnt == null) {
            cnt = 1;
        } else {
            cnt = cnt + 1;
        }
        factoryCounter.put(clazz, cnt);
    }

    private static final Map<Class<? extends Invoker>, Integer> invokerCounter = new HashMap<>();

    private static synchronized void incInvokerCounter(Class<? extends Invoker> clazz) {
        assertNotNull(clazz);

        Integer cnt = invokerCounter.get(clazz);
        if (cnt == null) {
            cnt = 1;
        } else {
            cnt = cnt + 1;
        }
        invokerCounter.put(clazz, cnt);
    }

    @BeforeClass
    public static synchronized void clearStatistics() throws Exception {
        factoryCounter.clear();
        invokerCounter.clear();
    }

    @AfterClass
    public static synchronized void checkStatistics() throws Exception {
        System.out.println("#################");
        System.out.println("Method Factories:");
        print(factoryCounter);
        System.out.println("#################");
        System.out.println("Method Invokers:");
        print(invokerCounter);
        System.out.println("#################");

        int fieldsCount = MethodsBean.class.getDeclaredFields().length - 1; // Exclude "listeners" field

        assertEquals(new Integer(fieldsCount * 2), factoryCounter.get(ReflectiveInvokerFactory.class));
        assertEquals(new Integer(fieldsCount * 2), factoryCounter.get(DynamicInvokerFactory.class));

        assertEquals(new Integer(fieldsCount * 2), invokerCounter.get(ReflectiveMethodInvoker.class));
        assertEquals(new Integer(fieldsCount * 2), invokerCounter.get(DynamicMethodInvoker.class));

        clearStatistics();
    }

    private static void print(Map<?, ?> map) {
        assertNotNull(map);
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.println("" + entry.getKey() + " = " + entry.getValue());
        }
    }


    @DataPoints
    public static InvokerFactory[][] FACTORIES = new InvokerFactory[][]{
            {new ReflectiveInvokerFactory()},
            {new DynamicInvokerFactory()}
    };

    // *************************** Boolean *****************************************************************************
    @Theory
    public void testPrimBooleanGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("isPrimBoolean");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertFalse(bean.isPrimBoolean());

        Object res = invoker.invoke(bean);
        assertEquals(Boolean.FALSE, res);

        bean.setPrimBoolean(true);
        assertTrue(bean.isPrimBoolean());

        res = invoker.invoke(bean);
        assertEquals(Boolean.TRUE, res);
    }

    @Theory
    public void testPrimBooleanSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setPrimBoolean", Boolean.TYPE);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertFalse(bean.isPrimBoolean());

        invoker.invoke(bean, true);
        assertTrue(bean.isPrimBoolean());

        Boolean nullVal = null;
        invoker.invoke(bean, nullVal);
        assertFalse(bean.isPrimBoolean());
    }

    @Theory
    public void testWrapBooleanGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getWrapBoolean");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapBoolean());

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.setWrapBoolean(true);
        assertTrue(bean.getWrapBoolean());

        res = invoker.invoke(bean);
        assertEquals(Boolean.TRUE, res);
    }

    @Theory
    public void testWrapBooleanSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setWrapBoolean", Boolean.class);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapBoolean());

        invoker.invoke(bean, true);
        assertTrue(bean.getWrapBoolean());

        Boolean nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.getWrapBoolean());
    }

    // *************************** Character ***************************************************************************
    @Theory
    public void testPrimCharGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getPrimChar");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertTrue(bean.getPrimChar() == '\u0000');

        Object res = invoker.invoke(bean);
        assertEquals('\u0000', res);

        bean.setPrimChar('T');
        assertTrue(bean.getPrimChar() == 'T');

        res = invoker.invoke(bean);
        assertEquals('T', res);
    }

    @Theory
    public void testPrimCharSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setPrimChar", Character.TYPE);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertTrue(bean.getPrimChar() == '\u0000');

        invoker.invoke(bean, 'T');
        assertTrue(bean.getPrimChar() == 'T');

        Character nullVal = null;
        invoker.invoke(bean, nullVal);
        assertTrue(bean.getPrimChar() == '\u0000');
    }

    @Theory
    public void testWrapCharGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getWrapChar");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapChar());

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.setWrapChar('T');
        assertEquals(new Character('T'), bean.getWrapChar());

        res = invoker.invoke(bean);
        assertEquals('T', res);
    }

    @Theory
    public void testWrapCharSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setWrapChar", Character.class);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapChar());

        invoker.invoke(bean, 'T');
        assertEquals(new Character('T'), bean.getWrapChar());

        Character nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.getWrapChar());
    }

    // *************************** Byte ********************************************************************************
    @Theory
    public void testPrimByteGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getPrimByte");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertTrue(bean.getPrimByte() == (byte) 0);

        Object res = invoker.invoke(bean);
        assertEquals((byte) 0, res);

        bean.setPrimByte((byte) 3);
        assertTrue(bean.getPrimByte() == (byte) 3);

        res = invoker.invoke(bean);
        assertEquals((byte) 3, res);
    }

    @Theory
    public void testPrimByteSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setPrimByte", Byte.TYPE);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertTrue(bean.getPrimByte() == (byte) 0);

        invoker.invoke(bean, (byte) 3);
        assertTrue(bean.getPrimByte() == (byte) 3);

        Byte nullVal = null;
        invoker.invoke(bean, nullVal);
        assertTrue(bean.getPrimByte() == (byte) 0);
    }

    @Theory
    public void testWrapByteGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getWrapByte");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapByte());

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.setWrapByte((byte) 3);
        assertEquals(new Byte((byte) 3), bean.getWrapByte());

        res = invoker.invoke(bean);
        assertEquals((byte) 3, res);
    }

    @Theory
    public void testWrapByteSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setWrapByte", Byte.class);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapByte());

        invoker.invoke(bean, (byte) 3);
        assertEquals(new Byte((byte) 3), bean.getWrapByte());

        Byte nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.getWrapByte());
    }

    // *************************** Short ********************************************************************************
    @Theory
    public void testPrimShortGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getPrimShort");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertTrue(bean.getPrimShort() == (short) 0);

        Object res = invoker.invoke(bean);
        assertEquals((short) 0, res);

        bean.setPrimShort((short) 3);
        assertTrue(bean.getPrimShort() == (short) 3);

        res = invoker.invoke(bean);
        assertEquals((short) 3, res);
    }

    @Theory
    public void testPrimShortSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setPrimShort", Short.TYPE);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertTrue(bean.getPrimShort() == (short) 0);

        invoker.invoke(bean, (short) 3);
        assertTrue(bean.getPrimShort() == (short) 3);

        Short nullVal = null;
        invoker.invoke(bean, nullVal);
        assertTrue(bean.getPrimShort() == (short) 0);
    }

    @Theory
    public void testWrapShortGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getWrapShort");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapShort());

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.setWrapShort((short) 3);
        assertEquals(new Short((short) 3), bean.getWrapShort());

        res = invoker.invoke(bean);
        assertEquals((short) 3, res);
    }

    @Theory
    public void testWrapShortSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setWrapShort", Short.class);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapShort());

        invoker.invoke(bean, (short) 3);
        assertEquals(new Short((short) 3), bean.getWrapShort());

        Short nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.getWrapShort());
    }

    // *************************** Integer *****************************************************************************
    @Theory
    public void testPrimIntGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getPrimInt");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertTrue(bean.getPrimInt() == 0);

        Object res = invoker.invoke(bean);
        assertEquals(0, res);

        bean.setPrimInt(3);
        assertTrue(bean.getPrimInt() == 3);

        res = invoker.invoke(bean);
        assertEquals(3, res);
    }

    @Theory
    public void testPrimIntSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setPrimInt", Integer.TYPE);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertTrue(bean.getPrimInt() == 0);

        invoker.invoke(bean, 3);
        assertTrue(bean.getPrimInt() == 3);

        Integer nullVal = null;
        invoker.invoke(bean, nullVal);
        assertTrue(bean.getPrimInt() == 0);
    }

    @Theory
    public void testWrapIntGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getWrapInt");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapInt());

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.setWrapInt(3);
        assertEquals(new Integer(3), bean.getWrapInt());

        res = invoker.invoke(bean);
        assertEquals(3, res);
    }

    @Theory
    public void testWrapIntSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setWrapInt", Integer.class);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapInt());

        invoker.invoke(bean, 3);
        assertEquals(new Integer(3), bean.getWrapInt());

        Integer nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.getWrapInt());
    }

    // *************************** Long ********************************************************************************
    @Theory
    public void testPrimLongGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getPrimLong");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertTrue(bean.getPrimLong() == 0L);

        Object res = invoker.invoke(bean);
        assertEquals(0L, res);

        bean.setPrimLong(3L);
        assertTrue(bean.getPrimLong() == 3L);

        res = invoker.invoke(bean);
        assertEquals(3L, res);
    }

    @Theory
    public void testPrimLongSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setPrimLong", Long.TYPE);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertTrue(bean.getPrimLong() == 0L);

        invoker.invoke(bean, 3L);
        assertTrue(bean.getPrimLong() == 3L);

        Long nullVal = null;
        invoker.invoke(bean, nullVal);
        assertTrue(bean.getPrimLong() == 0L);
    }

    @Theory
    public void testWrapLongGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getWrapLong");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapLong());

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.setWrapLong(3L);
        assertEquals(new Long(3L), bean.getWrapLong());

        res = invoker.invoke(bean);
        assertEquals(3L, res);
    }

    @Theory
    public void testWrapLongSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setWrapLong", Long.class);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapLong());

        invoker.invoke(bean, 3L);
        assertEquals(new Long(3L), bean.getWrapLong());

        Long nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.getWrapLong());
    }

    // *************************** Float *******************************************************************************
    @Theory
    public void testPrimFloatGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getPrimFloat");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertEquals(0F, bean.getPrimFloat(), 0.01F);

        Object res = invoker.invoke(bean);
        assertEquals(0F, (Float) res, 0.01F);

        bean.setPrimFloat(3.5F);
        assertEquals(3.5F, bean.getPrimFloat(), 0.01F);

        res = invoker.invoke(bean);
        assertEquals(3.5F, (Float) res, 0.01F);
    }

    @Theory
    public void testPrimFloatSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setPrimFloat", Float.TYPE);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertEquals(0F, bean.getPrimFloat(), 0.01F);

        invoker.invoke(bean, 3.5F);
        assertEquals(3.5F, bean.getPrimFloat(), 0.01F);

        Float nullVal = null;
        invoker.invoke(bean, nullVal);
        assertEquals(0F, bean.getPrimFloat(), 0.01F);
    }

    @Theory
    public void testWrapFloatGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getWrapFloat");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapFloat());

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.setWrapFloat(3.5F);
        assertEquals(3.5F, bean.getWrapFloat(), 0.01F);

        res = invoker.invoke(bean);
        assertEquals(3.5F, (Float) res, 0.01F);
    }

    @Theory
    public void testWrapFloatSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setWrapFloat", Float.class);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapFloat());

        invoker.invoke(bean, 3.5F);
        assertEquals(3.5F, bean.getWrapFloat(), 0.01F);

        Float nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.getWrapFloat());
    }

    // *************************** Double ******************************************************************************
    @Theory
    public void testPrimDoubleGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getPrimDouble");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertEquals(0D, bean.getPrimDouble(), 0.01D);

        Object res = invoker.invoke(bean);
        assertEquals(0D, (Double) res, 0.01D);

        bean.setPrimDouble(3.5D);
        assertEquals(3.5D, bean.getPrimDouble(), 0.01D);

        res = invoker.invoke(bean);
        assertEquals(3.5D, (Double) res, 0.01D);
    }

    @Theory
    public void testPrimDoubleSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setPrimDouble", Double.TYPE);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertEquals(0D, bean.getPrimDouble(), 0.01D);

        invoker.invoke(bean, 3.5D);
        assertEquals(3.5D, bean.getPrimDouble(), 0.01D);

        Double nullVal = null;
        invoker.invoke(bean, nullVal);
        assertEquals(0D, bean.getPrimDouble(), 0.01D);
    }

    @Theory
    public void testWrapDoubleGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getWrapDouble");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapDouble());

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.setWrapDouble(3.5D);
        assertEquals(3.5D, bean.getWrapDouble(), 0.01D);

        res = invoker.invoke(bean);
        assertEquals(3.5D, (Double) res, 0.01D);
    }

    @Theory
    public void testWrapDoubleSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setWrapDouble", Double.class);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapDouble());

        invoker.invoke(bean, 3.5D);
        assertEquals(3.5D, bean.getWrapDouble(), 0.01D);

        Double nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.getWrapDouble());
    }

    // *************************** String ******************************************************************************
    @Theory
    public void testWrapStringGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getWrapString");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapString());

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.setWrapString("Test");
        assertEquals("Test", bean.getWrapString());

        res = invoker.invoke(bean);
        assertEquals("Test", res);
    }

    @Theory
    public void testWrapStringSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setWrapString", String.class);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapString());

        invoker.invoke(bean, "Test");
        assertEquals("Test", bean.getWrapString());
    }

    // *************************** List ********************************************************************************
    @Theory
    @SuppressWarnings("unchecked")
    public void testWrapListGetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("getWrapList");
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapList());

        Object res = invoker.invoke(bean);
        assertNull(res);

        List<String> list = new ArrayList<>();
        list.add("Test");

        bean.setWrapList(list);
        assertNotNull(bean.getWrapList());

        res = invoker.invoke(bean);
        assertNotNull(res);
        assertTrue(res instanceof List);

        List<String> resList = (List<String>) res;
        assertTrue(resList.size() == 1);
        assertEquals("Test", resList.get(0));
    }

    @Theory
    public void testWrapListSetter(InvokerFactory... factories) throws Exception {
        Method method = MethodsBean.class.getMethod("setWrapList", List.class);
        assertNotNull(method);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createMethodInvoker(method);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        MethodsBean bean = new MethodsBean();
        assertNull(bean.getWrapList());

        List<String> list = new ArrayList<>();
        list.add("Test");

        invoker.invoke(bean, list);

        List<String> resList = bean.getWrapList();
        assertTrue(resList.size() == 1);
        assertEquals("Test", resList.get(0));
    }
}
