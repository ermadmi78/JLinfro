package com.github.linfro.core.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-14
 * @since 1.0.0
 */
public interface InvokerFactory {
    public Invoker createMethodInvoker(Method method);

    public Invoker createGetterInvoker(Field field);

    public Invoker createSetterInvoker(Field field);
}
