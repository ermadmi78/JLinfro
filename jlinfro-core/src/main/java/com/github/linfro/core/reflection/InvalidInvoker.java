package com.github.linfro.core.reflection;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-16
 * @since 1.0.0
 */
public class InvalidInvoker implements Invoker {
    private final String message;

    public InvalidInvoker(String message) {
        this.message = notNull(message);
    }

    @Override
    public Object invoke(Object bean, Object... args) {
        throw new IllegalStateException(message);
    }
}
