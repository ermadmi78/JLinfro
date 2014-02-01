package com.github.linfro.core.common;

import java.util.function.Function;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class DefaultRevertFunction<F, T> implements RevertFunction<F, T> {
    private final Function<F, T> directFunc;
    private final Function<T, F> revertFunc;

    public DefaultRevertFunction(Function<F, T> directFunc, Function<T, F> revertFunc) {
        this.directFunc = notNull(directFunc);
        this.revertFunc = notNull(revertFunc);
    }

    @Override
    public T apply(F from) {
        return directFunc.apply(from);
    }

    @Override
    public F revert(T to) {
        return revertFunc.apply(to);
    }
}
