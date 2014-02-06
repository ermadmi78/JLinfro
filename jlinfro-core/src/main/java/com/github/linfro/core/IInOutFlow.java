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
public interface IInOutFlow<F> {
    public IInOutFlow<F> strong();

    public IInOutFlow<F> strong(Equality equality);

    public IInOutFlow<F> force();

    public Disposable to(HasValue<F> to);

    public Disposable to(GetAggregateValue<F> aggregateValue);

    public <T> IInOutFlow<T> map(Function<F, T> outFunc, Function<T, F> inFunc);

    public <T> IInOutFlow<T> mapNotNull(Function<F, T> outFunc, Function<T, F> inFunc);

    public IInOutFlow<F> sync();
}
