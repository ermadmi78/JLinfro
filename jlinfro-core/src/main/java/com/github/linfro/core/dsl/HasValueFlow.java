package com.github.linfro.core.dsl;

import com.github.linfro.core.common.Equality;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-23
 * @since 1.0.0
 */
public interface HasValueFlow<F> extends GetValueFlow<F> {
    @Override
    public HasValueFlow<F> strong();

    @Override
    public HasValueFlow<F> strong(Equality equality);

    @Override
    public HasValueFlow<F> force();

    public HasValueFlow<F> sync();
}
