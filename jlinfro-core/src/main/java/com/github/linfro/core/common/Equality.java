package com.github.linfro.core.common;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
@FunctionalInterface
public interface Equality {
    public boolean areEquals(Object obj1, Object obj2);
}
