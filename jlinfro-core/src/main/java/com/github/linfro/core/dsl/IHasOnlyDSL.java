package com.github.linfro.core.dsl;

import com.github.linfro.core.common.Function;
import com.github.linfro.core.common.RevertFunction;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-05
 * @since 1.0.0
 */
public interface IHasOnlyDSL<F> extends ICoreDSL<IHasOnlyDSL<F>, F> {
    public IHasOnlyDSL<F> sync();

    public <T> IHasOnlyDSL<T> transform(RevertFunction<F, T> function);

    public <T> IHasOnlyDSL<T> transform(Function<F, T> function, Function<T, F> revertFunction);
}
