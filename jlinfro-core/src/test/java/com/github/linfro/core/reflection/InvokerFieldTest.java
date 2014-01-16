package com.github.linfro.core.reflection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-14
 * @since 1.0.0
 */
@RunWith(Theories.class)
public class InvokerFieldTest {
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
        System.out.println("Field Factories:");
        print(factoryCounter);
        System.out.println("#################");
        System.out.println("Field Invokers:");
        print(invokerCounter);
        System.out.println("#################");

        int fieldsCount = FieldsBean.class.getDeclaredFields().length;

        assertEquals(new Integer(fieldsCount * 2), factoryCounter.get(ReflectiveInvokerFactory.class));
        assertEquals(new Integer(fieldsCount * 2), factoryCounter.get(DynamicInvokerFactory.class));

        assertEquals(new Integer(fieldsCount), invokerCounter.get(ReflectiveGetterInvoker.class));
        assertEquals(new Integer(fieldsCount), invokerCounter.get(ReflectiveSetterInvoker.class));
        assertEquals(new Integer(fieldsCount), invokerCounter.get(DynamicGetterInvoker.class));
        assertEquals(new Integer(fieldsCount), invokerCounter.get(DynamicSetterInvoker.class));

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
        Field field = FieldsBean.class.getField("primBoolean");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertFalse(bean.primBoolean);

        Object res = invoker.invoke(bean);
        assertEquals(Boolean.FALSE, res);

