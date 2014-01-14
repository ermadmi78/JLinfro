package com.github.linfro.core.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-14
 * @since 1.0.0
 */
public class ReflectiveInvokerFactory implements InvokerFactory {
    @Override
    public Invoker createMethodInvoker(Method method) {
        return new ReflectiveMethodInvoker(method);
    }

    @Override
    public Invoker createGetterInvoker(Field field) {
        return new ReflectiveGetterInvoker(field);
    }

    @Override
    public Invoker createSetterInvoker(Field field) {
        return new ReflectiveSetterInvoker(field);
    }
}
