package com.github.linfro.core.common;

import java.util.function.Function;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public class NullSafeFunction<T, R> implements Function<T, R> {
    protected final Function<T, R> function;

    public NullSafeFunction(Function<T, R> function) {
        this.function = notNull(function);
    }

    @Override
    public R apply(T input) {
        return input == null ? null : function.apply(input);
    }
}
