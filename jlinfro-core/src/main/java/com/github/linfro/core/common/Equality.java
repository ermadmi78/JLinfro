package com.github.linfro.core.common;

/**
 * @author Dmitry Ermakov
 * @version 2014-03-16
 * @since 1.0.0
 */
@FunctionalInterface
public interface Equality<T> {
    public boolean areEquals(T first, T second);
}
