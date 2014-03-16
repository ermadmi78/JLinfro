package com.github.linfro.core.dsl;

import com.github.linfro.core.GetAggregateValue;
import com.github.linfro.core.common.Disposable;

import java.util.function.Consumer;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-23
 * @since 1.0.0
 */
public interface GetValueFlow<F> {
    public GetValueFlow<F> force();

    public Disposable to(HasValueHolder<F> to);

    public Disposable to(Consumer<? super F> consumer);

    public Disposable to(GetAggregateValue<F> aggregateValue);
}