        bean.primBoolean = true;
        res = invoker.invoke(bean);
        assertEquals(Boolean.TRUE, res);
    }

    @Theory
    public void testPrimBooleanSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primBoolean");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertFalse(bean.primBoolean);

        invoker.invoke(bean, true);
        assertTrue(bean.primBoolean);

        Boolean nullVal = null;
        invoker.invoke(bean, nullVal);
        assertFalse(bean.primBoolean);
    }

    @Theory
    public void testWrapBooleanGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapBoolean");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapBoolean);

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.wrapBoolean = true;
        res = invoker.invoke(bean);
        assertEquals(Boolean.TRUE, res);
    }

    @Theory
    public void testWrapBooleanSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapBoolean");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapBoolean);

        invoker.invoke(bean, true);
        assertTrue(bean.wrapBoolean);

        Boolean nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.wrapBoolean);
    }

    // *************************** Character ***************************************************************************
    @Theory
    public void testPrimCharGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primChar");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertTrue(bean.primChar == '\u0000');

        Object res = invoker.invoke(bean);
        assertEquals('\u0000', res);

        bean.primChar = 'T';
        res = invoker.invoke(bean);
        assertEquals('T', res);
    }

    @Theory
    public void testPrimCharSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primChar");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertTrue(bean.primChar == '\u0000');

        invoker.invoke(bean, 'T');
        assertTrue(bean.primChar == 'T');

        Character nullVal = null;
        invoker.invoke(bean, nullVal);
        assertTrue(bean.primChar == '\u0000');
    }

    @Theory
    public void testWrapCharGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapChar");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapChar);

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.wrapChar = 'T';
        res = invoker.invoke(bean);
        assertEquals('T', res);
    }

    @Theory
    public void testWrapCharSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapChar");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapChar);

        invoker.invoke(bean, 'T');
        assertEquals(new Character('T'), bean.wrapChar);

        Character nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.wrapChar);
    }

    // *************************** Byte ********************************************************************************
    @Theory
    public void testPrimByteGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primByte");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertTrue(bean.primByte == (byte) 0);

        Object res = invoker.invoke(bean);
        assertEquals((byte) 0, res);

        bean.primByte = (byte) 3;
        res = invoker.invoke(bean);
        assertEquals((byte) 3, res);
    }

    @Theory
    public void testPrimByteSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primByte");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertTrue(bean.primByte == (byte) 0);

        invoker.invoke(bean, (byte) 3);
        assertTrue(bean.primByte == (byte) 3);

        Byte nullVal = null;
        invoker.invoke(bean, nullVal);
        assertTrue(bean.primByte == (byte) 0);
    }

    @Theory
    public void testWrapByteGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapByte");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapByte);

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.wrapByte = (byte) 3;
        res = invoker.invoke(bean);
        assertEquals((byte) 3, res);
    }

    @Theory
    public void testWrapByteSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapByte");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapByte);

        invoker.invoke(bean, (byte) 3);
        assertEquals(new Byte((byte) 3), bean.wrapByte);

        Byte nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.wrapByte);
    }

    // *************************** Short ********************************************************************************
    @Theory
    public void testPrimShortGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primShort");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertTrue(bean.primShort == (short) 0);

        Object res = invoker.invoke(bean);
        assertEquals((short) 0, res);

        bean.primShort = (short) 3;
        res = invoker.invoke(bean);
        assertEquals((short) 3, res);
    }

    @Theory
    public void testPrimShortSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primShort");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertTrue(bean.primShort == (short) 0);

        invoker.invoke(bean, (short) 3);
        assertTrue(bean.primShort == (short) 3);

        Short nullVal = null;
        invoker.invoke(bean, nullVal);
        assertTrue(bean.primShort == (short) 0);
    }

    @Theory
    public void testWrapShortGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapShort");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapShort);

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.wrapShort = (short) 3;
        res = invoker.invoke(bean);
        assertEquals((short) 3, res);
    }

    @Theory
    public void testWrapShortSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapShort");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapShort);

        invoker.invoke(bean, (short) 3);
        assertEquals(new Short((short) 3), bean.wrapShort);

        Short nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.wrapShort);
    }

    // *************************** Integer *****************************************************************************
    @Theory
    public void testPrimIntGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primInt");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertTrue(bean.primInt == 0);

        Object res = invoker.invoke(bean);
        assertEquals(0, res);

        bean.primInt = 3;
        res = invoker.invoke(bean);
        assertEquals(3, res);
    }

    @Theory
    public void testPrimIntSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primInt");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertTrue(bean.primInt == 0);

        invoker.invoke(bean, 3);
        assertTrue(bean.primInt == 3);

        Integer nullVal = null;
        invoker.invoke(bean, nullVal);
        assertTrue(bean.primInt == 0);
    }

    @Theory
    public void testWrapIntGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapInt");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapInt);

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.wrapInt = 3;
        res = invoker.invoke(bean);
        assertEquals(3, res);
    }

    @Theory
    public void testWrapIntSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapInt");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapInt);

        invoker.invoke(bean, 3);
        assertEquals(new Integer(3), bean.wrapInt);

        Integer nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.wrapInt);
    }

    // *************************** Long ********************************************************************************
    @Theory
    public void testPrimLongGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primLong");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertTrue(bean.primLong == 0L);

        Object res = invoker.invoke(bean);
        assertEquals(0L, res);

        bean.primLong = 3L;
        res = invoker.invoke(bean);
        assertEquals(3L, res);
    }

    @Theory
    public void testPrimLongSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primLong");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertTrue(bean.primLong == 0L);

        invoker.invoke(bean, 3L);
        assertTrue(bean.primLong == 3L);

        Long nullVal = null;
        invoker.invoke(bean, nullVal);
        assertTrue(bean.primLong == 0L);
    }

    @Theory
    public void testWrapLongGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapLong");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapLong);

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.wrapLong = 3L;
        res = invoker.invoke(bean);
        assertEquals(3L, res);
    }

    @Theory
    public void testWrapLongSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapLong");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapLong);

        invoker.invoke(bean, 3L);
        assertEquals(new Long(3L), bean.wrapLong);

        Long nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.wrapLong);
    }

    // *************************** Float *******************************************************************************
    @Theory
    public void testPrimFloatGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primFloat");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertEquals(0F, bean.primFloat, 0.01F);

        Object res = invoker.invoke(bean);
        assertEquals(0F, (Float) res, 0.01F);

        bean.primFloat = 3.5F;
        res = invoker.invoke(bean);
        assertEquals(3.5F, (Float) res, 0.01F);
    }

    @Theory
    public void testPrimFloatSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primFloat");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertEquals(0F, bean.primFloat, 0.01F);

        invoker.invoke(bean, 3.5F);
        assertEquals(3.5F, bean.primFloat, 0.01F);

        Float nullVal = null;
        invoker.invoke(bean, nullVal);
        assertEquals(0F, bean.primFloat, 0.01F);
    }

    @Theory
    public void testWrapFloatGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapFloat");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapFloat);

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.wrapFloat = 3.5F;
        res = invoker.invoke(bean);
        assertEquals(3.5F, (Float) res, 0.01F);
    }

    @Theory
    public void testWrapFloatSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapFloat");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapFloat);

        invoker.invoke(bean, 3.5F);
        assertEquals(3.5F, bean.wrapFloat, 0.01F);

        Float nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.wrapFloat);
    }

    // *************************** Double ******************************************************************************
    @Theory
    public void testPrimDoubleGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primDouble");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertEquals(0D, bean.primDouble, 0.01D);

        Object res = invoker.invoke(bean);
        assertEquals(0D, (Double) res, 0.01D);

        bean.primDouble = 3.5D;
        res = invoker.invoke(bean);
        assertEquals(3.5D, (Double) res, 0.01D);
    }

    @Theory
    public void testPrimDoubleSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("primDouble");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertEquals(0D, bean.primDouble, 0.01D);

        invoker.invoke(bean, 3.5D);
        assertEquals(3.5D, bean.primDouble, 0.01D);

        Double nullVal = null;
        invoker.invoke(bean, nullVal);
        assertEquals(0D, bean.primDouble, 0.01D);
    }

    @Theory
    public void testWrapDoubleGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapDouble");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapDouble);

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.wrapDouble = 3.5D;
        res = invoker.invoke(bean);
        assertEquals(3.5D, (Double) res, 0.01D);
    }

    @Theory
    public void testWrapDoubleSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapDouble");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapDouble);

        invoker.invoke(bean, 3.5D);
        assertEquals(3.5D, bean.wrapDouble, 0.01D);

        Double nullVal = null;
        invoker.invoke(bean, nullVal);
        assertNull(bean.wrapDouble);
    }

    // *************************** String ******************************************************************************
    @Theory
    public void testWrapStringGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapString");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapString);

        Object res = invoker.invoke(bean);
        assertNull(res);

        bean.wrapString = "Test";
        res = invoker.invoke(bean);
        assertEquals("Test", res);
    }

    @Theory
    public void testWrapStringSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapString");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapString);

        invoker.invoke(bean, "Test");
        assertEquals("Test", bean.wrapString);
    }

    // *************************** List ********************************************************************************
    @Theory
    @SuppressWarnings("unchecked")
    public void testWrapListGetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapList");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createGetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapList);

        Object res = invoker.invoke(bean);
        assertNull(res);

        List<String> list = new ArrayList<>();
        list.add("Test");

        bean.wrapList = list;
        res = invoker.invoke(bean);
        assertNotNull(res);
        assertTrue(res instanceof List);

        List<String> resList = (List<String>) res;
        assertTrue(resList.size() == 1);
        assertEquals("Test", resList.get(0));
    }

    @Theory
    public void testWrapListSetter(InvokerFactory... factories) throws Exception {
        Field field = FieldsBean.class.getField("wrapList");
        assertNotNull(field);

        InvokerFactory factory = factories[0];
        incFactoryCounter(factory.getClass());

        Invoker invoker = factory.createSetterInvoker(field);
        assertNotNull(invoker);
        incInvokerCounter(invoker.getClass());

        FieldsBean bean = new FieldsBean();
        assertNull(bean.wrapList);

        List<String> list = new ArrayList<>();
        list.add("Test");

        invoker.invoke(bean, list);

        List<String> resList = bean.wrapList;
        assertTrue(resList.size() == 1);
        assertEquals("Test", resList.get(0));
    }
}
