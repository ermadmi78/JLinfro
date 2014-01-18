package com.github.linfro.core.reflection;

import com.github.linfro.core.common.Disposable;

import java.util.HashMap;
import java.util.Map;

import static com.github.linfro.core.common.ObjectUtil.notNull;
import static com.github.linfro.core.reflection.ReflectionUtil.*;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-16
 * @since 1.0.0
 */
public class Property implements Disposable {
    private final String name;
    private final InvokerFactory factory;

    private final String[] nameChain;
    private final Map<Class<?>, PropertyInvoker>[] invokerChain;

    public Property(String name) {
        this(name, getInvokerFactory());
    }

    @SuppressWarnings("unchecked")
    public Property(String name, InvokerFactory factory) {
        this.name = notNull(name);
        this.factory = notNull(factory);
        this.nameChain = notNull(splitProperty(this.name));
        this.invokerChain = new Map[this.nameChain.length];
    }

    public String getName() {
        return name;
    }

    public Object getPropertyValue(Object bean) {
        notNull(bean);

        Object cur = bean;
        for (int i = 0; i < nameChain.length; i++) {
            if (cur == null) {
                return null;
            }

            String prop = nameChain[i];
            Map<Class<?>, PropertyInvoker> map = invokerChain[i];
            if (map == null) {
                map = new HashMap<>(3);
                invokerChain[i] = map;
            }

            Class<?> beanClass = notNull(cur.getClass());
            PropertyInvoker invoker = map.get(beanClass);
            if (invoker == null) {
                invoker = notNull(createPropertyInvoker(beanClass, prop, factory));
                map.put(beanClass, invoker);
            }

            cur = invoker.getPropertyValue(cur);
        }

        return cur;
    }

    public void setPropertyValue(Object bean, Object value) {
        notNull(bean);

        Object cur = bean;
        for (int i = 0; i < nameChain.length; i++) {
            if (cur == null) {
                return;
            }

            String prop = nameChain[i];
            Map<Class<?>, PropertyInvoker> map = invokerChain[i];
            if (map == null) {
                map = new HashMap<>(3);
                invokerChain[i] = map;
            }

            Class<?> beanClass = notNull(cur.getClass());
            PropertyInvoker invoker = map.get(beanClass);
            if (invoker == null) {
                invoker = notNull(createPropertyInvoker(beanClass, prop, factory));
                map.put(beanClass, invoker);
            }

            if (i == (nameChain.length - 1)) {
                invoker.setPropertyValue(cur, value);
                break;
            } else {
                cur = invoker.getPropertyValue(cur);
            }
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < invokerChain.length; i++) {
            invokerChain[i] = null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Property property = (Property) obj;
        return name.equals(property.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
