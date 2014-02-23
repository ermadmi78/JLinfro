package com.github.linfro.core;

import com.github.linfro.core.common.Disposable;
import com.github.linfro.core.common.Equality;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-06
 * @since 1.0.0
 */
public interface IOutFlow<F> {
    public IOutFlow<F> strong();

    public IOutFlow<F> strong(Equality equality);

    public IOutFlow<F> force();

    public Disposable to(Consumer<? super F> consumer);

    public Disposable to(HasValue<F> to);

    public Disposable to(GetAggregateValue<F> aggregateValue);

    public <T> IOutFlow<T> map(Function<F, T> function);

    public <T> IOutFlow<T> mapNotNull(Function<F, T> function);

    public IOutFlow<F> nvl(F nullValue);

    public IOutFlow<F> filter(Predicate<? super F> predicate);

    public IOutFlow<F> putMetaInfo(String metaInfoKey, Object metaInfoValue);

    public IOutFlow<F> named(String name);

    public IOutFlow<List<F>> union(GetValue... args);
}
