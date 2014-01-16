package com.github.linfro.core.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-16
 * @since 1.0.0
 */
public class DynamicGetterInvoker implements Invoker {
    private final MethodHandle getter;

    public DynamicGetterInvoker(Field field) {
        notNull(field);
        try {
            getter = notNull(
                    MethodHandles.lookup().unreflectGetter(field).asType(
                            MethodType.methodType(Object.class, Object.class)
                    )
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object invoke(Object bean, Object... args) {
        notNull(bean);
        if ((args != null) && (args.length > 0)) {
            throw new IllegalArgumentException("Invalid arguments count: expected 0 but found " +
                    args.length + " arguments");
        }

        try {
            return getter.invokeExact(bean);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
