package com.github.linfro.core;

import com.github.linfro.core.dsl.GetterDSL;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public interface Getter<T> extends GetterDSL {
    public T getValue();
}
