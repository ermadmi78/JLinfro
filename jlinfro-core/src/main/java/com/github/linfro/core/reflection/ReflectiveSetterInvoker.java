package com.github.linfro.core.reflection;

import java.lang.reflect.Field;

import static com.github.linfro.core.common.ObjectUtil.notNull;
import static com.github.linfro.core.reflection.ReflectionUtil.getDefaultPrimitiveValue;
import static com.github.linfro.core.reflection.ReflectionUtil.isPrimitive;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-14
 * @since 1.0.0
 */
public class ReflectiveSetterInvoker implements Invoker {
    private final Field field;
    private final Class<?> type;

    public ReflectiveSetterInvoker(Field field) {
        this.field = notNull(field);
        if (!this.field.isAccessible()) {
            this.field.setAccessible(true);
        }
        this.type = notNull(this.field.getType());
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
            field.set(bean, arg);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
