package com.github.linfro.core;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-23
 * @since 1.0.0
 */
public interface HasValueHolder<T> extends GetValueDSL<T> {
    @Override
    public HasValue<T> getContentValue();
}
