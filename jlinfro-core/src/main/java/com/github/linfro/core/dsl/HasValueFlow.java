package com.github.linfro.core.dsl;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-23
 * @since 1.0.0
 */
public interface HasValueFlow<F> extends GetValueFlow<F> {
    @Override
    public HasValueFlow<F> force();

    public HasValueFlow<F> sync();
}
