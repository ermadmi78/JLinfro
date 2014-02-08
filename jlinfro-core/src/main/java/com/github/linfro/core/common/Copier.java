package com.github.linfro.core.common;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
@FunctionalInterface
public interface Copier {
    public Object copy(Object obj);
}
