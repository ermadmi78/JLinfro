package com.github.linfro.core.common;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public interface Function<F, T> {
    public T apply(F from);
}
