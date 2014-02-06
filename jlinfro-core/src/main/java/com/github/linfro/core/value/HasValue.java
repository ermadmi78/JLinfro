package com.github.linfro.core.value;

import com.github.linfro.core.Flow;
import com.github.linfro.core.IHybridFlow;
import com.github.linfro.core.common.NullSafeFunction;

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

    @Override
    public default IHybridFlow<T> flow() {
        return Flow.from(this);
    }

    public default <M> HasDisposableValue<M> map(Function<T, M> outFunc, Function<M, T> inFunc) {
        return new HasTransformedValue<>(this, outFunc, inFunc);
    }

    public default <M> HasDisposableValue<M> mapNotNull(Function<T, M> outFunc, Function<M, T> inFunc) {
        return new HasTransformedValue<>(this, new NullSafeFunction<T, M>(outFunc), new NullSafeFunction<M, T>(inFunc));
    }
}
