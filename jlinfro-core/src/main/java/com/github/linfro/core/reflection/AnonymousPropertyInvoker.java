package com.github.linfro.core.reflection;

import java.util.HashMap;
import java.util.Map;

import static com.github.linfro.core.common.ObjectUtil.notNull;
import static com.github.linfro.core.reflection.ReflectionUtil.getInvokerFactory;
import static com.github.linfro.core.reflection.ReflectionUtil.splitProperty;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-26
 * @since 1.0.0
 */
public class AnonymousPropertyInvoker {
    protected final String propertyName;
    protected final InvokerFactory factory;

    protected final String[] nameChain;
    protected final Map<Class<?>, PropertyInvoker> invokerMap = new HashMap<>(5);

    public AnonymousPropertyInvoker(String propertyName) {
        this(propertyName, getInvokerFactory());
    }

    public AnonymousPropertyInvoker(String propertyName, InvokerFactory factory) {
        this.propertyName = notNull(propertyName);
        this.factory = notNull(factory);

        nameChain = splitProperty(this.propertyName);
    }

    public String getPropertyName() {
        return propertyName;
    }

    protected PropertyInvoker getPropertyInvoker(Class<?> beanClass) {
        notNull(beanClass);
        PropertyInvoker result = invokerMap.get(beanClass);
        if (result == null) {
            result = new CompositePropertyInvoker(beanClass, propertyName, nameChain, factory);
            invokerMap.put(beanClass, result);
        }

        return result;
    }

    public Object getPropertyValue(Object bean) {
        if (bean == null) {
            return null;
        }

        PropertyInvoker invoker = notNull(getPropertyInvoker(bean.getClass()));
        return invoker.getPropertyValue(bean);
    }

    public void setPropertyValue(Object bean, Object value) {
        if (bean == null) {
            return;
        }

        PropertyInvoker invoker = notNull(getPropertyInvoker(bean.getClass()));
        invoker.setPropertyValue(bean, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        AnonymousPropertyInvoker that = (AnonymousPropertyInvoker) obj;
        return propertyName.equals(that.propertyName);
    }

    @Override
    public int hashCode() {
        return propertyName.hashCode();
    }
}
