package com.github.linfro.core.reflection;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-25
 * @since 1.0.0
 */
public interface PropertyInvoker {
    public Class<?> getBeanClass();

    public String getPropertyName();

    public Class<?> getPropertyType();

    public Object getPropertyValue(Object bean);

    public void setPropertyValue(Object bean, Object value);

    public boolean canRead();

    public boolean canWrite();
}
