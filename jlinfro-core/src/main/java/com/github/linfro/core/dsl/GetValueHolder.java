package com.github.linfro.core.dsl;

import com.github.linfro.core.GetValue;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-23
 * @since 1.0.0
 */
public interface GetValueHolder<T> {
    public GetValue<T> getContentValue();
}
