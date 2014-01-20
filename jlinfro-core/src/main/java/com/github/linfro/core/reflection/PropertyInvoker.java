package com.github.linfro.core.reflection;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-16
 * @since 1.0.0
 */
public class PropertyInvoker {
    protected final Class<?> beanClass;
    protected final String propertyName;
    protected final Class<?> propertyType;
    protected final Invoker getter;
    protected final Invoker setter;

    public PropertyInvoker(Class<?> beanClass, String propertyName, Class<?> propertyType, Invoker getter, Invoker setter) {
        this.beanClass = notNull(beanClass);
        this.propertyName = notNull(propertyName);
        this.propertyType = notNull(propertyType);
        this.getter = notNull(getter);
        this.setter = notNull(setter);
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public Object getPropertyValue(Object bean) {
        return getter.invoke(bean);
    }

    public void setPropertyValue(Object bean, Object value) {
        setter.invoke(bean, value);
    }

    public boolean canRead() {
        return !(getter instanceof InvalidInvoker);
    }

    public boolean canWrite() {
        return !(setter instanceof InvalidInvoker);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PropertyInvoker that = (PropertyInvoker) obj;
        return beanClass.equals(that.beanClass) && propertyName.equals(that.propertyName);
    }

    @Override
    public int hashCode() {
        int result = beanClass.hashCode();
        result = 31 * result + propertyName.hashCode();
        return result;
    }
}
