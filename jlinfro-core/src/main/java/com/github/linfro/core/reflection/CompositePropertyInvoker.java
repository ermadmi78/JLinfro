package com.github.linfro.core.reflection;

import static com.github.linfro.core.common.ObjectUtil.notNull;
import static com.github.linfro.core.reflection.ReflectionUtil.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-16
 * @since 1.0.0
 */
public class CompositePropertyInvoker implements PropertyInvoker {
    protected final Class<?> beanClass;
    protected final String propertyName;
    protected final Class<?> propertyType;

    protected final String[] nameChain;
    protected final SimplePropertyInvoker[] invokerChain;
    protected final boolean readAbility;
    protected final boolean writeAbility;

    public CompositePropertyInvoker(Class<?> beanClass, String propertyName) {
        this(beanClass, propertyName, splitProperty(propertyName), getInvokerFactory());
    }

    public CompositePropertyInvoker(Class<?> beanClass, String propertyName, InvokerFactory factory) {
        this(beanClass, propertyName, splitProperty(propertyName), factory);
    }

    public CompositePropertyInvoker(Class<?> beanClass, String propertyName, String[] nameChain) {
        this(beanClass, propertyName, nameChain, getInvokerFactory());
    }

    public CompositePropertyInvoker(Class<?> beanClass, String propertyName, String[] nameChain, InvokerFactory factory) {
        this.beanClass = notNull(beanClass);
        this.propertyName = notNull(propertyName);
        this.nameChain = notNull(nameChain);
        if (this.nameChain.length <= 0) {
            throw new IllegalArgumentException("Name chain must not be empty");
        }
        notNull(factory);

        this.invokerChain = new SimplePropertyInvoker[this.nameChain.length];
        Class<?> curBeanClass = beanClass;
        boolean read = true;
        for (int i = 0; i < this.nameChain.length; i++) {
            String name = notNull(this.nameChain[i]);
            SimplePropertyInvoker invoker = notNull(createSimplePropertyInvoker(curBeanClass, name, factory));
            this.invokerChain[i] = invoker;
            curBeanClass = notNull(invoker.getPropertyType());

            read &= invoker.canRead();
        }

        this.propertyType = notNull(curBeanClass);
        this.readAbility = read;
        this.writeAbility = read && this.invokerChain[this.invokerChain.length - 1].canWrite();
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Class<?> getPropertyType() {
        return propertyType;
    }

    @Override
    public Object getPropertyValue(Object bean) {
        Object curBean = bean;
        for (SimplePropertyInvoker propertyInvoker : invokerChain) {
            if (curBean == null) {
                return null;
            }

            curBean = propertyInvoker.getPropertyValue(curBean);
        }

        return curBean;
    }

    @Override
    public void setPropertyValue(Object bean, Object value) {
        Object curBean = bean;
        for (int i = 0; i < invokerChain.length; i++) {
            if (curBean == null) {
                return;
            }

            SimplePropertyInvoker propertyInvoker = invokerChain[i];
            if (i < (invokerChain.length - 1)) {
                curBean = propertyInvoker.getPropertyValue(curBean);
            } else {
                propertyInvoker.setPropertyValue(curBean, value);
            }
        }
    }

    @Override
    public boolean canRead() {
        return readAbility;
    }

    @Override
    public boolean canWrite() {
        return writeAbility;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CompositePropertyInvoker that = (CompositePropertyInvoker) obj;
        return beanClass.equals(that.beanClass) && propertyName.equals(that.propertyName);
    }

    @Override
    public int hashCode() {
        int result = beanClass.hashCode();
        result = 31 * result + propertyName.hashCode();
        return result;
    }
}
