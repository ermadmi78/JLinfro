package com.github.linfro.core.reflection;

import java.lang.reflect.Field;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-14
 * @since 1.0.0
 */
public class ReflectiveGetterInvoker implements Invoker {
    private final Field field;

    public ReflectiveGetterInvoker(Field field) {
        this.field = notNull(field);
        if (!this.field.isAccessible()) {
            this.field.setAccessible(true);
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
            return field.get(bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
