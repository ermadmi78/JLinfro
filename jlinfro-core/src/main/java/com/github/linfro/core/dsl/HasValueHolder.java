package com.github.linfro.core.dsl;

import com.github.linfro.core.HasValue;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-23
 * @since 1.0.0
 */
public interface HasValueHolder<T> extends GetValueHolder<T> {
    @Override
    public HasValue<T> getContentValue();
}
