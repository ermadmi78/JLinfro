package com.github.linfro.core.reflection;

import com.github.linfro.core.common.Disposable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.github.linfro.core.common.ObjectUtil.notNull;
import static com.github.linfro.core.common.ObjectUtil.nvl;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-20
 * @since 1.0.0
 */
public class InvokerFactoryDecorator implements InvokerFactory, Disposable {
    private final InvokerFactory factory;
    private final Map<String, Integer> methodMap = new HashMap<>();
    private final Map<String, Integer> getterMap = new HashMap<>();
    private final Map<String, Integer> setterMap = new HashMap<>();

    public InvokerFactoryDecorator(InvokerFactory factory) {
        this.factory = notNull(factory);
    }

    private static void inc(Map<String, Integer> map, String name) {
        notNull(map);
        notNull(name);
        map.put(name, nvl(map.get(name), 0) + 1);
    }

    @Override
    public synchronized Invoker createMethodInvoker(Method method) {
        inc(methodMap, notNull(method).getName());
        return factory.createMethodInvoker(method);
    }

    @Override
    public synchronized Invoker createGetterInvoker(Field field) {
        inc(getterMap, notNull(field).getName());
        return factory.createGetterInvoker(field);
    }

    @Override
    public synchronized Invoker createSetterInvoker(Field field) {
        inc(setterMap, notNull(field).getName());
        return factory.createSetterInvoker(field);
    }

    @Override
    public synchronized void dispose() {
        methodMap.clear();
        getterMap.clear();
        setterMap.clear();
    }

    public synchronized int getMethodCount() {
        return methodMap.size();
    }

    public synchronized int getMethodCount(String name) {
        return nvl(methodMap.get(notNull(name)), 0);
    }

    public synchronized int getGetterCount() {
        return getterMap.size();
    }

    public synchronized int getGetterCount(String name) {
        return nvl(getterMap.get(notNull(name)), 0);
    }

    public synchronized int getSetterCount() {
        return setterMap.size();
    }

    public synchronized int getSetterCount(String name) {
        return nvl(setterMap.get(notNull(name)), 0);
    }
}
