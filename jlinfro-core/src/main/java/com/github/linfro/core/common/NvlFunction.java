package com.github.linfro.core.common;

import java.util.function.Function;

import static com.github.linfro.core.common.ObjectUtil.nvl;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-09
 * @since 1.0.0
 */
public class NvlFunction<T> implements Function<T, T> {
    protected final T nullValue;

    public NvlFunction(T nullValue) {
        this.nullValue = nullValue;
    }

    @Override
    public T apply(T incoming) {
        return nvl(incoming, nullValue);
    }
}
