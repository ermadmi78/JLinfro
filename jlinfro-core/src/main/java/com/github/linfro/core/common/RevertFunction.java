package com.github.linfro.core.common;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public interface RevertFunction<F, T> extends Function<F, T> {
    @Override
    public T apply(F from);

    public F revert(T to);
}
