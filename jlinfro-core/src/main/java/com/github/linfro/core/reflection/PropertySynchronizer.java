package com.github.linfro.core.reflection;

import com.github.linfro.core.common.Disposable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.github.linfro.core.common.ObjectUtil.notNull;
import static com.github.linfro.core.reflection.ReflectionUtil.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-26
 * @since 1.0.0
 */
public class PropertySynchronizer implements Disposable {
    private final Object firstBean;
    private final String firstName;
    private final Object secondBean;
    private final String secondName;
    private final boolean ignoreNull;

    private final PropertyChangeListener firstListener;
    private final PropertyChangeListener secondListener;

    private boolean flag = false;
    private boolean disposed = false;

    public PropertySynchronizer(Object firstBean, String firstName,
                                Object secondBean, String secondName, boolean ignoreNull) {
        this(firstBean, firstName, secondBean, secondName, ignoreNull, getInvokerFactory());
    }

    public PropertySynchronizer(Object firstBean, String firstName, Object secondBean, String secondName,
                                boolean ignoreNull, InvokerFactory factory) {
        notNull(factory);
        this.firstBean = notNull(firstBean);
        this.firstName = notNull(firstName);
        this.secondBean = notNull(secondBean);
        this.secondName = notNull(secondName);
        this.ignoreNull = ignoreNull;

        PropertyInvoker firstProperty = new CompositePropertyInvoker(this.firstBean.getClass(), this.firstName, factory);
        PropertyInvoker secondProperty = new CompositePropertyInvoker(this.secondBean.getClass(), this.secondName, factory);

        this.firstListener = new BeanListener(this.firstBean, firstProperty, this.secondBean, secondProperty);
        this.secondListener = new BeanListener(this.secondBean, secondProperty, this.firstBean, firstProperty);

        addPropertyChangeListener(this.firstBean, this.firstName, this.firstListener);
        addPropertyChangeListener(this.secondBean, this.secondName, this.secondListener);
    }

    private class BeanListener implements PropertyChangeListener {
        private final Object sourceBean;
        private final PropertyInvoker sourceProperty;

        private final Object destinationBean;
        private final PropertyInvoker destinationProperty;

        private BeanListener(Object sourceBean, PropertyInvoker sourceProperty,
                             Object destinationBean, PropertyInvoker destinationProperty) {
            this.sourceBean = notNull(sourceBean);
            this.sourceProperty = notNull(sourceProperty);
            this.destinationBean = notNull(destinationBean);
            this.destinationProperty = notNull(destinationProperty);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (disposed) {
                return;
            }

            if (flag) {
                return;
            }

            flag = true;
            try {
                Object value = sourceProperty.getPropertyValue(sourceBean);
                if ((value != null) || !ignoreNull) {
                    destinationProperty.setPropertyValue(destinationBean, value);
                }
            } finally {
                flag = false;
            }
        }
    }

    @Override
    public void dispose() {
        if (!disposed) {
            disposed = true;
            removePropertyChangeListener(firstBean, firstName, firstListener);
            removePropertyChangeListener(secondBean, secondName, secondListener);
        }
    }
}
