package com.github.linfro.core.common;

import java.util.function.Function;

import static com.github.linfro.core.common.ObjectUtil.notNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public class DefaultRevertFunction<F, T> implements RevertFunction<F, T> {
    private final Function<F, T> outFunc;
    private final Function<T, F> inFunc;

    public DefaultRevertFunction(Function<F, T> outFunc, Function<T, F> inFunc) {
        this.outFunc = notNull(outFunc);
        this.inFunc = notNull(inFunc);
    }

    @Override
    public T apply(F from) {
        return outFunc.apply(from);
    }

    @Override
    public F revert(T to) {
        return inFunc.apply(to);
    }
}
