package com.github.linfro.core.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import static com.github.linfro.core.common.ObjectUtil.notNull;
import static com.github.linfro.core.reflection.ReflectionUtil.getDefaultPrimitiveValue;
import static com.github.linfro.core.reflection.ReflectionUtil.isPrimitive;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-12
 * @since 1.0.0
 */
public class DynamicMethodInvoker implements Invoker {
    private final MethodHandle initialHandle;
    private final MethodHandle adoptedHandle;
    private final Class<?>[] types;

    public DynamicMethodInvoker(Method method) {
        notNull(method);
        this.types = notNull(method.getParameterTypes());

        try {
            initialHandle = notNull(MethodHandles.lookup().unreflect(method));
            adoptedHandle = notNull(
                    MethodHandles.spreadInvoker(initialHandle.type(), 1).asType(
                            MethodType.methodType(
                                    Object.class, MethodHandle.class, Object.class, Object[].class
                            )
                    )
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
            return adoptedHandle.invokeExact(initialHandle, bean, safeArgs);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
