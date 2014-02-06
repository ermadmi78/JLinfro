package com.github.linfro.core.value;

import com.github.linfro.core.Flow;
import com.github.linfro.core.IOutFlow;
import com.github.linfro.core.common.AutoDisposable;
import com.github.linfro.core.common.NullSafeFunction;

import java.util.function.Function;

/**
 * @author Dmitry Ermakov
 * @version 2014-01-04
 * @since 1.0.0
 */
public interface GetValue<T> extends Getter<T> {
    @Override
    public T getValue();

    public void addChangeListener(ValueChangeListener<? super T> listener);

    public void removeChangeListener(ValueChangeListener<? super T> listener);

    public default void autoDispose() {
        if (this instanceof AutoDisposable) {
            AutoDisposable disposable = (AutoDisposable) this;
            if (disposable.isAutoDispose()) {
                disposable.dispose();
            }
        }
    }

    public default IOutFlow<T> flow() {
        return Flow.from(this);
    }

    public default <M> GetDisposableValue<M> map(Function<T, M> function) {
        return new GetTransformedValue<>(this, function);
    }

    public default <M> GetDisposableValue<M> mapNotNull(Function<T, M> function) {
        return new GetTransformedValue<>(this, new NullSafeFunction<>(function));
    }
}
