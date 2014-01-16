package com.github.linfro.core.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

import static com.github.linfro.core.common.ObjectUtil.notNull;
import static com.github.linfro.core.reflection.ReflectionUtil.getDefaultPrimitiveValue;
import static com.github.linfro.core.reflection.ReflectionUtil.isPrimitive;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-16
 * @since 1.0.0
 */
public class DynamicSetterInvoker implements Invoker {
    private final MethodHandle setter;
    private final Class<?> type;

    public DynamicSetterInvoker(Field field) {
        notNull(field);
        this.type = notNull(field.getType());

        try {
            setter = notNull(
                    MethodHandles.lookup().unreflectSetter(field).asType(
                            MethodType.methodType(Object.class, Object.class, Object.class)
                    )
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object invoke(Object bean, Object... args) {
        notNull(bean);

        int argsCount = args == null ? 0 : args.length;
        if (argsCount != 1) {
            throw new IllegalArgumentException("Invalid arguments count: expected 1 but found " +
                    argsCount + " arguments");
        }

        Object arg = args[0];
        if ((arg == null) && isPrimitive(type)) {
            arg = getDefaultPrimitiveValue(type);
        }

        try {
            return setter.invokeExact(bean, arg);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
