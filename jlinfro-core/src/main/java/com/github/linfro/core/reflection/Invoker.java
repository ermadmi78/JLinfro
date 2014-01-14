package com.github.linfro.core.reflection;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-12
 * @since 1.0.0
 */
public interface Invoker {
    public Object invoke(Object bean, Object... args);
}
