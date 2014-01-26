package com.github.linfro.core.reflection;

import com.github.linfro.core.common.Disposable;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.github.linfro.core.reflection.ReflectionUtil.synchronizeProperty;
import static org.junit.Assert.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-26
 * @since 1.0.0
 */
public class PropertyChangeListenerTest {
    private static class Listener implements PropertyChangeListener {
        private int invokeCount = 0;

        public int getInvokeCount() {
            return invokeCount;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            invokeCount++;
        }
    }

    @Test
    public void testAddRemoveListener() throws Exception {
        final Listener listener = new Listener();

        MethodsBean bean = new MethodsBean();
        assertTrue(
                ReflectionUtil.addPropertyChangeListener(bean, "primInt", listener)
        );
        assertEquals(0, listener.getInvokeCount());

        bean.setPrimInt(5);
        assertEquals(1, listener.getInvokeCount());

        assertTrue(
                ReflectionUtil.removePropertyChangeListener(bean, "primInt", listener)
        );
        assertEquals(1, listener.getInvokeCount());

        bean.setPrimInt(17);
        assertEquals(1, listener.getInvokeCount());
    }

    @Test
    public void testNoListeners() throws Exception {
        final Listener listener = new Listener();

        TestSecondInnerBean bean = new TestSecondInnerBean();
        assertFalse(
                ReflectionUtil.addPropertyChangeListener(bean, "value", listener)
        );
        assertEquals(0, listener.getInvokeCount());

        bean.setValue(5);
        assertEquals(0, listener.getInvokeCount());

        assertFalse(
                ReflectionUtil.removePropertyChangeListener(bean, "value", listener)
        );
        assertEquals(0, listener.getInvokeCount());

        bean.setValue(17);
        assertEquals(0, listener.getInvokeCount());
    }

    @Test
    public void testPropertySynchronizerIgnoreNull() throws Exception {
        final MethodsBean firstBean = new MethodsBean();
        firstBean.setWrapInt(5);

        final MethodsBean secondBean = new MethodsBean();
        secondBean.setWrapInt(15);

        Disposable disposable = synchronizeProperty(firstBean, "wrapInt", secondBean, "wrapInt", true);
        assertEquals(new Integer(5), firstBean.getWrapInt());
        assertEquals(new Integer(15), secondBean.getWrapInt());

        firstBean.setWrapInt(7);
        assertEquals(new Integer(7), firstBean.getWrapInt());
        assertEquals(new Integer(7), secondBean.getWrapInt());

        secondBean.setWrapInt(111);
        assertEquals(new Integer(111), firstBean.getWrapInt());
        assertEquals(new Integer(111), secondBean.getWrapInt());

        firstBean.setWrapInt(null);
        assertNull(firstBean.getWrapInt());
        assertEquals(new Integer(111), secondBean.getWrapInt());

        firstBean.setWrapInt(22);
        assertEquals(new Integer(22), firstBean.getWrapInt());
        assertEquals(new Integer(22), secondBean.getWrapInt());

        secondBean.setWrapInt(null);
        assertEquals(new Integer(22), firstBean.getWrapInt());
        assertNull(secondBean.getWrapInt());

        disposable.dispose();

        secondBean.setWrapInt(57);
        assertEquals(new Integer(22), firstBean.getWrapInt());
        assertEquals(new Integer(57), secondBean.getWrapInt());

        firstBean.setWrapInt(178);
        assertEquals(new Integer(178), firstBean.getWrapInt());
        assertEquals(new Integer(57), secondBean.getWrapInt());
    }

    @Test
    public void testPropertySynchronizer() throws Exception {
        final MethodsBean firstBean = new MethodsBean();
        firstBean.setWrapInt(5);

        final MethodsBean secondBean = new MethodsBean();
        secondBean.setWrapInt(15);

        Disposable disposable = synchronizeProperty(firstBean, "wrapInt", secondBean, "wrapInt", false);
        assertEquals(new Integer(5), firstBean.getWrapInt());
        assertEquals(new Integer(15), secondBean.getWrapInt());

        firstBean.setWrapInt(7);
        assertEquals(new Integer(7), firstBean.getWrapInt());
        assertEquals(new Integer(7), secondBean.getWrapInt());

        secondBean.setWrapInt(111);
        assertEquals(new Integer(111), firstBean.getWrapInt());
        assertEquals(new Integer(111), secondBean.getWrapInt());

        firstBean.setWrapInt(null);
        assertNull(firstBean.getWrapInt());
        assertNull(secondBean.getWrapInt());

        firstBean.setWrapInt(22);
        assertEquals(new Integer(22), firstBean.getWrapInt());
        assertEquals(new Integer(22), secondBean.getWrapInt());

        secondBean.setWrapInt(null);
        assertNull(firstBean.getWrapInt());
        assertNull(secondBean.getWrapInt());

        firstBean.setWrapInt(22);
        assertEquals(new Integer(22), firstBean.getWrapInt());
        assertEquals(new Integer(22), secondBean.getWrapInt());

        disposable.dispose();

        secondBean.setWrapInt(57);
        assertEquals(new Integer(22), firstBean.getWrapInt());
        assertEquals(new Integer(57), secondBean.getWrapInt());

        firstBean.setWrapInt(178);
        assertEquals(new Integer(178), firstBean.getWrapInt());
        assertEquals(new Integer(57), secondBean.getWrapInt());
    }
}
