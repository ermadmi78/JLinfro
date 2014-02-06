package com.github.linfro.core.value;

import com.github.linfro.core.Flow;

import java.util.function.Function;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public interface HasValue<T> extends GetValue<T> {
    @Override
    public T getValue();

    public void setValue(T value);

    @Override
    public void addChangeListener(ValueChangeListener<? super T> listener);

    @Override
    public void removeChangeListener(ValueChangeListener<? super T> listener);

    public default Flow.HybridFlow<T> flow() {
        return Flow.from(this);
    }

    public default <M> HasDisposableValue<M> map(Function<T, M> outFunc, Function<M, T> inFunc) {
        return new TransformedHasValue<>(this, outFunc, inFunc);
    }
}
