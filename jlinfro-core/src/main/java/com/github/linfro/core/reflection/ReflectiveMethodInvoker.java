package com.github.linfro.core.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.linfro.core.common.ObjectUtil.notNull;
import static com.github.linfro.core.reflection.ReflectionUtil.getDefaultPrimitiveValue;
import static com.github.linfro.core.reflection.ReflectionUtil.isPrimitive;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-12
 * @since 1.0.0
 */
public class ReflectiveMethodInvoker implements Invoker {
    private final Method method;
    private final Class<?>[] types;

    public ReflectiveMethodInvoker(Method method) {
        this.method = notNull(method);
        if (!this.method.isAccessible()) {
            this.method.setAccessible(true);
        }
        this.types = notNull(method.getParameterTypes());
    }

    @Override
    public Object invoke(Object bean, Object... args) {
        notNull(bean);

        if (args == null) {
            args = new Object[0];
        }

        if (args.length != types.length) {
            throw new IllegalArgumentException("Invalid arguments count: expected " + types.length
                    + " but found " + args.length + " arguments");
        }

        Object[] safeArgs = args;
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Class<?> type = types[i];
            if ((arg == null) && isPrimitive(type)) {
                if (safeArgs == args) {
                    safeArgs = new Object[args.length];
                    System.arraycopy(args, 0, safeArgs, 0, args.length);
                }

                safeArgs[i] = getDefaultPrimitiveValue(type);
            }
        }

        try {
            return method.invoke(bean, safeArgs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
