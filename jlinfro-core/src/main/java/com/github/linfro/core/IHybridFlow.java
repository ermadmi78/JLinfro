package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.Equality;
import com.github.linfro.core.value.GetAggregateValue;
import com.github.linfro.core.value.HasValue;

import java.util.function.Function;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public interface IHybridFlow<F> extends IOutFlow<F> {
    @Override
    public IHybridFlow<F> strong();

    @Override
    public IHybridFlow<F> strong(Equality equality);

    @Override
    public IHybridFlow<F> force();

    @Override
    public Disposable to(HasValue<F> to);

    @Override
    public Disposable to(GetAggregateValue<F> aggregateValue);

    @Override
    public <T> IOutFlow<T> map(Function<F, T> function);

    @Override
    public <T> IOutFlow<T> mapNotNull(Function<F, T> function);

    public <T> IHybridFlow<T> map(Function<F, T> inFunc, Function<T, F> outFunc);

    public <T> IHybridFlow<T> mapNotNull(Function<F, T> inFunc, Function<T, F> outFunc);

    public IInOutFlow<F> sync();
}
