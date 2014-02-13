package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.Equality;
import com.github.linfro.core.value.GetAggregateValue;
import com.github.linfro.core.value.HasValue;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public interface IInOutFlow<F> {
    public IInOutFlow<F> sync();

    public IInOutFlow<F> strong();

    public IInOutFlow<F> strong(Equality equality);

    public IInOutFlow<F> force();

    public Disposable to(HasValue<F> to);

    public Disposable to(GetAggregateValue<F> aggregateValue);

    public <T> IInOutFlow<T> map(Function<F, T> outFunc, Function<T, F> inFunc);

    public <T> IInOutFlow<T> mapNotNull(Function<F, T> outFunc, Function<T, F> inFunc);

    public IInOutFlow<F> nvl(F outNullValue, F inNullValue);

    public IInOutFlow<F> filter(Predicate<? super F> outPredicate, Predicate<? super F> inPredicate);

    public IInOutFlow<F> biMap(Function<F, F> inOutFunction);

    public IInOutFlow<F> biMapNotNull(Function<F, F> inOutFunction);

    public IInOutFlow<F> biNvl(F inOutNullValue);

    public IInOutFlow<F> putMetaInfo(String metaInfoKey, Object metaInfoValue);

    public IInOutFlow<F> named(String name);
}
