package com.github.linfro.core.dsl;

import com.github.linfro.core.common.Function;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public interface IGetDSL<F> extends ICoreDSL<IGetDSL<F>, F> {
    public <T> IGetDSL<T> transform(Function<F, T> function);
}
